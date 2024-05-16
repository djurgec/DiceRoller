package com.google.android.material.transition.platform;

import android.graphics.RectF;

interface FitModeEvaluator {
  void applyMask(RectF paramRectF, float paramFloat, FitModeResult paramFitModeResult);
  
  FitModeResult evaluate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
  
  boolean shouldMaskStartBounds(FitModeResult paramFitModeResult);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\FitModeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */