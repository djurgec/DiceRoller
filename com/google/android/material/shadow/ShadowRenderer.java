package com.google.android.material.shadow;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import androidx.core.graphics.ColorUtils;

public class ShadowRenderer {
  private static final int COLOR_ALPHA_END = 0;
  
  private static final int COLOR_ALPHA_MIDDLE = 20;
  
  private static final int COLOR_ALPHA_START = 68;
  
  private static final int[] cornerColors;
  
  private static final float[] cornerPositions;
  
  private static final int[] edgeColors = new int[3];
  
  private static final float[] edgePositions = new float[] { 0.0F, 0.5F, 1.0F };
  
  private final Paint cornerShadowPaint;
  
  private final Paint edgeShadowPaint;
  
  private final Path scratch = new Path();
  
  private int shadowEndColor;
  
  private int shadowMiddleColor;
  
  private final Paint shadowPaint = new Paint();
  
  private int shadowStartColor;
  
  private Paint transparentPaint = new Paint();
  
  static {
    cornerColors = new int[4];
    cornerPositions = new float[] { 0.0F, 0.0F, 0.5F, 1.0F };
  }
  
  public ShadowRenderer() {
    this(-16777216);
  }
  
  public ShadowRenderer(int paramInt) {
    setShadowColor(paramInt);
    this.transparentPaint.setColor(0);
    Paint paint = new Paint(4);
    this.cornerShadowPaint = paint;
    paint.setStyle(Paint.Style.FILL);
    this.edgeShadowPaint = new Paint(paint);
  }
  
  public void drawCornerShadow(Canvas paramCanvas, Matrix paramMatrix, RectF paramRectF, int paramInt, float paramFloat1, float paramFloat2) {
    boolean bool;
    if (paramFloat2 < 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    Path path = this.scratch;
    if (bool) {
      int[] arrayOfInt = cornerColors;
      arrayOfInt[0] = 0;
      arrayOfInt[1] = this.shadowEndColor;
      arrayOfInt[2] = this.shadowMiddleColor;
      arrayOfInt[3] = this.shadowStartColor;
    } else {
      path.rewind();
      path.moveTo(paramRectF.centerX(), paramRectF.centerY());
      path.arcTo(paramRectF, paramFloat1, paramFloat2);
      path.close();
      paramRectF.inset(-paramInt, -paramInt);
      int[] arrayOfInt = cornerColors;
      arrayOfInt[0] = 0;
      arrayOfInt[1] = this.shadowStartColor;
      arrayOfInt[2] = this.shadowMiddleColor;
      arrayOfInt[3] = this.shadowEndColor;
    } 
    float f1 = paramRectF.width() / 2.0F;
    if (f1 <= 0.0F)
      return; 
    float f3 = 1.0F - paramInt / f1;
    float f2 = (1.0F - f3) / 2.0F;
    float[] arrayOfFloat = cornerPositions;
    arrayOfFloat[1] = f3;
    arrayOfFloat[2] = f3 + f2;
    RadialGradient radialGradient = new RadialGradient(paramRectF.centerX(), paramRectF.centerY(), f1, cornerColors, arrayOfFloat, Shader.TileMode.CLAMP);
    this.cornerShadowPaint.setShader((Shader)radialGradient);
    paramCanvas.save();
    paramCanvas.concat(paramMatrix);
    paramCanvas.scale(1.0F, paramRectF.height() / paramRectF.width());
    if (!bool) {
      paramCanvas.clipPath(path, Region.Op.DIFFERENCE);
      paramCanvas.drawPath(path, this.transparentPaint);
    } 
    paramCanvas.drawArc(paramRectF, paramFloat1, paramFloat2, true, this.cornerShadowPaint);
    paramCanvas.restore();
  }
  
  public void drawEdgeShadow(Canvas paramCanvas, Matrix paramMatrix, RectF paramRectF, int paramInt) {
    paramRectF.bottom += paramInt;
    paramRectF.offset(0.0F, -paramInt);
    int[] arrayOfInt = edgeColors;
    arrayOfInt[0] = this.shadowEndColor;
    arrayOfInt[1] = this.shadowMiddleColor;
    arrayOfInt[2] = this.shadowStartColor;
    this.edgeShadowPaint.setShader((Shader)new LinearGradient(paramRectF.left, paramRectF.top, paramRectF.left, paramRectF.bottom, arrayOfInt, edgePositions, Shader.TileMode.CLAMP));
    paramCanvas.save();
    paramCanvas.concat(paramMatrix);
    paramCanvas.drawRect(paramRectF, this.edgeShadowPaint);
    paramCanvas.restore();
  }
  
  public Paint getShadowPaint() {
    return this.shadowPaint;
  }
  
  public void setShadowColor(int paramInt) {
    this.shadowStartColor = ColorUtils.setAlphaComponent(paramInt, 68);
    this.shadowMiddleColor = ColorUtils.setAlphaComponent(paramInt, 20);
    this.shadowEndColor = ColorUtils.setAlphaComponent(paramInt, 0);
    this.shadowPaint.setColor(this.shadowStartColor);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shadow\ShadowRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */