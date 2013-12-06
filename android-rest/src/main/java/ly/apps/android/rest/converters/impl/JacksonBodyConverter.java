package ly.apps.android.rest.converters.impl;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.exceptions.SerializationException;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;


public class JacksonBodyConverter implements BodyConverter {

    private ObjectMapper mapper;

    public JacksonBodyConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonBodyConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }



    @Override
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        Logger.d("JacksonBodyConverter.toRequestBody: object: " + object);
        try {
            HttpEntity result = new StringEntity(mapper.writeValueAsString(object), "UTF-8");
            Logger.d("JacksonBodyConverter.toRequestBody: result: " + result);
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(e);
        } catch (JsonMappingException e) {
            throw new SerializationException(e);
        } catch (JsonGenerationException e) {
            throw new SerializationException(e);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, Callback<T> callback) {
        Logger.d("JacksonBodyConverter.fromResponseBody: target: " + target + " responseBody: " + responseBody);
        try {
            T result;
            if (callback.isResponseIsCollection()) {
                result = mapper.readValue(responseBody.getContent(), mapper.getTypeFactory().constructCollectionType(callback.getCollectionType(), (Class<T>) target));
            } else {
                result = mapper.readValue(responseBody.getContent(), (Class<T>) target);
            }
            Logger.d("JacksonBodyConverter.fromResponseBody: result: " + result);
            return result;
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        return HeaderUtils.CONTENT_TYPE_JSON.startsWith(contentType);
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        return HeaderUtils.CONTENT_TYPE_JSON.startsWith(contentType);
    }
}
