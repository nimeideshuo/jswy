package com.ahjswy.cn.scaner;

import android.content.Context;
import mexxen.mx5010.barcode.BarcodeConfig;
import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

public class ScanerMX5020 extends Scaner {
	BarcodeConfig bc;
	BarcodeManager bm;

	public ScanerMX5020(Context context) {
		bc = new BarcodeConfig(context);
		bm = new BarcodeManager(context);
		bm.addListener(bl);
		defaultSetting();
	}

	/**
	 * 设置输出模式
	 * 
	 * BarcodeConfig.PLAYTONE_MODE_NONE
	 * 
	 * 0 复制粘贴模式
	 * 
	 * BarcodeConfig.PLAYTONE_MODE_ONLY_SOUND
	 * 
	 * 1 模拟键盘模式
	 * 
	 * BarcodeConfig.PLAYTONE_MODE_ONLY_VIBRATE
	 * 
	 * 2 不显示模式(复制在粘贴板)
	 * 
	 * BarcodeConfig.PLAYTONE_MODE_SOUND_AND_VIBRATE
	 * 
	 * @param mode
	 */
	public void setOutputMode(int mode) {
		bc.setOutputMode(mode);
	}

	// BarcodeListener barcodeListener;
	//
	// public void setBarcodeListener(BarcodeListener barcodeListener) {
	// this.barcodeListener = barcodeListener;
	// }

	@Override
	public void defaultSetting() {
		if (bc == null) {
			return;
		}
		bc.setOutputMode(BarcodeConfig.PLAYTONE_MODE_SOUND_AND_VIBRATE);
	}

	BarcodeListener bl = new BarcodeListener() {

		@Override
		public void barcodeEvent(BarcodeEvent event) {
			if (event.getOrder().equals("SCANNER_READ")) {
				if (barcodeListener != null) {
					barcodeListener.setBarcode(bm.getBarcode().toString().trim());
				}
			}
		}
	};

	@Override
	public boolean removeListener() {
		try {
			bm.removeListener(bl);
			bm = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
