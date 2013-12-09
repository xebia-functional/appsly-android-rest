package ly.apps.android.rest.cache;

/**
 * Created by raulraja on 12/8/13.
 */
public class CacheService {

    private CacheManager cacheManager;

    private boolean cacheDisabled;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public boolean isCacheDisabled() {
        return cacheDisabled;
    }

    public void setCacheDisabled(boolean cacheDisabled) {
        this.cacheDisabled = cacheDisabled;
    }
}
