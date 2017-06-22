package com.benben.qcloud.benLive.service;

import android.util.Log;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.bean.User;
import com.benben.qcloud.benLive.gift.bean.Gift;
import com.benben.qcloud.benLive.gift.bean.GiftCount;
import com.benben.qcloud.benLive.gift.bean.RechargeStatements;
import com.benben.qcloud.benLive.gift.bean.Wallet;
import com.benben.qcloud.benLive.utils.ResultUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
//                .addInterceptor(httpLoggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(I.SERVER_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
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
        if (result != null && result.isRetMsg()) {
            return result.getRetData();
        }
        return null;
    }

    /**
     * 加载所有礼物
     *
     * @return 礼物集合
     */
    public List<Gift> loadGiftList() {
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
            if (gifts != null && gifts.isRetMsg()) {
                return gifts.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            if (wallet != null && wallet.isRetMsg()) {
                return wallet.getRetData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户给主播送礼物
     * @param username 用户名
     * @param anchor 主播ID
     * @param giftId 礼物ID
     * @param giftNum 礼物数量
     * @return
     */
    public Wallet givingGifts(String username, String anchor, int giftId, int giftNum) {
        Call<String> call = service.givingGifts(username, anchor, giftId, giftNum);
        Result<Wallet> result;
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "givingGifts: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            result = ResultUtils.getResultFromJson(res.body(), Wallet.class);
            if (result != null && result.isRetMsg()) {
                return result.getRetData();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页加载充值流水
     * @param username 用户名
     * @param pageId 页码
     * @param pageSize 每页的条数
     * @return
     */
    public List<RechargeStatements> getRechargeStatements(String username, int pageId, int pageSize) {
        Result<List<RechargeStatements>> result;
        Call<String> call = service.getRechargeStatements(username, pageId, pageSize);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "getRechargeStatements: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            String body = res.body();
            result = ResultUtils.getListResultFromJson(body, RequestInterceptor.class);
            return result.getRetData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页获取送礼物流水
     * @param username 用户名
     * @param pageId 页码
     * @param pageSize 每页显示的条数
     * @return
     */
    public List<GiftCount> getGivingGiftStatements(String username, int pageId, int pageSize) {
        Result<List<GiftCount>> result;
        Call<String> call = service.getGivingGiftStatements(username, pageId, pageSize);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "getGivingGiftStatements: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            result = ResultUtils.getListResultFromJson(res.body(), GiftCount.class);
            if (result != null && result.isRetMsg()) {
                return result.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页获取收礼物流水
     * @param username 用户名
     * @param pageId 页码
     * @param pageSize 每页显示的条数
     * @return
     */
    public List<GiftCount> getReceivingGiftStatementsServlet(String username, int pageId, int pageSize) {
        Result<List<GiftCount>> result;
        Call<String> call = service.getReceivingGiftStatementsServlet(username, pageId, pageSize);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "getReceivingGiftStatementsServlet: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            result = ResultUtils.getListResultFromJson(res.body(), GiftCount.class);
            if (result != null && result.isRetMsg()) {
                return result.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 统计主播礼物信息
     * @param anchorName 主播ID
     * @return
     */
    public List<GiftCount> getGiftStatementsByAnchor(String anchorName) {
        Result<List<GiftCount>> result;
        Call<String> call = service.getGiftStatementsByAnchor(anchorName);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "getGiftStatementsByAnchor: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }

            result = ResultUtils.getListResultFromJson(res.body(), GiftCount.class);
            if (result != null && result.isRetMsg()) {
                return result.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 账户充值
     * @param username 用户名
     * @param rmb 充值金额
     * @return
     */
    public Wallet recharge(String username, int rmb) {
        Result<Wallet> result;
        Call<String> call = service.recharge(username, rmb);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "recharge: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return null;
            }
            result = ResultUtils.getResultFromJson(res.body(), Wallet.class);
            if (result != null && result.isRetMsg()) {
                return result.getRetData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册
     * @param username 用户名
     * @param nick 昵称
     * @param password 密码
     * @return
     */
    public boolean register(String username, String nick, String password) {
        Call<String> call = service.register(username, nick, password);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "register: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return false;
            }
            Result result = ResultUtils.getResultFromJson(res.body(), User.class);
            if (result != null) {
                return result.isRetMsg();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消注册
     * @param username 用户名
     * @return
     */
    public boolean unRegister(String username) {
        Call<String> call = service.unregister(username);
        try {
            Response<String> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "unRegister: code = " + res.code()
                        + "error = " + res.errorBody().toString());
                return false;
            }
            Result result = ResultUtils.getResultFromJson(res.body(), User.class);
            if (result != null) {
                return result.isRetMsg();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

