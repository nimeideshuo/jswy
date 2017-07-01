package com.ahjswy.cn.service;

import java.util.LinkedHashMap;

import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.model.User;
import com.ahjswy.cn.request.ReqUsrCheckAuthority;
import com.ahjswy.cn.request.ReqUsrUserLocation;
import com.ahjswy.cn.request.ReqUsrUserLogin;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.Service;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.utils.Utils_help;

public class ServiceUser {
	private String baseAddress = "user";
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

	public String usr_CheckAuthority(String paramString) {
		String url = Service.getServiceAddress(this.baseAddress, "checkauthority");
		ReqUsrCheckAuthority localReqUsrCheckAuthority = new ReqUsrCheckAuthority();
		localReqUsrCheckAuthority.setUserid(SystemState.getUser().getId());
		localReqUsrCheckAuthority.setAuthority(paramString);
		map.put("parameter", JSONUtil.object2Json(localReqUsrCheckAuthority));
		return new Utils_help().getServiceInfor(url, map);
	}

	// 获取权限
	public String usr_GetAuthoritys(String paramString) {
		String url = Utils.getServiceAddress(this.baseAddress, "getauthoritys");
		ReqUsrCheckAuthority localReqUsrCheckAuthority = new ReqUsrCheckAuthority();
		localReqUsrCheckAuthority.setUserid(SystemState.getUser().getId());
		localReqUsrCheckAuthority.setAuthority(paramString);
		map.put("parameter", JSONUtil.object2Json(localReqUsrCheckAuthority));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String usr_QueryUser() {
		String url = Utils.getServiceAddress(this.baseAddress, "queryuser");
		return new Utils_help().getServiceInfor(url, map);
	}

	public String usr_UploadUserLocation(double paramDouble1, double paramDouble2, String paramString) {
		String url = Utils.getServiceAddress(this.baseAddress, "uploaduserlocation");
		User localUser = (User) SystemState.getObject("cu_user", User.class);
		ReqUsrUserLocation localReqUsrUserLocation = new ReqUsrUserLocation();
		localReqUsrUserLocation.setId(localUser.getId());
		localReqUsrUserLocation.setName(localUser.getName());
		localReqUsrUserLocation.setLongitude(paramDouble1);
		localReqUsrUserLocation.setLatitude(paramDouble2);
		localReqUsrUserLocation.setAddress(paramString);
		map.put("parameter", JSONUtil.object2Json(localReqUsrUserLocation));
		return new Utils_help().getServiceInfor(url, map);
	}

	public String usr_UserLogin(String paramString1, String paramString2) {
		String url = Utils.getServiceAddress(this.baseAddress, "userlogin");
		ReqUsrUserLogin localReqUsrUserLogin = new ReqUsrUserLogin(paramString1, paramString2, "mmendianban");
		map.put("parameter", JSONUtil.object2Json(localReqUsrUserLogin));
		return new Utils_help().getServiceInfor(url, map);
	}
}