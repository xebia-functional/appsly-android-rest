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

package it.restrung.rest.marshalling.response;

import it.restrung.rest.marshalling.HeaderPair;
import it.restrung.rest.marshalling.HeaderPairImpl;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Wraps an http message exposing the methods that the API caller will be allowed to customize and contribute to before a response is serialized
 */
public class HttpMessageResponseOperationImpl implements ResponseOperation {

    /**
     * The message delegate that is receiving the customizations
     */
    private HttpResponse delegate;

    /**
     * The response entity
     */
    private HttpEntity httpEntity;

    /**
     * Default delegate based constructor
     *
     * @param delegate the HttpResponse delegate
     */
    public HttpMessageResponseOperationImpl(HttpResponse delegate) {
        this.delegate = delegate;
        this.httpEntity = delegate.getEntity();
    }

    /**
     * @see ResponseOperation#getStatusCode()
     */
    @Override
    public int getStatusCode() {
        return delegate.getStatusLine().getStatusCode();
    }

    /**
     * @see ResponseOperation#setStatusCode(int)
     */
    @Override
    public void setStatusCode(int statusCode) throws IllegalStateException {
        delegate.setStatusCode(statusCode);
    }

    /**
     * @see ResponseOperation#setReasonPhrase(String)
     */
    @Override
    public void setReasonPhrase(String reasonPhrase) throws IllegalStateException {
        delegate.setReasonPhrase(reasonPhrase);
    }

    /**
     * @see ResponseOperation#isRepeatable()
     */
    @Override
    public boolean isRepeatable() {
        return httpEntity.isRepeatable();
    }

    /**
     * @see ResponseOperation#isChunked()
     */
    @Override
    public boolean isChunked() {
        return httpEntity.isChunked();
    }

    /**
     * @see ResponseOperation#getContentLength()
     */
    @Override
    public long getContentLength() {
        return httpEntity.getContentLength();
    }

    /**
     * @see ResponseOperation#getContentType()
     */
    @Override
    public HeaderPair getContentType() {
        return getHeaderPair(httpEntity.getContentType());
    }

    /**
     * @see ResponseOperation#getContentEncoding()
     */
    @Override
    public HeaderPair getContentEncoding() {
        return getHeaderPair(httpEntity.getContentEncoding());
    }

    /**
     * Private factory method to get a header pair
     *
     * @param header the http commons header
     * @return the header pair
     */
    private static HeaderPair getHeaderPair(Header header) {
        return new HeaderPairImpl(header);
    }

    /**
     * @see ResponseOperation#getContent()
     */
    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return httpEntity.getContent();
    }

    /**
     * @see ResponseOperation#isStreaming()
     */
    @Override
    public boolean isStreaming() {
        return httpEntity.isStreaming();
    }

    /**
     * @see ResponseOperation#getLocale()
     */
    @Override
    public Locale getLocale() {
        return delegate.getLocale();
    }

    /**
     * @see ResponseOperation#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale locale) {
        delegate.setLocale(locale);
    }
}
