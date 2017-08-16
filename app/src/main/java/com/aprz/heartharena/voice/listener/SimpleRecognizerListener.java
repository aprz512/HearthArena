package com.aprz.heartharena.voice.listener;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.aprz.heartharena.config.SpeakRecognizerConfig;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechError;

/**
 * Created by aprz on 17-8-14.
 * email: lyldalek@gmail.com
 * desc:
 */

public abstract class SimpleRecognizerListener implements RecognizerListener {

    public abstract View getErrorMsgView();

    //    @Override
//    public void onBeginOfSpeech() {
        // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//    }

    @Override
    public void onError(SpeechError error) {
        // Tips：
        // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
        // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
        if (SpeakRecognizerConfig.TRANSLATE_ENABLE && error.getErrorCode() == 14002) {
            Snackbar.make(getErrorMsgView(), error.getPlainDescription(true) + "\n请确认是否已开通翻译功能",
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getErrorMsgView(), error.getPlainDescription(true),
                    Snackbar.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onEndOfSpeech() {
        // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//    }

//    @Override
//    public void onResult(RecognizerResult results, boolean isLast) {
//        if (SpeakRecognizerConfig.TRANSLATE_ENABLE) {
//        } else {
//        }

//        if (isLast) {
//             TODO 最后的结果
//        }
//    }

//    @Override
//    public void onVolumeChanged(int volume, byte[] data) {
//    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        // 若使用本地能力，会话id为null
        //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
        //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        //		Log.d(TAG, "session id =" + sid);
        //	}
    }

}
