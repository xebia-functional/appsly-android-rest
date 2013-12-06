package ly.apps.android.rest.client;

import android.content.Context;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.util.Collection;


public abstract class Callback<Result> extends BaseJsonHttpResponseHandler<Result> {

    private Context context;

    private Class<Result> targetClass;

    private BodyConverter bodyConverter;

    private Header[] additionalHeaders;

    private String requestContentType = HeaderUtils.CONTENT_TYPE_JSON;

    private boolean responseIsCollection;

    private Class<? extends Collection> collectionType;

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

    public boolean isResponseIsCollection() {
        return responseIsCollection;
    }

    public void setResponseIsCollection(boolean isList) {
        this.responseIsCollection = isList;
    }

    public Class<? extends Collection> getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Class<? extends Collection> collectionType) {
        this.collectionType = collectionType;
    }

    public abstract void onResponse(Response<Result> response);

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawResponse, Result response) {
        Logger.d("onSuccess: status" + statusCode + " rawResponse: " + rawResponse);
        Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawResponse, response);
        onResponse(httpResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, String rawData, Result errorResponse) {
        Logger.d("onFailure: status" + statusCode + " rawResponse: " + rawData);
        Response<Result> httpResponse = new Response<Result>(statusCode, headers, rawData, errorResponse, e);
        onResponse(httpResponse);
    }

    @Override
    protected Result parseResponse(String responseBody) throws Throwable {
        Logger.d("parsingResponse: intoTargetClass: " + targetClass + "responseBody: " + responseBody);
        return bodyConverter.fromResponseBody(targetClass, HeaderUtils.CONTENT_TYPE_JSON,new StringEntity(responseBody, "UTF-8"), this);
    }

    public Callback(String encoding) {
        super(encoding);
    }

    @Override
    public void onStart() {
        Logger.d("Callback.onStart");
        super.onStart();
    }

    @Override
    public void onFinish() {
        Logger.d("Callback.onFinish");
        super.onFinish();
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        Logger.d("Callback.onProgress");
        super.onProgress(bytesWritten, totalSize);
    }

    @Override
    public void onRetry() {
        Logger.d("Callback.onRetry");
        super.onRetry();
    }
}
