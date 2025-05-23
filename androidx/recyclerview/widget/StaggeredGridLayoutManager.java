package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
  static final boolean DEBUG = false;
  
  @Deprecated
  public static final int GAP_HANDLING_LAZY = 1;
  
  public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
  
  public static final int GAP_HANDLING_NONE = 0;
  
  public static final int HORIZONTAL = 0;
  
  static final int INVALID_OFFSET = -2147483648;
  
  private static final float MAX_SCROLL_FACTOR = 0.33333334F;
  
  private static final String TAG = "StaggeredGridLManager";
  
  public static final int VERTICAL = 1;
  
  private final AnchorInfo mAnchorInfo = new AnchorInfo();
  
  private final Runnable mCheckForGapsRunnable = new Runnable() {
      final StaggeredGridLayoutManager this$0;
      
      public void run() {
        StaggeredGridLayoutManager.this.checkForGaps();
      }
    };
  
  private int mFullSizeSpec;
  
  private int mGapStrategy = 2;
  
  private boolean mLaidOutInvalidFullSpan = false;
  
  private boolean mLastLayoutFromEnd;
  
  private boolean mLastLayoutRTL;
  
  private final LayoutState mLayoutState;
  
  LazySpanLookup mLazySpanLookup = new LazySpanLookup();
  
  private int mOrientation;
  
  private SavedState mPendingSavedState;
  
  int mPendingScrollPosition = -1;
  
  int mPendingScrollPositionOffset = Integer.MIN_VALUE;
  
  private int[] mPrefetchDistances;
  
  OrientationHelper mPrimaryOrientation;
  
  private BitSet mRemainingSpans;
  
  boolean mReverseLayout = false;
  
  OrientationHelper mSecondaryOrientation;
  
  boolean mShouldReverseLayout = false;
  
  private int mSizePerSpan;
  
  private boolean mSmoothScrollbarEnabled = true;
  
  private int mSpanCount = -1;
  
  Span[] mSpans;
  
  private final Rect mTmpRect = new Rect();
  
  public StaggeredGridLayoutManager(int paramInt1, int paramInt2) {
    this.mOrientation = paramInt2;
    setSpanCount(paramInt1);
    this.mLayoutState = new LayoutState();
    createOrientationHelpers();
  }
  
  public StaggeredGridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    RecyclerView.LayoutManager.Properties properties = getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setOrientation(properties.orientation);
    setSpanCount(properties.spanCount);
    setReverseLayout(properties.reverseLayout);
    this.mLayoutState = new LayoutState();
    createOrientationHelpers();
  }
  
  private void appendViewToAllSpans(View paramView) {
    for (int i = this.mSpanCount - 1; i >= 0; i--)
      this.mSpans[i].appendToSpan(paramView); 
  }
  
  private void applyPendingSavedState(AnchorInfo paramAnchorInfo) {
    if (this.mPendingSavedState.mSpanOffsetsSize > 0)
      if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
        for (byte b = 0; b < this.mSpanCount; b++) {
          this.mSpans[b].clear();
          int j = this.mPendingSavedState.mSpanOffsets[b];
          int i = j;
          if (j != Integer.MIN_VALUE)
            if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
              i = j + this.mPrimaryOrientation.getEndAfterPadding();
            } else {
              i = j + this.mPrimaryOrientation.getStartAfterPadding();
            }  
          this.mSpans[b].setLine(i);
        } 
      } else {
        this.mPendingSavedState.invalidateSpanInfo();
        SavedState savedState = this.mPendingSavedState;
        savedState.mAnchorPosition = savedState.mVisibleAnchorPosition;
      }  
    this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
    setReverseLayout(this.mPendingSavedState.mReverseLayout);
    resolveShouldLayoutReverse();
    if (this.mPendingSavedState.mAnchorPosition != -1) {
      this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
      paramAnchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
    } else {
      paramAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
    } 
    if (this.mPendingSavedState.mSpanLookupSize > 1) {
      this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
      this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
    } 
  }
  
  private void attachViewToSpans(View paramView, LayoutParams paramLayoutParams, LayoutState paramLayoutState) {
    if (paramLayoutState.mLayoutDirection == 1) {
      if (paramLayoutParams.mFullSpan) {
        appendViewToAllSpans(paramView);
      } else {
        paramLayoutParams.mSpan.appendToSpan(paramView);
      } 
    } else if (paramLayoutParams.mFullSpan) {
      prependViewToAllSpans(paramView);
    } else {
      paramLayoutParams.mSpan.prependToSpan(paramView);
    } 
  }
  
  private int calculateScrollDirectionForPosition(int paramInt) {
    boolean bool;
    int i = getChildCount();
    byte b = -1;
    if (i == 0) {
      if (this.mShouldReverseLayout)
        b = 1; 
      return b;
    } 
    if (paramInt < getFirstChildPosition()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool == this.mShouldReverseLayout)
      b = 1; 
    return b;
  }
  
  private boolean checkSpanForGap(Span paramSpan) {
    if (this.mShouldReverseLayout) {
      if (paramSpan.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding())
        return (paramSpan.getLayoutParams((View)paramSpan.mViews.get(paramSpan.mViews.size() - 1))).mFullSpan ^ true; 
    } else if (paramSpan.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
      return (paramSpan.getLayoutParams((View)paramSpan.mViews.get(0))).mFullSpan ^ true;
    } 
    return false;
  }
  
  private int computeScrollExtent(RecyclerView.State paramState) {
    return (getChildCount() == 0) ? 0 : ScrollbarHelper.computeScrollExtent(paramState, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
  }
  
  private int computeScrollOffset(RecyclerView.State paramState) {
    return (getChildCount() == 0) ? 0 : ScrollbarHelper.computeScrollOffset(paramState, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
  }
  
  private int computeScrollRange(RecyclerView.State paramState) {
    return (getChildCount() == 0) ? 0 : ScrollbarHelper.computeScrollRange(paramState, this.mPrimaryOrientation, findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
  }
  
  private int convertFocusDirectionToLayoutDirection(int paramInt) {
    int i = -1;
    int j = Integer.MIN_VALUE;
    switch (paramInt) {
      default:
        return Integer.MIN_VALUE;
      case 130:
        if (this.mOrientation == 1)
          j = 1; 
        return j;
      case 66:
        if (this.mOrientation == 0)
          j = 1; 
        return j;
      case 33:
        if (this.mOrientation != 1)
          i = Integer.MIN_VALUE; 
        return i;
      case 17:
        if (this.mOrientation != 0)
          i = Integer.MIN_VALUE; 
        return i;
      case 2:
        return (this.mOrientation == 1) ? 1 : (isLayoutRTL() ? -1 : 1);
      case 1:
        break;
    } 
    return (this.mOrientation == 1) ? -1 : (isLayoutRTL() ? 1 : -1);
  }
  
  private LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int paramInt) {
    LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
    fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
    for (byte b = 0; b < this.mSpanCount; b++)
      fullSpanItem.mGapPerSpan[b] = paramInt - this.mSpans[b].getEndLine(paramInt); 
    return fullSpanItem;
  }
  
  private LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int paramInt) {
    LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
    fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
    for (byte b = 0; b < this.mSpanCount; b++)
      fullSpanItem.mGapPerSpan[b] = this.mSpans[b].getStartLine(paramInt) - paramInt; 
    return fullSpanItem;
  }
  
  private void createOrientationHelpers() {
    this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
    this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
  }
  
  private int fill(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState, RecyclerView.State paramState) {
    int k;
    BitSet bitSet = this.mRemainingSpans;
    int i = this.mSpanCount;
    bitSet.set(0, i, true);
    if (this.mLayoutState.mInfinite) {
      if (paramLayoutState.mLayoutDirection == 1) {
        i = Integer.MAX_VALUE;
      } else {
        i = Integer.MIN_VALUE;
      } 
    } else if (paramLayoutState.mLayoutDirection == 1) {
      i = paramLayoutState.mEndLine + paramLayoutState.mAvailable;
    } else {
      i = paramLayoutState.mStartLine - paramLayoutState.mAvailable;
    } 
    updateAllRemainingSpans(paramLayoutState.mLayoutDirection, i);
    if (this.mShouldReverseLayout) {
      k = this.mPrimaryOrientation.getEndAfterPadding();
    } else {
      k = this.mPrimaryOrientation.getStartAfterPadding();
    } 
    int j;
    for (j = 0; paramLayoutState.hasMore(paramState) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty()); j = 1) {
      int m;
      int n;
      int i1;
      Span span;
      View view = paramLayoutState.next(paramRecycler);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int i2 = layoutParams.getViewLayoutPosition();
      j = this.mLazySpanLookup.getSpan(i2);
      if (j == -1) {
        i1 = 1;
      } else {
        i1 = 0;
      } 
      if (i1) {
        if (layoutParams.mFullSpan) {
          span = this.mSpans[0];
        } else {
          span = getNextSpan(paramLayoutState);
        } 
        this.mLazySpanLookup.setSpan(i2, span);
      } else {
        span = this.mSpans[j];
      } 
      layoutParams.mSpan = span;
      if (paramLayoutState.mLayoutDirection == 1) {
        addView(view);
      } else {
        addView(view, 0);
      } 
      measureChildWithDecorationsAndMargin(view, layoutParams, false);
      if (paramLayoutState.mLayoutDirection == 1) {
        if (layoutParams.mFullSpan) {
          j = getMaxEnd(k);
        } else {
          j = span.getEndLine(k);
        } 
        int i3 = this.mPrimaryOrientation.getDecoratedMeasurement(view) + j;
        m = j;
        n = i3;
        if (i1) {
          m = j;
          n = i3;
          if (layoutParams.mFullSpan) {
            LazySpanLookup.FullSpanItem fullSpanItem = createFullSpanItemFromEnd(j);
            fullSpanItem.mGapDir = -1;
            fullSpanItem.mPosition = i2;
            this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
            m = j;
            n = i3;
          } 
        } 
      } else {
        if (layoutParams.mFullSpan) {
          j = getMinStart(k);
        } else {
          j = span.getStartLine(k);
        } 
        int i3 = j - this.mPrimaryOrientation.getDecoratedMeasurement(view);
        m = i3;
        n = j;
        if (i1) {
          m = i3;
          n = j;
          if (layoutParams.mFullSpan) {
            LazySpanLookup.FullSpanItem fullSpanItem = createFullSpanItemFromStart(j);
            fullSpanItem.mGapDir = 1;
            fullSpanItem.mPosition = i2;
            this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
            n = j;
            m = i3;
          } 
        } 
      } 
      if (layoutParams.mFullSpan && paramLayoutState.mItemDirection == -1)
        if (i1) {
          this.mLaidOutInvalidFullSpan = true;
        } else {
          if (paramLayoutState.mLayoutDirection == 1) {
            j = areAllEndsEqual() ^ true;
          } else {
            j = areAllStartsEqual() ^ true;
          } 
          if (j != 0) {
            LazySpanLookup.FullSpanItem fullSpanItem = this.mLazySpanLookup.getFullSpanItem(i2);
            if (fullSpanItem != null)
              fullSpanItem.mHasUnwantedGapAfter = true; 
            this.mLaidOutInvalidFullSpan = true;
          } 
        }  
      attachViewToSpans(view, layoutParams, paramLayoutState);
      if (isLayoutRTL() && this.mOrientation == 1) {
        if (layoutParams.mFullSpan) {
          j = this.mSecondaryOrientation.getEndAfterPadding();
        } else {
          j = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - span.mIndex) * this.mSizePerSpan;
        } 
        int i3 = this.mSecondaryOrientation.getDecoratedMeasurement(view);
        i1 = j;
        j -= i3;
      } else {
        if (layoutParams.mFullSpan) {
          j = this.mSecondaryOrientation.getStartAfterPadding();
        } else {
          j = span.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
        } 
        i1 = this.mSecondaryOrientation.getDecoratedMeasurement(view) + j;
      } 
      if (this.mOrientation == 1) {
        layoutDecoratedWithMargins(view, j, m, i1, n);
      } else {
        layoutDecoratedWithMargins(view, m, j, n, i1);
      } 
      if (layoutParams.mFullSpan) {
        updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, i);
      } else {
        updateRemainingSpans(span, this.mLayoutState.mLayoutDirection, i);
      } 
      recycle(paramRecycler, this.mLayoutState);
      if (this.mLayoutState.mStopInFocusable && view.hasFocusable())
        if (layoutParams.mFullSpan) {
          this.mRemainingSpans.clear();
        } else {
          this.mRemainingSpans.set(span.mIndex, false);
        }  
    } 
    if (j == 0)
      recycle(paramRecycler, this.mLayoutState); 
    if (this.mLayoutState.mLayoutDirection == -1) {
      i = getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
      i = this.mPrimaryOrientation.getStartAfterPadding() - i;
    } else {
      i = getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
    } 
    if (i > 0) {
      i = Math.min(paramLayoutState.mAvailable, i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  private int findFirstReferenceChildPosition(int paramInt) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      int j = getPosition(getChildAt(b));
      if (j >= 0 && j < paramInt)
        return j; 
    } 
    return 0;
  }
  
  private int findLastReferenceChildPosition(int paramInt) {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      int j = getPosition(getChildAt(i));
      if (j >= 0 && j < paramInt)
        return j; 
    } 
    return 0;
  }
  
  private void fixEndGap(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = getMaxEnd(-2147483648);
    if (i == Integer.MIN_VALUE)
      return; 
    i = this.mPrimaryOrientation.getEndAfterPadding() - i;
    if (i > 0) {
      i -= -scrollBy(-i, paramRecycler, paramState);
      if (paramBoolean && i > 0)
        this.mPrimaryOrientation.offsetChildren(i); 
      return;
    } 
  }
  
  private void fixStartGap(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = getMinStart(2147483647);
    if (i == Integer.MAX_VALUE)
      return; 
    i -= this.mPrimaryOrientation.getStartAfterPadding();
    if (i > 0) {
      i -= scrollBy(i, paramRecycler, paramState);
      if (paramBoolean && i > 0)
        this.mPrimaryOrientation.offsetChildren(-i); 
      return;
    } 
  }
  
  private int getMaxEnd(int paramInt) {
    int i = this.mSpans[0].getEndLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int k = this.mSpans[b].getEndLine(paramInt);
      int j = i;
      if (k > i)
        j = k; 
      b++;
      i = j;
    } 
    return i;
  }
  
  private int getMaxStart(int paramInt) {
    int i = this.mSpans[0].getStartLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int k = this.mSpans[b].getStartLine(paramInt);
      int j = i;
      if (k > i)
        j = k; 
      b++;
      i = j;
    } 
    return i;
  }
  
  private int getMinEnd(int paramInt) {
    int i = this.mSpans[0].getEndLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int k = this.mSpans[b].getEndLine(paramInt);
      int j = i;
      if (k < i)
        j = k; 
      b++;
      i = j;
    } 
    return i;
  }
  
  private int getMinStart(int paramInt) {
    int i = this.mSpans[0].getStartLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int k = this.mSpans[b].getStartLine(paramInt);
      int j = i;
      if (k < i)
        j = k; 
      b++;
      i = j;
    } 
    return i;
  }
  
  private Span getNextSpan(LayoutState paramLayoutState) {
    Span span;
    int i;
    int j;
    byte b;
    if (preferLastSpan(paramLayoutState.mLayoutDirection)) {
      i = this.mSpanCount - 1;
      j = -1;
      b = -1;
    } else {
      i = 0;
      j = this.mSpanCount;
      b = 1;
    } 
    if (paramLayoutState.mLayoutDirection == 1) {
      paramLayoutState = null;
      int n = Integer.MAX_VALUE;
      int i1 = this.mPrimaryOrientation.getStartAfterPadding();
      while (i != j) {
        Span span1 = this.mSpans[i];
        int i3 = span1.getEndLine(i1);
        int i2 = n;
        if (i3 < n) {
          span = span1;
          i2 = i3;
        } 
        i += b;
        n = i2;
      } 
      return span;
    } 
    paramLayoutState = null;
    int k = Integer.MIN_VALUE;
    int m = this.mPrimaryOrientation.getEndAfterPadding();
    while (i != j) {
      Span span1 = this.mSpans[i];
      int i1 = span1.getStartLine(m);
      int n = k;
      if (i1 > k) {
        span = span1;
        n = i1;
      } 
      i += b;
      k = n;
    } 
    return span;
  }
  
  private void handleUpdate(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    int k;
    if (this.mShouldReverseLayout) {
      k = getLastChildPosition();
    } else {
      k = getFirstChildPosition();
    } 
    if (paramInt3 == 8) {
      if (paramInt1 < paramInt2) {
        j = paramInt2 + 1;
        i = paramInt1;
      } else {
        j = paramInt1 + 1;
        i = paramInt2;
      } 
    } else {
      i = paramInt1;
      j = paramInt1 + paramInt2;
    } 
    this.mLazySpanLookup.invalidateAfter(i);
    switch (paramInt3) {
      case 8:
        this.mLazySpanLookup.offsetForRemoval(paramInt1, 1);
        this.mLazySpanLookup.offsetForAddition(paramInt2, 1);
        break;
      case 2:
        this.mLazySpanLookup.offsetForRemoval(paramInt1, paramInt2);
        break;
      case 1:
        this.mLazySpanLookup.offsetForAddition(paramInt1, paramInt2);
        break;
    } 
    if (j <= k)
      return; 
    if (this.mShouldReverseLayout) {
      paramInt1 = getFirstChildPosition();
    } else {
      paramInt1 = getLastChildPosition();
    } 
    if (i <= paramInt1)
      requestLayout(); 
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    calculateItemDecorationsForChild(paramView, this.mTmpRect);
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    paramInt1 = updateSpecWithExtra(paramInt1, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
    paramInt2 = updateSpecWithExtra(paramInt2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, LayoutParams paramLayoutParams, boolean paramBoolean) {
    if (paramLayoutParams.mFullSpan) {
      if (this.mOrientation == 1) {
        measureChildWithDecorationsAndMargin(paramView, this.mFullSizeSpec, getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom(), paramLayoutParams.height, true), paramBoolean);
      } else {
        measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight(), paramLayoutParams.width, true), this.mFullSizeSpec, paramBoolean);
      } 
    } else if (this.mOrientation == 1) {
      measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(this.mSizePerSpan, getWidthMode(), 0, paramLayoutParams.width, false), getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom(), paramLayoutParams.height, true), paramBoolean);
    } else {
      measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight(), paramLayoutParams.width, true), getChildMeasureSpec(this.mSizePerSpan, getHeightMode(), 0, paramLayoutParams.height, false), paramBoolean);
    } 
  }
  
  private void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    AnchorInfo anchorInfo = this.mAnchorInfo;
    if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && paramState.getItemCount() == 0) {
      removeAndRecycleAllViews(paramRecycler);
      anchorInfo.reset();
      return;
    } 
    boolean bool = anchorInfo.mValid;
    boolean bool1 = true;
    if (!bool || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null) {
      b = 1;
    } else {
      b = 0;
    } 
    if (b) {
      anchorInfo.reset();
      if (this.mPendingSavedState != null) {
        applyPendingSavedState(anchorInfo);
      } else {
        resolveShouldLayoutReverse();
        anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
      } 
      updateAnchorInfoForLayout(paramState, anchorInfo);
      anchorInfo.mValid = true;
    } 
    if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1 && (anchorInfo.mLayoutFromEnd != this.mLastLayoutFromEnd || isLayoutRTL() != this.mLastLayoutRTL)) {
      this.mLazySpanLookup.clear();
      anchorInfo.mInvalidateOffsets = true;
    } 
    if (getChildCount() > 0) {
      SavedState savedState = this.mPendingSavedState;
      if (savedState == null || savedState.mSpanOffsetsSize < 1)
        if (anchorInfo.mInvalidateOffsets) {
          for (b = 0; b < this.mSpanCount; b++) {
            this.mSpans[b].clear();
            if (anchorInfo.mOffset != Integer.MIN_VALUE)
              this.mSpans[b].setLine(anchorInfo.mOffset); 
          } 
        } else if (b != 0 || this.mAnchorInfo.mSpanReferenceLines == null) {
          for (b = 0; b < this.mSpanCount; b++)
            this.mSpans[b].cacheReferenceLineAndClear(this.mShouldReverseLayout, anchorInfo.mOffset); 
          this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
        } else {
          for (b = 0; b < this.mSpanCount; b++) {
            Span span = this.mSpans[b];
            span.clear();
            span.setLine(this.mAnchorInfo.mSpanReferenceLines[b]);
          } 
        }  
    } 
    detachAndScrapAttachedViews(paramRecycler);
    this.mLayoutState.mRecycle = false;
    this.mLaidOutInvalidFullSpan = false;
    updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
    updateLayoutState(anchorInfo.mPosition, paramState);
    if (anchorInfo.mLayoutFromEnd) {
      setLayoutStateDirection(-1);
      fill(paramRecycler, this.mLayoutState, paramState);
      setLayoutStateDirection(1);
      this.mLayoutState.mCurrentPosition = anchorInfo.mPosition + this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState);
    } else {
      setLayoutStateDirection(1);
      fill(paramRecycler, this.mLayoutState, paramState);
      setLayoutStateDirection(-1);
      this.mLayoutState.mCurrentPosition = anchorInfo.mPosition + this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState);
    } 
    repositionToWrapContentIfNecessary();
    if (getChildCount() > 0)
      if (this.mShouldReverseLayout) {
        fixEndGap(paramRecycler, paramState, true);
        fixStartGap(paramRecycler, paramState, false);
      } else {
        fixStartGap(paramRecycler, paramState, true);
        fixEndGap(paramRecycler, paramState, false);
      }  
    boolean bool2 = false;
    byte b = bool2;
    if (paramBoolean) {
      b = bool2;
      if (!paramState.isPreLayout()) {
        if (this.mGapStrategy == 0 || getChildCount() <= 0 || (!this.mLaidOutInvalidFullSpan && hasGapsToFix() == null))
          bool1 = false; 
        b = bool2;
        if (bool1) {
          removeCallbacks(this.mCheckForGapsRunnable);
          b = bool2;
          if (checkForGaps())
            b = 1; 
        } 
      } 
    } 
    if (paramState.isPreLayout())
      this.mAnchorInfo.reset(); 
    this.mLastLayoutFromEnd = anchorInfo.mLayoutFromEnd;
    this.mLastLayoutRTL = isLayoutRTL();
    if (b != 0) {
      this.mAnchorInfo.reset();
      onLayoutChildren(paramRecycler, paramState, false);
    } 
  }
  
  private boolean preferLastSpan(int paramInt) {
    boolean bool;
    int i = this.mOrientation;
    boolean bool1 = true;
    boolean bool2 = true;
    if (i == 0) {
      if (paramInt == -1) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool != this.mShouldReverseLayout) {
        bool = bool2;
      } else {
        bool = false;
      } 
      return bool;
    } 
    if (paramInt == -1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool == this.mShouldReverseLayout) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool == isLayoutRTL()) {
      bool = bool1;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void prependViewToAllSpans(View paramView) {
    for (int i = this.mSpanCount - 1; i >= 0; i--)
      this.mSpans[i].prependToSpan(paramView); 
  }
  
  private void recycle(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState) {
    if (!paramLayoutState.mRecycle || paramLayoutState.mInfinite)
      return; 
    if (paramLayoutState.mAvailable == 0) {
      if (paramLayoutState.mLayoutDirection == -1) {
        recycleFromEnd(paramRecycler, paramLayoutState.mEndLine);
      } else {
        recycleFromStart(paramRecycler, paramLayoutState.mStartLine);
      } 
    } else if (paramLayoutState.mLayoutDirection == -1) {
      int i = paramLayoutState.mStartLine - getMaxStart(paramLayoutState.mStartLine);
      if (i < 0) {
        i = paramLayoutState.mEndLine;
      } else {
        i = paramLayoutState.mEndLine - Math.min(i, paramLayoutState.mAvailable);
      } 
      recycleFromEnd(paramRecycler, i);
    } else {
      int i = getMinEnd(paramLayoutState.mEndLine) - paramLayoutState.mEndLine;
      if (i < 0) {
        i = paramLayoutState.mStartLine;
      } else {
        i = paramLayoutState.mStartLine + Math.min(i, paramLayoutState.mAvailable);
      } 
      recycleFromStart(paramRecycler, i);
    } 
  }
  
  private void recycleFromEnd(RecyclerView.Recycler paramRecycler, int paramInt) {
    int i = getChildCount() - 1;
    while (i >= 0) {
      View view = getChildAt(i);
      if (this.mPrimaryOrientation.getDecoratedStart(view) >= paramInt && this.mPrimaryOrientation.getTransformedStartWithDecoration(view) >= paramInt) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mFullSpan) {
          byte b;
          for (b = 0; b < this.mSpanCount; b++) {
            if ((this.mSpans[b]).mViews.size() == 1)
              return; 
          } 
          for (b = 0; b < this.mSpanCount; b++)
            this.mSpans[b].popEnd(); 
        } else {
          if (layoutParams.mSpan.mViews.size() == 1)
            return; 
          layoutParams.mSpan.popEnd();
        } 
        removeAndRecycleView(view, paramRecycler);
        i--;
        continue;
      } 
      return;
    } 
  }
  
  private void recycleFromStart(RecyclerView.Recycler paramRecycler, int paramInt) {
    while (getChildCount() > 0) {
      View view = getChildAt(0);
      if (this.mPrimaryOrientation.getDecoratedEnd(view) <= paramInt && this.mPrimaryOrientation.getTransformedEndWithDecoration(view) <= paramInt) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mFullSpan) {
          byte b;
          for (b = 0; b < this.mSpanCount; b++) {
            if ((this.mSpans[b]).mViews.size() == 1)
              return; 
          } 
          for (b = 0; b < this.mSpanCount; b++)
            this.mSpans[b].popStart(); 
        } else {
          if (layoutParams.mSpan.mViews.size() == 1)
            return; 
          layoutParams.mSpan.popStart();
        } 
        removeAndRecycleView(view, paramRecycler);
        continue;
      } 
      return;
    } 
  }
  
  private void repositionToWrapContentIfNecessary() {
    if (this.mSecondaryOrientation.getMode() == 1073741824)
      return; 
    float f = 0.0F;
    int k = getChildCount();
    int i;
    for (i = 0; i < k; i++) {
      View view = getChildAt(i);
      float f1 = this.mSecondaryOrientation.getDecoratedMeasurement(view);
      if (f1 >= f) {
        float f2 = f1;
        if (((LayoutParams)view.getLayoutParams()).isFullSpan())
          f2 = 1.0F * f1 / this.mSpanCount; 
        f = Math.max(f, f2);
      } 
    } 
    int m = this.mSizePerSpan;
    int j = Math.round(this.mSpanCount * f);
    i = j;
    if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE)
      i = Math.min(j, this.mSecondaryOrientation.getTotalSpace()); 
    updateMeasureSpecs(i);
    if (this.mSizePerSpan == m)
      return; 
    for (i = 0; i < k; i++) {
      View view = getChildAt(i);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (!layoutParams.mFullSpan)
        if (isLayoutRTL() && this.mOrientation == 1) {
          view.offsetLeftAndRight(-(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * m);
        } else {
          j = layoutParams.mSpan.mIndex * this.mSizePerSpan;
          int n = layoutParams.mSpan.mIndex * m;
          if (this.mOrientation == 1) {
            view.offsetLeftAndRight(j - n);
          } else {
            view.offsetTopAndBottom(j - n);
          } 
        }  
    } 
  }
  
  private void resolveShouldLayoutReverse() {
    if (this.mOrientation == 1 || !isLayoutRTL()) {
      this.mShouldReverseLayout = this.mReverseLayout;
      return;
    } 
    this.mShouldReverseLayout = this.mReverseLayout ^ true;
  }
  
  private void setLayoutStateDirection(int paramInt) {
    boolean bool1;
    this.mLayoutState.mLayoutDirection = paramInt;
    LayoutState layoutState = this.mLayoutState;
    boolean bool2 = this.mShouldReverseLayout;
    boolean bool = true;
    if (paramInt == -1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool2 == bool1) {
      paramInt = bool;
    } else {
      paramInt = -1;
    } 
    layoutState.mItemDirection = paramInt;
  }
  
  private void updateAllRemainingSpans(int paramInt1, int paramInt2) {
    for (byte b = 0; b < this.mSpanCount; b++) {
      if (!(this.mSpans[b]).mViews.isEmpty())
        updateRemainingSpans(this.mSpans[b], paramInt1, paramInt2); 
    } 
  }
  
  private boolean updateAnchorFromChildren(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    int i;
    if (this.mLastLayoutFromEnd) {
      i = findLastReferenceChildPosition(paramState.getItemCount());
    } else {
      i = findFirstReferenceChildPosition(paramState.getItemCount());
    } 
    paramAnchorInfo.mPosition = i;
    paramAnchorInfo.mOffset = Integer.MIN_VALUE;
    return true;
  }
  
  private void updateLayoutState(int paramInt, RecyclerView.State paramState) {
    boolean bool1;
    LayoutState layoutState2 = this.mLayoutState;
    boolean bool2 = false;
    layoutState2.mAvailable = 0;
    this.mLayoutState.mCurrentPosition = paramInt;
    byte b2 = 0;
    byte b1 = 0;
    int i = b2;
    int j = b1;
    if (isSmoothScrolling()) {
      int k = paramState.getTargetScrollPosition();
      i = b2;
      j = b1;
      if (k != -1) {
        boolean bool = this.mShouldReverseLayout;
        if (k < paramInt) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (bool == bool1) {
          j = this.mPrimaryOrientation.getTotalSpace();
          i = b2;
        } else {
          i = this.mPrimaryOrientation.getTotalSpace();
          j = b1;
        } 
      } 
    } 
    if (getClipToPadding()) {
      this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - i;
      this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + j;
    } else {
      this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + j;
      this.mLayoutState.mStartLine = -i;
    } 
    this.mLayoutState.mStopInFocusable = false;
    this.mLayoutState.mRecycle = true;
    LayoutState layoutState1 = this.mLayoutState;
    if (this.mPrimaryOrientation.getMode() == 0 && this.mPrimaryOrientation.getEnd() == 0) {
      bool1 = true;
    } else {
      bool1 = bool2;
    } 
    layoutState1.mInfinite = bool1;
  }
  
  private void updateRemainingSpans(Span paramSpan, int paramInt1, int paramInt2) {
    int i = paramSpan.getDeletedSize();
    if (paramInt1 == -1) {
      if (paramSpan.getStartLine() + i <= paramInt2)
        this.mRemainingSpans.set(paramSpan.mIndex, false); 
    } else if (paramSpan.getEndLine() - i >= paramInt2) {
      this.mRemainingSpans.set(paramSpan.mIndex, false);
    } 
  }
  
  private int updateSpecWithExtra(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 == 0 && paramInt3 == 0)
      return paramInt1; 
    int i = View.MeasureSpec.getMode(paramInt1);
    return (i == Integer.MIN_VALUE || i == 1073741824) ? View.MeasureSpec.makeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt1) - paramInt2 - paramInt3), i) : paramInt1;
  }
  
  boolean areAllEndsEqual() {
    int i = this.mSpans[0].getEndLine(-2147483648);
    for (byte b = 1; b < this.mSpanCount; b++) {
      if (this.mSpans[b].getEndLine(-2147483648) != i)
        return false; 
    } 
    return true;
  }
  
  boolean areAllStartsEqual() {
    int i = this.mSpans[0].getStartLine(-2147483648);
    for (byte b = 1; b < this.mSpanCount; b++) {
      if (this.mSpans[b].getStartLine(-2147483648) != i)
        return false; 
    } 
    return true;
  }
  
  public void assertNotInLayoutOrScroll(String paramString) {
    if (this.mPendingSavedState == null)
      super.assertNotInLayoutOrScroll(paramString); 
  }
  
  public boolean canScrollHorizontally() {
    boolean bool;
    if (this.mOrientation == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean canScrollVertically() {
    int i = this.mOrientation;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  boolean checkForGaps() {
    int i;
    int j;
    byte b;
    if (getChildCount() == 0 || this.mGapStrategy == 0 || !isAttachedToWindow())
      return false; 
    if (this.mShouldReverseLayout) {
      j = getLastChildPosition();
      i = getFirstChildPosition();
    } else {
      j = getFirstChildPosition();
      i = getLastChildPosition();
    } 
    if (j == 0 && hasGapsToFix() != null) {
      this.mLazySpanLookup.clear();
      requestSimpleAnimationsInNextLayout();
      requestLayout();
      return true;
    } 
    if (!this.mLaidOutInvalidFullSpan)
      return false; 
    if (this.mShouldReverseLayout) {
      b = -1;
    } else {
      b = 1;
    } 
    LazySpanLookup.FullSpanItem fullSpanItem1 = this.mLazySpanLookup.getFirstFullSpanItemInRange(j, i + 1, b, true);
    if (fullSpanItem1 == null) {
      this.mLaidOutInvalidFullSpan = false;
      this.mLazySpanLookup.forceInvalidateAfter(i + 1);
      return false;
    } 
    LazySpanLookup.FullSpanItem fullSpanItem2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(j, fullSpanItem1.mPosition, b * -1, true);
    if (fullSpanItem2 == null) {
      this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem1.mPosition);
    } else {
      this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem2.mPosition + 1);
    } 
    requestSimpleAnimationsInNextLayout();
    requestLayout();
    return true;
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    if (this.mOrientation != 0)
      paramInt1 = paramInt2; 
    if (getChildCount() == 0 || paramInt1 == 0)
      return; 
    prepareLayoutStateForDelta(paramInt1, paramState);
    int[] arrayOfInt = this.mPrefetchDistances;
    if (arrayOfInt == null || arrayOfInt.length < this.mSpanCount)
      this.mPrefetchDistances = new int[this.mSpanCount]; 
    paramInt1 = 0;
    paramInt2 = 0;
    while (paramInt2 < this.mSpanCount) {
      int j;
      if (this.mLayoutState.mItemDirection == -1) {
        j = this.mLayoutState.mStartLine - this.mSpans[paramInt2].getStartLine(this.mLayoutState.mStartLine);
      } else {
        j = this.mSpans[paramInt2].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
      } 
      int i = paramInt1;
      if (j >= 0) {
        this.mPrefetchDistances[paramInt1] = j;
        i = paramInt1 + 1;
      } 
      paramInt2++;
      paramInt1 = i;
    } 
    Arrays.sort(this.mPrefetchDistances, 0, paramInt1);
    for (paramInt2 = 0; paramInt2 < paramInt1 && this.mLayoutState.hasMore(paramState); paramInt2++) {
      paramLayoutPrefetchRegistry.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[paramInt2]);
      LayoutState layoutState = this.mLayoutState;
      layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
    } 
  }
  
  public int computeHorizontalScrollExtent(RecyclerView.State paramState) {
    return computeScrollExtent(paramState);
  }
  
  public int computeHorizontalScrollOffset(RecyclerView.State paramState) {
    return computeScrollOffset(paramState);
  }
  
  public int computeHorizontalScrollRange(RecyclerView.State paramState) {
    return computeScrollRange(paramState);
  }
  
  public PointF computeScrollVectorForPosition(int paramInt) {
    paramInt = calculateScrollDirectionForPosition(paramInt);
    PointF pointF = new PointF();
    if (paramInt == 0)
      return null; 
    if (this.mOrientation == 0) {
      pointF.x = paramInt;
      pointF.y = 0.0F;
    } else {
      pointF.x = 0.0F;
      pointF.y = paramInt;
    } 
    return pointF;
  }
  
  public int computeVerticalScrollExtent(RecyclerView.State paramState) {
    return computeScrollExtent(paramState);
  }
  
  public int computeVerticalScrollOffset(RecyclerView.State paramState) {
    return computeScrollOffset(paramState);
  }
  
  public int computeVerticalScrollRange(RecyclerView.State paramState) {
    return computeScrollRange(paramState);
  }
  
  public int[] findFirstCompletelyVisibleItemPositions(int[] paramArrayOfint) {
    if (paramArrayOfint == null) {
      paramArrayOfint = new int[this.mSpanCount];
    } else if (paramArrayOfint.length < this.mSpanCount) {
      throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length);
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      paramArrayOfint[b] = this.mSpans[b].findFirstCompletelyVisibleItemPosition(); 
    return paramArrayOfint;
  }
  
  View findFirstVisibleItemClosestToEnd(boolean paramBoolean) {
    int j = this.mPrimaryOrientation.getStartAfterPadding();
    int k = this.mPrimaryOrientation.getEndAfterPadding();
    View view = null;
    int i = getChildCount() - 1;
    while (i >= 0) {
      View view2 = getChildAt(i);
      int m = this.mPrimaryOrientation.getDecoratedStart(view2);
      int n = this.mPrimaryOrientation.getDecoratedEnd(view2);
      View view1 = view;
      if (n > j)
        if (m >= k) {
          view1 = view;
        } else {
          if (n <= k || !paramBoolean)
            return view2; 
          view1 = view;
          if (view == null)
            view1 = view2; 
        }  
      i--;
      view = view1;
    } 
    return view;
  }
  
  View findFirstVisibleItemClosestToStart(boolean paramBoolean) {
    int i = this.mPrimaryOrientation.getStartAfterPadding();
    int j = this.mPrimaryOrientation.getEndAfterPadding();
    int k = getChildCount();
    View view = null;
    byte b = 0;
    while (b < k) {
      View view2 = getChildAt(b);
      int m = this.mPrimaryOrientation.getDecoratedStart(view2);
      View view1 = view;
      if (this.mPrimaryOrientation.getDecoratedEnd(view2) > i)
        if (m >= j) {
          view1 = view;
        } else {
          if (m >= i || !paramBoolean)
            return view2; 
          view1 = view;
          if (view == null)
            view1 = view2; 
        }  
      b++;
      view = view1;
    } 
    return view;
  }
  
  int findFirstVisibleItemPositionInt() {
    int i;
    View view;
    if (this.mShouldReverseLayout) {
      view = findFirstVisibleItemClosestToEnd(true);
    } else {
      view = findFirstVisibleItemClosestToStart(true);
    } 
    if (view == null) {
      i = -1;
    } else {
      i = getPosition(view);
    } 
    return i;
  }
  
  public int[] findFirstVisibleItemPositions(int[] paramArrayOfint) {
    if (paramArrayOfint == null) {
      paramArrayOfint = new int[this.mSpanCount];
    } else if (paramArrayOfint.length < this.mSpanCount) {
      throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length);
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      paramArrayOfint[b] = this.mSpans[b].findFirstVisibleItemPosition(); 
    return paramArrayOfint;
  }
  
  public int[] findLastCompletelyVisibleItemPositions(int[] paramArrayOfint) {
    if (paramArrayOfint == null) {
      paramArrayOfint = new int[this.mSpanCount];
    } else if (paramArrayOfint.length < this.mSpanCount) {
      throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length);
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      paramArrayOfint[b] = this.mSpans[b].findLastCompletelyVisibleItemPosition(); 
    return paramArrayOfint;
  }
  
  public int[] findLastVisibleItemPositions(int[] paramArrayOfint) {
    if (paramArrayOfint == null) {
      paramArrayOfint = new int[this.mSpanCount];
    } else if (paramArrayOfint.length < this.mSpanCount) {
      throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length);
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      paramArrayOfint[b] = this.mSpans[b].findLastVisibleItemPosition(); 
    return paramArrayOfint;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : super.getColumnCountForAccessibility(paramRecycler, paramState);
  }
  
  int getFirstChildPosition() {
    int j = getChildCount();
    int i = 0;
    if (j != 0)
      i = getPosition(getChildAt(0)); 
    return i;
  }
  
  public int getGapStrategy() {
    return this.mGapStrategy;
  }
  
  int getLastChildPosition() {
    int i = getChildCount();
    if (i == 0) {
      i = 0;
    } else {
      i = getPosition(getChildAt(i - 1));
    } 
    return i;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public boolean getReverseLayout() {
    return this.mReverseLayout;
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : super.getRowCountForAccessibility(paramRecycler, paramState);
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  View hasGapsToFix() {
    int k;
    int i = getChildCount() - 1;
    BitSet bitSet = new BitSet(this.mSpanCount);
    bitSet.set(0, this.mSpanCount, true);
    int j = this.mOrientation;
    byte b = -1;
    if (j == 1 && isLayoutRTL()) {
      j = 1;
    } else {
      j = -1;
    } 
    if (this.mShouldReverseLayout) {
      k = 0 - 1;
    } else {
      boolean bool = false;
      k = i + 1;
      i = bool;
    } 
    if (i < k)
      b = 1; 
    int m;
    for (m = i; m != k; m += b) {
      View view = getChildAt(m);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (bitSet.get(layoutParams.mSpan.mIndex)) {
        if (checkSpanForGap(layoutParams.mSpan))
          return view; 
        bitSet.clear(layoutParams.mSpan.mIndex);
      } 
      if (!layoutParams.mFullSpan && m + b != k) {
        View view1 = getChildAt(m + b);
        int n = 0;
        i = 0;
        if (this.mShouldReverseLayout) {
          n = this.mPrimaryOrientation.getDecoratedEnd(view);
          int i1 = this.mPrimaryOrientation.getDecoratedEnd(view1);
          if (n < i1)
            return view; 
          if (n == i1)
            i = 1; 
        } else {
          int i2 = this.mPrimaryOrientation.getDecoratedStart(view);
          int i1 = this.mPrimaryOrientation.getDecoratedStart(view1);
          if (i2 > i1)
            return view; 
          i = n;
          if (i2 == i1)
            i = 1; 
        } 
        if (i != 0) {
          LayoutParams layoutParams1 = (LayoutParams)view1.getLayoutParams();
          if (layoutParams.mSpan.mIndex - layoutParams1.mSpan.mIndex < 0) {
            i = 1;
          } else {
            i = 0;
          } 
          if (j < 0) {
            n = 1;
          } else {
            n = 0;
          } 
          if (i != n)
            return view; 
        } 
      } 
    } 
    return null;
  }
  
  public void invalidateSpanAssignments() {
    this.mLazySpanLookup.clear();
    requestLayout();
  }
  
  public boolean isAutoMeasureEnabled() {
    boolean bool;
    if (this.mGapStrategy != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean isLayoutRTL() {
    int i = getLayoutDirection();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public void offsetChildrenHorizontal(int paramInt) {
    super.offsetChildrenHorizontal(paramInt);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].onOffset(paramInt); 
  }
  
  public void offsetChildrenVertical(int paramInt) {
    super.offsetChildrenVertical(paramInt);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].onOffset(paramInt); 
  }
  
  public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler) {
    super.onDetachedFromWindow(paramRecyclerView, paramRecycler);
    removeCallbacks(this.mCheckForGapsRunnable);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].clear(); 
    paramRecyclerView.requestLayout();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    int i;
    if (getChildCount() == 0)
      return null; 
    paramView = findContainingItemView(paramView);
    if (paramView == null)
      return null; 
    resolveShouldLayoutReverse();
    int k = convertFocusDirectionToLayoutDirection(paramInt);
    if (k == Integer.MIN_VALUE)
      return null; 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    boolean bool2 = layoutParams.mFullSpan;
    Span span = layoutParams.mSpan;
    if (k == 1) {
      paramInt = getLastChildPosition();
    } else {
      paramInt = getFirstChildPosition();
    } 
    updateLayoutState(paramInt, paramState);
    setLayoutStateDirection(k);
    LayoutState layoutState = this.mLayoutState;
    layoutState.mCurrentPosition = layoutState.mItemDirection + paramInt;
    this.mLayoutState.mAvailable = (int)(this.mPrimaryOrientation.getTotalSpace() * 0.33333334F);
    this.mLayoutState.mStopInFocusable = true;
    layoutState = this.mLayoutState;
    int j = 0;
    layoutState.mRecycle = false;
    fill(paramRecycler, this.mLayoutState, paramState);
    this.mLastLayoutFromEnd = this.mShouldReverseLayout;
    if (!bool2) {
      View view = span.getFocusableViewAfter(paramInt, k);
      if (view != null && view != paramView)
        return view; 
    } 
    if (preferLastSpan(k)) {
      for (i = this.mSpanCount - 1; i >= 0; i--) {
        View view = this.mSpans[i].getFocusableViewAfter(paramInt, k);
        if (view != null && view != paramView)
          return view; 
      } 
    } else {
      for (i = 0; i < this.mSpanCount; i++) {
        View view = this.mSpans[i].getFocusableViewAfter(paramInt, k);
        if (view != null && view != paramView)
          return view; 
      } 
    } 
    boolean bool1 = this.mReverseLayout;
    if (k == -1) {
      i = 1;
    } else {
      i = 0;
    } 
    paramInt = j;
    if ((bool1 ^ true) == i)
      paramInt = 1; 
    if (!bool2) {
      if (paramInt != 0) {
        i = span.findFirstPartiallyVisibleItemPosition();
      } else {
        i = span.findLastPartiallyVisibleItemPosition();
      } 
      View view = findViewByPosition(i);
      if (view != null && view != paramView)
        return view; 
    } 
    if (preferLastSpan(k)) {
      for (i = this.mSpanCount - 1; i >= 0; i--) {
        if (i != span.mIndex) {
          if (paramInt != 0) {
            j = this.mSpans[i].findFirstPartiallyVisibleItemPosition();
          } else {
            j = this.mSpans[i].findLastPartiallyVisibleItemPosition();
          } 
          View view = findViewByPosition(j);
          if (view != null && view != paramView)
            return view; 
        } 
      } 
    } else {
      for (i = 0; i < this.mSpanCount; i++) {
        if (paramInt != 0) {
          j = this.mSpans[i].findFirstPartiallyVisibleItemPosition();
        } else {
          j = this.mSpans[i].findLastPartiallyVisibleItemPosition();
        } 
        View view = findViewByPosition(j);
        if (view != null && view != paramView)
          return view; 
      } 
    } 
    return null;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (getChildCount() > 0) {
      View view1 = findFirstVisibleItemClosestToStart(false);
      View view2 = findFirstVisibleItemClosestToEnd(false);
      if (view1 == null || view2 == null)
        return; 
      int i = getPosition(view1);
      int j = getPosition(view2);
      if (i < j) {
        paramAccessibilityEvent.setFromIndex(i);
        paramAccessibilityEvent.setToIndex(j);
      } else {
        paramAccessibilityEvent.setFromIndex(j);
        paramAccessibilityEvent.setToIndex(i);
      } 
    } 
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int k = this.mOrientation;
    int j = 1;
    int i = 1;
    if (k == 0) {
      j = layoutParams.getSpanIndex();
      if (layoutParams.mFullSpan)
        i = this.mSpanCount; 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(j, i, -1, -1, false, false));
    } else {
      k = layoutParams.getSpanIndex();
      i = j;
      if (layoutParams.mFullSpan)
        i = this.mSpanCount; 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, k, i, false, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    handleUpdate(paramInt1, paramInt2, 1);
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mLazySpanLookup.clear();
    requestLayout();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    handleUpdate(paramInt1, paramInt2, 8);
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    handleUpdate(paramInt1, paramInt2, 2);
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    handleUpdate(paramInt1, paramInt2, 4);
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    onLayoutChildren(paramRecycler, paramState, true);
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingScrollPosition = -1;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    this.mPendingSavedState = null;
    this.mAnchorInfo.reset();
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof SavedState) {
      this.mPendingSavedState = (SavedState)paramParcelable;
      requestLayout();
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    if (this.mPendingSavedState != null)
      return new SavedState(this.mPendingSavedState); 
    SavedState savedState = new SavedState();
    savedState.mReverseLayout = this.mReverseLayout;
    savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
    savedState.mLastLayoutRTL = this.mLastLayoutRTL;
    LazySpanLookup lazySpanLookup = this.mLazySpanLookup;
    if (lazySpanLookup != null && lazySpanLookup.mData != null) {
      savedState.mSpanLookup = this.mLazySpanLookup.mData;
      savedState.mSpanLookupSize = savedState.mSpanLookup.length;
      savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
    } else {
      savedState.mSpanLookupSize = 0;
    } 
    if (getChildCount() > 0) {
      int i;
      if (this.mLastLayoutFromEnd) {
        i = getLastChildPosition();
      } else {
        i = getFirstChildPosition();
      } 
      savedState.mAnchorPosition = i;
      savedState.mVisibleAnchorPosition = findFirstVisibleItemPositionInt();
      savedState.mSpanOffsetsSize = this.mSpanCount;
      savedState.mSpanOffsets = new int[this.mSpanCount];
      for (byte b = 0; b < this.mSpanCount; b++) {
        if (this.mLastLayoutFromEnd) {
          int j = this.mSpans[b].getEndLine(-2147483648);
          i = j;
          if (j != Integer.MIN_VALUE)
            i = j - this.mPrimaryOrientation.getEndAfterPadding(); 
        } else {
          int j = this.mSpans[b].getStartLine(-2147483648);
          i = j;
          if (j != Integer.MIN_VALUE)
            i = j - this.mPrimaryOrientation.getStartAfterPadding(); 
        } 
        savedState.mSpanOffsets[b] = i;
      } 
    } else {
      savedState.mAnchorPosition = -1;
      savedState.mVisibleAnchorPosition = -1;
      savedState.mSpanOffsetsSize = 0;
    } 
    return savedState;
  }
  
  public void onScrollStateChanged(int paramInt) {
    if (paramInt == 0)
      checkForGaps(); 
  }
  
  void prepareLayoutStateForDelta(int paramInt, RecyclerView.State paramState) {
    byte b;
    int i;
    if (paramInt > 0) {
      b = 1;
      i = getLastChildPosition();
    } else {
      b = -1;
      i = getFirstChildPosition();
    } 
    this.mLayoutState.mRecycle = true;
    updateLayoutState(i, paramState);
    setLayoutStateDirection(b);
    LayoutState layoutState = this.mLayoutState;
    layoutState.mCurrentPosition = layoutState.mItemDirection + i;
    this.mLayoutState.mAvailable = Math.abs(paramInt);
  }
  
  int scrollBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (getChildCount() == 0 || paramInt == 0)
      return 0; 
    prepareLayoutStateForDelta(paramInt, paramState);
    int i = fill(paramRecycler, this.mLayoutState, paramState);
    if (this.mLayoutState.mAvailable >= i)
      if (paramInt < 0) {
        paramInt = -i;
      } else {
        paramInt = i;
      }  
    this.mPrimaryOrientation.offsetChildren(-paramInt);
    this.mLastLayoutFromEnd = this.mShouldReverseLayout;
    this.mLayoutState.mAvailable = 0;
    recycle(paramRecycler, this.mLayoutState);
    return paramInt;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void scrollToPosition(int paramInt) {
    SavedState savedState = this.mPendingSavedState;
    if (savedState != null && savedState.mAnchorPosition != paramInt)
      this.mPendingSavedState.invalidateAnchorPositionInfo(); 
    this.mPendingScrollPosition = paramInt;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    requestLayout();
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2) {
    SavedState savedState = this.mPendingSavedState;
    if (savedState != null)
      savedState.invalidateAnchorPositionInfo(); 
    this.mPendingScrollPosition = paramInt1;
    this.mPendingScrollPositionOffset = paramInt2;
    requestLayout();
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void setGapStrategy(int paramInt) {
    assertNotInLayoutOrScroll((String)null);
    if (paramInt == this.mGapStrategy)
      return; 
    if (paramInt == 0 || paramInt == 2) {
      this.mGapStrategy = paramInt;
      requestLayout();
      return;
    } 
    throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      paramInt1 = chooseSize(paramInt1, this.mSizePerSpan * this.mSpanCount + i, getMinimumWidth());
    } else {
      paramInt1 = chooseSize(paramInt1, paramRect.width() + i, getMinimumWidth());
      paramInt2 = chooseSize(paramInt2, this.mSizePerSpan * this.mSpanCount + j, getMinimumHeight());
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt == 0 || paramInt == 1) {
      assertNotInLayoutOrScroll((String)null);
      if (paramInt == this.mOrientation)
        return; 
      this.mOrientation = paramInt;
      OrientationHelper orientationHelper = this.mPrimaryOrientation;
      this.mPrimaryOrientation = this.mSecondaryOrientation;
      this.mSecondaryOrientation = orientationHelper;
      requestLayout();
      return;
    } 
    throw new IllegalArgumentException("invalid orientation.");
  }
  
  public void setReverseLayout(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    SavedState savedState = this.mPendingSavedState;
    if (savedState != null && savedState.mReverseLayout != paramBoolean)
      this.mPendingSavedState.mReverseLayout = paramBoolean; 
    this.mReverseLayout = paramBoolean;
    requestLayout();
  }
  
  public void setSpanCount(int paramInt) {
    assertNotInLayoutOrScroll((String)null);
    if (paramInt != this.mSpanCount) {
      invalidateSpanAssignments();
      this.mSpanCount = paramInt;
      this.mRemainingSpans = new BitSet(this.mSpanCount);
      this.mSpans = new Span[this.mSpanCount];
      for (paramInt = 0; paramInt < this.mSpanCount; paramInt++)
        this.mSpans[paramInt] = new Span(paramInt); 
      requestLayout();
    } 
  }
  
  public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt) {
    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(paramRecyclerView.getContext());
    linearSmoothScroller.setTargetPosition(paramInt);
    startSmoothScroll(linearSmoothScroller);
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean updateAnchorFromPendingData(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    boolean bool1 = paramState.isPreLayout();
    boolean bool = false;
    if (!bool1) {
      int i = this.mPendingScrollPosition;
      if (i == -1)
        return false; 
      if (i < 0 || i >= paramState.getItemCount()) {
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        return false;
      } 
      SavedState savedState = this.mPendingSavedState;
      if (savedState == null || savedState.mAnchorPosition == -1 || this.mPendingSavedState.mSpanOffsetsSize < 1) {
        View view = findViewByPosition(this.mPendingScrollPosition);
        if (view != null) {
          if (this.mShouldReverseLayout) {
            i = getLastChildPosition();
          } else {
            i = getFirstChildPosition();
          } 
          paramAnchorInfo.mPosition = i;
          if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
            if (paramAnchorInfo.mLayoutFromEnd) {
              paramAnchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(view);
            } else {
              paramAnchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(view);
            } 
            return true;
          } 
          if (this.mPrimaryOrientation.getDecoratedMeasurement(view) > this.mPrimaryOrientation.getTotalSpace()) {
            if (paramAnchorInfo.mLayoutFromEnd) {
              i = this.mPrimaryOrientation.getEndAfterPadding();
            } else {
              i = this.mPrimaryOrientation.getStartAfterPadding();
            } 
            paramAnchorInfo.mOffset = i;
            return true;
          } 
          i = this.mPrimaryOrientation.getDecoratedStart(view) - this.mPrimaryOrientation.getStartAfterPadding();
          if (i < 0) {
            paramAnchorInfo.mOffset = -i;
            return true;
          } 
          i = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(view);
          if (i < 0) {
            paramAnchorInfo.mOffset = i;
            return true;
          } 
          paramAnchorInfo.mOffset = Integer.MIN_VALUE;
        } else {
          paramAnchorInfo.mPosition = this.mPendingScrollPosition;
          i = this.mPendingScrollPositionOffset;
          if (i == Integer.MIN_VALUE) {
            if (calculateScrollDirectionForPosition(paramAnchorInfo.mPosition) == 1)
              bool = true; 
            paramAnchorInfo.mLayoutFromEnd = bool;
            paramAnchorInfo.assignCoordinateFromPadding();
          } else {
            paramAnchorInfo.assignCoordinateFromPadding(i);
          } 
          paramAnchorInfo.mInvalidateOffsets = true;
        } 
        return true;
      } 
      paramAnchorInfo.mOffset = Integer.MIN_VALUE;
      paramAnchorInfo.mPosition = this.mPendingScrollPosition;
      return true;
    } 
    return false;
  }
  
  void updateAnchorInfoForLayout(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    if (updateAnchorFromPendingData(paramState, paramAnchorInfo))
      return; 
    if (updateAnchorFromChildren(paramState, paramAnchorInfo))
      return; 
    paramAnchorInfo.assignCoordinateFromPadding();
    paramAnchorInfo.mPosition = 0;
  }
  
  void updateMeasureSpecs(int paramInt) {
    this.mSizePerSpan = paramInt / this.mSpanCount;
    this.mFullSizeSpec = View.MeasureSpec.makeMeasureSpec(paramInt, this.mSecondaryOrientation.getMode());
  }
  
  class AnchorInfo {
    boolean mInvalidateOffsets;
    
    boolean mLayoutFromEnd;
    
    int mOffset;
    
    int mPosition;
    
    int[] mSpanReferenceLines;
    
    boolean mValid;
    
    final StaggeredGridLayoutManager this$0;
    
    AnchorInfo() {
      reset();
    }
    
    void assignCoordinateFromPadding() {
      int i;
      if (this.mLayoutFromEnd) {
        i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
      } else {
        i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
      } 
      this.mOffset = i;
    }
    
    void assignCoordinateFromPadding(int param1Int) {
      if (this.mLayoutFromEnd) {
        this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - param1Int;
      } else {
        this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + param1Int;
      } 
    }
    
    void reset() {
      this.mPosition = -1;
      this.mOffset = Integer.MIN_VALUE;
      this.mLayoutFromEnd = false;
      this.mInvalidateOffsets = false;
      this.mValid = false;
      int[] arrayOfInt = this.mSpanReferenceLines;
      if (arrayOfInt != null)
        Arrays.fill(arrayOfInt, -1); 
    }
    
    void saveSpanReferenceLines(StaggeredGridLayoutManager.Span[] param1ArrayOfSpan) {
      int i = param1ArrayOfSpan.length;
      int[] arrayOfInt = this.mSpanReferenceLines;
      if (arrayOfInt == null || arrayOfInt.length < i)
        this.mSpanReferenceLines = new int[StaggeredGridLayoutManager.this.mSpans.length]; 
      for (byte b = 0; b < i; b++)
        this.mSpanReferenceLines[b] = param1ArrayOfSpan[b].getStartLine(-2147483648); 
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    boolean mFullSpan;
    
    StaggeredGridLayoutManager.Span mSpan;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public final int getSpanIndex() {
      StaggeredGridLayoutManager.Span span = this.mSpan;
      return (span == null) ? -1 : span.mIndex;
    }
    
    public boolean isFullSpan() {
      return this.mFullSpan;
    }
    
    public void setFullSpan(boolean param1Boolean) {
      this.mFullSpan = param1Boolean;
    }
  }
  
  static class LazySpanLookup {
    private static final int MIN_SIZE = 10;
    
    int[] mData;
    
    List<FullSpanItem> mFullSpanItems;
    
    private int invalidateFullSpansAfter(int param1Int) {
      byte b1;
      if (this.mFullSpanItems == null)
        return -1; 
      FullSpanItem fullSpanItem = getFullSpanItem(param1Int);
      if (fullSpanItem != null)
        this.mFullSpanItems.remove(fullSpanItem); 
      byte b2 = -1;
      int i = this.mFullSpanItems.size();
      byte b = 0;
      while (true) {
        b1 = b2;
        if (b < i) {
          if (((FullSpanItem)this.mFullSpanItems.get(b)).mPosition >= param1Int) {
            b1 = b;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (b1 != -1) {
        fullSpanItem = this.mFullSpanItems.get(b1);
        this.mFullSpanItems.remove(b1);
        return fullSpanItem.mPosition;
      } 
      return -1;
    }
    
    private void offsetFullSpansForAddition(int param1Int1, int param1Int2) {
      List<FullSpanItem> list = this.mFullSpanItems;
      if (list == null)
        return; 
      for (int i = list.size() - 1; i >= 0; i--) {
        FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
        if (fullSpanItem.mPosition >= param1Int1)
          fullSpanItem.mPosition += param1Int2; 
      } 
    }
    
    private void offsetFullSpansForRemoval(int param1Int1, int param1Int2) {
      List<FullSpanItem> list = this.mFullSpanItems;
      if (list == null)
        return; 
      for (int i = list.size() - 1; i >= 0; i--) {
        FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
        if (fullSpanItem.mPosition >= param1Int1)
          if (fullSpanItem.mPosition < param1Int1 + param1Int2) {
            this.mFullSpanItems.remove(i);
          } else {
            fullSpanItem.mPosition -= param1Int2;
          }  
      } 
    }
    
    public void addFullSpanItem(FullSpanItem param1FullSpanItem) {
      if (this.mFullSpanItems == null)
        this.mFullSpanItems = new ArrayList<>(); 
      int i = this.mFullSpanItems.size();
      for (byte b = 0; b < i; b++) {
        FullSpanItem fullSpanItem = this.mFullSpanItems.get(b);
        if (fullSpanItem.mPosition == param1FullSpanItem.mPosition)
          this.mFullSpanItems.remove(b); 
        if (fullSpanItem.mPosition >= param1FullSpanItem.mPosition) {
          this.mFullSpanItems.add(b, param1FullSpanItem);
          return;
        } 
      } 
      this.mFullSpanItems.add(param1FullSpanItem);
    }
    
    void clear() {
      int[] arrayOfInt = this.mData;
      if (arrayOfInt != null)
        Arrays.fill(arrayOfInt, -1); 
      this.mFullSpanItems = null;
    }
    
    void ensureSize(int param1Int) {
      int[] arrayOfInt = this.mData;
      if (arrayOfInt == null) {
        arrayOfInt = new int[Math.max(param1Int, 10) + 1];
        this.mData = arrayOfInt;
        Arrays.fill(arrayOfInt, -1);
      } else if (param1Int >= arrayOfInt.length) {
        arrayOfInt = this.mData;
        int[] arrayOfInt1 = new int[sizeForPosition(param1Int)];
        this.mData = arrayOfInt1;
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
        arrayOfInt1 = this.mData;
        Arrays.fill(arrayOfInt1, arrayOfInt.length, arrayOfInt1.length, -1);
      } 
    }
    
    int forceInvalidateAfter(int param1Int) {
      List<FullSpanItem> list = this.mFullSpanItems;
      if (list != null)
        for (int i = list.size() - 1; i >= 0; i--) {
          if (((FullSpanItem)this.mFullSpanItems.get(i)).mPosition >= param1Int)
            this.mFullSpanItems.remove(i); 
        }  
      return invalidateAfter(param1Int);
    }
    
    public FullSpanItem getFirstFullSpanItemInRange(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      List<FullSpanItem> list = this.mFullSpanItems;
      if (list == null)
        return null; 
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        FullSpanItem fullSpanItem = this.mFullSpanItems.get(b);
        if (fullSpanItem.mPosition >= param1Int2)
          return null; 
        if (fullSpanItem.mPosition >= param1Int1 && (param1Int3 == 0 || fullSpanItem.mGapDir == param1Int3 || (param1Boolean && fullSpanItem.mHasUnwantedGapAfter)))
          return fullSpanItem; 
      } 
      return null;
    }
    
    public FullSpanItem getFullSpanItem(int param1Int) {
      List<FullSpanItem> list = this.mFullSpanItems;
      if (list == null)
        return null; 
      for (int i = list.size() - 1; i >= 0; i--) {
        FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
        if (fullSpanItem.mPosition == param1Int)
          return fullSpanItem; 
      } 
      return null;
    }
    
    int getSpan(int param1Int) {
      int[] arrayOfInt = this.mData;
      return (arrayOfInt == null || param1Int >= arrayOfInt.length) ? -1 : arrayOfInt[param1Int];
    }
    
    int invalidateAfter(int param1Int) {
      int[] arrayOfInt = this.mData;
      if (arrayOfInt == null)
        return -1; 
      if (param1Int >= arrayOfInt.length)
        return -1; 
      int i = invalidateFullSpansAfter(param1Int);
      if (i == -1) {
        arrayOfInt = this.mData;
        Arrays.fill(arrayOfInt, param1Int, arrayOfInt.length, -1);
        return this.mData.length;
      } 
      Arrays.fill(this.mData, param1Int, i + 1, -1);
      return i + 1;
    }
    
    void offsetForAddition(int param1Int1, int param1Int2) {
      int[] arrayOfInt = this.mData;
      if (arrayOfInt == null || param1Int1 >= arrayOfInt.length)
        return; 
      ensureSize(param1Int1 + param1Int2);
      arrayOfInt = this.mData;
      System.arraycopy(arrayOfInt, param1Int1, arrayOfInt, param1Int1 + param1Int2, arrayOfInt.length - param1Int1 - param1Int2);
      Arrays.fill(this.mData, param1Int1, param1Int1 + param1Int2, -1);
      offsetFullSpansForAddition(param1Int1, param1Int2);
    }
    
    void offsetForRemoval(int param1Int1, int param1Int2) {
      int[] arrayOfInt = this.mData;
      if (arrayOfInt == null || param1Int1 >= arrayOfInt.length)
        return; 
      ensureSize(param1Int1 + param1Int2);
      arrayOfInt = this.mData;
      System.arraycopy(arrayOfInt, param1Int1 + param1Int2, arrayOfInt, param1Int1, arrayOfInt.length - param1Int1 - param1Int2);
      arrayOfInt = this.mData;
      Arrays.fill(arrayOfInt, arrayOfInt.length - param1Int2, arrayOfInt.length, -1);
      offsetFullSpansForRemoval(param1Int1, param1Int2);
    }
    
    void setSpan(int param1Int, StaggeredGridLayoutManager.Span param1Span) {
      ensureSize(param1Int);
      this.mData[param1Int] = param1Span.mIndex;
    }
    
    int sizeForPosition(int param1Int) {
      int i;
      for (i = this.mData.length; i <= param1Int; i *= 2);
      return i;
    }
    
    static class FullSpanItem implements Parcelable {
      public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>() {
          public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param3Parcel) {
            return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param3Parcel);
          }
          
          public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param3Int) {
            return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param3Int];
          }
        };
      
      int mGapDir;
      
      int[] mGapPerSpan;
      
      boolean mHasUnwantedGapAfter;
      
      int mPosition;
      
      FullSpanItem() {}
      
      FullSpanItem(Parcel param2Parcel) {
        this.mPosition = param2Parcel.readInt();
        this.mGapDir = param2Parcel.readInt();
        int i = param2Parcel.readInt();
        boolean bool = true;
        if (i != 1)
          bool = false; 
        this.mHasUnwantedGapAfter = bool;
        i = param2Parcel.readInt();
        if (i > 0) {
          int[] arrayOfInt = new int[i];
          this.mGapPerSpan = arrayOfInt;
          param2Parcel.readIntArray(arrayOfInt);
        } 
      }
      
      public int describeContents() {
        return 0;
      }
      
      int getGapForSpan(int param2Int) {
        int[] arrayOfInt = this.mGapPerSpan;
        if (arrayOfInt == null) {
          param2Int = 0;
        } else {
          param2Int = arrayOfInt[param2Int];
        } 
        return param2Int;
      }
      
      public String toString() {
        return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        param2Parcel.writeInt(this.mPosition);
        param2Parcel.writeInt(this.mGapDir);
        param2Parcel.writeInt(this.mHasUnwantedGapAfter);
        int[] arrayOfInt = this.mGapPerSpan;
        if (arrayOfInt != null && arrayOfInt.length > 0) {
          param2Parcel.writeInt(arrayOfInt.length);
          param2Parcel.writeIntArray(this.mGapPerSpan);
        } else {
          param2Parcel.writeInt(0);
        } 
      }
    }
    
    static final class null implements Parcelable.Creator<FullSpanItem> {
      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param2Parcel) {
        return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param2Parcel);
      }
      
      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param2Int) {
        return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param2Int];
      }
    }
  }
  
  static class FullSpanItem implements Parcelable {
    public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>() {
        public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param3Parcel) {
          return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param3Parcel);
        }
        
        public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param3Int) {
          return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param3Int];
        }
      };
    
    int mGapDir;
    
    int[] mGapPerSpan;
    
    boolean mHasUnwantedGapAfter;
    
    int mPosition;
    
    FullSpanItem() {}
    
    FullSpanItem(Parcel param1Parcel) {
      this.mPosition = param1Parcel.readInt();
      this.mGapDir = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.mHasUnwantedGapAfter = bool;
      i = param1Parcel.readInt();
      if (i > 0) {
        int[] arrayOfInt = new int[i];
        this.mGapPerSpan = arrayOfInt;
        param1Parcel.readIntArray(arrayOfInt);
      } 
    }
    
    public int describeContents() {
      return 0;
    }
    
    int getGapForSpan(int param1Int) {
      int[] arrayOfInt = this.mGapPerSpan;
      if (arrayOfInt == null) {
        param1Int = 0;
      } else {
        param1Int = arrayOfInt[param1Int];
      } 
      return param1Int;
    }
    
    public String toString() {
      return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mPosition);
      param1Parcel.writeInt(this.mGapDir);
      param1Parcel.writeInt(this.mHasUnwantedGapAfter);
      int[] arrayOfInt = this.mGapPerSpan;
      if (arrayOfInt != null && arrayOfInt.length > 0) {
        param1Parcel.writeInt(arrayOfInt.length);
        param1Parcel.writeIntArray(this.mGapPerSpan);
      } else {
        param1Parcel.writeInt(0);
      } 
    }
  }
  
  static final class null implements Parcelable.Creator<LazySpanLookup.FullSpanItem> {
    public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param1Parcel) {
      return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param1Parcel);
    }
    
    public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param1Int) {
      return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param1Int];
    }
  }
  
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel param2Parcel) {
          return new StaggeredGridLayoutManager.SavedState(param2Parcel);
        }
        
        public StaggeredGridLayoutManager.SavedState[] newArray(int param2Int) {
          return new StaggeredGridLayoutManager.SavedState[param2Int];
        }
      };
    
    boolean mAnchorLayoutFromEnd;
    
    int mAnchorPosition;
    
    List<StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem> mFullSpanItems;
    
    boolean mLastLayoutRTL;
    
    boolean mReverseLayout;
    
    int[] mSpanLookup;
    
    int mSpanLookupSize;
    
    int[] mSpanOffsets;
    
    int mSpanOffsetsSize;
    
    int mVisibleAnchorPosition;
    
    public SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.mAnchorPosition = param1Parcel.readInt();
      this.mVisibleAnchorPosition = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      this.mSpanOffsetsSize = i;
      if (i > 0) {
        int[] arrayOfInt = new int[i];
        this.mSpanOffsets = arrayOfInt;
        param1Parcel.readIntArray(arrayOfInt);
      } 
      i = param1Parcel.readInt();
      this.mSpanLookupSize = i;
      if (i > 0) {
        int[] arrayOfInt = new int[i];
        this.mSpanLookup = arrayOfInt;
        param1Parcel.readIntArray(arrayOfInt);
      } 
      i = param1Parcel.readInt();
      boolean bool2 = false;
      if (i == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.mReverseLayout = bool1;
      if (param1Parcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.mAnchorLayoutFromEnd = bool1;
      boolean bool1 = bool2;
      if (param1Parcel.readInt() == 1)
        bool1 = true; 
      this.mLastLayoutRTL = bool1;
      this.mFullSpanItems = param1Parcel.readArrayList(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem.class.getClassLoader());
    }
    
    public SavedState(SavedState param1SavedState) {
      this.mSpanOffsetsSize = param1SavedState.mSpanOffsetsSize;
      this.mAnchorPosition = param1SavedState.mAnchorPosition;
      this.mVisibleAnchorPosition = param1SavedState.mVisibleAnchorPosition;
      this.mSpanOffsets = param1SavedState.mSpanOffsets;
      this.mSpanLookupSize = param1SavedState.mSpanLookupSize;
      this.mSpanLookup = param1SavedState.mSpanLookup;
      this.mReverseLayout = param1SavedState.mReverseLayout;
      this.mAnchorLayoutFromEnd = param1SavedState.mAnchorLayoutFromEnd;
      this.mLastLayoutRTL = param1SavedState.mLastLayoutRTL;
      this.mFullSpanItems = param1SavedState.mFullSpanItems;
    }
    
    public int describeContents() {
      return 0;
    }
    
    void invalidateAnchorPositionInfo() {
      this.mSpanOffsets = null;
      this.mSpanOffsetsSize = 0;
      this.mAnchorPosition = -1;
      this.mVisibleAnchorPosition = -1;
    }
    
    void invalidateSpanInfo() {
      this.mSpanOffsets = null;
      this.mSpanOffsetsSize = 0;
      this.mSpanLookupSize = 0;
      this.mSpanLookup = null;
      this.mFullSpanItems = null;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mAnchorPosition);
      param1Parcel.writeInt(this.mVisibleAnchorPosition);
      param1Parcel.writeInt(this.mSpanOffsetsSize);
      if (this.mSpanOffsetsSize > 0)
        param1Parcel.writeIntArray(this.mSpanOffsets); 
      param1Parcel.writeInt(this.mSpanLookupSize);
      if (this.mSpanLookupSize > 0)
        param1Parcel.writeIntArray(this.mSpanLookup); 
      param1Parcel.writeInt(this.mReverseLayout);
      param1Parcel.writeInt(this.mAnchorLayoutFromEnd);
      param1Parcel.writeInt(this.mLastLayoutRTL);
      param1Parcel.writeList(this.mFullSpanItems);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel param1Parcel) {
      return new StaggeredGridLayoutManager.SavedState(param1Parcel);
    }
    
    public StaggeredGridLayoutManager.SavedState[] newArray(int param1Int) {
      return new StaggeredGridLayoutManager.SavedState[param1Int];
    }
  }
  
  class Span {
    static final int INVALID_LINE = -2147483648;
    
    int mCachedEnd = Integer.MIN_VALUE;
    
    int mCachedStart = Integer.MIN_VALUE;
    
    int mDeletedSize = 0;
    
    final int mIndex;
    
    ArrayList<View> mViews = new ArrayList<>();
    
    final StaggeredGridLayoutManager this$0;
    
    Span(int param1Int) {
      this.mIndex = param1Int;
    }
    
    void appendToSpan(View param1View) {
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(param1View);
      layoutParams.mSpan = this;
      this.mViews.add(param1View);
      this.mCachedEnd = Integer.MIN_VALUE;
      if (this.mViews.size() == 1)
        this.mCachedStart = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(param1View); 
    }
    
    void cacheReferenceLineAndClear(boolean param1Boolean, int param1Int) {
      int i;
      if (param1Boolean) {
        i = getEndLine(-2147483648);
      } else {
        i = getStartLine(-2147483648);
      } 
      clear();
      if (i == Integer.MIN_VALUE)
        return; 
      if ((param1Boolean && i < StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) || (!param1Boolean && i > StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding()))
        return; 
      int j = i;
      if (param1Int != Integer.MIN_VALUE)
        j = i + param1Int; 
      this.mCachedEnd = j;
      this.mCachedStart = j;
    }
    
    void calculateCachedEnd() {
      ArrayList<View> arrayList = this.mViews;
      View view = arrayList.get(arrayList.size() - 1);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
      if (layoutParams.mFullSpan) {
        StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition());
        if (fullSpanItem != null && fullSpanItem.mGapDir == 1)
          this.mCachedEnd += fullSpanItem.getGapForSpan(this.mIndex); 
      } 
    }
    
    void calculateCachedStart() {
      View view = this.mViews.get(0);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
      if (layoutParams.mFullSpan) {
        StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition());
        if (fullSpanItem != null && fullSpanItem.mGapDir == -1)
          this.mCachedStart -= fullSpanItem.getGapForSpan(this.mIndex); 
      } 
    }
    
    void clear() {
      this.mViews.clear();
      invalidateCache();
      this.mDeletedSize = 0;
    }
    
    public int findFirstCompletelyVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOneVisibleChild(this.mViews.size() - 1, -1, true);
      } else {
        i = findOneVisibleChild(0, this.mViews.size(), true);
      } 
      return i;
    }
    
    public int findFirstPartiallyVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
      } else {
        i = findOnePartiallyVisibleChild(0, this.mViews.size(), true);
      } 
      return i;
    }
    
    public int findFirstVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOneVisibleChild(this.mViews.size() - 1, -1, false);
      } else {
        i = findOneVisibleChild(0, this.mViews.size(), false);
      } 
      return i;
    }
    
    public int findLastCompletelyVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOneVisibleChild(0, this.mViews.size(), true);
      } else {
        i = findOneVisibleChild(this.mViews.size() - 1, -1, true);
      } 
      return i;
    }
    
    public int findLastPartiallyVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOnePartiallyVisibleChild(0, this.mViews.size(), true);
      } else {
        i = findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
      } 
      return i;
    }
    
    public int findLastVisibleItemPosition() {
      int i;
      if (StaggeredGridLayoutManager.this.mReverseLayout) {
        i = findOneVisibleChild(0, this.mViews.size(), false);
      } else {
        i = findOneVisibleChild(this.mViews.size() - 1, -1, false);
      } 
      return i;
    }
    
    int findOnePartiallyOrCompletelyVisibleChild(int param1Int1, int param1Int2, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3) {
      byte b;
      int j = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
      int i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
      if (param1Int2 > param1Int1) {
        b = 1;
      } else {
        b = -1;
      } 
      while (param1Int1 != param1Int2) {
        boolean bool1;
        View view = this.mViews.get(param1Int1);
        int m = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
        int k = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
        boolean bool2 = false;
        if (param1Boolean3 ? (m <= i) : (m < i)) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (param1Boolean3 ? (k >= j) : (k > j))
          bool2 = true; 
        if (bool1 && bool2)
          if (param1Boolean1 && param1Boolean2) {
            if (m >= j && k <= i)
              return StaggeredGridLayoutManager.this.getPosition(view); 
          } else {
            if (param1Boolean2)
              return StaggeredGridLayoutManager.this.getPosition(view); 
            if (m < j || k > i)
              return StaggeredGridLayoutManager.this.getPosition(view); 
          }  
        param1Int1 += b;
      } 
      return -1;
    }
    
    int findOnePartiallyVisibleChild(int param1Int1, int param1Int2, boolean param1Boolean) {
      return findOnePartiallyOrCompletelyVisibleChild(param1Int1, param1Int2, false, false, param1Boolean);
    }
    
    int findOneVisibleChild(int param1Int1, int param1Int2, boolean param1Boolean) {
      return findOnePartiallyOrCompletelyVisibleChild(param1Int1, param1Int2, param1Boolean, true, false);
    }
    
    public int getDeletedSize() {
      return this.mDeletedSize;
    }
    
    int getEndLine() {
      int i = this.mCachedEnd;
      if (i != Integer.MIN_VALUE)
        return i; 
      calculateCachedEnd();
      return this.mCachedEnd;
    }
    
    int getEndLine(int param1Int) {
      int i = this.mCachedEnd;
      if (i != Integer.MIN_VALUE)
        return i; 
      if (this.mViews.size() == 0)
        return param1Int; 
      calculateCachedEnd();
      return this.mCachedEnd;
    }
    
    public View getFocusableViewAfter(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: aconst_null
      //   1: astore #5
      //   3: aconst_null
      //   4: astore #4
      //   6: iload_2
      //   7: iconst_m1
      //   8: if_icmpne -> 113
      //   11: aload_0
      //   12: getfield mViews : Ljava/util/ArrayList;
      //   15: invokevirtual size : ()I
      //   18: istore_3
      //   19: iconst_0
      //   20: istore_2
      //   21: iload_2
      //   22: iload_3
      //   23: if_icmpge -> 106
      //   26: aload_0
      //   27: getfield mViews : Ljava/util/ArrayList;
      //   30: iload_2
      //   31: invokevirtual get : (I)Ljava/lang/Object;
      //   34: checkcast android/view/View
      //   37: astore #5
      //   39: aload_0
      //   40: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   43: getfield mReverseLayout : Z
      //   46: ifeq -> 62
      //   49: aload_0
      //   50: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   53: aload #5
      //   55: invokevirtual getPosition : (Landroid/view/View;)I
      //   58: iload_1
      //   59: if_icmple -> 106
      //   62: aload_0
      //   63: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   66: getfield mReverseLayout : Z
      //   69: ifne -> 88
      //   72: aload_0
      //   73: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   76: aload #5
      //   78: invokevirtual getPosition : (Landroid/view/View;)I
      //   81: iload_1
      //   82: if_icmplt -> 88
      //   85: goto -> 106
      //   88: aload #5
      //   90: invokevirtual hasFocusable : ()Z
      //   93: ifeq -> 106
      //   96: aload #5
      //   98: astore #4
      //   100: iinc #2, 1
      //   103: goto -> 21
      //   106: aload #4
      //   108: astore #5
      //   110: goto -> 227
      //   113: aload_0
      //   114: getfield mViews : Ljava/util/ArrayList;
      //   117: invokevirtual size : ()I
      //   120: iconst_1
      //   121: isub
      //   122: istore_2
      //   123: aload #5
      //   125: astore #4
      //   127: aload #4
      //   129: astore #5
      //   131: iload_2
      //   132: iflt -> 227
      //   135: aload_0
      //   136: getfield mViews : Ljava/util/ArrayList;
      //   139: iload_2
      //   140: invokevirtual get : (I)Ljava/lang/Object;
      //   143: checkcast android/view/View
      //   146: astore #6
      //   148: aload_0
      //   149: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   152: getfield mReverseLayout : Z
      //   155: ifeq -> 175
      //   158: aload #4
      //   160: astore #5
      //   162: aload_0
      //   163: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   166: aload #6
      //   168: invokevirtual getPosition : (Landroid/view/View;)I
      //   171: iload_1
      //   172: if_icmpge -> 227
      //   175: aload_0
      //   176: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   179: getfield mReverseLayout : Z
      //   182: ifne -> 205
      //   185: aload_0
      //   186: getfield this$0 : Landroidx/recyclerview/widget/StaggeredGridLayoutManager;
      //   189: aload #6
      //   191: invokevirtual getPosition : (Landroid/view/View;)I
      //   194: iload_1
      //   195: if_icmpgt -> 205
      //   198: aload #4
      //   200: astore #5
      //   202: goto -> 227
      //   205: aload #4
      //   207: astore #5
      //   209: aload #6
      //   211: invokevirtual hasFocusable : ()Z
      //   214: ifeq -> 227
      //   217: aload #6
      //   219: astore #4
      //   221: iinc #2, -1
      //   224: goto -> 127
      //   227: aload #5
      //   229: areturn
    }
    
    StaggeredGridLayoutManager.LayoutParams getLayoutParams(View param1View) {
      return (StaggeredGridLayoutManager.LayoutParams)param1View.getLayoutParams();
    }
    
    int getStartLine() {
      int i = this.mCachedStart;
      if (i != Integer.MIN_VALUE)
        return i; 
      calculateCachedStart();
      return this.mCachedStart;
    }
    
    int getStartLine(int param1Int) {
      int i = this.mCachedStart;
      if (i != Integer.MIN_VALUE)
        return i; 
      if (this.mViews.size() == 0)
        return param1Int; 
      calculateCachedStart();
      return this.mCachedStart;
    }
    
    void invalidateCache() {
      this.mCachedStart = Integer.MIN_VALUE;
      this.mCachedEnd = Integer.MIN_VALUE;
    }
    
    void onOffset(int param1Int) {
      int i = this.mCachedStart;
      if (i != Integer.MIN_VALUE)
        this.mCachedStart = i + param1Int; 
      i = this.mCachedEnd;
      if (i != Integer.MIN_VALUE)
        this.mCachedEnd = i + param1Int; 
    }
    
    void popEnd() {
      int i = this.mViews.size();
      View view = this.mViews.remove(i - 1);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      layoutParams.mSpan = null;
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view); 
      if (i == 1)
        this.mCachedStart = Integer.MIN_VALUE; 
      this.mCachedEnd = Integer.MIN_VALUE;
    }
    
    void popStart() {
      View view = this.mViews.remove(0);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      layoutParams.mSpan = null;
      if (this.mViews.size() == 0)
        this.mCachedEnd = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view); 
      this.mCachedStart = Integer.MIN_VALUE;
    }
    
    void prependToSpan(View param1View) {
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(param1View);
      layoutParams.mSpan = this;
      this.mViews.add(0, param1View);
      this.mCachedStart = Integer.MIN_VALUE;
      if (this.mViews.size() == 1)
        this.mCachedEnd = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(param1View); 
    }
    
    void setLine(int param1Int) {
      this.mCachedStart = param1Int;
      this.mCachedEnd = param1Int;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\StaggeredGridLayoutManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */