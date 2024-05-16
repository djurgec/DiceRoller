package com.google.android.material.bottomsheet;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final int CORNER_ANIMATION_DURATION = 500;
  
  private static final int DEF_STYLE_RES = R.style.Widget_Design_BottomSheet_Modal;
  
  private static final float HIDE_FRICTION = 0.1F;
  
  private static final float HIDE_THRESHOLD = 0.5F;
  
  private static final int NO_WIDTH = -1;
  
  public static final int PEEK_HEIGHT_AUTO = -1;
  
  public static final int SAVE_ALL = -1;
  
  public static final int SAVE_FIT_TO_CONTENTS = 2;
  
  public static final int SAVE_HIDEABLE = 4;
  
  public static final int SAVE_NONE = 0;
  
  public static final int SAVE_PEEK_HEIGHT = 1;
  
  public static final int SAVE_SKIP_COLLAPSED = 8;
  
  private static final int SIGNIFICANT_VEL_THRESHOLD = 500;
  
  public static final int STATE_COLLAPSED = 4;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_EXPANDED = 3;
  
  public static final int STATE_HALF_EXPANDED = 6;
  
  public static final int STATE_HIDDEN = 5;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "BottomSheetBehavior";
  
  int activePointerId;
  
  private final ArrayList<BottomSheetCallback> callbacks = new ArrayList<>();
  
  private int childHeight;
  
  int collapsedOffset;
  
  private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      final BottomSheetBehavior this$0;
      
      private boolean releasedLow(View param1View) {
        boolean bool;
        if (param1View.getTop() > (BottomSheetBehavior.this.parentHeight + BottomSheetBehavior.this.getExpandedOffset()) / 2) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
      
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        return param1View.getLeft();
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        int i = BottomSheetBehavior.this.getExpandedOffset();
        if (BottomSheetBehavior.this.hideable) {
          param1Int2 = BottomSheetBehavior.this.parentHeight;
        } else {
          param1Int2 = BottomSheetBehavior.this.collapsedOffset;
        } 
        return MathUtils.clamp(param1Int1, i, param1Int2);
      }
      
      public int getViewVerticalDragRange(View param1View) {
        return BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset;
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (param1Int == 1 && BottomSheetBehavior.this.draggable)
          BottomSheetBehavior.this.setStateInternal(1); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        BottomSheetBehavior.this.dispatchOnSlide(param1Int2);
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        int i;
        byte b;
        if (param1Float2 < 0.0F) {
          if (BottomSheetBehavior.this.fitToContents) {
            i = BottomSheetBehavior.this.fitToContentsOffset;
            b = 3;
          } else if (param1View.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } else {
            i = BottomSheetBehavior.this.getExpandedOffset();
            b = 3;
          } 
        } else if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(param1View, param1Float2)) {
          if ((Math.abs(param1Float1) < Math.abs(param1Float2) && param1Float2 > 500.0F) || releasedLow(param1View)) {
            i = BottomSheetBehavior.this.parentHeight;
            b = 5;
          } else if (BottomSheetBehavior.this.fitToContents) {
            i = BottomSheetBehavior.this.fitToContentsOffset;
            b = 3;
          } else if (Math.abs(param1View.getTop() - BottomSheetBehavior.this.getExpandedOffset()) < Math.abs(param1View.getTop() - BottomSheetBehavior.this.halfExpandedOffset)) {
            i = BottomSheetBehavior.this.getExpandedOffset();
            b = 3;
          } else {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } 
        } else if (param1Float2 == 0.0F || Math.abs(param1Float1) > Math.abs(param1Float2)) {
          i = param1View.getTop();
          if (BottomSheetBehavior.this.fitToContents) {
            if (Math.abs(i - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
              i = BottomSheetBehavior.this.fitToContentsOffset;
              b = 3;
            } else {
              i = BottomSheetBehavior.this.collapsedOffset;
              b = 4;
            } 
          } else if (i < BottomSheetBehavior.this.halfExpandedOffset) {
            if (i < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
              i = BottomSheetBehavior.this.getExpandedOffset();
              b = 3;
            } else {
              i = BottomSheetBehavior.this.halfExpandedOffset;
              b = 6;
            } 
          } else if (Math.abs(i - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } else {
            i = BottomSheetBehavior.this.collapsedOffset;
            b = 4;
          } 
        } else if (BottomSheetBehavior.this.fitToContents) {
          i = BottomSheetBehavior.this.collapsedOffset;
          b = 4;
        } else {
          i = param1View.getTop();
          if (Math.abs(i - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } else {
            i = BottomSheetBehavior.this.collapsedOffset;
            b = 4;
          } 
        } 
        BottomSheetBehavior.this.startSettlingAnimation(param1View, b, i, true);
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        int i = BottomSheetBehavior.this.state;
        boolean bool = true;
        if (i == 1)
          return false; 
        if (BottomSheetBehavior.this.touchingScrollingChild)
          return false; 
        if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == param1Int) {
          View view;
          if (BottomSheetBehavior.this.nestedScrollingChildRef != null) {
            view = BottomSheetBehavior.this.nestedScrollingChildRef.get();
          } else {
            view = null;
          } 
          if (view != null && view.canScrollVertically(-1))
            return false; 
        } 
        if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != param1View)
          bool = false; 
        return bool;
      }
    };
  
  private boolean draggable = true;
  
  float elevation = -1.0F;
  
  private int expandHalfwayActionId = -1;
  
  int expandedOffset;
  
  private boolean fitToContents = true;
  
  int fitToContentsOffset;
  
  private int gestureInsetBottom;
  
  private boolean gestureInsetBottomIgnored;
  
  int halfExpandedOffset;
  
  float halfExpandedRatio = 0.5F;
  
  boolean hideable;
  
  private boolean ignoreEvents;
  
  private Map<View, Integer> importantForAccessibilityMap;
  
  private int initialY;
  
  private int insetBottom;
  
  private int insetTop;
  
  private ValueAnimator interpolatorAnimator;
  
  private boolean isShapeExpanded;
  
  private int lastNestedScrollDy;
  
  private MaterialShapeDrawable materialShapeDrawable;
  
  private int maxWidth = -1;
  
  private float maximumVelocity;
  
  private boolean nestedScrolled;
  
  WeakReference<View> nestedScrollingChildRef;
  
  private boolean paddingBottomSystemWindowInsets;
  
  private boolean paddingLeftSystemWindowInsets;
  
  private boolean paddingRightSystemWindowInsets;
  
  private boolean paddingTopSystemWindowInsets;
  
  int parentHeight;
  
  int parentWidth;
  
  private int peekHeight;
  
  private boolean peekHeightAuto;
  
  private int peekHeightGestureInsetBuffer;
  
  private int peekHeightMin;
  
  private int saveFlags = 0;
  
  private SettleRunnable settleRunnable = null;
  
  private ShapeAppearanceModel shapeAppearanceModelDefault;
  
  private boolean shapeThemingEnabled;
  
  private boolean skipCollapsed;
  
  int state = 4;
  
  boolean touchingScrollingChild;
  
  private boolean updateImportantForAccessibilityOnSiblings = false;
  
  private VelocityTracker velocityTracker;
  
  ViewDragHelper viewDragHelper;
  
  WeakReference<V> viewRef;
  
  public BottomSheetBehavior() {}
  
  public BottomSheetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.peekHeightGestureInsetBuffer = paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_min_touch_target_size);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BottomSheetBehavior_Layout);
    this.shapeThemingEnabled = typedArray.hasValue(R.styleable.BottomSheetBehavior_Layout_shapeAppearance);
    boolean bool = typedArray.hasValue(R.styleable.BottomSheetBehavior_Layout_backgroundTint);
    if (bool) {
      createMaterialShapeDrawable(paramContext, paramAttributeSet, bool, MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.BottomSheetBehavior_Layout_backgroundTint));
    } else {
      createMaterialShapeDrawable(paramContext, paramAttributeSet, bool);
    } 
    createShapeValueAnimator();
    if (Build.VERSION.SDK_INT >= 21)
      this.elevation = typedArray.getDimension(R.styleable.BottomSheetBehavior_Layout_android_elevation, -1.0F); 
    if (typedArray.hasValue(R.styleable.BottomSheetBehavior_Layout_android_maxWidth))
      setMaxWidth(typedArray.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_android_maxWidth, -1)); 
    TypedValue typedValue = typedArray.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
    if (typedValue != null && typedValue.data == -1) {
      setPeekHeight(typedValue.data);
    } else {
      setPeekHeight(typedArray.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
    } 
    setHideable(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
    setGestureInsetBottomIgnored(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
    setFitToContents(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
    setSkipCollapsed(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
    setDraggable(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
    setSaveFlags(typedArray.getInt(R.styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
    setHalfExpandedRatio(typedArray.getFloat(R.styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5F));
    typedValue = typedArray.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_expandedOffset);
    if (typedValue != null && typedValue.type == 16) {
      setExpandedOffset(typedValue.data);
    } else {
      setExpandedOffset(typedArray.getDimensionPixelOffset(R.styleable.BottomSheetBehavior_Layout_behavior_expandedOffset, 0));
    } 
    this.paddingBottomSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_paddingBottomSystemWindowInsets, false);
    this.paddingLeftSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_paddingLeftSystemWindowInsets, false);
    this.paddingRightSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_paddingRightSystemWindowInsets, false);
    this.paddingTopSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_paddingTopSystemWindowInsets, true);
    typedArray.recycle();
    this.maximumVelocity = ViewConfiguration.get(paramContext).getScaledMaximumFlingVelocity();
  }
  
  private int addAccessibilityActionForState(V paramV, int paramInt1, int paramInt2) {
    return ViewCompat.addAccessibilityAction((View)paramV, paramV.getResources().getString(paramInt1), createAccessibilityViewCommandForState(paramInt2));
  }
  
  private void calculateCollapsedOffset() {
    int i = calculatePeekHeight();
    if (this.fitToContents) {
      this.collapsedOffset = Math.max(this.parentHeight - i, this.fitToContentsOffset);
    } else {
      this.collapsedOffset = this.parentHeight - i;
    } 
  }
  
  private void calculateHalfExpandedOffset() {
    this.halfExpandedOffset = (int)(this.parentHeight * (1.0F - this.halfExpandedRatio));
  }
  
  private int calculatePeekHeight() {
    if (this.peekHeightAuto)
      return Math.min(Math.max(this.peekHeightMin, this.parentHeight - this.parentWidth * 9 / 16), this.childHeight) + this.insetBottom; 
    if (!this.gestureInsetBottomIgnored && !this.paddingBottomSystemWindowInsets) {
      int i = this.gestureInsetBottom;
      if (i > 0)
        return Math.max(this.peekHeight, i + this.peekHeightGestureInsetBuffer); 
    } 
    return this.peekHeight + this.insetBottom;
  }
  
  private AccessibilityViewCommand createAccessibilityViewCommandForState(final int state) {
    return new AccessibilityViewCommand() {
        final BottomSheetBehavior this$0;
        
        final int val$state;
        
        public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
          BottomSheetBehavior.this.setState(state);
          return true;
        }
      };
  }
  
  private void createMaterialShapeDrawable(Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean) {
    createMaterialShapeDrawable(paramContext, paramAttributeSet, paramBoolean, null);
  }
  
  private void createMaterialShapeDrawable(Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean, ColorStateList paramColorStateList) {
    if (this.shapeThemingEnabled) {
      this.shapeAppearanceModelDefault = ShapeAppearanceModel.builder(paramContext, paramAttributeSet, R.attr.bottomSheetStyle, DEF_STYLE_RES).build();
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModelDefault);
      this.materialShapeDrawable = materialShapeDrawable;
      materialShapeDrawable.initializeElevationOverlay(paramContext);
      if (paramBoolean && paramColorStateList != null) {
        this.materialShapeDrawable.setFillColor(paramColorStateList);
      } else {
        TypedValue typedValue = new TypedValue();
        paramContext.getTheme().resolveAttribute(16842801, typedValue, true);
        this.materialShapeDrawable.setTint(typedValue.data);
      } 
    } 
  }
  
  private void createShapeValueAnimator() {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    this.interpolatorAnimator = valueAnimator;
    valueAnimator.setDuration(500L);
    this.interpolatorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BottomSheetBehavior this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            if (BottomSheetBehavior.this.materialShapeDrawable != null)
              BottomSheetBehavior.this.materialShapeDrawable.setInterpolation(f); 
          }
        });
  }
  
  public static <V extends View> BottomSheetBehavior<V> from(V paramV) {
    ViewGroup.LayoutParams layoutParams = paramV.getLayoutParams();
    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
      if (behavior instanceof BottomSheetBehavior)
        return (BottomSheetBehavior<V>)behavior; 
      throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    } 
    throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
  }
  
  private float getYVelocity() {
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker == null)
      return 0.0F; 
    velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
    return this.velocityTracker.getYVelocity(this.activePointerId);
  }
  
  private void replaceAccessibilityActionForState(V paramV, AccessibilityNodeInfoCompat.AccessibilityActionCompat paramAccessibilityActionCompat, int paramInt) {
    ViewCompat.replaceAccessibilityAction((View)paramV, paramAccessibilityActionCompat, null, createAccessibilityViewCommandForState(paramInt));
  }
  
  private void reset() {
    this.activePointerId = -1;
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.velocityTracker = null;
    } 
  }
  
  private void restoreOptionalState(SavedState paramSavedState) {
    int i = this.saveFlags;
    if (i == 0)
      return; 
    if (i == -1 || (i & 0x1) == 1)
      this.peekHeight = paramSavedState.peekHeight; 
    i = this.saveFlags;
    if (i == -1 || (i & 0x2) == 2)
      this.fitToContents = paramSavedState.fitToContents; 
    i = this.saveFlags;
    if (i == -1 || (i & 0x4) == 4)
      this.hideable = paramSavedState.hideable; 
    i = this.saveFlags;
    if (i == -1 || (i & 0x8) == 8)
      this.skipCollapsed = paramSavedState.skipCollapsed; 
  }
  
  private void setWindowInsetsListener(View paramView) {
    final boolean shouldHandleGestureInsets;
    if (Build.VERSION.SDK_INT >= 29 && !isGestureInsetBottomIgnored() && !this.peekHeightAuto) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!this.paddingBottomSystemWindowInsets && !this.paddingLeftSystemWindowInsets && !this.paddingRightSystemWindowInsets && !bool)
      return; 
    ViewUtils.doOnApplyWindowInsets(paramView, new ViewUtils.OnApplyWindowInsetsListener() {
          final BottomSheetBehavior this$0;
          
          final boolean val$shouldHandleGestureInsets;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, ViewUtils.RelativePadding param1RelativePadding) {
            BottomSheetBehavior.access$102(BottomSheetBehavior.this, param1WindowInsetsCompat.getSystemWindowInsetTop());
            boolean bool = ViewUtils.isLayoutRtl(param1View);
            int i = param1View.getPaddingBottom();
            int j = param1View.getPaddingLeft();
            int k = param1View.getPaddingRight();
            if (BottomSheetBehavior.this.paddingBottomSystemWindowInsets) {
              BottomSheetBehavior.access$302(BottomSheetBehavior.this, param1WindowInsetsCompat.getSystemWindowInsetBottom());
              i = param1RelativePadding.bottom + BottomSheetBehavior.this.insetBottom;
            } 
            if (BottomSheetBehavior.this.paddingLeftSystemWindowInsets) {
              if (bool) {
                j = param1RelativePadding.end;
              } else {
                j = param1RelativePadding.start;
              } 
              j += param1WindowInsetsCompat.getSystemWindowInsetLeft();
            } 
            if (BottomSheetBehavior.this.paddingRightSystemWindowInsets) {
              if (bool) {
                k = param1RelativePadding.start;
              } else {
                k = param1RelativePadding.end;
              } 
              k += param1WindowInsetsCompat.getSystemWindowInsetRight();
            } 
            param1View.setPadding(j, param1View.getPaddingTop(), k, i);
            if (shouldHandleGestureInsets)
              BottomSheetBehavior.access$602(BottomSheetBehavior.this, (param1WindowInsetsCompat.getMandatorySystemGestureInsets()).bottom); 
            if (BottomSheetBehavior.this.paddingBottomSystemWindowInsets || shouldHandleGestureInsets)
              BottomSheetBehavior.this.updatePeekHeight(false); 
            return param1WindowInsetsCompat;
          }
        });
  }
  
  private void settleToStatePendingLayout(final int finalState) {
    final View child = (View)this.viewRef.get();
    if (view == null)
      return; 
    ViewParent viewParent = view.getParent();
    if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
      view.post(new Runnable() {
            final BottomSheetBehavior this$0;
            
            final View val$child;
            
            final int val$finalState;
            
            public void run() {
              BottomSheetBehavior.this.settleToState(child, finalState);
            }
          });
    } else {
      settleToState(view, finalState);
    } 
  }
  
  private void updateAccessibilityActions() {
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null)
      return; 
    View view = (View)weakReference.get();
    if (view == null)
      return; 
    ViewCompat.removeAccessibilityAction(view, 524288);
    ViewCompat.removeAccessibilityAction(view, 262144);
    ViewCompat.removeAccessibilityAction(view, 1048576);
    int i = this.expandHalfwayActionId;
    if (i != -1)
      ViewCompat.removeAccessibilityAction(view, i); 
    boolean bool = this.fitToContents;
    i = 6;
    if (!bool && this.state != 6)
      this.expandHalfwayActionId = addAccessibilityActionForState((V)view, R.string.bottomsheet_action_expand_halfway, 6); 
    if (this.hideable && this.state != 5)
      replaceAccessibilityActionForState((V)view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, 5); 
    switch (this.state) {
      default:
        return;
      case 6:
        replaceAccessibilityActionForState((V)view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, 4);
        replaceAccessibilityActionForState((V)view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, 3);
      case 4:
        if (this.fitToContents)
          i = 3; 
        replaceAccessibilityActionForState((V)view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, i);
      case 3:
        break;
    } 
    if (this.fitToContents)
      i = 4; 
    replaceAccessibilityActionForState((V)view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, i);
  }
  
  private void updateDrawableForTargetState(int paramInt) {
    boolean bool;
    if (paramInt == 2)
      return; 
    if (paramInt == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.isShapeExpanded != bool) {
      this.isShapeExpanded = bool;
      if (this.materialShapeDrawable != null) {
        ValueAnimator valueAnimator = this.interpolatorAnimator;
        if (valueAnimator != null)
          if (valueAnimator.isRunning()) {
            this.interpolatorAnimator.reverse();
          } else {
            float f;
            if (bool) {
              f = 0.0F;
            } else {
              f = 1.0F;
            } 
            this.interpolatorAnimator.setFloatValues(new float[] { 1.0F - f, f });
            this.interpolatorAnimator.start();
          }  
      } 
    } 
  }
  
  private void updateImportantForAccessibility(boolean paramBoolean) {
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null)
      return; 
    ViewParent viewParent = ((View)weakReference.get()).getParent();
    if (!(viewParent instanceof CoordinatorLayout))
      return; 
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)viewParent;
    int i = coordinatorLayout.getChildCount();
    if (Build.VERSION.SDK_INT >= 16 && paramBoolean)
      if (this.importantForAccessibilityMap == null) {
        this.importantForAccessibilityMap = new HashMap<>(i);
      } else {
        return;
      }  
    for (byte b = 0; b < i; b++) {
      View view = coordinatorLayout.getChildAt(b);
      if (view != this.viewRef.get())
        if (paramBoolean) {
          if (Build.VERSION.SDK_INT >= 16)
            this.importantForAccessibilityMap.put(view, Integer.valueOf(view.getImportantForAccessibility())); 
          if (this.updateImportantForAccessibilityOnSiblings)
            ViewCompat.setImportantForAccessibility(view, 4); 
        } else if (this.updateImportantForAccessibilityOnSiblings) {
          Map<View, Integer> map = this.importantForAccessibilityMap;
          if (map != null && map.containsKey(view))
            ViewCompat.setImportantForAccessibility(view, ((Integer)this.importantForAccessibilityMap.get(view)).intValue()); 
        }  
    } 
    if (!paramBoolean) {
      this.importantForAccessibilityMap = null;
    } else if (this.updateImportantForAccessibilityOnSiblings) {
      ((View)this.viewRef.get()).sendAccessibilityEvent(8);
    } 
  }
  
  private void updatePeekHeight(boolean paramBoolean) {
    if (this.viewRef != null) {
      calculateCollapsedOffset();
      if (this.state == 4) {
        View view = (View)this.viewRef.get();
        if (view != null)
          if (paramBoolean) {
            settleToStatePendingLayout(this.state);
          } else {
            view.requestLayout();
          }  
      } 
    } 
  }
  
  public void addBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    if (!this.callbacks.contains(paramBottomSheetCallback))
      this.callbacks.add(paramBottomSheetCallback); 
  }
  
  public void disableShapeAnimations() {
    this.interpolatorAnimator = null;
  }
  
  void dispatchOnSlide(int paramInt) {
    View view = (View)this.viewRef.get();
    if (view != null && !this.callbacks.isEmpty()) {
      float f;
      int i = this.collapsedOffset;
      if (paramInt > i || i == getExpandedOffset()) {
        i = this.collapsedOffset;
        f = (i - paramInt) / (this.parentHeight - i);
      } else {
        i = this.collapsedOffset;
        f = (i - paramInt) / (i - getExpandedOffset());
      } 
      for (paramInt = 0; paramInt < this.callbacks.size(); paramInt++)
        ((BottomSheetCallback)this.callbacks.get(paramInt)).onSlide(view, f); 
    } 
  }
  
  View findScrollingChild(View paramView) {
    if (ViewCompat.isNestedScrollingEnabled(paramView))
      return paramView; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      byte b = 0;
      int i = viewGroup.getChildCount();
      while (b < i) {
        paramView = findScrollingChild(viewGroup.getChildAt(b));
        if (paramView != null)
          return paramView; 
        b++;
      } 
    } 
    return null;
  }
  
  public int getExpandedOffset() {
    int i;
    if (this.fitToContents) {
      i = this.fitToContentsOffset;
    } else {
      int j = this.expandedOffset;
      if (this.paddingTopSystemWindowInsets) {
        i = 0;
      } else {
        i = this.insetTop;
      } 
      i = Math.max(j, i);
    } 
    return i;
  }
  
  public float getHalfExpandedRatio() {
    return this.halfExpandedRatio;
  }
  
  MaterialShapeDrawable getMaterialShapeDrawable() {
    return this.materialShapeDrawable;
  }
  
  public int getMaxWidth() {
    return this.maxWidth;
  }
  
  public int getPeekHeight() {
    int i;
    if (this.peekHeightAuto) {
      i = -1;
    } else {
      i = this.peekHeight;
    } 
    return i;
  }
  
  int getPeekHeightMin() {
    return this.peekHeightMin;
  }
  
  public int getSaveFlags() {
    return this.saveFlags;
  }
  
  public boolean getSkipCollapsed() {
    return this.skipCollapsed;
  }
  
  public int getState() {
    return this.state;
  }
  
  public boolean isDraggable() {
    return this.draggable;
  }
  
  public boolean isFitToContents() {
    return this.fitToContents;
  }
  
  public boolean isGestureInsetBottomIgnored() {
    return this.gestureInsetBottomIgnored;
  }
  
  public boolean isHideable() {
    return this.hideable;
  }
  
  public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams paramLayoutParams) {
    super.onAttachedToLayoutParams(paramLayoutParams);
    this.viewRef = null;
    this.viewDragHelper = null;
  }
  
  public void onDetachedFromLayoutParams() {
    super.onDetachedFromLayoutParams();
    this.viewRef = null;
    this.viewDragHelper = null;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    View view;
    int i;
    boolean bool = paramV.isShown();
    boolean bool1 = false;
    if (!bool || !this.draggable) {
      this.ignoreEvents = true;
      return false;
    } 
    int j = paramMotionEvent.getActionMasked();
    if (j == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    V v = null;
    switch (j) {
      case 1:
      case 3:
        this.touchingScrollingChild = false;
        this.activePointerId = -1;
        if (this.ignoreEvents) {
          this.ignoreEvents = false;
          return false;
        } 
        break;
      case 0:
        i = (int)paramMotionEvent.getX();
        this.initialY = (int)paramMotionEvent.getY();
        if (this.state != 2) {
          WeakReference<View> weakReference1 = this.nestedScrollingChildRef;
          if (weakReference1 != null) {
            View view1 = weakReference1.get();
          } else {
            weakReference1 = null;
          } 
          if (weakReference1 != null && paramCoordinatorLayout.isPointInChildBounds((View)weakReference1, i, this.initialY)) {
            this.activePointerId = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
            this.touchingScrollingChild = true;
          } 
        } 
        if (this.activePointerId == -1 && !paramCoordinatorLayout.isPointInChildBounds((View)paramV, i, this.initialY)) {
          bool = true;
        } else {
          bool = false;
        } 
        this.ignoreEvents = bool;
        break;
    } 
    if (!this.ignoreEvents) {
      ViewDragHelper viewDragHelper = this.viewDragHelper;
      if (viewDragHelper != null && viewDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
        return true; 
    } 
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    paramV = v;
    if (weakReference != null)
      view = weakReference.get(); 
    if (j == 2 && view != null && !this.ignoreEvents && this.state != 1 && !paramCoordinatorLayout.isPointInChildBounds(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()) && this.viewDragHelper != null && Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
      bool = true;
    } else {
      bool = bool1;
    } 
    return bool;
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, final V child, int paramInt) {
    if (ViewCompat.getFitsSystemWindows((View)paramCoordinatorLayout) && !ViewCompat.getFitsSystemWindows((View)child))
      child.setFitsSystemWindows(true); 
    if (this.viewRef == null) {
      this.peekHeightMin = paramCoordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
      setWindowInsetsListener((View)child);
      this.viewRef = new WeakReference<>(child);
      if (this.shapeThemingEnabled) {
        MaterialShapeDrawable materialShapeDrawable1 = this.materialShapeDrawable;
        if (materialShapeDrawable1 != null)
          ViewCompat.setBackground((View)child, (Drawable)materialShapeDrawable1); 
      } 
      MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
      if (materialShapeDrawable != null) {
        boolean bool;
        float f = this.elevation;
        if (f == -1.0F)
          f = ViewCompat.getElevation((View)child); 
        materialShapeDrawable.setElevation(f);
        if (this.state == 3) {
          bool = true;
        } else {
          bool = false;
        } 
        this.isShapeExpanded = bool;
        materialShapeDrawable = this.materialShapeDrawable;
        if (bool) {
          f = 0.0F;
        } else {
          f = 1.0F;
        } 
        materialShapeDrawable.setInterpolation(f);
      } 
      updateAccessibilityActions();
      if (ViewCompat.getImportantForAccessibility((View)child) == 0)
        ViewCompat.setImportantForAccessibility((View)child, 1); 
      int m = child.getMeasuredWidth();
      int n = this.maxWidth;
      if (m > n && n != -1) {
        final ViewGroup.LayoutParams lp = child.getLayoutParams();
        layoutParams.width = this.maxWidth;
        child.post(new Runnable() {
              final BottomSheetBehavior this$0;
              
              final View val$child;
              
              final ViewGroup.LayoutParams val$lp;
              
              public void run() {
                child.setLayoutParams(lp);
              }
            });
      } 
    } 
    if (this.viewDragHelper == null)
      this.viewDragHelper = ViewDragHelper.create((ViewGroup)paramCoordinatorLayout, this.dragCallback); 
    int i = child.getTop();
    paramCoordinatorLayout.onLayoutChild((View)child, paramInt);
    this.parentWidth = paramCoordinatorLayout.getWidth();
    this.parentHeight = paramCoordinatorLayout.getHeight();
    int j = child.getHeight();
    this.childHeight = j;
    paramInt = this.parentHeight;
    int k = this.insetTop;
    if (paramInt - j < k)
      if (this.paddingTopSystemWindowInsets) {
        this.childHeight = paramInt;
      } else {
        this.childHeight = paramInt - k;
      }  
    this.fitToContentsOffset = Math.max(0, paramInt - this.childHeight);
    calculateHalfExpandedOffset();
    calculateCollapsedOffset();
    paramInt = this.state;
    if (paramInt == 3) {
      ViewCompat.offsetTopAndBottom((View)child, getExpandedOffset());
    } else if (paramInt == 6) {
      ViewCompat.offsetTopAndBottom((View)child, this.halfExpandedOffset);
    } else if (this.hideable && paramInt == 5) {
      ViewCompat.offsetTopAndBottom((View)child, this.parentHeight);
    } else if (paramInt == 4) {
      ViewCompat.offsetTopAndBottom((View)child, this.collapsedOffset);
    } else if (paramInt == 1 || paramInt == 2) {
      ViewCompat.offsetTopAndBottom((View)child, i - child.getTop());
    } 
    this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild((View)child));
    return true;
  }
  
  public boolean onNestedPreFling(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, float paramFloat1, float paramFloat2) {
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    boolean bool = false;
    if (weakReference != null) {
      if (paramView == weakReference.get() && (this.state != 3 || super.onNestedPreFling(paramCoordinatorLayout, (View)paramV, paramView, paramFloat1, paramFloat2)))
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  public void onNestedPreScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    if (paramInt3 == 1)
      return; 
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    if (weakReference != null) {
      View view = weakReference.get();
    } else {
      weakReference = null;
    } 
    if (paramView != weakReference)
      return; 
    paramInt1 = paramV.getTop();
    int i = paramInt1 - paramInt2;
    if (paramInt2 > 0) {
      if (i < getExpandedOffset()) {
        paramArrayOfint[1] = paramInt1 - getExpandedOffset();
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(3);
      } else {
        if (!this.draggable)
          return; 
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } 
    } else if (paramInt2 < 0 && !paramView.canScrollVertically(-1)) {
      paramInt3 = this.collapsedOffset;
      if (i <= paramInt3 || this.hideable) {
        if (!this.draggable)
          return; 
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } else {
        paramArrayOfint[1] = paramInt1 - paramInt3;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(4);
      } 
    } 
    dispatchOnSlide(paramV.getTop());
    this.lastNestedScrollDy = paramInt2;
    this.nestedScrolled = true;
  }
  
  public void onNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfint) {}
  
  public void onRestoreInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV, Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramCoordinatorLayout, (View)paramV, savedState.getSuperState());
    restoreOptionalState(savedState);
    if (savedState.state == 1 || savedState.state == 2) {
      this.state = 4;
      return;
    } 
    this.state = savedState.state;
  }
  
  public Parcelable onSaveInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV) {
    return (Parcelable)new SavedState(super.onSaveInstanceState(paramCoordinatorLayout, (View)paramV), this);
  }
  
  public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    boolean bool = false;
    this.lastNestedScrollDy = 0;
    this.nestedScrolled = false;
    if ((paramInt1 & 0x2) != 0)
      bool = true; 
    return bool;
  }
  
  public void onStopNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt) {
    byte b;
    if (paramV.getTop() == getExpandedOffset()) {
      setStateInternal(3);
      return;
    } 
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    if (weakReference == null || paramView != weakReference.get() || !this.nestedScrolled)
      return; 
    if (this.lastNestedScrollDy > 0) {
      if (this.fitToContents) {
        paramInt = this.fitToContentsOffset;
        b = 3;
      } else if (paramV.getTop() > this.halfExpandedOffset) {
        paramInt = this.halfExpandedOffset;
        b = 6;
      } else {
        paramInt = getExpandedOffset();
        b = 3;
      } 
    } else if (this.hideable && shouldHide((View)paramV, getYVelocity())) {
      paramInt = this.parentHeight;
      b = 5;
    } else if (this.lastNestedScrollDy == 0) {
      paramInt = paramV.getTop();
      if (this.fitToContents) {
        if (Math.abs(paramInt - this.fitToContentsOffset) < Math.abs(paramInt - this.collapsedOffset)) {
          paramInt = this.fitToContentsOffset;
          b = 3;
        } else {
          paramInt = this.collapsedOffset;
          b = 4;
        } 
      } else {
        b = this.halfExpandedOffset;
        if (paramInt < b) {
          if (paramInt < Math.abs(paramInt - this.collapsedOffset)) {
            paramInt = getExpandedOffset();
            b = 3;
          } else {
            paramInt = this.halfExpandedOffset;
            b = 6;
          } 
        } else if (Math.abs(paramInt - b) < Math.abs(paramInt - this.collapsedOffset)) {
          paramInt = this.halfExpandedOffset;
          b = 6;
        } else {
          paramInt = this.collapsedOffset;
          b = 4;
        } 
      } 
    } else if (this.fitToContents) {
      paramInt = this.collapsedOffset;
      b = 4;
    } else {
      paramInt = paramV.getTop();
      if (Math.abs(paramInt - this.halfExpandedOffset) < Math.abs(paramInt - this.collapsedOffset)) {
        paramInt = this.halfExpandedOffset;
        b = 6;
      } else {
        paramInt = this.collapsedOffset;
        b = 4;
      } 
    } 
    startSettlingAnimation((View)paramV, b, paramInt, false);
    this.nestedScrolled = false;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (!paramV.isShown())
      return false; 
    int i = paramMotionEvent.getActionMasked();
    if (this.state == 1 && i == 0)
      return true; 
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null)
      viewDragHelper.processTouchEvent(paramMotionEvent); 
    if (i == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    if (this.viewDragHelper != null && i == 2 && !this.ignoreEvents && Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop())
      this.viewDragHelper.captureChildView((View)paramV, paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex())); 
    return this.ignoreEvents ^ true;
  }
  
  public void removeBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    this.callbacks.remove(paramBottomSheetCallback);
  }
  
  @Deprecated
  public void setBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    Log.w("BottomSheetBehavior", "BottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes all existing callbacks, including ones set internally by library authors, which may result in unintended behavior. This may change in the future. Please use `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your own callbacks.");
    this.callbacks.clear();
    if (paramBottomSheetCallback != null)
      this.callbacks.add(paramBottomSheetCallback); 
  }
  
  public void setDraggable(boolean paramBoolean) {
    this.draggable = paramBoolean;
  }
  
  public void setExpandedOffset(int paramInt) {
    if (paramInt >= 0) {
      this.expandedOffset = paramInt;
      return;
    } 
    throw new IllegalArgumentException("offset must be greater than or equal to 0");
  }
  
  public void setFitToContents(boolean paramBoolean) {
    int i;
    if (this.fitToContents == paramBoolean)
      return; 
    this.fitToContents = paramBoolean;
    if (this.viewRef != null)
      calculateCollapsedOffset(); 
    if (this.fitToContents && this.state == 6) {
      i = 3;
    } else {
      i = this.state;
    } 
    setStateInternal(i);
    updateAccessibilityActions();
  }
  
  public void setGestureInsetBottomIgnored(boolean paramBoolean) {
    this.gestureInsetBottomIgnored = paramBoolean;
  }
  
  public void setHalfExpandedRatio(float paramFloat) {
    if (paramFloat > 0.0F && paramFloat < 1.0F) {
      this.halfExpandedRatio = paramFloat;
      if (this.viewRef != null)
        calculateHalfExpandedOffset(); 
      return;
    } 
    throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
  }
  
  public void setHideable(boolean paramBoolean) {
    if (this.hideable != paramBoolean) {
      this.hideable = paramBoolean;
      if (!paramBoolean && this.state == 5)
        setState(4); 
      updateAccessibilityActions();
    } 
  }
  
  public void setMaxWidth(int paramInt) {
    this.maxWidth = paramInt;
  }
  
  public void setPeekHeight(int paramInt) {
    setPeekHeight(paramInt, false);
  }
  
  public final void setPeekHeight(int paramInt, boolean paramBoolean) {
    boolean bool = false;
    if (paramInt == -1) {
      if (!this.peekHeightAuto) {
        this.peekHeightAuto = true;
        bool = true;
      } 
    } else if (this.peekHeightAuto || this.peekHeight != paramInt) {
      this.peekHeightAuto = false;
      this.peekHeight = Math.max(0, paramInt);
      bool = true;
    } 
    if (bool)
      updatePeekHeight(paramBoolean); 
  }
  
  public void setSaveFlags(int paramInt) {
    this.saveFlags = paramInt;
  }
  
  public void setSkipCollapsed(boolean paramBoolean) {
    this.skipCollapsed = paramBoolean;
  }
  
  public void setState(int paramInt) {
    if (paramInt == this.state)
      return; 
    if (this.viewRef == null) {
      if (paramInt == 4 || paramInt == 3 || paramInt == 6 || (this.hideable && paramInt == 5))
        this.state = paramInt; 
      return;
    } 
    settleToStatePendingLayout(paramInt);
  }
  
  void setStateInternal(int paramInt) {
    if (this.state == paramInt)
      return; 
    this.state = paramInt;
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null)
      return; 
    View view = (View)weakReference.get();
    if (view == null)
      return; 
    if (paramInt == 3) {
      updateImportantForAccessibility(true);
    } else if (paramInt == 6 || paramInt == 5 || paramInt == 4) {
      updateImportantForAccessibility(false);
    } 
    updateDrawableForTargetState(paramInt);
    for (byte b = 0; b < this.callbacks.size(); b++)
      ((BottomSheetCallback)this.callbacks.get(b)).onStateChanged(view, paramInt); 
    updateAccessibilityActions();
  }
  
  public void setUpdateImportantForAccessibilityOnSiblings(boolean paramBoolean) {
    this.updateImportantForAccessibilityOnSiblings = paramBoolean;
  }
  
  void settleToState(View paramView, int paramInt) {
    int i;
    int j;
    if (paramInt == 4) {
      i = this.collapsedOffset;
      j = paramInt;
    } else if (paramInt == 6) {
      int k = this.halfExpandedOffset;
      i = k;
      j = paramInt;
      if (this.fitToContents) {
        i = k;
        j = paramInt;
        if (k <= this.fitToContentsOffset) {
          j = 3;
          i = this.fitToContentsOffset;
        } 
      } 
    } else if (paramInt == 3) {
      i = getExpandedOffset();
      j = paramInt;
    } else if (this.hideable && paramInt == 5) {
      i = this.parentHeight;
      j = paramInt;
    } else {
      throw new IllegalArgumentException("Illegal state argument: " + paramInt);
    } 
    startSettlingAnimation(paramView, j, i, false);
  }
  
  boolean shouldHide(View paramView, float paramFloat) {
    boolean bool1 = this.skipCollapsed;
    boolean bool = true;
    if (bool1)
      return true; 
    if (paramView.getTop() < this.collapsedOffset)
      return false; 
    int i = calculatePeekHeight();
    if (Math.abs(paramView.getTop() + 0.1F * paramFloat - this.collapsedOffset) / i <= 0.5F)
      bool = false; 
    return bool;
  }
  
  void startSettlingAnimation(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null && (paramBoolean ? viewDragHelper.settleCapturedViewAt(paramView.getLeft(), paramInt2) : viewDragHelper.smoothSlideViewTo(paramView, paramView.getLeft(), paramInt2))) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0) {
      setStateInternal(2);
      updateDrawableForTargetState(paramInt1);
      if (this.settleRunnable == null)
        this.settleRunnable = new SettleRunnable(paramView, paramInt1); 
      if (!this.settleRunnable.isPosted) {
        this.settleRunnable.targetState = paramInt1;
        ViewCompat.postOnAnimation(paramView, this.settleRunnable);
        SettleRunnable.access$802(this.settleRunnable, true);
      } else {
        this.settleRunnable.targetState = paramInt1;
      } 
    } else {
      setStateInternal(paramInt1);
    } 
  }
  
  public static abstract class BottomSheetCallback {
    public abstract void onSlide(View param1View, float param1Float);
    
    public abstract void onStateChanged(View param1View, int param1Int);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SaveFlags {}
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomSheetBehavior.SavedState(param2Parcel, null);
        }
        
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomSheetBehavior.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomSheetBehavior.SavedState[] newArray(int param2Int) {
          return new BottomSheetBehavior.SavedState[param2Int];
        }
      };
    
    boolean fitToContents;
    
    boolean hideable;
    
    int peekHeight;
    
    boolean skipCollapsed;
    
    final int state;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, (ClassLoader)null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.state = param1Parcel.readInt();
      this.peekHeight = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool2 = false;
      if (i == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.fitToContents = bool1;
      if (param1Parcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.hideable = bool1;
      boolean bool1 = bool2;
      if (param1Parcel.readInt() == 1)
        bool1 = true; 
      this.skipCollapsed = bool1;
    }
    
    @Deprecated
    public SavedState(Parcelable param1Parcelable, int param1Int) {
      super(param1Parcelable);
      this.state = param1Int;
    }
    
    public SavedState(Parcelable param1Parcelable, BottomSheetBehavior<?> param1BottomSheetBehavior) {
      super(param1Parcelable);
      this.state = param1BottomSheetBehavior.state;
      this.peekHeight = param1BottomSheetBehavior.peekHeight;
      this.fitToContents = param1BottomSheetBehavior.fitToContents;
      this.hideable = param1BottomSheetBehavior.hideable;
      this.skipCollapsed = param1BottomSheetBehavior.skipCollapsed;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.state);
      param1Parcel.writeInt(this.peekHeight);
      param1Parcel.writeInt(this.fitToContents);
      param1Parcel.writeInt(this.hideable);
      param1Parcel.writeInt(this.skipCollapsed);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomSheetBehavior.SavedState(param1Parcel, null);
    }
    
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomSheetBehavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomSheetBehavior.SavedState[] newArray(int param1Int) {
      return new BottomSheetBehavior.SavedState[param1Int];
    }
  }
  
  private class SettleRunnable implements Runnable {
    private boolean isPosted;
    
    int targetState;
    
    final BottomSheetBehavior this$0;
    
    private final View view;
    
    SettleRunnable(View param1View, int param1Int) {
      this.view = param1View;
      this.targetState = param1Int;
    }
    
    public void run() {
      if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.view, this);
      } else {
        BottomSheetBehavior.this.setStateInternal(this.targetState);
      } 
      this.isPosted = false;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomsheet\BottomSheetBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */