package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class MediaStoreSignature implements Key {
  private final long dateModified;
  
  private final String mimeType;
  
  private final int orientation;
  
  public MediaStoreSignature(String paramString, long paramLong, int paramInt) {
    if (paramString == null)
      paramString = ""; 
    this.mimeType = paramString;
    this.dateModified = paramLong;
    this.orientation = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return (this.dateModified != ((MediaStoreSignature)paramObject).dateModified) ? false : ((this.orientation != ((MediaStoreSignature)paramObject).orientation) ? false : (!!this.mimeType.equals(((MediaStoreSignature)paramObject).mimeType)));
  }
  
  public int hashCode() {
    int i = this.mimeType.hashCode();
    long l = this.dateModified;
    return (i * 31 + (int)(l ^ l >>> 32L)) * 31 + this.orientation;
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ByteBuffer.allocate(12).putLong(this.dateModified).putInt(this.orientation).array());
    paramMessageDigest.update(this.mimeType.getBytes(CHARSET));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\signature\MediaStoreSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */