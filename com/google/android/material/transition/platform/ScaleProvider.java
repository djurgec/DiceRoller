package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;

public final class ScaleProvider implements VisibilityAnimatorProvider {
  private boolean growing;
  
  private float incomingEndScale = 1.0F;
  
  private float incomingStartScale = 0.8F;
  
  private float outgoingEndScale = 1.1F;
  
  private float outgoingStartScale = 1.0F;
  
  private boolean scaleOnDisappear = true;
  
  public ScaleProvider() {
    this(true);
  }
  
  public ScaleProvider(boolean paramBoolean) {
    this.growing = paramBoolean;
  }
  
  private static Animator createScaleAnimator(final View view, float paramFloat1, float paramFloat2) {
    final float originalScaleX = view.getScaleX();
    final float originalScaleY = view.getScaleY();
    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(View.SCALE_X, new float[] { f1 * paramFloat1, f1 * paramFloat2 }), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[] { f2 * paramFloat1, f2 * paramFloat2 }) });
    objectAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final float val$originalScaleX;
          
          final float val$originalScaleY;
          
          final View val$view;
          
          public void onAnimationEnd(Animator param1Animator) {
            view.setScaleX(originalScaleX);
            view.setScaleY(originalScaleY);
          }
        });
    return (Animator)objectAnimator;
  }
  
  public Animator createAppear(ViewGroup paramViewGroup, View paramView) {
    return this.growing ? createScaleAnimator(paramView, this.incomingStartScale, this.incomingEndScale) : createScaleAnimator(paramView, this.outgoingEndScale, this.outgoingStartScale);
  }
  
  public Animator createDisappear(ViewGroup paramViewGroup, View paramView) {
    return !this.scaleOnDisappear ? null : (this.growing ? createScaleAnimator(paramView, this.outgoingStartScale, this.outgoingEndScale) : createScaleAnimator(paramView, this.incomingEndScale, this.incomingStartScale));
  }
  
  public float getIncomingEndScale() {
    return this.incomingEndScale;
  }
  
  public float getIncomingStartScale() {
    return this.incomingStartScale;
  }
  
  public float getOutgoingEndScale() {
    return this.outgoingEndScale;
  }
  
  public float getOutgoingStartScale() {
    return this.outgoingStartScale;
  }
  
  public boolean isGrowing() {
    return this.growing;
  }
  
  public boolean isScaleOnDisappear() {
    return this.scaleOnDisappear;
  }
  
  public void setGrowing(boolean paramBoolean) {
    this.growing = paramBoolean;
  }
  
  public void setIncomingEndScale(float paramFloat) {
    this.incomingEndScale = paramFloat;
  }
  
  public void setIncomingStartScale(float paramFloat) {
    this.incomingStartScale = paramFloat;
  }
  
  public void setOutgoingEndScale(float paramFloat) {
    this.outgoingEndScale = paramFloat;
  }
  
  public void setOutgoingStartScale(float paramFloat) {
    this.outgoingStartScale = paramFloat;
  }
  
  public void setScaleOnDisappear(boolean paramBoolean) {
    this.scaleOnDisappear = paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\ScaleProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */