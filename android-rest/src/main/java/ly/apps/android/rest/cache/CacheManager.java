package ly.apps.android.rest.cache;

/**
 * Manages the request cache whenever enabled for a given request
 */
public interface CacheManager {

    /**
     * Calculates a cache key based on a array of params
     *
     * @param args the params
     * @return the unique cache key
     */
    String getCacheKey(Object... args);

    /**
     * Adds an object to the cache given a cache key
     * @param key the key
     * @param object the value
     */
    <T> void put(String key, T object);

    /**
     * Retrieves an object from the cache given a cache key
     * @param key the key
     * @return the value if found
     */
    <T> T get(String key);

    /**
     * Invalidates a set of entries in the cache
     * @param keys the keys to invalidate
     */
    void invalidate(String ... keys);

    /**
     * Removes all entries from the cache
     */
    void invalidateAll();

}
