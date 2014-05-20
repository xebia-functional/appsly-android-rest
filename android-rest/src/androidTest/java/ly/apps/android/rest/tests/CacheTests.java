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

package ly.apps.android.rest.tests;

import android.test.InstrumentationTestCase;
import ly.apps.android.rest.cache.CacheInfo;
import ly.apps.android.rest.cache.CacheManager;
import ly.apps.android.rest.cache.CachePolicy;
import ly.apps.android.rest.cache.ContextPersistentCacheManager;

import java.io.IOException;

public class CacheTests extends InstrumentationTestCase {

    private static final int ONE_WEEK = 60 * 60 * 1000 * 24 * 7;

    private CacheInfo putCacheInfo(String key) {
        return new CacheInfo(key, CachePolicy.ENABLED, -1);
    }

    private CacheInfo getCacheInfo(String key) {
        return new CacheInfo(key, CachePolicy.ENABLED, ONE_WEEK);
    }

    private CacheManager getObjectCache() {
        return new ContextPersistentCacheManager(getInstrumentation().getTargetContext().getApplicationContext());
    }

    public void testPutGet() throws IOException, ClassNotFoundException {
        CacheManager objectCache = getObjectCache();
//        if (objectCache.isCacheAvailable()) {
        objectCache.put("a", "b", putCacheInfo("a"));
        assertEquals("b", objectCache.get("a", getCacheInfo("a")));
//        }
    }

    public void testMultipleObjects() throws IOException, ClassNotFoundException {
        CacheManager objectCache = getObjectCache();
        for (int i = 0; i < 100; i++) {
            String number = String.valueOf(i);
            objectCache.put(number, number, putCacheInfo(number));
        }
        for (int i = 0; i < 100; i++) {
            String number = String.valueOf(i);
            String value = objectCache.get(number, getCacheInfo(number));
            assertEquals(value, number);
        }
    }

    public void testMultipleObjectsDifferentManagers() throws IOException, ClassNotFoundException {
        CacheManager objectCache = new ContextPersistentCacheManager(getInstrumentation().getTargetContext().getApplicationContext());
        CacheManager otherCache = new ContextPersistentCacheManager(getInstrumentation().getTargetContext().getApplicationContext());
        for (int i = 0; i < 100; i++) {
            String number = String.valueOf(i);
            objectCache.put(number, number, putCacheInfo(number));
        }
        for (int i = 0; i < 100; i++) {
            String number = String.valueOf(i);
            String value = otherCache.get(number, getCacheInfo(number));
            assertEquals(value, number);
        }
    }
}