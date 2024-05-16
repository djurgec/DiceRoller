package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class GranularRoundedCorners extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners".getBytes(CHARSET);
  
  private final float bottomLeft;
  
  private final float bottomRight;
  
  private final float topLeft;
  
  private final float topRight;
  
  public GranularRoundedCorners(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.topLeft = paramFloat1;
    this.topRight = paramFloat2;
    this.bottomRight = paramFloat3;
    this.bottomLeft = paramFloat4;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof GranularRoundedCorners;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (this.topLeft == ((GranularRoundedCorners)paramObject).topLeft) {
        bool = bool1;
        if (this.topRight == ((GranularRoundedCorners)paramObject).topRight) {
          bool = bool1;
          if (this.bottomRight == ((GranularRoundedCorners)paramObject).bottomRight) {
            bool = bool1;
            if (this.bottomLeft == ((GranularRoundedCorners)paramObject).bottomLeft)
              bool = true; 
          } 
        } 
      } 
      return bool;
    } 
    return false;
  }
  
  public int hashCode() {
    int i = Util.hashCode("com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners".hashCode(), Util.hashCode(this.topLeft));
    i = Util.hashCode(this.topRight, i);
    i = Util.hashCode(this.bottomRight, i);
    return Util.hashCode(this.bottomLeft, i);
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.roundedCorners(paramBitmapPool, paramBitmap, this.topLeft, this.topRight, this.bottomRight, this.bottomLeft);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
    paramMessageDigest.update(ByteBuffer.allocate(16).putFloat(this.topLeft).putFloat(this.topRight).putFloat(this.bottomRight).putFloat(this.bottomLeft).array());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\GranularRoundedCorners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */