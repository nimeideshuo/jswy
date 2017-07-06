package com.ahjswy.cn.ui.inventory;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.model.DefDocPD;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.SearchHelper;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.AutoTextView;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class InventoryEditActivity extends BaseActivity implements OnTouchListener {
	private RelativeLayout root;
	private AutoTextView atvSearch;
	private LinearLayout linearSearch;
	private Button btnAdd;
	private SwipeMenuListView mListView;
	private ServiceStore serviceStore;
	private boolean ishaschanged;
	private SearchHelper searchHelper;
	private DocContainerEntity docContainer;
	DefDocPD doc;
	List<DefDocItemPD> listItem;
	private ArrayList<Long> listItemDelete;
	private InventoryItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_inventory);
		serviceStore = new ServiceStore();
		initListView();
		initView();
		initData();

	}

	private void initView() {
		root = (RelativeLayout) findViewById(R.id.root);
		linearSearch = (LinearLayout) findViewById(R.id.lieSearch);
		atvSearch = (AutoTextView) findViewById(R.id.atvSearch);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		searchHelper = new SearchHelper(this, this.linearSearch);
		listItemDelete = new ArrayList<Long>();
		btnAdd.setOnClickListener(addMoreListener);
	}

	private void initListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mListView.setOnTouchListener(this);
		adapter = new InventoryItemAdapter(this);
	}

	private void initData() {
		docContainer = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		this.doc = ((DefDocPD) JSONUtil.readValue(docContainer.getDoc(), DefDocPD.class));
		this.listItem = JSONUtil.str2list(docContainer.getItem(), DefDocItemPD.class);
		ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		adapter.setData(this.listItem);
	}

	private View.OnClickListener addMoreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if ((menuPopup != null) && (menuPopup.isShowing())) {
				menuPopup.dismiss();
				LayoutParams layoutParams = InventoryEditActivity.this.getWindow().getAttributes();
				layoutParams.alpha = 1.0F;
				InventoryEditActivity.this.getWindow().setAttributes(layoutParams);
			}
			if (!TextUtils.isEmptyS(doc.getWarehouseid())) {
				PDH.showMessage("请先选择盘点仓库");
				atvSearch.setText("");
				return;
			}
			List<GoodsThin> select = searchHelper.getAdapter().getSelect();
			if (select.isEmpty()) {
				atvSearch.setText("");
			}
			if (!"1".equals(new AccountPreference().getValue("goods_select_more"))) {
				ArrayList<DefDocItemPD> localArrayList = new ArrayList<DefDocItemPD>();
				for (int i = 0; i < select.size(); i++) {
					GoodsThin localGoodsThin = select.get(i);
					localArrayList.add(fillItem(localGoodsThin, 0.0D, 0.0D, 0.0D));
				}
				startActivityForResult(new Intent(InventoryEditActivity.this, InventoryAddMoreGoodsAct.class)
						.putExtra("items", JSONUtil.object2Json(localArrayList))
						.putExtra("warehouseid", doc.getWarehouseid()), 2);
			}
		}

	};
	InventoryEditMenuPopup menuPopup;

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		this.getResources().getDrawable(R.drawable.search_clear_pressed);
		switch (menu.getItemId()) {
		case 0:
			if (this.menuPopup == null) {
				this.menuPopup = new InventoryEditMenuPopup(this);
			}
			menuPopup.showAtLocation(this.root, 80, 0, 0);
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 0.8F;
			getWindow().setAttributes(localLayoutParams);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(menu);
	}

	protected DefDocItemPD fillItem(GoodsThin paramGoodsThin, double paramDouble1, double paramDouble2,
			double paramDouble3) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemPD localDefDocItemPD = new DefDocItemPD();
		localDefDocItemPD.setItemid(0L);
		localDefDocItemPD.setDocid(this.doc.getDocid());
		localDefDocItemPD.setGoodsid(paramGoodsThin.getId());
		localDefDocItemPD.setGoodsname(paramGoodsThin.getName());
		localDefDocItemPD.setBarcode(paramGoodsThin.getBarcode());
		localDefDocItemPD.setSpecification(paramGoodsThin.getSpecification());
		localDefDocItemPD.setModel(paramGoodsThin.getModel());
		GoodsUnit localGoodsUnit;
		if (Utils.DEFAULT_TransferDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		localDefDocItemPD.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItemPD.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItemPD.setStocknum(Utils.normalize(paramDouble1, 2));
		localDefDocItemPD.setBigstocknum(localGoodsUnitDAO.getBigNum(localDefDocItemPD.getGoodsid(),
				localDefDocItemPD.getUnitid(), localDefDocItemPD.getStocknum()));
		localDefDocItemPD.setNum(paramDouble3);
		localDefDocItemPD.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItemPD.getGoodsid(),
				localDefDocItemPD.getUnitid(), localDefDocItemPD.getNum()));
		localDefDocItemPD.setNetnum(Utils.normalize(localDefDocItemPD.getNum() - localDefDocItemPD.getStocknum(), 2));
		localDefDocItemPD.setBignetnum(localGoodsUnitDAO.getBigNum(localDefDocItemPD.getGoodsid(),
				localDefDocItemPD.getUnitid(), localDefDocItemPD.getNetnum()));
		localDefDocItemPD.setCostprice(paramDouble2);
		localDefDocItemPD.setNetamount(
				Utils.normalizeSubtotal(localDefDocItemPD.getNetnum() * localDefDocItemPD.getCostprice()));
		localDefDocItemPD.setRemark("");
		localDefDocItemPD.setRversion(0L);
		localDefDocItemPD.setIsusebatch(paramGoodsThin.isIsusebatch());
		return localDefDocItemPD;

	}

	private String validateDoc() {

		if (TextUtils.isEmptyS(this.doc.getBuildtime())) {
			this.doc.setBuildtime(Utils.formatDate(Utils.getCurrentTime(false), "yyyy-MM-dd HH:mm:ss"));
		}
		if (TextUtils.isEmptyS(this.doc.getBuilderid())) {
			this.doc.setBuilderid(SystemState.getUser().getId());
		}
		if (TextUtils.isEmptyS(this.doc.getDepartmentid())) {
			return "部门不能为空";
		}
		if (TextUtils.isEmptyS(this.doc.getWarehouseid())) {
			return "仓库不能为空";
		}
		if (TextUtils.isEmptyS(this.doc.getPrinttemplate())) {
			return "打印模板不能为空";
		}
		for (int i = 0; i < listItem.size(); i++) {
			DefDocItemPD itemPD = listItem.get(i);
			if (TextUtils.isEmptyS(itemPD.getUnitid())) {
				return "【" + ((DefDocItemPD) itemPD).getGoodsname() + "】没有选择单位";
			}
			if ((TextUtils.isEmptyS(itemPD.getBatch())) && (itemPD.isIsusebatch())) {
				return "【" + itemPD.getGoodsname() + "】没有选择批次";
			}
		}

		return "";
	}

	public DefDocPD getDoc() {
		return this.doc;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
		// this.menuPopup.dismiss();
		// v = getWindow().getAttributes();
		// v.alpha = 1.0F;
		// getWindow().setAttributes(v);
		// }
		return false;
	}

	private List<DefDocItemPD> newListItem;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setActionBarText();
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 0:
				this.newListItem = JSONUtil.str2list(data.getStringExtra("items"), DefDocItemPD.class);
				break;
			}
		}
	}

	@Override
	public void setActionBarText() {
		super.setActionBarText();
		if (this.ishaschanged) {
			setTitle(doc.getShowid());
		} else {
			setTitle("*" + doc.getShowid());
		}
	}

	// TODO 过账
	public void check(boolean print) {

	}

	// 属性
	public void docProperty() {
		// TODO Auto-generated method stub

	}

	public void save() {
		// TODO Auto-generated method stub

	}

	public void delete() {
		// TODO Auto-generated method stub

	}
}
