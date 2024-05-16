package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import com.google.android.material.animation.MotionSpec;
import java.util.List;

interface MotionStrategy {
  void addAnimationListener(Animator.AnimatorListener paramAnimatorListener);
  
  AnimatorSet createAnimator();
  
  MotionSpec getCurrentMotionSpec();
  
  int getDefaultMotionSpecResource();
  
  List<Animator.AnimatorListener> getListeners();
  
  MotionSpec getMotionSpec();
  
  void onAnimationCancel();
  
  void onAnimationEnd();
  
  void onAnimationStart(Animator paramAnimator);
  
  void onChange(ExtendedFloatingActionButton.OnChangedCallback paramOnChangedCallback);
  
  void performNow();
  
  void removeAnimationListener(Animator.AnimatorListener paramAnimatorListener);
  
  void setMotionSpec(MotionSpec paramMotionSpec);
  
  boolean shouldCancel();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\MotionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */