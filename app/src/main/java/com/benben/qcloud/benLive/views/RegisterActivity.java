package com.benben.qcloud.benLive.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.QavsdkApplication;
import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.bean.UserInfo;
import com.benben.qcloud.benLive.presenters.LoginHelper;
import com.benben.qcloud.benLive.presenters.UserServerHelper;
import com.benben.qcloud.benLive.presenters.viewinface.LoginView;
import com.benben.qcloud.benLive.service.LiveManager;
import com.benben.qcloud.benLive.utils.DialogUtils;
import com.benben.qcloud.benLive.utils.MD5;
import com.benben.qcloud.benLive.utils.ResultUtils;
import com.benben.qcloud.benLive.views.customviews.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.benben.qcloud.benLive.R.id.repassword;
import static com.benben.qcloud.benLive.R.id.username;

/**
 * 注册账号类
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, LoginView {
    @BindView(R.id.inviteCode)
    EditText mInviteCode;
    @BindView(R.id.checkCode)
    EditText mCheckCode;
    @BindView(R.id.getCheckCode)
    Button getCode;
    @BindView(R.id.inviteCodeLayout)
    LinearLayout invitCodeLayout;
    private EditText mUserName, mPassword, mRepassword;
    private TextView mBtnRegister;
    private ImageView mBtnBack;
    QavsdkApplication mMyApplication;
    LoginHelper mLoginHeloper;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    // 邀请码开关 生成的随机验证码  腾讯注册结果
    int code, ranCode, tenchRes;
    private TimeCount time;
    String userId, userPW, inviteCode, checkCode;
    UserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_independent_register);
        ButterKnife.bind(this);
        initState();

        mUserName = (EditText) findViewById(username);
        mPassword = (EditText) findViewById(R.id.password);
        mRepassword = (EditText) findViewById(repassword);
        mBtnRegister = (TextView) findViewById(R.id.btn_register);
        mBtnBack = (ImageView) findViewById(R.id.back);
        mBtnBack.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);

        mMyApplication = (QavsdkApplication) getApplication();
        mLoginHeloper = new LoginHelper(this, this);

        // 倒计时定时器
        time = new TimeCount(60000, 1000);
        // 检查是否需要邀请码
        checkInvite();
    }

    private void checkInvite() {
        Intent intent = getIntent();
        code = intent.getIntExtra("inviteStatus", 0);
        if (code == 0) {
            invitCodeLayout.setVisibility(View.VISIBLE);
        } else if (code == -1) {
            invitCodeLayout.setVisibility(View.GONE);
        }
    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onDestroy() {
        mLoginHeloper.onDestory();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            DialogUtils.showDialog("正在注册...", this);
            if (checkInput()) {
                // 注册腾讯账号
                registerTenchServer(userId, MD5.getMessageDigest(userPW));
                // 在自己服务器上注册
                registerMyServer(userId, MD5.getMessageDigest(userPW), inviteCode, code, tenchRes);
                //注册一个账号
//                mLoginHeloper.standardRegister(userId, MD5.getMessageDigest(mPassword.getText().toString()));
            }
            DialogUtils.dismissDialog();
        }

        if (view.getId() == R.id.back) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    Result result = null;
    String strRes;

    private void registerMyServer(final String userId, final String pwd, final String inviteCode, final int code, final int tenchRes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                result = LiveManager.get().register(userId, pwd, inviteCode, String.valueOf(code), String.valueOf(tenchRes));
                if (result.getRetCode() == 0) {
                    boolean success = getResult();
                    if (success) {
                        mLoginHeloper.standardLogin(userId, info.getOriginal_password());
                    }

                } else if (result.getRetCode() == -1) {
                    strRes = (String) result.getRetData();
                }
            }
        }).start();

        Toast.makeText(mMyApplication, strRes, Toast.LENGTH_SHORT).show();
    }

    // 在自己服务器登录结果
    private boolean getResult() {
        String json = LiveManager.get().login(userId, MD5.getMessageDigest(userPW));
        try {
            JSONObject obj = new JSONObject(json);
            int code = obj.getInt("code");
            if (code == 0) {
                Result result = ResultUtils.getResultFromJson(json, UserInfo.class);
                if (result != null && result.getRetData() != null) {
                    info = (UserInfo) result.getRetData();
                    return true;
                }
            } else if (code == -1) {
                strRes = obj.getString("data");
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void registerTenchServer(final String userId, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserServerHelper.RequestBackInfo backInfo =
                        UserServerHelper.getInstance().registerId(userId, pwd);
                if (backInfo != null && backInfo.getErrorCode() == 0) {
                    tenchRes = 0;
                } else if (backInfo != null) {
                    tenchRes = 1;
                }
            }
        }).start();
    }

    @Override
    public void loginSucc() {
        jumpIntoHomeActivity();
    }

    @Override
    public void loginFail(String module, int errCode, String errMsg) {
        Toast.makeText(this, "code " + errCode + "     " + errMsg, Toast.LENGTH_SHORT).show();

    }

    /**
     * 直接跳转主界面
     */
    private void jumpIntoHomeActivity() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.getCheckCode)
    public void onViewClicked() {
        userId = mUserName.getText().toString().trim();
        if (userId.isEmpty()) {
            Toast.makeText(mMyApplication, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        ranCode = (int) ((Math.random() * 9 + 1) * 100000);
        Log.e(TAG, "onViewClicked: ranCode = " + ranCode);
        new Thread(new Runnable() {
            @Override
            public void run() {
                LiveManager.get().sendCheckCode(userId, String.valueOf(ranCode));
            }
        }).start();
        time.start();
    }

    private boolean checkInput() {
        userId = mUserName.getText().toString();
        userPW = mPassword.getText().toString();
        String userPW2 = mRepassword.getText().toString();
        checkCode = mCheckCode.getText().toString().trim();
        if (code == 0) {
            inviteCode = mInviteCode.getText().toString().trim();
            if (TextUtils.isEmpty(inviteCode)) {
                Toast.makeText(mMyApplication, "请输入邀请码", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            inviteCode = "";
        }

        if (TextUtils.isEmpty(userId)) {
            mUserName.setError("手机号不能为空");
            return false;
        } else if (userId.length() != 11) {
            mUserName.setError("手机号格式有误");
            return false;
        } else if (TextUtils.isEmpty(userPW)) {
            mPassword.setError("密码不能为空");
            return false;
        } else if (isCN(userPW)) {
            mPassword.setError("密码中不能包含汉字");
            return false;
        } else if (TextUtils.isEmpty(userPW2)) {
            mRepassword.setError("确认密码不能为空");
            return false;
        } else if (!userPW.equals(userPW2)) {
            mRepassword.setError("两次密码输入不一致");
            return false;
        } else if (userPW.length() < 8 || userPW.length() > 16) {
            mPassword.setError("请输入8~16位密码");
            return false;
        } else if (TextUtils.isEmpty(checkCode)) {
            mCheckCode.setError("请输入验证码");
            return false;
        } else if (!checkCode.equals(ranCode)) {
            mCheckCode.setError("验证码输入错误");
            return false;
        }

        return true;
    }

    // 自定义倒计时
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setClickable(false);
            getCode.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            getCode.setText("点击重新获取");
            getCode.setClickable(true);
        }
    }

    // 判断输入内容是否为汉字
    public boolean isCN(String str) {
        byte[] bytes = str.getBytes();
        if (bytes.length == str.length()) {
            return false;
        } else {
            return true;
        }
    }
}
