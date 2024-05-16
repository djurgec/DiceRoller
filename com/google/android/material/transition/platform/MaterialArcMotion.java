package com.google.android.material.transition.platform;

import android.graphics.Path;
import android.graphics.PointF;
import android.transition.PathMotion;

public final class MaterialArcMotion extends PathMotion {
  private static PointF getControlPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return (paramFloat2 > paramFloat4) ? new PointF(paramFloat3, paramFloat2) : new PointF(paramFloat1, paramFloat4);
  }
  
  public Path getPath(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    Path path = new Path();
    path.moveTo(paramFloat1, paramFloat2);
    PointF pointF = getControlPoint(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    path.quadTo(pointF.x, pointF.y, paramFloat3, paramFloat4);
    return path;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\MaterialArcMotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */