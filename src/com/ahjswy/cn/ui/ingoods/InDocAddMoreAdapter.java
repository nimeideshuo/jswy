package com.ahjswy.cn.ui.ingoods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.model.DefDocItemXS;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.utils.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class InDocAddMoreAdapter extends BaseAdapter {
	private Calendar cal;
	private Context context;
	private List<DefDocItemXS> listItems;
	private int selectPosition = -1;// 记住选中的edtitext

	public InDocAddMoreAdapter(Context paramContext) {
		this.context = paramContext;
		this.listItems = new ArrayList<DefDocItemXS>();
		cal = Calendar.getInstance();
	}

	@Override
	public int getCount() {
		return this.listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return this.listItems.get(position);
	}

	public List<DefDocItemXS> getData() {
		return this.listItems;
	}

	// 添加一个
	public void addData(DefDocItemXS item) {
		listItems.add(item);
		notifyDataSetChanged();
	}

	public void addItemDate(ArrayList<DefDocItemXS> listItem) {
		listItems.addAll(listItem);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.item_indoc_add_more_goods, null);
		TextView tvName = ((TextView) convertView.findViewById(R.id.tvName));
		// TextView tvBarcode = ((TextView)
		// convertView.findViewById(R.id.tvBarcode));
		Button btnUnit = ((Button) convertView.findViewById(R.id.btnUnit));
		final EditText etNum = ((EditText) convertView.findViewById(R.id.etNum));
		// final EditText etBatch = ((EditText)
		// convertView.findViewById(R.id.etBatch));
		// Button btnDate = ((Button) convertView.findViewById(R.id.btnDate));
		// if (listItems.get(position).isIsusebatch()) {
		// convertView.findViewById(R.id.linearLayout).setVisibility(0);
		// convertView.findViewById(R.id.divider).setVisibility(0);
		// }
		etNum.setFocusable(true);
		etNum.setFilterTouchesWhenObscured(true);
		if (selectPosition == position) {
			etNum.requestFocus();
		}
		etNum.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				selectPosition = position;
			}
		});
		btnUnit.setTag(position);
		etNum.setTag(Integer.valueOf(position));
		// btnDate.setTag(Integer.valueOf(position));
		// btnDate.setOnClickListener(dateClickListener);
		// etBatch.setTag(Integer.valueOf(position));
		btnUnit.setOnClickListener(onClickListener);
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
				if (s.toString().length() > 0 && Double.parseDouble(s.toString()) > 0.0D) {
					(listItems.get(i)).setNum(Utils.normalize(Utils.getDouble(s.toString()).doubleValue(), 2));
				}
			}
		});
		// etBatch.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// int i = ((Integer) etBatch.getTag()).intValue();
		// ((DefDocItem) listItems.get(i)).setBatch(s.toString());
		// }
		// });
		tvName.setText(listItems.get(position).getGoodsname());
		// tvBarcode.setText(listItems.get(position).getBarcode());
		btnUnit.setText(listItems.get(position).getUnitname());
		if ((listItems.get(position).getNum() + "".length()) > 0 && listItems.get(position).getNum() > 0.0D) {
			String num = Utils.removeZero(listItems.get(position).getNum() + "");
			etNum.setText(num);
		}
		// etBatch.setText(listItems.get(position).getBatch());
		// if (listItems.get(position).isIsusebatch()) {
		// // 显示
		// etBatch.setVisibility(0);
		// // btnDate.setVisibility(0);
		// if (listItems.get(position).getProductiondate() != null) {
		// long l = 0;
		// try {
		// l = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").parse(listItems.get(position).getProductiondate())
		// .getTime();
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// btnDate.setText(Utils.formatDate(l, "yyyy-MM-dd"));
		// } else {
		// btnDate.setText(Utils.formatDate(new Date().getTime(),
		// "yyyy-MM-dd"));
		// }
		// listItems.get(position).setProductiondate(Utils.formatDate(new
		// Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
		// } else {
		// // 显示
		// etBatch.setVisibility(View.GONE);
		// btnDate.setVisibility(View.GONE);
		// }

		return convertView;
	}

	public void setData(List<DefDocItemXS> paramList) {
		this.listItems.clear();
		this.listItems.addAll(paramList);
		notifyDataSetChanged();
	}

	// public class Item {
	// // 时间
	// private Button btnDate;
	// // 单位
	// private Button btnUnit;
	// // 批次
	// private EditText etBatch;
	// // 数量
	// private EditText etNum;
	// // 条码
	// private TextView tvBarcode;
	// // 名称
	// private TextView tvName;
	//
	// public Item(View view) {
	// this.tvName = ((TextView) view.findViewById(R.id.tvName));
	// this.tvBarcode = ((TextView) view.findViewById(R.id.tvBarcode));
	// this.btnUnit = ((Button) view.findViewById(R.id.btnUnit));
	// this.etNum = ((EditText) view.findViewById(R.id.etNum));
	// this.etBatch = ((EditText) view.findViewById(R.id.etBatch));
	// this.btnDate = ((Button) view.findViewById(R.id.btnDate));
	// // this.etNum.setMode(2);
	// }
	//
	// public Item() {
	// }
	//
	// public void setValue(DefDocItem paramDefDocItem) throws ParseException {
	// this.tvName.setText(paramDefDocItem.getGoodsname());
	// this.tvBarcode.setText(paramDefDocItem.getBarcode());
	// this.btnUnit.setText(paramDefDocItem.getUnitname());
	// if (paramDefDocItem.getNum() > 0.0D) {
	// String num = Utils.removeZero(paramDefDocItem.getNum() + "");
	// etNum.setText(num);
	// } else {
	// etNum.setText("");
	// }
	// this.etBatch.setText(paramDefDocItem.getBatch());
	// if (paramDefDocItem.isIsusebatch()) {
	// // 显示
	// this.etBatch.setVisibility(0);
	// this.btnDate.setVisibility(0);
	// if (paramDefDocItem.getProductiondate() != null) {
	// long l = new SimpleDateFormat("yyyy-MM-dd
	// HH:mm:ss").parse(paramDefDocItem.getProductiondate())
	// .getTime();
	// this.btnDate.setText(Utils.formatDate(l, "yyyy-MM-dd"));
	// } else {
	// this.btnDate.setText(Utils.formatDate(new Date().getTime(),
	// "yyyy-MM-dd"));
	// }
	// paramDefDocItem.setProductiondate(Utils.formatDate(new Date().getTime(),
	// "yyyy-MM-dd HH:mm:ss"));
	// return;
	// }
	// // 显示
	// this.etBatch.setVisibility(View.GONE);
	// this.btnDate.setVisibility(View.GONE);
	//
	// }
	// }

	// 设置单位
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		public void onClick(final View paramAnonymousView) {
			final int i = ((Integer) paramAnonymousView.getTag()).intValue();
			final List<GoodsUnit> localList = new GoodsUnitDAO().queryGoodsUnits(listItems.get(i).getGoodsid());
			final String[] arrayOfString = new String[localList.size()];
			for (int j = 0; j < localList.size(); j++) {
				arrayOfString[j] = (localList.get(j)).getUnitname();
			}
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(InDocAddMoreAdapter.this.context);
			localBuilder.setTitle("单位选择");
			localBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
					((Button) paramAnonymousView).setText(arrayOfString[paramAnonymous2Int]);
					DefDocItemXS localDefDocItem = (DefDocItemXS) InDocAddMoreAdapter.this.listItems.get(i);
					localDefDocItem.setUnitid(((GoodsUnit) localList.get(paramAnonymous2Int)).getUnitid());
					localDefDocItem.setUnitname(((GoodsUnit) localList.get(paramAnonymous2Int)).getUnitname());
				}
			});
			localBuilder.create().show();
			return;
		}
	};
	private View.OnClickListener dateClickListener = new View.OnClickListener() {
		private Button btn;
		private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1,
					int paramAnonymous2Int2, int paramAnonymous2Int3) {
				cal.set(1, paramAnonymous2Int1);
				cal.set(2, paramAnonymous2Int2);
				cal.set(5, paramAnonymous2Int3);
				SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				btn.setText(localSimpleDateFormat.format(Long.valueOf(cal.getTimeInMillis())));
				(listItems.get(position)).setProductiondate(btn.getText() + " 00:00:00");
				if (Utils.intGenerateBatch != 0) {
					(listItems.get(position)).setBatch(Utils.generateBatch(cal.getTimeInMillis()));
					notifyDataSetChanged();
				}
			}
		};
		private int position = 0;

		public void onClick(View view) {
			this.btn = ((Button) view);
			this.position = ((Integer) view.getTag()).intValue();
			try {
				cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(btn.getText().toString()));
				new DatePickerDialog(context, listener, cal.get(1), cal.get(2), cal.get(5)).show();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return;
		}
	};

	// private class BatchWatcher implements TextWatcher {
	// private Item item;
	//
	// public BatchWatcher(Item item) {
	// this.item = item;
	// }
	//
	// public void afterTextChanged(Editable paramEditable) {
	// int i = ((Integer) this.item.etBatch.getTag()).intValue();
	// ((DefDocItem) listItems.get(i)).setBatch(paramEditable.toString());
	// }
	//
	// public void beforeTextChanged(CharSequence paramCharSequence, int
	// paramInt1, int paramInt2, int paramInt3) {
	// }
	//
	// public void onTextChanged(CharSequence paramCharSequence, int paramInt1,
	// int paramInt2, int paramInt3) {
	//
	// }
	// }

	// @SuppressWarnings("unused")
	// private class NumWatcher implements TextWatcher {
	// private Item item;
	//
	// public NumWatcher(Item item) {
	// this.item = item;
	// }
	//
	// public void afterTextChanged(Editable s) {
	// int i = ((Integer) this.item.etNum.getTag()).intValue();
	// if (s.toString().length() > 0) {
	// if (Double.parseDouble(s.toString()) > 0.0D) {
	// (listItems.get(i)).setNum(Utils.normalize(Utils.getDouble(s.toString()).doubleValue(),
	// 2));
	// }
	// } else {
	// (listItems.get(i)).setNum(0.0D);
	// }
	//
	// }
	//
	// public void beforeTextChanged(CharSequence paramCharSequence, int
	// paramInt1, int paramInt2, int paramInt3) {
	//
	// }
	//
	// public void onTextChanged(CharSequence paramCharSequence, int paramInt1,
	// int paramInt2, int paramInt3) {
	// }
	// }

}
