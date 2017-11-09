package com.ahjswy.cn.ui.ingoods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.dao.Sv_docitem;
import com.ahjswy.cn.model.DefDoc;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocPayType;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.popupmenu.InDocEditMenuPopup;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.MAlertDialog;
import com.ahjswy.cn.ui.SearchHelper;
import com.ahjswy.cn.ui.SwyMain;
import com.ahjswy.cn.ui.ingoods.InDocItemAdapter.Sum;
import com.ahjswy.cn.ui.outgoods.OutDocPayAct;
import com.ahjswy.cn.ui.outgoods.SaleCustomerHistoryActivity;
import com.ahjswy.cn.utils.DocUtils;
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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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

//销售退货单
public class InDocEditActivity extends BaseActivity implements OnItemClickListener, OnTouchListener, Sum {
	private ServiceStore serviceStore;
	private LinearLayout linearSearch;
	// 添加
	private Button btnAdd;
	private SwipeMenuListView listView;
	private Button bt_sumNumber;
	private Button bt_totalSum;
	private boolean ishaschanged;
	private DefDoc doc;
	private List<DefDocPayType> listPayType;
	private List<Long> listItemDelete;
	private ArrayList<DefDocItemXS> listItem;
	private SearchHelper searchHelper;
	private AutoTextView atvSearch;
	InDocItemAdapter adapter;
	private InDocEditMenuPopup menuPopup;
	// private ArrayList<DefDocItemXS> newListItem;
	Scaner scaner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_indocedit_activity);
		intView();
		intDate();
		refreshUI();
		sum();
	}

	private void intView() {

		atvSearch = ((AutoTextView) findViewById(R.id.atvSearch));
		linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
		btnGoodClass = (Button) findViewById(R.id.btn_goodClass);
		searchHelper = new SearchHelper(this, linearSearch);
		atvSearch.setOnItemClickListener(this.onItemClickListeners);
		// 添加按钮监听
		btnAdd = (Button) findViewById(R.id.btnAdd);
		// 底部 总数量
		bt_sumNumber = (Button) findViewById(R.id.bt_sumNumber);
		// 底部总价格
		bt_totalSum = (Button) findViewById(R.id.bt_totalSum);
		serviceStore = new ServiceStore();
		ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		listView = (SwipeMenuListView) findViewById(R.id.listView_addShop);
		listView.setOnTouchListener(this);

		adapter = new InDocItemAdapter(this);
		adapter.setSum(this);
		btnAdd.setOnClickListener(addMoreListener);
		dialog = new Dialog_listCheckBox(InDocEditActivity.this);
		docContainerEntity = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		this.doc = JSONUtil.readValue(docContainerEntity.getDoc(), DefDoc.class);
		listItem = (ArrayList<DefDocItemXS>) JSONUtil.str2list(docContainerEntity.getItem(), DefDocItemXS.class);
		listPayType = JSONUtil.str2list(docContainerEntity.getPaytype(), DefDocPayType.class);
		if (listItem != null) {
			adapter.setData(listItem);
			listView.setAdapter(adapter);
		}
		maler = new MAlertDialog(this);
		unitDao = new GoodsUnitDAO();
		// ap = new AccountPreference();
		sv = new Sv_docitem();
		// goodspricedao = new GoodsPriceDAO();
	}

	private void intDate() {

		SwipeMenuCreator local5 = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem localSwipeMenuItem1 = new SwipeMenuItem(getApplicationContext());
				localSwipeMenuItem1.setTitle("复制");
				localSwipeMenuItem1.setTitleSize(14);
				localSwipeMenuItem1.setTitleColor(-16777216);
				localSwipeMenuItem1.setWidth(100);
				localSwipeMenuItem1.setBackground(new ColorDrawable(Color.rgb(48, 177, 245)));
				menu.addMenuItem(localSwipeMenuItem1);
				SwipeMenuItem localSwipeMenuItem2 = new SwipeMenuItem(getApplicationContext());
				localSwipeMenuItem2.setTitle("删除");
				localSwipeMenuItem2.setTitleSize(14);
				localSwipeMenuItem2.setTitleColor(-16777216);
				localSwipeMenuItem2.setBackground(new ColorDrawable(Color.rgb(201, 201, 206)));
				localSwipeMenuItem2.setWidth(100);
				menu.addMenuItem(localSwipeMenuItem2);
			}
		};
		listView.setMenuCreator(local5);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					DefDocItemXS defdocitem = listItem.get(position);
					defdocitem.setItemid(0L);
					listItem.add(defdocitem);

					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							sum();
							DBupdataDocItem();
						}
					}, 180L);
					break;
				case 1:
					long l = listItem.get(position).getItemid();
					if (l > 0L) {
						listItemDelete.add(Long.valueOf(l));
					}
					listItem.remove(position);
					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.setData(listItem);
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							sum();
							DBupdataDocItem();
						}
					}, 180L);
					break;

				default:
					break;
				}
				return false;
			}
		});

	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			if (doc.isIsavailable() && doc.isIsposted()) {
				return;
			}
			atvSearch.setText("");
			if (dialog != null) {
				dialog.dismiss();
			}
			readBarcode(barcode);
		}
	};

	private void readBarcode(String barcode) {
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();

		if (goodsThinList.size() == 1) {
			int num = Utils.isCombination() ? 1 : 0;
			DefDocItemXS defdocitem = fillItem(goodsThinList.get(0), num, 0.0D);
			localArrayList.add(defdocitem);
			Intent intent = new Intent().setClass(InDocEditActivity.this, InDocAddMoreGoodsAct.class);
			intent.putExtra("items", JSONUtil.object2Json(localArrayList));
			intent.putExtra("doc", doc);
			startActivityForResult(intent, 2);
		} else if (goodsThinList.size() > 1) {
			dialog.setGoods(goodsThinList);
			dialog.setTempGoods(goodsThinList);
			dialog.ShowMe();
			dialog.ensure(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					List<GoodsThin> select = dialog.getSelect();
					int num = Utils.isCombination() ? 1 : 0;
					for (int i = 0; i < select.size(); i++) {
						DefDocItemXS defdocitem = fillItem(select.get(i), num, 0.0D);
						localArrayList.add(defdocitem);
					}
					if (localArrayList.size() == 0) {
						return;
					}
					Intent intent = new Intent().setClass(InDocEditActivity.this, InDocAddMoreGoodsAct.class);
					intent.putExtra("items", JSONUtil.object2Json(localArrayList));
					intent.putExtra("doc", doc);
					startActivityForResult(intent, 2);
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
	}

	OnItemClickListener onItemClickListeners = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if ((InDocEditActivity.this.menuPopup != null) && (InDocEditActivity.this.menuPopup.isShowing())) {
				InDocEditActivity.this.menuPopup.dismiss();
				WindowManager.LayoutParams localLayoutParams = InDocEditActivity.this.getWindow().getAttributes();
				localLayoutParams.alpha = 1.0F;
				InDocEditActivity.this.getWindow().setAttributes(localLayoutParams);
			}

			final GoodsThin localGoodsThin = (GoodsThin) InDocEditActivity.this.searchHelper.getAdapter().getTempGoods()
					.get(position);
			atvSearch.setText("");
			PDH.show(InDocEditActivity.this, new ProgressCallBack() {

				@Override
				public void action() {
					DefDocItemXS docItem = InDocEditActivity.this.fillItem(localGoodsThin, 0.0D, 0.0D);
					// ArrayList<DefDocItemXS> newListItem = new
					// ArrayList<DefDocItemXS>();
					// newListItem.add(docItem);
					docItem.setPrice(DocUtils.getGoodsPrice(doc.getCustomerid(), docItem));
					setAddItem(docItem);
					Intent localIntent = new Intent();
					localIntent.putExtra("docitem", docItem);
					localIntent.putExtra("customerid", doc.getCustomerid());
					startActivityForResult(localIntent.setClass(InDocEditActivity.this, InDocAddGoodAct.class), 4);
					// if (!TextUtils.isEmpty(doc.getOpendate())) {
					// return;
					// }
					// ArrayList<ReqStrGetGoodsPrice> localArrayList = new
					// ArrayList<ReqStrGetGoodsPrice>();
					// ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new
					// ReqStrGetGoodsPrice();
					// localReqStrGetGoodsPrice.setType(2);
					// localReqStrGetGoodsPrice.setCustomerid(InDocEditActivity.this.doc.getCustomerid());
					// localReqStrGetGoodsPrice.setWarehouseid(localDefDocItem.getWarehouseid());
					// localReqStrGetGoodsPrice.setGoodsid(localDefDocItem.getGoodsid());
					// localReqStrGetGoodsPrice.setUnitid(localDefDocItem.getUnitid());
					// localReqStrGetGoodsPrice.setPrice(0.0D);
					// localReqStrGetGoodsPrice.setIsdiscount(false);
					// localArrayList.add(localReqStrGetGoodsPrice);
					// String localString = new
					// ServiceGoods().gds_GetMultiGoodsPrice(localArrayList,
					// true,
					// localDefDocItem.isIsusebatch());
					// handlerGet.sendMessage(handlerGet.obtainMessage(2,
					// localString));
				}

			});
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setIcon(getResources().getDrawable(R.drawable.btn_submenu)).setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			intenToMain();
			break;
		case 0:
			if (this.menuPopup == null) {
				this.menuPopup = new InDocEditMenuPopup(this);
			}
			this.menuPopup.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 0.8F;
			getWindow().setAttributes(localLayoutParams);
			closeInputMethod();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		scaner.removeListener();
	}

	public DefDoc getDoc() {
		return doc;
	}

	public void refreshUI() {
		if (doc.isIsavailable() && doc.isIsposted()) {
			findViewById(R.id.linearSearch).setVisibility(View.GONE);
			findViewById(R.id.top).setVisibility(View.GONE);
			findViewById(R.id.linNumber).setVisibility(View.GONE);
			listView.setItemSwipe(false);
		} else {
			findViewById(R.id.linearSearch).setVisibility(View.VISIBLE);
			findViewById(R.id.top).setVisibility(View.VISIBLE);
			findViewById(R.id.linNumber).setVisibility(View.VISIBLE);
			listView.setItemSwipe(true);
			listView.setOnItemClickListener(this);
		}
	}

	private String validateDoc() {
		String message = null;
		if (TextUtils.isEmptyS(this.doc.getBuildtime())) {
			this.doc.setBuildtime(Utils.formatDate(Utils.getCurrentTime(false), "yyyy-MM-dd HH:mm:ss"));
		}
		if (TextUtils.isEmptyS(this.doc.getBuilderid())) {
			this.doc.setBuilderid(SystemState.getUser().getId());
		}
		if (!TextUtils.isEmptyS(doc.getCustomerid())) {
			return "没有选择客户!";
		}
		if (!TextUtils.isEmptyS(this.doc.getDepartmentid())) {
			return "部门不能为空!";
		}
		if ((this.doc.getDiscountratio() <= 0.0D) || (this.doc.getDiscountratio() > 1.0D)) {
			return "整单折扣必须大于0且小于等于1";
		}
		if (!TextUtils.isEmptyS(this.doc.getDeliverytime())) {
			return "交货日期不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getSettletime())) {
			return "结算日期不能为空";
		}
		if (!TextUtils.isEmptyS(this.doc.getPrinttemplate())) {
			return "打印模板不能为空";
		}
		for (int k = 0; k < listItem.size(); k++) {
			DefDocItemXS localDefDocItem = (DefDocItemXS) this.listItem.get(k);
			if (localDefDocItem.getNum() == 0.0D) {
				message = "【" + localDefDocItem.getGoodsname() + "】数量为0";
			}
			if (!TextUtils.isEmptyS(localDefDocItem.getWarehouseid())) {
				message = "【" + localDefDocItem.getGoodsname() + "】没有选择仓库";
			}

			if (!TextUtils.isEmptyS(localDefDocItem.getUnitid())) {
				message = "【" + localDefDocItem.getGoodsname() + "】没有选择单位";
			}

			if (!(TextUtils.isEmptyS(localDefDocItem.getBatch())) && (localDefDocItem.isIsusebatch())) {
				message = "【" + localDefDocItem.getGoodsname() + "】没有选择批次";
			}

		}
		return message;
	}

	View.OnClickListener addMoreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			List<GoodsThin> localList = searchHelper.getAdapter().getSelect();
			if ((localList == null) || (localList.size() == 0)) {
				PDH.showMessage("请选择商品！");
				return;
			}
			atvSearch.setText("");
			// if (!"1".equals(ap.getValue("goods_select_more"))) {
			ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
			int num = Utils.isCombination() ? 1 : 0;
			for (int i = 0; i < localList.size(); i++) {
				GoodsThin localGoodsThin = (GoodsThin) localList.get(i);
				localArrayList.add(fillItem(localGoodsThin, num, 0.0D));
			}
			startActivityForResult(new Intent().setClass(InDocEditActivity.this, InDocAddMoreGoodsAct.class)
					.putExtra("items", JSONUtil.object2Json(localArrayList)).putExtra("doc", doc), 2);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
	}

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

	// 过账
	public void check(final boolean paramBoolean) {
		String localString = validateDoc();
		if (localString != null) {
			InfoDialog.showError(this, localString);
			return;
		}
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
				PDH.show(InDocEditActivity.this, new PDH.ProgressCallBack() {

					@Override
					public void action() {
						dialog.dismiss();
						String localString = serviceStore.str_CheckXTDoc(doc, listItem, listPayType, listItemDelete,
								paramBoolean);
						handler.sendMessage(handler.obtainMessage(1, localString));
					}
				});
			}
		});
	}

	// 客史
	public void getCustomerHistory() {
		if (TextUtils.isEmptyS(doc.getCustomerid())) {
			Intent localIntent = new Intent();
			localIntent.putExtra("customerid", this.doc.getCustomerid());
			localIntent.putExtra("customername", this.doc.getCustomername());
			startActivityForResult(localIntent.setClass(this, SaleCustomerHistoryActivity.class), 7);
			return;
		}
		PDH.showMessage("单据未指定客户");
	}

	// 属性
	public void docProperty() {
		// deleBm();
		Intent localIntent = new Intent(InDocEditActivity.this, InDocOpenActivity.class);
		localIntent.putExtra("doc", doc);
		startActivityForResult(localIntent, 0);
	}

	// 付款
	public void pay() {
		boolean isreadonly = false;
		Intent localIntent = new Intent();
		if ((!this.doc.isIsavailable()) || (!this.doc.isIsposted())) {
			isreadonly = true;
		} else {
			isreadonly = false;
		}
		// 获取折扣合计的总金额
		localIntent.putExtra("discountsubtotal", sum);
		// 判断是否是 销售退货
		localIntent.putExtra("isreceive", false);
		localIntent.putExtra("isreadonly", isreadonly);
		localIntent.putExtra("preference", this.doc.getPreference());
		localIntent.putExtra("listpaytype", JSONUtil.object2Json(this.listPayType));
		startActivityForResult(localIntent.setClass(this, OutDocPayAct.class), 8);
	}

	// 删除
	public void delete() {
		if (doc.getDocid() > 0L) {
			final Dialog_message dialog = new Dialog_message(this);
			dialog.show();
			dialog.setMessage("确定删除?");
			dialog.setCancelListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					PDH.show(InDocEditActivity.this, new ProgressCallBack() {
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
		Intent intent = new Intent(InDocEditActivity.this, SwyMain.class);
		startActivity(intent);
		finish();
	}

	// 保存
	public void save() {
		String localString = validateDoc();
		if (localString != null) {
			InfoDialog.showError(this, localString);
			return;
		}
		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				String localString = serviceStore.str_SaveXTDoc(doc, adapter.getData(), listPayType, listItemDelete);
				handler.sendMessage(handler.obtainMessage(0, localString));
			}
		});
	}

	double sum = 0.0D;

	// sum 总价
	public void sum() {
		List<DefDocItemXS> data = adapter.getData();
		listItemDelete = adapter.getListItemDelete();
		bt_sumNumber.setText("数量:" + data.size() + "个");
		double sumMoney = 0D;
		int sumNum = 0;
		Map<String, Object> map = new HashMap<>();
		for (DefDocItemXS defDocItemXS : data) {
			sumMoney += defDocItemXS.getDiscountsubtotal();
			sumNum += defDocItemXS.getNum();
			map.put(defDocItemXS.getGoodsid(), defDocItemXS);
		}
		sum = Utils.normalizePrice(sumMoney);
		btnGoodClass.setText("品种:" + map.size());
		bt_sumNumber.setText("数量:" + sumNum);
		bt_totalSum.setText("总价:\n" + Utils.normalizePrice(sumMoney) + "元");

	}

	private void intenToMain() {
		if (ishaschanged) {
			maler.show();
			maler.setMessage("是否保存当前单据?");
			maler.setNeutralButton(new MAlertDialog.OnClickListener() {

				@Override
				public void onClick(MAlertDialog dialog) {
					dialog.dismiss();
					String localString = validateDoc();
					if (localString != null) {
						InfoDialog.showError(InDocEditActivity.this, localString);
						return;
					}
					PDH.show(InDocEditActivity.this, new ProgressCallBack() {

						@Override
						public void action() {
							final String localString = serviceStore.str_SaveXTDoc(doc, listItem, listPayType,
									listItemDelete);
							handler.post(new Runnable() {
								public void run() {
									if (RequestHelper.isSuccess(localString)) {
										PDH.showSuccess("保存成功");
										Intent intent = new Intent(InDocEditActivity.this, SwyMain.class);
										startActivity(intent);
										finish();
										sv.deleteDoc(docContainerEntity.getDoctype());
										return;
									}
									RequestHelper.showError(localString);
								}
							});
						}
					});
				}
			});
			maler.setNegativeButton(new MAlertDialog.OnClickListener() {

				@Override
				public void onClick(MAlertDialog dialog) {
					dialog.dismiss();
					Intent intent = new Intent(InDocEditActivity.this, SwyMain.class);
					startActivity(intent);
					finish();
					sv.deleteDoc(docContainerEntity.getDoctype());
				}
			});
			return;
		}
		setResult(RESULT_FIRST_USER);
		finish();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String localString = msg.obj.toString();
			if (RequestHelper.isSuccess(localString)) {
				if (msg.what == 2) {
					PDH.showMessage("删除成功");
					Intent intent = new Intent(InDocEditActivity.this, SwyMain.class);
					startActivity(intent);
					finish();
					sv.deleteDoc(docContainerEntity.getDoctype());
					return;
				}
				DocContainerEntity localDocContainerEntity = (DocContainerEntity) JSONUtil.fromJson(localString,
						DocContainerEntity.class);
				doc = ((DefDoc) JSONUtil.readValue(localDocContainerEntity.getDoc(), DefDoc.class));
				listItem = (ArrayList<DefDocItemXS>) JSONUtil.str2list(localDocContainerEntity.getItem(),
						DefDocItemXS.class);
				listPayType = JSONUtil.str2list(localDocContainerEntity.getPaytype(), DefDocPayType.class);
				listItemDelete = new ArrayList<Long>();
				switch (msg.what) {
				case 0:
					adapter.setData(listItem);
					refreshUI();
					PDH.showSuccess("保存成功");
					ishaschanged = false;
					setActionBarText();
					sv.deleteDoc(docContainerEntity.getDoctype());
					startActivity(new Intent(InDocEditActivity.this, SwyMain.class));
					finish();
					return;
				case 1:
					if ((InDocEditActivity.this.doc.isIsavailable()) && (InDocEditActivity.this.doc.isIsposted())) {
						PDH.showSuccess("过账成功");
						Intent intent = new Intent(InDocEditActivity.this, SwyMain.class);
						startActivity(intent);
						finish();
						sv.deleteDoc(docContainerEntity.getDoctype());
						return;
					}
					adapter.setData(listItem);
					refreshUI();
					InfoDialog.showError(InDocEditActivity.this, localDocContainerEntity.getInfo());
					return;
				}
			}
			InfoDialog.showError(InDocEditActivity.this, localString);
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent localIntent = new Intent();
		localIntent.putExtra("customerid", doc.getCustomerid());
		localIntent.putExtra("position", position);
		localIntent.putExtra("docitem", (Serializable) listItem.get(position));
		startActivityForResult(localIntent.setClass(InDocEditActivity.this, InDocAddGoodAct.class), 3);
	}

	private DefDocItemXS fillItem(GoodsThin goodsThin, double num, double price) {
		DefDocItemXS localDefDocItem = new DefDocItemXS();
		localDefDocItem.setItemid(0L);
		localDefDocItem.setDocid(this.doc.getDocid());
		localDefDocItem.setGoodsid(goodsThin.getId());
		localDefDocItem.setGoodsname(goodsThin.getName());
		localDefDocItem.setBarcode(goodsThin.getBarcode());
		localDefDocItem.setSpecification(goodsThin.getSpecification());
		localDefDocItem.setModel(goodsThin.getModel());
		localDefDocItem.setWarehouseid(this.doc.getWarehouseid());
		localDefDocItem.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = unitDao.queryBaseUnit(goodsThin.getId());
		} else {
			localGoodsUnit = unitDao.queryBigUnit(goodsThin.getId());
		}
		localDefDocItem.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItem.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItem.setNum(Utils.normalize(num, 2));
		localDefDocItem.setBignum(
				unitDao.getBigNum(localDefDocItem.getGoodsid(), localDefDocItem.getUnitid(), localDefDocItem.getNum()));
		localDefDocItem.setPrice(Utils.normalizePrice(price));
		localDefDocItem.setSubtotal(Utils.normalizeSubtotal(localDefDocItem.getNum() * localDefDocItem.getPrice()));
		localDefDocItem.setDiscountratio(this.doc.getDiscountratio());
		localDefDocItem
				.setDiscountprice(Utils.normalizePrice(localDefDocItem.getPrice() * this.doc.getDiscountratio()));
		localDefDocItem.setDiscountsubtotal(
				Utils.normalizeSubtotal(localDefDocItem.getNum() * localDefDocItem.getDiscountprice()));
		localDefDocItem.setIsgift(localDefDocItem.getPrice() == 0.0D ? true : false);
		localDefDocItem.setCostprice(0.0D);
		localDefDocItem.setRemark("");
		localDefDocItem.setRversion(0L);
		localDefDocItem.setIsdiscount(false);
		localDefDocItem.setIsusebatch(goodsThin.isIsusebatch());
		return localDefDocItem;
	}

	protected void combinationItem() {
		int combinationNum = listItem.size();
		ArrayList<DefDocItemXS> data = new ArrayList<DefDocItemXS>(adapter.getData());
		ArrayList<DefDocItemXS> listDocItem = new ArrayList<DefDocItemXS>();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < data.size(); i++) {
			DefDocItemXS items1 = data.get(i);
			if (map.get(items1.getGoodsid()) == null) {
				map.put(items1.getGoodsid(), new DefDocItemXS(items1));
				continue;
			}
			DefDocItemXS itemxs2 = (DefDocItemXS) map.get(items1.getGoodsid());
			if (items1.getGoodsid().equals(itemxs2.getGoodsid()) && items1.getUnitid().equals(itemxs2.getUnitid())
					&& items1.getPrice() == itemxs2.getPrice()
					&& items1.getDiscountratio() == itemxs2.getDiscountratio()
					&& items1.getWarehouseid().equals(itemxs2.getWarehouseid())) {
				itemxs2.setNum(itemxs2.getNum() + items1.getNum());
				itemxs2.setBignum(unitDao.getBigNum(itemxs2.getGoodsid(), itemxs2.getUnitid(), itemxs2.getNum()));
				itemxs2.setSubtotal(itemxs2.getNum() * itemxs2.getPrice());
				itemxs2.setDiscountsubtotal(itemxs2.getNum() * itemxs2.getPrice() * itemxs2.getDiscountratio());
				map.put(itemxs2.getGoodsid(), itemxs2);
				if (items1.getItemid() != 0) {
					listItemDelete.add(items1.getItemid());
				}
			} else {
				listDocItem.add(items1);
			}

		}
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
			listDocItem.add((DefDocItemXS) map.get(string));
		}
		if ((combinationNum - listDocItem.size()) == 0) {
			// 没有要合并的商品!
			return;
		}
		// 设置数据
		listItem.clear();
		listItem.addAll(listDocItem);
		showSuccess("同品增加成功!");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				doc = (DefDoc) data.getSerializableExtra("doc");
				ishaschanged = true;
				setActionBarText();
				DBupdataDocItem();
				break;
			case 1:
				doc = (DefDoc) data.getSerializableExtra("doc");
				DBupdataDocItem();
				break;
			case 2:

				// 修改 为本地查询客史价格
				PDH.show(this, "数据查询中...", new ProgressCallBack() {

					@Override
					public void action() {
						ArrayList<DefDocItemXS> newListItem = (ArrayList<DefDocItemXS>) JSONUtil
								.str2list(data.getStringExtra("items"), DefDocItemXS.class);
						for (DefDocItemXS itemXS : newListItem) {
							itemXS.setPrice(DocUtils.getGoodsPrice(doc.getCustomerid(), itemXS));
							setAddItem(itemXS);
						}
						if (Utils.isCombination()) {
							listItem.addAll(newListItem);
							combinationItem();
						} else {
							listItem.addAll(0, newListItem);
						}
						runOnUiThread(new Runnable() {
							public void run() {
								adapter.setData(listItem);
								listView.setAdapter(adapter);
								ishaschanged = true;
								setActionBarText();
								refreshUI();
								DBupdataDocItem();
							}
						});
					}
				});

				// final ArrayList<ReqStrGetGoodsPrice> localArrayList = new
				// ArrayList<ReqStrGetGoodsPrice>();
				// for (int k = 0; k < newListItem.size(); k++) {
				// DefDocItemXS localDefDocItem3 = (DefDocItemXS)
				// this.newListItem.get(k);
				// ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new
				// ReqStrGetGoodsPrice();
				// localReqStrGetGoodsPrice.setType(2);
				// localReqStrGetGoodsPrice.setCustomerid(this.doc.getCustomerid());
				// localReqStrGetGoodsPrice.setWarehouseid(localDefDocItem3.getWarehouseid());
				// localReqStrGetGoodsPrice.setGoodsid(localDefDocItem3.getGoodsid());
				// localReqStrGetGoodsPrice.setUnitid(localDefDocItem3.getUnitid());
				// localReqStrGetGoodsPrice.setPrice(0.0D);
				// localReqStrGetGoodsPrice.setIsdiscount(false);
				// localArrayList.add(localReqStrGetGoodsPrice);
				// }
				// PDH.show(this, new ProgressCallBack() {
				//
				// @Override
				// public void action() {
				// String localString2 = new
				// ServiceGoods().gds_GetMultiGoodsPrice(localArrayList, true,
				// true);
				// handlerGet.sendMessage(handlerGet.obtainMessage(1,
				// localString2));
				// }
				// });
				ishaschanged = true;
				setActionBarText();
				break;
			case 3:
				int j = data.getIntExtra("position", 0);
				DefDocItemXS localDefDocItem2 = (DefDocItemXS) data.getSerializableExtra("docitem");
				listItem.set(j, localDefDocItem2);
				adapter.setData(listItem);
				refreshUI();
				if (Utils.isCombination()) {
					combinationItem();
				}
				DBupdataDocItem();
				break;
			case 4:
				DefDocItemXS localDefDocItem4 = (DefDocItemXS) data.getSerializableExtra("docitem");
				adapter.addItem(localDefDocItem4);
				listView.setAdapter(adapter);
				refreshUI();
				if (Utils.isCombination()) {
					combinationItem();
				}
				DBupdataDocItem();
				break;

			case 7:
				List<DefDocItemXS> itemXSList = JSONUtil.str2list(data.getStringExtra("selecteditem"),
						DefDocItemXS.class);
				for (int i = 0; i < itemXSList.size(); i++) {
					DefDocItemXS item = new DefDocItemXS(itemXSList.get(i));
					item.setDocid(this.doc.getDocid());
					item.setItemid(0);
					listItem.add(item);
				}
				this.adapter.setData(this.listItem);
				listView.setAdapter(adapter);
				this.refreshUI();
				sum();
				if (Utils.isCombination()) {
					combinationItem();
				}
				DBupdataDocItem();
				break;
			case 8:
				// 支付
				String localString1 = data.getStringExtra("listpaytype");
				if (TextUtils.isEmptyS(localString1)) {
					this.listPayType = JSONUtil.str2list(localString1, DefDocPayType.class);
					double d = data.getDoubleExtra("preference", 0.0D);
					if (d != this.doc.getPreference()) {
						this.doc.setPreference(d);
					}
				}
				DBupdataDocItem();
				break;
			}
		}

	}

	protected void setAddItem(DefDocItemXS docItem) {
		// 折后小计
		docItem.setSubtotal(Utils.normalizeSubtotal(docItem.getNum() * docItem.getPrice()));
		// 重点查看是否有错误!
		docItem.setDiscountratio(doc.getDiscountratio());
		docItem.setIsgift(docItem.getPrice() == 0.0D ? true : false);
		docItem.setDiscountprice(Utils.normalizePrice(docItem.getPrice() * docItem.getDiscountratio()));
		docItem.setDiscountsubtotal(Utils.normalizeSubtotal(docItem.getNum() * docItem.getDiscountprice()));
		String bigNum = unitDao.getBigNum(docItem.getGoodsid(), docItem.getUnitid(), docItem.getNum());
		docItem.setBignum(bigNum);
	}

	// 价格处理
	// Handler handlerGet = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// String localString1 = msg.obj.toString();
	// List<ReqStrGetGoodsPrice> localList = null;
	// if (RequestHelper.isSuccess(localString1)) {
	// if (msg.what == 1) {
	// // 单价list
	// localList = JSONUtil.str2list(localString1,
	// ReqStrGetGoodsPrice.class);
	// for (int j = 0; j < newListItem.size(); j++) {
	// DefDocItemXS defDocItem = (DefDocItemXS) newListItem.get(j);
	// ReqStrGetGoodsPrice getGoodsPrice = (ReqStrGetGoodsPrice)
	// localList.get(j);
	// if
	// ((defDocItem.getGoodsid().equals(getGoodsPrice.getGoodsid()))
	// &&
	// (defDocItem.getUnitid().equals(getGoodsPrice.getUnitid()))) {
	// String localString2 = getGoodsPrice.getWarehouseid();
	// if ((TextUtils.isEmptyS(localString2))
	// && (localString2.equals(defDocItem.getWarehouseid()))) {
	// defDocItem.setWarehouseid(localString2);
	// defDocItem.setWarehousename(new
	// WarehouseDAO().getWarehouse(localString2).getName());
	// }
	// defDocItem.setPrice(Utils.normalizePrice(getGoodsPrice.getPrice()));
	// defDocItem
	// .setSubtotal(Utils.normalizeSubtotal(defDocItem.getNum() *
	// defDocItem.getPrice()));
	// defDocItem.setDiscountratio(doc.getDiscountratio());
	// defDocItem.setDiscountprice(
	// Utils.normalizePrice(defDocItem.getPrice() *
	// defDocItem.getDiscountratio()));
	// defDocItem.setDiscountsubtotal(
	// Utils.normalizeSubtotal(defDocItem.getNum() *
	// defDocItem.getDiscountprice()));
	// defDocItem.setIsgift(defDocItem.getPrice() == 0.0D ? true :
	// false);
	// String bigNum = unitDao.getBigNum(defDocItem.getGoodsid(),
	// defDocItem.getUnitid(),
	// defDocItem.getNum());
	// defDocItem.setBignum(bigNum);
	// defDocItem.setIsdiscount(getGoodsPrice.getIsdiscount());
	// }
	// }
	// if (Utils.isCombination()) {
	// listItem.addAll(newListItem);
	// combinationItem();
	// } else {
	// listItem.addAll(0, newListItem);
	// }
	// adapter.setData(listItem);
	// listView.setAdapter(adapter);
	// ishaschanged = true;
	// setActionBarText();
	// refreshUI();
	// DBupdataDocItem();
	// return;
	// }
	// if (msg.what == 2) {
	// DefDocItemXS localDefDocItem2 = (DefDocItemXS)
	// InDocEditActivity.this.newListItem.get(0);
	// localList = JSONUtil.str2list(localString1,
	// ReqStrGetGoodsPrice.class);
	// ReqStrGetGoodsPrice localReqStrGetGoodsPrice2 =
	// (ReqStrGetGoodsPrice) localList.get(0);
	// String localString3 =
	// localReqStrGetGoodsPrice2.getWarehouseid();
	// if ((!TextUtils.isEmptyS(localString3))
	// && (!localString3.equals(localDefDocItem2.getWarehouseid())))
	// {
	// localDefDocItem2.setWarehouseid(localString3);
	// localDefDocItem2.setWarehousename(new
	// WarehouseDAO().getWarehouse(localString3).getName());
	// }
	// localDefDocItem2.setPrice(Utils.normalizePrice(localReqStrGetGoodsPrice2.getPrice()));
	// localDefDocItem2.setSubtotal(
	// Utils.normalizeSubtotal(localDefDocItem2.getNum() *
	// localDefDocItem2.getPrice()));
	// localDefDocItem2.setDiscountratio(InDocEditActivity.this.doc.getDiscountratio());
	// localDefDocItem2.setDiscountprice(
	// Utils.normalizePrice(localDefDocItem2.getPrice() *
	// localDefDocItem2.getDiscountratio()));
	// localDefDocItem2.setDiscountsubtotal(
	// Utils.normalizeSubtotal(localDefDocItem2.getNum() *
	// localDefDocItem2.getDiscountprice()));
	// localDefDocItem2.setIsgift(localDefDocItem2.getPrice() == 0 ?
	// true : false);
	// localDefDocItem2.setIsdiscount(localReqStrGetGoodsPrice2.getIsdiscount());
	// Intent localIntent = new Intent();
	// localIntent.putExtra("docitem", localDefDocItem2);
	// localIntent.putExtra("customerid", doc.getCustomerid());
	// InDocEditActivity.this.startActivityForResult(
	// localIntent.setClass(InDocEditActivity.this,
	// InDocAddGoodAct.class), 4);
	// }
	// } else {
	// RequestHelper.showError(localString1);
	// }
	// }
	//
	// };
	// private BarcodeManager bm;
	private Dialog_listCheckBox dialog;
	MAlertDialog maler;
	private Button btnGoodClass;
	private GoodsUnitDAO unitDao;
	// private AccountPreference ap;
	private Sv_docitem sv;
	private DocContainerEntity docContainerEntity;
	// private GoodsPriceDAO goodspricedao;

	// /**
	// * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	// *
	// */
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// intenToMain();
	// return false;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	// }

	protected void DBupdataDocItem() {
		DocContainerEntity docEntity = new DocContainerEntity();
		// 保存到本地
		docEntity.setDeleteinitem(docContainerEntity.getDeleteinitem());
		docEntity.setDeleteitem(JSONUtil.object2Json(listItemDelete));
		docEntity.setDoc(JSONUtil.object2Json(doc));
		docEntity.setItem(JSONUtil.object2Json(listItem));
		docEntity.setDoctype(docContainerEntity.getDoctype());
		docEntity.setPaytype(JSONUtil.object2Json(listPayType));
		if (sv.queryDoc(docContainerEntity.getDoctype()) == null) {
			sv.insetDocItem(docEntity);
		} else {
			sv.updataDocItem(docEntity);
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 1.0F;
			getWindow().setAttributes(localLayoutParams);
		}
		return false;
	}

	@Override
	public void setActionBarText() {
		String showId = TextUtils.isEmpty(doc.getShowid()) ? "销售退货单" : doc.getShowid();
		if (this.ishaschanged) {
			getActionBar().setTitle(showId + "*");
		} else {
			getActionBar().setTitle(showId);
		}
	}
}
