package androidx.core.view;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public final class ViewParentCompat {
  private static final String TAG = "ViewParentCompat";
  
  private static int[] sTempNestedScrollConsumed;
  
  private static int[] getTempNestedScrollConsumed() {
    int[] arrayOfInt = sTempNestedScrollConsumed;
    if (arrayOfInt == null) {
      sTempNestedScrollConsumed = new int[2];
    } else {
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
    } 
    return sTempNestedScrollConsumed;
  }
  
  public static void notifySubtreeAccessibilityStateChanged(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19)
      paramViewParent.notifySubtreeAccessibilityStateChanged(paramView1, paramView2, paramInt); 
  }
  
  public static boolean onNestedFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        return paramViewParent.onNestedFling(paramView, paramFloat1, paramFloat2, paramBoolean);
      } catch (AbstractMethodError abstractMethodError) {
        Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onNestedFling", abstractMethodError);
      } 
    } else if (paramViewParent instanceof NestedScrollingParent) {
      return ((NestedScrollingParent)paramViewParent).onNestedFling((View)abstractMethodError, paramFloat1, paramFloat2, paramBoolean);
    } 
    return false;
  }
  
  public static boolean onNestedPreFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        return paramViewParent.onNestedPreFling(paramView, paramFloat1, paramFloat2);
      } catch (AbstractMethodError abstractMethodError) {
        Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onNestedPreFling", abstractMethodError);
      } 
    } else if (paramViewParent instanceof NestedScrollingParent) {
      return ((NestedScrollingParent)paramViewParent).onNestedPreFling((View)abstractMethodError, paramFloat1, paramFloat2);
    } 
    return false;
  }
  
  public static void onNestedPreScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    onNestedPreScroll(paramViewParent, paramView, paramInt1, paramInt2, paramArrayOfint, 0);
  }
  
  public static void onNestedPreScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint, paramInt3);
    } else if (paramInt3 == 0) {
      if (Build.VERSION.SDK_INT >= 21) {
        try {
          paramViewParent.onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint);
        } catch (AbstractMethodError abstractMethodError) {
          Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onNestedPreScroll", abstractMethodError);
        } 
      } else if (paramViewParent instanceof NestedScrollingParent) {
        ((NestedScrollingParent)paramViewParent).onNestedPreScroll((View)abstractMethodError, paramInt1, paramInt2, paramArrayOfint);
      } 
    } 
  }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    onNestedScroll(paramViewParent, paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0, getTempNestedScrollConsumed());
  }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    onNestedScroll(paramViewParent, paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, getTempNestedScrollConsumed());
  }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfint) {
    if (paramViewParent instanceof NestedScrollingParent3) {
      ((NestedScrollingParent3)paramViewParent).onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfint);
    } else {
      paramArrayOfint[0] = paramArrayOfint[0] + paramInt3;
      paramArrayOfint[1] = paramArrayOfint[1] + paramInt4;
      if (paramViewParent instanceof NestedScrollingParent2) {
        ((NestedScrollingParent2)paramViewParent).onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
      } else if (paramInt5 == 0) {
        if (Build.VERSION.SDK_INT >= 21) {
          try {
            paramViewParent.onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
          } catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onNestedScroll", abstractMethodError);
          } 
        } else if (paramViewParent instanceof NestedScrollingParent) {
          ((NestedScrollingParent)paramViewParent).onNestedScroll((View)abstractMethodError, paramInt1, paramInt2, paramInt3, paramInt4);
        } 
      } 
    } 
  }
  
  public static void onNestedScrollAccepted(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    onNestedScrollAccepted(paramViewParent, paramView1, paramView2, paramInt, 0);
  }
  
  public static void onNestedScrollAccepted(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
    } else if (paramInt2 == 0) {
      if (Build.VERSION.SDK_INT >= 21) {
        try {
          paramViewParent.onNestedScrollAccepted(paramView1, paramView2, paramInt1);
        } catch (AbstractMethodError abstractMethodError) {
          Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onNestedScrollAccepted", abstractMethodError);
        } 
      } else if (paramViewParent instanceof NestedScrollingParent) {
        ((NestedScrollingParent)paramViewParent).onNestedScrollAccepted((View)abstractMethodError, paramView2, paramInt1);
      } 
    } 
  }
  
  public static boolean onStartNestedScroll(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    return onStartNestedScroll(paramViewParent, paramView1, paramView2, paramInt, 0);
  }
  
  public static boolean onStartNestedScroll(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    if (paramViewParent instanceof NestedScrollingParent2)
      return ((NestedScrollingParent2)paramViewParent).onStartNestedScroll(paramView1, paramView2, paramInt1, paramInt2); 
    if (paramInt2 == 0)
      if (Build.VERSION.SDK_INT >= 21) {
        try {
          return paramViewParent.onStartNestedScroll(paramView1, paramView2, paramInt1);
        } catch (AbstractMethodError abstractMethodError) {
          Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onStartNestedScroll", abstractMethodError);
        } 
      } else if (paramViewParent instanceof NestedScrollingParent) {
        return ((NestedScrollingParent)paramViewParent).onStartNestedScroll((View)abstractMethodError, paramView2, paramInt1);
      }  
    return false;
  }
  
  public static void onStopNestedScroll(ViewParent paramViewParent, View paramView) {
    onStopNestedScroll(paramViewParent, paramView, 0);
  }
  
  public static void onStopNestedScroll(ViewParent paramViewParent, View paramView, int paramInt) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onStopNestedScroll(paramView, paramInt);
    } else if (paramInt == 0) {
      if (Build.VERSION.SDK_INT >= 21) {
        try {
          paramViewParent.onStopNestedScroll(paramView);
        } catch (AbstractMethodError abstractMethodError) {
          Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface method onStopNestedScroll", abstractMethodError);
        } 
      } else if (paramViewParent instanceof NestedScrollingParent) {
        ((NestedScrollingParent)paramViewParent).onStopNestedScroll((View)abstractMethodError);
      } 
    } 
  }
  
  @Deprecated
  public static boolean requestSendAccessibilityEvent(ViewParent paramViewParent, View paramView, AccessibilityEvent paramAccessibilityEvent) {
    return paramViewParent.requestSendAccessibilityEvent(paramView, paramAccessibilityEvent);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\ViewParentCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */