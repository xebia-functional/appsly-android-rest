package ly.apps.android.rest.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class CacheAwareHttpClient extends AsyncHttpClient {

    private CacheManager cacheManager;

    public CacheAwareHttpClient(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void enableHttpResponseCache(final long httpCacheSize, final File httpCacheDir) {
        try {
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Logger.d("android.net.http.HttpResponseCache not available, probably because we're running on a pre-ICS version of Android. Using com.integralblue.httpresponsecache.HttpHttpResponseCache.");
            try {
                com.integralblue.httpresponsecache.HttpResponseCache.install(httpCacheDir, httpCacheSize);
            } catch (Exception e) {
                Logger.d("Failed to set up com.integralblue.httpresponsecache.HttpResponseCache " + e.getMessage());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RequestHandle sendRequest(final DefaultHttpClient client, final HttpContext httpContext, final HttpUriRequest uriRequest, final String contentType, final ResponseHandlerInterface responseHandler, final Context context) {

        Logger.d("CacheAwareHttpClient.sendRequest");

        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }

        responseHandler.setRequestHeaders(uriRequest.getAllHeaders());
        responseHandler.setRequestURI(uriRequest.getURI());

        AsyncTask<Void, Void, Object> task;

        if (Callback.class.isAssignableFrom(responseHandler.getClass())) {
            final Callback<Object> callback = (Callback<Object>) responseHandler;
            final CacheInfo cacheInfo = callback.getCacheInfo();
            callback.setCacheManager(cacheManager);
            if (callback.getCacheInfo().getKey() == null) {
                try {
                    callback.getCacheInfo().setKey(uriRequest.getURI().toURL().toString());
                } catch (MalformedURLException e) {
                    Logger.e("unchacheable because uri threw : ", e);
                }
            }

            task = new AsyncTask<Void, Void, Object>() {

                @Override
                public Object doInBackground(Void... params) {
                    Object cachedResult = null;
                    if (Callback.class.isAssignableFrom(responseHandler.getClass())) {
                        switch (callback.getCacheInfo().getPolicy()) {
                            case ENABLED:
                                try {
                                    cachedResult = cacheManager.get(cacheInfo.getKey(), cacheInfo);
                                } catch (IOException e) {
                                    Logger.e("cache error", e);
                                } catch (ClassNotFoundException e) {
                                    Logger.e("cache error", e);
                                }
                                break;
                            case NETWORK_ENABLED:
                                try {
                                    cachedResult = cacheManager.get(cacheInfo.getKey(), cacheInfo);
                                } catch (IOException e) {
                                    Logger.e("cache error", e);
                                } catch (ClassNotFoundException e) {
                                    Logger.e("cache error", e);
                                }
                                break;
                            case LOAD_IF_OFFLINE:
                                if (callback.getContext() == null) {
                                    throw new IllegalArgumentException("Attempt to use LOAD_IF_OFFLINE on a callback with no context provided. Context is required to lookup internet connectivity");
                                }
                                ConnectivityManager connectivityManager = (ConnectivityManager) callback.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                                    try {
                                        cachedResult = cacheManager.get(cacheInfo.getKey(), cacheInfo);
                                    } catch (IOException e) {
                                        Logger.e("cache error", e);
                                    } catch (ClassNotFoundException e) {
                                        Logger.e("cache error", e);
                                    }
                                }
                                break;
                            default:
                                break;

                        }
                    }
                    return cachedResult;
                }

                @Override
                public void onPostExecute(Object result) {
                    if (result != null) {
                        Logger.d("CacheAwareHttpClient.sendRequest.onPostExecute proceeding with cache: " + result);
                        callback.getCacheInfo().setLoadedFromCache(true);
                        callback.onSuccess(HttpStatus.SC_OK, null, null, result);
                    } else {
                        Logger.d("CacheAwareHttpClient.sendRequest.onPostExecute proceeding uncached");
                        CacheAwareHttpClient.super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
                    }
                }

            };
            task.execute();
        }

        return new RequestHandle(null);
    }
}