package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.response.RespGoodsPriceEntity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GoodsPriceDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	public double queryPrice(String goodsid, String unitid, int pricesystemid) {
		this.db = this.helper.getReadableDatabase();
		String sql = "select price from sz_goodsprice where goodsid=? and unitid=? and pricesystemid=?";
		try {
			Cursor cursor = db.rawQuery(sql, new String[] { goodsid, unitid, pricesystemid + "" });
			if (cursor.moveToNext()) {
				return cursor.getDouble(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public List<RespGoodsPriceEntity> queryPriceList(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String sql = "select goodsid,unitid,unitname,pricesystemid,pricesystemname,price from sz_goodsprice where goodsid=? and price!='0.0' and price is not null ";
		List<RespGoodsPriceEntity> listPriceEntity = new ArrayList<RespGoodsPriceEntity>();
		try {
			Cursor cursor = db.rawQuery(sql, new String[] { goodsid, });
			while (cursor.moveToNext()) {
				RespGoodsPriceEntity entity = new RespGoodsPriceEntity();
				entity.setGoodsid(cursor.getString(0));
				entity.setUnitid(cursor.getString(1));
				entity.setUnitname(cursor.getString(2));
				entity.setPricesystemid(cursor.getString(3));
				entity.setPricesystemname(cursor.getString(4));
				entity.setPrice(cursor.getDouble(5));
				listPriceEntity.add(entity);
			}
			return listPriceEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPriceEntity;
	}
}
