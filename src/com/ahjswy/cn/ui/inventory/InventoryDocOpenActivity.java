package com.ahjswy.cn.ui.inventory;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.model.DefDocPD;
import com.ahjswy.cn.model.Department;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.Warehouse;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.DepartmentSearchAct;
import com.ahjswy.cn.ui.WarehouseSearchAct;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InventoryDocOpenActivity extends BaseActivity implements OnClickListener {

	private EditText etRemark;
	private EditText etSummary;
	private Button btnDepartment;
	private Button btnWarehouse;
	DefDocPD doc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dia_inventory_opendoc);
		initView();
		initData();
	}

	private void initView() {
		btnDepartment = (Button) findViewById(R.id.btnDepartment);
		btnWarehouse = (Button) findViewById(R.id.btnWarehouse);
		etRemark = (EditText) findViewById(R.id.etRemark);
		etSummary = (EditText) findViewById(R.id.etSummary);
		btnDepartment.setOnClickListener(this);
		btnWarehouse.setOnClickListener(this);
	}

	private void initData() {
		Department localDepartment = SystemState.getDepartment();
		if (localDepartment != null) {
			this.btnDepartment.setTag(localDepartment.getDid());
			this.btnDepartment.setText(localDepartment.getDname());
		}
		// 设置仓库
		Warehouse localWarehouse = SystemState.getWarehouse();
		if (localWarehouse != null) {
			this.btnWarehouse.setTag(localWarehouse.getId().toString());
			this.btnWarehouse.setText(localWarehouse.getName());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		if ((this.doc == null) || (!this.doc.isIsavailable()) || (!this.doc.isIsposted())) {
			paramMenu.add(0, 0, 0, "确定").setShowAsAction(2);
		}
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case 0:
			if (!TextUtils.isEmptyS(this.btnDepartment.getTag().toString())) {
				PDH.showMessage("部门不能为空");
			} else if (!TextUtils.isEmptyS(this.btnWarehouse.getTag().toString())) {
				PDH.showMessage("仓库不能为空");
			} else if (doc != null) {
				fillDoc();
				Intent intent = new Intent();
				intent.putExtra("doc", this.doc);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				initDoc();
			}

			break;
		}
		return super.onOptionsItemSelected(menu);
	}

	/**
	 * 跳转到盘点开单
	 */
	private void fillDoc() {
		if (TextUtils.isEmptyS(this.btnDepartment.getTag().toString())) {
			doc.setDepartmentid(btnDepartment.getTag().toString());
			doc.setDepartmentname(btnDepartment.getText().toString());
		} else {
			this.doc.setDepartmentid(null);
			this.doc.setDepartmentname("");

		}
		if (TextUtils.isEmptyS(btnWarehouse.getTag().toString())) {
			doc.setWarehouseid(btnWarehouse.getTag().toString());
			doc.setWarehousename(btnWarehouse.getText().toString());
		} else {
			this.doc.setWarehouseid(null);
			this.doc.setWarehousename("");
		}
		this.doc.setRemark(this.etRemark.getText().toString());
		this.doc.setSummary(this.etSummary.getText().toString());
	}

	private void initDoc() {
		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				String str = new ServiceStore().str_InitPDDoc(btnDepartment.getTag().toString(),
						btnWarehouse.getTag().toString());
				handler.sendMessage(handler.obtainMessage(0, str));
			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String str = msg.obj.toString();
			if (RequestHelper.isSuccess(str)) {
				DocContainerEntity docent = (DocContainerEntity) JSONUtil.readValue(str, DocContainerEntity.class);
				doc = ((DefDocPD) JSONUtil.readValue(docent.getDoc(), DefDocPD.class));
				fillDoc();
				Intent localIntent = new Intent();
				localIntent.setClass(InventoryDocOpenActivity.this, InventoryEditActivity.class);
				localIntent.putExtra("docContainer", docent);
				localIntent.putExtra("ishaschanged", true);
				startActivity(localIntent);
				finish();
				return;
			}
			PDH.showFail(str);
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDepartment:
			startActivityForResult(new Intent(this, DepartmentSearchAct.class), 1);
			break;
		case R.id.btnWarehouse:
			startActivityForResult(new Intent(this, WarehouseSearchAct.class), 2);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:
				Department department = (Department) data.getExtras().get("department");
				btnDepartment.setText(department.getDname());
				btnDepartment.setTag(department.getDid());
				break;
			case 2:
				Warehouse warehouse = (Warehouse) data.getExtras().get("warehouse");
				this.btnWarehouse.setText(warehouse.getName());
				this.btnWarehouse.setTag(warehouse.getId());
				break;
			}
		}
	}

	@Override
	public void setActionBarText() {
		super.setActionBarText();
		if (this.doc != null) {
			setTitle(this.doc.getShowid());
		} else {
			setTitle("盘点开单");
		}

	}
}
