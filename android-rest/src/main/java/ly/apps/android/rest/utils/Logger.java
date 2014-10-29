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

package ly.apps.android.rest.utils;

import android.util.Log;
import ly.apps.android.rest.BuildConfig;

public class Logger {

    public static final String TAG = "android-rest";

    public static void e(String msg, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg), t);
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
        }
    }

}
