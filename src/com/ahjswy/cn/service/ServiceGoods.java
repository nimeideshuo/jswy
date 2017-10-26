package com.ahjswy.cn.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;

import com.ahjswy.cn.bean.GoodEntity;
import com.ahjswy.cn.model.Goods;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Pricesystem;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.request.ReqStrGetGoodsPricePD;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.utils.Utils_help;

public class ServiceGoods {
	private String baseAddress = "goods";
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

	public String gds_GetAllGoodsBatchPD(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "getallgoodsbatchpd");
		map.put("goodsid", paramString2);
		map.put("warehouseid", paramString1);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetGoodsBatch(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "getgoodsbatch");
		map.put("goodsid", paramString2);
		map.put("warehouseid", paramString1);
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetGoodsWarehouses(String goodsid, boolean isgetbatch) {
		String url = Utils.getServiceAddress(this.baseAddress, "getgoodswarehouses");
		map.put("goodsid", goodsid);
		map.put("isgetbatch", isgetbatch + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPrice(List<ReqStrGetGoodsPrice> paramList, boolean ischeckwarehouse,
			boolean isgetbatch) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodsprice");
		map.put("parameter", JSONUtil.object2Json(paramList));
		map.put("ischeckwarehouse", ischeckwarehouse + "");
		map.put("isgetbatch", isgetbatch + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPriceDB(List<ReqStrGetGoodsPrice> paramList, String inwarehouseid,
			boolean ischeckwarehouse, boolean isgetbatch) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodspricedb");

		map.put("parameter", JSONUtil.object2Json(paramList));
		map.put("inwarehouseid", inwarehouseid);
		map.put("ischeckwarehouse", ischeckwarehouse + "");
		map.put("isgetbatch", isgetbatch + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String gds_GetMultiGoodsPricePD(List<ReqStrGetGoodsPricePD> paramList) {
		String url = Utils.getServiceAddress(this.baseAddress, "getmultigoodspricepd");
		map.put("parameter", JSONUtil.object2Json(paramList));
		return new Utils_help().getServiceInfor(url, map);
	}

	/**
	 * 添加商品
	 * 
	 * @return
	 */
	public String gds_AddGood(Goods goods, List<Pricesystem> price, List<GoodsUnit> listGoodUnit) {
		String url = Utils.getServiceAddress(this.baseAddress, "AddGoods");
		if (goods == null || price == null || listGoodUnit == null) {
			return "ReqAddGoods is null";
		}
		GoodEntity goodEntity = new GoodEntity();
		goodEntity.setGoods(JSONUtil.object2Json(goods));
		goodEntity.setGoodsPrice(JSONUtil.object2Json(price));
		goodEntity.setGoodsunit(JSONUtil.object2Json(listGoodUnit));
		map.put("parameter", JSONUtil.object2Json(goodEntity));
		return new Utils_help().getServiceInfor(url, map);
	}

	/*
	 * querygoodsavgprice 查询 商品平均价格
	 */
	public String gds_Querygoodsavgprice(GoodEntity goodEntity) {
		String url = Utils.getServiceAddress(this.baseAddress, "querygoodsavgprice");
		map.put("parameter", JSONUtil.object2Json(goodEntity));
		map.put("buildbegintime", goodEntity.getBuildbegintime());
		map.put("buildendtime", goodEntity.getBuildendtime());
		return new Utils_help().getServiceInfor(url, map);
	}

}