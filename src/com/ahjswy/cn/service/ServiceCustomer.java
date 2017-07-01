package com.ahjswy.cn.service;

import java.util.LinkedHashMap;

import com.ahjswy.cn.model.Customer;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.Service;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.utils.Utils_help;

public class ServiceCustomer {
	private String baseAddress = "customer";
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	public String cu_AddCustomerForSale(Customer paramCustomer, boolean paramBoolean1, boolean paramBoolean2) {
		String url = Service.getServiceAddress(this.baseAddress, "addcustomerforsale");
		map.put("parameter", JSONUtil.object2Json(paramCustomer));
		map.put("isignorsamename", paramBoolean1 + "");
		map.put("isignorsametel", paramBoolean2 + "");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String cu_GetNewCustomerID() {
		String url = Utils.getServiceAddress(this.baseAddress, "getnewcustomerid");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String cu_GetCustomer() {
		String url = Utils.getServiceAddress(this.baseAddress, "getcustomer");
		return new Utils_help().getServiceInfor(url, map);

	}
}