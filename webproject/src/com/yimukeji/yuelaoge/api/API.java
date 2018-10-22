package com.yimukeji.yuelaoge.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.database.MemberDao;
import com.yimukeji.yuelaoge.database.MemberDaoImpl;

public class API {
	HttpServletRequest mRequest;
	HttpServletResponse mResponse;

	public API(HttpServletRequest request, HttpServletResponse response) {
		mRequest = request;
		mResponse = response;
		mResponse.setHeader("Content-type", "text/html;charset=UTF-8");
		mResponse.setCharacterEncoding("UTF-8");
	}

	public void login() throws ServletException, IOException {
		int type = Integer.parseInt(mRequest.getParameter("user_type"));
		if (type == 1) {// 会员
			Domain<Member> domain = new Domain<Member>();
			String name = mRequest.getParameter("name");
			if (name == null || name.isEmpty()) {
				domain.msg = "用户名不能为空";
				mResponse.getWriter().append(JSON.toJSONString(domain));
				return;
			}
			String pwd = mRequest.getParameter("password");
			if (pwd == null || pwd.isEmpty()) {
				domain.msg = "密码不能为空";
				mResponse.getWriter().append(JSON.toJSONString(domain));
				return;
			}
			MemberDao ud = new MemberDaoImpl();
			Member user = null;
			if (user != null) {
				domain.code = 1;
				domain.msg = "用户查询为空";
				domain.data = user;
				mResponse.getWriter().append(JSON.toJSONString(domain));
			} else {
				domain.msg = "查询成功";
				mResponse.getWriter().append(JSON.toJSONString(domain));
			}
		}
	}
}
