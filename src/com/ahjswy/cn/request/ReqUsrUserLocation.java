package com.ahjswy.cn.request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReqUsrUserLocation
{

  @JsonProperty("address")
  private String address;

  @JsonProperty("id")
  private String id;

  @JsonProperty("latitude")
  private double latitude;

  @JsonProperty("longitude")
  private double longitude;

  @JsonProperty("name")
  private String name;

  public ReqUsrUserLocation()
  {
  }

  public ReqUsrUserLocation(String paramString1, String paramString2, double paramDouble1, double paramDouble2, String paramString3)
  {
    this.id = paramString1;
    this.name = paramString2;
    this.longitude = paramDouble1;
    this.latitude = paramDouble2;
    this.address = paramString3;
  }

  @JsonIgnore
  public String getAddress()
  {
    return this.address;
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
  public void setAddress(String paramString)
  {
    this.address = paramString;
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
}