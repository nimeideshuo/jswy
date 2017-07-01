package com.ahjswy.cn.model;

import java.io.Serializable;

public class Unit
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;

  public String getId()
  {
    return this.id;
  }

  public String getName()
  {
    return this.name;
  }

  public void setId(String paramString)
  {
    this.id = paramString;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }
}