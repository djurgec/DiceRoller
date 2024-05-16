package com.google.android.material.transition;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.TransitionValues;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MaterialSharedAxis extends MaterialVisibility<VisibilityAnimatorProvider> {
  private static final int DEFAULT_THEMED_DURATION_ATTR = R.attr.motionDurationLong1;
  
  private static final int DEFAULT_THEMED_EASING_ATTR = R.attr.motionEasingStandard;
  
  public static final int X = 0;
  
  public static final int Y = 1;
  
  public static final int Z = 2;
  
  private final int axis;
  
  private final boolean forward;
  
  public MaterialSharedAxis(int paramInt, boolean paramBoolean) {
    super(createPrimaryAnimatorProvider(paramInt, paramBoolean), createSecondaryAnimatorProvider());
    this.axis = paramInt;
    this.forward = paramBoolean;
  }
  
  private static VisibilityAnimatorProvider createPrimaryAnimatorProvider(int paramInt, boolean paramBoolean) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Invalid axis: " + paramInt);
      case 2:
        return new ScaleProvider(paramBoolean);
      case 1:
        if (paramBoolean) {
          paramInt = 80;
        } else {
          paramInt = 48;
        } 
        return new SlideDistanceProvider(paramInt);
      case 0:
        break;
    } 
    if (paramBoolean) {
      paramInt = 8388613;
    } else {
      paramInt = 8388611;
    } 
    return new SlideDistanceProvider(paramInt);
  }
  
  private static VisibilityAnimatorProvider createSecondaryAnimatorProvider() {
    return new FadeThroughProvider();
  }
  
  public int getAxis() {
    return this.axis;
  }
  
  int getDurationThemeAttrResId(boolean paramBoolean) {
    return DEFAULT_THEMED_DURATION_ATTR;
  }
  
  int getEasingThemeAttrResId(boolean paramBoolean) {
    return DEFAULT_THEMED_EASING_ATTR;
  }
  
  public boolean isForward() {
    return this.forward;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Axis {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\MaterialSharedAxis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */