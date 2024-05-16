package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import com.bumptech.glide.util.Util;

class AttributeStrategy implements LruPoolStrategy {
  private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();
  
  private final KeyPool keyPool = new KeyPool();
  
  static String getBitmapString(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return "[" + paramInt1 + "x" + paramInt2 + "], " + paramConfig;
  }
  
  private static String getBitmapString(Bitmap paramBitmap) {
    return getBitmapString(paramBitmap.getWidth(), paramBitmap.getHeight(), paramBitmap.getConfig());
  }
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Key key = this.keyPool.get(paramInt1, paramInt2, paramConfig);
    return this.groupedMap.get(key);
  }
  
  public int getSize(Bitmap paramBitmap) {
    return Util.getBitmapByteSize(paramBitmap);
  }
  
  public String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return getBitmapString(paramInt1, paramInt2, paramConfig);
  }
  
  public String logBitmap(Bitmap paramBitmap) {
    return getBitmapString(paramBitmap);
  }
  
  public void put(Bitmap paramBitmap) {
    Key key = this.keyPool.get(paramBitmap.getWidth(), paramBitmap.getHeight(), paramBitmap.getConfig());
    this.groupedMap.put(key, paramBitmap);
  }
  
  public Bitmap removeLast() {
    return this.groupedMap.removeLast();
  }
  
  public String toString() {
    return "AttributeStrategy:\n  " + this.groupedMap;
  }
  
  static class Key implements Poolable {
    private Bitmap.Config config;
    
    private int height;
    
    private final AttributeStrategy.KeyPool pool;
    
    private int width;
    
    public Key(AttributeStrategy.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      if (bool) {
        param1Object = param1Object;
        bool = bool1;
        if (this.width == ((Key)param1Object).width) {
          bool = bool1;
          if (this.height == ((Key)param1Object).height) {
            bool = bool1;
            if (this.config == ((Key)param1Object).config)
              bool = true; 
          } 
        } 
        return bool;
      } 
      return false;
    }
    
    public int hashCode() {
      byte b;
      int j = this.width;
      int i = this.height;
      Bitmap.Config config = this.config;
      if (config != null) {
        b = config.hashCode();
      } else {
        b = 0;
      } 
      return (j * 31 + i) * 31 + b;
    }
    
    public void init(int param1Int1, int param1Int2, Bitmap.Config param1Config) {
      this.width = param1Int1;
      this.height = param1Int2;
      this.config = param1Config;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      return AttributeStrategy.getBitmapString(this.width, this.height, this.config);
    }
  }
  
  static class KeyPool extends BaseKeyPool<Key> {
    protected AttributeStrategy.Key create() {
      return new AttributeStrategy.Key(this);
    }
    
    AttributeStrategy.Key get(int param1Int1, int param1Int2, Bitmap.Config param1Config) {
      AttributeStrategy.Key key = get();
      key.init(param1Int1, param1Int2, param1Config);
      return key;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\AttributeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */