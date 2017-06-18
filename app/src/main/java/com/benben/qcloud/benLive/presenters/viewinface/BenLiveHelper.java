package com.benben.qcloud.benLive.presenters.viewinface;

import android.content.Context;

import com.benben.qcloud.benLive.bean.User;
import com.benben.qcloud.benLive.model.IUserModel;
import com.benben.qcloud.benLive.model.MySelfInfo;
import com.benben.qcloud.benLive.model.UserModel;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/18.
 */

public class BenLiveHelper {
    private static final String TAG = "BenLiveHelper";
    private static BenLiveHelper instance = null;
    private String username;
    private User currentUser;
    private Map<String, User> contactList;
    IUserModel model;


    private BenLiveHelper() {
    }

    public synchronized static BenLiveHelper getInstance() {
        if (instance == null) {
            instance = new BenLiveHelper();
        }
        return instance;
    }

    public void init(final Context context) {
        model = new UserModel();
    }

    // 获取用户信息
    public User getUserInfo(String username) {
        User user = null;
        if (username.equals(MySelfInfo.getInstance().getId())) {
            return getCurrentUserInfo();
        }
        user = getContactList().get(username);
        if (user == null) {
            user = new User(username);
        }
        return user;
    }

    // 获取当前用户信息
    public synchronized User getCurrentUserInfo() {
        if (currentUser == null) {
            String username = MySelfInfo.getInstance().getId();
            currentUser = new User(username);
            String nick = getCurrentNick();
            currentUser.setUserNick((nick != null) ? nick : username);
            currentUser.setUserAvatarPath(getcurrentUserAvatar());
        }
        return currentUser;
    }

    // 获取当前用户头像地址
    private String getcurrentUserAvatar() {
        return MySelfInfo.getInstance().getAvatar();
    }

    // 获取当前用户昵称
    private String getCurrentNick() {
        return MySelfInfo.getInstance().getNickName();
    }

    // 获取当前用户名
    public String getCurrentUserName(Context context) {
        if (username == null) {
            username = MySelfInfo.getInstance().getId();
        }
        return username;
    }

    // 设置当前用户名
    public void setCurrentUserName(String username) {
        this.username = username;
    }

    // 设置当前用户头像
    public void setCurrentUserAvatar(String avatar) {
        getCurrentUserInfo().setUserAvatarPath(avatar);
        MySelfInfo.getInstance().setAvatar(avatar);
    }
    // 设置当前用户昵称
    public void setCurrentUserNick(String nick) {
        getCurrentUserInfo().setUserNick(nick);
        MySelfInfo.getInstance().setNickName(nick);
    }

    // 同步用户信息
    public void syncUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO 从网络加载用户信息
            }
        }).start();
    }

    // 获取用户信息集合
    public Map<String, User> getContactList(){
        if (contactList == null) {
            contactList = new HashMap<>();
        }
        return contactList;
    }

    // 保存好用户信息到本地内存
    public void saveContactList(User user) {
        getContactList().put(user.getUserName(), user);
    }


    // 重置用户信息
    public synchronized void reset(Context context) {
        currentUser = null;
        MySelfInfo.getInstance().clearCache(context);
    }
}
