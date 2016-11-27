package com.tieudieu.fragmentstackmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chienchieu on 28/01/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResLayout());
        initVariables();
        initViews(savedInstanceState);
    }

    /**
     * return res of layout for activity
     * return
     */
    protected abstract int getResLayout();

    /**
     * initial variables
     */
    protected abstract void initVariables();

    /**
     * initial views
     * param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);
}
