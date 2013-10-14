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


import it.restrung.rest.annotations.JsonProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for JSONSerializable instance that relies upon Google's gson library to auto serialize beans to
 * a json string
 */
public abstract class AbstractJSONRequest implements JSONSerializable {

    /**
     * Register dates to be serialized as timestamps with seconds precision
     */
//    static {
//        gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
//            @Override
//            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
//                return date == null ? null : new JsonPrimitive(date.getTime() / 1000);
//            }
//        });
//    }

    /**
     * @see JSONSerializable#toJSON()
     */

//    public String toJSON() {
//        JSONObject root = new JSONObject();
//        root.put()
//        return root.toJSONString();
//    }
    @Override
    public String toJSON() {
        JSONObject root = new JSONObject();
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if ((method.getName().startsWith("get") || method.getName().startsWith("is")) && method.getName().length() > 3) {
                String propertyName = method.getName().substring(3);
                if (propertyName.equals("class")) {
                    continue;
                }
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                try {
                    Field foundField = getClass().getField(propertyName);
                    if (foundField.isAnnotationPresent(JsonProperty.class)) {
                        propertyName = foundField.getAnnotation(JsonProperty.class).value();
                    }
                } catch (NoSuchFieldException e) {
                    //todo log errors when field names mismatch their setter
                }

                Object result = null;
                try {
                    result = method.invoke(this, null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                if (result != null) {
                    result = jsonValueFor(result);
                }
                try {
                    root.put(propertyName, result);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
