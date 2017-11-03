package com.ahjswy.cn.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.MyApplication;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.dao.Sv_docitem;
import com.ahjswy.cn.model.DBUser;
import com.ahjswy.cn.model.DefDocXS;
import com.ahjswy.cn.model.Department;
import com.ahjswy.cn.model.DocContainerEntity;
import com.ahjswy.cn.popupmenu.MainMenuPopup;
import com.ahjswy.cn.request.ReqSynUpdateInfo;
import com.ahjswy.cn.service.ServiceStore;
import com.ahjswy.cn.service.ServiceSynchronize;
import com.ahjswy.cn.service.ServiceSystem;
import com.ahjswy.cn.ui.Main_set_bumen.BumenCall;
import com.ahjswy.cn.ui.addgoods.AddNewGoodSAct;
import com.ahjswy.cn.ui.field.AllGoodsActivity;
import com.ahjswy.cn.ui.field.NewCustomerAddAct;
import com.ahjswy.cn.ui.ingoods.InDocEditActivity;
import com.ahjswy.cn.ui.ingoods.InDocOpenActivity;
import com.ahjswy.cn.ui.inpurchase.InpurchaseOpenActivity;
import com.ahjswy.cn.ui.inventory.InventoryDocOpenActivity;
import com.ahjswy.cn.ui.inventory.InventoryRecordActivity;
import com.ahjswy.cn.ui.out_in_goods.OutInDocOpen;
import com.ahjswy.cn.ui.outgoods.OutDocEditActivity;
import com.ahjswy.cn.ui.outgoods.OutDocOpenActivity;
import com.ahjswy.cn.ui.outgoods.SaleRecordActivity;
import com.ahjswy.cn.ui.transfer.TransferDocOpenActivity;
import com.ahjswy.cn.ui.transfer.TransferRecordActivity;
import com.ahjswy.cn.utils.InfoDialog;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.PDH.ProgressCallBack;
import com.ahjswy.cn.utils.SwyUtils;
import com.ahjswy.cn.utils.UpdateUtils;
import com.ahjswy.cn.utils.Utils;
import com.ahjswy.cn.views.Dialog_message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;

