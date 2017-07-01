package com.ahjswy.cn.ui;

import com.ahjswy.cn.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	public LoadingDialog(Activity activity) {
		super(activity, R.style.Transparent);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dia_loading);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}

	public void setText(String paramString) {
		((TextView) findViewById(R.id.textView)).setText(paramString);
	}

	public void show(String paramString) {
		super.show();
		((TextView) findViewById(R.id.textView)).setText(paramString);
		((AnimationDrawable) findViewById(R.id.imageView).getBackground()).stop();
		((AnimationDrawable) findViewById(R.id.imageView).getBackground()).start();
	}
}