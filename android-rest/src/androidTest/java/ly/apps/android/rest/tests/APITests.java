/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * http://apps.ly
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.apps.android.rest.tests;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;
import ly.apps.android.rest.client.*;
import ly.apps.android.rest.utils.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class APITests extends InstrumentationTestCase {

    protected String TAG = "android-rest-test";

    private static OpenWeatherAPI api;

    public synchronized void setUp() {
        if (api == null) {
            Context context = getInstrumentation().getTargetContext().getApplicationContext();
            RestClient client = RestClientFactory.defaultClient(context);
            api = RestServiceFactory.getService("http://api.openweathermap.org/data/2.5", OpenWeatherAPI.class, client);
        }
    }

    public interface CallbackRunnable<T> {
        void run(CountDownLatch signal, Response<T>[] expectedResponse);
    }

    private <T> Response<T> expectSuccess(final CallbackRunnable<T> runnable) throws Throwable {
        final Response<T>[] expectedResponse = new Response[]{null};
        final CountDownLatch signal = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                runnable.run(signal, expectedResponse);
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertNotNull(expectedResponse[0]);
        Logger.d(String.format("STATUS: %d", expectedResponse[0].getStatusCode()));
        Logger.d(String.format("RESPONSE BODY:%s", expectedResponse[0].getRawData()));
        assertNull(expectedResponse[0].getError());
        assertNotNull(expectedResponse[0].getResult());
        return expectedResponse[0];
    }

    private class TestCallback<T> extends Callback<T> {

        private CountDownLatch signal;

        private Response<T>[] expectedResponse;

        private TestCallback(Class<T> targetClass, CountDownLatch signal, Response<T>[] expectedResponse) {
            super(targetClass);
            this.signal = signal;
            this.expectedResponse = expectedResponse;
        }

        protected TestCallback(CountDownLatch signal, Response<T>[] expectedResponse) {
            super(getInstrumentation().getTargetContext().getApplicationContext());
            this.signal = signal;
            this.expectedResponse = expectedResponse;
        }

        @Override
        public void onResponse(Response<T> response) {
            Log.d(TAG, "onResponse");
            expectedResponse[0] = response;
            signal.countDown();
        }

    }

//    public void testWeatherForecast() throws Throwable    {
//        Response<WeatherResponse> response = expectSuccess(new CallbackRunnable<WeatherResponse>() {
//            @Override
//            public void run(final CountDownLatch signal, final Response<WeatherResponse>[] expectedResponse) {
//                api.fetchForecastTypeVar("Seattle", new TestCallback<WeatherResponse>(WeatherResponse.class, signal, expectedResponse));
//            }
//        });
//    }


}