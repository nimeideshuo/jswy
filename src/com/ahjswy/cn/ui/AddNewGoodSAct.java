package com.ahjswy.cn.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.R;
import com.ahjswy.cn.cldb.CL_sz_pricesystem;
import com.ahjswy.cn.cldb.bean.sz_goodsclass;
import com.ahjswy.cn.cldb.bean.sz_pricesystem;
import com.ahjswy.cn.dao.UnitDAO;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.utils.MLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewGoodSAct extends BaseActivity implements OnClickListener, ScanerBarcodeListener {
	private LinearLayout unitRoot;
	private Button btnAddUnit;

	private CheckBox cbBaseUnit1;
	private CheckBox cbBigUnit1;
	private CheckBox cbBaseUnit2;
	private CheckBox cbBigUnit2;
	private LinearLayout linUnit2;
	private List<String> liUnit;
	private CheckBox cbIsDiscount;
	private CheckBox cbIsusebatch;
	LinearLayout linUnit3;
	private LinearLayout linUnit1;
	private Button btnGoodsClass;
	private EditText etBarcode;
	private ListView lvPrices;
	private List<sz_pricesystem> listPrice;
	private ScrollView svRoot;
	private PriceAdapter priceAdapter;
	private EditText etName;
	private EditText etSpecification;
	private EditText etModel;
	private EditText etSalecue;
	private EditText etRemark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_addnewgoodst);
		initView();
		initUnit1();
		Scaner factory = Scaner.factory(this);
		factory.setBarcodeListener(this);
	}

	private void initView() {
		etName = (EditText) findViewById(R.id.etName);
		etBarcode = (EditText) findViewById(R.id.etBarcode);
		etSpecification = (EditText) findViewById(R.id.etSpecification);
		etModel = (EditText) findViewById(R.id.etModel);
		cbIsDiscount = (CheckBox) findViewById(R.id.cbIsDiscount);
		cbIsusebatch = (CheckBox) findViewById(R.id.cbIsusebatch);
		etSalecue = (EditText) findViewById(R.id.etSalecue);
		etRemark = (EditText) findViewById(R.id.etRemark);
		btnGoodsClass = (Button) findViewById(R.id.btn_goodsClass);
		btnGoodsClass.setOnClickListener(this);
		Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnAddUnit = (Button) findViewById(R.id.btnAddUnit);
		unitRoot = (LinearLayout) findViewById(R.id.unitRoot);
		svRoot = (ScrollView) findViewById(R.id.svRoot);
		lvPrices = (ListView) findViewById(R.id.lvPrices);
		btnAddUnit.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}

	private void initUnit1() {
		linUnit1 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
		unitRoot.addView(linUnit1);
		cbBaseUnit1 = (CheckBox) linUnit1.findViewById(R.id.cbBaseUnit);
		cbBigUnit1 = (CheckBox) linUnit1.findViewById(R.id.cbBigUnit);
		EditText ratio1 = (EditText) linUnit1.findViewById(R.id.ratio);
		Spinner spUnit = (Spinner) linUnit1.findViewById(R.id.spUnit);
		cbIsDiscount.setChecked(true);
		cbBaseUnit1.setChecked(true);
		cbBigUnit1.setChecked(true);
		ratio1.setText(String.valueOf(1.0));
		ratio1.setEnabled(false);

		priceAdapter = new PriceAdapter();
		CL_sz_pricesystem pricesystem = new CL_sz_pricesystem();
		listPrice = pricesystem.queryAll();
		for (sz_pricesystem sz : listPrice) {
			MLog.d(sz.getPsname());
		}
		sz_pricesystem sz5 = listPrice.get(5);
		sz_pricesystem sz6 = listPrice.get(6);
		sz_pricesystem sz7 = listPrice.get(7);
		sz_pricesystem sz9 = listPrice.get(9);
		listPrice.remove(sz5);
		listPrice.remove(sz6);
		listPrice.remove(sz7);
		listPrice.remove(sz9);

		lvPrices.setAdapter(priceAdapter);
		// 请求 ScrollView 不要拦截 滑动事件
		// listView1.setOnTouchListener(onTouchListener);
		// 单位查询展示
		// =========单位示例 展示
		initUnit(spUnit);
		setHeight(lvPrices, priceAdapter);
		svRoot.setOnTouchListener(svTouchListener);
	}

	// 重新绘制 item高度
	public void setHeight(ListView listView, Adapter adapter) {
		int height = 0;
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View temp = adapter.getView(i, null, listView);
			temp.measure(0, 0);
			height += temp.getMeasuredHeight();
		}
		LayoutParams params = (LayoutParams) listView.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = height;
		listView.setLayoutParams(params);
	}

	private void initUnit(Spinner spUnit) {
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

	// ScrollView 触摸事件处理
	View.OnTouchListener svTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (lvPrices != null) {// 获取子View 清除焦点
				int childCount = lvPrices.getChildCount();
				for (int i = 0; i < childCount; i++) {
					lvPrices.getChildAt(i).clearFocus();
				}
			}
			return false;
		}
	};
	private CheckBox cbBaseUnit3;
	private CheckBox cbBigUnit3;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goodsClass:
			startActivityForResult(new Intent(this, SearchGoodsClassAct.class), 1);
			break;
		case R.id.btnAddUnit:
			// TODO 二级单位
			if (linUnit2 == null) {
				linUnit2 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
				unitRoot.addView(linUnit2);
				cbBaseUnit2 = (CheckBox) linUnit2.findViewById(R.id.cbBaseUnit);
				cbBigUnit2 = (CheckBox) linUnit2.findViewById(R.id.cbBigUnit);
				Spinner spUnit2 = (Spinner) linUnit2.findViewById(R.id.spUnit);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddNewGoodSAct.this,
						android.R.layout.simple_list_item_1, liUnit);
				spUnit2.setAdapter(adapter2);
				return;
			}
			// TODO 三级单位
			if (linUnit3 == null) {
				linUnit3 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
				unitRoot.addView(linUnit3);
				cbBaseUnit3 = (CheckBox) linUnit3.findViewById(R.id.cbBaseUnit);
				cbBigUnit3 = (CheckBox) linUnit3.findViewById(R.id.cbBigUnit);
				Spinner spUnit3 = (Spinner) linUnit3.findViewById(R.id.spUnit);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddNewGoodSAct.this,
						android.R.layout.simple_list_item_1, liUnit);
				spUnit3.setAdapter(adapter2);
				btnAddUnit.setVisibility(View.GONE);
				return;
			}
			break;
		case R.id.btnSubmit:
			submit();
			break;

		default:
			break;
		}
	}

	private void submit() {
		List<Test> listBase = new ArrayList<Test>();
		List<Test> listBig = new ArrayList<Test>();
		int count = 0;
		listBase.add(new Test("01", null, cbBaseUnit1.isChecked()));
		listBig.add(new Test("02", null, cbBigUnit1.isChecked()));
		if (linUnit2 != null) {
			listBase.add(new Test("11", null, cbBaseUnit2.isChecked()));
			listBig.add(new Test("12", null, cbBigUnit2.isChecked()));
		}
		if (linUnit3 != null) {
			listBase.add(new Test("21", null, cbBaseUnit3.isChecked()));
			listBig.add(new Test("22", null, cbBigUnit3.isChecked()));
		}
		if (listBase.size() == 0) {
			showError("必须选择一个基本单位");
			return;
		}
		for (int i = 0; i < listBase.size(); i++) {
			if (listBase.get(i).isCheck == false && listBig.get(i).isCheck == false) {
				showError("单位没有选择!");
				return;
			}
			if (listBase.get(i).isCheck) {
				count++;
			}
		}
		if (count > 1) {
			showError("只能有一个基本单位");
			return;
		}
		if (listBig.size() == 0) {
			showError("必须选择一个计件单位");
			return;
		}
		count = 0;
		for (int i = 0; i < listBig.size(); i++) {
			if (listBig.get(i).isCheck) {
				count++;
			}
		}
		if (count > 1) {
			showError("只能有一个计件单位");
			return;
		}
		Toast.makeText(this, "添加成功!", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, SwyMain.class));
		finish();
	}

	// OnClickListener unitListener1 = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.rbBaseUnit:
	// Toast.makeText(AddNewGoodSAct.this, "rbBaseUnit",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.rbBigUnit:
	// Toast.makeText(AddNewGoodSAct.this, "rbBigUnit",
	// Toast.LENGTH_SHORT).show();
	//
	// break;
	// }
	// }
	// };
	// View.OnClickListener unitListener2 = new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.rbBaseUnit:
	// Toast.makeText(AddNewGoodSAct.this, "rbBaseUnit2",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.rbBigUnit:
	// Toast.makeText(AddNewGoodSAct.this, "rbBigUnit2",
	// Toast.LENGTH_SHORT).show();
	// break;
	// }
	// }
	// };

	// OnCheckedChangeListener checkedlistener = new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView, boolean
	// isChecked) {
	// }
	// };
	public class Test {
		String id;
		String name;
		boolean isCheck;

		public Test(String id, String name, boolean isCheck) {
			super();
			this.id = id;
			this.name = name;
			this.isCheck = isCheck;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:
				// 商品类别检索 返回
				sz_goodsclass goodsClass = (sz_goodsclass) data.getSerializableExtra("goodclass");
				btnGoodsClass.setText(goodsClass.getName());
				btnGoodsClass.setTag(goodsClass);
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void setBarcode(String barcode) {
		etBarcode.setText(barcode);
	}

	public class PriceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listPrice.size();
		}

		@Override
		public sz_pricesystem getItem(int position) {
			return listPrice.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		int selectPosition;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = View.inflate(AddNewGoodSAct.this, R.layout.item_addgoods, null);
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			EditText edNumber = (EditText) view.findViewById(R.id.edNumber);
			String psname = listPrice.get(position).getPsname();
			tvName.setText(psname);
			return view;
		}

	}

	@Override
	public void setActionBarText() {
		setTitle("新增商品");
	}
}
