package com.yimukeji.yuelaoge.data;

import com.alibaba.fastjson.JSONArray;
import com.yimukeji.yuelaoge.bean.Meet;
import com.yimukeji.yuelaoge.bean.Member;

public interface MeetDao {
	public static final int ERROR_MEET_EXIST = -10;
	// 添加纪录
	public int add(int maleid, String malename, int maleyuelaoid, int femaleid, String femalename, int femaleyuelaoid);

	// 查询纪录
	/**
	 * 
	 * @param id   月老或会员ID
	 * @param type 月老还是会员
	 * @return
	 */
	public JSONArray query(int id, int type, int page);

	// 修改
	public void modify(Member account);

}
