package com.benben.qcloud.benLive.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.bean.Result;
import com.benben.qcloud.benLive.service.LiveManager;
import com.benben.qcloud.benLive.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoundPwdActivity extends AppCompatActivity {

    private static final String TAG = "FoundPwdActivity";
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.updatePwd)
    EditText updatePwd;
    @BindView(R.id.repassword)
    EditText repassword;
    @BindView(R.id.checkCode)
    EditText checkCode;
    @BindView(R.id.getCheckCode)
    Button getCode;

    // 手机号 密码 验证码 随机生成的验证码
    String userId, pwd, pwd2, code;

    // 设置倒计时显示
    private TimeCount time;
    int ranCode;

    String updateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_pwd);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
    }

    private boolean checkInput() {
        getData();
        if (TextUtils.isEmpty(userId)) {
            username.setError("手机号不能为空");
            return false;
        } else if (userId.length() != 11) {
            username.setError("手机号格式有误");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            updatePwd.setError("密码不能为空");
            return false;
        } else if (isCN(pwd)){
            updatePwd.setError("密码中不能包含汉字");
            return false;
        }else if (TextUtils.isEmpty(pwd2)) {
            repassword.setError("确认密码不能为空");
            return false;
        } else if (!pwd.equals(pwd2)) {
            repassword.setError("两次密码输入不一致");
            return false;
        } else if (pwd.length() < 8 || pwd.length() > 16) {
            updatePwd.setError("请输入8~16位密码");
            return false;
        }

        return true;
    }

    private void getData() {
        userId = username.getText().toString().trim();
        pwd = updatePwd.getText().toString().trim();
        pwd2 = repassword.getText().toString().trim();
    }

    @OnClick({R.id.getCheckCode, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getCheckCode:
                if (checkInput()) {
                    // 随机生成验证码
                    ranCode = (int) ((Math.random() * 9 + 1) * 100000);
                    Log.e(TAG, "onViewClicked: numcode = " + ranCode);

                    // 发送验证码
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LiveManager.get().sendCheckCode(userId, String.valueOf(ranCode));
                        }
                    }).start();
                    // 开始倒计时
                    time.start();
                }
                break;
            case R.id.btn_register:
                DialogUtils.showDialog("正在提交...",this);
                if (checkInput()) {
                    code = checkCode.getText().toString().trim();
                    if (!TextUtils.isEmpty(code)) {
                        if (code.equals(String.valueOf(ranCode))) {
                            Log.e(TAG, "onViewClicked: 开始注册");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "run: 注册中。。。。");
                                    Result result = LiveManager.get().forgetPwd(userId, pwd);
                                    updateResult = (String) result.getRetData();
                                    if (result.getRetCode() == 0) {
                                        startActivity(new Intent(FoundPwdActivity.this,LoginActivity.class));
                                        finish();
                                        DialogUtils.dismissDialog();
                                    }
                                }
                            }).start();
                            Toast.makeText(this, updateResult, Toast.LENGTH_SHORT).show();
                            DialogUtils.dismissDialog();

                        }else {
                            Toast.makeText(this, "验证码输入有误", Toast.LENGTH_SHORT).show();
                            DialogUtils.dismissDialog();
                        }
                    }else {
                        Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    }

                }
                DialogUtils.dismissDialog();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    //自定义定时器
   public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            // 参数依次为总时长和计时间隔
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setClickable(false);
            getCode.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            // 计时完毕后触发
            getCode.setText("点击重新获取");
            getCode.setClickable(true);
        }
    }

    // 判断输入内容是否为汉字
    public boolean isCN(String str) {
        byte[] bytes = str.getBytes();
        if (bytes.length == str.length()) {
            return false;
        }else {
            return true;
        }
    }
}
