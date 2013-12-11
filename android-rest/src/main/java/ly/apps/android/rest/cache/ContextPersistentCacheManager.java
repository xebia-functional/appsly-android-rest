package ly.apps.android.rest.cache;


import android.content.Context;
import ly.apps.android.rest.utils.Logger;
import ly.apps.android.rest.utils.ObjectCache;
import ly.apps.android.rest.utils.StringUtils;

import java.io.IOException;
import java.util.Date;

public class ContextPersistentCacheManager implements CacheManager {

    private ObjectCache objectCache;

    private Context context;

    public ContextPersistentCacheManager(Context context) {
        this.context = context;
        objectCache = new ObjectCache(context, "android-rest", 20971520);
    }

    /**
     * Invalidates the cache for this context by removing all files associated with it
     */
    @Override
    public void invalidateAll() throws IOException {
        objectCache.clearCache();
    }

    /**
     * Adds an object to the cache based on a list of params that are used to calculate the cache key
     *
     * @param key    the cache
     * @param object the object to be cached
     */
    @Override
    public <T> void put(String key, T object, CacheInfo cacheInfo) throws IOException {
        objectCache.put(StringUtils.md5(key), new CacheEntry(object, new Date().getTime()));
    }

    /**
     * Gets and deserializes an object from a file into its memory original representation
     *
     * @return the in memory original object
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, CacheInfo cacheInfo) throws IOException, ClassNotFoundException {
        T result = null;
        CacheEntry cacheEntry = (CacheEntry) objectCache.getObject(StringUtils.md5(key));
        if (cacheEntry != null) {
            long expiresOn = cacheEntry.getLastUpdated() + cacheInfo.getTimeToLive();
            long now = new Date().getTime();
            if (expiresOn >= now) {
                result = (T) cacheEntry.getData();
                Logger.d(String.format("Loading from cache because expiresOn : %d > now : %d", expiresOn, now));
            } else {
                Logger.d(String.format("Invalidated key : %s from cache because expiresOn : %d < now : %d", key, expiresOn, now));
            }
        }
        return result;
    }


}
