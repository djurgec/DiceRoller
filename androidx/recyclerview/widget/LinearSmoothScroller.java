package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class LinearSmoothScroller extends RecyclerView.SmoothScroller {
  private static final boolean DEBUG = false;
  
  private static final float MILLISECONDS_PER_INCH = 25.0F;
  
  public static final int SNAP_TO_ANY = 0;
  
  public static final int SNAP_TO_END = 1;
  
  public static final int SNAP_TO_START = -1;
  
  private static final float TARGET_SEEK_EXTRA_SCROLL_RATIO = 1.2F;
  
  private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
  
  protected final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
  
  private final DisplayMetrics mDisplayMetrics;
  
  private boolean mHasCalculatedMillisPerPixel = false;
  
  protected int mInterimTargetDx = 0;
  
  protected int mInterimTargetDy = 0;
  
  protected final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
  
  private float mMillisPerPixel;
  
  protected PointF mTargetVector;
  
  public LinearSmoothScroller(Context paramContext) {
    this.mDisplayMetrics = paramContext.getResources().getDisplayMetrics();
  }
  
  private int clampApplyScroll(int paramInt1, int paramInt2) {
    paramInt2 = paramInt1 - paramInt2;
    return (paramInt1 * paramInt2 <= 0) ? 0 : paramInt2;
  }
  
  private float getSpeedPerPixel() {
    if (!this.mHasCalculatedMillisPerPixel) {
      this.mMillisPerPixel = calculateSpeedPerPixel(this.mDisplayMetrics);
      this.mHasCalculatedMillisPerPixel = true;
    } 
    return this.mMillisPerPixel;
  }
  
  public int calculateDtToFit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    switch (paramInt5) {
      default:
        throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
      case 1:
        return paramInt4 - paramInt2;
      case 0:
        paramInt1 = paramInt3 - paramInt1;
        if (paramInt1 > 0)
          return paramInt1; 
        paramInt1 = paramInt4 - paramInt2;
        return (paramInt1 < 0) ? paramInt1 : 0;
      case -1:
        break;
    } 
    return paramInt3 - paramInt1;
  }
  
  public int calculateDxToMakeVisible(View paramView, int paramInt) {
    RecyclerView.LayoutManager layoutManager = getLayoutManager();
    if (layoutManager == null || !layoutManager.canScrollHorizontally())
      return 0; 
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    return calculateDtToFit(layoutManager.getDecoratedLeft(paramView) - layoutParams.leftMargin, layoutManager.getDecoratedRight(paramView) + layoutParams.rightMargin, layoutManager.getPaddingLeft(), layoutManager.getWidth() - layoutManager.getPaddingRight(), paramInt);
  }
  
  public int calculateDyToMakeVisible(View paramView, int paramInt) {
    RecyclerView.LayoutManager layoutManager = getLayoutManager();
    if (layoutManager == null || !layoutManager.canScrollVertically())
      return 0; 
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    return calculateDtToFit(layoutManager.getDecoratedTop(paramView) - layoutParams.topMargin, layoutManager.getDecoratedBottom(paramView) + layoutParams.bottomMargin, layoutManager.getPaddingTop(), layoutManager.getHeight() - layoutManager.getPaddingBottom(), paramInt);
  }
  
  protected float calculateSpeedPerPixel(DisplayMetrics paramDisplayMetrics) {
    return 25.0F / paramDisplayMetrics.densityDpi;
  }
  
  protected int calculateTimeForDeceleration(int paramInt) {
    return (int)Math.ceil(calculateTimeForScrolling(paramInt) / 0.3356D);
  }
  
  protected int calculateTimeForScrolling(int paramInt) {
    return (int)Math.ceil((Math.abs(paramInt) * getSpeedPerPixel()));
  }
  
  protected int getHorizontalSnapPreference() {
    byte b;
    PointF pointF = this.mTargetVector;
    if (pointF == null || pointF.x == 0.0F)
      return 0; 
    if (this.mTargetVector.x > 0.0F) {
      b = 1;
    } else {
      b = -1;
    } 
    return b;
  }
  
  protected int getVerticalSnapPreference() {
    byte b;
    PointF pointF = this.mTargetVector;
    if (pointF == null || pointF.y == 0.0F)
      return 0; 
    if (this.mTargetVector.y > 0.0F) {
      b = 1;
    } else {
      b = -1;
    } 
    return b;
  }
  
  protected void onSeekTargetStep(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.SmoothScroller.Action paramAction) {
    if (getChildCount() == 0) {
      stop();
      return;
    } 
    this.mInterimTargetDx = clampApplyScroll(this.mInterimTargetDx, paramInt1);
    paramInt1 = clampApplyScroll(this.mInterimTargetDy, paramInt2);
    this.mInterimTargetDy = paramInt1;
    if (this.mInterimTargetDx == 0 && paramInt1 == 0)
      updateActionForInterimTarget(paramAction); 
  }
  
  protected void onStart() {}
  
  protected void onStop() {
    this.mInterimTargetDy = 0;
    this.mInterimTargetDx = 0;
    this.mTargetVector = null;
  }
  
  protected void onTargetFound(View paramView, RecyclerView.State paramState, RecyclerView.SmoothScroller.Action paramAction) {
    int j = calculateDxToMakeVisible(paramView, getHorizontalSnapPreference());
    int i = calculateDyToMakeVisible(paramView, getVerticalSnapPreference());
    int k = calculateTimeForDeceleration((int)Math.sqrt((j * j + i * i)));
    if (k > 0)
      paramAction.update(-j, -i, k, (Interpolator)this.mDecelerateInterpolator); 
  }
  
  protected void updateActionForInterimTarget(RecyclerView.SmoothScroller.Action paramAction) {
    PointF pointF = computeScrollVectorForPosition(getTargetPosition());
    if (pointF == null || (pointF.x == 0.0F && pointF.y == 0.0F)) {
      paramAction.jumpTo(getTargetPosition());
      stop();
      return;
    } 
    normalize(pointF);
    this.mTargetVector = pointF;
    this.mInterimTargetDx = (int)(pointF.x * 10000.0F);
    this.mInterimTargetDy = (int)(pointF.y * 10000.0F);
    int i = calculateTimeForScrolling(10000);
    paramAction.update((int)(this.mInterimTargetDx * 1.2F), (int)(this.mInterimTargetDy * 1.2F), (int)(i * 1.2F), (Interpolator)this.mLinearInterpolator);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\LinearSmoothScroller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */