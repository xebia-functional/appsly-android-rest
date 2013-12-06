package ly.apps.android.rest.converters.impl;

import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.utils.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonQueryParamsConverter implements QueryParamsConverter {

    private ObjectMapper mapper;

    public JacksonQueryParamsConverter() {
        this(new ObjectMapper());
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JacksonQueryParamsConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String parsePathParams(String path, Map<Integer, String> pathParams, Object[] args) throws UnsupportedEncodingException {
        for (Map.Entry<Integer, String> pathEntry : pathParams.entrySet()) {
            Object paramVal = args[pathEntry.getKey()];
            if (paramVal == null) {
                throw new IllegalArgumentException(String.format("No null param values are allowed for key: [%s]", pathEntry.getValue()));
            }
            String value = URLEncoder.encode(paramVal.toString(), "UTF-8");
            path = path.replaceAll("\\{(" + pathEntry.getValue() + ")\\}", value);
        }
        return path;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String parseBundledQueryParams(String path, Object object) throws UnsupportedEncodingException {
        if (object != null) {
            path = path.contains("?") ? path : path + "?";
            Map<String,Object> props = mapper.convertValue(object, Map.class);
            List<String> vals = new ArrayList<String>();
            for (Map.Entry<String, Object> queryParamEntry : props.entrySet()) {
                if (queryParamEntry.getValue() != null) {
                    String value = URLEncoder.encode(queryParamEntry.getValue().toString(), "UTF-8");
                    vals.add(queryParamEntry.getKey() + "=" + value);
                }
            }
            path = path + StringUtils.join(vals.toArray(), "&");
        }
        return path;
    }

    @Override
    public String parseQueryParams(String path, Map<Integer, String> queryParams, Object[] args) throws UnsupportedEncodingException {
        if (queryParams.size() > 0) {
            path = path.contains("?") ? path : path + "?";
            List<String> vals = new ArrayList<String>();
            for (Map.Entry<Integer, String> queryParamEntry : queryParams.entrySet()) {
                Object paramVal = args[queryParamEntry.getKey()];
                if (paramVal != null) {
                    String value = URLEncoder.encode(paramVal.toString(), "UTF-8");
                    vals.add(String.format("%s=%s", queryParamEntry.getValue(), value));
                }
            }
            path = path + StringUtils.join(vals.toArray(), "&");
        }
        return path;
    }

}
