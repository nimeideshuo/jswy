package com.ahjswy.cn.dao;

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

	public long saveFieldSaleItem() {
		return 0;
		// this.db = this.helper.getWritableDatabase();
		// ContentValues localContentValues = new ContentValues();
		// localContentValues.put("fieldsaleid",
		// Long.valueOf(paramFieldSaleItem.getFieldsaleid()));
		// localContentValues.put("goodsid", paramFieldSaleItem.getGoodsid());
		// localContentValues.put("warehouseid",
		// paramFieldSaleItem.getWarehouseid());
		// localContentValues.put("saleunitid",
		// paramFieldSaleItem.getSaleunitid());
		// localContentValues.put("salenum",
		// Double.valueOf(paramFieldSaleItem.getSalenum()));
		// localContentValues.put("saleprice",
		// Double.valueOf(paramFieldSaleItem.getSaleprice()));
		// localContentValues.put("cancelbasenum",
		// Double.valueOf(paramFieldSaleItem.getCancelbasenum()));
		// localContentValues.put("giveunitid",
		// paramFieldSaleItem.getGiveunitid());
		// localContentValues.put("givenum",
		// Double.valueOf(paramFieldSaleItem.getGivenum()));
		// int i;
		// if (paramFieldSaleItem.isIspromotion()) {
		// i = 1;
		// localContentValues.put("ispromotion", Integer.valueOf(i));
		// localContentValues.put("promotiontype",
		// Integer.valueOf(paramFieldSaleItem.getPromotiontype()));
		// localContentValues.put("giftgoodsid",
		// paramFieldSaleItem.getGiftgoodsid());
		// localContentValues.put("giftgoodsname",
		// paramFieldSaleItem.getGiftgoodsname());
		// localContentValues.put("giftunitid",
		// paramFieldSaleItem.getGiftunitid());
		// localContentValues.put("giftunitname",
		// paramFieldSaleItem.getGiftunitname());
		// localContentValues.put("giftnum",
		// Double.valueOf(paramFieldSaleItem.getGiftnum()));
		// if (!paramFieldSaleItem.isIsexhibition())
		// break label326;
		// }
		// label326: for (String localString = "1";; localString = "0") {
		// localContentValues.put("isexhibition", localString);
		// localContentValues.put("saleremark",
		// paramFieldSaleItem.getSaleremark());
		// localContentValues.put("giftremark",
		// paramFieldSaleItem.getGiftremark());
		// localContentValues.put("cancelremark",
		// paramFieldSaleItem.getCancelremark());
		// localContentValues.put("giveremark",
		// paramFieldSaleItem.getGiveremark());
		// long l = this.db.insert("kf_fieldsaleitem", null,
		// localContentValues);
		// if (this.db != null)
		// this.db.close();
		// return l;
		// i = 0;
		// break;
		// }
	}

}
