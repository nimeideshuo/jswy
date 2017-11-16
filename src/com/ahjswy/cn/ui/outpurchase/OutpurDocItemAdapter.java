package com.ahjswy.cn.ui.outpurchase;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.model.DefDocItemCG;
import com.ahjswy.cn.model.DefDocItemTH;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OutpurDocItemAdapter extends BaseAdapter {
	Context mContext;
	List<DefDocItemTH> items;
	ArrayList<Long> listItemDelete;

	public OutpurDocItemAdapter(Context context) {
		this.mContext = context;
		if (listItemDelete == null) {
			listItemDelete = new ArrayList<Long>();
		}
		if (items == null) {
			items = new ArrayList<DefDocItemTH>();
		}
	}

	public void addItem(DefDocItemTH defdocitemth) {
		this.items.add(defdocitemth);
		notifyDataSetChanged();
	}

	public void addItem(int paramInt, DefDocItemTH defdocitemth) {
		this.items.set(paramInt, defdocitemth);
		notifyDataSetChanged();
	}

	public void addItemlist(ArrayList<DefDocItemTH> newListItem) {
		this.items.addAll(newListItem);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (this.items != null) {
			return items.size();
		}
		return 0;
	}

	public List<DefDocItemTH> getData() {
		return this.items;
	}

	public void setData(List<DefDocItemTH> defdocitemcg) {
		this.items = defdocitemcg;
		notifyDataSetChanged();
	}

	public ArrayList<Long> getListItemDelete() {
		return listItemDelete;
	}

	public void setListItemDeleteClean() {
		listItemDelete.clear();
	}

	@Override
	public Object getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void remove(DefDocItemCG defdocitemcg) {
		this.items.remove(defdocitemcg);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_outdoc_goods, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.setValue(items.get(position));
		holder.tvSerialid.setText((position + 1) + "");
		if (sum != null) {
			sum.sum();
		}
		return convertView;
	}

	public class ViewHolder {
		// 【赠】
		public TextView tvIsGive;
		// 名称
		public TextView tvName;
		// 条码
		public TextView tvBarcode;
		// 单价
		public TextView tvPrice;
		// 仓库
		public TextView tvWarehouse;
		// 批次
		public TextView tvBatch;
		// 折扣比例
		public TextView tvDiscountRatio;
		// 数量
		public TextView tvNum;
		// 折后合计
		public TextView tvDiscountSubtotal;
		// 序列号
		public TextView tvSerialid;
		// 单价
		public LinearLayout ll_Price;
		// 滑动布局

		public ViewHolder(View v) {
			tvSerialid = (TextView) v.findViewById(R.id.tvSerialid);
			ll_Price = (LinearLayout) v.findViewById(R.id.ll_Price);
			tvIsGive = (TextView) v.findViewById(R.id.tvIsGive);
			tvName = (TextView) v.findViewById(R.id.tvGoodsName);
			tvBarcode = (TextView) v.findViewById(R.id.tvBarcode);
			tvPrice = (TextView) v.findViewById(R.id.tvPrice);
			tvWarehouse = (TextView) v.findViewById(R.id.tvWarehouse);
			tvBatch = (TextView) v.findViewById(R.id.tvBatch);
			tvDiscountRatio = (TextView) v.findViewById(R.id.tvDiscountRatio);
			tvDiscountSubtotal = (TextView) v.findViewById(R.id.tvDiscountSubtotal);
			tvNum = (TextView) v.findViewById(R.id.tvNum);
		}

		public void setValue(DefDocItemTH defdocitemcg) {
			// 【赠】不显示 单价 如果是赠品 Isgift true
			if ((defdocitemcg.isIsgift())) { // ||
												// (defdocitemcg.isIspromotion())
				this.tvIsGive.setVisibility(0);
				this.ll_Price.setVisibility(8);
			} else {
				this.tvIsGive.setVisibility(8);
				this.ll_Price.setVisibility(0);
			} // 商品名字
			tvName.setText(defdocitemcg.getGoodsname());
			// 商品 的条码
			if (TextUtils.isEmptyS(defdocitemcg.getBarcode())) {
				this.tvBarcode.setText(defdocitemcg.getBarcode());
			}
			// 设置数量
			this.tvNum.setText(Utils.getNumber(defdocitemcg.getNum()) + defdocitemcg.getUnitname());
			// 设置单价
			this.tvPrice.setText(Utils.getPrice(defdocitemcg.getPrice()) + "元/" + defdocitemcg.getUnitname());
			// 折扣比
			this.tvDiscountRatio.setText(Utils.getNumber(defdocitemcg.getDiscountratio()));
			// 折扣小计
			this.tvDiscountSubtotal.setText(Utils.getSubtotalMoney(defdocitemcg.getDiscountsubtotal()) + "元");
			// 仓库
			if (TextUtils.isEmptyS(defdocitemcg.getWarehousename())) {
				this.tvWarehouse.setText(defdocitemcg.getWarehousename());
			} else {
				this.tvWarehouse.setText("无仓库");
			}
			// 批次
			if (TextUtils.isEmptyS(defdocitemcg.getBatch())) {
				this.tvBatch.setText(defdocitemcg.getBatch());
			}
		}

	}

	private Sum sum;

	public void setSum(Sum sum) {
		this.sum = sum;
	}

	public interface Sum {
		public void sum();
	}

}
