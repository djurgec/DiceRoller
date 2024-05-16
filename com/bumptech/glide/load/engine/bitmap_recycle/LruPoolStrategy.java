package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

interface LruPoolStrategy {
  Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  int getSize(Bitmap paramBitmap);
  
  String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  String logBitmap(Bitmap paramBitmap);
  
  void put(Bitmap paramBitmap);
  
  Bitmap removeLast();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\LruPoolStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */