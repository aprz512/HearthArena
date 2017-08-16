package com.aprz.heartharena.bean;

import android.net.Uri;

/**
 * Created by aprz on 17-8-5.
 * 主要是定义了一些常量，
 * 例如用来访问信息数据的URI、MIME（Multipurpose Internet Mail Extensions）类型以及格式等
 */

public class CardTable {

    /*Data Field*/
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String SCORE = "score";
    public static final String SCORE_LEVEL = "score_level";
    public static final String RARITY = "rarity";
    public static final String PROFESSION = "profession";
    public static final String NEW_CARD = "is_new";
    public static final String COMMON = "is_common";
    public static final String SCORE_INT = "score_int";

    /*Default sort order*/
    public static final String DEFAULT_SORT_ORDER = " score_int desc ";


    /*Authority*/
    public static final String AUTHORITY = "com.aprz.providers.cards";


    /*Match Code*/
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;
    public static final int ITEM_POS = 3;
    public static final int ITEM_LIMIT = 4;


    /*MIME*/
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.aprz.card";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.aprz.card";


    /*Content URI*/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");

}
