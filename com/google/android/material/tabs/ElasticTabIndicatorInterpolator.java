package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.animation.AnimationUtils;

class ElasticTabIndicatorInterpolator extends TabIndicatorInterpolator {
  private static float accInterp(float paramFloat) {
    return (float)(1.0D - Math.cos(paramFloat * Math.PI / 2.0D));
  }
  
  private static float decInterp(float paramFloat) {
    return (float)Math.sin(paramFloat * Math.PI / 2.0D);
  }
  
  void setIndicatorBoundsForOffset(TabLayout paramTabLayout, View paramView1, View paramView2, float paramFloat, Drawable paramDrawable) {
    float f;
    boolean bool;
    RectF rectF2 = calculateIndicatorWidthForTab(paramTabLayout, paramView1);
    RectF rectF1 = calculateIndicatorWidthForTab(paramTabLayout, paramView2);
    if (rectF2.left < rectF1.left) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      f = accInterp(paramFloat);
      float f1 = decInterp(paramFloat);
      paramFloat = f;
      f = f1;
    } else {
      float f1 = decInterp(paramFloat);
      f = accInterp(paramFloat);
      paramFloat = f1;
    } 
    paramDrawable.setBounds(AnimationUtils.lerp((int)rectF2.left, (int)rectF1.left, paramFloat), (paramDrawable.getBounds()).top, AnimationUtils.lerp((int)rectF2.right, (int)rectF1.right, f), (paramDrawable.getBounds()).bottom);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\tabs\ElasticTabIndicatorInterpolator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */