package com.yimukeji.yuelaoge.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.data.MeetDao;
import com.yimukeji.yuelaoge.data.MeetDaoImpl;
import com.yimukeji.yuelaoge.data.MemberDao;
import com.yimukeji.yuelaoge.data.MemberDaoImpl;
import com.yimukeji.yuelaoge.data.YuelaoDao;
import com.yimukeji.yuelaoge.data.YuelaoDaoImpl;

public class API {
	HttpServletRequest mRequest;
	HttpServletResponse mResponse;

	private static final String METHOD_LOGIN = "login";// 登录
	private static final String METHOD_REGIST = "regist";// 会员注册
	private static final String METHOD_GETMEMBER = "getmember";// 月老获取成员信息
	private static final String METHOD_GET_MEMBER_MEET = "getmembermeet";// 月老获取成员会面信息
	private static final String METHOD_MEET = "meet";// 申请见面

	public static final int TYPE_NONE = 0;
	public static final int TYPE_MEMBER = 1;
	public static final int TYPE_YUELAO = 2;
	public static final int TYPE_ADMIN = 3;

	public API(HttpServletRequest request, HttpServletResponse response) {
		mRequest = request;
		mResponse = response;
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
	}

	public void request() throws ServletException, IOException {
		String method = mRequest.getParameter("method");
		if (method == null || method.isEmpty())
			return;
		switch (method) {
		case METHOD_LOGIN:
			login();
			break;
		case METHOD_REGIST:
			regist();
			break;
		case METHOD_GETMEMBER:
			getMember();
			break;
		case METHOD_GET_MEMBER_MEET:
			getMeetRecord();
			break;
		case METHOD_MEET:
			meet();
			break;
		default:
			break;
		}
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
			mResponse.getWriter().append(domain.toJson().toJSONString());
			return;
		}
		if (password == null || password.isEmpty()) {
			domain.msg = "密码不能为空";
			mResponse.getWriter().append(domain.toJson().toJSONString());
			return;
		}
		JSONObject data = null;
		switch (type) {
		case TYPE_MEMBER:// 会员
			MemberDao ud = new MemberDaoImpl();
			data = ud.login(phone, password);
			break;
		case TYPE_YUELAO:// 月老
			YuelaoDao yl = new YuelaoDaoImpl();
			data = yl.login(phone, password);
		}

		if (data != null) {
			System.out.println(data.toJSONString());
			domain.code = 1;
			domain.msg = "查询成功";
			domain.data = data;
			mResponse.getWriter().append(domain.toJson().toJSONString());
		} else {
			domain.code = 0;
			domain.msg = "用户名或密码错误";
			mResponse.getWriter().append(domain.toJson().toJSONString());
		}
	}

	public void regist() throws ServletException, IOException {
		String data = mRequest.getParameter("data");
		Domain<JSONObject> domain = new Domain<JSONObject>();
		Member member = JSON.parseObject(data, Member.class);
		if (member != null) {
			MemberDao md = new MemberDaoImpl();
			int result = md.add(member);
			if (result > 0) {
				domain.code = 1;
				domain.msg = "注册成功";
			} else {
				domain.code = 0;
				if (result == MemberDao.ERROR_PHONE_EXIST)
					domain.msg = "手机号已存在";
				else
					domain.msg = "注册失败";
			}

		} else {
			domain.code = 0;
			domain.msg = "参数有误";
		}
		mResponse.getWriter().append(domain.toJson().toJSONString());
	}

	public void getMember() throws ServletException, IOException {
		Domain<JSONArray> domain = new Domain<JSONArray>();
		try {
			int type = Integer.parseInt(mRequest.getParameter("type"));
			int page = Integer.parseInt(mRequest.getParameter("page"));
			int userid = Integer.parseInt(mRequest.getParameter("userid"));
			MemberDao md = new MemberDaoImpl();
			JSONArray array = md.getMember(type, page, userid);
			domain.code = 1;
			domain.msg = "查询完成";
			domain.data = array;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			domain.code = 0;
			domain.msg = "参数有误";
		}
		mResponse.getWriter().append(domain.toJson().toJSONString());
	}

	public void getMeetRecord() throws IOException {
		Domain<JSONArray> domain = new Domain<JSONArray>();
		try {
			int type = Integer.parseInt(mRequest.getParameter("type"));
			int page = Integer.parseInt(mRequest.getParameter("page"));
			int userid = Integer.parseInt(mRequest.getParameter("userid"));
			MeetDao md = new MeetDaoImpl();
			JSONArray array = md.query(userid, type, page);
			domain.code = 1;
			domain.msg = "查询完成";
			domain.data = array;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			domain.code = 0;
			domain.msg = "参数有误";
		}
		mResponse.getWriter().append(domain.toJson().toJSONString());
	}

	public void meet() throws ServletException, IOException {
		Domain<JSONObject> domain = new Domain<JSONObject>();

		int maleid = Integer.parseInt(mRequest.getParameter("maleid"));
		String malename = mRequest.getParameter("malename");
		int maleyuelaoid = Integer.parseInt(mRequest.getParameter("maleyuelaoid"));
		int femaleid = Integer.parseInt(mRequest.getParameter("femaleid"));
		String femalename = mRequest.getParameter("femalename");
		int femaleyuelaoid = Integer.parseInt(mRequest.getParameter("femaleyuelaoid"));
		MeetDao md = new MeetDaoImpl();
		int result = md.add(maleid, malename, maleyuelaoid, femaleid, femalename, femaleyuelaoid);
		if (result > 0) {
			domain.code = 1;
			domain.msg = "预约成功";
		} else {
			domain.code = 0;
			if (result == MeetDao.ERROR_MEET_EXIST)
				domain.msg = "已预约，请耐心等待";
			else
				domain.msg = "预约失败";
		}
		mResponse.getWriter().append(domain.toJson().toJSONString());
	}

}
