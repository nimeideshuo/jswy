package com.ahjswy.cn.ui;

import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.model.GoodsInfo;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.views.AutoTextViews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class GoodsSearchAct extends BaseActivity implements com.ahjswy.cn.views.AutoTextViews.OnTextChangeListener {
	private AutoTextViews etSearch;
	private ListView listView;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.act_customer_search);
		etSearch = (AutoTextViews) findViewById(R.id.etSearch);
		listView = (ListView) findViewById(R.id.listview);
		this.etSearch.setVisibility(0);
		this.etSearch.setOnTextChangeListener(this);
		this.etSearch.setHint("检索商品");
	}

	private List<GoodsInfo> sourceData;

	@Override
	public void onChanged(View paramView, String paramString) {
		// this.temp.clear();
		if (TextUtils.isEmptyS(paramString)) {
			this.listView.setAdapter(null);
			return;
		}
		if (TextUtils.isEmptyS(this.etSearch.getBeforeTextChange())) {
			// TODO SQL 代码待补全
			sourceData = new GoodsDAO().queryGoodsInfos(paramString);
			handler.sendMessage(this.handler.obtainMessage(0, paramString));
			return;
		}
		this.handler.sendMessage(handler.obtainMessage(0, paramString));
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String localString1 = msg.obj.toString();

		};
	};

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_FIRST_USER);
			finish();
			break;
		}
		return true;
	}

	public void onItemClick(AdapterView paramAdapterView, View paramView, int paramInt, long paramLong) {
		Intent localIntent = new Intent();
		// localIntent.putExtra("goods", (Serializable)
		// this.temp.get(paramInt));
		setResult(RESULT_OK, localIntent);
		finish();
	}

	public void setActionBarText() {
		getActionBar().setTitle("商品检索");
	}
}
