package com.ahjswy.cn.utils;

import android.util.Log;

public class MLog {
	public static final String TAG = "TEST";
	public static boolean isPrintLog = true;

	public MLog() {
		super();
	}

	public static void d(Object value) {
		if (isPrintLog) {
			Log.d("TEST", value + "");
		}

	}

	public static void d(Object o, Object value) {
		if (isPrintLog) {
			Log.d("" + o.getClass().getSimpleName(), "" + value);
		}
	}
}
