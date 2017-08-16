package com.aprz.heartharena.voice;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aprz.heartharena.R;
import com.aprz.heartharena.app.App;
import com.aprz.heartharena.config.SpeakRecognizerConfig;
import com.aprz.heartharena.utils.JsonParser;
import com.aprz.heartharena.voice.listener.OnGetSpeakRecognizerResultListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

/**
 * Created by aprz on 17-8-13.
 * email: lyldalek@gmail.com
 * desc:
 */

public class RecognizerDialogUtil {

    private SpeakRecognizerUtil mSpeakRecognizerUtil;
    private VoiceView mVoiceView;
    private TextView mTipView;
    private OnGetSpeakRecognizerResultListener mResultListener;
    private MaterialDialog mMaterialDialog;

    public void show(Context context) {
         mMaterialDialog = new MaterialDialog.Builder(context)
                .title(R.string.voice_recognizer)
                .titleGravity(GravityEnum.CENTER)
                .titleColor(ActivityCompat.getColor(context, R.color.gray))
                .customView(R.layout.speech_recognition_view, false)
                .backgroundColor(ActivityCompat.getColor(context, R.color.voice_background))
                .show();

        mMaterialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mSpeakRecognizerUtil != null) {
                    mSpeakRecognizerUtil.destroy();
                }
            }
        });

        View view = mMaterialDialog.getCustomView();
        assert view != null;
        mVoiceView = (VoiceView) view.findViewById(R.id.voice_view);
        mTipView = (TextView) view.findViewById(R.id.tip_text);
        mVoiceView.setOnVoiceViewClickListener(mOnVoiceViewClickListener);

        mSpeakRecognizerUtil = SpeakRecognizerUtil.getInstance();
        mSpeakRecognizerUtil.init(context, mInitListener);
        int ret = mSpeakRecognizerUtil.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            showTip("请开始说话");
        }
    }

    private void showTip(String tip) {
        mTipView.setText(tip);
    }

    public void dismiss() {
        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
        }
    }

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private VoiceView.OnVoiceViewClickListener mOnVoiceViewClickListener =
            new VoiceView.OnVoiceViewClickListener() {
                @Override
                public void start() {
                    int ret = mSpeakRecognizerUtil.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败,错误码：" + ret);
                    } else {
                        showTip("请开始说话");
                    }
                }

                @Override
                public void stop() {
                    mSpeakRecognizerUtil.cancel();
                }
            };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {


        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            mVoiceView.animateRadius(i*3);
        }

        @Override
        public void onBeginOfSpeech() {
            mVoiceView.setStateRecording();
            showTip(App.getInstance().getString(R.string.start_speak));
        }

        @Override
        public void onEndOfSpeech() {
            mVoiceView.setStateNormal();
            showTip(App.getInstance().getString(R.string.tap_to_speak));
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            mVoiceView.setStateNormal();
            showTip(App.getInstance().getString(R.string.tap_to_speak));
            if (SpeakRecognizerConfig.TRANSLATE_ENABLE) {
                printTransResult(recognizerResult);
            } else {
                printResult(recognizerResult);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if(SpeakRecognizerConfig.TRANSLATE_ENABLE && speechError.getErrorCode() == 14002) {
                showTip( speechError.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(speechError.getPlainDescription(true));
            }
            mVoiceView.setStateNormal();
            showTip(App.getInstance().getString(R.string.tap_to_speak));
        }

        @Override
        public void onEvent(int eventType, int i1, int i2, Bundle bundle) {
        }
    };


    private void printTransResult(RecognizerResult results) {
//        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
//        String oris = JsonParser.parseTransResult(results.getResultString(), "src");
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        if (mResultListener != null && !TextUtils.isEmpty(text)) {
            mResultListener.onGetResult(text);
            dismiss();
        }

//        String sn = null;
//        // 读取json结果中的sn字段
//        try {
//            JSONObject resultJson = new JSONObject(results.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void setResultListener(OnGetSpeakRecognizerResultListener resultListener) {
        mResultListener = resultListener;
    }
}
