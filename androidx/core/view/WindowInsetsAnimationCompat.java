package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.R;
import androidx.core.graphics.Insets;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class WindowInsetsAnimationCompat {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "WindowInsetsAnimCompat";
  
  private Impl mImpl;
  
  public WindowInsetsAnimationCompat(int paramInt, Interpolator paramInterpolator, long paramLong) {
    if (Build.VERSION.SDK_INT >= 30) {
      this.mImpl = new Impl30(paramInt, paramInterpolator, paramLong);
    } else if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new Impl21(paramInt, paramInterpolator, paramLong);
    } else {
      this.mImpl = new Impl(0, paramInterpolator, paramLong);
    } 
  }
  
  private WindowInsetsAnimationCompat(WindowInsetsAnimation paramWindowInsetsAnimation) {
    this(0, null, 0L);
    if (Build.VERSION.SDK_INT >= 30)
      this.mImpl = new Impl30(paramWindowInsetsAnimation); 
  }
  
  static void setCallback(View paramView, Callback paramCallback) {
    if (Build.VERSION.SDK_INT >= 30) {
      Impl30.setCallback(paramView, paramCallback);
    } else if (Build.VERSION.SDK_INT >= 21) {
      Impl21.setCallback(paramView, paramCallback);
    } 
  }
  
  static WindowInsetsAnimationCompat toWindowInsetsAnimationCompat(WindowInsetsAnimation paramWindowInsetsAnimation) {
    return new WindowInsetsAnimationCompat(paramWindowInsetsAnimation);
  }
  
  public float getAlpha() {
    return this.mImpl.getAlpha();
  }
  
  public long getDurationMillis() {
    return this.mImpl.getDurationMillis();
  }
  
  public float getFraction() {
    return this.mImpl.getFraction();
  }
  
  public float getInterpolatedFraction() {
    return this.mImpl.getInterpolatedFraction();
  }
  
  public Interpolator getInterpolator() {
    return this.mImpl.getInterpolator();
  }
  
  public int getTypeMask() {
    return this.mImpl.getTypeMask();
  }
  
  public void setAlpha(float paramFloat) {
    this.mImpl.setAlpha(paramFloat);
  }
  
  public void setFraction(float paramFloat) {
    this.mImpl.setFraction(paramFloat);
  }
  
  public static final class BoundsCompat {
    private final Insets mLowerBound;
    
    private final Insets mUpperBound;
    
    private BoundsCompat(WindowInsetsAnimation.Bounds param1Bounds) {
      this.mLowerBound = WindowInsetsAnimationCompat.Impl30.getLowerBounds(param1Bounds);
      this.mUpperBound = WindowInsetsAnimationCompat.Impl30.getHigherBounds(param1Bounds);
    }
    
    public BoundsCompat(Insets param1Insets1, Insets param1Insets2) {
      this.mLowerBound = param1Insets1;
      this.mUpperBound = param1Insets2;
    }
    
    public static BoundsCompat toBoundsCompat(WindowInsetsAnimation.Bounds param1Bounds) {
      return new BoundsCompat(param1Bounds);
    }
    
    public Insets getLowerBound() {
      return this.mLowerBound;
    }
    
    public Insets getUpperBound() {
      return this.mUpperBound;
    }
    
    public BoundsCompat inset(Insets param1Insets) {
      return new BoundsCompat(WindowInsetsCompat.insetInsets(this.mLowerBound, param1Insets.left, param1Insets.top, param1Insets.right, param1Insets.bottom), WindowInsetsCompat.insetInsets(this.mUpperBound, param1Insets.left, param1Insets.top, param1Insets.right, param1Insets.bottom));
    }
    
    public WindowInsetsAnimation.Bounds toBounds() {
      return WindowInsetsAnimationCompat.Impl30.createPlatformBounds(this);
    }
    
    public String toString() {
      return "Bounds{lower=" + this.mLowerBound + " upper=" + this.mUpperBound + "}";
    }
  }
  
  public static abstract class Callback {
    public static final int DISPATCH_MODE_CONTINUE_ON_SUBTREE = 1;
    
    public static final int DISPATCH_MODE_STOP = 0;
    
    WindowInsets mDispachedInsets;
    
    private final int mDispatchMode;
    
    public Callback(int param1Int) {
      this.mDispatchMode = param1Int;
    }
    
    public final int getDispatchMode() {
      return this.mDispatchMode;
    }
    
    public void onEnd(WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat) {}
    
    public void onPrepare(WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat) {}
    
    public abstract WindowInsetsCompat onProgress(WindowInsetsCompat param1WindowInsetsCompat, List<WindowInsetsAnimationCompat> param1List);
    
    public WindowInsetsAnimationCompat.BoundsCompat onStart(WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat, WindowInsetsAnimationCompat.BoundsCompat param1BoundsCompat) {
      return param1BoundsCompat;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface DispatchMode {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DispatchMode {}
  
  private static class Impl {
    private float mAlpha;
    
    private final long mDurationMillis;
    
    private float mFraction;
    
    private final Interpolator mInterpolator;
    
    private final int mTypeMask;
    
    Impl(int param1Int, Interpolator param1Interpolator, long param1Long) {
      this.mTypeMask = param1Int;
      this.mInterpolator = param1Interpolator;
      this.mDurationMillis = param1Long;
    }
    
    public float getAlpha() {
      return this.mAlpha;
    }
    
    public long getDurationMillis() {
      return this.mDurationMillis;
    }
    
    public float getFraction() {
      return this.mFraction;
    }
    
    public float getInterpolatedFraction() {
      Interpolator interpolator = this.mInterpolator;
      return (interpolator != null) ? interpolator.getInterpolation(this.mFraction) : this.mFraction;
    }
    
    public Interpolator getInterpolator() {
      return this.mInterpolator;
    }
    
    public int getTypeMask() {
      return this.mTypeMask;
    }
    
    public void setAlpha(float param1Float) {
      this.mAlpha = param1Float;
    }
    
    public void setFraction(float param1Float) {
      this.mFraction = param1Float;
    }
  }
  
  private static class Impl21 extends Impl {
    Impl21(int param1Int, Interpolator param1Interpolator, long param1Long) {
      super(param1Int, param1Interpolator, param1Long);
    }
    
    static int buildAnimationMask(WindowInsetsCompat param1WindowInsetsCompat1, WindowInsetsCompat param1WindowInsetsCompat2) {
      int j = 0;
      int i = 1;
      while (i <= 256) {
        int k = j;
        if (!param1WindowInsetsCompat1.getInsets(i).equals(param1WindowInsetsCompat2.getInsets(i)))
          k = j | i; 
        i <<= 1;
        j = k;
      } 
      return j;
    }
    
    static WindowInsetsAnimationCompat.BoundsCompat computeAnimationBounds(WindowInsetsCompat param1WindowInsetsCompat1, WindowInsetsCompat param1WindowInsetsCompat2, int param1Int) {
      Insets insets1 = param1WindowInsetsCompat1.getInsets(param1Int);
      Insets insets2 = param1WindowInsetsCompat2.getInsets(param1Int);
      return new WindowInsetsAnimationCompat.BoundsCompat(Insets.of(Math.min(insets1.left, insets2.left), Math.min(insets1.top, insets2.top), Math.min(insets1.right, insets2.right), Math.min(insets1.bottom, insets2.bottom)), Insets.of(Math.max(insets1.left, insets2.left), Math.max(insets1.top, insets2.top), Math.max(insets1.right, insets2.right), Math.max(insets1.bottom, insets2.bottom)));
    }
    
    private static View.OnApplyWindowInsetsListener createProxyListener(View param1View, WindowInsetsAnimationCompat.Callback param1Callback) {
      return new Impl21OnApplyWindowInsetsListener(param1View, param1Callback);
    }
    
    static void dispatchOnEnd(View param1View, WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat) {
      WindowInsetsAnimationCompat.Callback callback = getCallback(param1View);
      if (callback != null) {
        callback.onEnd(param1WindowInsetsAnimationCompat);
        if (callback.getDispatchMode() == 0)
          return; 
      } 
      if (param1View instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View;
        for (byte b = 0; b < viewGroup.getChildCount(); b++)
          dispatchOnEnd(viewGroup.getChildAt(b), param1WindowInsetsAnimationCompat); 
      } 
    }
    
    static void dispatchOnPrepare(View param1View, WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat, WindowInsets param1WindowInsets, boolean param1Boolean) {
      WindowInsetsAnimationCompat.Callback callback = getCallback(param1View);
      boolean bool = param1Boolean;
      if (callback != null) {
        callback.mDispachedInsets = param1WindowInsets;
        bool = param1Boolean;
        if (!param1Boolean) {
          callback.onPrepare(param1WindowInsetsAnimationCompat);
          if (callback.getDispatchMode() == 0) {
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          bool = param1Boolean;
        } 
      } 
      if (param1View instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View;
        for (byte b = 0; b < viewGroup.getChildCount(); b++)
          dispatchOnPrepare(viewGroup.getChildAt(b), param1WindowInsetsAnimationCompat, param1WindowInsets, bool); 
      } 
    }
    
    static void dispatchOnProgress(View param1View, WindowInsetsCompat param1WindowInsetsCompat, List<WindowInsetsAnimationCompat> param1List) {
      WindowInsetsAnimationCompat.Callback callback = getCallback(param1View);
      WindowInsetsCompat windowInsetsCompat = param1WindowInsetsCompat;
      param1WindowInsetsCompat = windowInsetsCompat;
      if (callback != null) {
        param1WindowInsetsCompat = callback.onProgress(windowInsetsCompat, param1List);
        if (callback.getDispatchMode() == 0)
          return; 
      } 
      if (param1View instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View;
        for (byte b = 0; b < viewGroup.getChildCount(); b++)
          dispatchOnProgress(viewGroup.getChildAt(b), param1WindowInsetsCompat, param1List); 
      } 
    }
    
    static void dispatchOnStart(View param1View, WindowInsetsAnimationCompat param1WindowInsetsAnimationCompat, WindowInsetsAnimationCompat.BoundsCompat param1BoundsCompat) {
      WindowInsetsAnimationCompat.Callback callback = getCallback(param1View);
      if (callback != null) {
        callback.onStart(param1WindowInsetsAnimationCompat, param1BoundsCompat);
        if (callback.getDispatchMode() == 0)
          return; 
      } 
      if (param1View instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View;
        for (byte b = 0; b < viewGroup.getChildCount(); b++)
          dispatchOnStart(viewGroup.getChildAt(b), param1WindowInsetsAnimationCompat, param1BoundsCompat); 
      } 
    }
    
    static WindowInsets forwardToViewIfNeeded(View param1View, WindowInsets param1WindowInsets) {
      return (param1View.getTag(R.id.tag_on_apply_window_listener) != null) ? param1WindowInsets : param1View.onApplyWindowInsets(param1WindowInsets);
    }
    
    static WindowInsetsAnimationCompat.Callback getCallback(View param1View) {
      WindowInsetsAnimationCompat.Callback callback;
      Object object = param1View.getTag(R.id.tag_window_insets_animation_callback);
      param1View = null;
      if (object instanceof Impl21OnApplyWindowInsetsListener)
        callback = ((Impl21OnApplyWindowInsetsListener)object).mCallback; 
      return callback;
    }
    
    static WindowInsetsCompat interpolateInsets(WindowInsetsCompat param1WindowInsetsCompat1, WindowInsetsCompat param1WindowInsetsCompat2, float param1Float, int param1Int) {
      WindowInsetsCompat.Builder builder = new WindowInsetsCompat.Builder(param1WindowInsetsCompat1);
      int i;
      for (i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) == 0) {
          builder.setInsets(i, param1WindowInsetsCompat1.getInsets(i));
        } else {
          Insets insets2 = param1WindowInsetsCompat1.getInsets(i);
          Insets insets1 = param1WindowInsetsCompat2.getInsets(i);
          builder.setInsets(i, WindowInsetsCompat.insetInsets(insets2, (int)(((insets2.left - insets1.left) * (1.0F - param1Float)) + 0.5D), (int)(((insets2.top - insets1.top) * (1.0F - param1Float)) + 0.5D), (int)(((insets2.right - insets1.right) * (1.0F - param1Float)) + 0.5D), (int)(((insets2.bottom - insets1.bottom) * (1.0F - param1Float)) + 0.5D)));
        } 
      } 
      return builder.build();
    }
    
    static void setCallback(View param1View, WindowInsetsAnimationCompat.Callback param1Callback) {
      Object object = param1View.getTag(R.id.tag_on_apply_window_listener);
      if (param1Callback == null) {
        param1View.setTag(R.id.tag_window_insets_animation_callback, null);
        if (object == null)
          param1View.setOnApplyWindowInsetsListener(null); 
      } else {
        View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = createProxyListener(param1View, param1Callback);
        param1View.setTag(R.id.tag_window_insets_animation_callback, onApplyWindowInsetsListener);
        if (object == null)
          param1View.setOnApplyWindowInsetsListener(onApplyWindowInsetsListener); 
      } 
    }
    
    private static class Impl21OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
      private static final int COMPAT_ANIMATION_DURATION = 160;
      
      final WindowInsetsAnimationCompat.Callback mCallback;
      
      private WindowInsetsCompat mLastInsets;
      
      Impl21OnApplyWindowInsetsListener(View param2View, WindowInsetsAnimationCompat.Callback param2Callback) {
        this.mCallback = param2Callback;
        WindowInsetsCompat windowInsetsCompat = ViewCompat.getRootWindowInsets(param2View);
        if (windowInsetsCompat != null) {
          windowInsetsCompat = (new WindowInsetsCompat.Builder(windowInsetsCompat)).build();
        } else {
          windowInsetsCompat = null;
        } 
        this.mLastInsets = windowInsetsCompat;
      }
      
      public WindowInsets onApplyWindowInsets(final View v, WindowInsets param2WindowInsets) {
        if (!v.isLaidOut()) {
          this.mLastInsets = WindowInsetsCompat.toWindowInsetsCompat(param2WindowInsets, v);
          return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param2WindowInsets);
        } 
        final WindowInsetsCompat targetInsets = WindowInsetsCompat.toWindowInsetsCompat(param2WindowInsets, v);
        if (this.mLastInsets == null)
          this.mLastInsets = ViewCompat.getRootWindowInsets(v); 
        if (this.mLastInsets == null) {
          this.mLastInsets = windowInsetsCompat1;
          return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param2WindowInsets);
        } 
        WindowInsetsAnimationCompat.Callback callback = WindowInsetsAnimationCompat.Impl21.getCallback(v);
        if (callback != null && Objects.equals(callback.mDispachedInsets, param2WindowInsets))
          return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param2WindowInsets); 
        final int animationMask = WindowInsetsAnimationCompat.Impl21.buildAnimationMask(windowInsetsCompat1, this.mLastInsets);
        if (i == 0)
          return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param2WindowInsets); 
        final WindowInsetsCompat startingInsets = this.mLastInsets;
        final WindowInsetsAnimationCompat anim = new WindowInsetsAnimationCompat(i, (Interpolator)new DecelerateInterpolator(), 160L);
        windowInsetsAnimationCompat.setFraction(0.0F);
        final ValueAnimator animator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F }).setDuration(windowInsetsAnimationCompat.getDurationMillis());
        final WindowInsetsAnimationCompat.BoundsCompat animationBounds = WindowInsetsAnimationCompat.Impl21.computeAnimationBounds(windowInsetsCompat1, windowInsetsCompat2, i);
        WindowInsetsAnimationCompat.Impl21.dispatchOnPrepare(v, windowInsetsAnimationCompat, param2WindowInsets, false);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
              
              final WindowInsetsAnimationCompat val$anim;
              
              final int val$animationMask;
              
              final WindowInsetsCompat val$startingInsets;
              
              final WindowInsetsCompat val$targetInsets;
              
              final View val$v;
              
              public void onAnimationUpdate(ValueAnimator param3ValueAnimator) {
                anim.setFraction(param3ValueAnimator.getAnimatedFraction());
                WindowInsetsCompat windowInsetsCompat = WindowInsetsAnimationCompat.Impl21.interpolateInsets(targetInsets, startingInsets, anim.getInterpolatedFraction(), animationMask);
                List<WindowInsetsAnimationCompat> list = Collections.singletonList(anim);
                WindowInsetsAnimationCompat.Impl21.dispatchOnProgress(v, windowInsetsCompat, list);
              }
            });
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
              
              final WindowInsetsAnimationCompat val$anim;
              
              final View val$v;
              
              public void onAnimationEnd(Animator param3Animator) {
                anim.setFraction(1.0F);
                WindowInsetsAnimationCompat.Impl21.dispatchOnEnd(v, anim);
              }
            });
        OneShotPreDrawListener.add(v, new Runnable() {
              final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
              
              final WindowInsetsAnimationCompat val$anim;
              
              final WindowInsetsAnimationCompat.BoundsCompat val$animationBounds;
              
              final ValueAnimator val$animator;
              
              final View val$v;
              
              public void run() {
                WindowInsetsAnimationCompat.Impl21.dispatchOnStart(v, anim, animationBounds);
                animator.start();
              }
            });
        this.mLastInsets = windowInsetsCompat1;
        return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param2WindowInsets);
      }
    }
    
    class null implements ValueAnimator.AnimatorUpdateListener {
      final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
      
      final WindowInsetsAnimationCompat val$anim;
      
      final int val$animationMask;
      
      final WindowInsetsCompat val$startingInsets;
      
      final WindowInsetsCompat val$targetInsets;
      
      final View val$v;
      
      public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
        anim.setFraction(param2ValueAnimator.getAnimatedFraction());
        WindowInsetsCompat windowInsetsCompat = WindowInsetsAnimationCompat.Impl21.interpolateInsets(targetInsets, startingInsets, anim.getInterpolatedFraction(), animationMask);
        List<WindowInsetsAnimationCompat> list = Collections.singletonList(anim);
        WindowInsetsAnimationCompat.Impl21.dispatchOnProgress(v, windowInsetsCompat, list);
      }
    }
    
    class null extends AnimatorListenerAdapter {
      final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
      
      final WindowInsetsAnimationCompat val$anim;
      
      final View val$v;
      
      public void onAnimationEnd(Animator param2Animator) {
        anim.setFraction(1.0F);
        WindowInsetsAnimationCompat.Impl21.dispatchOnEnd(v, anim);
      }
    }
    
    class null implements Runnable {
      final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
      
      final WindowInsetsAnimationCompat val$anim;
      
      final WindowInsetsAnimationCompat.BoundsCompat val$animationBounds;
      
      final ValueAnimator val$animator;
      
      final View val$v;
      
      public void run() {
        WindowInsetsAnimationCompat.Impl21.dispatchOnStart(v, anim, animationBounds);
        animator.start();
      }
    }
  }
  
  private static class Impl21OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
    private static final int COMPAT_ANIMATION_DURATION = 160;
    
    final WindowInsetsAnimationCompat.Callback mCallback;
    
    private WindowInsetsCompat mLastInsets;
    
    Impl21OnApplyWindowInsetsListener(View param1View, WindowInsetsAnimationCompat.Callback param1Callback) {
      this.mCallback = param1Callback;
      WindowInsetsCompat windowInsetsCompat = ViewCompat.getRootWindowInsets(param1View);
      if (windowInsetsCompat != null) {
        windowInsetsCompat = (new WindowInsetsCompat.Builder(windowInsetsCompat)).build();
      } else {
        windowInsetsCompat = null;
      } 
      this.mLastInsets = windowInsetsCompat;
    }
    
    public WindowInsets onApplyWindowInsets(final View v, WindowInsets param1WindowInsets) {
      if (!v.isLaidOut()) {
        this.mLastInsets = WindowInsetsCompat.toWindowInsetsCompat(param1WindowInsets, v);
        return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param1WindowInsets);
      } 
      final WindowInsetsCompat targetInsets = WindowInsetsCompat.toWindowInsetsCompat(param1WindowInsets, v);
      if (this.mLastInsets == null)
        this.mLastInsets = ViewCompat.getRootWindowInsets(v); 
      if (this.mLastInsets == null) {
        this.mLastInsets = windowInsetsCompat1;
        return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param1WindowInsets);
      } 
      WindowInsetsAnimationCompat.Callback callback = WindowInsetsAnimationCompat.Impl21.getCallback(v);
      if (callback != null && Objects.equals(callback.mDispachedInsets, param1WindowInsets))
        return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param1WindowInsets); 
      final int animationMask = WindowInsetsAnimationCompat.Impl21.buildAnimationMask(windowInsetsCompat1, this.mLastInsets);
      if (i == 0)
        return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param1WindowInsets); 
      final WindowInsetsCompat startingInsets = this.mLastInsets;
      final WindowInsetsAnimationCompat anim = new WindowInsetsAnimationCompat(i, (Interpolator)new DecelerateInterpolator(), 160L);
      windowInsetsAnimationCompat.setFraction(0.0F);
      final ValueAnimator animator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F }).setDuration(windowInsetsAnimationCompat.getDurationMillis());
      final WindowInsetsAnimationCompat.BoundsCompat animationBounds = WindowInsetsAnimationCompat.Impl21.computeAnimationBounds(windowInsetsCompat1, windowInsetsCompat2, i);
      WindowInsetsAnimationCompat.Impl21.dispatchOnPrepare(v, windowInsetsAnimationCompat, param1WindowInsets, false);
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
            
            final WindowInsetsAnimationCompat val$anim;
            
            final int val$animationMask;
            
            final WindowInsetsCompat val$startingInsets;
            
            final WindowInsetsCompat val$targetInsets;
            
            final View val$v;
            
            public void onAnimationUpdate(ValueAnimator param3ValueAnimator) {
              anim.setFraction(param3ValueAnimator.getAnimatedFraction());
              WindowInsetsCompat windowInsetsCompat = WindowInsetsAnimationCompat.Impl21.interpolateInsets(targetInsets, startingInsets, anim.getInterpolatedFraction(), animationMask);
              List<WindowInsetsAnimationCompat> list = Collections.singletonList(anim);
              WindowInsetsAnimationCompat.Impl21.dispatchOnProgress(v, windowInsetsCompat, list);
            }
          });
      valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
            
            final WindowInsetsAnimationCompat val$anim;
            
            final View val$v;
            
            public void onAnimationEnd(Animator param3Animator) {
              anim.setFraction(1.0F);
              WindowInsetsAnimationCompat.Impl21.dispatchOnEnd(v, anim);
            }
          });
      OneShotPreDrawListener.add(v, new Runnable() {
            final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
            
            final WindowInsetsAnimationCompat val$anim;
            
            final WindowInsetsAnimationCompat.BoundsCompat val$animationBounds;
            
            final ValueAnimator val$animator;
            
            final View val$v;
            
            public void run() {
              WindowInsetsAnimationCompat.Impl21.dispatchOnStart(v, anim, animationBounds);
              animator.start();
            }
          });
      this.mLastInsets = windowInsetsCompat1;
      return WindowInsetsAnimationCompat.Impl21.forwardToViewIfNeeded(v, param1WindowInsets);
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
    
    final WindowInsetsAnimationCompat val$anim;
    
    final int val$animationMask;
    
    final WindowInsetsCompat val$startingInsets;
    
    final WindowInsetsCompat val$targetInsets;
    
    final View val$v;
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      anim.setFraction(param1ValueAnimator.getAnimatedFraction());
      WindowInsetsCompat windowInsetsCompat = WindowInsetsAnimationCompat.Impl21.interpolateInsets(targetInsets, startingInsets, anim.getInterpolatedFraction(), animationMask);
      List<WindowInsetsAnimationCompat> list = Collections.singletonList(anim);
      WindowInsetsAnimationCompat.Impl21.dispatchOnProgress(v, windowInsetsCompat, list);
    }
  }
  
  class null extends AnimatorListenerAdapter {
    final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
    
    final WindowInsetsAnimationCompat val$anim;
    
    final View val$v;
    
    public void onAnimationEnd(Animator param1Animator) {
      anim.setFraction(1.0F);
      WindowInsetsAnimationCompat.Impl21.dispatchOnEnd(v, anim);
    }
  }
  
  class null implements Runnable {
    final WindowInsetsAnimationCompat.Impl21.Impl21OnApplyWindowInsetsListener this$0;
    
    final WindowInsetsAnimationCompat val$anim;
    
    final WindowInsetsAnimationCompat.BoundsCompat val$animationBounds;
    
    final ValueAnimator val$animator;
    
    final View val$v;
    
    public void run() {
      WindowInsetsAnimationCompat.Impl21.dispatchOnStart(v, anim, animationBounds);
      animator.start();
    }
  }
  
  private static class Impl30 extends Impl {
    private final WindowInsetsAnimation mWrapped;
    
    Impl30(int param1Int, Interpolator param1Interpolator, long param1Long) {
      this(new WindowInsetsAnimation(param1Int, param1Interpolator, param1Long));
    }
    
    Impl30(WindowInsetsAnimation param1WindowInsetsAnimation) {
      super(0, null, 0L);
      this.mWrapped = param1WindowInsetsAnimation;
    }
    
    public static WindowInsetsAnimation.Bounds createPlatformBounds(WindowInsetsAnimationCompat.BoundsCompat param1BoundsCompat) {
      return new WindowInsetsAnimation.Bounds(param1BoundsCompat.getLowerBound().toPlatformInsets(), param1BoundsCompat.getUpperBound().toPlatformInsets());
    }
    
    public static Insets getHigherBounds(WindowInsetsAnimation.Bounds param1Bounds) {
      return Insets.toCompatInsets(param1Bounds.getUpperBound());
    }
    
    public static Insets getLowerBounds(WindowInsetsAnimation.Bounds param1Bounds) {
      return Insets.toCompatInsets(param1Bounds.getLowerBound());
    }
    
    public static void setCallback(View param1View, WindowInsetsAnimationCompat.Callback param1Callback) {
      if (param1Callback != null) {
        ProxyCallback proxyCallback = new ProxyCallback(param1Callback);
      } else {
        param1Callback = null;
      } 
      param1View.setWindowInsetsAnimationCallback((WindowInsetsAnimation.Callback)param1Callback);
    }
    
    public long getDurationMillis() {
      return this.mWrapped.getDurationMillis();
    }
    
    public float getFraction() {
      return this.mWrapped.getFraction();
    }
    
    public float getInterpolatedFraction() {
      return this.mWrapped.getInterpolatedFraction();
    }
    
    public Interpolator getInterpolator() {
      return this.mWrapped.getInterpolator();
    }
    
    public int getTypeMask() {
      return this.mWrapped.getTypeMask();
    }
    
    public void setFraction(float param1Float) {
      this.mWrapped.setFraction(param1Float);
    }
    
    private static class ProxyCallback extends WindowInsetsAnimation.Callback {
      private final HashMap<WindowInsetsAnimation, WindowInsetsAnimationCompat> mAnimations = new HashMap<>();
      
      private final WindowInsetsAnimationCompat.Callback mCompat;
      
      private List<WindowInsetsAnimationCompat> mRORunningAnimations;
      
      private ArrayList<WindowInsetsAnimationCompat> mTmpRunningAnimations;
      
      ProxyCallback(WindowInsetsAnimationCompat.Callback param2Callback) {
        super(param2Callback.getDispatchMode());
        this.mCompat = param2Callback;
      }
      
      private WindowInsetsAnimationCompat getWindowInsetsAnimationCompat(WindowInsetsAnimation param2WindowInsetsAnimation) {
        WindowInsetsAnimationCompat windowInsetsAnimationCompat2 = this.mAnimations.get(param2WindowInsetsAnimation);
        WindowInsetsAnimationCompat windowInsetsAnimationCompat1 = windowInsetsAnimationCompat2;
        if (windowInsetsAnimationCompat2 == null) {
          windowInsetsAnimationCompat1 = WindowInsetsAnimationCompat.toWindowInsetsAnimationCompat(param2WindowInsetsAnimation);
          this.mAnimations.put(param2WindowInsetsAnimation, windowInsetsAnimationCompat1);
        } 
        return windowInsetsAnimationCompat1;
      }
      
      public void onEnd(WindowInsetsAnimation param2WindowInsetsAnimation) {
        this.mCompat.onEnd(getWindowInsetsAnimationCompat(param2WindowInsetsAnimation));
        this.mAnimations.remove(param2WindowInsetsAnimation);
      }
      
      public void onPrepare(WindowInsetsAnimation param2WindowInsetsAnimation) {
        this.mCompat.onPrepare(getWindowInsetsAnimationCompat(param2WindowInsetsAnimation));
      }
      
      public WindowInsets onProgress(WindowInsets param2WindowInsets, List<WindowInsetsAnimation> param2List) {
        ArrayList<WindowInsetsAnimationCompat> arrayList = this.mTmpRunningAnimations;
        if (arrayList == null) {
          arrayList = new ArrayList<>(param2List.size());
          this.mTmpRunningAnimations = arrayList;
          this.mRORunningAnimations = Collections.unmodifiableList(arrayList);
        } else {
          arrayList.clear();
        } 
        for (int i = param2List.size() - 1; i >= 0; i--) {
          WindowInsetsAnimation windowInsetsAnimation = param2List.get(i);
          WindowInsetsAnimationCompat windowInsetsAnimationCompat = getWindowInsetsAnimationCompat(windowInsetsAnimation);
          windowInsetsAnimationCompat.setFraction(windowInsetsAnimation.getFraction());
          this.mTmpRunningAnimations.add(windowInsetsAnimationCompat);
        } 
        return this.mCompat.onProgress(WindowInsetsCompat.toWindowInsetsCompat(param2WindowInsets), this.mRORunningAnimations).toWindowInsets();
      }
      
      public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation param2WindowInsetsAnimation, WindowInsetsAnimation.Bounds param2Bounds) {
        return this.mCompat.onStart(getWindowInsetsAnimationCompat(param2WindowInsetsAnimation), WindowInsetsAnimationCompat.BoundsCompat.toBoundsCompat(param2Bounds)).toBounds();
      }
    }
  }
  
  private static class ProxyCallback extends WindowInsetsAnimation.Callback {
    private final HashMap<WindowInsetsAnimation, WindowInsetsAnimationCompat> mAnimations = new HashMap<>();
    
    private final WindowInsetsAnimationCompat.Callback mCompat;
    
    private List<WindowInsetsAnimationCompat> mRORunningAnimations;
    
    private ArrayList<WindowInsetsAnimationCompat> mTmpRunningAnimations;
    
    ProxyCallback(WindowInsetsAnimationCompat.Callback param1Callback) {
      super(param1Callback.getDispatchMode());
      this.mCompat = param1Callback;
    }
    
    private WindowInsetsAnimationCompat getWindowInsetsAnimationCompat(WindowInsetsAnimation param1WindowInsetsAnimation) {
      WindowInsetsAnimationCompat windowInsetsAnimationCompat2 = this.mAnimations.get(param1WindowInsetsAnimation);
      WindowInsetsAnimationCompat windowInsetsAnimationCompat1 = windowInsetsAnimationCompat2;
      if (windowInsetsAnimationCompat2 == null) {
        windowInsetsAnimationCompat1 = WindowInsetsAnimationCompat.toWindowInsetsAnimationCompat(param1WindowInsetsAnimation);
        this.mAnimations.put(param1WindowInsetsAnimation, windowInsetsAnimationCompat1);
      } 
      return windowInsetsAnimationCompat1;
    }
    
    public void onEnd(WindowInsetsAnimation param1WindowInsetsAnimation) {
      this.mCompat.onEnd(getWindowInsetsAnimationCompat(param1WindowInsetsAnimation));
      this.mAnimations.remove(param1WindowInsetsAnimation);
    }
    
    public void onPrepare(WindowInsetsAnimation param1WindowInsetsAnimation) {
      this.mCompat.onPrepare(getWindowInsetsAnimationCompat(param1WindowInsetsAnimation));
    }
    
    public WindowInsets onProgress(WindowInsets param1WindowInsets, List<WindowInsetsAnimation> param1List) {
      ArrayList<WindowInsetsAnimationCompat> arrayList = this.mTmpRunningAnimations;
      if (arrayList == null) {
        arrayList = new ArrayList<>(param1List.size());
        this.mTmpRunningAnimations = arrayList;
        this.mRORunningAnimations = Collections.unmodifiableList(arrayList);
      } else {
        arrayList.clear();
      } 
      for (int i = param1List.size() - 1; i >= 0; i--) {
        WindowInsetsAnimation windowInsetsAnimation = param1List.get(i);
        WindowInsetsAnimationCompat windowInsetsAnimationCompat = getWindowInsetsAnimationCompat(windowInsetsAnimation);
        windowInsetsAnimationCompat.setFraction(windowInsetsAnimation.getFraction());
        this.mTmpRunningAnimations.add(windowInsetsAnimationCompat);
      } 
      return this.mCompat.onProgress(WindowInsetsCompat.toWindowInsetsCompat(param1WindowInsets), this.mRORunningAnimations).toWindowInsets();
    }
    
    public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation param1WindowInsetsAnimation, WindowInsetsAnimation.Bounds param1Bounds) {
      return this.mCompat.onStart(getWindowInsetsAnimationCompat(param1WindowInsetsAnimation), WindowInsetsAnimationCompat.BoundsCompat.toBoundsCompat(param1Bounds)).toBounds();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\WindowInsetsAnimationCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */