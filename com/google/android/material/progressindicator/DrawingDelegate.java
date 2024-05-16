package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;

abstract class DrawingDelegate<S extends BaseProgressIndicatorSpec> {
  protected DrawableWithAnimatedVisibilityChange drawable;
  
  S spec;
  
  public DrawingDelegate(S paramS) {
    this.spec = paramS;
  }
  
  abstract void adjustCanvas(Canvas paramCanvas, float paramFloat);
  
  abstract void fillIndicator(Canvas paramCanvas, Paint paramPaint, float paramFloat1, float paramFloat2, int paramInt);
  
  abstract void fillTrack(Canvas paramCanvas, Paint paramPaint);
  
  abstract int getPreferredHeight();
  
  abstract int getPreferredWidth();
  
  protected void registerDrawable(DrawableWithAnimatedVisibilityChange paramDrawableWithAnimatedVisibilityChange) {
    this.drawable = paramDrawableWithAnimatedVisibilityChange;
  }
  
  void validateSpecAndAdjustCanvas(Canvas paramCanvas, float paramFloat) {
    this.spec.validateSpec();
    adjustCanvas(paramCanvas, paramFloat);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\DrawingDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */