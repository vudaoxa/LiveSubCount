package com.tieudieu.fragmentstackmanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tieudieu.util.DebugLog;

import java.util.Stack;

/**
 * Created by chienchieu on 27/01/2016.
 */
public class FragmentStackManager<F extends Fragment> implements FragmentStackSwapper<F> {

    public static final int TIME_ANIMATION = 400;

    private InitializationParams mInitializationParams;
    private Handler mUiHandler;
    private F mContentFragment;
    private Stack<F> stackFragments;

    public FragmentStackManager() {
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void initialize(InitializationParams initializationParams) {
        if (initializationParams == null) {
            throw new IllegalArgumentException("Argument is mandatory");
        }
        mInitializationParams = initializationParams;
        stackFragments = new Stack<>();
    }

    private void performOperationIfAllowed(final Runnable operation) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                operation.run();
            }
        });
    }

    private void performOperationIfAllowed(final Runnable operation, boolean postDelay) {
        if (postDelay) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    operation.run();
                }
            }, TIME_ANIMATION);
        } else {
            performOperationIfAllowed(operation);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        DebugLog.e(String.format("onRestoreInstanceState(): savedInstanceState[%b]", savedInstanceState != null));

        if (savedInstanceState == null) {
            mInitializationParams.getScreenManager().onMainScreenRequested();
        } else {
            notifyFragmentChange();
        }
    }

    private void notifyFragmentChange() {
        if (findCurrentFragment()) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mInitializationParams != null) {
                        mInitializationParams.getScreenManager().onFragmentEntered(mContentFragment);
                    }
                }
            });
        }
    }

    private void notifyPause() {
        if (stackFragments.size() > 0) {
            stackFragments.lastElement().onPause();
        }
    }

    private void notifyCloseRequest() {
        DebugLog.v("notifyCloseRequest()");
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mInitializationParams.getScreenManager() != null) {
                    mInitializationParams.getScreenManager().onCloseRequested();
                }
            }
        });
    }

    private boolean findCurrentFragment() {
        // DebugLog.e("xyz~stackFragment~size"+(stackFragments == null?0:stackFragments.size()));
        if (stackFragments == null || stackFragments.size() == 0) {
            return false;
        } else {
            mContentFragment = stackFragments.lastElement();
            return mContentFragment != null;
        }
    }

    @Override
    public int size() {
        return stackFragments == null ? 0 : stackFragments.size();
    }

    @Override
    public F getCurrentFragment() {
        return mContentFragment;
    }

    @Override
    public F getFragmentByTag(String tag) {
        return null;
    }

    @Override
    public void swapFragment(final F fragment) {
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                DebugLog.v("swapFragment()");
                final FragmentTransaction ft = mInitializationParams.getFragmentManager().beginTransaction();
                if (mInitializationParams.isAnimationEnabled()) {
                    // ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_in, 0, 0);
                    ft.setCustomAnimations(R.anim.slide_left_in, 0, 0, 0);
                }
                ft.add(mInitializationParams.getContentFrame(), fragment);
                if (stackFragments.size() > 0) {
                    notifyPause();
                    if (!mInitializationParams.isAnimationEnabled()) {
                        ft.hide(stackFragments.lastElement());
                    }
                }
                stackFragments.push(fragment);
                ft.commit();
                mInitializationParams.getFragmentManager().executePendingTransactions();
                findCurrentFragment();
                notifyFragmentChange();
            }
        };
        performOperationIfAllowed(operation);

        // hide old fragment if animation anable
        if (mInitializationParams.isAnimationEnabled()) {
            Runnable operationHide = new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction ft = mInitializationParams.getFragmentManager().beginTransaction();
                    if (stackFragments.size() > 1) {
                        if (mInitializationParams.isAnimationEnabled()) {
                            ft.hide(stackFragments.get(stackFragments.size() - 2));
                        }
                    }
                    ft.commit();
                }
            };
            performOperationIfAllowed(operationHide, true);
        }
    }

    @Override
    public void popFragment() {
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                int stackEntries = stackFragments.size();
                // DebugLog.v(String.format("popFragment(): entries[%d]", stackEntries));
                if (stackEntries >= 2) {
                    FragmentTransaction ft = mInitializationParams.getFragmentManager().beginTransaction();
                    if (mInitializationParams.isAnimationEnabled()) {
                        ft.setCustomAnimations(0, R.anim.slide_right_out, 0, 0);
                    }
                    stackFragments.lastElement().onPause();
                    ft.remove(stackFragments.pop());
                    stackFragments.lastElement().onResume();
                    ft.show(stackFragments.lastElement());
                    ft.commit();
                    findCurrentFragment();
                    notifyFragmentChange();
                    // DebugLog.v("popFragment-fragmentStack.size()=" + stackFragments.size());
                } else {
                    notifyCloseRequest();
                }
            }
        };
        performOperationIfAllowed(operation);
    }

    @Override
    public void clearStack() {
        if (mInitializationParams.getHomeClass() == null) {
            clearStackAll();
        }
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                mInitializationParams.getFragmentManager().executePendingTransactions();
                FragmentTransaction ft = mInitializationParams.getFragmentManager().beginTransaction();

                for (int i = stackFragments.size() - 1; i >= 0; i--) {
                    if (stackFragments.get(i).getClass() == mInitializationParams.getHomeClass()) {
                        continue;
                    }
                    ft.remove(stackFragments.get(i));
                    stackFragments.remove(i);
                }
                ft.commit();
            }
        };
        performOperationIfAllowed(operation);
    }

    @Override
    public void clearStackAll() {
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                mInitializationParams.getFragmentManager().executePendingTransactions();
                FragmentTransaction ft = mInitializationParams.getFragmentManager().beginTransaction();

                for (int i = stackFragments.size() - 1; i >= 0; i--) {
                    ft.remove(stackFragments.get(i));
                    stackFragments.remove(i);
                }
                ft.commit();
            }
        };
        performOperationIfAllowed(operation);
    }
}
