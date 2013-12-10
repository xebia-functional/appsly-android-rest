package ly.apps.android.rest.cache;


import android.content.Context;
import com.integralblue.httpresponsecache.compat.MD5;
import ly.apps.android.rest.utils.Base64;
import ly.apps.android.rest.utils.IOUtils;
import ly.apps.android.rest.utils.Logger;

import java.io.File;
import java.util.Date;

public class ContextPersistentCacheManager implements CacheManager {

    private static final String CACHE_EXTENSION = ".obj.cache";

    private Context context;

    public ContextPersistentCacheManager(Context context) {
        this.context = context;
    }

    /**
     * Gets the cache file based on the requesting context, the cache key and the cache file extension
     *
     * @param context  the requesting context
     * @param cacheKey the cache key
     * @return the cache file
     */
    private static File getCacheFile(Context context, String cacheKey) {
        String md5 = Base64.encode(cacheKey.getBytes());
        return new File(String.format("%s/%s%s", context.getCacheDir(), md5, CACHE_EXTENSION));
    }

    /**
     * Invalidates the cache for this context by removing all files associated with it
     */
    @Override
    public void invalidateAll() {
        if (context.getCacheDir() != null) {
            for (File file : context.getCacheDir().listFiles()) {
                if (file.getName().endsWith(CACHE_EXTENSION)) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Adds an object to the cache based on a list of params that are used to calculate the cache key
     *
     * @param key    the cache
     * @param object the object to be cached
     */
    @Override
    public <T> void put(String key, T object, CacheInfo cacheInfo) {
        File file = getCacheFile(context, key);
        IOUtils.saveSerializableObjectToDisk(object, file);
    }

    /**
     * Gets and deserializes an object from a file into its memory original representation
     *
     * @return the in memory original object
     */
    @Override
    public <T> T get(final String key, CacheInfo cacheInfo) {
        File file = getCacheFile(context, key);
        T result = null;
        long expiresOn = file.lastModified() + cacheInfo.getTimeToLive();
        long now = new Date().getTime();
        if (expiresOn >= now) {
            result = IOUtils.loadSerializableObjectFromDisk(file);
            Logger.d(String.format("Loading from cache because expiresOn : %d > now : %d", expiresOn, now));
        } else {
            invalidate(key);
            Logger.d(String.format("Invalidated key : %s from cache because expiresOn : %d < now : %d", key, expiresOn, now));
        }
        return result;
    }


    /**
     * Invalidates a cache entry
     */
    @Override
    public void invalidate(String... keys) {
        for (String key : keys) {
            File file = getCacheFile(context, key);
            if (file.exists()) {
                file.delete();
            }
        }
    }

}
