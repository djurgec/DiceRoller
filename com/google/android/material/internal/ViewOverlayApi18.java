package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

class ViewOverlayApi18 implements ViewOverlayImpl {
  private final ViewOverlay viewOverlay;
  
  ViewOverlayApi18(View paramView) {
    this.viewOverlay = paramView.getOverlay();
  }
  
  public void add(Drawable paramDrawable) {
    this.viewOverlay.add(paramDrawable);
  }
  
  public void remove(Drawable paramDrawable) {
    this.viewOverlay.remove(paramDrawable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ViewOverlayApi18.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */