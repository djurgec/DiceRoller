package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.TransitionValues;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;

public final class MaterialFade extends MaterialVisibility<FadeProvider> {
  private static final float DEFAULT_FADE_END_THRESHOLD_ENTER = 0.3F;
  
  private static final float DEFAULT_START_SCALE = 0.8F;
  
  private static final int DEFAULT_THEMED_EASING_ATTR;
  
  private static final int DEFAULT_THEMED_INCOMING_DURATION_ATTR = R.attr.motionDurationShort2;
  
  private static final int DEFAULT_THEMED_OUTGOING_DURATION_ATTR = R.attr.motionDurationShort1;
  
  static {
    DEFAULT_THEMED_EASING_ATTR = R.attr.motionEasingLinear;
  }
  
  public MaterialFade() {
    super(createPrimaryAnimatorProvider(), createSecondaryAnimatorProvider());
  }
  
  private static FadeProvider createPrimaryAnimatorProvider() {
    FadeProvider fadeProvider = new FadeProvider();
    fadeProvider.setIncomingEndThreshold(0.3F);
    return fadeProvider;
  }
  
  private static VisibilityAnimatorProvider createSecondaryAnimatorProvider() {
    ScaleProvider scaleProvider = new ScaleProvider();
    scaleProvider.setScaleOnDisappear(false);
    scaleProvider.setIncomingStartScale(0.8F);
    return scaleProvider;
  }
  
  TimeInterpolator getDefaultEasingInterpolator(boolean paramBoolean) {
    return AnimationUtils.LINEAR_INTERPOLATOR;
  }
  
  int getDurationThemeAttrResId(boolean paramBoolean) {
    int i;
    if (paramBoolean) {
      i = DEFAULT_THEMED_INCOMING_DURATION_ATTR;
    } else {
      i = DEFAULT_THEMED_OUTGOING_DURATION_ATTR;
    } 
    return i;
  }
  
  int getEasingThemeAttrResId(boolean paramBoolean) {
    return DEFAULT_THEMED_EASING_ATTR;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\MaterialFade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */