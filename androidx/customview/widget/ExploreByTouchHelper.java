package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityRecord;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
  private static final String DEFAULT_CLASS_NAME = "android.view.View";
  
  public static final int HOST_ID = -1;
  
  public static final int INVALID_ID = -2147483648;
  
  private static final Rect INVALID_PARENT_BOUNDS = new Rect(2147483647, 2147483647, -2147483648, -2147483648);
  
  private static final FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat>() {
      public void obtainBounds(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat, Rect param1Rect) {
        param1AccessibilityNodeInfoCompat.getBoundsInParent(param1Rect);
      }
    };
  
  private static final FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat>() {
      public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> param1SparseArrayCompat, int param1Int) {
        return (AccessibilityNodeInfoCompat)param1SparseArrayCompat.valueAt(param1Int);
      }
      
      public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> param1SparseArrayCompat) {
        return param1SparseArrayCompat.size();
      }
    };
  
  int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
  
  private final View mHost;
  
  private int mHoveredVirtualViewId = Integer.MIN_VALUE;
  
  int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
  
  private final AccessibilityManager mManager;
  
  private MyNodeProvider mNodeProvider;
  
  private final int[] mTempGlobalRect = new int[2];
  
  private final Rect mTempParentRect = new Rect();
  
  private final Rect mTempScreenRect = new Rect();
  
  private final Rect mTempVisibleRect = new Rect();
  
  public ExploreByTouchHelper(View paramView) {
    if (paramView != null) {
      this.mHost = paramView;
      this.mManager = (AccessibilityManager)paramView.getContext().getSystemService("accessibility");
      paramView.setFocusable(true);
      if (ViewCompat.getImportantForAccessibility(paramView) == 0)
        ViewCompat.setImportantForAccessibility(paramView, 1); 
      return;
    } 
    throw new IllegalArgumentException("View may not be null");
  }
  
  private boolean clearAccessibilityFocus(int paramInt) {
    if (this.mAccessibilityFocusedVirtualViewId == paramInt) {
      this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
      this.mHost.invalidate();
      sendEventForVirtualView(paramInt, 65536);
      return true;
    } 
    return false;
  }
  
  private boolean clickKeyboardFocusedVirtualView() {
    boolean bool;
    int i = this.mKeyboardFocusedVirtualViewId;
    if (i != Integer.MIN_VALUE && onPerformActionForVirtualView(i, 16, null)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private AccessibilityEvent createEvent(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return createEventForChild(paramInt1, paramInt2);
      case -1:
        break;
    } 
    return createEventForHost(paramInt2);
  }
  
  private AccessibilityEvent createEventForChild(int paramInt1, int paramInt2) {
    AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain(paramInt2);
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = obtainAccessibilityNodeInfo(paramInt1);
    accessibilityEvent.getText().add(accessibilityNodeInfoCompat.getText());
    accessibilityEvent.setContentDescription(accessibilityNodeInfoCompat.getContentDescription());
    accessibilityEvent.setScrollable(accessibilityNodeInfoCompat.isScrollable());
    accessibilityEvent.setPassword(accessibilityNodeInfoCompat.isPassword());
    accessibilityEvent.setEnabled(accessibilityNodeInfoCompat.isEnabled());
    accessibilityEvent.setChecked(accessibilityNodeInfoCompat.isChecked());
    onPopulateEventForVirtualView(paramInt1, accessibilityEvent);
    if (!accessibilityEvent.getText().isEmpty() || accessibilityEvent.getContentDescription() != null) {
      accessibilityEvent.setClassName(accessibilityNodeInfoCompat.getClassName());
      AccessibilityRecordCompat.setSource((AccessibilityRecord)accessibilityEvent, this.mHost, paramInt1);
      accessibilityEvent.setPackageName(this.mHost.getContext().getPackageName());
      return accessibilityEvent;
    } 
    throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
  }
  
  private AccessibilityEvent createEventForHost(int paramInt) {
    AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain(paramInt);
    this.mHost.onInitializeAccessibilityEvent(accessibilityEvent);
    return accessibilityEvent;
  }
  
  private AccessibilityNodeInfoCompat createNodeForChild(int paramInt) {
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain();
    accessibilityNodeInfoCompat.setEnabled(true);
    accessibilityNodeInfoCompat.setFocusable(true);
    accessibilityNodeInfoCompat.setClassName("android.view.View");
    Rect rect = INVALID_PARENT_BOUNDS;
    accessibilityNodeInfoCompat.setBoundsInParent(rect);
    accessibilityNodeInfoCompat.setBoundsInScreen(rect);
    accessibilityNodeInfoCompat.setParent(this.mHost);
    onPopulateNodeForVirtualView(paramInt, accessibilityNodeInfoCompat);
    if (accessibilityNodeInfoCompat.getText() != null || accessibilityNodeInfoCompat.getContentDescription() != null) {
      accessibilityNodeInfoCompat.getBoundsInParent(this.mTempParentRect);
      if (!this.mTempParentRect.equals(rect)) {
        int i = accessibilityNodeInfoCompat.getActions();
        if ((i & 0x40) == 0) {
          if ((i & 0x80) == 0) {
            boolean bool;
            accessibilityNodeInfoCompat.setPackageName(this.mHost.getContext().getPackageName());
            accessibilityNodeInfoCompat.setSource(this.mHost, paramInt);
            if (this.mAccessibilityFocusedVirtualViewId == paramInt) {
              accessibilityNodeInfoCompat.setAccessibilityFocused(true);
              accessibilityNodeInfoCompat.addAction(128);
            } else {
              accessibilityNodeInfoCompat.setAccessibilityFocused(false);
              accessibilityNodeInfoCompat.addAction(64);
            } 
            if (this.mKeyboardFocusedVirtualViewId == paramInt) {
              bool = true;
            } else {
              bool = false;
            } 
            if (bool) {
              accessibilityNodeInfoCompat.addAction(2);
            } else if (accessibilityNodeInfoCompat.isFocusable()) {
              accessibilityNodeInfoCompat.addAction(1);
            } 
            accessibilityNodeInfoCompat.setFocused(bool);
            this.mHost.getLocationOnScreen(this.mTempGlobalRect);
            accessibilityNodeInfoCompat.getBoundsInScreen(this.mTempScreenRect);
            if (this.mTempScreenRect.equals(rect)) {
              accessibilityNodeInfoCompat.getBoundsInParent(this.mTempScreenRect);
              if (accessibilityNodeInfoCompat.mParentVirtualDescendantId != -1) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat1 = AccessibilityNodeInfoCompat.obtain();
                for (paramInt = accessibilityNodeInfoCompat.mParentVirtualDescendantId; paramInt != -1; paramInt = accessibilityNodeInfoCompat1.mParentVirtualDescendantId) {
                  accessibilityNodeInfoCompat1.setParent(this.mHost, -1);
                  accessibilityNodeInfoCompat1.setBoundsInParent(INVALID_PARENT_BOUNDS);
                  onPopulateNodeForVirtualView(paramInt, accessibilityNodeInfoCompat1);
                  accessibilityNodeInfoCompat1.getBoundsInParent(this.mTempParentRect);
                  this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
                } 
                accessibilityNodeInfoCompat1.recycle();
              } 
              this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
            } 
            if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
              this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
              if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                accessibilityNodeInfoCompat.setBoundsInScreen(this.mTempScreenRect);
                if (isVisibleToUser(this.mTempScreenRect))
                  accessibilityNodeInfoCompat.setVisibleToUser(true); 
              } 
            } 
            return accessibilityNodeInfoCompat;
          } 
          throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        } 
        throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
      } 
      throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
    } 
    throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
  }
  
  private AccessibilityNodeInfoCompat createNodeForHost() {
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(this.mHost);
    ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, accessibilityNodeInfoCompat);
    ArrayList<Integer> arrayList = new ArrayList();
    getVisibleVirtualViews(arrayList);
    if (accessibilityNodeInfoCompat.getChildCount() <= 0 || arrayList.size() <= 0) {
      byte b = 0;
      int i = arrayList.size();
      while (b < i) {
        accessibilityNodeInfoCompat.addChild(this.mHost, ((Integer)arrayList.get(b)).intValue());
        b++;
      } 
      return accessibilityNodeInfoCompat;
    } 
    throw new RuntimeException("Views cannot have both real and virtual children");
  }
  
  private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
    ArrayList<Integer> arrayList = new ArrayList();
    getVisibleVirtualViews(arrayList);
    SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = new SparseArrayCompat();
    for (byte b = 0; b < arrayList.size(); b++)
      sparseArrayCompat.put(b, createNodeForChild(b)); 
    return sparseArrayCompat;
  }
  
  private void getBoundsInParent(int paramInt, Rect paramRect) {
    obtainAccessibilityNodeInfo(paramInt).getBoundsInParent(paramRect);
  }
  
  private static Rect guessPreviouslyFocusedRect(View paramView, int paramInt, Rect paramRect) {
    int j = paramView.getWidth();
    int i = paramView.getHeight();
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        paramRect.set(0, -1, j, -1);
        return paramRect;
      case 66:
        paramRect.set(-1, 0, -1, i);
        return paramRect;
      case 33:
        paramRect.set(0, i, j, i);
        return paramRect;
      case 17:
        break;
    } 
    paramRect.set(j, 0, j, i);
    return paramRect;
  }
  
  private boolean isVisibleToUser(Rect paramRect) {
    boolean bool = false;
    if (paramRect == null || paramRect.isEmpty())
      return false; 
    if (this.mHost.getWindowVisibility() != 0)
      return false; 
    ViewParent viewParent = this.mHost.getParent();
    while (viewParent instanceof View) {
      View view = (View)viewParent;
      if (view.getAlpha() <= 0.0F || view.getVisibility() != 0)
        return false; 
      viewParent = view.getParent();
    } 
    if (viewParent != null)
      bool = true; 
    return bool;
  }
  
  private static int keyToDirection(int paramInt) {
    switch (paramInt) {
      default:
        return 130;
      case 22:
        return 66;
      case 21:
        return 17;
      case 19:
        break;
    } 
    return 33;
  }
  
  private boolean moveFocus(int paramInt, Rect paramRect) {
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat1;
    boolean bool;
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2;
    Rect rect;
    SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = getAllNodes();
    int i = this.mKeyboardFocusedVirtualViewId;
    if (i == Integer.MIN_VALUE) {
      accessibilityNodeInfoCompat2 = null;
    } else {
      accessibilityNodeInfoCompat2 = (AccessibilityNodeInfoCompat)sparseArrayCompat.get(i);
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
      case 33:
      case 66:
      case 130:
        rect = new Rect();
        i = this.mKeyboardFocusedVirtualViewId;
        if (i != Integer.MIN_VALUE) {
          getBoundsInParent(i, rect);
        } else if (paramRect != null) {
          rect.set(paramRect);
        } else {
          guessPreviouslyFocusedRect(this.mHost, paramInt, rect);
        } 
        accessibilityNodeInfoCompat1 = FocusStrategy.<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat>findNextFocusInAbsoluteDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat2, rect, paramInt);
        break;
      case 1:
      case 2:
        if (ViewCompat.getLayoutDirection(this.mHost) == 1) {
          bool = true;
        } else {
          bool = false;
        } 
        accessibilityNodeInfoCompat1 = FocusStrategy.<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat>findNextFocusInRelativeDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat2, paramInt, bool, false);
        break;
    } 
    if (accessibilityNodeInfoCompat1 == null) {
      paramInt = Integer.MIN_VALUE;
    } else {
      paramInt = sparseArrayCompat.keyAt(sparseArrayCompat.indexOfValue(accessibilityNodeInfoCompat1));
    } 
    return requestKeyboardFocusForVirtualView(paramInt);
  }
  
  private boolean performActionForChild(int paramInt1, int paramInt2, Bundle paramBundle) {
    switch (paramInt2) {
      default:
        return onPerformActionForVirtualView(paramInt1, paramInt2, paramBundle);
      case 128:
        return clearAccessibilityFocus(paramInt1);
      case 64:
        return requestAccessibilityFocus(paramInt1);
      case 2:
        return clearKeyboardFocusForVirtualView(paramInt1);
      case 1:
        break;
    } 
    return requestKeyboardFocusForVirtualView(paramInt1);
  }
  
  private boolean performActionForHost(int paramInt, Bundle paramBundle) {
    return ViewCompat.performAccessibilityAction(this.mHost, paramInt, paramBundle);
  }
  
  private boolean requestAccessibilityFocus(int paramInt) {
    if (!this.mManager.isEnabled() || !this.mManager.isTouchExplorationEnabled())
      return false; 
    int i = this.mAccessibilityFocusedVirtualViewId;
    if (i != paramInt) {
      if (i != Integer.MIN_VALUE)
        clearAccessibilityFocus(i); 
      this.mAccessibilityFocusedVirtualViewId = paramInt;
      this.mHost.invalidate();
      sendEventForVirtualView(paramInt, 32768);
      return true;
    } 
    return false;
  }
  
  private void updateHoveredVirtualView(int paramInt) {
    if (this.mHoveredVirtualViewId == paramInt)
      return; 
    int i = this.mHoveredVirtualViewId;
    this.mHoveredVirtualViewId = paramInt;
    sendEventForVirtualView(paramInt, 128);
    sendEventForVirtualView(i, 256);
  }
  
  public final boolean clearKeyboardFocusForVirtualView(int paramInt) {
    if (this.mKeyboardFocusedVirtualViewId != paramInt)
      return false; 
    this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    onVirtualViewKeyboardFocusChanged(paramInt, false);
    sendEventForVirtualView(paramInt, 8);
    return true;
  }
  
  public final boolean dispatchHoverEvent(MotionEvent paramMotionEvent) {
    boolean bool1 = this.mManager.isEnabled();
    boolean bool = false;
    if (!bool1 || !this.mManager.isTouchExplorationEnabled())
      return false; 
    switch (paramMotionEvent.getAction()) {
      default:
        return false;
      case 10:
        if (this.mHoveredVirtualViewId != Integer.MIN_VALUE) {
          updateHoveredVirtualView(-2147483648);
          return true;
        } 
        return false;
      case 7:
      case 9:
        break;
    } 
    int i = getVirtualViewAt(paramMotionEvent.getX(), paramMotionEvent.getY());
    updateHoveredVirtualView(i);
    if (i != Integer.MIN_VALUE)
      bool = true; 
    return bool;
  }
  
  public final boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool2 = false;
    boolean bool = false;
    boolean bool1 = bool2;
    if (paramKeyEvent.getAction() != 1) {
      int i = paramKeyEvent.getKeyCode();
      switch (i) {
        default:
          return bool2;
        case 61:
          if (paramKeyEvent.hasNoModifiers()) {
            bool1 = moveFocus(2, null);
          } else {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(1))
              bool1 = moveFocus(1, null); 
          } 
          return bool1;
        case 23:
        case 66:
          bool1 = bool2;
          if (paramKeyEvent.hasNoModifiers()) {
            bool1 = bool2;
            if (paramKeyEvent.getRepeatCount() == 0) {
              clickKeyboardFocusedVirtualView();
              bool1 = true;
            } 
          } 
          return bool1;
        case 19:
        case 20:
        case 21:
        case 22:
          break;
      } 
      bool1 = bool2;
      if (paramKeyEvent.hasNoModifiers()) {
        int k = keyToDirection(i);
        int j = paramKeyEvent.getRepeatCount();
        i = 0;
        while (true) {
          bool1 = bool;
          if (i < j + 1) {
            bool1 = bool;
            if (moveFocus(k, null)) {
              bool = true;
              i++;
              continue;
            } 
          } 
          break;
        } 
      } 
    } 
    return bool1;
  }
  
  public final int getAccessibilityFocusedVirtualViewId() {
    return this.mAccessibilityFocusedVirtualViewId;
  }
  
  public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View paramView) {
    if (this.mNodeProvider == null)
      this.mNodeProvider = new MyNodeProvider(); 
    return this.mNodeProvider;
  }
  
  @Deprecated
  public int getFocusedVirtualView() {
    return getAccessibilityFocusedVirtualViewId();
  }
  
  public final int getKeyboardFocusedVirtualViewId() {
    return this.mKeyboardFocusedVirtualViewId;
  }
  
  protected abstract int getVirtualViewAt(float paramFloat1, float paramFloat2);
  
  protected abstract void getVisibleVirtualViews(List<Integer> paramList);
  
  public final void invalidateRoot() {
    invalidateVirtualView(-1, 1);
  }
  
  public final void invalidateVirtualView(int paramInt) {
    invalidateVirtualView(paramInt, 0);
  }
  
  public final void invalidateVirtualView(int paramInt1, int paramInt2) {
    if (paramInt1 != Integer.MIN_VALUE && this.mManager.isEnabled()) {
      ViewParent viewParent = this.mHost.getParent();
      if (viewParent != null) {
        AccessibilityEvent accessibilityEvent = createEvent(paramInt1, 2048);
        AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, paramInt2);
        ViewParentCompat.requestSendAccessibilityEvent(viewParent, this.mHost, accessibilityEvent);
      } 
    } 
  }
  
  AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int paramInt) {
    return (paramInt == -1) ? createNodeForHost() : createNodeForChild(paramInt);
  }
  
  public final void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    int i = this.mKeyboardFocusedVirtualViewId;
    if (i != Integer.MIN_VALUE)
      clearKeyboardFocusForVirtualView(i); 
    if (paramBoolean)
      moveFocus(paramInt, paramRect); 
  }
  
  public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
    onPopulateEventForHost(paramAccessibilityEvent);
  }
  
  public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
    onPopulateNodeForHost(paramAccessibilityNodeInfoCompat);
  }
  
  protected abstract boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle);
  
  protected void onPopulateEventForHost(AccessibilityEvent paramAccessibilityEvent) {}
  
  protected void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent) {}
  
  protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {}
  
  protected abstract void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat);
  
  protected void onVirtualViewKeyboardFocusChanged(int paramInt, boolean paramBoolean) {}
  
  boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle) {
    switch (paramInt1) {
      default:
        return performActionForChild(paramInt1, paramInt2, paramBundle);
      case -1:
        break;
    } 
    return performActionForHost(paramInt2, paramBundle);
  }
  
  public final boolean requestKeyboardFocusForVirtualView(int paramInt) {
    if (!this.mHost.isFocused() && !this.mHost.requestFocus())
      return false; 
    int i = this.mKeyboardFocusedVirtualViewId;
    if (i == paramInt)
      return false; 
    if (i != Integer.MIN_VALUE)
      clearKeyboardFocusForVirtualView(i); 
    this.mKeyboardFocusedVirtualViewId = paramInt;
    onVirtualViewKeyboardFocusChanged(paramInt, true);
    sendEventForVirtualView(paramInt, 8);
    return true;
  }
  
  public final boolean sendEventForVirtualView(int paramInt1, int paramInt2) {
    if (paramInt1 == Integer.MIN_VALUE || !this.mManager.isEnabled())
      return false; 
    ViewParent viewParent = this.mHost.getParent();
    if (viewParent == null)
      return false; 
    AccessibilityEvent accessibilityEvent = createEvent(paramInt1, paramInt2);
    return ViewParentCompat.requestSendAccessibilityEvent(viewParent, this.mHost, accessibilityEvent);
  }
  
  private class MyNodeProvider extends AccessibilityNodeProviderCompat {
    final ExploreByTouchHelper this$0;
    
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int param1Int) {
      return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(param1Int));
    }
    
    public AccessibilityNodeInfoCompat findFocus(int param1Int) {
      if (param1Int == 2) {
        param1Int = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId;
      } else {
        param1Int = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
      } 
      return (param1Int == Integer.MIN_VALUE) ? null : createAccessibilityNodeInfo(param1Int);
    }
    
    public boolean performAction(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return ExploreByTouchHelper.this.performAction(param1Int1, param1Int2, param1Bundle);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\customview\widget\ExploreByTouchHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */