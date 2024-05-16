package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

class ViewGroupOverlayApi18 implements ViewGroupOverlayImpl {
  private final ViewGroupOverlay viewGroupOverlay;
  
  ViewGroupOverlayApi18(ViewGroup paramViewGroup) {
    this.viewGroupOverlay = paramViewGroup.getOverlay();
  }
  
  public void add(Drawable paramDrawable) {
    this.viewGroupOverlay.add(paramDrawable);
  }
  
  public void add(View paramView) {
    this.viewGroupOverlay.add(paramView);
  }
  
  public void remove(Drawable paramDrawable) {
    this.viewGroupOverlay.remove(paramDrawable);
  }
  
  public void remove(View paramView) {
    this.viewGroupOverlay.remove(paramView);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ViewGroupOverlayApi18.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */