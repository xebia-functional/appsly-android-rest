package ly.apps.android.rest.converters;

import com.google.gson.Gson;
import ly.apps.android.rest.exceptions.SerializationException;
import ly.apps.android.rest.utils.HeaderUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;


public class GsonConverter implements BodyConverter {

    private final Gson gson;

    private String encoding;

    public GsonConverter() {
        this(new Gson());
    }

    public GsonConverter(Gson gson) {
        this(gson, "UTF-8");
    }

    public GsonConverter(Gson gson, String encoding) {
        this.gson = gson;
        this.encoding = encoding;
    }


    @Override
    public <T> HttpEntity toRequestBody(T object, String contentType) {
        try {
            return new StringEntity(gson.toJson(object), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T fromResponseBody(Type target, String contentType, HttpEntity responseBody) {
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(responseBody.getContent(), "UTF-8");
        } catch (IOException e) {
            throw new SerializationException(e);
        }
        return gson.fromJson(isr, target);
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
