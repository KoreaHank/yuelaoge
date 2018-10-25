package com.yimukeji.yuelaoge.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.database.MemberDao;
import com.yimukeji.yuelaoge.database.MemberDaoImpl;
import com.yimukeji.yuelaoge.database.YuelaoDao;
import com.yimukeji.yuelaoge.database.YuelaoDaoImpl;

public class API {
	HttpServletRequest mRequest;
	HttpServletResponse mResponse;

	public API(HttpServletRequest request, HttpServletResponse response) {
		mRequest = request;
		mResponse = response;
		mResponse.setHeader("Content-type", "text/html;charset=UTF-8");
		mResponse.setCharacterEncoding("UTF-8");
	}

	/**
	 * 登录操作
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login() throws ServletException, IOException {
		int type = Integer.parseInt(mRequest.getParameter("user_type"));
		String phone = mRequest.getParameter("phone");
		String password = mRequest.getParameter("password");
		Domain<JSONObject> domain = new Domain<JSONObject>();
		if (phone == null || phone.isEmpty()) {
			domain.msg = "手机号不能为空";
			mResponse.getWriter().append(JSON.toJSONString(domain));
			return;
		}
		if (password == null || password.isEmpty()) {
			domain.msg = "密码不能为空";
			mResponse.getWriter().append(JSON.toJSONString(domain));
			return;
		}
		JSONObject data = null;
		switch (type) {
		case 1:// 会员
			MemberDao ud = new MemberDaoImpl();
			data = ud.login(phone, password);
			break;
		case 2:// 月老
			YuelaoDao yl = new YuelaoDaoImpl();
			data = yl.login(phone, password);
		}

		if (data != null) {
			System.out.println(data.toJSONString());
			domain.code = 1;
			domain.msg = "查询成功";
			domain.data = data;
			mResponse.getWriter().append(JSON.toJSONString(domain));
		} else {
			domain.code = 0;
			domain.msg = "用户名或密码错误";
			mResponse.getWriter().append(JSON.toJSONString(domain));
		}
	}

	public void regist() throws ServletException, IOException {
		String data = mRequest.getParameter("data");
		Domain<String> domain = new Domain<String>();
		Member member = JSON.parseObject(data, Member.class);
		if (member != null) {
			MemberDao md = new MemberDaoImpl();
			boolean result = md.add(member);
			if (result) {
				domain.code = 1;
				domain.msg = "注册成功";
			} else {
				domain.code = 0;
				domain.msg = "注册失败";
			}

		} else {
			domain.code = 0;
			domain.msg = "参数有误";
		}
		mResponse.getWriter().append(JSON.toJSONString(domain));
	}
}
