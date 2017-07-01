package com.ahjswy.cn.utils;

import com.ahjswy.cn.app.AccountPreference;

import android.content.Context;

public class Service {
	Context contexts;

	public Service(Context context) {
		this.contexts = context;
	}

	/*
	 * 进行接口地址 拼接
	 */
	public static String getServiceAddress(String paramString1, String paramString2) {
		return paramString1 + "/" + paramString2;
	}

	/*
	 * get 获取 IP 地址
	 */
	public static String getIpUrl(String serviceIP) {
		// http://192.168.1.181:9682/system/checkregister
		return "http://" + serviceIP + ":9682/";
	}
	private static AccountPreference ap = new AccountPreference();
	public static String getUrl() {
		return "http://" + ap.getServerIp() + ":90/";
	}

}
