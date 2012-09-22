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
import android.support.v4.content.AsyncTaskLoader;

/**
 * Abstract class for asynchronous loaders
 * @param <Result> the type of results
 */
public abstract class AsynchronousLoader<Result> extends AsyncTaskLoader<Result> {

	/**
	 * The results
	 */
	private Result result;

	/**
	 * Constructs an async loader associated to this context
	 * @param context
	 */
	public AsynchronousLoader(Context context) {
		super(context);
	}

	/**
	 * @see AsyncTaskLoader#deliverResult(Object)
	 */
	@Override
	public void deliverResult(Result data) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			return;
		}
		this.result = data;
		super.deliverResult(data);
	}

	/**
	 * @see android.support.v4.content.AsyncTaskLoader#onStartLoading()
	 */
	@Override
	protected void onStartLoading() {
		if (result != null) {
			deliverResult(result);
		}
		if (takeContentChanged() || result == null) {
			forceLoad();
		}
	}

	/**
	 * @see android.support.v4.content.AsyncTaskLoader#onStopLoading()
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * @see android.support.v4.content.AsyncTaskLoader#onReset()
	 */
	@Override
	protected void onReset() {
		super.onReset();
		// Ensure the loader is stopped
		onStopLoading();
		result = null;
	}
}
