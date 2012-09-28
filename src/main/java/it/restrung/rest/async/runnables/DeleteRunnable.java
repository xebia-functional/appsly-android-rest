package it.restrung.rest.async.runnables;

import it.restrung.rest.async.asynctasks.APIDeleteAsyncTask;
import it.restrung.rest.async.loaders.APIDeleteLoader;
import it.restrung.rest.client.APIDelegate;
import it.restrung.rest.marshalling.request.JSONSerializable;
import it.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for DELETE requests
 *
 * @param <T> the response type
 */
public class DeleteRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

    /**
     * An object to be serialized and sent in the request body
     */
    private JSONSerializable body;

    /**
     * Constructs a GET runnable operation
     *
     * @see AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(it.restrung.rest.client.APIDelegate, String, Object[])
     */
    public DeleteRunnable(APIDelegate<T> delegate, String url, JSONSerializable body, Object[] args) {
        super(delegate, url, args);
        this.body = body;
    }

    /**
     * @see AbstractCacheAwareRunnable#executeAsyncTask()
     */
    @Override
    public void executeAsyncTask() {
        new APIDeleteAsyncTask<T>(getUrl(), getDelegate(), null, body, getArgs()).execute();
    }

    /**
     * @see AbstractCacheAwareRunnable#executeLoader()
     */
    @Override
    public void executeLoader() {
        new APIDeleteLoader<T>(getUrl(), getDelegate(), null, body, getArgs()).execute();
    }
}
