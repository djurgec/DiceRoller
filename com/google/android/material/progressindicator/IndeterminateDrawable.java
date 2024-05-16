package com.google.android.material.progressindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.os.Build;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

public final class IndeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {
  private IndeterminateAnimatorDelegate<ObjectAnimator> animatorDelegate;
  
  private DrawingDelegate<S> drawingDelegate;
  
  IndeterminateDrawable(Context paramContext, BaseProgressIndicatorSpec paramBaseProgressIndicatorSpec, DrawingDelegate<S> paramDrawingDelegate, IndeterminateAnimatorDelegate<ObjectAnimator> paramIndeterminateAnimatorDelegate) {
    super(paramContext, paramBaseProgressIndicatorSpec);
    setDrawingDelegate(paramDrawingDelegate);
    setAnimatorDelegate(paramIndeterminateAnimatorDelegate);
  }
  
  public static IndeterminateDrawable<CircularProgressIndicatorSpec> createCircularDrawable(Context paramContext, CircularProgressIndicatorSpec paramCircularProgressIndicatorSpec) {
    return new IndeterminateDrawable<>(paramContext, paramCircularProgressIndicatorSpec, new CircularDrawingDelegate(paramCircularProgressIndicatorSpec), new CircularIndeterminateAnimatorDelegate(paramCircularProgressIndicatorSpec));
  }
  
  public static IndeterminateDrawable<LinearProgressIndicatorSpec> createLinearDrawable(Context paramContext, LinearProgressIndicatorSpec paramLinearProgressIndicatorSpec) {
    LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate;
    LinearDrawingDelegate linearDrawingDelegate = new LinearDrawingDelegate(paramLinearProgressIndicatorSpec);
    if (paramLinearProgressIndicatorSpec.indeterminateAnimationType == 0) {
      LinearIndeterminateContiguousAnimatorDelegate linearIndeterminateContiguousAnimatorDelegate = new LinearIndeterminateContiguousAnimatorDelegate(paramLinearProgressIndicatorSpec);
    } else {
      linearIndeterminateDisjointAnimatorDelegate = new LinearIndeterminateDisjointAnimatorDelegate(paramContext, paramLinearProgressIndicatorSpec);
    } 
    return new IndeterminateDrawable<>(paramContext, paramLinearProgressIndicatorSpec, linearDrawingDelegate, linearIndeterminateDisjointAnimatorDelegate);
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect = new Rect();
    if (getBounds().isEmpty() || !isVisible() || !paramCanvas.getClipBounds(rect))
      return; 
    paramCanvas.save();
    this.drawingDelegate.validateSpecAndAdjustCanvas(paramCanvas, getGrowFraction());
    this.drawingDelegate.fillTrack(paramCanvas, this.paint);
    for (byte b = 0; b < this.animatorDelegate.segmentColors.length; b++)
      this.drawingDelegate.fillIndicator(paramCanvas, this.paint, this.animatorDelegate.segmentPositions[b * 2], this.animatorDelegate.segmentPositions[b * 2 + 1], this.animatorDelegate.segmentColors[b]); 
    paramCanvas.restore();
  }
  
  IndeterminateAnimatorDelegate<ObjectAnimator> getAnimatorDelegate() {
    return this.animatorDelegate;
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
  
  void setAnimatorDelegate(IndeterminateAnimatorDelegate<ObjectAnimator> paramIndeterminateAnimatorDelegate) {
    this.animatorDelegate = paramIndeterminateAnimatorDelegate;
    paramIndeterminateAnimatorDelegate.registerDrawable(this);
  }
  
  void setDrawingDelegate(DrawingDelegate<S> paramDrawingDelegate) {
    this.drawingDelegate = paramDrawingDelegate;
    paramDrawingDelegate.registerDrawable(this);
  }
  
  boolean setVisibleInternal(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    paramBoolean2 = super.setVisibleInternal(paramBoolean1, paramBoolean2, paramBoolean3);
    if (!isRunning())
      this.animatorDelegate.cancelAnimatorImmediately(); 
    float f = this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(this.context.getContentResolver());
    if (paramBoolean1 && (paramBoolean3 || (Build.VERSION.SDK_INT <= 21 && f > 0.0F)))
      this.animatorDelegate.startAnimator(); 
    return paramBoolean2;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\IndeterminateDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */