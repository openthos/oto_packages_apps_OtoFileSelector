package com.openthos.fileselector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.openthos.fileselector.app.Constants;

import java.io.File;

public abstract class BaseActivity extends FragmentActivity {
    private String mCurrentPath;
    private String ROOT_PATH = Constants.ROOT_PATH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
        initListener();
    }

    public String getCurrentPath() {
        if (mCurrentPath == null) {
            mCurrentPath = ROOT_PATH;
        }
        return mCurrentPath;
    }

    public void setCurrentPath(String currentPath) {
        mCurrentPath = currentPath;
    }

    public String getLastPath() {
        String parent = new File(mCurrentPath).getParent();
        return mCurrentPath.equals(ROOT_PATH) || parent.equals(ROOT_PATH) ? ROOT_PATH : parent;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();
}
