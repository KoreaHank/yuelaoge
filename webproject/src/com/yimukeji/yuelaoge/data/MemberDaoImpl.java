package com.yimukeji.yuelaoge.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.DBConnection;
import com.yimukeji.yuelaoge.bean.Member;

public class MemberDaoImpl implements MemberDao {

	@Override
	public int add(Member member) {
		if (isExist(member.phone)) {
			return MemberDao.ERROR_PHONE_EXIST;
		}
		DBConnection.init();
		String sql = "insert into member(location,name,phone,password,sex,birthday,age,create_date,id_card,avatar,address,health,pecuniary,hobby,disposition,job,job_location,height,weight,education,marry,love,expect,remark,id_yuelao,comment)"
				+ " values('" + member.location + "','" + member.name + "','" + member.phone + "','" + member.password
				+ "','" + member.sex + "','" + member.birthday + "'," + member.age + ",'" + member.create_date + "','"
				+ member.id_card + "','" + member.avatar + "','" + member.address + "','" + member.health + "','"
				+ member.pecuniary + "','" + member.hobby + "','" + member.disposition + "','" + member.job + "','"
				+ member.job_location + "'," + member.height + "," + member.weight + ",'" + member.education + "','"
				+ member.marry + "','" + member.love + "','" + member.expect + "','" + member.remark + "',"
				+ member.id_yuelao + ",'" + member.comment + "')";
		int i = DBConnection.addUpdDel(sql);
		DBConnection.closeConn();
		return i;

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
				for (int i = 1; i <= columnCount; i++) {
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

	@Override
	public JSONArray getMember(int type, int page, int userid) {
		JSONArray array = new JSONArray();
		String sql = "";
		switch (type) {
		case TYPE_ALL:
			sql = "select * from member where verify=1 limit " + page * 10 + ",10";
			break;
		case TYPE_MALE:
			sql = "select * from member where verify=1 and sex='男'  limit " + page * 10 + ",10";
			break;
		case TYPE_FEMALE:
			sql = "select * from member where verify=1 and sex='女'  limit " + page * 10 + ",10";
			break;
		case TYPE_MINE:
			sql = "select * from member where verify=1 and id_yuelao=" + userid + " limit " + page * 10 + ",10";
			break;
		default:
			break;
		}
		try {
			DBConnection.init();
			ResultSet rs = DBConnection.selectSql(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				JSONObject object = new JSONObject();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					Object value = rs.getObject(columnName);
					object.put(columnName, value);
				}
				array.add(object);
			}
			DBConnection.closeConn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}

	@Override
	public boolean isExist(String phone) {
		//
		boolean flag = false;
		DBConnection.init();
		String sql = "select COUNT(*) from member where phone='" + phone + "'";
		ResultSet rs = DBConnection.selectSql(sql);
		int count = 0;
		try {
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (count > 0) {
			flag = true;
		}
		DBConnection.closeConn();
		return flag;
	}

}
