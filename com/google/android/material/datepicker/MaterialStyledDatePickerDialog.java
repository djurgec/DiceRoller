package com.google.android.material.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.R;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MaterialStyledDatePickerDialog extends DatePickerDialog {
  private static final int DEF_STYLE_ATTR = 16843612;
  
  private static final int DEF_STYLE_RES = R.style.MaterialAlertDialog_MaterialComponents_Picker_Date_Spinner;
  
  private final Drawable background;
  
  private final Rect backgroundInsets;
  
  public MaterialStyledDatePickerDialog(Context paramContext) {
    this(paramContext, 0);
  }
  
  public MaterialStyledDatePickerDialog(Context paramContext, int paramInt) {
    this(paramContext, paramInt, (DatePickerDialog.OnDateSetListener)null, -1, -1, -1);
  }
  
  public MaterialStyledDatePickerDialog(Context paramContext, int paramInt1, DatePickerDialog.OnDateSetListener paramOnDateSetListener, int paramInt2, int paramInt3, int paramInt4) {
    super(paramContext, paramInt1, paramOnDateSetListener, paramInt2, paramInt3, paramInt4);
    Context context = getContext();
    paramInt2 = MaterialAttributes.resolveOrThrow(getContext(), R.attr.colorSurface, getClass().getCanonicalName());
    paramInt1 = DEF_STYLE_RES;
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, 16843612, paramInt1);
    if (Build.VERSION.SDK_INT >= 21) {
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(paramInt2));
    } else {
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(0));
    } 
    Rect rect = MaterialDialogs.getDialogBackgroundInsets(context, 16843612, paramInt1);
    this.backgroundInsets = rect;
    this.background = (Drawable)MaterialDialogs.insetDrawable((Drawable)materialShapeDrawable, rect);
  }
  
  public MaterialStyledDatePickerDialog(Context paramContext, DatePickerDialog.OnDateSetListener paramOnDateSetListener, int paramInt1, int paramInt2, int paramInt3) {
    this(paramContext, 0, paramOnDateSetListener, paramInt1, paramInt2, paramInt3);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(this.background);
    getWindow().getDecorView().setOnTouchListener((View.OnTouchListener)new InsetDialogOnTouchListener((Dialog)this, this.backgroundInsets));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MaterialStyledDatePickerDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */