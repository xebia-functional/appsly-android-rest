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

package org.restrung.rest.cache;

import android.content.Context;

/**
 * Exposes behavior to obtain cache information
 */
public interface CacheRequestInfoProvider<Result> {

	/**
	 *
	 * @return the context initiating and handling the results
	 */
	Context getRequestingContext();

	/**
	 *
	 * @return the policy utilized for considering cached results
	 */
	RequestCache.LoadPolicy getCacheLoadPolicy();

	/**
	 *
	 * @return the policy to utilize when storing cached results resulting from this request
	 */
	RequestCache.StoragePolicy getCacheStoragePolicy();

	/**
	 * Sets the context initiating and handling the results
	 * @param context the context
	 */
	void setRequestingContext(Context context);

	/**
	 * Sets the policy for loading
	 * @param cacheLoadPolicy the policy utilized for considering cached results
	 */
	void setCacheLoadPolicy(RequestCache.LoadPolicy cacheLoadPolicy);

	/**
	 * Sets the policy for storing
	 * @param cacheStoragePolicy the policy to utilize when storing cached results resulting from this request
	 */
	void setCacheStoragePolicy(RequestCache.StoragePolicy cacheStoragePolicy);

	/**
	 * Sets the cache info for this request
	 * @param cacheInfo the cache info object
	 */
	void setCacheInfo(CacheInfo cacheInfo);

}
