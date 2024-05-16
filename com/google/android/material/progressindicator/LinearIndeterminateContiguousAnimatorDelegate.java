package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.color.MaterialColors;
import java.util.Arrays;

final class LinearIndeterminateContiguousAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
  private static final Property<LinearIndeterminateContiguousAnimatorDelegate, Float> ANIMATION_FRACTION = new Property<LinearIndeterminateContiguousAnimatorDelegate, Float>(Float.class, "animationFraction") {
      public Float get(LinearIndeterminateContiguousAnimatorDelegate param1LinearIndeterminateContiguousAnimatorDelegate) {
        return Float.valueOf(param1LinearIndeterminateContiguousAnimatorDelegate.getAnimationFraction());
      }
      
      public void set(LinearIndeterminateContiguousAnimatorDelegate param1LinearIndeterminateContiguousAnimatorDelegate, Float param1Float) {
        param1LinearIndeterminateContiguousAnimatorDelegate.setAnimationFraction(param1Float.floatValue());
      }
    };
  
  private static final int DURATION_PER_CYCLE_IN_MS = 333;
  
  private static final int TOTAL_DURATION_IN_MS = 667;
  
  private float animationFraction;
  
  private ObjectAnimator animator;
  
  private final BaseProgressIndicatorSpec baseSpec;
  
  private boolean dirtyColors;
  
  private FastOutSlowInInterpolator interpolator;
  
  private int newIndicatorColorIndex = 1;
  
  public LinearIndeterminateContiguousAnimatorDelegate(LinearProgressIndicatorSpec paramLinearProgressIndicatorSpec) {
    super(3);
    this.baseSpec = paramLinearProgressIndicatorSpec;
    this.interpolator = new FastOutSlowInInterpolator();
  }
  
  private float getAnimationFraction() {
    return this.animationFraction;
  }
  
  private void maybeInitializeAnimators() {
    if (this.animator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, new float[] { 0.0F, 1.0F });
      this.animator = objectAnimator;
      objectAnimator.setDuration(333L);
      this.animator.setInterpolator(null);
      this.animator.setRepeatCount(-1);
      this.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final LinearIndeterminateContiguousAnimatorDelegate this$0;
            
            public void onAnimationRepeat(Animator param1Animator) {
              super.onAnimationRepeat(param1Animator);
              LinearIndeterminateContiguousAnimatorDelegate linearIndeterminateContiguousAnimatorDelegate = LinearIndeterminateContiguousAnimatorDelegate.this;
              LinearIndeterminateContiguousAnimatorDelegate.access$002(linearIndeterminateContiguousAnimatorDelegate, (linearIndeterminateContiguousAnimatorDelegate.newIndicatorColorIndex + 1) % LinearIndeterminateContiguousAnimatorDelegate.this.baseSpec.indicatorColors.length);
              LinearIndeterminateContiguousAnimatorDelegate.access$202(LinearIndeterminateContiguousAnimatorDelegate.this, true);
            }
          });
    } 
  }
  
  private void maybeUpdateSegmentColors() {
    if (this.dirtyColors && this.segmentPositions[3] < 1.0F) {
      this.segmentColors[2] = this.segmentColors[1];
      this.segmentColors[1] = this.segmentColors[0];
      this.segmentColors[0] = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[this.newIndicatorColorIndex], this.drawable.getAlpha());
      this.dirtyColors = false;
    } 
  }
  
  private void updateSegmentPositions(int paramInt) {
    this.segmentPositions[0] = 0.0F;
    float f1 = getFractionInRange(paramInt, 0, 667);
    float[] arrayOfFloat1 = this.segmentPositions;
    float[] arrayOfFloat2 = this.segmentPositions;
    float f2 = this.interpolator.getInterpolation(f1);
    arrayOfFloat2[2] = f2;
    arrayOfFloat1[1] = f2;
    arrayOfFloat2 = this.segmentPositions;
    arrayOfFloat1 = this.segmentPositions;
    f1 = this.interpolator.getInterpolation(f1 + 0.49925038F);
    arrayOfFloat1[4] = f1;
    arrayOfFloat2[3] = f1;
    this.segmentPositions[5] = 1.0F;
  }
  
  public void cancelAnimatorImmediately() {
    ObjectAnimator objectAnimator = this.animator;
    if (objectAnimator != null)
      objectAnimator.cancel(); 
  }
  
  public void invalidateSpecValues() {
    resetPropertiesForNewStart();
  }
  
  public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {}
  
  public void requestCancelAnimatorAfterCurrentCycle() {}
  
  void resetPropertiesForNewStart() {
    this.dirtyColors = true;
    this.newIndicatorColorIndex = 1;
    Arrays.fill(this.segmentColors, MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.drawable.getAlpha()));
  }
  
  void setAnimationFraction(float paramFloat) {
    this.animationFraction = paramFloat;
    updateSegmentPositions((int)(333.0F * paramFloat));
    maybeUpdateSegmentColors();
    this.drawable.invalidateSelf();
  }
  
  public void startAnimator() {
    maybeInitializeAnimators();
    resetPropertiesForNewStart();
    this.animator.start();
  }
  
  public void unregisterAnimatorsCompleteCallback() {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\LinearIndeterminateContiguousAnimatorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */