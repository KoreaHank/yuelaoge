package com.yimukeji.yuelaoge.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.ui.fragment.MainFragment;
import com.yimukeji.yuelaoge.ui.fragment.MeFragment;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PAGE_MAIN = 1;
    private static final int PAGE_ME = 2;
    private int currentPage = PAGE_MAIN;
    private MainFragment mainFragment;
    private MeFragment meFragment;
    //view
    private RadioGroup mTabView;
    private ImageView mCreateView;
    private View mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabView = findViewById(R.id.rg_tab);
        mCreateView = findViewById(R.id.iv_create);
        mProgressView = findViewById(R.id.login_progress);
        mainFragment = new MainFragment();
        meFragment = new MeFragment();
        if (YuelaoApp.mType == YuelaoApp.TYPE_YUELAO)
            mCreateView.setVisibility(View.VISIBLE);
        mCreateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip2Regist();
            }
        });
        mTabView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main:
                        currentPage = PAGE_MAIN;
                        invalidateOptionsMenu();
                        showFragment(mainFragment);
//                        addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.fragment_container);
                        break;
                    case R.id.rb_me:
                        currentPage = PAGE_ME;
                        invalidateOptionsMenu();
                        showFragment(meFragment);
                        break;
                }
            }
        });
        ((RadioButton) mTabView.findViewById(R.id.rb_main)).setChecked(true);
        String location = YuelaoApp.getUserLocation();
        Log.e(TAG, "location:" + location);
        if (!TextUtils.isEmpty(location)) {
            getSupportActionBar().setTitle("月老阁（" + location + "站）");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentPage == PAGE_MAIN) {
            menu.findItem(R.id.all).setVisible(true);
            menu.findItem(R.id.male).setVisible(true);
            menu.findItem(R.id.female).setVisible(true);
            menu.findItem(R.id.mine).setVisible(true);
            menu.findItem(R.id.exit).setVisible(false);
            menu.findItem(R.id.pick).setVisible(false);
        } else if (currentPage == PAGE_ME) {
            menu.findItem(R.id.all).setVisible(false);
            menu.findItem(R.id.male).setVisible(false);
            menu.findItem(R.id.female).setVisible(false);
            menu.findItem(R.id.mine).setVisible(false);
            menu.findItem(R.id.exit).setVisible(true);
            menu.findItem(R.id.pick).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                mainFragment.setType(Member.TYPE_ALL);
                return true;
            case R.id.male:
                mainFragment.setType(Member.TYPE_MALE);
                return true;
            case R.id.female:
                mainFragment.setType(Member.TYPE_FEMALE);
                return true;
            case R.id.mine:
                mainFragment.setType(Member.TYPE_MINE);
                return true;
            case R.id.exit:
                YuelaoApp.exit();
                skip2Login();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragments(transaction);
        if (!fragment.isAdded())
            transaction.add(R.id.fragment_container, fragment);
        transaction.show(fragment);
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        transaction.hide(mainFragment);
        transaction.hide(meFragment);
    }


    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void skip2Regist() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
    }

    public void skip2Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
