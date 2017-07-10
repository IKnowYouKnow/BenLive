package com.benben.qcloud.benLive.views;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.data.LiveDao;
import com.benben.qcloud.benLive.model.MySelfInfo;
import com.benben.qcloud.benLive.presenters.LoginHelper;
import com.benben.qcloud.benLive.presenters.ProfileInfoHelper;
import com.benben.qcloud.benLive.presenters.UploadHelper;
import com.benben.qcloud.benLive.presenters.viewinface.LogoutView;
import com.benben.qcloud.benLive.presenters.viewinface.ProfileView;
import com.benben.qcloud.benLive.presenters.viewinface.UploadView;
import com.benben.qcloud.benLive.utils.Constants;
import com.benben.qcloud.benLive.utils.GlideCircleTransform;
import com.benben.qcloud.benLive.utils.SxbLog;
import com.benben.qcloud.benLive.utils.UIUtils;
import com.benben.qcloud.benLive.views.customviews.CustomSwitch;
import com.benben.qcloud.benLive.views.customviews.LineControllerView;
import com.benben.qcloud.benLive.views.customviews.RadioGroupDialog;
import com.benben.qcloud.benLive.views.customviews.WalletActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * 视频和照片输入页面
 */
public class FragmentProfile extends Fragment implements View.OnClickListener, LogoutView,
        ProfileView, UploadView {
    private static final String TAG = "FragmentLiveList";

    private final static int REQ_EDIT_NICKNAME = 0x100;
    private final static int REQ_EDIT_SIGN  = 0x200;
    private final static int REQ_FEEDBACK  = 0x300;
    private Uri iconUrl, iconCrop;
    private ProfileInfoHelper mProfileInfoHelper;
    private UploadHelper mUploadHelper;

    private static final int CROP_CHOOSE = 10;
    private static final int CAPTURE_IMAGE_CAMERA = 100;
    private static final int IMAGE_STORE = 200;
    private final String beautyTypes[] = new String[]{"内置美颜", "插件美颜"};
    private TextView mProfileName, mProfileId;
    private ImageView mAvatar, mEditProfile, mSex;
    private LoginHelper mLoginHeloper;
    private ProfileInfoHelper mProfileHelper;
    private LineControllerView lcvQulity, mFeedBack;
    private CustomSwitch csAnimator;
    // 我的钱包
    private LineControllerView myWallet;
    private boolean bPermission = false;


    LiveDao dao = new LiveDao();
    public FragmentProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileInfoHelper = new ProfileInfoHelper(this);
        mUploadHelper = new UploadHelper(getContext(), this);
        bPermission = checkCropPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profileframent_layout, container, false);
        view.findViewById(R.id.tv_logout).setOnClickListener(this);
        mAvatar = (ImageView) view.findViewById(R.id.profile_avatar);
        mProfileName = (TextView) view.findViewById(R.id.profile_name);
        mProfileId = (TextView) view.findViewById(R.id.profile_id);
        mEditProfile = (ImageView) view.findViewById(R.id.edit_profile);
        mSex = (ImageView) view.findViewById(R.id.profile_sex);
        mEditProfile.setOnClickListener(this);

        // 设置我的钱包点击事件
        myWallet = (LineControllerView) view.findViewById(R.id.lcv_my_wallet);
        myWallet.setOnClickListener(this);

        // 用户反馈
        mFeedBack = (LineControllerView) view.findViewById(R.id.lcv_suggest);
        mFeedBack.setOnClickListener(this);
        // 设置头像的点击事件
        mAvatar.setOnClickListener(this);
        csAnimator = (CustomSwitch) view.findViewById(R.id.cs_animator);
        lcvQulity = (LineControllerView) view.findViewById(R.id.lcv_video_qulity);
        csAnimator.setOnClickListener(this);
        lcvQulity.setOnClickListener(this);

        // 美颜方案
