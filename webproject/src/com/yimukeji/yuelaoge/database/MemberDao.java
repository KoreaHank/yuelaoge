package com.yimukeji.yuelaoge.database;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;

public interface MemberDao {
	
	public static final int TYPE_ALL = 0;
    public static final int TYPE_MALE = 1;
    public static final int TYPE_FEMALE = 2;
    public static final int TYPE_MINE = 3;

	// 添加纪录
	public boolean add(Member account);
	
	public JSONObject login(String phone,String password);
	
	public JSONArray getMember(int type,int page,int userid);

	// 查询纪录
	public List<Member> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Member account);

}
