package com.ahjswy.cn.ui;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.PricesystemDAO;
import com.ahjswy.cn.dao.UnitDAO;
import com.ahjswy.cn.model.Good;
import com.ahjswy.cn.model.Goods;
import com.ahjswy.cn.model.GoodsClass;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Pricesystem;
import com.ahjswy.cn.model.Unit;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

/**
 * 增加 商品
 * 
 * @author Administrator
 *
 */
public class AddNewGoodSAct extends BaseActivity implements OnClickListener, ScanerBarcodeListener {
	private LinearLayout unitRoot;
	private Button btnAddUnit;

	private CheckBox cbBaseUnit1;
	private CheckBox cbBigUnit1;
	private CheckBox cbBaseUnit2;
	private CheckBox cbBigUnit2;
	private LinearLayout linUnit2;
	// private List<String> liUnit;
	private CheckBox cbIsDiscount;
	private CheckBox cbIsusebatch;
	LinearLayout linUnit3;
	private LinearLayout linUnit1;
	private Button btnGoodsClass;

	private ListView lvPrices;
	private List<Pricesystem> listPrice;
	private ScrollView svRoot;
	private PriceAdapter priceAdapter;
	private EditText etName;
	private EditText etBarcode;
	private EditText etSpecification;
	private EditText etModel;
	private EditText etSalecue;
	private EditText etRemark;
	Good good;// 商品类

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
		dao = new PricesystemDAO();
		listGoodUnit = new ArrayList<GoodsUnit>();
		unit1 = new GoodsUnit();
	}

	private void initUnit1() {
		linUnit1 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
		unitRoot.addView(linUnit1);
		cbBaseUnit1 = (CheckBox) linUnit1.findViewById(R.id.cbBaseUnit);
		cbBigUnit1 = (CheckBox) linUnit1.findViewById(R.id.cbBigUnit);
		ratio1 = (EditText) linUnit1.findViewById(R.id.ratio);
		spUnit = (Spinner) linUnit1.findViewById(R.id.spUnit);
		cbIsDiscount.setChecked(true);
		cbBaseUnit1.setChecked(true);
		cbBigUnit1.setChecked(true);
		ratio1.setText(String.valueOf(1.0));
		ratio1.setEnabled(false);

		priceAdapter = new PriceAdapter();
		listPrice = dao.queryAll();
		lvPrices.setAdapter(priceAdapter);
		// 请求 ScrollView 不要拦截 滑动事件
		// listView1.setOnTouchListener(onTouchListener);
		// 单位查询展示
		// =========单位示例 展示
		// initUnit(spUnit);
		setHeight(lvPrices, priceAdapter);
		svRoot.setOnTouchListener(svTouchListener);
		listUnit = new UnitDAO().queryAll();
		arrayUnits = new String[listUnit.size()];
		for (int i = 0; i < listUnit.size(); i++) {
			arrayUnits[i] = listUnit.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayUnits);
		spUnit.setAdapter(adapter);
		spUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Unit unit = listUnit.get(position);
				unit1.setUnitid(unit.getId());
				unit1.setUnitname(unit.getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
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
	private PricesystemDAO dao;
	private List<GoodsUnit> listGoodUnit;
	private GoodsUnit unit1;
	private GoodsUnit unit2;
	private EditText ratio1;
	private Spinner spUnit;
	private String[] arrayUnits;
	private List<Unit> listUnit;
	private GoodsUnit unit3;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goodsClass:
			startActivityForResult(new Intent(this, SearchGoodsClassAct.class), 1);
			break;
		case R.id.btnAddUnit:
			// TODO 二级单位
			if (linUnit2 == null) {
				unit2 = new GoodsUnit();
				linUnit2 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
				unitRoot.addView(linUnit2);
				cbBaseUnit2 = (CheckBox) linUnit2.findViewById(R.id.cbBaseUnit);
				cbBigUnit2 = (CheckBox) linUnit2.findViewById(R.id.cbBigUnit);
				Spinner spUnit2 = (Spinner) linUnit2.findViewById(R.id.spUnit);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddNewGoodSAct.this,
						android.R.layout.simple_list_item_1, arrayUnits);
				spUnit2.setAdapter(adapter2);

				spUnit2.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						Unit unit = listUnit.get(position);
						unit2.setUnitid(unit.getId());
						unit2.setUnitname(unit.getName());
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
				return;
			}
			if (linUnit3 == null) {
				unit3 = new GoodsUnit();
				linUnit3 = (LinearLayout) View.inflate(this, R.layout.act_addgoods_unit, null);
				unitRoot.addView(linUnit3);
				cbBaseUnit3 = (CheckBox) linUnit3.findViewById(R.id.cbBaseUnit);
				cbBigUnit3 = (CheckBox) linUnit3.findViewById(R.id.cbBigUnit);
				Spinner spUnit3 = (Spinner) linUnit3.findViewById(R.id.spUnit);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddNewGoodSAct.this,
						android.R.layout.simple_list_item_1, arrayUnits);
				spUnit3.setAdapter(adapter2);
				spUnit3.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						Unit unit = listUnit.get(position);
						unit3.setUnitid(unit.getId());
						unit3.setUnitname(unit.getName());
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
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
		// List<Test> listBase = new ArrayList<Test>();
		// List<Test> listBig = new ArrayList<Test>();
		// int count = 0;
		// listBase.add(new Test("01", null, cbBaseUnit1.isChecked()));
		// listBig.add(new Test("02", null, cbBigUnit1.isChecked()));
		// if (linUnit2 != null) {
		// listBase.add(new Test("11", null, cbBaseUnit2.isChecked()));
		// listBig.add(new Test("12", null, cbBigUnit2.isChecked()));
		// }
		// if (linUnit3 != null) {
		// listBase.add(new Test("21", null, cbBaseUnit3.isChecked()));
		// listBig.add(new Test("22", null, cbBigUnit3.isChecked()));
		// }
		// if (listBase.size() == 0) {
		// showError("必须选择一个基本单位");
		// return;
		// }
		// for (int i = 0; i < listBase.size(); i++) {
		// if (listBase.get(i).isCheck == false && listBig.get(i).isCheck ==
		// false) {
		// showError("单位没有选择!");
		// return;
		// }
		// if (listBase.get(i).isCheck) {
		// count++;
		// }
		// }
		// if (count > 1) {
		// showError("只能有一个基本单位");
		// return;
		// }
		// if (listBig.size() == 0) {
		// showError("必须选择一个计件单位");
		// return;
		// }
		// count = 0;
		// for (int i = 0; i < listBig.size(); i++) {
		// if (listBig.get(i).isCheck) {
		// count++;
		// }
		// }
		// if (count > 1) {
		// showError("只能有一个计件单位");
		// return;
		// }
		// unit2.setUnitid(unitid);
		unit1.setIsbasic(cbBaseUnit1.isChecked());
		unit1.setIsshow(cbBigUnit1.isChecked());
		listGoodUnit.add(unit1);
		if (linUnit2 != null) {
			unit2.setIsbasic(cbBaseUnit2.isChecked());
			unit2.setIsshow(cbBigUnit2.isChecked());
			listGoodUnit.add(unit2);
		}
		if (linUnit3 != null) {
			unit3.setIsbasic(cbBaseUnit3.isChecked());
			unit3.setIsshow(cbBigUnit3.isChecked());
			listGoodUnit.add(unit3);
		}
		boolean isOneBasic = false;
		boolean isOneBig = false;
		for (int i = 0; i < listGoodUnit.size(); i++) {
			GoodsUnit goodsUnit = listGoodUnit.get(i);
			if (goodsUnit.isIsbasic()) {
				isOneBasic = true;
			}
			if (goodsUnit.isIsshow()) {
				isOneBig = true;
			}
			for (int j = 0; j < listGoodUnit.size(); j++) {
				if (i != j && goodsUnit.isIsbasic() && listGoodUnit.get(j).isIsbasic()) {
					showError("只能有一个基本单位");
					return;
				}
				if (i != j && goodsUnit.isIsshow() && listGoodUnit.get(j).isIsshow()) {
					showError("只能有一个计件单位");
					return;
				}
			}
		}
		if (!isOneBasic) {
			showError("请选择基本单位");
			return;
		}
		if (!isOneBig) {
			showError("请选择基计件单位");
			return;
		}
		// TODO 提交
		Goods goods = new Goods();
		goods.name = etName.getText().toString();
		goods.pinyin = "pinyin";
		goods.barcode = etBarcode.getText().toString();
		goods.model = etModel.getText().toString();
		goods.remark = etRemark.getText().toString();
		goods.salecue = etSalecue.getText().toString();
		goods.specification = etSpecification.getText().toString();
		goods.isdiscount = cbIsDiscount.isChecked();
		goods.isusebatch = cbIsusebatch.isChecked();
		String addGood = new ServiceStore().AddGood(goods, listPrice, listGoodUnit);
		if (addGood.isEmpty()) {
			Toast.makeText(this, "添加成功!", Toast.LENGTH_SHORT).show();
		}
		// startActivity(new Intent(this, SwyMain.class));
		// finish();
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
				GoodsClass goodsClass = (GoodsClass) data.getSerializableExtra("goodclass");
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
		public Pricesystem getItem(int position) {
			return listPrice.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(AddNewGoodSAct.this, R.layout.item_addgoods, null);
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			EditText edNumber = (EditText) view.findViewById(R.id.edNumber);
			Pricesystem pricesystem = listPrice.get(position);
			String psname = pricesystem.getPsname();
			tvName.setText(psname);
			edNumber.setText(pricesystem == null ? "" : pricesystem.getPrice());
			edNumber.setTag(Integer.valueOf(position));
			edNumber.addTextChangedListener(new MyWatcher(edNumber));
			return view;
		}

		public class MyWatcher implements TextWatcher {
			View v;

			public MyWatcher(View v) {
				this.v = v;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int i = ((Integer) v.getTag()).intValue();
				listPrice.get(i).setPrice(s.toString().isEmpty() ? "" : s.toString());
			}

		}

	}

	@Override
	public void setActionBarText() {
		setTitle("新增商品");
	}
}
