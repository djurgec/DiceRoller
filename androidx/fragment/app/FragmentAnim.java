package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R;

class FragmentAnim {
  static void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator paramAnimationOrAnimator, final FragmentTransition.Callback callback) {
    EndViewTransitionAnimation endViewTransitionAnimation;
    final View viewToAnimate = fragment.mView;
    final ViewGroup container = fragment.mContainer;
    viewGroup.startViewTransition(view);
    final CancellationSignal signal = new CancellationSignal();
    cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
          final Fragment val$fragment;
          
          public void onCancel() {
            if (fragment.getAnimatingAway() != null) {
              View view = fragment.getAnimatingAway();
              fragment.setAnimatingAway(null);
              view.clearAnimation();
            } 
            fragment.setAnimator(null);
          }
        });
    callback.onStart(fragment, cancellationSignal);
    if (paramAnimationOrAnimator.animation != null) {
      endViewTransitionAnimation = new EndViewTransitionAnimation(paramAnimationOrAnimator.animation, viewGroup, view);
      fragment.setAnimatingAway(fragment.mView);
      endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
            final FragmentTransition.Callback val$callback;
            
            final ViewGroup val$container;
            
            final Fragment val$fragment;
            
            final CancellationSignal val$signal;
            
            public void onAnimationEnd(Animation param1Animation) {
              container.post(new Runnable() {
                    final FragmentAnim.null this$0;
                    
                    public void run() {
                      if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        callback.onComplete(fragment, signal);
                      } 
                    }
                  });
            }
            
            public void onAnimationRepeat(Animation param1Animation) {}
            
            public void onAnimationStart(Animation param1Animation) {}
          });
      fragment.mView.startAnimation((Animation)endViewTransitionAnimation);
    } else {
      Animator animator = ((AnimationOrAnimator)endViewTransitionAnimation).animator;
      fragment.setAnimator(((AnimationOrAnimator)endViewTransitionAnimation).animator);
      animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final FragmentTransition.Callback val$callback;
            
            final ViewGroup val$container;
            
            final Fragment val$fragment;
            
            final CancellationSignal val$signal;
            
            final View val$viewToAnimate;
            
            public void onAnimationEnd(Animator param1Animator) {
              container.endViewTransition(viewToAnimate);
              param1Animator = fragment.getAnimator();
              fragment.setAnimator(null);
              if (param1Animator != null && container.indexOfChild(viewToAnimate) < 0)
                callback.onComplete(fragment, signal); 
            }
          });
      animator.setTarget(fragment.mView);
      animator.start();
    } 
  }
  
  private static int getNextAnim(Fragment paramFragment, boolean paramBoolean1, boolean paramBoolean2) {
    return paramBoolean2 ? (paramBoolean1 ? paramFragment.getPopEnterAnim() : paramFragment.getPopExitAnim()) : (paramBoolean1 ? paramFragment.getEnterAnim() : paramFragment.getExitAnim());
  }
  
  static AnimationOrAnimator loadAnimation(Context paramContext, Fragment paramFragment, boolean paramBoolean1, boolean paramBoolean2) {
    int k = paramFragment.getNextTransition();
    int j = getNextAnim(paramFragment, paramBoolean1, paramBoolean2);
    paramFragment.setAnimations(0, 0, 0, 0);
    if (paramFragment.mContainer != null && paramFragment.mContainer.getTag(R.id.visible_removing_fragment_view_tag) != null)
      paramFragment.mContainer.setTag(R.id.visible_removing_fragment_view_tag, null); 
    if (paramFragment.mContainer != null && paramFragment.mContainer.getLayoutTransition() != null)
      return null; 
    Animation animation = paramFragment.onCreateAnimation(k, paramBoolean1, j);
    if (animation != null)
      return new AnimationOrAnimator(animation); 
    Animator animator = paramFragment.onCreateAnimator(k, paramBoolean1, j);
    if (animator != null)
      return new AnimationOrAnimator(animator); 
    int i = j;
    if (j == 0) {
      i = j;
      if (k != 0)
        i = transitToAnimResourceId(k, paramBoolean1); 
    } 
    if (i != 0) {
      paramBoolean1 = "anim".equals(paramContext.getResources().getResourceTypeName(i));
      k = 0;
      j = k;
      if (paramBoolean1)
        try {
          Animation animation1 = AnimationUtils.loadAnimation(paramContext, i);
          if (animation1 != null)
            return new AnimationOrAnimator(animation1); 
          j = 1;
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          throw notFoundException;
        } catch (RuntimeException runtimeException) {
          j = k;
        }  
      if (j == 0)
        try {
          animator = AnimatorInflater.loadAnimator((Context)notFoundException, i);
          if (animator != null)
            return new AnimationOrAnimator(animator); 
        } catch (RuntimeException runtimeException) {
          if (!paramBoolean1) {
            Animation animation1 = AnimationUtils.loadAnimation((Context)notFoundException, i);
            if (animation1 != null)
              return new AnimationOrAnimator(animation1); 
          } else {
            throw runtimeException;
          } 
        }  
    } 
    return null;
  }
  
  private static int transitToAnimResourceId(int paramInt, boolean paramBoolean) {
    byte b = -1;
    switch (paramInt) {
      default:
        return b;
      case 8194:
        if (paramBoolean) {
          paramInt = R.animator.fragment_close_enter;
        } else {
          paramInt = R.animator.fragment_close_exit;
        } 
        return paramInt;
      case 4099:
        if (paramBoolean) {
          paramInt = R.animator.fragment_fade_enter;
        } else {
          paramInt = R.animator.fragment_fade_exit;
        } 
        return paramInt;
      case 4097:
        break;
    } 
    if (paramBoolean) {
      paramInt = R.animator.fragment_open_enter;
    } else {
      paramInt = R.animator.fragment_open_exit;
    } 
    return paramInt;
  }
  
  static class AnimationOrAnimator {
    public final Animation animation = null;
    
    public final Animator animator;
    
    AnimationOrAnimator(Animator param1Animator) {
      this.animator = param1Animator;
      if (param1Animator != null)
        return; 
      throw new IllegalStateException("Animator cannot be null");
    }
    
    AnimationOrAnimator(Animation param1Animation) {
      this.animator = null;
      if (param1Animation != null)
        return; 
      throw new IllegalStateException("Animation cannot be null");
    }
  }
  
  static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
    private boolean mAnimating = true;
    
    private final View mChild;
    
    private boolean mEnded;
    
    private final ViewGroup mParent;
    
    private boolean mTransitionEnded;
    
    EndViewTransitionAnimation(Animation param1Animation, ViewGroup param1ViewGroup, View param1View) {
      super(false);
      this.mParent = param1ViewGroup;
      this.mChild = param1View;
      addAnimation(param1Animation);
      param1ViewGroup.post(this);
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation, float param1Float) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation, param1Float)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public void run() {
      if (!this.mEnded && this.mAnimating) {
        this.mAnimating = false;
        this.mParent.post(this);
      } else {
        this.mParent.endViewTransition(this.mChild);
        this.mTransitionEnded = true;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentAnim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */