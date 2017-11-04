package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.cldb.Sz_stockwarn;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsPriceDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.CustomerRecords;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.scaner.Scaner;
import com.ahjswy.cn.scaner.Scaner.ScanerBarcodeListener;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_listCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * 销售单数量添加Activity
 * 
 * @author Administrator
 *
 */
public class OutDocAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItemXS> items;
	private OutDocAddMoreAdapter adapter;
	private DefDocXS doc;
	private Sz_stockwarn stockwarn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_out_doc_add_moregoods);
		intView();
		scaner = Scaner.factory(this);
		scaner.setBarcodeListener(barcodeListener);
	}

	private void intView() {
		dao = new GoodsUnitDAO();
		goodspricedao = new GoodsPriceDAO();
		dialog = new Dialog_listCheckBox(this);
		serviceGoods = new ServiceGoods();
		ap = new AccountPreference();
		stockwarn = new Sz_stockwarn();
		lv_commodity_add = (ListView) findViewById(R.id.lv_commodity_add);
		items = JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItemXS.class);
		doc = (DefDocXS) getIntent().getSerializableExtra("doc");
		adapter = new OutDocAddMoreAdapter(this);
		adapter.setDoc(doc);
		for (DefDocItemXS item : items) {
			item.setPrice(DocUtils.getGoodsPrice(doc.getCustomerid(), item));
			double sumstock = stockwarn.querySumStock(item.getGoodsid());
			item.stocksumnum = sumstock;
			item.goodSumStock = DocUtils.Stocknum(sumstock, item.unit);
			double stocknum = stockwarn.queryStockwarn(item.getWarehouseid(), item.getGoodsid());
			// stockwarn.queryStockwarnAll(goodsid);
			item.stocknum = stocknum;
			item.goodStock = DocUtils.Stocknum(stocknum, item.unit);
		}
		adapter.setItem(items);
		lv_commodity_add.setAdapter(adapter);
		// setInitItem();
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

	@Override
	protected void onPause() {
		super.onPause();
		scaner.removeListener();
	}

	protected void readBarcode(String barcode) {
		if (items.size() >= 50) {
			showError("已经开的够多了！请确认一下");
			return;
		}
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final Map<String, Object> goodsMap = new HashMap<String, Object>();
		if (goodsThinList.size() == 1) {
			long maxTempItemId = getMaxTempItemId();
			int num = Utils.isCombination() ? 1 : 0;
			DefDocItemXS fillItem = fillItem(goodsThinList.get(0), num, 0.0D, maxTempItemId + 1L);
			goodsMap.put(fillItem.getGoodsid(), fillItem);
			if (Utils.isCombination()) {
				combinationItem(goodsMap);
			}
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
					long maxTempItemId = getMaxTempItemId();
					for (int i = 0; i < select.size(); i++) {
						maxTempItemId += 1L;
						int num = Utils.isCombination() ? 1 : 0;
						DefDocItemXS fillItem = fillItem(select.get(i), num, 0.0D, maxTempItemId);
						goodsMap.put(fillItem.getGoodsid(), fillItem);
					}
					if (Utils.isCombination()) {
						combinationItem(goodsMap);
					}
					addItems(goodsMap);
					adapter.notifyDataSetInvalidated();
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				lv_commodity_add.setSelection(items.size());
			}
		}, 200);

	}

	private void combinationItem(Map<String, Object> goodsMap) {
		for (int i = 0; i < items.size(); i++) {
			DefDocItemXS itemXS = items.get(i);
			if (goodsMap.get(itemXS.getGoodsid()) != null) {
				itemXS.setNum(itemXS.getNum() + 1);
				goodsMap.remove(itemXS.getGoodsid());
				showError("相同商品数量+1");
			}
		}
	}

	// 扫码枪添加商品 转换
	protected void addItems(final Map<String, Object> goodsMap) {
		if (goodsMap.isEmpty()) {
			return;
		}
		PDH.show(this, "库存查询中...", new PDH.ProgressCallBack() {

			@Override
			public void action() {
				for (Object objitem : goodsMap.values()) {
					DefDocItemXS item = (DefDocItemXS) objitem;
					item.setPrice(DocUtils.getGoodsPrice(doc.getCustomerid(), item));
					double sumstock = stockwarn.querySumStock(item.getGoodsid());
					item.stocksumnum = sumstock;
					item.goodSumStock = DocUtils.Stocknum(sumstock, item.unit);
					double stocknum = stockwarn.queryStockwarn(item.getWarehouseid(), item.getGoodsid());
					item.stocknum = stocknum;
					item.goodStock = DocUtils.Stocknum(stocknum, item.unit);
					items.add(item);
				}
				adapter.setItem(items);
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetInvalidated();
					}
				});
			}
		});
	}

	// String localString =
	// serviceGoods.gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
	// defdocitemxs.isIsusebatch());
	// List<RespGoodsWarehouse> goodsWarehouses;
	// if (RequestHelper.isSuccess(localString)) {
	// goodsWarehouses = JSONUtil.str2list(localString,
	// RespGoodsWarehouse.class);
	// for (int j = 0; j < goodsWarehouses.size(); j++) {
	// if
	// (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
	// &&
	// defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid()))
	// {
	// RespGoodsWarehouse res = goodsWarehouses.get(j);
	// defdocitemxs.setStocknum(res);
	// if (Utils.DEFAULT_OutDocUNIT == 0) {
	// String stocknum = res.getStocknum() == 0 ? "0" +
	// defdocitemxs.getUnitname()
	// : String.valueOf(res.getStocknum()) +
	// defdocitemxs.getUnitname();
	// defdocitemxs.goodStock = stocknum;
	// } else {
	// String bigstocknum = res.getBigstocknum().length() == 0
	// ? "0" + defdocitemxs.getUnitname() :
	// res.getBigstocknum();
	// defdocitemxs.goodStock = bigstocknum;
	// }
	// break;
	// }
	//
	// }
	// } else {
	// showError("没有获取到库存数据!请重试!");
	// }
	// 查询客史价格
	// ReqStrGetGoodsPrice goodsPrice =
	// DocUtils.GetMultiGoodsPrice(doc.getCustomerid(),
	// defdocitemxs);
	// if (goodsPrice == null) {
	// showSuccess("商品单价没有查询到!");
	// }
	// defdocitemxs.setPrice(goodsPrice == null ? 0 :
	// goodsPrice.getPrice());
	// items.add(defdocitemxs);
	// runOnUiThread(new Runnable() {
	// public void run() {
	// adapter.setData(items);
	// }
	// });
	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setTitle("确定").setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
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

	// 初始化 设置 库存 单位转换
	// protected void setInitItem() {
	// PDH.show(this, "查询中...", new PDH.ProgressCallBack() {
	//
	// @Override
	// public void action() {
	// for(
	//
	// int i = 0;i<items.size();i++)
	// {
	// DefDocItemXS defdocitemxs = items.get(i);
	// String localString = new
	// ServiceGoods().gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
	// defdocitemxs.isIsusebatch());
	// List<RespGoodsWarehouse> goodsWarehouses;
	// if (RequestHelper.isSuccess(localString)) {
	// goodsWarehouses = JSONUtil.str2list(localString,
	// RespGoodsWarehouse.class);
	// for (int j = 0; j < goodsWarehouses.size(); j++) {
	// // 库存 查询
	// if
	// (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
	// &&
	//
	// defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid()))
	// {
	// RespGoodsWarehouse res = goodsWarehouses.get(j);
	// defdocitemxs.setStocknum(res);
	// if(Utils.DEFAULT_OutDocUNIT==0)
	//
	// {
	// String stocknum = res.getStocknum() == 0 ? "0" +
	// defdocitemxs.getUnitname()
	// : String.valueOf(res.getStocknum()) + defdocitemxs.getUnitname();
	// // 库存 的 设置
	// defdocitemxs.goodStock = stocknum;
	// }else
	// {
	// String bigstocknum = res.getBigstocknum().length() == 0
	// ? "0" + defdocitemxs.getUnitname() :
	// res.getBigstocknum();
	// // 库存 的 设置
	// defdocitemxs.goodStock = bigstocknum;
	// }
	// break;
	// }
	//
	// }
	// } else {
	// showError("没有获取到库存数据!请重试!");
	// }
	// CustomerRecords historyPrice =
	// DocUtils.getCustomerGoodsHistoryPrice(doc.getCustomerid(),
	// defdocitemxs.getGoodsid(),
	// defdocitemxs.getUnitid());if(historyPrice==null)
	// {
	// showSuccess("商品客史单价没有查询到!");
	// }else
	// {
	// defdocitemxs.setPrice(historyPrice.getPrice());
	// }
	//
	// // 查询商品客史价格
	// // ReqStrGetGoodsPrice goodsPrice =
	// // DocUtils.GetMultiGoodsPrice(doc.getCustomerid(),
	// // defdocitemxs);
	// // if (goodsPrice == null) {
	// // showSuccess("商品单价没有查询到!");
	// // }
	// // defdocitemxs.setPrice(goodsPrice == null ? 0 :
	// // goodsPrice.getPrice());
	// runOnUiThread(new Runnable() {
	// public void run() {
	// adapter.setData(items);
	// lv_commodity_add.setAdapter(adapter);
	// lv_commodity_add.setItemsCanFocus(true);
	// }
	// });
	// }
	// }
	// });
	//
	// }

	private List<DefDocItemXS> listDe;
	private ListView lv_commodity_add;
	// private BarcodeManager bm;
	// private BarcodeConfig barcodeConfig;
	private Dialog_listCheckBox dialog;
	private Scaner scaner;
	private ServiceGoods serviceGoods;
	private AccountPreference ap;
	private GoodsUnitDAO dao;
	private GoodsPriceDAO goodspricedao;

	// 保存输入的值 必须有一个 大于0 的
	private void tv_title_start() {
		List<DefDocItemXS> data = adapter.getData();
		listDe = new ArrayList<DefDocItemXS>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getNum() > 0.0D) {
				listDe.add(data.get(i));
			}
		}
		if (listDe.size() == 0) {
			PDH.showError("必需至少有一条商品数量大于0");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("items", JSONUtil.object2Json(listDe));
		setResult(RESULT_OK, intent);
		finish();
	}

	public long getMaxTempItemId() {
		long l1 = 0L;
		if (items.isEmpty()) {
			return l1;
		}
		for (int i = 0; i < items.size(); i++) {
			long l2 = items.get(i).getTempitemid();
			if (l2 > l1) {
				l1 = l2;
			}
		}
		return l1;
	}

	DefDocItemXS fillItem(GoodsThin paramGoodsThin, double paramDouble1, double paramDouble2, long paramLong) {
		DefDocItemXS itemXS = new DefDocItemXS();
		itemXS.setItemid(0L);
		itemXS.setTempitemid(paramLong);
		itemXS.setDocid(this.doc.getDocid());
		itemXS.setGoodsid(paramGoodsThin.getId());
		itemXS.setGoodsname(paramGoodsThin.getName());
		itemXS.setBarcode(paramGoodsThin.getBarcode());
		itemXS.setSpecification(paramGoodsThin.getSpecification());
		itemXS.setModel(paramGoodsThin.getModel());
		itemXS.setWarehouseid(this.doc.getWarehouseid());
		itemXS.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = dao.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = dao.queryBigUnit(paramGoodsThin.getId());
		}
		itemXS.setUnitid(localGoodsUnit.getUnitid());
		itemXS.setUnitname(localGoodsUnit.getUnitname());
		itemXS.unit = localGoodsUnit;
		itemXS.setNum(Utils.normalize(paramDouble1, 2));
		itemXS.setBignum(dao.getBigNum(itemXS.getGoodsid(), itemXS.getUnitid(), itemXS.getNum()));
		// 价格
		itemXS.setPrice(Utils.normalizePrice(paramDouble2));
		// 小计
		itemXS.setSubtotal(Utils.normalizeSubtotal(itemXS.getNum() * itemXS.getPrice()));
		// 折扣率
		itemXS.setDiscountratio(doc.getDiscountratio());
		// 折扣价格
		itemXS.setDiscountprice(Utils.normalizePrice(itemXS.getPrice() * this.doc.getDiscountratio()));
		// 折扣小计
		itemXS.setDiscountsubtotal(Utils.normalizeSubtotal(itemXS.getNum() * itemXS.getDiscountprice()));
		if (itemXS.getPrice() == 0.0D) {
			itemXS.setIsgift(true);
			itemXS.setCostprice(0.0D);
			itemXS.setRemark("");
			itemXS.setRversion(0L);
			itemXS.setIsdiscount(false);
			itemXS.setIsexhibition(false);
			itemXS.setIspromotion(false);
			itemXS.setParentitemid(0L);
			itemXS.setPromotiontype(-1);
			itemXS.setPromotiontypename(null);
			itemXS.setOutorderdocid(0L);
			itemXS.setOutorderdocshowid(null);
			itemXS.setOutorderitemid(0L);
			// 是否显示批次
			itemXS.setIsusebatch(paramGoodsThin.isIsusebatch());
			localGoodsUnit = dao.queryBigUnit(paramGoodsThin.getId());
		}
		return itemXS;
	}

	/**
	 * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	 * 
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// deleBm();
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
		}
		return true;
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("商品添加");
	}
}
