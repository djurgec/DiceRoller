package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public abstract class Visibility extends Transition {
  public static final int MODE_IN = 1;
  
  public static final int MODE_OUT = 2;
  
  private static final String PROPNAME_PARENT = "android:visibility:parent";
  
  private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
  
  static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
  
  private static final String[] sTransitionProperties = new String[] { "android:visibility:visibility", "android:visibility:parent" };
  
  private int mMode = 3;
  
  public Visibility() {}
  
  public Visibility(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.VISIBILITY_TRANSITION);
    int i = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)paramAttributeSet, "transitionVisibilityMode", 0, 0);
    typedArray.recycle();
    if (i != 0)
      setMode(i); 
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getVisibility();
    paramTransitionValues.values.put("android:visibility:visibility", Integer.valueOf(i));
    paramTransitionValues.values.put("android:visibility:parent", paramTransitionValues.view.getParent());
    int[] arrayOfInt = new int[2];
    paramTransitionValues.view.getLocationOnScreen(arrayOfInt);
    paramTransitionValues.values.put("android:visibility:screenLocation", arrayOfInt);
  }
  
  private VisibilityInfo getVisibilityChangeInfo(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = new VisibilityInfo();
    visibilityInfo.mVisibilityChange = false;
    visibilityInfo.mFadeIn = false;
    if (paramTransitionValues1 != null && paramTransitionValues1.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mStartVisibility = ((Integer)paramTransitionValues1.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mStartParent = (ViewGroup)paramTransitionValues1.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mStartVisibility = -1;
      visibilityInfo.mStartParent = null;
    } 
    if (paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mEndVisibility = ((Integer)paramTransitionValues2.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mEndParent = (ViewGroup)paramTransitionValues2.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mEndVisibility = -1;
      visibilityInfo.mEndParent = null;
    } 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null) {
      if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent)
        return visibilityInfo; 
      if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
        if (visibilityInfo.mStartVisibility == 0) {
          visibilityInfo.mFadeIn = false;
          visibilityInfo.mVisibilityChange = true;
        } else if (visibilityInfo.mEndVisibility == 0) {
          visibilityInfo.mFadeIn = true;
          visibilityInfo.mVisibilityChange = true;
        } 
      } else if (visibilityInfo.mEndParent == null) {
        visibilityInfo.mFadeIn = false;
        visibilityInfo.mVisibilityChange = true;
      } else if (visibilityInfo.mStartParent == null) {
        visibilityInfo.mFadeIn = true;
        visibilityInfo.mVisibilityChange = true;
      } 
    } else if (paramTransitionValues1 == null && visibilityInfo.mEndVisibility == 0) {
      visibilityInfo.mFadeIn = true;
      visibilityInfo.mVisibilityChange = true;
    } else if (paramTransitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
      visibilityInfo.mFadeIn = false;
      visibilityInfo.mVisibilityChange = true;
    } 
    return visibilityInfo;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    return (visibilityInfo.mVisibilityChange && (visibilityInfo.mStartParent != null || visibilityInfo.mEndParent != null)) ? (visibilityInfo.mFadeIn ? onAppear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility) : onDisappear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility)) : null;
  }
  
  public int getMode() {
    return this.mMode;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool = false;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return false; 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility") != paramTransitionValues1.values.containsKey("android:visibility:visibility"))
      return false; 
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    null = bool;
    if (visibilityInfo.mVisibilityChange) {
      if (visibilityInfo.mStartVisibility != 0) {
        null = bool;
        return (visibilityInfo.mEndVisibility == 0) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    boolean bool2 = false;
    if (paramTransitionValues == null)
      return false; 
    int i = ((Integer)paramTransitionValues.values.get("android:visibility:visibility")).intValue();
    View view = (View)paramTransitionValues.values.get("android:visibility:parent");
    boolean bool1 = bool2;
    if (i == 0) {
      bool1 = bool2;
      if (view != null)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    if ((this.mMode & 0x1) != 1 || paramTransitionValues2 == null)
      return null; 
    if (paramTransitionValues1 == null) {
      View view = (View)paramTransitionValues2.view.getParent();
      TransitionValues transitionValues1 = getMatchedTransitionValues(view, false);
      TransitionValues transitionValues2 = getTransitionValues(view, false);
      if ((getVisibilityChangeInfo(transitionValues1, transitionValues2)).mVisibilityChange)
        return null; 
    } 
    return onAppear(paramViewGroup, paramTransitionValues2.view, paramTransitionValues1, paramTransitionValues2);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public Animator onDisappear(final ViewGroup overlayHost, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    Animator animator;
    boolean bool1;
    final View finalOverlayView;
    if ((this.mMode & 0x2) != 2)
      return null; 
    if (paramTransitionValues1 == null)
      return null; 
    final View startView = paramTransitionValues1.view;
    if (paramTransitionValues2 != null) {
      view1 = paramTransitionValues2.view;
    } else {
      view1 = null;
    } 
    View view3 = null;
    View view5 = null;
    View view2 = null;
    boolean bool2 = false;
    View view6 = (View)view4.getTag(R.id.save_overlay_view);
    if (view6 != null) {
      view1 = view6;
      bool1 = true;
    } else {
      paramInt1 = 0;
      if (view1 == null || view1.getParent() == null) {
        if (view1 != null) {
          view3 = view1;
        } else {
          paramInt1 = 1;
        } 
      } else if (paramInt2 == 4) {
        view2 = view1;
      } else if (view4 == view1) {
        view2 = view1;
      } else {
        paramInt1 = 1;
      } 
      view1 = view3;
      view5 = view2;
      bool1 = bool2;
      if (paramInt1 != 0)
        if (view4.getParent() == null) {
          view1 = view4;
          view5 = view2;
          bool1 = bool2;
        } else {
          view1 = view3;
          view5 = view2;
          bool1 = bool2;
          if (view4.getParent() instanceof View) {
            view6 = (View)view4.getParent();
            TransitionValues transitionValues1 = getTransitionValues(view6, true);
            TransitionValues transitionValues2 = getMatchedTransitionValues(view6, true);
            if (!(getVisibilityChangeInfo(transitionValues1, transitionValues2)).mVisibilityChange) {
              View view7 = TransitionUtils.copyViewImage(overlayHost, view4, view6);
              View view8 = view2;
              bool1 = bool2;
            } else {
              paramInt1 = view6.getId();
              if (view6.getParent() == null) {
                View view7 = view3;
                View view8 = view2;
                bool1 = bool2;
                if (paramInt1 != -1) {
                  view7 = view3;
                  view8 = view2;
                  bool1 = bool2;
                  if (overlayHost.findViewById(paramInt1) != null) {
                    view7 = view3;
                    view8 = view2;
                    bool1 = bool2;
                    if (this.mCanRemoveViews) {
                      view7 = view4;
                      view8 = view2;
                      bool1 = bool2;
                    } 
                  } 
                } 
              } else {
                bool1 = bool2;
                view5 = view2;
                view1 = view3;
              } 
            } 
          } 
        }  
    } 
    if (view1 != null) {
      if (!bool1) {
        int[] arrayOfInt = (int[])paramTransitionValues1.values.get("android:visibility:screenLocation");
        paramInt1 = arrayOfInt[0];
        paramInt2 = arrayOfInt[1];
        arrayOfInt = new int[2];
        overlayHost.getLocationOnScreen(arrayOfInt);
        view1.offsetLeftAndRight(paramInt1 - arrayOfInt[0] - view1.getLeft());
        view1.offsetTopAndBottom(paramInt2 - arrayOfInt[1] - view1.getTop());
        ViewGroupUtils.getOverlay(overlayHost).add(view1);
      } 
      animator = onDisappear(overlayHost, view1, paramTransitionValues1, paramTransitionValues2);
      if (!bool1)
        if (animator == null) {
          ViewGroupUtils.getOverlay(overlayHost).remove(view1);
        } else {
          view4.setTag(R.id.save_overlay_view, view1);
          addListener(new TransitionListenerAdapter() {
                final Visibility this$0;
                
                final View val$finalOverlayView;
                
                final ViewGroup val$overlayHost;
                
                final View val$startView;
                
                public void onTransitionEnd(Transition param1Transition) {
                  startView.setTag(R.id.save_overlay_view, null);
                  ViewGroupUtils.getOverlay(overlayHost).remove(finalOverlayView);
                  param1Transition.removeListener(this);
                }
                
                public void onTransitionPause(Transition param1Transition) {
                  ViewGroupUtils.getOverlay(overlayHost).remove(finalOverlayView);
                }
                
                public void onTransitionResume(Transition param1Transition) {
                  if (finalOverlayView.getParent() == null) {
                    ViewGroupUtils.getOverlay(overlayHost).add(finalOverlayView);
                  } else {
                    Visibility.this.cancel();
                  } 
                }
              });
        }  
      return animator;
    } 
    if (view5 != null) {
      paramInt1 = view5.getVisibility();
      ViewUtils.setTransitionVisibility(view5, 0);
      Animator animator1 = onDisappear(overlayHost, view5, (TransitionValues)animator, paramTransitionValues2);
      if (animator1 != null) {
        DisappearListener disappearListener = new DisappearListener(view5, paramInt2, true);
        animator1.addListener((Animator.AnimatorListener)disappearListener);
        AnimatorUtils.addPauseListener(animator1, disappearListener);
        addListener(disappearListener);
      } else {
        ViewUtils.setTransitionVisibility(view5, paramInt1);
      } 
      return animator1;
    } 
    return null;
  }
  
  public void setMode(int paramInt) {
    if ((paramInt & 0xFFFFFFFC) == 0) {
      this.mMode = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
  }
  
  private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
    boolean mCanceled = false;
    
    private final int mFinalVisibility;
    
    private boolean mLayoutSuppressed;
    
    private final ViewGroup mParent;
    
    private final boolean mSuppressLayout;
    
    private final View mView;
    
    DisappearListener(View param1View, int param1Int, boolean param1Boolean) {
      this.mView = param1View;
      this.mFinalVisibility = param1Int;
      this.mParent = (ViewGroup)param1View.getParent();
      this.mSuppressLayout = param1Boolean;
      suppressLayout(true);
    }
    
    private void hideViewWhenNotCanceled() {
      if (!this.mCanceled) {
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null)
          viewGroup.invalidate(); 
      } 
      suppressLayout(false);
    }
    
    private void suppressLayout(boolean param1Boolean) {
      if (this.mSuppressLayout && this.mLayoutSuppressed != param1Boolean) {
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null) {
          this.mLayoutSuppressed = param1Boolean;
          ViewGroupUtils.suppressLayout(viewGroup, param1Boolean);
        } 
      } 
    }
    
    public void onAnimationCancel(Animator param1Animator) {
      this.mCanceled = true;
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      hideViewWhenNotCanceled();
    }
    
    public void onAnimationPause(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility); 
    }
    
    public void onAnimationRepeat(Animator param1Animator) {}
    
    public void onAnimationResume(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, 0); 
    }
    
    public void onAnimationStart(Animator param1Animator) {}
    
    public void onTransitionCancel(Transition param1Transition) {}
    
    public void onTransitionEnd(Transition param1Transition) {
      hideViewWhenNotCanceled();
      param1Transition.removeListener(this);
    }
    
    public void onTransitionPause(Transition param1Transition) {
      suppressLayout(false);
    }
    
    public void onTransitionResume(Transition param1Transition) {
      suppressLayout(true);
    }
    
    public void onTransitionStart(Transition param1Transition) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
  
  private static class VisibilityInfo {
    ViewGroup mEndParent;
    
    int mEndVisibility;
    
    boolean mFadeIn;
    
    ViewGroup mStartParent;
    
    int mStartVisibility;
    
    boolean mVisibilityChange;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Visibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */