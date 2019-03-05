package com.yimukeji.yuelaoge.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.YuelaogeAPI;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.bean.Yuelao;
import com.yimukeji.yuelaoge.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class MyInfoActivity extends AppCompatActivity {


    TextView mLocationView, mNameView, mBirthdayView, mAgeView, mAddrView, mHealthView, mPecuniaryView, mHobbyView, mCharacterView,
            mJobView, mJoblocationView, mHeightView, mWeightView, mEduView, mMarryView, mLoveView, mExpectView, mRemarkView,
            mCommentView, mCreateView, mChangeView;
    ImageView mAvatarView;

    View mProgressView;
    Member mMember;
    Yuelao mYuelao;
    UploadTask mUploadTask;
    UpdateInfoTask mUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        switch (YuelaoApp.mType) {
            case YuelaoApp.TYPE_MEMBER:
                mMember = YuelaoApp.mMember;
                initUser();
                break;
        }
    }

    private void initUser() {
        mProgressView = findViewById(R.id.progressbar);
        mNameView = findViewById(R.id.name);
        mAvatarView = findViewById(R.id.iv_avatar);
        mChangeView = findViewById(R.id.tv_change_avatar);
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

        if (mMember == null)
            return;
        YuelaoApp.setAvatar(mAvatarView, mMember.avatar);
        Drawable mMale = getResources().getDrawable(R.drawable.icon_male);
        Drawable mFemale = getResources().getDrawable(R.drawable.icon_female);
        mMale.setBounds(0, 0, mMale.getMinimumWidth(), mMale.getMinimumHeight());
        mFemale.setBounds(0, 0, mFemale.getMinimumWidth(), mFemale.getMinimumHeight());
        String nameText = "*" + mMember.name.substring(1);
        mNameView.setText(nameText);
        mNameView.setCompoundDrawables(null, null, mMember.sex.equals("男") ? mMale : mFemale, null);
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

    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;
    public static final int REQUEST_CROP = 3;
    private File mImageFile;

    public void onClickPicker(View v) {
        new AlertDialog.Builder(this)
                .setTitle("选择照片")
                .setItems(new String[]{"相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectAlbum();
                    }
                })
                .create()
                .show();
    }

    private void selectCamera() {
        createImageFile();
        if (!mImageFile.exists()) {
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(intent, REQUEST_CROP);
    }

    private void createImageFile() {
        mImageFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            mImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "出错啦", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                cropImage(Uri.fromFile(mImageFile));
                break;

            case REQUEST_ALBUM:
                createImageFile();
                if (!mImageFile.exists()) {
                    return;
                }

                Uri uri = data.getData();
                if (uri != null) {
                    cropImage(uri);
                }
                break;
            case REQUEST_CROP:
                Log.e("info", "crop!!!!");
                mAvatarView.setImageURI(Uri.fromFile(mImageFile));
                attemptUpload();
                break;
        }
    }

    private void attemptUpload() {
        if (mUploadTask != null)
            return;
        if (YuelaoApp.mType != YuelaoApp.TYPE_MEMBER)
            return;
        mUploadTask = new UploadTask();
        mUploadTask.execute((Void) null);
    }


    public class UploadTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            return YuelaogeAPI.upload(mImageFile);
        }

        @Override
        protected void onPostExecute(String result) {
            mUploadTask = null;
            showProgress(false);
            JSONObject object = JSONObject.parseObject(result);
            if (object != null) {
                int code = object.getIntValue("code");
                if (code == 1) {
                    Toast.makeText(MyInfoActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    attemptUpdate();//上传成功，更新本地数据
                } else {
                    Toast.makeText(MyInfoActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MyInfoActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mUploadTask = null;
            showProgress(false);
        }
    }


    private void attemptUpdate() {
        if (mUpdateTask != null)
            return;
        if (YuelaoApp.mType != YuelaoApp.TYPE_MEMBER)
            return;
        mUpdateTask = new UpdateInfoTask();
        mUpdateTask.execute((Void) null);
    }

    public class UpdateInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return YuelaogeAPI.getUserInfo(mMember.id, YuelaoApp.mType);
        }

        @Override
        protected void onPostExecute(String result) {
            mUpdateTask = null;
            JSONObject object = JSONObject.parseObject(result);
            if (object != null && object.getIntValue("code") == 1) {
                YuelaoApp.saveUser(YuelaoApp.mType, object.getJSONObject("data"));
            }
        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
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

