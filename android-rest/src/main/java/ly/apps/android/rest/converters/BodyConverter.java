package ly.apps.android.rest.converters;

import ly.apps.android.rest.client.Callback;
import org.apache.http.HttpEntity;

import java.lang.reflect.Type;


public interface BodyConverter {

    <T> HttpEntity toRequestBody(T object, String contentType);

    <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, Callback<T> callback);

    boolean supportsRequestContentType(String contentType);

    boolean supportsResponseContentType(String contentType);

}
