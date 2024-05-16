package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

public class NavigationRailMenuView extends NavigationBarMenuView {
  private final FrameLayout.LayoutParams layoutParams;
  
  public NavigationRailMenuView(Context paramContext) {
    super(paramContext);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
    this.layoutParams = layoutParams;
    layoutParams.gravity = 49;
    setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  private int makeSharedHeightSpec(int paramInt1, int paramInt2, int paramInt3) {
    paramInt2 /= Math.max(1, paramInt3);
    return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(paramInt1), paramInt2), 0);
  }
  
  private int measureChildHeight(View paramView, int paramInt1, int paramInt2) {
    if (paramView.getVisibility() != 8) {
      paramView.measure(paramInt1, paramInt2);
      return paramView.getMeasuredHeight();
    } 
    return 0;
  }
  
  private int measureSharedChildHeights(int paramInt1, int paramInt2, int paramInt3, View paramView) {
    makeSharedHeightSpec(paramInt1, paramInt2, paramInt3);
    if (paramView == null) {
      paramInt2 = makeSharedHeightSpec(paramInt1, paramInt2, paramInt3);
    } else {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(paramView.getMeasuredHeight(), 0);
    } 
    int j = getChildCount();
    int i = 0;
    paramInt3 = 0;
    while (paramInt3 < j) {
      View view = getChildAt(paramInt3);
      int k = i;
      if (view != paramView)
        k = i + measureChildHeight(view, paramInt1, paramInt2); 
      paramInt3++;
      i = k;
    } 
    return i;
  }
  
  private int measureShiftingChildHeights(int paramInt1, int paramInt2, int paramInt3) {
    int k = 0;
    View view = getChildAt(getSelectedItemPosition());
    int j = paramInt2;
    int i = paramInt3;
    if (view != null) {
      k = measureChildHeight(view, paramInt1, makeSharedHeightSpec(paramInt1, paramInt2, paramInt3));
      j = paramInt2 - k;
      i = paramInt3 - 1;
    } 
    return measureSharedChildHeights(paramInt1, j, i, view) + k;
  }
  
  protected NavigationBarItemView createNavigationBarItemView(Context paramContext) {
    return new NavigationRailItemView(paramContext);
  }
  
  int getMenuGravity() {
    return this.layoutParams.gravity;
  }
  
  boolean isTopGravity() {
    boolean bool;
    if ((this.layoutParams.gravity & 0x70) == 48) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    paramInt4 = 0;
    paramInt2 = 0;
    while (paramInt2 < i) {
      View view = getChildAt(paramInt2);
      int j = paramInt4;
      if (view.getVisibility() != 8) {
        j = view.getMeasuredHeight();
        view.layout(0, paramInt4, paramInt3 - paramInt1, j + paramInt4);
        j = paramInt4 + j;
      } 
      paramInt2++;
      paramInt4 = j;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.getSize(paramInt2);
    int i = getMenu().getVisibleItems().size();
    if (i > 1 && isShifting(getLabelVisibilityMode(), i)) {
      i = measureShiftingChildHeights(paramInt1, j, i);
    } else {
      i = measureSharedChildHeights(paramInt1, j, i, (View)null);
    } 
    j = View.MeasureSpec.getSize(paramInt1);
    setMeasuredDimension(View.resolveSizeAndState(j, paramInt1, 0), View.resolveSizeAndState(i, paramInt2, 0));
  }
  
  void setMenuGravity(int paramInt) {
    if (this.layoutParams.gravity != paramInt) {
      this.layoutParams.gravity = paramInt;
      setLayoutParams((ViewGroup.LayoutParams)this.layoutParams);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigationrail\NavigationRailMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */