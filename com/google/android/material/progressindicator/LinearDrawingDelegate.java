package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.google.android.material.color.MaterialColors;

final class LinearDrawingDelegate extends DrawingDelegate<LinearProgressIndicatorSpec> {
  private float displayedCornerRadius;
  
  private float displayedTrackThickness;
  
  private float trackLength = 300.0F;
  
  public LinearDrawingDelegate(LinearProgressIndicatorSpec paramLinearProgressIndicatorSpec) {
    super(paramLinearProgressIndicatorSpec);
  }
  
  private static void drawRoundedEnd(Canvas paramCanvas, Paint paramPaint, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean, RectF paramRectF) {
    paramCanvas.save();
    paramCanvas.translate(paramFloat3, 0.0F);
    if (!paramBoolean)
      paramCanvas.rotate(180.0F); 
    paramCanvas.drawRect(-paramFloat2, -paramFloat1 / 2.0F + paramFloat2, 0.0F, paramFloat1 / 2.0F - paramFloat2, paramPaint);
    paramCanvas.save();
    paramCanvas.translate(0.0F, -paramFloat1 / 2.0F + paramFloat2);
    paramCanvas.drawArc(paramRectF, 180.0F, 90.0F, true, paramPaint);
    paramCanvas.restore();
    paramCanvas.translate(0.0F, paramFloat1 / 2.0F - paramFloat2);
    paramCanvas.drawArc(paramRectF, 180.0F, -90.0F, true, paramPaint);
    paramCanvas.restore();
  }
  
  public void adjustCanvas(Canvas paramCanvas, float paramFloat) {
    Rect rect = paramCanvas.getClipBounds();
    this.trackLength = rect.width();
    float f1 = this.spec.trackThickness;
    paramCanvas.translate(rect.left + rect.width() / 2.0F, rect.top + rect.height() / 2.0F + Math.max(0.0F, (rect.height() - this.spec.trackThickness) / 2.0F));
    if (this.spec.drawHorizontallyInverse)
      paramCanvas.scale(-1.0F, 1.0F); 
    if ((this.drawable.isShowing() && this.spec.showAnimationBehavior == 1) || (this.drawable.isHiding() && this.spec.hideAnimationBehavior == 2))
      paramCanvas.scale(1.0F, -1.0F); 
    if (this.drawable.isShowing() || this.drawable.isHiding())
      paramCanvas.translate(0.0F, this.spec.trackThickness * (paramFloat - 1.0F) / 2.0F); 
    float f2 = this.trackLength;
    paramCanvas.clipRect(-f2 / 2.0F, -f1 / 2.0F, f2 / 2.0F, f1 / 2.0F);
    this.displayedTrackThickness = this.spec.trackThickness * paramFloat;
    this.displayedCornerRadius = this.spec.trackCornerRadius * paramFloat;
  }
  
  public void fillIndicator(Canvas paramCanvas, Paint paramPaint, float paramFloat1, float paramFloat2, int paramInt) {
    if (paramFloat1 == paramFloat2)
      return; 
    float f1 = this.trackLength;
    float f3 = -f1 / 2.0F;
    float f2 = this.displayedCornerRadius;
    paramFloat1 = f3 + f2 + (f1 - f2 * 2.0F) * paramFloat1;
    paramFloat2 = -f1 / 2.0F + f2 + (f1 - f2 * 2.0F) * paramFloat2;
    paramPaint.setStyle(Paint.Style.FILL);
    paramPaint.setAntiAlias(true);
    paramPaint.setColor(paramInt);
    f1 = this.displayedTrackThickness;
    paramCanvas.drawRect(paramFloat1, -f1 / 2.0F, paramFloat2, f1 / 2.0F, paramPaint);
    f1 = this.displayedCornerRadius;
    RectF rectF = new RectF(-f1, -f1, f1, f1);
    drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, paramFloat1, true, rectF);
    drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, paramFloat2, false, rectF);
  }
  
  void fillTrack(Canvas paramCanvas, Paint paramPaint) {
    int i = MaterialColors.compositeARGBWithAlpha(this.spec.trackColor, this.drawable.getAlpha());
    float f2 = -this.trackLength / 2.0F + this.displayedCornerRadius;
    float f1 = -f2;
    paramPaint.setStyle(Paint.Style.FILL);
    paramPaint.setAntiAlias(true);
    paramPaint.setColor(i);
    float f3 = this.displayedTrackThickness;
    paramCanvas.drawRect(f2, -f3 / 2.0F, f1, f3 / 2.0F, paramPaint);
    f3 = this.displayedCornerRadius;
    RectF rectF = new RectF(-f3, -f3, f3, f3);
    drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, f2, true, rectF);
    drawRoundedEnd(paramCanvas, paramPaint, this.displayedTrackThickness, this.displayedCornerRadius, f1, false, rectF);
  }
  
  public int getPreferredHeight() {
    return this.spec.trackThickness;
  }
  
  public int getPreferredWidth() {
    return -1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\LinearDrawingDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */