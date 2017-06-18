package com.benben.qcloud.benLive.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public static synchronized LiveDBManager getInstace() {
        if (dbMgr == null) {
            dbMgr = new LiveDBManager();
        }
        return dbMgr;
    }

    // 保存礼物
    public synchronized void saveGiftList(List<Gift> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(LiveDao.GIFT_TAB_NAME, null, null);
            for (Gift gift : list) {
                ContentValues values = new ContentValues();
                values.put(LiveDao.GIFT_COLUMN_ID, gift.getId());
                if (gift.getGname() != null) {
                    values.put(LiveDao.GIFT_COLUMN_NAME, gift.getGname());
                }
                if (gift.getGurl() != null) {
                    values.put(LiveDao.GIFT_COLUMN_URL, gift.getGurl());
                }
                if (gift.getGprice() != null) {
                    values.put(LiveDao.GIFT_COLUMN_PRICE, gift.getGprice());
                }
                db.replace(LiveDao.GIFT_TAB_NAME, null, values);
            }
        }
    }

    // 获取礼物列表
    public synchronized Map<Integer, Gift> getGiftList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<Integer, Gift> gifts = new HashMap<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + LiveDao.GIFT_TAB_NAME, null);
            while (cursor.moveToNext()) {
                int giftId = cursor.getInt(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_ID));
                String giftName = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_NAME));
                int giftPrice = cursor.getInt(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_PRICE));
                String giftUrl = cursor.getString(cursor.getColumnIndex(LiveDao.GIFT_COLUMN_URL));
                Gift gift = new Gift();
                gift.setId(giftId);
                gift.setGname(giftName);
                gift.setGurl(giftUrl);
                gift.setGprice(giftPrice);
                gifts.put(giftId, gift);
            }
            cursor.close();
        }
        return gifts;
    }
}
