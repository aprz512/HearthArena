package com.aprz.heartharena.app;

import android.app.Application;

import com.aprz.heartharena.image.ImageLoader;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by aprz on 17-8-6.
 *
 * --
 */

public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=598d60f2");
        ImageLoader.init(this);
    }


    public static App getInstance () {
        return sInstance;
    }

}
