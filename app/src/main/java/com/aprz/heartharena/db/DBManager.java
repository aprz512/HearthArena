package com.aprz.heartharena.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aprz.heartharena.bean.Card;

import java.util.ArrayList;

/**
 * Created by aprz on 17-8-7.
 * email: lyldalek@gmail.com
 * desc:
 * 数据库的 增删改查
 */

public class DBManager {

    private static class Holder {
        static DBManager holder = new DBManager();
    }

    public static DBManager getInstance() {
        return Holder.holder;
    }

    public static ArrayList<Card> getCards(Context context, String profession, String rarity) {
        return getCards(context, profession, rarity, 0);
    }

    public static ArrayList<Card> getCards(Context context, String profession, String rarity, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("wtf");
        }

        ArrayList<Card> cards = new ArrayList<>();

        DBHelper dbHelper = DBHelper.create(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (limit == 0) {
            cursor = database.rawQuery("select * from cards where profession = ? and rarity like ? order by score_int DESC",
                    new String[]{profession, "%" + rarity + "%"});
        } else {
            cursor = database.rawQuery("select * from cards where profession = ? and rarity like ? order by score_int DESC limit ?",
                    new String[]{profession, "%" + rarity + "%", String.valueOf(limit)});
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Card card = Card.createWithCursor(cursor);
                cards.add(card);
            } while (cursor.moveToNext());
            cursor.close();
        }

        database.close();

        return cards;
    }

    public static Card getCard(Context context, String cardName, String profession) {
        DBHelper dbHelper = DBHelper.create(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "select * from cards where name = ?",
                new String[]{cardName});

        Card card = null;

        if (cursor != null && cursor.moveToFirst()) {
            // 如果查询的结果多于一条,说明查询的是中立卡
            if (cursor.getCount() > 1) {
                cursor = database.rawQuery(
                        "select * from cards where name = ? and profession = ?",
                        new String[]{cardName, profession});

                if (cursor != null && cursor.moveToFirst()) {
                    card = Card.createWithCursor(cursor);
                    cursor.close();
                }
            } else {
                card = Card.createWithCursor(cursor);
                cursor.close();
            }
        }

        database.close();

        return card;
    }

    public static ArrayList<String> getAllCardName(Context context) {
        ArrayList<String> names = new ArrayList<>();

        DBHelper dbHelper = DBHelper.create(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("select name from cards", null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (!names.contains(name)) {
                    names.add(name);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        database.close();

        return names;
    }


    public static ArrayList<String> getSearchHistory(Context context, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("wtf");
        }

        ArrayList<String> historys = new ArrayList<>();

        DBHelper dbHelper = DBHelper.create(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();


        Cursor cursor;

        cursor = database.rawQuery("select count(*) from search_history", null);
        if (cursor != null && cursor.moveToFirst()) {
            long count = cursor.getLong(0);
            if (count > 10) {
                database.execSQL("delete from search_history where _id not in (select id from search_history order by _id desc limit 0, 10) ");
            }
        }

        if (limit == 0) {
            cursor = database.rawQuery("select history from search_history",
                    new String[]{});
        } else {
            cursor = database.rawQuery("select history from search_history limit ?",
                    new String[]{String.valueOf(limit)});
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String history = cursor.getString(cursor.getColumnIndex("history"));
                historys.add(history);
            } while (cursor.moveToNext());
            cursor.close();
        }

        database.close();

        return historys;
    }

}
