/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * http://apps.ly
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.apps.android.rest.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Pair;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * A http client impl aware of cache policies
 */
public class CacheAwareHttpClient extends AsyncHttpClient {

    private CacheManager cacheManager;

    public CacheAwareHttpClient(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Enable de underlying http response cache
     * @param httpCacheSize the max syze in bytes
     * @param httpCacheDir the dir where the cache will be stored
     * https://github.com/candrews/HttpResponseCache
     */
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

    /**
     * Intercepts all requests sent and run them through the cache policies
     * @see com.loopj.android.http.AsyncHttpClient#sendRequest(org.apache.http.impl.client.DefaultHttpClient, org.apache.http.protocol.HttpContext, org.apache.http.client.methods.HttpUriRequest, String, com.loopj.android.http.ResponseHandlerInterface, android.content.Context)
     */
    @Override
    @SuppressWarnings("unchecked")
    protected RequestHandle sendRequest(final DefaultHttpClient client, final HttpContext httpContext, final HttpUriRequest uriRequest, final String contentType, final ResponseHandlerInterface responseHandler, final Context context) {

        Logger.d("CacheAwareHttpClient.sendRequest");

        if (contentType != null && !uriRequest.containsHeader("Content-Type")) {
            uriRequest.addHeader("Content-Type", contentType);
        }

        responseHandler.setRequestHeaders(uriRequest.getAllHeaders());
        responseHandler.setRequestURI(uriRequest.getURI());

        AsyncTask<Void, Void, Pair<Object, Boolean>> task;

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

            task = new AsyncTask<Void, Void, Pair<Object, Boolean>>() {

                @Override
                public Pair<Object, Boolean> doInBackground(Void... params) {
                    Pair<Object, Boolean> cachedResult = null;
                    if (Callback.class.isAssignableFrom(responseHandler.getClass())) {
                        switch (callback.getCacheInfo().getPolicy()) {
                            case ENABLED:
                                try {
                                    cachedResult = new Pair<Object, Boolean>(cacheManager.get(cacheInfo.getKey(), cacheInfo), false);
                                } catch (IOException e) {
                                    Logger.e("cache error", e);
                                } catch (ClassNotFoundException e) {
                                    Logger.e("cache error", e);
                                }
                                break;
                            case NETWORK_ENABLED:
                                try {
                                    cachedResult = new Pair<Object, Boolean>(cacheManager.get(cacheInfo.getKey(), cacheInfo), true);
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
                                        cachedResult = new Pair<Object, Boolean>(cacheManager.get(cacheInfo.getKey(), cacheInfo), false);
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
                public void onPostExecute(Pair<Object, Boolean> result) {
                    if (result != null && result.first != null) {
                        Logger.d("CacheAwareHttpClient.sendRequest.onPostExecute proceeding with cache: " + result);
                        callback.getCacheInfo().setLoadedFromCache(true);
                        callback.onSuccess(HttpStatus.SC_OK, null, null, result.first);
                        if (result.second != null && result.second) { //retry request if necessary even if loaded fron cache such as in NETWORK_ENABLED
                            CacheAwareHttpClient.super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
                        }
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