package com.ahjswy.cn.app;

import com.ahjswy.cn.service.ServiceSystem;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;

public class RequestHelper {
	public static RequestHelper helper;

	public static RequestHelper get() {
		try {
			if (helper == null)
				helper = new RequestHelper();
			RequestHelper localRequestHelper = helper;
			return localRequestHelper;
		} finally {
		}
	}

	public static boolean isSuccess(String str) {
		return (A.isFail(str));
	}

	public static void showError(String str) {
		if ("无网络连接".equals(str)) {
			PDH.showError("无网络连接");
			return;
		}
		if ("nodog".equals(str)) {
			PDH.showError("加密狗不存在，程序即将退出");
			new Thread() {
				public void run() {
					try {
						Thread.sleep(2000L);
						MyApplication.getInstance().exit();
						return;
					} catch (InterruptedException localInterruptedException) {
						localInterruptedException.printStackTrace();
					}
				}
			}.start();
			return;
		}
		if ("forbid".equals(str)) {
			PDH.showFail("你的权限不足!");
			return;
		}
		if ("fail".equals(str)) {
			PDH.showFail("操作失败");
			return;
		}
		if ("login".equals(str)) {
			PDH.showMessage("请登录");
			return;
		}
		if (str.startsWith("<htm")) {
			PDH.showFail("请求失败, 服务器异常.");
			return;
		}
		PDH.showFail("操作失败，" + str);
	}

	// 验证 IP 是否正确
	public String checkConnect(String ipStr) {
		String str;
		if (TextUtils.isEmpty(ipStr)) {
			str = "请输入IP地址";
			return str;
		}
		str = new ServiceSystem().sys_CheckRegister(ipStr);
		if (str.equals("register")) {
			return "register";
		}
		if (str.equals("success")) {
			return "success";
		}
		return "链接超时";
	}
}