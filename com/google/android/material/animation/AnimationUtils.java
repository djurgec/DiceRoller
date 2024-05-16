package com.google.android.material.animation;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimationUtils {
  public static final TimeInterpolator DECELERATE_INTERPOLATOR;
  
  public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR;
  
  public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR;
  
  public static final TimeInterpolator LINEAR_INTERPOLATOR = (TimeInterpolator)new LinearInterpolator();
  
  public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR;
  
  static {
    FAST_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new FastOutSlowInInterpolator();
    FAST_OUT_LINEAR_IN_INTERPOLATOR = (TimeInterpolator)new FastOutLinearInInterpolator();
    LINEAR_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new LinearOutSlowInInterpolator();
    DECELERATE_INTERPOLATOR = (TimeInterpolator)new DecelerateInterpolator();
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    return (paramFloat5 < paramFloat3) ? paramFloat1 : ((paramFloat5 > paramFloat4) ? paramFloat2 : lerp(paramFloat1, paramFloat2, (paramFloat5 - paramFloat3) / (paramFloat4 - paramFloat3)));
  }
  
  public static int lerp(int paramInt1, int paramInt2, float paramFloat) {
    return Math.round((paramInt2 - paramInt1) * paramFloat) + paramInt1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\AnimationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */