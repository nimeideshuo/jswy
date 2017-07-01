package com.ahjswy.cn.request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReqUsrUserLogin
{

  @JsonProperty("key")
  private String key;

  @JsonProperty("password")
  private String password;

  @JsonProperty("userid")
  private String userid;

  public ReqUsrUserLogin()
  {
  }

  public ReqUsrUserLogin(String paramString1, String paramString2, String paramString3)
  {
    this.userid = paramString1;
    this.password = paramString2;
    this.key = paramString3;
  }

  @JsonIgnore
  public String getKey()
  {
    return this.key;
  }

  @JsonIgnore
  public String getPassword()
  {
    return this.password;
  }

  @JsonIgnore
  public String getUserid()
  {
    return this.userid;
  }

  @JsonIgnore
  public void setKey(String paramString)
  {
    this.key = paramString;
  }

  @JsonIgnore
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }

  @JsonIgnore
  public void setUserid(String paramString)
  {
    this.userid = paramString;
  }
}