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

package ly.apps.android.rest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ly.apps.android.rest.client.annotations.DELETE;
import ly.apps.android.rest.client.annotations.GET;
import ly.apps.android.rest.client.annotations.HEAD;
import ly.apps.android.rest.client.annotations.PATCH;
import ly.apps.android.rest.client.annotations.POST;
import ly.apps.android.rest.client.annotations.PUT;
import ly.apps.android.rest.client.annotations.RestService;

/**
 * Creates proxy instances for any interface annotated with @see RestService given a base url and a configured underlying RestClient.
 * The RestClient know about how to serialize responses and apply cache policies
 */
public class RestServiceFactory {

    private static final ConcurrentMap<String, Object> serviceCaches = new ConcurrentHashMap<String, Object>();
    private static final ConcurrentMap<Method, RestMethodCache> methodCaches = new ConcurrentHashMap<Method, RestMethodCache>();

    @SuppressWarnings("unchecked")
    public static <T> T getService(String baseUrl, Class<T> serviceClass, RestClient restClient) {
        T service;
        if (serviceClass.isAnnotationPresent(RestService.class)) {
            String key = serviceClass.getName();
            service = (T) serviceCaches.get(serviceClass.getName());
            if (service == null) {
                service = (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new RestInvocationHandler(baseUrl));
                T found = (T) serviceCaches.putIfAbsent(key, service);
                if (found != null) {
                    service = found;
                }
                initializeMethodCaches(serviceClass, restClient);
            }
        } else {
            throw new IllegalArgumentException(serviceClass + " is not annotated with @RestService");
        }
        return service;
    }

    public static RestMethodCache getMethodCache(Method method) {
        return methodCaches.get(method);
    }

    private static void initializeMethodCaches(Class<?> serviceClass, RestClient restClient) {
        for (Method method : serviceClass.getMethods()) {
            if (method.isAnnotationPresent(GET.class) ||
                    method.isAnnotationPresent(POST.class) ||
                    method.isAnnotationPresent(PUT.class) ||
                    method.isAnnotationPresent(DELETE.class) ||
                    method.isAnnotationPresent(PATCH.class) ||
                    method.isAnnotationPresent(HEAD.class)) {
                methodCaches.putIfAbsent(method, new RestMethodCache(method, restClient));
            }
        }
    }

    /**
     * Intercepts all calls to the the RestService Impl
     */
    @SuppressWarnings("unchecked")
    private static class RestInvocationHandler implements InvocationHandler {

        private String baseUrl;

        private RestInvocationHandler(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RestMethodCache methodCache = getMethodCache(method);
            if (methodCache != null) {
                Object value = args[args.length - 1];
                if (!Callback.class.isAssignableFrom(value.getClass())) {
                    throw new IllegalArgumentException("Missing APIDelegate as last parameter");
                }
                methodCache.invoke(baseUrl, (Callback) value, args);
                return null;
            } else {
                return method.invoke(this, args);
            }
        }

    }

}
