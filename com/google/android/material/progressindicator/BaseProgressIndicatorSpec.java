package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;

public abstract class BaseProgressIndicatorSpec {
  public int hideAnimationBehavior;
  
  public int[] indicatorColors = new int[0];
  
  public int showAnimationBehavior;
  
  public int trackColor;
  
  public int trackCornerRadius;
  
  public int trackThickness;
  
  protected BaseProgressIndicatorSpec(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    int i = paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_progress_track_thickness);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.BaseProgressIndicator, paramInt1, paramInt2, new int[0]);
    this.trackThickness = MaterialResources.getDimensionPixelSize(paramContext, typedArray, R.styleable.BaseProgressIndicator_trackThickness, i);
    this.trackCornerRadius = Math.min(MaterialResources.getDimensionPixelSize(paramContext, typedArray, R.styleable.BaseProgressIndicator_trackCornerRadius, 0), this.trackThickness / 2);
    this.showAnimationBehavior = typedArray.getInt(R.styleable.BaseProgressIndicator_showAnimationBehavior, 0);
    this.hideAnimationBehavior = typedArray.getInt(R.styleable.BaseProgressIndicator_hideAnimationBehavior, 0);
    loadIndicatorColors(paramContext, typedArray);
    loadTrackColor(paramContext, typedArray);
    typedArray.recycle();
  }
  
  private void loadIndicatorColors(Context paramContext, TypedArray paramTypedArray) {
    if (!paramTypedArray.hasValue(R.styleable.BaseProgressIndicator_indicatorColor)) {
      this.indicatorColors = new int[] { MaterialColors.getColor(paramContext, R.attr.colorPrimary, -1) };
      return;
    } 
    if ((paramTypedArray.peekValue(R.styleable.BaseProgressIndicator_indicatorColor)).type != 1) {
      this.indicatorColors = new int[] { paramTypedArray.getColor(R.styleable.BaseProgressIndicator_indicatorColor, -1) };
      return;
    } 
    int[] arrayOfInt = paramContext.getResources().getIntArray(paramTypedArray.getResourceId(R.styleable.BaseProgressIndicator_indicatorColor, -1));
    this.indicatorColors = arrayOfInt;
    if (arrayOfInt.length != 0)
      return; 
    throw new IllegalArgumentException("indicatorColors cannot be empty when indicatorColor is not used.");
  }
  
  private void loadTrackColor(Context paramContext, TypedArray paramTypedArray) {
    if (paramTypedArray.hasValue(R.styleable.BaseProgressIndicator_trackColor)) {
      this.trackColor = paramTypedArray.getColor(R.styleable.BaseProgressIndicator_trackColor, -1);
      return;
    } 
    this.trackColor = this.indicatorColors[0];
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(new int[] { 16842803 });
    float f = typedArray.getFloat(0, 0.2F);
    typedArray.recycle();
    int i = (int)(255.0F * f);
    this.trackColor = MaterialColors.compositeARGBWithAlpha(this.trackColor, i);
  }
  
  public boolean isHideAnimationEnabled() {
    boolean bool;
    if (this.hideAnimationBehavior != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isShowAnimationEnabled() {
    boolean bool;
    if (this.showAnimationBehavior != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  abstract void validateSpec();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\BaseProgressIndicatorSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */