package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class LinearProgressIndicator extends BaseProgressIndicator<LinearProgressIndicatorSpec> {
  public static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_LinearProgressIndicator;
  
  public static final int INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS = 0;
  
  public static final int INDETERMINATE_ANIMATION_TYPE_DISJOINT = 1;
  
  public static final int INDICATOR_DIRECTION_END_TO_START = 3;
  
  public static final int INDICATOR_DIRECTION_LEFT_TO_RIGHT = 0;
  
  public static final int INDICATOR_DIRECTION_RIGHT_TO_LEFT = 1;
  
  public static final int INDICATOR_DIRECTION_START_TO_END = 2;
  
  public LinearProgressIndicator(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearProgressIndicator(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.linearProgressIndicatorStyle);
  }
  
  public LinearProgressIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt, DEF_STYLE_RES);
    initializeDrawables();
  }
  
  private void initializeDrawables() {
    setIndeterminateDrawable(IndeterminateDrawable.createLinearDrawable(getContext(), this.spec));
    setProgressDrawable(DeterminateDrawable.createLinearDrawable(getContext(), this.spec));
  }
  
  LinearProgressIndicatorSpec createSpec(Context paramContext, AttributeSet paramAttributeSet) {
    return new LinearProgressIndicatorSpec(paramContext, paramAttributeSet);
  }
  
  public int getIndeterminateAnimationType() {
    return this.spec.indeterminateAnimationType;
  }
  
  public int getIndicatorDirection() {
    return this.spec.indicatorDirection;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    LinearProgressIndicatorSpec linearProgressIndicatorSpec = this.spec;
    paramInt1 = this.spec.indicatorDirection;
    paramBoolean = true;
    if (paramInt1 != 1 && (ViewCompat.getLayoutDirection((View)this) != 1 || this.spec.indicatorDirection != 2) && (ViewCompat.getLayoutDirection((View)this) != 0 || this.spec.indicatorDirection != 3))
      paramBoolean = false; 
    linearProgressIndicatorSpec.drawHorizontallyInverse = paramBoolean;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 -= getPaddingLeft() + getPaddingRight();
    paramInt2 -= getPaddingTop() + getPaddingBottom();
    IndeterminateDrawable<LinearProgressIndicatorSpec> indeterminateDrawable = getIndeterminateDrawable();
    if (indeterminateDrawable != null)
      indeterminateDrawable.setBounds(0, 0, paramInt1, paramInt2); 
    DeterminateDrawable<LinearProgressIndicatorSpec> determinateDrawable = getProgressDrawable();
    if (determinateDrawable != null)
      determinateDrawable.setBounds(0, 0, paramInt1, paramInt2); 
  }
  
  public void setIndeterminateAnimationType(int paramInt) {
    if (this.spec.indeterminateAnimationType == paramInt)
      return; 
    if (!visibleToUser() || !isIndeterminate()) {
      this.spec.indeterminateAnimationType = paramInt;
      this.spec.validateSpec();
      if (paramInt == 0) {
        getIndeterminateDrawable().setAnimatorDelegate(new LinearIndeterminateContiguousAnimatorDelegate(this.spec));
      } else {
        getIndeterminateDrawable().setAnimatorDelegate(new LinearIndeterminateDisjointAnimatorDelegate(getContext(), this.spec));
      } 
      invalidate();
      return;
    } 
    throw new IllegalStateException("Cannot change indeterminate animation type while the progress indicator is show in indeterminate mode.");
  }
  
  public void setIndicatorColor(int... paramVarArgs) {
    super.setIndicatorColor(paramVarArgs);
    this.spec.validateSpec();
  }
  
  public void setIndicatorDirection(int paramInt) {
    this.spec.indicatorDirection = paramInt;
    LinearProgressIndicatorSpec linearProgressIndicatorSpec = this.spec;
    boolean bool = true;
    if (paramInt != 1 && (ViewCompat.getLayoutDirection((View)this) != 1 || this.spec.indicatorDirection != 2) && (ViewCompat.getLayoutDirection((View)this) != 0 || paramInt != 3))
      bool = false; 
    linearProgressIndicatorSpec.drawHorizontallyInverse = bool;
    invalidate();
  }
  
  public void setProgressCompat(int paramInt, boolean paramBoolean) {
    if (this.spec != null && this.spec.indeterminateAnimationType == 0 && isIndeterminate())
      return; 
    super.setProgressCompat(paramInt, paramBoolean);
  }
  
  public void setTrackCornerRadius(int paramInt) {
    super.setTrackCornerRadius(paramInt);
    this.spec.validateSpec();
    invalidate();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IndeterminateAnimationType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IndicatorDirection {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\LinearProgressIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */