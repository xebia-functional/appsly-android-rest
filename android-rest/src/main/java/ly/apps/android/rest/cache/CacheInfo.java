/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
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

import java.util.Date;

/**
 * Holds information for the current request cache
 */
public class CacheInfo {

    /**
     * Represents a non cached request
     */
    public static final CacheInfo NONE = new CacheInfo(false, null);

    /**
     * true if the current results have been loaded from the cache
     */
    private boolean loadedFromCache;

    /**
     * the last time the results for this request were last updated
     */
    private Date lastRefreshed;

    /**
     * Construct a cache info object based on the values for loadedFromCache and lastRefreshed
     *
     * @param loadedFromCache true if the current results have been loaded from the cache
     * @param lastRefreshed   the last time the results for this request were last updated
     */
    public CacheInfo(boolean loadedFromCache, Date lastRefreshed) {
        this.loadedFromCache = loadedFromCache;
        this.lastRefreshed = lastRefreshed;
    }

    /**
     * If the results for this request were loaded from the cache or not
     *
     * @return true if the current results have been loaded from the cache
     */
    public boolean isLoadedFromCache() {
        return loadedFromCache;
    }

    /**
     * The last time the results for this request were updated
     *
     * @return the last time the results for this request were last updated
     */
    public Date getLastRefreshed() {
        return lastRefreshed;
    }
}
