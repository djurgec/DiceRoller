package com.google.android.material.shape;

public class EdgeTreatment {
  boolean forceIntersection() {
    return false;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, float paramFloat3, ShapePath paramShapePath) {
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
  
  @Deprecated
  public void getEdgePath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    getEdgePath(paramFloat1, paramFloat1 / 2.0F, paramFloat2, paramShapePath);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\EdgeTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */