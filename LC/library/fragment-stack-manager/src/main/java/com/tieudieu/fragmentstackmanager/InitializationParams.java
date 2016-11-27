package com.tieudieu.fragmentstackmanager;

import android.support.v4.app.FragmentManager;

/**
 * Created by chienchieu on 27/01/2016.
 */
public class InitializationParams {

    private FragmentManager mFragmentManager;
    private int mContentFrame;
    private ScreenManager mScreenManager;
    private boolean mIsAnimationEnabled;
    private Class<?> mHomeClass;

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public int getContentFrame() {
        return mContentFrame;
    }

    public ScreenManager getScreenManager() {
        return mScreenManager;
    }

    public boolean isAnimationEnabled (){
        return mIsAnimationEnabled;
    }

    public Class<?> getHomeClass() {
        return mHomeClass;
    }

    private InitializationParams(Builder builder) {
        mFragmentManager = builder.mFragmentManager;
        mContentFrame = builder.mContentFrame;
        mScreenManager = builder.mScreenManager;
        mIsAnimationEnabled = builder.mIsAnimationEnabled;
        mHomeClass = builder.mHomeClass;
    }

    /**
     * Parameters builder class.
     */
    public static class Builder {
        private FragmentManager mFragmentManager;
        private Integer mContentFrame;
        private ScreenManager mScreenManager;
        private boolean mIsAnimationEnabled = false;
        private Class<?> mHomeClass;

        public Builder fragmentManager(FragmentManager fragmentManager) {
            mFragmentManager = fragmentManager;
            return this;
        }

        public Builder contentFrame(int contentFrame) {
            mContentFrame = contentFrame;
            return this;
        }

        public Builder screenManager(ScreenManager screenManager) {
            mScreenManager = screenManager;
            return this;
        }

        public Builder enableAnimation(boolean enable){
            mIsAnimationEnabled = enable;
            return this;
        }

        public Builder setHomeClass(Class<?> cls){
            mHomeClass = cls;
            return this;
        }

        public InitializationParams build() {
            if ((mFragmentManager == null) || (mContentFrame == null) || (mScreenManager == null)) {
                throw new IllegalStateException("All parameters are mandatory");
            }
            return new InitializationParams(this);
        }
    }

}
