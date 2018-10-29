package com.yimukeji.yuelaoge.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.YuelaogeAPI;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.bean.Yuelao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends AppCompatActivity {

    private UserRegistTask mRegistTask;

    EditText mLocationView, mNameView, mPhoneView, mPasswordView,
            mSexView, mBirthdayView, mAgeView, mIDView, mAddrView, mHealthView, mPecuniaryView, mHobbyView, mCharacterView,
            mJobView, mJoblocationView, mHeightView, mWeightView, mEduView, mMarryView, mLoveView, mExpectView, mRemarkView,
            mCommentView;
    View mProgressView;
    EditText mErrorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        mLocationView = findViewById(R.id.location);
        mNameView = findViewById(R.id.name);
        mPhoneView = findViewById(R.id.phone);
        mPasswordView = findViewById(R.id.password);
        mSexView = findViewById(R.id.sex);
        mBirthdayView = findViewById(R.id.birthday);
        mAgeView = findViewById(R.id.age);
        mIDView = findViewById(R.id.idcard);
        mAddrView = findViewById(R.id.address);
        mHealthView = findViewById(R.id.health);
        mPecuniaryView = findViewById(R.id.pecuniary);
        mHobbyView = findViewById(R.id.hobby);
        mCharacterView = findViewById(R.id.character);
        mJobView = findViewById(R.id.job);
        mJoblocationView = findViewById(R.id.joblocation);
        mHeightView = findViewById(R.id.height);
        mWeightView = findViewById(R.id.weight);
        mEduView = findViewById(R.id.education);
        mMarryView = findViewById(R.id.marry);
        mLoveView = findViewById(R.id.love);
        mExpectView = findViewById(R.id.expect);
        mRemarkView = findViewById(R.id.remark);
        mCommentView = findViewById(R.id.comment);
        if (YuelaoApp.mType == YuelaoApp.TYPE_YUELAO)
            mCommentView.setVisibility(View.VISIBLE);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void regist(View view) {
        attemptRegist();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegist() {
        if (mRegistTask != null) {
            return;
        }
        if (mErrorView != null)
            mErrorView.setError(null);
        Member member = validParam();
        if (member != null) {
            mRegistTask = new UserRegistTask(JSON.toJSONString(member));
            mRegistTask.execute((Void) null);
        }
    }

    private Member validParam() {
        Member mMember = new Member();
        String location = mLocationView.getText().toString();
        if (TextUtils.isEmpty(location)) {
            mLocationView.setError(getString(R.string.error_field_required));
            return null;
        }
        mMember.location = location;

        String name = mNameView.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            return null;
        }
        mMember.name = name;
        String phone = mPhoneView.getText().toString();
        if (!isPhoneValid(phone))
            return null;
        mMember.phone = phone;
        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            return null;
        }
        mMember.password = password;

        String sex = mSexView.getText().toString();
        if (TextUtils.isEmpty(sex)) {
            mSexView.setError(getString(R.string.error_field_required));
            return null;
        }
        mMember.sex = sex;
        String birthday = mBirthdayView.getText().toString();
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayView.setError(getString(R.string.error_field_required));
            return null;
        }
        mMember.birthday = birthday;

        String age = mAgeView.getText().toString();
        if (TextUtils.isEmpty(age)) {
            mAgeView.setError(getString(R.string.error_field_required));
            return null;
        }
        int ageInt = 0;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (ageInt <= 0) {
            mAgeView.setError(getString(R.string.error_field_invalid));
            return null;
        }
        mMember.age = ageInt;

        mMember.create_date = "2018-10-29";
        mMember.id_card = mIDView.getText().toString();
        mMember.address = mAddrView.getText().toString();
        mMember.health = mHealthView.getText().toString();
        mMember.pecuniary = mPecuniaryView.getText().toString();
        mMember.hobby = mHobbyView.getText().toString();
        mMember.disposition = mCharacterView.getText().toString();
        mMember.job = mJobView.getText().toString();
        mMember.job_location = mJoblocationView.getText().toString();

        int height = 0;
        try {
            height = Integer.parseInt(mHeightView.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mMember.height = height;
        int weight = 0;
        try {
            weight = Integer.parseInt(mWeightView.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mMember.weight = weight;
        mMember.education = mEduView.getText().toString();
        mMember.marry = mMarryView.getText().toString();
        mMember.love = mLoveView.getText().toString();
        mMember.expect = mExpectView.getText().toString();
        mMember.remark = mRemarkView.getText().toString();
        if (YuelaoApp.mType == YuelaoApp.TYPE_YUELAO) {
            mMember.id_yuelao = YuelaoApp.mYuelao.id;
            mMember.comment = mCommentView.getText().toString();
        }
        return mMember;
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
    public class UserRegistTask extends AsyncTask<Void, Void, String> {

        private final String mData;

        UserRegistTask(String data) {
            mData = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            return YuelaogeAPI.regist(mData);
        }

        @Override
        protected void onPostExecute(String result) {
            mRegistTask = null;
            showProgress(false);
            JSONObject object = JSONObject.parseObject(result);
            if (object != null) {
                int code = object.getIntValue("code");
                if (code == 1) {
                    Log.e("RegistActivity", "注册成功");
                } else {
                    Toast.makeText(RegistActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegistActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mRegistTask = null;
            showProgress(false);
        }
    }


}

