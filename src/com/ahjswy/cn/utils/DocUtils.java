package com.ahjswy.cn.utils;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.CustomerFieldsaleGoodsDAO;
import com.ahjswy.cn.dao.GoodsPriceDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.CustomerRecords;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.UnitidPrice;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespGoodsPriceEntity;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.service.ServiceStore;

import android.text.TextUtils;

public class DocUtils {
	private static ServiceStore serviceStore;
	private static GoodsUnitDAO dao;
	private static GoodsPriceDAO goodspricedao = new GoodsPriceDAO();

	static {
		if (serviceStore == null) {
			serviceStore = new ServiceStore();
		}
		if (dao == null) {
			dao = new GoodsUnitDAO();
		}
	}

	public static ReqStrGetGoodsPrice GetMultiGoodsPrice(String customerid, DefDocItemXS item) {
		if (item == null) {
			return null;
		}
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
	// public static String Stocknum(RespGoodsWarehouse res, GoodsUnit
	// goodsUnit) {
	// if (res == null || goodsUnit == null) {
	// return "";
	// }
	// GoodsUnit goodsRatio = dao.queryBigUnitRatio(goodsUnit.getGoodsid(),
	// goodsUnit.getUnitid());
	// GoodsUnit queryBaseUnit = dao.queryBaseUnit(goodsUnit.getGoodsid());
	// int zs = (int) (res.getStocknum() / goodsRatio.getRatio());
	// int xs = (int) Utils.normalizePrice(res.getStocknum() %
	// goodsRatio.getRatio());
	// String stocknum = "";
	// if (zs != 0) {
	// stocknum = stocknum + zs + goodsUnit.getUnitname();
	// }
	// if (xs != 0) {
	// stocknum = stocknum + xs + queryBaseUnit.getUnitname();
	// }
	// if (stocknum.length() == 0) {
	// stocknum = "0" + goodsUnit.getUnitname();
	// }
	// return stocknum;
	// }
	/**
	 * 商品 库存单位转换
	 * 
	 * @param stocknumber
	 *            库存数量
	 * @param goodsid
	 *            商品id
	 * @return
	 */
	public static String Stocknum(double stocknumber, GoodsUnit goodsUnit) {
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

	/**
	 * 商品 库存单位转换
	 * 
	 * @param stocknumber
	 *            库存数量
	 * @param goodsid
	 *            商品id
	 * @return
	 */
	// public static String Stocknum(double stocknumber, String goodsid) {
	// if (TextUtils.isEmpty(goodsid)) {
	// return "";
	// }
	// GoodsUnit goodsRatio = dao.queryBigUnitRatio(goodsUnit.getGoodsid(),
	// goodsUnit.getUnitid());
	// GoodsUnit queryBaseUnit = dao.queryBaseUnit(goodsUnit.getGoodsid());
	// int zs = (int) (stocknumber / goodsRatio.getRatio());
	// int xs = (int) Utils.normalizePrice(stocknumber % goodsRatio.getRatio());
	// String stocknum = "";
	// if (zs != 0) {
	// stocknum = stocknum + zs + goodsUnit.getUnitname();
	// }
	// if (xs != 0) {
	// stocknum = stocknum + xs + queryBaseUnit.getUnitname();
	// }
	// if (stocknum.length() == 0) {
	// stocknum = "0" + goodsUnit.getUnitname();
	// }
	// return stocknum;
	// }

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

	private static CustomerFieldsaleGoodsDAO customerfield = new CustomerFieldsaleGoodsDAO();

	/**
	 * 查询客户商品客史价格
	 * 
	 * @param customerid
	 *            客户id
	 * @param goodsid
	 *            商品id
	 * @param unitid
	 *            单位id
	 * @return
	 */
	public static CustomerRecords getCustomerGoodsHistoryPrice(String customerid, String goodsid, String unitid) {
		if (TextUtils.isEmpty(customerid) || TextUtils.isEmpty(goodsid) || TextUtils.isEmpty(unitid)) {
			return null;
		}
		return customerfield.queryUnitidPrice(customerid, goodsid, unitid);
	}

	/**
	 * 查询多个商品客史价格
	 * 
	 * @param price
	 * @return
	 */
	public static List<UnitidPrice> getCustomerGoodsHistoryPrice(List<UnitidPrice> price) {
		if (price == null || price.size() == 0) {
			return null;
		}
		return customerfield.queryUnitidPriceList(price);
	}

	/**
	 * 查询商品 客史价格 或者 价格体系预设价格
	 * 
	 * @param customerid
	 * @param docItem
	 * @return
	 */
	public static double getGoodsPrice(String customerid, DefDocItemXS docItem) {
		return getGoodsPrice(customerid, docItem.getGoodsid(), docItem.getUnitid());
	}

	/**
	 * 查询商品 客史价格 或者 价格体系预设价格
	 * 
	 * @param customerid
	 * @param goodsid
	 * @param unitid
	 * @return
	 */
	public static double getGoodsPrice(String customerid, String goodsid, String unitid) {
		CustomerRecords historyPrice = DocUtils.getCustomerGoodsHistoryPrice(customerid, goodsid, unitid);
		if (historyPrice != null) {
			return historyPrice.getPrice();
		}
		return goodspricedao.queryPrice(goodsid, unitid, Utils.DEFAULT_PRICESYSTEM);
	}

	/**
	 * 查询商品价格体系
	 * 
	 * @param goodsid
	 * @return
	 */
	public static List<RespGoodsPriceEntity> queryGoodsPriceList(String goodsid) {
		return goodspricedao.queryPriceList(goodsid);
	}
}
