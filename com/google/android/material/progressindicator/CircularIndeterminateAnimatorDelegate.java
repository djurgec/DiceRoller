package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.util.Property;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.color.MaterialColors;

final class CircularIndeterminateAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
  private static final Property<CircularIndeterminateAnimatorDelegate, Float> ANIMATION_FRACTION;
  
  private static final Property<CircularIndeterminateAnimatorDelegate, Float> COMPLETE_END_FRACTION;
  
  private static final int CONSTANT_ROTATION_DEGREES = 1520;
  
  private static final int[] DELAY_TO_COLLAPSE_IN_MS;
  
  private static final int[] DELAY_TO_EXPAND_IN_MS = new int[] { 0, 1350, 2700, 4050 };
  
  private static final int[] DELAY_TO_FADE_IN_MS;
  
  private static final int DURATION_TO_COLLAPSE_IN_MS = 667;
  
  private static final int DURATION_TO_COMPLETE_END_IN_MS = 333;
  
  private static final int DURATION_TO_EXPAND_IN_MS = 667;
  
  private static final int DURATION_TO_FADE_IN_MS = 333;
  
  private static final int EXTRA_DEGREES_PER_CYCLE = 250;
  
  private static final int TAIL_DEGREES_OFFSET = -20;
  
  private static final int TOTAL_CYCLES = 4;
  
  private static final int TOTAL_DURATION_IN_MS = 5400;
  
  private float animationFraction;
  
  private ObjectAnimator animator;
  
  Animatable2Compat.AnimationCallback animatorCompleteCallback = null;
  
  private final BaseProgressIndicatorSpec baseSpec;
  
  private ObjectAnimator completeEndAnimator;
  
  private float completeEndFraction;
  
  private int indicatorColorIndexOffset = 0;
  
  private final FastOutSlowInInterpolator interpolator;
  
  static {
    DELAY_TO_COLLAPSE_IN_MS = new int[] { 667, 2017, 3367, 4717 };
    DELAY_TO_FADE_IN_MS = new int[] { 1000, 2350, 3700, 5050 };
    ANIMATION_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "animationFraction") {
        public Float get(CircularIndeterminateAnimatorDelegate param1CircularIndeterminateAnimatorDelegate) {
          return Float.valueOf(param1CircularIndeterminateAnimatorDelegate.getAnimationFraction());
        }
        
        public void set(CircularIndeterminateAnimatorDelegate param1CircularIndeterminateAnimatorDelegate, Float param1Float) {
          param1CircularIndeterminateAnimatorDelegate.setAnimationFraction(param1Float.floatValue());
        }
      };
    COMPLETE_END_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "completeEndFraction") {
        public Float get(CircularIndeterminateAnimatorDelegate param1CircularIndeterminateAnimatorDelegate) {
          return Float.valueOf(param1CircularIndeterminateAnimatorDelegate.getCompleteEndFraction());
        }
        
        public void set(CircularIndeterminateAnimatorDelegate param1CircularIndeterminateAnimatorDelegate, Float param1Float) {
          param1CircularIndeterminateAnimatorDelegate.setCompleteEndFraction(param1Float.floatValue());
        }
      };
  }
  
  public CircularIndeterminateAnimatorDelegate(CircularProgressIndicatorSpec paramCircularProgressIndicatorSpec) {
    super(1);
    this.baseSpec = paramCircularProgressIndicatorSpec;
    this.interpolator = new FastOutSlowInInterpolator();
  }
  
  private float getAnimationFraction() {
    return this.animationFraction;
  }
  
  private float getCompleteEndFraction() {
    return this.completeEndFraction;
  }
  
  private void maybeInitializeAnimators() {
    if (this.animator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, new float[] { 0.0F, 1.0F });
      this.animator = objectAnimator;
      objectAnimator.setDuration(5400L);
      this.animator.setInterpolator(null);
      this.animator.setRepeatCount(-1);
      this.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final CircularIndeterminateAnimatorDelegate this$0;
            
            public void onAnimationRepeat(Animator param1Animator) {
              super.onAnimationRepeat(param1Animator);
              CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
              CircularIndeterminateAnimatorDelegate.access$002(circularIndeterminateAnimatorDelegate, (circularIndeterminateAnimatorDelegate.indicatorColorIndexOffset + 4) % CircularIndeterminateAnimatorDelegate.this.baseSpec.indicatorColors.length);
            }
          });
    } 
    if (this.completeEndAnimator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, COMPLETE_END_FRACTION, new float[] { 0.0F, 1.0F });
      this.completeEndAnimator = objectAnimator;
      objectAnimator.setDuration(333L);
      this.completeEndAnimator.setInterpolator((TimeInterpolator)this.interpolator);
      this.completeEndAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final CircularIndeterminateAnimatorDelegate this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              CircularIndeterminateAnimatorDelegate.this.cancelAnimatorImmediately();
              CircularIndeterminateAnimatorDelegate.this.animatorCompleteCallback.onAnimationEnd(CircularIndeterminateAnimatorDelegate.this.drawable);
            }
          });
    } 
  }
  
  private void maybeUpdateSegmentColors(int paramInt) {
    for (int i = 0; i < 4; i++) {
      float f = getFractionInRange(paramInt, DELAY_TO_FADE_IN_MS[i], 333);
      if (f >= 0.0F && f <= 1.0F) {
        i = (this.indicatorColorIndexOffset + i) % this.baseSpec.indicatorColors.length;
        int j = this.baseSpec.indicatorColors.length;
        paramInt = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[i], this.drawable.getAlpha());
        i = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[(i + 1) % j], this.drawable.getAlpha());
        f = this.interpolator.getInterpolation(f);
        this.segmentColors[0] = ArgbEvaluatorCompat.getInstance().evaluate(f, Integer.valueOf(paramInt), Integer.valueOf(i)).intValue();
        break;
      } 
    } 
  }
  
  private void setCompleteEndFraction(float paramFloat) {
    this.completeEndFraction = paramFloat;
  }
  
  private void updateSegmentPositions(int paramInt) {
    this.segmentPositions[0] = this.animationFraction * 1520.0F - 20.0F;
    this.segmentPositions[1] = this.animationFraction * 1520.0F;
    for (byte b = 0; b < 4; b++) {
      float f = getFractionInRange(paramInt, DELAY_TO_EXPAND_IN_MS[b], 667);
      float[] arrayOfFloat1 = this.segmentPositions;
      arrayOfFloat1[1] = arrayOfFloat1[1] + this.interpolator.getInterpolation(f) * 250.0F;
      f = getFractionInRange(paramInt, DELAY_TO_COLLAPSE_IN_MS[b], 667);
      arrayOfFloat1 = this.segmentPositions;
      arrayOfFloat1[0] = arrayOfFloat1[0] + this.interpolator.getInterpolation(f) * 250.0F;
    } 
    float[] arrayOfFloat = this.segmentPositions;
    arrayOfFloat[0] = arrayOfFloat[0] + (this.segmentPositions[1] - this.segmentPositions[0]) * this.completeEndFraction;
    arrayOfFloat = this.segmentPositions;
    arrayOfFloat[0] = arrayOfFloat[0] / 360.0F;
    arrayOfFloat = this.segmentPositions;
    arrayOfFloat[1] = arrayOfFloat[1] / 360.0F;
  }
  
  void cancelAnimatorImmediately() {
    ObjectAnimator objectAnimator = this.animator;
    if (objectAnimator != null)
      objectAnimator.cancel(); 
  }
  
  public void invalidateSpecValues() {
    resetPropertiesForNewStart();
  }
  
  public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    this.animatorCompleteCallback = paramAnimationCallback;
  }
  
  void requestCancelAnimatorAfterCurrentCycle() {
    if (this.completeEndAnimator.isRunning())
      return; 
    if (this.drawable.isVisible()) {
      this.completeEndAnimator.start();
    } else {
      cancelAnimatorImmediately();
    } 
  }
  
  void resetPropertiesForNewStart() {
    this.indicatorColorIndexOffset = 0;
    this.segmentColors[0] = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.drawable.getAlpha());
    this.completeEndFraction = 0.0F;
  }
  
  void setAnimationFraction(float paramFloat) {
    this.animationFraction = paramFloat;
    int i = (int)(5400.0F * paramFloat);
    updateSegmentPositions(i);
    maybeUpdateSegmentColors(i);
    this.drawable.invalidateSelf();
  }
  
  void startAnimator() {
    maybeInitializeAnimators();
    resetPropertiesForNewStart();
    this.animator.start();
  }
  
  public void unregisterAnimatorsCompleteCallback() {
    this.animatorCompleteCallback = null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\CircularIndeterminateAnimatorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */