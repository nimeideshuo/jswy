package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OutDocAddMoreAdapter extends BaseAdapter {
	private Activity context;
	private List<DefDocItemXS> listItems;
	// private int selectPosition = -1;// 记住选中的edtitext

	public OutDocAddMoreAdapter(Activity context) {
		this.context = context;
		if (listItems == null) {
			this.listItems = new ArrayList<DefDocItemXS>();
		}
	}

	@Override
	public int getCount() {
		return this.listItems.size();
	}

	@Override
	public DefDocItemXS getItem(int position) {
		return this.listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public List<DefDocItemXS> getData() {
		return this.listItems;
	}

	public void setData(List<DefDocItemXS> paramList) {
		this.listItems.clear();
		this.listItems.addAll(paramList);
		notifyDataSetChanged();
	}

	public DefDocXS doc;

	public void setDoc(DefDocXS doc) {
		this.doc = doc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.act_out_doc_add_moreadapter_item, null);
		// 商品 name
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		// 二维码
		TextView tvBarcode = (TextView) convertView.findViewById(R.id.tvBarcode);
		// 数量
		final EditText etNum = (EditText) convertView.findViewById(R.id.etNum);
		// 单位
		Button btnUnit = (Button) convertView.findViewById(R.id.btnUnit);
		// 本次库存
		final TextView tv_Bfci = (TextView) convertView.findViewById(R.id.tv_Bfci);
		final TextView tv_dicPrice = (TextView) convertView.findViewById(R.id.tv_dicPrice);
		final DefDocItemXS itemXS = listItems.get(position);
		// 显示上次 采购
		btnUnit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				GoodsUnitDAO goodsUnitDAO = new GoodsUnitDAO();
				final List<GoodsUnit> listGoodsunit = goodsUnitDAO.queryGoodsUnits(itemXS.getGoodsid());
				final String[] arrayOfString = new String[listGoodsunit.size()];
				for (int i = 0; i < arrayOfString.length; i++) {
					arrayOfString[i] = (listGoodsunit.get(i)).getUnitname();
				}
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
				localBuilder.setTitle("单位选择");
				localBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int items) {
						// 设置单位
						((TextView) v).setText(arrayOfString[items].toString());
						GoodsUnit goodsUnit = listGoodsunit.get(items);
						// 设置 选择单位
						itemXS.setUnitid(goodsUnit.getUnitid());
						itemXS.setUnitname(goodsUnit.getUnitname());
						// 设置当前库存
						String stocknum = DocUtils.Stocknum(itemXS.getStocknum(), goodsUnit);
						itemXS.goodStock = stocknum;
						tv_Bfci.setText(stocknum);
						PDH.show(context, new PDH.ProgressCallBack() {

							@Override
							public void action() {
								// 查询商品价格
								final ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(),
										itemXS);
								if (goodsPrice == null) {
									return;
								}
								itemXS.setPrice(goodsPrice.getPrice());
								context.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										tv_dicPrice.setText("单价:" + goodsPrice.getPrice() + "元");
									}
								});
							}
						});
					}
				});
				localBuilder.create().show();
			}
		});
		tvName.setText(itemXS.getGoodsname());
		tvBarcode.setText(itemXS.getBarcode());
		btnUnit.setText(itemXS.getUnitname());
		tv_dicPrice.setText("单价:" + itemXS.getPrice() + "元");
		tv_Bfci.setText(itemXS.goodStock);
		if (listItems.get(position).getNum() == 0.0d) {
			etNum.setText("");
		} else {
			String num = Utils.removeZero(itemXS.getNum() + "");
			etNum.setText(num);
		}
		// if (selectPosition == position) {
		// etNum.requestFocus();
		// }
		// etNum.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// selectPosition = position;
		// }
		// });
		etNum.setTag(Integer.valueOf(position));
		btnUnit.setTag(Integer.valueOf(position));
		etNum.setFocusable(true);
		etNum.setFilterTouchesWhenObscured(true);
		etNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int i = ((Integer) etNum.getTag()).intValue();
				if (s.toString().length() > 0) {
					if (Double.parseDouble(s.toString()) > 0.0D) {
						(listItems.get(i)).setNum(Utils.normalize(Utils.getDouble(s.toString()).doubleValue(), 2));
					}
				} else {
					listItems.get(i).setNum(0);
				}
			}
		});
		return convertView;
	}

}
