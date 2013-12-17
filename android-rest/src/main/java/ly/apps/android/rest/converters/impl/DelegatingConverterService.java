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

package ly.apps.android.rest.converters.impl;


import ly.apps.android.rest.cache.CacheAwareCallback;
import ly.apps.android.rest.converters.BodyConverter;
import org.apache.http.HttpEntity;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Converter chain that delegates conversion in order to all registered body converters
 */
public class DelegatingConverterService implements BodyConverter {

    private List<BodyConverter> converters = new LinkedList<BodyConverter>();

    public void addConverter(BodyConverter value) {
        converters.add(value);
    }

    public void removeConverter(BodyConverter value) {
        converters.remove(value);
    }

    @Override
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        return getConverter(contentType).toRequestBody(object, contentType);
    }

    @Override
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, CacheAwareCallback<T> callback) {
        return getConverter(contentType).fromResponseBody(target, contentType, responseBody, callback);
    }

    public BodyConverter getConverter(String contentType) {
        BodyConverter found = null;
        for (BodyConverter converter : converters) {
            if(converter.supportsRequestContentType(contentType) ) {
                found = converter;
                break;
            }
        }
        if (found == null) {
            throw new UnsupportedOperationException("No converter found for content type: " + contentType);
        }
        return found;
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        boolean supported = false;
        for (BodyConverter converter : converters) {
            supported = converter.supportsRequestContentType(contentType);
            if (supported) {
                break;
            }
        }
        return supported;
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        boolean supported = false;
        for (BodyConverter converter : converters) {
            supported = converter.supportsResponseContentType(contentType);
            if (supported) {
                break;
            }
        }
        return supported;
    }
}
