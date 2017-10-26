package com.ahjswy.cn.ui.outgoods;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.request.ReqStrGetGoodsPrice;
import com.ahjswy.cn.response.RespGoodsPriceEntity;
import com.ahjswy.cn.service.ServiceSupport;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.TextUtils;
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

	public void setItem(List<DefDocItemXS> listItems) {
		this.listItems = listItems;
	}

	public void setData(List<DefDocItemXS> listItems) {
		this.listItems.clear();
		this.listItems.addAll(listItems);
		notifyDataSetChanged();
	}

	public DefDocXS doc;

	public void setDoc(DefDocXS doc) {
		this.doc = doc;
	}

	int selectPosition = -1;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.act_out_doc_add_moreadapter_item, null);
		// 商品 name
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		// 二维码
		TextView tvBarcode = (TextView) convertView.findViewById(R.id.tvBarcode);
		TextView tv_specification = (TextView) convertView.findViewById(R.id.tv_specification);
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
						String stocknum = DocUtils.Stocknum(itemXS.stocknum, goodsUnit);
						itemXS.goodStock = stocknum;
						itemXS.unit = goodsUnit;
						tv_Bfci.setText("库存:" + (TextUtils.isEmpty(itemXS.goodStock) ? "?" : itemXS.goodStock));
						PDH.show(context, new PDH.ProgressCallBack() {

							@Override
							public void action() {
								// 查询商品价格
								final ReqStrGetGoodsPrice goodsPrice = DocUtils.GetMultiGoodsPrice(doc.getCustomerid(),
										itemXS);
								if (goodsPrice == null) {
									return;
								}
								context.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										itemXS.setPrice(goodsPrice.getPrice());
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
		tv_specification.setText(itemXS.getSpecification() == null ? "" : "规格:" + itemXS.getSpecification());
		tvBarcode.setText(itemXS.getBarcode());
		btnUnit.setText(itemXS.getUnitname());
		tv_dicPrice.setText("单价:" + itemXS.getPrice() + "元");
		tv_dicPrice.setOnClickListener(priceOnClick);
		tv_dicPrice.setTag(Integer.valueOf(position));
		tv_Bfci.setText("库存:" + (TextUtils.isEmpty(itemXS.goodStock) ? "?" : itemXS.goodStock));
		etNum.setText(itemXS.getNum() == 0 ? "" : itemXS.getNum() + "");
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
		// etNum.setSelection(position - 1);// 调整光标到最后一行
		int selectionStart = etNum.getSelectionStart();
		int selectionEnd = etNum.getSelectionEnd();
		System.out.println("selectionStart>>" + selectionStart);
		System.out.println("selectionEnd>>" + selectionEnd);
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
				// String string = s.toString();
				// string = "";
				// if (!TextUtils.isEmpty(string)) {
				// if (Double.parseDouble(string) > 0.0D) {
				(listItems.get(i)).setNum(Utils.normalize(Utils.getDouble(s.toString()).doubleValue(), 2));
				// }
				// } else {
				// listItems.get(i).setNum(0);
				// }
			}
		});

		return convertView;
	}

	OnClickListener priceOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = ((Integer) v.getTag()).intValue();
			final TextView price = (TextView) v;
			final DefDocItemXS itemXS = listItems.get(i);
			String goodsPrice = new ServiceSupport().sup_QueryGoodsPrice(itemXS.getGoodsid());
			if (RequestHelper.isSuccess(goodsPrice)) {

				final List<RespGoodsPriceEntity> listGoodPrice = JSONUtil.str2list(goodsPrice,
						RespGoodsPriceEntity.class);
				if (listGoodPrice.isEmpty()) {
					PDH.showFail("没有查询到价格!");
					return;
				}
				String arrayPrice[] = new String[listGoodPrice.size()];
				for (int j = 0; j < listGoodPrice.size(); j++) {
					RespGoodsPriceEntity priceEntity = listGoodPrice.get(j);
					arrayPrice[j] = priceEntity.getPricesystemname() + ":" + priceEntity.getPrice();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("商品价格");
				builder.setItems(arrayPrice, new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						double price2 = listGoodPrice.get(which).getPrice();
						itemXS.setPrice(price2);
						price.setText("单价:" + price2 + "元");
					}
				});
				builder.show();
			} else {
				PDH.showFail("价格查询失败!请重试!");
				return;
			}
		}
	};
}
