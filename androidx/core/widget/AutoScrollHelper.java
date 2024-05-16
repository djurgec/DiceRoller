package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

public abstract class AutoScrollHelper implements View.OnTouchListener {
  private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
  
  private static final int DEFAULT_EDGE_TYPE = 1;
  
  private static final float DEFAULT_MAXIMUM_EDGE = 3.4028235E38F;
  
  private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
  
  private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
  
  private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
  
  private static final int DEFAULT_RAMP_UP_DURATION = 500;
  
  private static final float DEFAULT_RELATIVE_EDGE = 0.2F;
  
  private static final float DEFAULT_RELATIVE_VELOCITY = 1.0F;
  
  public static final int EDGE_TYPE_INSIDE = 0;
  
  public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
  
  public static final int EDGE_TYPE_OUTSIDE = 2;
  
  private static final int HORIZONTAL = 0;
  
  public static final float NO_MAX = 3.4028235E38F;
  
  public static final float NO_MIN = 0.0F;
  
  public static final float RELATIVE_UNSPECIFIED = 0.0F;
  
  private static final int VERTICAL = 1;
  
  private int mActivationDelay;
  
  private boolean mAlreadyDelayed;
  
  boolean mAnimating;
  
  private final Interpolator mEdgeInterpolator = (Interpolator)new AccelerateInterpolator();
  
  private int mEdgeType;
  
  private boolean mEnabled;
  
  private boolean mExclusive;
  
  private float[] mMaximumEdges = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
  
  private float[] mMaximumVelocity = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
  
  private float[] mMinimumVelocity = new float[] { 0.0F, 0.0F };
  
  boolean mNeedsCancel;
  
  boolean mNeedsReset;
  
  private float[] mRelativeEdges = new float[] { 0.0F, 0.0F };
  
  private float[] mRelativeVelocity = new float[] { 0.0F, 0.0F };
  
  private Runnable mRunnable;
  
  final ClampedScroller mScroller = new ClampedScroller();
  
  final View mTarget;
  
  public AutoScrollHelper(View paramView) {
    this.mTarget = paramView;
    DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    int j = (int)(displayMetrics.density * 1575.0F + 0.5F);
    int i = (int)(displayMetrics.density * 315.0F + 0.5F);
    setMaximumVelocity(j, j);
    setMinimumVelocity(i, i);
    setEdgeType(1);
    setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
    setRelativeEdges(0.2F, 0.2F);
    setRelativeVelocity(1.0F, 1.0F);
    setActivationDelay(DEFAULT_ACTIVATION_DELAY);
    setRampUpDuration(500);
    setRampDownDuration(500);
  }
  
  private float computeTargetVelocity(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
    paramFloat1 = getEdgeValue(this.mRelativeEdges[paramInt], paramFloat2, this.mMaximumEdges[paramInt], paramFloat1);
    if (paramFloat1 == 0.0F)
      return 0.0F; 
    float f2 = this.mRelativeVelocity[paramInt];
    paramFloat2 = this.mMinimumVelocity[paramInt];
    float f1 = this.mMaximumVelocity[paramInt];
    paramFloat3 = f2 * paramFloat3;
    return (paramFloat1 > 0.0F) ? constrain(paramFloat1 * paramFloat3, paramFloat2, f1) : -constrain(-paramFloat1 * paramFloat3, paramFloat2, f1);
  }
  
  static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat1 > paramFloat3) ? paramFloat3 : ((paramFloat1 < paramFloat2) ? paramFloat2 : paramFloat1);
  }
  
  static int constrain(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt1 > paramInt3) ? paramInt3 : ((paramInt1 < paramInt2) ? paramInt2 : paramInt1);
  }
  
  private float constrainEdgeValue(float paramFloat1, float paramFloat2) {
    if (paramFloat2 == 0.0F)
      return 0.0F; 
    int i = this.mEdgeType;
    switch (i) {
      default:
        return 0.0F;
      case 2:
        if (paramFloat1 < 0.0F)
          return paramFloat1 / -paramFloat2; 
      case 0:
      case 1:
        break;
    } 
    if (paramFloat1 < paramFloat2) {
      if (paramFloat1 >= 0.0F)
        return 1.0F - paramFloat1 / paramFloat2; 
      if (this.mAnimating && i == 1)
        return 1.0F; 
    } 
  }
  
  private float getEdgeValue(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramFloat3 = constrain(paramFloat1 * paramFloat2, 0.0F, paramFloat3);
    paramFloat1 = constrainEdgeValue(paramFloat4, paramFloat3);
    paramFloat1 = constrainEdgeValue(paramFloat2 - paramFloat4, paramFloat3) - paramFloat1;
    if (paramFloat1 < 0.0F) {
      paramFloat1 = -this.mEdgeInterpolator.getInterpolation(-paramFloat1);
    } else {
      if (paramFloat1 > 0.0F) {
        paramFloat1 = this.mEdgeInterpolator.getInterpolation(paramFloat1);
        return constrain(paramFloat1, -1.0F, 1.0F);
      } 
      return 0.0F;
    } 
    return constrain(paramFloat1, -1.0F, 1.0F);
  }
  
  private void requestStop() {
    if (this.mNeedsReset) {
      this.mAnimating = false;
    } else {
      this.mScroller.requestStop();
    } 
  }
  
  private void startAnimating() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRunnable : Ljava/lang/Runnable;
    //   4: ifnonnull -> 19
    //   7: aload_0
    //   8: new androidx/core/widget/AutoScrollHelper$ScrollAnimationRunnable
    //   11: dup
    //   12: aload_0
    //   13: invokespecial <init> : (Landroidx/core/widget/AutoScrollHelper;)V
    //   16: putfield mRunnable : Ljava/lang/Runnable;
    //   19: aload_0
    //   20: iconst_1
    //   21: putfield mAnimating : Z
    //   24: aload_0
    //   25: iconst_1
    //   26: putfield mNeedsReset : Z
    //   29: aload_0
    //   30: getfield mAlreadyDelayed : Z
    //   33: ifne -> 61
    //   36: aload_0
    //   37: getfield mActivationDelay : I
    //   40: istore_1
    //   41: iload_1
    //   42: ifle -> 61
    //   45: aload_0
    //   46: getfield mTarget : Landroid/view/View;
    //   49: aload_0
    //   50: getfield mRunnable : Ljava/lang/Runnable;
    //   53: iload_1
    //   54: i2l
    //   55: invokestatic postOnAnimationDelayed : (Landroid/view/View;Ljava/lang/Runnable;J)V
    //   58: goto -> 70
    //   61: aload_0
    //   62: getfield mRunnable : Ljava/lang/Runnable;
    //   65: invokeinterface run : ()V
    //   70: aload_0
    //   71: iconst_1
    //   72: putfield mAlreadyDelayed : Z
    //   75: return
  }
  
  public abstract boolean canTargetScrollHorizontally(int paramInt);
  
  public abstract boolean canTargetScrollVertically(int paramInt);
  
  void cancelTargetTouch() {
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
    this.mTarget.onTouchEvent(motionEvent);
    motionEvent.recycle();
  }
  
  public boolean isEnabled() {
    return this.mEnabled;
  }
  
  public boolean isExclusive() {
    return this.mExclusive;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    boolean bool = this.mEnabled;
    boolean bool1 = false;
    if (!bool)
      return false; 
    switch (paramMotionEvent.getActionMasked()) {
      case 1:
      case 3:
        requestStop();
        break;
      case 0:
        this.mNeedsCancel = true;
        this.mAlreadyDelayed = false;
      case 2:
        f2 = computeTargetVelocity(0, paramMotionEvent.getX(), paramView.getWidth(), this.mTarget.getWidth());
        f1 = computeTargetVelocity(1, paramMotionEvent.getY(), paramView.getHeight(), this.mTarget.getHeight());
        this.mScroller.setTargetVelocity(f2, f1);
        if (!this.mAnimating && shouldAnimate())
          startAnimating(); 
        break;
    } 
    bool = bool1;
    if (this.mExclusive) {
      bool = bool1;
      if (this.mAnimating)
        bool = true; 
    } 
    return bool;
  }
  
  public abstract void scrollTargetBy(int paramInt1, int paramInt2);
  
  public AutoScrollHelper setActivationDelay(int paramInt) {
    this.mActivationDelay = paramInt;
    return this;
  }
  
  public AutoScrollHelper setEdgeType(int paramInt) {
    this.mEdgeType = paramInt;
    return this;
  }
  
  public AutoScrollHelper setEnabled(boolean paramBoolean) {
    if (this.mEnabled && !paramBoolean)
      requestStop(); 
    this.mEnabled = paramBoolean;
    return this;
  }
  
  public AutoScrollHelper setExclusive(boolean paramBoolean) {
    this.mExclusive = paramBoolean;
    return this;
  }
  
  public AutoScrollHelper setMaximumEdges(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = this.mMaximumEdges;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    return this;
  }
  
  public AutoScrollHelper setMaximumVelocity(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = this.mMaximumVelocity;
    arrayOfFloat[0] = paramFloat1 / 1000.0F;
    arrayOfFloat[1] = paramFloat2 / 1000.0F;
    return this;
  }
  
  public AutoScrollHelper setMinimumVelocity(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = this.mMinimumVelocity;
    arrayOfFloat[0] = paramFloat1 / 1000.0F;
    arrayOfFloat[1] = paramFloat2 / 1000.0F;
    return this;
  }
  
  public AutoScrollHelper setRampDownDuration(int paramInt) {
    this.mScroller.setRampDownDuration(paramInt);
    return this;
  }
  
  public AutoScrollHelper setRampUpDuration(int paramInt) {
    this.mScroller.setRampUpDuration(paramInt);
    return this;
  }
  
  public AutoScrollHelper setRelativeEdges(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = this.mRelativeEdges;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    return this;
  }
  
  public AutoScrollHelper setRelativeVelocity(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = this.mRelativeVelocity;
    arrayOfFloat[0] = paramFloat1 / 1000.0F;
    arrayOfFloat[1] = paramFloat2 / 1000.0F;
    return this;
  }
  
  boolean shouldAnimate() {
    boolean bool;
    ClampedScroller clampedScroller = this.mScroller;
    int j = clampedScroller.getVerticalDirection();
    int i = clampedScroller.getHorizontalDirection();
    if ((j != 0 && canTargetScrollVertically(j)) || (i != 0 && canTargetScrollHorizontally(i))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static class ClampedScroller {
    private long mDeltaTime = 0L;
    
    private int mDeltaX = 0;
    
    private int mDeltaY = 0;
    
    private int mEffectiveRampDown;
    
    private int mRampDownDuration;
    
    private int mRampUpDuration;
    
    private long mStartTime = Long.MIN_VALUE;
    
    private long mStopTime = -1L;
    
    private float mStopValue;
    
    private float mTargetVelocityX;
    
    private float mTargetVelocityY;
    
    private float getValueAt(long param1Long) {
      long l1 = this.mStartTime;
      if (param1Long < l1)
        return 0.0F; 
      long l2 = this.mStopTime;
      if (l2 < 0L || param1Long < l2)
        return AutoScrollHelper.constrain((float)(param1Long - l1) / this.mRampUpDuration, 0.0F, 1.0F) * 0.5F; 
      float f = this.mStopValue;
      return 1.0F - f + f * AutoScrollHelper.constrain((float)(param1Long - l2) / this.mEffectiveRampDown, 0.0F, 1.0F);
    }
    
    private float interpolateValue(float param1Float) {
      return -4.0F * param1Float * param1Float + 4.0F * param1Float;
    }
    
    public void computeScrollDelta() {
      if (this.mDeltaTime != 0L) {
        long l1 = AnimationUtils.currentAnimationTimeMillis();
        float f = interpolateValue(getValueAt(l1));
        long l2 = l1 - this.mDeltaTime;
        this.mDeltaTime = l1;
        this.mDeltaX = (int)((float)l2 * f * this.mTargetVelocityX);
        this.mDeltaY = (int)((float)l2 * f * this.mTargetVelocityY);
        return;
      } 
      throw new RuntimeException("Cannot compute scroll delta before calling start()");
    }
    
    public int getDeltaX() {
      return this.mDeltaX;
    }
    
    public int getDeltaY() {
      return this.mDeltaY;
    }
    
    public int getHorizontalDirection() {
      float f = this.mTargetVelocityX;
      return (int)(f / Math.abs(f));
    }
    
    public int getVerticalDirection() {
      float f = this.mTargetVelocityY;
      return (int)(f / Math.abs(f));
    }
    
    public boolean isFinished() {
      boolean bool;
      if (this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + this.mEffectiveRampDown) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void requestStop() {
      long l = AnimationUtils.currentAnimationTimeMillis();
      this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(l - this.mStartTime), 0, this.mRampDownDuration);
      this.mStopValue = getValueAt(l);
      this.mStopTime = l;
    }
    
    public void setRampDownDuration(int param1Int) {
      this.mRampDownDuration = param1Int;
    }
    
    public void setRampUpDuration(int param1Int) {
      this.mRampUpDuration = param1Int;
    }
    
    public void setTargetVelocity(float param1Float1, float param1Float2) {
      this.mTargetVelocityX = param1Float1;
      this.mTargetVelocityY = param1Float2;
    }
    
    public void start() {
      long l = AnimationUtils.currentAnimationTimeMillis();
      this.mStartTime = l;
      this.mStopTime = -1L;
      this.mDeltaTime = l;
      this.mStopValue = 0.5F;
      this.mDeltaX = 0;
      this.mDeltaY = 0;
    }
  }
  
  private class ScrollAnimationRunnable implements Runnable {
    final AutoScrollHelper this$0;
    
    public void run() {
      if (!AutoScrollHelper.this.mAnimating)
        return; 
      if (AutoScrollHelper.this.mNeedsReset) {
        AutoScrollHelper.this.mNeedsReset = false;
        AutoScrollHelper.this.mScroller.start();
      } 
      AutoScrollHelper.ClampedScroller clampedScroller = AutoScrollHelper.this.mScroller;
      if (clampedScroller.isFinished() || !AutoScrollHelper.this.shouldAnimate()) {
        AutoScrollHelper.this.mAnimating = false;
        return;
      } 
      if (AutoScrollHelper.this.mNeedsCancel) {
        AutoScrollHelper.this.mNeedsCancel = false;
        AutoScrollHelper.this.cancelTargetTouch();
      } 
      clampedScroller.computeScrollDelta();
      int i = clampedScroller.getDeltaX();
      int j = clampedScroller.getDeltaY();
      AutoScrollHelper.this.scrollTargetBy(i, j);
      ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\AutoScrollHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */