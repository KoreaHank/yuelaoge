package com.yimukeji.yuelaoge;

import android.app.Application;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.bean.Yuelao;

public class YuelaoApp extends Application {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_ADMIN = 3;
    public static final int TYPE_MEMBER = 1;
    public static final int TYPE_YUELAO = 2;
    public static int mType = TYPE_NONE;
    public static Member mMember;
    public static Yuelao mYuelao;
    public static YuelaoApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initUserInfo();
    }

    private void initUserInfo() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        mType = sp.getInt("type", TYPE_NONE);
        String userjson = sp.getString("userjson", "");
        switch (mType) {
            case TYPE_YUELAO:
                mYuelao = JSON.parseObject(userjson, Yuelao.class);
                break;
            case TYPE_MEMBER:
                mMember = JSON.parseObject(userjson, Member.class);
                break;
        }
    }

    public static void saveUser(int type, JSONObject object) {
        SharedPreferences sp = mApp.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("type", type);
        editor.putString("userjson", object.toString());
        editor.commit();
        mType = type;
        switch (type) {
            case TYPE_YUELAO:
                mMember = JSON.toJavaObject(object, Member.class);
                break;
            case TYPE_MEMBER:
                mYuelao = JSON.toJavaObject(object, Yuelao.class);
                break;
        }

    }

    public static int getUserid() {
        int userid = -1;
        switch (YuelaoApp.mType) {
            case TYPE_NONE:

                break;
            case TYPE_MEMBER:
                userid = mMember == null ? -1 : mMember.id;
                break;
            case TYPE_YUELAO:
                userid = mYuelao == null ? -1 : mYuelao.id;
                break;
            case YuelaoApp.TYPE_ADMIN:
                break;
        }
        return userid;
    }
}
