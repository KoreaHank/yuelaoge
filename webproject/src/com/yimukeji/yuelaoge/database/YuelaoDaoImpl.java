package com.yimukeji.yuelaoge.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.bean.Member;
import com.yimukeji.yuelaoge.bean.Yuelao;

public class YuelaoDaoImpl implements YuelaoDao {

	@Override
	public JSONObject login(String phone, String password) {
		JSONObject object = null;
		try {
			DBConnection.init();
			ResultSet rs = DBConnection
					.selectSql("select * from yuelao where phone='" + phone + "' and password='" + password + "'");
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				object = new JSONObject();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object value = rs.getObject(columnName);
					object.put(columnName, value);
				}
				break;
			}
			DBConnection.closeConn();
		} catch (SQLException e) {
			e.printStackTrace();
			object = null;
		}
		return object;
	}

	@Override
	public boolean add(Yuelao account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Yuelao> query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modify(Yuelao account) {
		// TODO Auto-generated method stub

	}

}
