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

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Encapsulates the methods exposed in a response operation
 */
public interface ResponseOperation {

    /**
     * Gets the response status code
     */
    int getStatusCode();

    /**
     * Allows to modify the response status code
     *
     * @param statusCode the status code
     * @throws IllegalStateException
     */
    void setStatusCode(int statusCode) throws IllegalStateException;

    /**
     * Sets a reason message for an error or description on a response
     *
     * @param reasonPhrase the reason phrase
     * @throws IllegalStateException
     */
    void setReasonPhrase(String reasonPhrase) throws IllegalStateException;

    /**
     * If the request is repeatable
     *
     * @return If the request is repeatable
     */
    boolean isRepeatable();

    /**
     * If the content is chunked
     *
     * @return If the content is chunked
     */
    boolean isChunked();

    /**
     * @return teh content length
     */
    long getContentLength();

    /**
     * The header content type
     *
     * @return the header content type as header pair
     */
    HeaderPair getContentType();

    /**
     * The header content encoding
     *
     * @return the header content encoding as header pair
     */
    HeaderPair getContentEncoding();

    /**
     * The entity content as an input stream
     *
     * @return the content as an input stream
     */
    InputStream getContent() throws IOException, IllegalStateException;

    /**
     * If the entity is in streaming mode
     *
     * @return if the current enity is in streaming mode
     */
    boolean isStreaming();

    /**
     * Gets the response locale
     *
     * @return the response locale
     */
    Locale getLocale();

    /**
     * Sets the response locale
     *
     * @param locale the new response lcoale
     */
    void setLocale(Locale locale);
}
