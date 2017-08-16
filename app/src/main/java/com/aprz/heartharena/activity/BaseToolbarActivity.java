package com.aprz.heartharena.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;


import com.aprz.heartharena.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aprz on 17-8-16.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class BaseToolbarActivity extends RxAppCompatActivity {

    Toolbar mToolbar;
    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化传递的参数
        initIntentArgs(getIntent());

        //设置布局内容
        setContentView(R.layout.activity_base_toolbar);
        mFrameLayout = (FrameLayout) findViewById(R.id.content);
        LayoutInflater.from(this).inflate(getLayoutId(), mFrameLayout, true);

        //初始化控件
        configView(mFrameLayout, savedInstanceState);

        //初始化ToolBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(mToolbar);

        // 做加载数据之类的操作
        finishInflateView();
    }

    protected void initIntentArgs(Intent intent) {}

    public abstract int getLayoutId();

    public abstract void initToolBar(Toolbar toolbar);

    protected abstract void configView(FrameLayout frameLayout, Bundle savedInstanceState);

    protected abstract void finishInflateView();

}
