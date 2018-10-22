package com.yimukeji.yuelaoge.database;

import java.util.List;

import com.yimukeji.yuelaoge.bean.Register;

public interface RegisterDao {

	// 添加纪录
	public void add(Register account);

	// 查询纪录
	public List<Register> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Register account);

}
