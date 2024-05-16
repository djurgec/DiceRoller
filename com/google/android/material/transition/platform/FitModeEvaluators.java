package com.google.android.material.transition.platform;

import android.graphics.RectF;

class FitModeEvaluators {
  private static final FitModeEvaluator HEIGHT;
  
  private static final FitModeEvaluator WIDTH = new FitModeEvaluator() {
      public void applyMask(RectF param1RectF, float param1Float, FitModeResult param1FitModeResult) {
        float f = Math.abs(param1FitModeResult.currentEndHeight - param1FitModeResult.currentStartHeight);
        param1RectF.bottom -= f * param1Float;
      }
      
      public FitModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7) {
        param1Float1 = TransitionUtils.lerp(param1Float4, param1Float6, param1Float2, param1Float3, param1Float1, true);
        param1Float2 = param1Float1 / param1Float4;
        param1Float3 = param1Float1 / param1Float6;
        return new FitModeResult(param1Float2, param1Float3, param1Float1, param1Float5 * param1Float2, param1Float1, param1Float7 * param1Float3);
      }
      
      public boolean shouldMaskStartBounds(FitModeResult param1FitModeResult) {
        boolean bool;
        if (param1FitModeResult.currentStartHeight > param1FitModeResult.currentEndHeight) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
    };
  
  static {
    HEIGHT = new FitModeEvaluator() {
        public void applyMask(RectF param1RectF, float param1Float, FitModeResult param1FitModeResult) {
          float f = Math.abs(param1FitModeResult.currentEndWidth - param1FitModeResult.currentStartWidth);
          param1RectF.left += f / 2.0F * param1Float;
          param1RectF.right -= f / 2.0F * param1Float;
        }
        
        public FitModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7) {
          param1Float1 = TransitionUtils.lerp(param1Float5, param1Float7, param1Float2, param1Float3, param1Float1, true);
          param1Float2 = param1Float1 / param1Float5;
          param1Float3 = param1Float1 / param1Float7;
          return new FitModeResult(param1Float2, param1Float3, param1Float4 * param1Float2, param1Float1, param1Float6 * param1Float3, param1Float1);
        }
        
        public boolean shouldMaskStartBounds(FitModeResult param1FitModeResult) {
          boolean bool;
          if (param1FitModeResult.currentStartWidth > param1FitModeResult.currentEndWidth) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
      };
  }
  
  static FitModeEvaluator get(int paramInt, boolean paramBoolean, RectF paramRectF1, RectF paramRectF2) {
    FitModeEvaluator fitModeEvaluator;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Invalid fit mode: " + paramInt);
      case 2:
        return HEIGHT;
      case 1:
        return WIDTH;
      case 0:
        break;
    } 
    if (shouldAutoFitToWidth(paramBoolean, paramRectF1, paramRectF2)) {
      fitModeEvaluator = WIDTH;
    } else {
      fitModeEvaluator = HEIGHT;
    } 
    return fitModeEvaluator;
  }
  
  private static boolean shouldAutoFitToWidth(boolean paramBoolean, RectF paramRectF1, RectF paramRectF2) {
    float f5 = paramRectF1.width();
    float f3 = paramRectF1.height();
    float f4 = paramRectF2.width();
    float f1 = paramRectF2.height();
    float f2 = f1 * f5 / f4;
    f4 = f3 * f4 / f5;
    boolean bool = true;
    if (paramBoolean ? (f2 >= f3) : (f4 >= f1)) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\FitModeEvaluators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */