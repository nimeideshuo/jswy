package com.ahjswy.cn.ui.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.model.DefDocPD;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPricePD;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.MAlertDialog;
import com.ahjswy.cn.ui.SearchHelper;
import com.ahjswy.cn.utils.InfoDialog;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.AutoTextView;
import com.ahjswy.cn.views.Dialog_listCheckBox;
import com.ahjswy.cn.views.Dialog_message;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
	private Scaner scaner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_inventory);
		serviceStore = new ServiceStore();
		initListView();
		initView();
		initData();
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
	}

	private void initView() {
		root = (RelativeLayout) findViewById(R.id.root);
		linearSearch = (LinearLayout) findViewById(R.id.lieSearch);
		atvSearch = (AutoTextView) findViewById(R.id.atvSearch);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		searchHelper = new SearchHelper(this, this.linearSearch);
		listItemDelete = new ArrayList<Long>();
		btnAdd.setOnClickListener(addMoreListener);
		atvSearch.setOnItemClickListener(atvSearchonItemClickListener);
	}

	private void initListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mListView.setOnTouchListener(this);
		adapter = new InventoryItemAdapter(this);
		SwipeMenuCreator local5 = new SwipeMenuCreator() {
			public void create(SwipeMenu menu) {
				SwipeMenuItem menuItem = new SwipeMenuItem(getApplicationContext());
				menuItem.setTitle("删除");
				menuItem.setTitleSize(14);
				menuItem.setTitleColor(-16777216);
				menuItem.setBackground(new ColorDrawable(Color.rgb(201, 201, 206)));
				menuItem.setWidth(100);
				menu.addMenuItem(menuItem);
			}
		};
		mListView.setMenuCreator(local5);
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					long l = listItem.get(position).getItemid();
					if (l > 0L) {
						listItemDelete.add(Long.valueOf(l));
					}
					listItem.remove(position);
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							adapter.setData(InventoryEditActivity.this.listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
						}
					}, 180L);

					break;
				}
				return false;
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (doc.isIsavailable() && doc.isIsposted()) {
					return;
				}

				if ((menuPopup != null) && (menuPopup.isShowing())) {
					menuPopup.dismiss();
					WindowManager.LayoutParams attributes = getWindow().getAttributes();
					attributes.alpha = 1.0f;
					getWindow().setAttributes(attributes);
				}

				Intent intent = new Intent(InventoryEditActivity.this, InventoryAddGoodAct.class);
				intent.putExtra("warehouseid", doc.getWarehouseid());
				intent.putExtra("position", position);
				intent.putExtra("docitem", (Serializable) listItem.get(position));
				startActivityForResult(intent, 3);

			}
		});
	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			atvSearch.setText("");
			// if (dialog != null) {
			// dialog.dismiss();
			// }
			readBarcode(barcode);
		}
	};
	public AdapterView.OnItemClickListener atvSearchonItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if ((menuPopup != null) && (menuPopup.isShowing())) {
				menuPopup.dismiss();
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.alpha = 1.0F;
				getWindow().setAttributes(localLayoutParams);
			}
			if (!TextUtils.isEmptyS(doc.getWarehouseid())) {
				PDH.showMessage("请先选择盘点仓库");
				return;
			}
			// GoodsUnit localGoodsUnit;
			// if (Utils.DEFAULT_TransferDocUNIT == 0) {
			// localGoodsUnit = new
			// GoodsUnitDAO().getBasicUnit(localGoodsThin.getId());
			// } else {
			// localGoodsUnit = new
			// GoodsUnitDAO().getBigUnit(localGoodsThin.getId());
			// }
			// DefDocItemPD localDefDocItemPD = new DefDocItemPD();
			// localDefDocItemPD.setGoodsid(localGoodsThin.getId());
			// localDefDocItemPD.setGoodsname(localGoodsThin.getName());
			// localDefDocItemPD.setBarcode(localGoodsThin.getBarcode());
			// localDefDocItemPD.setSpecification(localGoodsThin.getSpecification());
			// localDefDocItemPD.setUnitid(localGoodsUnit.getUnitid());
			// localDefDocItemPD.setUnitname(localGoodsUnit.getUnitname());
			// localDefDocItemPD.setIsusebatch(localGoodsThin.isIsusebatch());
			// Intent localIntent = new Intent(InventoryEditActivity.this,
			// InventoryAddGoodAct.class);
			// localIntent.putExtra("warehouseid", doc.getWarehouseid());
			// localIntent.putExtra("docitem", localDefDocItemPD);
			// startActivityForResult(localIntent, 1);
			final GoodsThin localGoodsThin = (GoodsThin) searchHelper.getAdapter().getTempGoods().get(position);
			atvSearch.setText("");
			PDH.show(InventoryEditActivity.this, new PDH.ProgressCallBack() {
				public void action() {
					DefDocItemPD localDefDocItemPD = fillItem(localGoodsThin, 0.0D, 0.0D, 0.0D);
					newListItem = new ArrayList<DefDocItemPD>();
					newListItem.add(localDefDocItemPD);
					ArrayList<ReqStrGetGoodsPricePD> localArrayList = new ArrayList<ReqStrGetGoodsPricePD>();
					ReqStrGetGoodsPricePD rp = new ReqStrGetGoodsPricePD();
					rp.setGoodsid(localDefDocItemPD.getGoodsid());
					rp.setUnitid(localDefDocItemPD.getUnitid());
					rp.setWarehouseid(InventoryEditActivity.this.doc.getWarehouseid());
					localArrayList.add(rp);
					String localString = new ServiceGoods().gds_GetMultiGoodsPricePD(localArrayList);
					handlerGet.sendMessage(handlerGet.obtainMessage(2, localString));
				}
			});
		}

	};

	private void readBarcode(String barcode) {
		final ArrayList<GoodsThin> localGoodsThin = new GoodsDAO().getGoodsThinList(barcode);
		if (localGoodsThin == null) {
			return;
		}
		if (localGoodsThin.size() == 1) {
			PDH.show(InventoryEditActivity.this, new PDH.ProgressCallBack() {
				public void action() {
					DefDocItemPD localDefDocItemPD = fillItem(localGoodsThin.get(0), 0.0D, 0.0D, 0.0D);
					newListItem = new ArrayList<DefDocItemPD>();
					newListItem.add(localDefDocItemPD);
					ArrayList<ReqStrGetGoodsPricePD> localArrayList = new ArrayList<ReqStrGetGoodsPricePD>();
					ReqStrGetGoodsPricePD rp = new ReqStrGetGoodsPricePD();
					rp.setGoodsid(localDefDocItemPD.getGoodsid());
					rp.setUnitid(localDefDocItemPD.getUnitid());
					rp.setWarehouseid(InventoryEditActivity.this.doc.getWarehouseid());
					localArrayList.add(rp);
					String localString = new ServiceGoods().gds_GetMultiGoodsPricePD(localArrayList);
					handlerGet.sendMessage(handlerGet.obtainMessage(2, localString));
				}
			});
		} else if (localGoodsThin.size() > 1) {
			atvSearch.setText(barcode);
			// dialog.setGoods(localGoodsThin);
			// dialog.setTempGoods(localGoodsThin);
			// dialog.ShowMe();
			// dialog.ensure(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// List<GoodsThin> select = dialog.getSelect();
			//
			//
			//
			// }
			// });
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String message = msg.obj.toString();
			if (RequestHelper.isSuccess(message)) {
				if (msg.what == 2) {
					PDH.showSuccess("删除成功");
					setResult(Activity.RESULT_OK);
					finish();
					return;
				}
				DocContainerEntity localObject = (DocContainerEntity) JSONUtil.readValue(message,
						DocContainerEntity.class);
				doc = ((DefDocPD) JSONUtil.readValue(localObject.getDoc(), DefDocPD.class));
				listItem = JSONUtil.str2list(localObject.getItem(), DefDocItemPD.class);
				listItemDelete = new ArrayList<Long>();
				switch (msg.what) {
				case 0:
					adapter.setData(listItem);
					refreshUI();
					PDH.showSuccess("保存成功");
					ishaschanged = false;
					setActionBarText();
					return;
				case 1:
					if ((doc.isIsavailable()) && (doc.isIsposted())) {
						PDH.showSuccess("审核成功");
						setResult(Activity.RESULT_OK);
						finish();
						return;
					}
					adapter.setData(listItem);
					refreshUI();
					InfoDialog.showError(InventoryEditActivity.this, localObject.getInfo());
					return;
				}

			}
			InfoDialog.showError(InventoryEditActivity.this, message);
		};
	};

	private void initData() {
		docContainer = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		this.doc = ((DefDocPD) JSONUtil.readValue(docContainer.getDoc(), DefDocPD.class));
		this.listItem = JSONUtil.str2list(docContainer.getItem(), DefDocItemPD.class);
		ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		adapter.setData(this.listItem);
		mListView.setAdapter(adapter);
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
				return;
			}
			List<GoodsThin> select = searchHelper.getAdapter().getSelect();
			if (select.isEmpty()) {
				return;
			}
			atvSearch.setText("");
			if (!"1".equals(new AccountPreference().getValue("goods_select_more"))) {
				ArrayList<DefDocItemPD> localArrayList = new ArrayList<DefDocItemPD>();
				for (int i = 0; i < select.size(); i++) {
					GoodsThin localGoodsThin = select.get(i);
					localArrayList.add(fillItem(localGoodsThin, 0.0D, 0.0D, 1));
				}
				startActivityForResult(new Intent(InventoryEditActivity.this, InventoryAddMoreGoodsAct.class)
						.putExtra("items", JSONUtil.object2Json(localArrayList))
						.putExtra("warehouseid", doc.getWarehouseid()), 2);
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "单击显示菜单").setIcon(getResources().getDrawable(R.drawable.btn_submenu)).setShowAsAction(1);
		return super.onCreateOptionsMenu(menu);
	}

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
		case android.R.id.home:
			if (ishaschanged) {
				final MAlertDialog dialog = new MAlertDialog(this);
				dialog.setMessage("是否保存当前单据？");
				dialog.setCancelListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						save();
					}
				});
				dialog.setComfirmListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
				dialog.show();
				return true;
			}
			break;
		}
		return super.onOptionsItemSelected(menu);
	}

	public DefDocItemPD fillItem(GoodsThin goodsThin, double stocknum, double costprice, double number) {
		GoodsUnitDAO goodsUnitDAO = new GoodsUnitDAO();
		DefDocItemPD defDocItemPD = new DefDocItemPD();
		defDocItemPD.setItemid(0L);
		defDocItemPD.setDocid(doc.getDocid());
		defDocItemPD.setGoodsid(goodsThin.getId());
		defDocItemPD.setGoodsname(goodsThin.getName());
		defDocItemPD.setBarcode(goodsThin.getBarcode());
		defDocItemPD.setSpecification(goodsThin.getSpecification());
		defDocItemPD.setModel(goodsThin.getModel());
		GoodsUnit localGoodsUnit;
		if (Utils.DEFAULT_TransferDocUNIT == 0) {
			localGoodsUnit = goodsUnitDAO.queryBaseUnit(goodsThin.getId());
		} else {
			localGoodsUnit = goodsUnitDAO.queryBigUnit(goodsThin.getId());
		}
		defDocItemPD.setUnitid(localGoodsUnit.getUnitid());
		defDocItemPD.setUnitname(localGoodsUnit.getUnitname());
		defDocItemPD.setStocknum(Utils.normalize(stocknum, 2));
		defDocItemPD.setBigstocknum(goodsUnitDAO.getBigNum(defDocItemPD.getGoodsid(), defDocItemPD.getUnitid(),
				defDocItemPD.getStocknum()));
		defDocItemPD.setNum(number);
		defDocItemPD.setBignum(
				goodsUnitDAO.getBigNum(defDocItemPD.getGoodsid(), defDocItemPD.getUnitid(), defDocItemPD.getNum()));
		defDocItemPD.setNetnum(Utils.normalize(defDocItemPD.getNum() - defDocItemPD.getStocknum(), 2));
		defDocItemPD.setBignetnum(
				goodsUnitDAO.getBigNum(defDocItemPD.getGoodsid(), defDocItemPD.getUnitid(), defDocItemPD.getNetnum()));
		defDocItemPD.setCostprice(costprice);
		defDocItemPD.setNetamount(Utils.normalizeSubtotal(defDocItemPD.getNetnum() * defDocItemPD.getCostprice()));
		defDocItemPD.setRemark("");
		defDocItemPD.setRversion(0L);
		defDocItemPD.setIsusebatch(goodsThin.isIsusebatch());
		return defDocItemPD;

	}

	private String validateDoc() {

		if (!TextUtils.isEmptyS(this.doc.getBuildtime())) {
			this.doc.setBuildtime(Utils.formatDate(Utils.getCurrentTime(false), "yyyy-MM-dd HH:mm:ss"));
		}
		if (!TextUtils.isEmptyS(this.doc.getBuilderid())) {
			this.doc.setBuilderid(SystemState.getUser().getId());
		}
		if (!TextUtils.isEmptyS(this.doc.getDepartmentid())) {
			return "部门不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getWarehouseid())) {
			return "仓库不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getPrinttemplate())) {
			return "打印模板不能为空";
		}
		for (int i = 0; i < listItem.size(); i++) {
			DefDocItemPD itemPD = listItem.get(i);
			if (!TextUtils.isEmptyS(itemPD.getUnitid())) {
				return "【" + itemPD.getGoodsname() + "】没有选择单位";
			}
			if ((!TextUtils.isEmptyS(itemPD.getBatch())) && (itemPD.isIsusebatch())) {
				return "【" + itemPD.getGoodsname() + "】没有选择批次";
			}
		}

		return null;
	}

	public DefDocPD getDoc() {
		return this.doc;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			LayoutParams attributes = this.getWindow().getAttributes();
			attributes.alpha = 1.0f;
			getWindow().setAttributes(attributes);
		}
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
				this.doc = ((DefDocPD) data.getSerializableExtra("doc"));
				break;
			case 1:// TODO
				this.listItem.add((DefDocItemPD) data.getSerializableExtra("docitem"));
				this.adapter.setData(this.listItem);
				refreshUI();
				break;
			case 2:
				this.newListItem = JSONUtil.str2list(data.getStringExtra("items"), DefDocItemPD.class);
				ArrayList<ReqStrGetGoodsPricePD> listPricePD = new ArrayList<ReqStrGetGoodsPricePD>();
				for (int i = 0; i < newListItem.size(); i++) {
					DefDocItemPD defDocItemPD = newListItem.get(i);
					ReqStrGetGoodsPricePD rp = new ReqStrGetGoodsPricePD();
					rp.setWarehouseid(this.doc.getWarehouseid());
					rp.setGoodsid(defDocItemPD.getGoodsid());
					rp.setUnitid(defDocItemPD.getUnitid());
					rp.setStocknum(0.0D);
					rp.setCostprice(0.0D);
					rp.setBatch(defDocItemPD.getBatch());
					listPricePD.add(rp);
				}
				String respPricePD = new ServiceGoods().gds_GetMultiGoodsPricePD(listPricePD);
				this.handlerGet.sendMessage(this.handlerGet.obtainMessage(0, respPricePD));
				break;
			case 3:
				int i = data.getIntExtra("position", 0);
				DefDocItemPD localDefDocItemPD1 = (DefDocItemPD) data.getSerializableExtra("docitem");
				this.listItem.set(i, localDefDocItemPD1);
				this.adapter.setData(this.listItem);
				refreshUI();
				break;
			}

		}
	}

	// TODO refreshUI
	public void refreshUI() {
		if ((this.doc.isIsavailable()) && (this.doc.isIsposted())) {
			// findViewById(2131296403).setVisibility(8);
			// findViewById(2131296289).setVisibility(8);
			mListView.setItemSwipe(false);
		} else {
			// findViewById(2131296403).setVisibility(0);
			// findViewById(2131296289).setVisibility(0);
			mListView.setItemSwipe(true);
		}

	}

	public void check(final boolean print) {
		String validateDoc = validateDoc();
		if (validateDoc != null) {
			InfoDialog.showError(this, validateDoc);
			return;
		}
		if ((this.listItem == null) || (this.listItem.size() == 0)) {
			InfoDialog.showError(this, "空单不能审核");
			return;
		}
		final Dialog_message dialog = new Dialog_message(this);
		dialog.setMessage("审核后的单据不能修改。\n确定审核？");
		dialog.setCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				PDH.show(InventoryEditActivity.this, new PDH.ProgressCallBack() {

					@Override
					public void action() {
						String str = serviceStore.str_CheckPDDoc(doc, listItem, listItemDelete, print);
						handler.sendMessage(handler.obtainMessage(1, str));
					}

				});

			}
		});
		dialog.show();
	}

	// 属性
	public void docProperty() {
		Intent localIntent = new Intent();
		localIntent.putExtra("doc", this.doc);
		localIntent.putExtra("itemcount", this.listItem.size());
		startActivityForResult(localIntent.setClass(this, InventoryDocOpenActivity.class), 0);
	}

	public void save() {
		String str = validateDoc();
		if (str != null) {
			InfoDialog.showError(this, str);
			return;
		}
		PDH.show(this, new PDH.ProgressCallBack() {

			@Override
			public void action() {
				String str = serviceStore.str_SavePDDoc(doc, listItem, listItemDelete);
				handler.sendMessage(handler.obtainMessage(0, str));
			}

		});
	}

	public void delete() {
		if (this.doc.getDocid() > 0L) {
			final Dialog_message dialog = new Dialog_message(this);
			dialog.setMessage("确定删除?");
			dialog.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					PDH.show(InventoryEditActivity.this, new PDH.ProgressCallBack() {

						@Override
						public void action() {
							String str = serviceStore.str_DeleteDoc(doc.getDoctypeid(), doc.getDocid());
							handler.sendMessage(handler.obtainMessage(2, str));
						}

					});
				}
			});
			dialog.show();
		} else {
			finish();
		}
	}

	private Handler handlerGet = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String message = msg.obj.toString();
			if (RequestHelper.isSuccess(message)) {
				GoodsUnitDAO gd = new GoodsUnitDAO();
				List<ReqStrGetGoodsPricePD> listNewItem = JSONUtil.str2list(message, ReqStrGetGoodsPricePD.class);
				if (msg.what == 0) {
					for (int i = 0; i < newListItem.size(); i++) {
						DefDocItemPD itemPD = (DefDocItemPD) newListItem.get(i);
						for (int j = 0; j < listNewItem.size(); j++) {
							ReqStrGetGoodsPricePD goodsPricePD = listNewItem.get(j);
							if ((!itemPD.getGoodsid().equals(goodsPricePD.getGoodsid()))
									|| (!itemPD.getUnitid().equals(goodsPricePD.getUnitid()))) {
								break;
							}
							itemPD.setStocknum(goodsPricePD.getStocknum());
							itemPD.setBigstocknum(
									gd.getBigNum(itemPD.getGoodsid(), itemPD.getUnitid(), itemPD.getStocknum()));
							itemPD.setNetnum(Utils.normalize(itemPD.getNum() - itemPD.getStocknum(), 2));
							itemPD.setBignetnum(
									gd.getBigNum(itemPD.getGoodsid(), itemPD.getUnitid(), itemPD.getNetnum()));
							itemPD.setCostprice(goodsPricePD.getCostprice());
							itemPD.setNetamount(Utils.normalizeSubtotal(itemPD.getNetnum() * itemPD.getCostprice()));
							itemPD.setBatch(goodsPricePD.getBatch());
						}
					}
					listItem.addAll(newListItem);
					adapter.setData(listItem);
					refreshUI();
					ishaschanged = true;
					setActionBarText();
					return;
				}
				if (msg.what == 2) {
					DefDocItemPD itemPD = (DefDocItemPD) newListItem.get(0);
					ReqStrGetGoodsPricePD goodsPrice = (ReqStrGetGoodsPricePD) listNewItem.get(0);
					itemPD.setStocknum(goodsPrice.getStocknum());
					itemPD.setBigstocknum(gd.getBigNum(itemPD.getGoodsid(), itemPD.getUnitid(), itemPD.getStocknum()));
					itemPD.setNum(itemPD.getStocknum());
					itemPD.setBignum(itemPD.getBigstocknum());
					itemPD.setNetnum(0.0D);
					itemPD.setBignetnum("");
					itemPD.setCostprice(goodsPrice.getCostprice());
					itemPD.setNetamount(0.0D);
					Intent localIntent = new Intent(InventoryEditActivity.this, InventoryAddGoodAct.class);
					localIntent.putExtra("warehouseid", doc.getWarehouseid());
					localIntent.putExtra("docitem", itemPD);
					startActivityForResult(localIntent, 1);

				}
			}
		};
	};
	private ArrayList<DefDocItemPD> localArrayList;

	private void saveDoc() {
		final MAlertDialog dialog = new MAlertDialog(this);
		dialog.setMessage("是否保存当前单据？");
		dialog.setCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				save();
			}
		});
		dialog.setComfirmListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
		return;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			LayoutParams attributes = getWindow().getAttributes();
			attributes.alpha = 1.0F;
			getWindow().setAttributes(attributes);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ishaschanged) {
				saveDoc();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		System.out.println("onBackPressed");
		if (ishaschanged) {
			final MAlertDialog dialog = new MAlertDialog(this);
			dialog.setMessage("是否保存当前单据？");
			dialog.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					save();
				}
			});
			dialog.setComfirmListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
			return;
		}

		// super.onBackPressed();
	}

	@Override
	public void setActionBarText() {
		super.setActionBarText();
		if (this.ishaschanged) {
			setTitle("*" + doc.getShowid());
		} else {
			setTitle(doc.getShowid());

		}
	}
}
