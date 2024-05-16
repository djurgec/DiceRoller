package com.google.android.material.theme.overlay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.view.ContextThemeWrapper;
import com.google.android.material.R;

public class MaterialThemeOverlay {
  private static final int[] ANDROID_THEME_OVERLAY_ATTRS = new int[] { 16842752, R.attr.theme };
  
  private static final int[] MATERIAL_THEME_OVERLAY_ATTR = new int[] { R.attr.materialThemeOverlay };
  
  private static int obtainAndroidThemeOverlayId(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ANDROID_THEME_OVERLAY_ATTRS);
    int i = typedArray.getResourceId(0, 0);
    int j = typedArray.getResourceId(1, 0);
    typedArray.recycle();
    if (i == 0)
      i = j; 
    return i;
  }
  
  private static int obtainMaterialThemeOverlayId(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, MATERIAL_THEME_OVERLAY_ATTR, paramInt1, paramInt2);
    paramInt1 = typedArray.getResourceId(0, 0);
    typedArray.recycle();
    return paramInt1;
  }
  
  public static Context wrap(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    paramInt2 = obtainMaterialThemeOverlayId(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (paramContext instanceof ContextThemeWrapper && ((ContextThemeWrapper)paramContext).getThemeResId() == paramInt2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt2 == 0 || paramInt1 != 0)
      return paramContext; 
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(paramContext, paramInt2);
    paramInt1 = obtainAndroidThemeOverlayId(paramContext, paramAttributeSet);
    if (paramInt1 != 0)
      contextThemeWrapper.getTheme().applyStyle(paramInt1, true); 
    return (Context)contextThemeWrapper;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\theme\overlay\MaterialThemeOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */