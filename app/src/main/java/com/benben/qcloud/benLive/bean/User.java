package com.benben.qcloud.benLive.bean;

/**
 * Created by Administrator on 2017/6/18.
 */

public class User {
    // 用户名、昵称、头像、签名
    private String userName;
    private String userNick;
    private String userAvatarPath;
    private String userSig;

    private String avatar;

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userAvatarPath='" + userAvatarPath + '\'' +
                ", userSig='" + userSig + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserAvatarPath() {
        return userAvatarPath;
    }

    public void setUserAvatarPath(String userAvatarPath) {
        this.userAvatarPath = userAvatarPath;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public User(String userName, String userNick, String userAvatarPath, String userSig) {

        this.userName = userName;
        this.userNick = userNick;
        this.userAvatarPath = userAvatarPath;
        this.userSig = userSig;
    }

    public User(String userName) {

        this.userName = userName;
    }

    public String getAvatar() {
        if (avatar == null) {
            String path = "";
            avatar = path;
        }
        return avatar;
    }
}
