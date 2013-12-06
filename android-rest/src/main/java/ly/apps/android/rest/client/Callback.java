package ly.apps.android.rest.client;

import android.content.Context;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.utils.HeaderUtils;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;


public abstract class Callback<Result> extends BaseJsonHttpResponseHandler<Result> {

    private Context context;

    private Class<Result> targetClass;

    private BodyConverter bodyConverter;

    private Header[] additionalHeaders;

    private String requestContentType = HeaderUtils.JSON_CONTENT_TYPE;

    protected Callback() {
    }

    protected Callback(Context context) {
        this();
        this.context = context;
    }

    protected Callback(Context context, Class<Result> targetClass) {
        this.context = context;
        this.targetClass = targetClass;
    }

    protected Callback(Class<Result> targetClass) {
        this.targetClass = targetClass;
    }

    public Context getContext() {
        return context;
    }

    public void setTargetClass(Class<Result> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<Result> getTargetClass() {
        return targetClass;
    }

    public BodyConverter getBodyConverter() {
        return bodyConverter;
    }

    public void setBodyConverter(BodyConverter bodyConverter) {
        this.bodyConverter = bodyConverter;
    }

    public Header[] getAdditionalHeaders() {
        return additionalHeaders;
    }

    public void setAdditionalHeaders(Header[] additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public abstract void onResponse(Response<Result> response);

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawResponse, Result response) {
        Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawResponse, response);
        onResponse(httpResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, String rawData, Result errorResponse) {
        Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawData, errorResponse, e);
        onResponse(httpResponse);
    }

    @Override
    protected Result parseResponse(String responseBody) throws Throwable {
        return bodyConverter.fromResponseBody(targetClass, HeaderUtils.JSON_CONTENT_TYPE ,new StringEntity(responseBody, "UTF-8"));
    }
}
