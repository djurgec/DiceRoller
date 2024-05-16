package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RelativeCornerSize;

class RadialViewGroup extends ConstraintLayout {
  private static final String SKIP_TAG = "skip";
  
  private MaterialShapeDrawable background;
  
  private int radius;
  
  private final Runnable updateLayoutParametersRunnable;
  
  public RadialViewGroup(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RadialViewGroup(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public RadialViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    LayoutInflater.from(paramContext).inflate(R.layout.material_radial_view_group, (ViewGroup)this);
    ViewCompat.setBackground((View)this, createBackground());
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RadialViewGroup, paramInt, 0);
    this.radius = typedArray.getDimensionPixelSize(R.styleable.RadialViewGroup_materialCircleRadius, 0);
    this.updateLayoutParametersRunnable = new Runnable() {
        final RadialViewGroup this$0;
        
        public void run() {
          RadialViewGroup.this.updateLayoutParams();
        }
      };
    typedArray.recycle();
  }
  
  private Drawable createBackground() {
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
    this.background = materialShapeDrawable;
    materialShapeDrawable.setCornerSize((CornerSize)new RelativeCornerSize(0.5F));
    this.background.setFillColor(ColorStateList.valueOf(-1));
    return (Drawable)this.background;
  }
  
  private static boolean shouldSkipView(View paramView) {
    return "skip".equals(paramView.getTag());
  }
  
  private void updateLayoutParamsAsync() {
    Handler handler = getHandler();
    if (handler != null) {
      handler.removeCallbacks(this.updateLayoutParametersRunnable);
      handler.post(this.updateLayoutParametersRunnable);
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (paramView.getId() == -1)
      paramView.setId(ViewCompat.generateViewId()); 
    updateLayoutParamsAsync();
  }
  
  public int getRadius() {
    return this.radius;
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    updateLayoutParams();
  }
  
  public void onViewRemoved(View paramView) {
    super.onViewRemoved(paramView);
    updateLayoutParamsAsync();
  }
  
  public void setBackgroundColor(int paramInt) {
    this.background.setFillColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setRadius(int paramInt) {
    this.radius = paramInt;
    updateLayoutParams();
  }
  
  protected void updateLayoutParams() {
    int i = 1;
    int j = getChildCount();
    byte b = 0;
    while (b < j) {
      int k = i;
      if (shouldSkipView(getChildAt(b)))
        k = i + 1; 
      b++;
      i = k;
    } 
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(this);
    float f = 0.0F;
    b = 0;
    while (b < j) {
      View view = getChildAt(b);
      float f1 = f;
      if (view.getId() != R.id.circle_center)
        if (shouldSkipView(view)) {
          f1 = f;
        } else {
          constraintSet.constrainCircle(view.getId(), R.id.circle_center, this.radius, f);
          f1 = f + 360.0F / (j - i);
        }  
      b++;
      f = f1;
    } 
    constraintSet.applyTo(this);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\RadialViewGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */