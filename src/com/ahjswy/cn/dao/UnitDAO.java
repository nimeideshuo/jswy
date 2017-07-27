package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.GoodsUnit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UnitDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public List<GoodsUnit> queryAll() {
		db = helper.getReadableDatabase();
		List<GoodsUnit> listUnit = new ArrayList<GoodsUnit>();
		Cursor cursor = null;
		try {

			String sql = "select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit";
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				GoodsUnit unit = new GoodsUnit();
				unit.setGoodsid(cursor.getString(0));
				unit.setUnitid(cursor.getString(1));
				unit.setUnitname(cursor.getString(2));
				unit.setIsbasic(cursor.getInt(3) == 1 ? true : false);
				unit.setIsshow(cursor.getInt(4) == 1 ? true : false);
				unit.setRatio(cursor.getDouble(5));
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
