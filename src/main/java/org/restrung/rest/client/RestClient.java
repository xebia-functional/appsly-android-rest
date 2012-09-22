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

import org.restrung.rest.exceptions.APIException;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.JSONResponse;

import java.io.File;

/**
 * Defines the contract for REST style operations
 */
public interface RestClient {

	/**
	 * Performs an asynchronous GET request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param args     a list of args used in replacement to parse the url
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 */
	<T extends JSONResponse> void getAsync(APIDelegate<T> delegate, String url, Object... args);

	/**
	 * Performs an asynchronous DELETE request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param args     a list of args used in replacement to parse the url
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 */
	<T extends JSONResponse> void deleteAsync(APIDelegate<T> delegate, String url, Object... args);

	/**
	 * Performs an asynchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param body     the body to be posted as an implementer of JSONSerializable
	 * @param args     a list of args used in replacement to parse the url
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 */
	<T extends JSONResponse> void postAsync(APIDelegate<T> delegate, String url, JSONSerializable body, Object... args);

	/**
	 * Performs an asynchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format. Optionally allows a file to be posted in which case the body should be sent as the first part of
	 * a request with Multipart content type and the file as the second part.
	 *
	 * @param delegate   the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url        the url
	 * @param body       the body to be posted as an implementer of JSONSerializable
	 * @param file       the file to be posted. If not null the request will become a multipart request
	 * @param postParams an optional callback delegate that can receive post progress and override if the requests is to be sent as multipart
	 * @param args       a list of args used in replacement to parse the url
	 * @param <T>        All serializable responses are expected to implement JSONResponse
	 */
	<T extends JSONResponse> void postAsync(APIDelegate<T> delegate, String url, File file, JSONSerializable body, APIPostParams postParams, Object... args);

	/**
	 * Performs an asynchronous PUT request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param body     the body to be posted as an implementer of JSONSerializable
	 * @param args     a list of args used in replacement to parse the url
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 */
	<T extends JSONResponse> void putAsync(APIDelegate<T> delegate, String url, JSONSerializable body, Object... args);

	/**
	 * Performs a synchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param body     the body to be posted as an implementer of JSONSerializable
	 * @param timeout  the request timeout
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 * @return a serialized instance of <T>
	 */
	<T extends JSONResponse> T post(APIDelegate<T> delegate, String url, JSONSerializable body, int timeout) throws APIException;

	/**
	 * Performs a synchronous POST request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format. Optionally allows a file to be posted in which case the body should be sent as the first part of
	 * a request with Multipart content type and the file as the second part.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param body     the body to be posted as an implementer of JSONSerializable
	 * @param file     the file to be posted. If not null the request will become a multipart request
	 * @param timeout  the request timeout
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 * @return a serialized instance of <T>
	 */
	<T extends JSONResponse> T post(APIDelegate<T> delegate, String url, JSONSerializable body, File file, int timeout, APIPostParams apiPostParams) throws APIException;

	/**
	 * Performs a synchronous PUT request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param body     the body to be posted as an implementer of JSONSerializable
	 * @param timeout  the request timeout
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 * @return a serialized instance of <T>
	 */
	<T extends JSONResponse> T put(APIDelegate<T> delegate, String url, JSONSerializable body, int timeout) throws APIException;

	/**
	 * Performs a synchronous GET request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param timeout  the request timeout
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 * @return a serialized instance of <T>
	 */
	<T extends JSONResponse> T get(APIDelegate<T> delegate, String url, int timeout) throws APIException;

	/**
	 * Performs a synchronous DELETE request delegating results to a @see APIDelegate replacing url placeholder based
	 * on args using @see java.lang.String#format.
	 *
	 * @param delegate the APIDelegate that will handle results, error and request and response interception if necessary
	 * @param url      the url
	 * @param timeout  the request timeout
	 * @param <T>      All serializable responses are expected to implement JSONResponse
	 * @return a serialized instance of <T>
	 */
	<T extends JSONResponse> T delete(APIDelegate<T> delegate, String url, int timeout) throws APIException;
}
