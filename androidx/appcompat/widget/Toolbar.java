package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuHostHelper;
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup implements MenuHost {
  private static final String TAG = "Toolbar";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  int mButtonGravity;
  
  ImageButton mCollapseButtonView;
  
  private CharSequence mCollapseDescription;
  
  private Drawable mCollapseIcon;
  
  private boolean mCollapsible;
  
  private int mContentInsetEndWithActions;
  
  private int mContentInsetStartWithNavigation;
  
  private RtlSpacingHelper mContentInsets;
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  View mExpandedActionView;
  
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  
  private int mGravity = 8388627;
  
  private final ArrayList<View> mHiddenViews = new ArrayList<>();
  
  private ImageView mLogoView;
  
  private int mMaxButtonHeight;
  
  private MenuBuilder.Callback mMenuBuilderCallback;
  
  final MenuHostHelper mMenuHostHelper = new MenuHostHelper(new Toolbar$$ExternalSyntheticLambda0(this));
  
  private ActionMenuView mMenuView;
  
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
      final Toolbar this$0;
      
      public boolean onMenuItemClick(MenuItem param1MenuItem) {
        return Toolbar.this.mMenuHostHelper.onMenuItemSelected(param1MenuItem) ? true : ((Toolbar.this.mOnMenuItemClickListener != null) ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem) : false);
      }
    };
  
  private ImageButton mNavButtonView;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private ActionMenuPresenter mOuterActionMenuPresenter;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ArrayList<MenuItem> mProvidedMenuItems = new ArrayList<>();
  
  private final Runnable mShowOverflowMenuRunnable = new Runnable() {
      final Toolbar this$0;
      
      public void run() {
        Toolbar.this.showOverflowMenu();
      }
    };
  
  private CharSequence mSubtitleText;
  
  private int mSubtitleTextAppearance;
  
  private ColorStateList mSubtitleTextColor;
  
  private TextView mSubtitleTextView;
  
  private final int[] mTempMargins = new int[2];
  
  private final ArrayList<View> mTempViews = new ArrayList<>();
  
  private int mTitleMarginBottom;
  
  private int mTitleMarginEnd;
  
  private int mTitleMarginStart;
  
  private int mTitleMarginTop;
  
  private CharSequence mTitleText;
  
  private int mTitleTextAppearance;
  
  private ColorStateList mTitleTextColor;
  
  private TextView mTitleTextView;
  
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Toolbar(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.toolbarStyle);
  }
  
  public Toolbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.Toolbar, paramInt, 0);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.Toolbar, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    this.mTitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
    this.mSubtitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
    this.mGravity = tintTypedArray.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
    this.mButtonGravity = tintTypedArray.getInteger(R.styleable.Toolbar_buttonGravity, 48);
    int i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
    paramInt = i;
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleMargins))
      paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, i); 
    this.mTitleMarginBottom = paramInt;
    this.mTitleMarginTop = paramInt;
    this.mTitleMarginEnd = paramInt;
    this.mTitleMarginStart = paramInt;
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
    if (paramInt >= 0)
      this.mTitleMarginStart = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
    if (paramInt >= 0)
      this.mTitleMarginEnd = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
    if (paramInt >= 0)
      this.mTitleMarginTop = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
    if (paramInt >= 0)
      this.mTitleMarginBottom = paramInt; 
    this.mMaxButtonHeight = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
    int j = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, -2147483648);
    i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, -2147483648);
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
    int k = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt, k);
    if (j != Integer.MIN_VALUE || i != Integer.MIN_VALUE)
      this.mContentInsets.setRelative(j, i); 
    this.mContentInsetStartWithNavigation = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, -2147483648);
    this.mContentInsetEndWithActions = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, -2147483648);
    this.mCollapseIcon = tintTypedArray.getDrawable(R.styleable.Toolbar_collapseIcon);
    this.mCollapseDescription = tintTypedArray.getText(R.styleable.Toolbar_collapseContentDescription);
    CharSequence charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_title);
    if (!TextUtils.isEmpty(charSequence3))
      setTitle(charSequence3); 
    charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_subtitle);
    if (!TextUtils.isEmpty(charSequence3))
      setSubtitle(charSequence3); 
    this.mPopupContext = getContext();
    setPopupTheme(tintTypedArray.getResourceId(R.styleable.Toolbar_popupTheme, 0));
    Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.Toolbar_navigationIcon);
    if (drawable2 != null)
      setNavigationIcon(drawable2); 
    CharSequence charSequence2 = tintTypedArray.getText(R.styleable.Toolbar_navigationContentDescription);
    if (!TextUtils.isEmpty(charSequence2))
      setNavigationContentDescription(charSequence2); 
    Drawable drawable1 = tintTypedArray.getDrawable(R.styleable.Toolbar_logo);
    if (drawable1 != null)
      setLogo(drawable1); 
    CharSequence charSequence1 = tintTypedArray.getText(R.styleable.Toolbar_logoDescription);
    if (!TextUtils.isEmpty(charSequence1))
      setLogoDescription(charSequence1); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleTextColor))
      setTitleTextColor(tintTypedArray.getColorStateList(R.styleable.Toolbar_titleTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_subtitleTextColor))
      setSubtitleTextColor(tintTypedArray.getColorStateList(R.styleable.Toolbar_subtitleTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.Toolbar_menu, 0)); 
    tintTypedArray.recycle();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    int j = getChildCount();
    i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    paramList.clear();
    if (bool) {
      for (paramInt = j - 1; paramInt >= 0; paramInt--) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } else {
      for (paramInt = 0; paramInt < j; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } 
  }
  
  private void addSystemView(View paramView, boolean paramBoolean) {
    LayoutParams layoutParams;
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (layoutParams1 == null) {
      layoutParams = generateDefaultLayoutParams();
    } else if (!checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
      layoutParams = generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } else {
      layoutParams = layoutParams;
    } 
    layoutParams.mViewType = 1;
    if (paramBoolean && this.mExpandedActionView != null) {
      paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mHiddenViews.add(paramView);
    } else {
      addView(paramView, (ViewGroup.LayoutParams)layoutParams);
    } 
  }
  
  private void ensureContentInsets() {
    if (this.mContentInsets == null)
      this.mContentInsets = new RtlSpacingHelper(); 
  }
  
  private void ensureLogoView() {
    if (this.mLogoView == null)
      this.mLogoView = new AppCompatImageView(getContext()); 
  }
  
  private void ensureMenu() {
    ensureMenuView();
    if (this.mMenuView.peekMenu() == null) {
      MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
      if (this.mExpandedMenuPresenter == null)
        this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
      this.mMenuView.setExpandedActionViewsExclusive(true);
      menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } 
  }
  
  private void ensureMenuView() {
    if (this.mMenuView == null) {
      ActionMenuView actionMenuView = new ActionMenuView(getContext());
      this.mMenuView = actionMenuView;
      actionMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
      this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800005 | this.mButtonGravity & 0x70;
      this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      addSystemView((View)this.mMenuView, false);
    } 
  }
  
  private void ensureNavButtonView() {
    if (this.mNavButtonView == null) {
      this.mNavButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      this.mNavButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
  }
  
  private int getChildHorizontalGravity(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, i) & 0x7;
    switch (paramInt) {
      default:
        if (i == 1)
          return 5; 
        break;
      case 1:
      case 3:
      case 5:
        return paramInt;
    } 
    return 3;
  }
  
  private int getChildTop(View paramView, int paramInt) {
    int i;
    int j;
    int m;
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int k = paramView.getMeasuredHeight();
    if (paramInt > 0) {
      paramInt = (k - paramInt) / 2;
    } else {
      paramInt = 0;
    } 
    switch (getChildVerticalGravity(layoutParams.gravity)) {
      default:
        j = getPaddingTop();
        m = getPaddingBottom();
        paramInt = getHeight();
        i = (paramInt - j - m - k) / 2;
        if (i < layoutParams.topMargin) {
          paramInt = layoutParams.topMargin;
          return j + paramInt;
        } 
        break;
      case 80:
        return getHeight() - getPaddingBottom() - k - layoutParams.bottomMargin - paramInt;
      case 48:
        return getPaddingTop() - paramInt;
    } 
    k = paramInt - m - k - i - j;
    paramInt = i;
    if (k < layoutParams.bottomMargin)
      paramInt = Math.max(0, i - layoutParams.bottomMargin - k); 
    return j + paramInt;
  }
  
  private int getChildVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    switch (paramInt) {
      default:
        return this.mGravity & 0x70;
      case 16:
      case 48:
      case 80:
        break;
    } 
    return paramInt;
  }
  
  private ArrayList<MenuItem> getCurrentMenuItems() {
    ArrayList<MenuItem> arrayList = new ArrayList();
    Menu menu = getMenu();
    for (byte b = 0; b < menu.size(); b++)
      arrayList.add(menu.getItem(b)); 
    return arrayList;
  }
  
  private int getHorizontalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
  }
  
  private MenuInflater getMenuInflater() {
    return (MenuInflater)new SupportMenuInflater(getContext());
  }
  
  private int getVerticalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfint) {
    int k = paramArrayOfint[0];
    int j = paramArrayOfint[1];
    int i = 0;
    int m = paramList.size();
    for (byte b = 0; b < m; b++) {
      View view = paramList.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      k = layoutParams.leftMargin - k;
      j = layoutParams.rightMargin - j;
      int i1 = Math.max(0, k);
      int n = Math.max(0, j);
      k = Math.max(0, -k);
      j = Math.max(0, -j);
      i += view.getMeasuredWidth() + i1 + n;
    } 
    return i;
  }
  
  private boolean isChildOrHidden(View paramView) {
    return (paramView.getParent() == this || this.mHiddenViews.contains(paramView));
  }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.leftMargin - paramArrayOfint[0];
    paramInt1 += Math.max(0, i);
    paramArrayOfint[0] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 + layoutParams.rightMargin + i;
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.rightMargin - paramArrayOfint[1];
    paramInt1 -= Math.max(0, i);
    paramArrayOfint[1] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 - layoutParams.leftMargin + i;
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int j = marginLayoutParams.leftMargin - paramArrayOfint[0];
    int k = marginLayoutParams.rightMargin - paramArrayOfint[1];
    int i = Math.max(0, j) + Math.max(0, k);
    paramArrayOfint[0] = Math.max(0, -j);
    paramArrayOfint[1] = Math.max(0, -k);
    paramInt1 = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + i + paramInt2, marginLayoutParams.width);
    paramView.measure(paramInt1, getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height));
    return paramView.getMeasuredWidth() + i;
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width);
    paramInt2 = getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height);
    paramInt3 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = paramInt2;
    if (paramInt3 != 1073741824) {
      paramInt1 = paramInt2;
      if (paramInt5 >= 0) {
        if (paramInt3 != 0) {
          paramInt1 = Math.min(View.MeasureSpec.getSize(paramInt2), paramInt5);
        } else {
          paramInt1 = paramInt5;
        } 
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      } 
    } 
    paramView.measure(i, paramInt1);
  }
  
  private void onCreateMenu() {
    ArrayList<MenuItem> arrayList1 = getCurrentMenuItems();
    this.mMenuHostHelper.onCreateMenu(getMenu(), getMenuInflater());
    ArrayList<MenuItem> arrayList2 = getCurrentMenuItems();
    arrayList2.removeAll(arrayList1);
    this.mProvidedMenuItems = arrayList2;
  }
  
  private void postShowOverflowMenu() {
    removeCallbacks(this.mShowOverflowMenuRunnable);
    post(this.mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse() {
    if (!this.mCollapsible)
      return false; 
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (shouldLayout(view) && view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0)
        return false; 
    } 
    return true;
  }
  
  private boolean shouldLayout(View paramView) {
    boolean bool;
    if (paramView != null && paramView.getParent() == this && paramView.getVisibility() != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void addChildrenForExpandedActionView() {
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--)
      addView(this.mHiddenViews.get(i)); 
    this.mHiddenViews.clear();
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider) {
    this.mMenuHostHelper.addMenuProvider(paramMenuProvider);
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner) {
    this.mMenuHostHelper.addMenuProvider(paramMenuProvider, paramLifecycleOwner);
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner, Lifecycle.State paramState) {
    this.mMenuHostHelper.addMenuProvider(paramMenuProvider, paramLifecycleOwner, paramState);
  }
  
  public boolean canShowOverflowMenu() {
    if (getVisibility() == 0) {
      ActionMenuView actionMenuView = this.mMenuView;
      if (actionMenuView != null && actionMenuView.isOverflowReserved())
        return true; 
    } 
    return false;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (super.checkLayoutParams(paramLayoutParams) && paramLayoutParams instanceof LayoutParams) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void collapseActionView() {
    MenuItemImpl menuItemImpl;
    ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
    if (expandedActionViewMenuPresenter == null) {
      expandedActionViewMenuPresenter = null;
    } else {
      menuItemImpl = expandedActionViewMenuPresenter.mCurrentExpandedItem;
    } 
    if (menuItemImpl != null)
      menuItemImpl.collapseActionView(); 
  }
  
  public void dismissPopupMenus() {
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null)
      actionMenuView.dismissPopupMenus(); 
  }
  
  void ensureCollapseButtonView() {
    if (this.mCollapseButtonView == null) {
      AppCompatImageButton appCompatImageButton = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      this.mCollapseButtonView = appCompatImageButton;
      appCompatImageButton.setImageDrawable(this.mCollapseIcon);
      this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      layoutParams.mViewType = 2;
      this.mCollapseButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
            final Toolbar this$0;
            
            public void onClick(View param1View) {
              Toolbar.this.collapseActionView();
            }
          });
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ActionBar.LayoutParams) ? new LayoutParams((ActionBar.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)));
  }
  
  public CharSequence getCollapseContentDescription() {
    ImageButton imageButton = this.mCollapseButtonView;
    if (imageButton != null) {
      CharSequence charSequence = imageButton.getContentDescription();
    } else {
      imageButton = null;
    } 
    return (CharSequence)imageButton;
  }
  
  public Drawable getCollapseIcon() {
    ImageButton imageButton = this.mCollapseButtonView;
    if (imageButton != null) {
      Drawable drawable = imageButton.getDrawable();
    } else {
      imageButton = null;
    } 
    return (Drawable)imageButton;
  }
  
  public int getContentInsetEnd() {
    boolean bool;
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (rtlSpacingHelper != null) {
      bool = rtlSpacingHelper.getEnd();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getContentInsetEndWithActions() {
    int i = this.mContentInsetEndWithActions;
    if (i == Integer.MIN_VALUE)
      i = getContentInsetEnd(); 
    return i;
  }
  
  public int getContentInsetLeft() {
    boolean bool;
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (rtlSpacingHelper != null) {
      bool = rtlSpacingHelper.getLeft();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getContentInsetRight() {
    boolean bool;
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (rtlSpacingHelper != null) {
      bool = rtlSpacingHelper.getRight();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getContentInsetStart() {
    boolean bool;
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (rtlSpacingHelper != null) {
      bool = rtlSpacingHelper.getStart();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getContentInsetStartWithNavigation() {
    int i = this.mContentInsetStartWithNavigation;
    if (i == Integer.MIN_VALUE)
      i = getContentInsetStart(); 
    return i;
  }
  
  public int getCurrentContentInsetEnd() {
    int i = 0;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null) {
      MenuBuilder menuBuilder = actionMenuView.peekMenu();
      if (menuBuilder != null && menuBuilder.hasVisibleItems()) {
        i = 1;
      } else {
        i = 0;
      } 
    } 
    if (i) {
      i = Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
    } else {
      i = getContentInsetEnd();
    } 
    return i;
  }
  
  public int getCurrentContentInsetLeft() {
    int i;
    if (ViewCompat.getLayoutDirection((View)this) == 1) {
      i = getCurrentContentInsetEnd();
    } else {
      i = getCurrentContentInsetStart();
    } 
    return i;
  }
  
  public int getCurrentContentInsetRight() {
    int i;
    if (ViewCompat.getLayoutDirection((View)this) == 1) {
      i = getCurrentContentInsetStart();
    } else {
      i = getCurrentContentInsetEnd();
    } 
    return i;
  }
  
  public int getCurrentContentInsetStart() {
    int i;
    if (getNavigationIcon() != null) {
      i = Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
    } else {
      i = getContentInsetStart();
    } 
    return i;
  }
  
  public Drawable getLogo() {
    ImageView imageView = this.mLogoView;
    if (imageView != null) {
      Drawable drawable = imageView.getDrawable();
    } else {
      imageView = null;
    } 
    return (Drawable)imageView;
  }
  
  public CharSequence getLogoDescription() {
    ImageView imageView = this.mLogoView;
    if (imageView != null) {
      CharSequence charSequence = imageView.getContentDescription();
    } else {
      imageView = null;
    } 
    return (CharSequence)imageView;
  }
  
  public Menu getMenu() {
    ensureMenu();
    return this.mMenuView.getMenu();
  }
  
  View getNavButtonView() {
    return (View)this.mNavButtonView;
  }
  
  public CharSequence getNavigationContentDescription() {
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null) {
      CharSequence charSequence = imageButton.getContentDescription();
    } else {
      imageButton = null;
    } 
    return (CharSequence)imageButton;
  }
  
  public Drawable getNavigationIcon() {
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null) {
      Drawable drawable = imageButton.getDrawable();
    } else {
      imageButton = null;
    } 
    return (Drawable)imageButton;
  }
  
  ActionMenuPresenter getOuterActionMenuPresenter() {
    return this.mOuterActionMenuPresenter;
  }
  
  public Drawable getOverflowIcon() {
    ensureMenu();
    return this.mMenuView.getOverflowIcon();
  }
  
  Context getPopupContext() {
    return this.mPopupContext;
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public CharSequence getSubtitle() {
    return this.mSubtitleText;
  }
  
  final TextView getSubtitleTextView() {
    return this.mSubtitleTextView;
  }
  
  public CharSequence getTitle() {
    return this.mTitleText;
  }
  
  public int getTitleMarginBottom() {
    return this.mTitleMarginBottom;
  }
  
  public int getTitleMarginEnd() {
    return this.mTitleMarginEnd;
  }
  
  public int getTitleMarginStart() {
    return this.mTitleMarginStart;
  }
  
  public int getTitleMarginTop() {
    return this.mTitleMarginTop;
  }
  
  final TextView getTitleTextView() {
    return this.mTitleTextView;
  }
  
  public DecorToolbar getWrapper() {
    if (this.mWrapper == null)
      this.mWrapper = new ToolbarWidgetWrapper(this, true); 
    return this.mWrapper;
  }
  
  public boolean hasExpandedActionView() {
    boolean bool;
    ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
    if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    boolean bool;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null && actionMenuView.hideOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void inflateMenu(int paramInt) {
    getMenuInflater().inflate(paramInt, getMenu());
  }
  
  public void invalidateMenu() {
    for (MenuItem menuItem : this.mProvidedMenuItems)
      getMenu().removeItem(menuItem.getItemId()); 
    onCreateMenu();
  }
  
  public boolean isOverflowMenuShowPending() {
    boolean bool;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null && actionMenuView.isOverflowMenuShowPending()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null && actionMenuView.isOverflowMenuShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isTitleTruncated() {
    TextView textView = this.mTitleTextView;
    if (textView == null)
      return false; 
    Layout layout = textView.getLayout();
    if (layout == null)
      return false; 
    int i = layout.getLineCount();
    for (byte b = 0; b < i; b++) {
      if (layout.getEllipsisCount(b) > 0)
        return true; 
    } 
    return false;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeCallbacks(this.mShowOverflowMenuRunnable);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 9)
      this.mEatingHover = false; 
    if (!this.mEatingHover) {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if (i == 9 && !bool)
        this.mEatingHover = true; 
    } 
    if (i == 10 || i == 3)
      this.mEatingHover = false; 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   4: iconst_1
    //   5: if_icmpne -> 14
    //   8: iconst_1
    //   9: istore #7
    //   11: goto -> 17
    //   14: iconst_0
    //   15: istore #7
    //   17: aload_0
    //   18: invokevirtual getWidth : ()I
    //   21: istore #11
    //   23: aload_0
    //   24: invokevirtual getHeight : ()I
    //   27: istore #14
    //   29: aload_0
    //   30: invokevirtual getPaddingLeft : ()I
    //   33: istore #10
    //   35: aload_0
    //   36: invokevirtual getPaddingRight : ()I
    //   39: istore #12
    //   41: aload_0
    //   42: invokevirtual getPaddingTop : ()I
    //   45: istore #13
    //   47: aload_0
    //   48: invokevirtual getPaddingBottom : ()I
    //   51: istore #15
    //   53: iload #10
    //   55: istore #4
    //   57: iload #11
    //   59: iload #12
    //   61: isub
    //   62: istore #8
    //   64: aload_0
    //   65: getfield mTempMargins : [I
    //   68: astore #19
    //   70: aload #19
    //   72: iconst_1
    //   73: iconst_0
    //   74: iastore
    //   75: aload #19
    //   77: iconst_0
    //   78: iconst_0
    //   79: iastore
    //   80: aload_0
    //   81: invokestatic getMinimumHeight : (Landroid/view/View;)I
    //   84: istore_2
    //   85: iload_2
    //   86: iflt -> 102
    //   89: iload_2
    //   90: iload #5
    //   92: iload_3
    //   93: isub
    //   94: invokestatic min : (II)I
    //   97: istore #6
    //   99: goto -> 105
    //   102: iconst_0
    //   103: istore #6
    //   105: iload #4
    //   107: istore #5
    //   109: iload #8
    //   111: istore_2
    //   112: aload_0
    //   113: aload_0
    //   114: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   117: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   120: ifeq -> 169
    //   123: iload #7
    //   125: ifeq -> 150
    //   128: aload_0
    //   129: aload_0
    //   130: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   133: iload #8
    //   135: aload #19
    //   137: iload #6
    //   139: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   142: istore_2
    //   143: iload #4
    //   145: istore #5
    //   147: goto -> 169
    //   150: aload_0
    //   151: aload_0
    //   152: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   155: iload #4
    //   157: aload #19
    //   159: iload #6
    //   161: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   164: istore #5
    //   166: iload #8
    //   168: istore_2
    //   169: iload #5
    //   171: istore #4
    //   173: iload_2
    //   174: istore_3
    //   175: aload_0
    //   176: aload_0
    //   177: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   180: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   183: ifeq -> 230
    //   186: iload #7
    //   188: ifeq -> 212
    //   191: aload_0
    //   192: aload_0
    //   193: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   196: iload_2
    //   197: aload #19
    //   199: iload #6
    //   201: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   204: istore_3
    //   205: iload #5
    //   207: istore #4
    //   209: goto -> 230
    //   212: aload_0
    //   213: aload_0
    //   214: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   217: iload #5
    //   219: aload #19
    //   221: iload #6
    //   223: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   226: istore #4
    //   228: iload_2
    //   229: istore_3
    //   230: iload #4
    //   232: istore #5
    //   234: iload_3
    //   235: istore_2
    //   236: aload_0
    //   237: aload_0
    //   238: getfield mMenuView : Landroidx/appcompat/widget/ActionMenuView;
    //   241: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   244: ifeq -> 291
    //   247: iload #7
    //   249: ifeq -> 273
    //   252: aload_0
    //   253: aload_0
    //   254: getfield mMenuView : Landroidx/appcompat/widget/ActionMenuView;
    //   257: iload #4
    //   259: aload #19
    //   261: iload #6
    //   263: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   266: istore #5
    //   268: iload_3
    //   269: istore_2
    //   270: goto -> 291
    //   273: aload_0
    //   274: aload_0
    //   275: getfield mMenuView : Landroidx/appcompat/widget/ActionMenuView;
    //   278: iload_3
    //   279: aload #19
    //   281: iload #6
    //   283: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   286: istore_2
    //   287: iload #4
    //   289: istore #5
    //   291: aload_0
    //   292: invokevirtual getCurrentContentInsetLeft : ()I
    //   295: istore_3
    //   296: aload_0
    //   297: invokevirtual getCurrentContentInsetRight : ()I
    //   300: istore #4
    //   302: aload #19
    //   304: iconst_0
    //   305: iconst_0
    //   306: iload_3
    //   307: iload #5
    //   309: isub
    //   310: invokestatic max : (II)I
    //   313: iastore
    //   314: aload #19
    //   316: iconst_1
    //   317: iconst_0
    //   318: iload #4
    //   320: iload #11
    //   322: iload #12
    //   324: isub
    //   325: iload_2
    //   326: isub
    //   327: isub
    //   328: invokestatic max : (II)I
    //   331: iastore
    //   332: iload #5
    //   334: iload_3
    //   335: invokestatic max : (II)I
    //   338: istore_3
    //   339: iload_2
    //   340: iload #11
    //   342: iload #12
    //   344: isub
    //   345: iload #4
    //   347: isub
    //   348: invokestatic min : (II)I
    //   351: istore #4
    //   353: iload_3
    //   354: istore #5
    //   356: iload #4
    //   358: istore_2
    //   359: aload_0
    //   360: aload_0
    //   361: getfield mExpandedActionView : Landroid/view/View;
    //   364: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   367: ifeq -> 414
    //   370: iload #7
    //   372: ifeq -> 396
    //   375: aload_0
    //   376: aload_0
    //   377: getfield mExpandedActionView : Landroid/view/View;
    //   380: iload #4
    //   382: aload #19
    //   384: iload #6
    //   386: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   389: istore_2
    //   390: iload_3
    //   391: istore #5
    //   393: goto -> 414
    //   396: aload_0
    //   397: aload_0
    //   398: getfield mExpandedActionView : Landroid/view/View;
    //   401: iload_3
    //   402: aload #19
    //   404: iload #6
    //   406: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   409: istore #5
    //   411: iload #4
    //   413: istore_2
    //   414: iload #5
    //   416: istore #4
    //   418: iload_2
    //   419: istore_3
    //   420: aload_0
    //   421: aload_0
    //   422: getfield mLogoView : Landroid/widget/ImageView;
    //   425: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   428: ifeq -> 475
    //   431: iload #7
    //   433: ifeq -> 457
    //   436: aload_0
    //   437: aload_0
    //   438: getfield mLogoView : Landroid/widget/ImageView;
    //   441: iload_2
    //   442: aload #19
    //   444: iload #6
    //   446: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   449: istore_3
    //   450: iload #5
    //   452: istore #4
    //   454: goto -> 475
    //   457: aload_0
    //   458: aload_0
    //   459: getfield mLogoView : Landroid/widget/ImageView;
    //   462: iload #5
    //   464: aload #19
    //   466: iload #6
    //   468: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   471: istore #4
    //   473: iload_2
    //   474: istore_3
    //   475: aload_0
    //   476: aload_0
    //   477: getfield mTitleTextView : Landroid/widget/TextView;
    //   480: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   483: istore_1
    //   484: aload_0
    //   485: aload_0
    //   486: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   489: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   492: istore #16
    //   494: iconst_0
    //   495: istore_2
    //   496: iload_1
    //   497: ifeq -> 537
    //   500: aload_0
    //   501: getfield mTitleTextView : Landroid/widget/TextView;
    //   504: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   507: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   510: astore #17
    //   512: iconst_0
    //   513: aload #17
    //   515: getfield topMargin : I
    //   518: aload_0
    //   519: getfield mTitleTextView : Landroid/widget/TextView;
    //   522: invokevirtual getMeasuredHeight : ()I
    //   525: iadd
    //   526: aload #17
    //   528: getfield bottomMargin : I
    //   531: iadd
    //   532: iadd
    //   533: istore_2
    //   534: goto -> 537
    //   537: iload_2
    //   538: istore #8
    //   540: iload #16
    //   542: ifeq -> 580
    //   545: aload_0
    //   546: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   549: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   552: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   555: astore #17
    //   557: iload_2
    //   558: aload #17
    //   560: getfield topMargin : I
    //   563: aload_0
    //   564: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   567: invokevirtual getMeasuredHeight : ()I
    //   570: iadd
    //   571: aload #17
    //   573: getfield bottomMargin : I
    //   576: iadd
    //   577: iadd
    //   578: istore #8
    //   580: iload_1
    //   581: ifne -> 598
    //   584: iload #16
    //   586: ifeq -> 592
    //   589: goto -> 598
    //   592: iload #4
    //   594: istore_2
    //   595: goto -> 1359
    //   598: iload_1
    //   599: ifeq -> 611
    //   602: aload_0
    //   603: getfield mTitleTextView : Landroid/widget/TextView;
    //   606: astore #17
    //   608: goto -> 617
    //   611: aload_0
    //   612: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   615: astore #17
    //   617: iload #16
    //   619: ifeq -> 631
    //   622: aload_0
    //   623: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   626: astore #18
    //   628: goto -> 637
    //   631: aload_0
    //   632: getfield mTitleTextView : Landroid/widget/TextView;
    //   635: astore #18
    //   637: aload #17
    //   639: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   642: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   645: astore #17
    //   647: aload #18
    //   649: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   652: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   655: astore #18
    //   657: iload_1
    //   658: ifeq -> 674
    //   661: aload_0
    //   662: getfield mTitleTextView : Landroid/widget/TextView;
    //   665: invokevirtual getMeasuredWidth : ()I
    //   668: ifgt -> 689
    //   671: goto -> 674
    //   674: iload #16
    //   676: ifeq -> 695
    //   679: aload_0
    //   680: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   683: invokevirtual getMeasuredWidth : ()I
    //   686: ifle -> 695
    //   689: iconst_1
    //   690: istore #5
    //   692: goto -> 698
    //   695: iconst_0
    //   696: istore #5
    //   698: aload_0
    //   699: getfield mGravity : I
    //   702: bipush #112
    //   704: iand
    //   705: lookupswitch default -> 732, 48 -> 799, 80 -> 776
    //   732: iload #14
    //   734: iload #13
    //   736: isub
    //   737: iload #15
    //   739: isub
    //   740: iload #8
    //   742: isub
    //   743: iconst_2
    //   744: idiv
    //   745: istore #9
    //   747: iload #9
    //   749: aload #17
    //   751: getfield topMargin : I
    //   754: aload_0
    //   755: getfield mTitleMarginTop : I
    //   758: iadd
    //   759: if_icmpge -> 818
    //   762: aload #17
    //   764: getfield topMargin : I
    //   767: aload_0
    //   768: getfield mTitleMarginTop : I
    //   771: iadd
    //   772: istore_2
    //   773: goto -> 873
    //   776: iload #14
    //   778: iload #15
    //   780: isub
    //   781: aload #18
    //   783: getfield bottomMargin : I
    //   786: isub
    //   787: aload_0
    //   788: getfield mTitleMarginBottom : I
    //   791: isub
    //   792: iload #8
    //   794: isub
    //   795: istore_2
    //   796: goto -> 878
    //   799: aload_0
    //   800: invokevirtual getPaddingTop : ()I
    //   803: aload #17
    //   805: getfield topMargin : I
    //   808: iadd
    //   809: aload_0
    //   810: getfield mTitleMarginTop : I
    //   813: iadd
    //   814: istore_2
    //   815: goto -> 878
    //   818: iload #14
    //   820: iload #15
    //   822: isub
    //   823: iload #8
    //   825: isub
    //   826: iload #9
    //   828: isub
    //   829: iload #13
    //   831: isub
    //   832: istore #8
    //   834: iload #9
    //   836: istore_2
    //   837: iload #8
    //   839: aload #17
    //   841: getfield bottomMargin : I
    //   844: aload_0
    //   845: getfield mTitleMarginBottom : I
    //   848: iadd
    //   849: if_icmpge -> 873
    //   852: iconst_0
    //   853: iload #9
    //   855: aload #18
    //   857: getfield bottomMargin : I
    //   860: aload_0
    //   861: getfield mTitleMarginBottom : I
    //   864: iadd
    //   865: iload #8
    //   867: isub
    //   868: isub
    //   869: invokestatic max : (II)I
    //   872: istore_2
    //   873: iload #13
    //   875: iload_2
    //   876: iadd
    //   877: istore_2
    //   878: iload #7
    //   880: ifeq -> 1119
    //   883: iload #5
    //   885: ifeq -> 897
    //   888: aload_0
    //   889: getfield mTitleMarginStart : I
    //   892: istore #7
    //   894: goto -> 900
    //   897: iconst_0
    //   898: istore #7
    //   900: iload #7
    //   902: aload #19
    //   904: iconst_1
    //   905: iaload
    //   906: isub
    //   907: istore #7
    //   909: iload_3
    //   910: iconst_0
    //   911: iload #7
    //   913: invokestatic max : (II)I
    //   916: isub
    //   917: istore_3
    //   918: aload #19
    //   920: iconst_1
    //   921: iconst_0
    //   922: iload #7
    //   924: ineg
    //   925: invokestatic max : (II)I
    //   928: iastore
    //   929: iload_3
    //   930: istore #8
    //   932: iload_3
    //   933: istore #7
    //   935: iload_1
    //   936: ifeq -> 1009
    //   939: aload_0
    //   940: getfield mTitleTextView : Landroid/widget/TextView;
    //   943: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   946: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   949: astore #17
    //   951: iload #8
    //   953: aload_0
    //   954: getfield mTitleTextView : Landroid/widget/TextView;
    //   957: invokevirtual getMeasuredWidth : ()I
    //   960: isub
    //   961: istore #13
    //   963: aload_0
    //   964: getfield mTitleTextView : Landroid/widget/TextView;
    //   967: invokevirtual getMeasuredHeight : ()I
    //   970: iload_2
    //   971: iadd
    //   972: istore #9
    //   974: aload_0
    //   975: getfield mTitleTextView : Landroid/widget/TextView;
    //   978: iload #13
    //   980: iload_2
    //   981: iload #8
    //   983: iload #9
    //   985: invokevirtual layout : (IIII)V
    //   988: iload #13
    //   990: aload_0
    //   991: getfield mTitleMarginEnd : I
    //   994: isub
    //   995: istore_2
    //   996: iload #9
    //   998: aload #17
    //   1000: getfield bottomMargin : I
    //   1003: iadd
    //   1004: istore #9
    //   1006: goto -> 1015
    //   1009: iload_2
    //   1010: istore #9
    //   1012: iload #8
    //   1014: istore_2
    //   1015: iload #7
    //   1017: istore #8
    //   1019: iload #16
    //   1021: ifeq -> 1101
    //   1024: aload_0
    //   1025: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1028: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1031: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   1034: astore #17
    //   1036: iload #9
    //   1038: aload #17
    //   1040: getfield topMargin : I
    //   1043: iadd
    //   1044: istore #9
    //   1046: aload_0
    //   1047: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1050: invokevirtual getMeasuredWidth : ()I
    //   1053: istore #8
    //   1055: aload_0
    //   1056: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1059: invokevirtual getMeasuredHeight : ()I
    //   1062: iload #9
    //   1064: iadd
    //   1065: istore #13
    //   1067: aload_0
    //   1068: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1071: iload #7
    //   1073: iload #8
    //   1075: isub
    //   1076: iload #9
    //   1078: iload #7
    //   1080: iload #13
    //   1082: invokevirtual layout : (IIII)V
    //   1085: iload #7
    //   1087: aload_0
    //   1088: getfield mTitleMarginEnd : I
    //   1091: isub
    //   1092: istore #8
    //   1094: aload #17
    //   1096: getfield bottomMargin : I
    //   1099: istore #7
    //   1101: iload #5
    //   1103: ifeq -> 1113
    //   1106: iload_2
    //   1107: iload #8
    //   1109: invokestatic min : (II)I
    //   1112: istore_3
    //   1113: iload #4
    //   1115: istore_2
    //   1116: goto -> 1359
    //   1119: iload #5
    //   1121: ifeq -> 1133
    //   1124: aload_0
    //   1125: getfield mTitleMarginStart : I
    //   1128: istore #7
    //   1130: goto -> 1136
    //   1133: iconst_0
    //   1134: istore #7
    //   1136: iload #7
    //   1138: aload #19
    //   1140: iconst_0
    //   1141: iaload
    //   1142: isub
    //   1143: istore #7
    //   1145: iload #4
    //   1147: iconst_0
    //   1148: iload #7
    //   1150: invokestatic max : (II)I
    //   1153: iadd
    //   1154: istore #4
    //   1156: aload #19
    //   1158: iconst_0
    //   1159: iconst_0
    //   1160: iload #7
    //   1162: ineg
    //   1163: invokestatic max : (II)I
    //   1166: iastore
    //   1167: iload #4
    //   1169: istore #8
    //   1171: iload #4
    //   1173: istore #7
    //   1175: iload_1
    //   1176: ifeq -> 1249
    //   1179: aload_0
    //   1180: getfield mTitleTextView : Landroid/widget/TextView;
    //   1183: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1186: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   1189: astore #17
    //   1191: aload_0
    //   1192: getfield mTitleTextView : Landroid/widget/TextView;
    //   1195: invokevirtual getMeasuredWidth : ()I
    //   1198: iload #8
    //   1200: iadd
    //   1201: istore #13
    //   1203: aload_0
    //   1204: getfield mTitleTextView : Landroid/widget/TextView;
    //   1207: invokevirtual getMeasuredHeight : ()I
    //   1210: iload_2
    //   1211: iadd
    //   1212: istore #9
    //   1214: aload_0
    //   1215: getfield mTitleTextView : Landroid/widget/TextView;
    //   1218: iload #8
    //   1220: iload_2
    //   1221: iload #13
    //   1223: iload #9
    //   1225: invokevirtual layout : (IIII)V
    //   1228: iload #13
    //   1230: aload_0
    //   1231: getfield mTitleMarginEnd : I
    //   1234: iadd
    //   1235: istore_2
    //   1236: iload #9
    //   1238: aload #17
    //   1240: getfield bottomMargin : I
    //   1243: iadd
    //   1244: istore #9
    //   1246: goto -> 1255
    //   1249: iload_2
    //   1250: istore #9
    //   1252: iload #8
    //   1254: istore_2
    //   1255: iload #7
    //   1257: istore #8
    //   1259: iload #16
    //   1261: ifeq -> 1341
    //   1264: aload_0
    //   1265: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1268: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1271: checkcast androidx/appcompat/widget/Toolbar$LayoutParams
    //   1274: astore #17
    //   1276: iload #9
    //   1278: aload #17
    //   1280: getfield topMargin : I
    //   1283: iadd
    //   1284: istore #9
    //   1286: aload_0
    //   1287: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1290: invokevirtual getMeasuredWidth : ()I
    //   1293: iload #7
    //   1295: iadd
    //   1296: istore #13
    //   1298: aload_0
    //   1299: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1302: invokevirtual getMeasuredHeight : ()I
    //   1305: iload #9
    //   1307: iadd
    //   1308: istore #8
    //   1310: aload_0
    //   1311: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1314: iload #7
    //   1316: iload #9
    //   1318: iload #13
    //   1320: iload #8
    //   1322: invokevirtual layout : (IIII)V
    //   1325: iload #13
    //   1327: aload_0
    //   1328: getfield mTitleMarginEnd : I
    //   1331: iadd
    //   1332: istore #8
    //   1334: aload #17
    //   1336: getfield bottomMargin : I
    //   1339: istore #7
    //   1341: iload #5
    //   1343: ifeq -> 1356
    //   1346: iload_2
    //   1347: iload #8
    //   1349: invokestatic max : (II)I
    //   1352: istore_2
    //   1353: goto -> 1359
    //   1356: iload #4
    //   1358: istore_2
    //   1359: aload_0
    //   1360: aload_0
    //   1361: getfield mTempViews : Ljava/util/ArrayList;
    //   1364: iconst_3
    //   1365: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1368: aload_0
    //   1369: getfield mTempViews : Ljava/util/ArrayList;
    //   1372: invokevirtual size : ()I
    //   1375: istore #5
    //   1377: iconst_0
    //   1378: istore #4
    //   1380: iload #4
    //   1382: iload #5
    //   1384: if_icmpge -> 1415
    //   1387: aload_0
    //   1388: aload_0
    //   1389: getfield mTempViews : Ljava/util/ArrayList;
    //   1392: iload #4
    //   1394: invokevirtual get : (I)Ljava/lang/Object;
    //   1397: checkcast android/view/View
    //   1400: iload_2
    //   1401: aload #19
    //   1403: iload #6
    //   1405: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   1408: istore_2
    //   1409: iinc #4, 1
    //   1412: goto -> 1380
    //   1415: aload_0
    //   1416: aload_0
    //   1417: getfield mTempViews : Ljava/util/ArrayList;
    //   1420: iconst_5
    //   1421: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1424: aload_0
    //   1425: getfield mTempViews : Ljava/util/ArrayList;
    //   1428: invokevirtual size : ()I
    //   1431: istore #7
    //   1433: iconst_0
    //   1434: istore #5
    //   1436: iload_3
    //   1437: istore #4
    //   1439: iload #5
    //   1441: istore_3
    //   1442: iload_3
    //   1443: iload #7
    //   1445: if_icmpge -> 1477
    //   1448: aload_0
    //   1449: aload_0
    //   1450: getfield mTempViews : Ljava/util/ArrayList;
    //   1453: iload_3
    //   1454: invokevirtual get : (I)Ljava/lang/Object;
    //   1457: checkcast android/view/View
    //   1460: iload #4
    //   1462: aload #19
    //   1464: iload #6
    //   1466: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   1469: istore #4
    //   1471: iinc #3, 1
    //   1474: goto -> 1442
    //   1477: aload_0
    //   1478: aload_0
    //   1479: getfield mTempViews : Ljava/util/ArrayList;
    //   1482: iconst_1
    //   1483: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1486: aload_0
    //   1487: aload_0
    //   1488: getfield mTempViews : Ljava/util/ArrayList;
    //   1491: aload #19
    //   1493: invokespecial getViewListMeasuredWidth : (Ljava/util/List;[I)I
    //   1496: istore_3
    //   1497: iload #10
    //   1499: iload #11
    //   1501: iload #10
    //   1503: isub
    //   1504: iload #12
    //   1506: isub
    //   1507: iconst_2
    //   1508: idiv
    //   1509: iadd
    //   1510: iload_3
    //   1511: iconst_2
    //   1512: idiv
    //   1513: isub
    //   1514: istore #5
    //   1516: iload #5
    //   1518: iload_3
    //   1519: iadd
    //   1520: istore #7
    //   1522: iload #5
    //   1524: iload_2
    //   1525: if_icmpge -> 1533
    //   1528: iload_2
    //   1529: istore_3
    //   1530: goto -> 1552
    //   1533: iload #5
    //   1535: istore_3
    //   1536: iload #7
    //   1538: iload #4
    //   1540: if_icmple -> 1552
    //   1543: iload #5
    //   1545: iload #7
    //   1547: iload #4
    //   1549: isub
    //   1550: isub
    //   1551: istore_3
    //   1552: aload_0
    //   1553: getfield mTempViews : Ljava/util/ArrayList;
    //   1556: invokevirtual size : ()I
    //   1559: istore #7
    //   1561: iconst_0
    //   1562: istore #5
    //   1564: iload_3
    //   1565: istore #4
    //   1567: iload #5
    //   1569: istore_3
    //   1570: iload_3
    //   1571: iload #7
    //   1573: if_icmpge -> 1605
    //   1576: aload_0
    //   1577: aload_0
    //   1578: getfield mTempViews : Ljava/util/ArrayList;
    //   1581: iload_3
    //   1582: invokevirtual get : (I)Ljava/lang/Object;
    //   1585: checkcast android/view/View
    //   1588: iload #4
    //   1590: aload #19
    //   1592: iload #6
    //   1594: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   1597: istore #4
    //   1599: iinc #3, 1
    //   1602: goto -> 1570
    //   1605: aload_0
    //   1606: getfield mTempViews : Ljava/util/ArrayList;
    //   1609: invokevirtual clear : ()V
    //   1612: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m = 0;
    int k = 0;
    int[] arrayOfInt = this.mTempMargins;
    if (ViewUtils.isLayoutRtl((View)this)) {
      i2 = 1;
      i1 = 0;
    } else {
      i2 = 0;
      i1 = 1;
    } 
    int n = 0;
    if (shouldLayout((View)this.mNavButtonView)) {
      measureChildConstrained((View)this.mNavButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mNavButtonView);
      m = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mNavButtonView));
      k = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
    } 
    int j = m;
    int i = k;
    if (shouldLayout((View)this.mCollapseButtonView)) {
      measureChildConstrained((View)this.mCollapseButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mCollapseButtonView);
      j = Math.max(m, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mCollapseButtonView));
      i = View.combineMeasuredStates(k, this.mCollapseButtonView.getMeasuredState());
    } 
    k = getCurrentContentInsetStart();
    m = 0 + Math.max(k, n);
    arrayOfInt[i2] = Math.max(0, k - n);
    if (shouldLayout((View)this.mMenuView)) {
      measureChildConstrained((View)this.mMenuView, paramInt1, m, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mMenuView.getMeasuredWidth();
      k = getHorizontalMargins((View)this.mMenuView);
      j = Math.max(j, this.mMenuView.getMeasuredHeight() + getVerticalMargins((View)this.mMenuView));
      i = View.combineMeasuredStates(i, this.mMenuView.getMeasuredState());
      k = n + k;
    } else {
      k = 0;
    } 
    int i2 = getCurrentContentInsetEnd();
    n = m + Math.max(i2, k);
    arrayOfInt[i1] = Math.max(0, i2 - k);
    if (shouldLayout(this.mExpandedActionView)) {
      i1 = n + measureChildCollapseMargins(this.mExpandedActionView, paramInt1, n, paramInt2, 0, arrayOfInt);
      m = Math.max(j, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
      i = View.combineMeasuredStates(i, this.mExpandedActionView.getMeasuredState());
    } else {
      m = j;
      i1 = n;
    } 
    k = i1;
    n = m;
    j = i;
    if (shouldLayout((View)this.mLogoView)) {
      k = i1 + measureChildCollapseMargins((View)this.mLogoView, paramInt1, i1, paramInt2, 0, arrayOfInt);
      n = Math.max(m, this.mLogoView.getMeasuredHeight() + getVerticalMargins((View)this.mLogoView));
      j = View.combineMeasuredStates(i, this.mLogoView.getMeasuredState());
    } 
    int i1 = getChildCount();
    i = j;
    j = 0;
    m = k;
    k = i1;
    while (j < k) {
      View view = getChildAt(j);
      if (((LayoutParams)view.getLayoutParams()).mViewType == 0 && shouldLayout(view)) {
        m += measureChildCollapseMargins(view, paramInt1, m, paramInt2, 0, arrayOfInt);
        n = Math.max(n, view.getMeasuredHeight() + getVerticalMargins(view));
        i = View.combineMeasuredStates(i, view.getMeasuredState());
      } 
      j++;
    } 
    i1 = i;
    k = 0;
    j = 0;
    int i3 = this.mTitleMarginTop + this.mTitleMarginBottom;
    i2 = this.mTitleMarginStart + this.mTitleMarginEnd;
    i = i1;
    if (shouldLayout((View)this.mTitleTextView)) {
      measureChildCollapseMargins((View)this.mTitleTextView, paramInt1, m + i2, paramInt2, i3, arrayOfInt);
      k = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins((View)this.mTitleTextView);
      j = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins((View)this.mTitleTextView);
      i = View.combineMeasuredStates(i1, this.mTitleTextView.getMeasuredState());
    } 
    if (shouldLayout((View)this.mSubtitleTextView)) {
      k = Math.max(k, measureChildCollapseMargins((View)this.mSubtitleTextView, paramInt1, m + i2, paramInt2, j + i3, arrayOfInt));
      i2 = this.mSubtitleTextView.getMeasuredHeight();
      i1 = getVerticalMargins((View)this.mSubtitleTextView);
      i = View.combineMeasuredStates(i, this.mSubtitleTextView.getMeasuredState());
      j += i2 + i1;
    } 
    j = Math.max(n, j);
    i3 = getPaddingLeft();
    i2 = getPaddingRight();
    n = getPaddingTop();
    i1 = getPaddingBottom();
    k = View.resolveSizeAndState(Math.max(m + k + i3 + i2, getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & i);
    paramInt1 = View.resolveSizeAndState(Math.max(j + n + i1, getSuggestedMinimumHeight()), paramInt2, i << 16);
    if (shouldCollapse())
      paramInt1 = 0; 
    setMeasuredDimension(k, paramInt1);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null) {
      MenuBuilder menuBuilder = actionMenuView.peekMenu();
    } else {
      actionMenuView = null;
    } 
    if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && actionMenuView != null) {
      MenuItem menuItem = actionMenuView.findItem(savedState.expandedMenuItemId);
      if (menuItem != null)
        menuItem.expandActionView(); 
    } 
    if (savedState.isOverflowOpen)
      postShowOverflowMenu(); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      super.onRtlPropertiesChanged(paramInt); 
    ensureContentInsets();
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    rtlSpacingHelper.setDirection(bool);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
    if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null)
      savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId(); 
    savedState.isOverflowOpen = isOverflowMenuShowing();
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      this.mEatingTouch = false; 
    if (!this.mEatingTouch) {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (i == 0 && !bool)
        this.mEatingTouch = true; 
    } 
    if (i == 1 || i == 3)
      this.mEatingTouch = false; 
    return true;
  }
  
  void removeChildrenForExpandedActionView() {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      View view = getChildAt(i);
      if (((LayoutParams)view.getLayoutParams()).mViewType != 2 && view != this.mMenuView) {
        removeViewAt(i);
        this.mHiddenViews.add(view);
      } 
    } 
  }
  
  public void removeMenuProvider(MenuProvider paramMenuProvider) {
    this.mMenuHostHelper.removeMenuProvider(paramMenuProvider);
  }
  
  public void setCollapseContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getContext().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setCollapseContentDescription(charSequence);
  }
  
  public void setCollapseContentDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureCollapseButtonView(); 
    ImageButton imageButton = this.mCollapseButtonView;
    if (imageButton != null)
      imageButton.setContentDescription(paramCharSequence); 
  }
  
  public void setCollapseIcon(int paramInt) {
    setCollapseIcon(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setCollapseIcon(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureCollapseButtonView();
      this.mCollapseButtonView.setImageDrawable(paramDrawable);
    } else {
      ImageButton imageButton = this.mCollapseButtonView;
      if (imageButton != null)
        imageButton.setImageDrawable(this.mCollapseIcon); 
    } 
  }
  
  public void setCollapsible(boolean paramBoolean) {
    this.mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetEndWithActions(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetEndWithActions) {
      this.mContentInsetEndWithActions = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetStartWithNavigation(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetStartWithNavigation) {
      this.mContentInsetStartWithNavigation = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(int paramInt) {
    setLogo(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setLogo(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureLogoView();
      if (!isChildOrHidden((View)this.mLogoView))
        addSystemView((View)this.mLogoView, true); 
    } else {
      ImageView imageView1 = this.mLogoView;
      if (imageView1 != null && isChildOrHidden((View)imageView1)) {
        removeView((View)this.mLogoView);
        this.mHiddenViews.remove(this.mLogoView);
      } 
    } 
    ImageView imageView = this.mLogoView;
    if (imageView != null)
      imageView.setImageDrawable(paramDrawable); 
  }
  
  public void setLogoDescription(int paramInt) {
    setLogoDescription(getContext().getText(paramInt));
  }
  
  public void setLogoDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureLogoView(); 
    ImageView imageView = this.mLogoView;
    if (imageView != null)
      imageView.setContentDescription(paramCharSequence); 
  }
  
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter) {
    if (paramMenuBuilder == null && this.mMenuView == null)
      return; 
    ensureMenuView();
    MenuBuilder menuBuilder = this.mMenuView.peekMenu();
    if (menuBuilder == paramMenuBuilder)
      return; 
    if (menuBuilder != null) {
      menuBuilder.removeMenuPresenter((MenuPresenter)this.mOuterActionMenuPresenter);
      menuBuilder.removeMenuPresenter(this.mExpandedMenuPresenter);
    } 
    if (this.mExpandedMenuPresenter == null)
      this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
    paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
    if (paramMenuBuilder != null) {
      paramMenuBuilder.addMenuPresenter((MenuPresenter)paramActionMenuPresenter, this.mPopupContext);
      paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } else {
      paramActionMenuPresenter.initForMenu(this.mPopupContext, null);
      this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
      paramActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
    } 
    this.mMenuView.setPopupTheme(this.mPopupTheme);
    this.mMenuView.setPresenter(paramActionMenuPresenter);
    this.mOuterActionMenuPresenter = paramActionMenuPresenter;
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null)
      actionMenuView.setMenuCallbacks(paramCallback, paramCallback1); 
  }
  
  public void setNavigationContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getContext().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setNavigationContentDescription(charSequence);
  }
  
  public void setNavigationContentDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureNavButtonView(); 
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null) {
      imageButton.setContentDescription(paramCharSequence);
      TooltipCompat.setTooltipText((View)this.mNavButtonView, paramCharSequence);
    } 
  }
  
  public void setNavigationIcon(int paramInt) {
    setNavigationIcon(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setNavigationIcon(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureNavButtonView();
      if (!isChildOrHidden((View)this.mNavButtonView))
        addSystemView((View)this.mNavButtonView, true); 
    } else {
      ImageButton imageButton1 = this.mNavButtonView;
      if (imageButton1 != null && isChildOrHidden((View)imageButton1)) {
        removeView((View)this.mNavButtonView);
        this.mHiddenViews.remove(this.mNavButtonView);
      } 
    } 
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null)
      imageButton.setImageDrawable(paramDrawable); 
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener) {
    ensureNavButtonView();
    this.mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    ensureMenu();
    this.mMenuView.setOverflowIcon(paramDrawable);
  }
  
  public void setPopupTheme(int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
      } else {
        this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
      } 
    } 
  }
  
  public void setSubtitle(int paramInt) {
    setSubtitle(getContext().getText(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mSubtitleTextView == null) {
        Context context = getContext();
        AppCompatTextView appCompatTextView = new AppCompatTextView(context);
        this.mSubtitleTextView = appCompatTextView;
        appCompatTextView.setSingleLine();
        this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i = this.mSubtitleTextAppearance;
        if (i != 0)
          this.mSubtitleTextView.setTextAppearance(context, i); 
        ColorStateList colorStateList = this.mSubtitleTextColor;
        if (colorStateList != null)
          this.mSubtitleTextView.setTextColor(colorStateList); 
      } 
      if (!isChildOrHidden((View)this.mSubtitleTextView))
        addSystemView((View)this.mSubtitleTextView, true); 
    } else {
      TextView textView1 = this.mSubtitleTextView;
      if (textView1 != null && isChildOrHidden((View)textView1)) {
        removeView((View)this.mSubtitleTextView);
        this.mHiddenViews.remove(this.mSubtitleTextView);
      } 
    } 
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setText(paramCharSequence); 
    this.mSubtitleText = paramCharSequence;
  }
  
  public void setSubtitleTextAppearance(Context paramContext, int paramInt) {
    this.mSubtitleTextAppearance = paramInt;
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setSubtitleTextColor(int paramInt) {
    setSubtitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setSubtitleTextColor(ColorStateList paramColorStateList) {
    this.mSubtitleTextColor = paramColorStateList;
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setTextColor(paramColorStateList); 
  }
  
  public void setTitle(int paramInt) {
    setTitle(getContext().getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mTitleTextView == null) {
        Context context = getContext();
        AppCompatTextView appCompatTextView = new AppCompatTextView(context);
        this.mTitleTextView = appCompatTextView;
        appCompatTextView.setSingleLine();
        this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i = this.mTitleTextAppearance;
        if (i != 0)
          this.mTitleTextView.setTextAppearance(context, i); 
        ColorStateList colorStateList = this.mTitleTextColor;
        if (colorStateList != null)
          this.mTitleTextView.setTextColor(colorStateList); 
      } 
      if (!isChildOrHidden((View)this.mTitleTextView))
        addSystemView((View)this.mTitleTextView, true); 
    } else {
      TextView textView1 = this.mTitleTextView;
      if (textView1 != null && isChildOrHidden((View)textView1)) {
        removeView((View)this.mTitleTextView);
        this.mHiddenViews.remove(this.mTitleTextView);
      } 
    } 
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setText(paramCharSequence); 
    this.mTitleText = paramCharSequence;
  }
  
  public void setTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mTitleMarginStart = paramInt1;
    this.mTitleMarginTop = paramInt2;
    this.mTitleMarginEnd = paramInt3;
    this.mTitleMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setTitleMarginBottom(int paramInt) {
    this.mTitleMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginEnd(int paramInt) {
    this.mTitleMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginStart(int paramInt) {
    this.mTitleMarginStart = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginTop(int paramInt) {
    this.mTitleMarginTop = paramInt;
    requestLayout();
  }
  
  public void setTitleTextAppearance(Context paramContext, int paramInt) {
    this.mTitleTextAppearance = paramInt;
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setTitleTextColor(int paramInt) {
    setTitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setTitleTextColor(ColorStateList paramColorStateList) {
    this.mTitleTextColor = paramColorStateList;
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setTextColor(paramColorStateList); 
  }
  
  public boolean showOverflowMenu() {
    boolean bool;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null && actionMenuView.showOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private class ExpandedActionViewMenuPresenter implements MenuPresenter {
    MenuItemImpl mCurrentExpandedItem;
    
    MenuBuilder mMenu;
    
    final Toolbar this$0;
    
    public boolean collapseItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed(); 
      Toolbar toolbar = Toolbar.this;
      toolbar.removeView(toolbar.mExpandedActionView);
      toolbar = Toolbar.this;
      toolbar.removeView((View)toolbar.mCollapseButtonView);
      Toolbar.this.mExpandedActionView = null;
      Toolbar.this.addChildrenForExpandedActionView();
      this.mCurrentExpandedItem = null;
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      Toolbar.this.ensureCollapseButtonView();
      ViewParent viewParent = Toolbar.this.mCollapseButtonView.getParent();
      Toolbar toolbar = Toolbar.this;
      if (viewParent != toolbar) {
        if (viewParent instanceof ViewGroup)
          ((ViewGroup)viewParent).removeView((View)toolbar.mCollapseButtonView); 
        toolbar = Toolbar.this;
        toolbar.addView((View)toolbar.mCollapseButtonView);
      } 
      Toolbar.this.mExpandedActionView = param1MenuItemImpl.getActionView();
      this.mCurrentExpandedItem = param1MenuItemImpl;
      viewParent = Toolbar.this.mExpandedActionView.getParent();
      toolbar = Toolbar.this;
      if (viewParent != toolbar) {
        if (viewParent instanceof ViewGroup)
          ((ViewGroup)viewParent).removeView(toolbar.mExpandedActionView); 
        Toolbar.LayoutParams layoutParams = Toolbar.this.generateDefaultLayoutParams();
        layoutParams.gravity = 0x800003 | Toolbar.this.mButtonGravity & 0x70;
        layoutParams.mViewType = 2;
        Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        Toolbar toolbar1 = Toolbar.this;
        toolbar1.addView(toolbar1.mExpandedActionView);
      } 
      Toolbar.this.removeChildrenForExpandedActionView();
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(true);
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded(); 
      return true;
    }
    
    public boolean flagActionItems() {
      return false;
    }
    
    public int getId() {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup param1ViewGroup) {
      return null;
    }
    
    public void initForMenu(Context param1Context, MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null) {
        MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
        if (menuItemImpl != null)
          menuBuilder.collapseItemActionView(menuItemImpl); 
      } 
      this.mMenu = param1MenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder param1SubMenuBuilder) {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback param1Callback) {}
    
    public void updateMenuView(boolean param1Boolean) {
      if (this.mCurrentExpandedItem != null) {
        boolean bool2 = false;
        MenuBuilder menuBuilder = this.mMenu;
        boolean bool1 = bool2;
        if (menuBuilder != null) {
          int i = menuBuilder.size();
          byte b = 0;
          while (true) {
            bool1 = bool2;
            if (b < i) {
              if (this.mMenu.getItem(b) == this.mCurrentExpandedItem) {
                bool1 = true;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } 
        if (!bool1)
          collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
      } 
    }
  }
  
  public static class LayoutParams extends ActionBar.LayoutParams {
    static final int CUSTOM = 0;
    
    static final int EXPANDED = 2;
    
    static final int SYSTEM = 1;
    
    int mViewType = 0;
    
    public LayoutParams(int param1Int) {
      this(-2, -1, param1Int);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.gravity = 8388627;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super((ViewGroup.LayoutParams)param1MarginLayoutParams);
      copyMarginsFromCompat(param1MarginLayoutParams);
    }
    
    public LayoutParams(ActionBar.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.mViewType = param1LayoutParams.mViewType;
    }
    
    void copyMarginsFromCompat(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      this.leftMargin = param1MarginLayoutParams.leftMargin;
      this.topMargin = param1MarginLayoutParams.topMargin;
      this.rightMargin = param1MarginLayoutParams.rightMargin;
      this.bottomMargin = param1MarginLayoutParams.bottomMargin;
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public Toolbar.SavedState createFromParcel(Parcel param2Parcel) {
          return new Toolbar.SavedState(param2Parcel, null);
        }
        
        public Toolbar.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new Toolbar.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public Toolbar.SavedState[] newArray(int param2Int) {
          return new Toolbar.SavedState[param2Int];
        }
      };
    
    int expandedMenuItemId;
    
    boolean isOverflowOpen;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.expandedMenuItemId = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOverflowOpen = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.expandedMenuItemId);
      param1Parcel.writeInt(this.isOverflowOpen);
    }
  }
  
  class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel) {
      return new Toolbar.SavedState(param1Parcel, null);
    }
    
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new Toolbar.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public Toolbar.SavedState[] newArray(int param1Int) {
      return new Toolbar.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\Toolbar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */