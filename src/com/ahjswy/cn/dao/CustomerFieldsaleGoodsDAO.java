package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.CustomerRecords;

import android.R.transition;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TableRow;

public class CustomerFieldsaleGoodsDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public List<CustomerRecords> queryAllCustomerPrice() {
		db = helper.getWritableDatabase();
		String sql = "select customerid,goodsid,goodsthirdclassid,unitid,price cu_customerfieldsalegoods where issale = 1";
		List<CustomerRecords> listCustomerRecords = new ArrayList<CustomerRecords>();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				CustomerRecords records = new CustomerRecords();
				records.setCustomerid(cursor.getString(0));
				records.setGoodsid(cursor.getString(1));
				records.setGoodsthirdclassid(cursor.getString(2));
				records.setUnitid(cursor.getString(3));
				records.setPrice(cursor.getDouble(4));
				listCustomerRecords.add(records);
			}
			return listCustomerRecords;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询商品单位客史价格
	public CustomerRecords queryUnitidPrice(String customerid, String goodsid, String unitid) {
		db = helper.getWritableDatabase();
		String sql = "select customerid,goodsid,goodsthirdclassid,unitid,price from cu_customerfieldsalegoods where issale = 'true' and customerid=? and goodsid=? and unitid=?";
		try {
			Cursor cursor = db.rawQuery(sql, new String[] { customerid, goodsid, unitid });
			if (cursor.moveToNext()) {
				CustomerRecords records = new CustomerRecords();
				records.setCustomerid(cursor.getString(0));
				records.setGoodsid(cursor.getString(1));
				records.setGoodsthirdclassid(cursor.getString(2));
				records.setUnitid(cursor.getString(3));
				records.setPrice(cursor.getDouble(4));
				return records;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
