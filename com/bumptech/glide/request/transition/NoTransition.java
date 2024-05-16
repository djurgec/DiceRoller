package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class NoTransition<R> implements Transition<R> {
  static final NoTransition<?> NO_ANIMATION = new NoTransition();
  
  private static final TransitionFactory<?> NO_ANIMATION_FACTORY = new NoAnimationFactory();
  
  public static <R> Transition<R> get() {
    return (Transition)NO_ANIMATION;
  }
  
  public static <R> TransitionFactory<R> getFactory() {
    return (TransitionFactory)NO_ANIMATION_FACTORY;
  }
  
  public boolean transition(Object paramObject, Transition.ViewAdapter paramViewAdapter) {
    return false;
  }
  
  public static class NoAnimationFactory<R> implements TransitionFactory<R> {
    public Transition<R> build(DataSource param1DataSource, boolean param1Boolean) {
      return (Transition)NoTransition.NO_ANIMATION;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\NoTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */