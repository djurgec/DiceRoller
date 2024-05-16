package com.google.android.material.shape;

import android.graphics.RectF;

public class CornerTreatment {
  @Deprecated
  public void getCornerPath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {}
  
  public void getCornerPath(ShapePath paramShapePath, float paramFloat1, float paramFloat2, float paramFloat3) {
    getCornerPath(paramFloat1, paramFloat2, paramShapePath);
  }
  
  public void getCornerPath(ShapePath paramShapePath, float paramFloat1, float paramFloat2, RectF paramRectF, CornerSize paramCornerSize) {
    getCornerPath(paramShapePath, paramFloat1, paramFloat2, paramCornerSize.getCornerSize(paramRectF));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\CornerTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */