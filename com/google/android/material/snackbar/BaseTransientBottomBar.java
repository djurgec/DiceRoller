package com.google.android.material.snackbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
  static final int ANIMATION_DURATION = 250;
  
  static final int ANIMATION_FADE_DURATION = 180;
  
  private static final int ANIMATION_FADE_IN_DURATION = 150;
  
  private static final int ANIMATION_FADE_OUT_DURATION = 75;
  
  public static final int ANIMATION_MODE_FADE = 1;
  
  public static final int ANIMATION_MODE_SLIDE = 0;
  
  private static final float ANIMATION_SCALE_FROM_VALUE = 0.8F;
  
  public static final int LENGTH_INDEFINITE = -2;
  
  public static final int LENGTH_LONG = 0;
  
  public static final int LENGTH_SHORT = -1;
  
  static final int MSG_DISMISS = 1;
  
  static final int MSG_SHOW = 0;
  
  private static final int[] SNACKBAR_STYLE_ATTR;
  
  private static final String TAG;
  
  private static final boolean USE_OFFSET_API;
  
  static final Handler handler;
  
  private final AccessibilityManager accessibilityManager;
  
  private View anchorView;
  
  private final ViewTreeObserver.OnGlobalLayoutListener anchorViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
      final BaseTransientBottomBar this$0;
      
      public void onGlobalLayout() {
        if (!BaseTransientBottomBar.this.anchorViewLayoutListenerEnabled)
          return; 
        BaseTransientBottomBar baseTransientBottomBar = BaseTransientBottomBar.this;
        BaseTransientBottomBar.access$102(baseTransientBottomBar, baseTransientBottomBar.calculateBottomMarginForAnchorView());
        BaseTransientBottomBar.this.updateMargins();
      }
    };
  
  private boolean anchorViewLayoutListenerEnabled = false;
  
  private Behavior behavior;
  
  private final Runnable bottomMarginGestureInsetRunnable = new Runnable() {
      final BaseTransientBottomBar this$0;
      
      public void run() {
        if (BaseTransientBottomBar.this.view == null || BaseTransientBottomBar.this.context == null)
          return; 
        int i = BaseTransientBottomBar.this.getScreenHeight() - BaseTransientBottomBar.this.getViewAbsoluteBottom() + (int)BaseTransientBottomBar.this.view.getTranslationY();
        if (i >= BaseTransientBottomBar.this.extraBottomMarginGestureInset)
          return; 
        ViewGroup.LayoutParams layoutParams = BaseTransientBottomBar.this.view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
          Log.w(BaseTransientBottomBar.TAG, "Unable to apply gesture inset because layout params are not MarginLayoutParams");
          return;
        } 
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
        marginLayoutParams.bottomMargin += BaseTransientBottomBar.this.extraBottomMarginGestureInset - i;
        BaseTransientBottomBar.this.view.requestLayout();
      }
    };
  
  private List<BaseCallback<B>> callbacks;
  
  private final ContentViewCallback contentViewCallback;
  
  private final Context context;
  
  private int duration;
  
  private int extraBottomMarginAnchorView;
  
  private int extraBottomMarginGestureInset;
  
  private int extraBottomMarginWindowInset;
  
  private int extraLeftMarginWindowInset;
  
  private int extraRightMarginWindowInset;
  
  private boolean gestureInsetBottomIgnored;
  
  SnackbarManager.Callback managerCallback = new SnackbarManager.Callback() {
      final BaseTransientBottomBar this$0;
      
      public void dismiss(int param1Int) {
        BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, param1Int, 0, BaseTransientBottomBar.this));
      }
      
      public void show() {
        BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
      }
    };
  
  private Rect originalMargins;
  
  private final ViewGroup targetParent;
  
  protected final SnackbarBaseLayout view;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_OFFSET_API = bool;
    SNACKBAR_STYLE_ATTR = new int[] { R.attr.snackbarStyle };
    TAG = BaseTransientBottomBar.class.getSimpleName();
    handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
          public boolean handleMessage(Message param1Message) {
            switch (param1Message.what) {
              default:
                return false;
              case 1:
                ((BaseTransientBottomBar)param1Message.obj).hideView(param1Message.arg1);
                return true;
              case 0:
                break;
            } 
            ((BaseTransientBottomBar)param1Message.obj).showView();
            return true;
          }
        });
  }
  
  protected BaseTransientBottomBar(Context paramContext, ViewGroup paramViewGroup, View paramView, ContentViewCallback paramContentViewCallback) {
    if (paramViewGroup != null) {
      if (paramView != null) {
        if (paramContentViewCallback != null) {
          this.targetParent = paramViewGroup;
          this.contentViewCallback = paramContentViewCallback;
          this.context = paramContext;
          ThemeEnforcement.checkAppCompatTheme(paramContext);
          SnackbarBaseLayout snackbarBaseLayout = (SnackbarBaseLayout)LayoutInflater.from(paramContext).inflate(getSnackbarBaseLayoutResId(), paramViewGroup, false);
          this.view = snackbarBaseLayout;
          if (paramView instanceof SnackbarContentLayout)
            ((SnackbarContentLayout)paramView).updateActionTextColorAlphaIfNeeded(snackbarBaseLayout.getActionTextColorAlpha()); 
          snackbarBaseLayout.addView(paramView);
          ViewGroup.LayoutParams layoutParams = snackbarBaseLayout.getLayoutParams();
          if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
            this.originalMargins = new Rect(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
          } 
          ViewCompat.setAccessibilityLiveRegion((View)snackbarBaseLayout, 1);
          ViewCompat.setImportantForAccessibility((View)snackbarBaseLayout, 1);
          ViewCompat.setFitsSystemWindows((View)snackbarBaseLayout, true);
          ViewCompat.setOnApplyWindowInsetsListener((View)snackbarBaseLayout, new OnApplyWindowInsetsListener() {
                final BaseTransientBottomBar this$0;
                
                public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                  BaseTransientBottomBar.access$902(BaseTransientBottomBar.this, param1WindowInsetsCompat.getSystemWindowInsetBottom());
                  BaseTransientBottomBar.access$1002(BaseTransientBottomBar.this, param1WindowInsetsCompat.getSystemWindowInsetLeft());
                  BaseTransientBottomBar.access$1102(BaseTransientBottomBar.this, param1WindowInsetsCompat.getSystemWindowInsetRight());
                  BaseTransientBottomBar.this.updateMargins();
                  return param1WindowInsetsCompat;
                }
              });
          ViewCompat.setAccessibilityDelegate((View)snackbarBaseLayout, new AccessibilityDelegateCompat() {
                final BaseTransientBottomBar this$0;
                
                public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
                  super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
                  param1AccessibilityNodeInfoCompat.addAction(1048576);
                  param1AccessibilityNodeInfoCompat.setDismissable(true);
                }
                
                public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
                  if (param1Int == 1048576) {
                    BaseTransientBottomBar.this.dismiss();
                    return true;
                  } 
                  return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
                }
              });
          this.accessibilityManager = (AccessibilityManager)paramContext.getSystemService("accessibility");
          return;
        } 
        throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
      } 
      throw new IllegalArgumentException("Transient bottom bar must have non-null content");
    } 
    throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
  }
  
  protected BaseTransientBottomBar(ViewGroup paramViewGroup, View paramView, ContentViewCallback paramContentViewCallback) {
    this(paramViewGroup.getContext(), paramViewGroup, paramView, paramContentViewCallback);
  }
  
  private void animateViewOut(int paramInt) {
    if (this.view.getAnimationMode() == 1) {
      startFadeOutAnimation(paramInt);
    } else {
      startSlideOutAnimation(paramInt);
    } 
  }
  
  private int calculateBottomMarginForAnchorView() {
    View view = this.anchorView;
    if (view == null)
      return 0; 
    int[] arrayOfInt = new int[2];
    view.getLocationOnScreen(arrayOfInt);
    int i = arrayOfInt[1];
    arrayOfInt = new int[2];
    this.targetParent.getLocationOnScreen(arrayOfInt);
    return arrayOfInt[1] + this.targetParent.getHeight() - i;
  }
  
  private ValueAnimator getAlphaAnimator(float... paramVarArgs) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(paramVarArgs);
    valueAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BaseTransientBottomBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            BaseTransientBottomBar.this.view.setAlpha(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
          }
        });
    return valueAnimator;
  }
  
  private ValueAnimator getScaleAnimator(float... paramVarArgs) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(paramVarArgs);
    valueAnimator.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BaseTransientBottomBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            BaseTransientBottomBar.this.view.setScaleX(f);
            BaseTransientBottomBar.this.view.setScaleY(f);
          }
        });
    return valueAnimator;
  }
  
  private int getScreenHeight() {
    WindowManager windowManager = (WindowManager)this.context.getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
    return displayMetrics.heightPixels;
  }
  
  private int getTranslationYBottom() {
    int j = this.view.getHeight();
    ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
    int i = j;
    if (layoutParams instanceof ViewGroup.MarginLayoutParams)
      i = j + ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin; 
    return i;
  }
  
  private int getViewAbsoluteBottom() {
    int[] arrayOfInt = new int[2];
    this.view.getLocationOnScreen(arrayOfInt);
    return arrayOfInt[1] + this.view.getHeight();
  }
  
  private boolean isSwipeDismissable() {
    boolean bool;
    ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
    if (layoutParams instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior() instanceof SwipeDismissBehavior) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setUpBehavior(CoordinatorLayout.LayoutParams paramLayoutParams) {
    SwipeDismissBehavior<? extends View> swipeDismissBehavior = this.behavior;
    if (swipeDismissBehavior == null)
      swipeDismissBehavior = getNewBehavior(); 
    if (swipeDismissBehavior instanceof Behavior)
      ((Behavior)swipeDismissBehavior).setBaseTransientBottomBar(this); 
    swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
          final BaseTransientBottomBar this$0;
          
          public void onDismiss(View param1View) {
            if (param1View.getParent() != null)
              param1View.setVisibility(8); 
            BaseTransientBottomBar.this.dispatchDismiss(0);
          }
          
          public void onDragStateChanged(int param1Int) {
            switch (param1Int) {
              default:
                return;
              case 1:
              case 2:
                SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
              case 0:
                break;
            } 
            SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
          }
        });
    paramLayoutParams.setBehavior((CoordinatorLayout.Behavior)swipeDismissBehavior);
    if (this.anchorView == null)
      paramLayoutParams.insetEdge = 80; 
  }
  
  private boolean shouldUpdateGestureInset() {
    boolean bool;
    if (this.extraBottomMarginGestureInset > 0 && !this.gestureInsetBottomIgnored && isSwipeDismissable()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void showViewImpl() {
    if (shouldAnimate()) {
      animateViewIn();
    } else {
      if (this.view.getParent() != null)
        this.view.setVisibility(0); 
      onViewShown();
    } 
  }
  
  private void startFadeInAnimation() {
    ValueAnimator valueAnimator1 = getAlphaAnimator(new float[] { 0.0F, 1.0F });
    ValueAnimator valueAnimator2 = getScaleAnimator(new float[] { 0.8F, 1.0F });
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(new Animator[] { (Animator)valueAnimator1, (Animator)valueAnimator2 });
    animatorSet.setDuration(150L);
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewShown();
          }
        });
    animatorSet.start();
  }
  
  private void startFadeOutAnimation(final int event) {
    ValueAnimator valueAnimator = getAlphaAnimator(new float[] { 1.0F, 0.0F });
    valueAnimator.setDuration(75L);
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          final int val$event;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewHidden(event);
          }
        });
    valueAnimator.start();
  }
  
  private void startSlideInAnimation() {
    final int translationYBottom = getTranslationYBottom();
    if (USE_OFFSET_API) {
      ViewCompat.offsetTopAndBottom((View)this.view, i);
    } else {
      this.view.setTranslationY(i);
    } 
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setIntValues(new int[] { i, 0 });
    valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.setDuration(250L);
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewShown();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
          }
        });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          private int previousAnimatedIntValue;
          
          final BaseTransientBottomBar this$0;
          
          final int val$translationYBottom;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            int i = ((Integer)param1ValueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
              ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, i - this.previousAnimatedIntValue);
            } else {
              BaseTransientBottomBar.this.view.setTranslationY(i);
            } 
            this.previousAnimatedIntValue = i;
          }
        });
    valueAnimator.start();
  }
  
  private void startSlideOutAnimation(final int event) {
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setIntValues(new int[] { 0, getTranslationYBottom() });
    valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.setDuration(250L);
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          final int val$event;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewHidden(event);
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
          }
        });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          private int previousAnimatedIntValue = 0;
          
          final BaseTransientBottomBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            int i = ((Integer)param1ValueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
              ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, i - this.previousAnimatedIntValue);
            } else {
              BaseTransientBottomBar.this.view.setTranslationY(i);
            } 
            this.previousAnimatedIntValue = i;
          }
        });
    valueAnimator.start();
  }
  
  private void updateMargins() {
    ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      Rect rect = this.originalMargins;
      if (rect != null) {
        int i;
        if (this.anchorView != null) {
          i = this.extraBottomMarginAnchorView;
        } else {
          i = this.extraBottomMarginWindowInset;
        } 
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
        marginLayoutParams.bottomMargin = rect.bottom + i;
        marginLayoutParams.leftMargin = this.originalMargins.left + this.extraLeftMarginWindowInset;
        marginLayoutParams.rightMargin = this.originalMargins.right + this.extraRightMarginWindowInset;
        this.view.requestLayout();
        if (Build.VERSION.SDK_INT >= 29 && shouldUpdateGestureInset()) {
          this.view.removeCallbacks(this.bottomMarginGestureInsetRunnable);
          this.view.post(this.bottomMarginGestureInsetRunnable);
        } 
        return;
      } 
    } 
    Log.w(TAG, "Unable to update margins because layout params are not MarginLayoutParams");
  }
  
  public B addCallback(BaseCallback<B> paramBaseCallback) {
    if (paramBaseCallback == null)
      return (B)this; 
    if (this.callbacks == null)
      this.callbacks = new ArrayList<>(); 
    this.callbacks.add(paramBaseCallback);
    return (B)this;
  }
  
  void animateViewIn() {
    this.view.post(new Runnable() {
          final BaseTransientBottomBar this$0;
          
          public void run() {
            if (BaseTransientBottomBar.this.view == null)
              return; 
            if (BaseTransientBottomBar.this.view.getParent() != null)
              BaseTransientBottomBar.this.view.setVisibility(0); 
            if (BaseTransientBottomBar.this.view.getAnimationMode() == 1) {
              BaseTransientBottomBar.this.startFadeInAnimation();
            } else {
              BaseTransientBottomBar.this.startSlideInAnimation();
            } 
          }
        });
  }
  
  public void dismiss() {
    dispatchDismiss(3);
  }
  
  protected void dispatchDismiss(int paramInt) {
    SnackbarManager.getInstance().dismiss(this.managerCallback, paramInt);
  }
  
  public View getAnchorView() {
    return this.anchorView;
  }
  
  public int getAnimationMode() {
    return this.view.getAnimationMode();
  }
  
  public Behavior getBehavior() {
    return this.behavior;
  }
  
  public Context getContext() {
    return this.context;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  protected SwipeDismissBehavior<? extends View> getNewBehavior() {
    return new Behavior();
  }
  
  protected int getSnackbarBaseLayoutResId() {
    int i;
    if (hasSnackbarStyleAttr()) {
      i = R.layout.mtrl_layout_snackbar;
    } else {
      i = R.layout.design_layout_snackbar;
    } 
    return i;
  }
  
  public View getView() {
    return (View)this.view;
  }
  
  protected boolean hasSnackbarStyleAttr() {
    TypedArray typedArray = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
    boolean bool = false;
    int i = typedArray.getResourceId(0, -1);
    typedArray.recycle();
    if (i != -1)
      bool = true; 
    return bool;
  }
  
  final void hideView(int paramInt) {
    if (shouldAnimate() && this.view.getVisibility() == 0) {
      animateViewOut(paramInt);
    } else {
      onViewHidden(paramInt);
    } 
  }
  
  public boolean isAnchorViewLayoutListenerEnabled() {
    return this.anchorViewLayoutListenerEnabled;
  }
  
  public boolean isGestureInsetBottomIgnored() {
    return this.gestureInsetBottomIgnored;
  }
  
  public boolean isShown() {
    return SnackbarManager.getInstance().isCurrent(this.managerCallback);
  }
  
  public boolean isShownOrQueued() {
    return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
  }
  
  void onViewHidden(int paramInt) {
    SnackbarManager.getInstance().onDismissed(this.managerCallback);
    List<BaseCallback<B>> list = this.callbacks;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((BaseCallback<BaseTransientBottomBar>)this.callbacks.get(i)).onDismissed(this, paramInt);  
    ViewParent viewParent = this.view.getParent();
    if (viewParent instanceof ViewGroup)
      ((ViewGroup)viewParent).removeView((View)this.view); 
  }
  
  void onViewShown() {
    SnackbarManager.getInstance().onShown(this.managerCallback);
    List<BaseCallback<B>> list = this.callbacks;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((BaseCallback<BaseTransientBottomBar>)this.callbacks.get(i)).onShown(this);  
  }
  
  public B removeCallback(BaseCallback<B> paramBaseCallback) {
    if (paramBaseCallback == null)
      return (B)this; 
    List<BaseCallback<B>> list = this.callbacks;
    if (list == null)
      return (B)this; 
    list.remove(paramBaseCallback);
    return (B)this;
  }
  
  public B setAnchorView(int paramInt) {
    View view = this.targetParent.findViewById(paramInt);
    if (view != null)
      return setAnchorView(view); 
    throw new IllegalArgumentException("Unable to find anchor view with id: " + paramInt);
  }
  
  public B setAnchorView(View paramView) {
    ViewUtils.removeOnGlobalLayoutListener(this.anchorView, this.anchorViewLayoutListener);
    this.anchorView = paramView;
    ViewUtils.addOnGlobalLayoutListener(paramView, this.anchorViewLayoutListener);
    return (B)this;
  }
  
  public void setAnchorViewLayoutListenerEnabled(boolean paramBoolean) {
    this.anchorViewLayoutListenerEnabled = paramBoolean;
  }
  
  public B setAnimationMode(int paramInt) {
    this.view.setAnimationMode(paramInt);
    return (B)this;
  }
  
  public B setBehavior(Behavior paramBehavior) {
    this.behavior = paramBehavior;
    return (B)this;
  }
  
  public B setDuration(int paramInt) {
    this.duration = paramInt;
    return (B)this;
  }
  
  public B setGestureInsetBottomIgnored(boolean paramBoolean) {
    this.gestureInsetBottomIgnored = paramBoolean;
    return (B)this;
  }
  
  boolean shouldAnimate() {
    AccessibilityManager accessibilityManager = this.accessibilityManager;
    boolean bool = true;
    if (accessibilityManager == null)
      return true; 
    List list = accessibilityManager.getEnabledAccessibilityServiceList(1);
    if (list == null || !list.isEmpty())
      bool = false; 
    return bool;
  }
  
  public void show() {
    SnackbarManager.getInstance().show(getDuration(), this.managerCallback);
  }
  
  final void showView() {
    this.view.setOnAttachStateChangeListener(new OnAttachStateChangeListener() {
          final BaseTransientBottomBar this$0;
          
          public void onViewAttachedToWindow(View param1View) {
            if (Build.VERSION.SDK_INT >= 29) {
              WindowInsets windowInsets = BaseTransientBottomBar.this.view.getRootWindowInsets();
              if (windowInsets != null) {
                BaseTransientBottomBar.access$702(BaseTransientBottomBar.this, (windowInsets.getMandatorySystemGestureInsets()).bottom);
                BaseTransientBottomBar.this.updateMargins();
              } 
            } 
          }
          
          public void onViewDetachedFromWindow(View param1View) {
            if (BaseTransientBottomBar.this.isShownOrQueued())
              BaseTransientBottomBar.handler.post(new Runnable() {
                    final BaseTransientBottomBar.null this$1;
                    
                    public void run() {
                      BaseTransientBottomBar.this.onViewHidden(3);
                    }
                  }); 
          }
        });
    if (this.view.getParent() == null) {
      ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
      if (layoutParams instanceof CoordinatorLayout.LayoutParams)
        setUpBehavior((CoordinatorLayout.LayoutParams)layoutParams); 
      this.extraBottomMarginAnchorView = calculateBottomMarginForAnchorView();
      updateMargins();
      this.view.setVisibility(4);
      this.targetParent.addView((View)this.view);
    } 
    if (ViewCompat.isLaidOut((View)this.view)) {
      showViewImpl();
      return;
    } 
    this.view.setOnLayoutChangeListener(new OnLayoutChangeListener() {
          final BaseTransientBottomBar this$0;
          
          public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
            BaseTransientBottomBar.this.view.setOnLayoutChangeListener((BaseTransientBottomBar.OnLayoutChangeListener)null);
            BaseTransientBottomBar.this.showViewImpl();
          }
        });
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AnimationMode {}
  
  public static abstract class BaseCallback<B> {
    public static final int DISMISS_EVENT_ACTION = 1;
    
    public static final int DISMISS_EVENT_CONSECUTIVE = 4;
    
    public static final int DISMISS_EVENT_MANUAL = 3;
    
    public static final int DISMISS_EVENT_SWIPE = 0;
    
    public static final int DISMISS_EVENT_TIMEOUT = 2;
    
    public void onDismissed(B param1B, int param1Int) {}
    
    public void onShown(B param1B) {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface DismissEvent {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DismissEvent {}
  
  public static class Behavior extends SwipeDismissBehavior<View> {
    private final BaseTransientBottomBar.BehaviorDelegate delegate = new BaseTransientBottomBar.BehaviorDelegate(this);
    
    private void setBaseTransientBottomBar(BaseTransientBottomBar<?> param1BaseTransientBottomBar) {
      this.delegate.setBaseTransientBottomBar(param1BaseTransientBottomBar);
    }
    
    public boolean canSwipeDismissView(View param1View) {
      return this.delegate.canSwipeDismissView(param1View);
    }
    
    public boolean onInterceptTouchEvent(CoordinatorLayout param1CoordinatorLayout, View param1View, MotionEvent param1MotionEvent) {
      this.delegate.onInterceptTouchEvent(param1CoordinatorLayout, param1View, param1MotionEvent);
      return super.onInterceptTouchEvent(param1CoordinatorLayout, param1View, param1MotionEvent);
    }
  }
  
  public static class BehaviorDelegate {
    private SnackbarManager.Callback managerCallback;
    
    public BehaviorDelegate(SwipeDismissBehavior<?> param1SwipeDismissBehavior) {
      param1SwipeDismissBehavior.setStartAlphaSwipeDistance(0.1F);
      param1SwipeDismissBehavior.setEndAlphaSwipeDistance(0.6F);
      param1SwipeDismissBehavior.setSwipeDirection(0);
    }
    
    public boolean canSwipeDismissView(View param1View) {
      return param1View instanceof BaseTransientBottomBar.SnackbarBaseLayout;
    }
    
    public void onInterceptTouchEvent(CoordinatorLayout param1CoordinatorLayout, View param1View, MotionEvent param1MotionEvent) {
      switch (param1MotionEvent.getActionMasked()) {
        default:
          return;
        case 1:
        case 3:
          SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
        case 0:
          break;
      } 
      if (param1CoordinatorLayout.isPointInChildBounds(param1View, (int)param1MotionEvent.getX(), (int)param1MotionEvent.getY()))
        SnackbarManager.getInstance().pauseTimeout(this.managerCallback); 
    }
    
    public void setBaseTransientBottomBar(BaseTransientBottomBar<?> param1BaseTransientBottomBar) {
      this.managerCallback = param1BaseTransientBottomBar.managerCallback;
    }
  }
  
  @Deprecated
  public static interface ContentViewCallback extends ContentViewCallback {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Duration {}
  
  protected static interface OnAttachStateChangeListener {
    void onViewAttachedToWindow(View param1View);
    
    void onViewDetachedFromWindow(View param1View);
  }
  
  protected static interface OnLayoutChangeListener {
    void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  protected static class SnackbarBaseLayout extends FrameLayout {
    private static final View.OnTouchListener consumeAllTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View param2View, MotionEvent param2MotionEvent) {
          return true;
        }
      };
    
    private final float actionTextColorAlpha;
    
    private int animationMode;
    
    private final float backgroundOverlayColorAlpha;
    
    private ColorStateList backgroundTint;
    
    private PorterDuff.Mode backgroundTintMode;
    
    private BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener;
    
    private BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener;
    
    protected SnackbarBaseLayout(Context param1Context) {
      this(param1Context, (AttributeSet)null);
    }
    
    protected SnackbarBaseLayout(Context param1Context, AttributeSet param1AttributeSet) {
      super(MaterialThemeOverlay.wrap(param1Context, param1AttributeSet, 0, 0), param1AttributeSet);
      param1Context = getContext();
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.SnackbarLayout);
      if (typedArray.hasValue(R.styleable.SnackbarLayout_elevation))
        ViewCompat.setElevation((View)this, typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0)); 
      this.animationMode = typedArray.getInt(R.styleable.SnackbarLayout_animationMode, 0);
      this.backgroundOverlayColorAlpha = typedArray.getFloat(R.styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0F);
      setBackgroundTintList(MaterialResources.getColorStateList(param1Context, typedArray, R.styleable.SnackbarLayout_backgroundTint));
      setBackgroundTintMode(ViewUtils.parseTintMode(typedArray.getInt(R.styleable.SnackbarLayout_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN));
      this.actionTextColorAlpha = typedArray.getFloat(R.styleable.SnackbarLayout_actionTextColorAlpha, 1.0F);
      typedArray.recycle();
      setOnTouchListener(consumeAllTouchListener);
      setFocusable(true);
      if (getBackground() == null)
        ViewCompat.setBackground((View)this, createThemedBackground()); 
    }
    
    private Drawable createThemedBackground() {
      Drawable drawable;
      float f = getResources().getDimension(R.dimen.mtrl_snackbar_background_corner_radius);
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setShape(0);
      gradientDrawable.setCornerRadius(f);
      gradientDrawable.setColor(MaterialColors.layer((View)this, R.attr.colorSurface, R.attr.colorOnSurface, getBackgroundOverlayColorAlpha()));
      if (this.backgroundTint != null) {
        drawable = DrawableCompat.wrap((Drawable)gradientDrawable);
        DrawableCompat.setTintList(drawable, this.backgroundTint);
        return drawable;
      } 
      return DrawableCompat.wrap(drawable);
    }
    
    float getActionTextColorAlpha() {
      return this.actionTextColorAlpha;
    }
    
    int getAnimationMode() {
      return this.animationMode;
    }
    
    float getBackgroundOverlayColorAlpha() {
      return this.backgroundOverlayColorAlpha;
    }
    
    protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
      if (onAttachStateChangeListener != null)
        onAttachStateChangeListener.onViewAttachedToWindow((View)this); 
      ViewCompat.requestApplyInsets((View)this);
    }
    
    protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
      if (onAttachStateChangeListener != null)
        onAttachStateChangeListener.onViewDetachedFromWindow((View)this); 
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.onLayout(param1Boolean, param1Int1, param1Int2, param1Int3, param1Int4);
      BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener = this.onLayoutChangeListener;
      if (onLayoutChangeListener != null)
        onLayoutChangeListener.onLayoutChange((View)this, param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    void setAnimationMode(int param1Int) {
      this.animationMode = param1Int;
    }
    
    public void setBackground(Drawable param1Drawable) {
      setBackgroundDrawable(param1Drawable);
    }
    
    public void setBackgroundDrawable(Drawable param1Drawable) {
      Drawable drawable = param1Drawable;
      if (param1Drawable != null) {
        drawable = param1Drawable;
        if (this.backgroundTint != null) {
          drawable = DrawableCompat.wrap(param1Drawable.mutate());
          DrawableCompat.setTintList(drawable, this.backgroundTint);
          DrawableCompat.setTintMode(drawable, this.backgroundTintMode);
        } 
      } 
      super.setBackgroundDrawable(drawable);
    }
    
    public void setBackgroundTintList(ColorStateList param1ColorStateList) {
      this.backgroundTint = param1ColorStateList;
      if (getBackground() != null) {
        Drawable drawable = DrawableCompat.wrap(getBackground().mutate());
        DrawableCompat.setTintList(drawable, param1ColorStateList);
        DrawableCompat.setTintMode(drawable, this.backgroundTintMode);
        if (drawable != getBackground())
          super.setBackgroundDrawable(drawable); 
      } 
    }
    
    public void setBackgroundTintMode(PorterDuff.Mode param1Mode) {
      this.backgroundTintMode = param1Mode;
      if (getBackground() != null) {
        Drawable drawable = DrawableCompat.wrap(getBackground().mutate());
        DrawableCompat.setTintMode(drawable, param1Mode);
        if (drawable != getBackground())
          super.setBackgroundDrawable(drawable); 
      } 
    }
    
    void setOnAttachStateChangeListener(BaseTransientBottomBar.OnAttachStateChangeListener param1OnAttachStateChangeListener) {
      this.onAttachStateChangeListener = param1OnAttachStateChangeListener;
    }
    
    public void setOnClickListener(View.OnClickListener param1OnClickListener) {
      View.OnTouchListener onTouchListener;
      if (param1OnClickListener != null) {
        onTouchListener = null;
      } else {
        onTouchListener = consumeAllTouchListener;
      } 
      setOnTouchListener(onTouchListener);
      super.setOnClickListener(param1OnClickListener);
    }
    
    void setOnLayoutChangeListener(BaseTransientBottomBar.OnLayoutChangeListener param1OnLayoutChangeListener) {
      this.onLayoutChangeListener = param1OnLayoutChangeListener;
    }
  }
  
  static final class null implements View.OnTouchListener {
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\snackbar\BaseTransientBottomBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */