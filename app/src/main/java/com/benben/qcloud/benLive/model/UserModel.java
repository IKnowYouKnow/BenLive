package com.benben.qcloud.benLive.model;

import android.content.Context;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/6/17.
 */

public class UserModel implements IUserModel {

    private static final String TAG = "UserModel";
    @Override
    public void register(Context context, String username, String nickname, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addFormParam(I.User.USER_NAME,username)
                .addFormParam(I.User.NICK,nickname)
                .addFormParam(I.User.PASSWORD,password)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void unregister(Context context, String username, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void getUserInfo(Context context, String username, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl("personal_show")
                .addParam("user_name",username)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void getRechargerList(Context context, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl("servicer")
                .targetClass(String.class)
                .execute(listener);
    }


}
