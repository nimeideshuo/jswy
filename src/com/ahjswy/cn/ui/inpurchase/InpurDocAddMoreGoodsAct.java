package com.ahjswy.cn.ui.inpurchase;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.bean.Def_Doc;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemCG;
import com.ahjswy.cn.model.GoodsThin;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.response.RespGoodsStock;
import com.ahjswy.cn.response.RespServiceInfor;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.service.ServiceSupport;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_listCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import mexxen.mx5010.barcode.BarcodeConfig;
import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

public class InpurDocAddMoreGoodsAct extends BaseActivity {
	private ListView lv_commodity_add;
	private ArrayList<DefDocItemCG> items;
	private InpurDocAddMoreAdapter adapter;
	private Def_Doc doccg;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.act_out_doc_add_moregoods);
		intView();
		addListener();
	}

	private void intView() {
		lv_commodity_add = (ListView) findViewById(R.id.lv_commodity_add);
		items = (ArrayList<DefDocItemCG>) JSONUtil.str2list(getIntent().getStringExtra("items"), DefDocItemCG.class);
		doccg = (Def_Doc) getIntent().getSerializableExtra("doc");
		adapter = new InpurDocAddMoreAdapter(this);
		dialog = new Dialog_listCheckBox(this);
		support = new ServiceSupport();
		setInitItem();
	}

	ArrayList<DefDocItemCG> Newitems;
	private Dialog_listCheckBox dialog;

	public void addListener() {
		if (bm == null) {
			bm = new BarcodeManager(this);
		}
		if (Newitems == null) {
			Newitems = new ArrayList<DefDocItemCG>();
		}
		bm.addListener(new BarcodeListener() {
			@Override
			public void barcodeEvent(BarcodeEvent event) {
				if (event.getOrder().equals("SCANNER_READ")) {
					if (dialog != null) {
						dialog.dismiss();
					}
					readBarcode(bm.getBarcode().toString().trim());
				}
			}
		});
		// 扫码枪 功能调用 先new 对相 在调用
		BarcodeConfig barcodeConfig = new BarcodeConfig(this);
		// 设置条码输出模式 不显示模式(复制到粘贴板)
		barcodeConfig.setOutputMode(2);

	}

	protected void readBarcode(String barcode) {
		if (items.size() >= 20) {
			showError("已经够多了请确认下!");
			return;
		}
		ArrayList<GoodsThin> goodsThinList = new GoodsDAO().getGoodsThinList(barcode);
		if (goodsThinList.size() == 1) {
			DefDocItemCG fillItem = fillItem(goodsThinList.get(0), 0.0D, 0.0D, 0L);// 0
			Newitems.add(fillItem);
			addItems();
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
						DefDocItemCG fillItem = fillItem(select.get(i), 0.0D, 0.0D, 0L);
						Newitems.add(fillItem);
					}
					addItems();
				}
			});
		} else {
			PDH.showFail("没有查找到商品！可以尝试更新数据");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		deleBm();
	}

	protected void addItems() {
		for (int i = 0; i < Newitems.size(); i++) {
			DefDocItemCG defdocitemCG = Newitems.get(i);
			setItemsGoodsPrice(defdocitemCG);
			setItemStock(defdocitemCG);
		}
		items.addAll(Newitems);
		adapter.setData(items);
		Newitems.clear();
	}

	private void setInitItem() {
		for (int i = 0; i < items.size(); i++) {
			DefDocItemCG defdocitemcg = items.get(i);

			setItemsGoodsPrice(defdocitemcg);
			// defdocitemcg.setStocknum(goodsWarehouses.get(j));
			setItemStock(defdocitemcg);
			// DefDocItemCG defdocitemcg = items.get(i);
			// String localString = new
			// ServiceGoods().gds_GetGoodsWarehouses(defdocitemcg.getGoodsid(),
			// defdocitemcg.isIsusebatch());
			//
			// List<RespGoodsWarehouse> goodsWarehouses;
			// if (RequestHelper.isSuccess(localString)) {
			// goodsWarehouses = JSONUtil.str2list(localString,
			// RespGoodsWarehouse.class);
			// for (int j = 0; j < goodsWarehouses.size(); j++) {
			// if
			// (defdocitemcg.getGoodsid().equals(goodsWarehouses.get(j).getGoodsid())
			// &&
			// defdocitemcg.getWarehouseid().equals(goodsWarehouses.get(j).getWarehouseid()))
			// {
			// defdocitemcg.setStocknum(goodsWarehouses.get(j));
			// setItemsGoodsPrice(defdocitemcg);
			// break;
			// }
			// }
			// }
		}
		adapter.setData(items);
		lv_commodity_add.setAdapter(adapter);
	}

	private void setItemStock(DefDocItemCG defdocitemcg) {
		String queryStockNum = support.sup_QueryStockNum(defdocitemcg.getGoodsid(), defdocitemcg.getWarehouseid());
		RespGoodsStock stockEntity = JSONUtil.readValue(queryStockNum, RespGoodsStock.class);
		if (stockEntity == null) {
			defdocitemcg.stock = 0;
			return;
		}
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			defdocitemcg.bigstocknumber = stockEntity.stocknum + defdocitemcg.getUnitname();
		} else {
			defdocitemcg.bigstocknumber = stockEntity.bigstocknumber;
		}
		defdocitemcg.stock = stockEntity.stocknum;
	}

	/**
	 * 获取每个商品的 价格并设置给当前商品
	 * 
	 * @param defdocitemcg
	 */
	private void setItemsGoodsPrice(DefDocItemCG defdocitemcg) {
		List<DefDocItemCG> itemCGs = new ArrayList<DefDocItemCG>();
		itemCGs.add(defdocitemcg);
		String goodsPrice = new ServiceStore().GetGoodsPrice(doccg, JSONUtil.object2Json(itemCGs));
		if (TextUtils.isEmpty(goodsPrice)) {
			defdocitemcg.setIsgift(true);
			defdocitemcg.setPrice(0);
			defdocitemcg.setDiscountprice(0);
			return;
		}
		RespServiceInfor serviceInfor = JSONUtil.readValue3(goodsPrice);
		double price = Double.parseDouble(serviceInfor.Json.Data);
		if (price == 0) {
			defdocitemcg.setIsgift(true);
			defdocitemcg.setPrice(0);
			defdocitemcg.setDiscountprice(0);
		} else {
			defdocitemcg.setIsgift(false);
			defdocitemcg.setPrice(price);
			defdocitemcg.setDiscountprice(price * defdocitemcg.getDiscountratio());
		}
	}

	private DefDocItemCG fillItem(GoodsThin localGoodsThin, double paramDouble1, double paramDouble2, Long paramLong) {
		GoodsUnitDAO localGoodsUnitDAO = new GoodsUnitDAO();
		DefDocItemCG defDocItemCG = new DefDocItemCG();
		defDocItemCG.setGoodsid(localGoodsThin.getId());
		defDocItemCG.setGoodsname(localGoodsThin.getName());
		defDocItemCG.setBarcode(localGoodsThin.getBarcode());
		defDocItemCG.setSpecification(localGoodsThin.getSpecification());
		defDocItemCG.setWarehouseid(doccg.getWarehouseid());
		defDocItemCG.setWarehousename(doccg.getWarehousename());
		defDocItemCG.setItemid(0L);
		defDocItemCG.setTempitemid(paramLong);
		GoodsUnit localGoodsUnit = null;
		if (Utils.DEFAULT_OutDocUNIT == 0) {
			localGoodsUnit = localGoodsUnitDAO.queryBaseUnit(localGoodsThin.getId());
		} else {
			localGoodsUnit = localGoodsUnitDAO.queryBigUnit(localGoodsThin.getId());
		}
		defDocItemCG.setUnitid(localGoodsUnit.getUnitid());
		defDocItemCG.setUnitname(localGoodsUnit.getUnitname());
		defDocItemCG.setNum(Utils.normalize(paramDouble1, 2));
		defDocItemCG.setBignum(localGoodsUnitDAO.getBigNum(defDocItemCG.getGoodsid(), defDocItemCG.getUnitid(),
				defDocItemCG.getNum()));
		// 价格
		defDocItemCG.setPrice(Utils.normalizePrice(paramDouble2));
		// 小计
		defDocItemCG.setSubtotal(Utils.normalizeSubtotal(defDocItemCG.getNum() * defDocItemCG.getPrice()));
		// 折扣率
		defDocItemCG.setDiscountratio(doccg.getDiscountratio());
		// 折扣价格
		defDocItemCG.setDiscountprice(Utils.normalizePrice(defDocItemCG.getPrice() * doccg.getDiscountratio()));
		// 折后小计
		defDocItemCG.setDiscountsubtotal(defDocItemCG.getNum() * defDocItemCG.getDiscountprice());
		if (defDocItemCG.getPrice() == 0.0D) {
			defDocItemCG.setIsgift(true);
		}
		// 总金额
		// defDocItemCG.setCostprice(0.0D);
		defDocItemCG.setIsusebatch(localGoodsThin.isIsusebatch());

		return defDocItemCG;
	}

	// 添加商品处理

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setTitle("确定").setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			// deleBm();
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
			break;
		case 0:
			tv_title_start();
			break;

		default:
			break;
		}
		return true;
	}

	private List<DefDocItemCG> listDe;

	private void tv_title_start() {
		List<DefDocItemCG> data = adapter.getData();
		listDe = new ArrayList<DefDocItemCG>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getNum() > 0.0D) {
				listDe.add(data.get(i));
			}
		}
		if (listDe.size() == 0) {
			PDH.showError("必需至少有一条商品数量大于0");
			return;
		}
		for (int i = 0; i < listDe.size(); i++) {
			DefDocItemCG defDocItemCG = listDe.get(i);
			double discountratio = defDocItemCG.getDiscountratio();
			double price = defDocItemCG.getPrice();
			double num = defDocItemCG.getNum();
			defDocItemCG.setDiscountsubtotal(discountratio * price);
			defDocItemCG.setSubtotal(num * price);
		}
		// deleBm();
		Intent intent = new Intent();
		intent.putExtra("items", JSONUtil.object2Json(listDe));
		setResult(RESULT_OK, intent);
		finish();
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

	private BarcodeManager bm;
	private ServiceSupport support;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_FIRST_USER) {
			// addListener();
		} else if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// try {
				// newListItem = JSONUtil.str2list(data.getStringExtra("items"),
				// DefDocItemCG.class);
				// final ArrayList<ReqStrGetGoodsPrice> localArrayList = new
				// ArrayList<ReqStrGetGoodsPrice>();
				// for (int i = 0; i < newListItem.size(); i++) {
				// DefDocItemCG localDefDocItemXS5 = newListItem.get(i);
				// ReqStrGetGoodsPrice localReqStrGetGoodsPrice = new
				// ReqStrGetGoodsPrice();
				// localReqStrGetGoodsPrice.setType(1);
				// // TODO 是供应商id 还是客户id 查询价格初始化
				// localReqStrGetGoodsPrice.setCustomerid(defdoccg.getSupplerid());
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
				//
				// handlerGet.sendMessage(handlerGet.obtainMessage(0,
				// localString2));
				// }
				// });
				//
				// } catch (JsonProcessingException e) {
				// e.printStackTrace();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// break;

			default:
				break;
			}
		}
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("添加商品");
	}
}
