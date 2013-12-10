package ly.apps.android.rest.converters.impl;

import ly.apps.android.rest.cache.CacheAwareCallback;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.exceptions.SerializationException;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JacksonHttpFormValuesConverter implements BodyConverter {

    private ObjectMapper mapper;

    public JacksonHttpFormValuesConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonHttpFormValuesConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        Logger.d("JacksonHttpFormValuesConverter.toRequestBody: object: " + object);
        try {
            Map<String,Object> props = mapper.convertValue(object, Map.class);
            List<NameValuePair> vals = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> queryParamEntry : props.entrySet()) {
                if (queryParamEntry.getValue() != null) {
                    vals.add(new BasicNameValuePair(queryParamEntry.getKey(), queryParamEntry.getValue().toString()));
                }
            }
            return new UrlEncodedFormEntity(vals);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody, CacheAwareCallback<T> callback) {
        throw new UnsupportedOperationException("JacksonHttpFormValuesConverter only serializes request objects for " + HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED);
    }

    @Override
    public boolean supportsRequestContentType(String contentType) {
        return HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED.startsWith(contentType);
    }

    @Override
    public boolean supportsResponseContentType(String contentType) {
        return false;
    }
}
