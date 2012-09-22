package org.restrung.rest.async.runnables;

import android.support.v4.content.Loader;
import org.restrung.rest.async.asynctasks.APIGetAsyncTask;
import org.restrung.rest.async.loaders.APIGetLoader;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.marshalling.response.JSONResponse;

/**
 * Runnable operation for GET requests
 * @param <T> the response type
 */
public class GetRunnable<T extends JSONResponse> extends AbstractCacheAwareRunnable<T> {

	/**
	 * Constructs a GET runnable operation
	 * @see AbstractCacheAwareRunnable#AbstractCacheAwareRunnable(org.restrung.rest.client.APIDelegate, String, Object[])
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
