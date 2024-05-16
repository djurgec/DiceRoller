package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class ViewPropertyAnimationFactory<R> implements TransitionFactory<R> {
  private ViewPropertyTransition<R> animation;
  
  private final ViewPropertyTransition.Animator animator;
  
  public ViewPropertyAnimationFactory(ViewPropertyTransition.Animator paramAnimator) {
    this.animator = paramAnimator;
  }
  
  public Transition<R> build(DataSource paramDataSource, boolean paramBoolean) {
    if (paramDataSource == DataSource.MEMORY_CACHE || !paramBoolean)
      return NoTransition.get(); 
    if (this.animation == null)
      this.animation = new ViewPropertyTransition<>(this.animator); 
    return this.animation;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\ViewPropertyAnimationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */