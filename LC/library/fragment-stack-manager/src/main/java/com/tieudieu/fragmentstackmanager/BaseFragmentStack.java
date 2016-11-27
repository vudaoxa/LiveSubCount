package com.tieudieu.fragmentstackmanager;

import android.content.Context;

/**
 * Created by chienchieu on 27/01/2016.
 */
public abstract class BaseFragmentStack extends BaseFragment {
    private String title;
    private ScreenManager mScreenManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScreenManager) {
            mScreenManager = (ScreenManager) context;
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected ScreenManager getScreenManager() {
        return mScreenManager;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFragmentTitle() {
        return title;
    }

    public boolean showBackButton() {
        return false;
    }

    public int getIndexTag() {
        return 0;
    }

}
