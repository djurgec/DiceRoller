package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.google.android.material.color.MaterialColors;

final class CircularDrawingDelegate extends DrawingDelegate<CircularProgressIndicatorSpec> {
  private float adjustedRadius;
  
  private int arcDirectionFactor = 1;
  
  private float displayedCornerRadius;
  
  private float displayedTrackThickness;
  
  public CircularDrawingDelegate(CircularProgressIndicatorSpec paramCircularProgressIndicatorSpec) {
    super(paramCircularProgressIndicatorSpec);
  }
  
  private void drawRoundedEnd(Canvas paramCanvas, Paint paramPaint, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean, RectF paramRectF) {
    float f;
    if (paramBoolean) {
      f = -1.0F;
    } else {
      f = 1.0F;
    } 
    paramCanvas.save();
    paramCanvas.rotate(paramFloat3);
    paramCanvas.drawRect(this.adjustedRadius - paramFloat1 / 2.0F + paramFloat2, Math.min(0.0F, f * paramFloat2 * this.arcDirectionFactor), this.adjustedRadius + paramFloat1 / 2.0F - paramFloat2, Math.max(0.0F, f * paramFloat2 * this.arcDirectionFactor), paramPaint);
    paramCanvas.translate(this.adjustedRadius - paramFloat1 / 2.0F + paramFloat2, 0.0F);
    paramCanvas.drawArc(paramRectF, 180.0F, -f * 90.0F * this.arcDirectionFactor, true, paramPaint);
    paramCanvas.translate(paramFloat1 - paramFloat2 * 2.0F, 0.0F);
    paramCanvas.drawArc(paramRectF, 0.0F, 90.0F * f * this.arcDirectionFactor, true, paramPaint);
    paramCanvas.restore();
  }
  
  private int getSize() {
    return this.spec.indicatorSize + this.spec.indicatorInset * 2;
  }
  
  public void adjustCanvas(Canvas paramCanvas, float paramFloat) {
    byte b;
    float f = this.spec.indicatorSize / 2.0F + this.spec.indicatorInset;
    paramCanvas.translate(f, f);
    paramCanvas.rotate(-90.0F);
    paramCanvas.clipRect(-f, -f, f, f);
    if (this.spec.indicatorDirection == 0) {
      b = 1;
    } else {
      b = -1;
    } 
    this.arcDirectionFactor = b;
    this.displayedTrackThickness = this.spec.trackThickness * paramFloat;
    this.displayedCornerRadius = this.spec.trackCornerRadius * paramFloat;
    this.adjustedRadius = (this.spec.indicatorSize - this.spec.trackThickness) / 2.0F;
    if ((this.drawable.isShowing() && this.spec.showAnimationBehavior == 2) || (this.drawable.isHiding() && this.spec.hideAnimationBehavior == 1)) {
      this.adjustedRadius += (1.0F - paramFloat) * this.spec.trackThickness / 2.0F;
    } else if ((this.drawable.isShowing() && this.spec.showAnimationBehavior == 1) || (this.drawable.isHiding() && this.spec.hideAnimationBehavior == 2)) {
      this.adjustedRadius -= (1.0F - paramFloat) * this.spec.trackThickness / 2.0F;
    } 
  }
  
  void fillIndicator(Canvas paramCanvas, Paint paramPaint, float paramFloat1, float paramFloat2, int paramInt) {
    if (paramFloat1 == paramFloat2)
      return; 
    paramPaint.setStyle(Paint.Style.STROKE);
    paramPaint.setStrokeCap(Paint.Cap.BUTT);
    paramPaint.setAntiAlias(true);
    paramPaint.setColor(paramInt);
    paramPaint.setStrokeWidth(this.displayedTrackThickness);
    paramInt = this.arcDirectionFactor;
    float f = paramFloat1 * 360.0F * paramInt;
    if (paramFloat2 >= paramFloat1) {
      paramFloat1 = paramFloat2 - paramFloat1;
    } else {
      paramFloat1 = paramFloat2 + 1.0F - paramFloat1;
    } 
    paramFloat1 = paramFloat1 * 360.0F * paramInt;
    paramFloat2 = this.adjustedRadius;
    paramCanvas.drawArc(new RectF(-paramFloat2, -paramFloat2, paramFloat2, paramFloat2), f, paramFloat1, false, paramPaint);
    if (this.displayedCornerRadius > 0.0F && Math.abs(paramFloat1) < 360.0F) {
      paramPaint.setStyle(Paint.Style.FILL);
      paramFloat2 = this.displayedCornerRadius;
      RectF rectF = new RectF(-paramFloat2, -paramFloat2, paramFloat2, paramFloat2);
      drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, f, true, rectF);
      drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, f + paramFloat1, false, rectF);
    } 
  }
  
  void fillTrack(Canvas paramCanvas, Paint paramPaint) {
    int i = MaterialColors.compositeARGBWithAlpha(this.spec.trackColor, this.drawable.getAlpha());
    paramPaint.setStyle(Paint.Style.STROKE);
    paramPaint.setStrokeCap(Paint.Cap.BUTT);
    paramPaint.setAntiAlias(true);
    paramPaint.setColor(i);
    paramPaint.setStrokeWidth(this.displayedTrackThickness);
    float f = this.adjustedRadius;
    paramCanvas.drawArc(new RectF(-f, -f, f, f), 0.0F, 360.0F, false, paramPaint);
  }
  
  public int getPreferredHeight() {
    return getSize();
  }
  
  public int getPreferredWidth() {
    return getSize();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\CircularDrawingDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */