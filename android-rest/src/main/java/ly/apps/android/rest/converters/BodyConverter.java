/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * http://apps.ly
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

package ly.apps.android.rest.converters;

import ly.apps.android.rest.cache.CacheAwareCallback;
import ly.apps.android.rest.client.Callback;
import org.apache.http.HttpEntity;

import java.lang.reflect.Type;

/**
 * Determines how objects and responses are transformed into each other
 */
public interface BodyConverter {

    <T> HttpEntity toRequestBody(T object, String contentType);

    <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, CacheAwareCallback<T> callback);

    boolean supportsRequestContentType(String contentType);

    boolean supportsResponseContentType(String contentType);

}
