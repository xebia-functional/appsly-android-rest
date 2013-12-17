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

package ly.apps.android.rest.cache;

import java.io.IOException;

/**
 * Manages the request cache whenever enabled for a given request
 */
public interface CacheManager {

    /**
     * Adds an object to the cache given a cache key
     * @param key the key
     * @param object the value
     */
    <T> void put(String key, T object, CacheInfo cacheInfo) throws IOException;

    /**
     * Retrieves an object from the cache given a cache key
     * @param key the key
     * @return the value if found
     */
    <T> T get(String key, CacheInfo cacheInfo) throws IOException, ClassNotFoundException;

    /**
     * Removes all entries from the cache
     */
    void invalidateAll() throws IOException;

}
