package com.yimukeji.yuelaoge.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.DBConnection;
import com.yimukeji.yuelaoge.api.API;
import com.yimukeji.yuelaoge.bean.Meet;
import com.yimukeji.yuelaoge.bean.Member;

public class MeetDaoImpl implements MeetDao {

	@Override
	public JSONArray query(int id, int type, int page) {
		JSONArray array = new JSONArray();
		String sql = "";
		switch (type) {
		case API.TYPE_MEMBER:// 会员
			sql = "select * from meet where id_male= " + id + " OR id_female=" + id + "  limit " + page * 10 + ",10";
			break;
		case API.TYPE_YUELAO:// 月老
			sql = "select * from meet where id_male_yuelao=" + id + " OR id_female_yuelao=" + id + " limit " + page * 10
					+ ",10";
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
	public void modify(Member account) {
		// TODO Auto-generated method stub

	}

	public boolean isExist(int maleid, int femaleid) {
		//
		boolean flag = false;
		DBConnection.init();
		String sql = "select COUNT(*) from meet where (meet_state=1 or meet_state=2) and (id_male=" + maleid
				+ " and id_female=" + femaleid + ")";
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

	@Override
	public int add(int maleid, String malename, int maleyuelaoid, int femaleid, String femalename, int femaleyuelaoid) {
		if (isExist(maleid, femaleid)) {
			return MeetDao.ERROR_MEET_EXIST;
		}
		DBConnection.init();
		String sql = "insert into meet(id_male,name_male,id_male_yuelao,id_female,name_female,id_female_yuelao)"
				+ " values(" 
				+ maleid + ",'" 
				+ malename + "'," 
				+ maleyuelaoid + "," 
				+ femaleid + ",'" 
				+ femalename+ "'," 
				+ femaleyuelaoid +")";
		int i = DBConnection.addUpdDel(sql);
		DBConnection.closeConn();
		return i;
	}

}
