package com.ahjswy.cn.cldb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.response.RespStockwarn;

public class Sz_stockwarn extends CloudDBBase {
	public double queryStockwarn(String warehouseid, String goodsid) {
		StringBuilder stockwarnBuilder = new StringBuilder();
		stockwarnBuilder.append("select a.warehouseid,a.goodsid,stocknum from sz_stockwarn a where a.warehouseid='");
		stockwarnBuilder.append(warehouseid).append("' and ");
		stockwarnBuilder.append("a.goodsid='" + goodsid + "'");
		ResultSet query = null;
		try {
			query = executeQuery(stockwarnBuilder.toString());
			if (query == null) {
				return 0;
			}

			while (query.next()) {
				return Double.parseDouble(query.getString("stocknum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (query != null) {
				try {
					query.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return 0;
	}

	/**
	 * 查询商品 总库存
	 * 
	 * @param goodsid
	 * @return
	 */
	public double querySumStock(String goodsid) {
		String sql = "select SUM(stocknum) as stocknum from sz_stockwarn a where a.isavailable='1' and a.goodsid='"
				+ goodsid + "'";
		ResultSet query = null;
		try {
			query = executeQuery(sql);
			if (query == null) {
				return 0;
			}
			while (query.next()) {
				return query.getDouble("stocknum");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (query != null) {
					query.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public List<RespStockwarn> queryStockwarnAll(String goodsid) {
		String sql = "select a.warehouseid,a.goodsid,stocknum from sz_stockwarn a where a.isavailable='1' and a.goodsid='"
				+ goodsid + "'";
		ResultSet query = null;
		try {
			query = executeQuery(sql);
			if (query == null) {
				return null;
			}
			List<RespStockwarn> listStockwarn = new ArrayList<RespStockwarn>();
			while (query.next()) {
				RespStockwarn stockwarn = new RespStockwarn();
				stockwarn.warehouseid = query.getString("warehouseid");
				stockwarn.goodsid = query.getString("goodsid");
				stockwarn.stocknum = query.getDouble("stocknum");
				listStockwarn.add(stockwarn);
			}
			return listStockwarn;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (query != null) {
					query.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
