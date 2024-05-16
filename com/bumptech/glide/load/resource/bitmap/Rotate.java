package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Rotate extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.Rotate";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.Rotate".getBytes(CHARSET);
  
  private final int degreesToRotate;
  
  public Rotate(int paramInt) {
    this.degreesToRotate = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof Rotate;
    boolean bool = false;
    if (bool1) {
      paramObject = paramObject;
      if (this.degreesToRotate == ((Rotate)paramObject).degreesToRotate)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  public int hashCode() {
    return Util.hashCode("com.bumptech.glide.load.resource.bitmap.Rotate".hashCode(), Util.hashCode(this.degreesToRotate));
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.rotateImage(paramBitmap, this.degreesToRotate);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
    paramMessageDigest.update(ByteBuffer.allocate(4).putInt(this.degreesToRotate).array());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\Rotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */