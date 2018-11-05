package com.yimukeji.yuelaoge.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yimukeji.yuelaoge.R;
import com.yimukeji.yuelaoge.YuelaogeAPI;
import com.yimukeji.yuelaoge.bean.CommonParser;
import com.yimukeji.yuelaoge.bean.Domain;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.ui.UserDetailActivity;

import java.util.List;

public class MainFragment extends BaseFragment {

    RecyclerView mListView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;

    GetDataTask mGetDataTask;
    List<Member> mMembers;
    int nextPage = 0;
    boolean isAllLoaded = false;
    int mMemberType = Member.TYPE_ALL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent,false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final Member info = mMembers.get(position);
            holder.iv_sex.setImageResource(info.sex.equals("男") ? R.drawable.icon_male : R.drawable.icon_female);
            String nameText = "*" + info.name.substring(1);
            holder.tv_name.setText(nameText);
            holder.tv_age.setText(info.age + "岁");
            holder.tv_education.setText(info.education);
            holder.tv_address.setText(info.address);
            holder.tv_expect.setText("期望另一半：" + info.expect);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(mContext, UserDetailActivity.class);
                    mContext.startActivity(detailIntent);
                }
            });
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

        ImageView iv_avatar;
        ImageView iv_sex;
        TextView tv_name;
        TextView tv_age;
        TextView tv_education;
        TextView tv_address;
        TextView tv_expect;

        public MyHolder(View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_education = itemView.findViewById(R.id.tv_education);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_expect = itemView.findViewById(R.id.tv_expect);
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
            return YuelaogeAPI.getMember(mMemberType, page);
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
        Domain<Member> domain = CommonParser.parserList(result, Member.class);
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

    public void setType(int type) {
        mMemberType = type;
        nextPage = 0;
        isAllLoaded = false;
        mRefreshLayout.setRefreshing(true);
        getData();
    }
}
