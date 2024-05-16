package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

public class BottomNavigationMenuView extends NavigationBarMenuView {
  private final int activeItemMaxWidth;
  
  private final int activeItemMinWidth;
  
  private final int inactiveItemMaxWidth;
  
  private final int inactiveItemMinWidth;
  
  private final int itemHeight;
  
  private boolean itemHorizontalTranslationEnabled;
  
  private int[] tempChildWidths;
  
  public BottomNavigationMenuView(Context paramContext) {
    super(paramContext);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
    layoutParams.gravity = 17;
    setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    Resources resources = getResources();
    this.inactiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
    this.inactiveItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
    this.activeItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
    this.activeItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
    this.itemHeight = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
    this.tempChildWidths = new int[5];
  }
  
  protected NavigationBarItemView createNavigationBarItemView(Context paramContext) {
    return new BottomNavigationItemView(paramContext);
  }
  
  public boolean isItemHorizontalTranslationEnabled() {
    return this.itemHorizontalTranslationEnabled;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    paramInt3 -= paramInt1;
    paramInt4 -= paramInt2;
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
          view.layout(paramInt3 - paramInt2 - view.getMeasuredWidth(), 0, paramInt3 - paramInt2, paramInt4);
        } else {
          view.layout(paramInt2, 0, view.getMeasuredWidth() + paramInt2, paramInt4);
        } 
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    MenuBuilder menuBuilder = getMenu();
    int m = View.MeasureSpec.getSize(paramInt1);
    int i = menuBuilder.getVisibleItems().size();
    int k = getChildCount();
    int j = View.MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
    if (isShifting(getLabelVisibilityMode(), i) && isItemHorizontalTranslationEnabled()) {
      View view = getChildAt(getSelectedItemPosition());
      paramInt2 = this.activeItemMinWidth;
      paramInt1 = paramInt2;
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.activeItemMaxWidth, -2147483648), j);
        paramInt1 = Math.max(paramInt2, view.getMeasuredWidth());
      } 
      if (view.getVisibility() != 8) {
        paramInt2 = 1;
      } else {
        paramInt2 = 0;
      } 
      paramInt2 = i - paramInt2;
      int n = Math.min(m - this.inactiveItemMinWidth * paramInt2, Math.min(paramInt1, this.activeItemMaxWidth));
      if (paramInt2 == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = paramInt2;
      } 
      int i1 = Math.min((m - n) / paramInt1, this.inactiveItemMaxWidth);
      paramInt2 = m - n - i1 * paramInt2;
      paramInt1 = 0;
      while (paramInt1 < k) {
        if (getChildAt(paramInt1).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          if (paramInt1 == getSelectedItemPosition()) {
            i = n;
          } else {
            i = i1;
          } 
          arrayOfInt[paramInt1] = i;
          i = paramInt2;
          if (paramInt2 > 0) {
            arrayOfInt = this.tempChildWidths;
            arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
            i = paramInt2 - 1;
          } 
        } else {
          this.tempChildWidths[paramInt1] = 0;
          i = paramInt2;
        } 
        paramInt1++;
        paramInt2 = i;
      } 
    } else {
      if (i == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = i;
      } 
      int n = Math.min(m / paramInt1, this.activeItemMaxWidth);
      paramInt2 = m - n * i;
      for (paramInt1 = 0; paramInt1 < k; paramInt1++) {
        if (getChildAt(paramInt1).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          arrayOfInt[paramInt1] = n;
          if (paramInt2 > 0) {
            arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
            paramInt2--;
          } 
        } else {
          this.tempChildWidths[paramInt1] = 0;
        } 
      } 
    } 
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < k; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.tempChildWidths[paramInt1], 1073741824), j);
        (view.getLayoutParams()).width = view.getMeasuredWidth();
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
    setMeasuredDimension(View.resolveSizeAndState(paramInt2, View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824), 0), View.resolveSizeAndState(this.itemHeight, j, 0));
  }
  
  public void setItemHorizontalTranslationEnabled(boolean paramBoolean) {
    this.itemHorizontalTranslationEnabled = paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\BottomNavigationMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */