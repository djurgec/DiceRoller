package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public final class Util {
  private static final int HASH_ACCUMULATOR = 17;
  
  private static final int HASH_MULTIPLIER = 31;
  
  private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
  
  private static final char[] SHA_256_CHARS = new char[64];
  
  private static volatile Handler mainThreadHandler;
  
  public static void assertBackgroundThread() {
    if (isOnBackgroundThread())
      return; 
    throw new IllegalArgumentException("You must call this method on a background thread");
  }
  
  public static void assertMainThread() {
    if (isOnMainThread())
      return; 
    throw new IllegalArgumentException("You must call this method on the main thread");
  }
  
  public static boolean bothModelsNullEquivalentOrEquals(Object paramObject1, Object paramObject2) {
    if (paramObject1 == null) {
      boolean bool;
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    return (paramObject1 instanceof Model) ? ((Model)paramObject1).isEquivalentTo(paramObject2) : paramObject1.equals(paramObject2);
  }
  
  public static boolean bothNullOrEqual(Object paramObject1, Object paramObject2) {
    boolean bool;
    if (paramObject1 == null) {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
    } else {
      bool = paramObject1.equals(paramObject2);
    } 
    return bool;
  }
  
  private static String bytesToHex(byte[] paramArrayOfbyte, char[] paramArrayOfchar) {
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      char[] arrayOfChar = HEX_CHAR_ARRAY;
      paramArrayOfchar[b * 2] = arrayOfChar[i >>> 4];
      paramArrayOfchar[b * 2 + 1] = arrayOfChar[i & 0xF];
    } 
    return new String(paramArrayOfchar);
  }
  
  public static <T> Queue<T> createQueue(int paramInt) {
    return new ArrayDeque<>(paramInt);
  }
  
  public static int getBitmapByteSize(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return paramInt1 * paramInt2 * getBytesPerPixel(paramConfig);
  }
  
  public static int getBitmapByteSize(Bitmap paramBitmap) {
    if (!paramBitmap.isRecycled()) {
      if (Build.VERSION.SDK_INT >= 19)
        try {
          return paramBitmap.getAllocationByteCount();
        } catch (NullPointerException nullPointerException) {} 
      return paramBitmap.getHeight() * paramBitmap.getRowBytes();
    } 
    throw new IllegalStateException("Cannot obtain size for recycled Bitmap: " + paramBitmap + "[" + paramBitmap.getWidth() + "x" + paramBitmap.getHeight() + "] " + paramBitmap.getConfig());
  }
  
  private static int getBytesPerPixel(Bitmap.Config paramConfig) {
    Bitmap.Config config = paramConfig;
    if (paramConfig == null)
      config = Bitmap.Config.ARGB_8888; 
    switch (config) {
      default:
        return 4;
      case null:
        return 8;
      case null:
      case null:
        return 2;
      case null:
        break;
    } 
    return 1;
  }
  
  @Deprecated
  public static int getSize(Bitmap paramBitmap) {
    return getBitmapByteSize(paramBitmap);
  }
  
  public static <T> List<T> getSnapshot(Collection<T> paramCollection) {
    ArrayList<Collection<T>> arrayList = new ArrayList(paramCollection.size());
    for (Collection<T> paramCollection : paramCollection) {
      if (paramCollection != null)
        arrayList.add(paramCollection); 
    } 
    return (List)arrayList;
  }
  
  private static Handler getUiThreadHandler() {
    // Byte code:
    //   0: getstatic com/bumptech/glide/util/Util.mainThreadHandler : Landroid/os/Handler;
    //   3: ifnonnull -> 42
    //   6: ldc com/bumptech/glide/util/Util
    //   8: monitorenter
    //   9: getstatic com/bumptech/glide/util/Util.mainThreadHandler : Landroid/os/Handler;
    //   12: ifnonnull -> 30
    //   15: new android/os/Handler
    //   18: astore_0
    //   19: aload_0
    //   20: invokestatic getMainLooper : ()Landroid/os/Looper;
    //   23: invokespecial <init> : (Landroid/os/Looper;)V
    //   26: aload_0
    //   27: putstatic com/bumptech/glide/util/Util.mainThreadHandler : Landroid/os/Handler;
    //   30: ldc com/bumptech/glide/util/Util
    //   32: monitorexit
    //   33: goto -> 42
    //   36: astore_0
    //   37: ldc com/bumptech/glide/util/Util
    //   39: monitorexit
    //   40: aload_0
    //   41: athrow
    //   42: getstatic com/bumptech/glide/util/Util.mainThreadHandler : Landroid/os/Handler;
    //   45: areturn
    // Exception table:
    //   from	to	target	type
    //   9	30	36	finally
    //   30	33	36	finally
    //   37	40	36	finally
  }
  
  public static int hashCode(float paramFloat) {
    return hashCode(paramFloat, 17);
  }
  
  public static int hashCode(float paramFloat, int paramInt) {
    return hashCode(Float.floatToIntBits(paramFloat), paramInt);
  }
  
  public static int hashCode(int paramInt) {
    return hashCode(paramInt, 17);
  }
  
  public static int hashCode(int paramInt1, int paramInt2) {
    return paramInt2 * 31 + paramInt1;
  }
  
  public static int hashCode(Object paramObject, int paramInt) {
    int i;
    if (paramObject == null) {
      i = 0;
    } else {
      i = paramObject.hashCode();
    } 
    return hashCode(i, paramInt);
  }
  
  public static int hashCode(boolean paramBoolean) {
    return hashCode(paramBoolean, 17);
  }
  
  public static int hashCode(boolean paramBoolean, int paramInt) {
    return hashCode(paramBoolean, paramInt);
  }
  
  public static boolean isOnBackgroundThread() {
    return isOnMainThread() ^ true;
  }
  
  public static boolean isOnMainThread() {
    boolean bool;
    if (Looper.myLooper() == Looper.getMainLooper()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isValidDimension(int paramInt) {
    return (paramInt > 0 || paramInt == Integer.MIN_VALUE);
  }
  
  public static boolean isValidDimensions(int paramInt1, int paramInt2) {
    boolean bool;
    if (isValidDimension(paramInt1) && isValidDimension(paramInt2)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static void postOnUiThread(Runnable paramRunnable) {
    getUiThreadHandler().post(paramRunnable);
  }
  
  public static void removeCallbacksOnUiThread(Runnable paramRunnable) {
    getUiThreadHandler().removeCallbacks(paramRunnable);
  }
  
  public static String sha256BytesToHex(byte[] paramArrayOfbyte) {
    synchronized (SHA_256_CHARS) {
      return bytesToHex(paramArrayOfbyte, null);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */