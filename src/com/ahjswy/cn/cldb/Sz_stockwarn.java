package com.ahjswy.cn.cldb;

import java.sql.ResultSet;

public class Sz_stockwarn extends CloudDBBase {
	public double queryStockwarn(String warehouseid, String goodsid) {
		StringBuilder stockwarnBuilder = new StringBuilder();
		stockwarnBuilder.append("select a.warehouseid,a.goodsid,stocknum from sz_stockwarn a where a.warehouseid='");
		stockwarnBuilder.append(warehouseid).append("' and ");
		stockwarnBuilder.append("a.goodsid='" + goodsid + "'");
		ResultSet query = executeQuery(stockwarnBuilder.toString());
		if (query == null) {
			return -1;
		}
		try {
			while (query.next()) {
				return Double.parseDouble(query.getString("stocknum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
