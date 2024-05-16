package com.google.android.material.shape;

public class CutCornerTreatment extends CornerTreatment {
  float size = -1.0F;
  
  public CutCornerTreatment() {}
  
  @Deprecated
  public CutCornerTreatment(float paramFloat) {
    this.size = paramFloat;
  }
  
  public void getCornerPath(ShapePath paramShapePath, float paramFloat1, float paramFloat2, float paramFloat3) {
    paramShapePath.reset(0.0F, paramFloat3 * paramFloat2, 180.0F, 180.0F - paramFloat1);
    paramShapePath.lineTo((float)(Math.sin(Math.toRadians(paramFloat1)) * paramFloat3 * paramFloat2), (float)(Math.sin(Math.toRadians((90.0F - paramFloat1))) * paramFloat3 * paramFloat2));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\CutCornerTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */