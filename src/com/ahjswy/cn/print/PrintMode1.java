package com.ahjswy.cn.print;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.model.FieldSaleItemForPrint;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.utils.Utils;

import android.os.SystemClock;
import android.util.Log;

/**
 * 有单价的 打印模板
 * 
 * @author Administrator
 *
 */

public class PrintMode1 extends PrintMode {

	public PrintMode1(List<HashMap<String, String>> pageinfo) {
		super(pageinfo);
	}

	protected boolean print() throws IOException {
		printHead();
		println("-------------------------------");
		// 同一行 文字 设置 指定 位置 中间 空格 代替
		print_base(this.print_left(" "), 12, "数量");
		print_base(12, 22, "单价");
		print_base(22, 32, "小计");
		enter();
		for (int i = 0; i < this.datainfo.size(); i++) {
			this.printItem(this.datainfo.get(i));
		}
		this.println("-------------------------------");
		this.printFoot();
		this.tear_off();
		datainfo.clear();
		return true;
	}

	private void printItem(FieldSaleItemForPrint arg13) {
		int v11 = 22;
		int v10 = 12;
		String v3 = "";
		if (!arg13.getItemtype().equals("销") && !"销售退货单".equals(this.docInfo.getDoctype())) {
			v3 = "[" + arg13.getItemtype() + "]";
		}
		String v0 = String.valueOf(v3) + arg13.getGoodsname();
		if (!"0".equals(new AccountPreference().getValue("printbarcode", "0"))) {
			if (!TextUtils.isEmpty(arg13.getBarcode())) {
				v0 = String.valueOf(v0) + "\n" + arg13.getBarcode();
				if (!TextUtils.isEmpty(arg13.getRemark())) {
					v0 = String.valueOf(v0) + "，" + arg13.getRemark();
				}
			} else if (!TextUtils.isEmpty(arg13.getRemark())) {
				v0 = String.valueOf(v0) + "\n" + arg13.getRemark();
			}
		}
		String v2 = Utils.getNumber(arg13.getNum());
		String v4 = Utils.getSubtotalMoney(arg13.getPrice());
		String v5 = Utils.getSubtotalMoney(arg13.getDiscountsubtotal());
		if ("0".equals(new AccountPreference().getValue("clearlastzero", "0"))) {
			v2 = Utils.cutLastZero(v2);
			v4 = Utils.cutLastZero(v4);
			v5 = Utils.cutLastZero(v5);
		}
		if (("0".equals(new AccountPreference().getValue("minustuihuo", "0")))
				&& ((arg13.getItemtype().equals("退")) || (arg13.getItemtype().equals("入")))
				&& !"销售退货单".equals(this.docInfo.getDoctype())) {
			v2 = "-" + Utils.cutLastZero(v2);
			v5 = "-" + Utils.cutLastZero(v5);
		}
		v2 = String.valueOf(v2) + arg13.getUnitname();
		try {
			this.print_left(v0);

			this.enter();
			this.print_base(this.print_left("   "), v10, v2);
			this.print_base(v10, v11, v4);
			this.print_base(v11, 32, v5);
			this.enter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
