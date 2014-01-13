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
import ly.apps.android.rest.converters.QueryParamsConverter;

import java.io.File;

/**
 * Defines the contract for REST style operations
 */
public interface RestClient {

    /**
     * Performs an asynchronous GET request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format.
     *
     * @param url      the url
     * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void get(String url, Callback<T> delegate);

    /**
     * Performs an asynchronous DELETE request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format.
     *
     * @param url      the url
     * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void delete(String url, Callback<T> delegate);


    /**
     * Performs an asynchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format.
     *
     * @param url      the url
     * @param body     the body to be posted as an implementer of JSONSerializable
     * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void post(String url, Object body, Callback<T> delegate);

    /**
     * Performs an asynchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format. Optionally allows a file to be posted in which case the body should be sent as the first part of
     * a request with Multipart content type and the file as the second part.
     *
     * @param url        the url
     * @param file       the file to be posted. If not null the request will become a multipart request
     * @param delegate   the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void postFile(String url, File file, Callback<T> delegate);

    /**
     * Performs an asynchronous PUT request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format.
     *
     * @param url      the url
     * @param body     the body to be posted as an implementer of JSONSerializable
     * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void put(String url, Object body, Callback<T> delegate);


    /**
     * Performs an asynchronous PUT request delegating results to a @see APIDelegate replacing url placeholder based
     * on args using @see java.lang.String#format.
     *
     * @param url      the url
     * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
     */
    <T> void head(String url, Callback<T> delegate);

    /**
     * Converter used to serialize params to the query string
     */
    QueryParamsConverter getQueryParamsConverter();

    /**
     * Allows for programmatic shutdown of all requests incoming to a given context
     * Alternatively @see ly.apps.android.rest.client.RequestAwareContext
     * @param context the context
     */
    void cancelRequests(Context context, boolean mayInterruptIfRunning);

}
