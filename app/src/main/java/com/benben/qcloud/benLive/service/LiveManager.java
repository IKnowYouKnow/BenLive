package com.benben.qcloud.benLive.service;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.QavsdkApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LiveManager {
    private String appkey;
    LiveService service;
    private static LiveManager liveManager;
    private static final String TAG = "LiveManager";

    private LiveManager() {
        try {
            ApplicationInfo appInfo = QavsdkApplication.getInstance().getPackageManager().getApplicationInfo(
                    QavsdkApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            appkey = appInfo.metaData.getString("EASEMOB_APPKEY");
            appkey = appkey.replace("#","/");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("must set the easemob appkey");
        }
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
//                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://a1.easemob.com/"+appkey+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        service = retrofit1.create(LiveService.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(I.SERVER_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(LiveService.class);
    }

    static class RequestInterceptor implements Interceptor {

        @Override public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
//                    .header("Authorization", "Bearer " + EMClient.getInstance().getAccessToken())
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            okhttp3.Response response =  chain.proceed(request);
            return response;
        }
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
