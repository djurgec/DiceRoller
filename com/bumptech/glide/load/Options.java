package com.bumptech.glide.load;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import java.security.MessageDigest;

public final class Options implements Key {
  private final ArrayMap<Option<?>, Object> values = (ArrayMap<Option<?>, Object>)new CachedHashCodeArrayMap();
  
  private static <T> void updateDiskCacheKey(Option<T> paramOption, Object paramObject, MessageDigest paramMessageDigest) {
    paramOption.update((T)paramObject, paramMessageDigest);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Options) {
      paramObject = paramObject;
      return this.values.equals(((Options)paramObject).values);
    } 
    return false;
  }
  
  public <T> T get(Option<T> paramOption) {
    Object object;
    if (this.values.containsKey(paramOption)) {
      object = this.values.get(paramOption);
    } else {
      object = object.getDefaultValue();
    } 
    return (T)object;
  }
  
  public int hashCode() {
    return this.values.hashCode();
  }
  
  public void putAll(Options paramOptions) {
    this.values.putAll((SimpleArrayMap)paramOptions.values);
  }
  
  public <T> Options set(Option<T> paramOption, T paramT) {
    this.values.put(paramOption, paramT);
    return this;
  }
  
  public String toString() {
    return "Options{values=" + this.values + '}';
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    for (byte b = 0; b < this.values.size(); b++)
      updateDiskCacheKey((Option)this.values.keyAt(b), this.values.valueAt(b), paramMessageDigest); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\Options.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */