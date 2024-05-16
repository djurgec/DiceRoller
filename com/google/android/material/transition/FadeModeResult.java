package com.google.android.material.transition;

class FadeModeResult {
  final int endAlpha;
  
  final boolean endOnTop;
  
  final int startAlpha;
  
  private FadeModeResult(int paramInt1, int paramInt2, boolean paramBoolean) {
    this.startAlpha = paramInt1;
    this.endAlpha = paramInt2;
    this.endOnTop = paramBoolean;
  }
  
  static FadeModeResult endOnTop(int paramInt1, int paramInt2) {
    return new FadeModeResult(paramInt1, paramInt2, true);
  }
  
  static FadeModeResult startOnTop(int paramInt1, int paramInt2) {
    return new FadeModeResult(paramInt1, paramInt2, false);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\FadeModeResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */