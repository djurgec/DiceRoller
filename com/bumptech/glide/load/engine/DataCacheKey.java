package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

final class DataCacheKey implements Key {
  private final Key signature;
  
  private final Key sourceKey;
  
  DataCacheKey(Key paramKey1, Key paramKey2) {
    this.sourceKey = paramKey1;
    this.signature = paramKey2;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof DataCacheKey;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (this.sourceKey.equals(((DataCacheKey)paramObject).sourceKey)) {
        bool = bool1;
        if (this.signature.equals(((DataCacheKey)paramObject).signature))
          bool = true; 
      } 
      return bool;
    } 
    return false;
  }
  
  Key getSourceKey() {
    return this.sourceKey;
  }
  
  public int hashCode() {
    return this.sourceKey.hashCode() * 31 + this.signature.hashCode();
  }
  
  public String toString() {
    return "DataCacheKey{sourceKey=" + this.sourceKey + ", signature=" + this.signature + '}';
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.sourceKey.updateDiskCacheKey(paramMessageDigest);
    this.signature.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\DataCacheKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */