package androidx.core.util;

import android.text.TextUtils;
import java.util.Locale;

public final class Preconditions {
  public static void checkArgument(boolean paramBoolean) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException();
  }
  
  public static void checkArgument(boolean paramBoolean, Object paramObject) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException(String.valueOf(paramObject));
  }
  
  public static void checkArgument(boolean paramBoolean, String paramString, Object... paramVarArgs) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException(String.format(paramString, paramVarArgs));
  }
  
  public static double checkArgumentInRange(double paramDouble1, double paramDouble2, double paramDouble3, String paramString) {
    if (paramDouble1 >= paramDouble2) {
      if (paramDouble1 <= paramDouble3)
        return paramDouble1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[] { paramString, Double.valueOf(paramDouble2), Double.valueOf(paramDouble3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[] { paramString, Double.valueOf(paramDouble2), Double.valueOf(paramDouble3) }));
  }
  
  public static float checkArgumentInRange(float paramFloat1, float paramFloat2, float paramFloat3, String paramString) {
    if (paramFloat1 >= paramFloat2) {
      if (paramFloat1 <= paramFloat3)
        return paramFloat1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[] { paramString, Float.valueOf(paramFloat2), Float.valueOf(paramFloat3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[] { paramString, Float.valueOf(paramFloat2), Float.valueOf(paramFloat3) }));
  }
  
  public static int checkArgumentInRange(int paramInt1, int paramInt2, int paramInt3, String paramString) {
    if (paramInt1 >= paramInt2) {
      if (paramInt1 <= paramInt3)
        return paramInt1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
  }
  
  public static long checkArgumentInRange(long paramLong1, long paramLong2, long paramLong3, String paramString) {
    if (paramLong1 >= paramLong2) {
      if (paramLong1 <= paramLong3)
        return paramLong1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[] { paramString, Long.valueOf(paramLong2), Long.valueOf(paramLong3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[] { paramString, Long.valueOf(paramLong2), Long.valueOf(paramLong3) }));
  }
  
  public static int checkArgumentNonnegative(int paramInt) {
    if (paramInt >= 0)
      return paramInt; 
    throw new IllegalArgumentException();
  }
  
  public static int checkArgumentNonnegative(int paramInt, String paramString) {
    if (paramInt >= 0)
      return paramInt; 
    throw new IllegalArgumentException(paramString);
  }
  
  public static int checkFlagsArgument(int paramInt1, int paramInt2) {
    if ((paramInt1 & paramInt2) == paramInt1)
      return paramInt1; 
    throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(paramInt1) + ", but only 0x" + Integer.toHexString(paramInt2) + " are allowed");
  }
  
  public static <T> T checkNotNull(T paramT) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException();
  }
  
  public static <T> T checkNotNull(T paramT, Object paramObject) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException(String.valueOf(paramObject));
  }
  
  public static void checkState(boolean paramBoolean) {
    checkState(paramBoolean, null);
  }
  
  public static void checkState(boolean paramBoolean, String paramString) {
    if (paramBoolean)
      return; 
    throw new IllegalStateException(paramString);
  }
  
  public static <T extends CharSequence> T checkStringNotEmpty(T paramT) {
    if (!TextUtils.isEmpty((CharSequence)paramT))
      return paramT; 
    throw new IllegalArgumentException();
  }
  
  public static <T extends CharSequence> T checkStringNotEmpty(T paramT, Object paramObject) {
    if (!TextUtils.isEmpty((CharSequence)paramT))
      return paramT; 
    throw new IllegalArgumentException(String.valueOf(paramObject));
  }
  
  public static <T extends CharSequence> T checkStringNotEmpty(T paramT, String paramString, Object... paramVarArgs) {
    if (!TextUtils.isEmpty((CharSequence)paramT))
      return paramT; 
    throw new IllegalArgumentException(String.format(paramString, paramVarArgs));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cor\\util\Preconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */