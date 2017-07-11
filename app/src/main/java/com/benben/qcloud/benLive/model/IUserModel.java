package com.benben.qcloud.benLive.model;

import android.content.Context;
/**
 * Created by Administrator on 2017/6/17.
 */

public interface IUserModel {
    void register(Context context, String username, String nickname, String password,
                  OnCompleteListener<String> listener);

    void unregister(Context context, String username, OnCompleteListener<String> listener);

    void getUserInfo(Context context, String username, OnCompleteListener<String> listener);

    void getRechargerList(Context context, OnCompleteListener<String> listener);
}
