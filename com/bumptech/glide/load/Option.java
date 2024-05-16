package com.bumptech.glide.load;

import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option<T> {
  private static final CacheKeyUpdater<Object> EMPTY_UPDATER = new CacheKeyUpdater() {
      public void update(byte[] param1ArrayOfbyte, Object param1Object, MessageDigest param1MessageDigest) {}
    };
  
  private final CacheKeyUpdater<T> cacheKeyUpdater;
  
  private final T defaultValue;
  
  private final String key;
  
  private volatile byte[] keyBytes;
  
  private Option(String paramString, T paramT, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    this.key = Preconditions.checkNotEmpty(paramString);
    this.defaultValue = paramT;
    this.cacheKeyUpdater = (CacheKeyUpdater<T>)Preconditions.checkNotNull(paramCacheKeyUpdater);
  }
  
  public static <T> Option<T> disk(String paramString, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    return new Option<>(paramString, null, paramCacheKeyUpdater);
  }
  
  public static <T> Option<T> disk(String paramString, T paramT, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    return new Option<>(paramString, paramT, paramCacheKeyUpdater);
  }
  
  private static <T> CacheKeyUpdater<T> emptyUpdater() {
    return (CacheKeyUpdater)EMPTY_UPDATER;
  }
  
  private byte[] getKeyBytes() {
    if (this.keyBytes == null)
      this.keyBytes = this.key.getBytes(Key.CHARSET); 
    return this.keyBytes;
  }
  
  public static <T> Option<T> memory(String paramString) {
    return new Option<>(paramString, null, emptyUpdater());
  }
  
  public static <T> Option<T> memory(String paramString, T paramT) {
    return new Option<>(paramString, paramT, emptyUpdater());
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Option) {
      paramObject = paramObject;
      return this.key.equals(((Option)paramObject).key);
    } 
    return false;
  }
  
  public T getDefaultValue() {
    return this.defaultValue;
  }
  
  public int hashCode() {
    return this.key.hashCode();
  }
  
  public String toString() {
    return "Option{key='" + this.key + '\'' + '}';
  }
  
  public void update(T paramT, MessageDigest paramMessageDigest) {
    this.cacheKeyUpdater.update(getKeyBytes(), paramT, paramMessageDigest);
  }
  
  public static interface CacheKeyUpdater<T> {
    void update(byte[] param1ArrayOfbyte, T param1T, MessageDigest param1MessageDigest);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\Option.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */