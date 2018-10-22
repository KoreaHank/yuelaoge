package com.yimukeji.yuelaoge.database;

import java.util.List;

import com.yimukeji.yuelaoge.bean.Yuelao;

public interface YuelaoDao {

	// 添加纪录
	public void add(Yuelao account);

	// 查询纪录
	public List<Yuelao> query();

	// 删除
	public void delete(int id);

	// 修改
	public void modify(Yuelao account);

}
