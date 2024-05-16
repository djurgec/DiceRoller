package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

public class PagerSnapHelper extends SnapHelper {
  private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
  
  private OrientationHelper mHorizontalHelper;
  
  private OrientationHelper mVerticalHelper;
  
  private int distanceToCenter(RecyclerView.LayoutManager paramLayoutManager, View paramView, OrientationHelper paramOrientationHelper) {
    return paramOrientationHelper.getDecoratedStart(paramView) + paramOrientationHelper.getDecoratedMeasurement(paramView) / 2 - paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
  }
  
  private View findCenterView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int k = paramLayoutManager.getChildCount();
    if (k == 0)
      return null; 
    View view = null;
    int m = paramOrientationHelper.getStartAfterPadding();
    int j = paramOrientationHelper.getTotalSpace() / 2;
    int i = Integer.MAX_VALUE;
    byte b = 0;
    while (b < k) {
      View view1 = paramLayoutManager.getChildAt(b);
      int i1 = Math.abs(paramOrientationHelper.getDecoratedStart(view1) + paramOrientationHelper.getDecoratedMeasurement(view1) / 2 - m + j);
      int n = i;
      if (i1 < i) {
        n = i1;
        view = view1;
      } 
      b++;
      i = n;
    } 
    return view;
  }
  
  private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mHorizontalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(paramLayoutManager); 
    return this.mHorizontalHelper;
  }
  
  private OrientationHelper getOrientationHelper(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? getVerticalHelper(paramLayoutManager) : (paramLayoutManager.canScrollHorizontally() ? getHorizontalHelper(paramLayoutManager) : null);
  }
  
  private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mVerticalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mVerticalHelper = OrientationHelper.createVerticalHelper(paramLayoutManager); 
    return this.mVerticalHelper;
  }
  
  private boolean isForwardFling(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    boolean bool = paramLayoutManager.canScrollHorizontally();
    boolean bool2 = true;
    boolean bool1 = true;
    if (bool) {
      if (paramInt1 <= 0)
        bool1 = false; 
      return bool1;
    } 
    if (paramInt2 > 0) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  private boolean isReverseLayout(RecyclerView.LayoutManager paramLayoutManager) {
    int i = paramLayoutManager.getItemCount();
    boolean bool1 = paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider;
    boolean bool = false;
    if (bool1) {
      PointF pointF = ((RecyclerView.SmoothScroller.ScrollVectorProvider)paramLayoutManager).computeScrollVectorForPosition(i - 1);
      if (pointF != null) {
        if (pointF.x < 0.0F || pointF.y < 0.0F)
          bool = true; 
        return bool;
      } 
    } 
    return false;
  }
  
  public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager paramLayoutManager, View paramView) {
    int[] arrayOfInt = new int[2];
    if (paramLayoutManager.canScrollHorizontally()) {
      arrayOfInt[0] = distanceToCenter(paramLayoutManager, paramView, getHorizontalHelper(paramLayoutManager));
    } else {
      arrayOfInt[0] = 0;
    } 
    if (paramLayoutManager.canScrollVertically()) {
      arrayOfInt[1] = distanceToCenter(paramLayoutManager, paramView, getVerticalHelper(paramLayoutManager));
    } else {
      arrayOfInt[1] = 0;
    } 
    return arrayOfInt;
  }
  
  protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager) {
    return !(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
        final PagerSnapHelper this$0;
        
        protected float calculateSpeedPerPixel(DisplayMetrics param1DisplayMetrics) {
          return 100.0F / param1DisplayMetrics.densityDpi;
        }
        
        protected int calculateTimeForScrolling(int param1Int) {
          return Math.min(100, super.calculateTimeForScrolling(param1Int));
        }
        
        protected void onTargetFound(View param1View, RecyclerView.State param1State, RecyclerView.SmoothScroller.Action param1Action) {
          PagerSnapHelper pagerSnapHelper = PagerSnapHelper.this;
          int[] arrayOfInt = pagerSnapHelper.calculateDistanceToFinalSnap(pagerSnapHelper.mRecyclerView.getLayoutManager(), param1View);
          int i = arrayOfInt[0];
          int j = arrayOfInt[1];
          int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
          if (k > 0)
            param1Action.update(i, j, k, (Interpolator)this.mDecelerateInterpolator); 
        }
      };
  }
  
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    int k = paramLayoutManager.getItemCount();
    if (k == 0)
      return -1; 
    OrientationHelper orientationHelper = getOrientationHelper(paramLayoutManager);
    if (orientationHelper == null)
      return -1; 
    View view1 = null;
    int i = Integer.MIN_VALUE;
    View view2 = null;
    int j = Integer.MAX_VALUE;
    int m = paramLayoutManager.getChildCount();
    byte b = 0;
    while (b < m) {
      int n;
      View view4;
      View view3 = paramLayoutManager.getChildAt(b);
      if (view3 == null) {
        view4 = view2;
        n = j;
      } else {
        int i2 = distanceToCenter(paramLayoutManager, view3, orientationHelper);
        View view = view1;
        int i1 = i;
        if (i2 <= 0) {
          view = view1;
          i1 = i;
          if (i2 > i) {
            i1 = i2;
            view = view3;
          } 
        } 
        view1 = view;
        i = i1;
        view4 = view2;
        n = j;
        if (i2 >= 0) {
          view1 = view;
          i = i1;
          view4 = view2;
          n = j;
          if (i2 < j) {
            n = i2;
            view4 = view3;
            i = i1;
            view1 = view;
          } 
        } 
      } 
      b++;
      view2 = view4;
      j = n;
    } 
    boolean bool = isForwardFling(paramLayoutManager, paramInt1, paramInt2);
    if (bool && view2 != null)
      return paramLayoutManager.getPosition(view2); 
    if (!bool && view1 != null)
      return paramLayoutManager.getPosition(view1); 
    if (!bool)
      view1 = view2; 
    if (view1 == null)
      return -1; 
    paramInt2 = paramLayoutManager.getPosition(view1);
    if (isReverseLayout(paramLayoutManager) == bool) {
      paramInt1 = -1;
    } else {
      paramInt1 = 1;
    } 
    paramInt1 += paramInt2;
    return (paramInt1 < 0 || paramInt1 >= k) ? -1 : paramInt1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\PagerSnapHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */