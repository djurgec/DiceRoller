package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class DrawableWithAnimatedVisibilityChange extends Drawable implements Animatable2Compat {
  private static final boolean DEFAULT_DRAWABLE_RESTART = false;
  
  private static final int GROW_DURATION = 500;
  
  private static final Property<DrawableWithAnimatedVisibilityChange, Float> GROW_FRACTION = new Property<DrawableWithAnimatedVisibilityChange, Float>(Float.class, "growFraction") {
      public Float get(DrawableWithAnimatedVisibilityChange param1DrawableWithAnimatedVisibilityChange) {
        return Float.valueOf(param1DrawableWithAnimatedVisibilityChange.getGrowFraction());
      }
      
      public void set(DrawableWithAnimatedVisibilityChange param1DrawableWithAnimatedVisibilityChange, Float param1Float) {
        param1DrawableWithAnimatedVisibilityChange.setGrowFraction(param1Float.floatValue());
      }
    };
  
  private List<Animatable2Compat.AnimationCallback> animationCallbacks;
  
  AnimatorDurationScaleProvider animatorDurationScaleProvider;
  
  final BaseProgressIndicatorSpec baseSpec;
  
  final Context context;
  
  private float growFraction;
  
  private ValueAnimator hideAnimator;
  
  private boolean ignoreCallbacks;
  
  private Animatable2Compat.AnimationCallback internalAnimationCallback;
  
  private float mockGrowFraction;
  
  private boolean mockHideAnimationRunning;
  
  private boolean mockShowAnimationRunning;
  
  final Paint paint = new Paint();
  
  private ValueAnimator showAnimator;
  
  private int totalAlpha;
  
  DrawableWithAnimatedVisibilityChange(Context paramContext, BaseProgressIndicatorSpec paramBaseProgressIndicatorSpec) {
    this.context = paramContext;
    this.baseSpec = paramBaseProgressIndicatorSpec;
    this.animatorDurationScaleProvider = new AnimatorDurationScaleProvider();
    setAlpha(255);
  }
  
  private void dispatchAnimationEnd() {
    Animatable2Compat.AnimationCallback animationCallback = this.internalAnimationCallback;
    if (animationCallback != null)
      animationCallback.onAnimationEnd(this); 
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    if (list != null && !this.ignoreCallbacks) {
      Iterator<Animatable2Compat.AnimationCallback> iterator = list.iterator();
      while (iterator.hasNext())
        ((Animatable2Compat.AnimationCallback)iterator.next()).onAnimationEnd(this); 
    } 
  }
  
  private void dispatchAnimationStart() {
    Animatable2Compat.AnimationCallback animationCallback = this.internalAnimationCallback;
    if (animationCallback != null)
      animationCallback.onAnimationStart(this); 
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    if (list != null && !this.ignoreCallbacks) {
      Iterator<Animatable2Compat.AnimationCallback> iterator = list.iterator();
      while (iterator.hasNext())
        ((Animatable2Compat.AnimationCallback)iterator.next()).onAnimationStart(this); 
    } 
  }
  
  private void endAnimatorWithoutCallbacks(ValueAnimator... paramVarArgs) {
    boolean bool = this.ignoreCallbacks;
    this.ignoreCallbacks = true;
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      paramVarArgs[b].end(); 
    this.ignoreCallbacks = bool;
  }
  
  private void maybeInitializeAnimators() {
    if (this.showAnimator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, GROW_FRACTION, new float[] { 0.0F, 1.0F });
      this.showAnimator = (ValueAnimator)objectAnimator;
      objectAnimator.setDuration(500L);
      this.showAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      setShowAnimator(this.showAnimator);
    } 
    if (this.hideAnimator == null) {
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, GROW_FRACTION, new float[] { 1.0F, 0.0F });
      this.hideAnimator = (ValueAnimator)objectAnimator;
      objectAnimator.setDuration(500L);
      this.hideAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      setHideAnimator(this.hideAnimator);
    } 
  }
  
  private void setHideAnimator(ValueAnimator paramValueAnimator) {
    ValueAnimator valueAnimator = this.hideAnimator;
    if (valueAnimator == null || !valueAnimator.isRunning()) {
      this.hideAnimator = paramValueAnimator;
      paramValueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final DrawableWithAnimatedVisibilityChange this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              DrawableWithAnimatedVisibilityChange.this.setVisible(false, false);
              DrawableWithAnimatedVisibilityChange.this.dispatchAnimationEnd();
            }
          });
      return;
    } 
    throw new IllegalArgumentException("Cannot set hideAnimator while the current hideAnimator is running.");
  }
  
  private void setShowAnimator(ValueAnimator paramValueAnimator) {
    ValueAnimator valueAnimator = this.showAnimator;
    if (valueAnimator == null || !valueAnimator.isRunning()) {
      this.showAnimator = paramValueAnimator;
      paramValueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final DrawableWithAnimatedVisibilityChange this$0;
            
            public void onAnimationStart(Animator param1Animator) {
              super.onAnimationStart(param1Animator);
              DrawableWithAnimatedVisibilityChange.this.dispatchAnimationStart();
            }
          });
      return;
    } 
    throw new IllegalArgumentException("Cannot set showAnimator while the current showAnimator is running.");
  }
  
  public void clearAnimationCallbacks() {
    this.animationCallbacks.clear();
    this.animationCallbacks = null;
  }
  
  public int getAlpha() {
    return this.totalAlpha;
  }
  
  float getGrowFraction() {
    return (!this.baseSpec.isShowAnimationEnabled() && !this.baseSpec.isHideAnimationEnabled()) ? 1.0F : ((this.mockHideAnimationRunning || this.mockShowAnimationRunning) ? this.mockGrowFraction : this.growFraction);
  }
  
  ValueAnimator getHideAnimator() {
    return this.hideAnimator;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public boolean hideNow() {
    return setVisible(false, false, false);
  }
  
  public boolean isHiding() {
    boolean bool;
    ValueAnimator valueAnimator = this.hideAnimator;
    if ((valueAnimator != null && valueAnimator.isRunning()) || this.mockHideAnimationRunning) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isRunning() {
    return (isShowing() || isHiding());
  }
  
  public boolean isShowing() {
    boolean bool;
    ValueAnimator valueAnimator = this.showAnimator;
    if ((valueAnimator != null && valueAnimator.isRunning()) || this.mockShowAnimationRunning) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void registerAnimationCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    if (this.animationCallbacks == null)
      this.animationCallbacks = new ArrayList<>(); 
    if (!this.animationCallbacks.contains(paramAnimationCallback))
      this.animationCallbacks.add(paramAnimationCallback); 
  }
  
  public void setAlpha(int paramInt) {
    this.totalAlpha = paramInt;
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  void setGrowFraction(float paramFloat) {
    if (this.growFraction != paramFloat) {
      this.growFraction = paramFloat;
      invalidateSelf();
    } 
  }
  
  void setInternalAnimationCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    this.internalAnimationCallback = paramAnimationCallback;
  }
  
  void setMockHideAnimationRunning(boolean paramBoolean, float paramFloat) {
    this.mockHideAnimationRunning = paramBoolean;
    this.mockGrowFraction = paramFloat;
  }
  
  void setMockShowAnimationRunning(boolean paramBoolean, float paramFloat) {
    this.mockShowAnimationRunning = paramBoolean;
    this.mockGrowFraction = paramFloat;
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    return setVisible(paramBoolean1, paramBoolean2, true);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    float f = this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(this.context.getContentResolver());
    if (paramBoolean3 && f > 0.0F) {
      paramBoolean3 = true;
    } else {
      paramBoolean3 = false;
    } 
    return setVisibleInternal(paramBoolean1, paramBoolean2, paramBoolean3);
  }
  
  boolean setVisibleInternal(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    ValueAnimator valueAnimator;
    maybeInitializeAnimators();
    if (!isVisible() && !paramBoolean1)
      return false; 
    if (paramBoolean1) {
      valueAnimator = this.showAnimator;
    } else {
      valueAnimator = this.hideAnimator;
    } 
    if (!paramBoolean3) {
      if (valueAnimator.isRunning()) {
        valueAnimator.end();
      } else {
        endAnimatorWithoutCallbacks(new ValueAnimator[] { valueAnimator });
      } 
      return super.setVisible(paramBoolean1, false);
    } 
    if (paramBoolean3 && valueAnimator.isRunning())
      return false; 
    if (!paramBoolean1 || super.setVisible(paramBoolean1, false)) {
      paramBoolean3 = true;
    } else {
      paramBoolean3 = false;
    } 
    if (paramBoolean1) {
      paramBoolean1 = this.baseSpec.isShowAnimationEnabled();
    } else {
      paramBoolean1 = this.baseSpec.isHideAnimationEnabled();
    } 
    if (!paramBoolean1) {
      endAnimatorWithoutCallbacks(new ValueAnimator[] { valueAnimator });
      return paramBoolean3;
    } 
    if (paramBoolean2 || Build.VERSION.SDK_INT < 19 || !valueAnimator.isPaused()) {
      valueAnimator.start();
      return paramBoolean3;
    } 
    valueAnimator.resume();
    return paramBoolean3;
  }
  
  public void start() {
    setVisibleInternal(true, true, false);
  }
  
  public void stop() {
    setVisibleInternal(false, true, false);
  }
  
  public boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    if (list != null && list.contains(paramAnimationCallback)) {
      this.animationCallbacks.remove(paramAnimationCallback);
      if (this.animationCallbacks.isEmpty())
        this.animationCallbacks = null; 
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\DrawableWithAnimatedVisibilityChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */