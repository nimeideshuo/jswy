package com.ahjswy.cn.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.UnitDAO;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.utils.MLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewGoodSAct extends BaseActivity implements OnClickListener {
	private LinearLayout unitRoot;
	private Button btnAddUnit;

	private RadioButton rbBaseUnit1;
	private RadioButton rbBigUnit1;
	private RadioButton rbBaseUnit2;
	private RadioButton rbBigUnit2;

	private LinearLayout linUnit2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_addnewgoodst);
		initView();
	}

	private void initView() {
		EditText etName = (EditText) findViewById(R.id.etName);
		EditText etBarcode = (EditText) findViewById(R.id.etBarcode);
		EditText etSpecification = (EditText) findViewById(R.id.etSpecification);
		EditText etModel = (EditText) findViewById(R.id.etModel);
		cbIsDiscount = (CheckBox) findViewById(R.id.cbIsDiscount);
		cbIsusebatch = (CheckBox) findViewById(R.id.cbIsusebatch);

		// EditText etBatchPrice = (EditText) findViewById(R.id.etBatchPrice);
		// EditText etSalePrice = (EditText) findViewById(R.id.etSalePrice);
		// EditText etAveragePrice = (EditText)
		// findViewById(R.id.etAveragePrice);
		// EditText etSalecue = (EditText) findViewById(R.id.etSalecue);
		// EditText etRemark = (EditText) findViewById(R.id.etRemark);

		Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		btnAddUnit = (Button) findViewById(R.id.btnAddUnit);
		unitRoot = (LinearLayout) findViewById(R.id.unitRoot);
		linUnit1 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
		unitRoot.addView(linUnit1);
		btnAddUnit.setOnClickListener(this);
		rbBaseUnit1 = (RadioButton) linUnit1.findViewById(R.id.rbBaseUnit);
		rbBigUnit1 = (RadioButton) linUnit1.findViewById(R.id.rbBigUnit);
		EditText ratio1 = (EditText) linUnit1.findViewById(R.id.ratio);
		Spinner spUnit = (Spinner) linUnit1.findViewById(R.id.spUnit);
		cbIsDiscount.setChecked(true);
		rbBaseUnit1.setOnClickListener(unitListener1);
		rbBigUnit1.setOnClickListener(unitListener1);
		rbBaseUnit1.setOnCheckedChangeListener(checkedlistener);
		rbBigUnit1.setOnCheckedChangeListener(checkedlistener);
		rbBaseUnit1.setChecked(true);
		rbBigUnit1.setEnabled(false);
		ratio1.setText(String.valueOf(1.0));
		ratio1.setEnabled(false);
		// 单位查询展示
		// =========单位示例 展示
		List<GoodsUnit> listUnit = new UnitDAO().queryAll();
		HashSet<String> haunit = new HashSet<String>();
		for (int i = 0; i < listUnit.size(); i++) {
			haunit.add(listUnit.get(i).getUnitname());
		}
		liUnit = new ArrayList<String>();
		Iterator<String> iterator = haunit.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			liUnit.add(string);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liUnit);
		spUnit.setAdapter(adapter);
	}

	private List<String> liUnit;
	private CheckBox cbIsDiscount;
	private CheckBox cbIsusebatch;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddUnit:
			if (linUnit2 == null) {
				linUnit2 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
				unitRoot.addView(linUnit2);
				rgRoot2 = (RadioGroup) linUnit2.findViewById(R.id.rgRoot);
				rbBaseUnit2 = (RadioButton) linUnit2.findViewById(R.id.rbBaseUnit);
				rbBigUnit2 = (RadioButton) linUnit2.findViewById(R.id.rbBigUnit);
				Spinner spUnit2 = (Spinner) linUnit2.findViewById(R.id.spUnit);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
						liUnit);
				spUnit2.setAdapter(adapter2);
				rbBaseUnit2.setOnClickListener(unitListener2);
				rbBigUnit2.setOnClickListener(unitListener2);
				rbBaseUnit2.setOnCheckedChangeListener(checkedlistener);
				rbBigUnit2.setOnCheckedChangeListener(checkedlistener);
				rbBigUnit1.setEnabled(true);
			}
			break;
		case R.id.btnSubmit:
			submit();
			Toast.makeText(this, "提交 ", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	private void submit() {
		MLog.d("基本单位1:" + rbBaseUnit1.isChecked() + " 计件单位1:" + rbBigUnit1.isChecked());
		MLog.d("基本单位2:" + rbBaseUnit2.isChecked() + " 计件单位2:" + rbBigUnit2.isChecked());
		TranslateAnimation animation = new TranslateAnimation(0f, -5f, 0, 0);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(100);
		animation.setRepeatCount(3);
		animation.setRepeatMode(Animation.REVERSE);
		if (rbBaseUnit2 == null) {
			if (!rbBaseUnit1.isChecked()) {
				showError("基本单位必须选择!");
				return;
			}
			return;
		}
		if (!(rbBaseUnit1.isChecked() || rbBaseUnit2.isChecked())) {
			showError("请选择基本单位!");
			return;
		}
		if ((rbBaseUnit1.isChecked() && rbBaseUnit2.isChecked())) {
			showError("只能有一个基本单位!");
			return;
		}
		if (!(rbBaseUnit2.isChecked() && rbBigUnit2.isChecked())) {
			showError("单位请选择!");
			rgRoot2.startAnimation(animation);
			return;
		}
		// if (!(rbBaseUnit1.isChecked()&& rbBaseUnit2.isChecked())) {
		// showSuccess("必须有一个基本单位!");
		// return;
		// }
	}

	OnClickListener unitListener1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rbBaseUnit:
				Toast.makeText(AddNewGoodSAct.this, "rbBaseUnit", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rbBigUnit:
				Toast.makeText(AddNewGoodSAct.this, "rbBigUnit", Toast.LENGTH_SHORT).show();

				break;
			}
		}
	};
	View.OnClickListener unitListener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rbBaseUnit:
				Toast.makeText(AddNewGoodSAct.this, "rbBaseUnit2", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rbBigUnit:
				Toast.makeText(AddNewGoodSAct.this, "rbBigUnit2", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	OnCheckedChangeListener checkedlistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		}
	};
	private LinearLayout linUnit1;
	private RadioGroup rgRoot2;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:
				// 商品类别检索 返回 TODO
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void setActionBarText() {
		setTitle("新增商品");
	}

}
