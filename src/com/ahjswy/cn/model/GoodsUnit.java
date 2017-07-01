package com.ahjswy.cn.model;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class GoodsUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("goodsid")
	private String goodsid;

	@JsonProperty("isbasic")
	private boolean isbasic;

	@JsonProperty("isshow")
	private boolean isshow;

	@JsonProperty("ratio")
	private double ratio;

	@JsonProperty("unitid")
	private String unitid;

	@JsonProperty("unitname")
	private String unitname;

	public String getGoodsid() {
		return this.goodsid;
	}

	// 折扣率
	public double getRatio() {
		return this.ratio;
	}

	public String getUnitid() {
		return this.unitid;
	}

	public String getUnitname() {
		return this.unitname;
	}

	public boolean isIsbasic() {
		return this.isbasic;
	}

	public boolean isIsshow() {
		return this.isshow;
	}

	public void setGoodsid(String paramString) {
		this.goodsid = paramString;
	}

	public void setIsbasic(boolean paramBoolean) {
		this.isbasic = paramBoolean;
	}

	public void setIsshow(boolean paramBoolean) {
		this.isshow = paramBoolean;
	}

	public void setRatio(double paramDouble) {
		this.ratio = paramDouble;
	}

	public void setUnitid(String paramString) {
		this.unitid = paramString;
	}

	public void setUnitname(String paramString) {
		this.unitname = paramString;
	}

	@Override
	public String toString() {
		return "GoodsUnit [goodsid=" + goodsid + ", isbasic=" + isbasic + ", isshow=" + isshow + ", ratio=" + ratio
				+ ", unitid=" + unitid + ", unitname=" + unitname + "]";
	}
	
}