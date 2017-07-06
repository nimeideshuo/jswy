package com.ahjswy.cn.ui.inventory;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemPD;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.EditButtonView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class InventoryAddMoreAdapter extends BaseAdapter {
	private InventoryAddMoreGoodsAct context;
	private List<DefDocItemPD> listItems;

	public InventoryAddMoreAdapter(InventoryAddMoreGoodsAct context) {
		this.context = context;
		this.listItems = new ArrayList<DefDocItemPD>();
	}

	@Override
	public int getCount() {
		return this.listItems.size();
	}

	public List<DefDocItemPD> getData() {
		return this.listItems;
	}

	@Override
	public DefDocItemPD getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(R.layout.item_inventory_add_more_goods, null);
			Item item = new Item(convertView);
			convertView.setTag(item);
		} else {
			Item item = (Item) convertView.getTag();
			item.btnUnit.setTag(Integer.valueOf(position));
			item.etNum.setTag(Integer.valueOf(position));
			item.btnUnit.setOnClickListener(this.onClickListener);
			item.etNum.addTextChangedListener(new NumWatcher(item));
			item.setValue((DefDocItemPD) this.listItems.get(position));
		}
		return convertView;
	}

	public void setData(List<DefDocItemPD> paramList) {
		this.listItems.clear();
		this.listItems.addAll(paramList);
		notifyDataSetChanged();
	}

	private class Item {
		private Button btnUnit;
		private EditButtonView etNum;
		private TextView tvBarcode;
		private TextView tvName;

		public Item(View view) {
			this.tvName = ((TextView) view.findViewById(R.id.tvName));
			this.tvBarcode = ((TextView) view.findViewById(R.id.tvBarcode));
			this.btnUnit = ((Button) view.findViewById(R.id.btnUnit));
			this.etNum = ((EditButtonView) view.findViewById(R.id.etNum));
		}

		public void setValue(DefDocItemPD item) {
			this.tvName.setText(item.getGoodsname());
			this.tvBarcode.setText(item.getBarcode());
			this.btnUnit.setText(item.getUnitname());
			if (item.getNum() == 0.0D) {
				etNum.setText("");
			} else {
				etNum.setText(item.getNum() + "");
			}
		}

	}

	private class NumWatcher implements TextWatcher {
		Item item;

		public NumWatcher(Item item) {
			this.item = item;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			int i = Integer.parseInt(item.etNum.getTag().toString());
			listItems.get(i).setNum(Utils.normalize(Utils.getDouble(s.toString()).doubleValue(), 2));
		}

	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(final View v) {
			final int j = Integer.parseInt(v.getTag().toString());
			final List<GoodsUnit> localList = new GoodsUnitDAO().queryGoodsUnits((listItems.get(j)).getGoodsid());
			final String[] arrayOfString = new String[localList.size()];
			for (int i = 0; i < localList.size(); i++) {
				arrayOfString[i] = localList.get(i).getUnitname();
			}
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
			localBuilder.setTitle("单位选择");
			localBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Button) v).setText(arrayOfString[which]);
					DefDocItemPD defDocItemPD = listItems.get(j);
					defDocItemPD.setUnitid(localList.get(which).getUnitid());
					defDocItemPD.setUnitname(localList.get(which).getUnitname());
				}
			});
			localBuilder.show();
		}
	};
}
