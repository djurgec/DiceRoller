package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

public abstract class BitmapTransformation implements Transformation<Bitmap> {
  protected abstract Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2);
  
  public final Resource<Bitmap> transform(Context paramContext, Resource<Bitmap> paramResource, int paramInt1, int paramInt2) {
    if (Util.isValidDimensions(paramInt1, paramInt2)) {
      Resource<Bitmap> resource;
      BitmapPool bitmapPool = Glide.get(paramContext).getBitmapPool();
      Bitmap bitmap2 = (Bitmap)paramResource.get();
      if (paramInt1 == Integer.MIN_VALUE)
        paramInt1 = bitmap2.getWidth(); 
      if (paramInt2 == Integer.MIN_VALUE)
        paramInt2 = bitmap2.getHeight(); 
      Bitmap bitmap1 = transform(bitmapPool, bitmap2, paramInt1, paramInt2);
      if (bitmap2.equals(bitmap1)) {
        resource = paramResource;
      } else {
        resource = BitmapResource.obtain((Bitmap)resource, bitmapPool);
      } 
      return resource;
    } 
    throw new IllegalArgumentException("Cannot apply transformation on width: " + paramInt1 + " or height: " + paramInt2 + " less than or equal to zero and not Target.SIZE_ORIGINAL");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\BitmapTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */