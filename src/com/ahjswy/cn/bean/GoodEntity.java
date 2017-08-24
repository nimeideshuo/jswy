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
