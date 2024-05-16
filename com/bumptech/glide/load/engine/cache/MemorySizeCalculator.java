package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.util.Preconditions;

public final class MemorySizeCalculator {
  static final int BYTES_PER_ARGB_8888_PIXEL = 4;
  
  private static final int LOW_MEMORY_BYTE_ARRAY_POOL_DIVISOR = 2;
  
  private static final String TAG = "MemorySizeCalculator";
  
  private final int arrayPoolSize;
  
  private final int bitmapPoolSize;
  
  private final Context context;
  
  private final int memoryCacheSize;
  
  MemorySizeCalculator(Builder paramBuilder) {
    int i;
    this.context = paramBuilder.context;
    if (isLowMemoryDevice(paramBuilder.activityManager)) {
      i = paramBuilder.arrayPoolSizeBytes / 2;
    } else {
      i = paramBuilder.arrayPoolSizeBytes;
    } 
    this.arrayPoolSize = i;
    int k = getMaxSize(paramBuilder.activityManager, paramBuilder.maxSizeMultiplier, paramBuilder.lowMemoryMaxSizeMultiplier);
    int m = paramBuilder.screenDimensions.getWidthPixels() * paramBuilder.screenDimensions.getHeightPixels() * 4;
    int j = Math.round(m * paramBuilder.bitmapPoolScreens);
    m = Math.round(m * paramBuilder.memoryCacheScreens);
    int n = k - i;
    if (m + j <= n) {
      this.memoryCacheSize = m;
      this.bitmapPoolSize = j;
    } else {
      float f = n / (paramBuilder.bitmapPoolScreens + paramBuilder.memoryCacheScreens);
      this.memoryCacheSize = Math.round(paramBuilder.memoryCacheScreens * f);
      this.bitmapPoolSize = Math.round(paramBuilder.bitmapPoolScreens * f);
    } 
    if (Log.isLoggable("MemorySizeCalculator", 3)) {
      boolean bool;
      StringBuilder stringBuilder = (new StringBuilder()).append("Calculation complete, Calculated memory cache size: ").append(toMb(this.memoryCacheSize)).append(", pool size: ").append(toMb(this.bitmapPoolSize)).append(", byte array size: ").append(toMb(i)).append(", memory class limited? ");
      if (m + j > k) {
        bool = true;
      } else {
        bool = false;
      } 
      Log.d("MemorySizeCalculator", stringBuilder.append(bool).append(", max size: ").append(toMb(k)).append(", memoryClass: ").append(paramBuilder.activityManager.getMemoryClass()).append(", isLowMemoryDevice: ").append(isLowMemoryDevice(paramBuilder.activityManager)).toString());
    } 
  }
  
  private static int getMaxSize(ActivityManager paramActivityManager, float paramFloat1, float paramFloat2) {
    int i = paramActivityManager.getMemoryClass();
    boolean bool = isLowMemoryDevice(paramActivityManager);
    float f = (i * 1024 * 1024);
    if (bool)
      paramFloat1 = paramFloat2; 
    return Math.round(f * paramFloat1);
  }
  
  static boolean isLowMemoryDevice(ActivityManager paramActivityManager) {
    return (Build.VERSION.SDK_INT >= 19) ? paramActivityManager.isLowRamDevice() : true;
  }
  
  private String toMb(int paramInt) {
    return Formatter.formatFileSize(this.context, paramInt);
  }
  
  public int getArrayPoolSizeInBytes() {
    return this.arrayPoolSize;
  }
  
  public int getBitmapPoolSize() {
    return this.bitmapPoolSize;
  }
  
  public int getMemoryCacheSize() {
    return this.memoryCacheSize;
  }
  
  public static final class Builder {
    static final int ARRAY_POOL_SIZE_BYTES = 4194304;
    
    static final int BITMAP_POOL_TARGET_SCREENS;
    
    static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33F;
    
    static final float MAX_SIZE_MULTIPLIER = 0.4F;
    
    static final int MEMORY_CACHE_TARGET_SCREENS = 2;
    
    ActivityManager activityManager;
    
    int arrayPoolSizeBytes = 4194304;
    
    float bitmapPoolScreens = BITMAP_POOL_TARGET_SCREENS;
    
    final Context context;
    
    float lowMemoryMaxSizeMultiplier = 0.33F;
    
    float maxSizeMultiplier = 0.4F;
    
    float memoryCacheScreens = 2.0F;
    
    MemorySizeCalculator.ScreenDimensions screenDimensions;
    
    static {
      boolean bool;
      if (Build.VERSION.SDK_INT < 26) {
        bool = true;
      } else {
        bool = true;
      } 
      BITMAP_POOL_TARGET_SCREENS = bool;
    }
    
    public Builder(Context param1Context) {
      this.context = param1Context;
      this.activityManager = (ActivityManager)param1Context.getSystemService("activity");
      this.screenDimensions = new MemorySizeCalculator.DisplayMetricsScreenDimensions(param1Context.getResources().getDisplayMetrics());
      if (Build.VERSION.SDK_INT >= 26 && MemorySizeCalculator.isLowMemoryDevice(this.activityManager))
        this.bitmapPoolScreens = 0.0F; 
    }
    
    public MemorySizeCalculator build() {
      return new MemorySizeCalculator(this);
    }
    
    Builder setActivityManager(ActivityManager param1ActivityManager) {
      this.activityManager = param1ActivityManager;
      return this;
    }
    
    public Builder setArrayPoolSize(int param1Int) {
      this.arrayPoolSizeBytes = param1Int;
      return this;
    }
    
    public Builder setBitmapPoolScreens(float param1Float) {
      boolean bool;
      if (param1Float >= 0.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "Bitmap pool screens must be greater than or equal to 0");
      this.bitmapPoolScreens = param1Float;
      return this;
    }
    
    public Builder setLowMemoryMaxSizeMultiplier(float param1Float) {
      boolean bool;
      if (param1Float >= 0.0F && param1Float <= 1.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "Low memory max size multiplier must be between 0 and 1");
      this.lowMemoryMaxSizeMultiplier = param1Float;
      return this;
    }
    
    public Builder setMaxSizeMultiplier(float param1Float) {
      boolean bool;
      if (param1Float >= 0.0F && param1Float <= 1.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "Size multiplier must be between 0 and 1");
      this.maxSizeMultiplier = param1Float;
      return this;
    }
    
    public Builder setMemoryCacheScreens(float param1Float) {
      boolean bool;
      if (param1Float >= 0.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "Memory cache screens must be greater than or equal to 0");
      this.memoryCacheScreens = param1Float;
      return this;
    }
    
    Builder setScreenDimensions(MemorySizeCalculator.ScreenDimensions param1ScreenDimensions) {
      this.screenDimensions = param1ScreenDimensions;
      return this;
    }
  }
  
  private static final class DisplayMetricsScreenDimensions implements ScreenDimensions {
    private final DisplayMetrics displayMetrics;
    
    DisplayMetricsScreenDimensions(DisplayMetrics param1DisplayMetrics) {
      this.displayMetrics = param1DisplayMetrics;
    }
    
    public int getHeightPixels() {
      return this.displayMetrics.heightPixels;
    }
    
    public int getWidthPixels() {
      return this.displayMetrics.widthPixels;
    }
  }
  
  static interface ScreenDimensions {
    int getHeightPixels();
    
    int getWidthPixels();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\MemorySizeCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */