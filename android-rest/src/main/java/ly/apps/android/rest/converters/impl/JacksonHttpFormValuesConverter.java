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
import ly.apps.android.rest.exceptions.SerializationException;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Jackson body converter that converts beans into form data variables
 */
public class JacksonHttpFormValuesConverter implements BodyConverter {

    private ObjectMapper mapper;

    public JacksonHttpFormValuesConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(
                new SimpleModule("PassThruFileProperties",
                        new Version(1, 0, 0, null)) {{
                    addSerializer(FileFormField.class, new JsonSerializer<FileFormField>() {
                        @Override
                        public void serialize(FileFormField file, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                            jsonGenerator.writeObject(file);
                        }
                    });
                    addDeserializer(FileFormField.class, new JsonDeserializer<FileFormField>() {
                        @Override
                        public FileFormField deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                            return (FileFormField) jsonParser.getInputSource();
                        }
                    });
                }}
        );
    }

    public JacksonHttpFormValuesConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        Logger.d("JacksonHttpFormValuesConverter.toRequestBody: object: " + object);
        try {
            HttpEntity entity = null;
            List<NameValuePair> vals = new ArrayList<NameValuePair>();
            if (HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED.startsWith(contentType)) {
                Map<String,Object> props = mapper.convertValue(object, Map.class);
                for (Map.Entry<String, Object> formPartEntry : props.entrySet()) {
                    if (formPartEntry.getValue() != null) {
                        vals.add(new BasicNameValuePair(formPartEntry.getKey(), formPartEntry.getValue().toString()));
                    }
                }
                entity =  new UrlEncodedFormEntity(vals);
            } else if (HeaderUtils.CONTENT_TYPE_MULTIPART_FORM_DATA.startsWith(contentType)) {
                Map<String,Object> props = mapper.convertValue(object, Map.class);
                MultipartEntity multipartEntity = new MultipartEntity(null);
                for (Map.Entry<String, Object> formPartEntry : props.entrySet()) {
                    if (formPartEntry.getValue() != null) {
                        if (formPartEntry.getValue() instanceof FileFormField) {
                            FileFormField fileFormField = (FileFormField) formPartEntry.getValue();
                            multipartEntity.addPart(formPartEntry.getKey(), fileFormField.getFile(), fileFormField.getContentType());
                        } else {
                            multipartEntity.addPart(formPartEntry.getKey(), formPartEntry.getValue().toString());
                        }
                    }
                }
                entity = multipartEntity;
            }
            return entity;
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, CacheAwareCallback<T> callback) {
        throw new UnsupportedOperationException("JacksonHttpFormValuesConverter only serializes request objects for " + HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED);
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        return HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED.startsWith(contentType) || HeaderUtils.CONTENT_TYPE_MULTIPART_FORM_DATA.startsWith(contentType) ;
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        return false;
    }
}
