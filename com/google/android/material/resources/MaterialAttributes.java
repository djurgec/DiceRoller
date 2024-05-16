package com.google.android.material.resources;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import com.google.android.material.R;

public class MaterialAttributes {
  public static TypedValue resolve(Context paramContext, int paramInt) {
    TypedValue typedValue = new TypedValue();
    return paramContext.getTheme().resolveAttribute(paramInt, typedValue, true) ? typedValue : null;
  }
  
  public static boolean resolveBoolean(Context paramContext, int paramInt, boolean paramBoolean) {
    TypedValue typedValue = resolve(paramContext, paramInt);
    if (typedValue != null && typedValue.type == 18)
      if (typedValue.data != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }  
    return paramBoolean;
  }
  
  public static boolean resolveBooleanOrThrow(Context paramContext, int paramInt, String paramString) {
    boolean bool;
    if (resolveOrThrow(paramContext, paramInt, paramString) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static int resolveDimension(Context paramContext, int paramInt1, int paramInt2) {
    TypedValue typedValue = resolve(paramContext, paramInt1);
    return (typedValue == null || typedValue.type != 5) ? (int)paramContext.getResources().getDimension(paramInt2) : (int)typedValue.getDimension(paramContext.getResources().getDisplayMetrics());
  }
  
  public static int resolveInteger(Context paramContext, int paramInt1, int paramInt2) {
    TypedValue typedValue = resolve(paramContext, paramInt1);
    if (typedValue != null && typedValue.type == 16) {
      paramInt1 = typedValue.data;
    } else {
      paramInt1 = paramInt2;
    } 
    return paramInt1;
  }
  
  public static int resolveMinimumAccessibleTouchTarget(Context paramContext) {
    return resolveDimension(paramContext, R.attr.minTouchTargetSize, R.dimen.mtrl_min_touch_target_size);
  }
  
  public static int resolveOrThrow(Context paramContext, int paramInt, String paramString) {
    TypedValue typedValue = resolve(paramContext, paramInt);
    if (typedValue != null)
      return typedValue.data; 
    throw new IllegalArgumentException(String.format("%1$s requires a value for the %2$s attribute to be set in your app theme. You can either set the attribute in your theme or update your theme to inherit from Theme.MaterialComponents (or a descendant).", new Object[] { paramString, paramContext.getResources().getResourceName(paramInt) }));
  }
  
  public static int resolveOrThrow(View paramView, int paramInt) {
    return resolveOrThrow(paramView.getContext(), paramInt, paramView.getClass().getCanonicalName());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\resources\MaterialAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */