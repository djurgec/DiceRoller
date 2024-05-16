package com.google.android.material.navigationrail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationRailView extends NavigationBarView {
  private static final int DEFAULT_HEADER_GRAVITY = 49;
  
  static final int DEFAULT_MENU_GRAVITY = 49;
  
  static final int MAX_ITEM_COUNT = 7;
  
  private View headerView;
  
  private final int topMargin = getResources().getDimensionPixelSize(R.dimen.mtrl_navigation_rail_margin);
  
  public NavigationRailView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NavigationRailView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.navigationRailStyle);
  }
  
  public NavigationRailView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, R.style.Widget_MaterialComponents_NavigationRailView);
  }
  
  public NavigationRailView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(getContext(), paramAttributeSet, R.styleable.NavigationRailView, paramInt1, paramInt2, new int[0]);
    paramInt1 = tintTypedArray.getResourceId(R.styleable.NavigationRailView_headerLayout, 0);
    if (paramInt1 != 0)
      addHeaderView(paramInt1); 
    setMenuGravity(tintTypedArray.getInt(R.styleable.NavigationRailView_menuGravity, 49));
    tintTypedArray.recycle();
  }
  
  private NavigationRailMenuView getNavigationRailMenuView() {
    return (NavigationRailMenuView)getMenuView();
  }
  
  private boolean isHeaderViewVisible() {
    boolean bool;
    View view = this.headerView;
    if (view != null && view.getVisibility() != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private int makeMinWidthSpec(int paramInt) {
    int i = getSuggestedMinimumWidth();
    if (View.MeasureSpec.getMode(paramInt) != 1073741824 && i > 0) {
      int j = getPaddingLeft();
      int k = getPaddingRight();
      return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(paramInt), i + j + k), 1073741824);
    } 
    return paramInt;
  }
  
  public void addHeaderView(int paramInt) {
    addHeaderView(LayoutInflater.from(getContext()).inflate(paramInt, (ViewGroup)this, false));
  }
  
  public void addHeaderView(View paramView) {
    removeHeaderView();
    this.headerView = paramView;
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
    layoutParams.gravity = 49;
    layoutParams.topMargin = this.topMargin;
    addView(paramView, 0, (ViewGroup.LayoutParams)layoutParams);
  }
  
  protected NavigationRailMenuView createNavigationBarMenuView(Context paramContext) {
    return new NavigationRailMenuView(paramContext);
  }
  
  public View getHeaderView() {
    return this.headerView;
  }
  
  public int getMaxItemCount() {
    return 7;
  }
  
  public int getMenuGravity() {
    return getNavigationRailMenuView().getMenuGravity();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    NavigationRailMenuView navigationRailMenuView = getNavigationRailMenuView();
    paramInt1 = 0;
    if (isHeaderViewVisible()) {
      paramInt2 = this.headerView.getBottom() + this.topMargin;
      paramInt3 = navigationRailMenuView.getTop();
      if (paramInt3 < paramInt2)
        paramInt1 = paramInt2 - paramInt3; 
    } else if (navigationRailMenuView.isTopGravity()) {
      paramInt1 = this.topMargin;
    } 
    if (paramInt1 > 0)
      navigationRailMenuView.layout(navigationRailMenuView.getLeft(), navigationRailMenuView.getTop() + paramInt1, navigationRailMenuView.getRight(), navigationRailMenuView.getBottom() + paramInt1); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    paramInt1 = makeMinWidthSpec(paramInt1);
    super.onMeasure(paramInt1, paramInt2);
    if (isHeaderViewVisible()) {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - this.headerView.getMeasuredHeight() - this.topMargin, -2147483648);
      measureChild((View)getNavigationRailMenuView(), paramInt1, paramInt2);
    } 
  }
  
  public void removeHeaderView() {
    View view = this.headerView;
    if (view != null) {
      removeView(view);
      this.headerView = null;
    } 
  }
  
  public void setMenuGravity(int paramInt) {
    getNavigationRailMenuView().setMenuGravity(paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigationrail\NavigationRailView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */