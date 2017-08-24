package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Unit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UnitDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public List<Unit> queryAll() {
		db = helper.getReadableDatabase();
		List<Unit> listUnit = new ArrayList<Unit>();
		Cursor cursor = null;
		try {

			String sql = "select id,name from sz_unit where isavailable = '1'";
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Unit unit = new Unit();
				unit.setId(cursor.getString(0));
				unit.setName(cursor.getString(1));
				listUnit.add(unit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return listUnit;
	}
}
