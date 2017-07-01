package com.ahjswy.cn.app;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.ahjswy.cn.utils.JSONUtil;
public class Bean {
	private Map<String, Object> map;

	  public Bean()
	  {
	    this.map = new HashMap();
	  }

	  public Bean(Map<String, String> paramMap)
	  {
	    this.map.putAll(paramMap);
	  }

	  public Bean(String[] paramArrayOfString, Object[] paramArrayOfObject)
	  {
	    for (int i = 0; ; ++i)
	    {
	      if (i >= paramArrayOfString.length)
	        return;
	      put(paramArrayOfString[i], paramArrayOfObject[i]);
	    }
	  }

	  public void destory()
	  {
	    this.map = null;
	  }

	  public Object get(String paramString)
	  {
	    return this.map.get(paramString);
	  }

	  public String getJsonString()
	  {
	    return JSONUtil.makeJsonStr(this.map);
	  }

	  public String[] keys()
	  {
	    String[] arrayOfString = new String[this.map.size()];
	    this.map.keySet().toArray(arrayOfString);
	    return arrayOfString;
	  }

	  public String[][] makeArray()
	  {
	    String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { 2, this.map.size() });
	    this.map.keySet().toArray(arrayOfString[0]);
	    for (int i = 0; ; ++i)
	    {
	      if (i >= this.map.size())
	        return arrayOfString;
	      arrayOfString[1][i] = this.map.get(arrayOfString[0][i]).toString();
	    }
	  }

	  public List<NameValuePair> makeHttpParams()
	  {
	    ArrayList localArrayList = new ArrayList();
	    Iterator localIterator = this.map.keySet().iterator();
	    while (true)
	    {
	      if (!(localIterator.hasNext()))
	        return localArrayList;
	      String str = (String)localIterator.next();
	      localArrayList.add(new BasicNameValuePair(str, (String) this.map.get(str)));
	    }
	  }

	  public Bean put(String paramString, Object paramObject)
	  {
	    this.map.put(paramString, paramObject);
	    return this;
	  }

	  public void remove(String paramString)
	  {
	    this.map.remove(paramString);
	  }

	  public String toParamString()
	  {
	    StringBuilder localStringBuilder = new StringBuilder();
	    Iterator localIterator = this.map.keySet().iterator();
	    while (true)
	    {
	      if (!(localIterator.hasNext()))
	      {
	        if (localStringBuilder.length() > 0)
	          localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
	        return localStringBuilder.toString();
	      }
	      String str = (String)localIterator.next();
	      localStringBuilder.append(str + "=" + this.map.get(str) + "&");
	    }
	  }
}
