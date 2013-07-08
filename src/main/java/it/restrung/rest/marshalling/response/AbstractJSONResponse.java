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

package it.restrung.rest.marshalling.response;


import it.restrung.rest.annotations.JsonProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Convenience abstract class to be implemented by objects that can be deserialized from remote response
 * This class ues reflection to invoke setter from json properties
 */
public abstract class AbstractJSONResponse implements JSONResponse {

    /**
     * The json delegate constructed out of the response
     */
    protected transient JSONObject delegate;

    /**
     * A cache map of already serialized object properties
     */
    protected transient Map<String, Object> propertyMap = new HashMap<String, Object>();


    /**
     * @see JSONResponse#fromJSON(org.json.JSONObject)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void fromJSON(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            this.delegate = jsonObject;
            Method[] methods = getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set") && method.getName().length() > 3) {
                    Class argType = method.getParameterTypes()[0];

                    String propertyName = method.getName().substring(3);
                    propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);

                    try {
                        Field foundField = getClass().getDeclaredField(propertyName);
                        if (foundField.isAnnotationPresent(JsonProperty.class)) {
                            propertyName = foundField.getAnnotation(JsonProperty.class).value();
                        }
                    } catch (NoSuchFieldException e) {
                        //todo log errors when field names mismatch their setter
                    }

                    Object result = null;
                    if (String.class.isAssignableFrom(argType)) {
                        result = getString(propertyName);
                    } else if (Boolean.class.isAssignableFrom(argType) || boolean.class.isAssignableFrom(argType)) {
                        result = getBoolean(propertyName);
                    } else if (Double.class.isAssignableFrom(argType) || double.class.isAssignableFrom(argType)) {
                        result = getDouble(propertyName);
                    } else if (Long.class.isAssignableFrom(argType) || long.class.isAssignableFrom(argType)) {
                        result = getLong(propertyName);
                    } else if (Integer.class.isAssignableFrom(argType) || int.class.isAssignableFrom(argType)) {
                        result = getInt(propertyName);
                    } else if (Date.class.isAssignableFrom(argType)) {
                        result = getDate(propertyName);
                    } else if (JSONResponse.class.isAssignableFrom(argType)) {
                        result = getObject(propertyName, argType);
                    } else if (Enum.class.isAssignableFrom(argType)) {
                        String value = getString(propertyName);
                        if (value != null) {
                            result = Enum.valueOf((Class<Enum>) argType, getString(propertyName));
                        }
                    } else if (List.class.isAssignableFrom(argType)) {
                        Class typeArg = (Class) ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];
                        if (JSONResponse.class.isAssignableFrom(typeArg)) {
                            result = getList(propertyName, typeArg);
                        } else {
                            result = getElementCollection(propertyName);
                        }
                    } else if (Map.class.isAssignableFrom(argType)) {
                        Class typeArg = (Class) ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];
                        if (JSONResponse.class.isAssignableFrom(typeArg)) {
                            result = getMap(propertyName, typeArg);
                        } else {
                            result = getElementMap(propertyName);
                        }
                    } else {
                        throw new UnsupportedOperationException(String.format("%s is of type: %s which is not yet supported by the AbstractJSONResponse serialization", propertyName, argType));
                    }
                    try {
                        method.invoke(this, result);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * Gets a list property deserialized values from the cache or from the underlying JSONObject
     *
     * @param property  the property name
     * @param typeClass the type of JSONResponse contained in the array
     * @return the list of objects associated to this property in the JSON response
     */
    @SuppressWarnings("unchecked")
    private List<?> getList(String property, Class<?> typeClass) {
        List<Object> list = null;
        if (!propertyMap.containsKey(property)) {
            JSONArray array = delegate.optJSONArray(property);
            if (array != null) {
                list = new ArrayList<Object>(array.length());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.optJSONObject(i);
                    try {
                        Object response = typeClass.newInstance();
                        ((JSONResponse) response).fromJSON(jsonObject);
                        list.add(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
                propertyMap.put(property, list);
            }
        } else {
            list = (List<Object>) propertyMap.get(property);
        }
        return list;
    }

    /**
     * Gets a map property deserialized values from the cache or from the underlying JSONObject
     *
     * @param property  the property name
     * @param typeClass the type of JSONResponse contained in the property
     * @return the map of json response style objects associated to this property in the JSON response
     */
    @SuppressWarnings("unchecked")
    private Map<String, ?> getMap(String property, Class<?> typeClass) {
        Map<String, Object> map = null;
        if (!propertyMap.containsKey(property)) {
            JSONObject jsonMap = delegate.optJSONObject(property);
            if (jsonMap != null) {
                map = new LinkedHashMap<String, Object>(jsonMap.length());

                while (jsonMap.keys().hasNext()) {
                    String key = (String) jsonMap.keys().next();
                    JSONObject jsonObject = jsonMap.optJSONObject(key);
                    try {
                        Object response = typeClass.newInstance();
                        ((JSONResponse) response).fromJSON(jsonObject);
                        map.put(key, response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
                propertyMap.put(property, map);
            }
        } else {
            map = (Map<String, Object>) propertyMap.get(property);
        }
        return map;
    }

    /**
     * Gets a list property deserialized values from the cache or from the underlying JSONObject
     *
     * @param property the property name
     * @return the list of primitive or simple supported objects associated to this property in the JSON response
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getElementCollection(String property) {
        List<T> list = null;
        if (!propertyMap.containsKey(property)) {
            JSONArray jsonArray = delegate.optJSONArray(property);
            if (jsonArray != null) {
                list = new ArrayList<T>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    T item = (T) jsonArray.opt(i);
                    list.add(item);
                }
            }
            propertyMap.put(property, list);
        } else {
            list = (List<T>) propertyMap.get(property);
        }
        return list;
    }

    /**
     * Gets a map property deserialized values from the cache or from the underlying JSONObject
     *
     * @param property the property name
     * @return the map containining the keys as string and as values the primitive or simple supported objects associated to this property in the JSON response
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getElementMap(String property) {
        Map<String, Object> map = null;
        if (!propertyMap.containsKey(property)) {
            JSONObject jsonObject = delegate.optJSONObject(property);
            if (jsonObject != null) {
                map = new LinkedHashMap<String, Object>(jsonObject.length());
                while (jsonObject.keys().hasNext()) {
                    String key = (String) jsonObject.keys().next();
                    map.put(key, jsonObject.opt(key));
                }
            }
            propertyMap.put(property, map);
        } else {
            map = (Map<String, Object>) propertyMap.get(property);
        }
        return map;
    }

    /**
     * Gets a property deserialized value from the cache or from the underlying JSONObject
     *
     * @param property  the property name
     * @param typeClass the type of JSONResponse contained in the property
     */
    @SuppressWarnings("unchecked")
    private Object getObject(String property, Class<?> typeClass) {
        Object object = null;
        if (!propertyMap.containsKey(property)) {
            JSONObject jsonObject = delegate.optJSONObject(property);
            if (jsonObject != null) {
                try {
                    object = typeClass.newInstance();
                    ((JSONResponse) object).fromJSON(jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }
            propertyMap.put(property, object);
        } else {
            object = propertyMap.get(property);
        }
        return object;
    }

    /**
     * Gets a date property deserialized value from the cache or from the underlying JSONObject
     * This method assumes the property is in a timestamp long format with seconds precision
     *
     * @param property the property name
     */
    private Date getDate(String property) {
        Long timestamp = delegate.optLong(property);
        return timestamp != null ? new Date(timestamp * 1000) : null;
    }

    /**
     * Gets a string property deserialized value from the cache or from the underlying JSONObject
     * This method assumes that "null" actually means null
     *
     * @param property the property name
     */
    private String getString(String property) {
        String value = delegate.optString(property, null);
        return "null".equals(value) ? null : value;
    }

    /**
     * Gets a double property deserialized value from the cache or from the underlying JSONObject
     * This method defaults to 0.0 if the property is null
     *
     * @param property the property name
     */
    private double getDouble(String property) {
        return delegate.optDouble(property, 0.0);
    }

    /**
     * Gets a long property deserialized value from the cache or from the underlying JSONObject
     * This method defaults to 0 if the property is null
     *
     * @param property the property name
     */
    private long getLong(String property) {
        return delegate.optLong(property, 0);
    }

    /**
     * Gets an int property deserialized value from the cache or from the underlying JSONObject
     * This method defaults to 0 if the property is null
     *
     * @param property the property name
     */
    private int getInt(String property) {
        return delegate.optInt(property, 0);
    }

    /**
     * Gets a boolean property deserialized value from the cache or from the underlying JSONObject
     * This method defaults to false if the property is null
     *
     * @param property the property name
     */
    private boolean getBoolean(String property) {
        return delegate.optBoolean(property, false);
    }


}
