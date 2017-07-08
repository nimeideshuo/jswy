package com.ahjswy.cn.model;

import java.io.Serializable;

public class DefDocItemPD implements Serializable {
	private static final long serialVersionUID = 1L;
	private String barcode;
	private String batch;
	private String bignetnum;
	private String bignum;
	private String bigstocknum;
	private double costprice;
	private long docid;
	private String goodsid;
	private String goodsname;
	private boolean isusebatch;
	private long itemid;
	private String model;
	private double netamount;
	private double netnum;
	private double num;
	private String productiondate;
	private String remark;
	private long rversion;
	private String specification;
	private double stocknum;
	private String unitid;
	private String unitname;

	public DefDocItemPD() {
	}

	public DefDocItemPD(DefDocItemPD paramDefDocItemPD) {
		this.itemid = paramDefDocItemPD.getItemid();
		this.docid = paramDefDocItemPD.getDocid();
		this.goodsid = paramDefDocItemPD.getGoodsid();
		this.goodsname = paramDefDocItemPD.getGoodsname();
		this.unitid = paramDefDocItemPD.getUnitid();
		this.unitname = paramDefDocItemPD.getUnitname();
		this.num = paramDefDocItemPD.getNum();
		this.bignum = paramDefDocItemPD.getBignum();
		this.netnum = paramDefDocItemPD.getNetnum();
		this.bigstocknum = paramDefDocItemPD.getBigstocknum();
		this.stocknum = paramDefDocItemPD.getStocknum();
		this.bignetnum = paramDefDocItemPD.getBignetnum();
		this.netamount = paramDefDocItemPD.getNetamount();
		this.costprice = paramDefDocItemPD.getCostprice();
		this.remark = paramDefDocItemPD.getRemark();
		this.rversion = paramDefDocItemPD.getRversion();
		this.barcode = paramDefDocItemPD.getBarcode();
		this.model = paramDefDocItemPD.getModel();
		this.specification = paramDefDocItemPD.getSpecification();
		this.isusebatch = paramDefDocItemPD.isusebatch;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public String getBatch() {
		return this.batch;
	}

	public String getBignetnum() {
		return this.bignetnum;
	}

	public String getBignum() {
		return this.bignum;
	}

	public String getBigstocknum() {
		return this.bigstocknum;
	}

	public double getCostprice() {
		return this.costprice;
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

	public double getNetamount() {
		return this.netamount;
	}

	public double getNetnum() {
		return this.netnum;
	}

	public double getNum() {
		return this.num;
	}

	public String getProductiondate() {
		return this.productiondate;
	}

	public String getRemark() {
		return this.remark;
	}

	public long getRversion() {
		return this.rversion;
	}

	public String getSpecification() {
		return this.specification;
	}

	public double getStocknum() {
		return this.stocknum;
	}

	public String getUnitid() {
		return this.unitid;
	}

	public String getUnitname() {
		return this.unitname;
	}

	public boolean isIsusebatch() {
		return this.isusebatch;
	}

	public void setBarcode(String paramString) {
		this.barcode = paramString;
	}

	public void setBatch(String paramString) {
		this.batch = paramString;
	}

	public void setBignetnum(String paramString) {
		this.bignetnum = paramString;
	}

	public void setBignum(String paramString) {
		this.bignum = paramString;
	}

	public void setBigstocknum(String paramString) {
		this.bigstocknum = paramString;
	}

	public void setCostprice(double paramDouble) {
		this.costprice = paramDouble;
	}

	public void setDocid(long paramLong) {
		this.docid = paramLong;
	}

	public void setGoodsid(String paramString) {
		this.goodsid = paramString;
	}

	public void setGoodsname(String paramString) {
		this.goodsname = paramString;
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

	public void setNetamount(double paramDouble) {
		this.netamount = paramDouble;
	}

	public void setNetnum(double paramDouble) {
		this.netnum = paramDouble;
	}

	public void setNum(double paramDouble) {
		this.num = paramDouble;
	}

	public void setProductiondate(String paramString) {
		this.productiondate = paramString;
	}

	public void setRemark(String paramString) {
		this.remark = paramString;
	}

	public void setRversion(long paramLong) {
		this.rversion = paramLong;
	}

	public void setSpecification(String paramString) {
		this.specification = paramString;
	}

	public void setStocknum(double paramDouble) {
		this.stocknum = paramDouble;
	}

	public void setUnitid(String paramString) {
		this.unitid = paramString;
	}

	public void setUnitname(String paramString) {
		this.unitname = paramString;
	}

}