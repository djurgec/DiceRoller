package com.google.android.material.color;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;

public class MaterialColors {
  public static final float ALPHA_DISABLED = 0.38F;
  
  public static final float ALPHA_DISABLED_LOW = 0.12F;
  
  public static final float ALPHA_FULL = 1.0F;
  
  public static final float ALPHA_LOW = 0.32F;
  
  public static final float ALPHA_MEDIUM = 0.54F;
  
  public static int compositeARGBWithAlpha(int paramInt1, int paramInt2) {
    return ColorUtils.setAlphaComponent(paramInt1, Color.alpha(paramInt1) * paramInt2 / 255);
  }
  
  public static int getColor(Context paramContext, int paramInt1, int paramInt2) {
    TypedValue typedValue = MaterialAttributes.resolve(paramContext, paramInt1);
    return (typedValue != null) ? typedValue.data : paramInt2;
  }
  
  public static int getColor(Context paramContext, int paramInt, String paramString) {
    return MaterialAttributes.resolveOrThrow(paramContext, paramInt, paramString);
  }
  
  public static int getColor(View paramView, int paramInt) {
    return MaterialAttributes.resolveOrThrow(paramView, paramInt);
  }
  
  public static int getColor(View paramView, int paramInt1, int paramInt2) {
    return getColor(paramView.getContext(), paramInt1, paramInt2);
  }
  
  public static boolean isColorLight(int paramInt) {
    boolean bool;
    if (paramInt != 0 && ColorUtils.calculateLuminance(paramInt) > 0.5D) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static int layer(int paramInt1, int paramInt2) {
    return ColorUtils.compositeColors(paramInt2, paramInt1);
  }
  
  public static int layer(int paramInt1, int paramInt2, float paramFloat) {
    return layer(paramInt1, ColorUtils.setAlphaComponent(paramInt2, Math.round(Color.alpha(paramInt2) * paramFloat)));
  }
  
  public static int layer(View paramView, int paramInt1, int paramInt2) {
    return layer(paramView, paramInt1, paramInt2, 1.0F);
  }
  
  public static int layer(View paramView, int paramInt1, int paramInt2, float paramFloat) {
    return layer(getColor(paramView, paramInt1), getColor(paramView, paramInt2), paramFloat);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\color\MaterialColors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */