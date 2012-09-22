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

package org.restrung.rest.async;

import android.util.Log;
import org.restrung.rest.client.APICredentialsDelegate;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.client.APIPostParams;
import org.restrung.rest.exceptions.APIException;
import org.restrung.rest.exceptions.InvalidCredentialsException;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.JSONResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

/**
 * An Async operation used by both async tasks and loaders
 */
public class AsyncOperation <T extends JSONResponse> {

	/**
	 * Default timeout for requests in ms
	 */
	public static int DEFAULT_REQUEST_TIMEOUT = 10000;

	/**
	 * The url
	 */
	private String url;

	/**
	 * The API Delegate that gets notified of callbacks for results and errors
	 */
	private APIDelegate<T> delegate;

	/**
	 * A delegate callback and params object used in POST requests
	 */
	private APIPostParams delegateParams;

	/**
	 * A credentials delegate object that gets notified when receiving a 401 or other http codes related to authorization
	 */
	private APICredentialsDelegate apiCredentialsDelegate;

	/**
	 * An exception on this task
	 */
	private Exception exception;

	/**
	 * The request body
	 */
	private JSONSerializable body;

	/**
	 * A file that could be sent on multipart requests
	 */
	private File file;

	/**
	 * If the exception is related to bad credentials
	 */
	private boolean credentialsException;

	/**
	 * Constructs an async operation
	 *
	 * @param url                    the url
	 * @param delegate               the API delegate
	 * @param apiCredentialsDelegate the api credentials delegate
	 * @param params                 a set of params to be replaced in the url
	 */
	public AsyncOperation(String url, APIDelegate<T> delegate, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
		super();
		this.url = String.format(url, encode(params));
		this.delegate = delegate;
		this.apiCredentialsDelegate = apiCredentialsDelegate;
	}

	/**
	 * Constructs an async operation
	 *
	 * @param url                    the url
	 * @param file                   the file to be sent on a multipart
	 * @param delegate               the API delegate
	 * @param delegateParams         the post params to obtain progress and provide post related params
	 * @param apiCredentialsDelegate the api credentials delegate
	 * @param params                 a set of params to be replaced in the url
	 */
	public AsyncOperation(String url, JSONSerializable body, File file, APIDelegate<T> delegate, APIPostParams delegateParams, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
		super();
		this.url = String.format(url, encode(params));
		this.delegate = delegate;
		this.delegateParams = delegateParams;
		this.apiCredentialsDelegate = apiCredentialsDelegate;
		this.file = file;
		this.body = body;
	}

	/**
	 * Private helper that encodes an array of params
	 * @param params the params object
	 * @return the encoded array of params
	 */
	private static Object[] encode(Object[] params) {
		for (int i = 0; i < params.length; i++) {
			try {
				params[i] = URLEncoder.encode(params[i].toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(AsyncOperation.class.getName(), e.getLocalizedMessage());
			}
		}
		return params;
	}

	/**
	 * Handles the task or loader results in the main thread
	 */
	public void onPostExecute(T result) {
		if (exception == null) {
			if (result != null) {
				try {
					delegate.onResults(result);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			} else if (!credentialsException) {
				delegate.onResults(null);
			}
		} else {
			delegate.onError(exception);
		}
	}

	/**
	 * The API Delegate that receives callbacks for success and error
	 * @return the API delegate
	 */
	public APIDelegate<T> getApiDelegate() {
		return delegate;
	}

	/**
	 * The request Url
	 * @return the request url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * The post delegate params
	 * @return the post delegate params
	 */
	public APIPostParams getPostDelegateParams() {
		return delegateParams;
	}

	/**
	 * The API credentials delegate
	 * @return the api credentials delegate
	 */
	public APICredentialsDelegate getApiCredentialsDelegate() {
		return apiCredentialsDelegate;
	}

	/**
	 * The potential exception resulting from this operation
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * The file associated to this operation
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * The post, put body
	 * @return the post, put body as a JSONSerializable
	 */
	public JSONSerializable getBody() {
		return body;
	}

	/**
	 * if the exception is a credentials related exception
	 * @return true if a credential exception
	 */
	public boolean isCredentialsException() {
		return credentialsException;
	}

	/**
	 * Sets the exception
	 * @param exception the exception
	 */
	protected synchronized void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * Flags the current exception as a credentials exception
	 * @param credentialsException whether the exception is a credentials related exception
	 */
	protected synchronized void setCredentialsException(boolean credentialsException) {
		this.credentialsException = credentialsException;
	}

	/**
	 * Executes the request handling any exceptions
	 *
	 * @param runnable the operation to be executed
	 */
	public T executeWithExceptionHandling(Callable<T> runnable) {
		T result = null;
		try {
			result = runnable.call();
		} catch (InvalidCredentialsException e) {
			setException(e);
			if (apiCredentialsDelegate != null) {
				apiCredentialsDelegate.onInvalidCredentials(e);
				setCredentialsException(true);
			}
		} catch (APIException e) {
			setException(e);
		} catch (Exception e) {
			setException(e);
		}
		return result;
	}
}