package com.tieudieu.fragmentstackmanager

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by chienchieu on 04/02/2016.
 */
abstract class BaseFragment : Fragment() {

    protected var mContext: Context? = null

    protected var mRootView: View? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }
}
