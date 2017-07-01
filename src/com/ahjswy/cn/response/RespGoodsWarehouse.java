package com.ahjswy.cn.response;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class RespGoodsWarehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("batch")
	private String batch;

	@JsonProperty("bigstocknum")
	private String bigstocknum;

	@JsonProperty("goodsid")
	private String goodsid;

	@JsonProperty("productiondate")
	private String productiondate;

	@JsonProperty("stocknum")
	private double stocknum;

	@JsonProperty("warehouseid")
	private String warehouseid;

	@JsonProperty("warehousename")
	private String warehousename;

	public RespGoodsWarehouse() {
	}

	public RespGoodsWarehouse(String paramString1, String paramString2, String paramString3, double paramDouble,
			String paramString4) {
		this.goodsid = paramString1;
		this.warehouseid = paramString2;
		this.warehousename = paramString3;
		this.stocknum = paramDouble;
		this.bigstocknum = paramString4;
	}

	public String getBatch() {
		return this.batch;
	}

	@JsonIgnore
	public String getBigstocknum() {
		return this.bigstocknum;
	}

	@JsonIgnore
	public String getGoodsid() {
		return this.goodsid;
	}

	public String getProductiondate() {
		return this.productiondate;
	}

	@JsonIgnore
	public double getStocknum() {
		return this.stocknum;
	}

	@JsonIgnore
	public String getWarehouseid() {
		return this.warehouseid;
	}

	@JsonIgnore
	public String getWarehousename() {
		return this.warehousename;
	}

	public void setBatch(String paramString) {
		this.batch = paramString;
	}

	@JsonIgnore
	public void setBigstocknum(String paramString) {
		this.bigstocknum = paramString;
	}

	@JsonIgnore
	public void setGoodsid(String paramString) {
		this.goodsid = paramString;
	}

	public void setProductiondate(String paramString) {
		this.productiondate = paramString;
	}

	@JsonIgnore
	public void setStocknum(double paramDouble) {
		this.stocknum = paramDouble;
	}

	@JsonIgnore
	public void setWarehouseid(String paramString) {
		this.warehouseid = paramString;
	}

	@JsonIgnore
	public void setWarehousename(String paramString) {
		this.warehousename = paramString;
	}

	@Override
	public String toString() {
		return "RespGoodsWarehouse [batch=" + batch + ", bigstocknum=" + bigstocknum + ", goodsid=" + goodsid
				+ ", productiondate=" + productiondate + ", stocknum=" + stocknum + ", warehouseid=" + warehouseid
				+ ", warehousename=" + warehousename + "]";
	}

}