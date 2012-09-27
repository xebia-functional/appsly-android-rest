package it.restrung.rest.utils;

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
     * Checks if a delegate's context supports loaders
     *
     * @param delegate the delegate
     * @return true if the delegate requesting context supports loaders
     */
    public static boolean supportsLoaders(APIDelegate<?> delegate) {
        return delegate.getContextProvider().getLoaderManager() != null;
    }

}
