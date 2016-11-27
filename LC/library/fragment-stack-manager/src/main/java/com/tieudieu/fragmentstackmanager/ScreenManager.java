package com.tieudieu.fragmentstackmanager;

import android.support.v4.app.Fragment;

/**
 * Created by chienchieu on 27/01/2016.
 */
public interface ScreenManager {

    void onMainScreenRequested();
    void onSwapFragmentRequested(Fragment fragment);
    void onBackFragmentRequested();
    void onFragmentEntered(Fragment fragment);
    void onCloseRequested();
    void onNewScreenRequested(int indexTag, int typeContent, Object object);
    void onNewScreenRequested(int indexTag, String typeContent, Object object);

}
