package com.ahjswy.cn.utils;

import java.util.List;

import com.ahjswy.cn.app.MyApplication;
import com.ahjswy.cn.bean.bmob.ExceptionLog;
import com.ahjswy.cn.dao.Exception_logDAO;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

public class BmobUtils {
	final static Exception_logDAO logDAO = new Exception_logDAO();
	static BmobUtils bmobutils;
	private List<BmobObject> listexception;

	public static BmobUtils getInstance() {
		if (bmobutils == null) {
			bmobutils = new BmobUtils();
		}
		return bmobutils;
	}

	/**
	 * 长传日志
	 */
	public void updata() {
		listexception = logDAO.queryBmobAll();
		if (listexception.isEmpty()) {
			return;
		}
		new BmobBatch().insertBatch(listexception).doBatch(new QueryListListener<BatchResult>() {

			@Override
			public void done(List<BatchResult> o, BmobException e) {
				if (e == null) {
					for (int i = 0; i < o.size(); i++) {
						BatchResult result = o.get(i);
						ExceptionLog objectlog = (ExceptionLog) listexception.get(i);
						BmobException ex = result.getError();
						if (ex == null) {
							if (logDAO.deleteRow(objectlog.id)) {
								MLog.d("删除" + objectlog.id);
							}
							// 删除成功的
							MLog.d("第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + ","
									+ result.getUpdatedAt());
						} else {
							MLog.d("第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
						}

					}

				} else {
					MLog.d("上传云日志 失败 ");
				}
			}
		});
	}
}
