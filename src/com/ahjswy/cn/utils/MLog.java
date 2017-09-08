package com.ahjswy.cn.utils;

import com.ahjswy.cn.app.MyApplication;

import android.util.Log;

public class MLog {
	public static final String TAG = "TEST";
	public static final String[] devices = { "c7df977a039464db", "f3abcf1effec8b17" };

	public MLog() {
		super();
	}

	public static void d(Object value) {
		if (IsDevices()) {
			Log.d("TEST", value + "");
		}

	}

	public static void d(Object o, Object value) {
		if (IsDevices()) {
			Log.d("" + o.getClass().getSimpleName(), "" + value);
		}
	}

	public static boolean IsDevices() {
		String androidId = MyApplication.getAndroidId();
		for (int i = 0; i < devices.length; i++) {
			if (devices[i].equals(androidId)) {
				return true;
			}
		}
		return false;
	}
}
