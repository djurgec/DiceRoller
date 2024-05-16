package com.google.android.material.appbar;

import android.view.View;
import androidx.core.view.ViewCompat;

class ViewOffsetHelper {
  private boolean horizontalOffsetEnabled = true;
  
  private int layoutLeft;
  
  private int layoutTop;
  
  private int offsetLeft;
  
  private int offsetTop;
  
  private boolean verticalOffsetEnabled = true;
  
  private final View view;
  
  public ViewOffsetHelper(View paramView) {
    this.view = paramView;
  }
  
  void applyOffsets() {
    View view = this.view;
    ViewCompat.offsetTopAndBottom(view, this.offsetTop - view.getTop() - this.layoutTop);
    view = this.view;
    ViewCompat.offsetLeftAndRight(view, this.offsetLeft - view.getLeft() - this.layoutLeft);
  }
  
  public int getLayoutLeft() {
    return this.layoutLeft;
  }
  
  public int getLayoutTop() {
    return this.layoutTop;
  }
  
  public int getLeftAndRightOffset() {
    return this.offsetLeft;
  }
  
  public int getTopAndBottomOffset() {
    return this.offsetTop;
  }
  
  public boolean isHorizontalOffsetEnabled() {
    return this.horizontalOffsetEnabled;
  }
  
  public boolean isVerticalOffsetEnabled() {
    return this.verticalOffsetEnabled;
  }
  
  void onViewLayout() {
    this.layoutTop = this.view.getTop();
    this.layoutLeft = this.view.getLeft();
  }
  
  public void setHorizontalOffsetEnabled(boolean paramBoolean) {
    this.horizontalOffsetEnabled = paramBoolean;
  }
  
  public boolean setLeftAndRightOffset(int paramInt) {
    if (this.horizontalOffsetEnabled && this.offsetLeft != paramInt) {
      this.offsetLeft = paramInt;
      applyOffsets();
      return true;
    } 
    return false;
  }
  
  public boolean setTopAndBottomOffset(int paramInt) {
    if (this.verticalOffsetEnabled && this.offsetTop != paramInt) {
      this.offsetTop = paramInt;
      applyOffsets();
      return true;
    } 
    return false;
  }
  
  public void setVerticalOffsetEnabled(boolean paramBoolean) {
    this.verticalOffsetEnabled = paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\ViewOffsetHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */