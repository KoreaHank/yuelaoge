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
	public boolean add(Meet account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONArray query(int id, int type, int page) {
		JSONArray array = new JSONArray();
		String sql = "";
		switch (type) {
		case API.TYPE_MEMBER:// 会员
			sql = "select * from meet where id_male= " + id + " OR id_female=" + id + "  limit " + page * 10 + ",10";
			break;
		case API.TYPE_YUELAO://月老
			sql = "select * from meet where id_male_yuelao=" + id + " OR id_female_yuelao=" + id+" limit " + page * 10 + ",10";
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

}
