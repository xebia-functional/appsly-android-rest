package it.restrung.rest.client;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

/**
 * A context provider for Fragments
 */
public class FragmentContextProvider implements ContextProvider {

    /**
     * the fragment
     */
    private Fragment fragment;

    /**
     * Private default constructor
     * @param fragment the fragment
     */
    private FragmentContextProvider(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Factory method to construct a ContextProvider out of a fragment
     * @param fragment the fragment
     * @return the context provider
     */
    public static ContextProvider get(Fragment fragment) {
        return new FragmentContextProvider(fragment);
    }

    /**
     * @see ContextProvider#getLoaderManager()
     */
    @Override
    public LoaderManager getLoaderManager() {
        return fragment.getLoaderManager();
    }

    /**
     * @see ContextProvider#getContext()
     */
    @Override
    public Context getContext() {
        return fragment.getActivity();
    }
}
