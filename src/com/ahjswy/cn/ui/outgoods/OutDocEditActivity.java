package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.bean.Def_DocDraft;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.dao.WarehouseDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocPayType;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.FieldSaleForPrint;
import com.ahjswy.cn.model.FieldSaleItemForPrint;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Warehouse;
import com.ahjswy.cn.popupmenu.OutDocEditMenuPopup;
import com.ahjswy.cn.print.BTPrintHelper;
import com.ahjswy.cn.print.BTPrintHelper.PrintOverCall;
import com.ahjswy.cn.print.PrintMode;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespPromotionRule;
import com.ahjswy.cn.response.RespServiceInfor;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.MAlertDialog;
import com.ahjswy.cn.ui.SearchHelper;
import com.ahjswy.cn.ui.SwyMain;
import com.ahjswy.cn.utils.InfoDialog;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.PDH.ProgressCallBack;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.AutoTextView;
import com.ahjswy.cn.views.Dialog_listCheckBox;
import com.ahjswy.cn.views.Dialog_message;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import android.app.ActionBar;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class OutDocEditActivity extends BaseActivity implements OnItemClickListener, OnClickListener, OnTouchListener {
	private ServiceStore serviceStore;
	private List<DefDocItemXS> listItem;
	private List<DefDocPayType> listPayType;
	private boolean ishaschanged;
	// 添加
	private Button btnAdd;
	public DefDocXS doc;
	private SwipeMenuListView listview_copy_dele;
	private List<Long> listItemDelete;
	private OutDocItemAdapter adapter;
	private LinearLayout linearSearch;
	private SearchHelper searchHelper;
	private AutoTextView atvSearch;
	private OutDocEditMenuPopup menuPopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_out_doc_edit_activity);
		intView();
		refreshUI();
		if (!(doc.isIsavailable() && doc.isIsposted())) {
			addListener();
			intDate();
		}
	}

	private void intView() {
		// 输入匹配 listview
		linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
		searchHelper = new SearchHelper(this, linearSearch);
		atvSearch = ((AutoTextView) findViewById(R.id.atvSearch));
		atvSearch.setOnItemClickListener(onItemClickListeners);
		// 加载 头 布局
		// 加载查找 添加
		btnAdd = (Button) findViewById(R.id.btnAdd);
		listview_copy_dele = (SwipeMenuListView) findViewById(R.id.listView_addShop);
		listview_copy_dele.setOnTouchListener(this);
		// 底部 总数量
		bt_sumNumber = (Button) findViewById(R.id.bt_sumNumber);
		// 底部总价格
		bt_totalSum = (Button) findViewById(R.id.bt_totalSum);
		btnGoodClass = (Button) findViewById(R.id.btn_goodClass);
		serviceStore = new ServiceStore();
		ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		DocContainerEntity docContainerEntity = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		doc = ((DefDocXS) JSONUtil.readValue(docContainerEntity.getDoc(), DefDocXS.class));
		listItem = JSONUtil.str2list(docContainerEntity.getItem(), DefDocItemXS.class);
		listPayType = JSONUtil.str2list(docContainerEntity.getPaytype(), DefDocPayType.class);
		adapter = new OutDocItemAdapter(this);
		adapter.setData(listItem);
		listview_copy_dele.setAdapter(adapter);
		listItemDelete = new ArrayList<Long>();
		btnAdd.setOnClickListener(this);
		listview_copy_dele.setOnItemClickListener(this);
		dialog = new Dialog_listCheckBox(OutDocEditActivity.this);
	}

	private void intDate() {
		SwipeMenuCreator local5 = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem localSwipeMenuItem1 = new SwipeMenuItem(OutDocEditActivity.this.getApplicationContext());
				localSwipeMenuItem1.setTitle("复制");
				localSwipeMenuItem1.setTitleSize(14);
				localSwipeMenuItem1.setTitleColor(-16777216);
				localSwipeMenuItem1.setWidth(100);
				localSwipeMenuItem1.setBackground(new ColorDrawable(Color.rgb(48, 177, 245)));
				menu.addMenuItem(localSwipeMenuItem1);
				SwipeMenuItem localSwipeMenuItem2 = new SwipeMenuItem(OutDocEditActivity.this.getApplicationContext());
				localSwipeMenuItem2.setTitle("删除");
				localSwipeMenuItem2.setTitleSize(14);
				localSwipeMenuItem2.setTitleColor(-16777216);
				localSwipeMenuItem2.setBackground(new ColorDrawable(Color.rgb(201, 201, 206)));
				localSwipeMenuItem2.setWidth(100);
				menu.addMenuItem(localSwipeMenuItem2);
			}
		};
		listview_copy_dele.setMenuCreator(local5);
		listview_copy_dele.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					DefDocItemXS defdocitemxs = new DefDocItemXS(listItem.get(position));
					defdocitemxs.setItemid(0L);
					defdocitemxs.setTempitemid(1L + getMaxTempItemId());
					listItem.add(defdocitemxs);
					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							bottomCount();
						}
					}, 180L);
					break;
				case 1:
					// 删除
					DefDocItemXS itemdel = listItem.get(position);
					if (itemdel.getItemid() > 0L) {
						listItemDelete.add(Long.valueOf(itemdel.getItemid()));
					}
					listItem.remove(position);
					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							bottomCount();
						}
					}, 180L);
					break;
				}
				return false;
			}
		});
	}

	private void addListener() {
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
		// BarcodeConfig barcodeConfig = new BarcodeConfig(this);
		// // 设置条码输出模式 不显示模式(复制到粘贴板)
		// barcodeConfig.setOutputMode(2);
		// if (bm == null) {
		// bm = new BarcodeManager(this);
		// }
		// bm.addListener(bl);
	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			atvSearch.setText("");
			if (dialog != null) {
				dialog.dismiss();
			}
			readBarcode(barcode);
		}
	};
	// private BarcodeListener bl = new BarcodeListener() {
	//
	// @Override
	// public void barcodeEvent(BarcodeEvent event) {
	// if (event.getOrder().equals("SCANNER_READ")) {
	// atvSearch.setText("");
	// if (dialog != null) {
	// dialog.dismiss();
	// }
	// readBarcode(bm.getBarcode().toString().trim());
	// }
	// }
	// };

	private void readBarcode(String barcode) {
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
		if (goodsThinList.size() == 1) {
			long l = getMaxTempItemId();
			DefDocItemXS fillItem = fillItem(goodsThinList.get(0), 0.0D, 0.0D, l + 1L);
			localArrayList.add(fillItem);
			Intent intent = new Intent(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class);
			intent.putExtra("items", JSONUtil.object2Json(localArrayList));
			intent.putExtra("doc", doc);
			startActivityForResult(intent, 1);
		} else if (goodsThinList.size() > 1) {
			dialog.setGoods(goodsThinList);
			dialog.setTempGoods(goodsThinList);
			dialog.ShowMe();
			dialog.ensure(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					List<GoodsThin> select = dialog.getSelect();
					long l = getMaxTempItemId();
					for (int i = 0; i < select.size(); i++) {
						l += 1L;
						DefDocItemXS fillItem = fillItem(select.get(i), 0.0D, 0.0D, l);
						localArrayList.add(fillItem);
					}
					// deleBm();
					Intent intent = new Intent(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class);
					intent.putExtra("items", JSONUtil.object2Json(localArrayList));
					intent.putExtra("doc", doc);
					startActivityForResult(intent, 1);
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
	}

	// 获取最大的 TempItemId
	public long getMaxTempItemId() {
		long l1 = 0L;
		if (listItem.size() <= 0) {
			return l1;
		}
		for (int i = 0; i < listItem.size(); i++) {
			long l2 = listItem.get(i).getTempitemid();
			if (l2 > l1) {
				l1 = l2;
			}
		}
		return l1;
	}

	@Override
	protected void onPause() {
		super.onPause();
		scaner.removeListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			atvSearch.clearFocus();
			if (atvSearch.getText().toString().length() > 0) {
				addMoreListener();
			} else {
				PDH.showMessage("请选择商品！");
			}
			break;
		}
	}

	// public void deleBm() {
	// if (bm != null) {
	// bm.removeListener(new BarcodeListener() {
	//
	// @Override
	// public void barcodeEvent(BarcodeEvent arg0) {
	// }
	// });
	// bm.dismiss();
	// bm = null;
	// }
	// }

	public void closeInputMethod() {
		// 获取当前输入法状态
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		// true 显示
		if (isOpen) {
			// 关闭输入法
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void refreshUI() {
		if (doc.isIsavailable() && doc.isIsposted()) {
			findViewById(R.id.linearSearch).setVisibility(View.GONE);
			findViewById(R.id.top).setVisibility(View.GONE);
			findViewById(R.id.linNumber).setVisibility(View.GONE);
			listview_copy_dele.setItemSwipe(false);
		} else {
			findViewById(R.id.linearSearch).setVisibility(View.VISIBLE);
			findViewById(R.id.top).setVisibility(View.VISIBLE);
			findViewById(R.id.linNumber).setVisibility(View.VISIBLE);
			listview_copy_dele.setItemSwipe(true);
		}
	}

	/**
	 * main ontouch item
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DefDocItemXS defdocitemxs = (DefDocItemXS) parent.getItemAtPosition(position);
		atvSearch.setText("");
		if ((doc.isIsavailable()) && (doc.isIsposted())) {
			return;
		}
		// deleBm();
		int i = -1;
		Intent localIntent = new Intent();
		localIntent.putExtra("customerid", doc.getCustomerid());
		localIntent.putExtra("positiongive", i);
		localIntent.putExtra("docitemgive", "");
		localIntent.putExtra("position", position);
		localIntent.putExtra("docitem", defdocitemxs);
		startActivityForResult(localIntent.setClass(this, OutDocAddGoodAct.class), 2);
	}

	public DefDocXS getDoc() {
		return this.doc;
	}

	Handler handlerGet = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String localString1 = msg.obj.toString();
			List<ReqStrGetGoodsPrice> localList = null;
			GoodsUnitDAO localGoodsUnitDAO;
			if (RequestHelper.isSuccess(localString1)) {
				if (msg.what == 0) {
					localList = JSONUtil.str2list(localString1, ReqStrGetGoodsPrice.class);
					if (newListItem != null) {
						if ((msg.what != 0) && (msg.what != 1)) {
							return;
						}
						localGoodsUnitDAO = new GoodsUnitDAO();
						for (int i = 0; i < newListItem.size(); i++) {
							DefDocItemXS localDefDocItemXS1 = (DefDocItemXS) newListItem.get(i);
							for (int j = 0; j < localList.size(); j++) {
								ReqStrGetGoodsPrice localReqStrGetGoodsPrice1 = (ReqStrGetGoodsPrice) localList.get(j);
								if ((localDefDocItemXS1.getGoodsid().equals(localReqStrGetGoodsPrice1.getGoodsid()))
										&& (localDefDocItemXS1.getUnitid()
												.equals(localReqStrGetGoodsPrice1.getUnitid()))) {
									String localString2 = localReqStrGetGoodsPrice1.getWarehouseid();
									if ((TextUtils.isEmptyS(localString2))
											&& (localString2.equals(localDefDocItemXS1.getWarehouseid()))) {
										localDefDocItemXS1.setWarehouseid(localString2);
										localDefDocItemXS1.setWarehousename(
												new WarehouseDAO().getWarehouse(localString2).getName());
									}
									localDefDocItemXS1.setIsdiscount(localReqStrGetGoodsPrice1.getIsdiscount());
									localDefDocItemXS1.setPrice(Utils.normalizePrice(
											Utils.normalizePrice(localReqStrGetGoodsPrice1.getPrice())));
									localDefDocItemXS1.setSubtotal(Utils.normalizePrice(
											localDefDocItemXS1.getNum() * localDefDocItemXS1.getPrice()));
									if (localDefDocItemXS1.isIsdiscount()) {
										localDefDocItemXS1.setDiscountratio(doc.getDiscountratio());
										if (localDefDocItemXS1.isIsusebatch()) {
											localDefDocItemXS1.setBatch(localReqStrGetGoodsPrice1.getBatch());
											localDefDocItemXS1
													.setProductiondate(localReqStrGetGoodsPrice1.getProductiondate());
											localDefDocItemXS1
													.setWarehouseid(localReqStrGetGoodsPrice1.getWarehouseid());
											if (!TextUtils.isEmptyS(localReqStrGetGoodsPrice1.getWarehouseid())) {
												Warehouse localWarehouse = new WarehouseDAO()
														.getWarehouse(localReqStrGetGoodsPrice1.getWarehouseid());
												if (localWarehouse != null) {
													localDefDocItemXS1.setWarehousename(localWarehouse.getName());
												}

											}
										}
									}
									localDefDocItemXS1.setDiscountprice(Utils.normalizePrice(
											localDefDocItemXS1.getPrice() * localDefDocItemXS1.getDiscountratio()));
									localDefDocItemXS1.setDiscountsubtotal(Utils.normalizeSubtotal(
											localDefDocItemXS1.getNum() * localDefDocItemXS1.getDiscountprice()));
									boolean isgift = false;
									if (localDefDocItemXS1.getPrice() == 0.0D) {
										isgift = true;
									}
									localDefDocItemXS1.setIsgift(isgift);
									String bigNum = localGoodsUnitDAO.getBigNum(localDefDocItemXS1.getGoodsid(),
											localDefDocItemXS1.getUnitid(), localDefDocItemXS1.getNum());
									localDefDocItemXS1.setBignum(bigNum);
									localDefDocItemXS1.setIsdiscount(localReqStrGetGoodsPrice1.getIsdiscount());

								}
							}

						}
					}
					if (newListItem.size() > 0) {
						listItem.addAll(0, newListItem);
						adapter.setData(listItem);
						listview_copy_dele.setAdapter(adapter);
						addListener();
						ishaschanged = true;
						setActionBarText();
						bottomCount();
						refreshUI();
					}
					return;
				}
				if (msg.what == 2) {
					DefDocItemXS localDefDocItemXS2 = (DefDocItemXS) OutDocEditActivity.this.newListItem.get(0);
					localList = JSONUtil.str2list(localString1, ReqStrGetGoodsPrice.class);
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice2 = (ReqStrGetGoodsPrice) localList.get(0);
					String localString3 = localReqStrGetGoodsPrice2.getWarehouseid();
					if ((TextUtils.isEmptyS(localString3))
							&& (localString3.equals(localDefDocItemXS2.getWarehouseid()))) {
						localDefDocItemXS2.setWarehouseid(localString3);
						localDefDocItemXS2.setWarehousename(new WarehouseDAO().getWarehouse(localString3).getName());
					}
					localDefDocItemXS2.setIsdiscount(localReqStrGetGoodsPrice2.getIsdiscount());
					localDefDocItemXS2.setPrice(Utils.normalizePrice(localReqStrGetGoodsPrice2.getPrice()));
					localDefDocItemXS2.setSubtotal(
							Utils.normalizeSubtotal(localDefDocItemXS2.getNum() * localDefDocItemXS2.getPrice()));
					localDefDocItemXS2.setBatch(localReqStrGetGoodsPrice2.getBatch());
					localDefDocItemXS2.setProductiondate(localReqStrGetGoodsPrice2.getProductiondate());

					localDefDocItemXS2.setDiscountratio(OutDocEditActivity.this.doc.getDiscountratio());
					localDefDocItemXS2.setDiscountprice(Utils
							.normalizePrice(localDefDocItemXS2.getPrice() * localDefDocItemXS2.getDiscountratio()));
					localDefDocItemXS2.setDiscountsubtotal(Utils
							.normalizeSubtotal(localDefDocItemXS2.getNum() * localDefDocItemXS2.getDiscountprice()));
					if (localDefDocItemXS2.isIsdiscount()) {
						localDefDocItemXS2.setIsdiscount(localReqStrGetGoodsPrice2.getIsdiscount());
					}
					DefDocItemXS localDefDocItemXS3 = new DefDocItemXS(localDefDocItemXS2);
					if (localDefDocItemXS2.getPrice() == 0.0D) {
						localDefDocItemXS2.setIsgift(true);
						localDefDocItemXS3.setTempitemid(1L + localDefDocItemXS2.getTempitemid());
						localDefDocItemXS3.setNum(0.0D);
						localDefDocItemXS3.setSubtotal(0.0D);
						localDefDocItemXS3.setDiscountratio(1.0D);
						localDefDocItemXS3.setDiscountprice(0.0D);
						localDefDocItemXS3.setDiscountsubtotal(0.0D);
					}
					if (localDefDocItemXS2.isIsusebatch()) {
						localDefDocItemXS3.setBatch(localReqStrGetGoodsPrice2.getBatch());
						localDefDocItemXS3.setProductiondate(localReqStrGetGoodsPrice2.getProductiondate());
					}
					// deleBm();
					Intent localIntent = new Intent();
					localIntent.putExtra("customerid", doc.getCustomerid());
					localIntent.putExtra("position", 0);
					localIntent.putExtra("docitem", localDefDocItemXS2);
					// localIntent.putExtra("positiongive", -1);
					// localIntent.putExtra("docitemgive", localDefDocItemXS3);
					startActivityForResult(localIntent.setClass(OutDocEditActivity.this, OutDocAddGoodAct.class), 4);
					return;
				}
			}
			PDH.showFail(localString1);
		};

	};
	ArrayList<ReqStrGetGoodsPrice> localArrayList;
	private AdapterView.OnItemClickListener onItemClickListeners = new AdapterView.OnItemClickListener() {
		@SuppressWarnings("rawtypes")
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			if ((menuPopup != null) && (menuPopup.isShowing())) {
				menuPopup.dismiss();
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.alpha = 1.0F;
				getWindow().setAttributes(localLayoutParams);
			}

			final GoodsThin localGoodsThin = searchHelper.getAdapter().getTempGoods().get(position);
			atvSearch.setText("");
			PDH.show(OutDocEditActivity.this, new PDH.ProgressCallBack() {

				public void action() {
					long l = getMaxTempItemId();
					DefDocItemXS localDefDocItemXS = fillItem(localGoodsThin, 0.0D, 0.0D, l + 1L);
					newListItem = new ArrayList<DefDocItemXS>();
					newListItem.add(localDefDocItemXS);
					localArrayList = new ArrayList<ReqStrGetGoodsPrice>();
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new ReqStrGetGoodsPrice();
					localReqStrGetGoodsPrice.setType(1);
					localReqStrGetGoodsPrice.setCustomerid(OutDocEditActivity.this.doc.getCustomerid());
					localReqStrGetGoodsPrice.setWarehouseid(localDefDocItemXS.getWarehouseid());
					localReqStrGetGoodsPrice.setGoodsid(localDefDocItemXS.getGoodsid());
					localReqStrGetGoodsPrice.setUnitid(localDefDocItemXS.getUnitid());
					localReqStrGetGoodsPrice.setPrice(0.0D);
					localReqStrGetGoodsPrice.setIsdiscount(false);
					localArrayList.add(localReqStrGetGoodsPrice);
					String localString = new ServiceGoods().gds_GetMultiGoodsPrice(localArrayList, true,
							localDefDocItemXS.isIsusebatch());
					handlerGet.sendMessage(handlerGet.obtainMessage(2, localString));
				}
			});

		}

	};

	public void getGoodsPrice(final List<ReqStrGetGoodsPrice> re) {

		PDH.show(this, new ProgressCallBack() {

			@Override
			public void action() {
				String localString = new ServiceGoods().gds_GetMultiGoodsPrice(re, true, true);
				handlerGet.sendMessage(OutDocEditActivity.this.handlerGet.obtainMessage(1, localString));
			}
		});
	}

	private void addMoreListener() {
		if ((menuPopup != null) && (menuPopup.isShowing())) {
			menuPopup.dismiss();
			WindowManager.LayoutParams localLayoutParams = OutDocEditActivity.this.getWindow().getAttributes();
			localLayoutParams.alpha = 1.0F;
			OutDocEditActivity.this.getWindow().setAttributes(localLayoutParams);
		}
		List<GoodsThin> localList = searchHelper.getAdapter().getSelect();
		if ((localList == null) || (localList.size() == 0)) {
			return;
		}
		atvSearch.setText("");
		if (!"1".equals(new AccountPreference().getValue("goods_select_more"))) {
			ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
			long l = getMaxTempItemId();
			for (int i = 0; i < localList.size(); i++) {
				GoodsThin localGoodsThin = (GoodsThin) localList.get(i);
				l += 1L;
				DefDocItemXS item = fillItem(localGoodsThin, 0.0D, 0.0D, l);
				if (item != null) {
					localArrayList.add(item);
				}

			}
			startActivityForResult(new Intent().setClass(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class)
					.putExtra("items", JSONUtil.object2Json(localArrayList)).putExtra("doc", doc), 1);
		}

	}

	DefDocItemXS fillItem(GoodsThin paramGoodsThin, double paramDouble1, double paramDouble2, long paramLong) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemXS itemxs = new DefDocItemXS();
		itemxs.setItemid(0L);
		itemxs.setTempitemid(paramLong);
		itemxs.setDocid(this.doc.getDocid());
		itemxs.setGoodsid(paramGoodsThin.getId());
		itemxs.setGoodsname(paramGoodsThin.getName());
		itemxs.setBarcode(paramGoodsThin.getBarcode());
		itemxs.setSpecification(paramGoodsThin.getSpecification());
		itemxs.setModel(paramGoodsThin.getModel());
		itemxs.setWarehouseid(this.doc.getWarehouseid());
		itemxs.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		if (localGoodsUnit == null) {
			return null;
		}
		itemxs.setUnitid(localGoodsUnit.getUnitid());
		itemxs.setUnitname(localGoodsUnit.getUnitname());
		itemxs.setNum(Utils.normalize(paramDouble1, 2));
		itemxs.setBignum(localGoodsUnitDAO.getBigNum(itemxs.getGoodsid(), itemxs.getUnitid(), itemxs.getNum()));
		// 价格
		itemxs.setPrice(Utils.normalizePrice(paramDouble2));
		// 小计
		itemxs.setSubtotal(Utils.normalizeSubtotal(itemxs.getNum() * itemxs.getPrice()));
		// 折扣率
		itemxs.setDiscountratio(this.doc.getDiscountratio());
		// 折扣价格
		itemxs.setDiscountprice(Utils.normalizePrice(itemxs.getPrice() * this.doc.getDiscountratio()));
		// 折扣小计
		itemxs.setDiscountsubtotal(itemxs.getNum() * itemxs.getDiscountprice());
		if (itemxs.getPrice() == 0.0D) {
			itemxs.setIsgift(true);
			itemxs.setCostprice(0.0D);
			itemxs.setRemark("");
			itemxs.setRversion(0L);
			itemxs.setIsdiscount(false);
			itemxs.setIsexhibition(false);
			itemxs.setIspromotion(false);
			itemxs.setParentitemid(0L);
			itemxs.setPromotiontype(-1);
			itemxs.setPromotiontypename(null);
			itemxs.setOutorderdocid(0L);
			itemxs.setOutorderdocshowid(null);
			itemxs.setOutorderitemid(0L);
		}
		itemxs.setIsusebatch(paramGoodsThin.isIsusebatch());
		return itemxs;
	}

	@SuppressWarnings("unused")
	private DefDocItemXS fillItem(RespPromotionRule paramRespPromotionRule, double paramDouble, long paramLong) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemXS localDefDocItemXS = new DefDocItemXS();
		localDefDocItemXS.setItemid(0L);
		localDefDocItemXS.setTempitemid(paramLong);
		localDefDocItemXS.setDocid(this.doc.getDocid());
		localDefDocItemXS.setGoodsid(paramRespPromotionRule.getGoodsid());
		localDefDocItemXS.setGoodsname(paramRespPromotionRule.getGoodsname());
		localDefDocItemXS.setBarcode(paramRespPromotionRule.getBarcode());
		localDefDocItemXS.setSpecification(paramRespPromotionRule.getSpecification());
		localDefDocItemXS.setModel(paramRespPromotionRule.getModel());
		localDefDocItemXS.setWarehouseid(this.doc.getWarehouseid());
		localDefDocItemXS.setWarehousename(this.doc.getWarehousename());
		localDefDocItemXS.setUnitid(paramRespPromotionRule.getUnitid());
		localDefDocItemXS.setUnitname(paramRespPromotionRule.getUnitname());
		localDefDocItemXS.setNum(Utils.normalize(paramDouble, 2));
		localDefDocItemXS.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItemXS.getGoodsid(),
				localDefDocItemXS.getUnitid(), localDefDocItemXS.getNum()));
		localDefDocItemXS.setPrice(Utils.normalizePrice(paramRespPromotionRule.getPrice()));
		localDefDocItemXS
				.setSubtotal(Utils.normalizeSubtotal(localDefDocItemXS.getNum() * localDefDocItemXS.getPrice()));
		localDefDocItemXS.setDiscountratio(1.0D);
		localDefDocItemXS.setDiscountprice(localDefDocItemXS.getPrice());
		localDefDocItemXS.setDiscountsubtotal(localDefDocItemXS.getSubtotal());
		if (localDefDocItemXS.getPrice() == 0.0D) {
			localDefDocItemXS.setIsgift(true);
			localDefDocItemXS.setCostprice(0.0D);
			localDefDocItemXS.setRemark("");
			localDefDocItemXS.setRversion(0L);
			localDefDocItemXS.setIsdiscount(false);
			localDefDocItemXS.setIsexhibition(false);
			localDefDocItemXS.setIspromotion(true);
			localDefDocItemXS.setParentitemid(0L);
			localDefDocItemXS.setPromotiontype(0);
			localDefDocItemXS.setPromotiontypename("特惠");
			localDefDocItemXS.setOutorderdocid(0L);
			localDefDocItemXS.setOutorderdocshowid(null);
			localDefDocItemXS.setOutorderitemid(0L);
			localDefDocItemXS.setBatch(paramRespPromotionRule.getBatch());
			localDefDocItemXS.setProductiondate(paramRespPromotionRule.getProductiondate());
		}

		return localDefDocItemXS;
	}

	// 过账
	public void check(final boolean isprint) {
		String localString = validateDoc();
		if (localString != null) {
			InfoDialog.showError(this, localString);
			return;
		}
		listItem = adapter.getData();
		if ((this.listItem == null) || (this.listItem.size() == 0)) {
			InfoDialog.showError(this, "空单不能过账");
			return;
		}
		final Dialog_message dialog = new Dialog_message(this);
		dialog.show();
		dialog.setTitle("提示");
		dialog.setMessage("过账后的单据不能修改。\n确定过账？");
		dialog.setCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PDH.show(OutDocEditActivity.this, new PDH.ProgressCallBack() {

					@Override
					public void action() {
						dialog.dismiss();
						String localString = serviceStore.str_CheckXSDoc(doc, listItem, listPayType, listItemDelete,
								isprint);
						handler.sendMessage(handler.obtainMessage(1, localString));
					}
				});
			}
		});
	}

	// 收款
	public void pay() {
		boolean isreadonly = false;
		Intent localIntent = new Intent();
		if ((!this.doc.isIsavailable()) || (!this.doc.isIsposted())) {
			isreadonly = true;
		} else {
			isreadonly = false;
		}
		double discountsubtotal = 0.0d;
		for (DefDocItemXS item : listItem) {
			discountsubtotal += item.getDiscountsubtotal();
		}
		localIntent.putExtra("discountsubtotal", Utils.normalizeSubtotal(discountsubtotal));
		localIntent.putExtra("isreadonly", isreadonly);
		localIntent.putExtra("preference", this.doc.getPreference());
		localIntent.putExtra("listpaytype", JSONUtil.object2Json(this.listPayType));
		startActivityForResult(localIntent.setClass(this, OutDocPayAct.class), 8);
	}

	public void getCustomerHistory() {
		if (TextUtils.isEmptyS(this.doc.getCustomerid())) {
			// deleBm();
			Intent localIntent = new Intent();
			localIntent.putExtra("customerid", doc.getCustomerid());
			localIntent.putExtra("customername", doc.getCustomername());
			startActivityForResult(localIntent.setClass(this, SaleCustomerHistoryActivity.class), 7);
			return;
		}
		PDH.showMessage("单据未指定客户");
	}

	private boolean check;

	public void save(boolean check) {
		if (doc.isIsposted()) {
			return;
		}
		this.check = check;
		String localString = validateDoc();
		if (localString != null) {
			InfoDialog.showError(this, localString);
			return;
		}
		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				String localString = serviceStore.str_SaveXSDoc(doc, adapter.getData(), listPayType, listItemDelete);
				handler.sendMessage(handler.obtainMessage(0, localString));
			}
		});
	}

	private int BLUE_STATE = 0;
	private int BLUE_Print = 1;

	// 蓝牙过账/打印
	public void blueDevicePrint() {
		if (doc.isIsposted()) {
			bluePrint();
		} else {
			check(false);
			BLUE_STATE = BLUE_Print;
		}
	}

	private void bluePrint() {
		bottomCount();
		BTPrintHelper btprint = new BTPrintHelper(OutDocEditActivity.this);
		PrintMode printMode = PrintMode.getPrintMode();
		printMode.setDocInfo(getDocPrintInfo());
		printMode.setDatainfo(getDocPrintData());
		btprint.setMode(printMode);
		btprint.print(new AccountPreference().getPrinter());
		btprint.setPrintOverCall(new PrintOverCall() {

			@Override
			public void printOver() {
				showSuccess("打印完毕!");
			}
		});
	}

	public List<FieldSaleItemForPrint> getDocPrintData() {
		List<FieldSaleItemForPrint> datainfo = new ArrayList<FieldSaleItemForPrint>();
		for (DefDocItemXS item : listItem) {
			FieldSaleItemForPrint itemForPrint = new FieldSaleItemForPrint();
			itemForPrint.setBarcode(item.getBarcode());
			itemForPrint.setDiscountratio(item.getDiscountprice());
			itemForPrint.setDiscountsubtotal(item.getDiscountsubtotal());
			itemForPrint.setGoodsid(item.getGoodsid());
			itemForPrint.setGoodsname(item.getGoodsname());
			itemForPrint.setItemtype("13");
			itemForPrint.setNum(item.getNum());
			itemForPrint.setPrice(item.getPrice());
			itemForPrint.setRemark(item.getRemark());
			itemForPrint.setSpecification(item.getSpecification());
			itemForPrint.setUnitname(item.getUnitname());
			datainfo.add(itemForPrint);
		}
		return datainfo;
	}

	public FieldSaleForPrint getDocPrintInfo() {
		FieldSaleForPrint v2 = new FieldSaleForPrint();
		// v2.setId(doc.getId());
		v2.setDoctype("销售单");
		v2.setShowid(doc.getShowid());
		v2.setCustomername(this.doc.getCustomername());
		v2.setDepartmentname(this.doc.getDepartmentname());
		v2.setBuildername(this.doc.getBuildername());
		v2.setBuildtime(Utils.formatDate(this.doc.getBuildtime(), "yyyy-MM-dd HH:mm:ss"));
		v2.setSumamount("合计:" + getSumMoney());
		v2.setPreference("优惠:" + this.doc.getPreference());
		v2.setReceivable("应收:" + Utils.getSubtotalMoney(getSumMoney() - doc.getPreference()));
		v2.setReceived("已收:" + Utils.getRecvableMoney(received));
		v2.setNum("数量:" + String.valueOf(0));
		v2.setRemark(doc.getRemark());
		return v2;
	}

	private RespServiceInfor printDoc() {
		Def_DocDraft draft = new Def_DocDraft();
		draft.setDepartmentid(doc.getDepartmentid());
		draft.setDepartmentname(doc.getDepartmentname());
		draft.setShowid(doc.getShowid());
		draft.setDoctypeid("13");// 单据类型， 12销售订单
		draft.setId(doc.getDocid());// 单据id
		final String printdoc = serviceStore.PrintDoc(draft);
		if (!TextUtils.isEmpty(printdoc)) {
			return JSONUtil.readValue3(printdoc);
		}
		return null;
	}

	// 删除
	public void delete() {
		if (this.doc.getDocid() > 0L) {
			final Dialog_message dialog = new Dialog_message(this);
			dialog.show();
			dialog.setMessage("确定删除?");
			dialog.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					PDH.show(OutDocEditActivity.this, new ProgressCallBack() {

						@Override
						public void action() {
							String localString = serviceStore.str_DeleteDoc(doc.getDoctypeid(), doc.getDocid());
							handler.sendMessage(handler.obtainMessage(2, localString));
						}
					});

				}

			});
			return;
		}
		startActivity(new Intent(OutDocEditActivity.this, SwyMain.class));
		finish();
	}

	// 属性
	public void docProperty() {
		int k = 0;
		// if (this.doc.isIsavailable()) {
		// if (doc.isIsposted()) {
		//
		// }
		// }
		for (int i = 0; i < listItem.size(); i++) {
			DefDocItemXS localDefDocItemXS = (DefDocItemXS) this.listItem.get(i);
			if (localDefDocItemXS.isIspromotion()) {
				k = 1;
			} else {
				if (localDefDocItemXS.getOutorderdocid() <= 0L) {
					k = 2;
				}
			}
		}
		// deleBm();
		Intent localIntent = new Intent(this, OutDocOpenActivity.class);
		localIntent.putExtra("typechangecustomer", k);// typechangecustomer表示单据返回信息
		localIntent.putExtra("doc", doc);
		startActivityForResult(localIntent, 0);
	}

	private String validateDoc() {
		String message = null;
		if (TextUtils.isEmptyS(this.doc.getBuildtime())) {
			this.doc.setBuildtime(Utils.formatDate(Utils.getCurrentTime(false), "yyyy-MM-dd HH:mm:ss"));
		}
		if (TextUtils.isEmptyS(this.doc.getBuilderid())) {
			this.doc.setBuilderid(SystemState.getUser().getId());
		}
		if (!TextUtils.isEmptyS(this.doc.getDepartmentid())) {
			message = "部门不能为空";
		}
		if ((this.doc.getDiscountratio() <= 0.0D) || (this.doc.getDiscountratio() > 1.0D)) {
			PDH.showMessage("整单折扣必须大于0且小于等于1");
			return null;
		}
		if (!TextUtils.isEmptyS(this.doc.getDeliverytime())) {
			message = "交货日期不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getSettletime())) {
			message = "结算日期不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getPrinttemplate())) {
			message = "打印模板不能为空";
		}
		for (int k = 0; k < listItem.size(); k++) {
			DefDocItemXS localDefDocItemXS = (DefDocItemXS) this.listItem.get(k);
			if (localDefDocItemXS.getNum() == 0.0D) {
				message = "【" + localDefDocItemXS.getGoodsname() + "】数量为0";
			}
			if (!TextUtils.isEmptyS(localDefDocItemXS.getWarehouseid())) {
				message = "【" + localDefDocItemXS.getGoodsname() + "】没有选择仓库";
			}

			if (!TextUtils.isEmptyS(localDefDocItemXS.getUnitid())) {
				message = "【" + localDefDocItemXS.getGoodsname() + "】没有选择单位";
			}

			if (!(TextUtils.isEmptyS(localDefDocItemXS.getBatch())) && (localDefDocItemXS.isIsusebatch())) {
				message = "【" + localDefDocItemXS.getGoodsname() + "】没有选择批次";
			}

		}
		return message;
	}

	// private boolean mPrintDoc;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String localString = msg.obj.toString();
			if (RequestHelper.isSuccess(localString)) {
				if (msg.what == 2) {
					PDH.showMessage("删除成功");
					Intent intent = new Intent(OutDocEditActivity.this, SwyMain.class);
					startActivity(intent);
					finish();
					return;
				}
				boolean isSort = false;
				DocContainerEntity localDocContainerEntity = (DocContainerEntity) JSONUtil.readValue(localString,
						DocContainerEntity.class);
				doc = ((DefDocXS) JSONUtil.readValue(localDocContainerEntity.getDoc(), DefDocXS.class));
				OutDocEditActivity.this.listPayType = JSONUtil.str2list(localDocContainerEntity.getPaytype(),
						DefDocPayType.class);
				if (getMaxTempItemId() != 0) {
					isSort = true;
				}
				listItem = JSONUtil.str2list(localDocContainerEntity.getItem(), DefDocItemXS.class);
				listItemDelete = new ArrayList<Long>();
				switch (msg.what) {
				case 0:
					if (isSort) {
						Collections.sort(listItem);
					}
					adapter.setData(listItem);
					PDH.showSuccess("保存成功");
					ishaschanged = false;
					setActionBarText();
					if (check) {
						printDocHandler.sendEmptyMessage(0);
					}
					return;
				case 1:
					if ((OutDocEditActivity.this.doc.isIsavailable()) && (OutDocEditActivity.this.doc.isIsposted())) {
						PDH.showSuccess("过账成功");
						if (BLUE_STATE == BLUE_Print) {
							bluePrint();
						}
						startActivity(new Intent(OutDocEditActivity.this, SwyMain.class));
						// finish();
						return;
					}
					adapter.setData(listItem);
					refreshUI();
					InfoDialog.showError(OutDocEditActivity.this, localDocContainerEntity.getInfo());
					return;
				default:
					break;
				}
			}
			InfoDialog.showError(OutDocEditActivity.this, localString);
		}
	};
	private Handler printDocHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RespServiceInfor printDoc = printDoc();
			if (printDoc != null && printDoc.Result) {// 打印单据
				showSuccess("打印成功!");
			} else {
				showError("打印失败!服务请开启！");
			}
		};
	};

	public void printServiceDoc() {
		RespServiceInfor printDoc = printDoc();
		if (printDoc != null && printDoc.Result) {// 打印单据
			showSuccess("打印成功!");
		} else {
			showError("打印失败!服务请开启！");
		}
	}

	private double sumMoney = 0D;

	/**
	 * 计算底部 品种 数量 总价
	 */
	private void bottomCount() {
		sumMoney = 0D;
		int sumNum = 0;
		for (DefDocItemXS itemXS : listItem) {
			sumMoney += itemXS.getDiscountsubtotal();
			sumNum += itemXS.getNum();
		}
		btnGoodClass.setText("品种:" + listItem.size() + "个");
		bt_sumNumber.setText("数量:" + sumNum + "个");
		bt_totalSum.setText("总价:" + Utils.normalizePrice(sumMoney) + "元");
	}

	/**
	 * 当前总金额
	 * 
	 * @return
	 */
	public double getSumMoney() {
		return sumMoney;
	}

	private List<DefDocItemXS> newListItem;
	private Button bt_sumNumber;
	private Button bt_totalSum;
	// private BarcodeManager bm;
	private Dialog_listCheckBox dialog;
	private Button btnGoodClass;
	private double preference;// 优惠
	private double received;// 已收
	private Scaner scaner;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_FIRST_USER) {
			addListener();
		} else if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				addListener();
				doc = (DefDocXS) data.getSerializableExtra("doc");
				ishaschanged = true;
				setActionBarText();
				bottomCount();
				break;
			case 1:

				newListItem = JSONUtil.str2list(data.getStringExtra("items"), DefDocItemXS.class);
				final ArrayList<ReqStrGetGoodsPrice> localArrayList = new ArrayList<ReqStrGetGoodsPrice>();
				for (int i = 0; i < newListItem.size(); i++) {
					DefDocItemXS localDefDocItemXS5 = (DefDocItemXS) this.newListItem.get(i);
					ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new ReqStrGetGoodsPrice();
					localReqStrGetGoodsPrice.setType(1);
					localReqStrGetGoodsPrice.setCustomerid(this.doc.getCustomerid());
					localReqStrGetGoodsPrice.setWarehouseid(localDefDocItemXS5.getWarehouseid());
					localReqStrGetGoodsPrice.setGoodsid(localDefDocItemXS5.getGoodsid());
					localReqStrGetGoodsPrice.setUnitid(localDefDocItemXS5.getUnitid());
					localReqStrGetGoodsPrice.setPrice(0.0D);
					localReqStrGetGoodsPrice.setIsdiscount(false);
					localArrayList.add(localReqStrGetGoodsPrice);
				}
				PDH.show(this, new ProgressCallBack() {

					@Override
					public void action() {
						String localString2 = new ServiceGoods().gds_GetMultiGoodsPrice(localArrayList, true, true);
						handlerGet.sendMessage(handlerGet.obtainMessage(0, localString2));
					}
				});
				break;
			case 2:
				addListener();
				int j = data.getIntExtra("position", 0);
				DefDocItemXS localDefDocItemXS3 = (DefDocItemXS) data.getSerializableExtra("docitem");
				listItem = adapter.getData();
				listItem.set(j, localDefDocItemXS3);
				int k = data.getIntExtra("positiongive", -1);
				DefDocItemXS localDefDocItemXS4;
				if ((localDefDocItemXS3.isIspromotion()) && (localDefDocItemXS3.getPromotiontype() == 1)) {
					localDefDocItemXS4 = (DefDocItemXS) data.getSerializableExtra("docitemgive");
					if (k >= 0) {
						if ((localDefDocItemXS4 != null) && (localDefDocItemXS4.getNum() > 0.0D)) {
							this.listItem.set(k, localDefDocItemXS4);
						}
					}
				}
				this.adapter.setData(listItem);
				listview_copy_dele.setAdapter(adapter);
				ishaschanged = true;
				bottomCount();
				refreshUI();
				break;
			case 3:
				doc = (DefDocXS) data.getSerializableExtra("doc");
				break;
			case 4:
				addListener();
				DefDocItemXS defDocItemXS4 = (DefDocItemXS) data.getSerializableExtra("docitem");
				adapter.addItem(0, defDocItemXS4);
				listview_copy_dele.setAdapter(adapter);
				ishaschanged = true;
				refreshUI();
				bottomCount();
				break;
			case 7:
				// 客史

				break;
			case 8:
				// 收款
				String localString1 = data.getStringExtra("listpaytype");
				if (TextUtils.isEmptyS(localString1)) {
					listPayType = JSONUtil.str2list(localString1, DefDocPayType.class);
				}
				preference = data.getDoubleExtra("preference", 0.0D);
				received = data.getDoubleExtra("received", 0.0D);
				if (preference != this.doc.getPreference()) {
					this.doc.setPreference(preference);
				}
				break;
			}
		}
	}

	private void intenToMain() {
		if (ishaschanged) {
			final MAlertDialog maler = new MAlertDialog(this);
			maler.setMessage("是否保存当前单据?");
			maler.show();
			maler.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					maler.dismiss();
					String localString = validateDoc();
					if (localString != null) {
						InfoDialog.showError(OutDocEditActivity.this, localString);
						return;
					}
					PDH.show(OutDocEditActivity.this, new ProgressCallBack() {

						@Override
						public void action() {
							final String localString = serviceStore.str_SaveXSDoc(doc, listItem, listPayType,
									listItemDelete);
							handler.post(new Runnable() {
								public void run() {
									if (RequestHelper.isSuccess(localString)) {
										PDH.showSuccess("保存成功");
										Intent intent = new Intent(OutDocEditActivity.this, SwyMain.class);
										startActivity(intent);
										finish();
										return;
									}
									RequestHelper.showError(localString);
								}
							});
						}
					});
				}
			});
			maler.setComfirmListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(OutDocEditActivity.this, SwyMain.class);
					startActivity(intent);
					finish();
				}
			});
			return;
		}
		Intent intent = new Intent(OutDocEditActivity.this, SwyMain.class);
		startActivity(intent);
		finish();

	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			if (ishaschanged) {
				intenToMain();
			} else {
				setResult(RESULT_FIRST_USER);
				finish();
			}
			break;
		case 0:
			if (this.menuPopup == null) {
				this.menuPopup = new OutDocEditMenuPopup(this);
			}
			this.menuPopup.showAtLocation(listview_copy_dele, 80, 0, 0);
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 0.8F;
			getWindow().setAttributes(localLayoutParams);
			break;
		}
		return true;
	}

	@Override
	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 1.0F;
			getWindow().setAttributes(localLayoutParams);
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setIcon(getResources().getDrawable(R.drawable.btn_submenu)).setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
	public void setActionBarText() {
		ActionBar localActionBar = getActionBar();
		if (ishaschanged) {
			localActionBar.setTitle(doc.getShowid() + "*");
		} else {
			localActionBar.setTitle(doc.getShowid());
		}
	}

	/**
	 * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ishaschanged) {
				intenToMain();
			} else {
				setResult(RESULT_FIRST_USER);
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
