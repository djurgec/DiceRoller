package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class HardwareConfigState {
  public static final boolean BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED;
  
  private static final File FD_SIZE_LIST;
  
  public static final boolean HARDWARE_BITMAPS_SUPPORTED;
  
  private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_O = 700;
  
  private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P = 20000;
  
  private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
  
  static final int MIN_HARDWARE_DIMENSION_O = 128;
  
  private static final int MIN_HARDWARE_DIMENSION_P = 0;
  
  public static final int NO_MAX_FD_COUNT = -1;
  
  private static final String TAG = "HardwareConfig";
  
  private static volatile HardwareConfigState instance;
  
  private static volatile int manualOverrideMaxFdCount;
  
  private int decodesSinceLastFdCheck;
  
  private boolean isFdSizeBelowHardwareLimit = true;
  
  private final AtomicBoolean isHardwareConfigAllowedByAppState = new AtomicBoolean(false);
  
  private final boolean isHardwareConfigAllowedByDeviceModel = isHardwareConfigAllowedByDeviceModel();
  
  private final int minHardwareDimension;
  
  private final int sdkBasedMaxFdCount;
  
  static {
    boolean bool1;
    int i = Build.VERSION.SDK_INT;
    boolean bool2 = true;
    if (i < 29) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED = bool1;
    if (Build.VERSION.SDK_INT >= 26) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    HARDWARE_BITMAPS_SUPPORTED = bool1;
    FD_SIZE_LIST = new File("/proc/self/fd");
    manualOverrideMaxFdCount = -1;
  }
  
  HardwareConfigState() {
    if (Build.VERSION.SDK_INT >= 28) {
      this.sdkBasedMaxFdCount = 20000;
      this.minHardwareDimension = 0;
    } else {
      this.sdkBasedMaxFdCount = 700;
      this.minHardwareDimension = 128;
    } 
  }
  
  private boolean areHardwareBitmapsBlockedByAppState() {
    boolean bool;
    if (BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED && !this.isHardwareConfigAllowedByAppState.get()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static HardwareConfigState getInstance() {
    // Byte code:
    //   0: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   3: ifnonnull -> 39
    //   6: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   8: monitorenter
    //   9: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   12: ifnonnull -> 27
    //   15: new com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   27: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   29: monitorexit
    //   30: goto -> 39
    //   33: astore_0
    //   34: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   36: monitorexit
    //   37: aload_0
    //   38: athrow
    //   39: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   42: areturn
    // Exception table:
    //   from	to	target	type
    //   9	27	33	finally
    //   27	30	33	finally
    //   34	37	33	finally
  }
  
  private int getMaxFdCount() {
    int i;
    if (manualOverrideMaxFdCount != -1) {
      i = manualOverrideMaxFdCount;
    } else {
      i = this.sdkBasedMaxFdCount;
    } 
    return i;
  }
  
  private boolean isFdSizeBelowHardwareLimit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield decodesSinceLastFdCheck : I
    //   6: istore_1
    //   7: iconst_1
    //   8: istore_2
    //   9: iinc #1, 1
    //   12: aload_0
    //   13: iload_1
    //   14: putfield decodesSinceLastFdCheck : I
    //   17: iload_1
    //   18: bipush #50
    //   20: if_icmplt -> 111
    //   23: aload_0
    //   24: iconst_0
    //   25: putfield decodesSinceLastFdCheck : I
    //   28: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.FD_SIZE_LIST : Ljava/io/File;
    //   31: invokevirtual list : ()[Ljava/lang/String;
    //   34: arraylength
    //   35: istore_1
    //   36: aload_0
    //   37: invokespecial getMaxFdCount : ()I
    //   40: i2l
    //   41: lstore_3
    //   42: iload_1
    //   43: i2l
    //   44: lload_3
    //   45: lcmp
    //   46: ifge -> 52
    //   49: goto -> 54
    //   52: iconst_0
    //   53: istore_2
    //   54: aload_0
    //   55: iload_2
    //   56: putfield isFdSizeBelowHardwareLimit : Z
    //   59: iload_2
    //   60: ifne -> 111
    //   63: ldc 'Downsampler'
    //   65: iconst_5
    //   66: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   69: ifeq -> 111
    //   72: new java/lang/StringBuilder
    //   75: astore #5
    //   77: aload #5
    //   79: invokespecial <init> : ()V
    //   82: ldc 'Downsampler'
    //   84: aload #5
    //   86: ldc 'Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors '
    //   88: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: iload_1
    //   92: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   95: ldc ', limit '
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: lload_3
    //   101: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   110: pop
    //   111: aload_0
    //   112: getfield isFdSizeBelowHardwareLimit : Z
    //   115: istore_2
    //   116: aload_0
    //   117: monitorexit
    //   118: iload_2
    //   119: ireturn
    //   120: astore #5
    //   122: aload_0
    //   123: monitorexit
    //   124: aload #5
    //   126: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	120	finally
    //   12	17	120	finally
    //   23	42	120	finally
    //   54	59	120	finally
    //   63	111	120	finally
    //   111	116	120	finally
  }
  
  private static boolean isHardwareConfigAllowedByDeviceModel() {
    boolean bool;
    if (!isHardwareConfigDisallowedByB112551574() && !isHardwareConfigDisallowedByB147430447()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isHardwareConfigDisallowedByB112551574() {
    if (Build.VERSION.SDK_INT != 26)
      return false; 
    for (String str : Arrays.<String>asList(new String[] { 
          "SC-04J", "SM-N935", "SM-J720", "SM-G570F", "SM-G570M", "SM-G960", "SM-G965", "SM-G935", "SM-G930", "SM-A520", 
          "SM-A720F", "moto e5", "moto e5 play", "moto e5 plus", "moto e5 cruise", "moto g(6) forge", "moto g(6) play" })) {
      if (Build.MODEL.startsWith(str))
        return true; 
    } 
    return false;
  }
  
  private static boolean isHardwareConfigDisallowedByB147430447() {
    return (Build.VERSION.SDK_INT != 27) ? false : Arrays.<String>asList(new String[] { 
          "LG-M250", "LG-M320", "LG-Q710AL", "LG-Q710PL", "LGM-K121K", "LGM-K121L", "LGM-K121S", "LGM-X320K", "LGM-X320L", "LGM-X320S", 
          "LGM-X401L", "LGM-X401S", "LM-Q610.FG", "LM-Q610.FGN", "LM-Q617.FG", "LM-Q617.FGN", "LM-Q710.FG", "LM-Q710.FGN", "LM-X220PM", "LM-X220QMA", 
          "LM-X410PM" }).contains(Build.MODEL);
  }
  
  public boolean areHardwareBitmapsBlocked() {
    Util.assertMainThread();
    return this.isHardwareConfigAllowedByAppState.get() ^ true;
  }
  
  public void blockHardwareBitmaps() {
    Util.assertMainThread();
    this.isHardwareConfigAllowedByAppState.set(false);
  }
  
  public boolean isHardwareConfigAllowed(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    if (!paramBoolean1) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed by caller"); 
      return false;
    } 
    if (!this.isHardwareConfigAllowedByDeviceModel) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed by device model"); 
      return false;
    } 
    if (!HARDWARE_BITMAPS_SUPPORTED) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed by sdk"); 
      return false;
    } 
    if (areHardwareBitmapsBlockedByAppState()) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed by app state"); 
      return false;
    } 
    if (paramBoolean2) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed because exif orientation is required"); 
      return false;
    } 
    int i = this.minHardwareDimension;
    if (paramInt1 < i) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed because width is too small"); 
      return false;
    } 
    if (paramInt2 < i) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed because height is too small"); 
      return false;
    } 
    if (!isFdSizeBelowHardwareLimit()) {
      if (Log.isLoggable("HardwareConfig", 2))
        Log.v("HardwareConfig", "Hardware config disallowed because there are insufficient FDs"); 
      return false;
    } 
    return true;
  }
  
  boolean setHardwareConfigIfAllowed(int paramInt1, int paramInt2, BitmapFactory.Options paramOptions, boolean paramBoolean1, boolean paramBoolean2) {
    paramBoolean1 = isHardwareConfigAllowed(paramInt1, paramInt2, paramBoolean1, paramBoolean2);
    if (paramBoolean1) {
      paramOptions.inPreferredConfig = Bitmap.Config.HARDWARE;
      paramOptions.inMutable = false;
    } 
    return paramBoolean1;
  }
  
  public void unblockHardwareBitmaps() {
    Util.assertMainThread();
    this.isHardwareConfigAllowedByAppState.set(true);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\HardwareConfigState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */