package com.yimukeji.yuelaoge.database;

import java.sql.*;

public class DBConnection {
	static String url = "jdbc:mysql://localhost:3306/store?useUnicode=true&characterEncoding=utf8";
	static String username = "root";
	static String password = "";
	static Connection conn = null;
	static ResultSet rs = null;
	static PreparedStatement ps = null;

	public static void init() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int addUpdDel(String sql) {
		int i = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return i;
	}

	public static ResultSet selectSql(String sql) {
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static void closeConn() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
