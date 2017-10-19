package com.ahjswy.cn.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class DocContainerEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deleteinitem;

	private String deleteitem;

	private String doc;

	private String doctype;

	private String info;

	private String initem;

	private String item;

	private String paytype;
	private int svid;

	public int getSvid() {
		return svid;
	}

	public void setSvid(int svid) {
		this.svid = svid;
	}

	public String getDeleteinitem() {
		return this.deleteinitem;
	}

	public String getDeleteitem() {
		return this.deleteitem;
	}

	public String getDoc() {
		return this.doc;
	}

	public String getDoctype() {
		return this.doctype;
	}

	public String getInfo() {
		return this.info;
	}

	public String getInitem() {
		return this.initem;
	}

	public String getItem() {
		return this.item;
	}

	public String getPaytype() {
		return this.paytype;
	}

	public void setDeleteinitem(String deleteinitem) {
		this.deleteinitem = deleteinitem;
	}

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

	public String showid;

	@JsonIgnore
	public String getShowid() {
		return showid;
	}

	@JsonIgnore
	public void setShowid(String showid) {
		this.showid = showid;
	}
}