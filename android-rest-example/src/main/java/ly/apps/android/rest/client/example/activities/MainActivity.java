/*
 * Copyright (C) 2014 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ly.apps.android.rest.client.example.activities;

import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.*;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import ly.apps.android.rest.client.*;
import ly.apps.android.rest.client.example.R;
import ly.apps.android.rest.client.example.api.OpenWeatherAPI;
import ly.apps.android.rest.client.example.dialogs.AboutDialog;
import ly.apps.android.rest.client.example.responses.ForecastResponse;
import ly.apps.android.rest.client.example.responses.MainResponse;
import ly.apps.android.rest.client.example.responses.WeatherResponse;
import ly.apps.android.rest.client.example.utils.PreferencesManager;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class MainActivity extends ActionBarActivity {

    private OpenWeatherAPI api;
    private Map<String, Integer> icons = new HashMap<String, Integer>() {{
        put("01d", R.drawable.sunny);
        put("01n", R.drawable.moon);
        put("02d", R.drawable.few_clouds_day);
        put("02n", R.drawable.few_clouds_night);
        put("03d", R.drawable.cloud);
        put("03n", R.drawable.cloud);
        put("04d", R.drawable.broken_clouds);
        put("04n", R.drawable.broken_clouds);
        put("09d", R.drawable.shower_rain);
        put("09n", R.drawable.shower_rain);
        put("10d", R.drawable.rain);
        put("10n", R.drawable.rain);
        put("11d", R.drawable.thunderstorm);
        put("11n", R.drawable.thunderstorm);
        put("13d", R.drawable.snow);
        put("13n", R.drawable.snow);
        put("50d", R.drawable.mist);
        put("50n", R.drawable.mist);
    }};
    private TextView textViewDescription, textViewTemp, textViewWind, textViewHumidity, textViewTempMax, textViewTempMin, textViewCity;
    private ImageView imageViewIcon;
    private LinearLayout contentProgressBar, linearLayoutContent, contentBottom, descriptionTempContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDescription = (TextView) findViewById(R.id.textview_description);
        textViewTemp = (TextView) findViewById(R.id.textview_temp);
        imageViewIcon = (ImageView) findViewById(R.id.imageview_icon);
        textViewWind = (TextView) findViewById(R.id.textview_wind_response);
        textViewHumidity = (TextView) findViewById(R.id.textview_humidity_response);
        textViewTempMax = (TextView) findViewById(R.id.textview_tempmax_response);
        textViewTempMin = (TextView) findViewById(R.id.textview_tempmin_response);
        textViewCity = (TextView) findViewById(R.id.textview_city);
        contentProgressBar = (LinearLayout) findViewById(R.id.content_progressbar);
        linearLayoutContent = (LinearLayout) findViewById(R.id.content);
        contentBottom = (LinearLayout) findViewById(R.id.bottom_content);
        descriptionTempContent = (LinearLayout) findViewById(R.id.description_temp_content);

        RestClient client = RestClientFactory.defaultClient(getApplicationContext());
        api = RestServiceFactory.getService(getString(R.string.base_url), OpenWeatherAPI.class, client);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        checkImmersiveMode();

        if ((location != null) && checkConnection(getApplicationContext())) {
            setLocation(location);
        } else {
            failMessage();
        }

    }

    private void failMessage() {
        Toast.makeText(getApplication(), R.string.errorConnecting, Toast.LENGTH_LONG).show();
    }

    public void setLocation(Location location) {
        api.getForecast(location.getLatitude(), location.getLongitude(), new Callback<ForecastResponse>() {

            @Override
            public void onResponse(Response<ForecastResponse> response) {
                // This will be invoke in the UI thread after serialization with your objects ready to use
                if (response.getStatusCode() == HttpStatus.SC_OK && response.getResult() != null) {
                    setInfo(response);
                    showDialog();
                }
            }

        });

    }

    private void setInfo(Response<ForecastResponse> response) {

        WeatherResponse weatherResponse;
        MainResponse mainResponse;

        if (!response.getResult().getWeather().isEmpty()) {
            weatherResponse = response.getResult().getWeather().get(0);
            mainResponse = response.getResult().getMain();

            textViewDescription.setText(capitalize(weatherResponse.getDescription()));
            imageViewIcon.setImageResource(icons.get(weatherResponse.getIcon()));
            textViewTemp.setText(String.format("%dº", kelvinToFahrenheitRounded(mainResponse.getTemp())));
            textViewWind.setText(String.format("%s %s", Float.toString(response.getResult().getWind().getSpeed()), getString(R.string.wind_speed)));
            textViewHumidity.setText(String.format("%d%%", mainResponse.getHumidity()));
            textViewTempMax.setText(String.format("%dº", kelvinToFahrenheitRounded(mainResponse.getTemp_max())));
            textViewTempMin.setText(String.format("%dº", kelvinToFahrenheitRounded(mainResponse.getTemp_min())));
            textViewCity.setText(response.getResult().getName());

            contentProgressBar.setVisibility(View.GONE);
            linearLayoutContent.setVisibility(View.VISIBLE);

            animateContent();
        } else {
            failMessage();
        }

    }

    private void animateContent() {

        ViewHelper.setRotation(imageViewIcon, 20);
        ViewHelper.setTranslationY(imageViewIcon, -100);
        ViewHelper.setTranslationX(textViewCity, 100);
        ViewHelper.setAlpha(linearLayoutContent, 0);
        ViewHelper.setTranslationY(descriptionTempContent, 200);
        ViewHelper.setTranslationY(contentBottom, 200);

        animate(imageViewIcon)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .translationY(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        iconFall(imageViewIcon, 10);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();

        animate(textViewCity)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationX(0)
                .start();

        animate(linearLayoutContent)
                .setDuration(1000)
                .alpha(1)
                .start();

        animate(contentBottom)
                .setDuration(1000)
                .translationY(0)
                .start();

        animate(descriptionTempContent)
                .setDuration(1000)
                .translationY(0)
                .start();
    }


    /**
     * animates a view like a swing
     *
     * @param view   the view that will be animated
     * @param bounce the bounces of the animation
     */
    private void iconFall(final View view, final int bounce) {

        int newdegrees = bounce;

        if (bounce % 2 == 0) {
            newdegrees *= -1;
        }

        animate(view)
                .setDuration(bounce * 20)
                .rotation(newdegrees)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (bounce > 0) {
                            iconFall(view, bounce - 1);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();

    }


    private static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    /**
     * Returns a given temperature in kelvins converted into fahrenheit
     *
     * @param kelvins the temperature in kelvins
     * @return the temperature converted to fahrenheit
     */

    private static int kelvinToFahrenheitRounded(double kelvins) {
        return (int) Math.round(((kelvins - 273) * (9 / 5)) + 32);
    }

    private void showDialog() {

        if (PreferencesManager.getInstance(MainActivity.this).getShowAbout()) {
            AboutDialog logOutDialog = new AboutDialog();
            logOutDialog.show(getSupportFragmentManager(), "dialog");
        }

    }

    private void checkImmersiveMode() {
        FrameLayout.LayoutParams params;
        TypedValue typedValue;
        int actionBarHeight;
        Resources.Theme theme;

        typedValue = new TypedValue();
        theme = getTheme();
        params = (FrameLayout.LayoutParams) linearLayoutContent.getLayoutParams();

        if (theme != null && params != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
                params.setMargins(0, actionBarHeight + getStatusBarHeight(), 0, getNavigationBarHeight());

            }
        }

    }

    private int getStatusBarHeight() {
        int result = 0;
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight() {
        int result = 0;
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
