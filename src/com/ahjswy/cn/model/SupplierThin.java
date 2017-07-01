package com.ahjswy.cn.model;

import java.io.Serializable;

public class SupplierThin implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String pinyin;

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
}
