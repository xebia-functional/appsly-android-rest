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

import java.net.URI;
import java.util.List;

/**
 * Encapsulates the methods exposed in a request operation
 */
public interface RequestOperation {

	/**
	 * Removes headers from this operation
	 *
	 * @param header the header name
	 */
	void removeHeaders(String header);

	/**
	 * Sets a header in this operation, replacing any existing headers with the same name
	 *
	 * @param header the header name
	 * @param value  the header value
	 */
	void setHeader(String header, String value);

	/**
	 * Adds a header in this operation, not replacing any existing headers with the same name
	 *
	 * @param header the header name
	 * @param value  the header value
	 */
	void addHeader(String header, String value);

	/**
	 * Gets an unmodifiable list of all present headers for this operation
	 *
	 * @return unmodifiable list of all present headers for this operation
	 */
	List<HeaderPair> getAllHeaders();

	/**
	 * The http method in use
	 *
	 * @return http method in use
	 */
	String getMethod();

	/**
	 * The final URI to which the request will be sent
	 *
	 * @return URI to which the request will be sent
	 */
	URI getURI();

	/**
	 * Sets the uri
	 *
	 * @param uri The final URI to which the request will be sent
	 */
	void setURI(URI uri);

	/**
	 * Aborts this request
	 */
	void abort();

	/**
	 * If the request has been aborted
	 *
	 * @return true if the request has been aborted, false otherwise
	 */
	boolean isAborted();

}
