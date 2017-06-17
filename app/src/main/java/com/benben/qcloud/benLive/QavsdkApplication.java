package com.benben.qcloud.benLive;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.benben.qcloud.benLive.presenters.InitBusinessHelper;
import com.benben.qcloud.benLive.utils.SxbLogImpl;

import java.util.List;


/**
 * 全局Application
 */
public class QavsdkApplication extends Application {

    private static QavsdkApplication app;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();

        if (shouldInit()) {
            SxbLogImpl.init(getApplicationContext());

            //初始化APP
            InitBusinessHelper.initApp(context);
        }

//        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L,TimeUnit.MILLISECONDS)
//                .cookieJar(cookieJar)
//                .sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
//                .addInterceptor(new LoggerInterceptor("TAG"))
//                .build();
//        OkHttpUtils.initClient(okHttpClient);


//        LeakCanary.install(this);

        //创建AVSDK 控制器类
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        return context;
    }

    public static QavsdkApplication getInstance() {
        return app;
    }
}
