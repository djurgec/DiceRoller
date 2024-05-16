package com.google.android.material.progressindicator;

import android.animation.Animator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

abstract class IndeterminateAnimatorDelegate<T extends Animator> {
  protected IndeterminateDrawable drawable;
  
  protected final int[] segmentColors;
  
  protected final float[] segmentPositions;
  
  protected IndeterminateAnimatorDelegate(int paramInt) {
    this.segmentPositions = new float[paramInt * 2];
    this.segmentColors = new int[paramInt];
  }
  
  abstract void cancelAnimatorImmediately();
  
  protected float getFractionInRange(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt1 - paramInt2) / paramInt3;
  }
  
  public abstract void invalidateSpecValues();
  
  public abstract void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback paramAnimationCallback);
  
  protected void registerDrawable(IndeterminateDrawable paramIndeterminateDrawable) {
    this.drawable = paramIndeterminateDrawable;
  }
  
  abstract void requestCancelAnimatorAfterCurrentCycle();
  
  abstract void startAnimator();
  
  public abstract void unregisterAnimatorsCompleteCallback();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\IndeterminateAnimatorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */