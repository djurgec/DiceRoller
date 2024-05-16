package com.google.android.material.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

public class MaterialDialogs {
  public static Rect getDialogBackgroundInsets(Context paramContext, int paramInt1, int paramInt2) {
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, null, R.styleable.MaterialAlertDialog, paramInt1, paramInt2, new int[0]);
    paramInt2 = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetStart, paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_start));
    int i1 = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetTop, paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_top));
    int k = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetEnd, paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_end));
    int n = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetBottom, paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_bottom));
    typedArray.recycle();
    int j = paramInt2;
    int m = k;
    int i = j;
    paramInt1 = m;
    if (Build.VERSION.SDK_INT >= 17) {
      i = j;
      paramInt1 = m;
      if (paramContext.getResources().getConfiguration().getLayoutDirection() == 1) {
        i = k;
        paramInt1 = paramInt2;
      } 
    } 
    return new Rect(i, i1, paramInt1, n);
  }
  
  public static InsetDrawable insetDrawable(Drawable paramDrawable, Rect paramRect) {
    return new InsetDrawable(paramDrawable, paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\dialog\MaterialDialogs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */