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
