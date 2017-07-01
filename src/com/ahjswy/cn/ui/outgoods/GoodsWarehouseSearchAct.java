package com.ahjswy.cn.ui.outgoods;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.model.Goods;
import com.ahjswy.cn.response.RespGoodsWarehouse;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class GoodsWarehouseSearchAct extends BaseActivity implements AdapterView.OnItemClickListener {
	List<RespGoodsWarehouse> goodsWarehouses = null;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.listview);
		initView();
	}

	private void initView() {
		this.listview = ((ListView) findViewById(R.id.listview));
		this.listview.setOnItemClickListener(this);
		String localString = getIntent().getStringExtra("goodsid");
		position = getIntent().getStringExtra("position");

		loadData(new GoodsDAO().getGoods(localString));
		this.listview.setOnItemClickListener(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String localString = msg.obj.toString();

			if (msg.what == 0) {
				goodsWarehouses = JSONUtil.str2list(localString, RespGoodsWarehouse.class);
				if (goodsWarehouses.size() == 0) {
					PDH.showMessage("无可用发货仓库");
				}

			}
			if (msg.what != 1) {
				GoodsWarehouseSearchAdapter adapter = new GoodsWarehouseSearchAdapter(GoodsWarehouseSearchAct.this);
				adapter.setData(goodsWarehouses);
				listview.setAdapter(adapter);
				return;
			}
			PDH.showFail("发货仓库读取失败");
		}
	};
	private ListView listview;
	private String position;

	private void loadData(final Goods paramGoods) {
		PDH.show(this, "正在查询仓库...", new PDH.ProgressCallBack() {
			public void action() {
				String localString = new ServiceGoods().gds_GetGoodsWarehouses(paramGoods.getId(),
						paramGoods.isIsusebatch());
				// 成功走此方法
				if (RequestHelper.isSuccess(localString)) {
					handler.sendMessage(handler.obtainMessage(0, localString));
					return;
				}
				// 失败走此方法
				GoodsWarehouseSearchAct.this.handler
						.sendMessage(GoodsWarehouseSearchAct.this.handler.obtainMessage(1, localString));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
		Intent localIntent = new Intent();
		localIntent.putExtra("warehouse", (Serializable) this.goodsWarehouses.get(paramInt));
		setResult(RESULT_OK, localIntent);
		finish();
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_FIRST_USER);
			finish();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			setResult(RESULT_FIRST_USER);
			finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("仓库");
	}
}