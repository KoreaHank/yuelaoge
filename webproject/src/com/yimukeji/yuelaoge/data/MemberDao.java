package com.yimukeji.yuelaoge.data;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;

public interface MemberDao {
	public static final int ERROR_PHONE_EXIST = -10;

	public static final int TYPE_ALL = 0;
    public static final int TYPE_MALE = 1;
    public static final int TYPE_FEMALE = 2;
    public static final int TYPE_MINE = 3;

	// 添加纪录
	public int add(Member account);
	//判断该手机号是否存在
	public boolean isExist(String phone);
	//登录
	public JSONObject login(String phone,String password);
	//更新用户信息
	public JSONObject getUserInfo(int id);
	//获取成功信息
	public JSONArray getMember(int type,int page,int userid);
	//更新头像
	public boolean updateAvatar(int userid,String avatarName);
	// 查询纪录
	public List<Member> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Member account);

}
