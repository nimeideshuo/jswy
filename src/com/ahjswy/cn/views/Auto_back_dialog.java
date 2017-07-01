package com.ahjswy.cn.views;

import com.ahjswy.cn.interface_callback.Auto_Dialog_call_back;
import com.ahjswy.cn.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Auto_back_dialog extends Dialog implements android.view.View.OnClickListener {
	Auto_Dialog_call_back call_back;
	Context context;
	private Button bt_dialg_true, bt_dialg_false, bt_dialg_cancel;
	private TextView tv_message;
	private TextView tvTitle;

	public Auto_back_dialog(Context context, Auto_Dialog_call_back call_back) {
		super(context);
		this.context = context;
		this.call_back = call_back;
	}

	public Auto_back_dialog(Context context, int theme, Auto_Dialog_call_back call_back) {
		super(context, theme);
		this.context = context;
		this.call_back = call_back;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_dialog);
		initView();

	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tv_message = (TextView) findViewById(R.id.tv_message);

		bt_dialg_true = (Button) findViewById(R.id.btn_dialg_true);
		bt_dialg_false = (Button) findViewById(R.id.btn_dialg_false);
		bt_dialg_cancel = (Button) findViewById(R.id.btn_dialg_back);

		bt_dialg_true.setOnClickListener(this);
		bt_dialg_false.setOnClickListener(this);
		bt_dialg_cancel.setOnClickListener(this);
	}

	/*
	 * 设置 dialog 标题头
	 */
	public void setDialog_Title(String text) {
		tvTitle.setText(text);
	}

	/*
	 * 设置 message 消息
	 */
	public void setDialog_Message(String message) {
		tv_message.setText(message);
	}

	// 设置 dialog true 文字
	public void setDialog_True(String text) {
		bt_dialg_true.setText(text);
	}

	// 设置 dialog 否 文字
	public void setDialog_False(String text) {
		bt_dialg_false.setText(text);
	}

	// 设置 dialog cancel 取消
	public void setDialog_cancel(String text) {
		bt_dialg_cancel.setText(text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialg_true:
			call_back.auto_dia_true();
			break;
		case R.id.btn_dialg_false:
			call_back.auto_dia_false();
			break;
		case R.id.btn_dialg_back:
			dismiss();
			break;

		default:
			break;
		}
	}
}
