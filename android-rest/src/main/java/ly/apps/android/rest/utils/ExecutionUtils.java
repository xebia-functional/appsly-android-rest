package ly.apps.android.rest.utils;

import android.os.AsyncTask;
import android.os.Build;

public class ExecutionUtils {

    private ExecutionUtils(){}

    public static <Params,Progress,Result> void execute(AsyncTask<Params,Progress,Result> asyncTask, Params ... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            asyncTask.execute(params);
        }
    }

}
