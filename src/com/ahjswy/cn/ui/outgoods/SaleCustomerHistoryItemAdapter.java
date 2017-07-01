package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class SaleCustomerHistoryItemAdapter extends BaseAdapter {
	Context context;
	private List<DefDocItemXS> items;
	private boolean multichoice = false;
	private HashMap statusMap = new HashMap();

	public SaleCustomerHistoryItemAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		if (this.items != null) {
			return this.items.size();
		}
		return 0;
	}

	public List<DefDocItemXS> getData() {
		return this.items;
	}

	@Override
	public Object getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public long getItemId(int paramInt) {
		return items.get(paramInt).getItemid();
	}

	// public List<DefDocItemXS> getSelectList() {
	// ArrayList localArrayList = new ArrayList();
	// if (items.size() <= 0) {
	// return localArrayList;
	// }
	//
	// for (int i = 0; i <= items.size(); i++) {
	//
	// if ((this.statusMap.get(Integer.valueOf(i)) != null)
	// && (((Boolean) this.statusMap.get(Integer.valueOf(i))).booleanValue())) {
	// localArrayList.add((DefDocItemXS) this.items.get(i));
	// }
	//
	// }
	// return localArrayList;
	// }

	public void setData(List<DefDocItemXS> paramList) {
		this.items = paramList;
		notifyDataSetChanged();
	}

	public void setMultiChoice(boolean paramBoolean) {
		this.multichoice = paramBoolean;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TransferGoodsItem localTransferGoodsItem = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(R.layout.item_salecustomerhistory_goods, null);
			localTransferGoodsItem = new TransferGoodsItem(convertView);
			if (this.statusMap.get(Integer.valueOf(position)) != null) {
				localTransferGoodsItem.cbTV.setChecked(false);
			}
			convertView.setTag(localTransferGoodsItem);
		}
		localTransferGoodsItem = (TransferGoodsItem) convertView.getTag();
		localTransferGoodsItem.setValue(items.get(position));
		localTransferGoodsItem.tvSerialid.setText(position + 1 + "");
		// localTransferGoodsItem.cbTV
		// .setChecked(((Boolean)
		// this.statusMap.get(Integer.valueOf(position))).booleanValue());
		return convertView;
	}

	// public void setCheckePosition(int paramInt) {
	// if (this.statusMap.get(Integer.valueOf(paramInt)) == null) {
	// this.statusMap.put(Integer.valueOf(paramInt), Boolean.valueOf(true));
	// notifyDataSetChanged();
	// return;
	// }
	// HashMap localHashMap = this.statusMap;
	// Integer localInteger = Integer.valueOf(paramInt);
	// boolean booleanValue = ((Boolean)
	// this.statusMap.get(Integer.valueOf(paramInt))).booleanValue();
	// localHashMap.put(localInteger, Boolean.valueOf(booleanValue));
	// }

	public class TransferGoodsItem {
		private CheckedTextView cbTV;
		public TextView tvBarcode;
		public TextView tvDiscountRatio;
		public TextView tvDiscountSubtotal;
		public TextView tvName;
		public TextView tvNum;
		public TextView tvPrice;
		public TextView tvSerialid;

		public TransferGoodsItem(View view) {
			this.tvSerialid = ((TextView) view.findViewById(R.id.tvSerialid));
			this.tvName = ((TextView) view.findViewById(R.id.tvName));
			this.tvBarcode = ((TextView) view.findViewById(R.id.tvBarcode));
			this.tvNum = ((TextView) view.findViewById(R.id.tvNum));
			this.tvPrice = ((TextView) view.findViewById(R.id.tvPrice));
			this.tvDiscountRatio = ((TextView) view.findViewById(R.id.tvDiscountRatio));
			this.tvDiscountSubtotal = ((TextView) view.findViewById(R.id.tvDiscountSubtotal));
			this.cbTV = ((CheckedTextView) view.findViewById(R.id.cbTV));
		}

		public void setValue(DefDocItemXS paramDefDocItemXS) {
			this.tvName.setText(paramDefDocItemXS.getGoodsname());
			if (TextUtils.isEmptyS(paramDefDocItemXS.getBarcode())) {
				this.tvBarcode.setText(paramDefDocItemXS.getBarcode());
			}
			this.tvNum.setText(Utils.getNumber(paramDefDocItemXS.getNum()) + paramDefDocItemXS.getUnitname());
			this.tvPrice.setText(Utils.getPrice(paramDefDocItemXS.getPrice()) + "元/" + paramDefDocItemXS.getUnitname());
			this.tvDiscountRatio.setText(Utils.getNumber(paramDefDocItemXS.getDiscountratio()));
			this.tvDiscountSubtotal.setText(Utils.getSubtotalMoney(paramDefDocItemXS.getDiscountsubtotal()) + "元");
			if (SaleCustomerHistoryItemAdapter.this.multichoice) {
				this.cbTV.setVisibility(0);
				return;
			}
			SaleCustomerHistoryItemAdapter.this.statusMap.clear();
			this.cbTV.setVisibility(8);
		}
	}
}
