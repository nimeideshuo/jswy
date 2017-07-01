package com.ahjswy.cn.ui.addgoods;

import com.ahjswy.cn.R;
import com.ahjswy.cn.ui.BaseActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class GoodSUnitActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_goodsunit);
		initView();
		initData();
	}

	private void initView() {
		// 第一层
		Button btn_un1 = (Button) findViewById(R.id.btn_un1);
		CheckBox cbBaseUnit1 = (CheckBox) findViewById(R.id.cbBaseUnit1);
		CheckBox cbBigUnit1 = (CheckBox) findViewById(R.id.cbBigUnit1);
		EditText et_ratio1 = (EditText) findViewById(R.id.et_ratio1);
		Button btn_Price1 = (Button) findViewById(R.id.btn_Price1);
		// 第二层
		Button btn_un2 = (Button) findViewById(R.id.btn_un2);
		CheckBox cbBaseUnit2 = (CheckBox) findViewById(R.id.cbBaseUnit2);
		CheckBox cbBigUnit2 = (CheckBox) findViewById(R.id.cbBigUnit2);
		EditText et_ratio2 = (EditText) findViewById(R.id.et_ratio2);
		Button btn_Price2 = (Button) findViewById(R.id.btn_Price2);
		//第三层
		Button btn_un3 = (Button) findViewById(R.id.btn_un3);
		CheckBox cbBaseUnit3 = (CheckBox) findViewById(R.id.cbBaseUnit3);
		CheckBox cbBigUnit3 = (CheckBox) findViewById(R.id.cbBigUnit3);
		EditText et_ratio3 = (EditText) findViewById(R.id.et_ratio3);
		Button btn_Price3 = (Button) findViewById(R.id.btn_Price3);
	}

	private void initData() {
		
	}
}
