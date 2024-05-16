package com.google.android.material.transition;

class FadeModeEvaluators {
  private static final FadeModeEvaluator CROSS;
  
  private static final FadeModeEvaluator IN = new FadeModeEvaluator() {
      public FadeModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
        return FadeModeResult.endOnTop(255, TransitionUtils.lerp(0, 255, param1Float2, param1Float3, param1Float1));
      }
    };
  
  private static final FadeModeEvaluator OUT = new FadeModeEvaluator() {
      public FadeModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
        return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, param1Float2, param1Float3, param1Float1), 255);
      }
    };
  
  private static final FadeModeEvaluator THROUGH;
  
  static {
    CROSS = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
          return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, param1Float2, param1Float3, param1Float1), TransitionUtils.lerp(0, 255, param1Float2, param1Float3, param1Float1));
        }
      };
    THROUGH = new FadeModeEvaluator() {
        public FadeModeResult evaluate(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
          param1Float4 = (param1Float3 - param1Float2) * param1Float4 + param1Float2;
          return FadeModeResult.startOnTop(TransitionUtils.lerp(255, 0, param1Float2, param1Float4, param1Float1), TransitionUtils.lerp(0, 255, param1Float4, param1Float3, param1Float1));
        }
      };
  }
  
  static FadeModeEvaluator get(int paramInt, boolean paramBoolean) {
    FadeModeEvaluator fadeModeEvaluator;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Invalid fade mode: " + paramInt);
      case 3:
        return THROUGH;
      case 2:
        return CROSS;
      case 1:
        if (paramBoolean) {
          fadeModeEvaluator = OUT;
        } else {
          fadeModeEvaluator = IN;
        } 
        return fadeModeEvaluator;
      case 0:
        break;
    } 
    if (paramBoolean) {
      fadeModeEvaluator = IN;
    } else {
      fadeModeEvaluator = OUT;
    } 
    return fadeModeEvaluator;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\FadeModeEvaluators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */