package com.benben.qcloud.benLive.views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.model.CurLiveInfo;
import com.benben.qcloud.benLive.model.MySelfInfo;
import com.benben.qcloud.benLive.presenters.InitBusinessHelper;
import com.benben.qcloud.benLive.presenters.LoginHelper;
import com.benben.qcloud.benLive.presenters.ProfileInfoHelper;
import com.benben.qcloud.benLive.presenters.viewinface.ProfileView;
import com.benben.qcloud.benLive.utils.Constants;
import com.benben.qcloud.benLive.utils.SxbLog;
import com.benben.qcloud.benLive.views.customviews.BaseFragmentActivity;
import com.benben.qcloud.benLive.views.customviews.NotifyDialog;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveSDK;

import java.util.List;

/**
 * 主界面
 */
public class HomeActivity extends BaseFragmentActivity implements ProfileView,View.OnClickListener {
    private ProfileInfoHelper infoHelper;
    private LoginHelper mLoginHelper;
    ImageView liveList,liveOpen,myProfile;
    private static final String TAG = HomeActivity.class.getSimpleName();

    FrameLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        initState();
        SxbLog.i(TAG, "HomeActivity onStart");
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        boolean living = pref.getBoolean("living", false);
        liveList = (ImageView) findViewById(R.id.liveList);
        liveOpen = (ImageView) findViewById(R.id.liveOpen);
        myProfile = (ImageView) findViewById(R.id.myProfile);
        mLayout = (FrameLayout) findViewById(R.id.layout);

        liveList.setOnClickListener(this);
        liveOpen.setOnClickListener(this);
        myProfile.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanel,new FragmentList()).commit();
        liveList.setImageResource(R.drawable.shouye_select);


        // 检测是否需要获取头像
        if (TextUtils.isEmpty(MySelfInfo.getInstance().getAvatar())) {
            infoHelper = new ProfileInfoHelper(this);
            infoHelper.getMyProfile();
        }
        if (living) {
            NotifyDialog dialog = new NotifyDialog();
            dialog.show(getString(R.string.title_living), getSupportFragmentManager(), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HomeActivity.this, LiveActivity.class);
                    MySelfInfo.getInstance().setIdStatus(Constants.HOST);
                    MySelfInfo.getInstance().setJoinRoomWay(true);

                    CurLiveInfo.setHostID(MySelfInfo.getInstance().getNickName()==null?
                            MySelfInfo.getInstance().getId():MySelfInfo.getInstance().getNickName());
                    CurLiveInfo.setHostName(MySelfInfo.getInstance().getNickName()==null?
                    MySelfInfo.getInstance().getId():MySelfInfo.getInstance().getNickName());
                    CurLiveInfo.setHostAvator(MySelfInfo.getInstance().getAvatar());
                    CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
                    startActivity(intent);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("living", false);
                    editor.apply();
                }
            });
        }
    }
    private void initState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
    }
    @Override
    protected void onStart() {
        SxbLog.i(TAG, "HomeActivity onStart");
        super.onStart();
        if (ILiveSDK.getInstance().getAVContext() == null) {//retry
            InitBusinessHelper.initApp(getApplicationContext());
            SxbLog.i(TAG, "HomeActivity retry login");
            mLoginHelper = new LoginHelper(this);
            mLoginHelper.iLiveLogin(MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getUserSig());
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoginHelper != null)
            mLoginHelper.onDestory();
        SxbLog.i(TAG, "HomeActivity onDestroy");
        super.onDestroy();
    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {
        SxbLog.i(TAG, "updateProfileInfo");
        if (null != profile) {
            MySelfInfo.getInstance().setAvatar(profile.getFaceUrl());
            if (!TextUtils.isEmpty(profile.getNickName())) {
                MySelfInfo.getInstance().setNickName(profile.getNickName());
            } else {
                MySelfInfo.getInstance().setNickName(profile.getIdentifier());
            }
        }
    }

    @Override
    public void updateUserInfo(int reqid, List<TIMUserProfile> profiles) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liveList:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new FragmentList())
                .commit();
                liveList.setImageResource(R.drawable.shouye_select);
                myProfile.setImageResource(R.drawable.wode);
                break;
            case R.id.liveOpen:
                startActivity(new Intent(HomeActivity.this,PublishLiveActivity.class));
                break;
            case R.id.myProfile:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new FragmentProfile())
                        .commit();
                myProfile.setImageResource(R.drawable.wode_select);
                liveList.setImageResource(R.drawable.shouye);
                break;
        }
    }
}
