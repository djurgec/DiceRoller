package com.google.android.material.shadow;

import android.graphics.drawable.Drawable;

public interface ShadowViewDelegate {
  float getRadius();
  
  boolean isCompatPaddingEnabled();
  
  void setBackgroundDrawable(Drawable paramDrawable);
  
  void setShadowPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shadow\ShadowViewDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */