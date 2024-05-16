package com.google.android.material.progressindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.color.MaterialColors;

public final class DeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {
  private static final FloatPropertyCompat<DeterminateDrawable> INDICATOR_LENGTH_IN_LEVEL = new FloatPropertyCompat<DeterminateDrawable>("indicatorLevel") {
      public float getValue(DeterminateDrawable param1DeterminateDrawable) {
        return param1DeterminateDrawable.getIndicatorFraction() * 10000.0F;
      }
      
      public void setValue(DeterminateDrawable param1DeterminateDrawable, float param1Float) {
        param1DeterminateDrawable.setIndicatorFraction(param1Float / 10000.0F);
      }
    };
  
  private static final int MAX_DRAWABLE_LEVEL = 10000;
  
  private static final float SPRING_FORCE_STIFFNESS = 50.0F;
  
  private DrawingDelegate<S> drawingDelegate;
  
  private float indicatorFraction;
  
  private boolean skipAnimationOnLevelChange = false;
  
  private final SpringAnimation springAnimator;
  
  private final SpringForce springForce;
  
  DeterminateDrawable(Context paramContext, BaseProgressIndicatorSpec paramBaseProgressIndicatorSpec, DrawingDelegate<S> paramDrawingDelegate) {
    super(paramContext, paramBaseProgressIndicatorSpec);
    setDrawingDelegate(paramDrawingDelegate);
    SpringForce springForce = new SpringForce();
    this.springForce = springForce;
    springForce.setDampingRatio(1.0F);
    springForce.setStiffness(50.0F);
    SpringAnimation springAnimation = new SpringAnimation(this, INDICATOR_LENGTH_IN_LEVEL);
    this.springAnimator = springAnimation;
    springAnimation.setSpring(springForce);
    setGrowFraction(1.0F);
  }
  
  public static DeterminateDrawable<CircularProgressIndicatorSpec> createCircularDrawable(Context paramContext, CircularProgressIndicatorSpec paramCircularProgressIndicatorSpec) {
    return new DeterminateDrawable<>(paramContext, paramCircularProgressIndicatorSpec, new CircularDrawingDelegate(paramCircularProgressIndicatorSpec));
  }
  
  public static DeterminateDrawable<LinearProgressIndicatorSpec> createLinearDrawable(Context paramContext, LinearProgressIndicatorSpec paramLinearProgressIndicatorSpec) {
    return new DeterminateDrawable<>(paramContext, paramLinearProgressIndicatorSpec, new LinearDrawingDelegate(paramLinearProgressIndicatorSpec));
  }
  
  private float getIndicatorFraction() {
    return this.indicatorFraction;
  }
  
  private void setIndicatorFraction(float paramFloat) {
    this.indicatorFraction = paramFloat;
    invalidateSelf();
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect = new Rect();
    if (getBounds().isEmpty() || !isVisible() || !paramCanvas.getClipBounds(rect))
      return; 
    paramCanvas.save();
    this.drawingDelegate.validateSpecAndAdjustCanvas(paramCanvas, getGrowFraction());
    this.drawingDelegate.fillTrack(paramCanvas, this.paint);
    int i = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], getAlpha());
    this.drawingDelegate.fillIndicator(paramCanvas, this.paint, 0.0F, getIndicatorFraction(), i);
    paramCanvas.restore();
  }
  
  DrawingDelegate<S> getDrawingDelegate() {
    return this.drawingDelegate;
  }
  
  public int getIntrinsicHeight() {
    return this.drawingDelegate.getPreferredHeight();
  }
  
  public int getIntrinsicWidth() {
    return this.drawingDelegate.getPreferredWidth();
  }
  
  public void jumpToCurrentState() {
    this.springAnimator.cancel();
    setIndicatorFraction(getLevel() / 10000.0F);
  }
  
  protected boolean onLevelChange(int paramInt) {
    if (this.skipAnimationOnLevelChange) {
      this.springAnimator.cancel();
      setIndicatorFraction(paramInt / 10000.0F);
    } else {
      this.springAnimator.setStartValue(getIndicatorFraction() * 10000.0F);
      this.springAnimator.animateToFinalPosition(paramInt);
    } 
    return true;
  }
  
  void setDrawingDelegate(DrawingDelegate<S> paramDrawingDelegate) {
    this.drawingDelegate = paramDrawingDelegate;
    paramDrawingDelegate.registerDrawable(this);
  }
  
  void setLevelByFraction(float paramFloat) {
    setLevel((int)(10000.0F * paramFloat));
  }
  
  boolean setVisibleInternal(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    paramBoolean1 = super.setVisibleInternal(paramBoolean1, paramBoolean2, paramBoolean3);
    float f = this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(this.context.getContentResolver());
    if (f == 0.0F) {
      this.skipAnimationOnLevelChange = true;
    } else {
      this.skipAnimationOnLevelChange = false;
      this.springForce.setStiffness(50.0F / f);
    } 
    return paramBoolean1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\DeterminateDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */