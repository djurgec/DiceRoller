package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ViewUtils;

class TabIndicatorInterpolator {
  private static final int MIN_INDICATOR_WIDTH = 24;
  
  static RectF calculateIndicatorWidthForTab(TabLayout paramTabLayout, View paramView) {
    return (paramView == null) ? new RectF() : ((!paramTabLayout.isTabIndicatorFullWidth() && paramView instanceof TabLayout.TabView) ? calculateTabViewContentBounds((TabLayout.TabView)paramView, 24) : new RectF(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()));
  }
  
  static RectF calculateTabViewContentBounds(TabLayout.TabView paramTabView, int paramInt) {
    int i = paramTabView.getContentWidth();
    int k = paramTabView.getContentHeight();
    int j = (int)ViewUtils.dpToPx(paramTabView.getContext(), paramInt);
    paramInt = i;
    if (i < j)
      paramInt = j; 
    i = (paramTabView.getLeft() + paramTabView.getRight()) / 2;
    j = (paramTabView.getTop() + paramTabView.getBottom()) / 2;
    int m = paramInt / 2;
    k /= 2;
    paramInt /= 2;
    int n = i / 2;
    return new RectF((i - m), (j - k), (paramInt + i), (n + j));
  }
  
  void setIndicatorBoundsForOffset(TabLayout paramTabLayout, View paramView1, View paramView2, float paramFloat, Drawable paramDrawable) {
    RectF rectF2 = calculateIndicatorWidthForTab(paramTabLayout, paramView1);
    RectF rectF1 = calculateIndicatorWidthForTab(paramTabLayout, paramView2);
    paramDrawable.setBounds(AnimationUtils.lerp((int)rectF2.left, (int)rectF1.left, paramFloat), (paramDrawable.getBounds()).top, AnimationUtils.lerp((int)rectF2.right, (int)rectF1.right, paramFloat), (paramDrawable.getBounds()).bottom);
  }
  
  void setIndicatorBoundsForTab(TabLayout paramTabLayout, View paramView, Drawable paramDrawable) {
    RectF rectF = calculateIndicatorWidthForTab(paramTabLayout, paramView);
    paramDrawable.setBounds((int)rectF.left, (paramDrawable.getBounds()).top, (int)rectF.right, (paramDrawable.getBounds()).bottom);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\tabs\TabIndicatorInterpolator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */