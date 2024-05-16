package com.bumptech.glide.request.transition;

import android.view.View;

public class ViewPropertyTransition<R> implements Transition<R> {
  private final Animator animator;
  
  public ViewPropertyTransition(Animator paramAnimator) {
    this.animator = paramAnimator;
  }
  
  public boolean transition(R paramR, Transition.ViewAdapter paramViewAdapter) {
    if (paramViewAdapter.getView() != null)
      this.animator.animate(paramViewAdapter.getView()); 
    return false;
  }
  
  public static interface Animator {
    void animate(View param1View);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\ViewPropertyTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */