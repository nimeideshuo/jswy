package com.ahjswy.cn.ui.ingoods;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDoc;
import com.ahjswy.cn.model.DefDocItem;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.response.RespGoodsWarehouse;
import com.ahjswy.cn.service.ServiceGoods;
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
import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

/*
 * 销售退货==商品添加
 */
public class InDocAddMoreGoodsAct extends BaseActivity {
	private List<DefDocItem> items;
	private ListView listView;
	private InDocAddMoreAdapter adapter;
	private DefDoc doc;
	private BarcodeManager bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_indoc_add_more_goods);
		initView();
	}

	private void initView() {
		items = JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItem.class);
		doc = (DefDoc) getIntent().getSerializableExtra("doc");
		listView = ((ListView) findViewById(R.id.listView));
		adapter = new InDocAddMoreAdapter(this);
		adapter.setData(items);
		listView.setAdapter(adapter);
		dialog = new Dialog_listCheckBox(this);

	}

	private BarcodeListener bl = new BarcodeListener() {

		@Override
		public void barcodeEvent(BarcodeEvent event) {
			if (event.getOrder().equals("SCANNER_READ")) {
				if (dialog != null) {
					dialog.dismiss();
				}
				readBarcode(bm.getBarcode().toString().trim());
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		bm = new BarcodeManager(this);
		bm.addListener(bl);
	}

	@Override
	protected void onPause() {
		super.onPause();
		deleBm();
	}

	private void readBarcode(String barcode) {
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		final ArrayList<DefDocItem> localArrayList = new ArrayList<DefDocItem>();
		if (goodsThinList.size() == 1) {
			DefDocItem defdocitem = fillItem(goodsThinList.get(0), 0.0D, 0.0D);
			adapter.addData(defdocitem);
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
						DefDocItem defdocitem = fillItem(select.get(i), 0.0D, 0.0D);
						localArrayList.add(defdocitem);
					}
					adapter.addItemDate(localArrayList);
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
		adapter.notifyDataSetInvalidated();
	}

	protected void setInitItem(DefDocItem item) {
		String localString = new ServiceGoods().gds_GetGoodsWarehouses(item.getGoodsid(), item.isIsusebatch());
		if (RequestHelper.isSuccess(localString)) {
			List<RespGoodsWarehouse> goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
			for (int i = 0; i < goodsWarehouses.size(); i++) {
				if (item.getGoodsid().equals(goodsWarehouses.get(i).getGoodsid())
						&& item.getWarehouseid().equals(goodsWarehouses.get(i).getWarehouseid())) {
					RespGoodsWarehouse res = goodsWarehouses.get(i);
					String bigstocknum = res.getBigstocknum().length() == 0 ? "0" + item.getUnitname()
							: res.getBigstocknum();
					item.goodStock = bigstocknum;

				}
			}
		} else {
			showError("没有获取到库存数据!请重试!");
		}
	}

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

	public void deleBm() {
		if (bm != null) {
			bm.removeListener(new BarcodeListener() {

				@Override
				public void barcodeEvent(BarcodeEvent arg0) {

				}
			});
			bm.dismiss();
			bm = null;
		}

	}

	// // 扫码枪结果添加
	// Handler handlerItem = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// adapter.addItemDate((ArrayList<DefDocItem>) msg.obj);
	// };
	// };

	private void tv_title_start() {
		PDH.show(this, new PDH.ProgressCallBack() {
			public void action() {
				ArrayList<DefDocItem> localArrayList = new ArrayList<DefDocItem>();
				List<DefDocItem> data = adapter.getData();
				for (int i = 0; i < data.size(); i++) {
					DefDocItem localDefDocItem = (DefDocItem) data.get(i);
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

	private DefDocItem fillItem(GoodsThin paramGoodsThin, double paramDouble1, double paramDouble2) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItem localDefDocItem = new DefDocItem();
		localDefDocItem.setItemid(0L);
		localDefDocItem.setDocid(this.doc.getDocid());
		localDefDocItem.setGoodsid(paramGoodsThin.getId());
		localDefDocItem.setGoodsname(paramGoodsThin.getName());
		localDefDocItem.setBarcode(paramGoodsThin.getBarcode());
		localDefDocItem.setSpecification(paramGoodsThin.getSpecification());
		localDefDocItem.setModel(paramGoodsThin.getModel());
		localDefDocItem.setWarehouseid(this.doc.getWarehouseid());
		localDefDocItem.setWarehousename(this.doc.getWarehousename());
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(paramGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(paramGoodsThin.getId());
		}
		localDefDocItem.setUnitid(localGoodsUnit.getUnitid());
		localDefDocItem.setUnitname(localGoodsUnit.getUnitname());
		localDefDocItem.setNum(Utils.normalize(paramDouble1, 2));
		localDefDocItem.setBignum(localGoodsUnitDAO.getBigNum(localDefDocItem.getGoodsid(), localDefDocItem.getUnitid(),
				localDefDocItem.getNum()));
		localDefDocItem.setPrice(Utils.normalizePrice(paramDouble2));
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
		localDefDocItem.setIsusebatch(paramGoodsThin.isIsusebatch());

		return localDefDocItem;
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("商品添加");
	}
}
