package com.benben.qcloud.benLive.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.model.MySelfInfo;
import com.benben.qcloud.benLive.service.LiveManager;
import com.benben.qcloud.benLive.views.customviews.TemplateTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/10.
 */
public class AnchorCheckActivity extends AppCompatActivity {


    @BindView(R.id.tt_title)
    TemplateTitle ttTitle;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.userId)
    EditText mUserId;
    @BindView(R.id.money_name)
    EditText mMoneyName;
    @BindView(R.id.phoneNum)
    EditText mPhoneNum;
    @BindView(R.id.wechatId)
    EditText mWechatId;
    @BindView(R.id.aliPayId)
    EditText mAliPayId;

    private Dialog dialog;
    // 用户名 用户id 收款人姓名 联系方式 微信号 支付宝账号
    String username, userId, moneyName, phoneNum, wechatId, aliPayId;

    int resCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchor_check_activity);
        ButterKnife.bind(this);
        // 设置沉浸式标题栏
        initStatus();

        mUserId.setText(MySelfInfo.getInstance().getId());

        ttTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {

        username = mUsername.getText().toString().trim();
        userId = mUserId.getText().toString().trim();
        moneyName = mMoneyName.getText().toString().trim();
        phoneNum = mPhoneNum.getText().toString().trim();
        wechatId = mWechatId.getText().toString().trim();
        aliPayId = mAliPayId.getText().toString().trim();
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @OnClick(R.id.commit_check)
    public void onViewClicked() {
        initData();

        if (checkInput()) {
            showAlertDialog();
        }

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认提交");
        builder.setMessage("请认真检查账号信息，凡是因输入有误而无法完成汇款，后果自负！")
                .setPositiveButton("继续提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alog, int which) {
                        commitServer();
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void commitServer() {
        showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                resCode = LiveManager.get().anchorCheck(username, phoneNum, userId, wechatId, aliPayId, moneyName);
            }
        }).start();
        if (resCode == -1) {
            Toast.makeText(AnchorCheckActivity.this, "系统繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
            return;
        } else if (resCode == 0) {
            dismissDialog();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setTitle("正在提交，请稍后。。。");
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(username)) {
            mUsername.setError("姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(moneyName)) {
            mMoneyName.setError("收款人姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            mPhoneNum.setError("联系方式不能为空");
            return false;
        }
        if (TextUtils.isEmpty(wechatId)) {
            mWechatId.setError("微信号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(aliPayId)) {
            mAliPayId.setError("支付宝账号不能为空");
            return false;
        }
        if (phoneNum.length() != 11) {
            mPhoneNum.setError("手机号码格式不正确");
            return false;
        }
        return true;
    }
}
