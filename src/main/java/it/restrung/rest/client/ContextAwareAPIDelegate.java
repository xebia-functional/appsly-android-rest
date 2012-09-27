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

package it.restrung.rest.client;

import android.content.Context;
import android.support.v4.app.Fragment;
import it.restrung.rest.cache.CacheInfo;
import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.marshalling.request.RequestOperation;
import it.restrung.rest.marshalling.response.JSONResponse;
import it.restrung.rest.marshalling.response.ResponseOperation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wraps the API delegate with commodity values for cache policies, etc...
 */
public abstract class ContextAwareAPIDelegate<Result extends JSONResponse> implements APIDelegate<Result> {

    private static final AtomicInteger defaultOperationId = new AtomicInteger();

    /**
     * The context provider
     */
    private ContextProvider contextProvider;

    /**
     * The active cache loading policy for this delegate
     */
    private RequestCache.LoadPolicy cacheLoadPolicy = RequestCache.LoadPolicy.LOAD_IF_OFFLINE;

    /**
     * The active cache storage policy for this delegate
     */
    private RequestCache.StoragePolicy cacheStoragePolicy = RequestCache.StoragePolicy.PERMANENTLY;

    /**
     * The cache info fo this delegate
     */
    private CacheInfo cacheInfo;

    /**
     * the expected response type
     */
    private Class<Result> expectedResponseType;

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with default cache policies
     * @param context the context
     * @param target the expected response type
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target) {
        this(context, target, null, null);
    }

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with specific cache policies
     * @param context the context
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(context, target, cacheLoadPolicy, null);
    }

    /**
     * Constructor for FragmentActivity, Activity and other Context implementers with specific cache policies
     * @param context the context
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    protected ContextAwareAPIDelegate(Context context, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        init(target, cacheLoadPolicy, cacheStoragePolicy);
        setContextProvider(DefaultContextProvider.get(context));
    }

    /**
     * Constructor for Fragments with default cache policies
     * @param fragment the fragment
     * @param target the expected response type
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target) {
        this(fragment, target, null, null);
    }

    /**
     * Constructor for Fragments with specific cache policies
     * @param fragment the fragment
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy) {
        this(fragment, target, cacheLoadPolicy, null);
    }

    /**
     * Constructor for Fragments with specific cache policies
     * @param fragment the fragment
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    protected ContextAwareAPIDelegate(Fragment fragment, Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        init(target, cacheLoadPolicy, cacheStoragePolicy);
        setContextProvider(FragmentContextProvider.get(fragment));
    }

    /**
     * Private helper that that all constructors delegate to and that initializes the delegate
     * @param target the expected response type
     * @param cacheLoadPolicy the cache load policy
     * @param cacheStoragePolicy the cache storage policy
     */
    private void init(Class<Result> target, RequestCache.LoadPolicy cacheLoadPolicy, RequestCache.StoragePolicy cacheStoragePolicy) {
        if (cacheLoadPolicy != null) {
            this.cacheLoadPolicy = cacheLoadPolicy;
        }
        if (cacheStoragePolicy != null) {
            this.cacheStoragePolicy = cacheStoragePolicy;
        }
        this.expectedResponseType = target;
    }

    /**
     * @see APIDelegate#getOperationId()
     */
    @Override
    public int getOperationId() {
        return defaultOperationId.incrementAndGet();
    }

    /**
     * @see APIDelegate#onRequest(it.restrung.rest.marshalling.request.RequestOperation)
     */
    @Override
    public void onRequest(RequestOperation operation) {
    }

    /**
     * @see APIDelegate#onResponse(it.restrung.rest.marshalling.response.ResponseOperation)
     */
    @Override
    public void onResponse(ResponseOperation operation) {
    }

    /**
     * @see it.restrung.rest.client.APIDelegate#getContextProvider()
     */
    @Override
    public ContextProvider getContextProvider() {
        return this.contextProvider;
    }

    /**
     * @see it.restrung.rest.client.APIDelegate#setContextProvider(ContextProvider)
     */
    @Override
    public void setContextProvider(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    /**
     * @see APIDelegate#getCacheLoadPolicy()
     */
    public RequestCache.LoadPolicy getCacheLoadPolicy() {
        return cacheLoadPolicy;
    }

    /**
     * @see APIDelegate#setCacheLoadPolicy(it.restrung.rest.cache.RequestCache.LoadPolicy)
     */
    public void setCacheLoadPolicy(RequestCache.LoadPolicy cacheLoadPolicy) {
        this.cacheLoadPolicy = cacheLoadPolicy;
    }

    /**
     * @see it.restrung.rest.client.APIDelegate#getCacheStoragePolicy()
     */
    public RequestCache.StoragePolicy getCacheStoragePolicy() {
        return cacheStoragePolicy;
    }

    /**
     * @see it.restrung.rest.client.APIDelegate#setCacheStoragePolicy(it.restrung.rest.cache.RequestCache.StoragePolicy)
     */
    public void setCacheStoragePolicy(RequestCache.StoragePolicy cacheStoragePolicy) {
        this.cacheStoragePolicy = cacheStoragePolicy;
    }

    /**
     * gets the cache info
     * @return the cache info
     */
    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    /**
     * Set the cache info object
     * @param cacheInfo the cache info object
     */
    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    /**
     * Gets the expected response type
     * @return the expected response type
     */
    public Class<Result> getExpectedResponseType() {
        return expectedResponseType;
    }

    /**
     * Sets the expected response type
     * @param expectedResponseType the expected response type
     */
    public void setExpectedResponseType(Class<Result> expectedResponseType) {
        this.expectedResponseType = expectedResponseType;
    }
}
