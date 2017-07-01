package com.ahjswy.cn.model;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Customer
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty("address")
  private String address;

  @JsonProperty("bankingaccount")
  private String bankingaccount;

  @JsonProperty("contact")
  private String contact;

  @JsonProperty("contactmoblie")
  private String contactmoblie;

  @JsonProperty("customertypeid")
  private String customertypeid;

  @JsonProperty("depositbank")
  private String depositbank;

  @JsonProperty("id")
  private String id;

  @JsonProperty("latitude")
  private double latitude;

  @JsonProperty("longitude")
  private double longitude;

  @JsonProperty("name")
  private String name;

  @JsonProperty("pinyin")
  private String pinyin;

  @JsonProperty("pricesystemid")
  private String pricesystemid;

  @JsonProperty("promotionid")
  private String promotionid;

  @JsonProperty("regionid")
  private String regionid;

  @JsonProperty("remark")
  private String remark;

  @JsonProperty("telephone")
  private String telephone;

  @JsonProperty("visitlineid")
  private String visitlineid;

  @JsonIgnore
  public String getAddress()
  {
    return this.address;
  }

  @JsonIgnore
  public String getBankingAccount()
  {
    return this.bankingaccount;
  }

  @JsonIgnore
  public String getContact()
  {
    return this.contact;
  }

  @JsonIgnore
  public String getContactMoblie()
  {
    return this.contactmoblie;
  }

  @JsonIgnore
  public String getCustomerTypeId()
  {
    return this.customertypeid;
  }

  @JsonIgnore
  public String getDepositBank()
  {
    return this.depositbank;
  }

  @JsonIgnore
  public String getId()
  {
    return this.id;
  }

  @JsonIgnore
  public double getLatitude()
  {
    return this.latitude;
  }

  @JsonIgnore
  public double getLongitude()
  {
    return this.longitude;
  }

  @JsonIgnore
  public String getName()
  {
    return this.name;
  }

  @JsonIgnore
  public String getPinyin()
  {
    return this.pinyin;
  }

  @JsonIgnore
  public String getPriceSystemId()
  {
    return this.pricesystemid;
  }

  @JsonIgnore
  public String getPromotionId()
  {
    return this.promotionid;
  }

  @JsonIgnore
  public String getRegionId()
  {
    return this.regionid;
  }

  @JsonIgnore
  public String getRemark()
  {
    return this.remark;
  }

  @JsonIgnore
  public String getTelephone()
  {
    return this.telephone;
  }

  @JsonIgnore
  public String getVisitLineId()
  {
    return this.visitlineid;
  }

  @JsonIgnore
  public void setAddress(String paramString)
  {
    this.address = paramString;
  }

  @JsonIgnore
  public void setBankingAccount(String paramString)
  {
    this.bankingaccount = paramString;
  }

  @JsonIgnore
  public void setContact(String paramString)
  {
    this.contact = paramString;
  }

  @JsonIgnore
  public void setContactMoblie(String paramString)
  {
    this.contactmoblie = paramString;
  }

  @JsonIgnore
  public void setCustomerTypeId(String paramString)
  {
    this.customertypeid = paramString;
  }

  @JsonIgnore
  public void setDepositBank(String paramString)
  {
    this.depositbank = paramString;
  }

  @JsonIgnore
  public void setId(String paramString)
  {
    this.id = paramString;
  }

  @JsonIgnore
  public void setLatitude(double paramDouble)
  {
    this.latitude = paramDouble;
  }

  @JsonIgnore
  public void setLongitude(double paramDouble)
  {
    this.longitude = paramDouble;
  }

  @JsonIgnore
  public void setName(String paramString)
  {
    this.name = paramString;
  }

  @JsonIgnore
  public void setPinyin(String paramString)
  {
    this.pinyin = paramString;
  }

  @JsonIgnore
  public void setPriceSystemId(String paramString)
  {
    this.pricesystemid = paramString;
  }

  @JsonIgnore
  public void setPromotionId(String paramString)
  {
    this.promotionid = paramString;
  }

  @JsonIgnore
  public void setRegionId(String paramString)
  {
    this.regionid = paramString;
  }

  @JsonIgnore
  public void setRemark(String paramString)
  {
    this.remark = paramString;
  }

  @JsonIgnore
  public void setTelephone(String paramString)
  {
    this.telephone = paramString;
  }

  @JsonIgnore
  public void setVisitLineId(String paramString)
  {
    this.visitlineid = paramString;
  }
}