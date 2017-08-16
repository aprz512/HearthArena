package com.aprz.heartharena.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.aprz.heartharena.bean.CardTable;
import com.aprz.heartharena.bean.SearchHistoryTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

/**
 * Created by aprz on 17-8-5.
 * <p>
 * 数据库
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "arena.db";

    private static final String DB_TABLE = "cards";
    private static final String DB_TABLE_SEARCH_HISTORY = "search_history";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static DBHelper create(Context context) {
        return new DBHelper(context, DB_NAME, null, 2);
    }

}
