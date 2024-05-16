package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import java.util.List;

public class AnimatorSetCompat {
  public static void playTogether(AnimatorSet paramAnimatorSet, List<Animator> paramList) {
    long l = 0L;
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      Animator animator = paramList.get(b);
      l = Math.max(l, animator.getStartDelay() + animator.getDuration());
      b++;
    } 
    ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[] { 0, 0 });
    valueAnimator.setDuration(l);
    paramList.add(0, valueAnimator);
    paramAnimatorSet.playTogether(paramList);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\AnimatorSetCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */