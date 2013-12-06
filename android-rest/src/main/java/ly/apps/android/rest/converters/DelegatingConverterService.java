package ly.apps.android.rest.converters;


import org.apache.http.HttpEntity;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class DelegatingConverterService implements BodyConverter {

    private List<BodyConverter> converters = new LinkedList<BodyConverter>();

    public void addConverter(BodyConverter value) {
        converters.add(value);
    }

    public void removeConverter(BodyConverter value) {
        converters.remove(value);
    }

    @Override
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        return getConverter(contentType).toRequestBody(object, contentType);
    }

    @Override
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody) {
        return getConverter(contentType).fromResponseBody(target, contentType, responseBody);
    }

    public BodyConverter getConverter(String contentType) {
        BodyConverter found = null;
        for (BodyConverter converter : converters) {
            if(converter.supportsRequestContentType(contentType) ) {
                found = converter;
                break;
            }
        }
        if (found == null) {
            throw new UnsupportedOperationException("No converter found for content type: " + contentType);
        }
        return found;
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        boolean supported = false;
        for (BodyConverter converter : converters) {
            supported = converter.supportsRequestContentType(contentType);
            if (supported) {
                break;
            }
        }
        return supported;
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        boolean supported = false;
        for (BodyConverter converter : converters) {
            supported = converter.supportsResponseContentType(contentType);
            if (supported) {
                break;
            }
        }
        return supported;
    }
}
