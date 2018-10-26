package com.yimukeji.yuelaoge.bean;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Hank
 * 通用解析器
 */
public class CommonParser {

    public static final String TAG = "CommonParser";

    public static <T> Domain<T> parserObj(String json, Class<T> cls) {
        Log.i(TAG, "json->" + json);
        Domain<T> d = new Domain<T>();
        try {
            JSONObject object = new JSONObject(json);
            d.code = object.optInt("code");
            d.message = object.optString("msg");
            d.success = d.code == 1;
            if (d.success) {
                d.data = JSON.parseObject(object.optString("data"), cls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static <T> Domain<T> parserList(String json, final Class<T> cls) {
        Log.i(TAG, "json->" + json);
        Domain<T> d = new Domain<T>();
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (object == null)
            return d;

        d.code = object.optInt("code");
        d.message = object.optString("msg");
        d.success = d.code == 1;
        if (!d.success)
            return d;
        d.datalist = JSON.parseArray(object.optString("data"), cls);
        return d;
    }
}
