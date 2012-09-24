package it.restrung.rest.async.runnables;

import android.support.v4.content.Loader;
import it.restrung.rest.async.asynctasks.APIGetAsyncTask;
import it.restrung.rest.async.loaders.APIGetLoader;
import it.restrung.rest.client.APIDelegate;
import it.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for GET requests
 *
 * @param <T> the response type
 */
public class GetRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

    /**
     * Constructs a GET runnable operation
     *
     * @see AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(it.restrung.rest.client.APIDelegate, String, Object[])
     */
    public GetRunnable(APIDelegate<T> delegate, String url, Object[] args) {
        super(delegate, url, args);
    }

    /**
     * @see AbstractCacheAwareRunnable#executeAsyncTask()
     */
    @Override
    public void executeAsyncTask() {
        new APIGetAsyncTask<T>(getUrl(), getDelegate(), null, getArgs()) {
            @Override
            protected void onPostExecute(T result) {
                super.onPostExecute(result);
                reExecuteIfNetworkEnabled();
            }
        }.execute();
    }

    /**
     * @see AbstractCacheAwareRunnable#executeLoader()
     */
    @Override
    public void executeLoader() {
        new APIGetLoader<T>(getDelegate(), null, getUrl(), getArgs()) {
            @Override
            public void onLoadFinished(Loader<T> loader, T data) {
                super.onLoadFinished(loader, data);
                reExecuteIfNetworkEnabled();
            }
        }.execute();
    }
}
