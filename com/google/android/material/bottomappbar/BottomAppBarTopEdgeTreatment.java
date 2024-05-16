package com.google.android.material.bottomappbar;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment implements Cloneable {
  private static final int ANGLE_LEFT = 180;
  
  private static final int ANGLE_UP = 270;
  
  private static final int ARC_HALF = 180;
  
  private static final int ARC_QUARTER = 90;
  
  private static final float ROUNDED_CORNER_FAB_OFFSET = 1.75F;
  
  private float cradleVerticalOffset;
  
  private float fabCornerSize = -1.0F;
  
  private float fabDiameter;
  
  private float fabMargin;
  
  private float horizontalOffset;
  
  private float roundedCornerRadius;
  
  public BottomAppBarTopEdgeTreatment(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.fabMargin = paramFloat1;
    this.roundedCornerRadius = paramFloat2;
    setCradleVerticalOffset(paramFloat3);
    this.horizontalOffset = 0.0F;
  }
  
  float getCradleVerticalOffset() {
    return this.cradleVerticalOffset;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, float paramFloat3, ShapePath paramShapePath) {
    boolean bool;
    float f5 = this.fabDiameter;
    if (f5 == 0.0F) {
      paramShapePath.lineTo(paramFloat1, 0.0F);
      return;
    } 
    float f2 = (this.fabMargin * 2.0F + f5) / 2.0F;
    float f1 = paramFloat3 * this.roundedCornerRadius;
    float f3 = paramFloat2 + this.horizontalOffset;
    paramFloat2 = this.cradleVerticalOffset * paramFloat3 + (1.0F - paramFloat3) * f2;
    if (paramFloat2 / f2 >= 1.0F) {
      paramShapePath.lineTo(paramFloat1, 0.0F);
      return;
    } 
    float f6 = this.fabCornerSize;
    float f4 = f6 * paramFloat3;
    if (f6 == -1.0F || Math.abs(f6 * 2.0F - f5) < 0.1F) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!bool) {
      paramFloat3 = 1.75F;
      paramFloat2 = 0.0F;
    } else {
      paramFloat3 = 0.0F;
    } 
    f5 = f2 + f1;
    f6 = paramFloat2 + f1;
    float f8 = (float)Math.sqrt((f5 * f5 - f6 * f6));
    float f7 = f3 - f8;
    f5 = f3 + f8;
    f6 = (float)Math.toDegrees(Math.atan((f8 / f6)));
    paramFloat3 = 90.0F - f6 + paramFloat3;
    paramShapePath.lineTo(f7, 0.0F);
    paramShapePath.addArc(f7 - f1, 0.0F, f7 + f1, f1 * 2.0F, 270.0F, f6);
    if (bool) {
      paramShapePath.addArc(f3 - f2, -f2 - paramFloat2, f3 + f2, f2 - paramFloat2, 180.0F - paramFloat3, paramFloat3 * 2.0F - 180.0F);
    } else {
      paramFloat2 = this.fabMargin;
      paramShapePath.addArc(f3 - f2, -(f4 + paramFloat2), f3 - f2 + paramFloat2 + f4 * 2.0F, paramFloat2 + f4, 180.0F - paramFloat3, (paramFloat3 * 2.0F - 180.0F) / 2.0F);
      paramFloat2 = this.fabMargin;
      paramShapePath.lineTo(f3 + f2 - f4 + paramFloat2 / 2.0F, f4 + paramFloat2);
      paramFloat2 = this.fabMargin;
      paramShapePath.addArc(f3 + f2 - f4 * 2.0F + paramFloat2, -(f4 + paramFloat2), f3 + f2, paramFloat2 + f4, 90.0F, paramFloat3 - 90.0F);
    } 
    paramShapePath.addArc(f5 - f1, 0.0F, f5 + f1, f1 * 2.0F, 270.0F - f6, f6);
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
  
  public float getFabCornerRadius() {
    return this.fabCornerSize;
  }
  
  float getFabCradleMargin() {
    return this.fabMargin;
  }
  
  float getFabCradleRoundedCornerRadius() {
    return this.roundedCornerRadius;
  }
  
  public float getFabDiameter() {
    return this.fabDiameter;
  }
  
  public float getHorizontalOffset() {
    return this.horizontalOffset;
  }
  
  void setCradleVerticalOffset(float paramFloat) {
    if (paramFloat >= 0.0F) {
      this.cradleVerticalOffset = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
  }
  
  public void setFabCornerSize(float paramFloat) {
    this.fabCornerSize = paramFloat;
  }
  
  void setFabCradleMargin(float paramFloat) {
    this.fabMargin = paramFloat;
  }
  
  void setFabCradleRoundedCornerRadius(float paramFloat) {
    this.roundedCornerRadius = paramFloat;
  }
  
  public void setFabDiameter(float paramFloat) {
    this.fabDiameter = paramFloat;
  }
  
  void setHorizontalOffset(float paramFloat) {
    this.horizontalOffset = paramFloat;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomappbar\BottomAppBarTopEdgeTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */