package com.google.android.material.shape;

public class RoundedCornerTreatment extends CornerTreatment {
  float radius = -1.0F;
  
  public RoundedCornerTreatment() {}
  
  @Deprecated
  public RoundedCornerTreatment(float paramFloat) {
    this.radius = paramFloat;
  }
  
  public void getCornerPath(ShapePath paramShapePath, float paramFloat1, float paramFloat2, float paramFloat3) {
    paramShapePath.reset(0.0F, paramFloat3 * paramFloat2, 180.0F, 180.0F - paramFloat1);
    paramShapePath.addArc(0.0F, 0.0F, paramFloat3 * 2.0F * paramFloat2, 2.0F * paramFloat3 * paramFloat2, 180.0F, paramFloat1);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\RoundedCornerTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */