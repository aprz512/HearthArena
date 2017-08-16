package com.aprz.heartharena.config;

import android.os.Environment;
import android.support.v4.app.NavUtils;

import com.iflytek.cloud.SpeechConstant;

/**
 * Created by aprz on 17-8-14.
 * email: lyldalek@gmail.com
 * desc:
 */

public class SpeakRecognizerConfig {

    // 引擎类型
    public static final String ENGINE_TYPE = SpeechConstant.TYPE_CLOUD;
    // 设置返回结果格式
    public static final String RESULT_TYPE = "json";

    public static boolean TRANSLATE_ENABLE = false;


    public static final String ASR_SCH = "1";
    public static final String ADD_CAP = "translate";
    public static final String TRS_SRC = "its";


    public static final String EN_LANGUAGE = "en_us";
    public static final String EN_ACCENT = null;
    public static final String EN_ORI_LANG = "en";
    public static final String EN_TRANS_LANG = "cn";


    public static final String CN_LANGUAGE = "zh_cn";
    public static final String CN_ACCENT = "mandarin";
    public static final String CN_ORI_LANG = "cn";
    public static final String CN_TRANS_LANG = "en";

    // 设置音频来源为外部文件
    public static final String AUDIO_SOURCE = "-1";


    // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
    public static final String VAD_BOS = "4000";
    // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
    public static final String VAD_EOS = "1000";
    // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
    public static final String ASR_PTT = "0";
    // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
    // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
    public static final String AUDIO_FORMAT = "wav";
    public static final String ASR_AUDIO_PATH = Environment.getExternalStorageDirectory() + "/msc/iat.wav";

}
