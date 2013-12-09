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