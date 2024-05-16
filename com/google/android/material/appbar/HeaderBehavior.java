package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
  private static final int INVALID_POINTER = -1;
  
  private int activePointerId = -1;
  
  private Runnable flingRunnable;
  
  private boolean isBeingDragged;
  
  private int lastMotionY;
  
  OverScroller scroller;
  
  private int touchSlop = -1;
  
  private VelocityTracker velocityTracker;
  
  public HeaderBehavior() {}
  
  public HeaderBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void ensureVelocityTracker() {
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
  }
  
  boolean canDragView(V paramV) {
    return false;
  }
  
  final boolean fling(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, float paramFloat) {
    FlingRunnable flingRunnable;
    Runnable runnable = this.flingRunnable;
    if (runnable != null) {
      paramV.removeCallbacks(runnable);
      this.flingRunnable = null;
    } 
    if (this.scroller == null)
      this.scroller = new OverScroller(paramV.getContext()); 
    this.scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(paramFloat), 0, 0, paramInt1, paramInt2);
    if (this.scroller.computeScrollOffset()) {
      flingRunnable = new FlingRunnable(paramCoordinatorLayout, paramV);
      this.flingRunnable = flingRunnable;
      ViewCompat.postOnAnimation((View)paramV, flingRunnable);
      return true;
    } 
    onFlingFinished((CoordinatorLayout)flingRunnable, paramV);
    return false;
  }
  
  int getMaxDragOffset(V paramV) {
    return -paramV.getHeight();
  }
  
  int getScrollRangeForDragFling(V paramV) {
    return paramV.getHeight();
  }
  
  int getTopBottomOffsetForScrollingSibling() {
    return getTopAndBottomOffset();
  }
  
  void onFlingFinished(CoordinatorLayout paramCoordinatorLayout, V paramV) {}
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (this.touchSlop < 0)
      this.touchSlop = ViewConfiguration.get(paramCoordinatorLayout.getContext()).getScaledTouchSlop(); 
    if (paramMotionEvent.getActionMasked() == 2 && this.isBeingDragged) {
      int i = this.activePointerId;
      if (i == -1)
        return false; 
      i = paramMotionEvent.findPointerIndex(i);
      if (i == -1)
        return false; 
      i = (int)paramMotionEvent.getY(i);
      if (Math.abs(i - this.lastMotionY) > this.touchSlop) {
        this.lastMotionY = i;
        return true;
      } 
    } 
    if (paramMotionEvent.getActionMasked() == 0) {
      boolean bool;
      this.activePointerId = -1;
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if (canDragView(paramV) && paramCoordinatorLayout.isPointInChildBounds((View)paramV, i, j)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isBeingDragged = bool;
      if (bool) {
        this.lastMotionY = j;
        this.activePointerId = paramMotionEvent.getPointerId(0);
        ensureVelocityTracker();
        OverScroller overScroller = this.scroller;
        if (overScroller != null && !overScroller.isFinished()) {
          this.scroller.abortAnimation();
          return true;
        } 
      } 
    } 
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker != null)
      velocityTracker.addMovement(paramMotionEvent); 
    return false;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    VelocityTracker velocityTracker2;
    int j = 0;
    int k = 0;
    int m = paramMotionEvent.getActionMasked();
    boolean bool2 = true;
    int i = k;
    switch (m) {
      case 6:
        if (paramMotionEvent.getActionIndex() == 0) {
          i = 1;
        } else {
          i = 0;
        } 
        this.activePointerId = paramMotionEvent.getPointerId(i);
        this.lastMotionY = (int)(paramMotionEvent.getY(i) + 0.5F);
        break;
      case 2:
        i = paramMotionEvent.findPointerIndex(this.activePointerId);
        if (i == -1)
          return false; 
        k = (int)paramMotionEvent.getY(i);
        i = this.lastMotionY;
        this.lastMotionY = k;
        scroll(paramCoordinatorLayout, paramV, i - k, getMaxDragOffset(paramV), 0);
        break;
      case 1:
        velocityTracker2 = this.velocityTracker;
        i = k;
        if (velocityTracker2 != null) {
          i = 1;
          velocityTracker2.addMovement(paramMotionEvent);
          this.velocityTracker.computeCurrentVelocity(1000);
          float f = this.velocityTracker.getYVelocity(this.activePointerId);
          fling(paramCoordinatorLayout, paramV, -getScrollRangeForDragFling(paramV), 0, f);
        } 
      case 3:
        this.isBeingDragged = false;
        this.activePointerId = -1;
        velocityTracker1 = this.velocityTracker;
        j = i;
        if (velocityTracker1 != null) {
          velocityTracker1.recycle();
          this.velocityTracker = null;
          j = i;
        } 
        break;
    } 
    VelocityTracker velocityTracker1 = this.velocityTracker;
    if (velocityTracker1 != null)
      velocityTracker1.addMovement(paramMotionEvent); 
    boolean bool1 = bool2;
    if (!this.isBeingDragged)
      if (j != 0) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  final int scroll(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, getTopBottomOffsetForScrollingSibling() - paramInt1, paramInt2, paramInt3);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, paramInt, -2147483648, 2147483647);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    int j = getTopAndBottomOffset();
    byte b = 0;
    int i = b;
    if (paramInt2 != 0) {
      i = b;
      if (j >= paramInt2) {
        i = b;
        if (j <= paramInt3) {
          paramInt1 = MathUtils.clamp(paramInt1, paramInt2, paramInt3);
          i = b;
          if (j != paramInt1) {
            setTopAndBottomOffset(paramInt1);
            i = j - paramInt1;
          } 
        } 
      } 
    } 
    return i;
  }
  
  private class FlingRunnable implements Runnable {
    private final V layout;
    
    private final CoordinatorLayout parent;
    
    final HeaderBehavior this$0;
    
    FlingRunnable(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      this.parent = param1CoordinatorLayout;
      this.layout = param1V;
    }
    
    public void run() {
      if (this.layout != null && HeaderBehavior.this.scroller != null)
        if (HeaderBehavior.this.scroller.computeScrollOffset()) {
          HeaderBehavior<V> headerBehavior = HeaderBehavior.this;
          headerBehavior.setHeaderTopBottomOffset(this.parent, this.layout, headerBehavior.scroller.getCurrY());
          ViewCompat.postOnAnimation((View)this.layout, this);
        } else {
          HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
        }  
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\HeaderBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */