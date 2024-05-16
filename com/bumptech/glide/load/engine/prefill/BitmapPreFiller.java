package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public final class BitmapPreFiller {
  private final BitmapPool bitmapPool;
  
  private BitmapPreFillRunner current;
  
  private final DecodeFormat defaultFormat;
  
  private final MemoryCache memoryCache;
  
  public BitmapPreFiller(MemoryCache paramMemoryCache, BitmapPool paramBitmapPool, DecodeFormat paramDecodeFormat) {
    this.memoryCache = paramMemoryCache;
    this.bitmapPool = paramBitmapPool;
    this.defaultFormat = paramDecodeFormat;
  }
  
  private static int getSizeInBytes(PreFillType paramPreFillType) {
    return Util.getBitmapByteSize(paramPreFillType.getWidth(), paramPreFillType.getHeight(), paramPreFillType.getConfig());
  }
  
  PreFillQueue generateAllocationOrder(PreFillType... paramVarArgs) {
    long l2 = this.memoryCache.getMaxSize();
    long l3 = this.memoryCache.getCurrentSize();
    long l1 = this.bitmapPool.getMaxSize();
    int i = 0;
    int j = paramVarArgs.length;
    boolean bool = false;
    byte b;
    for (b = 0; b < j; b++)
      i += paramVarArgs[b].getWeight(); 
    float f = (float)(l2 - l3 + l1) / i;
    HashMap<Object, Object> hashMap = new HashMap<>();
    i = paramVarArgs.length;
    for (b = bool; b < i; b++) {
      PreFillType preFillType = paramVarArgs[b];
      hashMap.put(preFillType, Integer.valueOf(Math.round(preFillType.getWeight() * f) / getSizeInBytes(preFillType)));
    } 
    return new PreFillQueue((Map)hashMap);
  }
  
  public void preFill(PreFillType.Builder... paramVarArgs) {
    BitmapPreFillRunner bitmapPreFillRunner2 = this.current;
    if (bitmapPreFillRunner2 != null)
      bitmapPreFillRunner2.cancel(); 
    PreFillType[] arrayOfPreFillType = new PreFillType[paramVarArgs.length];
    for (byte b = 0; b < paramVarArgs.length; b++) {
      PreFillType.Builder builder = paramVarArgs[b];
      if (builder.getConfig() == null) {
        Bitmap.Config config;
        if (this.defaultFormat == DecodeFormat.PREFER_ARGB_8888) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        builder.setConfig(config);
      } 
      arrayOfPreFillType[b] = builder.build();
    } 
    PreFillQueue preFillQueue = generateAllocationOrder(arrayOfPreFillType);
    BitmapPreFillRunner bitmapPreFillRunner1 = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, preFillQueue);
    this.current = bitmapPreFillRunner1;
    Util.postOnUiThread(bitmapPreFillRunner1);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\prefill\BitmapPreFiller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */