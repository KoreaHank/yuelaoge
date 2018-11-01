package com.yimukeji.yuelaoge.data;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Yuelao;

public interface YuelaoDao {

	// 添加纪录
	public boolean add(Yuelao account);
	
	public JSONObject login(String phone,String password);

	// 查询纪录
	public List<Yuelao> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Yuelao account);

}
