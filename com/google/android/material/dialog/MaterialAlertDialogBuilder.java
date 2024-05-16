package com.google.android.material.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialAlertDialogBuilder extends AlertDialog.Builder {
  private static final int DEF_STYLE_ATTR = R.attr.alertDialogStyle;
  
  private static final int DEF_STYLE_RES = R.style.MaterialAlertDialog_MaterialComponents;
  
  private static final int MATERIAL_ALERT_DIALOG_THEME_OVERLAY = R.attr.materialAlertDialogTheme;
  
  private Drawable background;
  
  private final Rect backgroundInsets;
  
  public MaterialAlertDialogBuilder(Context paramContext) {
    this(paramContext, 0);
  }
  
  public MaterialAlertDialogBuilder(Context paramContext, int paramInt) {
    super(createMaterialAlertDialogThemedContext(paramContext), getOverridingThemeResId(paramContext, paramInt));
    Context context = getContext();
    Resources.Theme theme = context.getTheme();
    int j = DEF_STYLE_ATTR;
    int i = DEF_STYLE_RES;
    this.backgroundInsets = MaterialDialogs.getDialogBackgroundInsets(context, j, i);
    paramInt = MaterialColors.getColor(context, R.attr.colorSurface, getClass().getCanonicalName());
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, j, i);
    materialShapeDrawable.initializeElevationOverlay(context);
    materialShapeDrawable.setFillColor(ColorStateList.valueOf(paramInt));
    if (Build.VERSION.SDK_INT >= 28) {
      TypedValue typedValue = new TypedValue();
      theme.resolveAttribute(16844145, typedValue, true);
      float f = typedValue.getDimension(getContext().getResources().getDisplayMetrics());
      if (typedValue.type == 5 && f >= 0.0F)
        materialShapeDrawable.setCornerSize(f); 
    } 
    this.background = (Drawable)materialShapeDrawable;
  }
  
  private static Context createMaterialAlertDialogThemedContext(Context paramContext) {
    int i = getMaterialAlertDialogThemeOverlay(paramContext);
    paramContext = MaterialThemeOverlay.wrap(paramContext, null, DEF_STYLE_ATTR, DEF_STYLE_RES);
    return (Context)((i == 0) ? paramContext : new ContextThemeWrapper(paramContext, i));
  }
  
  private static int getMaterialAlertDialogThemeOverlay(Context paramContext) {
    TypedValue typedValue = MaterialAttributes.resolve(paramContext, MATERIAL_ALERT_DIALOG_THEME_OVERLAY);
    return (typedValue == null) ? 0 : typedValue.data;
  }
  
  private static int getOverridingThemeResId(Context paramContext, int paramInt) {
    if (paramInt == 0)
      paramInt = getMaterialAlertDialogThemeOverlay(paramContext); 
    return paramInt;
  }
  
  public AlertDialog create() {
    AlertDialog alertDialog = super.create();
    Window window = alertDialog.getWindow();
    View view = window.getDecorView();
    Drawable drawable = this.background;
    if (drawable instanceof MaterialShapeDrawable)
      ((MaterialShapeDrawable)drawable).setElevation(ViewCompat.getElevation(view)); 
    window.setBackgroundDrawable((Drawable)MaterialDialogs.insetDrawable(this.background, this.backgroundInsets));
    view.setOnTouchListener(new InsetDialogOnTouchListener((Dialog)alertDialog, this.backgroundInsets));
    return alertDialog;
  }
  
  public Drawable getBackground() {
    return this.background;
  }
  
  public MaterialAlertDialogBuilder setAdapter(ListAdapter paramListAdapter, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setAdapter(paramListAdapter, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setBackground(Drawable paramDrawable) {
    this.background = paramDrawable;
    return this;
  }
  
  public MaterialAlertDialogBuilder setBackgroundInsetBottom(int paramInt) {
    this.backgroundInsets.bottom = paramInt;
    return this;
  }
  
  public MaterialAlertDialogBuilder setBackgroundInsetEnd(int paramInt) {
    if (Build.VERSION.SDK_INT >= 17 && getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
      this.backgroundInsets.left = paramInt;
    } else {
      this.backgroundInsets.right = paramInt;
    } 
    return this;
  }
  
  public MaterialAlertDialogBuilder setBackgroundInsetStart(int paramInt) {
    if (Build.VERSION.SDK_INT >= 17 && getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
      this.backgroundInsets.right = paramInt;
    } else {
      this.backgroundInsets.left = paramInt;
    } 
    return this;
  }
  
  public MaterialAlertDialogBuilder setBackgroundInsetTop(int paramInt) {
    this.backgroundInsets.top = paramInt;
    return this;
  }
  
  public MaterialAlertDialogBuilder setCancelable(boolean paramBoolean) {
    return (MaterialAlertDialogBuilder)super.setCancelable(paramBoolean);
  }
  
  public MaterialAlertDialogBuilder setCursor(Cursor paramCursor, DialogInterface.OnClickListener paramOnClickListener, String paramString) {
    return (MaterialAlertDialogBuilder)super.setCursor(paramCursor, paramOnClickListener, paramString);
  }
  
  public MaterialAlertDialogBuilder setCustomTitle(View paramView) {
    return (MaterialAlertDialogBuilder)super.setCustomTitle(paramView);
  }
  
  public MaterialAlertDialogBuilder setIcon(int paramInt) {
    return (MaterialAlertDialogBuilder)super.setIcon(paramInt);
  }
  
  public MaterialAlertDialogBuilder setIcon(Drawable paramDrawable) {
    return (MaterialAlertDialogBuilder)super.setIcon(paramDrawable);
  }
  
  public MaterialAlertDialogBuilder setIconAttribute(int paramInt) {
    return (MaterialAlertDialogBuilder)super.setIconAttribute(paramInt);
  }
  
  public MaterialAlertDialogBuilder setItems(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setItems(paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setItems(CharSequence[] paramArrayOfCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setItems(paramArrayOfCharSequence, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setMessage(int paramInt) {
    return (MaterialAlertDialogBuilder)super.setMessage(paramInt);
  }
  
  public MaterialAlertDialogBuilder setMessage(CharSequence paramCharSequence) {
    return (MaterialAlertDialogBuilder)super.setMessage(paramCharSequence);
  }
  
  public MaterialAlertDialogBuilder setMultiChoiceItems(int paramInt, boolean[] paramArrayOfboolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(paramInt, paramArrayOfboolean, paramOnMultiChoiceClickListener);
  }
  
  public MaterialAlertDialogBuilder setMultiChoiceItems(Cursor paramCursor, String paramString1, String paramString2, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(paramCursor, paramString1, paramString2, paramOnMultiChoiceClickListener);
  }
  
  public MaterialAlertDialogBuilder setMultiChoiceItems(CharSequence[] paramArrayOfCharSequence, boolean[] paramArrayOfboolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(paramArrayOfCharSequence, paramArrayOfboolean, paramOnMultiChoiceClickListener);
  }
  
  public MaterialAlertDialogBuilder setNegativeButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setNegativeButton(paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setNegativeButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setNegativeButton(paramCharSequence, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setNegativeButtonIcon(Drawable paramDrawable) {
    return (MaterialAlertDialogBuilder)super.setNegativeButtonIcon(paramDrawable);
  }
  
  public MaterialAlertDialogBuilder setNeutralButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setNeutralButton(paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setNeutralButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setNeutralButton(paramCharSequence, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setNeutralButtonIcon(Drawable paramDrawable) {
    return (MaterialAlertDialogBuilder)super.setNeutralButtonIcon(paramDrawable);
  }
  
  public MaterialAlertDialogBuilder setOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener) {
    return (MaterialAlertDialogBuilder)super.setOnCancelListener(paramOnCancelListener);
  }
  
  public MaterialAlertDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener) {
    return (MaterialAlertDialogBuilder)super.setOnDismissListener(paramOnDismissListener);
  }
  
  public MaterialAlertDialogBuilder setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener) {
    return (MaterialAlertDialogBuilder)super.setOnItemSelectedListener(paramOnItemSelectedListener);
  }
  
  public MaterialAlertDialogBuilder setOnKeyListener(DialogInterface.OnKeyListener paramOnKeyListener) {
    return (MaterialAlertDialogBuilder)super.setOnKeyListener(paramOnKeyListener);
  }
  
  public MaterialAlertDialogBuilder setPositiveButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setPositiveButton(paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setPositiveButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setPositiveButton(paramCharSequence, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setPositiveButtonIcon(Drawable paramDrawable) {
    return (MaterialAlertDialogBuilder)super.setPositiveButtonIcon(paramDrawable);
  }
  
  public MaterialAlertDialogBuilder setSingleChoiceItems(int paramInt1, int paramInt2, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(paramInt1, paramInt2, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setSingleChoiceItems(Cursor paramCursor, int paramInt, String paramString, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(paramCursor, paramInt, paramString, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setSingleChoiceItems(ListAdapter paramListAdapter, int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(paramListAdapter, paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setSingleChoiceItems(CharSequence[] paramArrayOfCharSequence, int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(paramArrayOfCharSequence, paramInt, paramOnClickListener);
  }
  
  public MaterialAlertDialogBuilder setTitle(int paramInt) {
    return (MaterialAlertDialogBuilder)super.setTitle(paramInt);
  }
  
  public MaterialAlertDialogBuilder setTitle(CharSequence paramCharSequence) {
    return (MaterialAlertDialogBuilder)super.setTitle(paramCharSequence);
  }
  
  public MaterialAlertDialogBuilder setView(int paramInt) {
    return (MaterialAlertDialogBuilder)super.setView(paramInt);
  }
  
  public MaterialAlertDialogBuilder setView(View paramView) {
    return (MaterialAlertDialogBuilder)super.setView(paramView);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\dialog\MaterialAlertDialogBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */