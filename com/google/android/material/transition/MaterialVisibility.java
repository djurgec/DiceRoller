package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class MaterialVisibility<P extends VisibilityAnimatorProvider> extends Visibility {
  private final List<VisibilityAnimatorProvider> additionalAnimatorProviders = new ArrayList<>();
  
  private final P primaryAnimatorProvider;
  
  private VisibilityAnimatorProvider secondaryAnimatorProvider;
  
  protected MaterialVisibility(P paramP, VisibilityAnimatorProvider paramVisibilityAnimatorProvider) {
    this.primaryAnimatorProvider = paramP;
    this.secondaryAnimatorProvider = paramVisibilityAnimatorProvider;
  }
  
  private static void addAnimatorIfNeeded(List<Animator> paramList, VisibilityAnimatorProvider paramVisibilityAnimatorProvider, ViewGroup paramViewGroup, View paramView, boolean paramBoolean) {
    Animator animator;
    if (paramVisibilityAnimatorProvider == null)
      return; 
    if (paramBoolean) {
      animator = paramVisibilityAnimatorProvider.createAppear(paramViewGroup, paramView);
    } else {
      animator = animator.createDisappear(paramViewGroup, paramView);
    } 
    if (animator != null)
      paramList.add(animator); 
  }
  
  private Animator createAnimator(ViewGroup paramViewGroup, View paramView, boolean paramBoolean) {
    AnimatorSet animatorSet = new AnimatorSet();
    ArrayList<Animator> arrayList = new ArrayList();
    addAnimatorIfNeeded(arrayList, (VisibilityAnimatorProvider)this.primaryAnimatorProvider, paramViewGroup, paramView, paramBoolean);
    addAnimatorIfNeeded(arrayList, this.secondaryAnimatorProvider, paramViewGroup, paramView, paramBoolean);
    Iterator<VisibilityAnimatorProvider> iterator = this.additionalAnimatorProviders.iterator();
    while (iterator.hasNext())
      addAnimatorIfNeeded(arrayList, iterator.next(), paramViewGroup, paramView, paramBoolean); 
    maybeApplyThemeValues(paramViewGroup.getContext(), paramBoolean);
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    return (Animator)animatorSet;
  }
  
  private void maybeApplyThemeValues(Context paramContext, boolean paramBoolean) {
    TransitionUtils.maybeApplyThemeDuration((Transition)this, paramContext, getDurationThemeAttrResId(paramBoolean));
    TransitionUtils.maybeApplyThemeInterpolator((Transition)this, paramContext, getEasingThemeAttrResId(paramBoolean), getDefaultEasingInterpolator(paramBoolean));
  }
  
  public void addAdditionalAnimatorProvider(VisibilityAnimatorProvider paramVisibilityAnimatorProvider) {
    this.additionalAnimatorProviders.add(paramVisibilityAnimatorProvider);
  }
  
  public void clearAdditionalAnimatorProvider() {
    this.additionalAnimatorProviders.clear();
  }
  
  TimeInterpolator getDefaultEasingInterpolator(boolean paramBoolean) {
    return AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
  }
  
  int getDurationThemeAttrResId(boolean paramBoolean) {
    return 0;
  }
  
  int getEasingThemeAttrResId(boolean paramBoolean) {
    return 0;
  }
  
  public P getPrimaryAnimatorProvider() {
    return this.primaryAnimatorProvider;
  }
  
  public VisibilityAnimatorProvider getSecondaryAnimatorProvider() {
    return this.secondaryAnimatorProvider;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return createAnimator(paramViewGroup, paramView, true);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return createAnimator(paramViewGroup, paramView, false);
  }
  
  public boolean removeAdditionalAnimatorProvider(VisibilityAnimatorProvider paramVisibilityAnimatorProvider) {
    return this.additionalAnimatorProviders.remove(paramVisibilityAnimatorProvider);
  }
  
  public void setSecondaryAnimatorProvider(VisibilityAnimatorProvider paramVisibilityAnimatorProvider) {
    this.secondaryAnimatorProvider = paramVisibilityAnimatorProvider;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\MaterialVisibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */