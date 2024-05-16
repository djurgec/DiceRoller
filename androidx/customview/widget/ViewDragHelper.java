package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private OverScroller mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      final ViewDragHelper this$0;
      
      public void run() {
        ViewDragHelper.this.setDragState(0);
      }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(Context paramContext, ViewGroup paramViewGroup, Callback paramCallback) {
    if (paramViewGroup != null) {
      if (paramCallback != null) {
        this.mParentView = paramViewGroup;
        this.mCallback = paramCallback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
        this.mEdgeSize = (int)(20.0F * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(paramContext, sInterpolator);
        return;
      } 
      throw new IllegalArgumentException("Callback may not be null");
    } 
    throw new IllegalArgumentException("Parent view may not be null");
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    int i = this.mInitialEdgesTouched[paramInt1];
    boolean bool = false;
    if ((i & paramInt2) == paramInt2 && (this.mTrackingEdges & paramInt2) != 0 && (this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2 && (this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2) {
      i = this.mTouchSlop;
      if (paramFloat1 > i || paramFloat2 > i) {
        if (paramFloat1 < 0.5F * paramFloat2 && this.mCallback.onEdgeLock(paramInt2)) {
          int[] arrayOfInt = this.mEdgeDragsLocked;
          arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
          return false;
        } 
        boolean bool1 = bool;
        if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0) {
          bool1 = bool;
          if (paramFloat1 > this.mTouchSlop)
            bool1 = true; 
        } 
        return bool1;
      } 
    } 
    return false;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    int i;
    boolean bool1;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (i && bool1) {
      i = this.mTouchSlop;
      bool2 = bool4;
      if (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 > (i * i))
        bool2 = true; 
      return bool2;
    } 
    if (i != 0) {
      if (Math.abs(paramFloat1) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    if (bool1) {
      bool2 = bool3;
      if (Math.abs(paramFloat2) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    return false;
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      return 0.0F; 
    if (f > paramFloat3) {
      if (paramFloat1 <= 0.0F)
        paramFloat3 = -paramFloat3; 
      return paramFloat3;
    } 
    return paramFloat1;
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      return 0; 
    if (i > paramInt3) {
      if (paramInt1 <= 0)
        paramInt3 = -paramInt3; 
      return paramInt3;
    } 
    return paramInt1;
  }
  
  private void clearMotionHistory() {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null)
      return; 
    Arrays.fill(arrayOfFloat, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX == null || !isPointerDown(paramInt))
      return; 
    this.mInitialMotionX[paramInt] = 0.0F;
    this.mInitialMotionY[paramInt] = 0.0F;
    this.mLastMotionX[paramInt] = 0.0F;
    this.mLastMotionY[paramInt] = 0.0F;
    this.mInitialEdgesTouched[paramInt] = 0;
    this.mEdgeDragsInProgress[paramInt] = 0;
    this.mEdgeDragsLocked[paramInt] = 0;
    this.mPointersDown &= 1 << paramInt ^ 0xFFFFFFFF;
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f3 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f1 = j;
    float f2 = j;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(Math.abs((f1 + f2 * f3) / paramInt2) * 1000.0F) * 4;
    } else {
      paramInt1 = (int)((1.0F + Math.abs(paramInt1) / paramInt3) * 256.0F);
    } 
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f2;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int k = Math.abs(paramInt1);
    int i = Math.abs(paramInt2);
    int n = Math.abs(paramInt3);
    int j = Math.abs(paramInt4);
    int i1 = n + j;
    int m = k + i;
    if (paramInt3 != 0) {
      f1 = n;
      f2 = i1;
    } else {
      f1 = k;
      f2 = m;
    } 
    float f3 = f1 / f2;
    if (paramInt4 != 0) {
      f2 = j;
      f1 = i1;
    } else {
      f2 = i;
      f1 = m;
    } 
    float f1 = f2 / f1;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f3 + paramInt2 * f1);
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, float paramFloat, Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, Callback paramCallback) {
    return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback);
  }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = paramInt1;
    int i = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0) {
      paramInt1 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      ViewCompat.offsetLeftAndRight(this.mCapturedView, paramInt1 - k);
    } else {
      paramInt1 = j;
    } 
    if (paramInt4 != 0) {
      i = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      ViewCompat.offsetTopAndBottom(this.mCapturedView, i - m);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, paramInt1, i, paramInt1 - k, i - m); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null || arrayOfFloat.length <= paramInt) {
      float[] arrayOfFloat3 = new float[paramInt + 1];
      float[] arrayOfFloat2 = new float[paramInt + 1];
      float[] arrayOfFloat1 = new float[paramInt + 1];
      float[] arrayOfFloat4 = new float[paramInt + 1];
      int[] arrayOfInt2 = new int[paramInt + 1];
      int[] arrayOfInt1 = new int[paramInt + 1];
      int[] arrayOfInt3 = new int[paramInt + 1];
      if (arrayOfFloat != null) {
        System.arraycopy(arrayOfFloat, 0, arrayOfFloat3, 0, arrayOfFloat.length);
        arrayOfFloat = this.mInitialMotionY;
        System.arraycopy(arrayOfFloat, 0, arrayOfFloat2, 0, arrayOfFloat.length);
        arrayOfFloat = this.mLastMotionX;
        System.arraycopy(arrayOfFloat, 0, arrayOfFloat1, 0, arrayOfFloat.length);
        arrayOfFloat = this.mLastMotionY;
        System.arraycopy(arrayOfFloat, 0, arrayOfFloat4, 0, arrayOfFloat.length);
        int[] arrayOfInt = this.mInitialEdgesTouched;
        System.arraycopy(arrayOfInt, 0, arrayOfInt2, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsInProgress;
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsLocked;
        System.arraycopy(arrayOfInt, 0, arrayOfInt3, 0, arrayOfInt.length);
      } 
      this.mInitialMotionX = arrayOfFloat3;
      this.mInitialMotionY = arrayOfFloat2;
      this.mLastMotionX = arrayOfFloat1;
      this.mLastMotionY = arrayOfFloat4;
      this.mInitialEdgesTouched = arrayOfInt2;
      this.mEdgeDragsInProgress = arrayOfInt1;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = this.mCapturedView.getLeft();
    int i = this.mCapturedView.getTop();
    paramInt1 -= j;
    paramInt2 -= i;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return false;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(j, i, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    int j = 0;
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize)
      j = false | true; 
    int i = j;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      i = j | 0x4; 
    j = i;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      j = i | 0x2; 
    paramInt1 = j;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = j | 0x8; 
    return paramInt1;
  }
  
  private boolean isValidPointerForActionMove(int paramInt) {
    if (!isPointerDown(paramInt)) {
      Log.e("ViewDragHelper", "Ignoring pointerId=" + paramInt + " because ACTION_DOWN was not received " + "for this pointer before ACTION_MOVE. It likely happened because " + " ViewDragHelper did not receive all the events in the event stream.");
      return false;
    } 
    return true;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    int j = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      j = false | true; 
    int i = j;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      i = j | 0x4; 
    j = i;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      j = i | 0x2; 
    i = j;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      i = j | 0x8; 
    if (i != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | i;
      this.mCallback.onEdgeDragStarted(i, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getPointerCount();
    for (byte b = 0; b < i; b++) {
      int j = paramMotionEvent.getPointerId(b);
      if (isValidPointerForActionMove(j)) {
        float f2 = paramMotionEvent.getX(b);
        float f1 = paramMotionEvent.getY(b);
        this.mLastMotionX[j] = f2;
        this.mLastMotionY[j] = f1;
      } 
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int j = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int i = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, i, k - j, i - m);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool1 = paramView instanceof ViewGroup;
    boolean bool = true;
    if (bool1) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        if (paramInt3 + j >= view.getLeft() && paramInt3 + j < view.getRight() && paramInt4 + k >= view.getTop() && paramInt4 + k < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, paramInt3 + j - view.getLeft(), paramInt4 + k - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean)
      if (!paramView.canScrollHorizontally(-paramInt1)) {
        if (paramView.canScrollVertically(-paramInt2))
          return bool; 
      } else {
        return bool;
      }  
    return false;
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(View paramView, int paramInt) {
    if (paramView.getParent() == this.mParentView) {
      this.mCapturedView = paramView;
      this.mActivePointerId = paramInt;
      this.mCallback.onViewCaptured(paramView, paramInt);
      setDragState(1);
      return;
    } 
    throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
  }
  
  public boolean checkTouchSlop(int paramInt) {
    int i = this.mInitialMotionX.length;
    for (byte b = 0; b < i; b++) {
      if (checkTouchSlop(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool = isPointerDown(paramInt2);
    boolean bool4 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (!bool)
      return false; 
    if ((paramInt1 & 0x1) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    if (bool1 && paramInt1 != 0) {
      paramInt1 = this.mTouchSlop;
      bool2 = bool3;
      if (f1 * f1 + f2 * f2 > (paramInt1 * paramInt1))
        bool2 = true; 
      return bool2;
    } 
    if (bool1) {
      bool2 = bool4;
      if (Math.abs(f1) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    if (paramInt1 != 0) {
      if (Math.abs(f2) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    return false;
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    int i = this.mDragState;
    boolean bool = false;
    if (i == 2) {
      boolean bool2 = this.mScroller.computeScrollOffset();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      i = k - this.mCapturedView.getLeft();
      int j = m - this.mCapturedView.getTop();
      if (i != 0)
        ViewCompat.offsetLeftAndRight(this.mCapturedView, i); 
      if (j != 0)
        ViewCompat.offsetTopAndBottom(this.mCapturedView, j); 
      if (i != 0 || j != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, i, j); 
      boolean bool1 = bool2;
      if (bool2) {
        bool1 = bool2;
        if (k == this.mScroller.getFinalX()) {
          bool1 = bool2;
          if (m == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool1 = false;
          } 
        } 
      } 
      if (!bool1)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    paramBoolean = bool;
    if (this.mDragState == 2)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    for (int i = this.mParentView.getChildCount() - 1; i >= 0; i--) {
      View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
      if (paramInt1 >= view.getLeft() && paramInt1 < view.getRight() && paramInt2 >= view.getTop() && paramInt2 < view.getBottom())
        return view; 
    } 
    return null;
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mReleaseInProgress) {
      this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
      setDragState(2);
      return;
    } 
    throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
  }
  
  public int getActivePointerId() {
    return this.mActivePointerId;
  }
  
  public View getCapturedView() {
    return this.mCapturedView;
  }
  
  public int getEdgeSize() {
    return this.mEdgeSize;
  }
  
  public float getMinVelocity() {
    return this.mMinVelocity;
  }
  
  public int getTouchSlop() {
    return this.mTouchSlop;
  }
  
  public int getViewDragState() {
    return this.mDragState;
  }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) {
    return isViewUnder(this.mCapturedView, paramInt1, paramInt2);
  }
  
  public boolean isEdgeTouched(int paramInt) {
    int i = this.mInitialEdgesTouched.length;
    for (byte b = 0; b < i; b++) {
      if (isEdgeTouched(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) {
    boolean bool;
    if (isPointerDown(paramInt2) && (this.mInitialEdgesTouched[paramInt2] & paramInt1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isPointerDown(int paramInt) {
    int i = this.mPointersDown;
    boolean bool = true;
    if ((i & 1 << paramInt) == 0)
      bool = false; 
    return bool;
  }
  
  public boolean isViewUnder(View paramView, int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramView == null)
      return false; 
    if (paramInt1 >= paramView.getLeft() && paramInt1 < paramView.getRight() && paramInt2 >= paramView.getTop() && paramInt2 < paramView.getBottom())
      bool = true; 
    return bool;
  }
  
  public void processTouchEvent(MotionEvent paramMotionEvent) {
    int m;
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i) {
      default:
        return;
      case 6:
        m = paramMotionEvent.getPointerId(j);
        if (this.mDragState == 1 && m == this.mActivePointerId) {
          byte b = -1;
          int n = paramMotionEvent.getPointerCount();
          i = 0;
          while (true) {
            j = b;
            if (i < n) {
              j = paramMotionEvent.getPointerId(i);
              if (j != this.mActivePointerId) {
                float f3 = paramMotionEvent.getX(i);
                float f4 = paramMotionEvent.getY(i);
                View view1 = findTopChildUnder((int)f3, (int)f4);
                View view2 = this.mCapturedView;
                if (view1 == view2 && tryCaptureViewForDrag(view2, j)) {
                  j = this.mActivePointerId;
                  break;
                } 
              } 
              i++;
              continue;
            } 
            break;
          } 
          if (j == -1)
            releaseViewForPointerUp(); 
        } 
        clearMotionHistory(m);
      case 5:
        i = paramMotionEvent.getPointerId(j);
        f2 = paramMotionEvent.getX(j);
        f1 = paramMotionEvent.getY(j);
        saveInitialMotion(f2, f1, i);
        if (this.mDragState == 0) {
          tryCaptureViewForDrag(findTopChildUnder((int)f2, (int)f1), i);
          int n = this.mInitialEdgesTouched[i];
          j = this.mTrackingEdges;
          if ((n & j) != 0)
            this.mCallback.onEdgeTouched(j & n, i); 
        } else if (isCapturedViewUnder((int)f2, (int)f1)) {
          tryCaptureViewForDrag(this.mCapturedView, i);
        } 
      case 3:
        if (this.mDragState == 1)
          dispatchViewReleased(0.0F, 0.0F); 
        cancel();
      case 2:
        if (this.mDragState == 1) {
          if (isValidPointerForActionMove(this.mActivePointerId)) {
            i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
            f2 = paramMotionEvent.getX(i);
            f1 = paramMotionEvent.getY(i);
            float[] arrayOfFloat = this.mLastMotionX;
            j = this.mActivePointerId;
            i = (int)(f2 - arrayOfFloat[j]);
            j = (int)(f1 - this.mLastMotionY[j]);
            dragTo(this.mCapturedView.getLeft() + i, this.mCapturedView.getTop() + j, i, j);
            saveLastMotion(paramMotionEvent);
          } 
        } else {
          j = paramMotionEvent.getPointerCount();
          for (i = 0; i < j; i++) {
            int n = paramMotionEvent.getPointerId(i);
            if (isValidPointerForActionMove(n)) {
              f1 = paramMotionEvent.getX(i);
              f2 = paramMotionEvent.getY(i);
              float f3 = f1 - this.mInitialMotionX[n];
              float f4 = f2 - this.mInitialMotionY[n];
              reportNewEdgeDrags(f3, f4, n);
              if (this.mDragState == 1)
                break; 
              View view1 = findTopChildUnder((int)f1, (int)f2);
              if (checkTouchSlop(view1, f3, f4) && tryCaptureViewForDrag(view1, n))
                break; 
            } 
          } 
          saveLastMotion(paramMotionEvent);
        } 
      case 1:
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
      case 0:
        break;
    } 
    float f2 = paramMotionEvent.getX();
    float f1 = paramMotionEvent.getY();
    i = paramMotionEvent.getPointerId(0);
    View view = findTopChildUnder((int)f2, (int)f1);
    saveInitialMotion(f2, f1, i);
    tryCaptureViewForDrag(view, i);
    j = this.mInitialEdgesTouched[i];
    int k = this.mTrackingEdges;
    if ((j & k) != 0)
      this.mCallback.onEdgeTouched(k & j, i); 
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) {
    this.mTrackingEdges = paramInt;
  }
  
  public void setMinVelocity(float paramFloat) {
    this.mMinVelocity = paramFloat;
  }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (this.mReleaseInProgress)
      return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId)); 
    throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
  }
  
  public boolean shouldInterceptTouchEvent(MotionEvent paramMotionEvent) {
    View view;
    float f1;
    float f2;
    int i;
    byte b;
    int k = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (k == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (k) {
      case 6:
        clearMotionHistory(paramMotionEvent.getPointerId(j));
        break;
      case 5:
        i = paramMotionEvent.getPointerId(j);
        f2 = paramMotionEvent.getX(j);
        f1 = paramMotionEvent.getY(j);
        saveInitialMotion(f2, f1, i);
        j = this.mDragState;
        if (j == 0) {
          k = this.mInitialEdgesTouched[i];
          j = this.mTrackingEdges;
          if ((k & j) != 0)
            this.mCallback.onEdgeTouched(j & k, i); 
          break;
        } 
        if (j == 2) {
          view = findTopChildUnder((int)f2, (int)f1);
          if (view == this.mCapturedView)
            tryCaptureViewForDrag(view, i); 
        } 
        break;
      case 2:
        if (this.mInitialMotionX == null || this.mInitialMotionY == null)
          break; 
        i = view.getPointerCount();
        for (b = 0; b < i; b++) {
          int m = view.getPointerId(b);
          if (isValidPointerForActionMove(m)) {
            boolean bool1;
            float f4 = view.getX(b);
            f2 = view.getY(b);
            float f3 = f4 - this.mInitialMotionX[m];
            f1 = f2 - this.mInitialMotionY[m];
            View view1 = findTopChildUnder((int)f4, (int)f2);
            if (view1 != null && checkTouchSlop(view1, f3, f1)) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (bool1) {
              int n = view1.getLeft();
              int i1 = (int)f3;
              int i2 = this.mCallback.clampViewPositionHorizontal(view1, i1 + n, (int)f3);
              i1 = view1.getTop();
              int i3 = (int)f1;
              int i4 = this.mCallback.clampViewPositionVertical(view1, i3 + i1, (int)f1);
              i3 = this.mCallback.getViewHorizontalDragRange(view1);
              int i5 = this.mCallback.getViewVerticalDragRange(view1);
              if ((i3 == 0 || (i3 > 0 && i2 == n)) && (i5 == 0 || (i5 > 0 && i4 == i1)))
                break; 
            } 
            reportNewEdgeDrags(f3, f1, m);
            if (this.mDragState == 1 || (bool1 && tryCaptureViewForDrag(view1, m)))
              break; 
          } 
        } 
        saveLastMotion((MotionEvent)view);
        break;
      case 1:
      case 3:
        cancel();
        break;
      case 0:
        f1 = view.getX();
        f2 = view.getY();
        j = view.getPointerId(0);
        saveInitialMotion(f1, f2, j);
        view = findTopChildUnder((int)f1, (int)f2);
        if (view == this.mCapturedView && this.mDragState == 2)
          tryCaptureViewForDrag(view, j); 
        k = this.mInitialEdgesTouched[j];
        i = this.mTrackingEdges;
        if ((k & i) != 0)
          this.mCallback.onEdgeTouched(i & k, j); 
        break;
    } 
    boolean bool = false;
    if (this.mDragState == 1)
      bool = true; 
    return bool;
  }
  
  public boolean smoothSlideViewTo(View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    if (paramView == this.mCapturedView && this.mActivePointerId == paramInt)
      return true; 
    if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
      this.mActivePointerId = paramInt;
      captureChildView(paramView, paramInt);
      return true;
    } 
    return false;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int getOrderedChildIndex(int param1Int) {
      return param1Int;
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return 0;
    }
    
    public int getViewVerticalDragRange(View param1View) {
      return 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(View param1View, int param1Int);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\customview\widget\ViewDragHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */