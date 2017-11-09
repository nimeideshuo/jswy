package com.ahjswy.cn.ui.ingoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDoc;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_listCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/*
 * 销售退货==商品添加
 */
public class InDocAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItemXS> items;
	private ListView listView;
	private InDocAddMoreAdapter adapter;
	private DefDoc doc;
	Scaner scaner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_indoc_add_more_goods);
		initView();
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
	}

	private void initView() {
		items = JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItemXS.class);
		doc = (DefDoc) getIntent().getSerializableExtra("doc");
		listView = ((ListView) findViewById(R.id.listView));
		adapter = new InDocAddMoreAdapter(this);
		adapter.setData(items);
		listView.setAdapter(adapter);
		dialog = new Dialog_listCheckBox(this);
		ap = new AccountPreference();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		scaner.removeListener();
	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			if (dialog != null) {
				dialog.dismiss();
			}
			readBarcode(barcode);
		}
	};

	private void readBarcode(String barcode) {
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final Map<String, Object> goodsMap = new HashMap<String, Object>();
		if (goodsThinList.size() == 1) {
			int num = Utils.isCombination() ? 1 : 0;
			DefDocItemXS defdocitem = fillItem(goodsThinList.get(0), num, 0.0D);
			if (Utils.isCombination()) {
				goodsMap.put(defdocitem.getGoodsid(), defdocitem);
				combinationItem(goodsMap);
			} else {
				adapter.addData(defdocitem);
			}
			adapter.notifyDataSetInvalidated();
		} else if (goodsThinList.size() > 1) {
			dialog.setGoods(goodsThinList);
			dialog.setTempGoods(goodsThinList);
			dialog.ShowMe();
			dialog.ensure(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					List<GoodsThin> select = dialog.getSelect();
					for (int i = 0; i < select.size(); i++) {
						int num = Utils.isCombination() ? 1 : 0;
						DefDocItemXS defdocitem = fillItem(select.get(i), num, 0.0D);
						if (Utils.isCombination()) {
							goodsMap.put(defdocitem.getGoodsid(), defdocitem);
							combinationItem(goodsMap);
						} else {
							adapter.addData(defdocitem);
						}
					}
					adapter.notifyDataSetInvalidated();
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				listView.setSelection(items.size());
			}
		}, 200);

	}

	private void combinationItem(Map<String, Object> goodsMap) {
		List<DefDocItemXS> data = adapter.getData();
		for (int i = 0; i < data.size(); i++) {
			DefDocItemXS itemXS = data.get(i);
			if (goodsMap.get(itemXS.getGoodsid()) != null) {
				itemXS.setNum(itemXS.getNum() + 1);
				showError("相同商品数量+1");
				goodsMap.remove(itemXS.getGoodsid());
			}
		}
		for (Object item : goodsMap.values()) {
			adapter.addData((DefDocItemXS) item);
		}
	}

	// protected void addItems(Map<String, Object> goodsMap) {
	// List<DefDocItemXS> data = adapter.getData();
	// for (int i = 0; i < data.size(); i++) {
	// DefDocItemXS itemXS = data.get(i);
	// if (goodsMap.get(itemXS.getGoodsid()) != null) {
	// itemXS.setNum(itemXS.getNum() + 1);
	// showError("相同商品数量+1");
	// goodsMap.remove(itemXS.getGoodsid());
	// }
	// }
	// for (Object item : goodsMap.values()) {
	// adapter.addData((DefDocItemXS) item);
	// }
	// }

	// protected void setInitItem(DefDocItemXS item) {
	// String localString = new
	// ServiceGoods().gds_GetGoodsWarehouses(item.getGoodsid(),
	// item.isIsusebatch());
	// if (RequestHelper.isSuccess(localString)) {
	// List<RespGoodsWarehouse> goodsWarehouses = JSONUtil.str2list(localString,
	// RespGoodsWarehouse.class);
	// for (int i = 0; i < goodsWarehouses.size(); i++) {
	// if (item.getGoodsid().equals(goodsWarehouses.get(i).getGoodsid())
	// && item.getWarehouseid().equals(goodsWarehouses.get(i).getWarehouseid()))
	// {
	// RespGoodsWarehouse res = goodsWarehouses.get(i);
	// String bigstocknum = res.getBigstocknum().length() == 0 ? "0" +
	// item.getUnitname()
	// : res.getBigstocknum();
	// item.goodStock = bigstocknum;
	// }
	// }
	// } else {
	// showError("没有获取到库存数据!请重试!");
	// }
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setTitle("确定").setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
			break;
		case 0:
			tv_title_start();
			break;
		}

		return true;
	}

	private void tv_title_start() {
		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
				List<DefDocItemXS> data = adapter.getData();
				for (int i = 0; i < data.size(); i++) {
					DefDocItemXS localDefDocItem = (DefDocItemXS) data.get(i);
					if (localDefDocItem.getNum() > 0.0D) {
						localArrayList.add(localDefDocItem);
					}
				}
				if (localArrayList.size() <= 0) {
					// handler.sendMessage(handler.obtainMessage(0,
					// "必需至少有一条商品数量大于0"));
					showError("必需至少有一条商品数量大于0");
					return;
				}
				Intent localIntent = new Intent();
				localIntent.putExtra("items", JSONUtil.object2Json(localArrayList));
				setResult(RESULT_OK, localIntent);
				finish();
			}
		});

	}

	// Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// PDH.showMessage(msg.obj.toString());
	// };
	// };
	private Dialog_listCheckBox dialog;
	private AccountPreference ap;

	private DefDocItemXS fillItem(GoodsThin goodsThin, double num, double price) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
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
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(goodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(goodsThin.getId());
		}
		localDefDocItem.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItem.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItem.setNum(Utils.normalize(num, 2));
		localDefDocItem.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItem.getGoodsid(), localDefDocItem.getUnitid(),
				localDefDocItem.getNum()));
		localDefDocItem.setPrice(Utils.normalizePrice(price));
		localDefDocItem.setSubtotal(Utils.normalizeSubtotal(localDefDocItem.getNum() * localDefDocItem.getPrice()));
		localDefDocItem.setDiscountratio(this.doc.getDiscountratio());
		localDefDocItem
				.setDiscountprice(Utils.normalizePrice(localDefDocItem.getPrice() * this.doc.getDiscountratio()));
		localDefDocItem.setDiscountsubtotal(
				Utils.normalizeSubtotal(localDefDocItem.getNum() * localDefDocItem.getDiscountprice()));
		boolean isgift = false;
		if (localDefDocItem.getPrice() == 0.0D) {
			isgift = true;
		}
		localDefDocItem.setIsgift(isgift);
		localDefDocItem.setCostprice(0.0D);
		localDefDocItem.setRemark("");
		localDefDocItem.setRversion(0L);
		localDefDocItem.setIsdiscount(false);
		localDefDocItem.setIsusebatch(goodsThin.isIsusebatch());

		return localDefDocItem;
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("商品添加");
	}
}
