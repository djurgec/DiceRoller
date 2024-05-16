package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

final class CalendarItemStyle {
  private final ColorStateList backgroundColor;
  
  private final Rect insets;
  
  private final ShapeAppearanceModel itemShape;
  
  private final ColorStateList strokeColor;
  
  private final int strokeWidth;
  
  private final ColorStateList textColor;
  
  private CalendarItemStyle(ColorStateList paramColorStateList1, ColorStateList paramColorStateList2, ColorStateList paramColorStateList3, int paramInt, ShapeAppearanceModel paramShapeAppearanceModel, Rect paramRect) {
    Preconditions.checkArgumentNonnegative(paramRect.left);
    Preconditions.checkArgumentNonnegative(paramRect.top);
    Preconditions.checkArgumentNonnegative(paramRect.right);
    Preconditions.checkArgumentNonnegative(paramRect.bottom);
    this.insets = paramRect;
    this.textColor = paramColorStateList2;
    this.backgroundColor = paramColorStateList1;
    this.strokeColor = paramColorStateList3;
    this.strokeWidth = paramInt;
    this.itemShape = paramShapeAppearanceModel;
  }
  
  static CalendarItemStyle create(Context paramContext, int paramInt) {
    boolean bool;
    if (paramInt != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool, "Cannot create a CalendarItemStyle with a styleResId of 0");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramInt, R.styleable.MaterialCalendarItem);
    Rect rect = new Rect(typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetLeft, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetTop, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetRight, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetBottom, 0));
    ColorStateList colorStateList3 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.MaterialCalendarItem_itemFillColor);
    ColorStateList colorStateList1 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.MaterialCalendarItem_itemTextColor);
    ColorStateList colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.MaterialCalendarItem_itemStrokeColor);
    int j = typedArray.getDimensionPixelSize(R.styleable.MaterialCalendarItem_itemStrokeWidth, 0);
    paramInt = typedArray.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearance, 0);
    int i = typedArray.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearanceOverlay, 0);
    ShapeAppearanceModel shapeAppearanceModel = ShapeAppearanceModel.builder(paramContext, paramInt, i).build();
    typedArray.recycle();
    return new CalendarItemStyle(colorStateList3, colorStateList1, colorStateList2, j, shapeAppearanceModel, rect);
  }
  
  int getBottomInset() {
    return this.insets.bottom;
  }
  
  int getLeftInset() {
    return this.insets.left;
  }
  
  int getRightInset() {
    return this.insets.right;
  }
  
  int getTopInset() {
    return this.insets.top;
  }
  
  void styleItem(TextView paramTextView) {
    RippleDrawable rippleDrawable;
    MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable();
    MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable();
    materialShapeDrawable1.setShapeAppearanceModel(this.itemShape);
    materialShapeDrawable2.setShapeAppearanceModel(this.itemShape);
    materialShapeDrawable1.setFillColor(this.backgroundColor);
    materialShapeDrawable1.setStroke(this.strokeWidth, this.strokeColor);
    paramTextView.setTextColor(this.textColor);
    if (Build.VERSION.SDK_INT >= 21)
      rippleDrawable = new RippleDrawable(this.textColor.withAlpha(30), (Drawable)materialShapeDrawable1, (Drawable)materialShapeDrawable2); 
    ViewCompat.setBackground((View)paramTextView, (Drawable)new InsetDrawable((Drawable)rippleDrawable, this.insets.left, this.insets.top, this.insets.right, this.insets.bottom));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\CalendarItemStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */