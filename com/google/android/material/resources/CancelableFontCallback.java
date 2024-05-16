package com.google.android.material.resources;

import android.graphics.Typeface;

public final class CancelableFontCallback extends TextAppearanceFontCallback {
  private final ApplyFont applyFont;
  
  private boolean cancelled;
  
  private final Typeface fallbackFont;
  
  public CancelableFontCallback(ApplyFont paramApplyFont, Typeface paramTypeface) {
    this.fallbackFont = paramTypeface;
    this.applyFont = paramApplyFont;
  }
  
  private void updateIfNotCancelled(Typeface paramTypeface) {
    if (!this.cancelled)
      this.applyFont.apply(paramTypeface); 
  }
  
  public void cancel() {
    this.cancelled = true;
  }
  
  public void onFontRetrievalFailed(int paramInt) {
    updateIfNotCancelled(this.fallbackFont);
  }
  
  public void onFontRetrieved(Typeface paramTypeface, boolean paramBoolean) {
    updateIfNotCancelled(paramTypeface);
  }
  
  public static interface ApplyFont {
    void apply(Typeface param1Typeface);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\resources\CancelableFontCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */