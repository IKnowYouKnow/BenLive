package com.benben.qcloud.benLive.views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.presenters.LoginHelper;
import com.benben.qcloud.benLive.presenters.ProfileInfoHelper;
import com.benben.qcloud.benLive.presenters.viewinface.LogoutView;
import com.benben.qcloud.benLive.presenters.viewinface.ProfileView;
import com.benben.qcloud.benLive.utils.Constants;
import com.benben.qcloud.benLive.utils.SxbLog;
import com.benben.qcloud.benLive.utils.UIUtils;
import com.benben.qcloud.benLive.views.customviews.LineControllerView;
import com.benben.qcloud.benLive.views.customviews.RadioGroupDialog;
import com.benben.qcloud.benLive.views.customviews.SpeedTestDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVContext;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.livesdk.ILVLiveManager;
import com.benben.qcloud.benLive.model.MySelfInfo;
import com.benben.qcloud.benLive.utils.GlideCircleTransform;
import com.benben.qcloud.benLive.views.customviews.CustomSwitch;

import java.util.List;



/**
 * 视频和照片输入页面
 */
public class FragmentProfile extends Fragment implements View.OnClickListener, LogoutView, ProfileView {
    private static final String TAG = "FragmentLiveList";
    private final String beautyTypes[] = new String[]{"内置美颜", "插件美颜"};
    private TextView mProfileName, mProfileId;
    private ImageView mAvatar, mEditProfile;
    private LoginHelper mLoginHeloper;
    private ProfileInfoHelper mProfileHelper;
    private LineControllerView mVersion, mSpeedTest, lcvLog, lcvBeauty, lcvQulity;
    private CustomSwitch csAnimator;


