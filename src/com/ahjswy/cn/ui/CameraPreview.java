package com.ahjswy.cn.ui;

import java.io.IOException;

import com.ahjswy.cn.utils.MLog;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView {
	private String TAG;
	private Camera mCamera;
	private SurfaceHolder mHolder;

	public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CameraPreview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CameraPreview(Context arg2, Camera arg3) {
		super(arg2);
		this.TAG = "TAG";
		this.mCamera = arg3;
		this.mHolder = this.getHolder();
		// this.mHolder.addCallback(((SurfaceHolder$Callback) this));
	}

	public void surfaceChanged(SurfaceHolder arg5, int arg6, int arg7, int arg8) {
		if (this.mHolder.getSurface() != null) {
			try {
				this.mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				this.mCamera.setPreviewDisplay(this.mHolder);
				this.mCamera.startPreview();
			} catch (Exception v0) {
				MLog.d(this.TAG, "Error starting camera preview: " + v0.getMessage());
			}
		}
	}

	public void surfaceCreated(SurfaceHolder arg4) {
		try {
			this.mCamera.setPreviewDisplay(arg4);
			this.mCamera.startPreview();
		} catch (IOException v0) {
			MLog.d("Error setting camera preview: " + v0.getMessage());
		}
	}

}
