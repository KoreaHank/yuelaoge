package com.yimukeji.yuelaoge.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Domain<T extends JSON> {
	public int code = 0;
	public String msg;
	public T data;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		object.put("code", code);
		object.put("msg", msg);
		object.put("data", data);
		return object;
	}
}
