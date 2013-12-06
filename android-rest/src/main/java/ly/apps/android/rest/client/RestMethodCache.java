package ly.apps.android.rest.client;

import android.text.TextUtils;

import ly.apps.android.rest.client.annotations.Body;
import ly.apps.android.rest.client.annotations.DELETE;
import ly.apps.android.rest.client.annotations.FormField;
import ly.apps.android.rest.client.annotations.GET;
import ly.apps.android.rest.client.annotations.HEAD;
import ly.apps.android.rest.client.annotations.Header;
import ly.apps.android.rest.client.annotations.PATCH;
import ly.apps.android.rest.client.annotations.POST;
import ly.apps.android.rest.client.annotations.PUT;
import ly.apps.android.rest.client.annotations.Path;
import ly.apps.android.rest.client.annotations.QueryParam;
import ly.apps.android.rest.utils.ResponseTypeUtil;
import ly.apps.android.rest.utils.StringUtils;

import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RestMethodCache {

    public RestMethodCache(Method method, RestClient restClient) {
        this.method = method;
        this.restClient = restClient;
        parseMethod();
        parseParameters();
    }

    enum RequestType {
        GET, POST, PUT, PATCH, DELETE, HEAD
    }

    private Method method;

    private String path;

    private Map<Integer, String> pathParams = new HashMap<Integer, String>();

    private Map<Integer, String> queryParams = new HashMap<Integer, String>();

    private Map<Integer, String> formFields = new HashMap<Integer, String>();

    private Map<Integer, String> headers = new HashMap<Integer, String>();

    private int bodyPosition;

    private boolean bodyPresent;

    private RequestType requestType;

    private Type targetClass;

    private RestClient restClient;

    public Type getTargetClass() {
        return targetClass;
    }

    @SuppressWarnings("unchecked")
    private void parseMethod() {
        targetClass = ResponseTypeUtil.parseResponseType(method);
        if (method.isAnnotationPresent(GET.class)) {
            requestType = RequestType.GET;
            path = method.getAnnotation(GET.class).value();
        } else if (method.isAnnotationPresent(POST.class)) {
            requestType = RequestType.POST;
            path = method.getAnnotation(POST.class).value();
        } else if (method.isAnnotationPresent(PUT.class)) {
            requestType = RequestType.PUT;
            path = method.getAnnotation(PUT.class).value();
        } else if (method.isAnnotationPresent(PATCH.class)) {
            requestType = RequestType.PATCH;
            path = method.getAnnotation(PATCH.class).value();
        } else if (method.isAnnotationPresent(DELETE.class)) {
            requestType = RequestType.DELETE;
            path = method.getAnnotation(DELETE.class).value();
        } else if (method.isAnnotationPresent(HEAD.class)) {
            requestType = RequestType.HEAD;
            path = method.getAnnotation(HEAD.class).value();
        } else {
            throw new IllegalStateException("No http annotation (GET, POST, PUT, DELETE, PATCH, HEAD) found on method" + method);
        }
    }

    private void parseParameters() {
        Annotation[][] parameterAnnotationArrays = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotationArrays.length; i++) {
            Annotation[] parameterAnnotations = parameterAnnotationArrays[i];
            if (parameterAnnotations != null) {
                for (Annotation parameterAnnotation : parameterAnnotations) {
                    Class<? extends Annotation> annotationType = parameterAnnotation.annotationType();
                    if (annotationType == Path.class) {
                        pathParams.put(i,((Path) parameterAnnotation).value());
                    } else if (annotationType == QueryParam.class) {
                        queryParams.put(i,((QueryParam) parameterAnnotation).value());
                    } else if (annotationType == Header.class) {
                        headers.put(i,((Header) parameterAnnotation).value());
                    } else if (annotationType == Body.class) {
                        bodyPresent = true;
                        bodyPosition = i;
                    } else if (annotationType == FormField.class) {
                        formFields.put(i,((FormField) parameterAnnotation).value());
                    }
                }
            }
        }
    }

    public String parseArgs(String baseUrl, Object[] args) throws UnsupportedEncodingException {
        String resultingPath = path;
        for (Map.Entry<Integer, String> pathEntry : pathParams.entrySet()) {
            String value = URLEncoder.encode(args[pathEntry.getKey()].toString(), "UTF-8");
            resultingPath = resultingPath.replaceAll("\\{(" + value + ")\\}", value);
        }
        if (queryParams.size() > 0) {
            resultingPath = resultingPath + "?";
            String[] queryParamsArray = new String[queryParams.size()];
            int i = 0;
            for (Map.Entry<Integer, String> queryParamEntry : queryParams.entrySet()) {
                String value = URLEncoder.encode(args[queryParamEntry.getKey()].toString(), "UTF-8");
                queryParamsArray[i] = String.format("%s=%s", queryParamEntry.getValue(), value);
                i++;
            }
            resultingPath = resultingPath + StringUtils.join(queryParamsArray, "&");
        }
        return baseUrl + resultingPath;
    }

    @SuppressWarnings("unchecked")
    public <T> void invoke(String baseUrl, Callback<T> delegate, Object[] args) throws UnsupportedEncodingException {
        prepareDelegate(delegate, args);
        String url = parseArgs(baseUrl, args);
        Object body = null;
        if (bodyPresent) {
            body = args[bodyPosition];
        }
        switch (requestType) {
            case GET:
                restClient.get(url, delegate);
                break;
            case POST:
                restClient.post(url, body, delegate);
                break;
            case PUT:
                restClient.put(url, body, delegate);
                break;
            case PATCH:
                throw new UnsupportedOperationException("PATCH not yet supported by async http client");
            case DELETE:
                restClient.delete(url, delegate);
                break;
            case HEAD:
                restClient.head(url, delegate);
                break;
        }
    }

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @SuppressWarnings("unchecked")
    private <T> void prepareDelegate(Callback<T> delegate, Object[] args) {
        delegate.setTargetClass((Class<T>) targetClass);
        if (headers.size() > 0) {
            org.apache.http.Header[] headersArray = new org.apache.http.Header[headers.size()];
            int i = 0;
            for (Map.Entry<Integer, String> headerEntry : headers.entrySet()) {
                String value = args[headerEntry.getKey()].toString();
                headersArray[i] = new BasicHeader(headerEntry.getValue(), value);
                i++;
            }
            delegate.setAdditionalHeaders(headersArray);
        }
    }

}
