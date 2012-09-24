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

package it.restrung.rest.client;


import it.restrung.rest.marshalling.response.JSONResponse;
import it.restrung.rest.marshalling.request.RequestOperation;
import it.restrung.rest.marshalling.response.ResponseOperation;
import it.restrung.rest.cache.CacheRequestInfoProvider;

/**
 * Delegate callbacks that allows preparing and handling remote API results from a REST service
 */
public interface APIDelegate<Result extends JSONResponse> extends CacheRequestInfoProvider<Result> {

    /**
     * Used when the operation is sent as a loader. The underlying loader will replace other loaders with the same id.
     *
     * @return the operation id which uniquely identifies this operation when compared to other operations.
     */
    int getOperationId();

    /**
     * Gives caller a chance to customize or query  certain aspects of a request operation e.g. headers
     *
     * @param operation the operation
     */
    void onRequest(RequestOperation operation);

    /**
     * Gives caller a chance to customize or query certain aspects of a response operation e.g. statusCode
     *
     * @param operation the operation
     */
    void onResponse(ResponseOperation operation);

    /**
     * Delegates serialized results back to the caller
     *
     * @param result the serialized results
     */
    void onResults(Result result);

    /**
     * Delegates optential erros back to the caller
     *
     * @param e the error or exception resulting from the operation
     */
    void onError(Throwable e);

    /**
     * Informs the system as to what the expected response type for this call is
     *
     * @return the class type for the expected response
     */
    Class<Result> getExpectedResponseType();

}
