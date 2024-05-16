package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.widget.ViewDragHelper;

public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5F;
  
  private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0F;
  
  private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5F;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  public static final int SWIPE_DIRECTION_ANY = 2;
  
  public static final int SWIPE_DIRECTION_END_TO_START = 1;
  
  public static final int SWIPE_DIRECTION_START_TO_END = 0;
  
  float alphaEndSwipeDistance = 0.5F;
  
  float alphaStartSwipeDistance = 0.0F;
  
  private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      private static final int INVALID_POINTER_ID = -1;
      
      private int activePointerId = -1;
      
      private int originalCapturedViewLeft;
      
      final SwipeDismissBehavior this$0;
      
      private boolean shouldDismiss(View param1View, float param1Float) {
        boolean bool1 = false;
        boolean bool3 = false;
        boolean bool2 = false;
        if (param1Float != 0.0F) {
          boolean bool;
          if (ViewCompat.getLayoutDirection(param1View) == 1) {
            bool = true;
          } else {
            bool = false;
          } 
          if (SwipeDismissBehavior.this.swipeDirection == 2)
            return true; 
          if (SwipeDismissBehavior.this.swipeDirection == 0) {
            if (bool) {
              bool1 = bool2;
              if (param1Float < 0.0F)
                return true; 
            } else {
              bool1 = bool2;
              if (param1Float > 0.0F)
                return true; 
            } 
            return bool1;
          } 
          if (SwipeDismissBehavior.this.swipeDirection == 1) {
            if (bool ? (param1Float > 0.0F) : (param1Float < 0.0F))
              bool1 = true; 
            return bool1;
          } 
          return false;
        } 
        int i = param1View.getLeft();
        int k = this.originalCapturedViewLeft;
        int j = Math.round(param1View.getWidth() * SwipeDismissBehavior.this.dragDismissThreshold);
        bool1 = bool3;
        if (Math.abs(i - k) >= j)
          bool1 = true; 
        return bool1;
      }
      
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        int i;
        if (ViewCompat.getLayoutDirection(param1View) == 1) {
          param1Int2 = 1;
        } else {
          param1Int2 = 0;
        } 
        if (SwipeDismissBehavior.this.swipeDirection == 0) {
          if (param1Int2 != 0) {
            param1Int2 = this.originalCapturedViewLeft - param1View.getWidth();
            i = this.originalCapturedViewLeft;
          } else {
            param1Int2 = this.originalCapturedViewLeft;
            i = this.originalCapturedViewLeft + param1View.getWidth();
          } 
        } else if (SwipeDismissBehavior.this.swipeDirection == 1) {
          if (param1Int2 != 0) {
            param1Int2 = this.originalCapturedViewLeft;
            i = this.originalCapturedViewLeft + param1View.getWidth();
          } else {
            param1Int2 = this.originalCapturedViewLeft - param1View.getWidth();
            i = this.originalCapturedViewLeft;
          } 
        } else {
          param1Int2 = this.originalCapturedViewLeft - param1View.getWidth();
          i = this.originalCapturedViewLeft + param1View.getWidth();
        } 
        return SwipeDismissBehavior.clamp(param1Int2, param1Int1, i);
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        return param1View.getTop();
      }
      
      public int getViewHorizontalDragRange(View param1View) {
        return param1View.getWidth();
      }
      
      public void onViewCaptured(View param1View, int param1Int) {
        this.activePointerId = param1Int;
        this.originalCapturedViewLeft = param1View.getLeft();
        ViewParent viewParent = param1View.getParent();
        if (viewParent != null)
          viewParent.requestDisallowInterceptTouchEvent(true); 
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (SwipeDismissBehavior.this.listener != null)
          SwipeDismissBehavior.this.listener.onDragStateChanged(param1Int); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        float f1 = this.originalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.alphaStartSwipeDistance;
        float f2 = this.originalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.alphaEndSwipeDistance;
        if (param1Int1 <= f1) {
          param1View.setAlpha(1.0F);
        } else if (param1Int1 >= f2) {
          param1View.setAlpha(0.0F);
        } else {
          param1View.setAlpha(SwipeDismissBehavior.clamp(0.0F, 1.0F - SwipeDismissBehavior.fraction(f1, f2, param1Int1), 1.0F));
        } 
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        int i;
        this.activePointerId = -1;
        int j = param1View.getWidth();
        boolean bool = false;
        if (shouldDismiss(param1View, param1Float1)) {
          i = param1View.getLeft();
          int k = this.originalCapturedViewLeft;
          if (i < k) {
            i = k - j;
          } else {
            i = k + j;
          } 
          bool = true;
        } else {
          i = this.originalCapturedViewLeft;
        } 
        if (SwipeDismissBehavior.this.viewDragHelper.settleCapturedViewAt(i, param1View.getTop())) {
          ViewCompat.postOnAnimation(param1View, new SwipeDismissBehavior.SettleRunnable(param1View, bool));
        } else if (bool && SwipeDismissBehavior.this.listener != null) {
          SwipeDismissBehavior.this.listener.onDismiss(param1View);
        } 
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        boolean bool;
        int i = this.activePointerId;
        if ((i == -1 || i == param1Int) && SwipeDismissBehavior.this.canSwipeDismissView(param1View)) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
    };
  
  float dragDismissThreshold = 0.5F;
  
  private boolean interceptingEvents;
  
  OnDismissListener listener;
  
  private float sensitivity = 0.0F;
  
  private boolean sensitivitySet;
  
  int swipeDirection = 2;
  
  ViewDragHelper viewDragHelper;
  
  static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
  }
  
  static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return Math.min(Math.max(paramInt1, paramInt2), paramInt3);
  }
  
  private void ensureViewDragHelper(ViewGroup paramViewGroup) {
    if (this.viewDragHelper == null) {
      ViewDragHelper viewDragHelper;
      if (this.sensitivitySet) {
        viewDragHelper = ViewDragHelper.create(paramViewGroup, this.sensitivity, this.dragCallback);
      } else {
        viewDragHelper = ViewDragHelper.create((ViewGroup)viewDragHelper, this.dragCallback);
      } 
      this.viewDragHelper = viewDragHelper;
    } 
  }
  
  static float fraction(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1);
  }
  
  private void updateAccessibilityActions(View paramView) {
    ViewCompat.removeAccessibilityAction(paramView, 1048576);
    if (canSwipeDismissView(paramView))
      ViewCompat.replaceAccessibilityAction(paramView, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, null, new AccessibilityViewCommand() {
            final SwipeDismissBehavior this$0;
            
            public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
              // Byte code:
              //   0: aload_0
              //   1: getfield this$0 : Lcom/google/android/material/behavior/SwipeDismissBehavior;
              //   4: aload_1
              //   5: invokevirtual canSwipeDismissView : (Landroid/view/View;)Z
              //   8: istore #6
              //   10: iconst_0
              //   11: istore #5
              //   13: iload #6
              //   15: ifeq -> 129
              //   18: aload_1
              //   19: invokestatic getLayoutDirection : (Landroid/view/View;)I
              //   22: iconst_1
              //   23: if_icmpne -> 32
              //   26: iconst_1
              //   27: istore #4
              //   29: goto -> 35
              //   32: iconst_0
              //   33: istore #4
              //   35: aload_0
              //   36: getfield this$0 : Lcom/google/android/material/behavior/SwipeDismissBehavior;
              //   39: getfield swipeDirection : I
              //   42: ifne -> 50
              //   45: iload #4
              //   47: ifne -> 72
              //   50: iload #5
              //   52: istore_3
              //   53: aload_0
              //   54: getfield this$0 : Lcom/google/android/material/behavior/SwipeDismissBehavior;
              //   57: getfield swipeDirection : I
              //   60: iconst_1
              //   61: if_icmpne -> 74
              //   64: iload #5
              //   66: istore_3
              //   67: iload #4
              //   69: ifne -> 74
              //   72: iconst_1
              //   73: istore_3
              //   74: aload_1
              //   75: invokevirtual getWidth : ()I
              //   78: istore #5
              //   80: iload #5
              //   82: istore #4
              //   84: iload_3
              //   85: ifeq -> 93
              //   88: iload #5
              //   90: ineg
              //   91: istore #4
              //   93: aload_1
              //   94: iload #4
              //   96: invokestatic offsetLeftAndRight : (Landroid/view/View;I)V
              //   99: aload_1
              //   100: fconst_0
              //   101: invokevirtual setAlpha : (F)V
              //   104: aload_0
              //   105: getfield this$0 : Lcom/google/android/material/behavior/SwipeDismissBehavior;
              //   108: getfield listener : Lcom/google/android/material/behavior/SwipeDismissBehavior$OnDismissListener;
              //   111: ifnull -> 127
              //   114: aload_0
              //   115: getfield this$0 : Lcom/google/android/material/behavior/SwipeDismissBehavior;
              //   118: getfield listener : Lcom/google/android/material/behavior/SwipeDismissBehavior$OnDismissListener;
              //   121: aload_1
              //   122: invokeinterface onDismiss : (Landroid/view/View;)V
              //   127: iconst_1
              //   128: ireturn
              //   129: iconst_0
              //   130: ireturn
            }
          }); 
  }
  
  public boolean canSwipeDismissView(View paramView) {
    return true;
  }
  
  public int getDragState() {
    boolean bool;
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null) {
      bool = viewDragHelper.getViewDragState();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public OnDismissListener getListener() {
    return this.listener;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    boolean bool = this.interceptingEvents;
    switch (paramMotionEvent.getActionMasked()) {
      case 1:
      case 3:
        this.interceptingEvents = false;
        break;
      case 0:
        this.interceptingEvents = paramCoordinatorLayout.isPointInChildBounds((View)paramV, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        bool = this.interceptingEvents;
        break;
    } 
    if (bool) {
      ensureViewDragHelper((ViewGroup)paramCoordinatorLayout);
      return this.viewDragHelper.shouldInterceptTouchEvent(paramMotionEvent);
    } 
    return false;
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    boolean bool = super.onLayoutChild(paramCoordinatorLayout, (View)paramV, paramInt);
    if (ViewCompat.getImportantForAccessibility((View)paramV) == 0) {
      ViewCompat.setImportantForAccessibility((View)paramV, 1);
      updateAccessibilityActions((View)paramV);
    } 
    return bool;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null) {
      viewDragHelper.processTouchEvent(paramMotionEvent);
      return true;
    } 
    return false;
  }
  
  public void setDragDismissDistance(float paramFloat) {
    this.dragDismissThreshold = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setEndAlphaSwipeDistance(float paramFloat) {
    this.alphaEndSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setListener(OnDismissListener paramOnDismissListener) {
    this.listener = paramOnDismissListener;
  }
  
  public void setSensitivity(float paramFloat) {
    this.sensitivity = paramFloat;
    this.sensitivitySet = true;
  }
  
  public void setStartAlphaSwipeDistance(float paramFloat) {
    this.alphaStartSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setSwipeDirection(int paramInt) {
    this.swipeDirection = paramInt;
  }
  
  public static interface OnDismissListener {
    void onDismiss(View param1View);
    
    void onDragStateChanged(int param1Int);
  }
  
  private class SettleRunnable implements Runnable {
    private final boolean dismiss;
    
    final SwipeDismissBehavior this$0;
    
    private final View view;
    
    SettleRunnable(View param1View, boolean param1Boolean) {
      this.view = param1View;
      this.dismiss = param1Boolean;
    }
    
    public void run() {
      if (SwipeDismissBehavior.this.viewDragHelper != null && SwipeDismissBehavior.this.viewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.view, this);
      } else if (this.dismiss && SwipeDismissBehavior.this.listener != null) {
        SwipeDismissBehavior.this.listener.onDismiss(this.view);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\behavior\SwipeDismissBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */