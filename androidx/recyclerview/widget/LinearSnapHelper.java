package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.view.View;

public class LinearSnapHelper extends SnapHelper {
  private static final float INVALID_DISTANCE = 1.0F;
  
  private OrientationHelper mHorizontalHelper;
  
  private OrientationHelper mVerticalHelper;
  
  private float computeDistancePerChild(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    View view1 = null;
    View view2 = null;
    int i = Integer.MAX_VALUE;
    int k = Integer.MIN_VALUE;
    int m = paramLayoutManager.getChildCount();
    if (m == 0)
      return 1.0F; 
    byte b = 0;
    while (b < m) {
      int i1;
      View view4;
      View view3 = paramLayoutManager.getChildAt(b);
      int n = paramLayoutManager.getPosition(view3);
      if (n == -1) {
        view4 = view1;
        i1 = k;
      } else {
        int i2 = i;
        if (n < i) {
          i2 = n;
          view1 = view3;
        } 
        view4 = view1;
        i = i2;
        i1 = k;
        if (n > k) {
          i1 = n;
          i = i2;
          view2 = view3;
          view4 = view1;
        } 
      } 
      b++;
      view1 = view4;
      k = i1;
    } 
    if (view1 == null || view2 == null)
      return 1.0F; 
    int j = Math.min(paramOrientationHelper.getDecoratedStart(view1), paramOrientationHelper.getDecoratedStart(view2));
    j = Math.max(paramOrientationHelper.getDecoratedEnd(view1), paramOrientationHelper.getDecoratedEnd(view2)) - j;
    return (j == 0) ? 1.0F : (j * 1.0F / (k - i + 1));
  }
  
  private int distanceToCenter(RecyclerView.LayoutManager paramLayoutManager, View paramView, OrientationHelper paramOrientationHelper) {
    return paramOrientationHelper.getDecoratedStart(paramView) + paramOrientationHelper.getDecoratedMeasurement(paramView) / 2 - paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
  }
  
  private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper, int paramInt1, int paramInt2) {
    int[] arrayOfInt = calculateScrollDistance(paramInt1, paramInt2);
    float f = computeDistancePerChild(paramLayoutManager, paramOrientationHelper);
    if (f <= 0.0F)
      return 0; 
    if (Math.abs(arrayOfInt[0]) > Math.abs(arrayOfInt[1])) {
      paramInt1 = arrayOfInt[0];
    } else {
      paramInt1 = arrayOfInt[1];
    } 
    return Math.round(paramInt1 / f);
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
  
  private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mVerticalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mVerticalHelper = OrientationHelper.createVerticalHelper(paramLayoutManager); 
    return this.mVerticalHelper;
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
  
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    if (!(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider))
      return -1; 
    int i = paramLayoutManager.getItemCount();
    if (i == 0)
      return -1; 
    View view = findSnapView(paramLayoutManager);
    if (view == null)
      return -1; 
    int j = paramLayoutManager.getPosition(view);
    if (j == -1)
      return -1; 
    PointF pointF = ((RecyclerView.SmoothScroller.ScrollVectorProvider)paramLayoutManager).computeScrollVectorForPosition(i - 1);
    if (pointF == null)
      return -1; 
    if (paramLayoutManager.canScrollHorizontally()) {
      int k = estimateNextPositionDiffForFling(paramLayoutManager, getHorizontalHelper(paramLayoutManager), paramInt1, 0);
      paramInt1 = k;
      if (pointF.x < 0.0F)
        paramInt1 = -k; 
    } else {
      paramInt1 = 0;
    } 
    if (paramLayoutManager.canScrollVertically()) {
      int k = estimateNextPositionDiffForFling(paramLayoutManager, getVerticalHelper(paramLayoutManager), 0, paramInt2);
      paramInt2 = k;
      if (pointF.y < 0.0F)
        paramInt2 = -k; 
    } else {
      paramInt2 = 0;
    } 
    if (paramLayoutManager.canScrollVertically())
      paramInt1 = paramInt2; 
    if (paramInt1 == 0)
      return -1; 
    paramInt2 = j + paramInt1;
    paramInt1 = paramInt2;
    if (paramInt2 < 0)
      paramInt1 = 0; 
    paramInt2 = paramInt1;
    if (paramInt1 >= i)
      paramInt2 = i - 1; 
    return paramInt2;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\LinearSnapHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */