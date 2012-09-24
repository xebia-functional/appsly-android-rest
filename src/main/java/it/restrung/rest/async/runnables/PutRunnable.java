package it.restrung.rest.async.runnables;

import it.restrung.rest.client.APIDelegate;
import it.restrung.rest.async.asynctasks.APIPutAsyncTask;
import it.restrung.rest.async.loaders.APIPutLoader;
import it.restrung.rest.marshalling.request.JSONSerializable;
import it.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for PUT requests
 *
 * @param <T> the response type
 */
public class PutRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

    /**
     * An object to be serialized and sent in the request body
     */
    private JSONSerializable body;

    /**
     * Constructs a PUT runnable operation
     *
     * @param body a request object to send as content type application/json
     * @see AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(it.restrung.rest.client.APIDelegate, String, Object[])
     */
    public PutRunnable(APIDelegate<T> delegate, String path, JSONSerializable body, Object... args) {
        super(delegate, path, args);
        this.body = body;
    }

    /**
     * @see AbstractCacheAwareRunnable#executeAsyncTask()
     */
    @Override
    public void executeAsyncTask() {
        new APIPutAsyncTask<T>(getUrl(), body, getDelegate(), null, getArgs()).execute();
    }

    /**
     * @see AbstractCacheAwareRunnable#executeLoader()
     */
    @Override
    public void executeLoader() {
        new APIPutLoader<T>(getDelegate(), null, getUrl(), body, getArgs()).execute();
    }
}
