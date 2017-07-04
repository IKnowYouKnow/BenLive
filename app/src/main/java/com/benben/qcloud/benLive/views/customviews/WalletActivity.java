package com.benben.qcloud.benLive.views.customviews;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.gift.bean.Wallet;
import com.benben.qcloud.benLive.presenters.viewinface.BenLiveHelper;
import com.benben.qcloud.benLive.service.LiveManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.ttHead)
    TemplateTitle ttHead;
    @BindView(R.id.recharge)
    Button recharge;
    @BindView(R.id.withdraw)
    Button withdraw;
    @BindView(R.id.rule)
    TextView rule;
    @BindView(R.id.wallet)
    TextView wallet;
    @BindView(R.id.money)
    TextView money;
    Wallet balance;

    int currentWallet = 175;
    int currentMoney = currentWallet/10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_activity);
        initState();
        ButterKnife.bind(this);
        getUserWallet();
        setListener();
    }

    private void initState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * 获取用户账户信息
     */
    private void getUserWallet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                balance = LiveManager.get().getBalance(
                        BenLiveHelper.getInstance().getCurrentUserName(getApplicationContext()));
            }
        }).start();
        if (balance != null) {
            currentWallet = balance.getBalance();
//            if (currentWallet > 10) {
//                currentMoney = currentWallet/10;
//            } else {
//                currentMoney = 0;
//            }
            wallet.setText(currentWallet+".00");
            money.setText(currentMoney+".00");
        }else {
            wallet.setText(currentWallet+".00");
            money.setText(currentMoney+".00");
        }
    }

    /**
     * 监听单击事件
     */
    private void setListener() {
        ttHead.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recharge.setOnClickListener(this);
        withdraw.setOnClickListener(this);
        rule.setOnClickListener(this);
    }

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                Toast.makeText(this, "开始充值了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.withdraw:
                if (currentMoney >= 10) {
                    Toast.makeText(this, "正在提现", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "余额不足以提现", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rule:
                showRuleDialog();
                break;
        }
    }

    /**
     * 显示提现规则提示框
     */
    private void showRuleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("金币兑换及提现规则");
        dialog.setMessage("10金币 = 1元\n余额达到10元时才能提现");
        dialog.setPositiveButton("知道了", null);
        dialog.show();
    }


}
