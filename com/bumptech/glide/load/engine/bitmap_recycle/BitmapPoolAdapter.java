package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

public class BitmapPoolAdapter implements BitmapPool {
  public void clearMemory() {}
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return Bitmap.createBitmap(paramInt1, paramInt2, paramConfig);
  }
  
  public Bitmap getDirty(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return get(paramInt1, paramInt2, paramConfig);
  }
  
  public long getMaxSize() {
    return 0L;
  }
  
  public void put(Bitmap paramBitmap) {
    paramBitmap.recycle();
  }
  
  public void setSizeMultiplier(float paramFloat) {}
  
  public void trimMemory(int paramInt) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\BitmapPoolAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */