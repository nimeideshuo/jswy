package com.ahjswy.cn.ui;

import com.ahjswy.cn.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;

public class CameraActivity extends BaseActivity {
	private FrameLayout frameLayout;
	private Button btnTakePicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_field_camera);
		initView();
		initData();
	}

	private void initView() {
		frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(openCameraListener);
	}

	private void initData() {

	}

	OnClickListener openCameraListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// openCamera(1, i);
		}
	};
	private Camera mCamera;

	public Camera openCamera(int paramInt1, int cameraId) {
		try {
			mCamera = Camera.open(cameraId);
			mCamera.setDisplayOrientation(getCameraDisplayOrientation(cameraId, this.mCamera));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCamera;
		// if (this.mCamera != null) {
		// // this.type = arg3;
		// CameraPreview cameraPreview = new CameraPreview(this, this.mCamera);
		// this.frameLayout.addView(cameraPreview);
		// }
		// Fail to connect to camera service

	}

	private int frontCameraid = -1;

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("CameraActivity>>>onResume");
		if (this.mCamera == null) {
			int v0 = Camera.getNumberOfCameras();
			Camera.CameraInfo v2 = new Camera.CameraInfo();
			int v1;
			// for (v1 = 0; v1 < v0; ++v1) {
			// Camera.getCameraInfo(v1, v2);
			// if (v2.facing == 0) {
			// this.backCameraid = v1;
			// } else if (v2.facing == 1) {
			// this.frontCameraid = v1;
			// }
			// }
			//
			// if (this.backCameraid != -1 && this.type == 0) {
			// this.openCamera(0, this.backCameraid);
			// return;
			// }

			openCamera(1, this.frontCameraid);
		}

	}

	private int getCameraDisplayOrientation(int arg7, Camera arg8) {
		Camera.CameraInfo v1 = new Camera.CameraInfo();
		Camera.getCameraInfo(arg7, v1);
		int v0 = 0;
		switch (this.getWindowManager().getDefaultDisplay().getRotation()) {
		case 0: {
			v0 = 0;
			break;
		}
		case 1: {
			v0 = 90;
			break;
		}
		case 2: {
			v0 = 180;
			break;
		}
		case 3: {
			v0 = 270;
			break;
		}
		}
		int v2 = v1.facing == 1 ? (360 - (v1.orientation + v0) % 360) % 360 : (v1.orientation - v0 + 360) % 360;
		return v2;
	}
}
