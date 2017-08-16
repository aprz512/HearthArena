package com.aprz.heartharena.voice;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.aprz.heartharena.R;
import com.aprz.heartharena.app.App;
import com.aprz.heartharena.config.SpeakRecognizerConfig;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.RecognizerListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aprz on 17-8-13.
 * email: lyldalek@gmail.com
 * desc:
 */

public class SpeakRecognizerUtil {

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SpeakRecognizerUtil() {}

    private static class Holder {
        static SpeakRecognizerUtil sRecogniserUtil = new SpeakRecognizerUtil();
    }

    public static SpeakRecognizerUtil getInstance() {
        return Holder.sRecogniserUtil;
    }


    public void init(Context context, InitListener listener) {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, listener);

        mIatResults.clear();
        // 设置参数
        setParam();
    }

    public int startListening(RecognizerListener listener) {
        // 不显示听写对话框
        return mIat.startListening(listener);
    }

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeakRecognizerConfig.ENGINE_TYPE);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, SpeakRecognizerConfig.RESULT_TYPE);

        if (SpeakRecognizerConfig.TRANSLATE_ENABLE) {
            mIat.setParameter(SpeechConstant.ASR_SCH, SpeakRecognizerConfig.ASR_SCH);
            mIat.setParameter(SpeechConstant.ADD_CAP, SpeakRecognizerConfig.ADD_CAP);
            mIat.setParameter(SpeechConstant.TRS_SRC, SpeakRecognizerConfig.TRS_SRC);
        }

        String lag = SpeakRecognizerConfig.CN_ACCENT;
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, SpeakRecognizerConfig.EN_LANGUAGE);
            mIat.setParameter(SpeechConstant.ACCENT, SpeakRecognizerConfig.EN_ACCENT);

            if (SpeakRecognizerConfig.TRANSLATE_ENABLE) {
                mIat.setParameter(SpeechConstant.ORI_LANG, SpeakRecognizerConfig.EN_ORI_LANG);
                mIat.setParameter(SpeechConstant.TRANS_LANG, SpeakRecognizerConfig.EN_TRANS_LANG);
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, SpeakRecognizerConfig.CN_LANGUAGE);
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (SpeakRecognizerConfig.TRANSLATE_ENABLE) {
                mIat.setParameter(SpeechConstant.ORI_LANG, SpeakRecognizerConfig.CN_ORI_LANG);
                mIat.setParameter(SpeechConstant.TRANS_LANG, SpeakRecognizerConfig.CN_TRANS_LANG);
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, SpeakRecognizerConfig.VAD_BOS);

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, SpeakRecognizerConfig.VAD_EOS);

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, SpeakRecognizerConfig.ASR_PTT);

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, SpeakRecognizerConfig.AUDIO_FORMAT);
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, SpeakRecognizerConfig.ASR_AUDIO_PATH);
    }

    public void destroy() {
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    public void stopListening() {
        if (mIat != null) {
            mIat.stopListening();
        }
    }

    public void cancel() {
        if (mIat != null) {
            mIat.cancel();
        }
    }

}
