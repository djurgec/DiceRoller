package com.google.android.material.internal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

class ViewGroupOverlayApi14 extends ViewOverlayApi14 implements ViewGroupOverlayImpl {
  ViewGroupOverlayApi14(Context paramContext, ViewGroup paramViewGroup, View paramView) {
    super(paramContext, paramViewGroup, paramView);
  }
  
  static ViewGroupOverlayApi14 createFrom(ViewGroup paramViewGroup) {
    return (ViewGroupOverlayApi14)ViewOverlayApi14.createFrom((View)paramViewGroup);
  }
  
  public void add(View paramView) {
    this.overlayViewGroup.add(paramView);
  }
  
  public void remove(View paramView) {
    this.overlayViewGroup.remove(paramView);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ViewGroupOverlayApi14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */