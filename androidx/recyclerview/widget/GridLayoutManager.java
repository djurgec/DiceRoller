package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  private boolean mUsingSpansToEstimateScrollBarDimensions;
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt, boolean paramBoolean) {
    byte b1;
    byte b2;
    if (paramBoolean) {
      boolean bool = false;
      b2 = paramInt;
      b1 = 1;
      paramInt = bool;
    } else {
      paramInt--;
      b2 = -1;
      b1 = -1;
    } 
    int i = 0;
    while (paramInt != b2) {
      View view = this.mSet[paramInt];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = i;
      i += layoutParams.mSpanSize;
      paramInt += b1;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 25
    //   4: aload_0
    //   5: arraylength
    //   6: iload_1
    //   7: iconst_1
    //   8: iadd
    //   9: if_icmpne -> 25
    //   12: aload_0
    //   13: astore #10
    //   15: aload_0
    //   16: aload_0
    //   17: arraylength
    //   18: iconst_1
    //   19: isub
    //   20: iaload
    //   21: iload_2
    //   22: if_icmpeq -> 32
    //   25: iload_1
    //   26: iconst_1
    //   27: iadd
    //   28: newarray int
    //   30: astore #10
    //   32: aload #10
    //   34: iconst_0
    //   35: iconst_0
    //   36: iastore
    //   37: iload_2
    //   38: iload_1
    //   39: idiv
    //   40: istore #7
    //   42: iload_2
    //   43: iload_1
    //   44: irem
    //   45: istore #9
    //   47: iconst_0
    //   48: istore #4
    //   50: iconst_0
    //   51: istore_2
    //   52: iconst_1
    //   53: istore_3
    //   54: iload_3
    //   55: iload_1
    //   56: if_icmpgt -> 127
    //   59: iload #7
    //   61: istore #5
    //   63: iload_2
    //   64: iload #9
    //   66: iadd
    //   67: istore #8
    //   69: iload #8
    //   71: istore_2
    //   72: iload #5
    //   74: istore #6
    //   76: iload #8
    //   78: ifle -> 108
    //   81: iload #8
    //   83: istore_2
    //   84: iload #5
    //   86: istore #6
    //   88: iload_1
    //   89: iload #8
    //   91: isub
    //   92: iload #9
    //   94: if_icmpge -> 108
    //   97: iload #5
    //   99: iconst_1
    //   100: iadd
    //   101: istore #6
    //   103: iload #8
    //   105: iload_1
    //   106: isub
    //   107: istore_2
    //   108: iload #4
    //   110: iload #6
    //   112: iadd
    //   113: istore #4
    //   115: aload #10
    //   117: iload_3
    //   118: iload #4
    //   120: iastore
    //   121: iinc #3, 1
    //   124: goto -> 54
    //   127: aload #10
    //   129: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private int computeScrollOffsetWithSpanInfo(RecyclerView.State paramState) {
    if (getChildCount() == 0 || paramState.getItemCount() == 0)
      return 0; 
    ensureLayoutState();
    boolean bool = isSmoothScrollbarEnabled();
    View view1 = findFirstVisibleChildClosestToStart(bool ^ true, true);
    View view2 = findFirstVisibleChildClosestToEnd(bool ^ true, true);
    if (view1 == null || view2 == null)
      return 0; 
    int j = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
    int k = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
    int i = Math.min(j, k);
    k = Math.max(j, k);
    j = this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount);
    if (this.mShouldReverseLayout) {
      i = Math.max(0, j + 1 - k - 1);
    } else {
      i = Math.max(0, i);
    } 
    if (!bool)
      return i; 
    int m = Math.abs(this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedStart(view1));
    k = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
    j = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
    float f = m / (j - k + 1);
    return Math.round(i * f + (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(view1)));
  }
  
  private int computeScrollRangeWithSpanInfo(RecyclerView.State paramState) {
    if (getChildCount() == 0 || paramState.getItemCount() == 0)
      return 0; 
    ensureLayoutState();
    View view1 = findFirstVisibleChildClosestToStart(isSmoothScrollbarEnabled() ^ true, true);
    View view2 = findFirstVisibleChildClosestToEnd(isSmoothScrollbarEnabled() ^ true, true);
    if (view1 == null || view2 == null)
      return 0; 
    if (!isSmoothScrollbarEnabled())
      return this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount) + 1; 
    int m = this.mOrientationHelper.getDecoratedEnd(view2);
    int i = this.mOrientationHelper.getDecoratedStart(view1);
    int k = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
    int j = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
    int n = this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount);
    return (int)((m - i) / (j - k + 1) * (n + 1));
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    int i;
    if (paramInt == 1) {
      i = 1;
    } else {
      i = 0;
    } 
    paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (i) {
      while (paramInt > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int k = paramState.getItemCount();
      int j = paramAnchorInfo.mPosition;
      i = paramInt;
      paramInt = j;
      while (paramInt < k - 1) {
        j = getSpanIndex(paramRecycler, paramState, paramInt + 1);
        if (j > i) {
          paramInt++;
          i = j;
        } 
      } 
      paramAnchorInfo.mPosition = paramInt;
    } 
  }
  
  private void ensureViewSet() {
    View[] arrayOfView = this.mSet;
    if (arrayOfView == null || arrayOfView.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + paramInt);
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int i = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + paramInt);
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int i = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + paramInt);
      return 1;
    } 
    return this.mSpanSizeLookup.getSpanSize(i);
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(this.mSpanCount * paramFloat), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int i = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      paramInt = getChildMeasureSpec(k, paramInt, j, layoutParams.width, false);
      i = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i, layoutParams.height, true);
    } else {
      i = getChildMeasureSpec(k, paramInt, i, layoutParams.height, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), j, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, paramInt, i, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    if (getOrientation() == 1) {
      i = getWidth() - getPaddingRight() - getPaddingLeft();
    } else {
      i = getHeight() - getPaddingBottom() - getPaddingTop();
    } 
    calculateItemBorders(i);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, Math.max(0, paramLayoutState.mScrollingOffset));
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  public int computeHorizontalScrollOffset(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(paramState) : super.computeHorizontalScrollOffset(paramState);
  }
  
  public int computeHorizontalScrollRange(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(paramState) : super.computeHorizontalScrollRange(paramState);
  }
  
  public int computeVerticalScrollOffset(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(paramState) : super.computeVerticalScrollOffset(paramState);
  }
  
  public int computeVerticalScrollRange(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(paramState) : super.computeVerticalScrollRange(paramState);
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    View view1;
    byte b;
    ensureLayoutState();
    View view3 = null;
    View view2 = null;
    int i = this.mOrientationHelper.getStartAfterPadding();
    int j = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    while (paramInt1 != paramInt2) {
      View view4 = getChildAt(paramInt1);
      int k = getPosition(view4);
      View view6 = view3;
      View view5 = view2;
      if (k >= 0) {
        view6 = view3;
        view5 = view2;
        if (k < paramInt3)
          if (getSpanIndex(paramRecycler, paramState, k) != 0) {
            view6 = view3;
            view5 = view2;
          } else if (((RecyclerView.LayoutParams)view4.getLayoutParams()).isItemRemoved()) {
            view6 = view3;
            view5 = view2;
            if (view3 == null) {
              view6 = view4;
              view5 = view2;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view4) >= j || this.mOrientationHelper.getDecoratedEnd(view4) < i) {
            view6 = view3;
            view5 = view2;
            if (view2 == null) {
              view5 = view4;
              view6 = view3;
            } 
          } else {
            return view4;
          }  
      } 
      paramInt1 += b;
      view3 = view6;
      view2 = view5;
    } 
    if (view2 != null) {
      view1 = view2;
    } else {
      view1 = view3;
    } 
    return view1;
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
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1 && isLayoutRTL()) {
      int[] arrayOfInt1 = this.mCachedBorders;
      int i = this.mSpanCount;
      return arrayOfInt1[i - paramInt1] - arrayOfInt1[i - paramInt1 - paramInt2];
    } 
    int[] arrayOfInt = this.mCachedBorders;
    return arrayOfInt[paramInt1 + paramInt2] - arrayOfInt[paramInt1];
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  public boolean isUsingSpansToEstimateScrollbarDimensions() {
    return this.mUsingSpansToEstimateScrollBarDimensions;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    boolean bool;
    int i2 = this.mOrientationHelper.getModeInOther();
    if (i2 != 1073741824) {
      i = 1;
    } else {
      i = 0;
    } 
    int m = i;
    if (getChildCount() > 0) {
      n = this.mCachedBorders[this.mSpanCount];
    } else {
      n = 0;
    } 
    if (m)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int j = this.mSpanCount;
    if (!bool) {
      j = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition);
      i = 0;
      k = 0;
    } else {
      i = 0;
      k = 0;
    } 
    while (true) {
      int i3 = j;
      if (i < this.mSpanCount) {
        i3 = j;
        if (paramLayoutState.hasMore(paramState)) {
          i3 = j;
          if (j > 0) {
            int i4 = paramLayoutState.mCurrentPosition;
            i3 = getSpanSize(paramRecycler, paramState, i4);
            if (i3 <= this.mSpanCount) {
              j -= i3;
              if (j < 0) {
                i3 = j;
                break;
              } 
              View view = paramLayoutState.next(paramRecycler);
              if (view == null) {
                i3 = j;
                break;
              } 
              k += i3;
              this.mSet[i] = view;
              i++;
              continue;
            } 
            throw new IllegalArgumentException("Item at position " + i4 + " requires " + i3 + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
          } 
        } 
      } 
      break;
    } 
    if (i == 0) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    j = 0;
    assignSpans(paramRecycler, paramState, i, bool);
    int i1 = 0;
    float f;
    for (f = 0.0F; i1 < i; f = f1) {
      View view = this.mSet[i1];
      if (paramLayoutState.mScrapList == null) {
        if (bool) {
          addView(view);
        } else {
          addView(view, 0);
        } 
      } else if (bool) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
      calculateItemDecorationsForChild(view, this.mDecorInsets);
      measureChild(view, i2, false);
      int i3 = this.mOrientationHelper.getDecoratedMeasurement(view);
      k = j;
      if (i3 > j)
        k = i3; 
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      float f2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0F / layoutParams.mSpanSize;
      float f1 = f;
      if (f2 > f)
        f1 = f2; 
      i1++;
      j = k;
    } 
    if (m != 0) {
      guessMeasurement(f, n);
      j = 0;
      k = 0;
      while (k < i) {
        View view = this.mSet[k];
        measureChild(view, 1073741824, true);
        i1 = this.mOrientationHelper.getDecoratedMeasurement(view);
        n = j;
        if (i1 > j)
          n = i1; 
        k++;
        j = n;
      } 
    } 
    int n = 0;
    int k = i2;
    while (n < i) {
      View view = this.mSet[n];
      if (this.mOrientationHelper.getDecoratedMeasurement(view) != j) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        i1 = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        i2 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        int i3 = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
          i2 = getChildMeasureSpec(i3, 1073741824, i2, layoutParams.width, false);
          i1 = View.MeasureSpec.makeMeasureSpec(j - i1, 1073741824);
        } else {
          i2 = View.MeasureSpec.makeMeasureSpec(j - i2, 1073741824);
          i1 = getChildMeasureSpec(i3, 1073741824, i1, layoutParams.height, false);
        } 
        measureChildWithDecorationsAndMargin(view, i2, i1, true);
      } 
      n++;
    } 
    paramLayoutChunkResult.mConsumed = j;
    i1 = 0;
    m = 0;
    k = 0;
    n = 0;
    if (this.mOrientation == 1) {
      if (paramLayoutState.mLayoutDirection == -1) {
        n = paramLayoutState.mOffset;
        k = n - j;
        j = i1;
      } else {
        k = paramLayoutState.mOffset;
        n = k + j;
        j = i1;
      } 
    } else if (paramLayoutState.mLayoutDirection == -1) {
      m = paramLayoutState.mOffset;
      j = m - j;
    } else {
      i1 = paramLayoutState.mOffset;
      m = i1 + j;
      j = i1;
    } 
    i2 = 0;
    i1 = i;
    int i = n;
    while (i2 < i1) {
      View view = this.mSet[i2];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (this.mOrientation == 1) {
        if (isLayoutRTL()) {
          m = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
          n = m - this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = i;
          i = m;
          m = k;
          k = n;
        } else {
          m = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
          n = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = m;
          int i3 = n + m;
          n = i;
          m = k;
          k = j;
          i = i3;
          j = n;
        } 
      } else {
        k = j;
        i = m;
        j = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
        n = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
        m = j;
        j = n + j;
      } 
      layoutDecoratedWithMargins(view, k, m, i, j);
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        paramLayoutChunkResult.mIgnoreConsumed = true; 
      paramLayoutChunkResult.mFocusable |= view.hasFocusable();
      i2++;
      n = m;
      m = i;
      i = j;
      j = k;
      k = n;
    } 
    Arrays.fill((Object[])this.mSet, (Object)null);
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #22
    //   7: aload #22
    //   9: ifnonnull -> 14
    //   12: aconst_null
    //   13: areturn
    //   14: aload #22
    //   16: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   19: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   22: astore #21
    //   24: aload #21
    //   26: getfield mSpanIndex : I
    //   29: istore #16
    //   31: aload #21
    //   33: getfield mSpanIndex : I
    //   36: aload #21
    //   38: getfield mSpanSize : I
    //   41: iadd
    //   42: istore #15
    //   44: aload_0
    //   45: aload_1
    //   46: iload_2
    //   47: aload_3
    //   48: aload #4
    //   50: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;)Landroid/view/View;
    //   53: ifnonnull -> 58
    //   56: aconst_null
    //   57: areturn
    //   58: aload_0
    //   59: iload_2
    //   60: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   63: iconst_1
    //   64: if_icmpne -> 73
    //   67: iconst_1
    //   68: istore #20
    //   70: goto -> 76
    //   73: iconst_0
    //   74: istore #20
    //   76: iload #20
    //   78: aload_0
    //   79: getfield mShouldReverseLayout : Z
    //   82: if_icmpeq -> 90
    //   85: iconst_1
    //   86: istore_2
    //   87: goto -> 92
    //   90: iconst_0
    //   91: istore_2
    //   92: iload_2
    //   93: ifeq -> 112
    //   96: aload_0
    //   97: invokevirtual getChildCount : ()I
    //   100: iconst_1
    //   101: isub
    //   102: istore_2
    //   103: iconst_m1
    //   104: istore #7
    //   106: iconst_m1
    //   107: istore #8
    //   109: goto -> 123
    //   112: iconst_0
    //   113: istore_2
    //   114: iconst_1
    //   115: istore #7
    //   117: aload_0
    //   118: invokevirtual getChildCount : ()I
    //   121: istore #8
    //   123: aload_0
    //   124: getfield mOrientation : I
    //   127: iconst_1
    //   128: if_icmpne -> 144
    //   131: aload_0
    //   132: invokevirtual isLayoutRTL : ()Z
    //   135: ifeq -> 144
    //   138: iconst_1
    //   139: istore #9
    //   141: goto -> 147
    //   144: iconst_0
    //   145: istore #9
    //   147: aconst_null
    //   148: astore #21
    //   150: aconst_null
    //   151: astore_1
    //   152: aload_0
    //   153: aload_3
    //   154: aload #4
    //   156: iload_2
    //   157: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   160: istore #11
    //   162: iconst_m1
    //   163: istore #6
    //   165: iconst_0
    //   166: istore #5
    //   168: iconst_m1
    //   169: istore #14
    //   171: iconst_0
    //   172: istore #13
    //   174: iload_2
    //   175: istore #12
    //   177: iload_2
    //   178: istore #10
    //   180: iload #12
    //   182: iload #8
    //   184: if_icmpeq -> 553
    //   187: aload_0
    //   188: aload_3
    //   189: aload #4
    //   191: iload #12
    //   193: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   196: istore_2
    //   197: aload_0
    //   198: iload #12
    //   200: invokevirtual getChildAt : (I)Landroid/view/View;
    //   203: astore #23
    //   205: aload #23
    //   207: aload #22
    //   209: if_acmpne -> 215
    //   212: goto -> 553
    //   215: aload #23
    //   217: invokevirtual hasFocusable : ()Z
    //   220: ifeq -> 240
    //   223: iload_2
    //   224: iload #11
    //   226: if_icmpeq -> 240
    //   229: aload #21
    //   231: ifnull -> 237
    //   234: goto -> 553
    //   237: goto -> 543
    //   240: aload #23
    //   242: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   245: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   248: astore #24
    //   250: aload #24
    //   252: getfield mSpanIndex : I
    //   255: istore #17
    //   257: aload #24
    //   259: getfield mSpanIndex : I
    //   262: aload #24
    //   264: getfield mSpanSize : I
    //   267: iadd
    //   268: istore #18
    //   270: aload #23
    //   272: invokevirtual hasFocusable : ()Z
    //   275: ifeq -> 295
    //   278: iload #17
    //   280: iload #16
    //   282: if_icmpne -> 295
    //   285: iload #18
    //   287: iload #15
    //   289: if_icmpne -> 295
    //   292: aload #23
    //   294: areturn
    //   295: aload #23
    //   297: invokevirtual hasFocusable : ()Z
    //   300: ifeq -> 308
    //   303: aload #21
    //   305: ifnull -> 320
    //   308: aload #23
    //   310: invokevirtual hasFocusable : ()Z
    //   313: ifne -> 325
    //   316: aload_1
    //   317: ifnonnull -> 325
    //   320: iconst_1
    //   321: istore_2
    //   322: goto -> 458
    //   325: iload #17
    //   327: iload #16
    //   329: invokestatic max : (II)I
    //   332: istore_2
    //   333: iload #18
    //   335: iload #15
    //   337: invokestatic min : (II)I
    //   340: iload_2
    //   341: isub
    //   342: istore #19
    //   344: aload #23
    //   346: invokevirtual hasFocusable : ()Z
    //   349: ifeq -> 399
    //   352: iload #19
    //   354: iload #5
    //   356: if_icmple -> 364
    //   359: iconst_1
    //   360: istore_2
    //   361: goto -> 458
    //   364: iload #19
    //   366: iload #5
    //   368: if_icmpne -> 396
    //   371: iload #17
    //   373: iload #6
    //   375: if_icmple -> 383
    //   378: iconst_1
    //   379: istore_2
    //   380: goto -> 385
    //   383: iconst_0
    //   384: istore_2
    //   385: iload #9
    //   387: iload_2
    //   388: if_icmpne -> 396
    //   391: iconst_1
    //   392: istore_2
    //   393: goto -> 458
    //   396: goto -> 456
    //   399: aload #21
    //   401: ifnonnull -> 456
    //   404: iconst_0
    //   405: istore_2
    //   406: aload_0
    //   407: aload #23
    //   409: iconst_0
    //   410: iconst_1
    //   411: invokevirtual isViewPartiallyVisible : (Landroid/view/View;ZZ)Z
    //   414: ifeq -> 456
    //   417: iload #19
    //   419: iload #13
    //   421: if_icmple -> 429
    //   424: iconst_1
    //   425: istore_2
    //   426: goto -> 458
    //   429: iload #19
    //   431: iload #13
    //   433: if_icmpne -> 456
    //   436: iload #17
    //   438: iload #14
    //   440: if_icmple -> 445
    //   443: iconst_1
    //   444: istore_2
    //   445: iload #9
    //   447: iload_2
    //   448: if_icmpne -> 456
    //   451: iconst_1
    //   452: istore_2
    //   453: goto -> 458
    //   456: iconst_0
    //   457: istore_2
    //   458: iload_2
    //   459: ifeq -> 543
    //   462: aload #23
    //   464: invokevirtual hasFocusable : ()Z
    //   467: ifeq -> 507
    //   470: aload #24
    //   472: getfield mSpanIndex : I
    //   475: istore #6
    //   477: iload #18
    //   479: iload #15
    //   481: invokestatic min : (II)I
    //   484: istore_2
    //   485: iload #17
    //   487: iload #16
    //   489: invokestatic max : (II)I
    //   492: istore #5
    //   494: aload #23
    //   496: astore #21
    //   498: iload_2
    //   499: iload #5
    //   501: isub
    //   502: istore #5
    //   504: goto -> 543
    //   507: aload #24
    //   509: getfield mSpanIndex : I
    //   512: istore #14
    //   514: iload #18
    //   516: iload #15
    //   518: invokestatic min : (II)I
    //   521: istore_2
    //   522: iload #17
    //   524: iload #16
    //   526: invokestatic max : (II)I
    //   529: istore #13
    //   531: aload #23
    //   533: astore_1
    //   534: iload_2
    //   535: iload #13
    //   537: isub
    //   538: istore #13
    //   540: goto -> 543
    //   543: iload #12
    //   545: iload #7
    //   547: iadd
    //   548: istore #12
    //   550: goto -> 180
    //   553: aload #21
    //   555: ifnull -> 564
    //   558: aload #21
    //   560: astore_1
    //   561: goto -> 564
    //   564: aload_1
    //   565: areturn
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams.getSpanIndex(), layoutParams.getSpanSize(), i, 1, false, false));
    } else {
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, layoutParams.getSpanIndex(), layoutParams.getSpanSize(), false, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int[] arrayOfInt;
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int j = getPaddingLeft() + getPaddingRight();
    int i = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + i, getMinimumHeight());
      arrayOfInt = this.mCachedBorders;
      paramInt1 = chooseSize(paramInt1, arrayOfInt[arrayOfInt.length - 1] + j, getMinimumWidth());
    } else {
      paramInt1 = chooseSize(paramInt1, arrayOfInt.width() + j, getMinimumWidth());
      arrayOfInt = this.mCachedBorders;
      paramInt2 = chooseSize(paramInt2, arrayOfInt[arrayOfInt.length - 1] + i, getMinimumHeight());
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt == this.mSpanCount)
      return; 
    this.mPendingSpanCountChange = true;
    if (paramInt >= 1) {
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
      return;
    } 
    throw new IllegalArgumentException("Span count should be at least 1. Provided " + paramInt);
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (!paramBoolean) {
      super.setStackFromEnd(false);
      return;
    } 
    throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
  }
  
  public void setUsingSpansToEstimateScrollbarDimensions(boolean paramBoolean) {
    this.mUsingSpansToEstimateScrollBarDimensions = paramBoolean;
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
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
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanGroupIndices = false;
    
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    static int findFirstKeyLessThan(SparseIntArray param1SparseIntArray, int param1Int) {
      int j = 0;
      for (int i = param1SparseIntArray.size() - 1; j <= i; i = k - 1) {
        int k = j + i >>> 1;
        if (param1SparseIntArray.keyAt(k) < param1Int) {
          j = k + 1;
          continue;
        } 
      } 
      param1Int = j - 1;
      return (param1Int >= 0 && param1Int < param1SparseIntArray.size()) ? param1SparseIntArray.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanGroupIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanGroupIndices)
        return getSpanGroupIndex(param1Int1, param1Int2); 
      int i = this.mSpanGroupIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanGroupIndex(param1Int1, param1Int2);
      this.mSpanGroupIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int i = this.mSpanIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanIndex(param1Int1, param1Int2);
      this.mSpanIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      int i1 = 0;
      int m = 0;
      int n = 0;
      int i = i1;
      int j = m;
      int k = n;
      if (this.mCacheSpanGroupIndices) {
        int i3 = findFirstKeyLessThan(this.mSpanGroupIndexCache, param1Int1);
        i = i1;
        j = m;
        k = n;
        if (i3 != -1) {
          n = this.mSpanGroupIndexCache.get(i3);
          m = i3 + 1;
          i1 = getCachedSpanIndex(i3, param1Int2) + getSpanSize(i3);
          i = i1;
          j = n;
          k = m;
          if (i1 == param1Int2) {
            i = 0;
            j = n + 1;
            k = m;
          } 
        } 
      } 
      int i2 = getSpanSize(param1Int1);
      m = k;
      while (m < param1Int1) {
        n = getSpanSize(m);
        i1 = i + n;
        if (i1 == param1Int2) {
          i = 0;
          k = j + 1;
        } else {
          i = i1;
          k = j;
          if (i1 > param1Int2) {
            i = n;
            k = j + 1;
          } 
        } 
        m++;
        j = k;
      } 
      param1Int1 = j;
      if (i + i2 > param1Int2)
        param1Int1 = j + 1; 
      return param1Int1;
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      int n = getSpanSize(param1Int1);
      if (n == param1Int2)
        return 0; 
      int k = 0;
      int m = 0;
      int i = k;
      int j = m;
      if (this.mCacheSpanIndices) {
        int i1 = findFirstKeyLessThan(this.mSpanIndexCache, param1Int1);
        i = k;
        j = m;
        if (i1 >= 0) {
          i = this.mSpanIndexCache.get(i1) + getSpanSize(i1);
          j = i1 + 1;
        } 
      } 
      while (j < param1Int1) {
        k = getSpanSize(j);
        m = i + k;
        if (m == param1Int2) {
          i = 0;
        } else {
          i = m;
          if (m > param1Int2)
            i = k; 
        } 
        j++;
      } 
      return (i + n <= param1Int2) ? i : 0;
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanGroupIndexCache() {
      this.mSpanGroupIndexCache.clear();
    }
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanGroupIndexCacheEnabled() {
      return this.mCacheSpanGroupIndices;
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanGroupIndexCacheEnabled(boolean param1Boolean) {
      if (!param1Boolean)
        this.mSpanGroupIndexCache.clear(); 
      this.mCacheSpanGroupIndices = param1Boolean;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      if (!param1Boolean)
        this.mSpanGroupIndexCache.clear(); 
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\GridLayoutManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */