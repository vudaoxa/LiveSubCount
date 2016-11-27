package com.tieudieu.fragmentstackmanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by chienchieu on 04/02/2016.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

//    abstract public void initView();

}