//        lcvBeauty.setContent(beautyTypes[MySelfInfo.getInstance().getBeautyType() & 0x1]);

        lcvQulity.setContent(Constants.SD_GUEST.equals(
                MySelfInfo.getInstance().getGuestRole()) ? getString(R.string.str_video_sd) : getString(R.string.str_video_ld));

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
        mUploadHelper.onDestory();

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
            case R.id.profile_avatar:
                showPhotoDialog();
                break;
            case R.id.edit_profile:
                enterEditProfile();
                break;
            case R.id.cs_animator:
                MySelfInfo.getInstance().setbLiveAnimator(!MySelfInfo.getInstance().isbLiveAnimator());
                MySelfInfo.getInstance().writeToCache(getContext());
                csAnimator.setChecked(MySelfInfo.getInstance().isbLiveAnimator(), true);
                break;
            /*case R.id.lcv_beauty_type:
                changeBeautyType();
                break;*/
            case R.id.lcv_video_qulity:
                showVideoQulity();
                break;
            case R.id.tv_logout:
                if (null != mLoginHeloper)
                    mLoginHeloper.standardLogout(MySelfInfo.getInstance().getId());
                break;
            case R.id.lcv_suggest:
                EditActivity.navToEdit(this, "用户反馈", "请输入对我们的意见，我们将会做到更好", REQ_FEEDBACK);
                break;
            // 点击我的钱包跳转到钱包界面
            case R.id.lcv_my_wallet:
                startActivity(new Intent(getContext(),WalletActivity.class));
                break;
        }
    }

    public void showPhotoDialog() {
        final Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.pic_choose);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int)(display.getWidth()); //设置宽度

        pickDialog.getWindow().setAttributes(lp);

        TextView camera = (TextView) pickDialog.findViewById(R.id.chos_camera);
        TextView picLib = (TextView) pickDialog.findViewById(R.id.pic_lib);
        TextView cancel = (TextView) pickDialog.findViewById(R.id.btn_cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFrom(CAPTURE_IMAGE_CAMERA);
                pickDialog.dismiss();
            }
        });

        picLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFrom(IMAGE_STORE);
                pickDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDialog.dismiss();
            }
        });

        pickDialog.show();
    }

    /**
     * 获取图片资源
     *
     * @param type
     */
    private void getPicFrom(int type) {
        if (!bPermission){
            Toast.makeText(getActivity(), getString(com.benben.qcloud.benLive.R.string.tip_no_permission), Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case CAPTURE_IMAGE_CAMERA:
                Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                iconUrl = createCoverUri("_icon");
                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, iconUrl);
                startActivityForResult(intent_photo, CAPTURE_IMAGE_CAMERA);
                break;
            case IMAGE_STORE:
                iconUrl = createCoverUri("_select_icon");
                Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                intent_album.setType("image/*");
                startActivityForResult(intent_album, IMAGE_STORE);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            SxbLog.e(TAG, "onActivityResult->failed for request: " + requestCode + "/" + resultCode);
            return;
        }
        switch (requestCode){
            case REQ_EDIT_NICKNAME:
                mProfileInfoHelper.setMyNickName(data.getStringExtra(EditActivity.RETURN_EXTRA));
                break;
            case REQ_EDIT_SIGN:
                mProfileInfoHelper.setMySign(data.getStringExtra(EditActivity.RETURN_EXTRA));
                break;
            case CAPTURE_IMAGE_CAMERA:
                startPhotoZoom(iconUrl);
                break;
            case REQ_FEEDBACK:
                Toast.makeText(getContext(), "感谢您的反馈，我们会继续努力的", Toast.LENGTH_SHORT).show();
                break;
            case IMAGE_STORE:
                String path = UIUtils.getPath(getActivity(), data.getData());
                if (null != path){
                    SxbLog.e(TAG, "startPhotoZoom->path:" + path);
                    File file = new File(path);
                    startPhotoZoom(UIUtils.getUriFromFile(getActivity(), file));
                }
                break;
            case CROP_CHOOSE:
                mUploadHelper.uploadCover(iconCrop.getPath());
                break;
        }
    }
    public void startPhotoZoom(Uri uri) {
        iconCrop = createCoverUri("_icon_crop");

        getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 300);
        intent.putExtra("aspectY", 300);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconCrop);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_CHOOSE);

    }
    private Uri createCoverUri(String type) {
        String filename = MySelfInfo.getInstance().getId()+ type + ".jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(), filename);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSION_REQ_CODE);
            return null;
        }
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return UIUtils.getUriFromFile(getActivity(), outputImage);
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
        updateUserInfo(MySelfInfo.getInstance().getSign(), MySelfInfo.getInstance().getNickName(),
                MySelfInfo.getInstance().getAvatar());
    }

    @Override
    public void updateUserInfo(int reqid, List<TIMUserProfile> profiles) {

    }
   /* private void changeBeautyType() {
        RadioGroupDialog beautyTypeDialog = new RadioGroupDialog(getContext(), beautyTypes);
        beautyTypeDialog.setTitle(R.string.str_beauty_type);
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
    }*/

    private void showVideoQulity() {
        final String[] roles = new String[]{getString(R.string.str_video_sd), getString(R.string.str_video_ld)};
        final String[] values = new String[]{Constants.SD_GUEST, Constants.LD_GUEST};

        RadioGroupDialog roleDialog = new RadioGroupDialog(getContext(), roles);

        roleDialog.setTitle(R.string.str_video_qulity);
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
                        MySelfInfo.getInstance().getGuestRole()) ? getString(R.string.str_video_sd) : getString(R.string.str_video_ld));
            }
        });
        roleDialog.show();
    }

    private void updateUserInfo(String sign, String name, String url){
        mProfileName.setText(name);
        if (TextUtils.isEmpty(sign)) {
            mProfileId.setText("个性签名：颜色不一样的烟火");
        }else {
            String signs = UIUtils.getLimitString(sign, 16);
            mProfileId.setText("个性签名：" + signs);
        }
        if (TextUtils.isEmpty(url)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_avatar);
            Bitmap cirBitMap = UIUtils.createCircleImage(bitmap, 0);
            mAvatar.setImageBitmap(cirBitMap);
        } else {
            SxbLog.d(TAG, "profile avator: " + url);
            RequestManager req = Glide.with(getActivity());
            req.load(url).transform(new GlideCircleTransform(getActivity())).into(mAvatar);
        }
        if (MySelfInfo.getInstance().getSex() == null) {
            mSex.setImageResource(R.drawable.sex_girl);
        }else {
            String sex = MySelfInfo.getInstance().getSex();
            mSex.setImageResource(sex.equals("女")?R.drawable.sex_girl:R.drawable.sex_boy);
        }
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        if (0 == code) {
            mProfileInfoHelper.setMyAvator(url);
        }else{
            SxbLog.w(TAG, "onUploadResult->failed: "+code);
        }
    }
    private boolean checkCropPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)){
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0){
                ActivityCompat.requestPermissions(getActivity(),
                        (String[]) permissions.toArray(new String[0]),
                        Constants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults){
                    if (ret != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }
}
