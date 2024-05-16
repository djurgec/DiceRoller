package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TintTypedArray;

public class MaterialResources {
  private static final float FONT_SCALE_1_3 = 1.3F;
  
  private static final float FONT_SCALE_2_0 = 2.0F;
  
  public static ColorStateList getColorStateList(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      int i = paramTypedArray.getResourceId(paramInt, 0);
      if (i != 0) {
        ColorStateList colorStateList = AppCompatResources.getColorStateList(paramContext, i);
        if (colorStateList != null)
          return colorStateList; 
      } 
    } 
    if (Build.VERSION.SDK_INT <= 15) {
      int i = paramTypedArray.getColor(paramInt, -1);
      if (i != -1)
        return ColorStateList.valueOf(i); 
    } 
    return paramTypedArray.getColorStateList(paramInt);
  }
  
  public static ColorStateList getColorStateList(Context paramContext, TintTypedArray paramTintTypedArray, int paramInt) {
    if (paramTintTypedArray.hasValue(paramInt)) {
      int i = paramTintTypedArray.getResourceId(paramInt, 0);
      if (i != 0) {
        ColorStateList colorStateList = AppCompatResources.getColorStateList(paramContext, i);
        if (colorStateList != null)
          return colorStateList; 
      } 
    } 
    if (Build.VERSION.SDK_INT <= 15) {
      int i = paramTintTypedArray.getColor(paramInt, -1);
      if (i != -1)
        return ColorStateList.valueOf(i); 
    } 
    return paramTintTypedArray.getColorStateList(paramInt);
  }
  
  public static int getDimensionPixelSize(Context paramContext, TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    TypedValue typedValue = new TypedValue();
    if (!paramTypedArray.getValue(paramInt1, typedValue) || typedValue.type != 2)
      return paramTypedArray.getDimensionPixelSize(paramInt1, paramInt2); 
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(new int[] { typedValue.data });
    paramInt1 = typedArray.getDimensionPixelSize(0, paramInt2);
    typedArray.recycle();
    return paramInt1;
  }
  
  public static Drawable getDrawable(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      int i = paramTypedArray.getResourceId(paramInt, 0);
      if (i != 0) {
        Drawable drawable = AppCompatResources.getDrawable(paramContext, i);
        if (drawable != null)
          return drawable; 
      } 
    } 
    return paramTypedArray.getDrawable(paramInt);
  }
  
  static int getIndexWithValue(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    return paramTypedArray.hasValue(paramInt1) ? paramInt1 : paramInt2;
  }
  
  public static TextAppearance getTextAppearance(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      paramInt = paramTypedArray.getResourceId(paramInt, 0);
      if (paramInt != 0)
        return new TextAppearance(paramContext, paramInt); 
    } 
    return null;
  }
  
  public static boolean isFontScaleAtLeast1_3(Context paramContext) {
    boolean bool;
    if ((paramContext.getResources().getConfiguration()).fontScale >= 1.3F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isFontScaleAtLeast2_0(Context paramContext) {
    boolean bool;
    if ((paramContext.getResources().getConfiguration()).fontScale >= 2.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\resources\MaterialResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */