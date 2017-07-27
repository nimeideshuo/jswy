package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.CustomerThin;
import com.ahjswy.cn.model.SupplierThin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CustomerDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public CustomerThin getCustomer(String paramString) {
		List<CustomerThin> queryAllCustomer = queryAllCustomer();
		for (int i = 0; i < queryAllCustomer.size(); i++) {
			if (paramString.equals(queryAllCustomer.get(i).getId())) {
				return queryAllCustomer.get(i);
			}
		}
		return null;
	}

	// 查找所有SQL 客户
	public List<CustomerThin> queryAllCustomer() {
		this.db = this.helper.getWritableDatabase();
		Cursor cursor = this.db.rawQuery(
				"select id, name, pinyin, contactmoblie, address, settleterm, discountratio, promotionid, promotionname, pricesystemid from cu_customer where isavailable='1' and iscustomer='1' order by name",
				null);
		ArrayList<CustomerThin> localArrayList = new ArrayList<CustomerThin>();
		try {
			while (cursor.moveToNext()) {
				CustomerThin localCustomerThin = new CustomerThin();
				localCustomerThin.setId(cursor.getString(0));
				localCustomerThin.setName(cursor.getString(1));
				localCustomerThin.setPinyin(cursor.getString(2));
				localCustomerThin.setContactmoblie(cursor.getString(3));
				localCustomerThin.setAddress(cursor.getString(4));
				localCustomerThin.setSettleterm(cursor.getInt(5));
				localCustomerThin.setDiscountratio(cursor.getDouble(6));
				localCustomerThin.setPromotionid(cursor.getString(7));
				localCustomerThin.setPromotionname(cursor.getString(8));
				localCustomerThin.setPricesystemid(cursor.getString(9));
				localArrayList.add(localCustomerThin);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return localArrayList;
	}

	public CustomerThin queryCustomer(String customerName) {
		this.db = this.helper.getWritableDatabase();
		Cursor cursor = this.db.rawQuery(
				"select id, name, pinyin, contactmoblie, address, settleterm, discountratio, promotionid, promotionname, pricesystemid from cu_customer where name=? ",
				new String[] { customerName });
		ArrayList<CustomerThin> localArrayList = new ArrayList<CustomerThin>();
		try {
			if (cursor.moveToNext()) {
				CustomerThin localCustomerThin = new CustomerThin();
				localCustomerThin.setId(cursor.getString(0));
				localCustomerThin.setName(cursor.getString(1));
				localCustomerThin.setPinyin(cursor.getString(2));
				localCustomerThin.setContactmoblie(cursor.getString(3));
				localCustomerThin.setAddress(cursor.getString(4));
				localCustomerThin.setSettleterm(cursor.getInt(5));
				localCustomerThin.setDiscountratio(cursor.getDouble(6));
				localCustomerThin.setPromotionid(cursor.getString(7));
				localCustomerThin.setPromotionname(cursor.getString(8));
				localCustomerThin.setPricesystemid(cursor.getString(9));
				return localCustomerThin;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return null;
	}

	// 查找所有SQL 客户
	public List<SupplierThin> queryAllSupplier() {
		this.db = this.helper.getWritableDatabase();
		Cursor cursor = this.db.rawQuery(
				"select id, name, pinyin from cu_customer where isavailable='1' and issupplier='1' order by name",
				null);
		ArrayList<SupplierThin> localArrayList = new ArrayList<SupplierThin>();
		try {
			while (cursor.moveToNext()) {
				SupplierThin supplierthin = new SupplierThin();
				supplierthin.setId(cursor.getString(0));
				supplierthin.setName(cursor.getString(1));
				supplierthin.setPinyin(cursor.getString(2));
				localArrayList.add(supplierthin);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return localArrayList;
	}
}
