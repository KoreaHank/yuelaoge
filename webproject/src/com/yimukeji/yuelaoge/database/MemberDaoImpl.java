package com.yimukeji.yuelaoge.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;

public class MemberDaoImpl implements MemberDao {

	@Override
	public boolean add(Member member) {

		boolean flag = false;
		DBConnection.init();
		int i = DBConnection.addUpdDel("insert into member values('" + member.location + "','" + member.name + "',"
				+ member.phone + ",'" + member.password + "','" + member.sex + "','" + member.birthday + "','"
				+ member.id_card + "','" + member.avatar + "','" + member.address + "','" + member.health + "','"
				+ member.hobby + "','" + member.character + "','" + member.job + "','" + member.job_location + "',"
				+ member.height + "," + member.weight + ",'" + member.education + "','" + member.marry + "','"
				+ member.love + "','" + member.expect + "'," + member.id_yuelao + ",'" + member.comment + "',"
				+ member.flower + "," + member.coin + "," + member.check + "," + member.vip + ",'" + member.vip_date
				+ "')");

		if (i > 0) {
			flag = true;
		}
		DBConnection.closeConn();
		return flag;

	}

	@Override
	public List<Member> query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modify(Member account) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject login(String phone, String password) {
		JSONObject object = null;
		try {
			DBConnection.init();
			ResultSet rs = DBConnection
					.selectSql("select * from member where phone='" + phone + "' and password='" + password + "'");
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				object = new JSONObject();
				for (int i = 0; i < columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					Object value = rs.getObject(columnName);
					object.put(columnName, value);
				}
				break;
			}
			DBConnection.closeConn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

}
