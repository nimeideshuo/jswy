package com.ahjswy.cn.model;

import java.io.Serializable;

import com.ahjswy.cn.response.RespGoodsWarehouse;

public class DefDocItemXS implements Serializable, Comparable<DefDocItemXS> {
	private static final long serialVersionUID = 1L;
	// 条码
	private String barcode;
	// 批次
	private String batch;
	// 单位 数量
	private String bignum;
	// 成本费
	private double costprice;
	// 折扣单价
	private double discountprice;
	// 折扣
	private double discountratio;
	// 折扣小计
	private double discountsubtotal;
	private long docid;
	// 商品id
	private String goodsid;
	// 商品名称
	private String goodsname;
	// 是否 打折
	private boolean isdiscount;
	// 是否陈列
	private boolean isexhibition;
	// 是否 赠品
	private boolean isgift;
	// 【促】
	private boolean ispromotion;
	// 是否 批次
	private boolean isusebatch;

	private long itemid;
	// 手机号码
	private String model;
	// 数量
	private double num;

	private long outorderdocid;
	private String outorderdocshowid;
	private long outorderitemid;
	private long parentitemid;
	// 单价
	private double price;
	// 生产时间
	private String productiondate;
	// 促销类型
	private int promotiontype;
	// 促销名称
	private String promotiontypename;
	// 备注
	private String remark;
	private long rversion;
	// 规格
	private String specification;
	// 小计
	private double subtotal;

	private long tempitemid;
	// 单位id
	private String unitid;
	// 单位名称
	private String unitname;
	// 仓库id
	private String warehouseid;
	// 仓库名称
	private String warehousename;
	private RespGoodsWarehouse stocknum;
	public String goodStock;

	public DefDocItemXS() {
	}

	public DefDocItemXS(DefDocItemXS item) {
		this.itemid = item.getItemid();
		this.tempitemid = item.getTempitemid();
		this.docid = item.getDocid();
		this.goodsid = item.getGoodsid();
		this.goodsname = item.getGoodsname();
		this.warehouseid = item.getWarehouseid();
		this.warehousename = item.getWarehousename();
		this.unitid = item.getUnitid();
		this.unitname = item.getUnitname();
		this.num = item.getNum();
		this.bignum = item.getBignum();
		this.price = item.getPrice();
		this.subtotal = item.getSubtotal();
		this.discountratio = item.getDiscountratio();
		this.discountprice = item.getDiscountprice();
		this.discountsubtotal = item.getDiscountsubtotal();
		this.isgift = item.isgift;
		this.costprice = item.getCostprice();
		this.remark = item.getRemark();
		this.rversion = item.getRversion();
		this.isdiscount = item.isdiscount;
		this.barcode = item.getBarcode();
		this.batch = item.getBatch();
		this.productiondate = item.getProductiondate();
		this.model = item.getModel();
		this.specification = item.getSpecification();
		this.isexhibition = item.isexhibition;
		this.isusebatch = item.isusebatch;
		if ((item.isIspromotion()) && (item.getPromotiontype() == 0)) {
			this.ispromotion = true;
			this.parentitemid = 0L;
			this.promotiontype = item.getPromotiontype();
		} else {
			this.ispromotion = false;
			this.parentitemid = 0L;
			this.promotiontype = -1;
		}
		if (promotiontypename == item.getPromotiontypename()) {
			this.outorderdocid = 0L;
			this.outorderdocshowid = null;
			this.outorderitemid = 0L;
		} else {
			this.promotiontypename = null;
		}

	}

	public RespGoodsWarehouse getStocknum() {
		return stocknum;
	}

