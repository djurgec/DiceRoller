package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

public class GlideUrl implements Key {
  private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%;$";
  
  private volatile byte[] cacheKeyBytes;
  
  private int hashCode;
  
  private final Headers headers;
  
  private String safeStringUrl;
  
  private URL safeUrl;
  
  private final String stringUrl;
  
  private final URL url = null;
  
  public GlideUrl(String paramString) {
    this(paramString, Headers.DEFAULT);
  }
  
  public GlideUrl(String paramString, Headers paramHeaders) {
    this.stringUrl = Preconditions.checkNotEmpty(paramString);
    this.headers = (Headers)Preconditions.checkNotNull(paramHeaders);
  }
  
  public GlideUrl(URL paramURL) {
    this(paramURL, Headers.DEFAULT);
  }
  
  public GlideUrl(URL paramURL, Headers paramHeaders) {
    this.stringUrl = null;
    this.headers = (Headers)Preconditions.checkNotNull(paramHeaders);
  }
  
  private byte[] getCacheKeyBytes() {
    if (this.cacheKeyBytes == null)
      this.cacheKeyBytes = getCacheKey().getBytes(CHARSET); 
    return this.cacheKeyBytes;
  }
  
  private String getSafeStringUrl() {
    if (TextUtils.isEmpty(this.safeStringUrl)) {
      String str2 = this.stringUrl;
      String str1 = str2;
      if (TextUtils.isEmpty(str2))
        str1 = ((URL)Preconditions.checkNotNull(this.url)).toString(); 
      this.safeStringUrl = Uri.encode(str1, "@#&=*+-_.,:!?()/~'%;$");
    } 
    return this.safeStringUrl;
  }
  
  private URL getSafeUrl() throws MalformedURLException {
    if (this.safeUrl == null)
      this.safeUrl = new URL(getSafeStringUrl()); 
    return this.safeUrl;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof GlideUrl;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (getCacheKey().equals(paramObject.getCacheKey())) {
        bool = bool1;
        if (this.headers.equals(((GlideUrl)paramObject).headers))
          bool = true; 
      } 
      return bool;
    } 
    return false;
  }
  
  public String getCacheKey() {
    String str = this.stringUrl;
    if (str == null)
      str = ((URL)Preconditions.checkNotNull(this.url)).toString(); 
    return str;
  }
  
  public Map<String, String> getHeaders() {
    return this.headers.getHeaders();
  }
  
  public int hashCode() {
    if (this.hashCode == 0) {
      int i = getCacheKey().hashCode();
      this.hashCode = i;
      this.hashCode = i * 31 + this.headers.hashCode();
    } 
    return this.hashCode;
  }
  
  public String toString() {
    return getCacheKey();
  }
  
  public String toStringUrl() {
    return getSafeStringUrl();
  }
  
  public URL toURL() throws MalformedURLException {
    return getSafeUrl();
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(getCacheKeyBytes());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\GlideUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */