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

package ly.apps.android.rest.client.example.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.CheckBox;
import ly.apps.android.rest.client.example.R;
import ly.apps.android.rest.client.example.utils.PreferencesManager;


public class AboutDialog extends DialogFragment {


    /**
     * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final CheckBox checkBox = new CheckBox(getActivity());
        checkBox.setText(R.string.dontShow);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            checkBox.setTextColor(Color.WHITE);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about)
                .setMessage(R.string.aboutMessage)
                .setView(checkBox)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesManager.getInstance(getActivity()).setShowAbout(!checkBox.isChecked());
                    }
                })
                .setNegativeButton(R.string.visit47, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = getString(R.string.fourty_seven_website);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
                .setNeutralButton(R.string.goToGitHub, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = getString(R.string.android_rest_github_url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
                .create();

    }
}
