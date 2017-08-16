package com.aprz.heartharena.utils;

import android.support.v4.app.ActivityCompat;

import com.aprz.heartharena.R;
import com.aprz.heartharena.app.App;

/**
 * Created by aprz on 17-8-11.
 * email: lyldalek@gmail.com
 * desc:
 */

public class ScoreTextColorUtil {


    public static int getColor(String scoreLevel) {

        switch (scoreLevel) {
            case "极好":
                return ActivityCompat.getColor(App.getInstance(), R.color.great);
            case "好":
                return ActivityCompat.getColor(App.getInstance(), R.color.good);
            case "不错":
                return ActivityCompat.getColor(App.getInstance(), R.color.above_average);
            case "中等":
                return ActivityCompat.getColor(App.getInstance(), R.color.average);
            case "稍差":
                return ActivityCompat.getColor(App.getInstance(), R.color.below_average);
            case "差":
                return ActivityCompat.getColor(App.getInstance(), R.color.bad);
            case "极差":
                return ActivityCompat.getColor(App.getInstance(), R.color.terrible);
            default:
                return -1;
        }

    }

}
