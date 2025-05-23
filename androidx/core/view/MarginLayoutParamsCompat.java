package androidx.core.view;

import android.os.Build;
import android.view.ViewGroup;

public final class MarginLayoutParamsCompat {
  public static int getLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams) {
    boolean bool1;
    if (Build.VERSION.SDK_INT >= 17) {
      bool1 = paramMarginLayoutParams.getLayoutDirection();
    } else {
      bool1 = false;
    } 
    boolean bool2 = bool1;
    if (bool1) {
      bool2 = bool1;
      if (bool1 != true)
        bool2 = false; 
    } 
    return bool2;
  }
  
  public static int getMarginEnd(ViewGroup.MarginLayoutParams paramMarginLayoutParams) {
    return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.getMarginEnd() : paramMarginLayoutParams.rightMargin;
  }
  
  public static int getMarginStart(ViewGroup.MarginLayoutParams paramMarginLayoutParams) {
    return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.getMarginStart() : paramMarginLayoutParams.leftMargin;
  }
  
  public static boolean isMarginRelative(ViewGroup.MarginLayoutParams paramMarginLayoutParams) {
    return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.isMarginRelative() : false;
  }
  
  public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramMarginLayoutParams.resolveLayoutDirection(paramInt); 
  }
  
  public static void setLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramMarginLayoutParams.setLayoutDirection(paramInt); 
  }
  
  public static void setMarginEnd(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramMarginLayoutParams.setMarginEnd(paramInt);
    } else {
      paramMarginLayoutParams.rightMargin = paramInt;
    } 
  }
  
  public static void setMarginStart(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramMarginLayoutParams.setMarginStart(paramInt);
    } else {
      paramMarginLayoutParams.leftMargin = paramInt;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\MarginLayoutParamsCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */