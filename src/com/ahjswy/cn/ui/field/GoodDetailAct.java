package com.ahjswy.cn.ui.field;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.R;
import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.RequestHelper;
import com.ahjswy.cn.dao.GoodsDAO;
import com.ahjswy.cn.dao.GoodsImageDAO;
import com.ahjswy.cn.dao.GoodsUnitDAO;
import com.ahjswy.cn.dao.WarehouseDAO;
import com.ahjswy.cn.model.Goods;
import com.ahjswy.cn.model.GoodsImage;
import com.ahjswy.cn.model.GoodsUnit;
import com.ahjswy.cn.model.Warehouse;
import com.ahjswy.cn.response.RespGoodsPriceEntity;
import com.ahjswy.cn.response.RespGoodsWarehouse;
import com.ahjswy.cn.response.RespGoodsWarehouseStockEntity;
import com.ahjswy.cn.service.ServiceGoods;
import com.ahjswy.cn.service.ServiceSupport;
import com.ahjswy.cn.ui.BaseActivity;
import com.ahjswy.cn.ui.Swy_splash;
import com.ahjswy.cn.utils.BitmapUtils;
import com.ahjswy.cn.utils.DocUtils;
import com.ahjswy.cn.utils.JSONUtil;
import com.ahjswy.cn.utils.PDH;
import com.ahjswy.cn.utils.PDH.ProgressCallBack;
import com.ahjswy.cn.utils.TextUtils;
import com.ahjswy.cn.views.Dialog_message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GoodDetailAct extends BaseActivity {
	private Goods goods;
	private ImageView imageView;
	private TextView tvBarcode;
	private TextView tvGoodsID;
	private TextView tvModel;
	// private TextView tvSaleCue;
	private TextView tvSpecificaion;
	private TextView tvStockNum;
	private ViewPager viewPager;
	private List<GoodsImage> goodsImages;
	private BitmapUtils bitmapUtils;
	private MyPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_stockiteminfo_details);
		this.goods = new GoodsDAO().getGoods(getIntent().getStringExtra("goodsid"));
		this.bitmapUtils = new BitmapUtils();
		initView();
		initData();
	}

	private void initView() {
		this.imageView = (ImageView) findViewById(R.id.imageView);
		this.tvGoodsID = (TextView) findViewById(R.id.tvGoodsID);
		this.tvBarcode = (TextView) findViewById(R.id.tvBarcode);
		this.tvSpecificaion = (TextView) findViewById(R.id.tvSpecification);
		this.tvModel = (TextView) findViewById(R.id.tvModel);
		this.tvStockNum = (TextView) findViewById(R.id.tvStockNum);
		dialogPrices = new Dialog_message(this);
		warehousedao = new WarehouseDAO();
		goodsWarehouse = new ArrayList<RespGoodsWarehouse>();
	}

	private void initData() {
		if (!"1".equals(new AccountPreference().getValue("ViewKCStockBrowse", "0"))) {
			this.tvStockNum.setVisibility(View.GONE);
		}
		this.tvGoodsID.setText("商品编号：" + this.goods.getId());
		this.tvBarcode.setText("商品条码：" + TextUtils.out(this.goods.getBarcode()));
		this.tvSpecificaion.setText("规         格：" + TextUtils.out(this.goods.getSpecification()));
		this.tvModel.setText("型         号：" + TextUtils.out(this.goods.getModel()));
		if (TextUtils.isEmpty(goods.getBigstocknumber())) {
			tvStockNum.setText("库存数量：无库存");
		} else {
			tvStockNum.setText("库存数量：" + goods.getBigstocknumber());
		}
		this.viewPager = ((ViewPager) findViewById(R.id.viewPager));
		LayoutParams params = viewPager.getLayoutParams();
		params.height = (Swy_splash.height / 2 - 20);
		this.imageView.setLayoutParams(params);
		goodsImages = new GoodsImageDAO().get(goods.getId());
		if (this.goodsImages == null || this.goodsImages.size() == 0) {
			this.imageView.setVisibility(0);
			ViewGroup.LayoutParams ivParams = this.imageView.getLayoutParams();
			ivParams.height = Swy_splash.height / 2 - 40;
			this.imageView.setLayoutParams(ivParams);
		} else {
			this.imageView.setVisibility(View.GONE);
			this.pagerAdapter = new MyPagerAdapter(this, this.goodsImages);
			this.viewPager.setAdapter(this.pagerAdapter);
		}
		boolean isShowPrice = getIntent().getBooleanExtra("showPrice", false);
		if (isShowPrice) {
			PDH.show(this, new ProgressCallBack() {
				public void action() {
					handlerPrice.sendMessage(
							handlerPrice.obtainMessage(1, new ServiceSupport().sup_QueryGoodsPrice(goods.getId())));

				}
			});
		}
		PDH.show(this, new ProgressCallBack() {

			@Override
			public void action() {
				// 查找 比对 需要可用 仓库 查找库存
				List<Warehouse> allWarehouses = warehousedao.getAllWarehouses();
				String stocknum = new ServiceGoods().gds_GetGoodsWarehouses(goods.getId(), goods.isIsusebatch());
				if (RequestHelper.isSuccess(stocknum)) {
					List<RespGoodsWarehouse> respGoodsWarehouses = JSONUtil.str2list(stocknum,
							RespGoodsWarehouse.class);
					for (int i = 0; i < allWarehouses.size(); i++) {
						for (int j = 0; j < respGoodsWarehouses.size(); j++) {
							if (allWarehouses.get(i).getId().equals(respGoodsWarehouses.get(j).getWarehouseid())) {
								goodsWarehouse.add(respGoodsWarehouses.get(j));
								break;
							}
						}
					}
					handlerStock.sendMessage(handlerStock.obtainMessage(2));
				} else {
					showError("查询库存失败!");
				}
			}

		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if ("1".equals(new AccountPreference().getValue(AccountPreference.ViewChangeprice, "0"))) {
			menu.add(0, 0, 0, "价格").setShowAsAction(2);
		}
		// if ("1".equals(new
		// AccountPreference().getValue(AccountPreference.ViewKCStockBrowse,
		// "0"))) {
		// menu.add(0, 1, 0, "分库").setShowAsAction(2);
		// }
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		if (menu.getItemId() == 0) {
			PDH.show(this, new ProgressCallBack() {
				public void action() {
					handlerPrice.sendMessage(
							handlerPrice.obtainMessage(1, new ServiceSupport().sup_QueryGoodsPrice(goods.getId())));
				}
			});
		}

		// else if (menu.getItemId() == 1) {
		// PDH.show(((Activity) this), new ProgressCallBack() {
		//
		// public void action() {
		// handlerStock.sendMessage(handlerStock.obtainMessage(1,
		// new ServiceSupport().sup_QueryGoodsWarehouseStock(goods.getId())));
		// }
		// });
		// }

		return super.onOptionsItemSelected(menu);
	}

	private Handler handlerPrice = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String message = (String) msg.obj;
			if (dialogPrices.isShowing()) {
				dialogPrices.dismiss();
			}
			if (RequestHelper.isSuccess(message)) {
				List<RespGoodsPriceEntity> listGoodPrice = JSONUtil.str2list(message, RespGoodsPriceEntity.class);
				if (listGoodPrice.size() > 0) {
					String dialogMessage = "\n";
					for (int i = 0; i < listGoodPrice.size(); ++i) {
						RespGoodsPriceEntity rp = listGoodPrice.get(i);
						dialogMessage = dialogMessage + rp.getPricesystemname() + "：" + rp.getPrice() + "元/"
								+ rp.getUnitname() + "\n";
					}
					// TextView tv = new TextView(GoodDetailAct.this);
					// tv.setTextSize(16f);
					// tv.setPadding(50, 0, 0, 0);
					// dialogPrice
					// AlertDialog.Builder dialogPrice = new
					// AlertDialog.Builder(GoodDetailAct.this);
					// dialogPrice.setView(tv);
					// dialogPrice.setTitle(goods.getName());
					// tv.setText(dialogMessage);
					// AlertDialog create = dialogPrice.create();
					// create.show();
					dialogPrices.setRootPadding(10, 50, 10, 0);
					dialogPrices.setVisibilityBottom(View.GONE);
					dialogPrices.setGravity(Gravity.TOP);
					dialogPrices.show();
					dialogPrices.setTitle(goods.getName());
					dialogPrices.setMessage(dialogMessage);
				} else {
					PDH.showMessage("无价格信息");
				}
			} else {
				PDH.showFail(message);
			}
		};
	};
	Handler handlerStock = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				String message = (String) msg.obj;
				if (RequestHelper.isSuccess(message)) {
					List<RespGoodsWarehouseStockEntity> listStock = JSONUtil.str2list(message,
							RespGoodsWarehouseStockEntity.class);
					if (!listStock.isEmpty()) {
						String dialogMessage = "\n";
						String stockNum;
						for (int i = 0; i < listStock.size(); ++i) {
							RespGoodsWarehouseStockEntity resStock = listStock.get(i);
							dialogMessage = dialogMessage + resStock.getWarehousename() + "：";
							if (TextUtils.isEmpty(resStock.getStocknum())) {
								stockNum = "无库存";
							} else {
								stockNum = resStock.getStocknum();
							}
							dialogMessage = dialogMessage + stockNum + "\n";
						}
						TextView tv = new TextView(GoodDetailAct.this);
						tv.setTextSize(16f);
						tv.setPadding(50, 0, 0, 0);
						AlertDialog.Builder dialog = new AlertDialog.Builder(GoodDetailAct.this);
						dialog.setView(((View) tv));
						dialog.setTitle(GoodDetailAct.this.goods.getName());
						tv.setText(dialogMessage);
						dialog.create().show();
					} else {
						PDH.showMessage("无库存信息");
					}
				} else {
					PDH.showFail(message);
				}
			}
			if (msg.what == 2) {
				if (goodsWarehouse.isEmpty()) {
					return;
				}
				GoodsUnit bigUnit = new GoodsUnitDAO().queryBigUnit(goodsWarehouse.get(0).getGoodsid());
				StringBuilder sb = new StringBuilder();
				int stocknum = 0;
				for (RespGoodsWarehouse warehouse : goodsWarehouse) {
					stocknum += warehouse.getStocknum();
					sb.append(warehouse.getWarehousename() + respSpaceStr(warehouse.getWarehousename()) + ":  "
							+ DocUtils.Stocknum((int) warehouse.getStocknum(), bigUnit)).append("\n\n");
				}
				sb.append("库存数量 :  " + DocUtils.Stocknum(stocknum, bigUnit));
				tvStockNum.setText(sb.toString());
			}
		};
	};

	// 计算需要添加的空格
	private String respSpaceStr(String str) {
		String spacestr = "";
		if (str.length() < 5) {
			for (int i = 0; i < 5 - str.length(); i++) {
				spacestr += "   ";
			}
		}
		return spacestr;
	}

	private Dialog_message dialogPrices;
	private WarehouseDAO warehousedao;
	private List<RespGoodsWarehouse> goodsWarehouse;

	class MyPagerAdapter extends PagerAdapter {
		public Context context;
		public List<GoodsImage> jobImageInfos;

		public MyPagerAdapter(Context context, List<GoodsImage> jobImageInfos) {
			super();
			this.context = context;
			this.jobImageInfos = jobImageInfos;
		}

		private void setViewData(View view, List<GoodsImage> paramList, int p_index) {
			if (p_index < 0) {
				finish();
			}
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			String v1 = String.valueOf(bitmapUtils.getPicDir()) + "/" + paramList.get(p_index).getImagePath();
			viewHolder.bmp = BitmapFactory.decodeFile(v1);
			if (viewHolder.bmp == null) {
				viewHolder.bmp = BitmapFactory.decodeResource(getResources(), R.drawable.swylogoimage);
			}
			viewHolder.imageView.setImageBitmap(viewHolder.bmp);
			viewHolder.tvPageNo.setText(String.valueOf(p_index + 1) + "/" + this.getCount());
		}

		private View constructView() {
			View localView = getLayoutInflater().inflate(R.layout.item_field_picture, null);
			ViewHolder localViewHolder = new ViewHolder();
			localViewHolder.tvPageNo = ((TextView) localView.findViewById(R.id.tvPageNo));
			localViewHolder.imageView = ((ImageView) localView.findViewById(R.id.album_imgview));
			localView.setTag(localViewHolder);
			return localView;
		}

		@Override
		public int getCount() {
			return this.jobImageInfos.size();
		}

		public int getItemPosition(Object object) {
			return -2;
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			View view = this.constructView();
			setViewData(view, this.jobImageInfos, position);
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	class ViewHolder {
		public Bitmap bmp;
		public ImageView imageView;
		public TextView tvPageNo;

		public void clear() {
			if (this.bmp != null && !this.bmp.isRecycled()) {
				this.bmp.recycle();
				this.bmp = null;
				System.gc();
			}
		}
	}

	public void setActionBarText() {
		this.getActionBar().setTitle(this.goods.getName());
	}

}
