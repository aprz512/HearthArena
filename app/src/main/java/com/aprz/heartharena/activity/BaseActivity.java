package com.aprz.heartharena.activity;

import android.content.Intent;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aprz on 17-8-13.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化传递的参数
        initIntentArgs(getIntent());

        //设置布局内容
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        mUnbinder = ButterKnife.bind(this);
        //初始化控件
        configView(savedInstanceState);
        //初始化ToolBar
        initToolBar();

        // 做加载数据之类的操作
        finishInflateView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


    protected void initIntentArgs(Intent intent) {}

    public abstract int getLayoutId();

    public abstract void initToolBar();

    protected abstract void configView(Bundle savedInstanceState);

    protected abstract void finishInflateView();



}
