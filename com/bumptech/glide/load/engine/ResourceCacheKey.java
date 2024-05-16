package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

final class ResourceCacheKey implements Key {
  private static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES = new LruCache(50L);
  
  private final ArrayPool arrayPool;
  
  private final Class<?> decodedResourceClass;
  
  private final int height;
  
  private final Options options;
  
  private final Key signature;
  
  private final Key sourceKey;
  
  private final Transformation<?> transformation;
  
  private final int width;
  
  ResourceCacheKey(ArrayPool paramArrayPool, Key paramKey1, Key paramKey2, int paramInt1, int paramInt2, Transformation<?> paramTransformation, Class<?> paramClass, Options paramOptions) {
    this.arrayPool = paramArrayPool;
    this.sourceKey = paramKey1;
    this.signature = paramKey2;
    this.width = paramInt1;
    this.height = paramInt2;
    this.transformation = paramTransformation;
    this.decodedResourceClass = paramClass;
    this.options = paramOptions;
  }
  
  private byte[] getResourceClassBytes() {
    LruCache<Class<?>, byte[]> lruCache = RESOURCE_CLASS_BYTES;
    byte[] arrayOfByte2 = (byte[])lruCache.get(this.decodedResourceClass);
    byte[] arrayOfByte1 = arrayOfByte2;
    if (arrayOfByte2 == null) {
      arrayOfByte1 = this.decodedResourceClass.getName().getBytes(CHARSET);
      lruCache.put(this.decodedResourceClass, arrayOfByte1);
    } 
    return arrayOfByte1;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof ResourceCacheKey;
    boolean bool = false;
    if (bool1) {
      paramObject = paramObject;
      if (this.height == ((ResourceCacheKey)paramObject).height && this.width == ((ResourceCacheKey)paramObject).width && Util.bothNullOrEqual(this.transformation, ((ResourceCacheKey)paramObject).transformation) && this.decodedResourceClass.equals(((ResourceCacheKey)paramObject).decodedResourceClass) && this.sourceKey.equals(((ResourceCacheKey)paramObject).sourceKey) && this.signature.equals(((ResourceCacheKey)paramObject).signature) && this.options.equals(((ResourceCacheKey)paramObject).options))
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  public int hashCode() {
    int j = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
    Transformation<?> transformation = this.transformation;
    int i = j;
    if (transformation != null)
      i = j * 31 + transformation.hashCode(); 
    return (i * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
  }
  
  public String toString() {
    return "ResourceCacheKey{sourceKey=" + this.sourceKey + ", signature=" + this.signature + ", width=" + this.width + ", height=" + this.height + ", decodedResourceClass=" + this.decodedResourceClass + ", transformation='" + this.transformation + '\'' + ", options=" + this.options + '}';
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    byte[] arrayOfByte = (byte[])this.arrayPool.getExact(8, byte[].class);
    ByteBuffer.wrap(arrayOfByte).putInt(this.width).putInt(this.height).array();
    this.signature.updateDiskCacheKey(paramMessageDigest);
    this.sourceKey.updateDiskCacheKey(paramMessageDigest);
    paramMessageDigest.update(arrayOfByte);
    Transformation<?> transformation = this.transformation;
    if (transformation != null)
      transformation.updateDiskCacheKey(paramMessageDigest); 
    this.options.updateDiskCacheKey(paramMessageDigest);
    paramMessageDigest.update(getResourceClassBytes());
    this.arrayPool.put(arrayOfByte);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\ResourceCacheKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */