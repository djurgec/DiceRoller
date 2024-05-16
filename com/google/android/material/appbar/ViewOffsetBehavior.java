package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private int tempLeftRightOffset = 0;
  
  private int tempTopBottomOffset = 0;
  
  private ViewOffsetHelper viewOffsetHelper;
  
  public ViewOffsetBehavior() {}
  
  public ViewOffsetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public int getLeftAndRightOffset() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null) {
      bool = viewOffsetHelper.getLeftAndRightOffset();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getTopAndBottomOffset() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null) {
      bool = viewOffsetHelper.getTopAndBottomOffset();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isHorizontalOffsetEnabled() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null && viewOffsetHelper.isHorizontalOffsetEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isVerticalOffsetEnabled() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null && viewOffsetHelper.isVerticalOffsetEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void layoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    layoutChild(paramCoordinatorLayout, paramV, paramInt);
    if (this.viewOffsetHelper == null)
      this.viewOffsetHelper = new ViewOffsetHelper((View)paramV); 
    this.viewOffsetHelper.onViewLayout();
    this.viewOffsetHelper.applyOffsets();
    paramInt = this.tempTopBottomOffset;
    if (paramInt != 0) {
      this.viewOffsetHelper.setTopAndBottomOffset(paramInt);
      this.tempTopBottomOffset = 0;
    } 
    paramInt = this.tempLeftRightOffset;
    if (paramInt != 0) {
      this.viewOffsetHelper.setLeftAndRightOffset(paramInt);
      this.tempLeftRightOffset = 0;
    } 
    return true;
  }
  
  public void setHorizontalOffsetEnabled(boolean paramBoolean) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      viewOffsetHelper.setHorizontalOffsetEnabled(paramBoolean); 
  }
  
  public boolean setLeftAndRightOffset(int paramInt) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      return viewOffsetHelper.setLeftAndRightOffset(paramInt); 
    this.tempLeftRightOffset = paramInt;
    return false;
  }
  
  public boolean setTopAndBottomOffset(int paramInt) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      return viewOffsetHelper.setTopAndBottomOffset(paramInt); 
    this.tempTopBottomOffset = paramInt;
    return false;
  }
  
  public void setVerticalOffsetEnabled(boolean paramBoolean) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      viewOffsetHelper.setVerticalOffsetEnabled(paramBoolean); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\ViewOffsetBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */