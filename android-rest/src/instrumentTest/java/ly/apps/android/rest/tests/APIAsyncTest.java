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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;

public abstract class APIAsyncTest implements Runnable {

    private int timeout;

    private TimeUnit unit;

    protected APIAsyncTest() {
        this(100, TimeUnit.SECONDS);
    }

    protected APIAsyncTest(int timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    private CountDownLatch signal = new CountDownLatch(1);

    public void complete() {
        Log.d(getClass().getName(), "complete()");
        signal.countDown();
    }

    public abstract void execute() throws Throwable;

    public void run() {
        Log.d(getClass().getName(), "run()");
        try {
            execute();
            signal.await(timeout, unit);
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }


}
