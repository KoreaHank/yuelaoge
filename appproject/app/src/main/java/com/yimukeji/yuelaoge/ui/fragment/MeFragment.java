package com.yimukeji.yuelaoge.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.ui.LoginActivity;

public class MeFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        String nickname = CloudApp.mUser.nickname;
//        User.setAvatarAndNickname(mAvatarView, null, CloudApp.getUserid(), CloudApp.getNickname(), mContext);
//        mNicknameView.setText(nickname);
    }

    private void exit() {
//        CloudApp.exit();
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivity(intent);
//        getActivity().finish();

    }


}
