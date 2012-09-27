package it.restrung.rest.client;

import android.content.Context;
import android.support.v4.app.LoaderManager;

/**
 * A provider that returns information to help out with Android's Fragment and Activity hierarchy issues to obtain
 * a decent context and loader manager
 */
public interface ContextProvider {

    /**
     * Get the loader manager
     * @return the loader manager
     */
    LoaderManager getLoaderManager();

    /**
     * Gets the context from which the operation will extract services, cache directories and other info
     * @return the context
     */
    Context getContext();

}
