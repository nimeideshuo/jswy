package com.ahjswy.cn.scaner;

import android.content.Context;

public class Scaner {

	public Scaner() {
	}

	public static Scaner factory(Context context) {
		String model = android.os.Build.MODEL;
		switch (model) {
		case "MX-5020":
			return new ScanerMX5020(context);
		case "Data Terminal":
			return new ScanerMX5060(context);
		case "i6200S":
			return new ScanerI6200S(context);
		default:
			break;
		}
		return new DefaultScan();
	}

	/**
	 * 删除监听
	 */
	public boolean removeListener() {
		return false;
	}

	public void defaultSetting() {

	}

	ScanerBarcodeListener barcodeListener;

	public void setBarcodeListener(ScanerBarcodeListener barcodeListener) {
		this.barcodeListener = barcodeListener;
	}

	public interface ScanerBarcodeListener {
		public void setBarcode(String barcode);
	}

}
