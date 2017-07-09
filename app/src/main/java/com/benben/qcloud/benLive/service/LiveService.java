package com.benben.qcloud.benLive.service;

import com.benben.qcloud.benLive.I;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by clawpo on 2017/6/7.
 */

public interface LiveService {
    /** 客户端发送的全部礼物信息并展示，包括礼物的名称、图片地址和价格的请求 */
    @GET("getallgift")
    Call<String> getAllGifts();

    /** 客户端发送的根据用户名获取账户余额的请求 */
    @GET("live/getBalance")
    Call<String> getBalance(@Query("uname") String username);

    /** 客户端发送的查询所有直播间的请求 */
    @GET("live/getAllChatRoom")
    Call<String> getAllChatRoom();

    /** 客户端发送的创建直播室 */
    @GET("live/createChatRoom")
    Call<String> createChatRoom(
            @Query("auth") String auth,
            @Query("name") String name,
            @Query("description") String description,
            @Query("owner") String owner,
            @Query("maxusers") String maxusers,
            @Query("members") String members
    );

    /** 客户端发送的删除直播室 */
    @GET("live/deleteChatRoom")
    Call<String> deleteChatRoom(
            @Query("auth") String auth,
            @Query("chatRoomId") String chatRoomId
    );

    /** 加载用户数据 */
    @GET("personal_show")
    Call<String> loadUserInfo(@Query(I.User.USER_NAME) String username);

    /** 注册用户 */
    @Multipart
    @POST("register")
    Call<String> register(
            @Query(I.User.PHONE_NUM) String username,
            @Query(I.User.PASSWORD) String password,
            @Query(I.User.INVITE_CODE) String inviteCode,
            @Query("type") String inviteStatus,
            @Query("step") String tenchRes);

    /** 登录 */
    @GET("login")
    Call<String> login(@Query(I.User.PHONE_NUM) String phoneNum,
                       @Query(I.User.PASSWORD) String password);
    /** 忘记密码 */
    @GET("mod_password")
    Call<String> forgetPwd(@Query(I.User.PHONE_NUM) String phoneNum,
                           @Query(I.User.PASSWORD) String password);

    /**
     * 获取手机验证码
     */
    @GET("sendsms")
    Call<String> sendCheckCode(@Query("mobile") String phone,
                               @Query("code") String code);

    /**
     * 检查注册通道是否开启
     */
    @GET("check_invite")
    Call<String> checkIsRegister();
}
