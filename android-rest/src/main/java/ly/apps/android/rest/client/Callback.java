package ly.apps.android.rest.client;

import android.content.Context;
import android.os.AsyncTask;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import ly.apps.android.rest.cache.CacheAwareCallback;
import ly.apps.android.rest.cache.CacheInfo;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.Logger;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.util.Collection;


public abstract class Callback<Result> extends CacheAwareCallback<Result> {

    protected Callback(CacheInfo cacheInfo) {
        super(cacheInfo);
    }

    protected Callback(Context context, Class<Result> targetClass) {
        super(context, targetClass);
    }

    protected Callback(Class<Result> targetClass) {
        super(targetClass);
    }

    protected Callback() {
    }

    protected Callback(Context context) {
        super(context);
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
