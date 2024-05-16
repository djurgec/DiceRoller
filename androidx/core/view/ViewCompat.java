package androidx.core.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContentInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.OnReceiveContentListener;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.collection.SimpleArrayMap;
import androidx.core.R;
import androidx.core.util.Preconditions;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
  private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
  
  public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
  
  public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
  
  public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
  
  @Deprecated
  public static final int LAYER_TYPE_HARDWARE = 2;
  
  @Deprecated
  public static final int LAYER_TYPE_NONE = 0;
  
  @Deprecated
  public static final int LAYER_TYPE_SOFTWARE = 1;
  
  public static final int LAYOUT_DIRECTION_INHERIT = 2;
  
  public static final int LAYOUT_DIRECTION_LOCALE = 3;
  
  public static final int LAYOUT_DIRECTION_LTR = 0;
  
  public static final int LAYOUT_DIRECTION_RTL = 1;
  
  @Deprecated
  public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
  
  @Deprecated
  public static final int MEASURED_SIZE_MASK = 16777215;
  
  @Deprecated
  public static final int MEASURED_STATE_MASK = -16777216;
  
  @Deprecated
  public static final int MEASURED_STATE_TOO_SMALL = 16777216;
  
  private static final OnReceiveContentViewBehavior NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR;
  
  @Deprecated
  public static final int OVER_SCROLL_ALWAYS = 0;
  
  @Deprecated
  public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
  
  @Deprecated
  public static final int OVER_SCROLL_NEVER = 2;
  
  public static final int SCROLL_AXIS_HORIZONTAL = 1;
  
  public static final int SCROLL_AXIS_NONE = 0;
  
  public static final int SCROLL_AXIS_VERTICAL = 2;
  
  public static final int SCROLL_INDICATOR_BOTTOM = 2;
  
  public static final int SCROLL_INDICATOR_END = 32;
  
  public static final int SCROLL_INDICATOR_LEFT = 4;
  
  public static final int SCROLL_INDICATOR_RIGHT = 8;
  
  public static final int SCROLL_INDICATOR_START = 16;
  
  public static final int SCROLL_INDICATOR_TOP = 1;
  
  private static final String TAG = "ViewCompat";
  
  public static final int TYPE_NON_TOUCH = 1;
  
  public static final int TYPE_TOUCH = 0;
  
  private static boolean sAccessibilityDelegateCheckFailed;
  
  private static Field sAccessibilityDelegateField;
  
  private static final AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager;
  
  private static Method sChildrenDrawingOrderMethod;
  
  private static Method sDispatchFinishTemporaryDetach;
  
  private static Method sDispatchStartTemporaryDetach;
  
  private static Field sMinHeightField;
  
  private static boolean sMinHeightFieldFetched;
  
  private static Field sMinWidthField;
  
  private static boolean sMinWidthFieldFetched;
  
  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
  
  private static boolean sTempDetachBound;
  
  private static ThreadLocal<Rect> sThreadLocalRect;
  
  private static WeakHashMap<View, String> sTransitionNameMap;
  
  private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;
  
  static {
    sAccessibilityDelegateCheckFailed = false;
    ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[] { 
        R.id.accessibility_custom_action_0, R.id.accessibility_custom_action_1, R.id.accessibility_custom_action_2, R.id.accessibility_custom_action_3, R.id.accessibility_custom_action_4, R.id.accessibility_custom_action_5, R.id.accessibility_custom_action_6, R.id.accessibility_custom_action_7, R.id.accessibility_custom_action_8, R.id.accessibility_custom_action_9, 
        R.id.accessibility_custom_action_10, R.id.accessibility_custom_action_11, R.id.accessibility_custom_action_12, R.id.accessibility_custom_action_13, R.id.accessibility_custom_action_14, R.id.accessibility_custom_action_15, R.id.accessibility_custom_action_16, R.id.accessibility_custom_action_17, R.id.accessibility_custom_action_18, R.id.accessibility_custom_action_19, 
        R.id.accessibility_custom_action_20, R.id.accessibility_custom_action_21, R.id.accessibility_custom_action_22, R.id.accessibility_custom_action_23, R.id.accessibility_custom_action_24, R.id.accessibility_custom_action_25, R.id.accessibility_custom_action_26, R.id.accessibility_custom_action_27, R.id.accessibility_custom_action_28, R.id.accessibility_custom_action_29, 
        R.id.accessibility_custom_action_30, R.id.accessibility_custom_action_31 };
    NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = ViewCompat$$ExternalSyntheticLambda0.INSTANCE;
    sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
  }
  
  private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
    return new AccessibilityViewProperty<Boolean>(R.id.tag_accessibility_heading, Boolean.class, 28) {
        Boolean frameworkGet(View param1View) {
          return Boolean.valueOf(ViewCompat.Api28Impl.isAccessibilityHeading(param1View));
        }
        
        void frameworkSet(View param1View, Boolean param1Boolean) {
          ViewCompat.Api28Impl.setAccessibilityHeading(param1View, param1Boolean.booleanValue());
        }
        
        boolean shouldUpdate(Boolean param1Boolean1, Boolean param1Boolean2) {
          return booleanNullToFalseEquals(param1Boolean1, param1Boolean2) ^ true;
        }
      };
  }
  
  public static int addAccessibilityAction(View paramView, CharSequence paramCharSequence, AccessibilityViewCommand paramAccessibilityViewCommand) {
    int i = getAvailableActionIdFromResources(paramView, paramCharSequence);
    if (i != -1)
      addAccessibilityAction(paramView, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, paramCharSequence, paramAccessibilityViewCommand)); 
    return i;
  }
  
  private static void addAccessibilityAction(View paramView, AccessibilityNodeInfoCompat.AccessibilityActionCompat paramAccessibilityActionCompat) {
    if (Build.VERSION.SDK_INT >= 21) {
      ensureAccessibilityDelegateCompat(paramView);
      removeActionWithId(paramAccessibilityActionCompat.getId(), paramView);
      getActionList(paramView).add(paramAccessibilityActionCompat);
      notifyViewAccessibilityStateChangedIfNeeded(paramView, 0);
    } 
  }
  
  public static void addKeyboardNavigationClusters(View paramView, Collection<View> paramCollection, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.addKeyboardNavigationClusters(paramView, paramCollection, paramInt); 
  }
  
  public static void addOnUnhandledKeyEventListener(View paramView, OnUnhandledKeyEventListenerCompat paramOnUnhandledKeyEventListenerCompat) {
    if (Build.VERSION.SDK_INT >= 28) {
      Api28Impl.addOnUnhandledKeyEventListener(paramView, paramOnUnhandledKeyEventListenerCompat);
      return;
    } 
    ArrayList<OnUnhandledKeyEventListenerCompat> arrayList2 = (ArrayList)paramView.getTag(R.id.tag_unhandled_key_listeners);
    ArrayList<OnUnhandledKeyEventListenerCompat> arrayList1 = arrayList2;
    if (arrayList2 == null) {
      arrayList1 = new ArrayList();
      paramView.setTag(R.id.tag_unhandled_key_listeners, arrayList1);
    } 
    arrayList1.add(paramOnUnhandledKeyEventListenerCompat);
    if (arrayList1.size() == 1)
      UnhandledKeyEventManager.registerListeningView(paramView); 
  }
  
  public static ViewPropertyAnimatorCompat animate(View paramView) {
    if (sViewPropertyAnimatorMap == null)
      sViewPropertyAnimatorMap = new WeakHashMap<>(); 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = sViewPropertyAnimatorMap.get(paramView);
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat1 = viewPropertyAnimatorCompat2;
    if (viewPropertyAnimatorCompat2 == null) {
      viewPropertyAnimatorCompat1 = new ViewPropertyAnimatorCompat(paramView);
      sViewPropertyAnimatorMap.put(paramView, viewPropertyAnimatorCompat1);
    } 
    return viewPropertyAnimatorCompat1;
  }
  
  private static void bindTempDetach() {
    try {
      sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
      sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("ViewCompat", "Couldn't find method", noSuchMethodException);
    } 
    sTempDetachBound = true;
  }
  
  @Deprecated
  public static boolean canScrollHorizontally(View paramView, int paramInt) {
    return paramView.canScrollHorizontally(paramInt);
  }
  
  @Deprecated
  public static boolean canScrollVertically(View paramView, int paramInt) {
    return paramView.canScrollVertically(paramInt);
  }
  
  public static void cancelDragAndDrop(View paramView) {
    if (Build.VERSION.SDK_INT >= 24)
      Api24Impl.cancelDragAndDrop(paramView); 
  }
  
  @Deprecated
  public static int combineMeasuredStates(int paramInt1, int paramInt2) {
    return View.combineMeasuredStates(paramInt1, paramInt2);
  }
  
  private static void compatOffsetLeftAndRight(View paramView, int paramInt) {
    paramView.offsetLeftAndRight(paramInt);
    if (paramView.getVisibility() == 0) {
      tickleInvalidationFlag(paramView);
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View)
        tickleInvalidationFlag((View)viewParent); 
    } 
  }
  
  private static void compatOffsetTopAndBottom(View paramView, int paramInt) {
    paramView.offsetTopAndBottom(paramInt);
    if (paramView.getVisibility() == 0) {
      tickleInvalidationFlag(paramView);
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View)
        tickleInvalidationFlag((View)viewParent); 
    } 
  }
  
  public static WindowInsetsCompat computeSystemWindowInsets(View paramView, WindowInsetsCompat paramWindowInsetsCompat, Rect paramRect) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.computeSystemWindowInsets(paramView, paramWindowInsetsCompat, paramRect) : paramWindowInsetsCompat;
  }
  
  public static WindowInsetsCompat dispatchApplyWindowInsets(View paramView, WindowInsetsCompat paramWindowInsetsCompat) {
    if (Build.VERSION.SDK_INT >= 21) {
      WindowInsets windowInsets = paramWindowInsetsCompat.toWindowInsets();
      if (windowInsets != null) {
        WindowInsets windowInsets1 = Api20Impl.dispatchApplyWindowInsets(paramView, windowInsets);
        if (!windowInsets1.equals(windowInsets))
          return WindowInsetsCompat.toWindowInsetsCompat(windowInsets1, paramView); 
      } 
    } 
    return paramWindowInsetsCompat;
  }
  
  public static void dispatchFinishTemporaryDetach(View paramView) {
    if (Build.VERSION.SDK_INT >= 24) {
      Api24Impl.dispatchFinishTemporaryDetach(paramView);
    } else {
      if (!sTempDetachBound)
        bindTempDetach(); 
      Method method = sDispatchFinishTemporaryDetach;
      if (method != null) {
        try {
          method.invoke(paramView, new Object[0]);
        } catch (Exception exception) {
          Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", exception);
        } 
      } else {
        exception.onFinishTemporaryDetach();
      } 
    } 
  }
  
  public static boolean dispatchNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.dispatchNestedFling(paramView, paramFloat1, paramFloat2, paramBoolean) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean) : false);
  }
  
  public static boolean dispatchNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.dispatchNestedPreFling(paramView, paramFloat1, paramFloat2) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedPreFling(paramFloat1, paramFloat2) : false);
  }
  
  public static boolean dispatchNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.dispatchNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2) : false);
  }
  
  public static boolean dispatchNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3) {
    return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2, paramInt3) : ((paramInt3 == 0) ? dispatchNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2) : false);
  }
  
  public static void dispatchNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int paramInt5, int[] paramArrayOfint2) {
    if (paramView instanceof NestedScrollingChild3) {
      ((NestedScrollingChild3)paramView).dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint1, paramInt5, paramArrayOfint2);
    } else {
      dispatchNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint1, paramInt5);
    } 
  }
  
  public static boolean dispatchNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.dispatchNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint) : false);
  }
  
  public static boolean dispatchNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5) {
    return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramInt5) : ((paramInt5 == 0) ? dispatchNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint) : false);
  }
  
  public static void dispatchStartTemporaryDetach(View paramView) {
    if (Build.VERSION.SDK_INT >= 24) {
      Api24Impl.dispatchStartTemporaryDetach(paramView);
    } else {
      if (!sTempDetachBound)
        bindTempDetach(); 
      Method method = sDispatchStartTemporaryDetach;
      if (method != null) {
        try {
          method.invoke(paramView, new Object[0]);
        } catch (Exception exception) {
          Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", exception);
        } 
      } else {
        exception.onStartTemporaryDetach();
      } 
    } 
  }
  
  static boolean dispatchUnhandledKeyEventBeforeCallback(View paramView, KeyEvent paramKeyEvent) {
    return (Build.VERSION.SDK_INT >= 28) ? false : UnhandledKeyEventManager.at(paramView).dispatch(paramView, paramKeyEvent);
  }
  
  static boolean dispatchUnhandledKeyEventBeforeHierarchy(View paramView, KeyEvent paramKeyEvent) {
    return (Build.VERSION.SDK_INT >= 28) ? false : UnhandledKeyEventManager.at(paramView).preDispatch(paramKeyEvent);
  }
  
  public static void enableAccessibleClickableSpanSupport(View paramView) {
    if (Build.VERSION.SDK_INT >= 19)
      ensureAccessibilityDelegateCompat(paramView); 
  }
  
  static void ensureAccessibilityDelegateCompat(View paramView) {
    AccessibilityDelegateCompat accessibilityDelegateCompat2 = getAccessibilityDelegate(paramView);
    AccessibilityDelegateCompat accessibilityDelegateCompat1 = accessibilityDelegateCompat2;
    if (accessibilityDelegateCompat2 == null)
      accessibilityDelegateCompat1 = new AccessibilityDelegateCompat(); 
    setAccessibilityDelegate(paramView, accessibilityDelegateCompat1);
  }
  
  public static int generateViewId() {
    if (Build.VERSION.SDK_INT >= 17)
      return Api17Impl.generateViewId(); 
    while (true) {
      AtomicInteger atomicInteger = sNextGeneratedId;
      int k = atomicInteger.get();
      int j = k + 1;
      int i = j;
      if (j > 16777215)
        i = 1; 
      if (atomicInteger.compareAndSet(k, i))
        return k; 
    } 
  }
  
  public static AccessibilityDelegateCompat getAccessibilityDelegate(View paramView) {
    View.AccessibilityDelegate accessibilityDelegate = getAccessibilityDelegateInternal(paramView);
    return (accessibilityDelegate == null) ? null : ((accessibilityDelegate instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) ? ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter)accessibilityDelegate).mCompat : new AccessibilityDelegateCompat(accessibilityDelegate));
  }
  
  private static View.AccessibilityDelegate getAccessibilityDelegateInternal(View paramView) {
    return (Build.VERSION.SDK_INT >= 29) ? Api29Impl.getAccessibilityDelegate(paramView) : getAccessibilityDelegateThroughReflection(paramView);
  }
  
  private static View.AccessibilityDelegate getAccessibilityDelegateThroughReflection(View paramView) {
    if (sAccessibilityDelegateCheckFailed)
      return null; 
    if (sAccessibilityDelegateField == null)
      try {
        Field field = View.class.getDeclaredField("mAccessibilityDelegate");
        sAccessibilityDelegateField = field;
        field.setAccessible(true);
      } finally {
        paramView = null;
        sAccessibilityDelegateCheckFailed = true;
      }  
    try {
      Object object;
      return (object instanceof View.AccessibilityDelegate) ? (View.AccessibilityDelegate)object : null;
    } finally {
      paramView = null;
      sAccessibilityDelegateCheckFailed = true;
    } 
  }
  
  public static int getAccessibilityLiveRegion(View paramView) {
    return (Build.VERSION.SDK_INT >= 19) ? Api19Impl.getAccessibilityLiveRegion(paramView) : 0;
  }
  
  public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View paramView) {
    if (Build.VERSION.SDK_INT >= 16) {
      AccessibilityNodeProvider accessibilityNodeProvider = Api16Impl.getAccessibilityNodeProvider(paramView);
      if (accessibilityNodeProvider != null)
        return new AccessibilityNodeProviderCompat(accessibilityNodeProvider); 
    } 
    return null;
  }
  
  public static CharSequence getAccessibilityPaneTitle(View paramView) {
    return paneTitleProperty().get(paramView);
  }
  
  private static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View paramView) {
    ArrayList<AccessibilityNodeInfoCompat.AccessibilityActionCompat> arrayList2 = (ArrayList)paramView.getTag(R.id.tag_accessibility_actions);
    ArrayList<AccessibilityNodeInfoCompat.AccessibilityActionCompat> arrayList1 = arrayList2;
    if (arrayList2 == null) {
      arrayList1 = new ArrayList();
      paramView.setTag(R.id.tag_accessibility_actions, arrayList1);
    } 
    return arrayList1;
  }
  
  @Deprecated
  public static float getAlpha(View paramView) {
    return paramView.getAlpha();
  }
  
  private static int getAvailableActionIdFromResources(View paramView, CharSequence paramCharSequence) {
    int i = -1;
    List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> list = getActionList(paramView);
    byte b;
    for (b = 0; b < list.size(); b++) {
      if (TextUtils.equals(paramCharSequence, ((AccessibilityNodeInfoCompat.AccessibilityActionCompat)list.get(b)).getLabel()))
        return ((AccessibilityNodeInfoCompat.AccessibilityActionCompat)list.get(b)).getId(); 
    } 
    b = 0;
    while (true) {
      int[] arrayOfInt = ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
      if (b < arrayOfInt.length && i == -1) {
        int k = arrayOfInt[b];
        int j = 1;
        for (byte b1 = 0; b1 < list.size(); b1++) {
          byte b2;
          if (((AccessibilityNodeInfoCompat.AccessibilityActionCompat)list.get(b1)).getId() != k) {
            b2 = 1;
          } else {
            b2 = 0;
          } 
          j &= b2;
        } 
        if (j != 0)
          i = k; 
        b++;
        continue;
      } 
      break;
    } 
    return i;
  }
  
  public static ColorStateList getBackgroundTintList(View paramView) {
    if (Build.VERSION.SDK_INT >= 21)
      return Api21Impl.getBackgroundTintList(paramView); 
    if (paramView instanceof TintableBackgroundView) {
      ColorStateList colorStateList = ((TintableBackgroundView)paramView).getSupportBackgroundTintList();
    } else {
      paramView = null;
    } 
    return (ColorStateList)paramView;
  }
  
  public static PorterDuff.Mode getBackgroundTintMode(View paramView) {
    if (Build.VERSION.SDK_INT >= 21)
      return Api21Impl.getBackgroundTintMode(paramView); 
    if (paramView instanceof TintableBackgroundView) {
      PorterDuff.Mode mode = ((TintableBackgroundView)paramView).getSupportBackgroundTintMode();
    } else {
      paramView = null;
    } 
    return (PorterDuff.Mode)paramView;
  }
  
  public static Rect getClipBounds(View paramView) {
    return (Build.VERSION.SDK_INT >= 18) ? Api18Impl.getClipBounds(paramView) : null;
  }
  
  public static Display getDisplay(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getDisplay(paramView) : (isAttachedToWindow(paramView) ? ((WindowManager)paramView.getContext().getSystemService("window")).getDefaultDisplay() : null);
  }
  
  public static float getElevation(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getElevation(paramView) : 0.0F;
  }
  
  private static Rect getEmptyTempRect() {
    if (sThreadLocalRect == null)
      sThreadLocalRect = new ThreadLocal<>(); 
    Rect rect2 = sThreadLocalRect.get();
    Rect rect1 = rect2;
    if (rect2 == null) {
      rect1 = new Rect();
      sThreadLocalRect.set(rect1);
    } 
    rect1.setEmpty();
    return rect1;
  }
  
  private static OnReceiveContentViewBehavior getFallback(View paramView) {
    return (paramView instanceof OnReceiveContentViewBehavior) ? (OnReceiveContentViewBehavior)paramView : NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR;
  }
  
  public static boolean getFitsSystemWindows(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.getFitsSystemWindows(paramView) : false;
  }
  
  public static int getImportantForAccessibility(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.getImportantForAccessibility(paramView) : 0;
  }
  
  public static int getImportantForAutofill(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.getImportantForAutofill(paramView) : 0;
  }
  
  public static int getLabelFor(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getLabelFor(paramView) : 0;
  }
  
  @Deprecated
  public static int getLayerType(View paramView) {
    return paramView.getLayerType();
  }
  
  public static int getLayoutDirection(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getLayoutDirection(paramView) : 0;
  }
  
  @Deprecated
  public static Matrix getMatrix(View paramView) {
    return paramView.getMatrix();
  }
  
  @Deprecated
  public static int getMeasuredHeightAndState(View paramView) {
    return paramView.getMeasuredHeightAndState();
  }
  
  @Deprecated
  public static int getMeasuredState(View paramView) {
    return paramView.getMeasuredState();
  }
  
  @Deprecated
  public static int getMeasuredWidthAndState(View paramView) {
    return paramView.getMeasuredWidthAndState();
  }
  
  public static int getMinimumHeight(View paramView) {
    if (Build.VERSION.SDK_INT >= 16)
      return Api16Impl.getMinimumHeight(paramView); 
    if (!sMinHeightFieldFetched) {
      try {
        Field field1 = View.class.getDeclaredField("mMinHeight");
        sMinHeightField = field1;
        field1.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinHeightFieldFetched = true;
    } 
    Field field = sMinHeightField;
    if (field != null)
      try {
        return ((Integer)field.get(paramView)).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  public static int getMinimumWidth(View paramView) {
    if (Build.VERSION.SDK_INT >= 16)
      return Api16Impl.getMinimumWidth(paramView); 
    if (!sMinWidthFieldFetched) {
      try {
        Field field1 = View.class.getDeclaredField("mMinWidth");
        sMinWidthField = field1;
        field1.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinWidthFieldFetched = true;
    } 
    Field field = sMinWidthField;
    if (field != null)
      try {
        return ((Integer)field.get(paramView)).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  public static int getNextClusterForwardId(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.getNextClusterForwardId(paramView) : -1;
  }
  
  public static String[] getOnReceiveContentMimeTypes(View paramView) {
    return (Build.VERSION.SDK_INT >= 31) ? Api31Impl.getReceiveContentMimeTypes(paramView) : (String[])paramView.getTag(R.id.tag_on_receive_content_mime_types);
  }
  
  @Deprecated
  public static int getOverScrollMode(View paramView) {
    return paramView.getOverScrollMode();
  }
  
  public static int getPaddingEnd(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getPaddingEnd(paramView) : paramView.getPaddingRight();
  }
  
  public static int getPaddingStart(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getPaddingStart(paramView) : paramView.getPaddingLeft();
  }
  
  public static ViewParent getParentForAccessibility(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.getParentForAccessibility(paramView) : paramView.getParent();
  }
  
  @Deprecated
  public static float getPivotX(View paramView) {
    return paramView.getPivotX();
  }
  
  @Deprecated
  public static float getPivotY(View paramView) {
    return paramView.getPivotY();
  }
  
  public static WindowInsetsCompat getRootWindowInsets(View paramView) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getRootWindowInsets(paramView) : ((Build.VERSION.SDK_INT >= 21) ? Api21Impl.getRootWindowInsets(paramView) : null);
  }
  
  @Deprecated
  public static float getRotation(View paramView) {
    return paramView.getRotation();
  }
  
  @Deprecated
  public static float getRotationX(View paramView) {
    return paramView.getRotationX();
  }
  
  @Deprecated
  public static float getRotationY(View paramView) {
    return paramView.getRotationY();
  }
  
  @Deprecated
  public static float getScaleX(View paramView) {
    return paramView.getScaleX();
  }
  
  @Deprecated
  public static float getScaleY(View paramView) {
    return paramView.getScaleY();
  }
  
  public static int getScrollIndicators(View paramView) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getScrollIndicators(paramView) : 0;
  }
  
  public static CharSequence getStateDescription(View paramView) {
    return stateDescriptionProperty().get(paramView);
  }
  
  public static List<Rect> getSystemGestureExclusionRects(View paramView) {
    return (Build.VERSION.SDK_INT >= 29) ? Api29Impl.getSystemGestureExclusionRects(paramView) : Collections.emptyList();
  }
  
  public static String getTransitionName(View paramView) {
    if (Build.VERSION.SDK_INT >= 21)
      return Api21Impl.getTransitionName(paramView); 
    WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
    return (weakHashMap == null) ? null : weakHashMap.get(paramView);
  }
  
  @Deprecated
  public static float getTranslationX(View paramView) {
    return paramView.getTranslationX();
  }
  
  @Deprecated
  public static float getTranslationY(View paramView) {
    return paramView.getTranslationY();
  }
  
  public static float getTranslationZ(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getTranslationZ(paramView) : 0.0F;
  }
  
  public static WindowInsetsControllerCompat getWindowInsetsController(View paramView) {
    if (Build.VERSION.SDK_INT >= 30)
      return Api30Impl.getWindowInsetsController(paramView); 
    Context context = paramView.getContext();
    while (true) {
      boolean bool = context instanceof ContextWrapper;
      Context context1 = null;
      if (bool) {
        WindowInsetsControllerCompat windowInsetsControllerCompat;
        if (context instanceof Activity) {
          Window window = ((Activity)context).getWindow();
          context = context1;
          if (window != null)
            windowInsetsControllerCompat = WindowCompat.getInsetsController(window, paramView); 
          return windowInsetsControllerCompat;
        } 
        Context context2 = ((ContextWrapper)windowInsetsControllerCompat).getBaseContext();
        continue;
      } 
      return null;
    } 
  }
  
  @Deprecated
  public static int getWindowSystemUiVisibility(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.getWindowSystemUiVisibility(paramView) : 0;
  }
  
  @Deprecated
  public static float getX(View paramView) {
    return paramView.getX();
  }
  
  @Deprecated
  public static float getY(View paramView) {
    return paramView.getY();
  }
  
  public static float getZ(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getZ(paramView) : 0.0F;
  }
  
  public static boolean hasAccessibilityDelegate(View paramView) {
    boolean bool;
    if (getAccessibilityDelegateInternal(paramView) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean hasExplicitFocusable(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.hasExplicitFocusable(paramView) : paramView.hasFocusable();
  }
  
  public static boolean hasNestedScrollingParent(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.hasNestedScrollingParent(paramView) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).hasNestedScrollingParent() : false);
  }
  
  public static boolean hasNestedScrollingParent(View paramView, int paramInt) {
    if (paramView instanceof NestedScrollingChild2) {
      ((NestedScrollingChild2)paramView).hasNestedScrollingParent(paramInt);
    } else if (paramInt == 0) {
      return hasNestedScrollingParent(paramView);
    } 
    return false;
  }
  
  public static boolean hasOnClickListeners(View paramView) {
    return (Build.VERSION.SDK_INT >= 15) ? Api15Impl.hasOnClickListeners(paramView) : false;
  }
  
  public static boolean hasOverlappingRendering(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.hasOverlappingRendering(paramView) : true;
  }
  
  public static boolean hasTransientState(View paramView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.hasTransientState(paramView) : false;
  }
  
  public static boolean isAccessibilityHeading(View paramView) {
    boolean bool1;
    Boolean bool = accessibilityHeadingProperty().get(paramView);
    if (bool != null && bool.booleanValue()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  public static boolean isAttachedToWindow(View paramView) {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19)
      return Api19Impl.isAttachedToWindow(paramView); 
    if (paramView.getWindowToken() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isFocusedByDefault(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.isFocusedByDefault(paramView) : false;
  }
  
  public static boolean isImportantForAccessibility(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.isImportantForAccessibility(paramView) : true;
  }
  
  public static boolean isImportantForAutofill(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.isImportantForAutofill(paramView) : true;
  }
  
  public static boolean isInLayout(View paramView) {
    return (Build.VERSION.SDK_INT >= 18) ? Api18Impl.isInLayout(paramView) : false;
  }
  
  public static boolean isKeyboardNavigationCluster(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.isKeyboardNavigationCluster(paramView) : false;
  }
  
  public static boolean isLaidOut(View paramView) {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19)
      return Api19Impl.isLaidOut(paramView); 
    if (paramView.getWidth() > 0 && paramView.getHeight() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isLayoutDirectionResolved(View paramView) {
    return (Build.VERSION.SDK_INT >= 19) ? Api19Impl.isLayoutDirectionResolved(paramView) : false;
  }
  
  public static boolean isNestedScrollingEnabled(View paramView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.isNestedScrollingEnabled(paramView) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).isNestedScrollingEnabled() : false);
  }
  
  @Deprecated
  public static boolean isOpaque(View paramView) {
    return paramView.isOpaque();
  }
  
  public static boolean isPaddingRelative(View paramView) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.isPaddingRelative(paramView) : false;
  }
  
  public static boolean isScreenReaderFocusable(View paramView) {
    boolean bool1;
    Boolean bool = screenReaderFocusableProperty().get(paramView);
    if (bool != null && bool.booleanValue()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  @Deprecated
  public static void jumpDrawablesToCurrentState(View paramView) {
    paramView.jumpDrawablesToCurrentState();
  }
  
  public static View keyboardNavigationClusterSearch(View paramView1, View paramView2, int paramInt) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.keyboardNavigationClusterSearch(paramView1, paramView2, paramInt) : null;
  }
  
  static void notifyViewAccessibilityStateChangedIfNeeded(View paramView, int paramInt) {
    boolean bool;
    AccessibilityEvent accessibilityEvent;
    AccessibilityManager accessibilityManager = (AccessibilityManager)paramView.getContext().getSystemService("accessibility");
    if (!accessibilityManager.isEnabled())
      return; 
    if (getAccessibilityPaneTitle(paramView) != null && paramView.getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = getAccessibilityLiveRegion(paramView);
    char c = ' ';
    if (i != 0 || bool) {
      accessibilityEvent = AccessibilityEvent.obtain();
      if (!bool)
        c = 'ࠀ'; 
      accessibilityEvent.setEventType(c);
      Api19Impl.setContentChangeTypes(accessibilityEvent, paramInt);
      if (bool) {
        accessibilityEvent.getText().add(getAccessibilityPaneTitle(paramView));
        setViewImportanceForAccessibilityIfNeeded(paramView);
      } 
      paramView.sendAccessibilityEventUnchecked(accessibilityEvent);
      return;
    } 
    if (paramInt == 32) {
      AccessibilityEvent accessibilityEvent1 = AccessibilityEvent.obtain();
      paramView.onInitializeAccessibilityEvent(accessibilityEvent1);
      accessibilityEvent1.setEventType(32);
      Api19Impl.setContentChangeTypes(accessibilityEvent1, paramInt);
      accessibilityEvent1.setSource(paramView);
      paramView.onPopulateAccessibilityEvent(accessibilityEvent1);
      accessibilityEvent1.getText().add(getAccessibilityPaneTitle(paramView));
      accessibilityEvent.sendAccessibilityEvent(accessibilityEvent1);
    } else if (paramView.getParent() != null) {
      ViewParent viewParent = paramView.getParent();
      try {
        Api19Impl.notifySubtreeAccessibilityStateChanged(viewParent, paramView, paramView, paramInt);
      } catch (AbstractMethodError abstractMethodError) {
        Log.e("ViewCompat", paramView.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", abstractMethodError);
      } 
    } 
  }
  
  public static void offsetLeftAndRight(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramView.offsetLeftAndRight(paramInt);
    } else if (Build.VERSION.SDK_INT >= 21) {
      Rect rect = getEmptyTempRect();
      int i = 0;
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View) {
        View view = (View)viewParent;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        i = rect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()) ^ true;
      } 
      compatOffsetLeftAndRight(paramView, paramInt);
      if (i != 0 && rect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))
        ((View)viewParent).invalidate(rect); 
    } else {
      compatOffsetLeftAndRight(paramView, paramInt);
    } 
  }
  
  public static void offsetTopAndBottom(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramView.offsetTopAndBottom(paramInt);
    } else if (Build.VERSION.SDK_INT >= 21) {
      Rect rect = getEmptyTempRect();
      int i = 0;
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View) {
        View view = (View)viewParent;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        i = rect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()) ^ true;
      } 
      compatOffsetTopAndBottom(paramView, paramInt);
      if (i != 0 && rect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))
        ((View)viewParent).invalidate(rect); 
    } else {
      compatOffsetTopAndBottom(paramView, paramInt);
    } 
  }
  
  public static WindowInsetsCompat onApplyWindowInsets(View paramView, WindowInsetsCompat paramWindowInsetsCompat) {
    if (Build.VERSION.SDK_INT >= 21) {
      WindowInsets windowInsets = paramWindowInsetsCompat.toWindowInsets();
      if (windowInsets != null) {
        WindowInsets windowInsets1 = Api20Impl.onApplyWindowInsets(paramView, windowInsets);
        if (!windowInsets1.equals(windowInsets))
          return WindowInsetsCompat.toWindowInsetsCompat(windowInsets1, paramView); 
      } 
    } 
    return paramWindowInsetsCompat;
  }
  
  @Deprecated
  public static void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) {
    paramView.onInitializeAccessibilityEvent(paramAccessibilityEvent);
  }
  
  public static void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    paramView.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfoCompat.unwrap());
  }
  
  @Deprecated
  public static void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) {
    paramView.onPopulateAccessibilityEvent(paramAccessibilityEvent);
  }
  
  private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
    return new AccessibilityViewProperty<CharSequence>(R.id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
        CharSequence frameworkGet(View param1View) {
          return ViewCompat.Api28Impl.getAccessibilityPaneTitle(param1View);
        }
        
        void frameworkSet(View param1View, CharSequence param1CharSequence) {
          ViewCompat.Api28Impl.setAccessibilityPaneTitle(param1View, param1CharSequence);
        }
        
        boolean shouldUpdate(CharSequence param1CharSequence1, CharSequence param1CharSequence2) {
          return TextUtils.equals(param1CharSequence1, param1CharSequence2) ^ true;
        }
      };
  }
  
  public static boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.performAccessibilityAction(paramView, paramInt, paramBundle) : false;
  }
  
  public static ContentInfoCompat performReceiveContent(View paramView, ContentInfoCompat paramContentInfoCompat) {
    ContentInfoCompat contentInfoCompat;
    if (Log.isLoggable("ViewCompat", 3))
      Log.d("ViewCompat", "performReceiveContent: " + paramContentInfoCompat + ", view=" + paramView.getClass().getSimpleName() + "[" + paramView.getId() + "]"); 
    if (Build.VERSION.SDK_INT >= 31)
      return Api31Impl.performReceiveContent(paramView, paramContentInfoCompat); 
    OnReceiveContentListener onReceiveContentListener = (OnReceiveContentListener)paramView.getTag(R.id.tag_on_receive_content_listener);
    if (onReceiveContentListener != null) {
      paramContentInfoCompat = onReceiveContentListener.onReceiveContent(paramView, paramContentInfoCompat);
      if (paramContentInfoCompat == null) {
        paramView = null;
      } else {
        contentInfoCompat = getFallback(paramView).onReceiveContent(paramContentInfoCompat);
      } 
      return contentInfoCompat;
    } 
    return getFallback((View)contentInfoCompat).onReceiveContent(paramContentInfoCompat);
  }
  
  public static void postInvalidateOnAnimation(View paramView) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.postInvalidateOnAnimation(paramView);
    } else {
      paramView.postInvalidate();
    } 
  }
  
  public static void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.postInvalidateOnAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      paramView.postInvalidate(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  public static void postOnAnimation(View paramView, Runnable paramRunnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.postOnAnimation(paramView, paramRunnable);
    } else {
      paramView.postDelayed(paramRunnable, ValueAnimator.getFrameDelay());
    } 
  }
  
  public static void postOnAnimationDelayed(View paramView, Runnable paramRunnable, long paramLong) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.postOnAnimationDelayed(paramView, paramRunnable, paramLong);
    } else {
      paramView.postDelayed(paramRunnable, ValueAnimator.getFrameDelay() + paramLong);
    } 
  }
  
  public static void removeAccessibilityAction(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 21) {
      removeActionWithId(paramInt, paramView);
      notifyViewAccessibilityStateChangedIfNeeded(paramView, 0);
    } 
  }
  
  private static void removeActionWithId(int paramInt, View paramView) {
    List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> list = getActionList(paramView);
    for (byte b = 0; b < list.size(); b++) {
      if (((AccessibilityNodeInfoCompat.AccessibilityActionCompat)list.get(b)).getId() == paramInt) {
        list.remove(b);
        break;
      } 
    } 
  }
  
  public static void removeOnUnhandledKeyEventListener(View paramView, OnUnhandledKeyEventListenerCompat paramOnUnhandledKeyEventListenerCompat) {
    if (Build.VERSION.SDK_INT >= 28) {
      Api28Impl.removeOnUnhandledKeyEventListener(paramView, paramOnUnhandledKeyEventListenerCompat);
      return;
    } 
    ArrayList arrayList = (ArrayList)paramView.getTag(R.id.tag_unhandled_key_listeners);
    if (arrayList != null) {
      arrayList.remove(paramOnUnhandledKeyEventListenerCompat);
      if (arrayList.size() == 0)
        UnhandledKeyEventManager.unregisterListeningView(paramView); 
    } 
  }
  
  public static void replaceAccessibilityAction(View paramView, AccessibilityNodeInfoCompat.AccessibilityActionCompat paramAccessibilityActionCompat, CharSequence paramCharSequence, AccessibilityViewCommand paramAccessibilityViewCommand) {
    if (paramAccessibilityViewCommand == null && paramCharSequence == null) {
      removeAccessibilityAction(paramView, paramAccessibilityActionCompat.getId());
    } else {
      addAccessibilityAction(paramView, paramAccessibilityActionCompat.createReplacementAction(paramCharSequence, paramAccessibilityViewCommand));
    } 
  }
  
  public static void requestApplyInsets(View paramView) {
    if (Build.VERSION.SDK_INT >= 20) {
      Api20Impl.requestApplyInsets(paramView);
    } else if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.requestFitSystemWindows(paramView);
    } 
  }
  
  public static <T extends View> T requireViewById(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 28)
      return (T)Api28Impl.<View>requireViewById(paramView, paramInt); 
    paramView = paramView.findViewById(paramInt);
    if (paramView != null)
      return (T)paramView; 
    throw new IllegalArgumentException("ID does not reference a View inside this View");
  }
  
  @Deprecated
  public static int resolveSizeAndState(int paramInt1, int paramInt2, int paramInt3) {
    return View.resolveSizeAndState(paramInt1, paramInt2, paramInt3);
  }
  
  public static boolean restoreDefaultFocus(View paramView) {
    return (Build.VERSION.SDK_INT >= 26) ? Api26Impl.restoreDefaultFocus(paramView) : paramView.requestFocus();
  }
  
  public static void saveAttributeDataForStyleable(View paramView, Context paramContext, int[] paramArrayOfint, AttributeSet paramAttributeSet, TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    if (Build.VERSION.SDK_INT >= 29)
      Api29Impl.saveAttributeDataForStyleable(paramView, paramContext, paramArrayOfint, paramAttributeSet, paramTypedArray, paramInt1, paramInt2); 
  }
  
  private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
    return new AccessibilityViewProperty<Boolean>(R.id.tag_screen_reader_focusable, Boolean.class, 28) {
        Boolean frameworkGet(View param1View) {
          return Boolean.valueOf(ViewCompat.Api28Impl.isScreenReaderFocusable(param1View));
        }
        
        void frameworkSet(View param1View, Boolean param1Boolean) {
          ViewCompat.Api28Impl.setScreenReaderFocusable(param1View, param1Boolean.booleanValue());
        }
        
        boolean shouldUpdate(Boolean param1Boolean1, Boolean param1Boolean2) {
          return booleanNullToFalseEquals(param1Boolean1, param1Boolean2) ^ true;
        }
      };
  }
  
  public static void setAccessibilityDelegate(View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat) {
    View.AccessibilityDelegate accessibilityDelegate;
    AccessibilityDelegateCompat accessibilityDelegateCompat = paramAccessibilityDelegateCompat;
    if (paramAccessibilityDelegateCompat == null) {
      accessibilityDelegateCompat = paramAccessibilityDelegateCompat;
      if (getAccessibilityDelegateInternal(paramView) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter)
        accessibilityDelegateCompat = new AccessibilityDelegateCompat(); 
    } 
    if (accessibilityDelegateCompat == null) {
      paramAccessibilityDelegateCompat = null;
    } else {
      accessibilityDelegate = accessibilityDelegateCompat.getBridge();
    } 
    paramView.setAccessibilityDelegate(accessibilityDelegate);
  }
  
  public static void setAccessibilityHeading(View paramView, boolean paramBoolean) {
    accessibilityHeadingProperty().set(paramView, Boolean.valueOf(paramBoolean));
  }
  
  public static void setAccessibilityLiveRegion(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19)
      Api19Impl.setAccessibilityLiveRegion(paramView, paramInt); 
  }
  
  public static void setAccessibilityPaneTitle(View paramView, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 19) {
      paneTitleProperty().set(paramView, paramCharSequence);
      if (paramCharSequence != null) {
        sAccessibilityPaneVisibilityManager.addAccessibilityPane(paramView);
      } else {
        sAccessibilityPaneVisibilityManager.removeAccessibilityPane(paramView);
      } 
    } 
  }
  
  @Deprecated
  public static void setActivated(View paramView, boolean paramBoolean) {
    paramView.setActivated(paramBoolean);
  }
  
  @Deprecated
  public static void setAlpha(View paramView, float paramFloat) {
    paramView.setAlpha(paramFloat);
  }
  
  public static void setAutofillHints(View paramView, String... paramVarArgs) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setAutofillHints(paramView, paramVarArgs); 
  }
  
  public static void setBackground(View paramView, Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.setBackground(paramView, paramDrawable);
    } else {
      paramView.setBackgroundDrawable(paramDrawable);
    } 
  }
  
  public static void setBackgroundTintList(View paramView, ColorStateList paramColorStateList) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setBackgroundTintList(paramView, paramColorStateList);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramView.getBackground();
        if (Api21Impl.getBackgroundTintList(paramView) != null || Api21Impl.getBackgroundTintMode(paramView) != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramView.getDrawableState()); 
          Api16Impl.setBackground(paramView, drawable);
        } 
      } 
    } else if (paramView instanceof TintableBackgroundView) {
      ((TintableBackgroundView)paramView).setSupportBackgroundTintList((ColorStateList)drawable);
    } 
  }
  
  public static void setBackgroundTintMode(View paramView, PorterDuff.Mode paramMode) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setBackgroundTintMode(paramView, paramMode);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramView.getBackground();
        if (Api21Impl.getBackgroundTintList(paramView) != null || Api21Impl.getBackgroundTintMode(paramView) != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramView.getDrawableState()); 
          Api16Impl.setBackground(paramView, drawable);
        } 
      } 
    } else if (paramView instanceof TintableBackgroundView) {
      ((TintableBackgroundView)paramView).setSupportBackgroundTintMode((PorterDuff.Mode)drawable);
    } 
  }
  
  @Deprecated
  public static void setChildrenDrawingOrderEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (sChildrenDrawingOrderMethod == null) {
      try {
        sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[] { boolean.class });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", noSuchMethodException);
      } 
      sChildrenDrawingOrderMethod.setAccessible(true);
    } 
    try {
      sChildrenDrawingOrderMethod.invoke(paramViewGroup, new Object[] { Boolean.valueOf(paramBoolean) });
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", illegalAccessException);
    } catch (IllegalArgumentException illegalArgumentException) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", illegalArgumentException);
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", invocationTargetException);
    } 
  }
  
  public static void setClipBounds(View paramView, Rect paramRect) {
    if (Build.VERSION.SDK_INT >= 18)
      Api18Impl.setClipBounds(paramView, paramRect); 
  }
  
  public static void setElevation(View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      Api21Impl.setElevation(paramView, paramFloat); 
  }
  
  @Deprecated
  public static void setFitsSystemWindows(View paramView, boolean paramBoolean) {
    paramView.setFitsSystemWindows(paramBoolean);
  }
  
  public static void setFocusedByDefault(View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setFocusedByDefault(paramView, paramBoolean); 
  }
  
  public static void setHasTransientState(View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 16)
      Api16Impl.setHasTransientState(paramView, paramBoolean); 
  }
  
  public static void setImportantForAccessibility(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19) {
      Api16Impl.setImportantForAccessibility(paramView, paramInt);
    } else if (Build.VERSION.SDK_INT >= 16) {
      int i = paramInt;
      if (paramInt == 4)
        i = 2; 
      Api16Impl.setImportantForAccessibility(paramView, i);
    } 
  }
  
  public static void setImportantForAutofill(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setImportantForAutofill(paramView, paramInt); 
  }
  
  public static void setKeyboardNavigationCluster(View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setKeyboardNavigationCluster(paramView, paramBoolean); 
  }
  
  public static void setLabelFor(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      Api17Impl.setLabelFor(paramView, paramInt); 
  }
  
  public static void setLayerPaint(View paramView, Paint paramPaint) {
    if (Build.VERSION.SDK_INT >= 17) {
      Api17Impl.setLayerPaint(paramView, paramPaint);
    } else {
      paramView.setLayerType(paramView.getLayerType(), paramPaint);
      paramView.invalidate();
    } 
  }
  
  @Deprecated
  public static void setLayerType(View paramView, int paramInt, Paint paramPaint) {
    paramView.setLayerType(paramInt, paramPaint);
  }
  
  public static void setLayoutDirection(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      Api17Impl.setLayoutDirection(paramView, paramInt); 
  }
  
  public static void setNestedScrollingEnabled(View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setNestedScrollingEnabled(paramView, paramBoolean);
    } else if (paramView instanceof NestedScrollingChild) {
      ((NestedScrollingChild)paramView).setNestedScrollingEnabled(paramBoolean);
    } 
  }
  
  public static void setNextClusterForwardId(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setNextClusterForwardId(paramView, paramInt); 
  }
  
  public static void setOnApplyWindowInsetsListener(View paramView, OnApplyWindowInsetsListener paramOnApplyWindowInsetsListener) {
    if (Build.VERSION.SDK_INT >= 21)
      Api21Impl.setOnApplyWindowInsetsListener(paramView, paramOnApplyWindowInsetsListener); 
  }
  
  public static void setOnReceiveContentListener(View paramView, String[] paramArrayOfString, OnReceiveContentListener paramOnReceiveContentListener) {
    if (Build.VERSION.SDK_INT >= 31) {
      Api31Impl.setOnReceiveContentListener(paramView, paramArrayOfString, paramOnReceiveContentListener);
      return;
    } 
    if (paramArrayOfString == null || paramArrayOfString.length == 0)
      paramArrayOfString = null; 
    byte b = 0;
    if (paramOnReceiveContentListener != null) {
      boolean bool;
      if (paramArrayOfString != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "When the listener is set, MIME types must also be set");
    } 
    if (paramArrayOfString != null) {
      boolean bool1;
      boolean bool2 = false;
      int i = paramArrayOfString.length;
      while (true) {
        bool1 = bool2;
        if (b < i) {
          if (paramArrayOfString[b].startsWith("*")) {
            bool1 = true;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      Preconditions.checkArgument(bool1 ^ true, "A MIME type set here must not start with *: " + Arrays.toString((Object[])paramArrayOfString));
    } 
    paramView.setTag(R.id.tag_on_receive_content_mime_types, paramArrayOfString);
    paramView.setTag(R.id.tag_on_receive_content_listener, paramOnReceiveContentListener);
  }
  
  @Deprecated
  public static void setOverScrollMode(View paramView, int paramInt) {
    paramView.setOverScrollMode(paramInt);
  }
  
  public static void setPaddingRelative(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (Build.VERSION.SDK_INT >= 17) {
      Api17Impl.setPaddingRelative(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      paramView.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  @Deprecated
  public static void setPivotX(View paramView, float paramFloat) {
    paramView.setPivotX(paramFloat);
  }
  
  @Deprecated
  public static void setPivotY(View paramView, float paramFloat) {
    paramView.setPivotY(paramFloat);
  }
  
  public static void setPointerIcon(View paramView, PointerIconCompat paramPointerIconCompat) {
    if (Build.VERSION.SDK_INT >= 24) {
      if (paramPointerIconCompat != null) {
        Object object = paramPointerIconCompat.getPointerIcon();
      } else {
        paramPointerIconCompat = null;
      } 
      Api24Impl.setPointerIcon(paramView, (PointerIcon)paramPointerIconCompat);
    } 
  }
  
  @Deprecated
  public static void setRotation(View paramView, float paramFloat) {
    paramView.setRotation(paramFloat);
  }
  
  @Deprecated
  public static void setRotationX(View paramView, float paramFloat) {
    paramView.setRotationX(paramFloat);
  }
  
  @Deprecated
  public static void setRotationY(View paramView, float paramFloat) {
    paramView.setRotationY(paramFloat);
  }
  
  @Deprecated
  public static void setSaveFromParentEnabled(View paramView, boolean paramBoolean) {
    paramView.setSaveFromParentEnabled(paramBoolean);
  }
  
  @Deprecated
  public static void setScaleX(View paramView, float paramFloat) {
    paramView.setScaleX(paramFloat);
  }
  
  @Deprecated
  public static void setScaleY(View paramView, float paramFloat) {
    paramView.setScaleY(paramFloat);
  }
  
  public static void setScreenReaderFocusable(View paramView, boolean paramBoolean) {
    screenReaderFocusableProperty().set(paramView, Boolean.valueOf(paramBoolean));
  }
  
  public static void setScrollIndicators(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23)
      Api23Impl.setScrollIndicators(paramView, paramInt); 
  }
  
  public static void setScrollIndicators(View paramView, int paramInt1, int paramInt2) {
    if (Build.VERSION.SDK_INT >= 23)
      Api23Impl.setScrollIndicators(paramView, paramInt1, paramInt2); 
  }
  
  public static void setStateDescription(View paramView, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 19)
      stateDescriptionProperty().set(paramView, paramCharSequence); 
  }
  
  public static void setSystemGestureExclusionRects(View paramView, List<Rect> paramList) {
    if (Build.VERSION.SDK_INT >= 29)
      Api29Impl.setSystemGestureExclusionRects(paramView, paramList); 
  }
  
  public static void setTooltipText(View paramView, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.setTooltipText(paramView, paramCharSequence); 
  }
  
  public static void setTransitionName(View paramView, String paramString) {
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setTransitionName(paramView, paramString);
    } else {
      if (sTransitionNameMap == null)
        sTransitionNameMap = new WeakHashMap<>(); 
      sTransitionNameMap.put(paramView, paramString);
    } 
  }
  
  @Deprecated
  public static void setTranslationX(View paramView, float paramFloat) {
    paramView.setTranslationX(paramFloat);
  }
  
  @Deprecated
  public static void setTranslationY(View paramView, float paramFloat) {
    paramView.setTranslationY(paramFloat);
  }
  
  public static void setTranslationZ(View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      Api21Impl.setTranslationZ(paramView, paramFloat); 
  }
  
  private static void setViewImportanceForAccessibilityIfNeeded(View paramView) {
    if (getImportantForAccessibility(paramView) == 0)
      setImportantForAccessibility(paramView, 1); 
    for (ViewParent viewParent = paramView.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
      if (getImportantForAccessibility((View)viewParent) == 4) {
        setImportantForAccessibility(paramView, 2);
        break;
      } 
    } 
  }
  
  public static void setWindowInsetsAnimationCallback(View paramView, WindowInsetsAnimationCompat.Callback paramCallback) {
    WindowInsetsAnimationCompat.setCallback(paramView, paramCallback);
  }
  
  @Deprecated
  public static void setX(View paramView, float paramFloat) {
    paramView.setX(paramFloat);
  }
  
  @Deprecated
  public static void setY(View paramView, float paramFloat) {
    paramView.setY(paramFloat);
  }
  
  public static void setZ(View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      Api21Impl.setZ(paramView, paramFloat); 
  }
  
  public static boolean startDragAndDrop(View paramView, ClipData paramClipData, View.DragShadowBuilder paramDragShadowBuilder, Object paramObject, int paramInt) {
    return (Build.VERSION.SDK_INT >= 24) ? Api24Impl.startDragAndDrop(paramView, paramClipData, paramDragShadowBuilder, paramObject, paramInt) : paramView.startDrag(paramClipData, paramDragShadowBuilder, paramObject, paramInt);
  }
  
  public static boolean startNestedScroll(View paramView, int paramInt) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.startNestedScroll(paramView, paramInt) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).startNestedScroll(paramInt) : false);
  }
  
  public static boolean startNestedScroll(View paramView, int paramInt1, int paramInt2) {
    return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).startNestedScroll(paramInt1, paramInt2) : ((paramInt2 == 0) ? startNestedScroll(paramView, paramInt1) : false);
  }
  
  private static AccessibilityViewProperty<CharSequence> stateDescriptionProperty() {
    return new AccessibilityViewProperty<CharSequence>(R.id.tag_state_description, CharSequence.class, 64, 30) {
        CharSequence frameworkGet(View param1View) {
          return ViewCompat.Api30Impl.getStateDescription(param1View);
        }
        
        void frameworkSet(View param1View, CharSequence param1CharSequence) {
          ViewCompat.Api30Impl.setStateDescription(param1View, param1CharSequence);
        }
        
        boolean shouldUpdate(CharSequence param1CharSequence1, CharSequence param1CharSequence2) {
          return TextUtils.equals(param1CharSequence1, param1CharSequence2) ^ true;
        }
      };
  }
  
  public static void stopNestedScroll(View paramView) {
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.stopNestedScroll(paramView);
    } else if (paramView instanceof NestedScrollingChild) {
      ((NestedScrollingChild)paramView).stopNestedScroll();
    } 
  }
  
  public static void stopNestedScroll(View paramView, int paramInt) {
    if (paramView instanceof NestedScrollingChild2) {
      ((NestedScrollingChild2)paramView).stopNestedScroll(paramInt);
    } else if (paramInt == 0) {
      stopNestedScroll(paramView);
    } 
  }
  
  private static void tickleInvalidationFlag(View paramView) {
    float f = paramView.getTranslationY();
    paramView.setTranslationY(1.0F + f);
    paramView.setTranslationY(f);
  }
  
  public static void updateDragShadow(View paramView, View.DragShadowBuilder paramDragShadowBuilder) {
    if (Build.VERSION.SDK_INT >= 24)
      Api24Impl.updateDragShadow(paramView, paramDragShadowBuilder); 
  }
  
  static class AccessibilityPaneVisibilityManager implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {
    private final WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap<>();
    
    private void checkPaneVisibility(View param1View, boolean param1Boolean) {
      boolean bool;
      if (param1View.getVisibility() == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      if (param1Boolean != bool) {
        byte b;
        if (bool) {
          b = 16;
        } else {
          b = 32;
        } 
        ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(param1View, b);
        this.mPanesToVisible.put(param1View, Boolean.valueOf(bool));
      } 
    }
    
    private void registerForLayoutCallback(View param1View) {
      param1View.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    
    private void unregisterForLayoutCallback(View param1View) {
      ViewCompat.Api16Impl.removeOnGlobalLayoutListener(param1View.getViewTreeObserver(), this);
    }
    
    void addAccessibilityPane(View param1View) {
      boolean bool;
      WeakHashMap<View, Boolean> weakHashMap = this.mPanesToVisible;
      if (param1View.getVisibility() == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      weakHashMap.put(param1View, Boolean.valueOf(bool));
      param1View.addOnAttachStateChangeListener(this);
      if (ViewCompat.Api19Impl.isAttachedToWindow(param1View))
        registerForLayoutCallback(param1View); 
    }
    
    public void onGlobalLayout() {
      if (Build.VERSION.SDK_INT < 28)
        for (Map.Entry<View, Boolean> entry : this.mPanesToVisible.entrySet())
          checkPaneVisibility((View)entry.getKey(), ((Boolean)entry.getValue()).booleanValue());  
    }
    
    public void onViewAttachedToWindow(View param1View) {
      registerForLayoutCallback(param1View);
    }
    
    public void onViewDetachedFromWindow(View param1View) {}
    
    void removeAccessibilityPane(View param1View) {
      this.mPanesToVisible.remove(param1View);
      param1View.removeOnAttachStateChangeListener(this);
      unregisterForLayoutCallback(param1View);
    }
  }
  
  static abstract class AccessibilityViewProperty<T> {
    private final int mContentChangeType;
    
    private final int mFrameworkMinimumSdk;
    
    private final int mTagKey;
    
    private final Class<T> mType;
    
    AccessibilityViewProperty(int param1Int1, Class<T> param1Class, int param1Int2) {
      this(param1Int1, param1Class, 0, param1Int2);
    }
    
    AccessibilityViewProperty(int param1Int1, Class<T> param1Class, int param1Int2, int param1Int3) {
      this.mTagKey = param1Int1;
      this.mType = param1Class;
      this.mContentChangeType = param1Int2;
      this.mFrameworkMinimumSdk = param1Int3;
    }
    
    private boolean extrasAvailable() {
      boolean bool;
      if (Build.VERSION.SDK_INT >= 19) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private boolean frameworkAvailable() {
      boolean bool;
      if (Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean booleanNullToFalseEquals(Boolean param1Boolean1, Boolean param1Boolean2) {
      boolean bool1;
      boolean bool2;
      boolean bool3 = true;
      if (param1Boolean1 != null && param1Boolean1.booleanValue()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (param1Boolean2 != null && param1Boolean2.booleanValue()) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (bool1 != bool2)
        bool3 = false; 
      return bool3;
    }
    
    abstract T frameworkGet(View param1View);
    
    abstract void frameworkSet(View param1View, T param1T);
    
    T get(View param1View) {
      if (frameworkAvailable())
        return frameworkGet(param1View); 
      if (extrasAvailable()) {
        Object object = param1View.getTag(this.mTagKey);
        if (this.mType.isInstance(object))
          return (T)object; 
      } 
      return null;
    }
    
    void set(View param1View, T param1T) {
      if (frameworkAvailable()) {
        frameworkSet(param1View, param1T);
      } else if (extrasAvailable() && shouldUpdate(get(param1View), param1T)) {
        ViewCompat.ensureAccessibilityDelegateCompat(param1View);
        param1View.setTag(this.mTagKey, param1T);
        ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(param1View, this.mContentChangeType);
      } 
    }
    
    boolean shouldUpdate(T param1T1, T param1T2) {
      return param1T2.equals(param1T1) ^ true;
    }
  }
  
  static class Api15Impl {
    static boolean hasOnClickListeners(View param1View) {
      return param1View.hasOnClickListeners();
    }
  }
  
  static class Api16Impl {
    static AccessibilityNodeProvider getAccessibilityNodeProvider(View param1View) {
      return param1View.getAccessibilityNodeProvider();
    }
    
    static boolean getFitsSystemWindows(View param1View) {
      return param1View.getFitsSystemWindows();
    }
    
    static int getImportantForAccessibility(View param1View) {
      return param1View.getImportantForAccessibility();
    }
    
    static int getMinimumHeight(View param1View) {
      return param1View.getMinimumHeight();
    }
    
    static int getMinimumWidth(View param1View) {
      return param1View.getMinimumWidth();
    }
    
    static ViewParent getParentForAccessibility(View param1View) {
      return param1View.getParentForAccessibility();
    }
    
    static int getWindowSystemUiVisibility(View param1View) {
      return param1View.getWindowSystemUiVisibility();
    }
    
    static boolean hasOverlappingRendering(View param1View) {
      return param1View.hasOverlappingRendering();
    }
    
    static boolean hasTransientState(View param1View) {
      return param1View.hasTransientState();
    }
    
    static boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      return param1View.performAccessibilityAction(param1Int, param1Bundle);
    }
    
    static void postInvalidateOnAnimation(View param1View) {
      param1View.postInvalidateOnAnimation();
    }
    
    static void postInvalidateOnAnimation(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      param1View.postInvalidateOnAnimation(param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    static void postOnAnimation(View param1View, Runnable param1Runnable) {
      param1View.postOnAnimation(param1Runnable);
    }
    
    static void postOnAnimationDelayed(View param1View, Runnable param1Runnable, long param1Long) {
      param1View.postOnAnimationDelayed(param1Runnable, param1Long);
    }
    
    static void removeOnGlobalLayoutListener(ViewTreeObserver param1ViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener param1OnGlobalLayoutListener) {
      param1ViewTreeObserver.removeOnGlobalLayoutListener(param1OnGlobalLayoutListener);
    }
    
    static void requestFitSystemWindows(View param1View) {
      param1View.requestFitSystemWindows();
    }
    
    static void setBackground(View param1View, Drawable param1Drawable) {
      param1View.setBackground(param1Drawable);
    }
    
    static void setHasTransientState(View param1View, boolean param1Boolean) {
      param1View.setHasTransientState(param1Boolean);
    }
    
    static void setImportantForAccessibility(View param1View, int param1Int) {
      param1View.setImportantForAccessibility(param1Int);
    }
  }
  
  static class Api17Impl {
    static int generateViewId() {
      return View.generateViewId();
    }
    
    static Display getDisplay(View param1View) {
      return param1View.getDisplay();
    }
    
    static int getLabelFor(View param1View) {
      return param1View.getLabelFor();
    }
    
    static int getLayoutDirection(View param1View) {
      return param1View.getLayoutDirection();
    }
    
    static int getPaddingEnd(View param1View) {
      return param1View.getPaddingEnd();
    }
    
    static int getPaddingStart(View param1View) {
      return param1View.getPaddingStart();
    }
    
    static boolean isPaddingRelative(View param1View) {
      return param1View.isPaddingRelative();
    }
    
    static void setLabelFor(View param1View, int param1Int) {
      param1View.setLabelFor(param1Int);
    }
    
    static void setLayerPaint(View param1View, Paint param1Paint) {
      param1View.setLayerPaint(param1Paint);
    }
    
    static void setLayoutDirection(View param1View, int param1Int) {
      param1View.setLayoutDirection(param1Int);
    }
    
    static void setPaddingRelative(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      param1View.setPaddingRelative(param1Int1, param1Int2, param1Int3, param1Int4);
    }
  }
  
  static class Api18Impl {
    static Rect getClipBounds(View param1View) {
      return param1View.getClipBounds();
    }
    
    static boolean isInLayout(View param1View) {
      return param1View.isInLayout();
    }
    
    static void setClipBounds(View param1View, Rect param1Rect) {
      param1View.setClipBounds(param1Rect);
    }
  }
  
  static class Api19Impl {
    static int getAccessibilityLiveRegion(View param1View) {
      return param1View.getAccessibilityLiveRegion();
    }
    
    static boolean isAttachedToWindow(View param1View) {
      return param1View.isAttachedToWindow();
    }
    
    static boolean isLaidOut(View param1View) {
      return param1View.isLaidOut();
    }
    
    static boolean isLayoutDirectionResolved(View param1View) {
      return param1View.isLayoutDirectionResolved();
    }
    
    static void notifySubtreeAccessibilityStateChanged(ViewParent param1ViewParent, View param1View1, View param1View2, int param1Int) {
      param1ViewParent.notifySubtreeAccessibilityStateChanged(param1View1, param1View2, param1Int);
    }
    
    static void setAccessibilityLiveRegion(View param1View, int param1Int) {
      param1View.setAccessibilityLiveRegion(param1Int);
    }
    
    static void setContentChangeTypes(AccessibilityEvent param1AccessibilityEvent, int param1Int) {
      param1AccessibilityEvent.setContentChangeTypes(param1Int);
    }
  }
  
  static class Api20Impl {
    static WindowInsets dispatchApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
      return param1View.dispatchApplyWindowInsets(param1WindowInsets);
    }
    
    static WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
      return param1View.onApplyWindowInsets(param1WindowInsets);
    }
    
    static void requestApplyInsets(View param1View) {
      param1View.requestApplyInsets();
    }
  }
  
  private static class Api21Impl {
    static void callCompatInsetAnimationCallback(WindowInsets param1WindowInsets, View param1View) {
      View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = (View.OnApplyWindowInsetsListener)param1View.getTag(R.id.tag_window_insets_animation_callback);
      if (onApplyWindowInsetsListener != null)
        onApplyWindowInsetsListener.onApplyWindowInsets(param1View, param1WindowInsets); 
    }
    
    static WindowInsetsCompat computeSystemWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, Rect param1Rect) {
      WindowInsets windowInsets = param1WindowInsetsCompat.toWindowInsets();
      if (windowInsets != null)
        return WindowInsetsCompat.toWindowInsetsCompat(param1View.computeSystemWindowInsets(windowInsets, param1Rect), param1View); 
      param1Rect.setEmpty();
      return param1WindowInsetsCompat;
    }
    
    static boolean dispatchNestedFling(View param1View, float param1Float1, float param1Float2, boolean param1Boolean) {
      return param1View.dispatchNestedFling(param1Float1, param1Float2, param1Boolean);
    }
    
    static boolean dispatchNestedPreFling(View param1View, float param1Float1, float param1Float2) {
      return param1View.dispatchNestedPreFling(param1Float1, param1Float2);
    }
    
    static boolean dispatchNestedPreScroll(View param1View, int param1Int1, int param1Int2, int[] param1ArrayOfint1, int[] param1ArrayOfint2) {
      return param1View.dispatchNestedPreScroll(param1Int1, param1Int2, param1ArrayOfint1, param1ArrayOfint2);
    }
    
    static boolean dispatchNestedScroll(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int[] param1ArrayOfint) {
      return param1View.dispatchNestedScroll(param1Int1, param1Int2, param1Int3, param1Int4, param1ArrayOfint);
    }
    
    static ColorStateList getBackgroundTintList(View param1View) {
      return param1View.getBackgroundTintList();
    }
    
    static PorterDuff.Mode getBackgroundTintMode(View param1View) {
      return param1View.getBackgroundTintMode();
    }
    
    static float getElevation(View param1View) {
      return param1View.getElevation();
    }
    
    public static WindowInsetsCompat getRootWindowInsets(View param1View) {
      return WindowInsetsCompat.Api21ReflectionHolder.getRootWindowInsets(param1View);
    }
    
    static String getTransitionName(View param1View) {
      return param1View.getTransitionName();
    }
    
    static float getTranslationZ(View param1View) {
      return param1View.getTranslationZ();
    }
    
    static float getZ(View param1View) {
      return param1View.getZ();
    }
    
    static boolean hasNestedScrollingParent(View param1View) {
      return param1View.hasNestedScrollingParent();
    }
    
    static boolean isImportantForAccessibility(View param1View) {
      return param1View.isImportantForAccessibility();
    }
    
    static boolean isNestedScrollingEnabled(View param1View) {
      return param1View.isNestedScrollingEnabled();
    }
    
    static void setBackgroundTintList(View param1View, ColorStateList param1ColorStateList) {
      param1View.setBackgroundTintList(param1ColorStateList);
    }
    
    static void setBackgroundTintMode(View param1View, PorterDuff.Mode param1Mode) {
      param1View.setBackgroundTintMode(param1Mode);
    }
    
    static void setElevation(View param1View, float param1Float) {
      param1View.setElevation(param1Float);
    }
    
    static void setNestedScrollingEnabled(View param1View, boolean param1Boolean) {
      param1View.setNestedScrollingEnabled(param1Boolean);
    }
    
    static void setOnApplyWindowInsetsListener(final View v, final OnApplyWindowInsetsListener listener) {
      if (Build.VERSION.SDK_INT < 30)
        v.setTag(R.id.tag_on_apply_window_listener, listener); 
      if (listener == null) {
        v.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener)v.getTag(R.id.tag_window_insets_animation_callback));
        return;
      } 
      v.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            WindowInsetsCompat mLastInsets = null;
            
            final OnApplyWindowInsetsListener val$listener;
            
            final View val$v;
            
            public WindowInsets onApplyWindowInsets(View param2View, WindowInsets param2WindowInsets) {
              WindowInsetsCompat windowInsetsCompat2 = WindowInsetsCompat.toWindowInsetsCompat(param2WindowInsets, param2View);
              if (Build.VERSION.SDK_INT < 30) {
                ViewCompat.Api21Impl.callCompatInsetAnimationCallback(param2WindowInsets, v);
                if (windowInsetsCompat2.equals(this.mLastInsets))
                  return listener.onApplyWindowInsets(param2View, windowInsetsCompat2).toWindowInsets(); 
              } 
              this.mLastInsets = windowInsetsCompat2;
              WindowInsetsCompat windowInsetsCompat1 = listener.onApplyWindowInsets(param2View, windowInsetsCompat2);
              if (Build.VERSION.SDK_INT >= 30)
                return windowInsetsCompat1.toWindowInsets(); 
              ViewCompat.requestApplyInsets(param2View);
              return windowInsetsCompat1.toWindowInsets();
            }
          });
    }
    
    static void setTransitionName(View param1View, String param1String) {
      param1View.setTransitionName(param1String);
    }
    
    static void setTranslationZ(View param1View, float param1Float) {
      param1View.setTranslationZ(param1Float);
    }
    
    static void setZ(View param1View, float param1Float) {
      param1View.setZ(param1Float);
    }
    
    static boolean startNestedScroll(View param1View, int param1Int) {
      return param1View.startNestedScroll(param1Int);
    }
    
    static void stopNestedScroll(View param1View) {
      param1View.stopNestedScroll();
    }
  }
  
  class null implements View.OnApplyWindowInsetsListener {
    WindowInsetsCompat mLastInsets = null;
    
    final OnApplyWindowInsetsListener val$listener;
    
    final View val$v;
    
    public WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
      WindowInsetsCompat windowInsetsCompat2 = WindowInsetsCompat.toWindowInsetsCompat(param1WindowInsets, param1View);
      if (Build.VERSION.SDK_INT < 30) {
        ViewCompat.Api21Impl.callCompatInsetAnimationCallback(param1WindowInsets, v);
        if (windowInsetsCompat2.equals(this.mLastInsets))
          return listener.onApplyWindowInsets(param1View, windowInsetsCompat2).toWindowInsets(); 
      } 
      this.mLastInsets = windowInsetsCompat2;
      WindowInsetsCompat windowInsetsCompat1 = listener.onApplyWindowInsets(param1View, windowInsetsCompat2);
      if (Build.VERSION.SDK_INT >= 30)
        return windowInsetsCompat1.toWindowInsets(); 
      ViewCompat.requestApplyInsets(param1View);
      return windowInsetsCompat1.toWindowInsets();
    }
  }
  
  private static class Api23Impl {
    public static WindowInsetsCompat getRootWindowInsets(View param1View) {
      WindowInsets windowInsets = param1View.getRootWindowInsets();
      if (windowInsets == null)
        return null; 
      WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets);
      windowInsetsCompat.setRootWindowInsets(windowInsetsCompat);
      windowInsetsCompat.copyRootViewBounds(param1View.getRootView());
      return windowInsetsCompat;
    }
    
    static int getScrollIndicators(View param1View) {
      return param1View.getScrollIndicators();
    }
    
    static void setScrollIndicators(View param1View, int param1Int) {
      param1View.setScrollIndicators(param1Int);
    }
    
    static void setScrollIndicators(View param1View, int param1Int1, int param1Int2) {
      param1View.setScrollIndicators(param1Int1, param1Int2);
    }
  }
  
  static class Api24Impl {
    static void cancelDragAndDrop(View param1View) {
      param1View.cancelDragAndDrop();
    }
    
    static void dispatchFinishTemporaryDetach(View param1View) {
      param1View.dispatchFinishTemporaryDetach();
    }
    
    static void dispatchStartTemporaryDetach(View param1View) {
      param1View.dispatchStartTemporaryDetach();
    }
    
    static void setPointerIcon(View param1View, PointerIcon param1PointerIcon) {
      param1View.setPointerIcon(param1PointerIcon);
    }
    
    static boolean startDragAndDrop(View param1View, ClipData param1ClipData, View.DragShadowBuilder param1DragShadowBuilder, Object param1Object, int param1Int) {
      return param1View.startDragAndDrop(param1ClipData, param1DragShadowBuilder, param1Object, param1Int);
    }
    
    static void updateDragShadow(View param1View, View.DragShadowBuilder param1DragShadowBuilder) {
      param1View.updateDragShadow(param1DragShadowBuilder);
    }
  }
  
  static class Api26Impl {
    static void addKeyboardNavigationClusters(View param1View, Collection<View> param1Collection, int param1Int) {
      param1View.addKeyboardNavigationClusters(param1Collection, param1Int);
    }
    
    static int getImportantForAutofill(View param1View) {
      return param1View.getImportantForAutofill();
    }
    
    static int getNextClusterForwardId(View param1View) {
      return param1View.getNextClusterForwardId();
    }
    
    static boolean hasExplicitFocusable(View param1View) {
      return param1View.hasExplicitFocusable();
    }
    
    static boolean isFocusedByDefault(View param1View) {
      return param1View.isFocusedByDefault();
    }
    
    static boolean isImportantForAutofill(View param1View) {
      return param1View.isImportantForAutofill();
    }
    
    static boolean isKeyboardNavigationCluster(View param1View) {
      return param1View.isKeyboardNavigationCluster();
    }
    
    static View keyboardNavigationClusterSearch(View param1View1, View param1View2, int param1Int) {
      return param1View1.keyboardNavigationClusterSearch(param1View2, param1Int);
    }
    
    static boolean restoreDefaultFocus(View param1View) {
      return param1View.restoreDefaultFocus();
    }
    
    static void setAutofillHints(View param1View, String... param1VarArgs) {
      param1View.setAutofillHints(param1VarArgs);
    }
    
    static void setFocusedByDefault(View param1View, boolean param1Boolean) {
      param1View.setFocusedByDefault(param1Boolean);
    }
    
    static void setImportantForAutofill(View param1View, int param1Int) {
      param1View.setImportantForAutofill(param1Int);
    }
    
    static void setKeyboardNavigationCluster(View param1View, boolean param1Boolean) {
      param1View.setKeyboardNavigationCluster(param1Boolean);
    }
    
    static void setNextClusterForwardId(View param1View, int param1Int) {
      param1View.setNextClusterForwardId(param1Int);
    }
    
    static void setTooltipText(View param1View, CharSequence param1CharSequence) {
      param1View.setTooltipText(param1CharSequence);
    }
  }
  
  static class Api28Impl {
    static void addOnUnhandledKeyEventListener(View param1View, ViewCompat.OnUnhandledKeyEventListenerCompat param1OnUnhandledKeyEventListenerCompat) {
      SimpleArrayMap simpleArrayMap2 = (SimpleArrayMap)param1View.getTag(R.id.tag_unhandled_key_listeners);
      SimpleArrayMap simpleArrayMap1 = simpleArrayMap2;
      if (simpleArrayMap2 == null) {
        simpleArrayMap1 = new SimpleArrayMap();
        param1View.setTag(R.id.tag_unhandled_key_listeners, simpleArrayMap1);
      } 
      Objects.requireNonNull(param1OnUnhandledKeyEventListenerCompat);
      ViewCompat$Api28Impl$$ExternalSyntheticLambda0 viewCompat$Api28Impl$$ExternalSyntheticLambda0 = new ViewCompat$Api28Impl$$ExternalSyntheticLambda0(param1OnUnhandledKeyEventListenerCompat);
      simpleArrayMap1.put(param1OnUnhandledKeyEventListenerCompat, viewCompat$Api28Impl$$ExternalSyntheticLambda0);
      param1View.addOnUnhandledKeyEventListener(viewCompat$Api28Impl$$ExternalSyntheticLambda0);
    }
    
    static CharSequence getAccessibilityPaneTitle(View param1View) {
      return param1View.getAccessibilityPaneTitle();
    }
    
    static boolean isAccessibilityHeading(View param1View) {
      return param1View.isAccessibilityHeading();
    }
    
    static boolean isScreenReaderFocusable(View param1View) {
      return param1View.isScreenReaderFocusable();
    }
    
    static void removeOnUnhandledKeyEventListener(View param1View, ViewCompat.OnUnhandledKeyEventListenerCompat param1OnUnhandledKeyEventListenerCompat) {
      SimpleArrayMap simpleArrayMap = (SimpleArrayMap)param1View.getTag(R.id.tag_unhandled_key_listeners);
      if (simpleArrayMap == null)
        return; 
      View.OnUnhandledKeyEventListener onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener)simpleArrayMap.get(param1OnUnhandledKeyEventListenerCompat);
      if (onUnhandledKeyEventListener != null)
        param1View.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener); 
    }
    
    static <T> T requireViewById(View param1View, int param1Int) {
      return (T)param1View.requireViewById(param1Int);
    }
    
    static void setAccessibilityHeading(View param1View, boolean param1Boolean) {
      param1View.setAccessibilityHeading(param1Boolean);
    }
    
    static void setAccessibilityPaneTitle(View param1View, CharSequence param1CharSequence) {
      param1View.setAccessibilityPaneTitle(param1CharSequence);
    }
    
    static void setScreenReaderFocusable(View param1View, boolean param1Boolean) {
      param1View.setScreenReaderFocusable(param1Boolean);
    }
  }
  
  private static class Api29Impl {
    static View.AccessibilityDelegate getAccessibilityDelegate(View param1View) {
      return param1View.getAccessibilityDelegate();
    }
    
    static List<Rect> getSystemGestureExclusionRects(View param1View) {
      return param1View.getSystemGestureExclusionRects();
    }
    
    static void saveAttributeDataForStyleable(View param1View, Context param1Context, int[] param1ArrayOfint, AttributeSet param1AttributeSet, TypedArray param1TypedArray, int param1Int1, int param1Int2) {
      param1View.saveAttributeDataForStyleable(param1Context, param1ArrayOfint, param1AttributeSet, param1TypedArray, param1Int1, param1Int2);
    }
    
    static void setSystemGestureExclusionRects(View param1View, List<Rect> param1List) {
      param1View.setSystemGestureExclusionRects(param1List);
    }
  }
  
  private static class Api30Impl {
    static CharSequence getStateDescription(View param1View) {
      return param1View.getStateDescription();
    }
    
    public static WindowInsetsControllerCompat getWindowInsetsController(View param1View) {
      WindowInsetsController windowInsetsController = param1View.getWindowInsetsController();
      if (windowInsetsController != null) {
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(windowInsetsController);
      } else {
        windowInsetsController = null;
      } 
      return (WindowInsetsControllerCompat)windowInsetsController;
    }
    
    static void setStateDescription(View param1View, CharSequence param1CharSequence) {
      param1View.setStateDescription(param1CharSequence);
    }
  }
  
  private static final class Api31Impl {
    public static String[] getReceiveContentMimeTypes(View param1View) {
      return param1View.getReceiveContentMimeTypes();
    }
    
    public static ContentInfoCompat performReceiveContent(View param1View, ContentInfoCompat param1ContentInfoCompat) {
      ContentInfo contentInfo2 = param1ContentInfoCompat.toContentInfo();
      ContentInfo contentInfo1 = param1View.performReceiveContent(contentInfo2);
      return (contentInfo1 == null) ? null : ((contentInfo1 == contentInfo2) ? param1ContentInfoCompat : ContentInfoCompat.toContentInfoCompat(contentInfo1));
    }
    
    public static void setOnReceiveContentListener(View param1View, String[] param1ArrayOfString, OnReceiveContentListener param1OnReceiveContentListener) {
      if (param1OnReceiveContentListener == null) {
        param1View.setOnReceiveContentListener(param1ArrayOfString, null);
      } else {
        param1View.setOnReceiveContentListener(param1ArrayOfString, new ViewCompat.OnReceiveContentListenerAdapter(param1OnReceiveContentListener));
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusRealDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusRelativeDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NestedScrollType {}
  
  private static final class OnReceiveContentListenerAdapter implements OnReceiveContentListener {
    private final OnReceiveContentListener mJetpackListener;
    
    OnReceiveContentListenerAdapter(OnReceiveContentListener param1OnReceiveContentListener) {
      this.mJetpackListener = param1OnReceiveContentListener;
    }
    
    public ContentInfo onReceiveContent(View param1View, ContentInfo param1ContentInfo) {
      ContentInfoCompat contentInfoCompat2 = ContentInfoCompat.toContentInfoCompat(param1ContentInfo);
      ContentInfoCompat contentInfoCompat1 = this.mJetpackListener.onReceiveContent(param1View, contentInfoCompat2);
      return (contentInfoCompat1 == null) ? null : ((contentInfoCompat1 == contentInfoCompat2) ? param1ContentInfo : contentInfoCompat1.toContentInfo());
    }
  }
  
  public static interface OnUnhandledKeyEventListenerCompat {
    boolean onUnhandledKeyEvent(View param1View, KeyEvent param1KeyEvent);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollAxis {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollIndicators {}
  
  static class UnhandledKeyEventManager {
    private static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList<>();
    
    private SparseArray<WeakReference<View>> mCapturedKeys = null;
    
    private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;
    
    private WeakHashMap<View, Boolean> mViewsContainingListeners = null;
    
    static UnhandledKeyEventManager at(View param1View) {
      UnhandledKeyEventManager unhandledKeyEventManager2 = (UnhandledKeyEventManager)param1View.getTag(R.id.tag_unhandled_key_event_manager);
      UnhandledKeyEventManager unhandledKeyEventManager1 = unhandledKeyEventManager2;
      if (unhandledKeyEventManager2 == null) {
        unhandledKeyEventManager1 = new UnhandledKeyEventManager();
        param1View.setTag(R.id.tag_unhandled_key_event_manager, unhandledKeyEventManager1);
      } 
      return unhandledKeyEventManager1;
    }
    
    private View dispatchInOrder(View param1View, KeyEvent param1KeyEvent) {
      WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
      if (weakHashMap == null || !weakHashMap.containsKey(param1View))
        return null; 
      if (param1View instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View;
        for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
          View view = dispatchInOrder(viewGroup.getChildAt(i), param1KeyEvent);
          if (view != null)
            return view; 
        } 
      } 
      return onUnhandledKeyEvent(param1View, param1KeyEvent) ? param1View : null;
    }
    
    private SparseArray<WeakReference<View>> getCapturedKeys() {
      if (this.mCapturedKeys == null)
        this.mCapturedKeys = new SparseArray(); 
      return this.mCapturedKeys;
    }
    
    private boolean onUnhandledKeyEvent(View param1View, KeyEvent param1KeyEvent) {
      ArrayList<ViewCompat.OnUnhandledKeyEventListenerCompat> arrayList = (ArrayList)param1View.getTag(R.id.tag_unhandled_key_listeners);
      if (arrayList != null)
        for (int i = arrayList.size() - 1; i >= 0; i--) {
          if (((ViewCompat.OnUnhandledKeyEventListenerCompat)arrayList.get(i)).onUnhandledKeyEvent(param1View, param1KeyEvent))
            return true; 
        }  
      return false;
    }
    
    private void recalcViewsWithUnhandled() {
      // Byte code:
      //   0: aload_0
      //   1: getfield mViewsContainingListeners : Ljava/util/WeakHashMap;
      //   4: astore_2
      //   5: aload_2
      //   6: ifnull -> 13
      //   9: aload_2
      //   10: invokevirtual clear : ()V
      //   13: getstatic androidx/core/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   16: astore_3
      //   17: aload_3
      //   18: invokevirtual isEmpty : ()Z
      //   21: ifeq -> 25
      //   24: return
      //   25: aload_3
      //   26: monitorenter
      //   27: aload_0
      //   28: getfield mViewsContainingListeners : Ljava/util/WeakHashMap;
      //   31: ifnonnull -> 47
      //   34: new java/util/WeakHashMap
      //   37: astore_2
      //   38: aload_2
      //   39: invokespecial <init> : ()V
      //   42: aload_0
      //   43: aload_2
      //   44: putfield mViewsContainingListeners : Ljava/util/WeakHashMap;
      //   47: aload_3
      //   48: invokevirtual size : ()I
      //   51: iconst_1
      //   52: isub
      //   53: istore_1
      //   54: iload_1
      //   55: iflt -> 149
      //   58: getstatic androidx/core/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   61: astore_2
      //   62: aload_2
      //   63: iload_1
      //   64: invokevirtual get : (I)Ljava/lang/Object;
      //   67: checkcast java/lang/ref/WeakReference
      //   70: invokevirtual get : ()Ljava/lang/Object;
      //   73: checkcast android/view/View
      //   76: astore #4
      //   78: aload #4
      //   80: ifnonnull -> 92
      //   83: aload_2
      //   84: iload_1
      //   85: invokevirtual remove : (I)Ljava/lang/Object;
      //   88: pop
      //   89: goto -> 143
      //   92: aload_0
      //   93: getfield mViewsContainingListeners : Ljava/util/WeakHashMap;
      //   96: aload #4
      //   98: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
      //   101: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   104: pop
      //   105: aload #4
      //   107: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   110: astore_2
      //   111: aload_2
      //   112: instanceof android/view/View
      //   115: ifeq -> 143
      //   118: aload_0
      //   119: getfield mViewsContainingListeners : Ljava/util/WeakHashMap;
      //   122: aload_2
      //   123: checkcast android/view/View
      //   126: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
      //   129: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   132: pop
      //   133: aload_2
      //   134: invokeinterface getParent : ()Landroid/view/ViewParent;
      //   139: astore_2
      //   140: goto -> 111
      //   143: iinc #1, -1
      //   146: goto -> 54
      //   149: aload_3
      //   150: monitorexit
      //   151: return
      //   152: astore_2
      //   153: aload_3
      //   154: monitorexit
      //   155: aload_2
      //   156: athrow
      // Exception table:
      //   from	to	target	type
      //   27	47	152	finally
      //   47	54	152	finally
      //   58	78	152	finally
      //   83	89	152	finally
      //   92	111	152	finally
      //   111	140	152	finally
      //   149	151	152	finally
      //   153	155	152	finally
    }
    
    static void registerListeningView(View param1View) {
      synchronized (sViewsWithListeners) {
        Iterator<WeakReference<View>> iterator = null.iterator();
        while (iterator.hasNext()) {
          if (((WeakReference<View>)iterator.next()).get() == param1View)
            return; 
        } 
        ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
        WeakReference<View> weakReference = new WeakReference();
        this((T)param1View);
        arrayList.add(weakReference);
        return;
      } 
    }
    
    static void unregisterListeningView(View param1View) {
      // Byte code:
      //   0: getstatic androidx/core/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   3: astore_2
      //   4: aload_2
      //   5: monitorenter
      //   6: iconst_0
      //   7: istore_1
      //   8: getstatic androidx/core/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   11: astore_3
      //   12: iload_1
      //   13: aload_3
      //   14: invokevirtual size : ()I
      //   17: if_icmpge -> 50
      //   20: aload_3
      //   21: iload_1
      //   22: invokevirtual get : (I)Ljava/lang/Object;
      //   25: checkcast java/lang/ref/WeakReference
      //   28: invokevirtual get : ()Ljava/lang/Object;
      //   31: aload_0
      //   32: if_acmpne -> 44
      //   35: aload_3
      //   36: iload_1
      //   37: invokevirtual remove : (I)Ljava/lang/Object;
      //   40: pop
      //   41: aload_2
      //   42: monitorexit
      //   43: return
      //   44: iinc #1, 1
      //   47: goto -> 8
      //   50: aload_2
      //   51: monitorexit
      //   52: return
      //   53: astore_0
      //   54: aload_2
      //   55: monitorexit
      //   56: aload_0
      //   57: athrow
      // Exception table:
      //   from	to	target	type
      //   8	43	53	finally
      //   50	52	53	finally
      //   54	56	53	finally
    }
    
    boolean dispatch(View param1View, KeyEvent param1KeyEvent) {
      boolean bool;
      if (param1KeyEvent.getAction() == 0)
        recalcViewsWithUnhandled(); 
      param1View = dispatchInOrder(param1View, param1KeyEvent);
      if (param1KeyEvent.getAction() == 0) {
        int i = param1KeyEvent.getKeyCode();
        if (param1View != null && !KeyEvent.isModifierKey(i))
          getCapturedKeys().put(i, new WeakReference<>(param1View)); 
      } 
      if (param1View != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean preDispatch(KeyEvent param1KeyEvent) {
      WeakReference<KeyEvent> weakReference1 = this.mLastDispatchedPreViewKeyEvent;
      if (weakReference1 != null && weakReference1.get() == param1KeyEvent)
        return false; 
      this.mLastDispatchedPreViewKeyEvent = new WeakReference<>(param1KeyEvent);
      WeakReference<KeyEvent> weakReference2 = null;
      SparseArray<WeakReference<View>> sparseArray = getCapturedKeys();
      weakReference1 = weakReference2;
      if (param1KeyEvent.getAction() == 1) {
        int i = sparseArray.indexOfKey(param1KeyEvent.getKeyCode());
        weakReference1 = weakReference2;
        if (i >= 0) {
          weakReference1 = (WeakReference<KeyEvent>)sparseArray.valueAt(i);
          sparseArray.removeAt(i);
        } 
      } 
      weakReference2 = weakReference1;
      if (weakReference1 == null)
        weakReference2 = (WeakReference<KeyEvent>)sparseArray.get(param1KeyEvent.getKeyCode()); 
      if (weakReference2 != null) {
        View view = (View)weakReference2.get();
        if (view != null && ViewCompat.isAttachedToWindow(view))
          onUnhandledKeyEvent(view, param1KeyEvent); 
        return true;
      } 
      return false;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\ViewCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */