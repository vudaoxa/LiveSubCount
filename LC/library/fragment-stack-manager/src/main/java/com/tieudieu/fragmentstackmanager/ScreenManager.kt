package com.tieudieu.fragmentstackmanager

import android.support.v4.app.Fragment

/**
 * Created by chienchieu on 27/01/2016.
 */
interface ScreenManager {
    fun onMainScreenRequested()
    fun onSwapFragmentRequested(fragment: Fragment)
    fun onBackFragmentRequested()
    fun onFragmentEntered(fragment: Fragment?)
    fun onCloseRequested()
    fun onNewScreenRequested(indexTag: Int, typeContent: String?, obj: Any?)
    fun onNewScreenRequested(indexTag: Int, fragment: Fragment?, obj: Any?)
}
