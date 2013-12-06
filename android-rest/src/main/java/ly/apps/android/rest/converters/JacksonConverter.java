package ly.apps.android.rest.converters;

import ly.apps.android.rest.exceptions.SerializationException;
import ly.apps.android.rest.utils.HeaderUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;


public class JacksonConverter implements BodyConverter {

    private ObjectMapper mapper;

    public JacksonConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }



    @Override
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        try {
            return new StringEntity(mapper.writeValueAsString(object), "UTF-8");
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
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody) {
        InputStreamReader isr;
        try {
            return mapper.readValue(responseBody.getContent(), (Class<T>) target);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        return HeaderUtils.JSON_CONTENT_TYPE.startsWith(contentType);
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        return HeaderUtils.JSON_CONTENT_TYPE.startsWith(contentType);
    }
}
