package com.bumptech.glide;

import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

public final class GenericTransitionOptions<TranscodeType> extends TransitionOptions<GenericTransitionOptions<TranscodeType>, TranscodeType> {
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(int paramInt) {
    return (new GenericTransitionOptions<>()).transition(paramInt);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(TransitionFactory<? super TranscodeType> paramTransitionFactory) {
    return (new GenericTransitionOptions<>()).transition(paramTransitionFactory);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(ViewPropertyTransition.Animator paramAnimator) {
    return (new GenericTransitionOptions<>()).transition(paramAnimator);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> withNoTransition() {
    return (new GenericTransitionOptions<>()).dontTransition();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\GenericTransitionOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */