package com.ahjswy.cn.crash;

import java.lang.Thread.UncaughtExceptionHandler;

import com.ahjswy.cn.app.MyApplication;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.bean.bmob.ExceptionLog;
import com.ahjswy.cn.dao.Exception_logDAO;
import com.ahjswy.cn.ui.MAlertDialog;
import com.ahjswy.cn.utils.MLog;
import com.ahjswy.cn.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CrashHandler implements UncaughtExceptionHandler {
	private static final CrashHandler sHandler = new CrashHandler();
	private static final UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	private Context context;
	private UncaughtExceptionHandler mDefaultHandler;
	private Exception_logDAO logdao;
	private boolean crashing = false;

	public static CrashHandler getInstance() {
		return sHandler;
	}

	/**
	 * 初始化
	 *
	 * @param context
	 */
	public void init(Context context) {
		logdao = new Exception_logDAO();
		this.context = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	Throwable ex;
	Thread thread;

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (crashing) {
			return;
		}
		crashing = true;
		this.ex = ex;
		this.thread = thread;
		ex.printStackTrace();
		handlelException(ex);
		// 我们没有处理异常 并且默认异常处理不为空 则交给系统处理
		// if (!handlelException(ex) && mDefaultHandler != null) {
		// // 系统处理
		// mDefaultHandler.uncaughtException(thread, ex);
		// }
	}

	/**
	 * 处理异常消息
	 * 
	 * @param ex
	 * @return
	 */
	private boolean handlelException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		try {
			StackTraceElement[] trace = ex.getStackTrace();
			StringBuffer buffer = new StringBuffer();
			for (StackTraceElement s : trace) {
				buffer.append(s.toString()).append("\n");
			}
			MLog.d(this, buffer.toString() + "错误信息>>>" + ex.getMessage());
			reqLog = getDefaultReqLog();
			reqLog.message = ex.getMessage();
			reqLog.log = buffer.toString();
			reqLog.save(listener);
			// MAlertDialog dialog = new MAlertDialog(context);
			// dialog.setTitle("网络质量差");
			// dialog.setMessage("是否重新打开软件?否(退出!)");
			// dialog.setNeutralButton(new MAlertDialog.OnClickListener() {
			//
			// @Override
			// public void onClick(MAlertDialog dialog) {
			// Intent intent = new Intent(context, Object.class);
			// context.startActivity(intent);
			// }
			// });
			// dialog.setNegativeButton(new MAlertDialog.OnClickListener() {
			//
			// @Override
			// public void onClick(MAlertDialog dialog) {
			// MyApplication.getInstance().exit();
			// }
			// });
			// dialog.show();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	SaveListener<String> listener = new SaveListener<String>() {

		@Override
		public void done(String objectId, BmobException e) {
			if (e != null) {
				// 写入本地数据库
				logdao.insertLog(reqLog);
				MLog.d(this, e.getMessage());
			} else {
				// 上传云端成功
				MLog.d("上传成功!");
			}
			if (mDefaultHandler != null) {
				mDefaultHandler.uncaughtException(thread, ex);
			}
		}
	};
	private ExceptionLog reqLog;

	public ExceptionLog getDefaultReqLog() {
		ExceptionLog log = new ExceptionLog();
		log.userid = SystemState.getUser().getId();
		log.username = SystemState.getUser().getName();
		log.model = android.os.Build.MODEL;
		log.versionname = MyApplication.getInstance().getVersionName();
		log.deviceid = MyApplication.getInstance().getAndroidId();
		log.datetime = Utils.getDataTime();
		log.accountset = SystemState.getAccountSet().getDatabase();
		return log;
	}

}
