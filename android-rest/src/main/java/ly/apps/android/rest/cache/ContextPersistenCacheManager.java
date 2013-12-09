package ly.apps.android.rest.cache;


import android.content.Context;
import ly.apps.android.rest.utils.IOUtils;

import java.io.File;
import java.util.Arrays;

public class ContextPersistenCacheManager implements CacheManager {

    private static final String CACHE_EXTENSION = ".obj.cache";

    private Context context;

    public ContextPersistenCacheManager(Context context) {
        this.context = context;
    }

    @Override
    public String getCacheKey(Object... args) {
        return String.valueOf(Arrays.deepHashCode(args));
    }

    /**
     * Gets the cache file based on the requesting context, the cache key and the cache file extension
     *
     * @param context  the requesting context
     * @param cacheKey the cache key
     * @return the cache file
     */
    private static File getCacheFile(Context context, String cacheKey) {
        return new File(String.format("%s/%s%s", context.getCacheDir(), cacheKey, CACHE_EXTENSION));
    }

    /**
     * Invalidates the cache for this context by removing all files associated with it
     */
    @Override
    public void invalidateAll() {
        for (File file : context.getCacheDir().listFiles()) {
            if (file.getName().endsWith(CACHE_EXTENSION)) {
                file.delete();
            }
        }
    }

    /**
     * Adds an object to the cache based on a list of params that are used to calculate the cache key
     *
     * @param key the cache
     * @param object  the object to be cached
     */
    @Override
    public <T> void put(String key, T object) {
        File file = getCacheFile(context, key);
        IOUtils.saveSerializableObjectToDisk(object, file);
    }

    /**
     * Gets and deserializes an object from a file into its memory original representation
     *
     * @return the in memory original object
     */
    @Override
    public <T> T get(String key) {
        File file = getCacheFile(context, key);
        return IOUtils.loadSerializableObjectFromDisk(file);
    }

    /**
     * Invalidates a cache entry
     */
    @Override
    public void invalidate(String ...keys) {
        for (String key : keys) {
            File file = getCacheFile(context, key);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
