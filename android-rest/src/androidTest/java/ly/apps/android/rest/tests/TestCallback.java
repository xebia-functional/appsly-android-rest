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

import android.util.Log;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class TestCallback<T> extends Callback<T> {

    private APIAsyncTest test;

    private Response<T>[] expected;

    private int timeout;

    private TimeUnit unit;

    private CountDownLatch signal = new CountDownLatch(1);

    @Override
    public void onResponse(Response<T> response) {
        Log.d(getClass().getName(), "onResponse() : " + response);
        expected[0] = response;
    }

    @Override
    public void onFinish() {
        Log.d(getClass().getName(), "onFinish()");
        super.onFinish();
        test.complete();
    }
}
