package com.benben.qcloud.benLive.utils;

import android.util.Log;

import com.benben.qcloud.benLive.I;
import com.benben.qcloud.benLive.bean.Result;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by clawpo on 2016/9/21.
 */
public class ResultUtils {
    public static <T> Result getResultFromJson(String jsonStr, Class<T> clazz){
        Result result = new Result();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result.setRetCode(jsonObject.getInt("code"));
//            result.setRetMsg(jsonObject.getBoolean("retMsg"));
            if(!jsonObject.isNull("data")) {
                Log.e("Utils", "data is not null");
                JSONObject jsonRetData = jsonObject.getJSONObject("data");
                Log.e("Utils", "getResultFromJson: jsonData = " + jsonRetData);
                if (jsonRetData != null) {
                    Log.e("Utils", "jsonRetData=" + jsonRetData);
                    String date;
                    try {
                        date = URLDecoder.decode(jsonRetData.toString(), I.UTF_8);
                        Log.e("Utils", "jsonRetData=" + date);
                        T t = new Gson().fromJson(date, clazz);
                        result.setRetData(t);
                        return result;

                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                        T t = new Gson().fromJson(jsonRetData.toString(), clazz);
                        result.setRetData(t);
                        return result;
                    }
                }
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public static Result getStringFromJson(String jsonStr) {
        Result result = new Result();
        try {
            JSONObject json = new JSONObject(jsonStr);
            result.setRetCode(json.getInt("code"));
            result.setRetData(json.getString("data"));
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String TAG = "ResultUtils";
    public static <T> Result getListResultFromJson(String jsonStr, Class<T> clazz){
        Result result = new Result();
        Log.e("Utils","jsonStr="+jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result.setRetCode(jsonObject.getInt("code"));
            if(!jsonObject.isNull("data")) {
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null) {
                    List<T> list = new ArrayList<T>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonGroupAvatar = array.getJSONObject(i);
                        T ga = new Gson().fromJson(jsonGroupAvatar.toString(), clazz);
                        list.add(ga);
                    }
                    result.setRetData(list);
                    return result;
                }
            }
            Log.e(TAG, "getListResultFromJson: result = "+result );
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

}
