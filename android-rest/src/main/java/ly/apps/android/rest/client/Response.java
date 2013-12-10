package ly.apps.android.rest.client;


import ly.apps.android.rest.cache.CacheInfo;
import org.apache.http.Header;

public class Response<Result> {

    private int statusCode;

    private Header[] headers;

    private String rawData;

    private Result result;

    private Throwable error;

    private CacheInfo cacheInfo;

    public Response(int statusCode, Header[] headers, String rawData, Result result, Throwable error, CacheInfo cacheInfo) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.rawData = rawData;
        this.result = result;
        this.error = error;
        this.cacheInfo = cacheInfo;
    }

    public Result getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }
}
