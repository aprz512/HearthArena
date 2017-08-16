package com.aprz.heartharena.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

/**
 * Created by aprz on 17-8-5.
 *
 * 数据库拷贝工具
 */

public class DBCopier {

    public static void copy(final Context context, final OnCopyDatabaseDoneListener listener) {

        final Runnable callback = new Runnable() {
            @Override
            public void run() {
                listener.onDatabaseCopyDone();
            }
        };

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                createDataBase(context);
                SystemClock.sleep(2000);
                Looper mainLooper = Looper.getMainLooper();
                Handler handler = new Handler(mainLooper);
                handler.postAtFrontOfQueue(callback);
            }
        });
    }

    private static void createDataBase(Context context) {
        try {
            File file = context.getDatabasePath("arena.db");
            if (file.exists()) {
                file.delete();
            }

            copyDatabaseFile(context, file.getAbsolutePath());
        } catch (IOException e) {
            throw new Error("数据库创建失败");
        }
    }

    private static void copyDatabaseFile(Context context, String absolutePath) throws IOException {
        InputStream is = context.getAssets().open("arena.db");
        OutputStream os = new FileOutputStream(absolutePath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
    }

    public interface OnCopyDatabaseDoneListener{

        void onDatabaseCopyDone();

    }

}
