package ly.apps.android.rest.converters;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public interface QueryParamsConverter {

    String parsePathParams(String path, Map<Integer, String> pathParams, Object[] args) throws UnsupportedEncodingException;

    String parseBundledQueryParams(String path, Object object) throws UnsupportedEncodingException;

    String parseQueryParams(String path, Map<Integer, String> queryParams, Object[] args) throws UnsupportedEncodingException;

}
