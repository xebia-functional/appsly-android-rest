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

import android.net.Uri;
import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.utils.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jackson body converter that converts beans and individual props into path and query string params
 */
public class JacksonQueryParamsConverter implements QueryParamsConverter {

    private ObjectMapper mapper;

    public JacksonQueryParamsConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonQueryParamsConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String parsePathParams(String path, Map<Integer, String> pathParams, Object[] args) throws UnsupportedEncodingException {
        for (Map.Entry<Integer, String> pathEntry : pathParams.entrySet()) {
            Object paramVal = args[pathEntry.getKey()];
            if (paramVal == null) {
                throw new IllegalArgumentException(String.format("No null param values are allowed for key: [%s]", pathEntry.getValue()));
            }
            String value = Uri.encode(paramVal.toString());
            path = path.replaceAll("\\{(" + pathEntry.getValue() + ")\\}", value);
        }
        return path;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String parseBundledQueryParams(String path, Object object) throws UnsupportedEncodingException {
        if (object != null) {
            path = path.contains("?") ? path : path + "?";
            Map<String,Object> props = mapper.convertValue(object, Map.class);
            List<String> vals = new ArrayList<String>();
            for (Map.Entry<String, Object> queryParamEntry : props.entrySet()) {
                if (queryParamEntry.getValue() != null) {
                    String value = Uri.encode(queryParamEntry.getValue().toString());
                    vals.add(queryParamEntry.getKey() + "=" + value);
                }
            }
            path = path + StringUtils.join(vals.toArray(), "&");
        }
        return path;
    }

    @Override
    public String parseQueryParams(String path, Map<Integer, String> queryParams, Object[] args) throws UnsupportedEncodingException {
        if (queryParams.size() > 0) {
            path = path.contains("?") ? path : path + "?";
            List<String> vals = new ArrayList<String>();
            for (Map.Entry<Integer, String> queryParamEntry : queryParams.entrySet()) {
                Object paramVal = args[queryParamEntry.getKey()];
                if (paramVal != null) {
                    String value = Uri.encode(paramVal.toString());
                    vals.add(String.format("%s=%s", queryParamEntry.getValue(), value));
                }
            }
            path = path + StringUtils.join(vals.toArray(), "&");
        }
        return path;
    }

}
