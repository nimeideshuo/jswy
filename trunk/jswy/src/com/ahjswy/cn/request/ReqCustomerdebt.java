package com.ahjswy.cn.request;

import java.io.Serializable;

public class ReqCustomerdebt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String customerid;
	public boolean iscustomer;
	public double amount;

	public ReqCustomerdebt() {
		super();
	}

	public ReqCustomerdebt(String customerid, boolean iscustomer, double amount) {
		super();
		this.customerid = customerid;
		this.iscustomer = iscustomer;
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public boolean isIscustomer() {
		return iscustomer;
	}

	public void setIscustomer(boolean iscustomer) {
		this.iscustomer = iscustomer;
	}

	@Override
	public String toString() {
		return "ReqCustomerdebt [customerid=" + customerid + ", iscustomer=" + iscustomer + ", amount=" + amount + "]";
	}

}
