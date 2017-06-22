package com.benben.qcloud.benLive.views.customviews;

import android.os.Bundle;
import android.view.Window;

import com.benben.qcloud.benLive.R;

public class WalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_wallet_activity);
    }
}
