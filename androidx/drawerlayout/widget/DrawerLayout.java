package androidx.drawerlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup {
  private static final boolean ALLOW_EDGE_LOCK = false;
  
  static final boolean CAN_HIDE_DESCENDANTS;
  
  private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
  
  private static final int DEFAULT_SCRIM_COLOR = -1728053248;
  
  private static final int DRAWER_ELEVATION = 10;
  
  static final int[] LAYOUT_ATTRS;
  
  public static final int LOCK_MODE_LOCKED_CLOSED = 1;
  
  public static final int LOCK_MODE_LOCKED_OPEN = 2;
  
  public static final int LOCK_MODE_UNDEFINED = 3;
  
  public static final int LOCK_MODE_UNLOCKED = 0;
  
  private static final int MIN_DRAWER_MARGIN = 64;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final int PEEK_DELAY = 160;
  
  private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "DrawerLayout";
  
  private static final int[] THEME_ATTRS = new int[] { 16843828 };
  
  private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
  
  private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
  
  private Rect mChildHitRect;
  
  private Matrix mChildInvertedMatrix;
  
  private boolean mChildrenCanceledTouch;
  
  private boolean mDisallowInterceptRequested;
  
  private boolean mDrawStatusBarBackground;
  
  private float mDrawerElevation;
  
  private int mDrawerState;
  
  private boolean mFirstLayout = true;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private Object mLastInsets;
  
  private final ViewDragCallback mLeftCallback;
  
  private final ViewDragHelper mLeftDragger;
  
  private DrawerListener mListener;
  
  private List<DrawerListener> mListeners;
  
  private int mLockModeEnd = 3;
  
  private int mLockModeLeft = 3;
  
  private int mLockModeRight = 3;
  
  private int mLockModeStart = 3;
  
  private int mMinDrawerMargin;
  
  private final ArrayList<View> mNonDrawerViews;
  
  private final ViewDragCallback mRightCallback;
  
  private final ViewDragHelper mRightDragger;
  
  private int mScrimColor = -1728053248;
  
  private float mScrimOpacity;
  
  private Paint mScrimPaint = new Paint();
  
  private Drawable mShadowEnd = null;
  
  private Drawable mShadowLeft = null;
  
  private Drawable mShadowLeftResolved;
  
  private Drawable mShadowRight = null;
  
  private Drawable mShadowRightResolved;
  
  private Drawable mShadowStart = null;
  
  private Drawable mStatusBarBackground;
  
  private CharSequence mTitleLeft;
  
  private CharSequence mTitleRight;
  
  static {
    LAYOUT_ATTRS = new int[] { 16842931 };
    if (Build.VERSION.SDK_INT >= 19) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    CAN_HIDE_DESCENDANTS = bool1;
    if (Build.VERSION.SDK_INT >= 21) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    SET_DRAWER_SHADOW_FROM_ELEVATION = bool1;
  }
  
  public DrawerLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setDescendantFocusability(262144);
    float f1 = (getResources().getDisplayMetrics()).density;
    this.mMinDrawerMargin = (int)(64.0F * f1 + 0.5F);
    float f2 = 400.0F * f1;
    ViewDragCallback viewDragCallback2 = new ViewDragCallback(3);
    this.mLeftCallback = viewDragCallback2;
    ViewDragCallback viewDragCallback1 = new ViewDragCallback(5);
    this.mRightCallback = viewDragCallback1;
    ViewDragHelper viewDragHelper2 = ViewDragHelper.create(this, 1.0F, viewDragCallback2);
    this.mLeftDragger = viewDragHelper2;
    viewDragHelper2.setEdgeTrackingEnabled(1);
    viewDragHelper2.setMinVelocity(f2);
    viewDragCallback2.setDragger(viewDragHelper2);
    ViewDragHelper viewDragHelper1 = ViewDragHelper.create(this, 1.0F, viewDragCallback1);
    this.mRightDragger = viewDragHelper1;
    viewDragHelper1.setEdgeTrackingEnabled(2);
    viewDragHelper1.setMinVelocity(f2);
    viewDragCallback1.setDragger(viewDragHelper1);
    setFocusableInTouchMode(true);
    ViewCompat.setImportantForAccessibility((View)this, 1);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    setMotionEventSplittingEnabled(false);
    if (ViewCompat.getFitsSystemWindows((View)this))
      if (Build.VERSION.SDK_INT >= 21) {
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
              final DrawerLayout this$0;
              
              public WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
                boolean bool;
                DrawerLayout drawerLayout = (DrawerLayout)param1View;
                if (param1WindowInsets.getSystemWindowInsetTop() > 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                drawerLayout.setChildInsets(param1WindowInsets, bool);
                return param1WindowInsets.consumeSystemWindowInsets();
              }
            });
        setSystemUiVisibility(1280);
        TypedArray typedArray = paramContext.obtainStyledAttributes(THEME_ATTRS);
        try {
          this.mStatusBarBackground = typedArray.getDrawable(0);
        } finally {
          typedArray.recycle();
        } 
      } else {
        this.mStatusBarBackground = null;
      }  
    this.mDrawerElevation = 10.0F * f1;
    this.mNonDrawerViews = new ArrayList<>();
  }
  
  private boolean dispatchTransformedGenericPointerEvent(MotionEvent paramMotionEvent, View paramView) {
    boolean bool;
    if (!paramView.getMatrix().isIdentity()) {
      paramMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.recycle();
    } else {
      float f1 = (getScrollX() - paramView.getLeft());
      float f2 = (getScrollY() - paramView.getTop());
      paramMotionEvent.offsetLocation(f1, f2);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-f1, -f2);
    } 
    return bool;
  }
  
  private MotionEvent getTransformedMotionEvent(MotionEvent paramMotionEvent, View paramView) {
    float f1 = (getScrollX() - paramView.getLeft());
    float f2 = (getScrollY() - paramView.getTop());
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.offsetLocation(f1, f2);
    Matrix matrix = paramView.getMatrix();
    if (!matrix.isIdentity()) {
      if (this.mChildInvertedMatrix == null)
        this.mChildInvertedMatrix = new Matrix(); 
      matrix.invert(this.mChildInvertedMatrix);
      paramMotionEvent.transform(this.mChildInvertedMatrix);
    } 
    return paramMotionEvent;
  }
  
  static String gravityToString(int paramInt) {
    return ((paramInt & 0x3) == 3) ? "LEFT" : (((paramInt & 0x5) == 5) ? "RIGHT" : Integer.toHexString(paramInt));
  }
  
  private static boolean hasOpaqueBackground(View paramView) {
    Drawable drawable = paramView.getBackground();
    boolean bool = false;
    if (drawable != null) {
      if (drawable.getOpacity() == -1)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private boolean hasPeekingDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      if (((LayoutParams)getChildAt(b).getLayoutParams()).isPeeking)
        return true; 
    } 
    return false;
  }
  
  private boolean hasVisibleDrawer() {
    boolean bool;
    if (findVisibleDrawer() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean includeChildForAccessibility(View paramView) {
    boolean bool;
    if (ViewCompat.getImportantForAccessibility(paramView) != 4 && ViewCompat.getImportantForAccessibility(paramView) != 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isInBoundsOfChild(float paramFloat1, float paramFloat2, View paramView) {
    if (this.mChildHitRect == null)
      this.mChildHitRect = new Rect(); 
    paramView.getHitRect(this.mChildHitRect);
    return this.mChildHitRect.contains((int)paramFloat1, (int)paramFloat2);
  }
  
  private boolean mirror(Drawable paramDrawable, int paramInt) {
    if (paramDrawable == null || !DrawableCompat.isAutoMirrored(paramDrawable))
      return false; 
    DrawableCompat.setLayoutDirection(paramDrawable, paramInt);
    return true;
  }
  
  private Drawable resolveLeftShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      Drawable drawable = this.mShadowStart;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowStart;
      } 
    } else {
      Drawable drawable = this.mShadowEnd;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowEnd;
      } 
    } 
    return this.mShadowLeft;
  }
  
  private Drawable resolveRightShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      Drawable drawable = this.mShadowEnd;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowEnd;
      } 
    } else {
      Drawable drawable = this.mShadowStart;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowStart;
      } 
    } 
    return this.mShadowRight;
  }
  
  private void resolveShadowDrawables() {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    this.mShadowLeftResolved = resolveLeftShadow();
    this.mShadowRightResolved = resolveRightShadow();
  }
  
  private void updateChildrenImportantForAccessibility(View paramView, boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((!paramBoolean && !isDrawerView(view)) || (paramBoolean && view == paramView)) {
        ViewCompat.setImportantForAccessibility(view, 1);
      } else {
        ViewCompat.setImportantForAccessibility(view, 4);
      } 
    } 
  }
  
  public void addDrawerListener(DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    if (this.mListeners == null)
      this.mListeners = new ArrayList<>(); 
    this.mListeners.add(paramDrawerListener);
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    if (getDescendantFocusability() == 393216)
      return; 
    int j = getChildCount();
    int i = 0;
    byte b;
    for (b = 0; b < j; b++) {
      View view = getChildAt(b);
      if (isDrawerView(view)) {
        if (isDrawerOpen(view)) {
          i = 1;
          view.addFocusables(paramArrayList, paramInt1, paramInt2);
        } 
      } else {
        this.mNonDrawerViews.add(view);
      } 
    } 
    if (!i) {
      i = this.mNonDrawerViews.size();
      for (b = 0; b < i; b++) {
        View view = this.mNonDrawerViews.get(b);
        if (view.getVisibility() == 0)
          view.addFocusables(paramArrayList, paramInt1, paramInt2); 
      } 
    } 
    this.mNonDrawerViews.clear();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (findOpenDrawer() != null || isDrawerView(paramView)) {
      ViewCompat.setImportantForAccessibility(paramView, 4);
    } else {
      ViewCompat.setImportantForAccessibility(paramView, 1);
    } 
    if (!CAN_HIDE_DESCENDANTS)
      ViewCompat.setAccessibilityDelegate(paramView, this.mChildAccessibilityDelegate); 
  }
  
  void cancelChildViewTouch() {
    if (!this.mChildrenCanceledTouch) {
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
      int i = getChildCount();
      for (byte b = 0; b < i; b++)
        getChildAt(b).dispatchTouchEvent(motionEvent); 
      motionEvent.recycle();
      this.mChildrenCanceledTouch = true;
    } 
  }
  
  boolean checkDrawerViewAbsoluteGravity(View paramView, int paramInt) {
    boolean bool;
    if ((getDrawerViewAbsoluteGravity(paramView) & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void closeDrawer(int paramInt) {
    closeDrawer(paramInt, true);
  }
  
  public void closeDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      closeDrawer(view, paramBoolean);
      return;
    } 
    throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(paramInt));
  }
  
  public void closeDrawer(View paramView) {
    closeDrawer(paramView, true);
  }
  
  public void closeDrawer(View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 0.0F;
        layoutParams.openState = 0;
      } else if (paramBoolean) {
        layoutParams.openState = 0x4 | layoutParams.openState;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, -paramView.getWidth(), paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 0.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(4);
      } 
      invalidate();
      return;
    } 
    throw new IllegalArgumentException("View " + paramView + " is not a sliding drawer");
  }
  
  public void closeDrawers() {
    closeDrawers(false);
  }
  
  void closeDrawers(boolean paramBoolean) {
    boolean bool;
    byte b1 = 0;
    int i = getChildCount();
    byte b2 = 0;
    while (b2 < i) {
      boolean bool1;
      View view = getChildAt(b2);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int j = b1;
      if (isDrawerView(view))
        if (paramBoolean && !layoutParams.isPeeking) {
          j = b1;
        } else {
          boolean bool2;
          j = view.getWidth();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            bool2 = b1 | this.mLeftDragger.smoothSlideViewTo(view, -j, view.getTop());
          } else {
            bool2 |= this.mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
          } 
          layoutParams.isPeeking = false;
          bool1 = bool2;
        }  
      b2++;
      bool = bool1;
    } 
    this.mLeftCallback.removeCallbacks();
    this.mRightCallback.removeCallbacks();
    if (bool)
      invalidate(); 
  }
  
  public void computeScroll() {
    int i = getChildCount();
    float f = 0.0F;
    for (byte b = 0; b < i; b++)
      f = Math.max(f, ((LayoutParams)getChildAt(b).getLayoutParams()).onScreen); 
    this.mScrimOpacity = f;
    boolean bool2 = this.mLeftDragger.continueSettling(true);
    boolean bool1 = this.mRightDragger.continueSettling(true);
    if (bool2 || bool1)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) == 0 || paramMotionEvent.getAction() == 10 || this.mScrimOpacity <= 0.0F)
      return super.dispatchGenericMotionEvent(paramMotionEvent); 
    int i = getChildCount();
    if (i != 0) {
      float f2 = paramMotionEvent.getX();
      float f1 = paramMotionEvent.getY();
      while (--i >= 0) {
        View view = getChildAt(i);
        if (isInBoundsOfChild(f2, f1, view) && !isContentView(view) && dispatchTransformedGenericPointerEvent(paramMotionEvent, view))
          return true; 
        i--;
      } 
    } 
    return false;
  }
  
  void dispatchOnDrawerClosed(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 1) {
      layoutParams.openState = 0;
      List<DrawerListener> list = this.mListeners;
      if (list != null)
        for (int i = list.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerClosed(paramView);  
      updateChildrenImportantForAccessibility(paramView, false);
      if (hasWindowFocus()) {
        paramView = getRootView();
        if (paramView != null)
          paramView.sendAccessibilityEvent(32); 
      } 
    } 
  }
  
  void dispatchOnDrawerOpened(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 0) {
      layoutParams.openState = 1;
      List<DrawerListener> list = this.mListeners;
      if (list != null)
        for (int i = list.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerOpened(paramView);  
      updateChildrenImportantForAccessibility(paramView, true);
      if (hasWindowFocus())
        sendAccessibilityEvent(32); 
    } 
  }
  
  void dispatchOnDrawerSlide(View paramView, float paramFloat) {
    List<DrawerListener> list = this.mListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((DrawerListener)this.mListeners.get(i)).onDrawerSlide(paramView, paramFloat);  
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    int m = getHeight();
    boolean bool1 = isContentView(paramView);
    int i = 0;
    int j = getWidth();
    int k = paramCanvas.save();
    if (bool1) {
      int n = getChildCount();
      byte b = 0;
      while (b < n) {
        View view = getChildAt(b);
        int i1 = i;
        int i2 = j;
        if (view != paramView) {
          i1 = i;
          i2 = j;
          if (view.getVisibility() == 0) {
            i1 = i;
            i2 = j;
            if (hasOpaqueBackground(view)) {
              i1 = i;
              i2 = j;
              if (isDrawerView(view))
                if (view.getHeight() < m) {
                  i1 = i;
                  i2 = j;
                } else if (checkDrawerViewAbsoluteGravity(view, 3)) {
                  i2 = view.getRight();
                  i1 = i;
                  if (i2 > i)
                    i1 = i2; 
                  i2 = j;
                } else {
                  int i3 = view.getLeft();
                  i1 = i;
                  i2 = j;
                  if (i3 < j) {
                    i2 = i3;
                    i1 = i;
                  } 
                }  
            } 
          } 
        } 
        b++;
        i = i1;
        j = i2;
      } 
      paramCanvas.clipRect(i, 0, j, getHeight());
    } else {
      i = 0;
    } 
    boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(k);
    float f = this.mScrimOpacity;
    if (f > 0.0F && bool1) {
      int i1 = this.mScrimColor;
      int n = (int)(((0xFF000000 & i1) >>> 24) * f);
      this.mScrimPaint.setColor(n << 24 | i1 & 0xFFFFFF);
      paramCanvas.drawRect(i, 0.0F, j, getHeight(), this.mScrimPaint);
    } else if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(paramView, 3)) {
      int n = this.mShadowLeftResolved.getIntrinsicWidth();
      j = paramView.getRight();
      i = this.mLeftDragger.getEdgeSize();
      f = Math.max(0.0F, Math.min(j / i, 1.0F));
      this.mShadowLeftResolved.setBounds(j, paramView.getTop(), j + n, paramView.getBottom());
      this.mShadowLeftResolved.setAlpha((int)(255.0F * f));
      this.mShadowLeftResolved.draw(paramCanvas);
    } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(paramView, 5)) {
      j = this.mShadowRightResolved.getIntrinsicWidth();
      i = paramView.getLeft();
      int n = getWidth();
      int i1 = this.mRightDragger.getEdgeSize();
      f = Math.max(0.0F, Math.min((n - i) / i1, 1.0F));
      this.mShadowRightResolved.setBounds(i - j, paramView.getTop(), i, paramView.getBottom());
      this.mShadowRightResolved.setAlpha((int)(255.0F * f));
      this.mShadowRightResolved.draw(paramCanvas);
    } 
    return bool2;
  }
  
  View findDrawerWithGravity(int paramInt) {
    int i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    int j = getChildCount();
    for (paramInt = 0; paramInt < j; paramInt++) {
      View view = getChildAt(paramInt);
      if ((getDrawerViewAbsoluteGravity(view) & 0x7) == (i & 0x7))
        return view; 
    } 
    return null;
  }
  
  View findOpenDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((((LayoutParams)view.getLayoutParams()).openState & 0x1) == 1)
        return view; 
    } 
    return null;
  }
  
  View findVisibleDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (isDrawerView(view) && isDrawerVisible(view))
        return view; 
    } 
    return null;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams(-1, -1);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutParams layoutParams;
    if (paramLayoutParams instanceof LayoutParams) {
      layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
    } else if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      layoutParams = new LayoutParams(layoutParams);
    } else {
      layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    return (ViewGroup.LayoutParams)layoutParams;
  }
  
  public float getDrawerElevation() {
    return SET_DRAWER_SHADOW_FROM_ELEVATION ? this.mDrawerElevation : 0.0F;
  }
  
  public int getDrawerLockMode(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    switch (paramInt) {
      default:
        return 0;
      case 8388613:
        paramInt = this.mLockModeEnd;
        if (paramInt != 3)
          return paramInt; 
        if (i == 0) {
          paramInt = this.mLockModeRight;
        } else {
          paramInt = this.mLockModeLeft;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 8388611:
        paramInt = this.mLockModeStart;
        if (paramInt != 3)
          return paramInt; 
        if (i == 0) {
          paramInt = this.mLockModeLeft;
        } else {
          paramInt = this.mLockModeRight;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 5:
        paramInt = this.mLockModeRight;
        if (paramInt != 3)
          return paramInt; 
        if (i == 0) {
          paramInt = this.mLockModeEnd;
        } else {
          paramInt = this.mLockModeStart;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 3:
        break;
    } 
    paramInt = this.mLockModeLeft;
    if (paramInt != 3)
      return paramInt; 
    if (i == 0) {
      paramInt = this.mLockModeStart;
    } else {
      paramInt = this.mLockModeEnd;
    } 
    if (paramInt != 3)
      return paramInt; 
  }
  
  public int getDrawerLockMode(View paramView) {
    if (isDrawerView(paramView))
      return getDrawerLockMode(((LayoutParams)paramView.getLayoutParams()).gravity); 
    throw new IllegalArgumentException("View " + paramView + " is not a drawer");
  }
  
  public CharSequence getDrawerTitle(int paramInt) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    return (paramInt == 3) ? this.mTitleLeft : ((paramInt == 5) ? this.mTitleRight : null);
  }
  
  int getDrawerViewAbsoluteGravity(View paramView) {
    return GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
  }
  
  float getDrawerViewOffset(View paramView) {
    return ((LayoutParams)paramView.getLayoutParams()).onScreen;
  }
  
  public Drawable getStatusBarBackgroundDrawable() {
    return this.mStatusBarBackground;
  }
  
  boolean isContentView(View paramView) {
    boolean bool;
    if (((LayoutParams)paramView.getLayoutParams()).gravity == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDrawerOpen(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerOpen(view) : false;
  }
  
  public boolean isDrawerOpen(View paramView) {
    if (isDrawerView(paramView)) {
      int i = ((LayoutParams)paramView.getLayoutParams()).openState;
      boolean bool = true;
      if ((i & 0x1) != 1)
        bool = false; 
      return bool;
    } 
    throw new IllegalArgumentException("View " + paramView + " is not a drawer");
  }
  
  boolean isDrawerView(View paramView) {
    int i = ((LayoutParams)paramView.getLayoutParams()).gravity;
    i = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(paramView));
    return ((i & 0x3) != 0) ? true : (((i & 0x5) != 0));
  }
  
  public boolean isDrawerVisible(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerVisible(view) : false;
  }
  
  public boolean isDrawerVisible(View paramView) {
    if (isDrawerView(paramView)) {
      boolean bool;
      if (((LayoutParams)paramView.getLayoutParams()).onScreen > 0.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    throw new IllegalArgumentException("View " + paramView + " is not a drawer");
  }
  
  void moveDrawerToOffset(View paramView, float paramFloat) {
    float f = getDrawerViewOffset(paramView);
    int i = paramView.getWidth();
    int j = (int)(i * f);
    i = (int)(i * paramFloat) - j;
    if (!checkDrawerViewAbsoluteGravity(paramView, 3))
      i = -i; 
    paramView.offsetLeftAndRight(i);
    setDrawerViewOffset(paramView, paramFloat);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      boolean bool;
      if (Build.VERSION.SDK_INT >= 21) {
        Object object = this.mLastInsets;
        if (object != null) {
          bool = ((WindowInsets)object).getSystemWindowInsetTop();
        } else {
          bool = false;
        } 
      } else {
        bool = false;
      } 
      if (bool) {
        this.mStatusBarBackground.setBounds(0, 0, getWidth(), bool);
        this.mStatusBarBackground.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    int i = paramMotionEvent.getActionMasked();
    boolean bool5 = this.mLeftDragger.shouldInterceptTouchEvent(paramMotionEvent);
    boolean bool6 = this.mRightDragger.shouldInterceptTouchEvent(paramMotionEvent);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool4 = true;
    switch (i) {
      default:
        i = bool1;
        break;
      case 2:
        i = bool1;
        if (this.mLeftDragger.checkTouchSlop(3)) {
          this.mLeftCallback.removeCallbacks();
          this.mRightCallback.removeCallbacks();
          i = bool1;
        } 
        break;
      case 1:
      case 3:
        closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        i = bool1;
        break;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        i = bool2;
        if (this.mScrimOpacity > 0.0F) {
          View view = this.mLeftDragger.findTopChildUnder((int)f1, (int)f2);
          i = bool2;
          if (view != null) {
            i = bool2;
            if (isContentView(view))
              i = 1; 
          } 
        } 
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        break;
    } 
    boolean bool3 = bool4;
    if (!(bool5 | bool6)) {
      bool3 = bool4;
      if (i == 0) {
        bool3 = bool4;
        if (!hasPeekingDrawer())
          if (this.mChildrenCanceledTouch) {
            bool3 = bool4;
          } else {
            bool3 = false;
          }  
      } 
    } 
    return bool3;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && hasVisibleDrawer()) {
      paramKeyEvent.startTracking();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    View view;
    if (paramInt == 4) {
      boolean bool;
      view = findVisibleDrawer();
      if (view != null && getDrawerLockMode(view) == 0)
        closeDrawers(); 
      if (view != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    return super.onKeyUp(paramInt, (KeyEvent)view);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mInLayout = true;
    int i = paramInt3 - paramInt1;
    int j = getChildCount();
    for (paramInt3 = 0; paramInt3 < j; paramInt3++) {
      View view = getChildAt(paramInt3);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (isContentView(view)) {
          view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
        } else {
          float f;
          int k;
          boolean bool;
          int m;
          int i2;
          int n = view.getMeasuredWidth();
          int i1 = view.getMeasuredHeight();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            k = -n + (int)(n * layoutParams.onScreen);
            f = (n + k) / n;
          } else {
            k = i - (int)(n * layoutParams.onScreen);
            f = (i - k) / n;
          } 
          if (f != layoutParams.onScreen) {
            bool = true;
          } else {
            bool = false;
          } 
          switch (layoutParams.gravity & 0x70) {
            default:
              view.layout(k, layoutParams.topMargin, k + n, layoutParams.topMargin + i1);
              break;
            case 80:
              paramInt1 = paramInt4 - paramInt2;
              view.layout(k, paramInt1 - layoutParams.bottomMargin - view.getMeasuredHeight(), k + n, paramInt1 - layoutParams.bottomMargin);
              break;
            case 16:
              i2 = paramInt4 - paramInt2;
              m = (i2 - i1) / 2;
              if (m < layoutParams.topMargin) {
                paramInt1 = layoutParams.topMargin;
              } else {
                paramInt1 = m;
                if (m + i1 > i2 - layoutParams.bottomMargin)
                  paramInt1 = i2 - layoutParams.bottomMargin - i1; 
              } 
              view.layout(k, paramInt1, k + n, paramInt1 + i1);
              break;
          } 
          if (bool)
            setDrawerViewOffset(view, f); 
          if (layoutParams.onScreen > 0.0F) {
            paramInt1 = 0;
          } else {
            paramInt1 = 4;
          } 
          if (view.getVisibility() != paramInt1)
            view.setVisibility(paramInt1); 
        } 
      } 
    } 
    this.mInLayout = false;
    this.mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore #9
    //   6: iload_2
    //   7: invokestatic getMode : (I)I
    //   10: istore #13
    //   12: iload_1
    //   13: invokestatic getSize : (I)I
    //   16: istore #8
    //   18: iload_2
    //   19: invokestatic getSize : (I)I
    //   22: istore #12
    //   24: iload #9
    //   26: ldc_w 1073741824
    //   29: if_icmpne -> 56
    //   32: iload #9
    //   34: istore #7
    //   36: iload #13
    //   38: istore #5
    //   40: iload #8
    //   42: istore #11
    //   44: iload #12
    //   46: istore #10
    //   48: iload #13
    //   50: ldc_w 1073741824
    //   53: if_icmpeq -> 165
    //   56: aload_0
    //   57: invokevirtual isInEditMode : ()Z
    //   60: ifeq -> 828
    //   63: iload #9
    //   65: ldc_w -2147483648
    //   68: if_icmpne -> 79
    //   71: ldc_w 1073741824
    //   74: istore #6
    //   76: goto -> 98
    //   79: iload #9
    //   81: istore #6
    //   83: iload #9
    //   85: ifne -> 98
    //   88: ldc_w 1073741824
    //   91: istore #6
    //   93: sipush #300
    //   96: istore #8
    //   98: iload #13
    //   100: ldc_w -2147483648
    //   103: if_icmpne -> 126
    //   106: ldc_w 1073741824
    //   109: istore #5
    //   111: iload #6
    //   113: istore #7
    //   115: iload #8
    //   117: istore #11
    //   119: iload #12
    //   121: istore #10
    //   123: goto -> 165
    //   126: iload #6
    //   128: istore #7
    //   130: iload #13
    //   132: istore #5
    //   134: iload #8
    //   136: istore #11
    //   138: iload #12
    //   140: istore #10
    //   142: iload #13
    //   144: ifne -> 165
    //   147: ldc_w 1073741824
    //   150: istore #5
    //   152: sipush #300
    //   155: istore #10
    //   157: iload #8
    //   159: istore #11
    //   161: iload #6
    //   163: istore #7
    //   165: aload_0
    //   166: iload #11
    //   168: iload #10
    //   170: invokevirtual setMeasuredDimension : (II)V
    //   173: aload_0
    //   174: getfield mLastInsets : Ljava/lang/Object;
    //   177: ifnull -> 193
    //   180: aload_0
    //   181: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   184: ifeq -> 193
    //   187: iconst_1
    //   188: istore #9
    //   190: goto -> 196
    //   193: iconst_0
    //   194: istore #9
    //   196: aload_0
    //   197: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   200: istore #15
    //   202: iconst_0
    //   203: istore #8
    //   205: iconst_0
    //   206: istore #6
    //   208: aload_0
    //   209: invokevirtual getChildCount : ()I
    //   212: istore #14
    //   214: iconst_0
    //   215: istore #12
    //   217: iload #12
    //   219: iload #14
    //   221: if_icmpge -> 827
    //   224: aload_0
    //   225: iload #12
    //   227: invokevirtual getChildAt : (I)Landroid/view/View;
    //   230: astore #19
    //   232: aload #19
    //   234: invokevirtual getVisibility : ()I
    //   237: bipush #8
    //   239: if_icmpne -> 245
    //   242: goto -> 558
    //   245: aload #19
    //   247: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   250: checkcast androidx/drawerlayout/widget/DrawerLayout$LayoutParams
    //   253: astore #20
    //   255: iload #9
    //   257: ifeq -> 504
    //   260: aload #20
    //   262: getfield gravity : I
    //   265: iload #15
    //   267: invokestatic getAbsoluteGravity : (II)I
    //   270: istore #13
    //   272: aload #19
    //   274: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   277: ifeq -> 376
    //   280: getstatic android/os/Build$VERSION.SDK_INT : I
    //   283: bipush #21
    //   285: if_icmplt -> 373
    //   288: aload_0
    //   289: getfield mLastInsets : Ljava/lang/Object;
    //   292: checkcast android/view/WindowInsets
    //   295: astore #18
    //   297: iload #13
    //   299: iconst_3
    //   300: if_icmpne -> 329
    //   303: aload #18
    //   305: aload #18
    //   307: invokevirtual getSystemWindowInsetLeft : ()I
    //   310: aload #18
    //   312: invokevirtual getSystemWindowInsetTop : ()I
    //   315: iconst_0
    //   316: aload #18
    //   318: invokevirtual getSystemWindowInsetBottom : ()I
    //   321: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   324: astore #17
    //   326: goto -> 362
    //   329: aload #18
    //   331: astore #17
    //   333: iload #13
    //   335: iconst_5
    //   336: if_icmpne -> 362
    //   339: aload #18
    //   341: iconst_0
    //   342: aload #18
    //   344: invokevirtual getSystemWindowInsetTop : ()I
    //   347: aload #18
    //   349: invokevirtual getSystemWindowInsetRight : ()I
    //   352: aload #18
    //   354: invokevirtual getSystemWindowInsetBottom : ()I
    //   357: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   360: astore #17
    //   362: aload #19
    //   364: aload #17
    //   366: invokevirtual dispatchApplyWindowInsets : (Landroid/view/WindowInsets;)Landroid/view/WindowInsets;
    //   369: pop
    //   370: goto -> 504
    //   373: goto -> 504
    //   376: getstatic android/os/Build$VERSION.SDK_INT : I
    //   379: bipush #21
    //   381: if_icmplt -> 501
    //   384: aload_0
    //   385: getfield mLastInsets : Ljava/lang/Object;
    //   388: checkcast android/view/WindowInsets
    //   391: astore #18
    //   393: iload #13
    //   395: iconst_3
    //   396: if_icmpne -> 425
    //   399: aload #18
    //   401: aload #18
    //   403: invokevirtual getSystemWindowInsetLeft : ()I
    //   406: aload #18
    //   408: invokevirtual getSystemWindowInsetTop : ()I
    //   411: iconst_0
    //   412: aload #18
    //   414: invokevirtual getSystemWindowInsetBottom : ()I
    //   417: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   420: astore #17
    //   422: goto -> 458
    //   425: aload #18
    //   427: astore #17
    //   429: iload #13
    //   431: iconst_5
    //   432: if_icmpne -> 458
    //   435: aload #18
    //   437: iconst_0
    //   438: aload #18
    //   440: invokevirtual getSystemWindowInsetTop : ()I
    //   443: aload #18
    //   445: invokevirtual getSystemWindowInsetRight : ()I
    //   448: aload #18
    //   450: invokevirtual getSystemWindowInsetBottom : ()I
    //   453: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   456: astore #17
    //   458: aload #20
    //   460: aload #17
    //   462: invokevirtual getSystemWindowInsetLeft : ()I
    //   465: putfield leftMargin : I
    //   468: aload #20
    //   470: aload #17
    //   472: invokevirtual getSystemWindowInsetTop : ()I
    //   475: putfield topMargin : I
    //   478: aload #20
    //   480: aload #17
    //   482: invokevirtual getSystemWindowInsetRight : ()I
    //   485: putfield rightMargin : I
    //   488: aload #20
    //   490: aload #17
    //   492: invokevirtual getSystemWindowInsetBottom : ()I
    //   495: putfield bottomMargin : I
    //   498: goto -> 504
    //   501: goto -> 504
    //   504: aload_0
    //   505: aload #19
    //   507: invokevirtual isContentView : (Landroid/view/View;)Z
    //   510: ifeq -> 561
    //   513: aload #19
    //   515: iload #11
    //   517: aload #20
    //   519: getfield leftMargin : I
    //   522: isub
    //   523: aload #20
    //   525: getfield rightMargin : I
    //   528: isub
    //   529: ldc_w 1073741824
    //   532: invokestatic makeMeasureSpec : (II)I
    //   535: iload #10
    //   537: aload #20
    //   539: getfield topMargin : I
    //   542: isub
    //   543: aload #20
    //   545: getfield bottomMargin : I
    //   548: isub
    //   549: ldc_w 1073741824
    //   552: invokestatic makeMeasureSpec : (II)I
    //   555: invokevirtual measure : (II)V
    //   558: goto -> 769
    //   561: aload_0
    //   562: aload #19
    //   564: invokevirtual isDrawerView : (Landroid/view/View;)Z
    //   567: ifeq -> 775
    //   570: getstatic androidx/drawerlayout/widget/DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION : Z
    //   573: ifeq -> 601
    //   576: aload #19
    //   578: invokestatic getElevation : (Landroid/view/View;)F
    //   581: fstore #4
    //   583: aload_0
    //   584: getfield mDrawerElevation : F
    //   587: fstore_3
    //   588: fload #4
    //   590: fload_3
    //   591: fcmpl
    //   592: ifeq -> 601
    //   595: aload #19
    //   597: fload_3
    //   598: invokestatic setElevation : (Landroid/view/View;F)V
    //   601: aload_0
    //   602: aload #19
    //   604: invokevirtual getDrawerViewAbsoluteGravity : (Landroid/view/View;)I
    //   607: bipush #7
    //   609: iand
    //   610: istore #16
    //   612: iload #16
    //   614: iconst_3
    //   615: if_icmpne -> 624
    //   618: iconst_1
    //   619: istore #13
    //   621: goto -> 627
    //   624: iconst_0
    //   625: istore #13
    //   627: iload #13
    //   629: ifeq -> 637
    //   632: iload #8
    //   634: ifne -> 650
    //   637: iload #13
    //   639: ifne -> 705
    //   642: iload #6
    //   644: ifne -> 650
    //   647: goto -> 705
    //   650: new java/lang/IllegalStateException
    //   653: dup
    //   654: new java/lang/StringBuilder
    //   657: dup
    //   658: invokespecial <init> : ()V
    //   661: ldc_w 'Child drawer has absolute gravity '
    //   664: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   667: iload #16
    //   669: invokestatic gravityToString : (I)Ljava/lang/String;
    //   672: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   675: ldc_w ' but this '
    //   678: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   681: ldc 'DrawerLayout'
    //   683: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   686: ldc_w ' already has a '
    //   689: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   692: ldc_w 'drawer view along that edge'
    //   695: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   698: invokevirtual toString : ()Ljava/lang/String;
    //   701: invokespecial <init> : (Ljava/lang/String;)V
    //   704: athrow
    //   705: iload #13
    //   707: ifeq -> 716
    //   710: iconst_1
    //   711: istore #8
    //   713: goto -> 719
    //   716: iconst_1
    //   717: istore #6
    //   719: aload #19
    //   721: iload_1
    //   722: aload_0
    //   723: getfield mMinDrawerMargin : I
    //   726: aload #20
    //   728: getfield leftMargin : I
    //   731: iadd
    //   732: aload #20
    //   734: getfield rightMargin : I
    //   737: iadd
    //   738: aload #20
    //   740: getfield width : I
    //   743: invokestatic getChildMeasureSpec : (III)I
    //   746: iload_2
    //   747: aload #20
    //   749: getfield topMargin : I
    //   752: aload #20
    //   754: getfield bottomMargin : I
    //   757: iadd
    //   758: aload #20
    //   760: getfield height : I
    //   763: invokestatic getChildMeasureSpec : (III)I
    //   766: invokevirtual measure : (II)V
    //   769: iinc #12, 1
    //   772: goto -> 217
    //   775: new java/lang/IllegalStateException
    //   778: dup
    //   779: new java/lang/StringBuilder
    //   782: dup
    //   783: invokespecial <init> : ()V
    //   786: ldc_w 'Child '
    //   789: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   792: aload #19
    //   794: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   797: ldc_w ' at index '
    //   800: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   803: iload #12
    //   805: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   808: ldc_w ' does not have a valid layout_gravity - must be Gravity.LEFT, '
    //   811: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   814: ldc_w 'Gravity.RIGHT or Gravity.NO_GRAVITY'
    //   817: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   820: invokevirtual toString : ()Ljava/lang/String;
    //   823: invokespecial <init> : (Ljava/lang/String;)V
    //   826: athrow
    //   827: return
    //   828: new java/lang/IllegalArgumentException
    //   831: dup
    //   832: ldc_w 'DrawerLayout must be measured with MeasureSpec.EXACTLY.'
    //   835: invokespecial <init> : (Ljava/lang/String;)V
    //   838: athrow
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.openDrawerGravity != 0) {
      View view = findDrawerWithGravity(savedState.openDrawerGravity);
      if (view != null)
        openDrawer(view); 
    } 
    if (savedState.lockModeLeft != 3)
      setDrawerLockMode(savedState.lockModeLeft, 3); 
    if (savedState.lockModeRight != 3)
      setDrawerLockMode(savedState.lockModeRight, 5); 
    if (savedState.lockModeStart != 3)
      setDrawerLockMode(savedState.lockModeStart, 8388611); 
    if (savedState.lockModeEnd != 3)
      setDrawerLockMode(savedState.lockModeEnd, 8388613); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    resolveShadowDrawables();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.openState;
      boolean bool = false;
      if (j == 1) {
        j = 1;
      } else {
        j = 0;
      } 
      if (layoutParams.openState == 2)
        bool = true; 
      if (j != 0 || bool) {
        savedState.openDrawerGravity = layoutParams.gravity;
        break;
      } 
    } 
    savedState.lockModeLeft = this.mLockModeLeft;
    savedState.lockModeRight = this.mLockModeRight;
    savedState.lockModeStart = this.mLockModeStart;
    savedState.lockModeEnd = this.mLockModeEnd;
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    View view;
    boolean bool1;
    boolean bool3;
    this.mLeftDragger.processTouchEvent(paramMotionEvent);
    this.mRightDragger.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    boolean bool2 = true;
    switch (i & 0xFF) {
      default:
        return true;
      case 3:
        closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
      case 1:
        f2 = paramMotionEvent.getX();
        f1 = paramMotionEvent.getY();
        bool3 = true;
        view = this.mLeftDragger.findTopChildUnder((int)f2, (int)f1);
        bool1 = bool3;
        if (view != null) {
          bool1 = bool3;
          if (isContentView(view)) {
            f2 -= this.mInitialMotionX;
            f1 -= this.mInitialMotionY;
            i = this.mLeftDragger.getTouchSlop();
            bool1 = bool3;
            if (f2 * f2 + f1 * f1 < (i * i)) {
              view = findOpenDrawer();
              bool1 = bool3;
              if (view != null)
                if (getDrawerLockMode(view) == 2) {
                  bool1 = bool2;
                } else {
                  bool1 = false;
                }  
            } 
          } 
        } 
        closeDrawers(bool1);
        this.mDisallowInterceptRequested = false;
      case 0:
        break;
    } 
    float f1 = view.getX();
    float f2 = view.getY();
    this.mInitialMotionX = f1;
    this.mInitialMotionY = f2;
    this.mDisallowInterceptRequested = false;
    this.mChildrenCanceledTouch = false;
  }
  
  public void openDrawer(int paramInt) {
    openDrawer(paramInt, true);
  }
  
  public void openDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      openDrawer(view, paramBoolean);
      return;
    } 
    throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(paramInt));
  }
  
  public void openDrawer(View paramView) {
    openDrawer(paramView, true);
  }
  
  public void openDrawer(View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 1.0F;
        layoutParams.openState = 1;
        updateChildrenImportantForAccessibility(paramView, true);
      } else if (paramBoolean) {
        layoutParams.openState |= 0x2;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, 0, paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth() - paramView.getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 1.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(0);
      } 
      invalidate();
      return;
    } 
    throw new IllegalArgumentException("View " + paramView + " is not a sliding drawer");
  }
  
  public void removeDrawerListener(DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    List<DrawerListener> list = this.mListeners;
    if (list == null)
      return; 
    list.remove(paramDrawerListener);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    this.mDisallowInterceptRequested = paramBoolean;
    if (paramBoolean)
      closeDrawers(true); 
  }
  
  public void requestLayout() {
    if (!this.mInLayout)
      super.requestLayout(); 
  }
  
  public void setChildInsets(Object paramObject, boolean paramBoolean) {
    this.mLastInsets = paramObject;
    this.mDrawStatusBarBackground = paramBoolean;
    if (!paramBoolean && getBackground() == null) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    setWillNotDraw(paramBoolean);
    requestLayout();
  }
  
  public void setDrawerElevation(float paramFloat) {
    this.mDrawerElevation = paramFloat;
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (isDrawerView(view))
        ViewCompat.setElevation(view, this.mDrawerElevation); 
    } 
  }
  
  @Deprecated
  public void setDrawerListener(DrawerListener paramDrawerListener) {
    DrawerListener drawerListener = this.mListener;
    if (drawerListener != null)
      removeDrawerListener(drawerListener); 
    if (paramDrawerListener != null)
      addDrawerListener(paramDrawerListener); 
    this.mListener = paramDrawerListener;
  }
  
  public void setDrawerLockMode(int paramInt) {
    setDrawerLockMode(paramInt, 3);
    setDrawerLockMode(paramInt, 5);
  }
  
  public void setDrawerLockMode(int paramInt1, int paramInt2) {
    int i = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this));
    switch (paramInt2) {
      case 8388613:
        this.mLockModeEnd = paramInt1;
        break;
      case 8388611:
        this.mLockModeStart = paramInt1;
        break;
      case 5:
        this.mLockModeRight = paramInt1;
        break;
      case 3:
        this.mLockModeLeft = paramInt1;
        break;
    } 
    if (paramInt1 != 0) {
      ViewDragHelper viewDragHelper;
      if (i == 3) {
        viewDragHelper = this.mLeftDragger;
      } else {
        viewDragHelper = this.mRightDragger;
      } 
      viewDragHelper.cancel();
    } 
    switch (paramInt1) {
      default:
        return;
      case 2:
        view = findDrawerWithGravity(i);
        if (view != null)
          openDrawer(view); 
      case 1:
        break;
    } 
    View view = findDrawerWithGravity(i);
    if (view != null)
      closeDrawer(view); 
  }
  
  public void setDrawerLockMode(int paramInt, View paramView) {
    if (isDrawerView(paramView)) {
      setDrawerLockMode(paramInt, ((LayoutParams)paramView.getLayoutParams()).gravity);
      return;
    } 
    throw new IllegalArgumentException("View " + paramView + " is not a " + "drawer with appropriate layout_gravity");
  }
  
  public void setDrawerShadow(int paramInt1, int paramInt2) {
    setDrawerShadow(ContextCompat.getDrawable(getContext(), paramInt1), paramInt2);
  }
  
  public void setDrawerShadow(Drawable paramDrawable, int paramInt) {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    if ((paramInt & 0x800003) == 8388611) {
      this.mShadowStart = paramDrawable;
    } else if ((paramInt & 0x800005) == 8388613) {
      this.mShadowEnd = paramDrawable;
    } else if ((paramInt & 0x3) == 3) {
      this.mShadowLeft = paramDrawable;
    } else if ((paramInt & 0x5) == 5) {
      this.mShadowRight = paramDrawable;
    } else {
      return;
    } 
    resolveShadowDrawables();
    invalidate();
  }
  
  public void setDrawerTitle(int paramInt, CharSequence paramCharSequence) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    if (paramInt == 3) {
      this.mTitleLeft = paramCharSequence;
    } else if (paramInt == 5) {
      this.mTitleRight = paramCharSequence;
    } 
  }
  
  void setDrawerViewOffset(View paramView, float paramFloat) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat == layoutParams.onScreen)
      return; 
    layoutParams.onScreen = paramFloat;
    dispatchOnDrawerSlide(paramView, paramFloat);
  }
  
  public void setScrimColor(int paramInt) {
    this.mScrimColor = paramInt;
    invalidate();
  }
  
  public void setStatusBarBackground(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    this.mStatusBarBackground = drawable;
    invalidate();
  }
  
  public void setStatusBarBackground(Drawable paramDrawable) {
    this.mStatusBarBackground = paramDrawable;
    invalidate();
  }
  
  public void setStatusBarBackgroundColor(int paramInt) {
    this.mStatusBarBackground = (Drawable)new ColorDrawable(paramInt);
    invalidate();
  }
  
  void updateDrawerState(int paramInt1, int paramInt2, View paramView) {
    int i = this.mLeftDragger.getViewDragState();
    paramInt1 = this.mRightDragger.getViewDragState();
    if (i == 1 || paramInt1 == 1) {
      paramInt1 = 1;
    } else if (i == 2 || paramInt1 == 2) {
      paramInt1 = 2;
    } else {
      paramInt1 = 0;
    } 
    if (paramView != null && paramInt2 == 0) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (layoutParams.onScreen == 0.0F) {
        dispatchOnDrawerClosed(paramView);
      } else if (layoutParams.onScreen == 1.0F) {
        dispatchOnDrawerOpened(paramView);
      } 
    } 
    if (paramInt1 != this.mDrawerState) {
      this.mDrawerState = paramInt1;
      List<DrawerListener> list = this.mListeners;
      if (list != null)
        for (paramInt2 = list.size() - 1; paramInt2 >= 0; paramInt2--)
          ((DrawerListener)this.mListeners.get(paramInt2)).onDrawerStateChanged(paramInt1);  
    } 
  }
  
  static {
    boolean bool1;
    boolean bool2 = true;
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    final DrawerLayout this$0;
    
    private void addChildrenForAccessibility(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat, ViewGroup param1ViewGroup) {
      int i = param1ViewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = param1ViewGroup.getChildAt(b);
        if (DrawerLayout.includeChildForAccessibility(view))
          param1AccessibilityNodeInfoCompat.addChild(view); 
      } 
    }
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      List<CharSequence> list;
      CharSequence charSequence;
      if (param1AccessibilityEvent.getEventType() == 32) {
        list = param1AccessibilityEvent.getText();
        View view = DrawerLayout.this.findVisibleDrawer();
        if (view != null) {
          int i = DrawerLayout.this.getDrawerViewAbsoluteGravity(view);
          charSequence = DrawerLayout.this.getDrawerTitle(i);
          if (charSequence != null)
            list.add(charSequence); 
        } 
        return true;
      } 
      return super.dispatchPopulateAccessibilityEvent((View)list, (AccessibilityEvent)charSequence);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(DrawerLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      } else {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
        super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
        param1AccessibilityNodeInfoCompat.setSource(param1View);
        ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
        if (viewParent instanceof View)
          param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
        copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.recycle();
        addChildrenForAccessibility(param1AccessibilityNodeInfoCompat, (ViewGroup)param1View);
      } 
      param1AccessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setFocusable(false);
      param1AccessibilityNodeInfoCompat.setFocused(false);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(param1View)) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      if (!DrawerLayout.includeChildForAccessibility(param1View))
        param1AccessibilityNodeInfoCompat.setParent(null); 
    }
  }
  
  public static interface DrawerListener {
    void onDrawerClosed(View param1View);
    
    void onDrawerOpened(View param1View);
    
    void onDrawerSlide(View param1View, float param1Float);
    
    void onDrawerStateChanged(int param1Int);
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int FLAG_IS_CLOSING = 4;
    
    private static final int FLAG_IS_OPENED = 1;
    
    private static final int FLAG_IS_OPENING = 2;
    
    public int gravity = 0;
    
    boolean isPeeking;
    
    float onScreen;
    
    int openState;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, DrawerLayout.LAYOUT_ATTRS);
      this.gravity = typedArray.getInt(0, 0);
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.gravity = param1LayoutParams.gravity;
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new DrawerLayout.SavedState(param2Parcel, null);
        }
        
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new DrawerLayout.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public DrawerLayout.SavedState[] newArray(int param2Int) {
          return new DrawerLayout.SavedState[param2Int];
        }
      };
    
    int lockModeEnd;
    
    int lockModeLeft;
    
    int lockModeRight;
    
    int lockModeStart;
    
    int openDrawerGravity = 0;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.openDrawerGravity = param1Parcel.readInt();
      this.lockModeLeft = param1Parcel.readInt();
      this.lockModeRight = param1Parcel.readInt();
      this.lockModeStart = param1Parcel.readInt();
      this.lockModeEnd = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.openDrawerGravity);
      param1Parcel.writeInt(this.lockModeLeft);
      param1Parcel.writeInt(this.lockModeRight);
      param1Parcel.writeInt(this.lockModeStart);
      param1Parcel.writeInt(this.lockModeEnd);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new DrawerLayout.SavedState(param1Parcel, null);
    }
    
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new DrawerLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public DrawerLayout.SavedState[] newArray(int param1Int) {
      return new DrawerLayout.SavedState[param1Int];
    }
  }
  
  public static abstract class SimpleDrawerListener implements DrawerListener {
    public void onDrawerClosed(View param1View) {}
    
    public void onDrawerOpened(View param1View) {}
    
    public void onDrawerSlide(View param1View, float param1Float) {}
    
    public void onDrawerStateChanged(int param1Int) {}
  }
  
  private class ViewDragCallback extends ViewDragHelper.Callback {
    private final int mAbsGravity;
    
    private ViewDragHelper mDragger;
    
    private final Runnable mPeekRunnable = new Runnable() {
        final DrawerLayout.ViewDragCallback this$1;
        
        public void run() {
          DrawerLayout.ViewDragCallback.this.peekDrawer();
        }
      };
    
    final DrawerLayout this$0;
    
    ViewDragCallback(int param1Int) {
      this.mAbsGravity = param1Int;
    }
    
    private void closeOtherDrawer() {
      int i = this.mAbsGravity;
      byte b = 3;
      if (i == 3)
        b = 5; 
      View view = DrawerLayout.this.findDrawerWithGravity(b);
      if (view != null)
        DrawerLayout.this.closeDrawer(view); 
    }
    
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3))
        return Math.max(-param1View.getWidth(), Math.min(param1Int1, 0)); 
      param1Int2 = DrawerLayout.this.getWidth();
      return Math.max(param1Int2 - param1View.getWidth(), Math.min(param1Int1, param1Int2));
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      boolean bool;
      if (DrawerLayout.this.isDrawerView(param1View)) {
        bool = param1View.getWidth();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      View view;
      if ((param1Int1 & 0x1) == 1) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
      } 
      if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0)
        this.mDragger.captureChildView(view, param1Int2); 
    }
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {
      DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      ((DrawerLayout.LayoutParams)param1View.getLayoutParams()).isPeeking = false;
      closeOtherDrawer();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      DrawerLayout.this.updateDrawerState(this.mAbsGravity, param1Int, this.mDragger.getCapturedView());
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      float f;
      param1Int2 = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        f = (param1Int2 + param1Int1) / param1Int2;
      } else {
        f = (DrawerLayout.this.getWidth() - param1Int1) / param1Int2;
      } 
      DrawerLayout.this.setDrawerViewOffset(param1View, f);
      if (f == 0.0F) {
        param1Int1 = 4;
      } else {
        param1Int1 = 0;
      } 
      param1View.setVisibility(param1Int1);
      DrawerLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      int i;
      param1Float2 = DrawerLayout.this.getDrawerViewOffset(param1View);
      int j = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        if (param1Float1 > 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F)) {
          i = 0;
        } else {
          i = -j;
        } 
      } else {
        i = DrawerLayout.this.getWidth();
        if (param1Float1 < 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F))
          i -= j; 
      } 
      this.mDragger.settleCapturedViewAt(i, param1View.getTop());
      DrawerLayout.this.invalidate();
    }
    
    void peekDrawer() {
      View view;
      int k = this.mDragger.getEdgeSize();
      int i = this.mAbsGravity;
      int j = 0;
      if (i == 3) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i != 0) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
        if (view != null)
          j = -view.getWidth(); 
        j += k;
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
        j = DrawerLayout.this.getWidth() - k;
      } 
      if (view != null && ((i != 0 && view.getLeft() < j) || (i == 0 && view.getLeft() > j)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams)view.getLayoutParams();
        this.mDragger.smoothSlideViewTo(view, j, view.getTop());
        layoutParams.isPeeking = true;
        DrawerLayout.this.invalidate();
        closeOtherDrawer();
        DrawerLayout.this.cancelChildViewTouch();
      } 
    }
    
    public void removeCallbacks() {
      DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
    }
    
    public void setDragger(ViewDragHelper param1ViewDragHelper) {
      this.mDragger = param1ViewDragHelper;
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      boolean bool;
      if (DrawerLayout.this.isDrawerView(param1View) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(param1View) == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null implements Runnable {
    final DrawerLayout.ViewDragCallback this$1;
    
    public void run() {
      this.this$1.peekDrawer();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\drawerlayout\widget\DrawerLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */