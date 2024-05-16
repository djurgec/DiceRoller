package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

public abstract class NavigationBarItemView extends FrameLayout implements MenuView.ItemView {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int INVALID_ITEM_POSITION = -1;
  
  private BadgeDrawable badgeDrawable;
  
  private final int defaultMargin;
  
  private ImageView icon;
  
  private ColorStateList iconTint;
  
  private boolean isShifting;
  
  private MenuItemImpl itemData;
  
  private int itemPosition = -1;
  
  private final ViewGroup labelGroup;
  
  private int labelVisibilityMode;
  
  private final TextView largeLabel;
  
  private Drawable originalIconDrawable;
  
  private float scaleDownFactor;
  
  private float scaleUpFactor;
  
  private float shiftAmount;
  
  private final TextView smallLabel;
  
  private Drawable wrappedIconDrawable;
  
  public NavigationBarItemView(Context paramContext) {
    super(paramContext);
    LayoutInflater.from(paramContext).inflate(getItemLayoutResId(), (ViewGroup)this, true);
    this.icon = (ImageView)findViewById(R.id.navigation_bar_item_icon_view);
    ViewGroup viewGroup = (ViewGroup)findViewById(R.id.navigation_bar_item_labels_group);
    this.labelGroup = viewGroup;
    TextView textView1 = (TextView)findViewById(R.id.navigation_bar_item_small_label_view);
    this.smallLabel = textView1;
    TextView textView2 = (TextView)findViewById(R.id.navigation_bar_item_large_label_view);
    this.largeLabel = textView2;
    setBackgroundResource(getItemBackgroundResId());
    this.defaultMargin = getResources().getDimensionPixelSize(getItemDefaultMarginResId());
    viewGroup.setTag(R.id.mtrl_view_tag_bottom_padding, Integer.valueOf(viewGroup.getPaddingBottom()));
    ViewCompat.setImportantForAccessibility((View)textView1, 2);
    ViewCompat.setImportantForAccessibility((View)textView2, 2);
    setFocusable(true);
    calculateTextScaleFactors(textView1.getTextSize(), textView2.getTextSize());
    ImageView imageView = this.icon;
    if (imageView != null)
      imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            final NavigationBarItemView this$0;
            
            public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
              if (NavigationBarItemView.this.icon.getVisibility() == 0) {
                NavigationBarItemView navigationBarItemView = NavigationBarItemView.this;
                navigationBarItemView.tryUpdateBadgeBounds((View)navigationBarItemView.icon);
              } 
            }
          }); 
  }
  
  private void calculateTextScaleFactors(float paramFloat1, float paramFloat2) {
    this.shiftAmount = paramFloat1 - paramFloat2;
    this.scaleUpFactor = paramFloat2 * 1.0F / paramFloat1;
    this.scaleDownFactor = 1.0F * paramFloat1 / paramFloat2;
  }
  
  private FrameLayout getCustomParentForBadge(View paramView) {
    ImageView imageView = this.icon;
    View view = null;
    if (paramView == imageView) {
      FrameLayout frameLayout;
      paramView = view;
      if (BadgeUtils.USE_COMPAT_PARENT)
        frameLayout = (FrameLayout)this.icon.getParent(); 
      return frameLayout;
    } 
    return null;
  }
  
  private int getItemVisiblePosition() {
    ViewGroup viewGroup = (ViewGroup)getParent();
    int j = viewGroup.indexOfChild((View)this);
    int i = 0;
    byte b = 0;
    while (b < j) {
      View view = viewGroup.getChildAt(b);
      int k = i;
      if (view instanceof NavigationBarItemView) {
        k = i;
        if (view.getVisibility() == 0)
          k = i + 1; 
      } 
      b++;
      i = k;
    } 
    return i;
  }
  
  private int getSuggestedIconHeight() {
    int i = 0;
    BadgeDrawable badgeDrawable = this.badgeDrawable;
    if (badgeDrawable != null)
      i = badgeDrawable.getMinimumHeight() / 2; 
    return Math.max(i, ((FrameLayout.LayoutParams)this.icon.getLayoutParams()).topMargin) + this.icon.getMeasuredWidth() + i;
  }
  
  private int getSuggestedIconWidth() {
    int i;
    BadgeDrawable badgeDrawable = this.badgeDrawable;
    if (badgeDrawable == null) {
      i = 0;
    } else {
      i = badgeDrawable.getMinimumWidth() - this.badgeDrawable.getHorizontalOffset();
    } 
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.icon.getLayoutParams();
    return Math.max(i, layoutParams.leftMargin) + this.icon.getMeasuredWidth() + Math.max(i, layoutParams.rightMargin);
  }
  
  private boolean hasBadge() {
    boolean bool;
    if (this.badgeDrawable != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static void setViewLayoutParams(View paramView, int paramInt1, int paramInt2) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)paramView.getLayoutParams();
    layoutParams.topMargin = paramInt1;
    layoutParams.gravity = paramInt2;
    paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  private static void setViewScaleValues(View paramView, float paramFloat1, float paramFloat2, int paramInt) {
    paramView.setScaleX(paramFloat1);
    paramView.setScaleY(paramFloat2);
    paramView.setVisibility(paramInt);
  }
  
  private void tryAttachBadgeToAnchor(View paramView) {
    if (!hasBadge())
      return; 
    if (paramView != null) {
      setClipChildren(false);
      setClipToPadding(false);
      BadgeUtils.attachBadgeDrawable(this.badgeDrawable, paramView, getCustomParentForBadge(paramView));
    } 
  }
  
  private void tryRemoveBadgeFromAnchor(View paramView) {
    if (!hasBadge())
      return; 
    if (paramView != null) {
      setClipChildren(true);
      setClipToPadding(true);
      BadgeUtils.detachBadgeDrawable(this.badgeDrawable, paramView);
    } 
    this.badgeDrawable = null;
  }
  
  private void tryUpdateBadgeBounds(View paramView) {
    if (!hasBadge())
      return; 
    BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, paramView, getCustomParentForBadge(paramView));
  }
  
  private static void updateViewPaddingBottom(View paramView, int paramInt) {
    paramView.setPadding(paramView.getPaddingLeft(), paramView.getPaddingTop(), paramView.getPaddingRight(), paramInt);
  }
  
  public BadgeDrawable getBadge() {
    return this.badgeDrawable;
  }
  
  protected int getItemBackgroundResId() {
    return R.drawable.mtrl_navigation_bar_item_background;
  }
  
  public MenuItemImpl getItemData() {
    return this.itemData;
  }
  
  protected int getItemDefaultMarginResId() {
    return R.dimen.mtrl_navigation_bar_item_default_margin;
  }
  
  protected abstract int getItemLayoutResId();
  
  public int getItemPosition() {
    return this.itemPosition;
  }
  
  protected int getSuggestedMinimumHeight() {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.labelGroup.getLayoutParams();
    return getSuggestedIconHeight() + layoutParams.topMargin + this.labelGroup.getMeasuredHeight() + layoutParams.bottomMargin;
  }
  
  protected int getSuggestedMinimumWidth() {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.labelGroup.getLayoutParams();
    int j = layoutParams.leftMargin;
    int i = this.labelGroup.getMeasuredWidth();
    int k = layoutParams.rightMargin;
    return Math.max(getSuggestedIconWidth(), j + i + k);
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
    CharSequence charSequence;
    this.itemData = paramMenuItemImpl;
    setCheckable(paramMenuItemImpl.isCheckable());
    setChecked(paramMenuItemImpl.isChecked());
    setEnabled(paramMenuItemImpl.isEnabled());
    setIcon(paramMenuItemImpl.getIcon());
    setTitle(paramMenuItemImpl.getTitle());
    setId(paramMenuItemImpl.getItemId());
    if (!TextUtils.isEmpty(paramMenuItemImpl.getContentDescription()))
      setContentDescription(paramMenuItemImpl.getContentDescription()); 
    if (!TextUtils.isEmpty(paramMenuItemImpl.getTooltipText())) {
      charSequence = paramMenuItemImpl.getTooltipText();
    } else {
      charSequence = paramMenuItemImpl.getTitle();
    } 
    if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23)
      TooltipCompat.setTooltipText((View)this, charSequence); 
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    } 
    setVisibility(paramInt);
  }
  
  public int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    BadgeDrawable badgeDrawable = this.badgeDrawable;
    if (badgeDrawable != null && badgeDrawable.isVisible()) {
      CharSequence charSequence = this.itemData.getTitle();
      if (!TextUtils.isEmpty(this.itemData.getContentDescription()))
        charSequence = this.itemData.getContentDescription(); 
      paramAccessibilityNodeInfo.setContentDescription(charSequence + ", " + this.badgeDrawable.getContentDescription());
    } 
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo);
    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, getItemVisiblePosition(), 1, false, isSelected()));
    if (isSelected()) {
      accessibilityNodeInfoCompat.setClickable(false);
      accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
    } 
    accessibilityNodeInfoCompat.setRoleDescription(getResources().getString(R.string.item_view_role_description));
  }
  
  public boolean prefersCondensedTitle() {
    return false;
  }
  
  void removeBadge() {
    tryRemoveBadgeFromAnchor((View)this.icon);
  }
  
  void setBadge(BadgeDrawable paramBadgeDrawable) {
    this.badgeDrawable = paramBadgeDrawable;
    ImageView imageView = this.icon;
    if (imageView != null)
      tryAttachBadgeToAnchor((View)imageView); 
  }
  
  public void setCheckable(boolean paramBoolean) {
    refreshDrawableState();
  }
  
  public void setChecked(boolean paramBoolean) {
    float f;
    ViewGroup viewGroup2;
    TextView textView2;
    ViewGroup viewGroup1;
    TextView textView1;
    TextView textView3 = this.largeLabel;
    textView3.setPivotX((textView3.getWidth() / 2));
    textView3 = this.largeLabel;
    textView3.setPivotY(textView3.getBaseline());
    textView3 = this.smallLabel;
    textView3.setPivotX((textView3.getWidth() / 2));
    textView3 = this.smallLabel;
    textView3.setPivotY(textView3.getBaseline());
    switch (this.labelVisibilityMode) {
      case 2:
        setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
        this.largeLabel.setVisibility(8);
        this.smallLabel.setVisibility(8);
        break;
      case 1:
        viewGroup2 = this.labelGroup;
        updateViewPaddingBottom((View)viewGroup2, ((Integer)viewGroup2.getTag(R.id.mtrl_view_tag_bottom_padding)).intValue());
        if (paramBoolean) {
          setViewLayoutParams((View)this.icon, (int)(this.defaultMargin + this.shiftAmount), 49);
          setViewScaleValues((View)this.largeLabel, 1.0F, 1.0F, 0);
          TextView textView = this.smallLabel;
          float f1 = this.scaleUpFactor;
          setViewScaleValues((View)textView, f1, f1, 4);
          break;
        } 
        setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
        textView2 = this.largeLabel;
        f = this.scaleDownFactor;
        setViewScaleValues((View)textView2, f, f, 4);
        setViewScaleValues((View)this.smallLabel, 1.0F, 1.0F, 0);
        break;
      case 0:
        if (paramBoolean) {
          setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
          ViewGroup viewGroup = this.labelGroup;
          updateViewPaddingBottom((View)viewGroup, ((Integer)viewGroup.getTag(R.id.mtrl_view_tag_bottom_padding)).intValue());
          this.largeLabel.setVisibility(0);
        } else {
          setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
          updateViewPaddingBottom((View)this.labelGroup, 0);
          this.largeLabel.setVisibility(4);
        } 
        this.smallLabel.setVisibility(4);
        break;
      case -1:
        if (this.isShifting) {
          if (paramBoolean) {
            setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
            ViewGroup viewGroup = this.labelGroup;
            updateViewPaddingBottom((View)viewGroup, ((Integer)viewGroup.getTag(R.id.mtrl_view_tag_bottom_padding)).intValue());
            this.largeLabel.setVisibility(0);
          } else {
            setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
            updateViewPaddingBottom((View)this.labelGroup, 0);
            this.largeLabel.setVisibility(4);
          } 
          this.smallLabel.setVisibility(4);
          break;
        } 
        viewGroup1 = this.labelGroup;
        updateViewPaddingBottom((View)viewGroup1, ((Integer)viewGroup1.getTag(R.id.mtrl_view_tag_bottom_padding)).intValue());
        if (paramBoolean) {
          setViewLayoutParams((View)this.icon, (int)(this.defaultMargin + this.shiftAmount), 49);
          setViewScaleValues((View)this.largeLabel, 1.0F, 1.0F, 0);
          TextView textView = this.smallLabel;
          f = this.scaleUpFactor;
          setViewScaleValues((View)textView, f, f, 4);
          break;
        } 
        setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
        textView1 = this.largeLabel;
        f = this.scaleDownFactor;
        setViewScaleValues((View)textView1, f, f, 4);
        setViewScaleValues((View)this.smallLabel, 1.0F, 1.0F, 0);
        break;
    } 
    refreshDrawableState();
    setSelected(paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean) {
    super.setEnabled(paramBoolean);
    this.smallLabel.setEnabled(paramBoolean);
    this.largeLabel.setEnabled(paramBoolean);
    this.icon.setEnabled(paramBoolean);
    if (paramBoolean) {
      ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(getContext(), 1002));
    } else {
      ViewCompat.setPointerIcon((View)this, null);
    } 
  }
  
  public void setIcon(Drawable paramDrawable) {
    if (paramDrawable == this.originalIconDrawable)
      return; 
    this.originalIconDrawable = paramDrawable;
    Drawable drawable = paramDrawable;
    if (paramDrawable != null) {
      Drawable.ConstantState constantState = paramDrawable.getConstantState();
      if (constantState != null)
        paramDrawable = constantState.newDrawable(); 
      paramDrawable = DrawableCompat.wrap(paramDrawable).mutate();
      this.wrappedIconDrawable = paramDrawable;
      ColorStateList colorStateList = this.iconTint;
      drawable = paramDrawable;
      if (colorStateList != null) {
        DrawableCompat.setTintList(paramDrawable, colorStateList);
        drawable = paramDrawable;
      } 
    } 
    this.icon.setImageDrawable(drawable);
  }
  
  public void setIconSize(int paramInt) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.icon.getLayoutParams();
    layoutParams.width = paramInt;
    layoutParams.height = paramInt;
    this.icon.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.iconTint = paramColorStateList;
    if (this.itemData != null) {
      Drawable drawable = this.wrappedIconDrawable;
      if (drawable != null) {
        DrawableCompat.setTintList(drawable, paramColorStateList);
        this.wrappedIconDrawable.invalidateSelf();
      } 
    } 
  }
  
  public void setItemBackground(int paramInt) {
    Drawable drawable;
    if (paramInt == 0) {
      drawable = null;
    } else {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } 
    setItemBackground(drawable);
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    Drawable drawable = paramDrawable;
    if (paramDrawable != null) {
      drawable = paramDrawable;
      if (paramDrawable.getConstantState() != null)
        drawable = paramDrawable.getConstantState().newDrawable().mutate(); 
    } 
    ViewCompat.setBackground((View)this, drawable);
  }
  
  public void setItemPosition(int paramInt) {
    this.itemPosition = paramInt;
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    if (this.labelVisibilityMode != paramInt) {
      this.labelVisibilityMode = paramInt;
      MenuItemImpl menuItemImpl = this.itemData;
      if (menuItemImpl != null) {
        paramInt = 1;
      } else {
        paramInt = 0;
      } 
      if (paramInt != 0)
        setChecked(menuItemImpl.isChecked()); 
    } 
  }
  
  public void setShifting(boolean paramBoolean) {
    if (this.isShifting != paramBoolean) {
      boolean bool;
      this.isShifting = paramBoolean;
      MenuItemImpl menuItemImpl = this.itemData;
      if (menuItemImpl != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        setChecked(menuItemImpl.isChecked()); 
    } 
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {}
  
  public void setTextAppearanceActive(int paramInt) {
    TextViewCompat.setTextAppearance(this.largeLabel, paramInt);
    calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
  }
  
  public void setTextAppearanceInactive(int paramInt) {
    TextViewCompat.setTextAppearance(this.smallLabel, paramInt);
    calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
  }
  
  public void setTextColor(ColorStateList paramColorStateList) {
    if (paramColorStateList != null) {
      this.smallLabel.setTextColor(paramColorStateList);
      this.largeLabel.setTextColor(paramColorStateList);
    } 
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.smallLabel.setText(paramCharSequence);
    this.largeLabel.setText(paramCharSequence);
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl == null || TextUtils.isEmpty(menuItemImpl.getContentDescription()))
      setContentDescription(paramCharSequence); 
    menuItemImpl = this.itemData;
    if (menuItemImpl != null && !TextUtils.isEmpty(menuItemImpl.getTooltipText()))
      paramCharSequence = this.itemData.getTooltipText(); 
    if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23)
      TooltipCompat.setTooltipText((View)this, paramCharSequence); 
  }
  
  public boolean showsIcon() {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationBarItemView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */