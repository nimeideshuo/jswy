package com.ahjswy.cn.utils;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.MyApplication;
import com.ahjswy.cn.ui.LoadingDialog;
import com.ahjswy.cn.ui.WaitingDialog;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PDH {
	public static final int DEFAULT = 0;
	public static final int ERROR = 2;
	public static final int FAIL = 3;
	public static final int SUCCESS = 1;
	private static int TOAST_SHOW_TIME = 2000;
	private static Handler handler = new Handler(); // dialog_err_icon
													// dialog_fail
	private static int[] icons = { R.drawable.dialog_def_icon, R.drawable.dialog_suc_icon, R.drawable.dialog_err_icon,
			R.drawable.dialog_fail };
	private static Toast toast;
	private static ImageView toastImage;
	private static TextView toastText;
	private static View toastView;

	private static String getText(int paramInt) {
		return MyApplication.getInstance().getResources().getString(paramInt);
	}

	public static void show(final Activity activity, final ProgressCallBack paCallBack) {
		final WaitingDialog localWaitingDialog = new WaitingDialog(activity);
		localWaitingDialog.show();
		new Thread() {
			public void run() {
				paCallBack.action();
				PDH.handler.post(new Runnable() {
					public void run() {
						localWaitingDialog.dismiss();
					}
				});
			}
		}.start();
	}

	public static void show(final Activity activity, final String text, final ProgressCallBack progressCallBack) {
		final LoadingDialog localLoadingDialog = new LoadingDialog(activity);
		localLoadingDialog.show(text);
		new Thread() {
			public void run() {
				progressCallBack.action();
				PDH.handler.post(new Runnable() {
					public void run() {
						localLoadingDialog.dismiss();
					}
				});
			}
		}.start();
	}

	public static void showError(int paramInt) {
		showToast(paramInt, TOAST_SHOW_TIME, ERROR);
	}

	public static void showError(String message) {
		showToast(message, TOAST_SHOW_TIME, ERROR);
	}

	public static void showFail(int paramInt) {
		showToast(paramInt, TOAST_SHOW_TIME, FAIL);
	}

	public static void showFail(String message) {
		showToast(message, TOAST_SHOW_TIME, FAIL);
	}

	public static void showMessage(int paramInt) {
		showToast(paramInt, TOAST_SHOW_TIME, DEFAULT);
	}

	public static void showMessage(String message) {
		showToast(message, TOAST_SHOW_TIME, DEFAULT);
	}

	public static void showSuccess(int paramInt) {
		showToast(paramInt, TOAST_SHOW_TIME, SUCCESS);
	}

	public static void showSuccess(String message) {
		showToast(message, TOAST_SHOW_TIME, SUCCESS);
	}

	public static void showToast(int paramInt1, int paramInt2, int paramInt3) {
		try {
			showToast(getText(paramInt1), paramInt2, paramInt3);
			return;
		} finally {
		}
	}

	public static void showToast(String paramString, int paramInt1, int imView) {
		try {
			if (toastView == null) {
				toastView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.toast, null);
				toastImage = (ImageView) toastView.findViewById(R.id.iv_toast);
				toastText = (TextView) toastView.findViewById(R.id.tv_toast);
				toast = new Toast(MyApplication.getInstance());
				toast.setView(toastView);
				toast.setGravity(17, 0, 0);
			}
			toastText.setText(paramString);
			toastImage.setImageResource(icons[imView]);
			toast.setDuration(paramInt1);
			toast.show();
			return;
		} finally {
		}
	}

	public static abstract interface ProgressCallBack {
		public abstract void action();
	}
}
