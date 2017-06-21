package com.benben.qcloud.benLive.service;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.QavsdkApplication;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.bean.User;
import com.benben.qcloud.benLive.gift.bean.Gift;
import com.benben.qcloud.benLive.utils.ResultUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
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
            appkey = appkey.replace("#", "/");
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
                .baseUrl("http://a1.easemob.com/" + appkey + "/")
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

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
//                    .header("Authorization", "Bearer " + EMClient.getInstance().getAccessToken())
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            okhttp3.Response response = chain.proceed(request);
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

    // 获取礼物列表
    public List<Gift> getAllGifts() {
        final Call<String> allGifts = service.getAllGifts();
        try {
            Result<List<Gift>> result = HandleSyncRequesttoList(allGifts, Gift.class);
            if (result != null & result.isRetMsg()) {
                List<Gift> gifts = result.getRetData();
                return gifts;
            }
        } catch (LiveException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User loadUserInfoFromService(String username) {
        Call<String> call = service.loadUserInfo(username);
        Result<User> result = null;
        try {
            result = HandleSyncRequest(call, User.class);
        } catch (LiveException e) {
            e.printStackTrace();
        }
        if (result != null && result.isRetMsg()) {
            return result.getRetData();
        }
        return null;
    }

    /**
     * String s = "";
     * gson.fromJson(s, new TypeToken<User>() {}.getType());
     */
    private <T, A> T getT(Call<A> responseCall) {
        Result<T> result = (Result<T>) HandleSyncT(responseCall);
        Log.e(TAG, "getT: result = " + result);
        if (result != null && result.isRetMsg()) {
            return result.getRetData();
        } else {
            Toast.makeText(QavsdkApplication.getContext(), "Failed", Toast.LENGTH_SHORT).show();
            ;
        }
        return null;
    }

    private <T> T HandleSyncT(Call<T> responseCall, String faild) {
        try {
            Response<T> res = responseCall.execute();
            if (!res.isSuccessful()) {
                Toast.makeText(QavsdkApplication.getContext(), "isFail", Toast.LENGTH_SHORT).show();
            }
            T t = res.body();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T HandleSyncT(Call<T> responseCall) {
        T t = null;
        try {
            Response<T> res = responseCall.execute();
            if (!res.isSuccessful()) {
                Toast.makeText(QavsdkApplication.getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
            t = res.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    private <T> T HandleRequest(Call<String> responseCall, Class<T> clazz) {
        T t = null;
        try {
            Response<String> res = responseCall.execute();
            String body = res.body();
            Result result = ResultUtils.getResultFromJson(body, clazz);
            if (result != null && result.isRetMsg()) {
                t = (T) result.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    private <T> Result<T> HandleSyncRequest(Call<String> responseCall, Class<T> clazz) throws LiveException {
        try {
            Response<String> res = responseCall.execute();
            if (!res.isSuccessful()) {
                throw new LiveException(res.code(), res.errorBody().string());
            }
            String body = res.body();
            Result result = ResultUtils.getResultFromJson(body, clazz);
            return result;
        } catch (IOException e) {
            throw new LiveException(e.getMessage());
        }
    }

    private <T> Result<List<T>> HandleSyncRequesttoList(Call<String> responseCall, Class<T> clazz) throws LiveException {
        try {
            Response<String> res = responseCall.execute();
            if (!res.isSuccessful()) {
                throw new LiveException(res.code(), res.errorBody().string());
            }
            String body = res.body();
            Result result = ResultUtils.getListResultFromJson(body, clazz);
            return result;
        } catch (IOException e) {
            throw new LiveException(e.getMessage());
        }
    }

    private <T> Response<T> handleResponseCall(Call<T> responseCall) throws LiveException {
        try {
            Response<T> response = responseCall.execute();
            if (!response.isSuccessful()) {
                throw new LiveException(response.code(), response.errorBody().string());
            }
            return response;
        } catch (IOException e) {
            throw new LiveException(e.getMessage());
        }
    }

    private RequestBody jsonToRequestBody(String jsonStr) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
    }
}
