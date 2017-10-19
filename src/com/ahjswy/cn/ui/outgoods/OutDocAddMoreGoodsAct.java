package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespGoodsWarehouse;
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
	// ArrayList<DefDocItemXS> Newitems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_out_doc_add_moregoods);
		intView();
	}

	private void intView() {
		lv_commodity_add = (ListView) findViewById(R.id.lv_commodity_add);
		items = JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItemXS.class);
		doc = (DefDocXS) getIntent().getSerializableExtra("doc");
		adapter = new OutDocAddMoreAdapter(this);
		adapter.setDoc(doc);
		setInitItem();
		dialog = new Dialog_listCheckBox(this);
		// Newitems = new ArrayList<DefDocItemXS>();
		serviceGoods = new ServiceGoods();
		ap = new AccountPreference();
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
		// TODO 商品合并控制
		// 查找比对 是否存在 商品 有的情况下数量 +1
		// for (int i = 0; i < items.size(); i++) {
		// DefDocItemXS itemXS = items.get(i);
		// if (goodsMap.get(itemXS.getGoodsid()) != null) {
		// itemXS.setNum(itemXS.getNum() + 1);
		// goodsMap.remove(itemXS.getGoodsid());
		// showError("相同商品数量+1");
		// }
		// }
		if (goodsMap.isEmpty()) {
			return;
		}
		PDH.show(this, "库存查询中...", new PDH.ProgressCallBack() {

			@Override
			public void action() {
				for (Object item : goodsMap.values()) {
					DefDocItemXS defdocitemxs = (DefDocItemXS) item;
					String localString = serviceGoods.gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
							defdocitemxs.isIsusebatch());
					List<RespGoodsWarehouse> goodsWarehouses;
					if (RequestHelper.isSuccess(localString)) {
						goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
						for (int j = 0; j < goodsWarehouses.size(); j++) {
							if (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
									&& defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid())) {
								RespGoodsWarehouse res = goodsWarehouses.get(j);
								defdocitemxs.setStocknum(res);
								if (Utils.DEFAULT_OutDocUNIT == 0) {
									String stocknum = res.getStocknum() == 0 ? "0" + defdocitemxs.getUnitname()
											: String.valueOf(res.getStocknum()) + defdocitemxs.getUnitname();
									defdocitemxs.goodStock = stocknum;
								} else {
									String bigstocknum = res.getBigstocknum().length() == 0
											? "0" + defdocitemxs.getUnitname() : res.getBigstocknum();
									defdocitemxs.goodStock = bigstocknum;
								}
								break;
							}

						}
					} else {
						showError("没有获取到库存数据!请重试!");
					}
					ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(), defdocitemxs);
					if (goodsPrice == null) {
						showSuccess("商品单价没有查询到!");
					}
					defdocitemxs.setPrice(goodsPrice == null ? 0 : goodsPrice.getPrice());
					items.add(defdocitemxs);
					runOnUiThread(new Runnable() {
						public void run() {
							adapter.setData(items);
						}
					});
				}
			}
		});
	}

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
	protected void setInitItem() {
		PDH.show(this, "查询中...", new PDH.ProgressCallBack() {

			@Override
			public void action() {
				for (int i = 0; i < items.size(); i++) {
					DefDocItemXS defdocitemxs = items.get(i);
					String localString = new ServiceGoods().gds_GetGoodsWarehouses(defdocitemxs.getGoodsid(),
							defdocitemxs.isIsusebatch());
					List<RespGoodsWarehouse> goodsWarehouses;
					if (RequestHelper.isSuccess(localString)) {
						goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
						for (int j = 0; j < goodsWarehouses.size(); j++) {
							// 库存 查询
							if (defdocitemxs.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
									&& defdocitemxs.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid())) {
								RespGoodsWarehouse res = goodsWarehouses.get(j);
								defdocitemxs.setStocknum(res);
								if (Utils.DEFAULT_OutDocUNIT == 0) {
									String stocknum = res.getStocknum() == 0 ? "0" + defdocitemxs.getUnitname()
											: String.valueOf(res.getStocknum()) + defdocitemxs.getUnitname();
									// 库存 的 设置
									defdocitemxs.goodStock = stocknum;
								} else {
									String bigstocknum = res.getBigstocknum().length() == 0
											? "0" + defdocitemxs.getUnitname() : res.getBigstocknum();
									// 库存 的 设置
									defdocitemxs.goodStock = bigstocknum;
								}
								break;
							}

						}
					} else {
						showError("没有获取到库存数据!请重试!");
					}
					// 查询商品客史价格
					ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(), defdocitemxs);
					if (goodsPrice == null) {
						showSuccess("商品单价没有查询到!");
					}
					defdocitemxs.setPrice(goodsPrice == null ? 0 : goodsPrice.getPrice());
					runOnUiThread(new Runnable() {
						public void run() {
							adapter.setData(items);
							lv_commodity_add.setAdapter(adapter);
							lv_commodity_add.setItemsCanFocus(true);
						}
					});
				}
			}
		});

	}

	private List<DefDocItemXS> listDe;
	private ListView lv_commodity_add;
	// private BarcodeManager bm;
	// private BarcodeConfig barcodeConfig;
	private Dialog_listCheckBox dialog;
	private Scaner scaner;
	private ServiceGoods serviceGoods;
	private AccountPreference ap;

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
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemXS localDefDocItemXS = new DefDocItemXS();
		localDefDocItemXS.setItemid(0L);
		localDefDocItemXS.setTempitemid(paramLong);
		localDefDocItemXS.setDocid(this.doc.getDocid());
		localDefDocItemXS.setGoodsid(paramGoodsThin.getId());
		localDefDocItemXS.setGoodsname(paramGoodsThin.getName());
		localDefDocItemXS.setBarcode(paramGoodsThin.getBarcode());
		localDefDocItemXS.setSpecification(paramGoodsThin.getSpecification());
		localDefDocItemXS.setModel(paramGoodsThin.getModel());
		localDefDocItemXS.setWarehouseid(this.doc.getWarehouseid());
		localDefDocItemXS.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		localDefDocItemXS.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItemXS.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItemXS.setNum(Utils.normalize(paramDouble1, 2));
		localDefDocItemXS.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItemXS.getGoodsid(),
				localDefDocItemXS.getUnitid(), localDefDocItemXS.getNum()));
		// 价格
		localDefDocItemXS.setPrice(Utils.normalizePrice(paramDouble2));
		// 小计
		localDefDocItemXS
				.setSubtotal(Utils.normalizeSubtotal(localDefDocItemXS.getNum() * localDefDocItemXS.getPrice()));
		// 折扣率
		localDefDocItemXS.setDiscountratio(doc.getDiscountratio());
		// 折扣价格
		localDefDocItemXS
				.setDiscountprice(Utils.normalizePrice(localDefDocItemXS.getPrice() * this.doc.getDiscountratio()));
		// 折扣小计
		localDefDocItemXS.setDiscountsubtotal(
				Utils.normalizeSubtotal(localDefDocItemXS.getNum() * localDefDocItemXS.getDiscountprice()));
		if (localDefDocItemXS.getPrice() == 0.0D) {
			localDefDocItemXS.setIsgift(true);
			localDefDocItemXS.setCostprice(0.0D);
			localDefDocItemXS.setRemark("");
			localDefDocItemXS.setRversion(0L);
			localDefDocItemXS.setIsdiscount(false);
			localDefDocItemXS.setIsexhibition(false);
			localDefDocItemXS.setIspromotion(false);
			localDefDocItemXS.setParentitemid(0L);
			localDefDocItemXS.setPromotiontype(-1);
			localDefDocItemXS.setPromotiontypename(null);
			localDefDocItemXS.setOutorderdocid(0L);
			localDefDocItemXS.setOutorderdocshowid(null);
			localDefDocItemXS.setOutorderitemid(0L);
			// 是否显示批次
			localDefDocItemXS.setIsusebatch(paramGoodsThin.isIsusebatch());
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		return localDefDocItemXS;
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
