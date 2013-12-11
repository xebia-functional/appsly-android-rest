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
