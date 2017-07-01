package com.ahjswy.cn.utils;

import android.util.Log;

public class MLog {
	public static final String TAG = "TEST";

	public MLog() {
		super();
	}

	public static void d(Object value) {
		Log.d("TEST", value + "");
	}

	public static void d(Object o, Object value) {
		Log.d("" + o.getClass().getSimpleName(), "" + value);
	}
}
