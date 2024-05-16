package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class RoundedCorners extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.RoundedCorners".getBytes(CHARSET);
  
  private final int roundingRadius;
  
  public RoundedCorners(int paramInt) {
    boolean bool;
    if (paramInt > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool, "roundingRadius must be greater than 0.");
    this.roundingRadius = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof RoundedCorners;
    boolean bool = false;
    if (bool1) {
      paramObject = paramObject;
      if (this.roundingRadius == ((RoundedCorners)paramObject).roundingRadius)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  public int hashCode() {
    return Util.hashCode("com.bumptech.glide.load.resource.bitmap.RoundedCorners".hashCode(), Util.hashCode(this.roundingRadius));
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.roundedCorners(paramBitmapPool, paramBitmap, this.roundingRadius);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
    paramMessageDigest.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\RoundedCorners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */