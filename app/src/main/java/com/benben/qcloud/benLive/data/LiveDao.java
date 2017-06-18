package com.benben.qcloud.benLive.data;

import com.benben.qcloud.benLive.gift.bean.Gift;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/18.
 */

public class LiveDao {
    public static final String GIFT_TAB_NAME = "t_benLive_gift";
    public static final String GIFT_COLUMN_ID = "m_gift_id";
    public static final String GIFT_COLUMN_NAME = "m_gift_name";
    public static final String GIFT_COLUMN_URL = "m_gift_url";
    public static final String GIFT_COLUMN_PRICE = "m_gift_price";

    public LiveDao(){}

    public void setGiftList(List<Gift> list) {
        LiveDBManager.getInstace().saveGiftList(list);
    }

    public Map<Integer, Gift> getGiftList() {
        return LiveDBManager.getInstace().getGiftList();
    }


}
