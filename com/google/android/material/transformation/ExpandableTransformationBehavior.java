package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

@Deprecated
public abstract class ExpandableTransformationBehavior extends ExpandableBehavior {
  private AnimatorSet currentAnimation;
  
  public ExpandableTransformationBehavior() {}
  
  public ExpandableTransformationBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  protected abstract AnimatorSet onCreateExpandedStateChangeAnimation(View paramView1, View paramView2, boolean paramBoolean1, boolean paramBoolean2);
  
  protected boolean onExpandedStateChange(View paramView1, View paramView2, boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool;
    AnimatorSet animatorSet2 = this.currentAnimation;
    if (animatorSet2 != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      animatorSet2.cancel(); 
    AnimatorSet animatorSet1 = onCreateExpandedStateChangeAnimation(paramView1, paramView2, paramBoolean1, bool);
    this.currentAnimation = animatorSet1;
    animatorSet1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final ExpandableTransformationBehavior this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            ExpandableTransformationBehavior.access$002(ExpandableTransformationBehavior.this, null);
          }
        });
    this.currentAnimation.start();
    if (!paramBoolean2)
      this.currentAnimation.end(); 
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transformation\ExpandableTransformationBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */