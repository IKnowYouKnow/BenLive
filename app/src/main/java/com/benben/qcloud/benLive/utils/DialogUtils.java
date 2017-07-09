package com.benben.qcloud.benLive.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/7/7.
 */

public class DialogUtils {
   private static ProgressDialog pd;

    public static void showDialog(String showText, Context context) {
        pd = new ProgressDialog(context);
        pd.setMessage(showText);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public static void dismissDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
