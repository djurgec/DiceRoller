package androidx.viewpager2.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

final class FakeDrag {
  private int mActualDraggedDistance;
  
  private long mFakeDragBeginTime;
  
  private int mMaximumVelocity;
  
  private final RecyclerView mRecyclerView;
  
  private float mRequestedDragDistance;
  
  private final ScrollEventAdapter mScrollEventAdapter;
  
  private VelocityTracker mVelocityTracker;
  
  private final ViewPager2 mViewPager;
  
  FakeDrag(ViewPager2 paramViewPager2, ScrollEventAdapter paramScrollEventAdapter, RecyclerView paramRecyclerView) {
    this.mViewPager = paramViewPager2;
    this.mScrollEventAdapter = paramScrollEventAdapter;
    this.mRecyclerView = paramRecyclerView;
  }
  
  private void addFakeMotionEvent(long paramLong, int paramInt, float paramFloat1, float paramFloat2) {
    MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, paramLong, paramInt, paramFloat1, paramFloat2, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
  }
  
  private void beginFakeVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
      this.mMaximumVelocity = ViewConfiguration.get(this.mViewPager.getContext()).getScaledMaximumFlingVelocity();
    } else {
      velocityTracker.clear();
    } 
  }
  
  boolean beginFakeDrag() {
    if (this.mScrollEventAdapter.isDragging())
      return false; 
    this.mActualDraggedDistance = 0;
    this.mRequestedDragDistance = false;
    this.mFakeDragBeginTime = SystemClock.uptimeMillis();
    beginFakeVelocityTracker();
    this.mScrollEventAdapter.notifyBeginFakeDrag();
    if (!this.mScrollEventAdapter.isIdle())
      this.mRecyclerView.stopScroll(); 
    addFakeMotionEvent(this.mFakeDragBeginTime, 0, 0.0F, 0.0F);
    return true;
  }
  
  boolean endFakeDrag() {
    if (!this.mScrollEventAdapter.isFakeDragging())
      return false; 
    this.mScrollEventAdapter.notifyEndFakeDrag();
    VelocityTracker velocityTracker = this.mVelocityTracker;
    velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
    int j = (int)velocityTracker.getXVelocity();
    int i = (int)velocityTracker.getYVelocity();
    if (!this.mRecyclerView.fling(j, i))
      this.mViewPager.snapToPage(); 
    return true;
  }
  
  boolean fakeDragBy(float paramFloat) {
    boolean bool1;
    boolean bool2;
    boolean bool = this.mScrollEventAdapter.isFakeDragging();
    boolean bool3 = false;
    if (!bool)
      return false; 
    paramFloat = this.mRequestedDragDistance - paramFloat;
    this.mRequestedDragDistance = paramFloat;
    int i = Math.round(paramFloat - this.mActualDraggedDistance);
    this.mActualDraggedDistance += i;
    long l = SystemClock.uptimeMillis();
    if (this.mViewPager.getOrientation() == 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1) {
      bool2 = i;
    } else {
      bool2 = false;
    } 
    if (bool1)
      i = bool3; 
    float f = 0.0F;
    if (bool1) {
      paramFloat = this.mRequestedDragDistance;
    } else {
      paramFloat = 0.0F;
    } 
    if (!bool1)
      f = this.mRequestedDragDistance; 
    this.mRecyclerView.scrollBy(bool2, i);
    addFakeMotionEvent(l, 2, paramFloat, f);
    return true;
  }
  
  boolean isFakeDragging() {
    return this.mScrollEventAdapter.isFakeDragging();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\FakeDrag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */