package com.tieudieu.fragmentstackmanager;

import android.support.v4.app.Fragment;

/**
 * Created by chienchieu on 27/01/2016.
 */
public interface FragmentStackSwapper<F extends Fragment> {

    void swapFragment(F fragment);
    void popFragment();
    void clearStack();
    void clearStackAll();
    int size();

    F getCurrentFragment();
    F getFragmentByTag(String tag);

}
