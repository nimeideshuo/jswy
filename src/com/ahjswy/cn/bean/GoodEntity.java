package com.ahjswy.cn.bean;

import java.io.Serializable;

public class GoodEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String goods;
	public String goodsPrice;
	public String goodsunit;
	public String goodsimage;
	public String goodsid;
	public String customerid;
	public String buildendtime;
	public String buildbegintime;
	public int unittype;

	public String getBuildendtime() {
		return buildendtime;
	}

	public void setBuildendtime(String buildendtime) {
		this.buildendtime = buildendtime;
	}

	public String getBuildbegintime() {
		return buildbegintime;
	}

	public void setBuildbegintime(String buildbegintime) {
		this.buildbegintime = buildbegintime;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public int getUnittype() {
		return unittype;
	}

	public void setUnittype(int unittype) {
		this.unittype = unittype;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsunit() {
		return goodsunit;
	}

	public void setGoodsunit(String goodsunit) {
		this.goodsunit = goodsunit;
	}

	public String getGoodsimage() {
		return goodsimage;
	}

	public void setGoodsimage(String goodsimage) {
		this.goodsimage = goodsimage;
	}

}
