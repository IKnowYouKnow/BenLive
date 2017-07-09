package com.benben.qcloud.benLive.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.benben.qcloud.benLive.R;
import com.benben.qcloud.benLive.data.LiveDao;
import com.benben.qcloud.benLive.gift.bean.Gift;
import com.benben.qcloud.benLive.service.LiveManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private List<Gift> mGiftsList;
    LiveDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initState();
        dao = new LiveDao();
        mGiftsList = new ArrayList<>();
        Handler handler = new Handler();

        getGiftList();
        handler.postDelayed(new SplashHandler(), 2000);
    }

    private void getGiftList() {
        // 获取礼物信息并存到本地
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGiftsList = LiveManager.get().getGiftList();
                if (mGiftsList != null && mGiftsList.size() > 0) {
                    dao.setGiftList(mGiftsList);
                    Log.e(TAG, "run: giftList.size = "+mGiftsList.size());
                }
            }
        }).start();
        if (mGiftsList.size()<0) {
            Toast.makeText(this, "礼物列表加载失败", Toast.LENGTH_SHORT).show();
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

    class SplashHandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();

        }

    }

}
