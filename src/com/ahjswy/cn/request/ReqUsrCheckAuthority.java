package com.ahjswy.cn.request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReqUsrCheckAuthority
{

  @JsonProperty("authority")
  private String authority;

  @JsonProperty("userid")
  private String userid;

  public ReqUsrCheckAuthority()
  {
  }

  public ReqUsrCheckAuthority(String paramString1, String paramString2)
  {
    this.userid = paramString1;
    this.authority = paramString2;
  }

  @JsonIgnore
  public String getAuthority()
  {
    return this.authority;
  }

  @JsonIgnore
  public String getUserid()
  {
    return this.userid;
  }

  @JsonIgnore
  public void setAuthority(String paramString)
  {
    this.authority = paramString;
  }

  @JsonIgnore
  public void setUserid(String paramString)
  {
    this.userid = paramString;
  }
}