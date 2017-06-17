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
    public void register(Context context, String username, String nickname, String password, OnCompleteListener listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .post()
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nickname)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .execute(listener);


//        com.zhy.http.okhttp.OkHttpUtils
//                .post()
//                .url(I.SERVER_ROOT+I.REQUEST_REGISTER)
//                .addParams(I.User.USER_NAME,username)
//                .addParams(I.User.NICK,nickname)
//                .addParams(I.User.PASSWORD,password)
//                .build()
//                .execute(new Callback() {
//                    @Override
//                    public Object parseNetworkResponse(Response response, int id) throws Exception {
//                        Log.e(TAG, "parseNetworkResponse: body = "+response.body());
//                        return response.body();
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Object response, int id) {
//
//                    }
//                });

    }

    @Override
    public void unregister(Context context, String username, OnCompleteListener listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void getUserInfo(Context context, String username, OnCompleteListener listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl("personal_show")
                .addParam("user_name",username)
                .targetClass(String.class)
                .execute(listener);
    }
}
