package org.restrung.rest.async.runnables;

import org.restrung.rest.async.loaders.APIDeleteLoader;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for DELETE requests
 * @param <T> the response type
 */
public class DeleteRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

	/**
	 * Constructs a GET runnable operation
	 * @see org.restrung.rest.async.runnables.AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(org.restrung.rest.client.APIDelegate, String, Object[])
	 */
	public DeleteRunnable(APIDelegate<T> delegate, String url, Object[] args) {
		super(delegate, url, args);
	}

	/**
	 * @see AbstractCacheAwareRunnable#executeAsyncTask()
	 */
	@Override
	public void executeAsyncTask() {
		new APIDeleteLoader<T>(getUrl(), getDelegate(), null, getArgs()).execute();
	}

	/**
	 * @see AbstractCacheAwareRunnable#executeLoader()
	 */
	@Override
	public void executeLoader() {
		new APIDeleteLoader<T>(getUrl(), getDelegate(), null, getArgs()).execute();
	}
}
