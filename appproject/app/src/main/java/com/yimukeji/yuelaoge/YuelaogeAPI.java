package com.yimukeji.yuelaoge;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class YuelaogeAPI {
    private static final String TAG = "YuelaogeAPI";
    private static final String BASE_URL = "http://172.16.10.248:8080/yuelaoge/ApiServlet";
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

    public static String getMember(int type, int page) {
        HashMap<String, String> param = getBaseMap();
        param.put("method", "getmember");
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
