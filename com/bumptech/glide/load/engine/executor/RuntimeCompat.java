package com.bumptech.glide.load.engine.executor;

import android.os.Build;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

final class RuntimeCompat {
  private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
  
  private static final String CPU_NAME_REGEX = "cpu[0-9]+";
  
  private static final String TAG = "GlideRuntimeCompat";
  
  static int availableProcessors() {
    int j = Runtime.getRuntime().availableProcessors();
    int i = j;
    if (Build.VERSION.SDK_INT < 17)
      i = Math.max(getCoreCountPre17(), j); 
    return i;
  }
  
  private static int getCoreCountPre17() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: invokestatic allowThreadDiskReads : ()Landroid/os/StrictMode$ThreadPolicy;
    //   5: astore_3
    //   6: new java/io/File
    //   9: astore #5
    //   11: aload #5
    //   13: ldc '/sys/devices/system/cpu/'
    //   15: invokespecial <init> : (Ljava/lang/String;)V
    //   18: ldc 'cpu[0-9]+'
    //   20: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   23: astore_1
    //   24: new com/bumptech/glide/load/engine/executor/RuntimeCompat$1
    //   27: astore #4
    //   29: aload #4
    //   31: aload_1
    //   32: invokespecial <init> : (Ljava/util/regex/Pattern;)V
    //   35: aload #5
    //   37: aload #4
    //   39: invokevirtual listFiles : (Ljava/io/FilenameFilter;)[Ljava/io/File;
    //   42: astore_1
    //   43: goto -> 72
    //   46: astore #4
    //   48: aload_2
    //   49: astore_1
    //   50: ldc 'GlideRuntimeCompat'
    //   52: bipush #6
    //   54: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   57: ifeq -> 72
    //   60: ldc 'GlideRuntimeCompat'
    //   62: ldc 'Failed to calculate accurate cpu count'
    //   64: aload #4
    //   66: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   69: pop
    //   70: aload_2
    //   71: astore_1
    //   72: aload_3
    //   73: invokestatic setThreadPolicy : (Landroid/os/StrictMode$ThreadPolicy;)V
    //   76: aload_1
    //   77: ifnull -> 86
    //   80: aload_1
    //   81: arraylength
    //   82: istore_0
    //   83: goto -> 88
    //   86: iconst_0
    //   87: istore_0
    //   88: iconst_1
    //   89: iload_0
    //   90: invokestatic max : (II)I
    //   93: ireturn
    //   94: astore_1
    //   95: aload_3
    //   96: invokestatic setThreadPolicy : (Landroid/os/StrictMode$ThreadPolicy;)V
    //   99: aload_1
    //   100: athrow
    // Exception table:
    //   from	to	target	type
    //   6	43	46	finally
    //   50	70	94	finally
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\executor\RuntimeCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */