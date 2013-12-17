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


/**
 * Enum representing the different policies available that determine where cached results will be served
 */
public enum CachePolicy {

    /**
     * Never use the cache
     */
    NEVER,

    /**
     * Load from the cache when we are offline
     */
    LOAD_IF_OFFLINE,

    /**
     * Load from the cache if we encounter an error
     */
    LOAD_ON_ERROR,

    /**
     * Load from the cache if we have data stored
     */
    ENABLED,

    /**
     * Load from the cache if the request times out
     */
    LOAD_IF_TIMEOUT,

    /**
     * Load from the cache then refresh the cache with a network call (calls subscribers twice if the network reload
     * brings results different from those store in the cache based on the equals and hashcode impl)
     */
    NETWORK_ENABLED
}