package com.aprz.heartharena.voice.listener;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;

/**
 * Created by aprz on 17-8-14.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class SimpleInitListener implements InitListener {

    public abstract View getErrorMsgView();

    @Override
    public void onInit(int code) {
        if (code != ErrorCode.SUCCESS) {
            Snackbar.make(getErrorMsgView(), "初始化失败，错误码：" + code, Snackbar.LENGTH_LONG).show();
        } else {
            onSuccess(code);
        }
    }

    public abstract void onSuccess(int code);

}
