<%@page import="com.yimukeji.yuelaoge.ApiServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>月老阁：登录</title>
</head>
<body>
	<center>
		<h1>月老阁</h1>
		<form id="indexform" name="indexForm" action="ApiServlet"
			method="post">
			<table border="0">
				<tr>
					<td>账号：</td>
					<td><input type="text" name="username"></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="password"></td>
				</tr>
				<tr>
					<td>类型：</td>
					<td><input type="radio" name="member">会员 <input
						type="radio" name="yuelao">月老 <input type="radio"
						name="manager">管理员</td>
				</tr>
			</table>
			<br> <input type="submit" value="登录"
				style="color: red; width: 200px">
		</form>
	</center>

</body>
</html>