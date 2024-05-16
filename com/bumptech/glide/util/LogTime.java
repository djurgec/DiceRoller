package com.bumptech.glide.util;

import android.os.Build;
import android.os.SystemClock;

public final class LogTime {
  private static final double MILLIS_MULTIPLIER;
  
  static {
    int i = Build.VERSION.SDK_INT;
    double d = 1.0D;
    if (i >= 17)
      d = 1.0D / Math.pow(10.0D, 6.0D); 
    MILLIS_MULTIPLIER = d;
  }
  
  public static double getElapsedMillis(long paramLong) {
    return (getLogTime() - paramLong) * MILLIS_MULTIPLIER;
  }
  
  public static long getLogTime() {
    return (Build.VERSION.SDK_INT >= 17) ? SystemClock.elapsedRealtimeNanos() : SystemClock.uptimeMillis();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\LogTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */