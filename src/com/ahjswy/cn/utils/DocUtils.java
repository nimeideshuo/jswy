package com.ahjswy.cn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.CustomerDAO;
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
	private static GoodsUnitDAO unitDAO = new GoodsUnitDAO();
	private static CustomerDAO customerdao = new CustomerDAO();
	private static GoodsPriceDAO goodspricedao = new GoodsPriceDAO();
	private static AccountPreference ap = new AccountPreference();

	static {
		if (serviceStore == null) {
			serviceStore = new ServiceStore();
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
		String stockbigName = null;
		if (goodsUnit == null) {
			return "";
		}
		// 查询商品比例
		double ratio = unitDAO.getGoodsUnitRatio(goodsUnit.getGoodsid(), goodsUnit.getUnitid());
		// 判断是否整除
		double num = stocknumber % ratio;
		if (num == 0) {
			return (int) (stocknumber / ratio) + goodsUnit.getUnitname();
		}
		GoodsUnit baseBaseUnit = unitDAO.queryBaseUnit(goodsUnit.getGoodsid());
		if (baseBaseUnit == null) {
			return "";
		}
		stockbigName = (int) (stocknumber / ratio) == 0 ? "" : (int) (stocknumber / ratio) + goodsUnit.getUnitname();
		if (num % baseBaseUnit.getRatio() == 0) {
			return stockbigName + (int) num + baseBaseUnit.getUnitname();
		}
		return stockbigName + Utils.normalize(num * baseBaseUnit.getRatio(), 2) + baseBaseUnit.getUnitname();
	}

	public static String Stocknum(double stocknumber, String goodsid, String unitid, String unitname) {
		String stockbigName = null;
		if (TextUtils.isEmpty(goodsid) || TextUtils.isEmpty(unitid) || TextUtils.isEmpty(unitname)) {
			return "";
		}
		// 查询商品比例
		double ratio = unitDAO.getGoodsUnitRatio(goodsid, unitid);
		// 判断是否整除
		double num = stocknumber % ratio;
		if (num == 0) {
			return (int) (stocknumber / ratio) + unitname;
		}
		GoodsUnit baseBaseUnit = unitDAO.queryBaseUnit(goodsid);
		if (baseBaseUnit == null) {
			return "";
		}
		stockbigName = (int) (stocknumber / ratio) == 0 ? "" : (int) (stocknumber / ratio) + unitname;
		if (num % baseBaseUnit.getRatio() == 0) {
			return stockbigName + (int) num + baseBaseUnit.getUnitname();
		}
		return stockbigName + Utils.normalize(num * baseBaseUnit.getRatio(), 2) + baseBaseUnit.getUnitname();

		// if (TextUtils.isEmpty(goodsid)) {
		// return "";
		// }
		// GoodsUnit goodsRatio = dao.queryBigUnitRatio(goodsid, unitid);
		// GoodsUnit queryBaseUnit = dao.queryBaseUnit(goodsid);
		// int zs = (int) (stocknumber / goodsRatio.getRatio());
		// int xs = (int) Utils.normalizePrice(stocknumber %
		// goodsRatio.getRatio());
		// String stocknum = "";
		// if (zs != 0) {
		// stocknum = stocknum + zs + unitname;
		// }
		// if (xs != 0) {
		// stocknum = stocknum + xs + queryBaseUnit.getUnitname();
		// }
		// if (stocknum.length() == 0) {
		// stocknum = "0" + unitname;
		// }
		// return stocknum;
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
		GoodsUnit goodsRatio = unitDAO.queryBigUnitRatio(unit.getGoodsid(), unit.getUnitid());
		GoodsUnit queryBaseUnit = unitDAO.queryBaseUnit(unit.getGoodsid());
		if (goodsRatio == null || queryBaseUnit == null) {
			return "";
		}
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
	public static CustomerRecords getCustomerGoodsHistoryPrice(String customerid, String goodsid) {
		if (TextUtils.isEmpty(customerid) || TextUtils.isEmpty(goodsid)) {
			return null;
		}
		return customerfield.queryUnitidPrice(customerid, goodsid);
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
		if (customerid == null || docItem == null) {
			return 0;
		}
		double ratio = unitDAO.getGoodsUnitRatio(docItem.getGoodsid(), docItem.getUnitid());
		if (customerdao.isUseCustomerprice(customerid)) {
			CustomerRecords historyPrice = getCustomerGoodsHistoryPrice(customerid, docItem.getGoodsid());
			if (historyPrice != null) {
				double historyratio = unitDAO.getGoodsUnitRatio(docItem.getGoodsid(), historyPrice.getUnitid());
				// 除以 自身比例 * 与 要换算的比例
				return Utils.normalize(historyPrice.getPrice() / historyratio * ratio, 2);
			}
		}
		// 客户价格体系 中的设置
		String pricesystemid = customerdao.queryPricesystemid(customerid);
		if (!TextUtils.isEmpty(pricesystemid)) {
			// 使用客户的 价格体系的计件单价
			double bigPrice = goodspricedao.queryBasicPrice(docItem.getGoodsid(), docItem.getUnitid(), pricesystemid);
			if (bigPrice != 0) {
				return bigPrice;
			}

			double price = goodspricedao.queryBasicPrice(docItem.getGoodsid(), pricesystemid);
			return price * ratio;
		}
		// 使用默认的的 价格体系的计件单价
		double bigPrice = goodspricedao.queryBasicPrice(docItem.getGoodsid(), docItem.getUnitid(),
				Utils.DEFAULT_PRICESYSTEM + "");
		if (bigPrice != 0) {
			return bigPrice;
		}
		// 价格体系默认设置
		double basicPrice = getGoodsBasicPrice(docItem);
		return Utils.normalize(basicPrice * ratio, 2);
	}

	/**
	 * 根据单位查询 查询商品 客史价格 或者 价格体系预设价格
	 * 
	 * @param customerid
	 * @param docItem
	 * @return
	 */
	public static double getGoodsPrice(String customerid, String goodsid, String unitid) {
		CustomerRecords historyPrice = getCustomerGoodsHistoryPrice(customerid, goodsid);
		double ratio = unitDAO.getGoodsUnitRatio(goodsid, unitid);
		if (customerdao.isUseCustomerprice(customerid)) {
			// 是否使用客户客史单价
			if (historyPrice != null) {
				double historyratio = unitDAO.getGoodsUnitRatio(goodsid, historyPrice.getUnitid());
				return Utils.normalize(historyPrice.getPrice() / historyratio * ratio, 2);
				// 除以 自身比例 * 与 要换算的比例
			}
		}
		// 使用客户价格体系
		String pricesystemid = customerdao.queryPricesystemid(customerid);
		if (!TextUtils.isEmpty(pricesystemid)) {// 查询客户 价格体系 比对 基本单位的价格，计件单位 比例*
			// 使用客户的 价格体系的计件单价
			double bigPrice = goodspricedao.queryBasicPrice(goodsid, unitid, pricesystemid);
			if (bigPrice != 0) {
				return bigPrice;
			}
			double price = goodspricedao.queryBasicPrice(goodsid, pricesystemid);
			return price * ratio;
		}
		// 使用默认的的 价格体系的计件单价
		double bigPrice = goodspricedao.queryBasicPrice(goodsid, unitid, Utils.DEFAULT_PRICESYSTEM + "");
		if (bigPrice != 0) {
			return bigPrice;
		}
		// 客户 价格体系
		double basicPrice = getGoodsBasicPrice(goodsid, unitid);
		return Utils.normalize(basicPrice * ratio, 2);
	}

	/**
	 * 查询商品基本 价格
	 * 
	 * @param customerid
	 * @param docItem
	 * @return
	 */
	public static double getGoodsBasicPrice(DefDocItemXS docItem) {
		double basicPrice = goodspricedao.queryBasicPrice(docItem.getGoodsid(), docItem.getUnitid(),
				Utils.DEFAULT_PRICESYSTEM + "");
		if (basicPrice != 0) {
			return basicPrice;
		}
		// 显示价格体系查询的默认价格
		return goodspricedao.queryBasicPrice(docItem.getGoodsid(), Utils.DEFAULT_PRICESYSTEM + "");

	}

	/**
	 * 查询商品基本 价格
	 * 
	 * @param customerid
	 * @param goodsid
	 * @return
	 */
	public static double getGoodsBasicPrice(String goodsid, String unitid) {
		double basicPrice = goodspricedao.queryBasicPrice(goodsid, unitid, Utils.DEFAULT_PRICESYSTEM + "");
		if (basicPrice != 0) {
			return basicPrice;
		}
		// 显示价格体系查询的默认价格
		return goodspricedao.queryBasicPrice(goodsid, Utils.DEFAULT_PRICESYSTEM + "");
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

	/**
	 * 
	 * 获取商品的辅助计算件数
	 * 
	 * @param goodsid
	 *            商品 id
	 * @param num
	 *            商品数量
	 * @return
	 */
	public static double getAssistnum(String goodsid, String unitid, double num) {
		double unitRatio = unitDAO.getGoodsUnitRatio(goodsid, unitid);
		double bigUnitRatio = unitDAO.getBigUnitRatio(goodsid);
		return Utils.normalize((num * unitRatio) / bigUnitRatio, 4);
	}

	/**
	 * 蓝牙开关是否打开
	 * 
	 * @return
	 */
	public static boolean isBluetoothPrint() {
		return Boolean.parseBoolean(ap.getValue("bluetoothPrintIsShow", "false"));
	}

	/**
	 * 合并相同商品
	 * 
	 * @param item
	 */
	public static void combinationItem(List<DefDocItemXS> item) {
		int size = item.size();
		for (int i = 0; i < item.size(); i++) {
			DefDocItemXS items1 = item.get(i);
			for (int j = 0; j < item.size(); j++) {
				if (i == j) {
					continue;
				}
				DefDocItemXS itemxs2 = item.get(j);
				boolean isgoodsid = items1.getGoodsid().equals(itemxs2.getGoodsid());
				boolean isunid = items1.getUnitid().equals(itemxs2.getUnitid());
				boolean isprice = items1.getPrice() == itemxs2.getPrice();
				boolean isratio = items1.getDiscountratio() == itemxs2.getDiscountratio();
				boolean isWarehouseid = items1.getWarehouseid().equals(itemxs2.getWarehouseid());
				if (isgoodsid && isunid && isprice && isratio && isWarehouseid) {
					items1.setNum(items1.getNum() + itemxs2.getNum());
					items1.setBignum(unitDAO.getBigNum(items1.getGoodsid(), items1.getUnitid(), items1.getNum()));
					items1.setSubtotal(items1.getNum() * items1.getPrice());
					items1.setDiscountsubtotal(items1.getNum() * items1.getPrice() * items1.getDiscountratio());
					item.remove(j);
					break;
				}
			}
		}
		if (size != item.size()) {
			combinationItem(item);
		}
	}

}
