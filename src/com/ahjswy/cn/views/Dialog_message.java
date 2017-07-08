package com.ahjswy.cn.views;

import com.ahjswy.cn.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dialog_message extends Dialog {

	// Dialog_ture_false_CallBack message_callBack;
	Context context;
	private TextView tv_message;
	private TextView tv_title_message;
	private Button btn_dialg_money_true;
	private Button btn_dialg_money_back;

	public Dialog_message(Context context) {
		super(context, R.style.MyDialog_NoTitle);
		this.context = context;
		setContentView(R.layout.dialog_pay_money);
		initView();
	}

	// /*
	// * 自定义主题 走次方法
	// */
	// public Dialog_message_true_false(Context context) {
	// super(context, R.style.MyDialog_NoTitle);
	// this.context = context;
	// }

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.dialog_pay_money);
	// initView();
	//
	// }

	private void initView() {
		tv_message = (TextView) findViewById(R.id.tv_message);
		btn_dialg_money_true = (Button) findViewById(R.id.btn_dialg_money_true);
		btn_dialg_money_back = (Button) findViewById(R.id.btn_dialg_money_back);
		tv_title_message = (TextView) findViewById(R.id.tv_title_message);
		// 设置监听
		btn_dialg_money_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	// 设置 dialog 标题
	public void setTitle(String text) {
		tv_title_message.setText(text);
	}

	// 设置 dialog 消息
	public void setMessage(String text) {
		tv_message.setText(text);
	}

	// 设置 dialog true
	public void setTextTrue(String text) {
		btn_dialg_money_true.setText(text);
	}

	// 设置 dialog false
	public void setTextFalse(String text) {
		btn_dialg_money_back.setText(text);
	}

	/**
	 * 确认
	 * 
	 * @param onClickListener
	 */
	public void setCancelListener(View.OnClickListener onClickListener) {
		btn_dialg_money_true.setOnClickListener(onClickListener);
	}

	// 取消监听
	public void setComfirmListener(View.OnClickListener onClickListener) {
		btn_dialg_money_back.setOnClickListener(onClickListener);
	}

}
