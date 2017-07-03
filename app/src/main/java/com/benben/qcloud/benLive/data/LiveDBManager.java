package com.benben.qcloud.benLive.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.benben.qcloud.benLive.QavsdkApplication;
import com.benben.qcloud.benLive.gift.bean.Gift;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/18.
 */

public class LiveDBManager {
    private static LiveDBManager dbMgr = new LiveDBManager();
    private LiveDBOpenHelper dbHelper;

    private LiveDBManager() {
        dbHelper = LiveDBOpenHelper.getInstance(
                QavsdkApplication.getInstance().getApplicationContext());
    }

    public static synchronized LiveDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new LiveDBManager();
        }
        return dbMgr;
    }

    private static final String TAG = "LiveDBManager";
    // 保存礼物
    public synchronized void saveGiftList(List<Gift> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.e(TAG, "saveGiftList: db.isOpen = "+db.isOpen() );
        Log.e(TAG, "saveGiftList: list.size = "+list.size() );
        if (db.isOpen()) {
            db.delete(LiveDao.GIFT_TAB_NAME, null, null);
            for (Gift gift : list) {
                ContentValues values = new ContentValues();
                values.put(LiveDao.GIFT_COLUMN_ID, Integer.parseInt(gift.getId()));
                Log.e(TAG, "saveGiftList: giftId = "+Integer.parseInt(gift.getId()));
                if (gift.getGname() != null) {
                    values.put(LiveDao.GIFT_COLUMN_NAME, gift.getGname());
                }
                if (gift.getGurl() != null) {
                    values.put(LiveDao.GIFT_COLUMN_URL, gift.getGurl());
                }
                if (gift.getGprice() != null) {
                    values.put(LiveDao.GIFT_COLUMN_PRICE, gift.getGprice());
                }
                long result = db.replace(LiveDao.GIFT_TAB_NAME, null, values);
                Log.e(TAG, "saveGiftList: result = " + result);
            }
        }
    }

    // 获取礼物列表
    public synchronized Map<String, Gift> getGiftList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, Gift> gifts = new HashMap<>();
        Log.e(TAG, "getGiftList: db.isOpen = "+db.isOpen() );
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + LiveDao.GIFT_TAB_NAME, null);
            while (cursor.moveToNext()) {
                int giftId = cursor.getInt(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_ID));
                String giftName = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_NAME));
                String giftPrice = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_PRICE));
                String giftUrl = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_URL));
                Gift gift = new Gift();
                gift.setId(giftId+"");
                gift.setGname(giftName);
                gift.setGurl(giftUrl);
                gift.setGprice(giftPrice);
                gifts.put(giftId+"", gift);
            }
            cursor.close();
        }
        Log.e(TAG, "getGiftList: gifts.size = " + gifts.size());
        return gifts;
    }
}
