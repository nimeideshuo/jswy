package com.ahjswy.cn.bean.bmob;

import java.io.Serializable;

import com.ahjswy.cn.app.MyApplication;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.utils.Utils;

import cn.bmob.v3.BmobObject;

/**
 * 云 用户类
 * 
 * @author Administrator
 *
 */
public class bo_swy_user extends BmobObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String userid;
	public String userName;
	public String versionname;
	public String deviceid;
	public String registerDate;
	public String code;
	public String database;
	public String accountset;
	public int sleep;
	public int state;// 0 默认 1, 警告，2 停止,3 退出
	public String message;
	public String memory;
	

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	private bo_swy_user() {
	}

	public static bo_swy_user factory() {
		bo_swy_user swyUser = new bo_swy_user();
//		swyUser.accountset = SystemState.getAccountSet().getDatabase();
//		swyUser.deviceid = MyApplication.getInstance().getAndroidId();
//		swyUser.userName = SystemState.getUser().getName();
//		swyUser.code = MyApplication.getInstance().getUniqueCode();
//		swyUser.versionname = MyApplication.getInstance().getVersionName();
//		swyUser.userid = SystemState.getDBUser().userid;
//		swyUser.registerDate = Utils.getData();
//		swyUser.message = "";
//		swyUser.state = 0;
//		swyUser.memory = "20";
//		swyUser.database = SystemState.getAccountSet().getDatabase();
//		swyUser.registerDate = Utils.getData();
		
		
		swyUser.accountset = "swy";
		swyUser.deviceid = MyApplication.getInstance().getAndroidId();
		swyUser.userName = "xiaowang";
		swyUser.code = MyApplication.getInstance().getUniqueCode();
		swyUser.versionname = MyApplication.getInstance().getVersionName();
		swyUser.userid = "001";
		swyUser.registerDate = Utils.getData();
		swyUser.message = "message";
//		swyUser.state = 0;
//		swyUser.sleep = 0;
		swyUser.memory = "5";
		swyUser.database = "shujuku";
		swyUser.registerDate = Utils.getData();
		return swyUser;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getAccountset() {
		return accountset;
	}

	public void setAccountset(String accountset) {
		this.accountset = accountset;
	}

	public Integer getSleep() {
		return sleep;
	}

	public void setSleep(Integer sleep) {
		this.sleep = sleep;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "bo_swy_user [userid=" + userid + ", userName=" + userName + ", versionname=" + versionname
				+ ", deviceid=" + deviceid + ", registerDate=" + registerDate + ", code=" + code + ", database="
				+ database + ", accountset=" + accountset + ", sleep=" + sleep + ", state=" + state + ", message="
				+ message + "]";
	}
}
