package com.ahjswy.cn.response;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class RespGoodsPriceEntity
{

  @JsonProperty("batch")
  private String batch;

  @JsonProperty("goodsid")
  private String goodsid;

  @JsonProperty("price")
  private double price;

  @JsonProperty("pricesystemid")
  private String pricesystemid;

  @JsonProperty("pricesystemname")
  private String pricesystemname;

  @JsonProperty("unitid")
  private String unitid;

  @JsonProperty("unitname")
  private String unitname;

  public String getBatch()
  {
    return this.batch;
  }

  @JsonIgnore
  public String getGoodsid()
  {
    return this.goodsid;
  }

  @JsonIgnore
  public double getPrice()
  {
    return this.price;
  }

  @JsonIgnore
  public String getPricesystemid()
  {
    return this.pricesystemid;
  }

  @JsonIgnore
  public String getPricesystemname()
  {
    return this.pricesystemname;
  }

  @JsonIgnore
  public String getUnitid()
  {
    return this.unitid;
  }

  @JsonIgnore
  public String getUnitname()
  {
    return this.unitname;
  }

  public void setBatch(String paramString)
  {
    this.batch = paramString;
  }

  @JsonIgnore
  public void setGoodsid(String paramString)
  {
    this.goodsid = paramString;
  }

  @JsonIgnore
  public void setPrice(double paramDouble)
  {
    this.price = paramDouble;
  }

  @JsonIgnore
  public void setPricesystemid(String paramString)
  {
    this.pricesystemid = paramString;
  }

  @JsonIgnore
  public void setPricesystemname(String paramString)
  {
    this.pricesystemname = paramString;
  }

  @JsonIgnore
  public void setUnitid(String paramString)
  {
    this.unitid = paramString;
  }

  @JsonIgnore
  public void setUnitname(String paramString)
  {
    this.unitname = paramString;
  }
}