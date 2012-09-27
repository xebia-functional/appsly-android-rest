package it.restrung.rest.client;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

/**
 * A context provider implementation for @see Context callers such as
 * Activity, FragmentActivity, Application, etc.
 */
public class DefaultContextProvider implements ContextProvider {

    /**
     * The context
     */
    private Context context;

    /**
     * Default private constructor
     * @param context the context
     */
    private DefaultContextProvider(Context context) {
        this.context = context;
    }

    /**
     * Factory method to obtain a ContextProvider
     * @param context the context
     * @return the context provider
     */
    public static ContextProvider get(Context context) {
        return new DefaultContextProvider(context);
    }

    /**
     * @see ContextProvider#getLoaderManager()
     */
    @Override
    public LoaderManager getLoaderManager() {
        return (FragmentActivity.class.isAssignableFrom(context.getClass())) ? ((FragmentActivity)context).getSupportLoaderManager() : null;
    }

    /**
     * @see ContextProvider#getContext()
     */
    @Override
    public Context getContext() {
        return context;
    }
}
