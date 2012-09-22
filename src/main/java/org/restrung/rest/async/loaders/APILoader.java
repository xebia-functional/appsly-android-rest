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

package org.restrung.rest.async.loaders;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import org.restrung.rest.async.AsyncOperation;
import org.restrung.rest.client.APICredentialsDelegate;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.client.APIPostParams;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.JSONResponse;
import org.restrung.rest.utils.ContextUtils;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Abstract loader
 *
 * @param <T> an implementer of JSONResponse
 */
public abstract class APILoader<T extends JSONResponse> implements LoaderManager.LoaderCallbacks<T> {

	/**
	 * The loader manager asociated to this loader
	 */
	private LoaderManager loaderManager;

	/**
	 * The context associated to this loader
	 */
	private Context context;

	/**
	 * The operation delegate that contains info about this operation
	 */
	private AsyncOperation<T> delegate;

	/**
	 * Constructs a loader
	 *
	 * @param url                    the url
	 * @param apiDelegate            the API delegate
	 * @param apiCredentialsDelegate the api credentials delegate
	 * @param params                 a set of params to be replaced in the url
	 */
	public APILoader(String url, APIDelegate<T> apiDelegate, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
		super();
		this.context = apiDelegate.getRequestingContext();
		this.loaderManager = ContextUtils.getLoaderManager(context);
		this.delegate = new AsyncOperation<T>(url, apiDelegate, apiCredentialsDelegate, params);
	}

	/**
	 * Constructs a loader
	 *
	 * @param url                    the url
	 * @param file                   the file
	 * @param body                   the body
	 * @param apiDelegate            the API delegate
	 * @param delegateParams         the delegate params
	 * @param apiCredentialsDelegate the api credentials delegate
	 * @param params                 a set of params to be replaced in the url
	 */
	public APILoader(String url, JSONSerializable body, File file, APIDelegate<T> apiDelegate, APIPostParams delegateParams, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
		super();
		this.context = apiDelegate.getRequestingContext();
		this.loaderManager = ContextUtils.getLoaderManager(context);
		this.delegate = new AsyncOperation<T>(url, body, file, apiDelegate, delegateParams, apiCredentialsDelegate, params);
	}

	/**
	 * executes this loader
	 */
	public void execute() {
		try {
			//APIDelegate instances that override APIDelegate#getOperationId have a chance to reuse ids so underlying loaders are restarted
			int operationId = delegate.getApiDelegate().getOperationId();
			operationId = operationId != 0 ? operationId : hashCode();
			if (loaderManager.getLoader(operationId) == null) {
				loaderManager.initLoader(operationId, null, this);
			} else {
				loaderManager.restartLoader(operationId, null, this);
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public void onLoadFinished(Loader<T> loader, T data) {
		delegate.onPostExecute(data);
	}

	/**
	 * @see LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader loader) {
	}

	/**
	 * The async operation
	 *
	 * @return the async operation
	 */
	public AsyncOperation<T> getOperation() {
		return delegate;
	}

	/**
	 * @see LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<T> onCreateLoader(int id, Bundle args) {
		return new InternalAsyncLoader<T>(context, this);
	}

	/**
	 * To be implemented by subclasses that provide the actual operation to run in the background
	 *
	 * @return the actual callable that represents the operation to be run in the background
	 */
	public abstract Callable<T> getCallable();

	private static class InternalAsyncLoader<Z extends JSONResponse> extends AsynchronousLoader<Z> {

		private APILoader<Z> loader;

		public InternalAsyncLoader(Context context, APILoader<Z> api) {
			super(context);
			this.loader = api;
		}

		/**
		 * @see org.restrung.rest.async.loaders.AsynchronousLoader#loadInBackground()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Z loadInBackground() {
			try {
				Looper.prepare();
			} catch (RuntimeException e) {
				Log.w(getClass().getSimpleName(), "Looper.prepare(); exception: ", e);
			}
			return loader.delegate.executeWithExceptionHandling(new Callable<Z>() {
				@Override
				public Z call() throws Exception {
					return loader.getCallable().call();
				}
			});
		}
	}
}
