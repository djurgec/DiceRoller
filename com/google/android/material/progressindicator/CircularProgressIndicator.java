package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CircularProgressIndicator extends BaseProgressIndicator<CircularProgressIndicatorSpec> {
  public static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CircularProgressIndicator;
  
  public static final int INDICATOR_DIRECTION_CLOCKWISE = 0;
  
  public static final int INDICATOR_DIRECTION_COUNTERCLOCKWISE = 1;
  
  public CircularProgressIndicator(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CircularProgressIndicator(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.circularProgressIndicatorStyle);
  }
  
  public CircularProgressIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt, DEF_STYLE_RES);
    initializeDrawables();
  }
  
  private void initializeDrawables() {
    setIndeterminateDrawable(IndeterminateDrawable.createCircularDrawable(getContext(), this.spec));
    setProgressDrawable(DeterminateDrawable.createCircularDrawable(getContext(), this.spec));
  }
  
  CircularProgressIndicatorSpec createSpec(Context paramContext, AttributeSet paramAttributeSet) {
    return new CircularProgressIndicatorSpec(paramContext, paramAttributeSet);
  }
  
  public int getIndicatorDirection() {
    return this.spec.indicatorDirection;
  }
  
  public int getIndicatorInset() {
    return this.spec.indicatorInset;
  }
  
  public int getIndicatorSize() {
    return this.spec.indicatorSize;
  }
  
  public void setIndicatorDirection(int paramInt) {
    this.spec.indicatorDirection = paramInt;
    invalidate();
  }
  
  public void setIndicatorInset(int paramInt) {
    if (this.spec.indicatorInset != paramInt) {
      this.spec.indicatorInset = paramInt;
      invalidate();
    } 
  }
  
  public void setIndicatorSize(int paramInt) {
    paramInt = Math.max(paramInt, getTrackThickness() * 2);
    if (this.spec.indicatorSize != paramInt) {
      this.spec.indicatorSize = paramInt;
      this.spec.validateSpec();
      invalidate();
    } 
  }
  
  public void setTrackThickness(int paramInt) {
    super.setTrackThickness(paramInt);
    this.spec.validateSpec();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IndicatorDirection {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\CircularProgressIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */