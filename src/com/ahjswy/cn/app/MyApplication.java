package com.ahjswy.cn.app;

import com.ahjswy.cn.crash.CrashHandler;
import com.ahjswy.cn.ui.Swy_splash;
import com.ahjswy.cn.utils.TextUtils;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.provider.Settings;
import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
	private static MyApplication instance;
	private static ActivityManager activityManager;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		activityManager = ActivityManager.getScreenManager();
		CrashHandler.getInstance().init(this);
		Bmob.initialize(this, "11482f9fcef5806efdf3f4a3f908048d");

	}

	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	public void exit() {
		activityManager.popAllActivityExceptOne(null);
		Process.killProcess(Process.myPid());
		System.exit(0);
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	public String getUniqueCode() {
		if ((TextUtils.isEmptyS(SystemCommons.UNIQUE_CODE))) {
			return SystemCommons.UNIQUE_CODE;
		}
		String str1 = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		CTelephoneInfo localCTelephoneInfo = CTelephoneInfo.getInstance(this);
		localCTelephoneInfo.setCTelephoneInfo();
		String str2 = localCTelephoneInfo.getImeiSIM1();
		String str3 = localCTelephoneInfo.getImeiSIM2();
		if (!TextUtils.isEmptyS(str2)) {
			str2 = "";
		}
		if (!TextUtils.isEmptyS(str3)) {
			str3 = "";
		}
		if ((TextUtils.isEmptyS(str1 + str2 + str3))) {
			// 将所有字母 全部转换成大写 toUpperCase
			SystemCommons.UNIQUE_CODE = (str1 + str2 + str3).toUpperCase();
			return (str1 + str2 + str3).toUpperCase();
		}
		WifiInfo localWifiInfo = ((WifiManager) getSystemService("wifi")).getConnectionInfo();
		SystemCommons.UNIQUE_CODE = localWifiInfo.getMacAddress();
		return localWifiInfo.getMacAddress();
	}

	// Android Id
	public String getAndroidId() {
		return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	/**
	 * 是不是测试版本
	 * 
	 * @return
	 */
	// public boolean isTestSWY() {
	// return true;
	// }

	// 得到本机Mac地址
	public String getMac() {
		// 获取wifi管理器
		WifiManager wifiMng = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfor = wifiMng.getConnectionInfo();
		return wifiInfor.getMacAddress();
	}

	public void main() {
		Intent localIntent = new Intent(this, Swy_splash.class);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(localIntent);
	}

	public void splash() {
		activityManager.popAllActivityExceptOne(null);
		main();
	}

	/**
	 * 获取 版本名称
	 * 
	 * @return
	 */
	public String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	// /**
	// * 初始化
	// */
	// public void initDefault() {
	// if (isTestSWY()) {
	// Department depar = new Department();
	// depar.setDid("01");
	// depar.setDname("门店");
	// depar.setWarehouseid("01");
	// depar.setWarehousename("仓库");
	// SystemState.saveObject("department", depar);
	// copyData();
	// }
	// }
	//
	// public void copyData() {
	// String dbPath = Environment.getDataDirectory() +
	// "/data/com.ahjswy.cn/databases/fields.db";
	// String shared_prefsPath = Environment.getDataDirectory() +
	// "/data/com.ahjswy.cn/shared_prefs";
	//
	// File file = new File(dbPath);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// try {
	// InputStream dbis =
	// MyApplication.getInstance().getAssets().open("fields.db");
	// copyFile(dbis, dbPath);
	// InputStream basicis =
	// MyApplication.getInstance().getAssets().open("basic_setting.xml");
	// copyFile(basicis, shared_prefsPath + "/basic_setting.xml");
	// InputStream bizparameteris =
	// MyApplication.getInstance().getAssets().open("bizparameter_info.xml");
	// copyFile(bizparameteris, shared_prefsPath + "/bizparameter_info.xml");
	// InputStream se_dois =
	// MyApplication.getInstance().getAssets().open("se_do.xml");
	// copyFile(se_dois, shared_prefsPath + "/se_do.xml");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void copyFile(InputStream input, String topath) {
	// FileOutputStream os = null;
	// try {
	// os = new FileOutputStream(new File(topath));
	// int byteCount = 0;
	// byte[] buffer = new byte[1024];
	// while ((byteCount = input.read(buffer)) != -1) {
	// os.write(buffer, 0, byteCount);
	// }
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (os != null) {
	// try {
	// os.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (input != null) {
	// try {
	// input.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	//
	// }

}
