package com.ahjswy.cn.dao;

import com.ahjswy.cn.model.FieldSaleItem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FieldSaleItemDAO {
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public FieldSaleItemDAO() {
		this.helper = new DBOpenHelper();
	}

	/**
	 * 删除 销售单据
	 * 
	 * @param serialid
	 * @return
	 */
	public boolean delete(long serialid) {
		db = helper.getWritableDatabase();
		try {
			String deleteItem = "delete from kf_fieldsaleitem where serialid=?";
			db.execSQL(deleteItem, new String[] { "" + serialid });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				return true;
			}
		}
		return false;

	}

	public boolean deleteAll() {
		db = helper.getWritableDatabase();
		this.db.execSQL("delete from kf_fieldsaleitem");
		return true;
	}

	/***
	 * 根据 商品 id 删除
	 * 
	 * @param goodsid
	 * @return
	 */
	public boolean deleteGoods(String goodsid) {
		db = helper.getWritableDatabase();
		try {
			String deleteItem = "delete from kf_fieldsaleitem where goodsid =?";
			db.execSQL(deleteItem, new String[] { goodsid });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				return true;
			}
		}
		return false;

	}

	public FieldSaleItem getFieldSaleItem(long goodsid) {
		this.db = this.helper.getWritableDatabase();
		String sql = "select serialid, fieldsaleid,warehouseid, goodsid, saleunitid,salenum,saleprice,saleremark,givenum,giveunitid,giveremark,cancelbasenum,cancelremark,remark,giftgoodsid,giftgoodsname,giftunitid,giftunitname,giftnum,giftremarkpromotiontype,ispromotion,isexhibition from kf_fieldsaleitem where item.goodsid=?";
		Cursor query = db.rawQuery(sql, new String[] { String.valueOf(goodsid) });
		FieldSaleItem item = null;
		if (query.moveToNext()) {
			item = new FieldSaleItem();
			item.setSerialid(query.getLong(0));
			item.setFieldsaleid(query.getLong(1));
			item.setWarehouseid(query.getString(2));
			item.setGoodsid(query.getString(3));
			item.setSaleunitid(query.getString(4));
			item.setSalenum(query.getDouble(5));
			item.setSaleprice(query.getDouble(6));
			item.setSaleremark(query.getString(7));
			item.setGivenum(query.getDouble(8));
			item.setGiveunitid(query.getString(9));
			item.setGiveremark(query.getString(10));
			item.setCancelbatchin(query.getString(11));
			item.setCancelremark(query.getString(12));

		}
		return item;
	}

	/**
	 * 是否有此商品
	 * 
	 * @param goodsid
	 * @return
	 */
	public boolean isGood(String goodsid) {
		String sql = "select goodsid from kf_fieldsaleitem where goodsid =?";
		this.db = this.helper.getWritableDatabase();
		Cursor query = db.rawQuery(sql, new String[] { goodsid });
		if (query.moveToNext()) {
			return true;
		}
		return false;
	}

	public long saveGood(String goods) {
		this.db = this.helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goodsid", goods);
		db.insert("kf_fieldsaleitem", null, values);
		return 0;
	}

	public long saveFieldSaleItem(FieldSaleItem item) {
		this.db = this.helper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("fieldsaleid", Long.valueOf(item.getFieldsaleid()));
		// content.put("warehouseid", item.getWarehouseid());
		content.put("goodsid", item.getGoodsid());
		// content.put("saleunitid", item.getSaleunitid());
		// content.put("salenum", Double.valueOf(item.getSalenum()));
		// content.put("saleprice", Double.valueOf(item.getSaleprice()));
		// content.put("cancelunitid", item.getCancelunitid());
		// content.put("cancelnum", Double.valueOf(item.getCancelnum()));
		// content.put("cancelprice", Double.valueOf(item.getCancelprice()));
		// content.put("giveunitid", item.getGiveunitid());
		// content.put("givenum", Double.valueOf(item.getGivenum()));
		// content.put("isexhibition", item.isIsexhibition() ? "1" : "0");
		// content.put("exchangeunitid", item.getExchangeunitid());
		// content.put("exchangenum", Double.valueOf(item.getExchangenum()));
		// content.put("ispromotion", item.getIspromotion() ? 1 : 0);
		// content.put("promotiontype",
		// Integer.valueOf(item.getPromotiontype()));
		// content.put("giftgoodsid", item.getGiftgoodsid());
		// content.put("giftgoodsname", item.getGiftgoodsname());
		// content.put("giftunitid", item.getGiftunitid());
		// content.put("giftunitname", item.getGiftunitname());
		// content.put("giftnum", Double.valueOf(item.getGiftnum()));
		// content.put("cancelbatchin", item.getCancelbatchin());
		// content.put("cancelproductiondatein",
		// item.getCancelproductiondatein());
		// content.put("exchangebatchin", item.getExchangebatchin());
		// content.put("exchangeproductiondatein",
		// item.getExchangeproductiondatein());
		// content.put("saleremark", item.getSaleremark());
		// content.put("giftremark", item.getGiftremark());
		// content.put("cancelremark", item.getCancelremark());
		// content.put("giveremark", item.getGiveremark());
		// content.put("exchangeremark", item.getExchangeremark());
		long insert = this.db.insert("kf_fieldsaleitem", null, content);
		if (this.db != null) {
			this.db.close();
		}
		return insert;
	}

	public boolean updateFieldSaleItem(FieldSaleItem arg11) {
		return false;
	}

	public void deleteZeroItem(long fieldsaleid) {
		this.db = this.helper.getWritableDatabase();
		String sql = "delete from kf_fieldsaleitem where fieldsaleid=? and salenum=0 and cancelnum=0 and givenum=0 and exchangenum=0";
		try {
			this.db.execSQL(sql, new String[] { String.valueOf(fieldsaleid) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}

	public int getCount(long fieldsaleid) {
		this.db = this.helper.getWritableDatabase();
		int content = 0;
		try {
			Cursor cursor = this.db.rawQuery("select count(*) from kf_fieldsaleitem where fieldsaleid = ? ",
					new String[] { String.valueOf(fieldsaleid) });
			if (cursor.moveToNext()) {
				content = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public int getCount() {
		this.db = this.helper.getWritableDatabase();
		String sql = "select max(serialid) from kf_fieldsaleitem ";
		Cursor query = db.rawQuery(sql, null);
		if (query.moveToNext()) {
			return query.getInt(0);
		}
		return 0;
	}

}
