package com.bumptech.glide.load.model;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LazyHeaders implements Headers {
  private volatile Map<String, String> combinedHeaders;
  
  private final Map<String, List<LazyHeaderFactory>> headers;
  
  LazyHeaders(Map<String, List<LazyHeaderFactory>> paramMap) {
    this.headers = Collections.unmodifiableMap(paramMap);
  }
  
  private String buildHeaderValue(List<LazyHeaderFactory> paramList) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      String str = ((LazyHeaderFactory)paramList.get(b)).buildHeader();
      if (!TextUtils.isEmpty(str)) {
        stringBuilder.append(str);
        if (b != paramList.size() - 1)
          stringBuilder.append(','); 
      } 
    } 
    return stringBuilder.toString();
  }
  
  private Map<String, String> generateHeaders() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
      String str = buildHeaderValue((List<LazyHeaderFactory>)entry.getValue());
      if (!TextUtils.isEmpty(str))
        hashMap.put(entry.getKey(), str); 
    } 
    return (Map)hashMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof LazyHeaders) {
      paramObject = paramObject;
      return this.headers.equals(((LazyHeaders)paramObject).headers);
    } 
    return false;
  }
  
  public Map<String, String> getHeaders() {
    // Byte code:
    //   0: aload_0
    //   1: getfield combinedHeaders : Ljava/util/Map;
    //   4: ifnonnull -> 37
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield combinedHeaders : Ljava/util/Map;
    //   13: ifnonnull -> 27
    //   16: aload_0
    //   17: aload_0
    //   18: invokespecial generateHeaders : ()Ljava/util/Map;
    //   21: invokestatic unmodifiableMap : (Ljava/util/Map;)Ljava/util/Map;
    //   24: putfield combinedHeaders : Ljava/util/Map;
    //   27: aload_0
    //   28: monitorexit
    //   29: goto -> 37
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    //   37: aload_0
    //   38: getfield combinedHeaders : Ljava/util/Map;
    //   41: areturn
    // Exception table:
    //   from	to	target	type
    //   9	27	32	finally
    //   27	29	32	finally
    //   33	35	32	finally
  }
  
  public int hashCode() {
    return this.headers.hashCode();
  }
  
  public String toString() {
    return "LazyHeaders{headers=" + this.headers + '}';
  }
  
  public static final class Builder {
    private static final Map<String, List<LazyHeaderFactory>> DEFAULT_HEADERS;
    
    private static final String DEFAULT_USER_AGENT;
    
    private static final String USER_AGENT_HEADER = "User-Agent";
    
    private boolean copyOnModify = true;
    
    private Map<String, List<LazyHeaderFactory>> headers = DEFAULT_HEADERS;
    
    private boolean isUserAgentDefault = true;
    
    static {
      String str = getSanitizedUserAgent();
      DEFAULT_USER_AGENT = str;
      HashMap<Object, Object> hashMap = new HashMap<>(2);
      if (!TextUtils.isEmpty(str))
        hashMap.put("User-Agent", Collections.singletonList(new LazyHeaders.StringHeaderFactory(str))); 
      DEFAULT_HEADERS = Collections.unmodifiableMap(hashMap);
    }
    
    private Map<String, List<LazyHeaderFactory>> copyHeaders() {
      HashMap<Object, Object> hashMap = new HashMap<>(this.headers.size());
      for (Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
        ArrayList arrayList = new ArrayList((Collection)entry.getValue());
        hashMap.put(entry.getKey(), arrayList);
      } 
      return (Map)hashMap;
    }
    
    private void copyIfNecessary() {
      if (this.copyOnModify) {
        this.copyOnModify = false;
        this.headers = copyHeaders();
      } 
    }
    
    private List<LazyHeaderFactory> getFactories(String param1String) {
      List<LazyHeaderFactory> list2 = this.headers.get(param1String);
      List<LazyHeaderFactory> list1 = list2;
      if (list2 == null) {
        list1 = new ArrayList();
        this.headers.put(param1String, list1);
      } 
      return list1;
    }
    
    static String getSanitizedUserAgent() {
      String str = System.getProperty("http.agent");
      if (TextUtils.isEmpty(str))
        return str; 
      int i = str.length();
      StringBuilder stringBuilder = new StringBuilder(str.length());
      for (byte b = 0; b < i; b++) {
        char c = str.charAt(b);
        if ((c > '\037' || c == '\t') && c < '') {
          stringBuilder.append(c);
        } else {
          stringBuilder.append('?');
        } 
      } 
      return stringBuilder.toString();
    }
    
    public Builder addHeader(String param1String, LazyHeaderFactory param1LazyHeaderFactory) {
      if (this.isUserAgentDefault && "User-Agent".equalsIgnoreCase(param1String))
        return setHeader(param1String, param1LazyHeaderFactory); 
      copyIfNecessary();
      getFactories(param1String).add(param1LazyHeaderFactory);
      return this;
    }
    
    public Builder addHeader(String param1String1, String param1String2) {
      return addHeader(param1String1, new LazyHeaders.StringHeaderFactory(param1String2));
    }
    
    public LazyHeaders build() {
      this.copyOnModify = true;
      return new LazyHeaders(this.headers);
    }
    
    public Builder setHeader(String param1String, LazyHeaderFactory param1LazyHeaderFactory) {
      copyIfNecessary();
      if (param1LazyHeaderFactory == null) {
        this.headers.remove(param1String);
      } else {
        List<LazyHeaderFactory> list = getFactories(param1String);
        list.clear();
        list.add(param1LazyHeaderFactory);
      } 
      if (this.isUserAgentDefault && "User-Agent".equalsIgnoreCase(param1String))
        this.isUserAgentDefault = false; 
      return this;
    }
    
    public Builder setHeader(String param1String1, String param1String2) {
      LazyHeaders.StringHeaderFactory stringHeaderFactory;
      if (param1String2 == null) {
        param1String2 = null;
      } else {
        stringHeaderFactory = new LazyHeaders.StringHeaderFactory(param1String2);
      } 
      return setHeader(param1String1, stringHeaderFactory);
    }
  }
  
  static final class StringHeaderFactory implements LazyHeaderFactory {
    private final String value;
    
    StringHeaderFactory(String param1String) {
      this.value = param1String;
    }
    
    public String buildHeader() {
      return this.value;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof StringHeaderFactory) {
        param1Object = param1Object;
        return this.value.equals(((StringHeaderFactory)param1Object).value);
      } 
      return false;
    }
    
    public int hashCode() {
      return this.value.hashCode();
    }
    
    public String toString() {
      return "StringHeaderFactory{value='" + this.value + '\'' + '}';
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\LazyHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */