package com.google.android.material.transition;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.TransitionValues;
import com.google.android.material.R;

public final class MaterialFadeThrough extends MaterialVisibility<FadeThroughProvider> {
  private static final float DEFAULT_START_SCALE = 0.92F;
  
  private static final int DEFAULT_THEMED_DURATION_ATTR = R.attr.motionDurationLong1;
  
  private static final int DEFAULT_THEMED_EASING_ATTR = R.attr.motionEasingStandard;
  
  public MaterialFadeThrough() {
    super(createPrimaryAnimatorProvider(), createSecondaryAnimatorProvider());
  }
  
  private static FadeThroughProvider createPrimaryAnimatorProvider() {
    return new FadeThroughProvider();
  }
  
  private static VisibilityAnimatorProvider createSecondaryAnimatorProvider() {
    ScaleProvider scaleProvider = new ScaleProvider();
    scaleProvider.setScaleOnDisappear(false);
    scaleProvider.setIncomingStartScale(0.92F);
    return scaleProvider;
  }
  
  int getDurationThemeAttrResId(boolean paramBoolean) {
    return DEFAULT_THEMED_DURATION_ATTR;
  }
  
  int getEasingThemeAttrResId(boolean paramBoolean) {
    return DEFAULT_THEMED_EASING_ATTR;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\MaterialFadeThrough.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */