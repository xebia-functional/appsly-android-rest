package org.restrung.rest.async.runnables;

import org.restrung.rest.async.asynctasks.APIPutAsyncTask;
import org.restrung.rest.async.loaders.APIPutLoader;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for PUT requests
 * @param <T> the response type
 */
public class PutRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

	/**
	 * An object to be serialized and sent in the request body
	 */
	private JSONSerializable body;

	/**
	 * Constructs a PUT runnable operation
	 * @see org.restrung.rest.async.runnables.AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(org.restrung.rest.client.APIDelegate, String, Object[])
	 * @param body a request object to send as content type application/json
	 */
	public PutRunnable(APIDelegate<T> delegate, String path, JSONSerializable body, Object... args) {
		super(delegate, path, args);
		this.body = body;
	}

	/**
	 * @see org.restrung.rest.async.runnables.AbstractCacheAwareRunnable#executeAsyncTask()
	 */
	@Override
	public void executeAsyncTask() {
		new APIPutAsyncTask<T>(getUrl(), body, getDelegate(), null, getArgs()).execute();
	}

	/**
	 * @see org.restrung.rest.async.runnables.AbstractCacheAwareRunnable#executeLoader()
	 */
	@Override
	public void executeLoader() {
		new APIPutLoader<T>(getDelegate(), null, getUrl(), body, getArgs()).execute();
	}
}
