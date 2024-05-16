package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeConfigStrategy implements LruPoolStrategy {
  private static final Bitmap.Config[] ALPHA_8_IN_CONFIGS;
  
  private static final Bitmap.Config[] ARGB_4444_IN_CONFIGS;
  
  private static final Bitmap.Config[] ARGB_8888_IN_CONFIGS;
  
  private static final int MAX_SIZE_MULTIPLE = 8;
  
  private static final Bitmap.Config[] RGBA_F16_IN_CONFIGS;
  
  private static final Bitmap.Config[] RGB_565_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.RGB_565 };
  
  private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();
  
  private final KeyPool keyPool = new KeyPool();
  
  private final Map<Bitmap.Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap<>();
  
  static {
    ARGB_4444_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.ARGB_4444 };
    ALPHA_8_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.ALPHA_8 };
  }
  
  private void decrementBitmapOfSize(Integer paramInteger, Bitmap paramBitmap) {
    NavigableMap<Integer, Integer> navigableMap = getSizesForConfig(paramBitmap.getConfig());
    Integer integer = navigableMap.get(paramInteger);
    if (integer != null) {
      if (integer.intValue() == 1) {
        navigableMap.remove(paramInteger);
      } else {
        navigableMap.put(paramInteger, Integer.valueOf(integer.intValue() - 1));
      } 
      return;
    } 
    throw new NullPointerException("Tried to decrement empty size, size: " + paramInteger + ", removed: " + logBitmap(paramBitmap) + ", this: " + this);
  }
  
  private Key findBestKey(int paramInt, Bitmap.Config paramConfig) {
    // Byte code:
    //   0: aload_0
    //   1: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   4: iload_1
    //   5: aload_2
    //   6: invokevirtual get : (ILandroid/graphics/Bitmap$Config;)Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$Key;
    //   9: astore #6
    //   11: aload_2
    //   12: invokestatic getInConfigs : (Landroid/graphics/Bitmap$Config;)[Landroid/graphics/Bitmap$Config;
    //   15: astore #7
    //   17: aload #7
    //   19: arraylength
    //   20: istore #4
    //   22: iconst_0
    //   23: istore_3
    //   24: aload #6
    //   26: astore #5
    //   28: iload_3
    //   29: iload #4
    //   31: if_icmpge -> 149
    //   34: aload #7
    //   36: iload_3
    //   37: aaload
    //   38: astore #8
    //   40: aload_0
    //   41: aload #8
    //   43: invokespecial getSizesForConfig : (Landroid/graphics/Bitmap$Config;)Ljava/util/NavigableMap;
    //   46: iload_1
    //   47: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   50: invokeinterface ceilingKey : (Ljava/lang/Object;)Ljava/lang/Object;
    //   55: checkcast java/lang/Integer
    //   58: astore #9
    //   60: aload #9
    //   62: ifnull -> 143
    //   65: aload #9
    //   67: invokevirtual intValue : ()I
    //   70: iload_1
    //   71: bipush #8
    //   73: imul
    //   74: if_icmpgt -> 143
    //   77: aload #9
    //   79: invokevirtual intValue : ()I
    //   82: iload_1
    //   83: if_icmpne -> 115
    //   86: aload #8
    //   88: ifnonnull -> 102
    //   91: aload #6
    //   93: astore #5
    //   95: aload_2
    //   96: ifnull -> 149
    //   99: goto -> 115
    //   102: aload #6
    //   104: astore #5
    //   106: aload #8
    //   108: aload_2
    //   109: invokevirtual equals : (Ljava/lang/Object;)Z
    //   112: ifne -> 149
    //   115: aload_0
    //   116: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   119: aload #6
    //   121: invokevirtual offer : (Lcom/bumptech/glide/load/engine/bitmap_recycle/Poolable;)V
    //   124: aload_0
    //   125: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   128: aload #9
    //   130: invokevirtual intValue : ()I
    //   133: aload #8
    //   135: invokevirtual get : (ILandroid/graphics/Bitmap$Config;)Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$Key;
    //   138: astore #5
    //   140: goto -> 149
    //   143: iinc #3, 1
    //   146: goto -> 24
    //   149: aload #5
    //   151: areturn
  }
  
  static String getBitmapString(int paramInt, Bitmap.Config paramConfig) {
    return "[" + paramInt + "](" + paramConfig + ")";
  }
  
  private static Bitmap.Config[] getInConfigs(Bitmap.Config paramConfig) {
    if (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals(paramConfig))
      return RGBA_F16_IN_CONFIGS; 
    switch (paramConfig) {
      default:
        return new Bitmap.Config[] { paramConfig };
      case null:
        return ALPHA_8_IN_CONFIGS;
      case null:
        return ARGB_4444_IN_CONFIGS;
      case null:
        return RGB_565_IN_CONFIGS;
      case null:
        break;
    } 
    return ARGB_8888_IN_CONFIGS;
  }
  
  private NavigableMap<Integer, Integer> getSizesForConfig(Bitmap.Config paramConfig) {
    NavigableMap<Object, Object> navigableMap2 = (NavigableMap)this.sortedSizes.get(paramConfig);
    NavigableMap<Object, Object> navigableMap1 = navigableMap2;
    if (navigableMap2 == null) {
      navigableMap1 = new TreeMap<>();
      this.sortedSizes.put(paramConfig, navigableMap1);
    } 
    return (NavigableMap)navigableMap1;
  }
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Key key = findBestKey(Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig), paramConfig);
    Bitmap bitmap = this.groupedMap.get(key);
    if (bitmap != null) {
      decrementBitmapOfSize(Integer.valueOf(key.size), bitmap);
      bitmap.reconfigure(paramInt1, paramInt2, paramConfig);
    } 
    return bitmap;
  }
  
  public int getSize(Bitmap paramBitmap) {
    return Util.getBitmapByteSize(paramBitmap);
  }
  
  public String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return getBitmapString(Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig), paramConfig);
  }
  
  public String logBitmap(Bitmap paramBitmap) {
    return getBitmapString(Util.getBitmapByteSize(paramBitmap), paramBitmap.getConfig());
  }
  
  public void put(Bitmap paramBitmap) {
    int i = Util.getBitmapByteSize(paramBitmap);
    Key key = this.keyPool.get(i, paramBitmap.getConfig());
    this.groupedMap.put(key, paramBitmap);
    NavigableMap<Integer, Integer> navigableMap = getSizesForConfig(paramBitmap.getConfig());
    Integer integer = navigableMap.get(Integer.valueOf(key.size));
    int j = key.size;
    i = 1;
    if (integer != null)
      i = 1 + integer.intValue(); 
    navigableMap.put(Integer.valueOf(j), Integer.valueOf(i));
  }
  
  public Bitmap removeLast() {
    Bitmap bitmap = this.groupedMap.removeLast();
    if (bitmap != null)
      decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)), bitmap); 
    return bitmap;
  }
  
  public String toString() {
    StringBuilder stringBuilder = (new StringBuilder()).append("SizeConfigStrategy{groupedMap=").append(this.groupedMap).append(", sortedSizes=(");
    for (Map.Entry<Bitmap.Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet())
      stringBuilder.append(entry.getKey()).append('[').append(entry.getValue()).append("], "); 
    if (!this.sortedSizes.isEmpty())
      stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ""); 
    return stringBuilder.append(")}").toString();
  }
  
  static {
    Bitmap.Config[] arrayOfConfig2 = new Bitmap.Config[2];
    arrayOfConfig2[0] = Bitmap.Config.ARGB_8888;
    arrayOfConfig2[1] = null;
    Bitmap.Config[] arrayOfConfig1 = arrayOfConfig2;
    if (Build.VERSION.SDK_INT >= 26) {
      arrayOfConfig1 = Arrays.<Bitmap.Config>copyOf(arrayOfConfig2, arrayOfConfig2.length + 1);
      arrayOfConfig1[arrayOfConfig1.length - 1] = Bitmap.Config.RGBA_F16;
    } 
    ARGB_8888_IN_CONFIGS = arrayOfConfig1;
    RGBA_F16_IN_CONFIGS = arrayOfConfig1;
  }
  
  static final class Key implements Poolable {
    private Bitmap.Config config;
    
    private final SizeConfigStrategy.KeyPool pool;
    
    int size;
    
    public Key(SizeConfigStrategy.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
    }
    
    Key(SizeConfigStrategy.KeyPool param1KeyPool, int param1Int, Bitmap.Config param1Config) {
      this(param1KeyPool);
      init(param1Int, param1Config);
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      if (bool) {
        param1Object = param1Object;
        bool = bool1;
        if (this.size == ((Key)param1Object).size) {
          bool = bool1;
          if (Util.bothNullOrEqual(this.config, ((Key)param1Object).config))
            bool = true; 
        } 
        return bool;
      } 
      return false;
    }
    
    public int hashCode() {
      byte b;
      int i = this.size;
      Bitmap.Config config = this.config;
      if (config != null) {
        b = config.hashCode();
      } else {
        b = 0;
      } 
      return i * 31 + b;
    }
    
    public void init(int param1Int, Bitmap.Config param1Config) {
      this.size = param1Int;
      this.config = param1Config;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      return SizeConfigStrategy.getBitmapString(this.size, this.config);
    }
  }
  
  static class KeyPool extends BaseKeyPool<Key> {
    protected SizeConfigStrategy.Key create() {
      return new SizeConfigStrategy.Key(this);
    }
    
    public SizeConfigStrategy.Key get(int param1Int, Bitmap.Config param1Config) {
      SizeConfigStrategy.Key key = get();
      key.init(param1Int, param1Config);
      return key;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\SizeConfigStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */