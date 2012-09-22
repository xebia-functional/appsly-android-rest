package org.restrung.rest.async.runnables;

import org.restrung.rest.cache.RequestCache;
import org.restrung.rest.client.APIDelegate;
import org.restrung.rest.marshalling.response.JSONResponse;
import org.restrung.rest.utils.ContextUtils;

/**
 * Abstract class for runnable operations that are aware of the cache policies
 * <T> a type implementer of JSONResponse
 */
public abstract class AbstractCacheAwareRunnable<T extends JSONResponse> implements Runnable {

	/**
	 * The url path
	 */
	private String url;

	/**
	 * The API delegate
	 */
	private APIDelegate<T> delegate;

	/**
	 * A set of args used for the url placeholders
	 */
	private Object[] args;

	/**
	 * Constructor
	 * @param delegate the delegate
	 * @param url the url
	 * @param args args for url placeholders as in @see java.lang.String#format(String, Object[])
	 */
	public AbstractCacheAwareRunnable(APIDelegate<T> delegate, String url, Object[] args) {
		this.url = url;
		this.delegate = delegate;
		this.args = args;
	}

	/**
	 * Retries the current runnable implementer if the cache network enabled policy is active
	 * @param <T>
	 */
	protected <T extends JSONResponse> void reExecuteIfNetworkEnabled() {
		if (RequestCache.LoadPolicy.NETWORK_ENABLED.equals(delegate.getCacheLoadPolicy())) {
			delegate.setCacheLoadPolicy(RequestCache.LoadPolicy.LOAD_IF_OFFLINE);
			run();
		}
	}

	@Override
	public void run() {
		if (ContextUtils.supportsLoaders(delegate)) {
			executeLoader();
		} else {
			executeAsyncTask();
		}
	}

	/**
	 * Asks implementers to execute the operation on a loader
	 */
	public abstract void executeLoader();

	/**
	 * Asks implementers to execute the operation on a loader
	 */
	public abstract void executeAsyncTask();

	/**
	 *
	 * @return the unparsed url path with var placeholders
	 */
	public String getUrl() {
		return url;
	}

	/**
	 *
	 * @return the api delegate
	 */
	public APIDelegate<T> getDelegate() {
		return delegate;
	}

	/**
	 *
	 * @return the args used for url placeholders
	 */
	public Object[] getArgs() {
		return args;
	}
}
