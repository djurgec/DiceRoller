package com.bumptech.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public interface Key {
  public static final Charset CHARSET = Charset.forName("UTF-8");
  
  public static final String STRING_CHARSET_NAME = "UTF-8";
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  void updateDiskCacheKey(MessageDigest paramMessageDigest);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\Key.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */