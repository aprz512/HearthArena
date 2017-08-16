package com.aprz.heartharena.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.aprz.heartharena.bean.CardTable;
import com.aprz.heartharena.db.DBHelper;

import java.util.HashMap;

/**
 * Created by aprz on 17-8-4.
 * content provider 为 cursor loader 提供数据
 * 虽然没有必要 但是就是想写这个
 */

public class CardProvider extends ContentProvider {


    private static final String DB_NAME = "arena.db";
    private static final String DB_TABLE = "cards";
    private static final int DB_VERSION = 1;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 根据 _id 返回多个
        uriMatcher.addURI(CardTable.AUTHORITY, "item", CardTable.ITEM);
        // 根据 _id 返回一个
        uriMatcher.addURI(CardTable.AUTHORITY, "item/#", CardTable.ITEM_ID);
        // 根据 位置 pos 返回一个，第几条的意思
        uriMatcher.addURI(CardTable.AUTHORITY, "pos/#", CardTable.ITEM_POS);
    }

    private static final HashMap<String, String> cardProjectionMap;

    static {
        cardProjectionMap = new HashMap<>();
        cardProjectionMap.put(CardTable.ID, CardTable.ID);
        cardProjectionMap.put(CardTable.NAME, CardTable.NAME);
        cardProjectionMap.put(CardTable.IMAGE, CardTable.IMAGE);
        cardProjectionMap.put(CardTable.SCORE, CardTable.SCORE);
        cardProjectionMap.put(CardTable.SCORE_LEVEL, CardTable.SCORE_LEVEL);
        cardProjectionMap.put(CardTable.RARITY, CardTable.RARITY);
        cardProjectionMap.put(CardTable.PROFESSION, CardTable.PROFESSION);
        cardProjectionMap.put(CardTable.NEW_CARD, CardTable.NEW_CARD);
        cardProjectionMap.put(CardTable.COMMON, CardTable.COMMON);
        cardProjectionMap.put(CardTable.SCORE_INT, CardTable.SCORE_INT);
    }

    private DBHelper dbHelper = null;
    private ContentResolver resolver = null;


    @Override
    public boolean onCreate() {

        Context context = getContext();
        assert context != null;
        resolver = context.getContentResolver();
        dbHelper = DBHelper.create(context);


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;
        switch (uriMatcher.match(uri)) {
            case CardTable.ITEM: {
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(cardProjectionMap);
                limit = uri.getQueryParameter("limit") + "," + uri.getQueryParameter("offset");
                Log.e("aprz", limit);
                break;
            }
            case CardTable.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(cardProjectionMap);
                sqlBuilder.appendWhere(CardTable.ID + "=" + id);
                break;
            }
            case CardTable.ITEM_POS: {
                String pos = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(cardProjectionMap);
                limit = pos + ", 1";
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, null, limit);
        cursor.setNotificationUri(resolver, uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CardTable.ITEM:
            case CardTable.ITEM_LIMIT:
                return CardTable.CONTENT_TYPE;
            case CardTable.ITEM_ID:
            case CardTable.ITEM_POS:
                return CardTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) != CardTable.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(DB_TABLE, CardTable.ID, values);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }
        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case CardTable.ITEM: {
                count = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            }
            case CardTable.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE, CardTable.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case CardTable.ITEM: {
                count = db.update(DB_TABLE, values, selection, selectionArgs);
                break;
            }
            case CardTable.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE, values, CardTable.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
}
