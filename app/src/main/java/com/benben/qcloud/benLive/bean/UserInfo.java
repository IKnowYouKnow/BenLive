package com.benben.qcloud.benLive.bean;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/7/6.
 */

public class UserInfo {
    private String phone_data;
    private String original_password;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + phone_data + '\'' +
                ", original_password='" + original_password + '\'' +
                '}';
    }

    public String getId() {
        return phone_data;
    }

    public void setId(String id) {
        this.phone_data = id;
    }

    public String getOriginal_password() {
        return original_password;
    }

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }

    public UserInfo(String id, String original_password) {

        this.phone_data = id;
        this.original_password = original_password;
    }
}
