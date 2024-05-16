package com.google.android.material.transition;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.TransitionValues;

public final class MaterialElevationScale extends MaterialVisibility<ScaleProvider> {
  private static final float DEFAULT_SCALE = 0.85F;
  
  private final boolean growing;
  
  public MaterialElevationScale(boolean paramBoolean) {
    super(createPrimaryAnimatorProvider(paramBoolean), createSecondaryAnimatorProvider());
    this.growing = paramBoolean;
  }
  
  private static ScaleProvider createPrimaryAnimatorProvider(boolean paramBoolean) {
    ScaleProvider scaleProvider = new ScaleProvider(paramBoolean);
    scaleProvider.setOutgoingEndScale(0.85F);
    scaleProvider.setIncomingStartScale(0.85F);
    return scaleProvider;
  }
  
  private static VisibilityAnimatorProvider createSecondaryAnimatorProvider() {
    return new FadeProvider();
  }
  
  public boolean isGrowing() {
    return this.growing;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\MaterialElevationScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */