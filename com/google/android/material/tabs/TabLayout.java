package com.google.android.material.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.DecorView;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@DecorView
public class TabLayout extends HorizontalScrollView {
  private static final int ANIMATION_DURATION = 300;
  
  static final int DEFAULT_GAP_TEXT_ICON = 8;
  
  private static final int DEFAULT_HEIGHT = 48;
  
  private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
  
  private static final int DEF_STYLE_RES = R.style.Widget_Design_TabLayout;
  
  static final int FIXED_WRAP_GUTTER_MIN = 16;
  
  public static final int GRAVITY_CENTER = 1;
  
  public static final int GRAVITY_FILL = 0;
  
  public static final int GRAVITY_START = 2;
  
  public static final int INDICATOR_ANIMATION_MODE_ELASTIC = 1;
  
  public static final int INDICATOR_ANIMATION_MODE_LINEAR = 0;
  
  public static final int INDICATOR_GRAVITY_BOTTOM = 0;
  
  public static final int INDICATOR_GRAVITY_CENTER = 1;
  
  public static final int INDICATOR_GRAVITY_STRETCH = 3;
  
  public static final int INDICATOR_GRAVITY_TOP = 2;
  
  private static final int INVALID_WIDTH = -1;
  
  private static final String LOG_TAG = "TabLayout";
  
  public static final int MODE_AUTO = 2;
  
  public static final int MODE_FIXED = 1;
  
  public static final int MODE_SCROLLABLE = 0;
  
  public static final int TAB_LABEL_VISIBILITY_LABELED = 1;
  
  public static final int TAB_LABEL_VISIBILITY_UNLABELED = 0;
  
  private static final int TAB_MIN_WIDTH_MARGIN = 56;
  
  private static final Pools.Pool<Tab> tabPool = (Pools.Pool<Tab>)new Pools.SynchronizedPool(16);
  
  private AdapterChangeListener adapterChangeListener;
  
  private int contentInsetStart;
  
  private BaseOnTabSelectedListener currentVpSelectedListener;
  
  boolean inlineLabel;
  
  int mode;
  
  private TabLayoutOnPageChangeListener pageChangeListener;
  
  private PagerAdapter pagerAdapter;
  
  private DataSetObserver pagerAdapterObserver;
  
  private final int requestedTabMaxWidth;
  
  private final int requestedTabMinWidth;
  
  private ValueAnimator scrollAnimator;
  
  private final int scrollableTabMinWidth;
  
  private BaseOnTabSelectedListener selectedListener;
  
  private final ArrayList<BaseOnTabSelectedListener> selectedListeners = new ArrayList<>();
  
  private Tab selectedTab;
  
  private boolean setupViewPagerImplicitly;
  
  final SlidingTabIndicator slidingTabIndicator;
  
  final int tabBackgroundResId;
  
  int tabGravity;
  
  ColorStateList tabIconTint;
  
  PorterDuff.Mode tabIconTintMode;
  
  int tabIndicatorAnimationDuration;
  
  int tabIndicatorAnimationMode;
  
  boolean tabIndicatorFullWidth;
  
  int tabIndicatorGravity;
  
  private TabIndicatorInterpolator tabIndicatorInterpolator;
  
  int tabMaxWidth = Integer.MAX_VALUE;
  
  int tabPaddingBottom;
  
  int tabPaddingEnd;
  
  int tabPaddingStart;
  
  int tabPaddingTop;
  
  ColorStateList tabRippleColorStateList;
  
  Drawable tabSelectedIndicator = (Drawable)new GradientDrawable();
  
  private int tabSelectedIndicatorColor = 0;
  
  int tabTextAppearance;
  
  ColorStateList tabTextColors;
  
  float tabTextMultiLineSize;
  
  float tabTextSize;
  
  private final Pools.Pool<TabView> tabViewPool = (Pools.Pool<TabView>)new Pools.SimplePool(12);
  
  private final ArrayList<Tab> tabs = new ArrayList<>();
  
  boolean unboundedRipple;
  
  ViewPager viewPager;
  
  public TabLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.tabStyle);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    setHorizontalScrollBarEnabled(false);
    SlidingTabIndicator slidingTabIndicator = new SlidingTabIndicator(paramContext);
    this.slidingTabIndicator = slidingTabIndicator;
    super.addView((View)slidingTabIndicator, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
    TypedArray typedArray1 = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.TabLayout, paramInt, i, new int[] { R.styleable.TabLayout_tabTextAppearance });
    if (getBackground() instanceof ColorDrawable) {
      ColorDrawable colorDrawable = (ColorDrawable)getBackground();
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(colorDrawable.getColor()));
      materialShapeDrawable.initializeElevationOverlay(paramContext);
      materialShapeDrawable.setElevation(ViewCompat.getElevation((View)this));
      ViewCompat.setBackground((View)this, (Drawable)materialShapeDrawable);
    } 
    setSelectedTabIndicator(MaterialResources.getDrawable(paramContext, typedArray1, R.styleable.TabLayout_tabIndicator));
    setSelectedTabIndicatorColor(typedArray1.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
    slidingTabIndicator.setSelectedIndicatorHeight(typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
    setSelectedTabIndicatorGravity(typedArray1.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
    setTabIndicatorFullWidth(typedArray1.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
    setTabIndicatorAnimationMode(typedArray1.getInt(R.styleable.TabLayout_tabIndicatorAnimationMode, 0));
    paramInt = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
    this.tabPaddingBottom = paramInt;
    this.tabPaddingEnd = paramInt;
    this.tabPaddingTop = paramInt;
    this.tabPaddingStart = paramInt;
    this.tabPaddingStart = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
    this.tabPaddingTop = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
    this.tabPaddingEnd = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
    this.tabPaddingBottom = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
    paramInt = typedArray1.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
    this.tabTextAppearance = paramInt;
    TypedArray typedArray2 = paramContext.obtainStyledAttributes(paramInt, R.styleable.TextAppearance);
    try {
      this.tabTextSize = typedArray2.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
      this.tabTextColors = MaterialResources.getColorStateList(paramContext, typedArray2, R.styleable.TextAppearance_android_textColor);
      typedArray2.recycle();
      if (typedArray1.hasValue(R.styleable.TabLayout_tabTextColor))
        this.tabTextColors = MaterialResources.getColorStateList(paramContext, typedArray1, R.styleable.TabLayout_tabTextColor); 
      if (typedArray1.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
        paramInt = typedArray1.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
        this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), paramInt);
      } 
      this.tabIconTint = MaterialResources.getColorStateList(paramContext, typedArray1, R.styleable.TabLayout_tabIconTint);
      this.tabIconTintMode = ViewUtils.parseTintMode(typedArray1.getInt(R.styleable.TabLayout_tabIconTintMode, -1), null);
      this.tabRippleColorStateList = MaterialResources.getColorStateList(paramContext, typedArray1, R.styleable.TabLayout_tabRippleColor);
      this.tabIndicatorAnimationDuration = typedArray1.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
      this.requestedTabMinWidth = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
      this.requestedTabMaxWidth = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
      this.tabBackgroundResId = typedArray1.getResourceId(R.styleable.TabLayout_tabBackground, 0);
      this.contentInsetStart = typedArray1.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
      this.mode = typedArray1.getInt(R.styleable.TabLayout_tabMode, 1);
      this.tabGravity = typedArray1.getInt(R.styleable.TabLayout_tabGravity, 0);
      this.inlineLabel = typedArray1.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
      this.unboundedRipple = typedArray1.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
      typedArray1.recycle();
      Resources resources = getResources();
      this.tabTextMultiLineSize = resources.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
      this.scrollableTabMinWidth = resources.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
      return;
    } finally {
      typedArray2.recycle();
    } 
  }
  
  private void addTabFromItemView(TabItem paramTabItem) {
    Tab tab = newTab();
    if (paramTabItem.text != null)
      tab.setText(paramTabItem.text); 
    if (paramTabItem.icon != null)
      tab.setIcon(paramTabItem.icon); 
    if (paramTabItem.customLayout != 0)
      tab.setCustomView(paramTabItem.customLayout); 
    if (!TextUtils.isEmpty(paramTabItem.getContentDescription()))
      tab.setContentDescription(paramTabItem.getContentDescription()); 
    addTab(tab);
  }
  
  private void addTabView(Tab paramTab) {
    TabView tabView = paramTab.view;
    tabView.setSelected(false);
    tabView.setActivated(false);
    this.slidingTabIndicator.addView((View)tabView, paramTab.getPosition(), (ViewGroup.LayoutParams)createLayoutParamsForTabs());
  }
  
  private void addViewInternal(View paramView) {
    if (paramView instanceof TabItem) {
      addTabFromItemView((TabItem)paramView);
      return;
    } 
    throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
  }
  
  private void animateToTab(int paramInt) {
    if (paramInt == -1)
      return; 
    if (getWindowToken() == null || !ViewCompat.isLaidOut((View)this) || this.slidingTabIndicator.childrenNeedLayout()) {
      setScrollPosition(paramInt, 0.0F, true);
      return;
    } 
    int i = getScrollX();
    int j = calculateScrollXForTab(paramInt, 0.0F);
    if (i != j) {
      ensureScrollAnimator();
      this.scrollAnimator.setIntValues(new int[] { i, j });
      this.scrollAnimator.start();
    } 
    this.slidingTabIndicator.animateIndicatorToPosition(paramInt, this.tabIndicatorAnimationDuration);
  }
  
  private void applyGravityForModeScrollable(int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 1:
        this.slidingTabIndicator.setGravity(1);
      case 0:
        Log.w("TabLayout", "MODE_SCROLLABLE + GRAVITY_FILL is not supported, GRAVITY_START will be used instead");
        break;
      case 2:
        break;
    } 
    this.slidingTabIndicator.setGravity(8388611);
  }
  
  private void applyModeAndGravity() {
    int i = 0;
    int j = this.mode;
    if (j == 0 || j == 2)
      i = Math.max(0, this.contentInsetStart - this.tabPaddingStart); 
    ViewCompat.setPaddingRelative((View)this.slidingTabIndicator, i, 0, 0, 0);
    switch (this.mode) {
      case 1:
      case 2:
        if (this.tabGravity == 2)
          Log.w("TabLayout", "GRAVITY_START is not supported with the current tab mode, GRAVITY_CENTER will be used instead"); 
        this.slidingTabIndicator.setGravity(1);
        break;
      case 0:
        applyGravityForModeScrollable(this.tabGravity);
        break;
    } 
    updateTabViews(true);
  }
  
  private int calculateScrollXForTab(int paramInt, float paramFloat) {
    int j = this.mode;
    int i = 0;
    if (j == 0 || j == 2) {
      View view1;
      View view2 = this.slidingTabIndicator.getChildAt(paramInt);
      if (paramInt + 1 < this.slidingTabIndicator.getChildCount()) {
        view1 = this.slidingTabIndicator.getChildAt(paramInt + 1);
      } else {
        view1 = null;
      } 
      if (view2 != null) {
        paramInt = view2.getWidth();
      } else {
        paramInt = 0;
      } 
      if (view1 != null)
        i = view1.getWidth(); 
      j = view2.getLeft() + paramInt / 2 - getWidth() / 2;
      paramInt = (int)((paramInt + i) * 0.5F * paramFloat);
      if (ViewCompat.getLayoutDirection((View)this) == 0) {
        paramInt = j + paramInt;
      } else {
        paramInt = j - paramInt;
      } 
      return paramInt;
    } 
    return 0;
  }
  
  private void configureTab(Tab paramTab, int paramInt) {
    paramTab.setPosition(paramInt);
    this.tabs.add(paramInt, paramTab);
    int i = this.tabs.size();
    while (++paramInt < i) {
      ((Tab)this.tabs.get(paramInt)).setPosition(paramInt);
      paramInt++;
    } 
  }
  
  private static ColorStateList createColorStateList(int paramInt1, int paramInt2) {
    int[][] arrayOfInt1 = new int[2][];
    int[] arrayOfInt = new int[2];
    arrayOfInt1[0] = SELECTED_STATE_SET;
    arrayOfInt[0] = paramInt2;
    paramInt2 = 0 + 1;
    arrayOfInt1[paramInt2] = EMPTY_STATE_SET;
    arrayOfInt[paramInt2] = paramInt1;
    return new ColorStateList(arrayOfInt1, arrayOfInt);
  }
  
  private LinearLayout.LayoutParams createLayoutParamsForTabs() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
    updateTabViewLayoutParams(layoutParams);
    return layoutParams;
  }
  
  private TabView createTabView(Tab paramTab) {
    TabView tabView;
    Pools.Pool<TabView> pool1 = this.tabViewPool;
    if (pool1 != null) {
      TabView tabView1 = (TabView)pool1.acquire();
    } else {
      pool1 = null;
    } 
    Pools.Pool<TabView> pool2 = pool1;
    if (pool1 == null)
      tabView = new TabView(getContext()); 
    tabView.setTab(paramTab);
    tabView.setFocusable(true);
    tabView.setMinimumWidth(getTabMinWidth());
    if (TextUtils.isEmpty(paramTab.contentDesc)) {
      tabView.setContentDescription(paramTab.text);
    } else {
      tabView.setContentDescription(paramTab.contentDesc);
    } 
    return tabView;
  }
  
  private void dispatchTabReselected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabReselected(paramTab); 
  }
  
  private void dispatchTabSelected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabSelected(paramTab); 
  }
  
  private void dispatchTabUnselected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabUnselected(paramTab); 
  }
  
  private void ensureScrollAnimator() {
    if (this.scrollAnimator == null) {
      ValueAnimator valueAnimator = new ValueAnimator();
      this.scrollAnimator = valueAnimator;
      valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.scrollAnimator.setDuration(this.tabIndicatorAnimationDuration);
      this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final TabLayout this$0;
            
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
              TabLayout.this.scrollTo(((Integer)param1ValueAnimator.getAnimatedValue()).intValue(), 0);
            }
          });
    } 
  }
  
  private int getDefaultHeight() {
    byte b1;
    byte b3 = 0;
    byte b2 = 0;
    int i = this.tabs.size();
    while (true) {
      b1 = b3;
      if (b2 < i) {
        Tab tab = this.tabs.get(b2);
        if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
          b1 = 1;
          break;
        } 
        b2++;
        continue;
      } 
      break;
    } 
    if (b1 && !this.inlineLabel) {
      b1 = 72;
    } else {
      b1 = 48;
    } 
    return b1;
  }
  
  private int getTabMinWidth() {
    null = this.requestedTabMinWidth;
    if (null != -1)
      return null; 
    null = this.mode;
    return (null == 0 || null == 2) ? this.scrollableTabMinWidth : 0;
  }
  
  private int getTabScrollRange() {
    return Math.max(0, this.slidingTabIndicator.getWidth() - getWidth() - getPaddingLeft() - getPaddingRight());
  }
  
  private void removeTabViewAt(int paramInt) {
    TabView tabView = (TabView)this.slidingTabIndicator.getChildAt(paramInt);
    this.slidingTabIndicator.removeViewAt(paramInt);
    if (tabView != null) {
      tabView.reset();
      this.tabViewPool.release(tabView);
    } 
    requestLayout();
  }
  
  private void setSelectedTabView(int paramInt) {
    int i = this.slidingTabIndicator.getChildCount();
    if (paramInt < i)
      for (byte b = 0; b < i; b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        boolean bool2 = false;
        if (b == paramInt) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        view.setSelected(bool1);
        boolean bool1 = bool2;
        if (b == paramInt)
          bool1 = true; 
        view.setActivated(bool1);
      }  
  }
  
  private void setupWithViewPager(ViewPager paramViewPager, boolean paramBoolean1, boolean paramBoolean2) {
    ViewPager viewPager = this.viewPager;
    if (viewPager != null) {
      TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
      if (tabLayoutOnPageChangeListener != null)
        viewPager.removeOnPageChangeListener(tabLayoutOnPageChangeListener); 
      AdapterChangeListener adapterChangeListener = this.adapterChangeListener;
      if (adapterChangeListener != null)
        this.viewPager.removeOnAdapterChangeListener(adapterChangeListener); 
    } 
    BaseOnTabSelectedListener baseOnTabSelectedListener = this.currentVpSelectedListener;
    if (baseOnTabSelectedListener != null) {
      removeOnTabSelectedListener(baseOnTabSelectedListener);
      this.currentVpSelectedListener = null;
    } 
    if (paramViewPager != null) {
      this.viewPager = paramViewPager;
      if (this.pageChangeListener == null)
        this.pageChangeListener = new TabLayoutOnPageChangeListener(this); 
      this.pageChangeListener.reset();
      paramViewPager.addOnPageChangeListener(this.pageChangeListener);
      baseOnTabSelectedListener = new ViewPagerOnTabSelectedListener(paramViewPager);
      this.currentVpSelectedListener = baseOnTabSelectedListener;
      addOnTabSelectedListener(baseOnTabSelectedListener);
      PagerAdapter pagerAdapter = paramViewPager.getAdapter();
      if (pagerAdapter != null)
        setPagerAdapter(pagerAdapter, paramBoolean1); 
      if (this.adapterChangeListener == null)
        this.adapterChangeListener = new AdapterChangeListener(); 
      this.adapterChangeListener.setAutoRefresh(paramBoolean1);
      paramViewPager.addOnAdapterChangeListener(this.adapterChangeListener);
      setScrollPosition(paramViewPager.getCurrentItem(), 0.0F, true);
    } else {
      this.viewPager = null;
      setPagerAdapter((PagerAdapter)null, false);
    } 
    this.setupViewPagerImplicitly = paramBoolean2;
  }
  
  private void updateAllTabs() {
    byte b = 0;
    int i = this.tabs.size();
    while (b < i) {
      ((Tab)this.tabs.get(b)).updateView();
      b++;
    } 
  }
  
  private void updateTabViewLayoutParams(LinearLayout.LayoutParams paramLayoutParams) {
    if (this.mode == 1 && this.tabGravity == 0) {
      paramLayoutParams.width = 0;
      paramLayoutParams.weight = 1.0F;
    } else {
      paramLayoutParams.width = -2;
      paramLayoutParams.weight = 0.0F;
    } 
  }
  
  @Deprecated
  public void addOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    if (!this.selectedListeners.contains(paramBaseOnTabSelectedListener))
      this.selectedListeners.add(paramBaseOnTabSelectedListener); 
  }
  
  public void addOnTabSelectedListener(OnTabSelectedListener paramOnTabSelectedListener) {
    addOnTabSelectedListener(paramOnTabSelectedListener);
  }
  
  public void addTab(Tab paramTab) {
    addTab(paramTab, this.tabs.isEmpty());
  }
  
  public void addTab(Tab paramTab, int paramInt) {
    addTab(paramTab, paramInt, this.tabs.isEmpty());
  }
  
  public void addTab(Tab paramTab, int paramInt, boolean paramBoolean) {
    if (paramTab.parent == this) {
      configureTab(paramTab, paramInt);
      addTabView(paramTab);
      if (paramBoolean)
        paramTab.select(); 
      return;
    } 
    throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
  }
  
  public void addTab(Tab paramTab, boolean paramBoolean) {
    addTab(paramTab, this.tabs.size(), paramBoolean);
  }
  
  public void addView(View paramView) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void clearOnTabSelectedListeners() {
    this.selectedListeners.clear();
  }
  
  protected Tab createTabFromPool() {
    Tab tab2 = (Tab)tabPool.acquire();
    Tab tab1 = tab2;
    if (tab2 == null)
      tab1 = new Tab(); 
    return tab1;
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return generateDefaultLayoutParams();
  }
  
  public int getSelectedTabPosition() {
    byte b;
    Tab tab = this.selectedTab;
    if (tab != null) {
      b = tab.getPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  public Tab getTabAt(int paramInt) {
    return (paramInt < 0 || paramInt >= getTabCount()) ? null : this.tabs.get(paramInt);
  }
  
  public int getTabCount() {
    return this.tabs.size();
  }
  
  public int getTabGravity() {
    return this.tabGravity;
  }
  
  public ColorStateList getTabIconTint() {
    return this.tabIconTint;
  }
  
  public int getTabIndicatorAnimationMode() {
    return this.tabIndicatorAnimationMode;
  }
  
  public int getTabIndicatorGravity() {
    return this.tabIndicatorGravity;
  }
  
  int getTabMaxWidth() {
    return this.tabMaxWidth;
  }
  
  public int getTabMode() {
    return this.mode;
  }
  
  public ColorStateList getTabRippleColor() {
    return this.tabRippleColorStateList;
  }
  
  public Drawable getTabSelectedIndicator() {
    return this.tabSelectedIndicator;
  }
  
  public ColorStateList getTabTextColors() {
    return this.tabTextColors;
  }
  
  public boolean hasUnboundedRipple() {
    return this.unboundedRipple;
  }
  
  public boolean isInlineLabel() {
    return this.inlineLabel;
  }
  
  public boolean isTabIndicatorFullWidth() {
    return this.tabIndicatorFullWidth;
  }
  
  public Tab newTab() {
    Tab tab = createTabFromPool();
    tab.parent = this;
    tab.view = createTabView(tab);
    if (tab.id != -1)
      tab.view.setId(tab.id); 
    return tab;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this);
    if (this.viewPager == null) {
      ViewParent viewParent = getParent();
      if (viewParent instanceof ViewPager)
        setupWithViewPager((ViewPager)viewParent, true, true); 
    } 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.setupViewPagerImplicitly) {
      setupWithViewPager((ViewPager)null);
      this.setupViewPagerImplicitly = false;
    } 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
      View view = this.slidingTabIndicator.getChildAt(b);
      if (view instanceof TabView)
        ((TabView)view).drawBackground(paramCanvas); 
    } 
    super.onDraw(paramCanvas);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo);
    accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getTabCount(), false, 1));
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = Math.round(ViewUtils.dpToPx(getContext(), getDefaultHeight()));
    int i = View.MeasureSpec.getMode(paramInt2);
    boolean bool2 = false;
    boolean bool1 = false;
    switch (i) {
      default:
        i = paramInt2;
        break;
      case 0:
        i = View.MeasureSpec.makeMeasureSpec(getPaddingTop() + j + getPaddingBottom(), 1073741824);
        break;
      case -2147483648:
        i = paramInt2;
        if (getChildCount() == 1) {
          i = paramInt2;
          if (View.MeasureSpec.getSize(paramInt2) >= j) {
            getChildAt(0).setMinimumHeight(j);
            i = paramInt2;
          } 
        } 
        break;
    } 
    j = View.MeasureSpec.getSize(paramInt1);
    if (View.MeasureSpec.getMode(paramInt1) != 0) {
      paramInt2 = this.requestedTabMaxWidth;
      if (paramInt2 <= 0)
        paramInt2 = (int)(j - ViewUtils.dpToPx(getContext(), 56)); 
      this.tabMaxWidth = paramInt2;
    } 
    super.onMeasure(paramInt1, i);
    if (getChildCount() == 1) {
      View view = getChildAt(0);
      paramInt1 = 0;
      switch (this.mode) {
        case 1:
          paramInt1 = bool1;
          if (view.getMeasuredWidth() != getMeasuredWidth())
            paramInt1 = 1; 
          break;
        case 0:
        case 2:
          paramInt1 = bool2;
          if (view.getMeasuredWidth() < getMeasuredWidth())
            paramInt1 = 1; 
          break;
      } 
      if (paramInt1 != 0) {
        paramInt1 = getChildMeasureSpec(i, getPaddingTop() + getPaddingBottom(), (view.getLayoutParams()).height);
        view.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), paramInt1);
      } 
    } 
  }
  
  void populateFromPagerAdapter() {
    removeAllTabs();
    PagerAdapter pagerAdapter = this.pagerAdapter;
    if (pagerAdapter != null) {
      int j = pagerAdapter.getCount();
      int i;
      for (i = 0; i < j; i++)
        addTab(newTab().setText(this.pagerAdapter.getPageTitle(i)), false); 
      ViewPager viewPager = this.viewPager;
      if (viewPager != null && j > 0) {
        i = viewPager.getCurrentItem();
        if (i != getSelectedTabPosition() && i < getTabCount())
          selectTab(getTabAt(i)); 
      } 
    } 
  }
  
  protected boolean releaseFromTabPool(Tab paramTab) {
    return tabPool.release(paramTab);
  }
  
  public void removeAllTabs() {
    for (int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; i--)
      removeTabViewAt(i); 
    Iterator<Tab> iterator = this.tabs.iterator();
    while (iterator.hasNext()) {
      Tab tab = iterator.next();
      iterator.remove();
      tab.reset();
      releaseFromTabPool(tab);
    } 
    this.selectedTab = null;
  }
  
  @Deprecated
  public void removeOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    this.selectedListeners.remove(paramBaseOnTabSelectedListener);
  }
  
  public void removeOnTabSelectedListener(OnTabSelectedListener paramOnTabSelectedListener) {
    removeOnTabSelectedListener(paramOnTabSelectedListener);
  }
  
  public void removeTab(Tab paramTab) {
    if (paramTab.parent == this) {
      removeTabAt(paramTab.getPosition());
      return;
    } 
    throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
  }
  
  public void removeTabAt(int paramInt) {
    int i;
    Tab tab = this.selectedTab;
    if (tab != null) {
      i = tab.getPosition();
    } else {
      i = 0;
    } 
    removeTabViewAt(paramInt);
    tab = this.tabs.remove(paramInt);
    if (tab != null) {
      tab.reset();
      releaseFromTabPool(tab);
    } 
    int k = this.tabs.size();
    for (int j = paramInt; j < k; j++)
      ((Tab)this.tabs.get(j)).setPosition(j); 
    if (i == paramInt) {
      if (this.tabs.isEmpty()) {
        tab = null;
      } else {
        tab = this.tabs.get(Math.max(0, paramInt - 1));
      } 
      selectTab(tab);
    } 
  }
  
  public void selectTab(Tab paramTab) {
    selectTab(paramTab, true);
  }
  
  public void selectTab(Tab paramTab, boolean paramBoolean) {
    Tab tab = this.selectedTab;
    if (tab == paramTab) {
      if (tab != null) {
        dispatchTabReselected(paramTab);
        animateToTab(paramTab.getPosition());
      } 
    } else {
      byte b;
      if (paramTab != null) {
        b = paramTab.getPosition();
      } else {
        b = -1;
      } 
      if (paramBoolean) {
        if ((tab == null || tab.getPosition() == -1) && b != -1) {
          setScrollPosition(b, 0.0F, true);
        } else {
          animateToTab(b);
        } 
        if (b != -1)
          setSelectedTabView(b); 
      } 
      this.selectedTab = paramTab;
      if (tab != null)
        dispatchTabUnselected(tab); 
      if (paramTab != null)
        dispatchTabSelected(paramTab); 
    } 
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    MaterialShapeUtils.setElevation((View)this, paramFloat);
  }
  
  public void setInlineLabel(boolean paramBoolean) {
    if (this.inlineLabel != paramBoolean) {
      this.inlineLabel = paramBoolean;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateOrientation(); 
      } 
      applyModeAndGravity();
    } 
  }
  
  public void setInlineLabelResource(int paramInt) {
    setInlineLabel(getResources().getBoolean(paramInt));
  }
  
  @Deprecated
  public void setOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    BaseOnTabSelectedListener baseOnTabSelectedListener = this.selectedListener;
    if (baseOnTabSelectedListener != null)
      removeOnTabSelectedListener(baseOnTabSelectedListener); 
    this.selectedListener = paramBaseOnTabSelectedListener;
    if (paramBaseOnTabSelectedListener != null)
      addOnTabSelectedListener(paramBaseOnTabSelectedListener); 
  }
  
  @Deprecated
  public void setOnTabSelectedListener(OnTabSelectedListener paramOnTabSelectedListener) {
    setOnTabSelectedListener(paramOnTabSelectedListener);
  }
  
  void setPagerAdapter(PagerAdapter paramPagerAdapter, boolean paramBoolean) {
    PagerAdapter pagerAdapter = this.pagerAdapter;
    if (pagerAdapter != null) {
      DataSetObserver dataSetObserver = this.pagerAdapterObserver;
      if (dataSetObserver != null)
        pagerAdapter.unregisterDataSetObserver(dataSetObserver); 
    } 
    this.pagerAdapter = paramPagerAdapter;
    if (paramBoolean && paramPagerAdapter != null) {
      if (this.pagerAdapterObserver == null)
        this.pagerAdapterObserver = new PagerAdapterObserver(); 
      paramPagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
    } 
    populateFromPagerAdapter();
  }
  
  void setScrollAnimatorListener(Animator.AnimatorListener paramAnimatorListener) {
    ensureScrollAnimator();
    this.scrollAnimator.addListener(paramAnimatorListener);
  }
  
  public void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean) {
    setScrollPosition(paramInt, paramFloat, paramBoolean, true);
  }
  
  public void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    int i = Math.round(paramInt + paramFloat);
    if (i < 0 || i >= this.slidingTabIndicator.getChildCount())
      return; 
    if (paramBoolean2)
      this.slidingTabIndicator.setIndicatorPositionFromTabPosition(paramInt, paramFloat); 
    ValueAnimator valueAnimator = this.scrollAnimator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.scrollAnimator.cancel(); 
    scrollTo(calculateScrollXForTab(paramInt, paramFloat), 0);
    if (paramBoolean1)
      setSelectedTabView(i); 
  }
  
  public void setSelectedTabIndicator(int paramInt) {
    if (paramInt != 0) {
      setSelectedTabIndicator(AppCompatResources.getDrawable(getContext(), paramInt));
    } else {
      setSelectedTabIndicator((Drawable)null);
    } 
  }
  
  public void setSelectedTabIndicator(Drawable paramDrawable) {
    if (this.tabSelectedIndicator != paramDrawable) {
      GradientDrawable gradientDrawable;
      if (paramDrawable == null)
        gradientDrawable = new GradientDrawable(); 
      this.tabSelectedIndicator = (Drawable)gradientDrawable;
    } 
  }
  
  public void setSelectedTabIndicatorColor(int paramInt) {
    this.tabSelectedIndicatorColor = paramInt;
  }
  
  public void setSelectedTabIndicatorGravity(int paramInt) {
    if (this.tabIndicatorGravity != paramInt) {
      this.tabIndicatorGravity = paramInt;
      ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    } 
  }
  
  @Deprecated
  public void setSelectedTabIndicatorHeight(int paramInt) {
    this.slidingTabIndicator.setSelectedIndicatorHeight(paramInt);
  }
  
  public void setTabGravity(int paramInt) {
    if (this.tabGravity != paramInt) {
      this.tabGravity = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabIconTint(ColorStateList paramColorStateList) {
    if (this.tabIconTint != paramColorStateList) {
      this.tabIconTint = paramColorStateList;
      updateAllTabs();
    } 
  }
  
  public void setTabIconTintResource(int paramInt) {
    setTabIconTint(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setTabIndicatorAnimationMode(int paramInt) {
    this.tabIndicatorAnimationMode = paramInt;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(paramInt + " is not a valid TabIndicatorAnimationMode");
      case 1:
        this.tabIndicatorInterpolator = new ElasticTabIndicatorInterpolator();
        return;
      case 0:
        break;
    } 
    this.tabIndicatorInterpolator = new TabIndicatorInterpolator();
  }
  
  public void setTabIndicatorFullWidth(boolean paramBoolean) {
    this.tabIndicatorFullWidth = paramBoolean;
    ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
  }
  
  public void setTabMode(int paramInt) {
    if (paramInt != this.mode) {
      this.mode = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabRippleColor(ColorStateList paramColorStateList) {
    if (this.tabRippleColorStateList != paramColorStateList) {
      this.tabRippleColorStateList = paramColorStateList;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateBackgroundDrawable(getContext()); 
      } 
    } 
  }
  
  public void setTabRippleColorResource(int paramInt) {
    setTabRippleColor(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setTabTextColors(int paramInt1, int paramInt2) {
    setTabTextColors(createColorStateList(paramInt1, paramInt2));
  }
  
  public void setTabTextColors(ColorStateList paramColorStateList) {
    if (this.tabTextColors != paramColorStateList) {
      this.tabTextColors = paramColorStateList;
      updateAllTabs();
    } 
  }
  
  @Deprecated
  public void setTabsFromPagerAdapter(PagerAdapter paramPagerAdapter) {
    setPagerAdapter(paramPagerAdapter, false);
  }
  
  public void setUnboundedRipple(boolean paramBoolean) {
    if (this.unboundedRipple != paramBoolean) {
      this.unboundedRipple = paramBoolean;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateBackgroundDrawable(getContext()); 
      } 
    } 
  }
  
  public void setUnboundedRippleResource(int paramInt) {
    setUnboundedRipple(getResources().getBoolean(paramInt));
  }
  
  public void setupWithViewPager(ViewPager paramViewPager) {
    setupWithViewPager(paramViewPager, true);
  }
  
  public void setupWithViewPager(ViewPager paramViewPager, boolean paramBoolean) {
    setupWithViewPager(paramViewPager, paramBoolean, false);
  }
  
  public boolean shouldDelayChildPressedState() {
    boolean bool;
    if (getTabScrollRange() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void updateTabViews(boolean paramBoolean) {
    for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
      View view = this.slidingTabIndicator.getChildAt(b);
      view.setMinimumWidth(getTabMinWidth());
      updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
      if (paramBoolean)
        view.requestLayout(); 
    } 
  }
  
  private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
    private boolean autoRefresh;
    
    final TabLayout this$0;
    
    public void onAdapterChanged(ViewPager param1ViewPager, PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2) {
      if (TabLayout.this.viewPager == param1ViewPager)
        TabLayout.this.setPagerAdapter(param1PagerAdapter2, this.autoRefresh); 
    }
    
    void setAutoRefresh(boolean param1Boolean) {
      this.autoRefresh = param1Boolean;
    }
  }
  
  @Deprecated
  public static interface BaseOnTabSelectedListener<T extends Tab> {
    void onTabReselected(T param1T);
    
    void onTabSelected(T param1T);
    
    void onTabUnselected(T param1T);
  }
  
  public static @interface LabelVisibility {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
  
  public static interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {}
  
  private class PagerAdapterObserver extends DataSetObserver {
    final TabLayout this$0;
    
    public void onChanged() {
      TabLayout.this.populateFromPagerAdapter();
    }
    
    public void onInvalidated() {
      TabLayout.this.populateFromPagerAdapter();
    }
  }
  
  class SlidingTabIndicator extends LinearLayout {
    ValueAnimator indicatorAnimator;
    
    private int layoutDirection = -1;
    
    int selectedPosition = -1;
    
    float selectionOffset;
    
    final TabLayout this$0;
    
    SlidingTabIndicator(Context param1Context) {
      super(param1Context);
      setWillNotDraw(false);
    }
    
    private void jumpIndicatorToSelectedPosition() {
      View view = getChildAt(this.selectedPosition);
      TabIndicatorInterpolator tabIndicatorInterpolator = TabLayout.this.tabIndicatorInterpolator;
      TabLayout tabLayout = TabLayout.this;
      tabIndicatorInterpolator.setIndicatorBoundsForTab(tabLayout, view, tabLayout.tabSelectedIndicator);
    }
    
    private void tweenIndicatorPosition(View param1View1, View param1View2, float param1Float) {
      boolean bool;
      if (param1View1 != null && param1View1.getWidth() > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        TabIndicatorInterpolator tabIndicatorInterpolator = TabLayout.this.tabIndicatorInterpolator;
        TabLayout tabLayout = TabLayout.this;
        tabIndicatorInterpolator.setIndicatorBoundsForOffset(tabLayout, param1View1, param1View2, param1Float, tabLayout.tabSelectedIndicator);
      } else {
        TabLayout.this.tabSelectedIndicator.setBounds(-1, (TabLayout.this.tabSelectedIndicator.getBounds()).top, -1, (TabLayout.this.tabSelectedIndicator.getBounds()).bottom);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    private void updateOrRecreateIndicatorAnimation(boolean param1Boolean, final int position, int param1Int2) {
      final View currentView = getChildAt(this.selectedPosition);
      final View targetView = getChildAt(position);
      if (view2 == null) {
        jumpIndicatorToSelectedPosition();
        return;
      } 
      ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
          final TabLayout.SlidingTabIndicator this$1;
          
          final View val$currentView;
          
          final View val$targetView;
          
          public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
            TabLayout.SlidingTabIndicator.this.tweenIndicatorPosition(currentView, targetView, param2ValueAnimator.getAnimatedFraction());
          }
        };
      if (param1Boolean) {
        ValueAnimator valueAnimator = new ValueAnimator();
        this.indicatorAnimator = valueAnimator;
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(param1Int2);
        valueAnimator.setFloatValues(new float[] { 0.0F, 1.0F });
        valueAnimator.addUpdateListener(animatorUpdateListener);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              final TabLayout.SlidingTabIndicator this$1;
              
              final int val$position;
              
              public void onAnimationEnd(Animator param2Animator) {
                TabLayout.SlidingTabIndicator.this.selectedPosition = position;
              }
              
              public void onAnimationStart(Animator param2Animator) {
                TabLayout.SlidingTabIndicator.this.selectedPosition = position;
              }
            });
        valueAnimator.start();
      } else {
        this.indicatorAnimator.removeAllUpdateListeners();
        this.indicatorAnimator.addUpdateListener(animatorUpdateListener);
      } 
    }
    
    void animateIndicatorToPosition(int param1Int1, int param1Int2) {
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning())
        this.indicatorAnimator.cancel(); 
      updateOrRecreateIndicatorAnimation(true, param1Int1, param1Int2);
    }
    
    boolean childrenNeedLayout() {
      byte b = 0;
      int i = getChildCount();
      while (b < i) {
        if (getChildAt(b).getWidth() <= 0)
          return true; 
        b++;
      } 
      return false;
    }
    
    public void draw(Canvas param1Canvas) {
      int j = TabLayout.this.tabSelectedIndicator.getBounds().height();
      int i = j;
      if (j < 0)
        i = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight(); 
      int k = 0;
      j = 0;
      switch (TabLayout.this.tabIndicatorGravity) {
        default:
          i = k;
          break;
        case 3:
          i = 0;
          j = getHeight();
          break;
        case 2:
          k = 0;
          j = i;
          i = k;
          break;
        case 1:
          j = (getHeight() - i) / 2;
          k = (getHeight() + i) / 2;
          i = j;
          j = k;
          break;
        case 0:
          i = getHeight() - i;
          j = getHeight();
          break;
      } 
      if (TabLayout.this.tabSelectedIndicator.getBounds().width() > 0) {
        Rect rect = TabLayout.this.tabSelectedIndicator.getBounds();
        TabLayout.this.tabSelectedIndicator.setBounds(rect.left, i, rect.right, j);
        Drawable drawable2 = TabLayout.this.tabSelectedIndicator;
        Drawable drawable1 = drawable2;
        if (TabLayout.this.tabSelectedIndicatorColor != 0) {
          drawable1 = DrawableCompat.wrap(drawable2);
          if (Build.VERSION.SDK_INT == 21) {
            drawable1.setColorFilter(TabLayout.this.tabSelectedIndicatorColor, PorterDuff.Mode.SRC_IN);
          } else {
            DrawableCompat.setTint(drawable1, TabLayout.this.tabSelectedIndicatorColor);
          } 
        } 
        drawable1.draw(param1Canvas);
      } 
      super.draw(param1Canvas);
    }
    
    float getIndicatorPosition() {
      return this.selectedPosition + this.selectionOffset;
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.onLayout(param1Boolean, param1Int1, param1Int2, param1Int3, param1Int4);
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning()) {
        updateOrRecreateIndicatorAnimation(false, this.selectedPosition, -1);
      } else {
        jumpIndicatorToSelectedPosition();
      } 
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      if (View.MeasureSpec.getMode(param1Int1) != 1073741824)
        return; 
      if (TabLayout.this.tabGravity == 1 || TabLayout.this.mode == 2) {
        int k = getChildCount();
        int i = 0;
        byte b = 0;
        while (b < k) {
          View view = getChildAt(b);
          int m = i;
          if (view.getVisibility() == 0)
            m = Math.max(i, view.getMeasuredWidth()); 
          b++;
          i = m;
        } 
        if (i <= 0)
          return; 
        int j = (int)ViewUtils.dpToPx(getContext(), 16);
        b = 0;
        if (i * k <= getMeasuredWidth() - j * 2) {
          for (j = 0; j < k; j++) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getChildAt(j).getLayoutParams();
            if (layoutParams.width != i || layoutParams.weight != 0.0F) {
              layoutParams.width = i;
              layoutParams.weight = 0.0F;
              b = 1;
            } 
          } 
        } else {
          TabLayout.this.tabGravity = 0;
          TabLayout.this.updateTabViews(false);
          b = 1;
        } 
        if (b != 0)
          super.onMeasure(param1Int1, param1Int2); 
      } 
    }
    
    public void onRtlPropertiesChanged(int param1Int) {
      super.onRtlPropertiesChanged(param1Int);
      if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != param1Int) {
        requestLayout();
        this.layoutDirection = param1Int;
      } 
    }
    
    void setIndicatorPositionFromTabPosition(int param1Int, float param1Float) {
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning())
        this.indicatorAnimator.cancel(); 
      this.selectedPosition = param1Int;
      this.selectionOffset = param1Float;
      tweenIndicatorPosition(getChildAt(param1Int), getChildAt(this.selectedPosition + 1), this.selectionOffset);
    }
    
    void setSelectedIndicatorHeight(int param1Int) {
      Rect rect = TabLayout.this.tabSelectedIndicator.getBounds();
      TabLayout.this.tabSelectedIndicator.setBounds(rect.left, 0, rect.right, param1Int);
      requestLayout();
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    final TabLayout.SlidingTabIndicator this$1;
    
    final View val$currentView;
    
    final View val$targetView;
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      this.this$1.tweenIndicatorPosition(currentView, targetView, param1ValueAnimator.getAnimatedFraction());
    }
  }
  
  class null extends AnimatorListenerAdapter {
    final TabLayout.SlidingTabIndicator this$1;
    
    final int val$position;
    
    public void onAnimationEnd(Animator param1Animator) {
      this.this$1.selectedPosition = position;
    }
    
    public void onAnimationStart(Animator param1Animator) {
      this.this$1.selectedPosition = position;
    }
  }
  
  public static class Tab {
    public static final int INVALID_POSITION = -1;
    
    private CharSequence contentDesc;
    
    private View customView;
    
    private Drawable icon;
    
    private int id = -1;
    
    private int labelVisibilityMode = 1;
    
    public TabLayout parent;
    
    private int position = -1;
    
    private Object tag;
    
    private CharSequence text;
    
    public TabLayout.TabView view;
    
    public BadgeDrawable getBadge() {
      return this.view.getBadge();
    }
    
    public CharSequence getContentDescription() {
      CharSequence charSequence;
      TabLayout.TabView tabView = this.view;
      if (tabView == null) {
        tabView = null;
      } else {
        charSequence = tabView.getContentDescription();
      } 
      return charSequence;
    }
    
    public View getCustomView() {
      return this.customView;
    }
    
    public Drawable getIcon() {
      return this.icon;
    }
    
    public int getId() {
      return this.id;
    }
    
    public BadgeDrawable getOrCreateBadge() {
      return this.view.getOrCreateBadge();
    }
    
    public int getPosition() {
      return this.position;
    }
    
    public int getTabLabelVisibility() {
      return this.labelVisibilityMode;
    }
    
    public Object getTag() {
      return this.tag;
    }
    
    public CharSequence getText() {
      return this.text;
    }
    
    public boolean isSelected() {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null) {
        boolean bool;
        if (tabLayout.getSelectedTabPosition() == this.position) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      } 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public void removeBadge() {
      this.view.removeBadge();
    }
    
    void reset() {
      this.parent = null;
      this.view = null;
      this.tag = null;
      this.icon = null;
      this.id = -1;
      this.text = null;
      this.contentDesc = null;
      this.position = -1;
      this.customView = null;
    }
    
    public void select() {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null) {
        tabLayout.selectTab(this);
        return;
      } 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setContentDescription(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setContentDescription(tabLayout.getResources().getText(param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setContentDescription(CharSequence param1CharSequence) {
      this.contentDesc = param1CharSequence;
      updateView();
      return this;
    }
    
    public Tab setCustomView(int param1Int) {
      return setCustomView(LayoutInflater.from(this.view.getContext()).inflate(param1Int, (ViewGroup)this.view, false));
    }
    
    public Tab setCustomView(View param1View) {
      this.customView = param1View;
      updateView();
      return this;
    }
    
    public Tab setIcon(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setIcon(Drawable param1Drawable) {
      this.icon = param1Drawable;
      if (this.parent.tabGravity == 1 || this.parent.mode == 2)
        this.parent.updateTabViews(true); 
      updateView();
      if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible())
        this.view.invalidate(); 
      return this;
    }
    
    public Tab setId(int param1Int) {
      this.id = param1Int;
      TabLayout.TabView tabView = this.view;
      if (tabView != null)
        tabView.setId(param1Int); 
      return this;
    }
    
    void setPosition(int param1Int) {
      this.position = param1Int;
    }
    
    public Tab setTabLabelVisibility(int param1Int) {
      this.labelVisibilityMode = param1Int;
      if (this.parent.tabGravity == 1 || this.parent.mode == 2)
        this.parent.updateTabViews(true); 
      updateView();
      if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible())
        this.view.invalidate(); 
      return this;
    }
    
    public Tab setTag(Object param1Object) {
      this.tag = param1Object;
      return this;
    }
    
    public Tab setText(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setText(tabLayout.getResources().getText(param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setText(CharSequence param1CharSequence) {
      if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(param1CharSequence))
        this.view.setContentDescription(param1CharSequence); 
      this.text = param1CharSequence;
      updateView();
      return this;
    }
    
    void updateView() {
      TabLayout.TabView tabView = this.view;
      if (tabView != null)
        tabView.update(); 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TabGravity {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TabIndicatorAnimationMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TabIndicatorGravity {}
  
  public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private int previousScrollState;
    
    private int scrollState;
    
    private final WeakReference<TabLayout> tabLayoutRef;
    
    public TabLayoutOnPageChangeListener(TabLayout param1TabLayout) {
      this.tabLayoutRef = new WeakReference<>(param1TabLayout);
    }
    
    public void onPageScrollStateChanged(int param1Int) {
      this.previousScrollState = this.scrollState;
      this.scrollState = param1Int;
    }
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null) {
        boolean bool1;
        param1Int2 = this.scrollState;
        boolean bool2 = false;
        if (param1Int2 != 2 || this.previousScrollState == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (param1Int2 != 2 || this.previousScrollState != 0)
          bool2 = true; 
        tabLayout.setScrollPosition(param1Int1, param1Float, bool1, bool2);
      } 
    }
    
    public void onPageSelected(int param1Int) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null && tabLayout.getSelectedTabPosition() != param1Int && param1Int < tabLayout.getTabCount()) {
        boolean bool;
        int i = this.scrollState;
        if (i == 0 || (i == 2 && this.previousScrollState == 0)) {
          bool = true;
        } else {
          bool = false;
        } 
        tabLayout.selectTab(tabLayout.getTabAt(param1Int), bool);
      } 
    }
    
    void reset() {
      this.scrollState = 0;
      this.previousScrollState = 0;
    }
  }
  
  public final class TabView extends LinearLayout {
    private View badgeAnchorView;
    
    private BadgeDrawable badgeDrawable;
    
    private Drawable baseBackgroundDrawable;
    
    private ImageView customIconView;
    
    private TextView customTextView;
    
    private View customView;
    
    private int defaultMaxLines = 2;
    
    private ImageView iconView;
    
    private TabLayout.Tab tab;
    
    private TextView textView;
    
    final TabLayout this$0;
    
    public TabView(Context param1Context) {
      super(param1Context);
      updateBackgroundDrawable(param1Context);
      ViewCompat.setPaddingRelative((View)this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
      setGravity(17);
      setOrientation(TabLayout.this.inlineLabel ^ true);
      setClickable(true);
      ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(getContext(), 1002));
    }
    
    private void addOnLayoutChangeListener(final View view) {
      if (view == null)
        return; 
      view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            final TabLayout.TabView this$1;
            
            final View val$view;
            
            public void onLayoutChange(View param2View, int param2Int1, int param2Int2, int param2Int3, int param2Int4, int param2Int5, int param2Int6, int param2Int7, int param2Int8) {
              if (view.getVisibility() == 0)
                TabLayout.TabView.this.tryUpdateBadgeDrawableBounds(view); 
            }
          });
    }
    
    private float approximateLineWidth(Layout param1Layout, int param1Int, float param1Float) {
      return param1Layout.getLineWidth(param1Int) * param1Float / param1Layout.getPaint().getTextSize();
    }
    
    private void clipViewToPaddingForBadge(boolean param1Boolean) {
      setClipChildren(param1Boolean);
      setClipToPadding(param1Boolean);
      ViewGroup viewGroup = (ViewGroup)getParent();
      if (viewGroup != null) {
        viewGroup.setClipChildren(param1Boolean);
        viewGroup.setClipToPadding(param1Boolean);
      } 
    }
    
    private FrameLayout createPreApi18BadgeAnchorRoot() {
      FrameLayout frameLayout = new FrameLayout(getContext());
      frameLayout.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
      return frameLayout;
    }
    
    private void drawBackground(Canvas param1Canvas) {
      Drawable drawable = this.baseBackgroundDrawable;
      if (drawable != null) {
        drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        this.baseBackgroundDrawable.draw(param1Canvas);
      } 
    }
    
    private BadgeDrawable getBadge() {
      return this.badgeDrawable;
    }
    
    private FrameLayout getCustomParentForBadge(View param1View) {
      ImageView imageView = this.iconView;
      FrameLayout frameLayout = null;
      if (param1View != imageView && param1View != this.textView)
        return null; 
      if (BadgeUtils.USE_COMPAT_PARENT)
        frameLayout = (FrameLayout)param1View.getParent(); 
      return frameLayout;
    }
    
    private BadgeDrawable getOrCreateBadge() {
      if (this.badgeDrawable == null)
        this.badgeDrawable = BadgeDrawable.create(getContext()); 
      tryUpdateBadgeAnchor();
      BadgeDrawable badgeDrawable = this.badgeDrawable;
      if (badgeDrawable != null)
        return badgeDrawable; 
      throw new IllegalStateException("Unable to create badge");
    }
    
    private boolean hasBadgeDrawable() {
      boolean bool;
      if (this.badgeDrawable != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private void inflateAndAddDefaultIconView() {
      FrameLayout frameLayout;
      TabView tabView = this;
      if (BadgeUtils.USE_COMPAT_PARENT) {
        frameLayout = createPreApi18BadgeAnchorRoot();
        addView((View)frameLayout, 0);
      } 
      ImageView imageView = (ImageView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)frameLayout, false);
      this.iconView = imageView;
      frameLayout.addView((View)imageView, 0);
    }
    
    private void inflateAndAddDefaultTextView() {
      FrameLayout frameLayout;
      TabView tabView = this;
      if (BadgeUtils.USE_COMPAT_PARENT) {
        frameLayout = createPreApi18BadgeAnchorRoot();
        addView((View)frameLayout);
      } 
      TextView textView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)frameLayout, false);
      this.textView = textView;
      frameLayout.addView((View)textView);
    }
    
    private void removeBadge() {
      if (this.badgeAnchorView != null)
        tryRemoveBadgeFromAnchor(); 
      this.badgeDrawable = null;
    }
    
    private void tryAttachBadgeToAnchor(View param1View) {
      if (!hasBadgeDrawable())
        return; 
      if (param1View != null) {
        clipViewToPaddingForBadge(false);
        BadgeUtils.attachBadgeDrawable(this.badgeDrawable, param1View, getCustomParentForBadge(param1View));
        this.badgeAnchorView = param1View;
      } 
    }
    
    private void tryRemoveBadgeFromAnchor() {
      if (!hasBadgeDrawable())
        return; 
      clipViewToPaddingForBadge(true);
      View view = this.badgeAnchorView;
      if (view != null) {
        BadgeUtils.detachBadgeDrawable(this.badgeDrawable, view);
        this.badgeAnchorView = null;
      } 
    }
    
    private void tryUpdateBadgeAnchor() {
      if (!hasBadgeDrawable())
        return; 
      if (this.customView != null) {
        tryRemoveBadgeFromAnchor();
      } else {
        if (this.iconView != null) {
          TabLayout.Tab tab = this.tab;
          if (tab != null && tab.getIcon() != null) {
            View view = this.badgeAnchorView;
            ImageView imageView = this.iconView;
            if (view != imageView) {
              tryRemoveBadgeFromAnchor();
              tryAttachBadgeToAnchor((View)this.iconView);
            } else {
              tryUpdateBadgeDrawableBounds((View)imageView);
            } 
            return;
          } 
        } 
        if (this.textView != null) {
          TabLayout.Tab tab = this.tab;
          if (tab != null && tab.getTabLabelVisibility() == 1) {
            View view = this.badgeAnchorView;
            TextView textView = this.textView;
            if (view != textView) {
              tryRemoveBadgeFromAnchor();
              tryAttachBadgeToAnchor((View)this.textView);
            } else {
              tryUpdateBadgeDrawableBounds((View)textView);
            } 
            return;
          } 
        } 
        tryRemoveBadgeFromAnchor();
      } 
    }
    
    private void tryUpdateBadgeDrawableBounds(View param1View) {
      if (hasBadgeDrawable() && param1View == this.badgeAnchorView)
        BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, param1View, getCustomParentForBadge(param1View)); 
    }
    
    private void updateBackgroundDrawable(Context param1Context) {
      LayerDrawable layerDrawable;
      int i = TabLayout.this.tabBackgroundResId;
      GradientDrawable gradientDrawable2 = null;
      if (i != 0) {
        Drawable drawable = AppCompatResources.getDrawable(param1Context, TabLayout.this.tabBackgroundResId);
        this.baseBackgroundDrawable = drawable;
        if (drawable != null && drawable.isStateful())
          this.baseBackgroundDrawable.setState(getDrawableState()); 
      } else {
        this.baseBackgroundDrawable = null;
      } 
      GradientDrawable gradientDrawable1 = new GradientDrawable();
      gradientDrawable1.setColor(0);
      if (TabLayout.this.tabRippleColorStateList != null) {
        RippleDrawable rippleDrawable;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1.0E-5F);
        gradientDrawable.setColor(-1);
        ColorStateList colorStateList = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
        if (Build.VERSION.SDK_INT >= 21) {
          if (TabLayout.this.unboundedRipple)
            gradientDrawable1 = null; 
          if (!TabLayout.this.unboundedRipple)
            gradientDrawable2 = gradientDrawable; 
          rippleDrawable = new RippleDrawable(colorStateList, (Drawable)gradientDrawable1, (Drawable)gradientDrawable2);
        } else {
          Drawable drawable = DrawableCompat.wrap((Drawable)gradientDrawable);
          DrawableCompat.setTintList(drawable, colorStateList);
          layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)rippleDrawable, drawable });
        } 
      } 
      ViewCompat.setBackground((View)this, (Drawable)layerDrawable);
      TabLayout.this.invalidate();
    }
    
    private void updateTextAndIcon(TextView param1TextView, ImageView param1ImageView) {
      Drawable drawable;
      TabLayout.Tab tab2 = this.tab;
      TextView textView = null;
      if (tab2 != null && tab2.getIcon() != null) {
        drawable = DrawableCompat.wrap(this.tab.getIcon()).mutate();
      } else {
        drawable = null;
      } 
      tab2 = this.tab;
      if (tab2 != null) {
        CharSequence charSequence = tab2.getText();
      } else {
        tab2 = null;
      } 
      if (param1ImageView != null)
        if (drawable != null) {
          param1ImageView.setImageDrawable(drawable);
          param1ImageView.setVisibility(0);
          setVisibility(0);
        } else {
          param1ImageView.setVisibility(8);
          param1ImageView.setImageDrawable(null);
        }  
      int i = TextUtils.isEmpty((CharSequence)tab2) ^ true;
      if (param1TextView != null)
        if (i != 0) {
          param1TextView.setText((CharSequence)tab2);
          if (this.tab.labelVisibilityMode == 1) {
            param1TextView.setVisibility(0);
          } else {
            param1TextView.setVisibility(8);
          } 
          setVisibility(0);
        } else {
          param1TextView.setVisibility(8);
          param1TextView.setText(null);
        }  
      if (param1ImageView != null) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)param1ImageView.getLayoutParams();
        byte b = 0;
        int j = b;
        if (i != 0) {
          j = b;
          if (param1ImageView.getVisibility() == 0)
            j = (int)ViewUtils.dpToPx(getContext(), 8); 
        } 
        if (TabLayout.this.inlineLabel) {
          if (j != MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) {
            MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, j);
            marginLayoutParams.bottomMargin = 0;
            param1ImageView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
            param1ImageView.requestLayout();
          } 
        } else if (j != marginLayoutParams.bottomMargin) {
          marginLayoutParams.bottomMargin = j;
          MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, 0);
          param1ImageView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
          param1ImageView.requestLayout();
        } 
      } 
      TabLayout.Tab tab1 = this.tab;
      param1TextView = textView;
      if (tab1 != null)
        CharSequence charSequence = tab1.contentDesc; 
      if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23) {
        TabLayout.Tab tab;
        if (i != 0)
          tab = tab2; 
        TooltipCompat.setTooltipText((View)this, (CharSequence)tab);
      } 
    }
    
    protected void drawableStateChanged() {
      super.drawableStateChanged();
      byte b = 0;
      int[] arrayOfInt = getDrawableState();
      Drawable drawable = this.baseBackgroundDrawable;
      int i = b;
      if (drawable != null) {
        i = b;
        if (drawable.isStateful())
          i = false | this.baseBackgroundDrawable.setState(arrayOfInt); 
      } 
      if (i != 0) {
        invalidate();
        TabLayout.this.invalidate();
      } 
    }
    
    int getContentHeight() {
      boolean bool = false;
      int j = 0;
      int i = 0;
      TextView textView = this.textView;
      byte b = 0;
      ImageView imageView = this.iconView;
      View view = this.customView;
      while (b < 3) {
        (new View[3])[0] = (View)textView;
        (new View[3])[1] = (View)imageView;
        (new View[3])[2] = view;
        View view1 = (new View[3])[b];
        boolean bool1 = bool;
        int m = j;
        int k = i;
        if (view1 != null) {
          bool1 = bool;
          m = j;
          k = i;
          if (view1.getVisibility() == 0) {
            m = view1.getTop();
            k = m;
            if (bool)
              k = Math.min(j, m); 
            j = k;
            m = view1.getBottom();
            k = m;
            if (bool)
              k = Math.max(i, m); 
            bool1 = true;
            m = j;
          } 
        } 
        b++;
        bool = bool1;
        j = m;
        i = k;
      } 
      return i - j;
    }
    
    int getContentWidth() {
      boolean bool = false;
      int j = 0;
      int i = 0;
      TextView textView = this.textView;
      byte b = 0;
      ImageView imageView = this.iconView;
      View view = this.customView;
      while (b < 3) {
        (new View[3])[0] = (View)textView;
        (new View[3])[1] = (View)imageView;
        (new View[3])[2] = view;
        View view1 = (new View[3])[b];
        boolean bool1 = bool;
        int m = j;
        int k = i;
        if (view1 != null) {
          bool1 = bool;
          m = j;
          k = i;
          if (view1.getVisibility() == 0) {
            m = view1.getLeft();
            k = m;
            if (bool)
              k = Math.min(j, m); 
            j = k;
            m = view1.getRight();
            k = m;
            if (bool)
              k = Math.max(i, m); 
            bool1 = true;
            m = j;
          } 
        } 
        b++;
        bool = bool1;
        j = m;
        i = k;
      } 
      return i - j;
    }
    
    public TabLayout.Tab getTab() {
      return this.tab;
    }
    
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      super.onInitializeAccessibilityNodeInfo(param1AccessibilityNodeInfo);
      BadgeDrawable badgeDrawable = this.badgeDrawable;
      if (badgeDrawable != null && badgeDrawable.isVisible()) {
        CharSequence charSequence = getContentDescription();
        param1AccessibilityNodeInfo.setContentDescription(charSequence + ", " + this.badgeDrawable.getContentDescription());
      } 
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(param1AccessibilityNodeInfo);
      accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.tab.getPosition(), 1, false, isSelected()));
      if (isSelected()) {
        accessibilityNodeInfoCompat.setClickable(false);
        accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
      } 
      accessibilityNodeInfoCompat.setRoleDescription(getResources().getString(R.string.item_view_role_description));
    }
    
    public void onMeasure(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_1
      //   1: invokestatic getSize : (I)I
      //   4: istore #5
      //   6: iload_1
      //   7: invokestatic getMode : (I)I
      //   10: istore #7
      //   12: aload_0
      //   13: getfield this$0 : Lcom/google/android/material/tabs/TabLayout;
      //   16: invokevirtual getTabMaxWidth : ()I
      //   19: istore #6
      //   21: iload #6
      //   23: ifle -> 55
      //   26: iload #7
      //   28: ifeq -> 38
      //   31: iload #5
      //   33: iload #6
      //   35: if_icmple -> 55
      //   38: aload_0
      //   39: getfield this$0 : Lcom/google/android/material/tabs/TabLayout;
      //   42: getfield tabMaxWidth : I
      //   45: ldc_w -2147483648
      //   48: invokestatic makeMeasureSpec : (II)I
      //   51: istore_1
      //   52: goto -> 55
      //   55: aload_0
      //   56: iload_1
      //   57: iload_2
      //   58: invokespecial onMeasure : (II)V
      //   61: aload_0
      //   62: getfield textView : Landroid/widget/TextView;
      //   65: ifnull -> 319
      //   68: aload_0
      //   69: getfield this$0 : Lcom/google/android/material/tabs/TabLayout;
      //   72: getfield tabTextSize : F
      //   75: fstore #4
      //   77: aload_0
      //   78: getfield defaultMaxLines : I
      //   81: istore #6
      //   83: aload_0
      //   84: getfield iconView : Landroid/widget/ImageView;
      //   87: astore #9
      //   89: aload #9
      //   91: ifnull -> 111
      //   94: aload #9
      //   96: invokevirtual getVisibility : ()I
      //   99: ifne -> 111
      //   102: iconst_1
      //   103: istore #5
      //   105: fload #4
      //   107: fstore_3
      //   108: goto -> 157
      //   111: aload_0
      //   112: getfield textView : Landroid/widget/TextView;
      //   115: astore #9
      //   117: fload #4
      //   119: fstore_3
      //   120: iload #6
      //   122: istore #5
      //   124: aload #9
      //   126: ifnull -> 157
      //   129: fload #4
      //   131: fstore_3
      //   132: iload #6
      //   134: istore #5
      //   136: aload #9
      //   138: invokevirtual getLineCount : ()I
      //   141: iconst_1
      //   142: if_icmple -> 157
      //   145: aload_0
      //   146: getfield this$0 : Lcom/google/android/material/tabs/TabLayout;
      //   149: getfield tabTextMultiLineSize : F
      //   152: fstore_3
      //   153: iload #6
      //   155: istore #5
      //   157: aload_0
      //   158: getfield textView : Landroid/widget/TextView;
      //   161: invokevirtual getTextSize : ()F
      //   164: fstore #4
      //   166: aload_0
      //   167: getfield textView : Landroid/widget/TextView;
      //   170: invokevirtual getLineCount : ()I
      //   173: istore #8
      //   175: aload_0
      //   176: getfield textView : Landroid/widget/TextView;
      //   179: invokestatic getMaxLines : (Landroid/widget/TextView;)I
      //   182: istore #6
      //   184: fload_3
      //   185: fload #4
      //   187: fcmpl
      //   188: ifne -> 203
      //   191: iload #6
      //   193: iflt -> 319
      //   196: iload #5
      //   198: iload #6
      //   200: if_icmpeq -> 319
      //   203: iconst_1
      //   204: istore #7
      //   206: iload #7
      //   208: istore #6
      //   210: aload_0
      //   211: getfield this$0 : Lcom/google/android/material/tabs/TabLayout;
      //   214: getfield mode : I
      //   217: iconst_1
      //   218: if_icmpne -> 290
      //   221: iload #7
      //   223: istore #6
      //   225: fload_3
      //   226: fload #4
      //   228: fcmpl
      //   229: ifle -> 290
      //   232: iload #7
      //   234: istore #6
      //   236: iload #8
      //   238: iconst_1
      //   239: if_icmpne -> 290
      //   242: aload_0
      //   243: getfield textView : Landroid/widget/TextView;
      //   246: invokevirtual getLayout : ()Landroid/text/Layout;
      //   249: astore #9
      //   251: aload #9
      //   253: ifnull -> 287
      //   256: iload #7
      //   258: istore #6
      //   260: aload_0
      //   261: aload #9
      //   263: iconst_0
      //   264: fload_3
      //   265: invokespecial approximateLineWidth : (Landroid/text/Layout;IF)F
      //   268: aload_0
      //   269: invokevirtual getMeasuredWidth : ()I
      //   272: aload_0
      //   273: invokevirtual getPaddingLeft : ()I
      //   276: isub
      //   277: aload_0
      //   278: invokevirtual getPaddingRight : ()I
      //   281: isub
      //   282: i2f
      //   283: fcmpl
      //   284: ifle -> 290
      //   287: iconst_0
      //   288: istore #6
      //   290: iload #6
      //   292: ifeq -> 319
      //   295: aload_0
      //   296: getfield textView : Landroid/widget/TextView;
      //   299: iconst_0
      //   300: fload_3
      //   301: invokevirtual setTextSize : (IF)V
      //   304: aload_0
      //   305: getfield textView : Landroid/widget/TextView;
      //   308: iload #5
      //   310: invokevirtual setMaxLines : (I)V
      //   313: aload_0
      //   314: iload_1
      //   315: iload_2
      //   316: invokespecial onMeasure : (II)V
      //   319: return
    }
    
    public boolean performClick() {
      boolean bool = super.performClick();
      if (this.tab != null) {
        if (!bool)
          playSoundEffect(0); 
        this.tab.select();
        return true;
      } 
      return bool;
    }
    
    void reset() {
      setTab((TabLayout.Tab)null);
      setSelected(false);
    }
    
    public void setSelected(boolean param1Boolean) {
      boolean bool;
      if (isSelected() != param1Boolean) {
        bool = true;
      } else {
        bool = false;
      } 
      super.setSelected(param1Boolean);
      if (bool && param1Boolean && Build.VERSION.SDK_INT < 16)
        sendAccessibilityEvent(4); 
      TextView textView = this.textView;
      if (textView != null)
        textView.setSelected(param1Boolean); 
      ImageView imageView = this.iconView;
      if (imageView != null)
        imageView.setSelected(param1Boolean); 
      View view = this.customView;
      if (view != null)
        view.setSelected(param1Boolean); 
    }
    
    void setTab(TabLayout.Tab param1Tab) {
      if (param1Tab != this.tab) {
        this.tab = param1Tab;
        update();
      } 
    }
    
    final void update() {
      boolean bool;
      View view1;
      TabLayout.Tab tab = this.tab;
      View view2 = null;
      if (tab != null) {
        view1 = tab.getCustomView();
      } else {
        view1 = null;
      } 
      if (view1 != null) {
        ViewParent viewParent = view1.getParent();
        if (viewParent != this) {
          if (viewParent != null)
            ((ViewGroup)viewParent).removeView(view1); 
          addView(view1);
        } 
        this.customView = view1;
        TextView textView2 = this.textView;
        if (textView2 != null)
          textView2.setVisibility(8); 
        ImageView imageView = this.iconView;
        if (imageView != null) {
          imageView.setVisibility(8);
          this.iconView.setImageDrawable(null);
        } 
        TextView textView1 = (TextView)view1.findViewById(16908308);
        this.customTextView = textView1;
        if (textView1 != null)
          this.defaultMaxLines = TextViewCompat.getMaxLines(textView1); 
        this.customIconView = (ImageView)view1.findViewById(16908294);
      } else {
        view1 = this.customView;
        if (view1 != null) {
          removeView(view1);
          this.customView = null;
        } 
        this.customTextView = null;
        this.customIconView = null;
      } 
      if (this.customView == null) {
        if (this.iconView == null)
          inflateAndAddDefaultIconView(); 
        if (tab != null && tab.getIcon() != null) {
          Drawable drawable = DrawableCompat.wrap(tab.getIcon()).mutate();
        } else {
          view1 = view2;
        } 
        if (view1 != null) {
          DrawableCompat.setTintList((Drawable)view1, TabLayout.this.tabIconTint);
          if (TabLayout.this.tabIconTintMode != null)
            DrawableCompat.setTintMode((Drawable)view1, TabLayout.this.tabIconTintMode); 
        } 
        if (this.textView == null) {
          inflateAndAddDefaultTextView();
          this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
        } 
        TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
        if (TabLayout.this.tabTextColors != null)
          this.textView.setTextColor(TabLayout.this.tabTextColors); 
        updateTextAndIcon(this.textView, this.iconView);
        tryUpdateBadgeAnchor();
        addOnLayoutChangeListener((View)this.iconView);
        addOnLayoutChangeListener((View)this.textView);
      } else {
        TextView textView = this.customTextView;
        if (textView != null || this.customIconView != null)
          updateTextAndIcon(textView, this.customIconView); 
      } 
      if (tab != null && !TextUtils.isEmpty(tab.contentDesc))
        setContentDescription(tab.contentDesc); 
      if (tab != null && tab.isSelected()) {
        bool = true;
      } else {
        bool = false;
      } 
      setSelected(bool);
    }
    
    final void updateOrientation() {
      setOrientation(TabLayout.this.inlineLabel ^ true);
      TextView textView = this.customTextView;
      if (textView != null || this.customIconView != null) {
        updateTextAndIcon(textView, this.customIconView);
        return;
      } 
      updateTextAndIcon(this.textView, this.iconView);
    }
  }
  
  class null implements View.OnLayoutChangeListener {
    final TabLayout.TabView this$1;
    
    final View val$view;
    
    public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      if (view.getVisibility() == 0)
        this.this$1.tryUpdateBadgeDrawableBounds(view); 
    }
  }
  
  public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
    private final ViewPager viewPager;
    
    public ViewPagerOnTabSelectedListener(ViewPager param1ViewPager) {
      this.viewPager = param1ViewPager;
    }
    
    public void onTabReselected(TabLayout.Tab param1Tab) {}
    
    public void onTabSelected(TabLayout.Tab param1Tab) {
      this.viewPager.setCurrentItem(param1Tab.getPosition());
    }
    
    public void onTabUnselected(TabLayout.Tab param1Tab) {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\tabs\TabLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */