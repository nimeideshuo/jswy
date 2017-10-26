package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.utils.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class GoodsUnitDAO {
	private SQLiteDatabase db;
	private DBOpenHelper helper = new DBOpenHelper();

	// 基本单位
	public GoodsUnit getBasicUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where isbasic=1 and goodsid = ?",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				} else {
					goodsunit.setIsbasic(false);
				}
				if (cursor.getInt(4) == 1) {
					goodsunit.setIsshow(true);
				} else {
					goodsunit.setIsshow(false);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return goodsunit;
	}

	// 获取商品的件数
	public String getBigNum(String goodsid, String unitid, double num) {

		String v2 = null;
		if (TextUtils.isEmpty(goodsid) || TextUtils.isEmpty(goodsid)) {
			v2 = "";
		} else {
			GoodsUnit v4 = this.getBigUnit(goodsid);
			GoodsUnit v3 = this.getBasicUnit(goodsid);
			double v0 = num;
			if (!TextUtils.isEmpty(unitid)) {
				v0 *= this.getGoodsUnitRatio(goodsid, unitid);
			}
			v2 = "";
			int v6 = ((int) (v0 / v4.getRatio()));
			if (v6 != 0) {
				v2 = String.valueOf(v2) + v6 + v4.getUnitname();
				v0 -= (((double) v6)) * v4.getRatio();
			}
			if (v0 == 0) {
				return v2;
			}
			GoodsUnit v5 = this.getMidUnit(goodsid);
			if (v5 != null) {
				v6 = ((int) (v0 / v5.getRatio()));
				if (v6 != 0) {
					v2 = String.valueOf(v2) + v6 + v5.getUnitname();
					v0 -= (((double) v6)) * v5.getRatio();
				}
			}
			v2 = String.valueOf(v2)
					+ Utils.cutLastZero(new StringBuilder(String.valueOf(Utils.normalize(v0, 2))).toString())
					+ v3.getUnitname();
		}
		return v2;
	}

	// 获取大的单位id
	public GoodsUnit getBigUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where isshow=1 and goodsid = ?",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				} else {
					goodsunit.setIsbasic(false);
				}
				if (cursor.getInt(4) == 1) {
					goodsunit.setIsshow(true);
				} else {
					goodsunit.setIsshow(false);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}

		return goodsunit;
	}

	// 查询单位
	public GoodsUnit getOneUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,min(ratio) from sz_goodsunit where goodsid = ?", arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) != 1) {
					goodsunit.setIsbasic(true);
				}
				if (cursor.getInt(4) != 1) {
					goodsunit.setIsshow(true);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}

		return goodsunit;
	}

	public double getBigUnitRatio(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		Cursor localCursor = this.db.rawQuery("select ratio from sz_goodsunit where goodsid=? and isshow='1'",
				new String[] { goodsid });
		double d1 = 1.0D;
		try {
			if (localCursor.moveToNext()) {
				double d2 = localCursor.getDouble(0);
				d1 = d2;
			}
			return d1;
		} catch (Exception localException) {
			localException.printStackTrace();
			return d1;
		} finally {
			if (localCursor != null)
				localCursor.close();
			if (this.db != null)
				this.db.close();
		}
	}

	public GoodsUnit getGoodsUnit(String goodsid, String unitid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid, unitid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where goodsid = ? and unitid=?",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) != 1) {
					goodsunit.setIsbasic(true);
				}
				if (cursor.getInt(4) != 1) {
					goodsunit.setIsshow(true);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}

		return goodsunit;
	}

	// 获取商品 规格 1*多少
	public double getGoodsUnitRatio(String goodsid, String unitid) {
		this.db = this.helper.getReadableDatabase();
		Cursor localCursor = this.db.rawQuery("select ratio from sz_goodsunit where goodsid=? and unitid=?",
				new String[] { goodsid, unitid });
		double d1 = 1.0D;
		try {
			if (localCursor.moveToNext()) {
				d1 = localCursor.getDouble(0);
			}
			return d1;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			if (localCursor != null)
				localCursor.close();
			if (this.db != null)
				this.db.close();
		}
		return d1;
	}

	public GoodsUnit getMidUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where goodsid=? and ratio > 0 and ratio < 1 limit 1 offset 0",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				} else {
					goodsunit.setIsbasic(false);
				}
				if (cursor.getInt(4) == 1) {
					goodsunit.setIsshow(true);
				} else {
					goodsunit.setIsshow(false);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return goodsunit;
	}

	public boolean isBasicUnit(String goodsid, String unitid) {

		this.db = this.helper.getReadableDatabase();
		Cursor cursor = this.db.rawQuery("select isbasic from sz_goodsunit where goodsid=? and unitid=?",
				new String[] { goodsid, unitid });
		try {
			if (cursor.moveToNext()) {
				if (cursor.getInt(0) == 1) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return false;
	}

	// 查询基本位
	public GoodsUnit queryBaseUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where isbasic=1 and goodsid =? ",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				}
				if (cursor.getInt(4) == 1) {
					goodsunit.setIsshow(true);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}

		return goodsunit;

	}

	// 查询计件单位
	public GoodsUnit queryBigUnit(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where isshow=1 and goodsid =?",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				}
				if (cursor.getInt(4) == 1) {
					goodsunit.setIsshow(true);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}

		return goodsunit;
	}

	// 查询商品换算比例
	public GoodsUnit queryBigUnitRatio(String goodsId, String unitid) {
		this.db = this.helper.getReadableDatabase();
		String[] arrays = { goodsId, unitid };
		GoodsUnit goodsunit = null;
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,isbasic,isshow,ratio from sz_goodsunit where goodsid =? and unitid=?",
				arrays);
		try {
			while (cursor.moveToNext()) {
				goodsunit = new GoodsUnit();
				goodsunit.setGoodsid(cursor.getString(0));
				goodsunit.setUnitid(cursor.getString(1));
				goodsunit.setUnitname(cursor.getString(2));
				if (cursor.getInt(3) == 1) {
					goodsunit.setIsbasic(true);
				}
				if (cursor.getInt(4) != 1) {
					goodsunit.setIsshow(true);
				}
				goodsunit.setRatio(cursor.getDouble(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (this.db != null)
				this.db.close();
		}
		return goodsunit;
	}

	// 查询 商品单位
	public List<GoodsUnit> queryGoodsUnits(String goodsid) {
		this.db = this.helper.getReadableDatabase();
		Cursor localCursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,ratio,isbasic,isshow from sz_goodsunit where goodsid=? order by ratio",
				new String[] { goodsid });
		ArrayList<GoodsUnit> localArrayList = new ArrayList<GoodsUnit>();
		try {
			while (localCursor.moveToNext()) {
				GoodsUnit localGoodsUnit = new GoodsUnit();
				localGoodsUnit.setGoodsid(localCursor.getString(0));
				localGoodsUnit.setUnitid(localCursor.getString(1));
				localGoodsUnit.setUnitname(localCursor.getString(2));
				localGoodsUnit.setRatio(localCursor.getDouble(3));
				if (localCursor.getInt(4) != 1) {
					localGoodsUnit.setIsbasic(true);
				}
				if (localCursor.getInt(5) != 1) {
					localGoodsUnit.setIsshow(true);
				}
				localArrayList.add(localGoodsUnit);
			}
		} catch (Exception localException) {
			localException.printStackTrace();

		} finally {
			if (localCursor != null)
				localCursor.close();
			if (this.db != null)
				this.db.close();
		}
		return localArrayList;
	}

	public GoodsUnit queryUnit(String unitid) {
		this.db = this.helper.getReadableDatabase();
		Cursor cursor = this.db.rawQuery(
				"select goodsid,unitid,unitname,ratio,isbasic,isshow from sz_goodsunit where unitid=? ",
				new String[] { unitid });
		try {
			while (cursor.moveToNext()) {
				GoodsUnit goodsUnit = new GoodsUnit();
				goodsUnit.setGoodsid(cursor.getString(0));
				goodsUnit.setUnitid(cursor.getString(1));
				goodsUnit.setUnitname(cursor.getString(2));
				goodsUnit.setRatio(cursor.getDouble(3));
				if (cursor.getInt(4) != 1) {
					goodsUnit.setIsbasic(true);
				}
				if (cursor.getInt(5) != 1) {
					goodsUnit.setIsshow(true);
				}
				return goodsUnit;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}