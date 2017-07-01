package com.ahjswy.cn.ui.addgoods;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.GoodsClass;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 新增商品
 * 
 * @author Administrator
 *
 */
public class AddGoodSActivity extends BaseActivity implements OnClickListener {
	private EditText et_goodsName;// 商品名称
	private EditText et_goods_barcode;// 商品条码
	private EditText ed_specification;// 规格
	private CheckBox cbDiscount;// 允许打折
	private Button btnGoodsClass;// 所属类别
	private EditText etRemarks;// 备注
	private Button btn_un1;
	private Button btn_un2;
	private Button btn_un3;
	private ServiceStore serviceStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_goods);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		et_goodsName = (EditText) findViewById(R.id.et_goodsName);
		et_goods_barcode = (EditText) findViewById(R.id.et_goods_barcode);
		ed_specification = (EditText) findViewById(R.id.ed_specification);
		cbDiscount = (CheckBox) findViewById(R.id.cbDiscount);
		btnGoodsClass = (Button) findViewById(R.id.btnGoodsClass);
		etRemarks = (EditText) findViewById(R.id.etRemarks);

		btn_un1 = (Button) findViewById(R.id.btn_un1);
		btn_un2 = (Button) findViewById(R.id.btn_un2);
		btn_un3 = (Button) findViewById(R.id.btn_un3);
		serviceStore = new ServiceStore();
	}

	private void initData() {
		cbDiscount.setChecked(true);
	}

	private void initListener() {
		btn_un1.setOnClickListener(this);
		btn_un2.setOnClickListener(this);
		btn_un3.setOnClickListener(this);
		btnGoodsClass.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "确定").setShowAsAction(1);
		return super.onCreateOptionsMenu(menu);
	}


	private void startInto() {
		// serviceStore.GetPriceSystem();
		// serviceStore.GetGoodsUnit(null);
		// String goodsClass = serviceStore.GoodsClass(0, 0);
		// JSONUtil.readValue2(goodsClass);
		// List<ResGoodsClass> listResGoodsClass =
		// JSONUtil.str2list(JSONUtil.Data, ResGoodsClass.class);
		String fillDoc = fillDoc();
		if (TextUtils.isEmpty(fillDoc)) {
			Intent intent = new Intent(this, GoodSUnitActivity.class);
			startActivity(intent);
			finish();
		} else {
			showError(fillDoc);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGoodsClass:// 商品类别
			startActivityForResult(new Intent(this, GoodsClassActivity.class), 1);
			break;
		case R.id.btn_un1:

			break;
		case R.id.btn_un2:

			break;
		case R.id.btn_un3:

			break;

		}
	}

	private String fillDoc() {
		String message = null;
		if (et_goodsName.getText().toString().isEmpty()) {
			message = "商品名称不能为空";
		}
		if (btnGoodsClass.getText().toString().isEmpty()) {
			message = "商品类别请选择";
		}
		// if (btn_un1.getText().toString().isEmpty() ||
		// btn_un2.getText().toString().isEmpty()
		// || btn_un3.getText().toString().isEmpty()) {
		// PDH.showMessage("商品名必须至少有一个计量单位");
		// return true;
		// }
		return message;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				GoodsClass goodsClass = (GoodsClass) data.getExtras().get("goodsClass");
				btnGoodsClass.setText(goodsClass.getName().toString());
				break;
			}
		}
	}
	public void setActionBarText() {
		setTitle("新增商品");
	}
}
