package ly.apps.android.rest.converters;

import org.apache.http.HttpEntity;

import java.lang.reflect.Type;


public interface BodyConverter {

    <T> HttpEntity toRequestBody(T object, String contentType);

    <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody);

    boolean supportsRequestContentType(String contentType);

    boolean supportsResponseContentType(String contentType);

}
