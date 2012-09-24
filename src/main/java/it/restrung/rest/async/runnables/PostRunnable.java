package it.restrung.rest.async.runnables;

import it.restrung.rest.async.asynctasks.APIPostAsyncTask;
import it.restrung.rest.client.APIDelegate;
import it.restrung.rest.async.loaders.APIPostLoader;
import it.restrung.rest.client.APIPostParams;
import it.restrung.rest.marshalling.request.JSONSerializable;
import it.restrung.rest.marshalling.response.JSONResponse;

import java.io.File;

/**
 * Runnable operation for POST requests
 *
 * @param <T> the response type
 */
public class PostRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

    /**
     * An object to be serialized and sent in the request body
     */
    private JSONSerializable body;

    /**
     * A set of params and callback to get post information and progress
     */
    private APIPostParams postParams;

    /**
     * A file to be sent with the request
     */
    private File file;

    /**
     * Constructs a POST runnable operation
     *
     * @param body       a request object to send as content type application/json
     * @param file       a request file to be sent with the bdy if included all will be sent as a multipart request
     * @param postParams a set of params and callback to get post information and progress
     * @see AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(it.restrung.rest.client.APIDelegate, String, Object[])
     */
    public PostRunnable(APIDelegate<T> delegate, String path, JSONSerializable body, File file, APIPostParams postParams, Object... args) {
        super(delegate, path, args);
        this.body = body;
        this.postParams = postParams;
        this.file = file;
    }

    /**
     * @see AbstractCacheAwareRunnable#executeAsyncTask()
     */
    @Override
    public void executeAsyncTask() {
        new APIPostAsyncTask<T>(getUrl(), body, file, getDelegate(), postParams, null, getArgs()).execute();
    }

    /**
     * @see AbstractCacheAwareRunnable#executeLoader()
     */
    @Override
    public void executeLoader() {
        new APIPostLoader<T>(getDelegate(), null, postParams, getUrl(), body, file, getArgs()).execute();
    }
}
