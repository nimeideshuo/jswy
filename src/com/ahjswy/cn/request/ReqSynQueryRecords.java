package com.ahjswy.cn.request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReqSynQueryRecords
{

  @JsonProperty("pageindex")
  private int pageindex;

  @JsonProperty("pagesize")
  private int pagesize;

  @JsonProperty("rversion")
  private long rversion;

  public ReqSynQueryRecords()
  {
  }

  public ReqSynQueryRecords(int paramInt1, int paramInt2, long paramLong)
  {
    this.pageindex = paramInt1;
    this.pagesize = paramInt2;
    this.rversion = paramLong;
  }

  @JsonIgnore
  public int getPageIndex()
  {
    return this.pageindex;
  }

  @JsonIgnore
  public int getPageSize()
  {
    return this.pagesize;
  }

  @JsonIgnore
  public long getRVersion()
  {
    return this.rversion;
  }

  @JsonIgnore
  public void setPageIndex(int paramInt)
  {
    this.pageindex = paramInt;
  }

  @JsonIgnore
  public void setPageSize(int paramInt)
  {
    this.pagesize = paramInt;
  }

  @JsonIgnore
  public void setRVersion(long paramLong)
  {
    this.rversion = paramLong;
  }
}