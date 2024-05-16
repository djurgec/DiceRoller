package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool implements BitmapPool {
  private static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;
  
  private static final String TAG = "LruBitmapPool";
  
  private final Set<Bitmap.Config> allowedConfigs;
  
  private long currentSize;
  
  private int evictions;
  
  private int hits;
  
  private final long initialMaxSize;
  
  private long maxSize;
  
  private int misses;
  
  private int puts;
  
  private final LruPoolStrategy strategy;
  
  private final BitmapTracker tracker;
  
  public LruBitmapPool(long paramLong) {
    this(paramLong, getDefaultStrategy(), getDefaultAllowedConfigs());
  }
  
  LruBitmapPool(long paramLong, LruPoolStrategy paramLruPoolStrategy, Set<Bitmap.Config> paramSet) {
    this.initialMaxSize = paramLong;
    this.maxSize = paramLong;
    this.strategy = paramLruPoolStrategy;
    this.allowedConfigs = paramSet;
    this.tracker = new NullBitmapTracker();
  }
  
  public LruBitmapPool(long paramLong, Set<Bitmap.Config> paramSet) {
    this(paramLong, getDefaultStrategy(), paramSet);
  }
  
  private static void assertNotHardwareConfig(Bitmap.Config paramConfig) {
    if (Build.VERSION.SDK_INT < 26)
      return; 
    if (paramConfig != Bitmap.Config.HARDWARE)
      return; 
    throw new IllegalArgumentException("Cannot create a mutable Bitmap with config: " + paramConfig + ". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
  }
  
  private static Bitmap createBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    if (paramConfig == null)
      paramConfig = DEFAULT_CONFIG; 
    return Bitmap.createBitmap(paramInt1, paramInt2, paramConfig);
  }
  
  private void dump() {
    if (Log.isLoggable("LruBitmapPool", 2))
      dumpUnchecked(); 
  }
  
  private void dumpUnchecked() {
    Log.v("LruBitmapPool", "Hits=" + this.hits + ", misses=" + this.misses + ", puts=" + this.puts + ", evictions=" + this.evictions + ", currentSize=" + this.currentSize + ", maxSize=" + this.maxSize + "\nStrategy=" + this.strategy);
  }
  
  private void evict() {
    trimToSize(this.maxSize);
  }
  
  private static Set<Bitmap.Config> getDefaultAllowedConfigs() {
    HashSet<? extends Bitmap.Config> hashSet = new HashSet(Arrays.asList((Object[])Bitmap.Config.values()));
    if (Build.VERSION.SDK_INT >= 19)
      hashSet.add(null); 
    if (Build.VERSION.SDK_INT >= 26)
      hashSet.remove(Bitmap.Config.HARDWARE); 
    return Collections.unmodifiableSet(hashSet);
  }
  
  private static LruPoolStrategy getDefaultStrategy() {
    AttributeStrategy attributeStrategy;
    if (Build.VERSION.SDK_INT >= 19) {
      SizeConfigStrategy sizeConfigStrategy = new SizeConfigStrategy();
    } else {
      attributeStrategy = new AttributeStrategy();
    } 
    return attributeStrategy;
  }
  
  private Bitmap getDirtyOrNull(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_3
    //   3: invokestatic assertNotHardwareConfig : (Landroid/graphics/Bitmap$Config;)V
    //   6: aload_0
    //   7: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   10: astore #5
    //   12: aload_3
    //   13: ifnull -> 22
    //   16: aload_3
    //   17: astore #4
    //   19: goto -> 27
    //   22: getstatic com/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool.DEFAULT_CONFIG : Landroid/graphics/Bitmap$Config;
    //   25: astore #4
    //   27: aload #5
    //   29: iload_1
    //   30: iload_2
    //   31: aload #4
    //   33: invokeinterface get : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   38: astore #4
    //   40: aload #4
    //   42: ifnonnull -> 108
    //   45: ldc 'LruBitmapPool'
    //   47: iconst_3
    //   48: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   51: ifeq -> 95
    //   54: new java/lang/StringBuilder
    //   57: astore #5
    //   59: aload #5
    //   61: invokespecial <init> : ()V
    //   64: ldc 'LruBitmapPool'
    //   66: aload #5
    //   68: ldc 'Missing bitmap='
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: aload_0
    //   74: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   77: iload_1
    //   78: iload_2
    //   79: aload_3
    //   80: invokeinterface logBitmap : (IILandroid/graphics/Bitmap$Config;)Ljava/lang/String;
    //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   94: pop
    //   95: aload_0
    //   96: aload_0
    //   97: getfield misses : I
    //   100: iconst_1
    //   101: iadd
    //   102: putfield misses : I
    //   105: goto -> 155
    //   108: aload_0
    //   109: aload_0
    //   110: getfield hits : I
    //   113: iconst_1
    //   114: iadd
    //   115: putfield hits : I
    //   118: aload_0
    //   119: aload_0
    //   120: getfield currentSize : J
    //   123: aload_0
    //   124: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   127: aload #4
    //   129: invokeinterface getSize : (Landroid/graphics/Bitmap;)I
    //   134: i2l
    //   135: lsub
    //   136: putfield currentSize : J
    //   139: aload_0
    //   140: getfield tracker : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool$BitmapTracker;
    //   143: aload #4
    //   145: invokeinterface remove : (Landroid/graphics/Bitmap;)V
    //   150: aload #4
    //   152: invokestatic normalize : (Landroid/graphics/Bitmap;)V
    //   155: ldc 'LruBitmapPool'
    //   157: iconst_2
    //   158: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   161: ifeq -> 205
    //   164: new java/lang/StringBuilder
    //   167: astore #5
    //   169: aload #5
    //   171: invokespecial <init> : ()V
    //   174: ldc 'LruBitmapPool'
    //   176: aload #5
    //   178: ldc 'Get bitmap='
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: aload_0
    //   184: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   187: iload_1
    //   188: iload_2
    //   189: aload_3
    //   190: invokeinterface logBitmap : (IILandroid/graphics/Bitmap$Config;)Ljava/lang/String;
    //   195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: invokevirtual toString : ()Ljava/lang/String;
    //   201: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   204: pop
    //   205: aload_0
    //   206: invokespecial dump : ()V
    //   209: aload_0
    //   210: monitorexit
    //   211: aload #4
    //   213: areturn
    //   214: astore_3
    //   215: aload_0
    //   216: monitorexit
    //   217: aload_3
    //   218: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	214	finally
    //   22	27	214	finally
    //   27	40	214	finally
    //   45	95	214	finally
    //   95	105	214	finally
    //   108	155	214	finally
    //   155	205	214	finally
    //   205	209	214	finally
  }
  
  private static void maybeSetPreMultiplied(Bitmap paramBitmap) {
    if (Build.VERSION.SDK_INT >= 19)
      paramBitmap.setPremultiplied(true); 
  }
  
  private static void normalize(Bitmap paramBitmap) {
    paramBitmap.setHasAlpha(true);
    maybeSetPreMultiplied(paramBitmap);
  }
  
  private void trimToSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSize : J
    //   6: lload_1
    //   7: lcmp
    //   8: ifle -> 153
    //   11: aload_0
    //   12: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   15: invokeinterface removeLast : ()Landroid/graphics/Bitmap;
    //   20: astore_3
    //   21: aload_3
    //   22: ifnonnull -> 54
    //   25: ldc 'LruBitmapPool'
    //   27: iconst_5
    //   28: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   31: ifeq -> 46
    //   34: ldc 'LruBitmapPool'
    //   36: ldc 'Size mismatch, resetting'
    //   38: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   41: pop
    //   42: aload_0
    //   43: invokespecial dumpUnchecked : ()V
    //   46: aload_0
    //   47: lconst_0
    //   48: putfield currentSize : J
    //   51: aload_0
    //   52: monitorexit
    //   53: return
    //   54: aload_0
    //   55: getfield tracker : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool$BitmapTracker;
    //   58: aload_3
    //   59: invokeinterface remove : (Landroid/graphics/Bitmap;)V
    //   64: aload_0
    //   65: aload_0
    //   66: getfield currentSize : J
    //   69: aload_0
    //   70: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   73: aload_3
    //   74: invokeinterface getSize : (Landroid/graphics/Bitmap;)I
    //   79: i2l
    //   80: lsub
    //   81: putfield currentSize : J
    //   84: aload_0
    //   85: aload_0
    //   86: getfield evictions : I
    //   89: iconst_1
    //   90: iadd
    //   91: putfield evictions : I
    //   94: ldc 'LruBitmapPool'
    //   96: iconst_3
    //   97: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   100: ifeq -> 142
    //   103: new java/lang/StringBuilder
    //   106: astore #4
    //   108: aload #4
    //   110: invokespecial <init> : ()V
    //   113: ldc 'LruBitmapPool'
    //   115: aload #4
    //   117: ldc 'Evicting bitmap='
    //   119: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: aload_0
    //   123: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   126: aload_3
    //   127: invokeinterface logBitmap : (Landroid/graphics/Bitmap;)Ljava/lang/String;
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: invokevirtual toString : ()Ljava/lang/String;
    //   138: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   141: pop
    //   142: aload_0
    //   143: invokespecial dump : ()V
    //   146: aload_3
    //   147: invokevirtual recycle : ()V
    //   150: goto -> 2
    //   153: aload_0
    //   154: monitorexit
    //   155: return
    //   156: astore_3
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_3
    //   160: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	156	finally
    //   25	46	156	finally
    //   46	51	156	finally
    //   54	142	156	finally
    //   142	150	156	finally
  }
  
  public void clearMemory() {
    if (Log.isLoggable("LruBitmapPool", 3))
      Log.d("LruBitmapPool", "clearMemory"); 
    trimToSize(0L);
  }
  
  public long evictionCount() {
    return this.evictions;
  }
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Bitmap bitmap1;
    Bitmap bitmap2 = getDirtyOrNull(paramInt1, paramInt2, paramConfig);
    if (bitmap2 != null) {
      bitmap2.eraseColor(0);
      bitmap1 = bitmap2;
    } else {
      bitmap1 = createBitmap(paramInt1, paramInt2, (Bitmap.Config)bitmap1);
    } 
    return bitmap1;
  }
  
  public long getCurrentSize() {
    return this.currentSize;
  }
  
  public Bitmap getDirty(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Bitmap bitmap2 = getDirtyOrNull(paramInt1, paramInt2, paramConfig);
    Bitmap bitmap1 = bitmap2;
    if (bitmap2 == null)
      bitmap1 = createBitmap(paramInt1, paramInt2, paramConfig); 
    return bitmap1;
  }
  
  public long getMaxSize() {
    return this.maxSize;
  }
  
  public long hitCount() {
    return this.hits;
  }
  
  public long missCount() {
    return this.misses;
  }
  
  public void put(Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 272
    //   6: aload_1
    //   7: invokevirtual isRecycled : ()Z
    //   10: ifne -> 255
    //   13: aload_1
    //   14: invokevirtual isMutable : ()Z
    //   17: ifeq -> 167
    //   20: aload_0
    //   21: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   24: aload_1
    //   25: invokeinterface getSize : (Landroid/graphics/Bitmap;)I
    //   30: i2l
    //   31: aload_0
    //   32: getfield maxSize : J
    //   35: lcmp
    //   36: ifgt -> 167
    //   39: aload_0
    //   40: getfield allowedConfigs : Ljava/util/Set;
    //   43: aload_1
    //   44: invokevirtual getConfig : ()Landroid/graphics/Bitmap$Config;
    //   47: invokeinterface contains : (Ljava/lang/Object;)Z
    //   52: ifne -> 58
    //   55: goto -> 167
    //   58: aload_0
    //   59: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   62: aload_1
    //   63: invokeinterface getSize : (Landroid/graphics/Bitmap;)I
    //   68: istore_2
    //   69: aload_0
    //   70: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   73: aload_1
    //   74: invokeinterface put : (Landroid/graphics/Bitmap;)V
    //   79: aload_0
    //   80: getfield tracker : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool$BitmapTracker;
    //   83: aload_1
    //   84: invokeinterface add : (Landroid/graphics/Bitmap;)V
    //   89: aload_0
    //   90: aload_0
    //   91: getfield puts : I
    //   94: iconst_1
    //   95: iadd
    //   96: putfield puts : I
    //   99: aload_0
    //   100: aload_0
    //   101: getfield currentSize : J
    //   104: iload_2
    //   105: i2l
    //   106: ladd
    //   107: putfield currentSize : J
    //   110: ldc 'LruBitmapPool'
    //   112: iconst_2
    //   113: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   116: ifeq -> 156
    //   119: new java/lang/StringBuilder
    //   122: astore_3
    //   123: aload_3
    //   124: invokespecial <init> : ()V
    //   127: ldc 'LruBitmapPool'
    //   129: aload_3
    //   130: ldc_w 'Put bitmap in pool='
    //   133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: aload_0
    //   137: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   140: aload_1
    //   141: invokeinterface logBitmap : (Landroid/graphics/Bitmap;)Ljava/lang/String;
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: invokevirtual toString : ()Ljava/lang/String;
    //   152: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   155: pop
    //   156: aload_0
    //   157: invokespecial dump : ()V
    //   160: aload_0
    //   161: invokespecial evict : ()V
    //   164: aload_0
    //   165: monitorexit
    //   166: return
    //   167: ldc 'LruBitmapPool'
    //   169: iconst_2
    //   170: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   173: ifeq -> 248
    //   176: new java/lang/StringBuilder
    //   179: astore_3
    //   180: aload_3
    //   181: invokespecial <init> : ()V
    //   184: ldc 'LruBitmapPool'
    //   186: aload_3
    //   187: ldc_w 'Reject bitmap from pool, bitmap: '
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: aload_0
    //   194: getfield strategy : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy;
    //   197: aload_1
    //   198: invokeinterface logBitmap : (Landroid/graphics/Bitmap;)Ljava/lang/String;
    //   203: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: ldc_w ', is mutable: '
    //   209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: aload_1
    //   213: invokevirtual isMutable : ()Z
    //   216: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   219: ldc_w ', is allowed config: '
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: aload_0
    //   226: getfield allowedConfigs : Ljava/util/Set;
    //   229: aload_1
    //   230: invokevirtual getConfig : ()Landroid/graphics/Bitmap$Config;
    //   233: invokeinterface contains : (Ljava/lang/Object;)Z
    //   238: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   241: invokevirtual toString : ()Ljava/lang/String;
    //   244: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   247: pop
    //   248: aload_1
    //   249: invokevirtual recycle : ()V
    //   252: aload_0
    //   253: monitorexit
    //   254: return
    //   255: new java/lang/IllegalStateException
    //   258: astore_1
    //   259: aload_1
    //   260: ldc_w 'Cannot pool recycled bitmap'
    //   263: invokespecial <init> : (Ljava/lang/String;)V
    //   266: aload_1
    //   267: athrow
    //   268: astore_1
    //   269: goto -> 285
    //   272: new java/lang/NullPointerException
    //   275: astore_1
    //   276: aload_1
    //   277: ldc_w 'Bitmap must not be null'
    //   280: invokespecial <init> : (Ljava/lang/String;)V
    //   283: aload_1
    //   284: athrow
    //   285: aload_0
    //   286: monitorexit
    //   287: aload_1
    //   288: athrow
    // Exception table:
    //   from	to	target	type
    //   6	55	268	finally
    //   58	156	268	finally
    //   156	164	268	finally
    //   167	248	268	finally
    //   248	252	268	finally
    //   255	268	268	finally
    //   272	285	268	finally
  }
  
  public void setSizeMultiplier(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield initialMaxSize : J
    //   7: l2f
    //   8: fload_1
    //   9: fmul
    //   10: invokestatic round : (F)I
    //   13: i2l
    //   14: putfield maxSize : J
    //   17: aload_0
    //   18: invokespecial evict : ()V
    //   21: aload_0
    //   22: monitorexit
    //   23: return
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	24	finally
  }
  
  public void trimMemory(int paramInt) {
    if (Log.isLoggable("LruBitmapPool", 3))
      Log.d("LruBitmapPool", "trimMemory, level=" + paramInt); 
    if (paramInt >= 40 || (Build.VERSION.SDK_INT >= 23 && paramInt >= 20)) {
      clearMemory();
      return;
    } 
    if (paramInt >= 20 || paramInt == 15)
      trimToSize(getMaxSize() / 2L); 
  }
  
  private static interface BitmapTracker {
    void add(Bitmap param1Bitmap);
    
    void remove(Bitmap param1Bitmap);
  }
  
  private static final class NullBitmapTracker implements BitmapTracker {
    public void add(Bitmap param1Bitmap) {}
    
    public void remove(Bitmap param1Bitmap) {}
  }
  
  private static class ThrowingBitmapTracker implements BitmapTracker {
    private final Set<Bitmap> bitmaps = Collections.synchronizedSet(new HashSet<>());
    
    public void add(Bitmap param1Bitmap) {
      if (!this.bitmaps.contains(param1Bitmap)) {
        this.bitmaps.add(param1Bitmap);
        return;
      } 
      throw new IllegalStateException("Can't add already added bitmap: " + param1Bitmap + " [" + param1Bitmap.getWidth() + "x" + param1Bitmap.getHeight() + "]");
    }
    
    public void remove(Bitmap param1Bitmap) {
      if (this.bitmaps.contains(param1Bitmap)) {
        this.bitmaps.remove(param1Bitmap);
        return;
      } 
      throw new IllegalStateException("Cannot remove bitmap not in tracker");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\LruBitmapPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */