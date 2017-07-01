package com.ahjswy.cn.ui.outgoods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.utils.JSONUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

public class SaleCustomerHistoryItemActivity extends BaseActivity {
	private SaleCustomerHistoryItemAdapter adapter;
	private List<DefDocItemXS> listItem;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_salecustomerhistoryitem);
		initView();
	}

	private void initView() {
		this.listView = (ListView) findViewById(R.id.listView);
		listItem = JSONUtil.str2list(getIntent().getStringExtra("listitem"), DefDocItemXS.class);
		this.adapter = new SaleCustomerHistoryItemAdapter(this);
		this.adapter.setData(listItem);
		this.listView.setAdapter(adapter);
		// refreshUI();
		// this.listView.setChoiceMode(3);
		// this.listView.setMultiChoiceModeListener(muliChoiceModeLisener);
	}

	MultiChoiceModeListener muliChoiceModeLisener = new MultiChoiceModeListener() {

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {

		}

		// title 选择菜单
		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
			adapter.setMultiChoice(true);
			arg0.setTitle("选择");
			arg0.getMenuInflater().inflate(2131623941, arg1);

			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
			switch (arg1.getItemId()) {
			case 0:
				for (int j = 0; j < adapter.getCount(); j++) {
					if (!listView.isItemChecked(j))
						listView.setItemChecked(j, true);
				}

				break;
			case 1:
				ArrayList<DefDocItemXS> localArrayList = new ArrayList<DefDocItemXS>();
				for (int i = 0; i <= adapter.getCount(); i++) {
					if (listView.isItemChecked(i)) {
						localArrayList.add(listItem.get(i));
					}
				}
				Intent localIntent = new Intent();
				localIntent.putExtra("selecteditem", JSONUtil.object2Json(localArrayList));
				setResult(-1, localIntent);
				finish();
				arg0.finish();
				break;

			default:
				arg0.finish();
				break;
			}
			return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2, boolean arg3) {

		}
	};
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setResult(-1, new Intent());
				finish();
				break;
			case 1:
				Intent localIntent = new Intent();
				localIntent.putExtra("selecteditem", JSONUtil.object2Json(listItem));
				setResult(-1, localIntent);
				finish();
				break;
			default:
				break;
			}

		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu) {
		paramMenu.add(0, 0, 0, "单击显示菜单").setTitle("添加全部").setShowAsAction(1);
		return super.onCreateOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_FIRST_USER, new Intent());
			finish();
			break;
		case 0:
			// TODO 功能待添加
			// Intent localIntent = new Intent();
			// localIntent.putExtra("selecteditem",
			// JSONUtil.object2Json(listItem));
			// setResult(-1, localIntent);
			// finish();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void setActionBarText() {
		getActionBar().setTitle("客史明细");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			setResult(-1, new Intent());
			finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