	public void setStocknum(RespGoodsWarehouse stocknum) {
		this.stocknum = stocknum;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public String getBatch() {
		return this.batch;
	}

	public String getBignum() {
		return this.bignum;
	}

	public double getCostprice() {
		return this.costprice;
	}

	// 折后单价
	public double getDiscountprice() {
		return this.discountprice;
	}

	// 单品折扣
	public double getDiscountratio() {
		return this.discountratio;
	}

	// 折扣小计
	public double getDiscountsubtotal() {
		return this.discountsubtotal;
	}

	public long getDocid() {
		return this.docid;
	}

	public String getGoodsid() {
		return this.goodsid;
	}

	public String getGoodsname() {
		return this.goodsname;
	}

	public long getItemid() {
		return this.itemid;
	}

	public String getModel() {
		return this.model;
	}

	// 数量
	public double getNum() {
		return this.num;
	}

	public long getOutorderdocid() {
		return this.outorderdocid;
	}

	public String getOutorderdocshowid() {
		return this.outorderdocshowid;
	}

	public long getOutorderitemid() {
		return this.outorderitemid;
	}

	public long getParentitemid() {
		return this.parentitemid;
	}

	// 单价
	public double getPrice() {
		return this.price;
	}

	// 生产时间
	public String getProductiondate() {
		return this.productiondate;
	}

	// 促销方式
	public int getPromotiontype() {
		return this.promotiontype;
	}

	// 促销名称
	public String getPromotiontypename() {
		return this.promotiontypename;
	}

	// 备注
	public String getRemark() {
		return this.remark;
	}

	public long getRversion() {
		return this.rversion;
	}

	// 规格
	public String getSpecification() {
		return this.specification;
	}

	// 小计
	public double getSubtotal() {
		return this.subtotal;
	}

	public long getTempitemid() {
		return this.tempitemid;
	}

	// 单位 id
	public String getUnitid() {
		return this.unitid;
	}

	// 单位名称
	public String getUnitname() {
		return this.unitname;
	}

	// 仓库 id
	public String getWarehouseid() {
		return this.warehouseid;
	}

	// 仓库 name
	public String getWarehousename() {
		return this.warehousename;
	}

	// 折扣
	public boolean isIsdiscount() {
		return this.isdiscount;
	}

	// 【列】
	public boolean isIsexhibition() {
		return this.isexhibition;
	}

	public boolean isIsgift() {
		return this.isgift;
	}

	// 促销
	public boolean isIspromotion() {
		return this.ispromotion;
	}

	// 批次
	public boolean isIsusebatch() {
		return this.isusebatch;
	}

	public void setBarcode(String paramString) {
		this.barcode = paramString;
	}

	// 批次
	public void setBatch(String paramString) {
		this.batch = paramString;
	}

	public void setBignum(String paramString) {
		this.bignum = paramString;
	}

	// 成本价格
	public void setCostprice(double paramDouble) {
		this.costprice = paramDouble;
	}

	// 折后单价
	public void setDiscountprice(double paramDouble) {
		this.discountprice = paramDouble;
	}

	// 停止 终止
	public void setDiscountratio(double paramDouble) {
		this.discountratio = paramDouble;
	}

	// 折扣小计
	public void setDiscountsubtotal(double paramDouble) {
		this.discountsubtotal = paramDouble;
	}

	public void setDocid(long paramLong) {
		this.docid = paramLong;
	}

	// 设置 商品 id
	public void setGoodsid(String paramString) {
		this.goodsid = paramString;
	}

	// 商品名称
	public void setGoodsname(String paramString) {
		this.goodsname = paramString;
	}

	// 折扣
	public void setIsdiscount(boolean paramBoolean) {
		this.isdiscount = paramBoolean;
	}

	public void setIsexhibition(boolean paramBoolean) {
		this.isexhibition = paramBoolean;
	}

	public void setIsgift(boolean paramBoolean) {
		this.isgift = paramBoolean;
	}

	public void setIspromotion(boolean paramBoolean) {
		this.ispromotion = paramBoolean;
	}

	public void setIsusebatch(boolean paramBoolean) {
		this.isusebatch = paramBoolean;
	}

	public void setItemid(long paramLong) {
		this.itemid = paramLong;
	}

	public void setModel(String paramString) {
		this.model = paramString;
	}

	// 数量
	public void setNum(double paramDouble) {
		this.num = paramDouble;
	}

	public void setOutorderdocid(long paramLong) {
		this.outorderdocid = paramLong;
	}

	public void setOutorderdocshowid(String paramString) {
		this.outorderdocshowid = paramString;
	}

	public void setOutorderitemid(long paramLong) {
		this.outorderitemid = paramLong;
	}

	public void setParentitemid(long paramLong) {
		this.parentitemid = paramLong;
	}

	public void setPrice(double paramDouble) {
		this.price = paramDouble;
	}

	public void setProductiondate(String paramString) {
		this.productiondate = paramString;
	}

	public void setPromotiontype(int paramInt) {
		this.promotiontype = paramInt;
	}

	public void setPromotiontypename(String paramString) {
		this.promotiontypename = paramString;
	}

	// 备注
	public void setRemark(String paramString) {
		this.remark = paramString;
	}

	public void setRversion(long paramLong) {
		this.rversion = paramLong;
	}

	public void setSpecification(String paramString) {
		this.specification = paramString;
	}

	// 小计
	public void setSubtotal(double paramDouble) {
		this.subtotal = paramDouble;
	}

	public void setTempitemid(long paramLong) {
		this.tempitemid = paramLong;
	}

	// 单位 ID
	public void setUnitid(String paramString) {
		this.unitid = paramString;
	}

	// 单位 name
	public void setUnitname(String paramString) {
		this.unitname = paramString;
	}

	public void setWarehouseid(String paramString) {
		this.warehouseid = paramString;
	}

	public void setWarehousename(String paramString) {
		this.warehousename = paramString;
	}

	@Override
	public int compareTo(DefDocItemXS another) {
		if (this.tempitemid < another.getTempitemid()) {
			return 1;
		}
		return -1;
	}

}