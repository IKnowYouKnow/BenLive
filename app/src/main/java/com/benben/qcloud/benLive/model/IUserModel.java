package com.benben.qcloud.benLive.model;

import android.content.Context;
/**
 * Created by Administrator on 2017/6/17.
 */

public interface IUserModel {
    void register(Context context, String username, String nickname, String password,
                  OnCompleteListener listener);

    void unregister(Context context, String username, OnCompleteListener listener);

    void getUserInfo(Context context, String username, OnCompleteListener listener);
}