    public FragmentProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.benben.qcloud.benLive.R.layout.profileframent_layout, container, false);

        view.findViewById(com.benben.qcloud.benLive.R.id.tv_logout).setOnClickListener(this);
        mAvatar = (ImageView) view.findViewById(com.benben.qcloud.benLive.R.id.profile_avatar);
        mProfileName = (TextView) view.findViewById(com.benben.qcloud.benLive.R.id.profile_name);
        mProfileId = (TextView) view.findViewById(com.benben.qcloud.benLive.R.id.profile_id);
        mEditProfile = (ImageView) view.findViewById(com.benben.qcloud.benLive.R.id.edit_profile);
        mSpeedTest = (LineControllerView) view.findViewById(com.benben.qcloud.benLive.R.id.profile_speed_test);
        mVersion = (LineControllerView) view.findViewById(com.benben.qcloud.benLive.R.id.version);
        mEditProfile.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mSpeedTest.setOnClickListener(this);

        csAnimator = (CustomSwitch) view.findViewById(com.benben.qcloud.benLive.R.id.cs_animator);
        lcvLog = (LineControllerView) view.findViewById(com.benben.qcloud.benLive.R.id.lcv_set_log_level);
        lcvBeauty = (LineControllerView) view.findViewById(com.benben.qcloud.benLive.R.id.lcv_beauty_type);
        lcvQulity = (LineControllerView) view.findViewById(com.benben.qcloud.benLive.R.id.lcv_video_qulity);
        csAnimator.setOnClickListener(this);
        lcvLog.setOnClickListener(this);
        lcvBeauty.setOnClickListener(this);
        lcvQulity.setOnClickListener(this);

        lcvLog.setContent(MySelfInfo.getInstance().getLogLevel().toString());
        lcvBeauty.setContent(beautyTypes[MySelfInfo.getInstance().getBeautyType() & 0x1]);

        lcvQulity.setContent(Constants.SD_GUEST.equals(
                MySelfInfo.getInstance().getGuestRole()) ? getString(com.benben.qcloud.benLive.R.string.str_video_sd) : getString(com.benben.qcloud.benLive.R.string.str_video_ld));

        csAnimator.setChecked(MySelfInfo.getInstance().isbLiveAnimator(), false);

        mLoginHeloper = new LoginHelper(getActivity().getApplicationContext(), this);
        mProfileHelper = new ProfileInfoHelper(this);

        updateUserInfo(ILiveLoginManager.getInstance().getMyUserId(),
                ILiveLoginManager.getInstance().getMyUserId(), null);
        return view;
    }

    @Override
    public void onDestroy() {
        if (null != mLoginHeloper)
            mLoginHeloper.onDestory();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mProfileHelper) {
            mProfileHelper.getMyProfile();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void enterEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case com.benben.qcloud.benLive.R.id.edit_profile:
                enterEditProfile();
                break;
            case com.benben.qcloud.benLive.R.id.cs_animator:
                MySelfInfo.getInstance().setbLiveAnimator(!MySelfInfo.getInstance().isbLiveAnimator());
                MySelfInfo.getInstance().writeToCache(getContext());
                csAnimator.setChecked(MySelfInfo.getInstance().isbLiveAnimator(), true);
                break;
            case com.benben.qcloud.benLive.R.id.lcv_set_log_level:
                changeLogLevel();
                break;
            case com.benben.qcloud.benLive.R.id.lcv_beauty_type:
                changeBeautyType();
                break;
            case com.benben.qcloud.benLive.R.id.version:
                showSDKVersion();
                break;
            case com.benben.qcloud.benLive.R.id.lcv_video_qulity:
                showVideoQulity();
                break;
            case com.benben.qcloud.benLive.R.id.tv_logout:
                if (null != mLoginHeloper)
                    mLoginHeloper.standardLogout(MySelfInfo.getInstance().getId());
                break;
            case com.benben.qcloud.benLive.R.id.profile_speed_test:
                new SpeedTestDialog(getContext()).start();
                break;
        }
    }


    private void showSDKVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("IM SDK: " + TIMManager.getInstance().getVersion() + "\r\n"
                + "AV SDK: " + AVContext.getVersion()+ "\r\n"
                + "Live SDK: " + ILVLiveManager.getInstance().getVersion() + "\r\n"
                + "ILiveSDK: " + ILiveSDK.getInstance().getVersion());
        builder.show();
    }

    private String getAppVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ;
        return version;
    }


    @Override
    public void logoutSucc() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putBoolean("living", false);
        editor.apply();
        Toast.makeText(getContext(), "Logout and quite", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void logoutFail() {

    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {
        if (null == getContext()){
            return;
        }
        if (TextUtils.isEmpty(profile.getNickName())) {
            MySelfInfo.getInstance().setNickName(profile.getIdentifier());
        } else {
            MySelfInfo.getInstance().setNickName(profile.getNickName());
        }
        if (!TextUtils.isEmpty(profile.getFaceUrl())) {
            MySelfInfo.getInstance().setAvatar(profile.getFaceUrl());
        }
        MySelfInfo.getInstance().writeToCache(getContext());
        updateUserInfo(ILiveLoginManager.getInstance().getMyUserId(), MySelfInfo.getInstance().getNickName(),
                MySelfInfo.getInstance().getAvatar());
    }

    @Override
    public void updateUserInfo(int reqid, List<TIMUserProfile> profiles) {

    }

    private void changeLogLevel() {
        RadioGroupDialog voiceTypeDialog = new RadioGroupDialog(getContext(), SxbLog.getStringValues());
        voiceTypeDialog.setTitle(com.benben.qcloud.benLive.R.string.set_log_level);
        voiceTypeDialog.setSelected(SxbLog.getLogLevel().ordinal());
        voiceTypeDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SxbLog.d(TAG, "changeLogLevel->onClick item:" + position);
                MySelfInfo.getInstance().setLogLevel(SxbLog.SxbLogLevel.values()[position]);
                SxbLog.setLogLevel(MySelfInfo.getInstance().getLogLevel());
                lcvLog.setContent(MySelfInfo.getInstance().getLogLevel().toString());
                MySelfInfo.getInstance().writeToCache(getContext());
            }
        });
        voiceTypeDialog.show();
    }

    private void changeBeautyType() {
        RadioGroupDialog beautyTypeDialog = new RadioGroupDialog(getContext(), beautyTypes);
        beautyTypeDialog.setTitle(com.benben.qcloud.benLive.R.string.str_beauty_type);
        beautyTypeDialog.setSelected(MySelfInfo.getInstance().getBeautyType());
        beautyTypeDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SxbLog.d(TAG, "changeBeautyType->onClick item:" + position);
                MySelfInfo.getInstance().setBeautyType(position);
                MySelfInfo.getInstance().writeToCache(getContext());
                lcvBeauty.setContent(beautyTypes[MySelfInfo.getInstance().getBeautyType() & 0x1]);
            }
        });
        beautyTypeDialog.show();
    }

    private void showVideoQulity() {
        final String[] roles = new String[]{getString(com.benben.qcloud.benLive.R.string.str_video_sd), getString(com.benben.qcloud.benLive.R.string.str_video_ld)};
        final String[] values = new String[]{Constants.SD_GUEST, Constants.LD_GUEST};

        RadioGroupDialog roleDialog = new RadioGroupDialog(getContext(), roles);

        roleDialog.setTitle(com.benben.qcloud.benLive.R.string.str_video_qulity);
        if (Constants.SD_GUEST.equals(MySelfInfo.getInstance().getGuestRole())) {
            roleDialog.setSelected(0);
        } else if (Constants.LD_GUEST.equals(MySelfInfo.getInstance().getGuestRole())){
            roleDialog.setSelected(1);
        }
        roleDialog.setOnItemClickListener(new RadioGroupDialog.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SxbLog.d(TAG, "showVideoQulity->onClick item:" + position);
                MySelfInfo.getInstance().setGuestRole(values[position]);
                MySelfInfo.getInstance().writeToCache(getContext());
                lcvQulity.setContent(Constants.SD_GUEST.equals(
                        MySelfInfo.getInstance().getGuestRole()) ? getString(com.benben.qcloud.benLive.R.string.str_video_sd) : getString(com.benben.qcloud.benLive.R.string.str_video_ld));
            }
        });
        roleDialog.show();
    }

    private void updateUserInfo(String id, String name, String url){
        mProfileName.setText(name);
        mProfileId.setText("ID:" + id);
        if (TextUtils.isEmpty(url)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), com.benben.qcloud.benLive.R.drawable.default_avatar);
            Bitmap cirBitMap = UIUtils.createCircleImage(bitmap, 0);
            mAvatar.setImageBitmap(cirBitMap);
        } else {
            SxbLog.d(TAG, "profile avator: " + url);
            RequestManager req = Glide.with(getActivity());
            req.load(url).transform(new GlideCircleTransform(getActivity())).into(mAvatar);
        }
    }
}
