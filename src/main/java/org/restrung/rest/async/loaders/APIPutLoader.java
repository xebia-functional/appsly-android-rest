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

import org.restrung.rest.async.AsyncOperation;
import org.restrung.rest.client.APICredentialsDelegate;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.client.RestClientFactory;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.JSONResponse;

import java.util.concurrent.Callable;

public class APIPutLoader<T extends JSONResponse> extends APILoader<T> {


    /**
     *
	 * @param delegate the delegate that will be notified on successful requests
	 * @param url the service endpoint
	 * @param body the json body to be sent as post
	 * @param params the params to be replaced on the url placeholders
	 */
    public APIPutLoader(APIDelegate<T> delegate, APICredentialsDelegate apiCredentialsDelegate, String url, JSONSerializable body, Object... params) {
        super(delegate, apiCredentialsDelegate, url, body, null, null, params);
    }

	@Override
	public Callable<T> getCallable() {
		return new Callable<T>() {
			@Override
			public T call() throws Exception {
				return getOperation().executeWithExceptionHandling(new Callable<T>() {
					@Override
					public T call() throws Exception {
						return RestClientFactory.getClient().put(getOperation().getApiDelegate(), getOperation().getUrl(), getOperation().getBody(), AsyncOperation.DEFAULT_REQUEST_TIMEOUT);
					}
				});
			}
		};
	}

}
