package com.google.android.material.shape;

public class TriangleEdgeTreatment extends EdgeTreatment {
  private final boolean inside;
  
  private final float size;
  
  public TriangleEdgeTreatment(float paramFloat, boolean paramBoolean) {
    this.size = paramFloat;
    this.inside = paramBoolean;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, float paramFloat3, ShapePath paramShapePath) {
    float f;
    paramShapePath.lineTo(paramFloat2 - this.size * paramFloat3, 0.0F);
    if (this.inside) {
      f = this.size;
    } else {
      f = -this.size;
    } 
    paramShapePath.lineTo(paramFloat2, f * paramFloat3);
    paramShapePath.lineTo(this.size * paramFloat3 + paramFloat2, 0.0F);
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\TriangleEdgeTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */