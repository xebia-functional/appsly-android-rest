package ly.apps.android.rest.utils;

import android.util.Log;

public class Logger {

    public static final String TAG = "android-rest";

    public static void e(String msg, Throwable t) {
        Log.e(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg), t);
    }

    public static void v(String msg) {
        Log.v(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
    }

    public static void w(String msg) {
        Log.w(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
    }

    public static void d(String msg) {
        Log.d(TAG, String.format("%s -> %s", Thread.currentThread().getName(), msg));
    }

}
