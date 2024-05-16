package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

public interface BitmapPool {
  void clearMemory();
  
  Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  Bitmap getDirty(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  long getMaxSize();
  
  void put(Bitmap paramBitmap);
  
  void setSizeMultiplier(float paramFloat);
  
  void trimMemory(int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\BitmapPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */