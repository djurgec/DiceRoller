package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Property;
import android.view.View;
import androidx.core.util.Preconditions;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.MotionSpec;
import java.util.ArrayList;
import java.util.List;

abstract class BaseMotionStrategy implements MotionStrategy {
  private final Context context;
  
  private MotionSpec defaultMotionSpec;
  
  private final ExtendedFloatingActionButton fab;
  
  private final ArrayList<Animator.AnimatorListener> listeners = new ArrayList<>();
  
  private MotionSpec motionSpec;
  
  private final AnimatorTracker tracker;
  
  BaseMotionStrategy(ExtendedFloatingActionButton paramExtendedFloatingActionButton, AnimatorTracker paramAnimatorTracker) {
    this.fab = paramExtendedFloatingActionButton;
    this.context = paramExtendedFloatingActionButton.getContext();
    this.tracker = paramAnimatorTracker;
  }
  
  public final void addAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.listeners.add(paramAnimatorListener);
  }
  
  public AnimatorSet createAnimator() {
    return createAnimator(getCurrentMotionSpec());
  }
  
  AnimatorSet createAnimator(MotionSpec paramMotionSpec) {
    ArrayList<ObjectAnimator> arrayList = new ArrayList();
    if (paramMotionSpec.hasPropertyValues("opacity"))
      arrayList.add(paramMotionSpec.getAnimator("opacity", this.fab, View.ALPHA)); 
    if (paramMotionSpec.hasPropertyValues("scale")) {
      arrayList.add(paramMotionSpec.getAnimator("scale", this.fab, View.SCALE_Y));
      arrayList.add(paramMotionSpec.getAnimator("scale", this.fab, View.SCALE_X));
    } 
    if (paramMotionSpec.hasPropertyValues("width"))
      arrayList.add(paramMotionSpec.getAnimator("width", this.fab, ExtendedFloatingActionButton.WIDTH)); 
    if (paramMotionSpec.hasPropertyValues("height"))
      arrayList.add(paramMotionSpec.getAnimator("height", this.fab, ExtendedFloatingActionButton.HEIGHT)); 
    if (paramMotionSpec.hasPropertyValues("paddingStart"))
      arrayList.add(paramMotionSpec.getAnimator("paddingStart", this.fab, ExtendedFloatingActionButton.PADDING_START)); 
    if (paramMotionSpec.hasPropertyValues("paddingEnd"))
      arrayList.add(paramMotionSpec.getAnimator("paddingEnd", this.fab, ExtendedFloatingActionButton.PADDING_END)); 
    if (paramMotionSpec.hasPropertyValues("labelOpacity"))
      arrayList.add(paramMotionSpec.getAnimator("labelOpacity", this.fab, new Property<ExtendedFloatingActionButton, Float>(Float.class, "LABEL_OPACITY_PROPERTY") {
              final BaseMotionStrategy this$0;
              
              public Float get(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
                int i = Color.alpha(param1ExtendedFloatingActionButton.originalTextCsl.getColorForState(param1ExtendedFloatingActionButton.getDrawableState(), BaseMotionStrategy.this.fab.originalTextCsl.getDefaultColor()));
                return Float.valueOf(AnimationUtils.lerp(0.0F, 1.0F, Color.alpha(param1ExtendedFloatingActionButton.getCurrentTextColor()) / 255.0F / i));
              }
              
              public void set(ExtendedFloatingActionButton param1ExtendedFloatingActionButton, Float param1Float) {
                int i = param1ExtendedFloatingActionButton.originalTextCsl.getColorForState(param1ExtendedFloatingActionButton.getDrawableState(), BaseMotionStrategy.this.fab.originalTextCsl.getDefaultColor());
                ColorStateList colorStateList = ColorStateList.valueOf(Color.argb((int)(255.0F * AnimationUtils.lerp(0.0F, Color.alpha(i) / 255.0F, param1Float.floatValue())), Color.red(i), Color.green(i), Color.blue(i)));
                if (param1Float.floatValue() == 1.0F) {
                  param1ExtendedFloatingActionButton.silentlyUpdateTextColor(param1ExtendedFloatingActionButton.originalTextCsl);
                } else {
                  param1ExtendedFloatingActionButton.silentlyUpdateTextColor(colorStateList);
                } 
              }
            })); 
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    return animatorSet;
  }
  
  public final MotionSpec getCurrentMotionSpec() {
    MotionSpec motionSpec = this.motionSpec;
    if (motionSpec != null)
      return motionSpec; 
    if (this.defaultMotionSpec == null)
      this.defaultMotionSpec = MotionSpec.createFromResource(this.context, getDefaultMotionSpecResource()); 
    return (MotionSpec)Preconditions.checkNotNull(this.defaultMotionSpec);
  }
  
  public final List<Animator.AnimatorListener> getListeners() {
    return this.listeners;
  }
  
  public MotionSpec getMotionSpec() {
    return this.motionSpec;
  }
  
  public void onAnimationCancel() {
    this.tracker.clear();
  }
  
  public void onAnimationEnd() {
    this.tracker.clear();
  }
  
  public void onAnimationStart(Animator paramAnimator) {
    this.tracker.onNextAnimationStart(paramAnimator);
  }
  
  public final void removeAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.listeners.remove(paramAnimatorListener);
  }
  
  public final void setMotionSpec(MotionSpec paramMotionSpec) {
    this.motionSpec = paramMotionSpec;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\BaseMotionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */