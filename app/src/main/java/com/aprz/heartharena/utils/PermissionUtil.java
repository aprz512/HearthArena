package com.aprz.heartharena.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by aprz on 17-8-14.
 * email: lyldalek@gmail.com
 * desc:
 */

public class PermissionUtil {

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                permission) == PackageManager.PERMISSION_GRANTED);
        if(!hasPermission){
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        }
    }

}
