package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
  static final boolean DEBUG = false;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVALID_OFFSET = -2147483648;
  
  private static final float MAX_SCROLL_FACTOR = 0.33333334F;
  
  private static final String TAG = "LinearLayoutManager";
  
  public static final int VERTICAL = 1;
  
  final AnchorInfo mAnchorInfo = new AnchorInfo();
  
  private int mInitialPrefetchItemCount = 2;
  
  private boolean mLastStackFromEnd;
  
  private final LayoutChunkResult mLayoutChunkResult = new LayoutChunkResult();
  
  private LayoutState mLayoutState;
  
  int mOrientation = 1;
  
  OrientationHelper mOrientationHelper;
  
  SavedState mPendingSavedState = null;
  
  int mPendingScrollPosition = -1;
  
  int mPendingScrollPositionOffset = Integer.MIN_VALUE;
  
  private boolean mRecycleChildrenOnDetach;
  
  private int[] mReusableIntPair = new int[2];
  
  private boolean mReverseLayout = false;
  
  boolean mShouldReverseLayout = false;
  
  private boolean mSmoothScrollbarEnabled = true;
  
  private boolean mStackFromEnd = false;
  
  public LinearLayoutManager(Context paramContext) {
    this(paramContext, 1, false);
  }
  
  public LinearLayoutManager(Context paramContext, int paramInt, boolean paramBoolean) {
    setOrientation(paramInt);
    setReverseLayout(paramBoolean);
  }
  
  public LinearLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    RecyclerView.LayoutManager.Properties properties = getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setOrientation(properties.orientation);
    setReverseLayout(properties.reverseLayout);
    setStackFromEnd(properties.stackFromEnd);
  }
  
  private int computeScrollExtent(RecyclerView.State paramState) {
    if (getChildCount() == 0)
      return 0; 
    ensureLayoutState();
    return ScrollbarHelper.computeScrollExtent(paramState, this.mOrientationHelper, findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
  }
  
  private int computeScrollOffset(RecyclerView.State paramState) {
    if (getChildCount() == 0)
      return 0; 
    ensureLayoutState();
    return ScrollbarHelper.computeScrollOffset(paramState, this.mOrientationHelper, findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
  }
  
  private int computeScrollRange(RecyclerView.State paramState) {
    if (getChildCount() == 0)
      return 0; 
    ensureLayoutState();
    return ScrollbarHelper.computeScrollRange(paramState, this.mOrientationHelper, findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
  }
  
  private View findFirstPartiallyOrCompletelyInvisibleChild() {
    return findOnePartiallyOrCompletelyInvisibleChild(0, getChildCount());
  }
  
  private View findFirstReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return findReferenceChild(paramRecycler, paramState, 0, getChildCount(), paramState.getItemCount());
  }
  
  private View findLastPartiallyOrCompletelyInvisibleChild() {
    return findOnePartiallyOrCompletelyInvisibleChild(getChildCount() - 1, -1);
  }
  
  private View findLastReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return findReferenceChild(paramRecycler, paramState, getChildCount() - 1, -1, paramState.getItemCount());
  }
  
  private View findPartiallyOrCompletelyInvisibleChildClosestToEnd() {
    View view;
    if (this.mShouldReverseLayout) {
      view = findFirstPartiallyOrCompletelyInvisibleChild();
    } else {
      view = findLastPartiallyOrCompletelyInvisibleChild();
    } 
    return view;
  }
  
  private View findPartiallyOrCompletelyInvisibleChildClosestToStart() {
    View view;
    if (this.mShouldReverseLayout) {
      view = findLastPartiallyOrCompletelyInvisibleChild();
    } else {
      view = findFirstPartiallyOrCompletelyInvisibleChild();
    } 
    return view;
  }
  
  private View findReferenceChildClosestToEnd(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    View view;
    if (this.mShouldReverseLayout) {
      view = findFirstReferenceChild(paramRecycler, paramState);
    } else {
      view = findLastReferenceChild((RecyclerView.Recycler)view, paramState);
    } 
    return view;
  }
  
  private View findReferenceChildClosestToStart(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    View view;
    if (this.mShouldReverseLayout) {
      view = findLastReferenceChild(paramRecycler, paramState);
    } else {
      view = findFirstReferenceChild((RecyclerView.Recycler)view, paramState);
    } 
    return view;
  }
  
  private int fixLayoutEndGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = this.mOrientationHelper.getEndAfterPadding() - paramInt;
    if (i > 0) {
      i = -scrollBy(-i, paramRecycler, paramState);
      if (paramBoolean) {
        paramInt = this.mOrientationHelper.getEndAfterPadding() - paramInt + i;
        if (paramInt > 0) {
          this.mOrientationHelper.offsetChildren(paramInt);
          return paramInt + i;
        } 
      } 
      return i;
    } 
    return 0;
  }
  
  private int fixLayoutStartGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = paramInt - this.mOrientationHelper.getStartAfterPadding();
    if (i > 0) {
      i = -scrollBy(i, paramRecycler, paramState);
      if (paramBoolean) {
        paramInt = paramInt + i - this.mOrientationHelper.getStartAfterPadding();
        if (paramInt > 0) {
          this.mOrientationHelper.offsetChildren(-paramInt);
          return i - paramInt;
        } 
      } 
      return i;
    } 
    return 0;
  }
  
  private View getChildClosestToEnd() {
    int i;
    if (this.mShouldReverseLayout) {
      i = 0;
    } else {
      i = getChildCount() - 1;
    } 
    return getChildAt(i);
  }
  
  private View getChildClosestToStart() {
    boolean bool;
    if (this.mShouldReverseLayout) {
      bool = getChildCount() - 1;
    } else {
      bool = false;
    } 
    return getChildAt(bool);
  }
  
  private void layoutForPredictiveAnimations(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2) {
    if (!paramState.willRunPredictiveAnimations() || getChildCount() == 0 || paramState.isPreLayout() || !supportsPredictiveItemAnimations())
      return; 
    int j = 0;
    int i = 0;
    List<RecyclerView.ViewHolder> list = paramRecycler.getScrapList();
    int m = list.size();
    int k = getPosition(getChildAt(0));
    for (byte b = 0; b < m; b++) {
      RecyclerView.ViewHolder viewHolder = list.get(b);
      if (!viewHolder.isRemoved()) {
        boolean bool;
        int n = viewHolder.getLayoutPosition();
        byte b1 = 1;
        if (n < k) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool != this.mShouldReverseLayout)
          b1 = -1; 
        if (b1 == -1) {
          j += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
        } else {
          i += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
        } 
      } 
    } 
    this.mLayoutState.mScrapList = list;
    if (j > 0) {
      updateLayoutStateToFillStart(getPosition(getChildClosestToStart()), paramInt1);
      this.mLayoutState.mExtraFillSpace = j;
      this.mLayoutState.mAvailable = 0;
      this.mLayoutState.assignPositionFromScrapList();
      fill(paramRecycler, this.mLayoutState, paramState, false);
    } 
    if (i > 0) {
      updateLayoutStateToFillEnd(getPosition(getChildClosestToEnd()), paramInt2);
      this.mLayoutState.mExtraFillSpace = i;
      this.mLayoutState.mAvailable = 0;
      this.mLayoutState.assignPositionFromScrapList();
      fill(paramRecycler, this.mLayoutState, paramState, false);
    } 
    this.mLayoutState.mScrapList = null;
  }
  
  private void logChildren() {
    Log.d("LinearLayoutManager", "internal representation of views on the screen");
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      Log.d("LinearLayoutManager", "item " + getPosition(view) + ", coord:" + this.mOrientationHelper.getDecoratedStart(view));
    } 
    Log.d("LinearLayoutManager", "==============");
  }
  
  private void recycleByLayoutState(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState) {
    if (!paramLayoutState.mRecycle || paramLayoutState.mInfinite)
      return; 
    int j = paramLayoutState.mScrollingOffset;
    int i = paramLayoutState.mNoRecycleSpace;
    if (paramLayoutState.mLayoutDirection == -1) {
      recycleViewsFromEnd(paramRecycler, j, i);
    } else {
      recycleViewsFromStart(paramRecycler, j, i);
    } 
  }
  
  private void recycleChildren(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2) {
    if (paramInt1 == paramInt2)
      return; 
    if (paramInt2 > paramInt1) {
      while (--paramInt2 >= paramInt1) {
        removeAndRecycleViewAt(paramInt2, paramRecycler);
        paramInt2--;
      } 
    } else {
      while (paramInt1 > paramInt2) {
        removeAndRecycleViewAt(paramInt1, paramRecycler);
        paramInt1--;
      } 
    } 
  }
  
  private void recycleViewsFromEnd(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2) {
    int i = getChildCount();
    if (paramInt1 < 0)
      return; 
    paramInt2 = this.mOrientationHelper.getEnd() - paramInt1 + paramInt2;
    if (this.mShouldReverseLayout) {
      for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (this.mOrientationHelper.getDecoratedStart(view) < paramInt2 || this.mOrientationHelper.getTransformedStartWithDecoration(view) < paramInt2) {
          recycleChildren(paramRecycler, 0, paramInt1);
          return;
        } 
      } 
    } else {
      for (paramInt1 = i - 1; paramInt1 >= 0; paramInt1--) {
        View view = getChildAt(paramInt1);
        if (this.mOrientationHelper.getDecoratedStart(view) < paramInt2 || this.mOrientationHelper.getTransformedStartWithDecoration(view) < paramInt2) {
          recycleChildren(paramRecycler, i - 1, paramInt1);
          return;
        } 
      } 
    } 
  }
  
  private void recycleViewsFromStart(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      return; 
    int i = paramInt1 - paramInt2;
    paramInt2 = getChildCount();
    if (this.mShouldReverseLayout) {
      for (paramInt1 = paramInt2 - 1; paramInt1 >= 0; paramInt1--) {
        View view = getChildAt(paramInt1);
        if (this.mOrientationHelper.getDecoratedEnd(view) > i || this.mOrientationHelper.getTransformedEndWithDecoration(view) > i) {
          recycleChildren(paramRecycler, paramInt2 - 1, paramInt1);
          return;
        } 
      } 
    } else {
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (this.mOrientationHelper.getDecoratedEnd(view) > i || this.mOrientationHelper.getTransformedEndWithDecoration(view) > i) {
          recycleChildren(paramRecycler, 0, paramInt1);
          return;
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
  
  private boolean updateAnchorFromChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    View view1;
    int j = getChildCount();
    int i = 0;
    if (j == 0)
      return false; 
    View view2 = getFocusedChild();
    if (view2 != null && paramAnchorInfo.isViewValidAsAnchor(view2, paramState)) {
      paramAnchorInfo.assignFromViewAndKeepVisibleRect(view2, getPosition(view2));
      return true;
    } 
    if (this.mLastStackFromEnd != this.mStackFromEnd)
      return false; 
    if (paramAnchorInfo.mLayoutFromEnd) {
      view1 = findReferenceChildClosestToEnd(paramRecycler, paramState);
    } else {
      view1 = findReferenceChildClosestToStart((RecyclerView.Recycler)view1, paramState);
    } 
    if (view1 != null) {
      paramAnchorInfo.assignFromView(view1, getPosition(view1));
      if (!paramState.isPreLayout() && supportsPredictiveItemAnimations()) {
        if (this.mOrientationHelper.getDecoratedStart(view1) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view1) < this.mOrientationHelper.getStartAfterPadding())
          i = 1; 
        if (i) {
          if (paramAnchorInfo.mLayoutFromEnd) {
            i = this.mOrientationHelper.getEndAfterPadding();
          } else {
            i = this.mOrientationHelper.getStartAfterPadding();
          } 
          paramAnchorInfo.mCoordinate = i;
        } 
      } 
      return true;
    } 
    return false;
  }
  
  private boolean updateAnchorFromPendingData(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    boolean bool = paramState.isPreLayout();
    boolean bool1 = false;
    if (!bool) {
      int i = this.mPendingScrollPosition;
      if (i != -1) {
        if (i < 0 || i >= paramState.getItemCount()) {
          this.mPendingScrollPosition = -1;
          this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
          return false;
        } 
        paramAnchorInfo.mPosition = this.mPendingScrollPosition;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null && savedState.hasValidAnchor()) {
          paramAnchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
          if (paramAnchorInfo.mLayoutFromEnd) {
            paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
          } else {
            paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
          } 
          return true;
        } 
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
          View view = findViewByPosition(this.mPendingScrollPosition);
          if (view != null) {
            if (this.mOrientationHelper.getDecoratedMeasurement(view) > this.mOrientationHelper.getTotalSpace()) {
              paramAnchorInfo.assignCoordinateFromPadding();
              return true;
            } 
            if (this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding() < 0) {
              paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
              paramAnchorInfo.mLayoutFromEnd = false;
              return true;
            } 
            if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view) < 0) {
              paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
              paramAnchorInfo.mLayoutFromEnd = true;
              return true;
            } 
            if (paramAnchorInfo.mLayoutFromEnd) {
              i = this.mOrientationHelper.getDecoratedEnd(view) + this.mOrientationHelper.getTotalSpaceChange();
            } else {
              i = this.mOrientationHelper.getDecoratedStart(view);
            } 
            paramAnchorInfo.mCoordinate = i;
          } else {
            if (getChildCount() > 0) {
              i = getPosition(getChildAt(0));
              if (this.mPendingScrollPosition < i) {
                bool = true;
              } else {
                bool = false;
              } 
              if (bool == this.mShouldReverseLayout)
                bool1 = true; 
              paramAnchorInfo.mLayoutFromEnd = bool1;
            } 
            paramAnchorInfo.assignCoordinateFromPadding();
          } 
          return true;
        } 
        paramAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        if (this.mShouldReverseLayout) {
          paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
        } else {
          paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
        } 
        return true;
      } 
    } 
    return false;
  }
  
  private void updateAnchorInfoForLayout(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    boolean bool;
    if (updateAnchorFromPendingData(paramState, paramAnchorInfo))
      return; 
    if (updateAnchorFromChildren(paramRecycler, paramState, paramAnchorInfo))
      return; 
    paramAnchorInfo.assignCoordinateFromPadding();
    if (this.mStackFromEnd) {
      bool = paramState.getItemCount() - 1;
    } else {
      bool = false;
    } 
    paramAnchorInfo.mPosition = bool;
  }
  
  private void updateLayoutState(int paramInt1, int paramInt2, boolean paramBoolean, RecyclerView.State paramState) {
    this.mLayoutState.mInfinite = resolveIsInfinite();
    this.mLayoutState.mLayoutDirection = paramInt1;
    int[] arrayOfInt = this.mReusableIntPair;
    boolean bool1 = false;
    arrayOfInt[0] = 0;
    boolean bool2 = true;
    boolean bool3 = true;
    arrayOfInt[1] = 0;
    calculateExtraLayoutSpace(paramState, arrayOfInt);
    int i = Math.max(0, this.mReusableIntPair[0]);
    int j = Math.max(0, this.mReusableIntPair[1]);
    if (paramInt1 == 1)
      bool1 = true; 
    LayoutState layoutState = this.mLayoutState;
    if (bool1) {
      paramInt1 = j;
    } else {
      paramInt1 = i;
    } 
    layoutState.mExtraFillSpace = paramInt1;
    layoutState = this.mLayoutState;
    if (bool1) {
      paramInt1 = i;
    } else {
      paramInt1 = j;
    } 
    layoutState.mNoRecycleSpace = paramInt1;
    if (bool1) {
      layoutState = this.mLayoutState;
      layoutState.mExtraFillSpace += this.mOrientationHelper.getEndPadding();
      View view = getChildClosestToEnd();
      layoutState = this.mLayoutState;
      paramInt1 = bool3;
      if (this.mShouldReverseLayout)
        paramInt1 = -1; 
      layoutState.mItemDirection = paramInt1;
      this.mLayoutState.mCurrentPosition = getPosition(view) + this.mLayoutState.mItemDirection;
      this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(view);
      paramInt1 = this.mOrientationHelper.getDecoratedEnd(view) - this.mOrientationHelper.getEndAfterPadding();
    } else {
      View view = getChildClosestToStart();
      LayoutState layoutState1 = this.mLayoutState;
      layoutState1.mExtraFillSpace += this.mOrientationHelper.getStartAfterPadding();
      layoutState1 = this.mLayoutState;
      if (this.mShouldReverseLayout) {
        paramInt1 = bool2;
      } else {
        paramInt1 = -1;
      } 
      layoutState1.mItemDirection = paramInt1;
      this.mLayoutState.mCurrentPosition = getPosition(view) + this.mLayoutState.mItemDirection;
      this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(view);
      paramInt1 = -this.mOrientationHelper.getDecoratedStart(view) + this.mOrientationHelper.getStartAfterPadding();
    } 
    this.mLayoutState.mAvailable = paramInt2;
    if (paramBoolean) {
      layoutState = this.mLayoutState;
      layoutState.mAvailable -= paramInt1;
    } 
    this.mLayoutState.mScrollingOffset = paramInt1;
  }
  
  private void updateLayoutStateToFillEnd(int paramInt1, int paramInt2) {
    boolean bool;
    this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - paramInt2;
    LayoutState layoutState = this.mLayoutState;
    if (this.mShouldReverseLayout) {
      bool = true;
    } else {
      bool = true;
    } 
    layoutState.mItemDirection = bool;
    this.mLayoutState.mCurrentPosition = paramInt1;
    this.mLayoutState.mLayoutDirection = 1;
    this.mLayoutState.mOffset = paramInt2;
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillEnd(AnchorInfo paramAnchorInfo) {
    updateLayoutStateToFillEnd(paramAnchorInfo.mPosition, paramAnchorInfo.mCoordinate);
  }
  
  private void updateLayoutStateToFillStart(int paramInt1, int paramInt2) {
    this.mLayoutState.mAvailable = paramInt2 - this.mOrientationHelper.getStartAfterPadding();
    this.mLayoutState.mCurrentPosition = paramInt1;
    LayoutState layoutState = this.mLayoutState;
    if (this.mShouldReverseLayout) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    } 
    layoutState.mItemDirection = paramInt1;
    this.mLayoutState.mLayoutDirection = -1;
    this.mLayoutState.mOffset = paramInt2;
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillStart(AnchorInfo paramAnchorInfo) {
    updateLayoutStateToFillStart(paramAnchorInfo.mPosition, paramAnchorInfo.mCoordinate);
  }
  
  public void assertNotInLayoutOrScroll(String paramString) {
    if (this.mPendingSavedState == null)
      super.assertNotInLayoutOrScroll(paramString); 
  }
  
  protected void calculateExtraLayoutSpace(RecyclerView.State paramState, int[] paramArrayOfint) {
    boolean bool = false;
    int j = 0;
    int i = getExtraLayoutSpace(paramState);
    if (this.mLayoutState.mLayoutDirection != -1) {
      j = i;
      i = bool;
    } 
    paramArrayOfint[0] = i;
    paramArrayOfint[1] = j;
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
  
  public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    if (this.mOrientation != 0)
      paramInt1 = paramInt2; 
    if (getChildCount() == 0 || paramInt1 == 0)
      return; 
    ensureLayoutState();
    if (paramInt1 > 0) {
      paramInt2 = 1;
    } else {
      paramInt2 = -1;
    } 
    updateLayoutState(paramInt2, Math.abs(paramInt1), true, paramState);
    collectPrefetchPositionsForLayoutState(paramState, this.mLayoutState, paramLayoutPrefetchRegistry);
  }
  
  public void collectInitialPrefetchPositions(int paramInt, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    boolean bool;
    SavedState savedState = this.mPendingSavedState;
    byte b = -1;
    if (savedState != null && savedState.hasValidAnchor()) {
      bool = this.mPendingSavedState.mAnchorLayoutFromEnd;
      i = this.mPendingSavedState.mAnchorPosition;
    } else {
      resolveShouldLayoutReverse();
      bool = this.mShouldReverseLayout;
      if (this.mPendingScrollPosition == -1) {
        if (bool) {
          i = paramInt - 1;
        } else {
          i = 0;
        } 
      } else {
        i = this.mPendingScrollPosition;
      } 
    } 
    if (!bool)
      b = 1; 
    int j = i;
    for (int i = 0; i < this.mInitialPrefetchItemCount && j >= 0 && j < paramInt; i++) {
      paramLayoutPrefetchRegistry.addPosition(j, 0);
      j += b;
    } 
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = paramLayoutState.mCurrentPosition;
    if (i >= 0 && i < paramState.getItemCount())
      paramLayoutPrefetchRegistry.addPosition(i, Math.max(0, paramLayoutState.mScrollingOffset)); 
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
    if (getChildCount() == 0)
      return null; 
    boolean bool1 = false;
    int i = getPosition(getChildAt(0));
    boolean bool = true;
    if (paramInt < i)
      bool1 = true; 
    paramInt = bool;
    if (bool1 != this.mShouldReverseLayout)
      paramInt = -1; 
    return (this.mOrientation == 0) ? new PointF(paramInt, 0.0F) : new PointF(0.0F, paramInt);
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
  
  int convertFocusDirectionToLayoutDirection(int paramInt) {
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
  
  LayoutState createLayoutState() {
    return new LayoutState();
  }
  
  void ensureLayoutState() {
    if (this.mLayoutState == null)
      this.mLayoutState = createLayoutState(); 
  }
  
  int fill(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState, RecyclerView.State paramState, boolean paramBoolean) {
    int j = paramLayoutState.mAvailable;
    if (paramLayoutState.mScrollingOffset != Integer.MIN_VALUE) {
      if (paramLayoutState.mAvailable < 0)
        paramLayoutState.mScrollingOffset += paramLayoutState.mAvailable; 
      recycleByLayoutState(paramRecycler, paramLayoutState);
    } 
    int i = paramLayoutState.mAvailable + paramLayoutState.mExtraFillSpace;
    LayoutChunkResult layoutChunkResult = this.mLayoutChunkResult;
    while (true) {
      while (true)
        break; 
      if (paramBoolean) {
        Object object = SYNTHETIC_LOCAL_VARIABLE_5;
        if (layoutChunkResult.mFocusable)
          break; 
      } 
    } 
    return j - paramLayoutState.mAvailable;
  }
  
  public int findFirstCompletelyVisibleItemPosition() {
    int i;
    View view = findOneVisibleChild(0, getChildCount(), true, false);
    if (view == null) {
      i = -1;
    } else {
      i = getPosition(view);
    } 
    return i;
  }
  
  View findFirstVisibleChildClosestToEnd(boolean paramBoolean1, boolean paramBoolean2) {
    return this.mShouldReverseLayout ? findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2) : findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2);
  }
  
  View findFirstVisibleChildClosestToStart(boolean paramBoolean1, boolean paramBoolean2) {
    return this.mShouldReverseLayout ? findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2) : findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2);
  }
  
  public int findFirstVisibleItemPosition() {
    int i;
    View view = findOneVisibleChild(0, getChildCount(), false, true);
    if (view == null) {
      i = -1;
    } else {
      i = getPosition(view);
    } 
    return i;
  }
  
  public int findLastCompletelyVisibleItemPosition() {
    int j = getChildCount();
    int i = -1;
    View view = findOneVisibleChild(j - 1, -1, true, false);
    if (view != null)
      i = getPosition(view); 
    return i;
  }
  
  public int findLastVisibleItemPosition() {
    int j = getChildCount();
    int i = -1;
    View view = findOneVisibleChild(j - 1, -1, false, true);
    if (view != null)
      i = getPosition(view); 
    return i;
  }
  
  View findOnePartiallyOrCompletelyInvisibleChild(int paramInt1, int paramInt2) {
    char c1;
    char c2;
    View view;
    ensureLayoutState();
    if (paramInt2 > paramInt1) {
      c1 = '\001';
    } else if (paramInt2 < paramInt1) {
      c1 = '￿';
    } else {
      c1 = Character.MIN_VALUE;
    } 
    if (!c1)
      return getChildAt(paramInt1); 
    if (this.mOrientationHelper.getDecoratedStart(getChildAt(paramInt1)) < this.mOrientationHelper.getStartAfterPadding()) {
      c2 = '䄄';
      c1 = '䀄';
    } else {
      c2 = '၁';
      c1 = 'ခ';
    } 
    if (this.mOrientation == 0) {
      view = this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(paramInt1, paramInt2, c2, c1);
    } else {
      view = this.mVerticalBoundCheck.findOneViewWithinBoundFlags(paramInt1, paramInt2, c2, c1);
    } 
    return view;
  }
  
  View findOneVisibleChild(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    char c1;
    View view;
    ensureLayoutState();
    char c2 = Character.MIN_VALUE;
    if (paramBoolean1) {
      c1 = '怃';
    } else {
      c1 = 'ŀ';
    } 
    if (paramBoolean2)
      c2 = 'ŀ'; 
    if (this.mOrientation == 0) {
      view = this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(paramInt1, paramInt2, c1, c2);
    } else {
      view = this.mVerticalBoundCheck.findOneViewWithinBoundFlags(paramInt1, paramInt2, c1, c2);
    } 
    return view;
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    View view;
    RecyclerView.State state;
    byte b;
    ensureLayoutState();
    paramState = null;
    paramRecycler = null;
    int j = this.mOrientationHelper.getStartAfterPadding();
    int i = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    while (paramInt1 != paramInt2) {
      View view1;
      View view2 = getChildAt(paramInt1);
      int k = getPosition(view2);
      RecyclerView.State state1 = paramState;
      RecyclerView.Recycler recycler = paramRecycler;
      if (k >= 0) {
        state1 = paramState;
        recycler = paramRecycler;
        if (k < paramInt3)
          if (((RecyclerView.LayoutParams)view2.getLayoutParams()).isItemRemoved()) {
            state1 = paramState;
            recycler = paramRecycler;
            if (paramState == null) {
              View view3 = view2;
              recycler = paramRecycler;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view2) >= i || this.mOrientationHelper.getDecoratedEnd(view2) < j) {
            state1 = paramState;
            recycler = paramRecycler;
            if (paramRecycler == null) {
              view1 = view2;
              state1 = paramState;
            } 
          } else {
            return view2;
          }  
      } 
      paramInt1 += b;
      paramState = state1;
      view = view1;
    } 
    if (view == null)
      state = paramState; 
    return (View)state;
  }
  
  public View findViewByPosition(int paramInt) {
    int j = getChildCount();
    if (j == 0)
      return null; 
    int i = paramInt - getPosition(getChildAt(0));
    if (i >= 0 && i < j) {
      View view = getChildAt(i);
      if (getPosition(view) == paramInt)
        return view; 
    } 
    return super.findViewByPosition(paramInt);
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(-2, -2);
  }
  
  @Deprecated
  protected int getExtraLayoutSpace(RecyclerView.State paramState) {
    return paramState.hasTargetScrollPosition() ? this.mOrientationHelper.getTotalSpace() : 0;
  }
  
  public int getInitialPrefetchItemCount() {
    return this.mInitialPrefetchItemCount;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public boolean getRecycleChildrenOnDetach() {
    return this.mRecycleChildrenOnDetach;
  }
  
  public boolean getReverseLayout() {
    return this.mReverseLayout;
  }
  
  public boolean getStackFromEnd() {
    return this.mStackFromEnd;
  }
  
  public boolean isAutoMeasureEnabled() {
    return true;
  }
  
  protected boolean isLayoutRTL() {
    int i = getLayoutDirection();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isSmoothScrollbarEnabled() {
    return this.mSmoothScrollbarEnabled;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LayoutState paramLayoutState, LayoutChunkResult paramLayoutChunkResult) {
    int i;
    int j;
    int k;
    int m;
    View view = paramLayoutState.next(paramRecycler);
    if (view == null) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
    if (paramLayoutState.mScrapList == null) {
      boolean bool1;
      boolean bool2 = this.mShouldReverseLayout;
      if (paramLayoutState.mLayoutDirection == -1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool2 == bool1) {
        addView(view);
      } else {
        addView(view, 0);
      } 
    } else {
      boolean bool1;
      boolean bool2 = this.mShouldReverseLayout;
      if (paramLayoutState.mLayoutDirection == -1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool2 == bool1) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
    } 
    measureChildWithMargins(view, 0, 0);
    paramLayoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(view);
    if (this.mOrientation == 1) {
      if (isLayoutRTL()) {
        j = getWidth() - getPaddingRight();
        i = j - this.mOrientationHelper.getDecoratedMeasurementInOther(view);
      } else {
        i = getPaddingLeft();
        j = this.mOrientationHelper.getDecoratedMeasurementInOther(view) + i;
      } 
      if (paramLayoutState.mLayoutDirection == -1) {
        k = paramLayoutState.mOffset;
        int n = paramLayoutState.mOffset;
        int i1 = paramLayoutChunkResult.mConsumed;
        m = j;
        j = k;
        k = i;
        n -= i1;
        i = m;
        m = n;
      } else {
        m = paramLayoutState.mOffset;
        int n = paramLayoutState.mOffset;
        k = paramLayoutChunkResult.mConsumed;
        n += k;
        k = i;
        i = j;
        j = n;
      } 
    } else {
      i = getPaddingTop();
      j = this.mOrientationHelper.getDecoratedMeasurementInOther(view) + i;
      if (paramLayoutState.mLayoutDirection == -1) {
        k = paramLayoutState.mOffset;
        int n = paramLayoutState.mOffset;
        m = paramLayoutChunkResult.mConsumed;
        n -= m;
        m = i;
        i = k;
        k = n;
      } else {
        k = paramLayoutState.mOffset;
        m = paramLayoutState.mOffset;
        int n = paramLayoutChunkResult.mConsumed;
        n = m + n;
        m = i;
        i = n;
      } 
    } 
    layoutDecoratedWithMargins(view, k, m, i, j);
    if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
      paramLayoutChunkResult.mIgnoreConsumed = true; 
    paramLayoutChunkResult.mFocusable = view.hasFocusable();
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo, int paramInt) {}
  
  public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler) {
    super.onDetachedFromWindow(paramRecyclerView, paramRecycler);
    if (this.mRecycleChildrenOnDetach) {
      removeAndRecycleAllViews(paramRecycler);
      paramRecycler.clear();
    } 
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    View view;
    resolveShouldLayoutReverse();
    if (getChildCount() == 0)
      return null; 
    paramInt = convertFocusDirectionToLayoutDirection(paramInt);
    if (paramInt == Integer.MIN_VALUE)
      return null; 
    ensureLayoutState();
    updateLayoutState(paramInt, (int)(this.mOrientationHelper.getTotalSpace() * 0.33333334F), false, paramState);
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    this.mLayoutState.mRecycle = false;
    fill(paramRecycler, this.mLayoutState, paramState, true);
    if (paramInt == -1) {
      paramView = findPartiallyOrCompletelyInvisibleChildClosestToStart();
    } else {
      paramView = findPartiallyOrCompletelyInvisibleChildClosestToEnd();
    } 
    if (paramInt == -1) {
      view = getChildClosestToStart();
    } else {
      view = getChildClosestToEnd();
    } 
    return view.hasFocusable() ? ((paramView == null) ? null : view) : paramView;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (getChildCount() > 0) {
      paramAccessibilityEvent.setFromIndex(findFirstVisibleItemPosition());
      paramAccessibilityEvent.setToIndex(findLastVisibleItemPosition());
    } 
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    SavedState savedState = this.mPendingSavedState;
    int k = -1;
    if ((savedState != null || this.mPendingScrollPosition != -1) && paramState.getItemCount() == 0) {
      removeAndRecycleAllViews(paramRecycler);
      return;
    } 
    savedState = this.mPendingSavedState;
    if (savedState != null && savedState.hasValidAnchor())
      this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition; 
    ensureLayoutState();
    this.mLayoutState.mRecycle = false;
    resolveShouldLayoutReverse();
    View view = getFocusedChild();
    if (!this.mAnchorInfo.mValid || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null) {
      this.mAnchorInfo.reset();
      this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
      updateAnchorInfoForLayout(paramRecycler, paramState, this.mAnchorInfo);
      this.mAnchorInfo.mValid = true;
    } else if (view != null && (this.mOrientationHelper.getDecoratedStart(view) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view) <= this.mOrientationHelper.getStartAfterPadding())) {
      this.mAnchorInfo.assignFromViewAndKeepVisibleRect(view, getPosition(view));
    } 
    LayoutState layoutState = this.mLayoutState;
    if (layoutState.mLastScrollDelta >= 0) {
      i = 1;
    } else {
      i = -1;
    } 
    layoutState.mLayoutDirection = i;
    int[] arrayOfInt = this.mReusableIntPair;
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 0;
    calculateExtraLayoutSpace(paramState, arrayOfInt);
    int n = Math.max(0, this.mReusableIntPair[0]) + this.mOrientationHelper.getStartAfterPadding();
    int m = Math.max(0, this.mReusableIntPair[1]) + this.mOrientationHelper.getEndPadding();
    int j = n;
    int i = m;
    if (paramState.isPreLayout()) {
      int i1 = this.mPendingScrollPosition;
      j = n;
      i = m;
      if (i1 != -1) {
        j = n;
        i = m;
        if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
          View view1 = findViewByPosition(i1);
          j = n;
          i = m;
          if (view1 != null) {
            if (this.mShouldReverseLayout) {
              i = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view1) - this.mPendingScrollPositionOffset;
            } else {
              i = this.mOrientationHelper.getDecoratedStart(view1);
              j = this.mOrientationHelper.getStartAfterPadding();
              i = this.mPendingScrollPositionOffset - i - j;
            } 
            if (i > 0) {
              j = n + i;
              i = m;
            } else {
              i = m - i;
              j = n;
            } 
          } 
        } 
      } 
    } 
    if (this.mAnchorInfo.mLayoutFromEnd) {
      if (this.mShouldReverseLayout)
        k = 1; 
    } else if (!this.mShouldReverseLayout) {
      k = 1;
    } 
    onAnchorReady(paramRecycler, paramState, this.mAnchorInfo, k);
    detachAndScrapAttachedViews(paramRecycler);
    this.mLayoutState.mInfinite = resolveIsInfinite();
    this.mLayoutState.mIsPreLayout = paramState.isPreLayout();
    this.mLayoutState.mNoRecycleSpace = 0;
    if (this.mAnchorInfo.mLayoutFromEnd) {
      updateLayoutStateToFillStart(this.mAnchorInfo);
      this.mLayoutState.mExtraFillSpace = j;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      k = this.mLayoutState.mOffset;
      m = this.mLayoutState.mCurrentPosition;
      j = i;
      if (this.mLayoutState.mAvailable > 0)
        j = i + this.mLayoutState.mAvailable; 
      updateLayoutStateToFillEnd(this.mAnchorInfo);
      this.mLayoutState.mExtraFillSpace = j;
      LayoutState layoutState1 = this.mLayoutState;
      layoutState1.mCurrentPosition += this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      j = this.mLayoutState.mOffset;
      i = k;
      if (this.mLayoutState.mAvailable > 0) {
        i = this.mLayoutState.mAvailable;
        updateLayoutStateToFillStart(m, k);
        this.mLayoutState.mExtraFillSpace = i;
        fill(paramRecycler, this.mLayoutState, paramState, false);
        i = this.mLayoutState.mOffset;
      } 
    } else {
      updateLayoutStateToFillEnd(this.mAnchorInfo);
      this.mLayoutState.mExtraFillSpace = i;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      k = this.mLayoutState.mOffset;
      m = this.mLayoutState.mCurrentPosition;
      i = j;
      if (this.mLayoutState.mAvailable > 0)
        i = j + this.mLayoutState.mAvailable; 
      updateLayoutStateToFillStart(this.mAnchorInfo);
      this.mLayoutState.mExtraFillSpace = i;
      LayoutState layoutState1 = this.mLayoutState;
      layoutState1.mCurrentPosition += this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      i = this.mLayoutState.mOffset;
      if (this.mLayoutState.mAvailable > 0) {
        j = this.mLayoutState.mAvailable;
        updateLayoutStateToFillEnd(m, k);
        this.mLayoutState.mExtraFillSpace = j;
        fill(paramRecycler, this.mLayoutState, paramState, false);
        j = this.mLayoutState.mOffset;
      } else {
        j = k;
      } 
    } 
    k = i;
    m = j;
    if (getChildCount() > 0)
      if ((this.mShouldReverseLayout ^ this.mStackFromEnd) != 0) {
        m = fixLayoutEndGap(j, paramRecycler, paramState, true);
        k = i + m;
        i = fixLayoutStartGap(k, paramRecycler, paramState, false);
        k += i;
        m = j + m + i;
      } else {
        k = fixLayoutStartGap(i, paramRecycler, paramState, true);
        j += k;
        m = fixLayoutEndGap(j, paramRecycler, paramState, false);
        k = i + k + m;
        m = j + m;
      }  
    layoutForPredictiveAnimations(paramRecycler, paramState, k, m);
    if (!paramState.isPreLayout()) {
      this.mOrientationHelper.onLayoutComplete();
    } else {
      this.mAnchorInfo.reset();
    } 
    this.mLastStackFromEnd = this.mStackFromEnd;
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSavedState = null;
    this.mPendingScrollPosition = -1;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
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
    if (getChildCount() > 0) {
      ensureLayoutState();
      int i = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
      savedState.mAnchorLayoutFromEnd = i;
      if (i != 0) {
        View view = getChildClosestToEnd();
        savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view);
        savedState.mAnchorPosition = getPosition(view);
      } else {
        View view = getChildClosestToStart();
        savedState.mAnchorPosition = getPosition(view);
        savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding();
      } 
    } else {
      savedState.invalidateAnchor();
    } 
    return savedState;
  }
  
  public void prepareForDrop(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
    ensureLayoutState();
    resolveShouldLayoutReverse();
    paramInt1 = getPosition(paramView1);
    paramInt2 = getPosition(paramView2);
    if (paramInt1 < paramInt2) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    } 
    if (this.mShouldReverseLayout) {
      if (paramInt1 == 1) {
        scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedStart(paramView2) + this.mOrientationHelper.getDecoratedMeasurement(paramView1));
      } else {
        scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(paramView2));
      } 
    } else if (paramInt1 == -1) {
      scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getDecoratedStart(paramView2));
    } else {
      scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getDecoratedEnd(paramView2) - this.mOrientationHelper.getDecoratedMeasurement(paramView1));
    } 
  }
  
  boolean resolveIsInfinite() {
    boolean bool;
    if (this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int scrollBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    byte b;
    if (getChildCount() == 0 || paramInt == 0)
      return 0; 
    ensureLayoutState();
    this.mLayoutState.mRecycle = true;
    if (paramInt > 0) {
      b = 1;
    } else {
      b = -1;
    } 
    int i = Math.abs(paramInt);
    updateLayoutState(b, i, true, paramState);
    int j = this.mLayoutState.mScrollingOffset + fill(paramRecycler, this.mLayoutState, paramState, false);
    if (j < 0)
      return 0; 
    if (i > j)
      paramInt = b * j; 
    this.mOrientationHelper.offsetChildren(-paramInt);
    this.mLayoutState.mLastScrollDelta = paramInt;
    return paramInt;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? 0 : scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void scrollToPosition(int paramInt) {
    this.mPendingScrollPosition = paramInt;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    SavedState savedState = this.mPendingSavedState;
    if (savedState != null)
      savedState.invalidateAnchor(); 
    requestLayout();
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2) {
    this.mPendingScrollPosition = paramInt1;
    this.mPendingScrollPositionOffset = paramInt2;
    SavedState savedState = this.mPendingSavedState;
    if (savedState != null)
      savedState.invalidateAnchor(); 
    requestLayout();
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? 0 : scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void setInitialPrefetchItemCount(int paramInt) {
    this.mInitialPrefetchItemCount = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt == 0 || paramInt == 1) {
      assertNotInLayoutOrScroll((String)null);
      if (paramInt != this.mOrientation || this.mOrientationHelper == null) {
        OrientationHelper orientationHelper = OrientationHelper.createOrientationHelper(this, paramInt);
        this.mOrientationHelper = orientationHelper;
        this.mAnchorInfo.mOrientationHelper = orientationHelper;
        this.mOrientation = paramInt;
        requestLayout();
      } 
      return;
    } 
    throw new IllegalArgumentException("invalid orientation:" + paramInt);
  }
  
  public void setRecycleChildrenOnDetach(boolean paramBoolean) {
    this.mRecycleChildrenOnDetach = paramBoolean;
  }
  
  public void setReverseLayout(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    if (paramBoolean == this.mReverseLayout)
      return; 
    this.mReverseLayout = paramBoolean;
    requestLayout();
  }
  
  public void setSmoothScrollbarEnabled(boolean paramBoolean) {
    this.mSmoothScrollbarEnabled = paramBoolean;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    if (this.mStackFromEnd == paramBoolean)
      return; 
    this.mStackFromEnd = paramBoolean;
    requestLayout();
  }
  
  boolean shouldMeasureTwice() {
    boolean bool;
    if (getHeightMode() != 1073741824 && getWidthMode() != 1073741824 && hasFlexibleChildInBothOrientations()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt) {
    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(paramRecyclerView.getContext());
    linearSmoothScroller.setTargetPosition(paramInt);
    startSmoothScroll(linearSmoothScroller);
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void validateChildOrder() {
    Log.d("LinearLayoutManager", "validating child count " + getChildCount());
    int i = getChildCount();
    boolean bool2 = true;
    boolean bool1 = true;
    if (i < 1)
      return; 
    int j = getPosition(getChildAt(0));
    int k = this.mOrientationHelper.getDecoratedStart(getChildAt(0));
    if (this.mShouldReverseLayout) {
      i = 1;
      while (i < getChildCount()) {
        View view = getChildAt(i);
        int m = getPosition(view);
        int n = this.mOrientationHelper.getDecoratedStart(view);
        if (m < j) {
          logChildren();
          StringBuilder stringBuilder = (new StringBuilder()).append("detected invalid position. loc invalid? ");
          if (n >= k)
            bool1 = false; 
          throw new RuntimeException(stringBuilder.append(bool1).toString());
        } 
        if (n <= k) {
          i++;
          continue;
        } 
        logChildren();
        throw new RuntimeException("detected invalid location");
      } 
    } else {
      i = 1;
      while (i < getChildCount()) {
        View view = getChildAt(i);
        int m = getPosition(view);
        int n = this.mOrientationHelper.getDecoratedStart(view);
        if (m < j) {
          logChildren();
          StringBuilder stringBuilder = (new StringBuilder()).append("detected invalid position. loc invalid? ");
          if (n < k) {
            bool1 = bool2;
          } else {
            bool1 = false;
          } 
          throw new RuntimeException(stringBuilder.append(bool1).toString());
        } 
        if (n >= k) {
          i++;
          continue;
        } 
        logChildren();
        throw new RuntimeException("detected invalid location");
      } 
    } 
  }
  
  static class AnchorInfo {
    int mCoordinate;
    
    boolean mLayoutFromEnd;
    
    OrientationHelper mOrientationHelper;
    
    int mPosition;
    
    boolean mValid;
    
    AnchorInfo() {
      reset();
    }
    
    void assignCoordinateFromPadding() {
      int i;
      if (this.mLayoutFromEnd) {
        i = this.mOrientationHelper.getEndAfterPadding();
      } else {
        i = this.mOrientationHelper.getStartAfterPadding();
      } 
      this.mCoordinate = i;
    }
    
    public void assignFromView(View param1View, int param1Int) {
      if (this.mLayoutFromEnd) {
        this.mCoordinate = this.mOrientationHelper.getDecoratedEnd(param1View) + this.mOrientationHelper.getTotalSpaceChange();
      } else {
        this.mCoordinate = this.mOrientationHelper.getDecoratedStart(param1View);
      } 
      this.mPosition = param1Int;
    }
    
    public void assignFromViewAndKeepVisibleRect(View param1View, int param1Int) {
      int i = this.mOrientationHelper.getTotalSpaceChange();
      if (i >= 0) {
        assignFromView(param1View, param1Int);
        return;
      } 
      this.mPosition = param1Int;
      if (this.mLayoutFromEnd) {
        param1Int = this.mOrientationHelper.getEndAfterPadding() - i - this.mOrientationHelper.getDecoratedEnd(param1View);
        this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - param1Int;
        if (param1Int > 0) {
          i = this.mOrientationHelper.getDecoratedMeasurement(param1View);
          int j = this.mCoordinate;
          int k = this.mOrientationHelper.getStartAfterPadding();
          i = j - i - Math.min(this.mOrientationHelper.getDecoratedStart(param1View) - k, 0) + k;
          if (i < 0)
            this.mCoordinate += Math.min(param1Int, -i); 
        } 
      } else {
        int j = this.mOrientationHelper.getDecoratedStart(param1View);
        param1Int = j - this.mOrientationHelper.getStartAfterPadding();
        this.mCoordinate = j;
        if (param1Int > 0) {
          int k = this.mOrientationHelper.getDecoratedMeasurement(param1View);
          int m = this.mOrientationHelper.getEndAfterPadding();
          int n = this.mOrientationHelper.getDecoratedEnd(param1View);
          i = this.mOrientationHelper.getEndAfterPadding() - Math.min(0, m - i - n) - k + j;
          if (i < 0)
            this.mCoordinate -= Math.min(param1Int, -i); 
        } 
      } 
    }
    
    boolean isViewValidAsAnchor(View param1View, RecyclerView.State param1State) {
      boolean bool;
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      if (!layoutParams.isItemRemoved() && layoutParams.getViewLayoutPosition() >= 0 && layoutParams.getViewLayoutPosition() < param1State.getItemCount()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void reset() {
      this.mPosition = -1;
      this.mCoordinate = Integer.MIN_VALUE;
      this.mLayoutFromEnd = false;
      this.mValid = false;
    }
    
    public String toString() {
      return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + ", mValid=" + this.mValid + '}';
    }
  }
  
  protected static class LayoutChunkResult {
    public int mConsumed;
    
    public boolean mFinished;
    
    public boolean mFocusable;
    
    public boolean mIgnoreConsumed;
    
    void resetInternal() {
      this.mConsumed = 0;
      this.mFinished = false;
      this.mIgnoreConsumed = false;
      this.mFocusable = false;
    }
  }
  
  static class LayoutState {
    static final int INVALID_LAYOUT = -2147483648;
    
    static final int ITEM_DIRECTION_HEAD = -1;
    
    static final int ITEM_DIRECTION_TAIL = 1;
    
    static final int LAYOUT_END = 1;
    
    static final int LAYOUT_START = -1;
    
    static final int SCROLLING_OFFSET_NaN = -2147483648;
    
    static final String TAG = "LLM#LayoutState";
    
    int mAvailable;
    
    int mCurrentPosition;
    
    int mExtraFillSpace = 0;
    
    boolean mInfinite;
    
    boolean mIsPreLayout = false;
    
    int mItemDirection;
    
    int mLastScrollDelta;
    
    int mLayoutDirection;
    
    int mNoRecycleSpace = 0;
    
    int mOffset;
    
    boolean mRecycle = true;
    
    List<RecyclerView.ViewHolder> mScrapList = null;
    
    int mScrollingOffset;
    
    private View nextViewFromScrapList() {
      int i = this.mScrapList.size();
      for (byte b = 0; b < i; b++) {
        View view = ((RecyclerView.ViewHolder)this.mScrapList.get(b)).itemView;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        if (!layoutParams.isItemRemoved() && this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
          assignPositionFromScrapList(view);
          return view;
        } 
      } 
      return null;
    }
    
    public void assignPositionFromScrapList() {
      assignPositionFromScrapList(null);
    }
    
    public void assignPositionFromScrapList(View param1View) {
      param1View = nextViewInLimitedList(param1View);
      if (param1View == null) {
        this.mCurrentPosition = -1;
      } else {
        this.mCurrentPosition = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition();
      } 
    }
    
    boolean hasMore(RecyclerView.State param1State) {
      boolean bool;
      int i = this.mCurrentPosition;
      if (i >= 0 && i < param1State.getItemCount()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void log() {
      Log.d("LLM#LayoutState", "avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection);
    }
    
    View next(RecyclerView.Recycler param1Recycler) {
      if (this.mScrapList != null)
        return nextViewFromScrapList(); 
      View view = param1Recycler.getViewForPosition(this.mCurrentPosition);
      this.mCurrentPosition += this.mItemDirection;
      return view;
    }
    
    public View nextViewInLimitedList(View param1View) {
      View view2;
      int j = this.mScrapList.size();
      View view1 = null;
      int i = Integer.MAX_VALUE;
      byte b = 0;
      while (true) {
        view2 = view1;
        if (b < j) {
          View view = ((RecyclerView.ViewHolder)this.mScrapList.get(b)).itemView;
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
          view2 = view1;
          int k = i;
          if (view != param1View)
            if (layoutParams.isItemRemoved()) {
              view2 = view1;
              k = i;
            } else {
              int m = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
              if (m < 0) {
                view2 = view1;
                k = i;
              } else {
                view2 = view1;
                k = i;
                if (m < i) {
                  view1 = view;
                  k = m;
                  view2 = view1;
                  if (m == 0) {
                    view2 = view1;
                    break;
                  } 
                } 
              } 
            }  
          b++;
          view1 = view2;
          i = k;
          continue;
        } 
        break;
      } 
      return view2;
    }
  }
  
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public LinearLayoutManager.SavedState createFromParcel(Parcel param2Parcel) {
          return new LinearLayoutManager.SavedState(param2Parcel);
        }
        
        public LinearLayoutManager.SavedState[] newArray(int param2Int) {
          return new LinearLayoutManager.SavedState[param2Int];
        }
      };
    
    boolean mAnchorLayoutFromEnd;
    
    int mAnchorOffset;
    
    int mAnchorPosition;
    
    public SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.mAnchorPosition = param1Parcel.readInt();
      this.mAnchorOffset = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.mAnchorLayoutFromEnd = bool;
    }
    
    public SavedState(SavedState param1SavedState) {
      this.mAnchorPosition = param1SavedState.mAnchorPosition;
      this.mAnchorOffset = param1SavedState.mAnchorOffset;
      this.mAnchorLayoutFromEnd = param1SavedState.mAnchorLayoutFromEnd;
    }
    
    public int describeContents() {
      return 0;
    }
    
    boolean hasValidAnchor() {
      boolean bool;
      if (this.mAnchorPosition >= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void invalidateAnchor() {
      this.mAnchorPosition = -1;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mAnchorPosition);
      param1Parcel.writeInt(this.mAnchorOffset);
      param1Parcel.writeInt(this.mAnchorLayoutFromEnd);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public LinearLayoutManager.SavedState createFromParcel(Parcel param1Parcel) {
      return new LinearLayoutManager.SavedState(param1Parcel);
    }
    
    public LinearLayoutManager.SavedState[] newArray(int param1Int) {
      return new LinearLayoutManager.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\LinearLayoutManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */