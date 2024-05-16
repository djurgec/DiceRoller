package com.google.android.material.appbar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior<View> {
  private int overlayTop;
  
  final Rect tempRect1 = new Rect();
  
  final Rect tempRect2 = new Rect();
  
  private int verticalLayoutGap = 0;
  
  public HeaderScrollingViewBehavior() {}
  
  public HeaderScrollingViewBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private static int resolveGravity(int paramInt) {
    if (paramInt == 0)
      paramInt = 8388659; 
    return paramInt;
  }
  
  abstract View findFirstDependency(List<View> paramList);
  
  final int getOverlapPixelsForOffset(View paramView) {
    int j = this.overlayTop;
    int i = 0;
    if (j != 0) {
      float f = getOverlapRatioForOffset(paramView);
      i = this.overlayTop;
      i = MathUtils.clamp((int)(f * i), 0, i);
    } 
    return i;
  }
  
  float getOverlapRatioForOffset(View paramView) {
    return 1.0F;
  }
  
  public final int getOverlayTop() {
    return this.overlayTop;
  }
  
  int getScrollRange(View paramView) {
    return paramView.getMeasuredHeight();
  }
  
  final int getVerticalLayoutGap() {
    return this.verticalLayoutGap;
  }
  
  protected void layoutChild(CoordinatorLayout paramCoordinatorLayout, View paramView, int paramInt) {
    Rect rect;
    View view = findFirstDependency(paramCoordinatorLayout.getDependencies(paramView));
    if (view != null) {
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)paramView.getLayoutParams();
      Rect rect1 = this.tempRect1;
      rect1.set(paramCoordinatorLayout.getPaddingLeft() + layoutParams.leftMargin, view.getBottom() + layoutParams.topMargin, paramCoordinatorLayout.getWidth() - paramCoordinatorLayout.getPaddingRight() - layoutParams.rightMargin, paramCoordinatorLayout.getHeight() + view.getBottom() - paramCoordinatorLayout.getPaddingBottom() - layoutParams.bottomMargin);
      WindowInsetsCompat windowInsetsCompat = paramCoordinatorLayout.getLastWindowInsets();
      if (windowInsetsCompat != null && ViewCompat.getFitsSystemWindows((View)paramCoordinatorLayout) && !ViewCompat.getFitsSystemWindows(paramView)) {
        rect1.left += windowInsetsCompat.getSystemWindowInsetLeft();
        rect1.right -= windowInsetsCompat.getSystemWindowInsetRight();
      } 
      rect = this.tempRect2;
      GravityCompat.apply(resolveGravity(layoutParams.gravity), paramView.getMeasuredWidth(), paramView.getMeasuredHeight(), rect1, rect, paramInt);
      paramInt = getOverlapPixelsForOffset(view);
      paramView.layout(rect.left, rect.top - paramInt, rect.right, rect.bottom - paramInt);
      this.verticalLayoutGap = rect.top - view.getBottom();
    } else {
      super.layoutChild((CoordinatorLayout)rect, paramView, paramInt);
      this.verticalLayoutGap = 0;
    } 
  }
  
  public boolean onMeasureChild(CoordinatorLayout paramCoordinatorLayout, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = (paramView.getLayoutParams()).height;
    if (i == -1 || i == -2) {
      View view = findFirstDependency(paramCoordinatorLayout.getDependencies(paramView));
      if (view != null) {
        int j = View.MeasureSpec.getSize(paramInt3);
        if (j > 0) {
          paramInt3 = j;
          if (ViewCompat.getFitsSystemWindows(view)) {
            WindowInsetsCompat windowInsetsCompat = paramCoordinatorLayout.getLastWindowInsets();
            paramInt3 = j;
            if (windowInsetsCompat != null)
              paramInt3 = j + windowInsetsCompat.getSystemWindowInsetTop() + windowInsetsCompat.getSystemWindowInsetBottom(); 
          } 
        } else {
          paramInt3 = paramCoordinatorLayout.getHeight();
        } 
        paramInt3 = getScrollRange(view) + paramInt3;
        j = view.getMeasuredHeight();
        if (shouldHeaderOverlapScrollingChild()) {
          paramView.setTranslationY(-j);
        } else {
          paramInt3 -= j;
        } 
        if (i == -1) {
          j = 1073741824;
        } else {
          j = Integer.MIN_VALUE;
        } 
        paramCoordinatorLayout.onMeasureChild(paramView, paramInt1, paramInt2, View.MeasureSpec.makeMeasureSpec(paramInt3, j), paramInt4);
        return true;
      } 
    } 
    return false;
  }
  
  public final void setOverlayTop(int paramInt) {
    this.overlayTop = paramInt;
  }
  
  protected boolean shouldHeaderOverlapScrollingChild() {
    return false;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\HeaderScrollingViewBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */