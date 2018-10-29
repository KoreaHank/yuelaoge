package com.yimukeji.yuelaoge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yimukeji.yuelaoge.api.API;

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/api")
public class ApiServlet extends HttpServlet {
	/**
	 * <servlet> <servlet-name>ApiServlet</servlet-name>
	 * <servlet-class>com.yimukeji.yuelaoge.ApiServlet</servlet-class> </servlet>
	 * <servlet-mapping> <servlet-name>ApiServlet</servlet-name>
	 * <url-pattern>api</url-pattern> </servlet-mapping>
	 */
	private static final long serialVersionUID = 1L;
	private static final String METHOD_LOGIN = "login";
	private static final String METHOD_REGIST = "regist";
	private static final String METHOD_GETMEMBER = "getmember";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApiServlet() {

		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		dealRequest(request, response);
	}

	private void dealRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method == null || method.isEmpty())
			return;
		API api = new API(request, response);
		switch (method) {
		case METHOD_LOGIN:
			api.login();
			break;
		case METHOD_REGIST:
			api.regist();
			break;
		case METHOD_GETMEMBER:
			api.getMember();
		default:
			break;
		}
	}
}
