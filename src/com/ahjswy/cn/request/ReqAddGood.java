package com.ahjswy.cn.request;

/**
 * 添加商品
 * 
 * @author Administrator
 *
 */
public class ReqAddGood {

	public String name;// 商品名称
	public String pinyin;// 拼音编码
	public String barcode;// 型号
	public String specification;// 规格
	public String model;// 型号
	public boolean saleprice;// 允许折扣
	public boolean batchprice;// 批次批次管理
	public String averageprice;// 平均单价
	public String remark;// 备注
	public String salecue;// 销售提示
	public String unitid;// 单位id
	public String goodsfirstclassid;// 所属一级类
	public String goodssecondclassid;// 所属二级类
	public String goodsthirdclassid;// 所属三级类
	public double extrasaleprice1;// 参考采购价
	public double extrasaleprice2;// 最近采购价
	public double extrasaleprice3;// 成本单价
	public double extrasaleprice4;// 最低售价
	public boolean isdiscount;// 是否允许打折

}
