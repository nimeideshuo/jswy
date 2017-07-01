package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.Warehouse;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WarehouseDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public List<Warehouse> getAllWarehouses() {
		this.db = this.helper.getReadableDatabase();
		Cursor cursor = this.db.rawQuery("select id, name from sz_warehouse where istruck != '1' and isavailable='1'",
				null);
		List<Warehouse> localArrayList = new ArrayList<Warehouse>();
		try {
			while (cursor.moveToNext()) {
				Warehouse warehouse = new Warehouse();
				String id = cursor.getString(0);
				String name = cursor.getString(1);
				warehouse.setId(id);
				warehouse.setName(name);
				localArrayList.add(warehouse);
			}
			return localArrayList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return localArrayList;
	}

	// 获取仓库
	public Warehouse getWarehouse(String paramString) {
		this.db = this.helper.getReadableDatabase();
		String array[] = { paramString };
		Cursor cursor = this.db.rawQuery("select id, name from sz_warehouse where id=?", array);
		Warehouse warehouse = null;
		try {
			while (cursor.moveToNext()) {
				warehouse = new Warehouse();
				String id = cursor.getString(0);
				String name = cursor.getString(1);
				warehouse.setId(id);
				warehouse.setName(name);
			}
			return warehouse;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return warehouse;
	}
}
