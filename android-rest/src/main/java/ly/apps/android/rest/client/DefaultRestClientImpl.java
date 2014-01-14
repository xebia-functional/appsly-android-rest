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

package ly.apps.android.rest.client;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import ly.apps.android.rest.cache.CacheAwareHttpClient;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.converters.impl.MultipartEntity;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.entity.FileEntity;

import java.io.File;
import java.util.Map;

/**
 * Default impl of the RestClient that delegates calls to an AsyncHttpClient
 */
public class DefaultRestClientImpl implements RestClient {

    private AsyncHttpClient client;

    private QueryParamsConverter queryParamsConverter;

    private BodyConverter converter;

    public DefaultRestClientImpl(AsyncHttpClient client, QueryParamsConverter queryParamsConverter, BodyConverter converter) {
        this.queryParamsConverter = queryParamsConverter;
        this.converter = converter;
        this.client = client;
    }

    @Override
    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        client.cancelRequests(context, mayInterruptIfRunning);
    }

    private <T> void prepareRequest(Callback<T> delegate) {
        delegate.setBodyConverter(converter);
    }

    @Override
    public <T> void get(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.get(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    @Override
    public <T> void delete(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.delete(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    @Override
    public <T> void post(String url, Object body, Callback<T> delegate) {
        prepareRequest(delegate);
        HttpEntity entity = converter.toRequestBody(body, delegate.getRequestContentType());
        if (entity instanceof MultipartEntity) {
            MultipartEntity multipartEntity = ((MultipartEntity)entity);
            multipartEntity.setProgressHandler(delegate);
            delegate.setRequestContentType(multipartEntity.getContentType().getValue());
        }
        client.post(delegate.getContext(), url, delegate.getAdditionalHeaders(), entity , delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void postFile(String url, File file, Callback<T> delegate) {
        prepareRequest(delegate);
        client.post(delegate.getContext(), url, delegate.getAdditionalHeaders(), new FileEntity(file, delegate.getRequestContentType()), delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void put(String url, Object body, Callback<T> delegate) {
        prepareRequest(delegate);
        client.put(delegate.getContext(), url, delegate.getAdditionalHeaders(), converter.toRequestBody(body, delegate.getRequestContentType()), delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void head(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.head(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    public void setClient(AsyncHttpClient client) {
        this.client = client;
    }

    public void setQueryParamsConverter(QueryParamsConverter queryParamsConverter) {
        this.queryParamsConverter = queryParamsConverter;
    }

    public BodyConverter getConverter() {
        return converter;
    }

    public void setConverter(BodyConverter converter) {
        this.converter = converter;
    }

    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
            client.removeHeader(header.getKey());
            client.addHeader(header.getKey(), header.getValue());
        }
    }

    public QueryParamsConverter getQueryParamsConverter() {
        return queryParamsConverter;
    }



}
