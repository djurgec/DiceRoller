package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
  private static final int[] ENABLED_PRESSED_STATE_SET;
  
  private static final int[] FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_STATE_SET;
  
  static final String LOG_TAG;
  
  private static final int[] PRESSED_STATE_SET;
  
  private static final int[] SELECTED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_STATE_SET;
  
  private static final int[] SELECTED_PRESSED_STATE_SET;
  
  private static final int[] SELECTED_STATE_SET;
  
  static final String TRANSPARENT_DEFAULT_COLOR_WARNING = "Use a non-transparent color for the default color as it will be used to finish ripple animations.";
  
  public static final boolean USE_FRAMEWORK_RIPPLE;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_FRAMEWORK_RIPPLE = bool;
    PRESSED_STATE_SET = new int[] { 16842919 };
    HOVERED_FOCUSED_STATE_SET = new int[] { 16843623, 16842908 };
    FOCUSED_STATE_SET = new int[] { 16842908 };
    HOVERED_STATE_SET = new int[] { 16843623 };
    SELECTED_PRESSED_STATE_SET = new int[] { 16842913, 16842919 };
    SELECTED_HOVERED_FOCUSED_STATE_SET = new int[] { 16842913, 16843623, 16842908 };
    SELECTED_FOCUSED_STATE_SET = new int[] { 16842913, 16842908 };
    SELECTED_HOVERED_STATE_SET = new int[] { 16842913, 16843623 };
    SELECTED_STATE_SET = new int[] { 16842913 };
    ENABLED_PRESSED_STATE_SET = new int[] { 16842910, 16842919 };
    LOG_TAG = RippleUtils.class.getSimpleName();
  }
  
  public static ColorStateList convertToRippleDrawableColor(ColorStateList paramColorStateList) {
    if (USE_FRAMEWORK_RIPPLE) {
      int[][] arrayOfInt3 = new int[2][];
      int[] arrayOfInt4 = new int[2];
      arrayOfInt3[0] = SELECTED_STATE_SET;
      arrayOfInt4[0] = getColorForState(paramColorStateList, SELECTED_PRESSED_STATE_SET);
      int j = 0 + 1;
      arrayOfInt3[j] = StateSet.NOTHING;
      arrayOfInt4[j] = getColorForState(paramColorStateList, PRESSED_STATE_SET);
      return new ColorStateList(arrayOfInt3, arrayOfInt4);
    } 
    int[][] arrayOfInt = new int[10][];
    int[] arrayOfInt1 = new int[10];
    int[] arrayOfInt2 = SELECTED_PRESSED_STATE_SET;
    arrayOfInt[0] = arrayOfInt2;
    arrayOfInt1[0] = getColorForState(paramColorStateList, arrayOfInt2);
    int i = 0 + 1;
    arrayOfInt2 = SELECTED_HOVERED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = SELECTED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = SELECTED_HOVERED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    arrayOfInt[++i] = SELECTED_STATE_SET;
    arrayOfInt1[i] = 0;
    i++;
    arrayOfInt2 = PRESSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = HOVERED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = HOVERED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    arrayOfInt[++i] = StateSet.NOTHING;
    arrayOfInt1[i] = 0;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private static int doubleAlpha(int paramInt) {
    return ColorUtils.setAlphaComponent(paramInt, Math.min(Color.alpha(paramInt) * 2, 255));
  }
  
  private static int getColorForState(ColorStateList paramColorStateList, int[] paramArrayOfint) {
    int i;
    if (paramColorStateList != null) {
      i = paramColorStateList.getColorForState(paramArrayOfint, paramColorStateList.getDefaultColor());
    } else {
      i = 0;
    } 
    if (USE_FRAMEWORK_RIPPLE)
      i = doubleAlpha(i); 
    return i;
  }
  
  public static ColorStateList sanitizeRippleDrawableColor(ColorStateList paramColorStateList) {
    if (paramColorStateList != null) {
      if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 27 && Color.alpha(paramColorStateList.getDefaultColor()) == 0 && Color.alpha(paramColorStateList.getColorForState(ENABLED_PRESSED_STATE_SET, 0)) != 0)
        Log.w(LOG_TAG, "Use a non-transparent color for the default color as it will be used to finish ripple animations."); 
      return paramColorStateList;
    } 
    return ColorStateList.valueOf(0);
  }
  
  public static boolean shouldDrawRippleCompat(int[] paramArrayOfint) {
    boolean bool2 = false;
    boolean bool1 = false;
    int i = paramArrayOfint.length;
    boolean bool4 = false;
    byte b = 0;
    while (b < i) {
      boolean bool;
      int j = paramArrayOfint[b];
      if (j == 16842910) {
        bool = true;
      } else if (j == 16842908) {
        bool1 = true;
        bool = bool2;
      } else if (j == 16842919) {
        bool1 = true;
        bool = bool2;
      } else {
        bool = bool2;
        if (j == 16843623) {
          bool1 = true;
          bool = bool2;
        } 
      } 
      b++;
      bool2 = bool;
    } 
    boolean bool3 = bool4;
    if (bool2) {
      bool3 = bool4;
      if (bool1)
        bool3 = true; 
    } 
    return bool3;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\ripple\RippleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */