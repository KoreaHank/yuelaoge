package com.yimukeji.yuelaoge.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaoApp;
import com.yimukeji.yuelaoge.YuelaogeAPI;
import com.yimukeji.yuelaoge.bean.CommonParser;
import com.yimukeji.yuelaoge.bean.Domain;
import com.yimukeji.yuelaoge.bean.Meet;
import java.util.List;

public class MeFragment extends BaseFragment {


    TextView mAvatarView;
    TextView mNicknameView;

    RecyclerView mListView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;

    GetDataTask mGetDataTask;
    List<Meet> mMembers;
    int nextPage = 0;
    boolean isAllLoaded = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAvatarView = getView().findViewById(R.id.avatar);
        mNicknameView = getView().findViewById(R.id.nickname);
        String name = YuelaoApp.getUserName();
        mAvatarView.setText(YuelaoApp.getAvatarText(name));
        mNicknameView.setText(name);

        mListView = getView().findViewById(R.id.listview);
        mRefreshLayout = getView().findViewById(R.id.refresh_layout);
        mLayoutManager = new LinearLayoutManager(mContext);
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nextPage = 0;
                isAllLoaded = false;
                getData();
            }
        });
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int childCount = recyclerView.getChildCount();//显示出来的item数量，不显示的直接remove
                int totalItemCount = mLayoutManager.getItemCount();//所有item数量
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
//                Log.i(TAG, "childCount:" + childCount + " totalItemCount:" + totalItemCount + " lastVis:" + lastVisibleItemPosition);
                //最后一个item显示、未加载所有数据、非加载完所有数据不超过一屏
                if (totalItemCount - lastVisibleItemPosition <= 1 && !isAllLoaded && totalItemCount >= 10)
                    getData();
            }
        });
        getData();
    }


    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meet, null);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final Meet info = mMembers.get(position);
            String nameMaleText = "*" + info.name_male.substring(1);
            String nameFemaleText = "*" + info.name_female.substring(1);
            holder.tv_name_male.setText(nameMaleText);
            holder.tv_name_female.setText(nameFemaleText);
            holder.tv_time.setText(info.meet_time);
            switch (info.meet_state) {
                case 1:
                    holder.tv_state.setText("等待中");
                    holder.tv_state.setTextColor(getResources().getColor(R.color.avatar_green));
                    break;
                case 2:
                    holder.tv_state.setText("预约中");
                    holder.tv_state.setTextColor(getResources().getColor(R.color.avatar_blue));
                    break;
                case 3:
                    holder.tv_state.setText("预约取消");
                    holder.tv_state.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case 4:
                    holder.tv_state.setText("已结束，月老卷 +10");
                    holder.tv_state.setTextColor(getResources().getColor(R.color.avatar_red));
                    break;
            }

//            holder.tv_age.setText(info.age + "岁");
//            holder.tv_hobby.setText("兴趣、爱好：" + info.hobby);
//            holder.tv_expect.setText("期望另一半：" + info.expect);
        }

        @Override
        public int getItemCount() {
            if (mMembers == null)
                return 0;
            else
                return mMembers.size();
        }
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_name_male;
        TextView tv_name_female;
        TextView tv_time;
        TextView tv_state;

        public MyHolder(View itemView) {
            super(itemView);
            tv_name_male = itemView.findViewById(R.id.tv_name_male);
            tv_name_female = itemView.findViewById(R.id.tv_name_female);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }


    private void getData() {
        if (mGetDataTask != null)
            return;
        mGetDataTask = new GetDataTask();
        mGetDataTask.execute(nextPage);
    }

    class GetDataTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int page = integers[0];
            return YuelaogeAPI.getMeet(YuelaoApp.mType, page);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mRefreshLayout.setRefreshing(false);
            onSuccess(s);
            mGetDataTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mRefreshLayout.setRefreshing(false);
            mGetDataTask = null;
        }
    }

    private void onSuccess(String result) {
        Domain<Meet> domain = CommonParser.parserList(result, Meet.class);
        if (domain.success) {
            if (domain.datalist != null && domain.datalist.size() > 0) {
                if (mMembers == null)
                    mMembers = domain.datalist;
                else {
                    if (nextPage == 0)
                        mMembers.clear();
                    mMembers.addAll(domain.datalist);
                }
                mAdapter.notifyDataSetChanged();
                nextPage++;
            } else {
                if (mMembers != null && mMembers.size() > 0) {
                    if (nextPage == 0) {
                        mMembers.clear();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "数据已加载完毕", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "无数据", Toast.LENGTH_SHORT).show();
                }
                isAllLoaded = true;
            }

        } else {
            Toast.makeText(mContext, domain.message, Toast.LENGTH_SHORT).show();
        }
    }


}
