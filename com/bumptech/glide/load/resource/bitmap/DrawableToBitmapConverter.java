package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import java.util.concurrent.locks.Lock;

final class DrawableToBitmapConverter {
  private static final BitmapPool NO_RECYCLE_BITMAP_POOL = (BitmapPool)new BitmapPoolAdapter() {
      public void put(Bitmap param1Bitmap) {}
    };
  
  private static final String TAG = "DrawableToBitmap";
  
  static Resource<Bitmap> convert(BitmapPool paramBitmapPool, Drawable paramDrawable, int paramInt1, int paramInt2) {
    Bitmap bitmap;
    Drawable drawable = paramDrawable.getCurrent();
    paramDrawable = null;
    boolean bool = false;
    if (drawable instanceof BitmapDrawable) {
      bitmap = ((BitmapDrawable)drawable).getBitmap();
    } else if (!(drawable instanceof android.graphics.drawable.Animatable)) {
      bitmap = drawToBitmap(paramBitmapPool, drawable, paramInt1, paramInt2);
      bool = true;
    } 
    if (!bool)
      paramBitmapPool = NO_RECYCLE_BITMAP_POOL; 
    return BitmapResource.obtain(bitmap, paramBitmapPool);
  }
  
  private static Bitmap drawToBitmap(BitmapPool paramBitmapPool, Drawable paramDrawable, int paramInt1, int paramInt2) {
    if (paramInt1 == Integer.MIN_VALUE && paramDrawable.getIntrinsicWidth() <= 0) {
      if (Log.isLoggable("DrawableToBitmap", 5))
        Log.w("DrawableToBitmap", "Unable to draw " + paramDrawable + " to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic width"); 
      return null;
    } 
    if (paramInt2 == Integer.MIN_VALUE && paramDrawable.getIntrinsicHeight() <= 0) {
      if (Log.isLoggable("DrawableToBitmap", 5))
        Log.w("DrawableToBitmap", "Unable to draw " + paramDrawable + " to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic height"); 
      return null;
    } 
    if (paramDrawable.getIntrinsicWidth() > 0)
      paramInt1 = paramDrawable.getIntrinsicWidth(); 
    if (paramDrawable.getIntrinsicHeight() > 0)
      paramInt2 = paramDrawable.getIntrinsicHeight(); 
    Lock lock = TransformationUtils.getBitmapDrawableLock();
    lock.lock();
    null = paramBitmapPool.get(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
    try {
      Canvas canvas = new Canvas();
      this(null);
      paramDrawable.setBounds(0, 0, paramInt1, paramInt2);
      paramDrawable.draw(canvas);
      canvas.setBitmap(null);
      return null;
    } finally {
      lock.unlock();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\DrawableToBitmapConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */