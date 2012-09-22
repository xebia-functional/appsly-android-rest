/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
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

package org.restrung.rest.client;

import android.content.Context;
import org.restrung.rest.cache.CacheInfo;
import org.restrung.rest.cache.RequestCache;
import org.restrung.rest.marshalling.request.RequestOperation;
import org.restrung.rest.marshalling.response.JSONResponse;
import org.restrung.rest.marshalling.response.ResponseOperation;

/**
 * Wraps the API delegate with commodity values for cache policies, etc...
 */
public abstract class ContextAwareAPIDelegate<Result extends JSONResponse> implements APIDelegate<Result> {

    private Context requestingContext;

    private RequestCache.LoadPolicy cacheLoadPolicy = RequestCache.LoadPolicy.LOAD_IF_OFFLINE;

    private RequestCache.StoragePolicy cacheStoragePolicy = RequestCache.StoragePolicy.PERMANENTLY;

    private CacheInfo cacheInfo;

    private Class<Result> expectedResponseType;

    protected ContextAwareAPIDelegate(Context requestingContext, Class<Result> target) {
        this(requestingContext, target, null, null);
    }

    protected ContextAwareAPIDelegate(Context requestingContext, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(requestingContext, target, cacheLoadPolicy, null);
    }

    protected ContextAwareAPIDelegate(Context requestingContext, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        this.requestingContext = requestingContext;
        if (cacheLoadPolicy != null) {
            this.cacheLoadPolicy = cacheLoadPolicy;
        }
        if (cacheStoragePolicy != null) {
            this.cacheStoragePolicy = cacheStoragePolicy;
        }
        this.expectedResponseType = target;
    }

    /**
     * @see org.restrung.rest.client.APIDelegate#getOperationId()
     */
    @Override
    public int getOperationId() {
        return 0;
    }

    /**
     * @see APIDelegate#onRequest(org.restrung.rest.marshalling.request.RequestOperation)
     */
    @Override
    public void onRequest(RequestOperation operation) {
    }

    /**
     * @see APIDelegate#onResponse(org.restrung.rest.marshalling.response.ResponseOperation)
     */
    @Override
    public void onResponse(ResponseOperation operation) {
    }

    /**
     * Return context
     * @return
     */
    public Context getRequestingContext() {
        return requestingContext;
    }

    /**
     * Set context
     * @param requestingContext
     */
    public void setRequestingContext(Context requestingContext) {
        this.requestingContext = requestingContext;
    }

    public RequestCache.LoadPolicy getCacheLoadPolicy() {
        return cacheLoadPolicy;
    }

    public void setCacheLoadPolicy(RequestCache.LoadPolicy cacheLoadPolicy) {
        this.cacheLoadPolicy = cacheLoadPolicy;
    }

    public RequestCache.StoragePolicy getCacheStoragePolicy() {
        return cacheStoragePolicy;
    }

    public void setCacheStoragePolicy(RequestCache.StoragePolicy cacheStoragePolicy) {
        this.cacheStoragePolicy = cacheStoragePolicy;
    }

    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    public Class<Result> getExpectedResponseType() {
        return expectedResponseType;
    }

    public void setExpectedResponseType(Class<Result> expectedResponseType) {
        this.expectedResponseType = expectedResponseType;
    }
}
