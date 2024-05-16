package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public final class FadeProvider implements VisibilityAnimatorProvider {
  private float incomingEndThreshold = 1.0F;
  
  private static Animator createFadeAnimator(final View view, final float startValue, final float endValue, final float startFraction, final float endFraction, final float originalAlpha) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final float val$endFraction;
          
          final float val$endValue;
          
          final float val$startFraction;
          
          final float val$startValue;
          
          final View val$view;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            view.setAlpha(TransitionUtils.lerp(startValue, endValue, startFraction, endFraction, f));
          }
        });
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final float val$originalAlpha;
          
          final View val$view;
          
          public void onAnimationEnd(Animator param1Animator) {
            view.setAlpha(originalAlpha);
          }
        });
    return (Animator)valueAnimator;
  }
  
  public Animator createAppear(ViewGroup paramViewGroup, View paramView) {
    float f;
    if (paramView.getAlpha() == 0.0F) {
      f = 1.0F;
    } else {
      f = paramView.getAlpha();
    } 
    return createFadeAnimator(paramView, 0.0F, f, 0.0F, this.incomingEndThreshold, f);
  }
  
  public Animator createDisappear(ViewGroup paramViewGroup, View paramView) {
    float f;
    if (paramView.getAlpha() == 0.0F) {
      f = 1.0F;
    } else {
      f = paramView.getAlpha();
    } 
    return createFadeAnimator(paramView, f, 0.0F, 0.0F, 1.0F, f);
  }
  
  public float getIncomingEndThreshold() {
    return this.incomingEndThreshold;
  }
  
  public void setIncomingEndThreshold(float paramFloat) {
    this.incomingEndThreshold = paramFloat;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\FadeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */