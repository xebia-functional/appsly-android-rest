/*
 * Copyright (C) 2014 47 Degrees, LLC
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

package ly.apps.androdi.rest.tests;

import android.content.Context;
import android.test.InstrumentationTestCase;
import ly.apps.android.rest.utils.ObjectCache;

import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class CacheTests extends InstrumentationTestCase {

    protected String TAG = "android-rest-test";

    private ObjectCache objectCache;

    public synchronized void setUp() {
        Context context = getInstrumentation().getTargetContext();
        objectCache = new ObjectCache(context.getApplicationContext(), UUID.randomUUID().toString(), 10000);
    }

    public void testPut() throws IOException, ClassNotFoundException {
        objectCache.put("a", "b");
        assertEquals("b", objectCache.getObject("a"));
    }

    public void testGet() throws IOException, ClassNotFoundException {
        objectCache.put("a", "b");
        assertEquals("b", objectCache.getObject("a"));
    }
}