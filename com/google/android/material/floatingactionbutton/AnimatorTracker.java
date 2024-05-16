package com.google.android.material.floatingactionbutton;

import android.animation.Animator;

class AnimatorTracker {
  private Animator currentAnimator;
  
  public void cancelCurrent() {
    Animator animator = this.currentAnimator;
    if (animator != null)
      animator.cancel(); 
  }
  
  public void clear() {
    this.currentAnimator = null;
  }
  
  public void onNextAnimationStart(Animator paramAnimator) {
    cancelCurrent();
    this.currentAnimator = paramAnimator;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\AnimatorTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */