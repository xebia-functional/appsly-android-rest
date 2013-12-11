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

/**
 * Manages the request cache whenever enabled for a given request
 */
public class RequestCache {

//    /**
//     * Prevents instantiation
//     */
//    private RequestCache() {
//    }
//
//    /**
//     * The extension used for the cache files
//     */
//    private static String CACHE_EXTENSION = ".obj.cache";
//
//    /**
//     * Enum representing the different policies available that determine where cached results will be served
//     */
//    public enum LoadPolicy {
//
//        /**
//         * Never use the cache
//         */
//        NEVER,
//
//        /**
//         * Load from the cache when we are offline
//         */
//        LOAD_IF_OFFLINE,
//
//        /**
//         * Load from the cache if we encounter an error
//         */
//        LOAD_ON_ERROR,
//
//        /**
//         * Load from the cache if we have data stored and the server returns a 304 (not modified) response
//         */
//        ETAG,
//
//        /**
//         * Load from the cache if we have data stored
//         */
//        ENABLED,
//
//        /**
//         * Load from the cache if the request times out
//         */
//        LOAD_IF_TIMEOUT,
//
//        /**
//         * Load from the cache then refresh the cache with a network call (calls subscribers twice)
//         */
//        NETWORK_ENABLED
//    }
//
//    /**
//     * Enum representing the policies for storing the cached results
//     */
//    public enum StoragePolicy {
//
//        /**
//         * The cache has been disabled. Attempts to store data will silently fail
//         */
//        DISABLED,
//
//        /**
//         * Cache data permanently, until explicitly expired or flushed
//         */
//        PERMANENT
//    }
//
//    /**
//     * Represents an operation wrapped in a callable that will be performed following the cache policies for loading
//     * and storage
//     *
//     * @param <T>
//     */
//    public static class CacheAwareOperation<T extends Serializable> {
//
//        /**
//         * The provider containing information regarding the cache policies
//         */
//        private CacheRequestInfoProvider<T> provider;
//
//        /**
//         * The operation that is a candidate to be performed and results replaced with cached results
//         */
//        private Callable<T> operation;
//
//        /**
//         * The params that are considered as unique key for the cache
//         */
//        private Object[] params;
//
//        /**
//         * Constructs a cache aware operation
//         *
//         * @param cacheRequestInfoProvider The provider containing information regarding the cache policies
//         * @param operation                The operation that is a candidate to be performed and results replaced with cached results
//         * @param params                   The params that are considered as unique key for the cache
//         */
//        public CacheAwareOperation(CacheRequestInfoProvider<T> cacheRequestInfoProvider, Callable<T> operation, Object... params) {
//            this.provider = cacheRequestInfoProvider;
//            this.operation = operation;
//            this.params = params;
//        }
//
//        /**
//         * loads results from the cache based on this provider and params from which the key will be determined to lookup
//         * the cache file
//         *
//         * @return the stored object
//         */
//        private T loadFromCache() {
//            return get(provider, params);
//        }
//
//        /**
//         * Performs the operation and stores the result in the cache
//         *
//         * @return the results
//         */
//        private T performOperation() throws Exception {
//            T result = operation.call();
//            provider.setCacheInfo(CacheInfo.NONE);
//            if (StoragePolicy.PERMANENT.equals(provider.getCacheStoragePolicy())) {
//                put(provider.getContextProvider().getContext(), result, params);
//            }
//            return result;
//        }
//
//        /**
//         * Enforces the cache policies
//         *
//         * @return the operation result
//         */
//        public T execute() throws Exception {
//            T result;
//            switch (provider.getCacheLoadPolicy()) {
//                case ENABLED:
//                case NETWORK_ENABLED: //both enabled and network enabled load from cache first, network enabled requests are retried at the the RestClient level
//                    result = loadFromCache();
//                    if (result == null) {
//                        result = performOperation();
//                    }
//                    break;
//                case ETAG:
//                    throw new UnsupportedOperationException("ETAG not yet supported");
//                case LOAD_IF_OFFLINE:
//                    ConnectivityManager connectivityManager = (ConnectivityManager) provider.getContextProvider().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if (connectivityManager.getActiveNetworkInfo().isConnected()) {
//                        result = performOperation();
//                    } else {
//                        result = loadFromCache();
//                    }
//                    break;
//                case LOAD_IF_TIMEOUT:
//                    try {
//                        result = performOperation();
//                    } catch (ConnectTimeoutException cte) {
//                        result = loadFromCache();
//                    } catch (SocketTimeoutException ste) {
//                        result = loadFromCache();
//                    }
//                    break;
//                case LOAD_ON_ERROR:
//                    try {
//                        result = performOperation();
//                    } catch (Exception ex) {
//                        result = loadFromCache();
//                    }
//                    break;
//                default:
//                    result = performOperation();
//
//            }
//            return result;
//        }
//    }
//
//    /**
//     * Calculates a cache key based on a array of params by performing a deep hashcode lookup
//     *
//     * @param params the params
//     * @return the unique cache key
//     */
//    public static int cacheKey(Object... params) {
//        return Arrays.deepHashCode(params);
//    }
//
//    /**
//     * Gets the cache file based on the requesting context, the cache key and the cache file extension
//     *
//     * @param context  the requesting context
//     * @param cacheKey the cache key
//     * @return the cache file
//     */
//    private static File getCacheFile(Context context, int cacheKey) {
//        return new File(String.format("%s/%d%s", context.getCacheDir(), cacheKey, CACHE_EXTENSION));
//    }
//
//    /**
//     * Invalidates the cache for the requesting context by removing all files associated with it
//     *
//     * @param context the requesting context
//     */
//    public static void invalidateAll(Context context) {
//        for (File file : context.getCacheDir().listFiles()) {
//            if (file.getName().endsWith(CACHE_EXTENSION)) {
//                file.delete();
//            }
//        }
//    }
//
//    /**
//     * Adds an object to the cache based on a list of params that are used to calculate the cache key
//     *
//     * @param context the requesting context
//     * @param result  the object to be cached
//     * @param params  the params to calculate the cache key
//     */
//    public static void put(Context context, Serializable result, Object... params) {
//        File file = getCacheFile(context, cacheKey(params));
//        FileUtils.saveSerializableObjectToDisk(result, file);
//    }
//
//    /**
//     * Gets and deserializes an object from a file into its memory original representation
//     *
//     * @param context the requesting context
//     * @param params  the params to calculate the cache key
//     * @param <T>     the resulting type
//     * @return the in memory original object
//     */
//    public static <T extends Serializable> T get(Context context, Object... params) {
//        File file = getCacheFile(context, cacheKey(params));
//        return FileUtils.loadSerializableObjectFromDisk(file);
//    }
//
//    /**
//     * Gets and deserializes an object from a file into its memory original representation
//     *
//     * @param cacheRequestInfoProvider the cache info provider
//     * @param params                   the params to calculate the cache key
//     * @param <T>                      the resulting type
//     * @return the in memory original object
//     */
//    public static <T extends Serializable> T get(CacheRequestInfoProvider<T> cacheRequestInfoProvider, Object... params) {
//        File file = getCacheFile(cacheRequestInfoProvider.getContextProvider().getContext(), cacheKey(params));
//        cacheRequestInfoProvider.setCacheInfo(new CacheInfo(true, new Date(file.lastModified())));
//        return FileUtils.loadSerializableObjectFromDisk(file);
//    }
//
//    /**
//     * Invalidates a cache entry
//     *
//     * @param context the requesting context
//     * @param params  the params to calculate the cache key
//     */
//    public static void invalidate(Context context, Object... params) {
//        File file = getCacheFile(context, cacheKey(params));
//        if (file.exists()) {
//            file.delete();
//        }
//    }
}
