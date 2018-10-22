package com.yimukeji.yuelaoge.api;

import java.util.List;

public class Domain<T> {
	public int code = 0;
	public String msg;
	public T data;
	public List<T> datalist;
}
