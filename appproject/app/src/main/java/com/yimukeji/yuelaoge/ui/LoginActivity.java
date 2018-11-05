package com.yimukeji.yuelaoge.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.YuelaogeAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPhoneView;
    private EditText mPasswordView;
    private TextView mRegistView;
    private View mProgressView;
    private RadioGroup mTypeGroupView;

    private int mType = YuelaoApp.TYPE_MEMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPhoneView = findViewById(R.id.phone);
        mPasswordView = findViewById(R.id.password);
        mRegistView = findViewById(R.id.regist);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mRegistView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                skip2Regist();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
        mTypeGroupView = findViewById(R.id.typegroup);
        mTypeGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.e(TAG, "i:" + i);
                switch (i) {
                    case R.id.typemember:
                        mType = YuelaoApp.TYPE_MEMBER;
                        break;
                    case R.id.typeyuelao:
                        mType = YuelaoApp.TYPE_YUELAO;
                        break;
                    case 1:
                        break;

                }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (!isPhoneValid(email)) {
            focusView = mPhoneView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private static final String mPhoneRegex = "^(1[3-9])\\d{9}$";

    private boolean isPhoneValid(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            return false;
        }
        Pattern pattern = Pattern.compile(mPhoneRegex);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            return false;
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            return YuelaogeAPI.login(mEmail, mPassword, mType);
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;
            showProgress(false);
            JSONObject object = JSONObject.parseObject(result);
            if (object != null) {
                int code = object.getIntValue("code");
                if (code == 1) {
                    YuelaoApp.saveUser(mType, object.getJSONObject("data"));
                    skip2Main();
                } else {
                    Toast.makeText(LoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void skip2Regist() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
    }

    public void skip2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

