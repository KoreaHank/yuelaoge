package com.yimukeji.yuelaoge.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.YuelaogeAPI;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.util.DateUtil;

import java.sql.Timestamp;

/**
 * A login screen that offers login via email/password.
 */
public class UserDetailActivity extends AppCompatActivity {


    TextView mLocationView, mNameView, mBirthdayView, mAgeView, mAddrView, mHealthView, mPecuniaryView, mHobbyView, mCharacterView,
            mJobView, mJoblocationView, mHeightView, mWeightView, mEduView, mMarryView, mLoveView, mExpectView, mRemarkView,
            mCommentView, mCreateView;
    ImageView mAvatarView, mSexView;
    LinearLayout mActionContainer;
    Button mMeetBtn;
    View mProgressView;
    Member mMember;

    MeetTask mMeetTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMember = (Member) getIntent().getSerializableExtra("member");
        setContentView(R.layout.activity_userdetail);
        initView();
        initData();
    }

    private void initView() {
        mProgressView = findViewById(R.id.progressbar);
        mNameView = findViewById(R.id.name);
        mAvatarView = findViewById(R.id.iv_avatar);
        mSexView = findViewById(R.id.iv_sex);
        mLocationView = findViewById(R.id.location);
        mBirthdayView = findViewById(R.id.birthday);
        mAgeView = findViewById(R.id.age);
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
        mCreateView = findViewById(R.id.create);
        mActionContainer = findViewById(R.id.ll_actions);
        mMeetBtn = findViewById(R.id.btn_meet);
        mMeetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptMeet();
            }
        });
        if (YuelaoApp.mType == YuelaoApp.TYPE_MEMBER && YuelaoApp.getUserid() != mMember.id) {
            mActionContainer.setVisibility(View.VISIBLE);
        } else {
            mActionContainer.setVisibility(View.GONE);
        }

    }

    private void initData() {
        if (mMember == null)
            return;
        YuelaoApp.setAvatar(mAvatarView, mMember.avatar);
        mSexView.setImageResource(mMember.sex.equals("男") ? R.drawable.icon_male : R.drawable.icon_female);
        String nameText = "*" + mMember.name.substring(1);
        mNameView.setText(nameText);
        mAgeView.setText(mMember.age + "岁");
        mEduView.setText(mMember.education);
        mAddrView.setText(mMember.address);

        String birdate = "";
        String createdate = "";
        try {
            long bir = Long.parseLong(mMember.birthday);
            birdate = DateUtil.parseTimestampToStr(new Timestamp(bir), DateUtil.DATE_FORMAT_YYYY_MM_DD);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            long cre = Long.parseLong(mMember.create_date);
            createdate = DateUtil.parseTimestampToStr(new Timestamp(cre), DateUtil.DATE_FORMAT_YYYY_MM_DD);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mBirthdayView.setText("出生日期：" + birdate);
        mCreateView.setText("录入日期：" + createdate);
        mHealthView.setText("健康状况：" + mMember.health);
        mPecuniaryView.setText("经济状况：" + mMember.pecuniary);
        mHobbyView.setText("兴趣爱好：" + mMember.hobby);
        mCharacterView.setText("性格特点：" + mMember.disposition);
        mJobView.setText("工作：" + mMember.job);
        mJoblocationView.setText("工作地点：" + mMember.job_location);
        mHeightView.setText("身高：" + String.valueOf(mMember.height) + "cm");
        mWeightView.setText("体重：" + String.valueOf(mMember.weight) + "kg");
        mMarryView.setText("婚姻状况：" + mMember.marry);
        mLoveView.setText("恋爱经历：" + mMember.love);
        mExpectView.setText("期望另一半：" + mMember.expect);
        mRemarkView.setText("备注：" + mMember.remark);
        mCommentView.setText("月老点评：" + mMember.comment);
    }

    private void attemptMeet() {
        if (mMeetTask != null)
            return;
        if (YuelaoApp.mType != YuelaoApp.TYPE_MEMBER)
            return;
        if (YuelaoApp.mMember.sex.equals("男")) {
            mMeetTask = new MeetTask(YuelaoApp.mMember, mMember);
        } else {
            mMeetTask = new MeetTask(mMember, YuelaoApp.mMember);
        }
        mMeetTask.execute((Void) null);
    }


    public class MeetTask extends AsyncTask<Void, Void, String> {

        Member mMale, mFemale;

        MeetTask(Member male, Member female) {
            mMale = male;
            mFemale = female;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            return YuelaogeAPI.meet(mMale.id, mMale.name, mMale.id_yuelao, mFemale.id, mFemale.name, mFemale.id_yuelao);
        }

        @Override
        protected void onPostExecute(String result) {
            mMeetTask = null;
            showProgress(false);
            JSONObject object = JSONObject.parseObject(result);
            if (object != null) {
                int code = object.getIntValue("code");
                if (code == 1) {
                    Toast.makeText(UserDetailActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetailActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mMeetTask = null;
            showProgress(false);
        }
    }

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

}

