package com.yimukeji.yuelaoge;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class YuelaogeAPI {
    private static final String TAG = "YuelaogeAPI";
    private static boolean debugMode = false;
    private static final String BASE_URL = debugMode ? "http://172.16.10.248:8080/yuelaoge/api" : "http://47.100.103.225:8080/yuelaoge/api";
    private static final String UPLOAD_URL = debugMode ? "http://172.16.10.248:8080/yuelaoge/upload" : "http://47.100.103.225:8080/yuelaoge/upload";
    public static final String AVATAR_BASE_URL = debugMode ? "http://172.16.10.248:8080/yuelaoge/uploads/" : "http://47.100.103.225:8080/yuelaoge/uploads/";
    private static OkHttpClient mOkHttpClient;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = (new OkHttpClient.Builder()).sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts()).hostnameVerifier(new TrustAllHostnameVerifier()).connectTimeout(10L, TimeUnit.SECONDS).build();
        }
        return mOkHttpClient;
    }

    public static HashMap<String, String> getBaseMap() {
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", String.valueOf(YuelaoApp.getUserid()));
        return param;
    }

    //登录
    public static String login(String phone, String password, int type) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "login");
        param.put("phone", phone);
        param.put("password", password);
        param.put("user_type", String.valueOf(type));
        return post(param);
    }

    public static String getUserInfo(int userid, int type) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "getuserinfo");
        param.put("user_type", String.valueOf(type));
        return post(param);
    }

    //登录
    public static String regist(String data) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "regist");
        param.put("data", data);
        return post(param);
    }

    public static String meet(int maleid, String malename, int maleyuelaoid, int femaleid, String femalename, int femaleyuelaoid) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "meet");
        param.put("maleid", String.valueOf(maleid));
        param.put("malename", malename);
        param.put("maleyuelaoid", String.valueOf(maleyuelaoid));
        param.put("femaleid", String.valueOf(femaleid));
        param.put("femalename", String.valueOf(femalename));
        param.put("femaleyuelaoid", String.valueOf(femaleyuelaoid));
        return post(param);
    }

    /**
     * 获取成员列表
     *
     * @param type
     * @param page
     * @return
     */
    public static String getMember(int type, int page) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "getmember");
        param.put("type", String.valueOf(type));
        param.put("page", String.valueOf(page));
        return post(param);
    }

    public static String getMeet(int type, int page) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "getmembermeet");
        param.put("type", String.valueOf(type));
        param.put("page", String.valueOf(page));
        return post(param);
    }

    private static String post(HashMap<String, String> hashMap) {
        RequestBody body = getRequestBody(hashMap);
        try {
            Request request = new Request.Builder().url(BASE_URL).post(body).build();
            Response response = getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.i(TAG, " result->" + result);
                return result;
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        return null;
    }


    public static String upload(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("userid", String.valueOf(YuelaoApp.getUserid()))
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)).build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();
        try {
            Response response = getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.i(TAG, " result->" + result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static RequestBody getRequestBody(HashMap<String, String> hashMap) {
        String param = "";
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : hashMap.keySet()) {
            param = param + key + ":" + hashMap.get(key) + " ";
            builder.add(key, hashMap.get(key));
        }
        Log.i(TAG, "request param->" + param);
        return builder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init((KeyManager[]) null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception var2) {
            ;
        }

        return ssfFactory;
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        private TrustAllHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAllCerts implements X509TrustManager {
        private TrustAllCerts() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
