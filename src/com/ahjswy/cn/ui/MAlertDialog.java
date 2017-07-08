package com.ahjswy.cn.ui;

import com.ahjswy.cn.R;
import com.ahjswy.cn.utils.TextUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MAlertDialog extends Dialog {
	private Button btn_dialg_true;
	private Button btn_dialg_false;
	private Button btn_dialg_back;
	private TextView tv_message;
	private TextView tvTitle;

	/*
	 * 自定义主题 走次方法
	 */
	public MAlertDialog(Context context) {
		super(context, R.style.MyDialog_NoTitle);
		intView();
	}

	private void intView() {
		setContentView(R.layout.title_dialog);
		btn_dialg_true = (Button) findViewById(R.id.btn_dialg_true);
		btn_dialg_false = (Button) findViewById(R.id.btn_dialg_false);
		btn_dialg_back = (Button) findViewById(R.id.btn_dialg_back);
		tv_message = (TextView) findViewById(R.id.tv_message);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		btn_dialg_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setMessage(String message) {
		tv_message.setText(message);
	}

	// 确认监听
	public void setCancelListener(View.OnClickListener onClickListener) {
		btn_dialg_true.setOnClickListener(onClickListener);
	}

	public void setCancelListener(String buttonStr, View.OnClickListener onClickListener) {
		btn_dialg_true.setOnClickListener(onClickListener);
		if (!TextUtils.isEmpty(buttonStr)) {
			btn_dialg_true.setText(buttonStr);
		}
	}

	public void setCancelListener(String cancel) {
		btn_dialg_true.setText(cancel);
	}

	// 取消监听
	public void setComfirmListener(View.OnClickListener paramOnClickListener) {
		btn_dialg_false.setOnClickListener(paramOnClickListener);
	}

	// 取消监听
	public void setComfirmListener(String comfirm) {
		btn_dialg_false.setText(comfirm);
	}

	public void setContentText(String tvTitles) {
		tvTitle.setText(tvTitles);
	}

	public void simpleShow(String tvTitles) {
		setContentText(tvTitles);
		show();
	}
}
