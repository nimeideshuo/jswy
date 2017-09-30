package com.ahjswy.cn.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ahjswy.cn.app.MyApplication;

import android.util.Log;

public class MLog {
	public static final String TAG = "TEST";
	static boolean isPrint = false;
	static {
		Map<String, String> devices = new HashMap<String, String>();
		devices.put("i6200S", "75673d09d9812c80");
		devices.put("BlueStacks", "f3abcf1effec8b17");
		devices.put("MX5020", "86bf794d381818a2");
		devices.put("MX5020", "86bf794d381818a2");
		devices.put("Al00", "f8945dd90ddf6393");
		isPrint = IsDevices(devices);
	}

	public MLog() {
		super();
	}

	public static void d(Object value) {
		if (isPrint) {
			Log.d("TEST", value + "");
		}

	}

	public static void d(Object o, Object value) {
		if (isPrint) {
			Log.d("" + o.getClass().getSimpleName(), "" + value);
		}
	}

	public static boolean IsDevices(Map<String, String> devices) {
		String androidId = MyApplication.getAndroidId();
		Iterator<String> iterator = devices.values().iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(androidId)) {
				return true;
			}
		}
		return false;
	}
}
