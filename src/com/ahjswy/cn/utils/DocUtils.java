package com.ahjswy.cn.utils;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespGoodsWarehouse;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.service.ServiceStore;

public class DocUtils {
	private static ServiceStore serviceStore;
	private static GoodsUnitDAO dao;
	static {
		if (serviceStore == null) {
			serviceStore = new ServiceStore();
		}
		if (dao == null) {
			dao = new GoodsUnitDAO();
		}
	}

	public static ReqStrGetGoodsPrice GetMultiGoodsPrice(String customerid, DefDocItemXS item) {
		ArrayList<ReqStrGetGoodsPrice> list = new ArrayList<ReqStrGetGoodsPrice>();
		ReqStrGetGoodsPrice rsp = new ReqStrGetGoodsPrice();
		rsp.setType(1);
		rsp.setCustomerid(customerid);
		rsp.setWarehouseid(item.getWarehouseid());
		rsp.setGoodsid(item.getGoodsid());
		rsp.setUnitid(item.getUnitid());
		rsp.setPrice(0.0D);
		rsp.setIsdiscount(false);
		list.add(rsp);
		String goodsPrice = new ServiceGoods().gds_GetMultiGoodsPrice(list, true, item.isIsusebatch());
		if (RequestHelper.isSuccess(goodsPrice)) {
			List<ReqStrGetGoodsPrice> str2list = JSONUtil.str2list(goodsPrice, ReqStrGetGoodsPrice.class);
			return str2list.size() == 0 ? null : str2list.get(0);
		}
		return null;
	}

	public static ReqStrGetGoodsPrice GetMultiGoodsPrice(String customerid, String warehouseid, String goodsid,
			String unitid) {
		ArrayList<ReqStrGetGoodsPrice> list = new ArrayList<ReqStrGetGoodsPrice>();
		ReqStrGetGoodsPrice rsp = new ReqStrGetGoodsPrice();
		rsp.setType(1);
		rsp.setCustomerid(customerid);
		rsp.setWarehouseid(warehouseid);
		rsp.setGoodsid(goodsid);
		rsp.setUnitid(unitid);
		rsp.setPrice(0.0D);
		rsp.setIsdiscount(false);
		list.add(rsp);
		String goodsPrice = new ServiceGoods().gds_GetMultiGoodsPrice(list, true, false);
		if (com.ahjswy.cn.utils.TextUtils.isEmptyS(goodsPrice)) {
			List<ReqStrGetGoodsPrice> str2list = JSONUtil.str2list(goodsPrice, ReqStrGetGoodsPrice.class);
			return str2list.get(0);
		}
		return null;
	}

	/**
	 * 计算 库存
	 * 
	 * @param res
	 *            RespGoodsWarehouse 当前商品仓库
	 * @param goodsUnit
	 *            当前单位
	 * @param unit
	 * @return
	 */
	public static String Stocknum(RespGoodsWarehouse res, GoodsUnit goodsUnit) {
		if (res == null || goodsUnit == null) {
			return "";
		}
		GoodsUnit goodsRatio = dao.queryBigUnitRatio(goodsUnit.getGoodsid(), goodsUnit.getUnitid());
		GoodsUnit queryBaseUnit = dao.queryBaseUnit(goodsUnit.getGoodsid());
		int zs = (int) (res.getStocknum() / goodsRatio.getRatio());
		int xs = (int) Utils.normalizePrice(res.getStocknum() % goodsRatio.getRatio());
		String stocknum = "";
		if (zs != 0) {
			stocknum = stocknum + zs + goodsUnit.getUnitname();
		}
		if (xs != 0) {
			stocknum = stocknum + xs + queryBaseUnit.getUnitname();
		}
		if (stocknum.length() == 0) {
			stocknum = "0" + goodsUnit.getUnitname();
		}
		return stocknum;
	}

	public static String Stocknum(int stocknumber, GoodsUnit goodsUnit) {
		if (goodsUnit == null) {
			return "";
		}
		GoodsUnit goodsRatio = dao.queryBigUnitRatio(goodsUnit.getGoodsid(), goodsUnit.getUnitid());
		GoodsUnit queryBaseUnit = dao.queryBaseUnit(goodsUnit.getGoodsid());
		int zs = (int) (stocknumber / goodsRatio.getRatio());
		int xs = (int) Utils.normalizePrice(stocknumber % goodsRatio.getRatio());
		String stocknum = "";
		if (zs != 0) {
			stocknum = stocknum + zs + goodsUnit.getUnitname();
		}
		if (xs != 0) {
			stocknum = stocknum + xs + queryBaseUnit.getUnitname();
		}
		if (stocknum.length() == 0) {
			stocknum = "0" + goodsUnit.getUnitname();
		}
		return stocknum;
	}

	// 库存数量测试可用
	public static String setItemStockUnit(int stockNum, GoodsUnit unit) {
		if (unit == null) {
			return "";
		}
		String stocknum = "";
		GoodsUnit goodsRatio = dao.queryBigUnitRatio(unit.getGoodsid(), unit.getUnitid());
		GoodsUnit queryBaseUnit = dao.queryBaseUnit(unit.getGoodsid());
		int zs = (int) (stockNum / goodsRatio.getRatio());
		int xs = (int) Utils.normalizePrice(stockNum % goodsRatio.getRatio());
		if (zs != 0) {
			stocknum = stocknum + zs + unit.getUnitname();
		}
		if (xs != 0) {
			stocknum = stocknum + xs + queryBaseUnit.getUnitname();
		}
		if (stocknum.length() == 0) {
			stocknum = "0" + unit.getUnitname();
		}
		return stocknum;
	}

}
