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

import java.util.Date;

/**
 * Holds information for the current request cache
 */
public class CacheInfo {

    /**
     * Represents a non cached request
     */
    public static final CacheInfo NONE = new CacheInfo(CachePolicy.NEVER, 0);

    /**
     * The request cache key
     */
    private String key;

    /**
     * The request cache policy
     */
    private CachePolicy policy;

    /**
     * The request time to live value
     */
    private long timeToLive;

    /**
     * true if the current results have been loaded from the cache
     */
    private boolean loadedFromCache;

    public CacheInfo(String key, CachePolicy policy, long timeToLive) {
        this.key = key;
        this.policy = policy;
        this.timeToLive = timeToLive;
    }

    public CacheInfo(CachePolicy policy, long timeToLive) {
        this.policy = policy;
        this.timeToLive = timeToLive;
    }

    /**
     * If the results for this request were loaded from the cache or not
     *
     * @return true if the current results have been loaded from the cache
     */
    public boolean isLoadedFromCache() {
        return loadedFromCache;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CachePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(CachePolicy policy) {
        this.policy = policy;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setLoadedFromCache(boolean loadedFromCache) {
        this.loadedFromCache = loadedFromCache;
    }

}
