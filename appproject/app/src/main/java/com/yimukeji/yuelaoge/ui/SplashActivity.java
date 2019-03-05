package com.yimukeji.yuelaoge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;

/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Class clz;
                if (YuelaoApp.mType == YuelaoApp.TYPE_NONE) {
                    clz = LoginActivity.class;
                } else {
                    clz = MainActivity.class;
                }
                skip(clz);
            }
        }, 2000);
    }

    private void skip(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        finish();
    }
}

