package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class GifBitmapProvider implements GifDecoder.BitmapProvider {
  private final ArrayPool arrayPool;
  
  private final BitmapPool bitmapPool;
  
  public GifBitmapProvider(BitmapPool paramBitmapPool) {
    this(paramBitmapPool, null);
  }
  
  public GifBitmapProvider(BitmapPool paramBitmapPool, ArrayPool paramArrayPool) {
    this.bitmapPool = paramBitmapPool;
    this.arrayPool = paramArrayPool;
  }
  
  public Bitmap obtain(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return this.bitmapPool.getDirty(paramInt1, paramInt2, paramConfig);
  }
  
  public byte[] obtainByteArray(int paramInt) {
    ArrayPool arrayPool = this.arrayPool;
    return (arrayPool == null) ? new byte[paramInt] : (byte[])arrayPool.get(paramInt, byte[].class);
  }
  
  public int[] obtainIntArray(int paramInt) {
    ArrayPool arrayPool = this.arrayPool;
    return (arrayPool == null) ? new int[paramInt] : (int[])arrayPool.get(paramInt, int[].class);
  }
  
  public void release(Bitmap paramBitmap) {
    this.bitmapPool.put(paramBitmap);
  }
  
  public void release(byte[] paramArrayOfbyte) {
    ArrayPool arrayPool = this.arrayPool;
    if (arrayPool == null)
      return; 
    arrayPool.put(paramArrayOfbyte);
  }
  
  public void release(int[] paramArrayOfint) {
    ArrayPool arrayPool = this.arrayPool;
    if (arrayPool == null)
      return; 
    arrayPool.put(paramArrayOfint);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\gif\GifBitmapProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */