package com.google.android.material.appbar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

class ViewUtilsLollipop {
  private static final int[] STATE_LIST_ANIM_ATTRS = new int[] { 16843848 };
  
  static void setBoundsViewOutlineProvider(View paramView) {
    paramView.setOutlineProvider(ViewOutlineProvider.BOUNDS);
  }
  
  static void setDefaultAppBarLayoutStateListAnimator(View paramView, float paramFloat) {
    int i = paramView.getResources().getInteger(R.integer.app_bar_elevation_anim_duration);
    StateListAnimator stateListAnimator = new StateListAnimator();
    int j = R.attr.state_liftable;
    int k = -R.attr.state_lifted;
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { 0.0F }).setDuration(i);
    stateListAnimator.addState(new int[] { 16842766, j, k }, (Animator)objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { paramFloat }).setDuration(i);
    stateListAnimator.addState(new int[] { 16842766 }, (Animator)objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { 0.0F }).setDuration(0L);
    stateListAnimator.addState(new int[0], (Animator)objectAnimator);
    paramView.setStateListAnimator(stateListAnimator);
  }
  
  static void setStateListAnimatorFromAttrs(View paramView, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    Context context = paramView.getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, STATE_LIST_ANIM_ATTRS, paramInt1, paramInt2, new int[0]);
    try {
      if (typedArray.hasValue(0))
        paramView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, typedArray.getResourceId(0, 0))); 
      return;
    } finally {
      typedArray.recycle();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\ViewUtilsLollipop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */