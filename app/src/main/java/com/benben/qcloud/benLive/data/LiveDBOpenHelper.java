package com.benben.qcloud.benLive.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/18.
 */

public class LiveDBOpenHelper extends SQLiteOpenHelper {
    private static final int version = 1;
    private static LiveDBOpenHelper instance;

    private static final String GIFT_TABLE_CREATE = "create table" +
            LiveDao.GIFT_TAB_NAME + "(" +
            LiveDao.GIFT_COLUMN_NAME + " text," +
            LiveDao.GIFT_COLUMN_URL + " text," +
            LiveDao.GIFT_COLUMN_PRICE + " text," +
            LiveDao.GIFT_COLUMN_ID + " text primary key)";

    public LiveDBOpenHelper(Context context) {
        super(context, getDatabaseNames(context), null, version);
    }

    private static String getDatabaseNames(Context context) {

        return context.getPackageName() + ".db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GIFT_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static LiveDBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LiveDBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void closeDB(){
        if (instance != null) {
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
