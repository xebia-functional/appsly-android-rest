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
import android.os.AsyncTask;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import ly.apps.android.rest.client.RequestAwareContext;
import ly.apps.android.rest.client.Response;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collection;

/**
 * Base callbacks with cache supprot
 * @param <Result>
 */
public abstract class CacheAwareCallback<Result> extends BaseJsonHttpResponseHandler<Result> {

    private int timesProcessed;

    private Class<Result> targetClass;

    private BodyConverter bodyConverter;

    private Header[] additionalHeaders;

    private String requestContentType = HeaderUtils.CONTENT_TYPE_JSON;

    private boolean responseIsCollection;

    private Class<? extends Collection> collectionType;

    protected CacheAwareCallback(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    protected CacheAwareCallback(Context context, Class<Result> targetClass) {
        this(context);
        this.targetClass = targetClass;
    }

    protected CacheAwareCallback(Class<Result> targetClass) {
        this.targetClass = targetClass;
    }

    public int getTimesProcessed() {
        return timesProcessed;
    }

    public void setTargetClass(Class<Result> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<Result> getTargetClass() {
        return targetClass;
    }

    public BodyConverter getBodyConverter() {
        return bodyConverter;
    }

    public void setBodyConverter(BodyConverter bodyConverter) {
        this.bodyConverter = bodyConverter;
    }

    public Header[] getAdditionalHeaders() {
        return additionalHeaders;
    }

    public void setAdditionalHeaders(Header[] additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public boolean isResponseIsCollection() {
        return responseIsCollection;
    }

    public void setResponseIsCollection(boolean isList) {
        this.responseIsCollection = isList;
    }

    public Class<? extends Collection> getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Class<? extends Collection> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, String rawData, Result errorResponse) {
        Logger.d("onFailure: status" + statusCode + " rawResponse: " + rawData);
        Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawData, errorResponse, e, getCacheInfo());
        if (proceedWithResponse()) {
            timesProcessed++;
            onResponse(httpResponse);
        }
    }

    @Override
    protected Result parseResponse(String responseBody) throws Throwable {
        Logger.d("parsingResponse: intoTargetClass: " + targetClass + "responseBody: " + responseBody);
        return bodyConverter.fromResponseBody(targetClass, HeaderUtils.CONTENT_TYPE_JSON, new StringEntity(responseBody, "UTF-8"), this);
    }

    private Context context;

    private CacheInfo cacheInfo;

    private CacheManager cacheManager;

    protected CacheAwareCallback() {
    }

    protected CacheAwareCallback(Context context) {
        this.context = context;
    }

    protected boolean proceedWithResponse() {
        boolean proceed = true;
        if (context != null && context instanceof RequestAwareContext) {
            proceed = !((RequestAwareContext) context).shouldStopReceivingCallbacks();
        }
        return proceed;
    }

    public Context getContext() {
        return context;
    }

    public abstract void onResponse(Response<Result> response);

    private boolean shouldCache() {
        return !cacheInfo.isLoadedFromCache() && cacheInfo != CacheInfo.NONE;
    }

    @Override
    public void onSuccess(final int statusCode, final Header[] headers, final String rawResponse, final Result response) {
        Logger.d("onSuccess: status" + statusCode + " rawResponse: " + rawResponse);
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                if (response != null && shouldCache()) {
                    try {
                        getCacheManager().put(getCacheInfo().getKey(), response, getCacheInfo());
                    } catch (IOException e) {
                        Logger.e("cache error", e);
                    }
                }
                return response;
            }

            @Override
            protected void onPostExecute(Result result) {
                Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawResponse, result, null, getCacheInfo());
                if (proceedWithResponse()) {
                    timesProcessed++;
                    onResponse(httpResponse);
                }
            }
        }.execute();
    }

    @Override
    public void onFailure(final int statusCode, final Header[] headers, final String responseBody, final Throwable e) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                Result cachedResponse = null;
                boolean loadFromCache = false;
                switch (cacheInfo.getPolicy()) {
                    case LOAD_IF_TIMEOUT:
                        if (e != null && (ConnectTimeoutException.class.isAssignableFrom(e.getClass()) || SocketTimeoutException.class.isAssignableFrom(e.getClass()))) {
                            loadFromCache = true;
                        }
                        break;
                    case LOAD_ON_ERROR:
                        loadFromCache = true;
                        break;
                    default:
                        break;

                }
                if (loadFromCache) {
                    try {
                        cachedResponse = cacheManager.get(cacheInfo.getKey(), cacheInfo);
                    } catch (IOException e1) {
                        Logger.e("cache error", e);
                    } catch (ClassNotFoundException e1) {
                        Logger.e("cache error", e);
                    }
                }
                return cachedResponse;
            }

            @Override
            public void onPostExecute(Result result) {
                Logger.d("CacheAwareCallback. Loading from cache after response error: " + e.getLocalizedMessage() + " result with cached result: " + result);
                if (result != null) {
                    cacheInfo.setLoadedFromCache(true);
                    onSuccess(statusCode, headers, responseBody, result);
                } else {
                    CacheAwareCallback.super.onFailure(statusCode, headers, responseBody, e);
                }
            }

        }.execute();
    }

    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
