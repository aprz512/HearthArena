package com.aprz.heartharena.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

import com.aprz.heartharena.R;
import com.aprz.heartharena.status.StatusLayoutManager;

/**
 * Created by aprz on 17-8-10.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class BaseFragment extends LazyFragment
        implements StatusLayoutManager.OnStatusLayoutInitListener {

    private StatusLayoutManager mLayoutManager;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected void configView(View root) {
        mLayoutManager = new StatusLayoutManager.Builder(getActivity())
                .setContentViewLayoutId(getContentLayoutResId())
                .setEmptyViewLayoutId(getEmptyLayoutResId())
                .setLoadingViewLayoutId(getLoadingLayoutResId())
                .setInitListener(this)
                .build();

        LinearLayout mainLayout = (LinearLayout) root.findViewById(R.id.ll_root);
        mainLayout.addView(mLayoutManager.getStatusLayout());
        mLayoutManager.showLoadingView();
    }

    @Override
    protected void finishInflateView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override
    public void initEmptyView(View emptyView, Bundle savedInstanceState) {

    }

    @Override
    public void initErrorView(View errorView, Bundle savedInstanceState) {

    }

    @Override
    protected String getDefaultFragmentTitle() {
        return null;
    }

    protected Activity getAttachActivity() {
        return mActivity;
    }

    public abstract @LayoutRes int getContentLayoutResId();

    protected  @LayoutRes int getEmptyLayoutResId() {
        return R.layout.fragment_empty;
    }

    protected  @LayoutRes int getLoadingLayoutResId() {
        return R.layout.fragment_loading;
    }

    protected void showLoadingView() {
        mLayoutManager.showLoadingView();
    }

    protected void showContentView() {
        mLayoutManager.showContentView();
    }

    protected void showEmptyView() {
        mLayoutManager.showEmptyView();
    }

}
