package com.ahjswy.cn.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DocContainerEntity implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty("deleteinitem")
	private String deleteinitem;

	@JsonProperty("deleteitem")
	private String deleteitem;

	@JsonProperty("doc")
	private String doc;

	@JsonProperty("doctype")
	private String doctype;

	@JsonProperty("info")
	private String info;

	@JsonProperty("initem")
	private String initem;

	@JsonProperty("item")
	private String item;

	@JsonProperty("paytype")
	private String paytype;

	@JsonIgnore
	public String getDeleteinitem() {
		return this.deleteinitem;
	}

	@JsonIgnore
	public String getDeleteitem() {
		return this.deleteitem;
	}

	@JsonIgnore
	public String getDoc() {
		return this.doc;
	}

	@JsonIgnore
	public String getDoctype() {
		return this.doctype;
	}

	@JsonIgnore
	public String getInfo() {
		return this.info;
	}

	@JsonIgnore
	public String getInitem() {
		return this.initem;
	}

	@JsonIgnore
	public String getItem() {
		return this.item;
	}

	@JsonIgnore
	public String getPaytype() {
		return this.paytype;
	}

	@JsonIgnore
	public void setDeleteinitem(String deleteinitem) {
		this.deleteinitem = deleteinitem;
	}

	@JsonIgnore
	public void setDeleteitem(String deleteitem) {
		this.deleteitem = deleteitem;
	}

	@JsonIgnore
	public void setDoc(String doc) {
		this.doc = doc;
	}

	@JsonIgnore
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	@JsonIgnore
	public void setInfo(String info) {
		this.info = info;
	}

	@JsonIgnore
	public void setInitem(String initem) {
		this.initem = initem;
	}

	@JsonIgnore
	public void setItem(String item) {
		this.item = item;
	}

	@JsonIgnore
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
}