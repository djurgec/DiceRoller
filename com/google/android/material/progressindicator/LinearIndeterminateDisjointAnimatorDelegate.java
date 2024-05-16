package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Property;
import android.view.animation.Interpolator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import java.util.Arrays;

final class LinearIndeterminateDisjointAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
  private static final Property<LinearIndeterminateDisjointAnimatorDelegate, Float> ANIMATION_FRACTION;
  
  private static final int[] DELAY_TO_MOVE_SEGMENT_ENDS;
  
  private static final int[] DURATION_TO_MOVE_SEGMENT_ENDS = new int[] { 533, 567, 850, 750 };
  
  private static final int TOTAL_DURATION_IN_MS = 1800;
  
  private float animationFraction;
  
  private ObjectAnimator animator;
  
  Animatable2Compat.AnimationCallback animatorCompleteCallback = null;
  
  private boolean animatorCompleteEndRequested;
  
  private final BaseProgressIndicatorSpec baseSpec;
  
  private boolean dirtyColors;
  
  private int indicatorColorIndex = 0;
  
  private final Interpolator[] interpolatorArray;
  
  static {
    DELAY_TO_MOVE_SEGMENT_ENDS = new int[] { 1267, 1000, 333, 0 };
    ANIMATION_FRACTION = new Property<LinearIndeterminateDisjointAnimatorDelegate, Float>(Float.class, "animationFraction") {
        public Float get(LinearIndeterminateDisjointAnimatorDelegate param1LinearIndeterminateDisjointAnimatorDelegate) {
          return Float.valueOf(param1LinearIndeterminateDisjointAnimatorDelegate.getAnimationFraction());
        }
        
        public void set(LinearIndeterminateDisjointAnimatorDelegate param1LinearIndeterminateDisjointAnimatorDelegate, Float param1Float) {
          param1LinearIndeterminateDisjointAnimatorDelegate.setAnimationFraction(param1Float.floatValue());
        }
      };
  }
  
  public LinearIndeterminateDisjointAnimatorDelegate(Context paramContext, LinearProgressIndicatorSpec paramLinearProgressIndicatorSpec) {
    super(2);
    this.baseSpec = paramLinearProgressIndicatorSpec;
    this.interpolatorArray = new Interpolator[] { AnimationUtilsCompat.loadInterpolator(paramContext, R.animator.linear_indeterminate_line1_head_interpolator), AnimationUtilsCompat.loadInterpolator(paramContext, R.animator.linear_indeterminate_line1_tail_interpolator), AnimationUtilsCompat.loadInterpolator(paramContext, R.animator.linear_indeterminate_line2_head_interpolator), AnimationUtilsCompat.loadInterpolator(paramContext, R.animator.linear_indeterminate_line2_tail_interpolator) };
  }
  
  private float getAnimationFraction() {
    return this.animationFraction;
  }
  
  private void maybeInitializeAnimators() {
    if (this.animator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, new float[] { 0.0F, 1.0F });
      this.animator = objectAnimator;
      objectAnimator.setDuration(1800L);
      this.animator.setInterpolator(null);
      this.animator.setRepeatCount(-1);
      this.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final LinearIndeterminateDisjointAnimatorDelegate this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              if (LinearIndeterminateDisjointAnimatorDelegate.this.animatorCompleteEndRequested) {
                LinearIndeterminateDisjointAnimatorDelegate.this.animator.setRepeatCount(-1);
                LinearIndeterminateDisjointAnimatorDelegate.this.animatorCompleteCallback.onAnimationEnd(LinearIndeterminateDisjointAnimatorDelegate.this.drawable);
                LinearIndeterminateDisjointAnimatorDelegate.access$302(LinearIndeterminateDisjointAnimatorDelegate.this, false);
              } 
            }
            
            public void onAnimationRepeat(Animator param1Animator) {
              super.onAnimationRepeat(param1Animator);
              LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate = LinearIndeterminateDisjointAnimatorDelegate.this;
              LinearIndeterminateDisjointAnimatorDelegate.access$002(linearIndeterminateDisjointAnimatorDelegate, (linearIndeterminateDisjointAnimatorDelegate.indicatorColorIndex + 1) % LinearIndeterminateDisjointAnimatorDelegate.this.baseSpec.indicatorColors.length);
              LinearIndeterminateDisjointAnimatorDelegate.access$202(LinearIndeterminateDisjointAnimatorDelegate.this, true);
            }
          });
    } 
  }
  
  private void maybeUpdateSegmentColors() {
    if (this.dirtyColors) {
      Arrays.fill(this.segmentColors, MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[this.indicatorColorIndex], this.drawable.getAlpha()));
      this.dirtyColors = false;
    } 
  }
  
  private void updateSegmentPositions(int paramInt) {
    for (byte b = 0; b < 4; b++) {
      float f = getFractionInRange(paramInt, DELAY_TO_MOVE_SEGMENT_ENDS[b], DURATION_TO_MOVE_SEGMENT_ENDS[b]);
      f = this.interpolatorArray[b].getInterpolation(f);
      this.segmentPositions[b] = Math.max(0.0F, Math.min(1.0F, f));
    } 
  }
  
  public void cancelAnimatorImmediately() {
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
  
  public void requestCancelAnimatorAfterCurrentCycle() {
    if (this.drawable.isVisible()) {
      this.animatorCompleteEndRequested = true;
      this.animator.setRepeatCount(0);
    } else {
      cancelAnimatorImmediately();
    } 
  }
  
  void resetPropertiesForNewStart() {
    this.indicatorColorIndex = 0;
    int i = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.drawable.getAlpha());
    this.segmentColors[0] = i;
    this.segmentColors[1] = i;
  }
  
  void setAnimationFraction(float paramFloat) {
    this.animationFraction = paramFloat;
    updateSegmentPositions((int)(1800.0F * paramFloat));
    maybeUpdateSegmentColors();
    this.drawable.invalidateSelf();
  }
  
  public void startAnimator() {
    maybeInitializeAnimators();
    resetPropertiesForNewStart();
    this.animator.start();
  }
  
  public void unregisterAnimatorsCompleteCallback() {
    this.animatorCompleteCallback = null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\LinearIndeterminateDisjointAnimatorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */