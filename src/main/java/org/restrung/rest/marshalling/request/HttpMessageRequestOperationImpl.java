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

package org.restrung.rest.marshalling.request;

import org.restrung.rest.marshalling.HeaderPair;
import org.restrung.rest.marshalling.HeaderPairImpl;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wrapps a http message exposing the methods that the API caller will be allowed to customize and contribute to before a request is sent out
 */
public class HttpMessageRequestOperationImpl implements RequestOperation {

    /**
     * The message delegate that is receiving the customizations
     */
    private HttpRequestBase delegate;

    /**
     * Default delegate based constructor
     *
     * @param delegate the HttpMessage delegate
     */
    public HttpMessageRequestOperationImpl(HttpRequestBase delegate) {
        this.delegate = delegate;
    }

    /**
     * Removes headers from this operation
     *
     * @param header the header name
     */
    @Override
    public void removeHeaders(String header) {
        delegate.removeHeaders(header);
    }

    /**
     * Sets a header in this operation, replacing any existing headers with the same name
     *
     * @param header the header name
     * @param value  the header value
     */
    @Override
    public void setHeader(String header, String value) {
        delegate.setHeader(header, value);
    }

    /**
     * Adds a header in this operation, not replacing any existing headers with the same name
     *
     * @param header the header name
     * @param value  the header value
     */
    @Override
    public void addHeader(String header, String value) {
        delegate.addHeader(header, value);
    }

    /**
     * Gets an unmodifiable list of all present headers for this operation
     *
     * @return unmodifiable list of all present headers for this operation
     */
    @Override
    public List<HeaderPair> getAllHeaders() {
        Header[] headers = delegate.getAllHeaders();
        List<HeaderPair> headerPairs = new ArrayList<HeaderPair>(headers.length);
        for (Header header : headers) {
            headerPairs.add(new HeaderPairImpl(header));
        }
        return Collections.unmodifiableList(headerPairs);
    }

    /**
     * The http method in use
     *
     * @return http method in use
     */
    @Override
    public String getMethod() {
        return delegate.getMethod();
    }

    /**
     * The final URI to which the request will be sent
     *
     * @return URI to which the request will be sent
     */
    @Override
    public URI getURI() {
        return delegate.getURI();
    }

    /**
     * Sets the uri
     *
     * @param uri The final URI to which the request will be sent
     */
    @Override
    public void setURI(URI uri) {
        delegate.setURI(uri);
    }

    /**
     * Aborts this request
     */
    @Override
    public void abort() {
        delegate.abort();
    }

    /**
     * If the request has been aborted
     *
     * @return true if the request has been aborted, false otherwise
     */
    @Override
    public boolean isAborted() {
        return delegate.isAborted();
    }


}
