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

package it.restrung.rest.marshalling.request;


import it.restrung.rest.utils.ClassUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for JSONSerializable instances that want the commodity to have their properties serialized to JSON
 */
public abstract class AbstractJSONRequest implements JSONSerializable {

    @Override
    public String toJSON() {
        JSONObject root = new JSONObject();
        for (Method method : ClassUtils.getAllGettersInHierarchy(getClass())) {
            String propertyName = ClassUtils.getJsonPropertyName(method);
            Object result;
            try {
                result = method.invoke(this, null);
                if (result != null) {
                    result = jsonValueFor(result);
                }
                root.put(propertyName, result);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return root.toString();
    }

    @SuppressWarnings("unchecked")
    private Object jsonValueFor(Object value) {
        Object result = null;
        if (value != null && !(value instanceof Class)) {
            result = value;
            try {
                Class<?> argType = value.getClass();
                if (Date.class.isAssignableFrom(argType)) {
                    result = ((Date) value).getTime() / 1000;
                } else if (JSONSerializable.class.isAssignableFrom(argType)) {
                    result = new JSONObject(((JSONSerializable) value).toJSON());
                } else if (Enum.class.isAssignableFrom(argType)) {
                    result = ((Enum) value).name();
                } else if (Map.class.isAssignableFrom(argType)) {
                    JSONObject nestedMap = new JSONObject();
                    Map<Object, Object> map = (Map<Object, Object>) value;
                    for (Map.Entry<?, Object> objectEntry : map.entrySet()) {
                        String key = objectEntry.getKey().toString();
                        nestedMap.put(key, jsonValueFor(objectEntry.getValue()));
                    }
                    result = nestedMap;
                } else if (List.class.isAssignableFrom(argType)) {
                    JSONArray resultArray = new JSONArray();
                    List<Object> results = (List<Object>) value;
                    for (Object item : results) {
                        if (JSONSerializable.class.isAssignableFrom(item.getClass())) {
                            JSONSerializable jsonSerializable = (JSONSerializable) item;
                            resultArray.put(new JSONObject(jsonSerializable.toJSON()));
                        } else {
                            resultArray.put(jsonValueFor(item));
                        }
                    }
                    result = resultArray;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


}
