package com.ahjswy.cn.popupmenu;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.ui.outgoods.OutDocEditActivity;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class OutDocEditMenuPopup extends PopupWindow implements OnClickListener {
	OnClickListener clicklistener;
	private OutDocEditActivity activity;
	private View root;
	private DefDocXS doc;
	private Button btnCheck;
	private LinearLayout btnCheckPrint;
	private LinearLayout btnCustomerHistory;
	private Button btnDocProperty;
	private LinearLayout btnDelete;
	private LinearLayout btnPay;
	private LinearLayout btnSave;
	// private LinearLayout btnPromotion;
	private LinearLayout ll_SavePrint;
	private AccountPreference ap;
	private LinearLayout ll_BlueDevicesPrint;

	public OutDocEditMenuPopup(OutDocEditActivity activity) {
		this.activity = activity;
		root = LayoutInflater.from(activity).inflate(R.layout.popup_menu_outexchangedocedit, null);
		setContentView(root);
		initView();
		doc = activity.getDoc();
		setAnimationStyle(R.style.buttom_in_out);
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		int i = localDisplayMetrics.widthPixels;
		int j = localDisplayMetrics.heightPixels;
		setWidth(i);
		if ((doc.isIsavailable()) && (doc.isIsposted())) {
			setHeight(j / 11);
			show(false);
			return;
		}
		setHeight(j / 6);
		show(true);
	}

	private void initView() {
		ap = new AccountPreference();
		btnCheck = (Button) root.findViewById(R.id.btnCheck);
		btnCheckPrint = (LinearLayout) root.findViewById(R.id.btnCheckPrint);
		btnCustomerHistory = (LinearLayout) root.findViewById(R.id.btnCustomerHistory);
		btnDocProperty = (Button) root.findViewById(R.id.btnDocProperty);
		btnDelete = (LinearLayout) root.findViewById(R.id.btnDelete);
		// btnPromotion = (LinearLayout) root.findViewById(R.id.btnPromotion);
		btnPay = (LinearLayout) root.findViewById(R.id.btnPay);
		btnSave = (LinearLayout) root.findViewById(R.id.btnSave);
		ll_SavePrint = (LinearLayout) root.findViewById(R.id.ll_SavePrint);
		ll_BlueDevicesPrint = (LinearLayout) root.findViewById(R.id.ll_BlueDevicesPrint);
		btnCheck.setOnClickListener(this);
		btnCheckPrint.setOnClickListener(this);
		btnCustomerHistory.setOnClickListener(this);
		btnDocProperty.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		ll_SavePrint.setOnClickListener(this);
		ll_BlueDevicesPrint.setOnClickListener(this);
		isShowBluetoothPrint(Boolean.parseBoolean(ap.getValue("bluetoothPrintIsShow")));
		boolean bluetoothPrintIsShow = Boolean.parseBoolean(ap.getValue("bluetoothPrintIsShow", "false"));
		if (!bluetoothPrintIsShow) {
			ll_BlueDevicesPrint.setVisibility(View.GONE);
		}
		boolean isposted = activity.doc.isIsposted();
		if (isposted) {
			TextView tv_bluePrint = (TextView) root.findViewById(R.id.tv_bluePrint);
			tv_bluePrint.setText("蓝牙打印");
			TextView tv_SavePrint = (TextView) root.findViewById(R.id.tv_SavePrint);
			tv_SavePrint.setText("打印");
			TextView tv_Pay = (TextView) root.findViewById(R.id.tv_Pay);
			tv_Pay.setText("收款状态");
		}
	}

	private void isShowBluetoothPrint(boolean isShow) {
		if (!isShow) {
			ll_BlueDevicesPrint.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		WindowManager.LayoutParams localLayoutParams = this.activity.getWindow().getAttributes();
		localLayoutParams.alpha = 1.0F;
		this.activity.getWindow().setAttributes(localLayoutParams);
		switch (v.getId()) {
		case R.id.btnCheck:
			// 过账
			activity.check(false);
			break;
		case R.id.btnCheckPrint:
			// 过账打印
			this.activity.check(true);
			break;
		case R.id.btnCustomerHistory:
			// 客史
			this.activity.getCustomerHistory();
			break;
		case R.id.btnDocProperty:
			// 属性
			this.activity.docProperty();
			break;
		case R.id.btnDelete:
			// 删除
			this.activity.delete();
			break;
		case R.id.btnPay:
			// 收款
			this.activity.pay();
			break;
		case R.id.btnSave:
			// 保存
			this.activity.save(false);
			break;
		case R.id.ll_BlueDevicesPrint:
			this.activity.blueDevicePrint();
			break;
		case R.id.ll_SavePrint:
			if (activity.doc.isIsposted()) {
				activity.printServiceDoc();
			} else {
				this.activity.save(true);
			}
			break;
		}
	}

	public void show(boolean paramBoolean) {
		if (paramBoolean) {
			doc = activity.getDoc();
		}
		if ((doc.isIsavailable()) && (doc.isIsposted())) {
			root.findViewById(R.id.linear_top).setVisibility(View.GONE);
			btnSave.setVisibility(View.GONE);
			// btnDelete.setVisibility(View.GONE);
		} else {
			root.findViewById(R.id.linear_top).setVisibility(View.VISIBLE);
			btnSave.setVisibility(View.VISIBLE);
			// btnDelete.setVisibility(View.VISIBLE);
		}
		btnDelete.setVisibility(View.GONE);
		// 促销显示
		// if (TextUtils.isEmptyS(doc.getPromotionid().toString())) {
		// // btnPromotion.setVisibility(8);
		// return;
		// }
		// btnPromotion.setVisibility(0);
	}
}
