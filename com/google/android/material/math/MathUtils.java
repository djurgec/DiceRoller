package com.google.android.material.math;

public final class MathUtils {
  public static final float DEFAULT_EPSILON = 1.0E-4F;
  
  public static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return (float)Math.hypot((paramFloat3 - paramFloat1), (paramFloat4 - paramFloat2));
  }
  
  public static float distanceToFurthestCorner(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    return max(dist(paramFloat1, paramFloat2, paramFloat3, paramFloat4), dist(paramFloat1, paramFloat2, paramFloat5, paramFloat4), dist(paramFloat1, paramFloat2, paramFloat5, paramFloat6), dist(paramFloat1, paramFloat2, paramFloat3, paramFloat6));
  }
  
  public static float floorMod(float paramFloat, int paramInt) {
    int j = (int)(paramFloat / paramInt);
    int i = j;
    if (Math.signum(paramFloat) * paramInt < 0.0F) {
      i = j;
      if ((j * paramInt) != paramFloat)
        i = j - 1; 
    } 
    return paramFloat - (i * paramInt);
  }
  
  public static int floorMod(int paramInt1, int paramInt2) {
    int j = paramInt1 / paramInt2;
    int i = j;
    if ((paramInt1 ^ paramInt2) < 0) {
      i = j;
      if (j * paramInt2 != paramInt1)
        i = j - 1; 
    } 
    return paramInt1 - i * paramInt2;
  }
  
  public static boolean geq(float paramFloat1, float paramFloat2, float paramFloat3) {
    boolean bool;
    if (paramFloat1 + paramFloat3 >= paramFloat2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (1.0F - paramFloat3) * paramFloat1 + paramFloat3 * paramFloat2;
  }
  
  private static float max(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (paramFloat1 <= paramFloat2 || paramFloat1 <= paramFloat3 || paramFloat1 <= paramFloat4)
      if (paramFloat2 > paramFloat3 && paramFloat2 > paramFloat4) {
        paramFloat1 = paramFloat2;
      } else if (paramFloat3 > paramFloat4) {
        paramFloat1 = paramFloat3;
      } else {
        paramFloat1 = paramFloat4;
      }  
    return paramFloat1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\math\MathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */