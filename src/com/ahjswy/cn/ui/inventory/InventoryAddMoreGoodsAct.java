package com.ahjswy.cn.ui.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocPD;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_listCheckBox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class InventoryAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItemPD> items;
	private ListView listView;
	private InventoryAddMoreAdapter adapter;
	private Scaner scaner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_inventory_add_more_goods);
		String stringExtra = getIntent().getStringExtra("items");
		doc = (DefDocPD) getIntent().getSerializableExtra("doc");
		items = JSONUtil.str2list(stringExtra, DefDocItemPD.class);
		listView = ((ListView) findViewById(R.id.listView));
		adapter = new InventoryAddMoreAdapter(this);
		this.adapter.setItem(items);
		this.listView.setAdapter(this.adapter);
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
		dialog = new Dialog_listCheckBox(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "确定").setShowAsAction(2);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		if (menu.getItemId() == 1) {
			PDH.show(this, new PDH.ProgressCallBack() {
				public void action() {
					Intent intent = new Intent();
					intent.putExtra("items", JSONUtil.object2Json(items));
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
		}
		return super.onOptionsItemSelected(menu);
	}

	ScanerBarcodeListener barcodeListener = new ScanerBarcodeListener() {

		@Override
		public void setBarcode(String barcode) {
			if (dialog != null) {
				dialog.dismiss();
			}
			readBarcode(barcode);
			// 代码 等待不全
		}
	};
	private Dialog_listCheckBox dialog;

	private void readBarcode(String barcode) {
		final ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		if (goodsThinList.isEmpty()) {
			showError("没有查找到商品!");
			return;
		}
		if (items.size() >= 50) {
			showError("已经开的够多了！请确认一下");
			return;
		}
		final Map<String, Object> goodsMap = new HashMap<String, Object>();
		if (goodsThinList.size() == 1) {
			DefDocItemPD fillItem = fillItem(goodsThinList.get(0), 0.0D, 0.0D, 1);
			goodsMap.put(fillItem.getGoodsid(), fillItem);
			addItems(goodsMap);
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
						DefDocItemPD fillItem = fillItem(select.get(i), 0d, 0.0D, 1);
						goodsMap.put(fillItem.getGoodsid(), fillItem);
					}
					addItems(goodsMap);
					adapter.notifyDataSetInvalidated();
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

	private void addItems(Map<String, Object> goodsMap) {
		for (int i = 0; i < items.size(); i++) {
			DefDocItemPD itemPD = items.get(i);
			if (goodsMap.get(itemPD.getGoodsid()) != null) {
				itemPD.setNum(itemPD.getNum() + 1);
				goodsMap.remove(itemPD.getGoodsid());
				showError("相同商品数量+1");
			}
		}
		if (goodsMap.isEmpty()) {
			return;
		}
		for (Object item : goodsMap.values()) {
			items.add((DefDocItemPD) item);
		}
		adapter.setItem(items);
	}

	private DefDocPD doc;

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

	public void setActionBarText() {
		setTitle("商品添加");
	}
}
