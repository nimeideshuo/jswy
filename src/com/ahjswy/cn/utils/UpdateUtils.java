package com.ahjswy.cn.utils;

import java.util.HashMap;
import java.util.List;

import com.ahjswy.cn.dao.DBOpenHelper;
import com.ahjswy.cn.dao.GoodsImageDAO;
import com.ahjswy.cn.model.GoodsImage;
import com.ahjswy.cn.request.ReqSynUpdateInfo;
import com.ahjswy.cn.service.ServiceSynchronize;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

public class UpdateUtils {

	public boolean executeUpdate(Handler paramHandler, List<ReqSynUpdateInfo> paramList, long paramLong) {
		// SQL http 接口
		ServiceSynchronize localServiceSynchronize = new ServiceSynchronize(paramLong);
		SwyUtils localSwyUtils = new SwyUtils();
		paramHandler.sendMessage(
				paramHandler.obtainMessage(-1, Integer.valueOf(localSwyUtils.getSumPagesFromUpdateInfo(paramList))));
		int i = 0;
		int log_deleterecord = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "log_deleterecord");
		if (log_deleterecord > 0) {
			for (int j = 1; j <= log_deleterecord; j++) {
				List<HashMap<String, String>> localList2 = localServiceSynchronize.syn_QueryDeleteRecordRecords(j);
				if (localList2 == null) {
					return false;
				}
				saveToLocalDB(localList2);
				i++;
				paramHandler.sendEmptyMessage(i);
			}
		}
		// 部门
		int m = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_department");
		if (m > 0) {
			for (int j = 1; j <= m; j++) {
				List<HashMap<String, String>> localList2 = localServiceSynchronize.syn_QueryDepartmentRecords(j);
				if (localList2 == null) {
					return false;
				}
				saveToLocalDB(localList2);
				i++;
				paramHandler.sendEmptyMessage(i);
			}

		}
		// 仓库
		int i1 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_warehouse");
		if (i1 > 0) {
			for (int j = 1; j <= i1; j++) {
				List<HashMap<String, String>> localList3 = localServiceSynchronize.syn_QueryWarehouseRecords(j);
				if (localList3 == null) {
					return false;
				}
				saveToLocalDB(localList3);
				i++;
				paramHandler.sendEmptyMessage(i);
			}

		}
		// 支付
		int i3 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_paytype");
		if (i3 > 0) {
			for (int j = 1; j <= i3; j++) {
				List<HashMap<String, String>> localList4 = localServiceSynchronize.syn_QueryPayTypeRecords(j);
				if (localList4 == null) {
					return false;
				}
				saveToLocalDB(localList4);
				i++;
				paramHandler.sendEmptyMessage(i);
			}

		}
		// 所有客户 包括供应商 由于返回值 电话号码 返回值 NULL 放在前面防止客户 电话等信息被覆盖
		int i6 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "cu_allcustomer");
		if (i6 > 0) {
			for (int j = 1; j <= i6; j++) {
				List<HashMap<String, String>> localList5 = localServiceSynchronize.syn_QueryAllCustomerRecords(j);
				if (localList5 == null) {
					return false;
				}
				saveToLocalDB(localList5);
				i++;
				paramHandler.sendEmptyMessage(i);
			}
		}

		// 客户
		int i5 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "cu_customer");
		if (i5 > 0) {
			for (int j = 1; j <= i5; j++) {
				List<HashMap<String, String>> localList5 = localServiceSynchronize.syn_QueryCustomerRecords(j);
				if (localList5 == null) {
					return false;
				}
				saveToLocalDB(localList5);
				i++;
				paramHandler.sendEmptyMessage(i);
			}
		}

		int i7 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "cu_customertype");
		if (i7 > 0) {
			for (int j = 1; j <= i7; j++) {
				List<HashMap<String, String>> localList6 = localServiceSynchronize.syn_QueryCustomerTypeRecords(j);
				if (localList6 == null) {
					return false;
				}
				saveToLocalDB(localList6);
				i++;
				paramHandler.sendEmptyMessage(i);
			}
		}
		int i9 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_region");
		if (i9 > 0) {
			for (int j = 1; j <= i9; j++) {
				List<HashMap<String, String>> localList7 = localServiceSynchronize.syn_QueryRegionRecords(j);
				if (localList7 == null) {
					return false;
				}
				saveToLocalDB(localList7);
				i++;
				paramHandler.sendEmptyMessage(i);
			}
		}
		int i11 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_visitline");
		if (i11 > 0) {
			for (int j = 1; j <= i11; j++) {
				List<HashMap<String, String>> localList8 = localServiceSynchronize.syn_QueryVisitLineRecords(j);
				if (localList8 == null) {

				}
				saveToLocalDB(localList8);
				i++;
				paramHandler.sendEmptyMessage(i);

			}
		}
		int i13 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_goods");
		if (i13 > 0) {
			for (int j = 1; j <= i13; j++) {
				List<HashMap<String, String>> localList9 = localServiceSynchronize.syn_QueryGoodsRecords(j);
				if (localList9 == null) {
					return false;
				}
				saveToLocalDB(localList9);
				i++;
				paramHandler.sendEmptyMessage(i);

			}
		}
		int i15 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_goodsunit");
		if (i15 > 0) {
			for (int j = 1; j <= i15; j++) {
				List<HashMap<String, String>> localList10 = localServiceSynchronize.syn_QueryGoodsUnitRecords(j);
				if (localList10 == null) {
					return false;
				}
				saveToLocalDB(localList10);
				i++;
				paramHandler.sendEmptyMessage(i);

			}
		}
		int v8 = (int) localSwyUtils.getPagesFromUpdateInfo(paramList, "sz_goodsimage");
		int v5 = 1;
		if (v5 <= v8) {
			List<HashMap<String, String>> records = localServiceSynchronize.syn_QueryGoodsImageRecords(v5);
			if (records == null) {
				return false;
			}
			this.saveToLocalDB(records);
			i++;
			paramHandler.sendEmptyMessage(i);
			++v5;
			GoodsImageDAO v3 = new GoodsImageDAO();
			List<GoodsImage> listNoImage = v3.getNoImage();
			MLog.d(listNoImage.size());
			if (listNoImage.size() > 0) {
				paramHandler.sendMessage(paramHandler.obtainMessage(-2, listNoImage.size()));
				for (v5 = 0; v5 < listNoImage.size(); ++v5) {
					GoodsImage goodsImage = listNoImage.get(v5);
					v3.saveImage(goodsImage.getSerialid(),
							localServiceSynchronize.syn_QueryGoodsImage(goodsImage.getImagePath()),
							goodsImage.getImagePath());
					paramHandler.sendEmptyMessage(v5 + 1);
				}
			}
		}

		// if (i ==
		// Integer.valueOf(localSwyUtils.getSumPagesFromUpdateInfo(paramList)))
		// {
		// // 同步成功
		// paramHandler.sendEmptyMessage(0);
		// return true;
		// } else {
		// paramHandler.sendEmptyMessage(-4);
		// }
		return true;
	}

	// 保存 HashMap 到 SQL
	public void saveToLocalDB(HashMap<String, String> map) {
		SQLiteDatabase localSQLiteDatabase = null;
		if (map.size() > 0 && map != null) {
			localSQLiteDatabase = new DBOpenHelper().getWritableDatabase();
			localSQLiteDatabase.beginTransaction();
			for (String key : map.keySet()) {
				localSQLiteDatabase.execSQL(map.get(key));
			}
			if (map.size() > 0) {
				// 必须写词句， 否则，查询不到插入的信息
				localSQLiteDatabase.setTransactionSuccessful();
			}
			// 结束
			localSQLiteDatabase.endTransaction();
			// 关闭
			localSQLiteDatabase.close();
		}
	}

	// 保存 List 到 SQL
	public void saveToLocalDB(List<HashMap<String, String>> paramList) {
		SQLiteDatabase localSQLiteDatabase = null;
		if ((paramList != null) && (paramList.size() > 0)) {
			localSQLiteDatabase = new DBOpenHelper().getWritableDatabase();
			// 开始
			localSQLiteDatabase.beginTransaction();
			try {
				for (int j = 0; j < paramList.size(); j++) {
					if (paramList.get(j).get("sql").trim().length() > 0) {
						String sql = (paramList.get(j)).get("sql").trim();
						localSQLiteDatabase.execSQL(sql);
					}
				}
			} catch (Exception e) {

			} finally {
				// 必须写词句， 否则，查询不到插入的信息
				localSQLiteDatabase.setTransactionSuccessful();
				// 结束
				localSQLiteDatabase.endTransaction();
				// 关闭
				localSQLiteDatabase.close();
			}

		}

	}

}
