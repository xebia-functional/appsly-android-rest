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

package it.restrung.rest.async.asynctasks;

import it.restrung.rest.async.AsyncOperation;
import it.restrung.rest.client.APICredentialsDelegate;
import it.restrung.rest.client.APIDelegate;
import it.restrung.rest.client.APIPostParams;
import it.restrung.rest.client.RestClientFactory;
import it.restrung.rest.marshalling.request.JSONSerializable;
import it.restrung.rest.marshalling.response.JSONResponse;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * AsyncTask for POST requests
 *
 * @param <T> an implementer of JSONResponse
 */
public class APIPostAsyncTask<T extends JSONResponse> extends APIAsyncTask<T> {

    /**
     * Constructs a multipart APIPostAsyncTask that posts a file and an object
     *
     * @param url                    the service endpoint
     * @param body                   the json body to be sent as post
     * @param file                   an optional file to be posted in the multipart
     * @param delegate               the delegate that will be notified on successful requests
     * @param apiCredentialsDelegate a credentials delegate
     * @param params                 the params to be replaced on the url placeholders
     */
    public APIPostAsyncTask(String url, JSONSerializable body, File file, APIDelegate<T> delegate, APIPostParams delegateParams, APICredentialsDelegate apiCredentialsDelegate, Object... params) {
        super(url, body, file, delegate, delegateParams, apiCredentialsDelegate, params);
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Object[])
     */
    @Override
    protected T doInBackground(String... args) {
        return getOperation().executeWithExceptionHandling(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return RestClientFactory.getClient().post(getOperation().getApiDelegate(), getOperation().getUrl(), getOperation().getBody(), getOperation().getFile(), AsyncOperation.DEFAULT_REQUEST_TIMEOUT, getOperation().getPostDelegateParams());
            }
        });
    }
}
