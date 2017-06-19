package com.benben.qcloud.benLive.service;

import android.util.Log;

import com.benben.qcloud.benLive.I;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LiveManager {
    LiveService service;
    private static LiveManager liveManager;
    private static final String TAG = "LiveManager";

    private LiveManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(I.SERVER_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(LiveService.class);
    }

    public static LiveManager get() {
        if (liveManager == null) {
            synchronized (LiveManager.class) {
                if (liveManager == null) {
                    liveManager = new LiveManager();
                }
            }
        }
        return liveManager;
    }

    public String register(String username, String usernick, String password) {
        Call<String> call = service.register(username, usernick, password);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "register: 注册失败");
            }
            return res.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getUserInfo(String username) {
        Call<String> call = service.loadUserInfo(username);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                Log.e(TAG, "onResponse: body = "+body);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: t = " + t);
            }
        });
    }
}
