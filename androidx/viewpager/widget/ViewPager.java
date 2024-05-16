package androidx.viewpager.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
  private static final int CLOSE_ENOUGH = 2;
  
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_GUTTER_SIZE = 16;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  
  private static final int DRAW_ORDER_DEFAULT = 0;
  
  private static final int DRAW_ORDER_FORWARD = 1;
  
  private static final int DRAW_ORDER_REVERSE = 2;
  
  private static final int INVALID_POINTER = -1;
  
  static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "ViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private static final ViewPositionComparator sPositionComparator;
  
  private int mActivePointerId = -1;
  
  PagerAdapter mAdapter;
  
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCloseEnough;
  
  int mCurItem;
  
  private int mDecorChildCount;
  
  private int mDefaultGutterSize;
  
  private int mDrawingOrder;
  
  private ArrayList<View> mDrawingOrderedChildren;
  
  private final Runnable mEndScrollRunnable = new Runnable() {
      final ViewPager this$0;
      
      public void run() {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
  
  private int mExpectedAdapterCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private float mFirstOffset = -3.4028235E38F;
  
  private int mFlingDistance;
  
  private int mGutterSize;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsScrollStarted;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList<>();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private float mLastOffset = Float.MAX_VALUE;
  
  private EdgeEffect mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private boolean mNeedCalculatePageOffsets = false;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 1;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private List<OnPageChangeListener> mOnPageChangeListeners;
  
  private int mPageMargin;
  
  private PageTransformer mPageTransformer;
  
  private int mPageTransformerLayerType;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffect mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrollingCacheEnabled;
  
  private final ItemInfo mTempItem = new ItemInfo();
  
  private final Rect mTempRect = new Rect();
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo param1ItemInfo1, ViewPager.ItemInfo param1ItemInfo2) {
          return param1ItemInfo1.position - param1ItemInfo2.position;
        }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
    sPositionComparator = new ViewPositionComparator();
  }
  
  public ViewPager(Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2) {
    float f2;
    int m = this.mAdapter.getCount();
    int i = getClientWidth();
    if (i > 0) {
      f2 = this.mPageMargin / i;
    } else {
      f2 = 0.0F;
    } 
    if (paramItemInfo2 != null) {
      i = paramItemInfo2.position;
      if (i < paramItemInfo1.position) {
        byte b = 0;
        f1 = paramItemInfo2.offset + paramItemInfo2.widthFactor + f2;
        while (++i <= paramItemInfo1.position && b < this.mItems.size()) {
          float f;
          int n;
          paramItemInfo2 = this.mItems.get(b);
          while (true) {
            f = f1;
            n = i;
            if (i > paramItemInfo2.position) {
              f = f1;
              n = i;
              if (b < this.mItems.size() - 1) {
                paramItemInfo2 = this.mItems.get(++b);
                continue;
              } 
            } 
            break;
          } 
          while (n < paramItemInfo2.position) {
            f += this.mAdapter.getPageWidth(n) + f2;
            n++;
          } 
          paramItemInfo2.offset = f;
          f1 = f + paramItemInfo2.widthFactor + f2;
          i = n + 1;
        } 
      } else if (i > paramItemInfo1.position) {
        int n = this.mItems.size() - 1;
        f1 = paramItemInfo2.offset;
        while (--i >= paramItemInfo1.position && n >= 0) {
          float f;
          int i1;
          paramItemInfo2 = this.mItems.get(n);
          while (true) {
            f = f1;
            i1 = i;
            if (i < paramItemInfo2.position) {
              f = f1;
              i1 = i;
              if (n > 0) {
                paramItemInfo2 = this.mItems.get(--n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 > paramItemInfo2.position) {
            f -= this.mAdapter.getPageWidth(i1) + f2;
            i1--;
          } 
          f1 = f - paramItemInfo2.widthFactor + f2;
          paramItemInfo2.offset = f1;
          i = i1 - 1;
        } 
      } 
    } 
    int k = this.mItems.size();
    float f3 = paramItemInfo1.offset;
    i = paramItemInfo1.position - 1;
    if (paramItemInfo1.position == 0) {
      f1 = paramItemInfo1.offset;
    } else {
      f1 = -3.4028235E38F;
    } 
    this.mFirstOffset = f1;
    if (paramItemInfo1.position == m - 1) {
      f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    } else {
      f1 = Float.MAX_VALUE;
    } 
    this.mLastOffset = f1;
    int j = paramInt - 1;
    float f1 = f3;
    while (j >= 0) {
      paramItemInfo2 = this.mItems.get(j);
      while (i > paramItemInfo2.position) {
        f1 -= this.mAdapter.getPageWidth(i) + f2;
        i--;
      } 
      f1 -= paramItemInfo2.widthFactor + f2;
      paramItemInfo2.offset = f1;
      if (paramItemInfo2.position == 0)
        this.mFirstOffset = f1; 
      j--;
      i--;
    } 
    f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor + f2;
    j = paramItemInfo1.position + 1;
    i = paramInt + 1;
    for (paramInt = j; i < k; paramInt++) {
      paramItemInfo1 = this.mItems.get(i);
      while (paramInt < paramItemInfo1.position) {
        f1 += this.mAdapter.getPageWidth(paramInt) + f2;
        paramInt++;
      } 
      if (paramItemInfo1.position == m - 1)
        this.mLastOffset = paramItemInfo1.widthFactor + f1 - 1.0F; 
      paramItemInfo1.offset = f1;
      f1 += paramItemInfo1.widthFactor + f2;
      i++;
    } 
    this.mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll(boolean paramBoolean) {
    boolean bool;
    if (this.mScrollState == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      setScrollingCacheEnabled(false);
      if ((true ^ this.mScroller.isFinished()) != 0) {
        this.mScroller.abortAnimation();
        int m = getScrollX();
        int j = getScrollY();
        int i = this.mScroller.getCurrX();
        int k = this.mScroller.getCurrY();
        if (m != i || j != k) {
          scrollTo(i, k);
          if (i != m)
            pageScrolled(i); 
        } 
      } 
    } 
    this.mPopulatePending = false;
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (itemInfo.scrolling) {
        bool = true;
        itemInfo.scrolling = false;
      } 
    } 
    if (bool)
      if (paramBoolean) {
        ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
      } else {
        this.mEndScrollRunnable.run();
      }  
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
    } else {
      float f;
      if (paramInt1 >= this.mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      } 
      paramInt1 = (int)(paramFloat + f) + paramInt1;
    } 
    paramInt2 = paramInt1;
    if (this.mItems.size() > 0) {
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      paramInt2 = Math.max(itemInfo1.position, Math.min(paramInt1, itemInfo2.position));
    } 
    return paramInt2;
  }
  
  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchOnPageSelected(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageSelected(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageSelected(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageSelected(paramInt); 
  }
  
  private void dispatchOnScrollStateChanged(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrollStateChanged(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrollStateChanged(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrollStateChanged(paramInt); 
  }
  
  private void enableLayers(boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      boolean bool;
      if (paramBoolean) {
        bool = this.mPageTransformerLayerType;
      } else {
        bool = false;
      } 
      getChildAt(b).setLayerType(bool, null);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = new Rect(); 
    if (paramView == null) {
      rect.set(0, 0, 0, 0);
      return rect;
    } 
    rect.left = paramView.getLeft();
    rect.right = paramView.getRight();
    rect.top = paramView.getTop();
    rect.bottom = paramView.getBottom();
    ViewParent viewParent = paramView.getParent();
    while (viewParent instanceof ViewGroup && viewParent != this) {
      ViewGroup viewGroup = (ViewGroup)viewParent;
      rect.left += viewGroup.getLeft();
      rect.right += viewGroup.getRight();
      rect.top += viewGroup.getTop();
      rect.bottom += viewGroup.getBottom();
      ViewParent viewParent1 = viewGroup.getParent();
    } 
    return rect;
  }
  
  private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private ItemInfo infoForCurrentScrollPosition() {
    float f1;
    int i = getClientWidth();
    float f2 = 0.0F;
    if (i > 0) {
      f1 = getScrollX() / i;
    } else {
      f1 = 0.0F;
    } 
    if (i > 0)
      f2 = this.mPageMargin / i; 
    int j = -1;
    float f3 = 0.0F;
    float f4 = 0.0F;
    boolean bool = true;
    ItemInfo itemInfo = null;
    i = 0;
    while (i < this.mItems.size()) {
      ItemInfo itemInfo2 = this.mItems.get(i);
      int k = i;
      ItemInfo itemInfo1 = itemInfo2;
      if (!bool) {
        k = i;
        itemInfo1 = itemInfo2;
        if (itemInfo2.position != j + 1) {
          itemInfo1 = this.mTempItem;
          itemInfo1.offset = f3 + f4 + f2;
          itemInfo1.position = j + 1;
          itemInfo1.widthFactor = this.mAdapter.getPageWidth(itemInfo1.position);
          k = i - 1;
        } 
      } 
      f3 = itemInfo1.offset;
      f4 = itemInfo1.widthFactor;
      if (bool || f1 >= f3) {
        if (f1 < f4 + f3 + f2 || k == this.mItems.size() - 1)
          return itemInfo1; 
        bool = false;
        j = itemInfo1.position;
        f4 = itemInfo1.widthFactor;
        i = k + 1;
        itemInfo = itemInfo1;
        continue;
      } 
      return itemInfo;
    } 
    return itemInfo;
  }
  
  private static boolean isDecorView(View paramView) {
    boolean bool;
    if (paramView.getClass().getAnnotation(DecorView.class) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2) {
    boolean bool;
    if ((paramFloat1 < this.mGutterSize && paramFloat2 > 0.0F) || (paramFloat1 > (getWidth() - this.mGutterSize) && paramFloat2 < 0.0F)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private boolean pageScrolled(int paramInt) {
    if (this.mItems.size() == 0) {
      if (this.mFirstLayout)
        return false; 
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (this.mCalledSuper)
        return false; 
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    } 
    ItemInfo itemInfo = infoForCurrentScrollPosition();
    int k = getClientWidth();
    int j = this.mPageMargin;
    float f = j / k;
    int i = itemInfo.position;
    f = (paramInt / k - itemInfo.offset) / (itemInfo.widthFactor + f);
    paramInt = (int)((k + j) * f);
    this.mCalledSuper = false;
    onPageScrolled(i, f, paramInt);
    if (this.mCalledSuper)
      return true; 
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat) {
    boolean bool4 = false;
    boolean bool3 = false;
    boolean bool5 = false;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + f1 - paramFloat;
    int i = getClientWidth();
    paramFloat = i * this.mFirstOffset;
    f1 = i * this.mLastOffset;
    boolean bool1 = true;
    boolean bool2 = true;
    ItemInfo itemInfo1 = this.mItems.get(0);
    ArrayList<ItemInfo> arrayList = this.mItems;
    ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
    if (itemInfo1.position != 0) {
      bool1 = false;
      paramFloat = itemInfo1.offset * i;
    } 
    if (itemInfo2.position != this.mAdapter.getCount() - 1) {
      bool2 = false;
      f1 = itemInfo2.offset * i;
    } 
    if (f2 < paramFloat) {
      bool3 = bool5;
      if (bool1) {
        this.mLeftEdge.onPull(Math.abs(paramFloat - f2) / i);
        bool3 = true;
      } 
    } else {
      paramFloat = f2;
      if (f2 > f1) {
        bool3 = bool4;
        if (bool2) {
          this.mRightEdge.onPull(Math.abs(f2 - f1) / i);
          bool3 = true;
        } 
        paramFloat = f1;
      } 
    } 
    this.mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    return bool3;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt2 > 0 && !this.mItems.isEmpty()) {
      if (!this.mScroller.isFinished()) {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
      } else {
        int i = getPaddingLeft();
        int j = getPaddingRight();
        int m = getPaddingLeft();
        int k = getPaddingRight();
        float f = getScrollX() / (paramInt2 - m - k + paramInt4);
        scrollTo((int)((paramInt1 - i - j + paramInt3) * f), getScrollY());
      } 
    } else {
      float f;
      ItemInfo itemInfo = infoForPosition(this.mCurItem);
      if (itemInfo != null) {
        f = Math.min(itemInfo.offset, this.mLastOffset);
      } else {
        f = 0.0F;
      } 
      paramInt1 = (int)((paramInt1 - getPaddingLeft() - getPaddingRight()) * f);
      if (paramInt1 != getScrollX()) {
        completeScroll(false);
        scrollTo(paramInt1, getScrollY());
      } 
    } 
  }
  
  private void removeNonDecorViews() {
    for (int i = 0; i < getChildCount(); i = j + 1) {
      int j = i;
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor) {
        removeViewAt(i);
        j = i - 1;
      } 
    } 
  }
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean) {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  private boolean resetTouch() {
    this.mActivePointerId = -1;
    endDrag();
    this.mLeftEdge.onRelease();
    this.mRightEdge.onRelease();
    return (this.mLeftEdge.isFinished() || this.mRightEdge.isFinished());
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    ItemInfo itemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (itemInfo != null)
      i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset))); 
    if (paramBoolean1) {
      smoothScrollTo(i, 0, paramInt2);
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
    } else {
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
      completeScroll(false);
      scrollTo(i, 0);
      pageScrolled(i);
    } 
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void sortChildDrawingOrder() {
    if (this.mDrawingOrder != 0) {
      ArrayList<View> arrayList = this.mDrawingOrderedChildren;
      if (arrayList == null) {
        this.mDrawingOrderedChildren = new ArrayList<>();
      } else {
        arrayList.clear();
      } 
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        this.mDrawingOrderedChildren.add(view);
      } 
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    } 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
      for (byte b = 0; b < getChildCount(); b++) {
        View view = getChildAt(b);
        if (view.getVisibility() == 0) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null && itemInfo.position == this.mCurItem)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      }  
    if (j != 262144 || i == paramArrayList.size()) {
      if (!isFocusable())
        return; 
      if ((paramInt2 & 0x1) == 1 && isInTouchMode() && !isFocusableInTouchMode())
        return; 
      if (paramArrayList != null)
        paramArrayList.add(this); 
    } 
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    itemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if (paramInt2 < 0 || paramInt2 >= this.mItems.size()) {
      this.mItems.add(itemInfo);
      return itemInfo;
    } 
    this.mItems.add(paramInt2, itemInfo);
    return itemInfo;
  }
  
  public void addOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList<>(); 
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }
  
  public void addOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList<>(); 
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
  }
  
  public void addTouchables(ArrayList<View> paramArrayList) {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem)
          view.addTouchables(paramArrayList); 
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    ViewGroup.LayoutParams layoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams))
      layoutParams = generateLayoutParams(paramLayoutParams); 
    paramLayoutParams = layoutParams;
    ((LayoutParams)paramLayoutParams).isDecor |= isDecorView(paramView);
    if (this.mInLayout) {
      if (paramLayoutParams == null || !((LayoutParams)paramLayoutParams).isDecor) {
        ((LayoutParams)paramLayoutParams).needsMeasure = true;
        addViewInLayout(paramView, paramInt, layoutParams);
        return;
      } 
      throw new IllegalStateException("Cannot add pager decor view during layout");
    } 
    super.addView(paramView, paramInt, layoutParams);
  }
  
  public boolean arrowScroll(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual findFocus : ()Landroid/view/View;
    //   4: astore #7
    //   6: aload #7
    //   8: aload_0
    //   9: if_acmpne -> 18
    //   12: aconst_null
    //   13: astore #6
    //   15: goto -> 180
    //   18: aload #7
    //   20: astore #6
    //   22: aload #7
    //   24: ifnull -> 180
    //   27: iconst_0
    //   28: istore_3
    //   29: aload #7
    //   31: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   34: astore #6
    //   36: iload_3
    //   37: istore_2
    //   38: aload #6
    //   40: instanceof android/view/ViewGroup
    //   43: ifeq -> 69
    //   46: aload #6
    //   48: aload_0
    //   49: if_acmpne -> 57
    //   52: iconst_1
    //   53: istore_2
    //   54: goto -> 69
    //   57: aload #6
    //   59: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   64: astore #6
    //   66: goto -> 36
    //   69: aload #7
    //   71: astore #6
    //   73: iload_2
    //   74: ifne -> 180
    //   77: new java/lang/StringBuilder
    //   80: dup
    //   81: invokespecial <init> : ()V
    //   84: astore #8
    //   86: aload #8
    //   88: aload #7
    //   90: invokevirtual getClass : ()Ljava/lang/Class;
    //   93: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload #7
    //   102: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   105: astore #6
    //   107: aload #6
    //   109: instanceof android/view/ViewGroup
    //   112: ifeq -> 147
    //   115: aload #8
    //   117: ldc_w ' => '
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: aload #6
    //   125: invokevirtual getClass : ()Ljava/lang/Class;
    //   128: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: pop
    //   135: aload #6
    //   137: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   142: astore #6
    //   144: goto -> 107
    //   147: ldc 'ViewPager'
    //   149: new java/lang/StringBuilder
    //   152: dup
    //   153: invokespecial <init> : ()V
    //   156: ldc_w 'arrowScroll tried to find focus based on non-child current focused view '
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: aload #8
    //   164: invokevirtual toString : ()Ljava/lang/String;
    //   167: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: invokevirtual toString : ()Ljava/lang/String;
    //   173: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   176: pop
    //   177: aconst_null
    //   178: astore #6
    //   180: iconst_0
    //   181: istore #5
    //   183: iconst_0
    //   184: istore #4
    //   186: invokestatic getInstance : ()Landroid/view/FocusFinder;
    //   189: aload_0
    //   190: aload #6
    //   192: iload_1
    //   193: invokevirtual findNextFocus : (Landroid/view/ViewGroup;Landroid/view/View;I)Landroid/view/View;
    //   196: astore #7
    //   198: aload #7
    //   200: ifnull -> 336
    //   203: aload #7
    //   205: aload #6
    //   207: if_acmpeq -> 336
    //   210: iload_1
    //   211: bipush #17
    //   213: if_icmpne -> 273
    //   216: aload_0
    //   217: aload_0
    //   218: getfield mTempRect : Landroid/graphics/Rect;
    //   221: aload #7
    //   223: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   226: getfield left : I
    //   229: istore_3
    //   230: aload_0
    //   231: aload_0
    //   232: getfield mTempRect : Landroid/graphics/Rect;
    //   235: aload #6
    //   237: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   240: getfield left : I
    //   243: istore_2
    //   244: aload #6
    //   246: ifnull -> 263
    //   249: iload_3
    //   250: iload_2
    //   251: if_icmplt -> 263
    //   254: aload_0
    //   255: invokevirtual pageLeft : ()Z
    //   258: istore #4
    //   260: goto -> 270
    //   263: aload #7
    //   265: invokevirtual requestFocus : ()Z
    //   268: istore #4
    //   270: goto -> 380
    //   273: iload_1
    //   274: bipush #66
    //   276: if_icmpne -> 270
    //   279: aload_0
    //   280: aload_0
    //   281: getfield mTempRect : Landroid/graphics/Rect;
    //   284: aload #7
    //   286: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   289: getfield left : I
    //   292: istore_2
    //   293: aload_0
    //   294: aload_0
    //   295: getfield mTempRect : Landroid/graphics/Rect;
    //   298: aload #6
    //   300: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   303: getfield left : I
    //   306: istore_3
    //   307: aload #6
    //   309: ifnull -> 326
    //   312: iload_2
    //   313: iload_3
    //   314: if_icmpgt -> 326
    //   317: aload_0
    //   318: invokevirtual pageRight : ()Z
    //   321: istore #4
    //   323: goto -> 333
    //   326: aload #7
    //   328: invokevirtual requestFocus : ()Z
    //   331: istore #4
    //   333: goto -> 380
    //   336: iload_1
    //   337: bipush #17
    //   339: if_icmpeq -> 374
    //   342: iload_1
    //   343: iconst_1
    //   344: if_icmpne -> 350
    //   347: goto -> 374
    //   350: iload_1
    //   351: bipush #66
    //   353: if_icmpeq -> 365
    //   356: iload #5
    //   358: istore #4
    //   360: iload_1
    //   361: iconst_2
    //   362: if_icmpne -> 380
    //   365: aload_0
    //   366: invokevirtual pageRight : ()Z
    //   369: istore #4
    //   371: goto -> 380
    //   374: aload_0
    //   375: invokevirtual pageLeft : ()Z
    //   378: istore #4
    //   380: iload #4
    //   382: ifeq -> 393
    //   385: aload_0
    //   386: iload_1
    //   387: invokestatic getContantForFocusDirection : (I)I
    //   390: invokevirtual playSoundEffect : (I)V
    //   393: iload #4
    //   395: ireturn
  }
  
  public boolean beginFakeDrag() {
    if (this.mIsBeingDragged)
      return false; 
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    } else {
      velocityTracker.clear();
    } 
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
    this.mFakeDragBeginTime = l;
    return true;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1 = paramView instanceof ViewGroup;
    boolean bool = true;
    if (bool1) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        if (paramInt2 + j >= view.getLeft() && paramInt2 + j < view.getRight() && paramInt3 + k >= view.getTop() && paramInt3 + k < view.getBottom() && canScroll(view, true, paramInt1, paramInt2 + j - view.getLeft(), paramInt3 + k - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean && paramView.canScrollHorizontally(-paramInt1)) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool2 = false;
    boolean bool1 = false;
    if (pagerAdapter == null)
      return false; 
    int j = getClientWidth();
    int i = getScrollX();
    if (paramInt < 0) {
      if (i > (int)(j * this.mFirstOffset))
        bool1 = true; 
      return bool1;
    } 
    if (paramInt > 0) {
      bool1 = bool2;
      if (i < (int)(j * this.mLastOffset))
        bool1 = true; 
      return bool1;
    } 
    return false;
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
  
  public void clearOnPageChangeListeners() {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void computeScroll() {
    this.mIsScrollStarted = true;
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int k = getScrollX();
      int j = getScrollY();
      int m = this.mScroller.getCurrX();
      int i = this.mScroller.getCurrY();
      if (k != m || j != i) {
        scrollTo(m, i);
        if (!pageScrolled(m)) {
          this.mScroller.abortAnimation();
          scrollTo(0, i);
        } 
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
      return;
    } 
    completeScroll(true);
  }
  
  void dataSetChanged() {
    byte b;
    int m = this.mAdapter.getCount();
    this.mExpectedAdapterCount = m;
    if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < m) {
      b = 1;
    } else {
      b = 0;
    } 
    int i = this.mCurItem;
    int j = 0;
    int k = 0;
    while (k < this.mItems.size()) {
      int n;
      int i1;
      int i2;
      ItemInfo itemInfo = this.mItems.get(k);
      int i3 = this.mAdapter.getItemPosition(itemInfo.object);
      if (i3 == -1) {
        n = i;
        i1 = j;
        i2 = k;
      } else if (i3 == -2) {
        this.mItems.remove(k);
        i3 = k - 1;
        k = j;
        if (!j) {
          this.mAdapter.startUpdate(this);
          k = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        b = 1;
        n = i;
        i1 = k;
        i2 = i3;
        if (this.mCurItem == itemInfo.position) {
          n = Math.max(0, Math.min(this.mCurItem, m - 1));
          b = 1;
          i1 = k;
          i2 = i3;
        } 
      } else {
        n = i;
        i1 = j;
        i2 = k;
        if (itemInfo.position != i3) {
          if (itemInfo.position == this.mCurItem)
            i = i3; 
          itemInfo.position = i3;
          b = 1;
          i2 = k;
          i1 = j;
          n = i;
        } 
      } 
      k = i2 + 1;
      i = n;
      j = i1;
    } 
    if (j != 0)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (b) {
      j = getChildCount();
      for (b = 0; b < j; b++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
        if (!layoutParams.isDecor)
          layoutParams.widthFactor = 0.0F; 
      } 
      setCurrentItemInternal(i, false, true);
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 4096)
      return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent); 
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))
          return true; 
      } 
    } 
    return false;
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: iconst_0
    //   6: istore_3
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_0
    //   10: invokevirtual getOverScrollMode : ()I
    //   13: istore #4
    //   15: iload #4
    //   17: ifeq -> 66
    //   20: iload #4
    //   22: iconst_1
    //   23: if_icmpne -> 49
    //   26: aload_0
    //   27: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   30: astore #8
    //   32: aload #8
    //   34: ifnull -> 49
    //   37: aload #8
    //   39: invokevirtual getCount : ()I
    //   42: iconst_1
    //   43: if_icmple -> 49
    //   46: goto -> 66
    //   49: aload_0
    //   50: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   53: invokevirtual finish : ()V
    //   56: aload_0
    //   57: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   60: invokevirtual finish : ()V
    //   63: goto -> 257
    //   66: aload_0
    //   67: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   70: invokevirtual isFinished : ()Z
    //   73: ifne -> 155
    //   76: aload_1
    //   77: invokevirtual save : ()I
    //   80: istore_3
    //   81: aload_0
    //   82: invokevirtual getHeight : ()I
    //   85: aload_0
    //   86: invokevirtual getPaddingTop : ()I
    //   89: isub
    //   90: aload_0
    //   91: invokevirtual getPaddingBottom : ()I
    //   94: isub
    //   95: istore_2
    //   96: aload_0
    //   97: invokevirtual getWidth : ()I
    //   100: istore #4
    //   102: aload_1
    //   103: ldc_w 270.0
    //   106: invokevirtual rotate : (F)V
    //   109: aload_1
    //   110: iload_2
    //   111: ineg
    //   112: aload_0
    //   113: invokevirtual getPaddingTop : ()I
    //   116: iadd
    //   117: i2f
    //   118: aload_0
    //   119: getfield mFirstOffset : F
    //   122: iload #4
    //   124: i2f
    //   125: fmul
    //   126: invokevirtual translate : (FF)V
    //   129: aload_0
    //   130: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   133: iload_2
    //   134: iload #4
    //   136: invokevirtual setSize : (II)V
    //   139: iconst_0
    //   140: aload_0
    //   141: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   144: aload_1
    //   145: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   148: ior
    //   149: istore_2
    //   150: aload_1
    //   151: iload_3
    //   152: invokevirtual restoreToCount : (I)V
    //   155: iload_2
    //   156: istore_3
    //   157: aload_0
    //   158: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   161: invokevirtual isFinished : ()Z
    //   164: ifne -> 257
    //   167: aload_1
    //   168: invokevirtual save : ()I
    //   171: istore #4
    //   173: aload_0
    //   174: invokevirtual getWidth : ()I
    //   177: istore #6
    //   179: aload_0
    //   180: invokevirtual getHeight : ()I
    //   183: istore #7
    //   185: aload_0
    //   186: invokevirtual getPaddingTop : ()I
    //   189: istore_3
    //   190: aload_0
    //   191: invokevirtual getPaddingBottom : ()I
    //   194: istore #5
    //   196: aload_1
    //   197: ldc_w 90.0
    //   200: invokevirtual rotate : (F)V
    //   203: aload_1
    //   204: aload_0
    //   205: invokevirtual getPaddingTop : ()I
    //   208: ineg
    //   209: i2f
    //   210: aload_0
    //   211: getfield mLastOffset : F
    //   214: fconst_1
    //   215: fadd
    //   216: fneg
    //   217: iload #6
    //   219: i2f
    //   220: fmul
    //   221: invokevirtual translate : (FF)V
    //   224: aload_0
    //   225: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   228: iload #7
    //   230: iload_3
    //   231: isub
    //   232: iload #5
    //   234: isub
    //   235: iload #6
    //   237: invokevirtual setSize : (II)V
    //   240: iload_2
    //   241: aload_0
    //   242: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   245: aload_1
    //   246: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   249: ior
    //   250: istore_3
    //   251: aload_1
    //   252: iload #4
    //   254: invokevirtual restoreToCount : (I)V
    //   257: iload_3
    //   258: ifeq -> 265
    //   261: aload_0
    //   262: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   265: return
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable drawable = this.mMarginDrawable;
    if (drawable != null && drawable.isStateful())
      drawable.setState(getDrawableState()); 
  }
  
  public void endFakeDrag() {
    if (this.mFakeDragging) {
      if (this.mAdapter != null) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int k = (int)velocityTracker.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int j = getClientWidth();
        int i = getScrollX();
        ItemInfo itemInfo = infoForCurrentScrollPosition();
        setCurrentItemInternal(determineTargetPage(itemInfo.position, (i / j - itemInfo.offset) / itemInfo.widthFactor, k, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, k);
      } 
      endDrag();
      this.mFakeDragging = false;
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramKeyEvent.getAction() == 0) {
      switch (paramKeyEvent.getKeyCode()) {
        default:
          return bool2;
        case 61:
          if (paramKeyEvent.hasNoModifiers()) {
            bool1 = arrowScroll(2);
          } else {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(1))
              bool1 = arrowScroll(1); 
          } 
          return bool1;
        case 22:
          if (paramKeyEvent.hasModifiers(2)) {
            bool1 = pageRight();
          } else {
            bool1 = arrowScroll(66);
          } 
          return bool1;
        case 21:
          break;
      } 
      if (paramKeyEvent.hasModifiers(2)) {
        bool1 = pageLeft();
      } else {
        bool1 = arrowScroll(17);
      } 
    } 
    return bool1;
  }
  
  public void fakeDragBy(float paramFloat) {
    if (this.mFakeDragging) {
      if (this.mAdapter == null)
        return; 
      this.mLastMotionX += paramFloat;
      float f2 = getScrollX() - paramFloat;
      int i = getClientWidth();
      paramFloat = i * this.mFirstOffset;
      float f1 = i * this.mLastOffset;
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      if (itemInfo1.position != 0)
        paramFloat = itemInfo1.offset * i; 
      if (itemInfo2.position != this.mAdapter.getCount() - 1)
        f1 = itemInfo2.offset * i; 
      if (f2 >= paramFloat) {
        paramFloat = f2;
        if (f2 > f1)
          paramFloat = f1; 
      } 
      this.mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(motionEvent);
      motionEvent.recycle();
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter() {
    return this.mAdapter;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    if (this.mDrawingOrder == 2) {
      paramInt1 = paramInt1 - 1 - paramInt2;
    } else {
      paramInt1 = paramInt2;
    } 
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(paramInt1)).getLayoutParams()).childIndex;
  }
  
  public int getCurrentItem() {
    return this.mCurItem;
  }
  
  public int getOffscreenPageLimit() {
    return this.mOffscreenPageLimit;
  }
  
  public int getPageMargin() {
    return this.mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView) {
    while (true) {
      ViewParent viewParent = paramView.getParent();
      if (viewParent != this) {
        if (viewParent != null) {
          if (!(viewParent instanceof View))
            return null; 
          paramView = (View)viewParent;
          continue;
        } 
        continue;
      } 
      return infoForChild(paramView);
    } 
  }
  
  ItemInfo infoForChild(View paramView) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (this.mAdapter.isViewFromObject(paramView, itemInfo.object))
        return itemInfo; 
    } 
    return null;
  }
  
  ItemInfo infoForPosition(int paramInt) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (itemInfo.position == paramInt)
        return itemInfo; 
    } 
    return null;
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    float f = (context.getResources().getDisplayMetrics()).density;
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffect(context);
    this.mRightEdge = new EdgeEffect(context);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          private final Rect mTempRect = new Rect();
          
          final ViewPager this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            WindowInsetsCompat windowInsetsCompat = ViewCompat.onApplyWindowInsets(param1View, param1WindowInsetsCompat);
            if (windowInsetsCompat.isConsumed())
              return windowInsetsCompat; 
            Rect rect = this.mTempRect;
            rect.left = windowInsetsCompat.getSystemWindowInsetLeft();
            rect.top = windowInsetsCompat.getSystemWindowInsetTop();
            rect.right = windowInsetsCompat.getSystemWindowInsetRight();
            rect.bottom = windowInsetsCompat.getSystemWindowInsetBottom();
            byte b = 0;
            int i = ViewPager.this.getChildCount();
            while (b < i) {
              param1WindowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(b), windowInsetsCompat);
              rect.left = Math.min(param1WindowInsetsCompat.getSystemWindowInsetLeft(), rect.left);
              rect.top = Math.min(param1WindowInsetsCompat.getSystemWindowInsetTop(), rect.top);
              rect.right = Math.min(param1WindowInsetsCompat.getSystemWindowInsetRight(), rect.right);
              rect.bottom = Math.min(param1WindowInsetsCompat.getSystemWindowInsetBottom(), rect.bottom);
              b++;
            } 
            return windowInsetsCompat.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
          }
        });
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mEndScrollRunnable);
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished())
      this.mScroller.abortAnimation(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
      int j = getScrollX();
      int m = getWidth();
      float f2 = this.mPageMargin / m;
      byte b = 0;
      ItemInfo itemInfo = this.mItems.get(0);
      float f1 = itemInfo.offset;
      int n = this.mItems.size();
      int i = itemInfo.position;
      int k = ((ItemInfo)this.mItems.get(n - 1)).position;
      while (i < k) {
        float f;
        ItemInfo itemInfo1;
        while (i > itemInfo.position && b < n) {
          ArrayList<ItemInfo> arrayList = this.mItems;
          itemInfo1 = arrayList.get(++b);
        } 
        if (i == itemInfo1.position) {
          f = (itemInfo1.offset + itemInfo1.widthFactor) * m;
          f1 = itemInfo1.offset + itemInfo1.widthFactor + f2;
        } else {
          float f4 = this.mAdapter.getPageWidth(i);
          f = m;
          float f3 = f1 + f4 + f2;
          f = (f1 + f4) * f;
          f1 = f3;
        } 
        if (this.mPageMargin + f > j) {
          this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(this.mPageMargin + f), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        } 
        if (f > (j + m))
          break; 
        i++;
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      resetTouch();
      return false;
    } 
    if (i != 0) {
      if (this.mIsBeingDragged)
        return true; 
      if (this.mIsUnableToDrag)
        return false; 
    } 
    switch (i) {
      case 6:
        onSecondaryPointerUp(paramMotionEvent);
        break;
      case 2:
        i = this.mActivePointerId;
        if (i == -1)
          break; 
        i = paramMotionEvent.findPointerIndex(i);
        f3 = paramMotionEvent.getX(i);
        f4 = f3 - this.mLastMotionX;
        f5 = Math.abs(f4);
        f2 = paramMotionEvent.getY(i);
        f1 = Math.abs(f2 - this.mInitialMotionY);
        if (f4 != 0.0F && !isGutterDrag(this.mLastMotionX, f4) && canScroll((View)this, false, (int)f4, (int)f3, (int)f2)) {
          this.mLastMotionX = f3;
          this.mLastMotionY = f2;
          this.mIsUnableToDrag = true;
          return false;
        } 
        i = this.mTouchSlop;
        if (f5 > i && 0.5F * f5 > f1) {
          this.mIsBeingDragged = true;
          requestParentDisallowInterceptTouchEvent(true);
          setScrollState(1);
          if (f4 > 0.0F) {
            f1 = this.mInitialMotionX + this.mTouchSlop;
          } else {
            f1 = this.mInitialMotionX - this.mTouchSlop;
          } 
          this.mLastMotionX = f1;
          this.mLastMotionY = f2;
          setScrollingCacheEnabled(true);
        } else if (f1 > i) {
          this.mIsUnableToDrag = true;
        } 
        if (this.mIsBeingDragged && performDrag(f3))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        break;
      case 0:
        f1 = paramMotionEvent.getX();
        this.mInitialMotionX = f1;
        this.mLastMotionX = f1;
        f1 = paramMotionEvent.getY();
        this.mInitialMotionY = f1;
        this.mLastMotionY = f1;
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        this.mIsUnableToDrag = false;
        this.mIsScrollStarted = true;
        this.mScroller.computeScrollOffset();
        if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
          this.mScroller.abortAnimation();
          this.mPopulatePending = false;
          populate();
          this.mIsBeingDragged = true;
          requestParentDisallowInterceptTouchEvent(true);
          setScrollState(1);
          break;
        } 
        completeScroll(false);
        this.mIsBeingDragged = false;
        break;
    } 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    return this.mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int m = getChildCount();
    int n = paramInt3 - paramInt1;
    int i1 = paramInt4 - paramInt2;
    paramInt1 = getPaddingLeft();
    paramInt2 = getPaddingTop();
    int i = getPaddingRight();
    paramInt4 = getPaddingBottom();
    int i2 = getScrollX();
    int j = 0;
    int k = 0;
    while (k < m) {
      View view = getChildAt(k);
      paramInt3 = paramInt1;
      int i3 = paramInt2;
      int i4 = i;
      int i5 = paramInt4;
      int i6 = j;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isDecor) {
          paramInt3 = layoutParams.gravity;
          i4 = layoutParams.gravity;
          switch (paramInt3 & 0x7) {
            default:
              paramInt3 = paramInt1;
              i3 = paramInt1;
              break;
            case 5:
              paramInt3 = n - i - view.getMeasuredWidth();
              i += view.getMeasuredWidth();
              i3 = paramInt1;
              break;
            case 3:
              paramInt3 = paramInt1;
              i3 = paramInt1 + view.getMeasuredWidth();
              break;
            case 1:
              paramInt3 = Math.max((n - view.getMeasuredWidth()) / 2, paramInt1);
              i3 = paramInt1;
              break;
          } 
          switch (i4 & 0x70) {
            default:
              paramInt1 = paramInt2;
              break;
            case 80:
              paramInt1 = i1 - paramInt4 - view.getMeasuredHeight();
              paramInt4 += view.getMeasuredHeight();
              break;
            case 48:
              paramInt1 = paramInt2;
              paramInt2 += view.getMeasuredHeight();
              break;
            case 16:
              paramInt1 = Math.max((i1 - view.getMeasuredHeight()) / 2, paramInt2);
              break;
          } 
          paramInt3 += i2;
          view.layout(paramInt3, paramInt1, paramInt3 + view.getMeasuredWidth(), paramInt1 + view.getMeasuredHeight());
          i6 = j + 1;
          paramInt3 = i3;
          i3 = paramInt2;
          i4 = i;
          i5 = paramInt4;
        } else {
          i6 = j;
          i5 = paramInt4;
          i4 = i;
          i3 = paramInt2;
          paramInt3 = paramInt1;
        } 
      } 
      k++;
      paramInt1 = paramInt3;
      paramInt2 = i3;
      i = i4;
      paramInt4 = i5;
      j = i6;
    } 
    k = n - paramInt1 - i;
    byte b = 0;
    paramInt3 = n;
    i = m;
    while (b < i) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null) {
            int i3 = paramInt1 + (int)(k * itemInfo.offset);
            if (layoutParams.needsMeasure) {
              layoutParams.needsMeasure = false;
              view.measure(View.MeasureSpec.makeMeasureSpec((int)(k * layoutParams.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(i1 - paramInt2 - paramInt4, 1073741824));
            } 
            view.layout(i3, paramInt2, view.getMeasuredWidth() + i3, view.getMeasuredHeight() + paramInt2);
          } 
        } 
      } 
      b++;
    } 
    this.mTopPageBounds = paramInt2;
    this.mBottomPageBounds = i1 - paramInt4;
    this.mDecorChildCount = j;
    if (this.mFirstLayout)
      scrollToItem(this.mCurItem, false, 0, false); 
    this.mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int k = getMeasuredWidth();
    int j = k / 10;
    this.mGutterSize = Math.min(j, this.mDefaultGutterSize);
    paramInt1 = k - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int m = getChildCount();
    byte b = 0;
    while (b < m) {
      int n;
      int i1;
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams != null && layoutParams.isDecor) {
          boolean bool;
          int i4;
          n = layoutParams.gravity & 0x7;
          int i3 = layoutParams.gravity & 0x70;
          int i2 = Integer.MIN_VALUE;
          i1 = Integer.MIN_VALUE;
          if (i3 == 48 || i3 == 80) {
            i3 = 1;
          } else {
            i3 = 0;
          } 
          if (n == 3 || n == 5) {
            bool = true;
          } else {
            bool = false;
          } 
          if (i3 != 0) {
            n = 1073741824;
          } else {
            n = i2;
            if (bool) {
              i1 = 1073741824;
              n = i2;
            } 
          } 
          if (layoutParams.width != -2) {
            i4 = 1073741824;
            if (layoutParams.width != -1) {
              n = layoutParams.width;
            } else {
              n = paramInt1;
            } 
          } else {
            i2 = paramInt1;
            i4 = n;
            n = i2;
          } 
          if (layoutParams.height != -2) {
            if (layoutParams.height != -1) {
              i2 = layoutParams.height;
              i1 = 1073741824;
            } else {
              i1 = 1073741824;
              i2 = paramInt2;
            } 
          } else {
            i2 = paramInt2;
          } 
          view.measure(View.MeasureSpec.makeMeasureSpec(n, i4), View.MeasureSpec.makeMeasureSpec(i2, i1));
          if (i3 != 0) {
            i1 = paramInt2 - view.getMeasuredHeight();
            n = paramInt1;
          } else {
            n = paramInt1;
            i1 = paramInt2;
            if (bool) {
              n = paramInt1 - view.getMeasuredWidth();
              i1 = paramInt2;
            } 
          } 
        } else {
          n = paramInt1;
          i1 = paramInt2;
        } 
      } else {
        i1 = paramInt2;
        n = paramInt1;
      } 
      b++;
      paramInt1 = n;
      paramInt2 = i1;
    } 
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i = getChildCount();
    for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams == null || !layoutParams.isDecor)
          view.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec); 
      } 
    } 
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    if (this.mDecorChildCount > 0) {
      int k = getScrollX();
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int m = getWidth();
      int n = getChildCount();
      byte b = 0;
      while (b < n) {
        int i1;
        int i2;
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          i2 = i;
          i1 = j;
        } else {
          switch (layoutParams.gravity & 0x7) {
            default:
              i1 = i;
              break;
            case 5:
              i1 = m - j - view.getMeasuredWidth();
              j += view.getMeasuredWidth();
              break;
            case 3:
              i1 = i;
              i += view.getWidth();
              break;
            case 1:
              i1 = Math.max((m - view.getMeasuredWidth()) / 2, i);
              break;
          } 
          int i3 = i1 + k - view.getLeft();
          i2 = i;
          i1 = j;
          if (i3 != 0) {
            view.offsetLeftAndRight(i3);
            i1 = j;
            i2 = i;
          } 
        } 
        b++;
        i = i2;
        j = i1;
      } 
    } 
    dispatchOnPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null) {
      int i = getScrollX();
      paramInt2 = getChildCount();
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (!((LayoutParams)view.getLayoutParams()).isDecor) {
          paramFloat = (view.getLeft() - i) / getClientWidth();
          this.mPageTransformer.transformPage(view, paramFloat);
        } 
      } 
    } 
    this.mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    int i;
    byte b;
    int j = getChildCount();
    if ((paramInt & 0x2) != 0) {
      i = 0;
      b = 1;
    } else {
      i = j - 1;
      b = -1;
      j = -1;
    } 
    while (i != j) {
      View view = getChildAt(i);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(paramInt, paramRect))
          return true; 
      } 
      i += b;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.restoreState(savedState.adapterState, savedState.loader);
      setCurrentItemInternal(savedState.position, false, true);
    } else {
      this.mRestoredCurItem = savedState.position;
      this.mRestoredAdapterState = savedState.adapterState;
      this.mRestoredClassLoader = savedState.loader;
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null)
      savedState.adapterState = pagerAdapter.saveState(); 
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      paramInt2 = this.mPageMargin;
      recomputeScrollPosition(paramInt1, paramInt3, paramInt2, paramInt2);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    if (this.mFakeDragging)
      return true; 
    if (paramMotionEvent.getAction() == 0 && paramMotionEvent.getEdgeFlags() != 0)
      return false; 
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter == null || pagerAdapter.getCount() == 0)
      return false; 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    boolean bool = false;
    switch (i & 0xFF) {
      case 6:
        onSecondaryPointerUp(paramMotionEvent);
        this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId));
        break;
      case 5:
        i = paramMotionEvent.getActionIndex();
        this.mLastMotionX = paramMotionEvent.getX(i);
        this.mActivePointerId = paramMotionEvent.getPointerId(i);
        break;
      case 3:
        if (this.mIsBeingDragged) {
          scrollToItem(this.mCurItem, true, 0, false);
          bool = resetTouch();
        } 
        break;
      case 2:
        if (!this.mIsBeingDragged) {
          i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          if (i == -1) {
            bool = resetTouch();
            break;
          } 
          float f1 = paramMotionEvent.getX(i);
          float f4 = Math.abs(f1 - this.mLastMotionX);
          float f2 = paramMotionEvent.getY(i);
          float f3 = Math.abs(f2 - this.mLastMotionY);
          if (f4 > this.mTouchSlop && f4 > f3) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            f3 = this.mInitialMotionX;
            if (f1 - f3 > 0.0F) {
              f1 = f3 + this.mTouchSlop;
            } else {
              f1 = f3 - this.mTouchSlop;
            } 
            this.mLastMotionX = f1;
            this.mLastMotionY = f2;
            setScrollState(1);
            setScrollingCacheEnabled(true);
            ViewParent viewParent = getParent();
            if (viewParent != null)
              viewParent.requestDisallowInterceptTouchEvent(true); 
          } 
        } 
        if (this.mIsBeingDragged)
          int j = false | performDrag(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId))); 
        break;
      case 1:
        if (this.mIsBeingDragged) {
          VelocityTracker velocityTracker = this.mVelocityTracker;
          velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
          int j = (int)velocityTracker.getXVelocity(this.mActivePointerId);
          this.mPopulatePending = true;
          i = getClientWidth();
          int k = getScrollX();
          ItemInfo itemInfo = infoForCurrentScrollPosition();
          float f1 = this.mPageMargin / i;
          setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / i - itemInfo.offset) / (itemInfo.widthFactor + f1), j, (int)(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, j);
          bool = resetTouch();
        } 
        break;
      case 0:
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
        f = paramMotionEvent.getY();
        this.mInitialMotionY = f;
        this.mLastMotionY = f;
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        break;
    } 
    if (bool)
      ViewCompat.postInvalidateOnAnimation((View)this); 
    return true;
  }
  
  boolean pageLeft() {
    int i = this.mCurItem;
    if (i > 0) {
      setCurrentItem(i - 1, true);
      return true;
    } 
    return false;
  }
  
  boolean pageRight() {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return true;
    } 
    return false;
  }
  
  void populate() {
    populate(this.mCurItem);
  }
  
  void populate(int paramInt) {
    String str;
    ItemInfo itemInfo;
    int i = this.mCurItem;
    if (i != paramInt) {
      itemInfo = infoForPosition(i);
      this.mCurItem = paramInt;
    } else {
      itemInfo = null;
    } 
    if (this.mAdapter == null) {
      sortChildDrawingOrder();
      return;
    } 
    if (this.mPopulatePending) {
      sortChildDrawingOrder();
      return;
    } 
    if (getWindowToken() == null)
      return; 
    this.mAdapter.startUpdate(this);
    int j = this.mOffscreenPageLimit;
    int k = Math.max(0, this.mCurItem - j);
    int m = this.mAdapter.getCount();
    int n = Math.min(m - 1, this.mCurItem + j);
    if (m == this.mExpectedAdapterCount) {
      ItemInfo itemInfo1;
      ItemInfo itemInfo2 = null;
      paramInt = 0;
      while (true) {
        itemInfo1 = itemInfo2;
        if (paramInt < this.mItems.size()) {
          ItemInfo itemInfo3 = this.mItems.get(paramInt);
          if (itemInfo3.position >= this.mCurItem) {
            itemInfo1 = itemInfo2;
            if (itemInfo3.position == this.mCurItem)
              itemInfo1 = itemInfo3; 
            break;
          } 
          paramInt++;
          continue;
        } 
        break;
      } 
      itemInfo2 = itemInfo1;
      if (itemInfo1 == null) {
        itemInfo2 = itemInfo1;
        if (m > 0)
          itemInfo2 = addNewItem(this.mCurItem, paramInt); 
      } 
      if (itemInfo2 != null) {
        float f2;
        float f3 = 0.0F;
        int i3 = paramInt - 1;
        if (i3 >= 0) {
          itemInfo1 = this.mItems.get(i3);
        } else {
          itemInfo1 = null;
        } 
        int i4 = getClientWidth();
        if (i4 <= 0) {
          f2 = 0.0F;
        } else {
          f2 = 2.0F - itemInfo2.widthFactor + getPaddingLeft() / i4;
        } 
        int i2 = this.mCurItem - 1;
        ItemInfo itemInfo3 = itemInfo1;
        int i1 = paramInt;
        while (i2 >= 0) {
          float f;
          if (f3 >= f2 && i2 < k) {
            if (itemInfo3 == null)
              break; 
            paramInt = i1;
            f = f3;
            i = i3;
            itemInfo1 = itemInfo3;
            if (i2 == itemInfo3.position) {
              paramInt = i1;
              f = f3;
              i = i3;
              itemInfo1 = itemInfo3;
              if (!itemInfo3.scrolling) {
                this.mItems.remove(i3);
                this.mAdapter.destroyItem(this, i2, itemInfo3.object);
                i = i3 - 1;
                paramInt = i1 - 1;
                if (i >= 0) {
                  itemInfo1 = this.mItems.get(i);
                } else {
                  itemInfo1 = null;
                } 
                f = f3;
              } 
            } 
          } else if (itemInfo3 != null && i2 == itemInfo3.position) {
            f = f3 + itemInfo3.widthFactor;
            i = i3 - 1;
            if (i >= 0) {
              itemInfo1 = this.mItems.get(i);
            } else {
              itemInfo1 = null;
            } 
            paramInt = i1;
          } else {
            f = f3 + (addNewItem(i2, i3 + 1)).widthFactor;
            paramInt = i1 + 1;
            if (i3 >= 0) {
              itemInfo1 = this.mItems.get(i3);
            } else {
              itemInfo1 = null;
            } 
            i = i3;
          } 
          i2--;
          i1 = paramInt;
          f3 = f;
          i3 = i;
          itemInfo3 = itemInfo1;
        } 
        float f1 = itemInfo2.widthFactor;
        paramInt = i1 + 1;
        if (f1 < 2.0F) {
          if (paramInt < this.mItems.size()) {
            itemInfo1 = this.mItems.get(paramInt);
          } else {
            itemInfo1 = null;
          } 
          if (i4 <= 0) {
            f2 = 0.0F;
          } else {
            f2 = getPaddingRight() / i4 + 2.0F;
          } 
          i2 = this.mCurItem + 1;
          i = k;
          i3 = j;
          while (i2 < m) {
            if (f1 >= f2 && i2 > n) {
              if (itemInfo1 == null)
                break; 
              if (i2 == itemInfo1.position && !itemInfo1.scrolling) {
                this.mItems.remove(paramInt);
                this.mAdapter.destroyItem(this, i2, itemInfo1.object);
                if (paramInt < this.mItems.size()) {
                  itemInfo1 = this.mItems.get(paramInt);
                } else {
                  itemInfo1 = null;
                } 
              } 
            } else if (itemInfo1 != null && i2 == itemInfo1.position) {
              f1 += itemInfo1.widthFactor;
              if (++paramInt < this.mItems.size()) {
                itemInfo1 = this.mItems.get(paramInt);
              } else {
                itemInfo1 = null;
              } 
            } else {
              itemInfo1 = addNewItem(i2, paramInt);
              paramInt++;
              f1 += itemInfo1.widthFactor;
              if (paramInt < this.mItems.size()) {
                itemInfo1 = this.mItems.get(paramInt);
              } else {
                itemInfo1 = null;
              } 
            } 
            i2++;
          } 
        } 
        calculatePageOffsets(itemInfo2, i1, itemInfo);
        this.mAdapter.setPrimaryItem(this, this.mCurItem, itemInfo2.object);
      } 
      this.mAdapter.finishUpdate(this);
      i = getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.childIndex = paramInt;
        if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0F) {
          ItemInfo itemInfo3 = infoForChild(view);
          if (itemInfo3 != null) {
            layoutParams.widthFactor = itemInfo3.widthFactor;
            layoutParams.position = itemInfo3.position;
          } 
        } 
      } 
      sortChildDrawingOrder();
      if (hasFocus()) {
        View view = findFocus();
        if (view != null) {
          ItemInfo itemInfo3 = infoForAnyChild(view);
        } else {
          view = null;
        } 
        if (view == null || ((ItemInfo)view).position != this.mCurItem)
          for (paramInt = 0; paramInt < getChildCount(); paramInt++) {
            View view1 = getChildAt(paramInt);
            ItemInfo itemInfo3 = infoForChild(view1);
            if (itemInfo3 != null && itemInfo3.position == this.mCurItem && view1.requestFocus(2))
              break; 
          }  
      } 
      return;
    } 
    try {
      str = getResources().getResourceName(getId());
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      str = Integer.toHexString(getId());
    } 
    throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + m + " Pager id: " + str + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
  }
  
  public void removeOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null)
      list.remove(paramOnAdapterChangeListener); 
  }
  
  public void removeOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.remove(paramOnPageChangeListener); 
  }
  
  public void removeView(View paramView) {
    if (this.mInLayout) {
      removeViewInLayout(paramView);
    } else {
      super.removeView(paramView);
    } 
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter) {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.setViewPagerObserver(null);
      this.mAdapter.startUpdate(this);
      for (byte b = 0; b < this.mItems.size(); b++) {
        ItemInfo itemInfo = this.mItems.get(b);
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
      } 
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    } 
    pagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    if (paramPagerAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      boolean bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else if (!bool) {
        populate();
      } else {
        requestLayout();
      } 
    } 
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null && !list.isEmpty()) {
      byte b = 0;
      int i = this.mAdapterChangeListeners.size();
      while (b < i) {
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(b)).onAdapterChanged(this, pagerAdapter, paramPagerAdapter);
        b++;
      } 
    } 
  }
  
  public void setCurrentItem(int paramInt) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, this.mFirstLayout ^ true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    int i;
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool = false;
    if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (paramInt1 < 0) {
      i = 0;
    } else {
      i = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        i = this.mAdapter.getCount() - 1; 
    } 
    paramInt1 = this.mOffscreenPageLimit;
    int j = this.mCurItem;
    if (i > j + paramInt1 || i < j - paramInt1)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    paramBoolean2 = bool;
    if (this.mCurItem != i)
      paramBoolean2 = true; 
    if (this.mFirstLayout) {
      this.mCurItem = i;
      if (paramBoolean2)
        dispatchOnPageSelected(i); 
      requestLayout();
    } else {
      populate(i);
      scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
    } 
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 1) {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + '\001');
      i = 1;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  @Deprecated
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt) {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt) {
    setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState(); 
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer) {
    setPageTransformer(paramBoolean, paramPageTransformer, 2);
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer, int paramInt) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    byte b = 1;
    if (paramPageTransformer != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (this.mPageTransformer != null) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (bool2 != bool3) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mPageTransformer = paramPageTransformer;
    setChildrenDrawingOrderEnabled(bool2);
    if (bool2) {
      if (paramBoolean)
        b = 2; 
      this.mDrawingOrder = b;
      this.mPageTransformerLayerType = paramInt;
    } else {
      this.mDrawingOrder = 0;
    } 
    if (bool1)
      populate(); 
  }
  
  void setScrollState(int paramInt) {
    if (this.mScrollState == paramInt)
      return; 
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null) {
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      enableLayers(bool);
    } 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      if (this.mIsScrollStarted) {
        i = this.mScroller.getCurrX();
      } else {
        i = this.mScroller.getStartX();
      } 
      this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    } else {
      i = getScrollX();
    } 
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getClientWidth();
    int m = paramInt1 / 2;
    float f3 = Math.min(1.0F, Math.abs(k) * 1.0F / paramInt1);
    float f2 = m;
    float f1 = m;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(Math.abs((f2 + f1 * f3) / paramInt3) * 1000.0F) * 4;
    } else {
      f1 = paramInt1;
      f2 = this.mAdapter.getPageWidth(this.mCurItem);
      paramInt1 = (int)((1.0F + Math.abs(k) / (this.mPageMargin + f1 * f2)) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mIsScrollStarted = false;
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable);
  }
  
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface DecorView {}
  
  static class ItemInfo {
    Object object;
    
    float offset;
    
    int position;
    
    boolean scrolling;
    
    float widthFactor;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    int childIndex;
    
    public int gravity;
    
    public boolean isDecor;
    
    boolean needsMeasure;
    
    int position;
    
    float widthFactor = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 48);
      typedArray.recycle();
    }
  }
  
  class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
    final ViewPager this$0;
    
    private boolean canScroll() {
      PagerAdapter pagerAdapter = ViewPager.this.mAdapter;
      boolean bool = true;
      if (pagerAdapter == null || ViewPager.this.mAdapter.getCount() <= 1)
        bool = false; 
      return bool;
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ViewPager.class.getName());
      param1AccessibilityEvent.setScrollable(canScroll());
      if (param1AccessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
        param1AccessibilityEvent.setItemCount(ViewPager.this.mAdapter.getCount());
        param1AccessibilityEvent.setFromIndex(ViewPager.this.mCurItem);
        param1AccessibilityEvent.setToIndex(ViewPager.this.mCurItem);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      param1AccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        param1AccessibilityNodeInfoCompat.addAction(4096); 
      if (ViewPager.this.canScrollHorizontally(-1))
        param1AccessibilityNodeInfoCompat.addAction(8192); 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      switch (param1Int) {
        default:
          return false;
        case 8192:
          if (ViewPager.this.canScrollHorizontally(-1)) {
            ViewPager viewPager = ViewPager.this;
            viewPager.setCurrentItem(viewPager.mCurItem - 1);
            return true;
          } 
          return false;
        case 4096:
          break;
      } 
      if (ViewPager.this.canScrollHorizontally(1)) {
        ViewPager viewPager = ViewPager.this;
        viewPager.setCurrentItem(viewPager.mCurItem + 1);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnAdapterChangeListener {
    void onAdapterChanged(ViewPager param1ViewPager, PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static interface PageTransformer {
    void transformPage(View param1View, float param1Float);
  }
  
  private class PagerObserver extends DataSetObserver {
    final ViewPager this$0;
    
    public void onChanged() {
      ViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated() {
      ViewPager.this.dataSetChanged();
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel) {
          return new ViewPager.SavedState(param2Parcel, null);
        }
        
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new ViewPager.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public ViewPager.SavedState[] newArray(int param2Int) {
          return new ViewPager.SavedState[param2Int];
        }
      };
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      this.position = param1Parcel.readInt();
      this.adapterState = param1Parcel.readParcelable(classLoader);
      this.loader = classLoader;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.position);
      param1Parcel.writeParcelable(this.adapterState, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel) {
      return new ViewPager.SavedState(param1Parcel, null);
    }
    
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new ViewPager.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public ViewPager.SavedState[] newArray(int param1Int) {
      return new ViewPager.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  static class ViewPositionComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      ViewPager.LayoutParams layoutParams1 = (ViewPager.LayoutParams)param1View1.getLayoutParams();
      ViewPager.LayoutParams layoutParams2 = (ViewPager.LayoutParams)param1View2.getLayoutParams();
      if (layoutParams1.isDecor != layoutParams2.isDecor) {
        byte b;
        if (layoutParams1.isDecor) {
          b = 1;
        } else {
          b = -1;
        } 
        return b;
      } 
      return layoutParams1.position - layoutParams2.position;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager\widget\ViewPager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */