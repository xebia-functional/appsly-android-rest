package it.restrung.rest.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import it.restrung.rest.client.APIDelegate;

/**
 * Static utilities for context related operations
 */
public class ContextUtils {

    /**
     * Prevent instantiation
     */
    private ContextUtils() {
    }

    /**
     * Gets a loader manager for the context
     *
     * @param context the context
     * @return the loader manager if any
     */
    public static LoaderManager getLoaderManager(Context context) {
        return FragmentActivity.class.isAssignableFrom(context.getClass()) ? ((FragmentActivity) context).getSupportLoaderManager() : null;
    }

    /**
     * Checks if a delegate's context supports loaders
     *
     * @param delegate the delegate
     * @return true if the delegate requesting context supports loaders
     */
    public static boolean supportsLoaders(APIDelegate<?> delegate) {
        return getLoaderManager(delegate.getRequestingContext()) != null;
    }

}
