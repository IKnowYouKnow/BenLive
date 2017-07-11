package com.benben.qcloud.benLive.service;

import android.util.Log;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.bean.User;
import com.benben.qcloud.benLive.gift.bean.Gift;
import com.benben.qcloud.benLive.gift.bean.Wallet;
import com.benben.qcloud.benLive.utils.ResultUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by Administrator on 2017/6/19.
 */

public class LiveManager {
    private String appkey;
    LiveService service;
    private static LiveManager liveManager;
    private static final String TAG = "LiveManager";

    private LiveManager() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
//                .addInterceptor(httpLoggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(I.SERVER_ROOT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient)
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

    /**
     * 从服务器加载用户信息
     *
     * @param username 用户ID
     * @return 用户信息
     */

    public User loadUserInfoFromService(String username) {
        Call<String> call = service.loadUserInfo(username);
        Result<User> result = null;
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "loadUserInfoFromService: code = " + res.code()
                        + "errorBody = " + res.errorBody().toString());
                return null;
            }
            String body = res.body();
            result = ResultUtils.getResultFromJson(body, User.class);
//            result = HandleSyncRequest(call, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.getRetData();
        }
        return null;
    }

    /**
     * 加载所有礼物
     *
     * @return 礼物集合
     */
    public List<Gift> getGiftList() {
        Call<String> call = service.getAllGifts();
        Result<List<Gift>> gifts;
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "loadGiftList: code = " + res.code()
                        + "errorBody = " + res.errorBody().toString());
                return null;
            }
            String body = res.body();
            gifts = ResultUtils.getListResultFromJson(body, Gift.class);
            if (gifts != null && gifts.getRetCode() == 0) {
                return gifts.getRetData();
            }else {
                Log.e(TAG, "getGiftList: error = " + gifts.getRetData());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getGiftList: 4" +e.getMessage().toString());
        }
        return null;
    }

    /**
     * 根据用户名获取账户余额
     * @param username 用户名
     * @return
     */
    public Wallet getBalance(String username) {
        Result<Wallet> wallet = null;
        Call<String> call = service.getBalance(username);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "getBalance: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            wallet = ResultUtils.getResultFromJson(res.body(), Wallet.class);
            if (wallet != null) {
                return wallet.getRetData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册账号
     * @param username 手机号
     * @param password 密码
     * @param invitCode 邀请码
     * @param invitStatus 邀请码开关
     * @param tenchRes 腾讯注册结果
     * @return
     */
    public Result register(String username,
                            String password, String invitCode,
                            String invitStatus, String tenchRes) {
        Call<String> call = service.register(username, password, invitCode, invitStatus, tenchRes);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "register: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            Log.e(TAG, "register: result = " + ResultUtils.getStringFromJson(res.body()));
            return ResultUtils.getStringFromJson(res.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 忘记密码
     * @param phoneNum 手机号
     * @param password 新密码
     * @return
     */
    public Result forgetPwd(String phoneNum, String password) {
        Call<String> call = service.forgetPwd(phoneNum, password);
        try {
            Response<String> response = call.execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "forgetPwd: response.error = " + response.errorBody());
                return null;
            }
            Log.e(TAG, "forgetPwd: response.body = " + response.body());
            Result res = ResultUtils.getStringFromJson(response.body());
            Log.e(TAG, "forgetPwd: res = " + res);
            if (res != null) {
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 发送手机验证码
     * @param mobile 手机号
     * @param code 随机生成的验证码
     * @return
     */
    public int sendCheckCode(String mobile, String code) {
        Call<String> call = service.sendCheckCode(mobile, code);
        try {
            Response<String> response = call.execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "sendCheckCode: error = " + response.errorBody());
                return -1;
            }
            return Integer.parseInt(response.body());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String login(String userId, String pwd) {
        Call<String> call = service.login(userId, pwd);
        try {
            Response<String> response = call.execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "login: error = " + response.errorBody());
                return null;
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 检查是否需要注册
     * @return
     */
    public int checkIsRegister() {
        Call<String> call = service.checkIsRegister();
        try {
            Response<String> response = call.execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "checkIsRegister: error = " + response.errorBody());
                return 2;
            }

            String body = response.body();
            Log.e(TAG, "checkIsRegister: body = " + body);
            JSONObject obj = new JSONObject(body);
            int code = obj.getInt("code");
            return code;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public int anchorCheck(String username, String phoneNum, String userId, String weChatId,
                           String aliPayId, String aliPayName){
        Call<String> call = service.anchorCheck(username, phoneNum, userId, weChatId, aliPayId, aliPayName);
        try {
            Response<String> response = call.execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "anchorCheck: error = " + response.errorBody());
                return -1;
            }
            String json = response.body();
            JSONObject obj = new JSONObject(json);
            int code = obj.getInt("code");
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

