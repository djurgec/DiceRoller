package androidx.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.R;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import java.util.ArrayList;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3, ScrollingView {
  private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
  
  static final int ANIMATED_SCROLL_GAP = 250;
  
  private static final int DEFAULT_SMOOTH_SCROLL_DURATION = 250;
  
  private static final int INVALID_POINTER = -1;
  
  static final float MAX_SCROLL_FACTOR = 0.5F;
  
  private static final int[] SCROLLVIEW_STYLEABLE = new int[] { 16843130 };
  
  private static final String TAG = "NestedScrollView";
  
  private int mActivePointerId = -1;
  
  private final NestedScrollingChildHelper mChildHelper;
  
  private View mChildToScrollTo = null;
  
  public EdgeEffect mEdgeGlowBottom;
  
  public EdgeEffect mEdgeGlowTop;
  
  private boolean mFillViewport;
  
  private boolean mIsBeingDragged = false;
  
  private boolean mIsLaidOut = false;
  
  private boolean mIsLayoutDirty = true;
  
  private int mLastMotionY;
  
  private long mLastScroll;
  
  private int mLastScrollerY;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private int mNestedYOffset;
  
  private OnScrollChangeListener mOnScrollChangeListener;
  
  private final NestedScrollingParentHelper mParentHelper;
  
  private SavedState mSavedState;
  
  private final int[] mScrollConsumed = new int[2];
  
  private final int[] mScrollOffset = new int[2];
  
  private OverScroller mScroller;
  
  private boolean mSmoothScrollingEnabled = true;
  
  private final Rect mTempRect = new Rect();
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  private float mVerticalScrollFactor;
  
  public NestedScrollView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.nestedScrollViewStyle);
  }
  
  public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.mEdgeGlowTop = EdgeEffectCompat.create(paramContext, paramAttributeSet);
    this.mEdgeGlowBottom = EdgeEffectCompat.create(paramContext, paramAttributeSet);
    initScrollView();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, SCROLLVIEW_STYLEABLE, paramInt, 0);
    setFillViewport(typedArray.getBoolean(0, false));
    typedArray.recycle();
    this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
    this.mChildHelper = new NestedScrollingChildHelper((View)this);
    setNestedScrollingEnabled(true);
    ViewCompat.setAccessibilityDelegate((View)this, ACCESSIBILITY_DELEGATE);
  }
  
  private void abortAnimatedScroll() {
    this.mScroller.abortAnimation();
    stopNestedScroll(1);
  }
  
  private boolean canScroll() {
    int i = getChildCount();
    boolean bool = false;
    if (i > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      if (view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin > getHeight() - getPaddingTop() - getPaddingBottom())
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt2 >= paramInt3 || paramInt1 < 0) ? 0 : ((paramInt2 + paramInt1 > paramInt3) ? (paramInt3 - paramInt2) : paramInt1);
  }
  
  private void doScrollY(int paramInt) {
    if (paramInt != 0)
      if (this.mSmoothScrollingEnabled) {
        smoothScrollBy(0, paramInt);
      } else {
        scrollBy(0, paramInt);
      }  
  }
  
  private boolean edgeEffectFling(int paramInt) {
    boolean bool = true;
    if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) != 0.0F) {
      this.mEdgeGlowTop.onAbsorb(paramInt);
    } else if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) != 0.0F) {
      this.mEdgeGlowBottom.onAbsorb(-paramInt);
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    recycleVelocityTracker();
    stopNestedScroll(0);
    this.mEdgeGlowTop.onRelease();
    this.mEdgeGlowBottom.onRelease();
  }
  
  private View findFocusableViewInBounds(boolean paramBoolean, int paramInt1, int paramInt2) {
    ArrayList<View> arrayList = getFocusables(2);
    View view = null;
    boolean bool = false;
    int i = arrayList.size();
    byte b = 0;
    while (b < i) {
      View view2 = arrayList.get(b);
      int k = view2.getTop();
      int j = view2.getBottom();
      View view1 = view;
      boolean bool1 = bool;
      if (paramInt1 < j) {
        view1 = view;
        bool1 = bool;
        if (k < paramInt2) {
          boolean bool2;
          boolean bool3 = false;
          if (paramInt1 < k && j < paramInt2) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          if (view == null) {
            view1 = view2;
            bool1 = bool2;
          } else {
            if ((paramBoolean && k < view.getTop()) || (!paramBoolean && j > view.getBottom()))
              bool3 = true; 
            if (bool) {
              view1 = view;
              bool1 = bool;
              if (bool2) {
                view1 = view;
                bool1 = bool;
                if (bool3) {
                  view1 = view2;
                  bool1 = bool;
                } 
              } 
            } else if (bool2) {
              view1 = view2;
              bool1 = true;
            } else {
              view1 = view;
              bool1 = bool;
              if (bool3) {
                view1 = view2;
                bool1 = bool;
              } 
            } 
          } 
        } 
      } 
      b++;
      view = view1;
      bool = bool1;
    } 
    return view;
  }
  
  private float getVerticalScrollFactorCompat() {
    if (this.mVerticalScrollFactor == 0.0F) {
      TypedValue typedValue = new TypedValue();
      Context context = getContext();
      if (context.getTheme().resolveAttribute(16842829, typedValue, true)) {
        this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
      } else {
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
      } 
    } 
    return this.mVerticalScrollFactor;
  }
  
  private boolean inChild(int paramInt1, int paramInt2) {
    int i = getChildCount();
    boolean bool = false;
    if (i > 0) {
      i = getScrollY();
      View view = getChildAt(0);
      if (paramInt2 >= view.getTop() - i && paramInt2 < view.getBottom() - i && paramInt1 >= view.getLeft() && paramInt1 < view.getRight())
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private void initOrResetVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    } else {
      velocityTracker.clear();
    } 
  }
  
  private void initScrollView() {
    this.mScroller = new OverScroller(getContext());
    setFocusable(true);
    setDescendantFocusability(262144);
    setWillNotDraw(false);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
  }
  
  private void initVelocityTrackerIfNotExists() {
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
  }
  
  private boolean isOffScreen(View paramView) {
    return isWithinDeltaOfScreen(paramView, 0, getHeight()) ^ true;
  }
  
  private static boolean isViewDescendantOf(View paramView1, View paramView2) {
    boolean bool = true;
    if (paramView1 == paramView2)
      return true; 
    ViewParent viewParent = paramView1.getParent();
    if (!(viewParent instanceof ViewGroup) || !isViewDescendantOf((View)viewParent, paramView2))
      bool = false; 
    return bool;
  }
  
  private boolean isWithinDeltaOfScreen(View paramView, int paramInt1, int paramInt2) {
    boolean bool;
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    if (this.mTempRect.bottom + paramInt1 >= getScrollY() && this.mTempRect.top - paramInt1 <= getScrollY() + paramInt2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void onNestedScrollInternal(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    int i = getScrollY();
    scrollBy(0, paramInt1);
    i = getScrollY() - i;
    if (paramArrayOfint != null)
      paramArrayOfint[1] = paramArrayOfint[1] + i; 
    this.mChildHelper.dispatchNestedScroll(0, i, 0, paramInt1 - i, null, paramInt2, paramArrayOfint);
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionY = (int)paramMotionEvent.getY(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private void recycleVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private int releaseVerticalGlow(int paramInt, float paramFloat) {
    float f1 = 0.0F;
    float f3 = paramFloat / getWidth();
    float f2 = paramInt / getHeight();
    if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) != 0.0F) {
      f1 = -EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, -f2, f3);
      paramFloat = f1;
      if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) == 0.0F) {
        this.mEdgeGlowTop.onRelease();
        paramFloat = f1;
      } 
    } else {
      paramFloat = f1;
      if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) != 0.0F) {
        f1 = EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, f2, 1.0F - f3);
        paramFloat = f1;
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) == 0.0F) {
          this.mEdgeGlowBottom.onRelease();
          paramFloat = f1;
        } 
      } 
    } 
    paramInt = Math.round(getHeight() * paramFloat);
    if (paramInt != 0)
      invalidate(); 
    return paramInt;
  }
  
  private void runAnimatedScroll(boolean paramBoolean) {
    if (paramBoolean) {
      startNestedScroll(2, 1);
    } else {
      stopNestedScroll(1);
    } 
    this.mLastScrollerY = getScrollY();
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  private boolean scrollAndFocus(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1;
    NestedScrollView nestedScrollView;
    boolean bool2 = true;
    int j = getHeight();
    int i = getScrollY();
    j = i + j;
    if (paramInt1 == 33) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    View view2 = findFocusableViewInBounds(bool1, paramInt2, paramInt3);
    View view1 = view2;
    if (view2 == null)
      nestedScrollView = this; 
    if (paramInt2 >= i && paramInt3 <= j) {
      bool1 = false;
    } else {
      if (bool1) {
        paramInt2 -= i;
      } else {
        paramInt2 = paramInt3 - j;
      } 
      doScrollY(paramInt2);
      bool1 = bool2;
    } 
    if (nestedScrollView != findFocus())
      nestedScrollView.requestFocus(paramInt1); 
    return bool1;
  }
  
  private void scrollToChild(View paramView) {
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    int i = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
    if (i != 0)
      scrollBy(0, i); 
  }
  
  private boolean scrollToChildRect(Rect paramRect, boolean paramBoolean) {
    boolean bool;
    int i = computeScrollDeltaToGetChildRectOnScreen(paramRect);
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      if (paramBoolean) {
        scrollBy(0, i);
      } else {
        smoothScrollBy(0, i);
      }  
    return bool;
  }
  
  private void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    if (getChildCount() == 0)
      return; 
    if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      int i1 = view.getHeight();
      int j = layoutParams.topMargin;
      int i = layoutParams.bottomMargin;
      int k = getHeight();
      int m = getPaddingTop();
      int n = getPaddingBottom();
      paramInt1 = getScrollY();
      paramInt2 = Math.max(0, Math.min(paramInt1 + paramInt2, Math.max(0, i1 + j + i - k - m - n)));
      this.mScroller.startScroll(getScrollX(), paramInt1, 0, paramInt2 - paramInt1, paramInt3);
      runAnimatedScroll(paramBoolean);
    } else {
      if (!this.mScroller.isFinished())
        abortAnimatedScroll(); 
      scrollBy(paramInt1, paramInt2);
    } 
    this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
  }
  
  private boolean stopGlowAnimations(MotionEvent paramMotionEvent) {
    boolean bool = false;
    if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) != 0.0F) {
      EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, 0.0F, paramMotionEvent.getY() / getHeight());
      bool = true;
    } 
    if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) != 0.0F) {
      EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, 0.0F, 1.0F - paramMotionEvent.getY() / getHeight());
      bool = true;
    } 
    return bool;
  }
  
  public void addView(View paramView) {
    if (getChildCount() <= 0) {
      super.addView(paramView);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramInt);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public boolean arrowScroll(int paramInt) {
    View view2 = findFocus();
    View view1 = view2;
    if (view2 == this)
      view1 = null; 
    view2 = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view1, paramInt);
    int i = getMaxScrollAmount();
    if (view2 != null && isWithinDeltaOfScreen(view2, i, getHeight())) {
      view2.getDrawingRect(this.mTempRect);
      offsetDescendantRectToMyCoords(view2, this.mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
      view2.requestFocus(paramInt);
    } else {
      int j;
      int k = i;
      if (paramInt == 33 && getScrollY() < k) {
        j = getScrollY();
      } else {
        j = k;
        if (paramInt == 130) {
          j = k;
          if (getChildCount() > 0) {
            View view = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            j = Math.min(view.getBottom() + layoutParams.bottomMargin - getScrollY() + getHeight() - getPaddingBottom(), i);
          } 
        } 
      } 
      if (j == 0)
        return false; 
      if (paramInt == 130) {
        paramInt = j;
      } else {
        paramInt = -j;
      } 
      doScrollY(paramInt);
    } 
    if (view1 != null && view1.isFocused() && isOffScreen(view1)) {
      paramInt = getDescendantFocusability();
      setDescendantFocusability(131072);
      requestFocus();
      setDescendantFocusability(paramInt);
    } 
    return true;
  }
  
  public int computeHorizontalScrollExtent() {
    return super.computeHorizontalScrollExtent();
  }
  
  public int computeHorizontalScrollOffset() {
    return super.computeHorizontalScrollOffset();
  }
  
  public int computeHorizontalScrollRange() {
    return super.computeHorizontalScrollRange();
  }
  
  public void computeScroll() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mScroller : Landroid/widget/OverScroller;
    //   4: invokevirtual isFinished : ()Z
    //   7: ifeq -> 11
    //   10: return
    //   11: aload_0
    //   12: getfield mScroller : Landroid/widget/OverScroller;
    //   15: invokevirtual computeScrollOffset : ()Z
    //   18: pop
    //   19: aload_0
    //   20: getfield mScroller : Landroid/widget/OverScroller;
    //   23: invokevirtual getCurrY : ()I
    //   26: istore_2
    //   27: iload_2
    //   28: aload_0
    //   29: getfield mLastScrollerY : I
    //   32: isub
    //   33: istore_1
    //   34: aload_0
    //   35: iload_2
    //   36: putfield mLastScrollerY : I
    //   39: aload_0
    //   40: getfield mScrollConsumed : [I
    //   43: astore #6
    //   45: iconst_0
    //   46: istore_3
    //   47: aload #6
    //   49: iconst_1
    //   50: iconst_0
    //   51: iastore
    //   52: aload_0
    //   53: iconst_0
    //   54: iload_1
    //   55: aload #6
    //   57: aconst_null
    //   58: iconst_1
    //   59: invokevirtual dispatchNestedPreScroll : (II[I[II)Z
    //   62: pop
    //   63: iload_1
    //   64: aload_0
    //   65: getfield mScrollConsumed : [I
    //   68: iconst_1
    //   69: iaload
    //   70: isub
    //   71: istore_2
    //   72: aload_0
    //   73: invokevirtual getScrollRange : ()I
    //   76: istore #4
    //   78: iload_2
    //   79: istore_1
    //   80: iload_2
    //   81: ifeq -> 153
    //   84: aload_0
    //   85: invokevirtual getScrollY : ()I
    //   88: istore_1
    //   89: aload_0
    //   90: iconst_0
    //   91: iload_2
    //   92: aload_0
    //   93: invokevirtual getScrollX : ()I
    //   96: iload_1
    //   97: iconst_0
    //   98: iload #4
    //   100: iconst_0
    //   101: iconst_0
    //   102: iconst_0
    //   103: invokevirtual overScrollByCompat : (IIIIIIIIZ)Z
    //   106: pop
    //   107: aload_0
    //   108: invokevirtual getScrollY : ()I
    //   111: iload_1
    //   112: isub
    //   113: istore_1
    //   114: iload_2
    //   115: iload_1
    //   116: isub
    //   117: istore_2
    //   118: aload_0
    //   119: getfield mScrollConsumed : [I
    //   122: astore #6
    //   124: aload #6
    //   126: iconst_1
    //   127: iconst_0
    //   128: iastore
    //   129: aload_0
    //   130: iconst_0
    //   131: iload_1
    //   132: iconst_0
    //   133: iload_2
    //   134: aload_0
    //   135: getfield mScrollOffset : [I
    //   138: iconst_1
    //   139: aload #6
    //   141: invokevirtual dispatchNestedScroll : (IIII[II[I)V
    //   144: iload_2
    //   145: aload_0
    //   146: getfield mScrollConsumed : [I
    //   149: iconst_1
    //   150: iaload
    //   151: isub
    //   152: istore_1
    //   153: iload_1
    //   154: ifeq -> 250
    //   157: aload_0
    //   158: invokevirtual getOverScrollMode : ()I
    //   161: istore #5
    //   163: iload #5
    //   165: ifeq -> 183
    //   168: iload_3
    //   169: istore_2
    //   170: iload #5
    //   172: iconst_1
    //   173: if_icmpne -> 185
    //   176: iload_3
    //   177: istore_2
    //   178: iload #4
    //   180: ifle -> 185
    //   183: iconst_1
    //   184: istore_2
    //   185: iload_2
    //   186: ifeq -> 246
    //   189: iload_1
    //   190: ifge -> 221
    //   193: aload_0
    //   194: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   197: invokevirtual isFinished : ()Z
    //   200: ifeq -> 246
    //   203: aload_0
    //   204: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   207: aload_0
    //   208: getfield mScroller : Landroid/widget/OverScroller;
    //   211: invokevirtual getCurrVelocity : ()F
    //   214: f2i
    //   215: invokevirtual onAbsorb : (I)V
    //   218: goto -> 246
    //   221: aload_0
    //   222: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   225: invokevirtual isFinished : ()Z
    //   228: ifeq -> 246
    //   231: aload_0
    //   232: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   235: aload_0
    //   236: getfield mScroller : Landroid/widget/OverScroller;
    //   239: invokevirtual getCurrVelocity : ()F
    //   242: f2i
    //   243: invokevirtual onAbsorb : (I)V
    //   246: aload_0
    //   247: invokespecial abortAnimatedScroll : ()V
    //   250: aload_0
    //   251: getfield mScroller : Landroid/widget/OverScroller;
    //   254: invokevirtual isFinished : ()Z
    //   257: ifne -> 267
    //   260: aload_0
    //   261: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   264: goto -> 272
    //   267: aload_0
    //   268: iconst_1
    //   269: invokevirtual stopNestedScroll : (I)V
    //   272: return
  }
  
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect) {
    if (getChildCount() == 0)
      return 0; 
    int n = getHeight();
    int i = getScrollY();
    int k = i + n;
    int m = getVerticalFadingEdgeLength();
    int j = i;
    if (paramRect.top > 0)
      j = i + m; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    i = k;
    if (paramRect.bottom < view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin)
      i = k - m; 
    m = i;
    boolean bool = false;
    if (paramRect.bottom > m && paramRect.top > j) {
      if (paramRect.height() > n) {
        i = 0 + paramRect.top - j;
      } else {
        i = 0 + paramRect.bottom - m;
      } 
      i = Math.min(i, view.getBottom() + layoutParams.bottomMargin - k);
    } else {
      i = bool;
      if (paramRect.top < j) {
        i = bool;
        if (paramRect.bottom < m) {
          if (paramRect.height() > n) {
            i = 0 - m - paramRect.bottom;
          } else {
            i = 0 - j - paramRect.top;
          } 
          i = Math.max(i, -getScrollY());
        } 
      } 
    } 
    return i;
  }
  
  public int computeVerticalScrollExtent() {
    return super.computeVerticalScrollExtent();
  }
  
  public int computeVerticalScrollOffset() {
    return Math.max(0, super.computeVerticalScrollOffset());
  }
  
  public int computeVerticalScrollRange() {
    int j = getChildCount();
    int i = getHeight() - getPaddingBottom() - getPaddingTop();
    if (j == 0)
      return i; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    j = view.getBottom() + layoutParams.bottomMargin;
    int k = getScrollY();
    int m = Math.max(0, j - i);
    if (k < 0) {
      i = j - k;
    } else {
      i = j;
      if (k > m)
        i = j + k - m; 
    } 
    return i;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return this.mChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return this.mChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2, 0);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3) {
    return this.mChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2, paramInt3);
  }
  
  public void dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int paramInt5, int[] paramArrayOfint2) {
    this.mChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint1, paramInt5, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return this.mChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5) {
    return this.mChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramInt5);
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: aload_0
    //   6: invokevirtual getScrollY : ()I
    //   9: istore #8
    //   11: aload_0
    //   12: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   15: invokevirtual isFinished : ()Z
    //   18: ifne -> 181
    //   21: aload_1
    //   22: invokevirtual save : ()I
    //   25: istore #9
    //   27: aload_0
    //   28: invokevirtual getWidth : ()I
    //   31: istore #4
    //   33: aload_0
    //   34: invokevirtual getHeight : ()I
    //   37: istore #7
    //   39: iconst_0
    //   40: istore_3
    //   41: iconst_0
    //   42: iload #8
    //   44: invokestatic min : (II)I
    //   47: istore #6
    //   49: getstatic android/os/Build$VERSION.SDK_INT : I
    //   52: bipush #21
    //   54: if_icmplt -> 67
    //   57: iload #4
    //   59: istore_2
    //   60: aload_0
    //   61: invokevirtual getClipToPadding : ()Z
    //   64: ifeq -> 87
    //   67: iload #4
    //   69: aload_0
    //   70: invokevirtual getPaddingLeft : ()I
    //   73: aload_0
    //   74: invokevirtual getPaddingRight : ()I
    //   77: iadd
    //   78: isub
    //   79: istore_2
    //   80: iconst_0
    //   81: aload_0
    //   82: invokevirtual getPaddingLeft : ()I
    //   85: iadd
    //   86: istore_3
    //   87: iload #7
    //   89: istore #5
    //   91: iload #6
    //   93: istore #4
    //   95: getstatic android/os/Build$VERSION.SDK_INT : I
    //   98: bipush #21
    //   100: if_icmplt -> 141
    //   103: iload #7
    //   105: istore #5
    //   107: iload #6
    //   109: istore #4
    //   111: aload_0
    //   112: invokevirtual getClipToPadding : ()Z
    //   115: ifeq -> 141
    //   118: iload #7
    //   120: aload_0
    //   121: invokevirtual getPaddingTop : ()I
    //   124: aload_0
    //   125: invokevirtual getPaddingBottom : ()I
    //   128: iadd
    //   129: isub
    //   130: istore #5
    //   132: iload #6
    //   134: aload_0
    //   135: invokevirtual getPaddingTop : ()I
    //   138: iadd
    //   139: istore #4
    //   141: aload_1
    //   142: iload_3
    //   143: i2f
    //   144: iload #4
    //   146: i2f
    //   147: invokevirtual translate : (FF)V
    //   150: aload_0
    //   151: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   154: iload_2
    //   155: iload #5
    //   157: invokevirtual setSize : (II)V
    //   160: aload_0
    //   161: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   164: aload_1
    //   165: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   168: ifeq -> 175
    //   171: aload_0
    //   172: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   175: aload_1
    //   176: iload #9
    //   178: invokevirtual restoreToCount : (I)V
    //   181: aload_0
    //   182: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   185: invokevirtual isFinished : ()Z
    //   188: ifne -> 369
    //   191: aload_1
    //   192: invokevirtual save : ()I
    //   195: istore #9
    //   197: aload_0
    //   198: invokevirtual getWidth : ()I
    //   201: istore #4
    //   203: aload_0
    //   204: invokevirtual getHeight : ()I
    //   207: istore #6
    //   209: iconst_0
    //   210: istore_2
    //   211: aload_0
    //   212: invokevirtual getScrollRange : ()I
    //   215: iload #8
    //   217: invokestatic max : (II)I
    //   220: iload #6
    //   222: iadd
    //   223: istore #7
    //   225: getstatic android/os/Build$VERSION.SDK_INT : I
    //   228: bipush #21
    //   230: if_icmplt -> 243
    //   233: iload #4
    //   235: istore_3
    //   236: aload_0
    //   237: invokevirtual getClipToPadding : ()Z
    //   240: ifeq -> 263
    //   243: iload #4
    //   245: aload_0
    //   246: invokevirtual getPaddingLeft : ()I
    //   249: aload_0
    //   250: invokevirtual getPaddingRight : ()I
    //   253: iadd
    //   254: isub
    //   255: istore_3
    //   256: iconst_0
    //   257: aload_0
    //   258: invokevirtual getPaddingLeft : ()I
    //   261: iadd
    //   262: istore_2
    //   263: iload #6
    //   265: istore #5
    //   267: iload #7
    //   269: istore #4
    //   271: getstatic android/os/Build$VERSION.SDK_INT : I
    //   274: bipush #21
    //   276: if_icmplt -> 317
    //   279: iload #6
    //   281: istore #5
    //   283: iload #7
    //   285: istore #4
    //   287: aload_0
    //   288: invokevirtual getClipToPadding : ()Z
    //   291: ifeq -> 317
    //   294: iload #6
    //   296: aload_0
    //   297: invokevirtual getPaddingTop : ()I
    //   300: aload_0
    //   301: invokevirtual getPaddingBottom : ()I
    //   304: iadd
    //   305: isub
    //   306: istore #5
    //   308: iload #7
    //   310: aload_0
    //   311: invokevirtual getPaddingBottom : ()I
    //   314: isub
    //   315: istore #4
    //   317: aload_1
    //   318: iload_2
    //   319: iload_3
    //   320: isub
    //   321: i2f
    //   322: iload #4
    //   324: i2f
    //   325: invokevirtual translate : (FF)V
    //   328: aload_1
    //   329: ldc_w 180.0
    //   332: iload_3
    //   333: i2f
    //   334: fconst_0
    //   335: invokevirtual rotate : (FFF)V
    //   338: aload_0
    //   339: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   342: iload_3
    //   343: iload #5
    //   345: invokevirtual setSize : (II)V
    //   348: aload_0
    //   349: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   352: aload_1
    //   353: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   356: ifeq -> 363
    //   359: aload_0
    //   360: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   363: aload_1
    //   364: iload #9
    //   366: invokevirtual restoreToCount : (I)V
    //   369: return
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    View view;
    this.mTempRect.setEmpty();
    boolean bool = canScroll();
    char c = '';
    if (!bool) {
      boolean bool2 = isFocused();
      bool = false;
      if (bool2 && paramKeyEvent.getKeyCode() != 4) {
        View view1 = findFocus();
        view = view1;
        if (view1 == this)
          view = null; 
        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, 130);
        if (view != null && view != this && view.requestFocus(130))
          bool = true; 
        return bool;
      } 
      return false;
    } 
    boolean bool1 = false;
    bool = bool1;
    if (view.getAction() == 0) {
      switch (view.getKeyCode()) {
        default:
          return bool1;
        case 62:
          if (view.isShiftPressed())
            c = '!'; 
          pageScroll(c);
          return bool1;
        case 20:
          if (!view.isAltPressed()) {
            bool = arrowScroll(130);
          } else {
            bool = fullScroll(130);
          } 
          return bool;
        case 19:
          break;
      } 
      if (!view.isAltPressed()) {
        bool = arrowScroll(33);
      } else {
        bool = fullScroll(33);
      } 
    } 
    return bool;
  }
  
  public void fling(int paramInt) {
    if (getChildCount() > 0) {
      this.mScroller.fling(getScrollX(), getScrollY(), 0, paramInt, 0, 0, -2147483648, 2147483647, 0, 0);
      runAnimatedScroll(true);
    } 
  }
  
  public boolean fullScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    this.mTempRect.top = 0;
    this.mTempRect.bottom = j;
    if (i) {
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        this.mTempRect.bottom = view.getBottom() + layoutParams.bottomMargin + getPaddingBottom();
        Rect rect = this.mTempRect;
        rect.top = rect.bottom - j;
      } 
    } 
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  protected float getBottomFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    int i = getVerticalFadingEdgeLength();
    int k = getHeight();
    int j = getPaddingBottom();
    j = view.getBottom() + layoutParams.bottomMargin - getScrollY() - k - j;
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public int getMaxScrollAmount() {
    return (int)(getHeight() * 0.5F);
  }
  
  public int getNestedScrollAxes() {
    return this.mParentHelper.getNestedScrollAxes();
  }
  
  int getScrollRange() {
    int i = 0;
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      i = Math.max(0, view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin - getHeight() - getPaddingTop() - getPaddingBottom());
    } 
    return i;
  }
  
  protected float getTopFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    int i = getVerticalFadingEdgeLength();
    int j = getScrollY();
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public boolean hasNestedScrollingParent() {
    return hasNestedScrollingParent(0);
  }
  
  public boolean hasNestedScrollingParent(int paramInt) {
    return this.mChildHelper.hasNestedScrollingParent(paramInt);
  }
  
  public boolean isFillViewport() {
    return this.mFillViewport;
  }
  
  public boolean isNestedScrollingEnabled() {
    return this.mChildHelper.isNestedScrollingEnabled();
  }
  
  public boolean isSmoothScrollingEnabled() {
    return this.mSmoothScrollingEnabled;
  }
  
  protected void measureChild(View paramView, int paramInt1, int paramInt2) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), layoutParams.width), View.MeasureSpec.makeMeasureSpec(0, 0));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mIsLaidOut = false;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) != 0) {
      switch (paramMotionEvent.getAction()) {
        default:
          return false;
        case 8:
          break;
      } 
      if (!this.mIsBeingDragged) {
        float f = paramMotionEvent.getAxisValue(9);
        if (f != 0.0F) {
          int i = (int)(getVerticalScrollFactorCompat() * f);
          int j = getScrollRange();
          int m = getScrollY();
          int k = m - i;
          if (k < 0) {
            i = 0;
          } else {
            i = k;
            if (k > j)
              i = j; 
          } 
          if (i != m) {
            super.scrollTo(getScrollX(), i);
            return true;
          } 
        } 
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    ViewParent viewParent;
    int j;
    int i = paramMotionEvent.getAction();
    boolean bool2 = true;
    boolean bool3 = true;
    if (i == 2 && this.mIsBeingDragged)
      return true; 
    switch (i & 0xFF) {
      default:
        return this.mIsBeingDragged;
      case 6:
        onSecondaryPointerUp(paramMotionEvent);
      case 2:
        j = this.mActivePointerId;
        if (j != -1) {
          i = paramMotionEvent.findPointerIndex(j);
          if (i == -1) {
            Log.e("NestedScrollView", "Invalid pointerId=" + j + " in onInterceptTouchEvent");
          } else {
            i = (int)paramMotionEvent.getY(i);
            if (Math.abs(i - this.mLastMotionY) > this.mTouchSlop && (0x2 & getNestedScrollAxes()) == 0) {
              this.mIsBeingDragged = true;
              this.mLastMotionY = i;
              initVelocityTrackerIfNotExists();
              this.mVelocityTracker.addMovement(paramMotionEvent);
              this.mNestedYOffset = 0;
              viewParent = getParent();
              if (viewParent != null)
                viewParent.requestDisallowInterceptTouchEvent(true); 
            } 
          } 
        } 
      case 1:
      case 3:
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        recycleVelocityTracker();
        if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        stopNestedScroll(0);
      case 0:
        break;
    } 
    i = (int)viewParent.getY();
    if (!inChild((int)viewParent.getX(), i)) {
      boolean bool = bool3;
      if (!stopGlowAnimations((MotionEvent)viewParent))
        if (!this.mScroller.isFinished()) {
          bool = bool3;
        } else {
          bool = false;
        }  
      this.mIsBeingDragged = bool;
      recycleVelocityTracker();
    } 
    this.mLastMotionY = i;
    this.mActivePointerId = viewParent.getPointerId(0);
    initOrResetVelocityTracker();
    this.mVelocityTracker.addMovement((MotionEvent)viewParent);
    this.mScroller.computeScrollOffset();
    boolean bool1 = bool2;
    if (!stopGlowAnimations((MotionEvent)viewParent))
      if (!this.mScroller.isFinished()) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    this.mIsBeingDragged = bool1;
    startNestedScroll(2, 0);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mIsLayoutDirty = false;
    View view = this.mChildToScrollTo;
    if (view != null && isViewDescendantOf(view, (View)this))
      scrollToChild(this.mChildToScrollTo); 
    this.mChildToScrollTo = null;
    if (!this.mIsLaidOut) {
      if (this.mSavedState != null) {
        scrollTo(getScrollX(), this.mSavedState.scrollPosition);
        this.mSavedState = null;
      } 
      paramInt1 = 0;
      if (getChildCount() > 0) {
        View view1 = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view1.getLayoutParams();
        paramInt1 = view1.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      } 
      int i = getPaddingTop();
      int j = getPaddingBottom();
      paramInt3 = getScrollY();
      paramInt1 = clamp(paramInt3, paramInt4 - paramInt2 - i - j, paramInt1);
      if (paramInt1 != paramInt3)
        scrollTo(getScrollX(), paramInt1); 
    } 
    scrollTo(getScrollX(), getScrollY());
    this.mIsLaidOut = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (!this.mFillViewport)
      return; 
    if (View.MeasureSpec.getMode(paramInt2) == 0)
      return; 
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      int i = view.getMeasuredHeight();
      paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - layoutParams.topMargin - layoutParams.bottomMargin;
      if (i < paramInt2)
        view.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824)); 
    } 
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (!paramBoolean) {
      dispatchNestedFling(0.0F, paramFloat2, true);
      fling((int)paramFloat2);
      return true;
    } 
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    return dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint, 0);
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint, (int[])null, paramInt3);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    onNestedScrollInternal(paramInt4, 0, (int[])null);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    onNestedScrollInternal(paramInt4, paramInt5, (int[])null);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfint) {
    onNestedScrollInternal(paramInt4, paramInt5, paramArrayOfint);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {
    onNestedScrollAccepted(paramView1, paramView2, paramInt, 0);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    this.mParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
    startNestedScroll(2, paramInt2);
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    super.scrollTo(paramInt1, paramInt2);
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    int i;
    View view;
    if (paramInt == 2) {
      i = 130;
    } else {
      i = paramInt;
      if (paramInt == 1)
        i = 33; 
    } 
    if (paramRect == null) {
      view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, null, i);
    } else {
      view = FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, paramRect, i);
    } 
    return (view == null) ? false : (isOffScreen(view) ? false : view.requestFocus(i, paramRect));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mSavedState = savedState;
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.scrollPosition = getScrollY();
    return (Parcelable)savedState;
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    OnScrollChangeListener onScrollChangeListener = this.mOnScrollChangeListener;
    if (onScrollChangeListener != null)
      onScrollChangeListener.onScrollChange(this, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    View view = findFocus();
    if (view == null || this == view)
      return; 
    if (isWithinDeltaOfScreen(view, 0, paramInt4)) {
      view.getDrawingRect(this.mTempRect);
      offsetDescendantRectToMyCoords(view, this.mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
    } 
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) {
    return onStartNestedScroll(paramView1, paramView2, paramInt, 0);
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    boolean bool;
    if ((paramInt1 & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onStopNestedScroll(View paramView) {
    onStopNestedScroll(paramView, 0);
  }
  
  public void onStopNestedScroll(View paramView, int paramInt) {
    this.mParentHelper.onStopNestedScroll(paramView, paramInt);
    stopNestedScroll(paramInt);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int j;
    int k;
    initVelocityTrackerIfNotExists();
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      this.mNestedYOffset = 0; 
    MotionEvent motionEvent = MotionEvent.obtain(paramMotionEvent);
    motionEvent.offsetLocation(0.0F, this.mNestedYOffset);
    switch (i) {
      case 6:
        onSecondaryPointerUp(paramMotionEvent);
        this.mLastMotionY = (int)paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId));
        break;
      case 5:
        i = paramMotionEvent.getActionIndex();
        this.mLastMotionY = (int)paramMotionEvent.getY(i);
        this.mActivePointerId = paramMotionEvent.getPointerId(i);
        break;
      case 3:
        if (this.mIsBeingDragged && getChildCount() > 0 && this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        this.mActivePointerId = -1;
        endDrag();
        break;
      case 2:
        k = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (k == -1) {
          Log.e("NestedScrollView", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
          break;
        } 
        j = (int)paramMotionEvent.getY(k);
        i = this.mLastMotionY - j;
        i -= releaseVerticalGlow(i, paramMotionEvent.getX(k));
        if (!this.mIsBeingDragged && Math.abs(i) > this.mTouchSlop) {
          ViewParent viewParent = getParent();
          if (viewParent != null)
            viewParent.requestDisallowInterceptTouchEvent(true); 
          this.mIsBeingDragged = true;
          if (i > 0) {
            i -= this.mTouchSlop;
          } else {
            i += this.mTouchSlop;
          } 
        } 
        if (this.mIsBeingDragged) {
          int m;
          if (dispatchNestedPreScroll(0, i, this.mScrollConsumed, this.mScrollOffset, 0)) {
            m = this.mScrollConsumed[1];
            this.mNestedYOffset += this.mScrollOffset[1];
            m = i - m;
          } else {
            m = i;
          } 
          this.mLastMotionY = j - this.mScrollOffset[1];
          int i1 = getScrollY();
          int n = getScrollRange();
          i = getOverScrollMode();
          if (i == 0 || (i == 1 && n > 0)) {
            j = 1;
          } else {
            j = 0;
          } 
          if (overScrollByCompat(0, m, 0, getScrollY(), 0, n, 0, 0, true) && !hasNestedScrollingParent(0)) {
            i = 1;
          } else {
            i = 0;
          } 
          int i2 = getScrollY() - i1;
          int[] arrayOfInt = this.mScrollConsumed;
          arrayOfInt[1] = 0;
          dispatchNestedScroll(0, i2, 0, m - i2, this.mScrollOffset, 0, arrayOfInt);
          i2 = this.mLastMotionY;
          arrayOfInt = this.mScrollOffset;
          this.mLastMotionY = i2 - arrayOfInt[1];
          this.mNestedYOffset += arrayOfInt[1];
          if (j != 0) {
            m -= this.mScrollConsumed[1];
            j = i1 + m;
            if (j < 0) {
              EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, -m / getHeight(), paramMotionEvent.getX(k) / getWidth());
              if (!this.mEdgeGlowBottom.isFinished())
                this.mEdgeGlowBottom.onRelease(); 
            } else if (j > n) {
              EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, m / getHeight(), 1.0F - paramMotionEvent.getX(k) / getWidth());
              if (!this.mEdgeGlowTop.isFinished())
                this.mEdgeGlowTop.onRelease(); 
            } 
            if (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished()) {
              ViewCompat.postInvalidateOnAnimation((View)this);
              i = 0;
            } 
          } 
          if (i != 0)
            this.mVelocityTracker.clear(); 
        } 
        break;
      case 1:
        velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        i = (int)velocityTracker.getYVelocity(this.mActivePointerId);
        if (Math.abs(i) >= this.mMinimumVelocity) {
          if (!edgeEffectFling(i) && !dispatchNestedPreFling(0.0F, -i)) {
            dispatchNestedFling(0.0F, -i, true);
            fling(-i);
          } 
        } else if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
          ViewCompat.postInvalidateOnAnimation((View)this);
        } 
        this.mActivePointerId = -1;
        endDrag();
        break;
      case 0:
        if (getChildCount() == 0)
          return false; 
        if (this.mIsBeingDragged) {
          ViewParent viewParent = getParent();
          if (viewParent != null)
            viewParent.requestDisallowInterceptTouchEvent(true); 
        } 
        if (!this.mScroller.isFinished())
          abortAnimatedScroll(); 
        this.mLastMotionY = (int)velocityTracker.getY();
        this.mActivePointerId = velocityTracker.getPointerId(0);
        startNestedScroll(2, 0);
        break;
    } 
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.addMovement(motionEvent); 
    motionEvent.recycle();
    return true;
  }
  
  boolean overScrollByCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    int i = getOverScrollMode();
    if (computeHorizontalScrollRange() > computeHorizontalScrollExtent()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (computeVerticalScrollRange() > computeVerticalScrollExtent()) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (i == 0 || (i == 1 && bool1)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (i == 0 || (i == 1 && bool2)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    paramInt3 += paramInt1;
    if (!bool1) {
      paramInt1 = 0;
    } else {
      paramInt1 = paramInt7;
    } 
    paramInt4 += paramInt2;
    if (!bool2) {
      paramInt2 = 0;
    } else {
      paramInt2 = paramInt8;
    } 
    paramInt7 = -paramInt1;
    paramInt1 += paramInt5;
    paramInt5 = -paramInt2;
    paramInt2 += paramInt6;
    if (paramInt3 > paramInt1) {
      paramBoolean = true;
    } else if (paramInt3 < paramInt7) {
      paramInt1 = paramInt7;
      paramBoolean = true;
    } else {
      paramBoolean = false;
      paramInt1 = paramInt3;
    } 
    if (paramInt4 > paramInt2) {
      bool3 = true;
    } else if (paramInt4 < paramInt5) {
      paramInt2 = paramInt5;
      bool3 = true;
    } else {
      bool3 = false;
      paramInt2 = paramInt4;
    } 
    if (bool3 && !hasNestedScrollingParent(1))
      this.mScroller.springBack(paramInt1, paramInt2, 0, 0, 0, getScrollRange()); 
    onOverScrolled(paramInt1, paramInt2, paramBoolean, bool3);
    return (paramBoolean || bool3);
  }
  
  public boolean pageScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    if (i) {
      this.mTempRect.top = getScrollY() + j;
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin + getPaddingBottom();
        if (this.mTempRect.top + j > i)
          this.mTempRect.top = i - j; 
      } 
    } else {
      this.mTempRect.top = getScrollY() - j;
      if (this.mTempRect.top < 0)
        this.mTempRect.top = 0; 
    } 
    Rect rect = this.mTempRect;
    rect.bottom = rect.top + j;
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    if (!this.mIsLayoutDirty) {
      scrollToChild(paramView2);
    } else {
      this.mChildToScrollTo = paramView2;
    } 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    paramRect.offset(paramView.getLeft() - paramView.getScrollX(), paramView.getTop() - paramView.getScrollY());
    return scrollToChildRect(paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    if (paramBoolean)
      recycleVelocityTracker(); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    this.mIsLayoutDirty = true;
    super.requestLayout();
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      int i4 = getWidth();
      int i3 = getPaddingLeft();
      int i5 = getPaddingRight();
      int i2 = view.getWidth();
      int i7 = layoutParams.leftMargin;
      int i6 = layoutParams.rightMargin;
      int j = getHeight();
      int i = getPaddingTop();
      int m = getPaddingBottom();
      int n = view.getHeight();
      int k = layoutParams.topMargin;
      int i1 = layoutParams.bottomMargin;
      paramInt1 = clamp(paramInt1, i4 - i3 - i5, i2 + i7 + i6);
      paramInt2 = clamp(paramInt2, j - i - m, n + k + i1);
      if (paramInt1 != getScrollX() || paramInt2 != getScrollY())
        super.scrollTo(paramInt1, paramInt2); 
    } 
  }
  
  public void setFillViewport(boolean paramBoolean) {
    if (paramBoolean != this.mFillViewport) {
      this.mFillViewport = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    this.mChildHelper.setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setOnScrollChangeListener(OnScrollChangeListener paramOnScrollChangeListener) {
    this.mOnScrollChangeListener = paramOnScrollChangeListener;
  }
  
  public void setSmoothScrollingEnabled(boolean paramBoolean) {
    this.mSmoothScrollingEnabled = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState() {
    return true;
  }
  
  public final void smoothScrollBy(int paramInt1, int paramInt2) {
    smoothScrollBy(paramInt1, paramInt2, 250, false);
  }
  
  public final void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3) {
    smoothScrollBy(paramInt1, paramInt2, paramInt3, false);
  }
  
  public final void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 250, false);
  }
  
  public final void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    smoothScrollTo(paramInt1, paramInt2, paramInt3, false);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    smoothScrollBy(paramInt1 - getScrollX(), paramInt2 - getScrollY(), paramInt3, paramBoolean);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, boolean paramBoolean) {
    smoothScrollTo(paramInt1, paramInt2, 250, paramBoolean);
  }
  
  public boolean startNestedScroll(int paramInt) {
    return startNestedScroll(paramInt, 0);
  }
  
  public boolean startNestedScroll(int paramInt1, int paramInt2) {
    return this.mChildHelper.startNestedScroll(paramInt1, paramInt2);
  }
  
  public void stopNestedScroll() {
    stopNestedScroll(0);
  }
  
  public void stopNestedScroll(int paramInt) {
    this.mChildHelper.stopNestedScroll(paramInt);
  }
  
  static class AccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      boolean bool;
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityEvent.setClassName(ScrollView.class.getName());
      if (nestedScrollView.getScrollRange() > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      param1AccessibilityEvent.setScrollable(bool);
      param1AccessibilityEvent.setScrollX(nestedScrollView.getScrollX());
      param1AccessibilityEvent.setScrollY(nestedScrollView.getScrollY());
      AccessibilityRecordCompat.setMaxScrollX((AccessibilityRecord)param1AccessibilityEvent, nestedScrollView.getScrollX());
      AccessibilityRecordCompat.setMaxScrollY((AccessibilityRecord)param1AccessibilityEvent, nestedScrollView.getScrollRange());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
      if (nestedScrollView.isEnabled()) {
        int i = nestedScrollView.getScrollRange();
        if (i > 0) {
          param1AccessibilityNodeInfoCompat.setScrollable(true);
          if (nestedScrollView.getScrollY() > 0) {
            param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
            param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP);
          } 
          if (nestedScrollView.getScrollY() < i) {
            param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
            param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN);
          } 
        } 
      } 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      if (!nestedScrollView.isEnabled())
        return false; 
      switch (param1Int) {
        default:
          return false;
        case 8192:
        case 16908344:
          i = nestedScrollView.getHeight();
          j = nestedScrollView.getPaddingBottom();
          param1Int = nestedScrollView.getPaddingTop();
          param1Int = Math.max(nestedScrollView.getScrollY() - i - j - param1Int, 0);
          if (param1Int != nestedScrollView.getScrollY()) {
            nestedScrollView.smoothScrollTo(0, param1Int, true);
            return true;
          } 
          return false;
        case 4096:
        case 16908346:
          break;
      } 
      int i = nestedScrollView.getHeight();
      param1Int = nestedScrollView.getPaddingBottom();
      int j = nestedScrollView.getPaddingTop();
      param1Int = Math.min(nestedScrollView.getScrollY() + i - param1Int - j, nestedScrollView.getScrollRange());
      if (param1Int != nestedScrollView.getScrollY()) {
        nestedScrollView.smoothScrollTo(0, param1Int, true);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnScrollChangeListener {
    void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public NestedScrollView.SavedState createFromParcel(Parcel param2Parcel) {
          return new NestedScrollView.SavedState(param2Parcel);
        }
        
        public NestedScrollView.SavedState[] newArray(int param2Int) {
          return new NestedScrollView.SavedState[param2Int];
        }
      };
    
    public int scrollPosition;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.scrollPosition = param1Parcel.readInt();
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.scrollPosition);
    }
  }
  
  class null implements Parcelable.Creator<SavedState> {
    public NestedScrollView.SavedState createFromParcel(Parcel param1Parcel) {
      return new NestedScrollView.SavedState(param1Parcel);
    }
    
    public NestedScrollView.SavedState[] newArray(int param1Int) {
      return new NestedScrollView.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\NestedScrollView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */