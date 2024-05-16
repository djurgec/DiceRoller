package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AppBarLayout extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
  private static final int DEF_STYLE_RES = R.style.Widget_Design_AppBarLayout;
  
  private static final int INVALID_SCROLL_RANGE = -1;
  
  static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
  
  static final int PENDING_ACTION_COLLAPSED = 2;
  
  static final int PENDING_ACTION_EXPANDED = 1;
  
  static final int PENDING_ACTION_FORCE = 8;
  
  static final int PENDING_ACTION_NONE = 0;
  
  private int currentOffset;
  
  private int downPreScrollRange = -1;
  
  private int downScrollRange = -1;
  
  private ValueAnimator elevationOverlayAnimator;
  
  private boolean haveChildWithInterpolator;
  
  private WindowInsetsCompat lastInsets;
  
  private boolean liftOnScroll;
  
  private WeakReference<View> liftOnScrollTargetView;
  
  private int liftOnScrollTargetViewId;
  
  private boolean liftable;
  
  private boolean liftableOverride;
  
  private boolean lifted;
  
  private List<BaseOnOffsetChangedListener> listeners;
  
  private int pendingAction = 0;
  
  private Drawable statusBarForeground;
  
  private int[] tmpStatesArray;
  
  private int totalScrollRange = -1;
  
  public AppBarLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.appBarLayoutStyle);
  }
  
  public AppBarLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    setOrientation(1);
    if (Build.VERSION.SDK_INT >= 21) {
      ViewUtilsLollipop.setBoundsViewOutlineProvider((View)this);
      ViewUtilsLollipop.setStateListAnimatorFromAttrs((View)this, paramAttributeSet, paramInt, i);
    } 
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.AppBarLayout, paramInt, i, new int[0]);
    ViewCompat.setBackground((View)this, typedArray.getDrawable(R.styleable.AppBarLayout_android_background));
    if (getBackground() instanceof ColorDrawable) {
      ColorDrawable colorDrawable = (ColorDrawable)getBackground();
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(colorDrawable.getColor()));
      materialShapeDrawable.initializeElevationOverlay(paramContext);
      ViewCompat.setBackground((View)this, (Drawable)materialShapeDrawable);
    } 
    if (typedArray.hasValue(R.styleable.AppBarLayout_expanded))
      setExpanded(typedArray.getBoolean(R.styleable.AppBarLayout_expanded, false), false, false); 
    if (Build.VERSION.SDK_INT >= 21 && typedArray.hasValue(R.styleable.AppBarLayout_elevation))
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, typedArray.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0)); 
    if (Build.VERSION.SDK_INT >= 26) {
      if (typedArray.hasValue(R.styleable.AppBarLayout_android_keyboardNavigationCluster))
        setKeyboardNavigationCluster(typedArray.getBoolean(R.styleable.AppBarLayout_android_keyboardNavigationCluster, false)); 
      if (typedArray.hasValue(R.styleable.AppBarLayout_android_touchscreenBlocksFocus))
        setTouchscreenBlocksFocus(typedArray.getBoolean(R.styleable.AppBarLayout_android_touchscreenBlocksFocus, false)); 
    } 
    this.liftOnScroll = typedArray.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
    this.liftOnScrollTargetViewId = typedArray.getResourceId(R.styleable.AppBarLayout_liftOnScrollTargetViewId, -1);
    setStatusBarForeground(typedArray.getDrawable(R.styleable.AppBarLayout_statusBarForeground));
    typedArray.recycle();
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          final AppBarLayout this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            return AppBarLayout.this.onWindowInsetChanged(param1WindowInsetsCompat);
          }
        });
  }
  
  private void clearLiftOnScrollTargetView() {
    WeakReference<View> weakReference = this.liftOnScrollTargetView;
    if (weakReference != null)
      weakReference.clear(); 
    this.liftOnScrollTargetView = null;
  }
  
  private View findLiftOnScrollTargetView(View paramView) {
    if (this.liftOnScrollTargetView == null) {
      int i = this.liftOnScrollTargetViewId;
      if (i != -1) {
        View view = null;
        if (paramView != null)
          view = paramView.findViewById(i); 
        paramView = view;
        if (view == null) {
          paramView = view;
          if (getParent() instanceof ViewGroup)
            paramView = ((ViewGroup)getParent()).findViewById(this.liftOnScrollTargetViewId); 
        } 
        if (paramView != null)
          this.liftOnScrollTargetView = new WeakReference<>(paramView); 
      } 
    } 
    WeakReference<View> weakReference = this.liftOnScrollTargetView;
    if (weakReference != null) {
      View view = weakReference.get();
    } else {
      weakReference = null;
    } 
    return (View)weakReference;
  }
  
  private boolean hasCollapsibleChild() {
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      if (((LayoutParams)getChildAt(b).getLayoutParams()).isCollapsible())
        return true; 
      b++;
    } 
    return false;
  }
  
  private void invalidateScrollRanges() {
    this.totalScrollRange = -1;
    this.downPreScrollRange = -1;
    this.downScrollRange = -1;
  }
  
  private void setExpanded(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    byte b1;
    byte b2;
    if (paramBoolean1) {
      b1 = 1;
    } else {
      b1 = 2;
    } 
    byte b3 = 0;
    if (paramBoolean2) {
      b2 = 4;
    } else {
      b2 = 0;
    } 
    if (paramBoolean3)
      b3 = 8; 
    this.pendingAction = b1 | b2 | b3;
    requestLayout();
  }
  
  private boolean setLiftableState(boolean paramBoolean) {
    if (this.liftable != paramBoolean) {
      this.liftable = paramBoolean;
      refreshDrawableState();
      return true;
    } 
    return false;
  }
  
  private boolean shouldDrawStatusBarForeground() {
    boolean bool;
    if (this.statusBarForeground != null && getTopInset() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean shouldOffsetFirstChild() {
    int i = getChildCount();
    boolean bool = false;
    if (i > 0) {
      View view = getChildAt(0);
      boolean bool1 = bool;
      if (view.getVisibility() != 8) {
        bool1 = bool;
        if (!ViewCompat.getFitsSystemWindows(view))
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  private void startLiftOnScrollElevationOverlayAnimation(final MaterialShapeDrawable background, boolean paramBoolean) {
    float f2;
    float f1 = getResources().getDimension(R.dimen.design_appbar_elevation);
    float f3 = 0.0F;
    if (paramBoolean) {
      f2 = 0.0F;
    } else {
      f2 = f1;
    } 
    if (paramBoolean)
      f3 = f1; 
    ValueAnimator valueAnimator = this.elevationOverlayAnimator;
    if (valueAnimator != null)
      valueAnimator.cancel(); 
    valueAnimator = ValueAnimator.ofFloat(new float[] { f2, f3 });
    this.elevationOverlayAnimator = valueAnimator;
    valueAnimator.setDuration(getResources().getInteger(R.integer.app_bar_elevation_anim_duration));
    this.elevationOverlayAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    this.elevationOverlayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final AppBarLayout this$0;
          
          final MaterialShapeDrawable val$background;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            background.setElevation(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
          }
        });
    this.elevationOverlayAnimator.start();
  }
  
  private void updateWillNotDraw() {
    setWillNotDraw(shouldDrawStatusBarForeground() ^ true);
  }
  
  public void addOnOffsetChangedListener(BaseOnOffsetChangedListener paramBaseOnOffsetChangedListener) {
    if (this.listeners == null)
      this.listeners = new ArrayList<>(); 
    if (paramBaseOnOffsetChangedListener != null && !this.listeners.contains(paramBaseOnOffsetChangedListener))
      this.listeners.add(paramBaseOnOffsetChangedListener); 
  }
  
  public void addOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener) {
    addOnOffsetChangedListener(paramOnOffsetChangedListener);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    if (shouldDrawStatusBarForeground()) {
      int i = paramCanvas.save();
      paramCanvas.translate(0.0F, -this.currentOffset);
      this.statusBarForeground.draw(paramCanvas);
      paramCanvas.restoreToCount(i);
    } 
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    Drawable drawable = this.statusBarForeground;
    if (drawable != null && drawable.isStateful() && drawable.setState(arrayOfInt))
      invalidateDrawable(drawable); 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-1, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (Build.VERSION.SDK_INT >= 19 && paramLayoutParams instanceof LinearLayout.LayoutParams) ? new LayoutParams((LinearLayout.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams));
  }
  
  public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
    return new Behavior();
  }
  
  int getDownNestedPreScrollRange() {
    int i = this.downPreScrollRange;
    if (i != -1)
      return i; 
    int k = 0;
    int j = getChildCount() - 1;
    while (j >= 0) {
      View view = getChildAt(j);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int m = view.getMeasuredHeight();
      i = layoutParams.scrollFlags;
      if ((i & 0x5) == 5) {
        int n = layoutParams.topMargin + layoutParams.bottomMargin;
        if ((i & 0x8) != 0) {
          i = n + ViewCompat.getMinimumHeight(view);
        } else if ((i & 0x2) != 0) {
          i = n + m - ViewCompat.getMinimumHeight(view);
        } else {
          i = n + m;
        } 
        n = i;
        if (j == 0) {
          n = i;
          if (ViewCompat.getFitsSystemWindows(view))
            n = Math.min(i, m - getTopInset()); 
        } 
        i = k + n;
      } else {
        i = k;
        if (k > 0)
          break; 
      } 
      j--;
      k = i;
    } 
    i = Math.max(0, k);
    this.downPreScrollRange = i;
    return i;
  }
  
  int getDownNestedScrollRange() {
    int j;
    int i = this.downScrollRange;
    if (i != -1)
      return i; 
    i = 0;
    byte b = 0;
    int k = getChildCount();
    while (true) {
      j = i;
      if (b < k) {
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int i1 = view.getMeasuredHeight();
        int i2 = layoutParams.topMargin;
        int n = layoutParams.bottomMargin;
        int m = layoutParams.scrollFlags;
        j = i;
        if ((m & 0x1) != 0) {
          i += i1 + i2 + n;
          if ((m & 0x2) != 0) {
            j = i - ViewCompat.getMinimumHeight(view);
            break;
          } 
          b++;
          continue;
        } 
      } 
      break;
    } 
    i = Math.max(0, j);
    this.downScrollRange = i;
    return i;
  }
  
  public int getLiftOnScrollTargetViewId() {
    return this.liftOnScrollTargetViewId;
  }
  
  public final int getMinimumHeightForVisibleOverlappingContent() {
    int j = getTopInset();
    int i = ViewCompat.getMinimumHeight((View)this);
    if (i != 0)
      return i * 2 + j; 
    i = getChildCount();
    if (i >= 1) {
      i = ViewCompat.getMinimumHeight(getChildAt(i - 1));
    } else {
      i = 0;
    } 
    return (i != 0) ? (i * 2 + j) : (getHeight() / 3);
  }
  
  int getPendingAction() {
    return this.pendingAction;
  }
  
  public Drawable getStatusBarForeground() {
    return this.statusBarForeground;
  }
  
  @Deprecated
  public float getTargetElevation() {
    return 0.0F;
  }
  
  final int getTopInset() {
    boolean bool;
    WindowInsetsCompat windowInsetsCompat = this.lastInsets;
    if (windowInsetsCompat != null) {
      bool = windowInsetsCompat.getSystemWindowInsetTop();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final int getTotalScrollRange() {
    int j;
    int i = this.totalScrollRange;
    if (i != -1)
      return i; 
    i = 0;
    byte b = 0;
    int k = getChildCount();
    while (true) {
      j = i;
      if (b < k) {
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n = view.getMeasuredHeight();
        int m = layoutParams.scrollFlags;
        j = i;
        if ((m & 0x1) != 0) {
          j = i + layoutParams.topMargin + n + layoutParams.bottomMargin;
          i = j;
          if (b == 0) {
            i = j;
            if (ViewCompat.getFitsSystemWindows(view))
              i = j - getTopInset(); 
          } 
          if ((m & 0x2) != 0) {
            j = i - ViewCompat.getMinimumHeight(view);
            break;
          } 
          b++;
          continue;
        } 
      } 
      break;
    } 
    i = Math.max(0, j);
    this.totalScrollRange = i;
    return i;
  }
  
  int getUpNestedPreScrollRange() {
    return getTotalScrollRange();
  }
  
  boolean hasChildWithInterpolator() {
    return this.haveChildWithInterpolator;
  }
  
  boolean hasScrollableChildren() {
    boolean bool;
    if (getTotalScrollRange() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isLiftOnScroll() {
    return this.liftOnScroll;
  }
  
  public boolean isLifted() {
    return this.lifted;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this);
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    if (this.tmpStatesArray == null)
      this.tmpStatesArray = new int[4]; 
    int[] arrayOfInt2 = this.tmpStatesArray;
    int[] arrayOfInt1 = super.onCreateDrawableState(arrayOfInt2.length + paramInt);
    if (this.liftable) {
      paramInt = R.attr.state_liftable;
    } else {
      paramInt = -R.attr.state_liftable;
    } 
    arrayOfInt2[0] = paramInt;
    if (this.liftable && this.lifted) {
      paramInt = R.attr.state_lifted;
    } else {
      paramInt = -R.attr.state_lifted;
    } 
    arrayOfInt2[1] = paramInt;
    if (this.liftable) {
      paramInt = R.attr.state_collapsible;
    } else {
      paramInt = -R.attr.state_collapsible;
    } 
    arrayOfInt2[2] = paramInt;
    if (this.liftable && this.lifted) {
      paramInt = R.attr.state_collapsed;
    } else {
      paramInt = -R.attr.state_collapsed;
    } 
    arrayOfInt2[3] = paramInt;
    return mergeDrawableStates(arrayOfInt1, arrayOfInt2);
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    clearLiftOnScrollTargetView();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramBoolean = ViewCompat.getFitsSystemWindows((View)this);
    boolean bool = true;
    if (paramBoolean && shouldOffsetFirstChild()) {
      paramInt2 = getTopInset();
      for (paramInt1 = getChildCount() - 1; paramInt1 >= 0; paramInt1--)
        ViewCompat.offsetTopAndBottom(getChildAt(paramInt1), paramInt2); 
    } 
    invalidateScrollRanges();
    this.haveChildWithInterpolator = false;
    paramInt1 = 0;
    paramInt2 = getChildCount();
    while (paramInt1 < paramInt2) {
      if (((LayoutParams)getChildAt(paramInt1).getLayoutParams()).getScrollInterpolator() != null) {
        this.haveChildWithInterpolator = true;
        break;
      } 
      paramInt1++;
    } 
    Drawable drawable = this.statusBarForeground;
    if (drawable != null)
      drawable.setBounds(0, 0, getWidth(), getTopInset()); 
    if (!this.liftableOverride) {
      paramBoolean = bool;
      if (!this.liftOnScroll)
        if (hasCollapsibleChild()) {
          paramBoolean = bool;
        } else {
          paramBoolean = false;
        }  
      setLiftableState(paramBoolean);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getMode(paramInt2);
    if (i != 1073741824 && ViewCompat.getFitsSystemWindows((View)this) && shouldOffsetFirstChild()) {
      paramInt1 = getMeasuredHeight();
      switch (i) {
        case 0:
          paramInt1 += getTopInset();
          break;
        case -2147483648:
          paramInt1 = MathUtils.clamp(getMeasuredHeight() + getTopInset(), 0, View.MeasureSpec.getSize(paramInt2));
          break;
      } 
      setMeasuredDimension(getMeasuredWidth(), paramInt1);
    } 
    invalidateScrollRanges();
  }
  
  void onOffsetChanged(int paramInt) {
    this.currentOffset = paramInt;
    if (!willNotDraw())
      ViewCompat.postInvalidateOnAnimation((View)this); 
    List<BaseOnOffsetChangedListener> list = this.listeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        BaseOnOffsetChangedListener<AppBarLayout> baseOnOffsetChangedListener = this.listeners.get(b);
        if (baseOnOffsetChangedListener != null)
          baseOnOffsetChangedListener.onOffsetChanged(this, paramInt); 
        b++;
      } 
    } 
  }
  
  WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = null;
    if (ViewCompat.getFitsSystemWindows((View)this))
      windowInsetsCompat = paramWindowInsetsCompat; 
    if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat)) {
      this.lastInsets = windowInsetsCompat;
      updateWillNotDraw();
      requestLayout();
    } 
    return paramWindowInsetsCompat;
  }
  
  public void removeOnOffsetChangedListener(BaseOnOffsetChangedListener paramBaseOnOffsetChangedListener) {
    List<BaseOnOffsetChangedListener> list = this.listeners;
    if (list != null && paramBaseOnOffsetChangedListener != null)
      list.remove(paramBaseOnOffsetChangedListener); 
  }
  
  public void removeOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener) {
    removeOnOffsetChangedListener(paramOnOffsetChangedListener);
  }
  
  void resetPendingAction() {
    this.pendingAction = 0;
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    MaterialShapeUtils.setElevation((View)this, paramFloat);
  }
  
  public void setExpanded(boolean paramBoolean) {
    setExpanded(paramBoolean, ViewCompat.isLaidOut((View)this));
  }
  
  public void setExpanded(boolean paramBoolean1, boolean paramBoolean2) {
    setExpanded(paramBoolean1, paramBoolean2, true);
  }
  
  public void setLiftOnScroll(boolean paramBoolean) {
    this.liftOnScroll = paramBoolean;
  }
  
  public void setLiftOnScrollTargetViewId(int paramInt) {
    this.liftOnScrollTargetViewId = paramInt;
    clearLiftOnScrollTargetView();
  }
  
  public boolean setLiftable(boolean paramBoolean) {
    this.liftableOverride = true;
    return setLiftableState(paramBoolean);
  }
  
  public boolean setLifted(boolean paramBoolean) {
    return setLiftedState(paramBoolean);
  }
  
  boolean setLiftedState(boolean paramBoolean) {
    if (this.lifted != paramBoolean) {
      this.lifted = paramBoolean;
      refreshDrawableState();
      if (this.liftOnScroll && getBackground() instanceof MaterialShapeDrawable)
        startLiftOnScrollElevationOverlayAnimation((MaterialShapeDrawable)getBackground(), paramBoolean); 
      return true;
    } 
    return false;
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt == 1) {
      super.setOrientation(paramInt);
      return;
    } 
    throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
  }
  
  public void setStatusBarForeground(Drawable paramDrawable) {
    Drawable drawable = this.statusBarForeground;
    if (drawable != paramDrawable) {
      Drawable drawable1 = null;
      if (drawable != null)
        drawable.setCallback(null); 
      if (paramDrawable != null)
        drawable1 = paramDrawable.mutate(); 
      this.statusBarForeground = drawable1;
      if (drawable1 != null) {
        boolean bool;
        if (drawable1.isStateful())
          this.statusBarForeground.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.statusBarForeground, ViewCompat.getLayoutDirection((View)this));
        paramDrawable = this.statusBarForeground;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.statusBarForeground.setCallback((Drawable.Callback)this);
      } 
      updateWillNotDraw();
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setStatusBarForegroundColor(int paramInt) {
    setStatusBarForeground((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setStatusBarForegroundResource(int paramInt) {
    setStatusBarForeground(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  @Deprecated
  public void setTargetElevation(float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, paramFloat); 
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Drawable drawable = this.statusBarForeground;
    if (drawable != null)
      drawable.setVisible(bool, false); 
  }
  
  boolean shouldLift(View paramView) {
    boolean bool;
    View view2 = findLiftOnScrollTargetView(paramView);
    View view1 = view2;
    if (view2 == null)
      view1 = paramView; 
    if (view1 != null && (view1.canScrollVertically(-1) || view1.getScrollY() > 0)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.statusBarForeground);
  }
  
  protected static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T> {
    private static final int INVALID_POSITION = -1;
    
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
    
    private WeakReference<View> lastNestedScrollingChildRef;
    
    private int lastStartedType;
    
    private ValueAnimator offsetAnimator;
    
    private int offsetDelta;
    
    private int offsetToChildIndexOnLayout = -1;
    
    private boolean offsetToChildIndexOnLayoutIsMinHeight;
    
    private float offsetToChildIndexOnLayoutPerc;
    
    private BaseDragCallback onDragCallback;
    
    public BaseBehavior() {}
    
    public BaseBehavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    private void addAccessibilityScrollActions(final CoordinatorLayout coordinatorLayout, final T appBarLayout, final View scrollingView) {
      if (getTopBottomOffsetForScrollingSibling() != -appBarLayout.getTotalScrollRange() && scrollingView.canScrollVertically(1))
        addActionToExpand(coordinatorLayout, appBarLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD, false); 
      if (getTopBottomOffsetForScrollingSibling() != 0)
        if (scrollingView.canScrollVertically(-1)) {
          final int dy = -appBarLayout.getDownNestedPreScrollRange();
          if (i != 0)
            ViewCompat.replaceAccessibilityAction((View)coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, null, new AccessibilityViewCommand() {
                  final AppBarLayout.BaseBehavior this$0;
                  
                  final AppBarLayout val$appBarLayout;
                  
                  final CoordinatorLayout val$coordinatorLayout;
                  
                  final int val$dy;
                  
                  final View val$scrollingView;
                  
                  public boolean perform(View param2View, AccessibilityViewCommand.CommandArguments param2CommandArguments) {
                    AppBarLayout.BaseBehavior.this.onNestedPreScroll(coordinatorLayout, appBarLayout, scrollingView, 0, dy, new int[] { 0, 0 }, 1);
                    return true;
                  }
                }); 
        } else {
          addActionToExpand(coordinatorLayout, appBarLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, true);
        }  
    }
    
    private void addActionToExpand(CoordinatorLayout param1CoordinatorLayout, final T appBarLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat param1AccessibilityActionCompat, final boolean expand) {
      ViewCompat.replaceAccessibilityAction((View)param1CoordinatorLayout, param1AccessibilityActionCompat, null, new AccessibilityViewCommand() {
            final AppBarLayout.BaseBehavior this$0;
            
            final AppBarLayout val$appBarLayout;
            
            final boolean val$expand;
            
            public boolean perform(View param2View, AccessibilityViewCommand.CommandArguments param2CommandArguments) {
              appBarLayout.setExpanded(expand);
              return true;
            }
          });
    }
    
    private void animateOffsetTo(CoordinatorLayout param1CoordinatorLayout, T param1T, int param1Int, float param1Float) {
      int i = Math.abs(getTopBottomOffsetForScrollingSibling() - param1Int);
      param1Float = Math.abs(param1Float);
      if (param1Float > 0.0F) {
        i = Math.round(i / param1Float * 1000.0F) * 3;
      } else {
        i = (int)((1.0F + i / param1T.getHeight()) * 150.0F);
      } 
      animateOffsetWithDuration(param1CoordinatorLayout, param1T, param1Int, i);
    }
    
    private void animateOffsetWithDuration(CoordinatorLayout param1CoordinatorLayout, final T child, int param1Int1, int param1Int2) {
      final ValueAnimator coordinatorLayout;
      int i = getTopBottomOffsetForScrollingSibling();
      if (i == param1Int1) {
        valueAnimator1 = this.offsetAnimator;
        if (valueAnimator1 != null && valueAnimator1.isRunning())
          this.offsetAnimator.cancel(); 
        return;
      } 
      ValueAnimator valueAnimator2 = this.offsetAnimator;
      if (valueAnimator2 == null) {
        valueAnimator2 = new ValueAnimator();
        this.offsetAnimator = valueAnimator2;
        valueAnimator2.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        this.offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              final AppBarLayout.BaseBehavior this$0;
              
              final AppBarLayout val$child;
              
              final CoordinatorLayout val$coordinatorLayout;
              
              public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
                AppBarLayout.BaseBehavior.this.setHeaderTopBottomOffset(coordinatorLayout, child, ((Integer)param2ValueAnimator.getAnimatedValue()).intValue());
              }
            });
      } else {
        valueAnimator2.cancel();
      } 
      this.offsetAnimator.setDuration(Math.min(param1Int2, 600));
      this.offsetAnimator.setIntValues(new int[] { i, param1Int1 });
      this.offsetAnimator.start();
    }
    
    private boolean canScrollChildren(CoordinatorLayout param1CoordinatorLayout, T param1T, View param1View) {
      boolean bool;
      if (param1T.hasScrollableChildren() && param1CoordinatorLayout.getHeight() - param1View.getHeight() <= param1T.getHeight()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private static boolean checkFlag(int param1Int1, int param1Int2) {
      boolean bool;
      if ((param1Int1 & param1Int2) == param1Int2) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private View findFirstScrollingChild(CoordinatorLayout param1CoordinatorLayout) {
      byte b = 0;
      int i = param1CoordinatorLayout.getChildCount();
      while (b < i) {
        View view = param1CoordinatorLayout.getChildAt(b);
        if (view instanceof androidx.core.view.NestedScrollingChild || view instanceof android.widget.ListView || view instanceof android.widget.ScrollView)
          return view; 
        b++;
      } 
      return null;
    }
    
    private static View getAppBarChildOnOffset(AppBarLayout param1AppBarLayout, int param1Int) {
      int j = Math.abs(param1Int);
      param1Int = 0;
      int i = param1AppBarLayout.getChildCount();
      while (param1Int < i) {
        View view = param1AppBarLayout.getChildAt(param1Int);
        if (j >= view.getTop() && j <= view.getBottom())
          return view; 
        param1Int++;
      } 
      return null;
    }
    
    private int getChildIndexOnOffset(T param1T, int param1Int) {
      byte b = 0;
      int i = param1T.getChildCount();
      while (b < i) {
        View view = param1T.getChildAt(b);
        int n = view.getTop();
        int m = view.getBottom();
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)view.getLayoutParams();
        int k = n;
        int j = m;
        if (checkFlag(layoutParams.getScrollFlags(), 32)) {
          k = n - layoutParams.topMargin;
          j = m + layoutParams.bottomMargin;
        } 
        if (k <= -param1Int && j >= -param1Int)
          return b; 
        b++;
      } 
      return -1;
    }
    
    private int interpolateOffset(T param1T, int param1Int) {
      int k = Math.abs(param1Int);
      int i = 0;
      int j = param1T.getChildCount();
      while (i < j) {
        View view = param1T.getChildAt(i);
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)view.getLayoutParams();
        Interpolator interpolator = layoutParams.getScrollInterpolator();
        if (k >= view.getTop() && k <= view.getBottom()) {
          if (interpolator != null) {
            i = 0;
            int m = layoutParams.getScrollFlags();
            if ((m & 0x1) != 0) {
              j = 0 + view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
              i = j;
              if ((m & 0x2) != 0)
                i = j - ViewCompat.getMinimumHeight(view); 
            } 
            j = i;
            if (ViewCompat.getFitsSystemWindows(view))
              j = i - param1T.getTopInset(); 
            if (j > 0) {
              i = view.getTop();
              i = Math.round(j * interpolator.getInterpolation((k - i) / j));
              return Integer.signum(param1Int) * (view.getTop() + i);
            } 
          } 
          break;
        } 
        i++;
      } 
      return param1Int;
    }
    
    private boolean shouldJumpElevationState(CoordinatorLayout param1CoordinatorLayout, T param1T) {
      List<View> list = param1CoordinatorLayout.getDependents((View)param1T);
      byte b = 0;
      int i = list.size();
      while (true) {
        boolean bool = false;
        if (b < i) {
          View view = list.get(b);
          CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)view.getLayoutParams()).getBehavior();
          if (behavior instanceof AppBarLayout.ScrollingViewBehavior) {
            if (((AppBarLayout.ScrollingViewBehavior)behavior).getOverlayTop() != 0)
              bool = true; 
            return bool;
          } 
          b++;
          continue;
        } 
        return false;
      } 
    }
    
    private void snapToChildIfNeeded(CoordinatorLayout param1CoordinatorLayout, T param1T) {
      int j = getTopBottomOffsetForScrollingSibling();
      int i = getChildIndexOnOffset(param1T, j);
      if (i >= 0) {
        View view = param1T.getChildAt(i);
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)view.getLayoutParams();
        int k = layoutParams.getScrollFlags();
        if ((k & 0x11) == 17) {
          int i1 = -view.getTop();
          int m = -view.getBottom();
          int n = m;
          if (i == param1T.getChildCount() - 1)
            n = m + param1T.getTopInset(); 
          if (checkFlag(k, 2)) {
            m = n + ViewCompat.getMinimumHeight(view);
            i = i1;
          } else {
            i = i1;
            m = n;
            if (checkFlag(k, 5)) {
              m = ViewCompat.getMinimumHeight(view) + n;
              if (j < m) {
                i = m;
                m = n;
              } else {
                i = i1;
              } 
            } 
          } 
          i1 = i;
          n = m;
          if (checkFlag(k, 32)) {
            i1 = i + layoutParams.topMargin;
            n = m - layoutParams.bottomMargin;
          } 
          if (j < (n + i1) / 2) {
            m = n;
          } else {
            m = i1;
          } 
          animateOffsetTo(param1CoordinatorLayout, param1T, MathUtils.clamp(m, -param1T.getTotalScrollRange(), 0), 0.0F);
        } 
      } 
    }
    
    private void updateAccessibilityActions(CoordinatorLayout param1CoordinatorLayout, T param1T) {
      ViewCompat.removeAccessibilityAction((View)param1CoordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
      ViewCompat.removeAccessibilityAction((View)param1CoordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
      View view = findFirstScrollingChild(param1CoordinatorLayout);
      if (view == null || param1T.getTotalScrollRange() == 0)
        return; 
      if (!(((CoordinatorLayout.LayoutParams)view.getLayoutParams()).getBehavior() instanceof AppBarLayout.ScrollingViewBehavior))
        return; 
      addAccessibilityScrollActions(param1CoordinatorLayout, param1T, view);
    }
    
    private void updateAppBarLayoutDrawableState(CoordinatorLayout param1CoordinatorLayout, T param1T, int param1Int1, int param1Int2, boolean param1Boolean) {
      View view = getAppBarChildOnOffset((AppBarLayout)param1T, param1Int1);
      if (view != null) {
        int i = ((AppBarLayout.LayoutParams)view.getLayoutParams()).getScrollFlags();
        boolean bool2 = false;
        boolean bool1 = bool2;
        if ((i & 0x1) != 0) {
          int j = ViewCompat.getMinimumHeight(view);
          boolean bool = false;
          bool1 = false;
          if (param1Int2 > 0 && (i & 0xC) != 0) {
            if (-param1Int1 >= view.getBottom() - j - param1T.getTopInset())
              bool1 = true; 
          } else {
            bool1 = bool2;
            if ((i & 0x2) != 0) {
              bool1 = bool;
              if (-param1Int1 >= view.getBottom() - j - param1T.getTopInset())
                bool1 = true; 
            } 
          } 
        } 
        if (param1T.isLiftOnScroll())
          bool1 = param1T.shouldLift(findFirstScrollingChild(param1CoordinatorLayout)); 
        bool1 = param1T.setLiftedState(bool1);
        if (param1Boolean || (bool1 && shouldJumpElevationState(param1CoordinatorLayout, param1T)))
          param1T.jumpDrawablesToCurrentState(); 
      } 
    }
    
    boolean canDragView(T param1T) {
      BaseDragCallback<T> baseDragCallback = this.onDragCallback;
      if (baseDragCallback != null)
        return baseDragCallback.canDrag(param1T); 
      WeakReference<View> weakReference = this.lastNestedScrollingChildRef;
      boolean bool = true;
      if (weakReference != null) {
        View view = weakReference.get();
        if (view == null || !view.isShown() || view.canScrollVertically(-1))
          bool = false; 
        return bool;
      } 
      return true;
    }
    
    int getMaxDragOffset(T param1T) {
      return -param1T.getDownNestedScrollRange();
    }
    
    int getScrollRangeForDragFling(T param1T) {
      return param1T.getTotalScrollRange();
    }
    
    int getTopBottomOffsetForScrollingSibling() {
      return getTopAndBottomOffset() + this.offsetDelta;
    }
    
    boolean isOffsetAnimatorRunning() {
      boolean bool;
      ValueAnimator valueAnimator = this.offsetAnimator;
      if (valueAnimator != null && valueAnimator.isRunning()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void onFlingFinished(CoordinatorLayout param1CoordinatorLayout, T param1T) {
      snapToChildIfNeeded(param1CoordinatorLayout, param1T);
      if (param1T.isLiftOnScroll())
        param1T.setLiftedState(param1T.shouldLift(findFirstScrollingChild(param1CoordinatorLayout))); 
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, T param1T, int param1Int) {
      boolean bool = super.onLayoutChild(param1CoordinatorLayout, param1T, param1Int);
      int i = param1T.getPendingAction();
      param1Int = this.offsetToChildIndexOnLayout;
      if (param1Int >= 0 && (i & 0x8) == 0) {
        View view = param1T.getChildAt(param1Int);
        param1Int = -view.getBottom();
        if (this.offsetToChildIndexOnLayoutIsMinHeight) {
          param1Int += ViewCompat.getMinimumHeight(view) + param1T.getTopInset();
        } else {
          param1Int += Math.round(view.getHeight() * this.offsetToChildIndexOnLayoutPerc);
        } 
        setHeaderTopBottomOffset(param1CoordinatorLayout, param1T, param1Int);
      } else if (i != 0) {
        if ((i & 0x4) != 0) {
          param1Int = 1;
        } else {
          param1Int = 0;
        } 
        if ((i & 0x2) != 0) {
          i = -param1T.getUpNestedPreScrollRange();
          if (param1Int != 0) {
            animateOffsetTo(param1CoordinatorLayout, param1T, i, 0.0F);
          } else {
            setHeaderTopBottomOffset(param1CoordinatorLayout, param1T, i);
          } 
        } else if ((i & 0x1) != 0) {
          if (param1Int != 0) {
            animateOffsetTo(param1CoordinatorLayout, param1T, 0, 0.0F);
          } else {
            setHeaderTopBottomOffset(param1CoordinatorLayout, param1T, 0);
          } 
        } 
      } 
      param1T.resetPendingAction();
      this.offsetToChildIndexOnLayout = -1;
      setTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -param1T.getTotalScrollRange(), 0));
      updateAppBarLayoutDrawableState(param1CoordinatorLayout, param1T, getTopAndBottomOffset(), 0, true);
      param1T.onOffsetChanged(getTopAndBottomOffset());
      updateAccessibilityActions(param1CoordinatorLayout, param1T);
      return bool;
    }
    
    public boolean onMeasureChild(CoordinatorLayout param1CoordinatorLayout, T param1T, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (((CoordinatorLayout.LayoutParams)param1T.getLayoutParams()).height == -2) {
        param1CoordinatorLayout.onMeasureChild((View)param1T, param1Int1, param1Int2, View.MeasureSpec.makeMeasureSpec(0, 0), param1Int4);
        return true;
      } 
      return super.onMeasureChild(param1CoordinatorLayout, (View)param1T, param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public void onNestedPreScroll(CoordinatorLayout param1CoordinatorLayout, T param1T, View param1View, int param1Int1, int param1Int2, int[] param1ArrayOfint, int param1Int3) {
      if (param1Int2 != 0) {
        if (param1Int2 < 0) {
          param1Int3 = -param1T.getTotalScrollRange();
          int i = param1T.getDownNestedPreScrollRange();
          param1Int1 = param1Int3;
          i += param1Int3;
          param1Int3 = param1Int1;
          param1Int1 = i;
        } else {
          param1Int3 = -param1T.getUpNestedPreScrollRange();
          param1Int1 = 0;
        } 
        if (param1Int3 != param1Int1)
          param1ArrayOfint[1] = scroll(param1CoordinatorLayout, param1T, param1Int2, param1Int3, param1Int1); 
      } 
      if (param1T.isLiftOnScroll())
        param1T.setLiftedState(param1T.shouldLift(param1View)); 
    }
    
    public void onNestedScroll(CoordinatorLayout param1CoordinatorLayout, T param1T, View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int[] param1ArrayOfint) {
      if (param1Int4 < 0)
        param1ArrayOfint[1] = scroll(param1CoordinatorLayout, param1T, param1Int4, -param1T.getDownNestedScrollRange(), 0); 
      if (param1Int4 == 0)
        updateAccessibilityActions(param1CoordinatorLayout, param1T); 
    }
    
    public void onRestoreInstanceState(CoordinatorLayout param1CoordinatorLayout, T param1T, Parcelable param1Parcelable) {
      SavedState savedState;
      if (param1Parcelable instanceof SavedState) {
        savedState = (SavedState)param1Parcelable;
        super.onRestoreInstanceState(param1CoordinatorLayout, (View)param1T, savedState.getSuperState());
        this.offsetToChildIndexOnLayout = savedState.firstVisibleChildIndex;
        this.offsetToChildIndexOnLayoutPerc = savedState.firstVisibleChildPercentageShown;
        this.offsetToChildIndexOnLayoutIsMinHeight = savedState.firstVisibleChildAtMinimumHeight;
      } else {
        super.onRestoreInstanceState(param1CoordinatorLayout, (View)param1T, (Parcelable)savedState);
        this.offsetToChildIndexOnLayout = -1;
      } 
    }
    
    public Parcelable onSaveInstanceState(CoordinatorLayout param1CoordinatorLayout, T param1T) {
      SavedState savedState;
      Parcelable parcelable = super.onSaveInstanceState(param1CoordinatorLayout, (View)param1T);
      int j = getTopAndBottomOffset();
      byte b = 0;
      int i = param1T.getChildCount();
      while (b < i) {
        View view = param1T.getChildAt(b);
        int k = view.getBottom() + j;
        if (view.getTop() + j <= 0 && k >= 0) {
          boolean bool;
          savedState = new SavedState(parcelable);
          savedState.firstVisibleChildIndex = b;
          if (k == ViewCompat.getMinimumHeight(view) + param1T.getTopInset()) {
            bool = true;
          } else {
            bool = false;
          } 
          savedState.firstVisibleChildAtMinimumHeight = bool;
          savedState.firstVisibleChildPercentageShown = k / view.getHeight();
          return (Parcelable)savedState;
        } 
        b++;
      } 
      return (Parcelable)savedState;
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout param1CoordinatorLayout, T param1T, View param1View1, View param1View2, int param1Int1, int param1Int2) {
      boolean bool;
      if ((param1Int1 & 0x2) != 0 && (param1T.isLiftOnScroll() || canScrollChildren(param1CoordinatorLayout, param1T, param1View1))) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        ValueAnimator valueAnimator = this.offsetAnimator;
        if (valueAnimator != null)
          valueAnimator.cancel(); 
      } 
      this.lastNestedScrollingChildRef = null;
      this.lastStartedType = param1Int2;
      return bool;
    }
    
    public void onStopNestedScroll(CoordinatorLayout param1CoordinatorLayout, T param1T, View param1View, int param1Int) {
      if (this.lastStartedType == 0 || param1Int == 1) {
        snapToChildIfNeeded(param1CoordinatorLayout, param1T);
        if (param1T.isLiftOnScroll())
          param1T.setLiftedState(param1T.shouldLift(param1View)); 
      } 
      this.lastNestedScrollingChildRef = new WeakReference<>(param1View);
    }
    
    public void setDragCallback(BaseDragCallback param1BaseDragCallback) {
      this.onDragCallback = param1BaseDragCallback;
    }
    
    int setHeaderTopBottomOffset(CoordinatorLayout param1CoordinatorLayout, T param1T, int param1Int1, int param1Int2, int param1Int3) {
      int i = getTopBottomOffsetForScrollingSibling();
      boolean bool = false;
      if (param1Int2 != 0 && i >= param1Int2 && i <= param1Int3) {
        param1Int2 = MathUtils.clamp(param1Int1, param1Int2, param1Int3);
        param1Int1 = bool;
        if (i != param1Int2) {
          if (param1T.hasChildWithInterpolator()) {
            param1Int1 = interpolateOffset(param1T, param1Int2);
          } else {
            param1Int1 = param1Int2;
          } 
          boolean bool1 = setTopAndBottomOffset(param1Int1);
          param1Int3 = i - param1Int2;
          this.offsetDelta = param1Int2 - param1Int1;
          if (!bool1 && param1T.hasChildWithInterpolator())
            param1CoordinatorLayout.dispatchDependentViewsChanged((View)param1T); 
          param1T.onOffsetChanged(getTopAndBottomOffset());
          if (param1Int2 < i) {
            param1Int1 = -1;
          } else {
            param1Int1 = 1;
          } 
          updateAppBarLayoutDrawableState(param1CoordinatorLayout, param1T, param1Int2, param1Int1, false);
          param1Int1 = param1Int3;
        } 
      } else {
        this.offsetDelta = 0;
        param1Int1 = bool;
      } 
      updateAccessibilityActions(param1CoordinatorLayout, param1T);
      return param1Int1;
    }
    
    public static abstract class BaseDragCallback<T extends AppBarLayout> {
      public abstract boolean canDrag(T param2T);
    }
    
    protected static class SavedState extends AbsSavedState {
      public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
          public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param3Parcel) {
            return new AppBarLayout.BaseBehavior.SavedState(param3Parcel, null);
          }
          
          public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
            return new AppBarLayout.BaseBehavior.SavedState(param3Parcel, param3ClassLoader);
          }
          
          public AppBarLayout.BaseBehavior.SavedState[] newArray(int param3Int) {
            return new AppBarLayout.BaseBehavior.SavedState[param3Int];
          }
        };
      
      boolean firstVisibleChildAtMinimumHeight;
      
      int firstVisibleChildIndex;
      
      float firstVisibleChildPercentageShown;
      
      public SavedState(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        super(param2Parcel, param2ClassLoader);
        boolean bool;
        this.firstVisibleChildIndex = param2Parcel.readInt();
        this.firstVisibleChildPercentageShown = param2Parcel.readFloat();
        if (param2Parcel.readByte() != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.firstVisibleChildAtMinimumHeight = bool;
      }
      
      public SavedState(Parcelable param2Parcelable) {
        super(param2Parcelable);
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        super.writeToParcel(param2Parcel, param2Int);
        param2Parcel.writeInt(this.firstVisibleChildIndex);
        param2Parcel.writeFloat(this.firstVisibleChildPercentageShown);
        param2Parcel.writeByte((byte)this.firstVisibleChildAtMinimumHeight);
      }
    }
    
    static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
      public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param2Parcel) {
        return new AppBarLayout.BaseBehavior.SavedState(param2Parcel, null);
      }
      
      public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        return new AppBarLayout.BaseBehavior.SavedState(param2Parcel, param2ClassLoader);
      }
      
      public AppBarLayout.BaseBehavior.SavedState[] newArray(int param2Int) {
        return new AppBarLayout.BaseBehavior.SavedState[param2Int];
      }
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    final AppBarLayout.BaseBehavior this$0;
    
    final AppBarLayout val$child;
    
    final CoordinatorLayout val$coordinatorLayout;
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      this.this$0.setHeaderTopBottomOffset(coordinatorLayout, child, ((Integer)param1ValueAnimator.getAnimatedValue()).intValue());
    }
  }
  
  class null implements AccessibilityViewCommand {
    final AppBarLayout.BaseBehavior this$0;
    
    final AppBarLayout val$appBarLayout;
    
    final CoordinatorLayout val$coordinatorLayout;
    
    final int val$dy;
    
    final View val$scrollingView;
    
    public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
      this.this$0.onNestedPreScroll(coordinatorLayout, appBarLayout, scrollingView, 0, dy, new int[] { 0, 0 }, 1);
      return true;
    }
  }
  
  class null implements AccessibilityViewCommand {
    final AppBarLayout.BaseBehavior this$0;
    
    final AppBarLayout val$appBarLayout;
    
    final boolean val$expand;
    
    public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
      appBarLayout.setExpanded(expand);
      return true;
    }
  }
  
  public static abstract class BaseDragCallback<T extends AppBarLayout> {
    public abstract boolean canDrag(T param1T);
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param3Parcel) {
          return new AppBarLayout.BaseBehavior.SavedState(param3Parcel, null);
        }
        
        public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
          return new AppBarLayout.BaseBehavior.SavedState(param3Parcel, param3ClassLoader);
        }
        
        public AppBarLayout.BaseBehavior.SavedState[] newArray(int param3Int) {
          return new AppBarLayout.BaseBehavior.SavedState[param3Int];
        }
      };
    
    boolean firstVisibleChildAtMinimumHeight;
    
    int firstVisibleChildIndex;
    
    float firstVisibleChildPercentageShown;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.firstVisibleChildIndex = param1Parcel.readInt();
      this.firstVisibleChildPercentageShown = param1Parcel.readFloat();
      if (param1Parcel.readByte() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.firstVisibleChildAtMinimumHeight = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.firstVisibleChildIndex);
      param1Parcel.writeFloat(this.firstVisibleChildPercentageShown);
      param1Parcel.writeByte((byte)this.firstVisibleChildAtMinimumHeight);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<BaseBehavior.SavedState> {
    public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param1Parcel) {
      return new AppBarLayout.BaseBehavior.SavedState(param1Parcel, null);
    }
    
    public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new AppBarLayout.BaseBehavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public AppBarLayout.BaseBehavior.SavedState[] newArray(int param1Int) {
      return new AppBarLayout.BaseBehavior.SavedState[param1Int];
    }
  }
  
  public static interface BaseOnOffsetChangedListener<T extends AppBarLayout> {
    void onOffsetChanged(T param1T, int param1Int);
  }
  
  public static class Behavior extends BaseBehavior<AppBarLayout> {
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public static abstract class DragCallback extends AppBarLayout.BaseBehavior.BaseDragCallback<AppBarLayout> {}
  }
  
  public static abstract class DragCallback extends BaseBehavior.BaseDragCallback<AppBarLayout> {}
  
  public static class LayoutParams extends LinearLayout.LayoutParams {
    static final int COLLAPSIBLE_FLAGS = 10;
    
    static final int FLAG_QUICK_RETURN = 5;
    
    static final int FLAG_SNAP = 17;
    
    public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
    
    public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
    
    public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
    
    public static final int SCROLL_FLAG_NO_SCROLL = 0;
    
    public static final int SCROLL_FLAG_SCROLL = 1;
    
    public static final int SCROLL_FLAG_SNAP = 16;
    
    public static final int SCROLL_FLAG_SNAP_MARGINS = 32;
    
    int scrollFlags = 1;
    
    Interpolator scrollInterpolator;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2, param1Float);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.AppBarLayout_Layout);
      this.scrollFlags = typedArray.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
      if (typedArray.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator))
        this.scrollInterpolator = AnimationUtils.loadInterpolator(param1Context, typedArray.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0)); 
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LinearLayout.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.scrollFlags = param1LayoutParams.scrollFlags;
      this.scrollInterpolator = param1LayoutParams.scrollInterpolator;
    }
    
    public int getScrollFlags() {
      return this.scrollFlags;
    }
    
    public Interpolator getScrollInterpolator() {
      return this.scrollInterpolator;
    }
    
    boolean isCollapsible() {
      int i = this.scrollFlags;
      boolean bool = true;
      if ((i & 0x1) != 1 || (i & 0xA) == 0)
        bool = false; 
      return bool;
    }
    
    public void setScrollFlags(int param1Int) {
      this.scrollFlags = param1Int;
    }
    
    public void setScrollInterpolator(Interpolator param1Interpolator) {
      this.scrollInterpolator = param1Interpolator;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ScrollFlags {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollFlags {}
  
  public static interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<AppBarLayout> {
    void onOffsetChanged(AppBarLayout param1AppBarLayout, int param1Int);
  }
  
  public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
    public ScrollingViewBehavior() {}
    
    public ScrollingViewBehavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ScrollingViewBehavior_Layout);
      setOverlayTop(typedArray.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
      typedArray.recycle();
    }
    
    private static int getAppBarLayoutOffset(AppBarLayout param1AppBarLayout) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)param1AppBarLayout.getLayoutParams()).getBehavior();
      return (behavior instanceof AppBarLayout.BaseBehavior) ? ((AppBarLayout.BaseBehavior)behavior).getTopBottomOffsetForScrollingSibling() : 0;
    }
    
    private void offsetChildAsNeeded(View param1View1, View param1View2) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)param1View2.getLayoutParams()).getBehavior();
      if (behavior instanceof AppBarLayout.BaseBehavior) {
        behavior = behavior;
        ViewCompat.offsetTopAndBottom(param1View1, param1View2.getBottom() - param1View1.getTop() + ((AppBarLayout.BaseBehavior)behavior).offsetDelta + getVerticalLayoutGap() - getOverlapPixelsForOffset(param1View2));
      } 
    }
    
    private void updateLiftedStateIfNeeded(View param1View1, View param1View2) {
      if (param1View2 instanceof AppBarLayout) {
        AppBarLayout appBarLayout = (AppBarLayout)param1View2;
        if (appBarLayout.isLiftOnScroll())
          appBarLayout.setLiftedState(appBarLayout.shouldLift(param1View1)); 
      } 
    }
    
    AppBarLayout findFirstDependency(List<View> param1List) {
      byte b = 0;
      int i = param1List.size();
      while (b < i) {
        View view = param1List.get(b);
        if (view instanceof AppBarLayout)
          return (AppBarLayout)view; 
        b++;
      } 
      return null;
    }
    
    float getOverlapRatioForOffset(View param1View) {
      if (param1View instanceof AppBarLayout) {
        AppBarLayout appBarLayout = (AppBarLayout)param1View;
        int k = appBarLayout.getTotalScrollRange();
        int j = appBarLayout.getDownNestedPreScrollRange();
        int i = getAppBarLayoutOffset(appBarLayout);
        if (j != 0 && k + i <= j)
          return 0.0F; 
        j = k - j;
        if (j != 0)
          return i / j + 1.0F; 
      } 
      return 0.0F;
    }
    
    int getScrollRange(View param1View) {
      return (param1View instanceof AppBarLayout) ? ((AppBarLayout)param1View).getTotalScrollRange() : super.getScrollRange(param1View);
    }
    
    public boolean layoutDependsOn(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      return param1View2 instanceof AppBarLayout;
    }
    
    public boolean onDependentViewChanged(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      offsetChildAsNeeded(param1View1, param1View2);
      updateLiftedStateIfNeeded(param1View1, param1View2);
      return false;
    }
    
    public void onDependentViewRemoved(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      if (param1View2 instanceof AppBarLayout) {
        ViewCompat.removeAccessibilityAction((View)param1CoordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
        ViewCompat.removeAccessibilityAction((View)param1CoordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
      } 
    }
    
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout param1CoordinatorLayout, View param1View, Rect param1Rect, boolean param1Boolean) {
      AppBarLayout appBarLayout = findFirstDependency(param1CoordinatorLayout.getDependencies(param1View));
      if (appBarLayout != null) {
        param1Rect.offset(param1View.getLeft(), param1View.getTop());
        Rect rect = this.tempRect1;
        rect.set(0, 0, param1CoordinatorLayout.getWidth(), param1CoordinatorLayout.getHeight());
        if (!rect.contains(param1Rect)) {
          appBarLayout.setExpanded(false, param1Boolean ^ true);
          return true;
        } 
      } 
      return false;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\AppBarLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */