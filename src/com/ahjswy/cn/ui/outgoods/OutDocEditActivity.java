package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.bean.Def_DocDraft;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.dao.Sv_docitem;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocPayType;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.model.FieldSaleForPrint;
import com.ahjswy.cn.model.FieldSaleItemForPrint;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.popupmenu.OutDocEditMenuPopup;
import com.ahjswy.cn.print.BTPrintHelper;
import com.ahjswy.cn.print.BTPrintHelper.PrintOverCall;
import com.ahjswy.cn.print.PrintMode;
import com.ahjswy.cn.request.ReqCustomerdebt;
import com.ahjswy.cn.response.RespServiceInfor;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceCustomer;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.MAlertDialog;
import com.ahjswy.cn.ui.SearchHelper;
import com.ahjswy.cn.ui.SwyMain;
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

import android.app.ActionBar;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OutDocEditActivity extends BaseActivity implements OnItemClickListener, OnTouchListener {
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
		intDate();
		refreshUI();
		bottomCount();
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
		ishaschanged = getIntent().getBooleanExtra("ishaschanged", true);
		docContainerEntity = (DocContainerEntity) getIntent().getSerializableExtra("docContainer");
		doc = ((DefDocXS) JSONUtil.readValue(docContainerEntity.getDoc(), DefDocXS.class));
		listItem = JSONUtil.str2list(docContainerEntity.getItem(), DefDocItemXS.class);
		listPayType = JSONUtil.str2list(docContainerEntity.getPaytype(), DefDocPayType.class);
		adapter = new OutDocItemAdapter(this);
		adapter.setData(listItem);
		listview_copy_dele.setAdapter(adapter);
		listItemDelete = new ArrayList<Long>();
		btnAdd.setOnClickListener(addGoodsClick);
		listview_copy_dele.setOnItemClickListener(this);
		dialog = new Dialog_listCheckBox(this);
		tv_Customer = (TextView) findViewById(R.id.tv_Customer);
		if (doc.isIsavailable() && !doc.isIsposted()) {
			queryCustomerdebt();
		}
		sv = new Sv_docitem();
		unitDAO = new GoodsUnitDAO();
		serviceStore = new ServiceStore();
	}

	protected void combinationItem() {
		int combinationNum = listItem.size();
		ArrayList<DefDocItemXS> data = new ArrayList<DefDocItemXS>(listItem);
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
				itemxs2.setBignum(unitDAO.getBigNum(itemxs2.getGoodsid(), itemxs2.getUnitid(), itemxs2.getNum()));
				itemxs2.setSubtotal(itemxs2.getNum() * itemxs2.getPrice());
				itemxs2.setDiscountsubtotal(itemxs2.getNum() * itemxs2.getPrice() * itemxs2.getDiscountratio());
				map.put(itemxs2.getGoodsid(), itemxs2);
				// 添加删除服务器数据
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
		showSuccess("同品增加成功!");
		// 设置数据
		listItem.clear();
		listItem.addAll(listDocItem);
	}

	/**
	 * 本地 sql 写入 与 更新
	 */
	public void setDBDoc() {
		try {
			DocContainerEntity docEntity = new DocContainerEntity();
			// 保存到本地
			docEntity.setDeleteinitem(docContainerEntity.getDeleteinitem());
			docEntity.setDeleteitem(JSONUtil.toJSONString(listItemDelete));
			docEntity.setDoc(JSONUtil.toJSONString(doc));
			docEntity.setItem(JSONUtil.toJSONString(listItem));
			docEntity.setDoctype(docContainerEntity.getDoctype());
			docEntity.setPaytype(JSONUtil.toJSONString(listPayType));
			if (sv.queryDoc(docContainerEntity.getDoctype()) == null) {
				sv.insetDocItem(docEntity);
			} else {
				sv.updataDocItem(docEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showError("网络链接不稳定!");
		}
	}

	private void queryCustomerdebt() {
		if (TextUtils.isEmptyS(doc.getCustomerid())) {
			// 查询客户 欠款
			String queryCustomerDebt = new ServiceCustomer()
					.cu_queryCustomer(new ReqCustomerdebt(doc.getCustomerid(), true, 0));
			if (RequestHelper.isSuccess(queryCustomerDebt)) {
				ReqCustomerdebt reqCustomerdebt = JSONUtil.readValue(queryCustomerDebt, ReqCustomerdebt.class);
				tv_Customer.setVisibility(View.VISIBLE);
				tv_Customer.setText("客户欠款: " + reqCustomerdebt.amount + " 元");
			} else {
				tv_Customer.setText("客户欠款: ? 元");
			}
		}
	}

	private void intDate() {
		SwipeMenuCreator local5 = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
				item1.setTitle("复制");
				item1.setTitleSize(14);
				item1.setTitleColor(-16777216);
				item1.setWidth(100);
				item1.setBackground(new ColorDrawable(Color.rgb(48, 177, 245)));
				menu.addMenuItem(item1);
				SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
				item2.setTitle("删除");
				item2.setTitleSize(14);
				item2.setTitleColor(-16777216);
				item2.setBackground(new ColorDrawable(Color.rgb(201, 201, 206)));
				item2.setWidth(100);
				menu.addMenuItem(item2);
			}
		};
		listview_copy_dele.setMenuCreator(local5);
		listview_copy_dele.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (listItem.size() == 0) {
					return false;
				}
				switch (index) {
				case 0:
					DefDocItemXS defdocitemxs = new DefDocItemXS(listItem.get(position));
					defdocitemxs.setItemid(0L);
					defdocitemxs.setTempitemid(1L + getMaxTempItemId());
					listItem.add(defdocitemxs);
					adapter.setData(listItem);
					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.notifyDataSetChanged();
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							bottomCount();
							setDBDoc();
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
					adapter.setData(listItem);
					handler.postDelayed(new Runnable() {
						public void run() {
							adapter.notifyDataSetChanged();
							refreshUI();
							ishaschanged = true;
							setActionBarText();
							bottomCount();
							setDBDoc();

						}
					}, 180L);
					break;
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			if (isScanerBarcode) {
				showError("数据处理中....");
				return;
			}
			// 已经过账的单据 不允许继续扫描开单
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
	boolean isScanerBarcode = false;

	private void readBarcode(String barcode) {

		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
		if (goodsThinList.size() == 1) {
			long l = getMaxTempItemId();
			int num = Utils.isCombination() ? 1 : 0;
			DefDocItemXS fillItem = fillItem(goodsThinList.get(0), num, 0.0D, l + 1L);
			localArrayList.add(fillItem);
			Intent intent = new Intent(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class);
			intent.putExtra("items", JSONUtil.toJSONString(localArrayList));
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
						int num = Utils.isCombination() ? 1 : 0;
						DefDocItemXS fillItem = fillItem(select.get(i), num, 0.0D, l);
						localArrayList.add(fillItem);
					}
					if (localArrayList.isEmpty()) {
						return;
					}
					Intent intent = new Intent(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class);
					intent.putExtra("items", JSONUtil.toJSONString(localArrayList));
					intent.putExtra("doc", doc);
					startActivityForResult(intent, 1);
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		scaner.removeListener();
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

	View.OnClickListener addGoodsClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			atvSearch.clearFocus();
			if ((menuPopup != null) && (menuPopup.isShowing())) {
				menuPopup.dismiss();
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.alpha = 1.0F;
				getWindow().setAttributes(localLayoutParams);
			}
			List<GoodsThin> localList = searchHelper.getAdapter().getSelect();
			if ((localList == null) || (localList.size() == 0)) {
				PDH.showMessage("请选择商品！");
				return;
			}
			atvSearch.setText("");
			// if (!"1".equals(ap.getValue("goods_select_more"))) {
			ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
			long l = getMaxTempItemId();
			for (int i = 0; i < localList.size(); i++) {
				GoodsThin localGoodsThin = (GoodsThin) localList.get(i);
				l += 1L;
				int num = Utils.isCombination() ? 1 : 0;
				DefDocItemXS item = fillItem(localGoodsThin, num, 0.0D, l);
				if (item != null) {
					localArrayList.add(item);
				} else {
					PDH.showMessage("商品有误!");
					return;
				}
			}
			Intent intent = new Intent(OutDocEditActivity.this, OutDocAddMoreGoodsAct.class);
			intent.putExtra("items", JSONUtil.toJSONString(localArrayList));
			intent.putExtra("doc", doc);
			startActivityForResult(intent, 1);
			// }

		}
	};

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
		Intent localIntent = new Intent();
		localIntent.putExtra("customerid", doc.getCustomerid());
		localIntent.putExtra("positiongive", -1);
		localIntent.putExtra("docitemgive", "");
		localIntent.putExtra("position", position);
		localIntent.putExtra("docitem", defdocitemxs);
		// 商品详情
		startActivityForResult(localIntent.setClass(this, OutDocAddGoodAct.class), 2);
	}

	public DefDocXS getDoc() {
		return this.doc;
	}

	// Handler handlerGet = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// String localString1 = msg.obj.toString();
	// List<ReqStrGetGoodsPrice> localList = null;
	// if (RequestHelper.isSuccess(localString1)) {
	// if (msg.what == 0) {
	// localList = JSONUtil.str2list(localString1, ReqStrGetGoodsPrice.class);
	// if (newListItem != null) {
	// for (int i = 0; i < newListItem.size(); i++) {
	// DefDocItemXS itemXS1 = (DefDocItemXS) newListItem.get(i);
	// for (int j = 0; j < localList.size(); j++) {
	// ReqStrGetGoodsPrice price1 = (ReqStrGetGoodsPrice) localList.get(j);
	// if ((itemXS1.getGoodsid().equals(price1.getGoodsid()))
	// && (itemXS1.getUnitid().equals(price1.getUnitid()))) {
	// String localString2 = price1.getWarehouseid();
	// if ((TextUtils.isEmptyS(localString2))
	// && (localString2.equals(itemXS1.getWarehouseid()))) {
	// itemXS1.setWarehouseid(localString2);
	// itemXS1.setWarehousename(
	// new WarehouseDAO().getWarehouse(localString2).getName());
	// }
	// itemXS1.setIsdiscount(price1.getIsdiscount());
	// //
	// itemXS1.setPrice(Utils.normalizePrice(Utils.normalizePrice(price1.getPrice())));
	// itemXS1.setSubtotal(Utils.normalizePrice(itemXS1.getNum() *
	// itemXS1.getPrice()));
	// if (itemXS1.isIsdiscount()) {
	// itemXS1.setDiscountratio(doc.getDiscountratio());
	// }
	// if (itemXS1.isIsusebatch()) {
	// itemXS1.setBatch(price1.getBatch());
	// itemXS1.setProductiondate(price1.getProductiondate());
	// itemXS1.setWarehouseid(price1.getWarehouseid());
	// }
	// // if
	// // (!TextUtils.isEmptyS(price1.getWarehouseid()))
	// // {
	// // Warehouse localWarehouse = new
	// // WarehouseDAO()
	// // .getWarehouse(price1.getWarehouseid());
	// // if (localWarehouse != null) {
	// // itemXS1.setWarehousename(localWarehouse.getName());
	// // }
	// //
	// // }
	// itemXS1.setDiscountprice(
	// Utils.normalizePrice(itemXS1.getPrice() * itemXS1.getDiscountratio()));
	// itemXS1.setDiscountsubtotal(
	// Utils.normalizeSubtotal(itemXS1.getNum() * itemXS1.getDiscountprice()));
	// itemXS1.setIsgift(itemXS1.getPrice() == 0.0D ? true : false);
	// String bigNum = unitDAO.getBigNum(itemXS1.getGoodsid(),
	// itemXS1.getUnitid(),
	// itemXS1.getNum());
	// itemXS1.setBignum(bigNum);
	// itemXS1.setIsdiscount(price1.getIsdiscount());
	//
	// }
	// }
	//
	// }
	// }
	// if (newListItem.size() > 0) {
	// if (Utils.isCombination()) {
	// listItem.addAll(newListItem);
	// combinationItem();
	// } else {
	// listItem.addAll(0, newListItem);
	// }
	// adapter.setData(listItem);
	// listview_copy_dele.setAdapter(adapter);
	// ishaschanged = true;
	// setActionBarText();
	// bottomCount();
	// refreshUI();
	// setDBDoc();
	// }
	// return;
	// }
	// if (msg.what == 2) {
	// DefDocItemXS itemXS2 = (DefDocItemXS) newListItem.get(0);
	// localList = JSONUtil.str2list(localString1, ReqStrGetGoodsPrice.class);
	// ReqStrGetGoodsPrice goodsPrice2 = (ReqStrGetGoodsPrice) localList.get(0);
	// String localString3 = goodsPrice2.getWarehouseid();
	// if ((TextUtils.isEmptyS(localString3)) &&
	// (localString3.equals(itemXS2.getWarehouseid()))) {
	// itemXS2.setWarehouseid(localString3);
	// itemXS2.setWarehousename(new
	// WarehouseDAO().getWarehouse(localString3).getName());
	// }
	// itemXS2.setIsdiscount(goodsPrice2.getIsdiscount());
	// itemXS2.setPrice(Utils.normalizePrice(goodsPrice2.getPrice()));
	// itemXS2.setSubtotal(Utils.normalizeSubtotal(itemXS2.getNum() *
	// itemXS2.getPrice()));
	// itemXS2.setBatch(goodsPrice2.getBatch());
	// itemXS2.setProductiondate(goodsPrice2.getProductiondate());
	//
	// itemXS2.setDiscountratio(doc.getDiscountratio());
	// itemXS2.setDiscountprice(Utils.normalizePrice(itemXS2.getPrice() *
	// itemXS2.getDiscountratio()));
	// itemXS2.setDiscountsubtotal(Utils.normalizeSubtotal(itemXS2.getNum() *
	// itemXS2.getDiscountprice()));
	// if (itemXS2.isIsdiscount()) {
	// itemXS2.setIsdiscount(goodsPrice2.getIsdiscount());
	// }
	// DefDocItemXS itemXS3 = new DefDocItemXS(itemXS2);
	// if (itemXS2.getPrice() == 0.0D) {
	// itemXS2.setIsgift(true);
	// itemXS3.setTempitemid(1L + itemXS2.getTempitemid());
	// itemXS3.setNum(0.0D);
	// itemXS3.setSubtotal(0.0D);
	// itemXS3.setDiscountratio(1.0D);
	// itemXS3.setDiscountprice(0.0D);
	// itemXS3.setDiscountsubtotal(0.0D);
	// }
	// if (itemXS2.isIsusebatch()) {
	// itemXS3.setBatch(goodsPrice2.getBatch());
	// itemXS3.setProductiondate(goodsPrice2.getProductiondate());
	// }
	// Intent localIntent = new Intent();
	// localIntent.putExtra("customerid", doc.getCustomerid());
	// localIntent.putExtra("position", 0);
	// localIntent.putExtra("docitem", itemXS2);
	// // localIntent.putExtra("positiongive", -1);
	// // localIntent.putExtra("docitemgive", localDefDocItemXS3);
	// startActivityForResult(localIntent.setClass(OutDocEditActivity.this,
	// OutDocAddGoodAct.class), 4);
	// return;
	// }
	// } else {
	// RequestHelper.showError(localString1);
	// }
	// };
	//
	// };
	// ArrayList<ReqStrGetGoodsPrice> localArrayList;
	private AdapterView.OnItemClickListener onItemClickListeners = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
					DefDocItemXS docItem = fillItem(localGoodsThin, 0, 0.0D, l + 1L);
					if (docItem == null) {
						return;
					}
					// ArrayList<DefDocItemXS> newListItem = new
					// ArrayList<DefDocItemXS>();
					// newListItem.add(docItem);
					docItem.setPrice(DocUtils.getGoodsPrice(doc.getCustomerid(), docItem));
					// 改动较大 待测试 查询商品单价
					setAddItem(docItem);
					Intent localIntent = new Intent();
					localIntent.putExtra("customerid", doc.getCustomerid());
					localIntent.putExtra("position", 0);
					localIntent.putExtra("docitem", docItem);
					startActivityForResult(localIntent.setClass(OutDocEditActivity.this, OutDocAddGoodAct.class), 4);

					// localArrayList = new ArrayList<ReqStrGetGoodsPrice>();
					// ReqStrGetGoodsPrice goodsPrice = new
					// ReqStrGetGoodsPrice();
					// goodsPrice.setType(1);
					// goodsPrice.setCustomerid(doc.getCustomerid());
					// goodsPrice.setWarehouseid(doc.getWarehouseid());
					// goodsPrice.setGoodsid(docItem.getGoodsid());
					// goodsPrice.setUnitid(docItem.getUnitid());
					// goodsPrice.setPrice(0.0D);
					// goodsPrice.setIsdiscount(false);
					// localArrayList.add(goodsPrice);
					// String localString = new
					// ServiceGoods().gds_GetMultiGoodsPrice(localArrayList,
					// true,
					// docItem.isIsusebatch());
					// handlerGet.sendMessage(handlerGet.obtainMessage(2,
					// localString));
				}

			});

		}

	};

	private void setAddItem(DefDocItemXS docItem) {
		// 辅助计件单位
		docItem.assistnum = DocUtils.getAssistnum(docItem.getGoodsid(), docItem.getUnitid(), docItem.getNum());
		// 折后小计
		docItem.setSubtotal(Utils.normalizeSubtotal(docItem.getNum() * docItem.getPrice()));
		// 折扣
		docItem.setDiscountratio(doc.getDiscountratio());
		// 是否赠送
		docItem.setIsgift(docItem.getPrice() == 0.0D ? true : false);
		docItem.setDiscountprice(Utils.normalizePrice(docItem.getPrice() * docItem.getDiscountratio()));
		docItem.setDiscountsubtotal(Utils.normalizeSubtotal(docItem.getNum() * docItem.getDiscountprice()));
		String bigNum = unitDAO.getBigNum(docItem.getGoodsid(), docItem.getUnitid(), docItem.getNum());
		docItem.setBignum(bigNum);
	}

	// public void getGoodsPrice(final List<ReqStrGetGoodsPrice> re) {
	//
	// PDH.show(this, new ProgressCallBack() {
	//
	// @Override
	// public void action() {
	// String localString = new ServiceGoods().gds_GetMultiGoodsPrice(re, true,
	// true);
	// handlerGet.sendMessage(handlerGet.obtainMessage(1, localString));
	// }
	// });
	// }

	DefDocItemXS fillItem(GoodsThin paramGoodsThin, double num, double price, long tempitemid) {
		DefDocItemXS itemxs = new DefDocItemXS();
		itemxs.setItemid(0L);
		itemxs.setTempitemid(tempitemid);
		itemxs.setDocid(doc.getDocid());
		itemxs.setGoodsid(paramGoodsThin.getId());
		itemxs.setGoodsname(paramGoodsThin.getName());
		itemxs.setBarcode(paramGoodsThin.getBarcode());
		itemxs.setSpecification(paramGoodsThin.getSpecification());
		itemxs.setModel(paramGoodsThin.getModel());
		itemxs.setWarehouseid(doc.getWarehouseid());
		itemxs.setWarehousename(doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = unitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = unitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		if (localGoodsUnit == null) {
			return null;
		}
		itemxs.setUnitid(localGoodsUnit.getUnitid());
		itemxs.setUnitname(localGoodsUnit.getUnitname());
		itemxs.unit = localGoodsUnit;
		itemxs.setNum(Utils.normalize(num, 2));
		itemxs.setBignum(unitDAO.getBigNum(itemxs.getGoodsid(), itemxs.getUnitid(), itemxs.getNum()));
		// 价格
		itemxs.setPrice(Utils.normalizePrice(price));
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

	// @SuppressWarnings("unused")
	// private DefDocItemXS fillItem(RespPromotionRule paramRespPromotionRule,
	// double paramDouble, long paramLong) {
	// GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
	// DefDocItemXS localDefDocItemXS = new DefDocItemXS();
	// localDefDocItemXS.setItemid(0L);
	// localDefDocItemXS.setTempitemid(paramLong);
	// localDefDocItemXS.setDocid(this.doc.getDocid());
	// localDefDocItemXS.setGoodsid(paramRespPromotionRule.getGoodsid());
	// localDefDocItemXS.setGoodsname(paramRespPromotionRule.getGoodsname());
	// localDefDocItemXS.setBarcode(paramRespPromotionRule.getBarcode());
	// localDefDocItemXS.setSpecification(paramRespPromotionRule.getSpecification());
	// localDefDocItemXS.setModel(paramRespPromotionRule.getModel());
	// localDefDocItemXS.setWarehouseid(this.doc.getWarehouseid());
	// localDefDocItemXS.setWarehousename(this.doc.getWarehousename());
	// localDefDocItemXS.setUnitid(paramRespPromotionRule.getUnitid());
	// localDefDocItemXS.setUnitname(paramRespPromotionRule.getUnitname());
	// localDefDocItemXS.setNum(Utils.normalize(paramDouble, 2));
	// localDefDocItemXS.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItemXS.getGoodsid(),
	// localDefDocItemXS.getUnitid(), localDefDocItemXS.getNum()));
	// localDefDocItemXS.setPrice(Utils.normalizePrice(paramRespPromotionRule.getPrice()));
	// localDefDocItemXS
	// .setSubtotal(Utils.normalizeSubtotal(localDefDocItemXS.getNum() *
	// localDefDocItemXS.getPrice()));
	// localDefDocItemXS.setDiscountratio(1.0D);
	// localDefDocItemXS.setDiscountprice(localDefDocItemXS.getPrice());
	// localDefDocItemXS.setDiscountsubtotal(localDefDocItemXS.getSubtotal());
	// if (localDefDocItemXS.getPrice() == 0.0D) {
	// localDefDocItemXS.setIsgift(true);
	// localDefDocItemXS.setCostprice(0.0D);
	// localDefDocItemXS.setRemark("");
	// localDefDocItemXS.setRversion(0L);
	// localDefDocItemXS.setIsdiscount(false);
	// localDefDocItemXS.setIsexhibition(false);
	// localDefDocItemXS.setIspromotion(true);
	// localDefDocItemXS.setParentitemid(0L);
	// localDefDocItemXS.setPromotiontype(0);
	// localDefDocItemXS.setPromotiontypename("特惠");
	// localDefDocItemXS.setOutorderdocid(0L);
	// localDefDocItemXS.setOutorderdocshowid(null);
	// localDefDocItemXS.setOutorderitemid(0L);
	// localDefDocItemXS.setBatch(paramRespPromotionRule.getBatch());
	// localDefDocItemXS.setProductiondate(paramRespPromotionRule.getProductiondate());
	// }
	//
	// return localDefDocItemXS;
	// }

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
		for (DefDocItemXS item : listItem) {
			item.assistnum = DocUtils.getAssistnum(item.getGoodsid(), item.getUnitid(), item.getNum());
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
		localIntent.putExtra("listpaytype", JSONUtil.toJSONString(this.listPayType));
		startActivityForResult(localIntent.setClass(this, OutDocPayAct.class), 8);
	}

	public void getCustomerHistory() {
		if (TextUtils.isEmptyS(this.doc.getCustomerid())) {
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
		for (DefDocItemXS item : listItem) {
			if (item.assistnum == 0) {
				item.assistnum = DocUtils.getAssistnum(item.getGoodsid(), item.getUnitid(), item.getNum());
			}
		}

		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				String localString = serviceStore.str_SaveXSDoc(doc, adapter.getData(), listPayType, listItemDelete);
				handler.sendMessage(handler.obtainMessage(0, localString));
			}
		});
	}

	// private int BLUE_STATE = 0;
	// private int BLUE_Print = 1;

	// 蓝牙过账/打印
	public void blueDevicePrint() {
		if (doc.isIsposted()) {
			bluePrint();
		} else {
			check(false);
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
		for (int i = 0; i < listItem.size(); i++) {
			DefDocItemXS item = listItem.get(i);
			FieldSaleItemForPrint itemForPrint = new FieldSaleItemForPrint();
			itemForPrint.setBarcode(item.getBarcode());
			itemForPrint.setDiscountratio(item.getDiscountprice());
			itemForPrint.setDiscountsubtotal(item.getDiscountsubtotal());
			itemForPrint.setGoodsid(item.getGoodsid());
			itemForPrint.setGoodsname(item.getGoodsname());
			itemForPrint.setItemtype(i + 1 + "");
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
		FieldSaleForPrint printItem = new FieldSaleForPrint();
		// v2.setId(doc.getId());
		printItem.setDoctype("销售单");
		printItem.setShowid(doc.getShowid());
		printItem.setCustomername(this.doc.getCustomername());
		printItem.setDepartmentname(this.doc.getDepartmentname());
		printItem.setBuildername(this.doc.getBuildername());
		printItem.setBuildtime(Utils.formatDate(this.doc.getBuildtime(), "yyyy-MM-dd HH:mm:ss"));
		printItem.setSumamount("合计:" + getSumMoney());
		printItem.setPreference("优惠:" + this.doc.getPreference());
		printItem.setReceivable("应收:" + Utils.getSubtotalMoney(getSumMoney() - doc.getPreference()));
		printItem.setReceived("已收:" + Utils.getRecvableMoney(received));
		printItem.setNum("数量:" + String.valueOf(0));
		printItem.setRemark(doc.getRemark());
		return printItem;
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
		sv.deleteDoc(docContainerEntity.getDoctype());
	}

	// 属性
	public void docProperty() {
		Intent localIntent = new Intent(this, OutDocOpenActivity.class);
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
		if (!TextUtils.isEmptyS(doc.getCustomerid())) {
			return "没有选择客户!";
		}
		if (!TextUtils.isEmptyS(this.doc.getDepartmentid())) {
			return "部门不能为空!";
		}
		if ((this.doc.getDiscountratio() <= 0.0D) || (this.doc.getDiscountratio() > 1.0D)) {
			return "整单折扣必须大于0且小于等于1!";
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
			DefDocItemXS localDefDocItemXS = listItem.get(k);
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
					sv.deleteDoc(docContainerEntity.getDoctype());
					return;
				}
				// boolean isSort = false;
				DocContainerEntity dce = (DocContainerEntity) JSONUtil.fromJson(localString, DocContainerEntity.class);
				if (dce == null) {
					return;
				}
				doc = JSONUtil.readValue(dce.getDoc(), DefDocXS.class);
				listPayType = JSONUtil.str2list(dce.getPaytype(), DefDocPayType.class);
				// if (getMaxTempItemId() != 0) {
				// isSort = true;
				// }
				listItem = JSONUtil.str2list(dce.getItem(), DefDocItemXS.class);
				listItemDelete = new ArrayList<Long>();
				switch (msg.what) {
				case 0:
					// if (isSort) {
					// Collections.sort(listItem);
					// }
					adapter.setData(listItem);
					PDH.showSuccess("保存成功");
					ishaschanged = false;
					setActionBarText();
					adapter.notifyDataSetChanged();
					if (check) {
						printDocHandler.sendEmptyMessage(0);
					}
					// setDBDoc();
					sv.deleteDoc(docContainerEntity.getDoctype());
					startActivity(new Intent(OutDocEditActivity.this, SwyMain.class));
					finish();
					return;
				case 1:
					if ((doc.isIsavailable()) && (doc.isIsposted())) {
						PDH.showSuccess("过账成功");
						if (DocUtils.isBluetoothPrint()) {
							bluePrint();
						}
						startActivity(new Intent(OutDocEditActivity.this, SwyMain.class));
						// finish();
						sv.deleteDoc(docContainerEntity.getDoctype());
						return;
					}
					adapter.setData(listItem);
					refreshUI();
					RequestHelper.showError(dce.getInfo());
					return;
				default:
					break;
				}
			} else {
				RequestHelper.showError(localString);
			}
		}
	};
	private Handler printDocHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RespServiceInfor printDoc = printDoc();
			if (printDoc != null && printDoc.Result) {
				// 打印单据
				showSuccess("打印成功!");
				startActivity(new Intent(OutDocEditActivity.this, SwyMain.class));
				finish();
			} else {
				showError("打印失败!服务请开启！");
			}
		};
	};

	// 保存打印
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
		Map<String, Object> map = new HashMap<>();
		for (DefDocItemXS itemXS : listItem) {
			sumMoney += itemXS.getDiscountsubtotal();
			sumNum += itemXS.getNum();
			map.put(itemXS.getGoodsid(), itemXS);
		}
		btnGoodClass.setText("品种:" + map.size());
		bt_sumNumber.setText("数量:" + sumNum);
		bt_totalSum.setText("总价:\n" + Utils.normalizePrice(sumMoney) + "元");
	}

	/**
	 * 当前总金额
	 * 
	 * @return
	 */
	public double getSumMoney() {
		return sumMoney;
	}

	// private List<DefDocItemXS> newListItem;
	private Button bt_sumNumber;
	private Button bt_totalSum;
	private Dialog_listCheckBox dialog;
	private Button btnGoodClass;
	private double preference;// 优惠
	private double received;// 已收
	private Scaner scaner;
	private TextView tv_Customer;
	private Sv_docitem sv;
	private DocContainerEntity docContainerEntity;
	private GoodsUnitDAO unitDAO;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				doc = (DefDocXS) data.getSerializableExtra("doc");
				ishaschanged = true;
				setActionBarText();
				bottomCount();
				queryCustomerdebt();
				setDBDoc();
				break;
			case 1:
				isScanerBarcode = true;
				PDH.show(this, "数据查询中...", new ProgressCallBack() {

					@Override
					public void action() {
						List<DefDocItemXS> newListItem = JSONUtil.str2list(data.getStringExtra("items"),
								DefDocItemXS.class);
						for (int i = 0; i < newListItem.size(); i++) {
							DefDocItemXS itemXS = newListItem.get(i);
							setAddItem(itemXS);
						}
						if (Utils.isCombination()) {
							listItem.addAll(newListItem);
							combinationItem();
						} else {
							listItem.addAll(0, newListItem);
						}
						adapter.setData(listItem);
						handlerItem.sendEmptyMessage(0);
					}
				});

				// List<UnitidPrice> price = new ArrayList<UnitidPrice>();
				// for (DefDocItemXS item : newListItem) {
				// UnitidPrice unitidPrice = new UnitidPrice();
				// unitidPrice.setCustomerid(doc.getCustomerid());
				// unitidPrice.setGoodsid(item.getGoodsid());
				// unitidPrice.setUnitid(item.getUnitid());
				// price.add(unitidPrice);
				// }
				// List<UnitidPrice> historyPrice =
				// DocUtils.getCustomerGoodsHistoryPrice(price);

				// final ArrayList<ReqStrGetGoodsPrice> localArrayList = new
				// ArrayList<ReqStrGetGoodsPrice>();
				// for (int i = 0; i < newListItem.size(); i++) {
				// DefDocItemXS localDefDocItemXS5 = (DefDocItemXS)
				// this.newListItem.get(i);
				// ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new
				// ReqStrGetGoodsPrice();
				// localReqStrGetGoodsPrice.setType(1);
				// localReqStrGetGoodsPrice.setCustomerid(this.doc.getCustomerid());
				// localReqStrGetGoodsPrice.setWarehouseid(localDefDocItemXS5.getWarehouseid());
				// localReqStrGetGoodsPrice.setGoodsid(localDefDocItemXS5.getGoodsid());
				// localReqStrGetGoodsPrice.setUnitid(localDefDocItemXS5.getUnitid());
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
				// handlerGet.sendMessage(handlerGet.obtainMessage(0,
				// localString2));
				// }
				// });
				break;
			case 2:
				int j = data.getIntExtra("position", 0);
				DefDocItemXS localDefDocItemXS3 = (DefDocItemXS) data.getSerializableExtra("docitem");
				// listItem = adapter.getData();
				listItem.set(j, localDefDocItemXS3);
				// int k = data.getIntExtra("positiongive", -1);
				// DefDocItemXS localDefDocItemXS4;
				// if ((localDefDocItemXS3.isIspromotion()) &&
				// (localDefDocItemXS3.getPromotiontype() == 1)) {
				// localDefDocItemXS4 = (DefDocItemXS)
				// data.getSerializableExtra("docitemgive");
				// if (k >= 0) {
				// if ((localDefDocItemXS4 != null) &&
				// (localDefDocItemXS4.getNum() > 0.0D)) {
				// this.listItem.set(k, localDefDocItemXS4);
				// }
				// }
				// }
				if (Utils.isCombination()) {
					combinationItem();
				}
				adapter.setData(listItem);
				// listview_copy_dele.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ishaschanged = true;
				bottomCount();
				refreshUI();
				setDBDoc();

				break;
			case 3:
				doc = (DefDocXS) data.getSerializableExtra("doc");
				setDBDoc();
				break;
			case 4:
				DefDocItemXS defDocItemXS4 = (DefDocItemXS) data.getSerializableExtra("docitem");
				defDocItemXS4.assistnum = DocUtils.getAssistnum(defDocItemXS4.getGoodsid(), defDocItemXS4.getUnitid(),
						defDocItemXS4.getNum());
				if (Utils.isCombination()) {
					listItem.add(defDocItemXS4);
					combinationItem();
				} else {
					listItem.add(0, defDocItemXS4);
				}
				adapter.setData(listItem);
				listview_copy_dele.setAdapter(adapter);
				ishaschanged = true;
				refreshUI();
				bottomCount();
				setDBDoc();
				break;
			case 7:
				List<DefDocItemXS> itemXSList = JSONUtil.str2list(data.getStringExtra("selecteditem"),
						DefDocItemXS.class);
				long itemId = this.getMaxTempItemId();
				for (int i = 0; i < itemXSList.size(); i++) {
					DefDocItemXS item = new DefDocItemXS(itemXSList.get(i));
					item.setDocid(this.doc.getDocid());
					item.setItemid(0);
					item.setTempitemid(itemId);
					listItem.add(item);
				}
				this.adapter.setData(this.listItem);
				listview_copy_dele.setAdapter(adapter);
				this.refreshUI();
				bottomCount();
				if (Utils.isCombination()) {
					combinationItem();
				}
				setDBDoc();
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
				setDBDoc();
				break;
			}
		}
	}

	Handler handlerItem = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ishaschanged = true;
				setActionBarText();
				bottomCount();
				refreshUI();
				setDBDoc();
				adapter.notifyDataSetChanged();
				isScanerBarcode = false;
				break;

			default:
				break;
			}
		};
	};

	private void intenToMain() {
		if (ishaschanged) {
			MAlertDialog maler = new MAlertDialog(this);
			maler.setMessage("是否保存当前单据?");
			maler.setNeutralButton(new MAlertDialog.OnClickListener() {

				@Override
				public void onClick(MAlertDialog dialog) {
					dialog.dismiss();
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
										sv.deleteDoc(docContainerEntity.getDoctype());
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
			maler.setNegativeButton(new MAlertDialog.OnClickListener() {

				@Override
				public void onClick(MAlertDialog dialog) {
					dialog.dismiss();
					sv.deleteDoc(docContainerEntity.getDoctype());
					Intent intent = new Intent(OutDocEditActivity.this, SwyMain.class);
					startActivity(intent);
					finish();

				}
			});
			maler.show();
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
			this.menuPopup.showAtLocation(listview_copy_dele, Gravity.BOTTOM, 0, 0);
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
		String showid = TextUtils.isEmpty(doc.getShowid()) ? "销售开单" : doc.getShowid();
		if (ishaschanged) {
			localActionBar.setTitle(showid + "*");
		} else {
			localActionBar.setTitle(showid);
		}
	}

	/**
	 * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	 * 
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// if (ishaschanged) {
	// intenToMain();
	// } else {
	// setResult(RESULT_FIRST_USER);
	// finish();
	// }
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	// }
}
