package com.yimukeji.yuelaoge.database;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;

public interface MemberDao {

	// 添加纪录
	public boolean add(Member account);
	
	public JSONObject login(String phone,String password);

	// 查询纪录
	public List<Member> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Member account);

}
