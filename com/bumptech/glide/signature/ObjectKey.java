package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class ObjectKey implements Key {
  private final Object object;
  
  public ObjectKey(Object paramObject) {
    this.object = Preconditions.checkNotNull(paramObject);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof ObjectKey) {
      paramObject = paramObject;
      return this.object.equals(((ObjectKey)paramObject).object);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.object.hashCode();
  }
  
  public String toString() {
    return "ObjectKey{object=" + this.object + '}';
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(this.object.toString().getBytes(CHARSET));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\signature\ObjectKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */