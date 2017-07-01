package com.ahjswy.cn.model;

import java.io.Serializable;

import com.ahjswy.cn.utils.TextUtils;

public class DefDocTransfer
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String builderid;
  private String buildername;
  private String buildtime;
  private String departmentid;
  private String departmentname;
  private long docid;
  private String doctypeid;
  private String doctypename;
  private String financialcfid;
  private String inwarehouseid;
  private String inwarehousename;
  private boolean isavailable;
  private boolean isposted;
  private String makerid;
  private String makername;
  private String maketime;
  private String outwarehouseid;
  private String outwarehousename;
  private int printnum;
  private String printtemplate;
  private String remark;
  private long rversion;
  private String showid;
  private String summary;

  public String getBuilderid()
  {
    return this.builderid;
  }

  public String getBuildername()
  {
    return this.buildername;
  }

  public String getBuildtime()
  {
    return this.buildtime;
  }

  public String getDepartmentid()
  {
    return this.departmentid;
  }

  public String getDepartmentname()
  {
    return this.departmentname;
  }

  public long getDocid()
  {
    return this.docid;
  }

  public String getDoctypeid()
  {
    return this.doctypeid;
  }

  public String getDoctypename()
  {
    return this.doctypename;
  }

  public String getFinancialcfid()
  {
    return this.financialcfid;
  }

  public String getInwarehouseid()
  {
    return this.inwarehouseid;
  }

  public String getInwarehousename()
  {
    return this.inwarehousename;
  }

  public String getMakerid()
  {
    return this.makerid;
  }

  public String getMakername()
  {
    return this.makername;
  }

  public String getMaketime()
  {
    return this.maketime;
  }

  public String getOutwarehouseid()
  {
    return this.outwarehouseid;
  }

  public String getOutwarehousename()
  {
    return this.outwarehousename;
  }

  public int getPrintnum()
  {
    return this.printnum;
  }

  public String getPrinttemplate()
  {
    return this.printtemplate;
  }

  public String getRemark()
  {
    return this.remark;
  }

  public long getRversion()
  {
    return this.rversion;
  }

  public String getShowid()
  {
    return this.showid;
  }

  public String getSummary()
  {
    return this.summary;
  }

  public boolean isIsavailable()
  {
    return this.isavailable;
  }

  public boolean isIsposted()
  {
    return this.isposted;
  }

  public void setBuilderid(String paramString)
  {
    if (!paramString.isEmpty())
    {
      this.builderid = paramString;
      return;
    }
    this.builderid = null;
  }

  public void setBuildername(String paramString)
  {
    this.buildername = paramString;
  }

  public void setBuildtime(String paramString)
  {
    this.buildtime = paramString;
  }

  public void setDepartmentid(String paramString)
  {
    if (!paramString.isEmpty())
    {
      this.departmentid = paramString;
      return;
    }
    this.departmentid = null;
  }

  public void setDepartmentname(String paramString)
  {
    this.departmentname = paramString;
  }

  public void setDocid(long paramLong)
  {
    this.docid = paramLong;
  }

  public void setDoctypeid(String paramString)
  {
    this.doctypeid = paramString;
  }

  public void setDoctypename(String paramString)
  {
    this.doctypename = paramString;
  }

  public void setFinancialcfid(String paramString)
  {
    this.financialcfid = paramString;
  }

  public void setInwarehouseid(String paramString)
  {
    if (!paramString.isEmpty())
    {
      this.inwarehouseid = paramString;
      return;
    }
    this.inwarehouseid = null;
  }

  public void setInwarehousename(String paramString)
  {
    this.inwarehousename = paramString;
  }

  public void setIsavailable(boolean paramBoolean)
  {
    this.isavailable = paramBoolean;
  }

  public void setIsposted(boolean paramBoolean)
  {
    this.isposted = paramBoolean;
  }

  public void setMakerid(String paramString)
  {
    if (!paramString.isEmpty())
    {
      this.makerid = paramString;
      return;
    }
    this.makerid = null;
  }

  public void setMakername(String paramString)
  {
    this.makername = paramString;
  }

  public void setMaketime(String paramString)
  {
    this.maketime = paramString;
  }

  public void setOutwarehouseid(String paramString)
  {
    if (!paramString.isEmpty())
    {
      this.outwarehouseid = paramString;
      return;
    }
    this.outwarehouseid = null;
  }

  public void setOutwarehousename(String paramString)
  {
    this.outwarehousename = paramString;
  }

  public void setPrintnum(int paramInt)
  {
    this.printnum = paramInt;
  }

  public void setPrinttemplate(String paramString)
  {
    this.printtemplate = paramString;
  }

  public void setRemark(String paramString)
  {
    this.remark = paramString;
  }

  public void setRversion(long paramLong)
  {
    this.rversion = paramLong;
  }

  public void setShowid(String paramString)
  {
    this.showid = paramString;
  }

  public void setSummary(String paramString)
  {
    this.summary = paramString;
  }
}