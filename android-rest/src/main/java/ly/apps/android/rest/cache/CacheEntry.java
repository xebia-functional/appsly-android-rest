package ly.apps.android.rest.cache;

import java.io.Serializable;

/**
 * Created by raulraja on 12/11/13.
 */
public class CacheEntry implements Serializable {

    private Object data;

    private long lastUpdated;

    public CacheEntry(Object data, long lastUpdated) {
        this.data = data;
        this.lastUpdated = lastUpdated;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
