package com.ahjswy.cn.ui.transfer;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.dao.WarehouseDAO;
import com.ahjswy.cn.model.DefDocItem;
import com.ahjswy.cn.model.DefDocTransfer;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Warehouse;
import com.ahjswy.cn.popupmenu.TransferEditMenuPopup;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TransferEditActivity extends BaseActivity implements OnTouchListener, ScanerBarcodeListener {

	private ServiceStore serviceStore;
	private RelativeLayout root;
	private Button btnAdd;
	private DefDocTransfer doc;
	private List<DefDocItem> listItem;
	private List<Long> listItemDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_transfer);
		serviceStore = new ServiceStore();
		root = (RelativeLayout) findViewById(R.id.root);
		initListView();
		LinearLayout linearSearch = ((LinearLayout) findViewById(R.id.lieSearch));
		atvSearch = ((AutoTextView) findViewById(R.id.atvSearch));
		searchHelper = new SearchHelper(this, linearSearch);
		atvSearch.setOnItemClickListener(onItemClickListener);
		btnAdd = ((Button) findViewById(R.id.btnAdd));
		this.root.setOnTouchListener(this);
		this.btnAdd.setOnClickListener(this.addMoreListener);
		this.ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		doccontainer = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		this.doc = ((DefDocTransfer) JSONUtil.readValue(doccontainer.getDoc(), DefDocTransfer.class));
		this.listItem = JSONUtil.str2list(doccontainer.getItem(), DefDocItem.class);
		this.listItemDelete = new ArrayList<Long>();
		factory = Scaner.factory(this);
		factory.setBarcodeListener(this);
	}

	private void initListView() {
		listView = ((SwipeMenuListView) findViewById(R.id.listView));
		listView.setOnTouchListener(this);
		adapter = new TransferItemAdapter(this);
		listView.setAdapter(adapter);
		SwipeMenuCreator local5 = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem localSwipeMenuItem = new SwipeMenuItem(TransferEditActivity.this.getApplicationContext());
				localSwipeMenuItem.setTitle("复制");
				localSwipeMenuItem.setTitleSize(14);
				localSwipeMenuItem.setTitleColor(-16777216);
				localSwipeMenuItem.setWidth(100);
				localSwipeMenuItem.setBackground(new ColorDrawable(Color.rgb(48, 177, 245)));
				menu.addMenuItem(localSwipeMenuItem);
				localSwipeMenuItem = new SwipeMenuItem(TransferEditActivity.this.getApplicationContext());
				localSwipeMenuItem.setTitle("删除");
				localSwipeMenuItem.setTitleSize(14);
				localSwipeMenuItem.setTitleColor(-16777216);
				localSwipeMenuItem.setBackground(new ColorDrawable(Color.rgb(201, 201, 206)));
				localSwipeMenuItem.setWidth(100);
				menu.addMenuItem(localSwipeMenuItem);
			}

		};

		this.listView.setMenuCreator(local5);
		this.listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					DefDocItem item = new DefDocItem(listItem.get(position));
					item.setItemid(0L);
					listItem.add(item);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
						}
					}, 180L);

					break;
				case 1:
					long l = listItem.get(position).getItemid();
					if (l > 0L) {
						listItemDelete.add(Long.valueOf(l));
					}
					listItem.remove(position);
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
						}
					}, 180L);
					break;
				default:
					break;
				}
				return false;
			}

		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if ((doc.isIsavailable()) && (doc.isIsposted())) {
					return;
				}
				if ((menuPopup != null) && (menuPopup.isShowing())) {
					menuPopup.dismiss();
					LayoutParams attributes = getWindow().getAttributes();
					attributes.alpha = 1.0F;
					getWindow().setAttributes(attributes);
				}
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("docitem", listItem.get(position));
				startActivityForResult(intent.setClass(TransferEditActivity.this, TransferAddGoodAct.class), 3);

			}
		});
	}

	private View.OnClickListener addMoreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if ((menuPopup != null) && (menuPopup.isShowing())) {
				menuPopup.dismiss();
				LayoutParams attributes = getWindow().getAttributes();
				attributes.alpha = 1.0F;
				getWindow().setAttributes(attributes);
			}
			List<GoodsThin> select = searchHelper.getAdapter().getSelect();
			if (select.isEmpty()) {
				return;
			}
			atvSearch.setText("");
			if (!"1".equals(new AccountPreference().getValue("goods_select_more"))) {
				ArrayList<DefDocItem> localArrayList = new ArrayList<DefDocItem>();

				for (int i = 0; i < select.size(); i++) {
					GoodsThin goodsthin = (GoodsThin) select.get(i);
					localArrayList.add(fillItem(goodsthin, 0.0D, 0.0D));
				}
				startActivityForResult(new Intent().setClass(TransferEditActivity.this, TransferAddMoreGoodsAct.class)
						.putExtra("items", JSONUtil.object2Json(localArrayList)), 2);
			}
		}

	};

	AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final GoodsThin goodsThin = searchHelper.getAdapter().getTempGoods().get(position);
			atvSearch.setText("");
			PDH.show(TransferEditActivity.this, new PDH.ProgressCallBack() {

				@Override
				public void action() {
					DefDocItem localObject = fillItem(goodsThin, 0.0D, 0.0D);
					newListItem = new ArrayList<DefDocItem>();
					newListItem.add(localObject);
					ArrayList<ReqStrGetGoodsPrice> localArrayList = new ArrayList<ReqStrGetGoodsPrice>();
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new ReqStrGetGoodsPrice();
					localReqStrGetGoodsPrice.setType(0);
					localReqStrGetGoodsPrice.setCustomerid(null);
					localReqStrGetGoodsPrice.setWarehouseid(localObject.getWarehouseid());
					localReqStrGetGoodsPrice.setGoodsid(localObject.getGoodsid());
					localReqStrGetGoodsPrice.setUnitid(localObject.getUnitid());
					localReqStrGetGoodsPrice.setPrice(0.0D);
					localReqStrGetGoodsPrice.setIsdiscount(false);
					localArrayList.add(localReqStrGetGoodsPrice);
					String result = new ServiceGoods().gds_GetMultiGoodsPriceDB(localArrayList, doc.getInwarehouseid(),
							true, localObject.isIsusebatch());
					handlerGet.sendMessage(handlerGet.obtainMessage(2, result));

				}

			});

		}

	};
	boolean ishaschanged;
	private SwipeMenuListView listView;
	private TransferItemAdapter adapter;
	private AutoTextView atvSearch;
	private SearchHelper searchHelper;
	private DocContainerEntity doccontainer;
	private List<DefDocItem> newListItem;

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
		if (!TextUtils.isEmptyS(this.doc.getInwarehouseid())) {
			return "调入仓库不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getPrinttemplate())) {
			return "打印模板不能为空";
		}
		for (int i = 0; i < listItem.size(); i++) {
			DefDocItem docItem = listItem.get(i);
			if (docItem.getNum() == 0.0D) {
				return "【" + docItem.getGoodsname() + "】数量为0";
			}
			if (!TextUtils.isEmptyS(docItem.getWarehouseid())) {
				return "【" + docItem.getGoodsname() + "】没有选择仓库";
			}
			if (!TextUtils.isEmptyS(docItem.getUnitid())) {
				return "【" + docItem.getGoodsname() + "】没有选择单位";
			}
			if (docItem.getWarehouseid().equals(this.doc.getInwarehouseid())) {
				return "【" + docItem.getGoodsname() + "】调出仓库与调入仓库相同";
			}
			if (!TextUtils.isEmptyS(docItem.getBatch()) && docItem.isIsusebatch()) {
				return "【" + docItem.getGoodsname() + "】没有选择批次";
			}
		}
		return null;
	}

	protected DefDocItem fillItem(GoodsThin goodsthin, double num, double price) {
		GoodsUnitDAO goodsDao = new GoodsUnitDAO();
		DefDocItem item = new DefDocItem();
		item.setItemid(0L);
		item.setDocid(this.doc.getDocid());
		item.setGoodsid(goodsthin.getId());
		item.setGoodsname(goodsthin.getName());
		item.setBarcode(goodsthin.getBarcode());
		item.setSpecification(goodsthin.getSpecification());
		item.setModel(goodsthin.getModel());
		item.setWarehouseid(this.doc.getOutwarehouseid());
		item.setWarehousename(this.doc.getOutwarehousename());
		GoodsUnit goodsunit = null;
		if (Utils.DEFAULT_TransferDocUNIT == 0) {
			goodsunit = goodsDao.queryBaseUnit(goodsthin.getId());
		} else {
			goodsunit = goodsDao.queryBigUnit(goodsthin.getId());
		}
		item.setUnitid(goodsunit.getUnitid());
		item.setUnitname(goodsunit.getUnitname());
		item.setNum(Utils.normalize(num, 2));
		item.setBignum(goodsDao.getBigNum(item.getGoodsid(), item.getUnitid(), item.getNum()));
		item.setPrice(Utils.normalizePrice(price));
		item.setSubtotal(Utils.normalizeSubtotal(item.getNum() * item.getPrice()));
		item.setDiscountratio(1.0D);
		item.setDiscountprice(0.0D);
		item.setDiscountsubtotal(0.0D);
		item.setIsgift(false);
		item.setCostprice(0.0D);
		item.setRemark("");
		item.setRversion(0L);
		item.setIsdiscount(false);
		item.setIsusebatch(goodsthin.isIsusebatch());
		return item;
	}

	public void check(final boolean isPrint) {
		String validateDoc = validateDoc();
		if (!validateDoc.isEmpty()) {
			InfoDialog.showError(this, validateDoc);
			return;
		}
		if ((this.listItem == null) || (this.listItem.size() == 0)) {
			InfoDialog.showError(this, "空单不能过账");
			return;
		}
		final MAlertDialog localObject = new MAlertDialog(this);
		localObject.setMessage("过账后的单据不能修改。\n确定过账？");
		localObject.setCancelListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				localObject.dismiss();
				PDH.show(TransferEditActivity.this, new PDH.ProgressCallBack() {
					public void action() {
						String str = serviceStore.str_CheckDBDoc(doc, listItem, listItemDelete, isPrint);
						handler.sendMessage(handler.obtainMessage(1, str));
					}
				});
			}
		});
		localObject.show();
	}

	public void docProperty() {
		Intent localIntent = new Intent(this, TransferDocOpenActivity.class);
		localIntent.putExtra("doc", this.doc);
		startActivityForResult(localIntent, 0);
	}

	public void delete() {
		if (this.doc.getDocid() > 0L) {
			final MAlertDialog localMAlertDialog = new MAlertDialog(this);
			localMAlertDialog.setMessage("确定删除？");
			localMAlertDialog.setCancelListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					localMAlertDialog.dismiss();
					PDH.show(TransferEditActivity.this, new PDH.ProgressCallBack() {

						@Override
						public void action() {
							String str = serviceStore.str_DeleteDoc(doc.getDoctypeid(), doc.getDocid());
							handler.sendMessage(handler.obtainMessage(2, str));
						}

					});
				}
			});
			localMAlertDialog.show();
			return;
		}
		finish();
	}

	public void save() {
		String str = validateDoc();
		if (str != null) {
			InfoDialog.showError(this, str);
			return;
		}
		String result = serviceStore.str_SaveDBDoc(doc, listItem, listItemDelete);
		handler.sendMessage(handler.obtainMessage(0, result));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "单击显示菜单").setIcon(getResources().getDrawable(R.drawable.btn_submenu)).setShowAsAction(1);
		return super.onCreateOptionsMenu(menu);
	}

	TransferEditMenuPopup menuPopup;

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		if (menu.getItemId() == 0) {
			LayoutParams attributes = getWindow().getAttributes();
			attributes.alpha = 0.8f;
			getWindow().setAttributes(attributes);
			if (menuPopup == null) {
				menuPopup = new TransferEditMenuPopup(this);
			}
			menuPopup.showAtLocation(listView, 80, 0, 0);
		}
		if (menu.getItemId() == android.R.id.home) {
			final MAlertDialog localMAlertDialog = new MAlertDialog(this);
			localMAlertDialog.setMessage("是否保存当前单据？");
			localMAlertDialog.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					localMAlertDialog.dismiss();
					save();
				}
			});
			localMAlertDialog.setComfirmListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					localMAlertDialog.dismiss();
					finish();
				}
			});
			localMAlertDialog.show();
		}
		return true;
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			LayoutParams attributes = getWindow().getAttributes();
			attributes.alpha = 1.0F;
			getWindow().setAttributes(attributes);
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String localObject = msg.obj.toString();
			if (RequestHelper.isSuccess(localObject)) {
				if (msg.what == 2) {
					PDH.showSuccess("删除成功");
					setResult(Activity.RESULT_OK);
					finish();
					return;
				}
				DocContainerEntity doccontainer = (DocContainerEntity) JSONUtil.readValue(localObject,
						DocContainerEntity.class);
				doc = ((DefDocTransfer) JSONUtil.readValue(doccontainer.getDoc(), DefDocTransfer.class));
				listItem = JSONUtil.str2list(doccontainer.getItem(), DefDocItem.class);
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
						PDH.showSuccess("过账成功");
						setResult(Activity.RESULT_OK);
						finish();
						return;
					}
					adapter.setData(TransferEditActivity.this.listItem);
					refreshUI();
					InfoDialog.showError(TransferEditActivity.this, doccontainer.getInfo());
					break;

				default:
					break;
				}
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 0:
				this.doc = (DefDocTransfer) data.getSerializableExtra("doc");
				break;
			case 2:
				newListItem = JSONUtil.str2list(data.getStringExtra("items"), DefDocItem.class);
				ArrayList<ReqStrGetGoodsPrice> listPrice = new ArrayList<ReqStrGetGoodsPrice>();
				for (int i = 0; i < newListItem.size(); i++) {
					DefDocItem localDefDocItem = (DefDocItem) this.newListItem.get(i);
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new ReqStrGetGoodsPrice();
					localReqStrGetGoodsPrice.setType(0);
					localReqStrGetGoodsPrice.setCustomerid(null);
					localReqStrGetGoodsPrice.setWarehouseid(localDefDocItem.getWarehouseid());
					localReqStrGetGoodsPrice.setGoodsid(localDefDocItem.getGoodsid());
					localReqStrGetGoodsPrice.setUnitid(localDefDocItem.getUnitid());
					localReqStrGetGoodsPrice.setPrice(0.0D);
					localReqStrGetGoodsPrice.setIsdiscount(false);
					listPrice.add(localReqStrGetGoodsPrice);
				}
				String priceDB = new ServiceGoods().gds_GetMultiGoodsPriceDB(listPrice, this.doc.getInwarehouseid(),
						true, true);
				this.handlerGet.sendMessage(this.handlerGet.obtainMessage(0, priceDB));

				break;
			case 3:

				int position = data.getIntExtra("position", 0);
				DefDocItem item = (DefDocItem) data.getSerializableExtra("docitem");
				this.listItem.set(position, item);
				this.adapter.setData(this.listItem);
				refreshUI();
				break;

			default:
				break;
			}
		}
	}

	private Handler handlerGet = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String message = msg.obj.toString();
			if (RequestHelper.isSuccess(message)) {
				List<ReqStrGetGoodsPrice> listPrice = JSONUtil.str2list(message, ReqStrGetGoodsPrice.class);
				if (msg.what == 0) {
					GoodsUnitDAO dao = new GoodsUnitDAO();
					for (int i = 0; i < listPrice.size(); i++) {
						DefDocItem item = newListItem.get(i);
						ReqStrGetGoodsPrice reqPrice = listPrice.get(i);
						if ((!item.getGoodsid().equals(reqPrice.getGoodsid()))
								|| (!item.getUnitid().equals(reqPrice.getUnitid()))) {
							break;
						}
						String warehouseid = reqPrice.getWarehouseid();
						if ((TextUtils.isEmptyS(warehouseid)) && (!(warehouseid).equals(item.getWarehouseid()))) {
							item.setWarehouseid(warehouseid);
							Warehouse warehouse = new WarehouseDAO().getWarehouse(warehouseid);
							if (warehouse != null) {
								item.setWarehousename(warehouse.getName());
							}
						}
						item.setBignum(dao.getBigNum(item.getGoodsid(), item.getUnitid(), item.getNum()));
						item.setBatch(reqPrice.getBatch());
						item.setProductiondate(reqPrice.getProductiondate());
						item.setPrice(Utils.normalizePrice(reqPrice.getPrice()));
						item.setSubtotal(Utils.normalizeSubtotal(item.getNum() * item.getPrice()));
						if (item.isIsusebatch()) {
							item.setBatch(reqPrice.getBatch());
							item.setProductiondate(reqPrice.getProductiondate());
							item.setWarehouseid(reqPrice.getWarehouseid());
							if (TextUtils.isEmptyS(reqPrice.getWarehouseid())) {
								Warehouse warehouse = new WarehouseDAO().getWarehouse(reqPrice.getWarehouseid());
								if (warehouse != null) {
									item.setWarehousename(warehouse.getName());
								}
							}
						}
						item.setIsdiscount(reqPrice.getIsdiscount());

					}
					listItem.addAll(newListItem);
					adapter.setData(listItem);
					refreshUI();
					ishaschanged = true;
					setActionBarText();

				}
				if (msg.what == 2) {
					DefDocItem docItem = newListItem.get(0);
					ReqStrGetGoodsPrice localObject1 = listPrice.get(0);
					String localObject2 = localObject1.getWarehouseid();
					if (localObject2 != null) {
						docItem.setWarehouseid(localObject2);
					}
					docItem.setPrice(Utils.normalizePrice(((ReqStrGetGoodsPrice) localObject1).getPrice()));
					docItem.setSubtotal(Utils.normalizeSubtotal(docItem.getNum() * docItem.getPrice()));
					docItem.setIsdiscount(((ReqStrGetGoodsPrice) localObject1).getIsdiscount());
					docItem.setBatch(((ReqStrGetGoodsPrice) localObject1).getBatch());
					docItem.setProductiondate(((ReqStrGetGoodsPrice) localObject1).getProductiondate());
					Intent intent = new Intent(TransferEditActivity.this, TransferAddGoodAct.class);
					intent.putExtra("docitem", docItem);
					startActivityForResult(intent, 1);

				}

			} else {
				PDH.showFail(message);
			}

		};
	};
	private Scaner factory;

	@Override
	public void setActionBarText() {
		if (this.ishaschanged) {
			setTitle(doc.getShowid() + "*");
		} else {
			setTitle(doc.getShowid());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			LayoutParams attributes = getWindow().getAttributes();
			attributes.alpha = 1.0F;
			getWindow().setAttributes(attributes);
		}
		return false;
	}

	public DefDocTransfer getDoc() {
		return doc;
	}

	@Override
	public void setBarcode(String barcode) {
		readBarcode(barcode);
	}

	// TODO
	private void readBarcode(String barcode) {
		atvSearch.setText("");
		final ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		if (goodsThinList.size() == 1) {
			PDH.show(TransferEditActivity.this, new PDH.ProgressCallBack() {

				@Override
				public void action() {
					DefDocItem localObject = fillItem(goodsThinList.get(0), 0.0D, 0.0D);
					newListItem = new ArrayList<DefDocItem>();
					newListItem.add(localObject);
					ArrayList<ReqStrGetGoodsPrice> localArrayList = new ArrayList<ReqStrGetGoodsPrice>();
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new ReqStrGetGoodsPrice();
					localReqStrGetGoodsPrice.setType(0);
					localReqStrGetGoodsPrice.setCustomerid(null);
					localReqStrGetGoodsPrice.setWarehouseid(localObject.getWarehouseid());
					localReqStrGetGoodsPrice.setGoodsid(localObject.getGoodsid());
					localReqStrGetGoodsPrice.setUnitid(localObject.getUnitid());
					localReqStrGetGoodsPrice.setPrice(0.0D);
					localReqStrGetGoodsPrice.setIsdiscount(false);
					localArrayList.add(localReqStrGetGoodsPrice);
					String result = new ServiceGoods().gds_GetMultiGoodsPriceDB(localArrayList, doc.getInwarehouseid(),
							true, localObject.isIsusebatch());
					handlerGet.sendMessage(handlerGet.obtainMessage(2, result));

				}

			});
		} else {
			atvSearch.setText(barcode);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		factory.removeListener();
	}

}