public class SwyMain extends BaseActivity implements OnClickListener, BumenCall {
	AccountPreference ap;
	private Department department;
	private ProgressDialog progressDialog;
	private View root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_field_main);
		initView();
		initDate();
	}

	private void initView() {
		findViewById(R.id.ll_return_goods).setOnClickListener(this);
		// findViewById(R.id.outpurchase).setOnClickListener(this);
		findViewById(R.id.ll_sale).setOnClickListener(this);
		findViewById(R.id.lin_Inpurchase).setOnClickListener(this);
		findViewById(R.id.ll_my_doc).setOnClickListener(this);
		findViewById(R.id.ll_newcustomer).setOnClickListener(this);
		findViewById(R.id.lin_outin).setOnClickListener(this);
		// findViewById(R.id.ll_addGoods).setOnClickListener(this);
		findViewById(R.id.ll_stockgoods).setOnClickListener(this);
		findViewById(R.id.ll_inventory).setOnClickListener(this);
		findViewById(R.id.ll_my_inventory).setOnClickListener(this);
		findViewById(R.id.addNewGoods).setOnClickListener(this);
		findViewById(R.id.ll_transfer).setOnClickListener(this);
		findViewById(R.id.ll_my_transfer).setOnClickListener(this);
		root = findViewById(R.id.root);
		root.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
				if ((menuPopup != null) && (menuPopup.isShowing())) {
					menuPopup.dismiss();
					WindowManager.LayoutParams localLayoutParams = SwyMain.this.getWindow().getAttributes();
					localLayoutParams.alpha = 1.0F;
					getWindow().setAttributes(localLayoutParams);
				}
				return false;
			}
		});
		ap = new AccountPreference();
	}

	// 测试代码
	// boolean isShow = false;
	// int numsss = 0;
	//
	// public class TestUnit {
	// public String goodsid;
	// public String unitid;
	// public boolean isbasic;
	// public boolean isshow;
	// public double ratio;
	// public String remark;
	// public String rversion;
	// }

	// TODO
	public void startOpenDoc(View v) {

		// List<UnitidPrice> price = new ArrayList<UnitidPrice>();
		// UnitidPrice price2 = new UnitidPrice();
		// price2.customerid = "00001";
		// price2.goodsid = "澳米伽-013";
		// price2.unitid = "04";
		// price.add(price2);
		// UnitidPrice price3 = new UnitidPrice();
		// price3.customerid = "00001";
		// price3.goodsid = "卤-025";
		// price3.unitid = "01";
		// price.add(price3);
		// UnitidPrice price4 = new UnitidPrice();
		// price4.customerid = "00001";
		// price4.goodsid = "晏子-016";
		// price4.unitid = "01";
		// price.add(price4);
		// List<UnitidPrice> priceLis =
		// DocUtils.getCustomerGoodsHistoryPrice(price);
		// if (priceLis != null) {
		// System.out.println("sssssssssssssss:" + priceLis.toString());
		// }

		// edNum = (EditText) findViewById(R.id.edNum);
		// edTime = (EditText) findViewById(R.id.edTime);
		// serviceStore = new ServiceStore();
		// PDH.show(this, "开单中....", new ProgressCallBack() {
		//
		// @Override
		// public void action() {
		// int num = Integer.parseInt(edNum.getText().toString());
		// for (int i = 0; i < num; i++) {
		// String localString = new ServiceStore().str_InitXSDoc("01", "01");
		// if (RequestHelper.isSuccess(localString)) {
		// openDoc(i, localString);
		// } else {
		// showError("没有获取到数据!失败!等待。。。" + localString);
		// SystemClock.sleep(5000);
		// String res = new ServiceStore().str_InitXSDoc("01", "01");
		// if (RequestHelper.isSuccess(localString)) {
		// openDoc(i, res);
		// } else {
		// showError(i + "没有获取到数据!失败!等待。。。" + res);
		// SystemClock.sleep(5000);
		// }
		//
		// }
		// }
		//
		// }
		// });

	}

	// protected void openDoc(int position, String localString) {
	// DocContainerEntity localDocContainerEntity = (DocContainerEntity)
	// JSONUtil.fromJson(localString,
	// DocContainerEntity.class);
	// doc = ((DefDocXS) JSONUtil.fromJson(localDocContainerEntity.getDoc(),
	// DefDocXS.class));
	// List<DefDocPayType> listPayType =
	// JSONUtil.str2list(localDocContainerEntity.getPaytype(),
	// DefDocPayType.class);
	// List<DefDocItemXS> listItem =
	// JSONUtil.str2list(localDocContainerEntity.getItem(), DefDocItemXS.class);
	// List<Long> listItemDelete = new ArrayList<>();
	// doc.setPromotionid(null);
	// doc.setDistributionid(null);
	// doc.setCustomerid("安康001");
	// // doc.setCustomername("东至侯结才");
	// String resFor = serviceStore.str_SaveXSDoc(doc, listItem, listPayType,
	// listItemDelete);
	// if (!RequestHelper.isSuccess(resFor)) {
	// showError("第 " + position + " 次请求错误！" + resFor);
	// SystemClock.sleep(5000);
	// return;
	// }
	// long long1 = Long.parseLong(edTime.getText().toString());
	// SystemClock.sleep(long1);
	// }

	private void initDate() {
		sv = new Sv_docitem();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(1);
		progressDialog.setCancelable(false);
		department = SystemState.getDepartment();
		if (department == null) {
			fostStartHandler.sendEmptyMessage(0);
		} else {

			String updateTime = Utils.formatDate(ap.getValue("basic_data_updatitme", "1990-01-01 00:00:00"),
					"yyyy-MM-dd");
			if (!Utils.formatDate(new Date().getTime(), "yyyy-MM-dd").equals(updateTime)) {

				PDH.show(this, "正在检查更新...", new PDH.ProgressCallBack() {
					public void action() {
						getMaxRVersion();
					}
				});
			}
		}

	}

	public void updataImg(View v) {
		// openViceCamera();
		// return mCamera;
		// startActivity(new Intent(this, CameraActivity.class));
		// BitmapUtils bitmp = new BitmapUtils();
		// String sd =
		// Environment.getExternalStorageDirectory().getAbsolutePath();
		// sd = sd + File.separator + "1.png";
		// if (!new File(sd).exists()) {
		// showSuccess("没找到图片");
		// return;
		// }
		// Bitmap decodeFile = BitmapFactory.decodeFile(sd);
		// ivSd.setImageBitmap(decodeFile);
		//
		// String imagefile = bitmp.bitmaptoString(decodeFile);
		// // Bitmap stringtoBitmap = bitmp.stringtoBitmap(bitmaptoString);
		// // bitmp.savePicture(stringtoBitmap, "3.jpg");
		// ReqVstAddVisitCustomerJobImage image = new
		// ReqVstAddVisitCustomerJobImage();
		// image.setImagefile(imagefile);
		// image.setImagepath("/000030001.jpg");
		// image.setVisitjobid(7);
		// String image2 = new ServiceVisit().vst_UploadVisitImage(image);
		// if (RequestHelper.isSuccess(image2)) {
		// showSuccess("上传成功!");
		// MLog.d(">>>" + imagefile);
		// } else {
		// showSuccess("上传失败!" + image2);
		// }
		// ======================
		// 查询商品历史均价
		// String url = Utils.getServiceAddress("", "");
		// LinkedHashMap<String, String> map = new LinkedHashMap<String,
		// String>();
		// ReqGoodDataPrice req = new ReqGoodDataPrice();
		// req.startDataTime = "2017-9-1";
		// req.endDataTime = "2017-9-15";
		// req.goodsId = "00001";
		// String object2Json = JSONUtil.object2Json(req);
		// map.put("parameter", object2Json);
		// new Utils_help().getServiceInfor(url, map);

	}

	// Camera camera;

	/**
	 * 打开摄像头
	 * 
	 * @return
	 */
	// private boolean openViceCamera() {
	// SurfaceView sf = (SurfaceView) findViewById(R.id.sf);
	// SurfaceHolder holder = sf.getHolder();
	// if (camera != null) {
	// closeCamera();
	// }
	// try {
	// Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	// int cameraCount = Camera.getNumberOfCameras();
	// camera = Camera.open(1);
	// camera.lock();
	// camera.setPreviewDisplay(holder);
	// camera.startPreview();
	// camera.unlock();
	// holder.addCallback(this);
	// return true;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// closeCamera();
	// return false;
	// }
	// }

	// private void closeCamera() {
	// camera.stopPreview();
	// camera.release();
	// camera = null;
	// }

	Handler fostStartHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 部门选择 dialog
				Main_set_bumen dia_bume = new Main_set_bumen(SwyMain.this, R.style.MyDialog_NoTitle, SwyMain.this);
				dia_bume.showMe();
				break;
			case 1:
				PDH.show(SwyMain.this, "正在检查更新...", new ProgressCallBack() {
					@Override
					public void action() {
						// 获取 存取的时间 和上次不一样 更新
						String updateTime = Utils.formatDate(ap.getValue("basic_data_updatitme", "1990-01-01 00:00:00"),
								"yyyy-MM-dd");
						if (!Utils.formatDate(new Date().getTime(), "yyyy-MM-dd").equals(updateTime)) {
							getMaxRVersion();
						}
					}
				});
				break;
			case 5:
				PDH.showFail("获取数据失败！");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "单击显示菜单").setIcon(getResources().getDrawable(R.drawable.btn_submenu)).setShowAsAction(1);
		return super.onCreateOptionsMenu(menu);
	}

	private MainMenuPopup menuPopup;

	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case 0:
			if (this.menuPopup == null) {
				this.menuPopup = new MainMenuPopup(this);
			}
			this.menuPopup.showAtLocation(this.root, 80, 0, 0);
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 0.8F;
			getWindow().setAttributes(localLayoutParams);
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 1.0F;
			getWindow().setAttributes(localLayoutParams);
			return;
		}
		switch (v.getId()) {
		// 销售
		case R.id.ll_sale:
			if (SystemState.getWarehouse() == null) {
				PDH.showMessage("请设置默认仓库");
				return;
			}
			// TODO
			DocContainerEntity queryDoc = sv.queryDoc("13");
			if (queryDoc == null) {
				startActivity(new Intent(this, OutDocOpenActivity.class));
			} else {
				Intent intent = new Intent(this, OutDocEditActivity.class);
				intent.putExtra("docContainer", queryDoc);
				intent.putExtra("ishaschanged", true);
				startActivity(intent);
			}
			break;
		// 退货
		case R.id.ll_return_goods:
			if (SystemState.getWarehouse() == null) {
				PDH.showMessage("请设置默认仓库");
				return;
			}
			// TODO
			DocContainerEntity entity = sv.queryDoc("14");
			if (entity == null) {
				startActivity(new Intent(SwyMain.this, InDocOpenActivity.class));
			} else {
				Intent localIntent = new Intent();
				localIntent.setClass(this, InDocEditActivity.class);
				localIntent.putExtra("docContainer", entity);
				startActivity(localIntent);
			}
			break;
		// 采购单
		case R.id.lin_Inpurchase:
			if (SystemState.getWarehouse() == null) {
				PDH.showMessage("请设置默认仓库");
				return;
			}
			Intent in_Inpurchase = new Intent(SwyMain.this, InpurchaseOpenActivity.class);
			startActivity(in_Inpurchase);
			break;
		// 采购退货
		// case R.id.outpurchase:
		// if (SystemState.getWarehouse() == null) {
		// PDH.showMessage("请设置默认仓库");
		// return;
		// }
		// Intent outpurchase = new Intent(SwyMain.this,
		// OutpurchaseOpenActivity.class);
		// startActivity(outpurchase);
		// finish();
		// break;
		// 我的销售
		case R.id.ll_my_doc:
			startActivity(new Intent(SwyMain.this, SaleRecordActivity.class));
			break;
		// 新增客户
		case R.id.ll_newcustomer:
			startActivity(new Intent(SwyMain.this, NewCustomerAddAct.class));
			break;
		// 销售订单
		case R.id.lin_outin:
			startActivity(new Intent(SwyMain.this, OutInDocOpen.class));
			break;
		// 新增商品
		case R.id.addNewGoods:
			startActivity(new Intent(SwyMain.this, AddNewGoodSAct.class));
			break;
		// 盘点
		case R.id.ll_inventory:
			if (SystemState.getWarehouse() == null) {
				PDH.showMessage("请设置默认仓库");
				return;
			}
			startActivity(new Intent(SwyMain.this, InventoryDocOpenActivity.class));
			break;
		// 我的盘点
		case R.id.ll_my_inventory:
			startActivity(new Intent(SwyMain.this, InventoryRecordActivity.class));
			break;
		// 产品手册
		case R.id.ll_stockgoods:
			startActivity(new Intent(this, AllGoodsActivity.class));
			break;
		// 调拨
		case R.id.ll_transfer:
			if (SystemState.getWarehouse() == null) {
				PDH.showMessage("请设置默认仓库");
				return;
			}
			startActivity(new Intent(this, TransferDocOpenActivity.class));
			break;
		// 我的调拨
		case R.id.ll_my_transfer:
			startActivity(new Intent(this, TransferRecordActivity.class));
			break;
		}
	}

	// 数据更新
	private Handler handlerProgress = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case -1:
				progressDialog.setProgress(0);
				progressDialog.setMessage("数据同步中");
				progressDialog.setMax(Integer.parseInt(msg.obj.toString()));
				progressDialog.show();
				break;
			case -2:
				progressDialog.setProgress(0);
				progressDialog.setMessage("商品图片同步中");
				progressDialog.setMax(Integer.parseInt(msg.obj.toString()));
				progressDialog.show();
				break;
			case -3:
				progressDialog.cancel();
				break;
			}
			progressDialog.setProgress(msg.what);
		};

	};
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				PDH.showSuccess("同步成功");
				progressDialog.cancel();
				break;
			case 2:
				InfoDialog.showError(SwyMain.this, "同步失败，请重试");
				break;
			}
		};
	};
	private ServiceStore serviceStore;
	private Sv_docitem sv;
	private EditText edTime;
	private EditText edNum;
	private DefDocXS doc;

	/**
	 * * 监听Back键按下事件,方法2: * 注意: * 返回值表示:是否能完全处理该事件 * 在此处返回false,所以会继续传播该事件. *
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((this.menuPopup != null) && (this.menuPopup.isShowing())) {
			this.menuPopup.dismiss();
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.alpha = 1.0F;
			getWindow().setAttributes(localLayoutParams);
			return true;
		}
		if (keyCode == 4) {
			final Dialog_message dialog_message = new Dialog_message(this);
			dialog_message.show();
			dialog_message.setMessage("确定退出程序?");
			dialog_message.setCancelListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 退出软件
					dialog_message.dismiss();
					finish();
					MyApplication.getInstance().exit();
				}
			});
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 处理 SQL 数据
	public void getMaxRVersion() {
		long max_rversion = Long.parseLong(new AccountPreference().getValue("max_rversion", "0"));
		List<ReqSynUpdateInfo> listReqSyn = new ServiceSynchronize().syn_QueryUpdateInfo(max_rversion);
		if (listReqSyn != null) {
			long rversion = new SwyUtils().getPagesFromUpdateInfo(listReqSyn, "rversion");
			if (rversion >= 0) {
				ap.setValue("basic_data_updatitme", Utils.formatDate(new Date().getTime()));
				if (rversion != max_rversion) {
					if (new UpdateUtils().executeUpdate(this.handlerProgress, listReqSyn, max_rversion)) {
						ap.setValue("max_rversion", String.valueOf(rversion));
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(2);
					}
					handlerProgress.sendEmptyMessage(-3);
				}
			}

		}

	}

	// 选择部门，后回调
	@Override
	public void callBack() {
		fostStartHandler.sendEmptyMessage(1);
	}

	@Override
	public void setActionBarText() {
		getActionBar().setHomeButtonEnabled(false);
		setTitle("競商勿忧-光辉");
	}

}
