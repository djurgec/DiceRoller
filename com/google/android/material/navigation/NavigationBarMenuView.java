package com.google.android.material.navigation;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.TextScale;
import java.util.HashSet;

public abstract class NavigationBarMenuView extends ViewGroup implements MenuView {
  private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private static final int ITEM_POOL_SIZE = 5;
  
  private SparseArray<BadgeDrawable> badgeDrawables = new SparseArray(5);
  
  private NavigationBarItemView[] buttons;
  
  private Drawable itemBackground;
  
  private int itemBackgroundRes;
  
  private int itemIconSize;
  
  private ColorStateList itemIconTint;
  
  private final Pools.Pool<NavigationBarItemView> itemPool = (Pools.Pool<NavigationBarItemView>)new Pools.SynchronizedPool(5);
  
  private int itemTextAppearanceActive;
  
  private int itemTextAppearanceInactive;
  
  private final ColorStateList itemTextColorDefault = createDefaultColorStateList(16842808);
  
  private ColorStateList itemTextColorFromUser;
  
  private int labelVisibilityMode;
  
  private MenuBuilder menu;
  
  private final View.OnClickListener onClickListener;
  
  private final SparseArray<View.OnTouchListener> onTouchListeners = new SparseArray(5);
  
  private NavigationBarPresenter presenter;
  
  private int selectedItemId = 0;
  
  private int selectedItemPosition = 0;
  
  private final TransitionSet set;
  
  public NavigationBarMenuView(Context paramContext) {
    super(paramContext);
    AutoTransition autoTransition = new AutoTransition();
    this.set = (TransitionSet)autoTransition;
    autoTransition.setOrdering(0);
    autoTransition.setDuration(115L);
    autoTransition.setInterpolator((TimeInterpolator)new FastOutSlowInInterpolator());
    autoTransition.addTransition((Transition)new TextScale());
    this.onClickListener = new View.OnClickListener() {
        final NavigationBarMenuView this$0;
        
        public void onClick(View param1View) {
          MenuItemImpl menuItemImpl = ((NavigationBarItemView)param1View).getItemData();
          if (!NavigationBarMenuView.this.menu.performItemAction((MenuItem)menuItemImpl, NavigationBarMenuView.this.presenter, 0))
            menuItemImpl.setChecked(true); 
        }
      };
    ViewCompat.setImportantForAccessibility((View)this, 1);
  }
  
  private NavigationBarItemView getNewItem() {
    NavigationBarItemView navigationBarItemView2 = (NavigationBarItemView)this.itemPool.acquire();
    NavigationBarItemView navigationBarItemView1 = navigationBarItemView2;
    if (navigationBarItemView2 == null)
      navigationBarItemView1 = createNavigationBarItemView(getContext()); 
    return navigationBarItemView1;
  }
  
  private boolean isValidId(int paramInt) {
    boolean bool;
    if (paramInt != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void removeUnusedBadges() {
    HashSet<Integer> hashSet = new HashSet();
    byte b;
    for (b = 0; b < this.menu.size(); b++)
      hashSet.add(Integer.valueOf(this.menu.getItem(b).getItemId())); 
    for (b = 0; b < this.badgeDrawables.size(); b++) {
      int i = this.badgeDrawables.keyAt(b);
      if (!hashSet.contains(Integer.valueOf(i)))
        this.badgeDrawables.delete(i); 
    } 
  }
  
  private void setBadgeIfNeeded(NavigationBarItemView paramNavigationBarItemView) {
    int i = paramNavigationBarItemView.getId();
    if (!isValidId(i))
      return; 
    BadgeDrawable badgeDrawable = (BadgeDrawable)this.badgeDrawables.get(i);
    if (badgeDrawable != null)
      paramNavigationBarItemView.setBadge(badgeDrawable); 
  }
  
  private void validateMenuItemId(int paramInt) {
    if (isValidId(paramInt))
      return; 
    throw new IllegalArgumentException(paramInt + " is not a valid view id");
  }
  
  public void buildMenuView() {
    removeAllViews();
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int j = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < j; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        if (navigationBarItemView != null) {
          this.itemPool.release(navigationBarItemView);
          navigationBarItemView.removeBadge();
        } 
      } 
    } 
    if (this.menu.size() == 0) {
      this.selectedItemId = 0;
      this.selectedItemPosition = 0;
      this.buttons = null;
      return;
    } 
    removeUnusedBadges();
    this.buttons = new NavigationBarItemView[this.menu.size()];
    boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
    int i;
    for (i = 0; i < this.menu.size(); i++) {
      this.presenter.setUpdateSuspended(true);
      this.menu.getItem(i).setCheckable(true);
      this.presenter.setUpdateSuspended(false);
      NavigationBarItemView navigationBarItemView = getNewItem();
      this.buttons[i] = navigationBarItemView;
      navigationBarItemView.setIconTintList(this.itemIconTint);
      navigationBarItemView.setIconSize(this.itemIconSize);
      navigationBarItemView.setTextColor(this.itemTextColorDefault);
      navigationBarItemView.setTextAppearanceInactive(this.itemTextAppearanceInactive);
      navigationBarItemView.setTextAppearanceActive(this.itemTextAppearanceActive);
      navigationBarItemView.setTextColor(this.itemTextColorFromUser);
      Drawable drawable = this.itemBackground;
      if (drawable != null) {
        navigationBarItemView.setItemBackground(drawable);
      } else {
        navigationBarItemView.setItemBackground(this.itemBackgroundRes);
      } 
      navigationBarItemView.setShifting(bool);
      navigationBarItemView.setLabelVisibilityMode(this.labelVisibilityMode);
      MenuItemImpl menuItemImpl = (MenuItemImpl)this.menu.getItem(i);
      navigationBarItemView.initialize(menuItemImpl, 0);
      navigationBarItemView.setItemPosition(i);
      int j = menuItemImpl.getItemId();
      navigationBarItemView.setOnTouchListener((View.OnTouchListener)this.onTouchListeners.get(j));
      navigationBarItemView.setOnClickListener(this.onClickListener);
      int k = this.selectedItemId;
      if (k != 0 && j == k)
        this.selectedItemPosition = i; 
      setBadgeIfNeeded(navigationBarItemView);
      addView((View)navigationBarItemView);
    } 
    i = Math.min(this.menu.size() - 1, this.selectedItemPosition);
    this.selectedItemPosition = i;
    this.menu.getItem(i).setChecked(true);
  }
  
  public ColorStateList createDefaultColorStateList(int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (!getContext().getTheme().resolveAttribute(paramInt, typedValue, true))
      return null; 
    ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
    if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true))
      return null; 
    int i = typedValue.data;
    paramInt = colorStateList.getDefaultColor();
    int[] arrayOfInt1 = DISABLED_STATE_SET;
    int[] arrayOfInt3 = CHECKED_STATE_SET;
    int[] arrayOfInt2 = EMPTY_STATE_SET;
    int j = colorStateList.getColorForState(arrayOfInt1, paramInt);
    return new ColorStateList(new int[][] { arrayOfInt1, arrayOfInt3, arrayOfInt2 }, new int[] { j, i, paramInt });
  }
  
  protected abstract NavigationBarItemView createNavigationBarItemView(Context paramContext);
  
  public NavigationBarItemView findItemView(int paramInt) {
    validateMenuItemId(paramInt);
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        if (navigationBarItemView.getId() == paramInt)
          return navigationBarItemView; 
      } 
    } 
    return null;
  }
  
  public BadgeDrawable getBadge(int paramInt) {
    return (BadgeDrawable)this.badgeDrawables.get(paramInt);
  }
  
  SparseArray<BadgeDrawable> getBadgeDrawables() {
    return this.badgeDrawables;
  }
  
  public ColorStateList getIconTintList() {
    return this.itemIconTint;
  }
  
  public Drawable getItemBackground() {
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    return (arrayOfNavigationBarItemView != null && arrayOfNavigationBarItemView.length > 0) ? arrayOfNavigationBarItemView[0].getBackground() : this.itemBackground;
  }
  
  @Deprecated
  public int getItemBackgroundRes() {
    return this.itemBackgroundRes;
  }
  
  public int getItemIconSize() {
    return this.itemIconSize;
  }
  
  public int getItemTextAppearanceActive() {
    return this.itemTextAppearanceActive;
  }
  
  public int getItemTextAppearanceInactive() {
    return this.itemTextAppearanceInactive;
  }
  
  public ColorStateList getItemTextColor() {
    return this.itemTextColorFromUser;
  }
  
  public int getLabelVisibilityMode() {
    return this.labelVisibilityMode;
  }
  
  protected MenuBuilder getMenu() {
    return this.menu;
  }
  
  BadgeDrawable getOrCreateBadge(int paramInt) {
    validateMenuItemId(paramInt);
    BadgeDrawable badgeDrawable2 = (BadgeDrawable)this.badgeDrawables.get(paramInt);
    BadgeDrawable badgeDrawable1 = badgeDrawable2;
    if (badgeDrawable2 == null) {
      badgeDrawable1 = BadgeDrawable.create(getContext());
      this.badgeDrawables.put(paramInt, badgeDrawable1);
    } 
    NavigationBarItemView navigationBarItemView = findItemView(paramInt);
    if (navigationBarItemView != null)
      navigationBarItemView.setBadge(badgeDrawable1); 
    return badgeDrawable1;
  }
  
  public int getSelectedItemId() {
    return this.selectedItemId;
  }
  
  protected int getSelectedItemPosition() {
    return this.selectedItemPosition;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.menu = paramMenuBuilder;
  }
  
  protected boolean isShifting(int paramInt1, int paramInt2) {
    boolean bool = true;
    if ((paramInt1 == -1) ? (paramInt2 > 3) : (paramInt1 == 0))
      bool = false; 
    return bool;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, this.menu.getVisibleItems().size(), false, 1));
  }
  
  void removeBadge(int paramInt) {
    validateMenuItemId(paramInt);
    BadgeDrawable badgeDrawable = (BadgeDrawable)this.badgeDrawables.get(paramInt);
    NavigationBarItemView navigationBarItemView = findItemView(paramInt);
    if (navigationBarItemView != null)
      navigationBarItemView.removeBadge(); 
    if (badgeDrawable != null)
      this.badgeDrawables.remove(paramInt); 
  }
  
  void setBadgeDrawables(SparseArray<BadgeDrawable> paramSparseArray) {
    this.badgeDrawables = paramSparseArray;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        navigationBarItemView.setBadge((BadgeDrawable)paramSparseArray.get(navigationBarItemView.getId()));
      } 
    } 
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.itemIconTint = paramColorStateList;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfNavigationBarItemView[b].setIconTintList(paramColorStateList); 
    } 
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.itemBackground = paramDrawable;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfNavigationBarItemView[b].setItemBackground(paramDrawable); 
    } 
  }
  
  public void setItemBackgroundRes(int paramInt) {
    this.itemBackgroundRes = paramInt;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfNavigationBarItemView[b].setItemBackground(paramInt); 
    } 
  }
  
  public void setItemIconSize(int paramInt) {
    this.itemIconSize = paramInt;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfNavigationBarItemView[b].setIconSize(paramInt); 
    } 
  }
  
  public void setItemOnTouchListener(int paramInt, View.OnTouchListener paramOnTouchListener) {
    if (paramOnTouchListener == null) {
      this.onTouchListeners.remove(paramInt);
    } else {
      this.onTouchListeners.put(paramInt, paramOnTouchListener);
    } 
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        if (navigationBarItemView.getItemData().getItemId() == paramInt)
          navigationBarItemView.setOnTouchListener(paramOnTouchListener); 
      } 
    } 
  }
  
  public void setItemTextAppearanceActive(int paramInt) {
    this.itemTextAppearanceActive = paramInt;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        navigationBarItemView.setTextAppearanceActive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          navigationBarItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextAppearanceInactive(int paramInt) {
    this.itemTextAppearanceInactive = paramInt;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++) {
        NavigationBarItemView navigationBarItemView = arrayOfNavigationBarItemView[b];
        navigationBarItemView.setTextAppearanceInactive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          navigationBarItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.itemTextColorFromUser = paramColorStateList;
    NavigationBarItemView[] arrayOfNavigationBarItemView = this.buttons;
    if (arrayOfNavigationBarItemView != null) {
      int i = arrayOfNavigationBarItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfNavigationBarItemView[b].setTextColor(paramColorStateList); 
    } 
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    this.labelVisibilityMode = paramInt;
  }
  
  public void setPresenter(NavigationBarPresenter paramNavigationBarPresenter) {
    this.presenter = paramNavigationBarPresenter;
  }
  
  void tryRestoreSelectedItemId(int paramInt) {
    int i = this.menu.size();
    for (byte b = 0; b < i; b++) {
      MenuItem menuItem = this.menu.getItem(b);
      if (paramInt == menuItem.getItemId()) {
        this.selectedItemId = paramInt;
        this.selectedItemPosition = b;
        menuItem.setChecked(true);
        break;
      } 
    } 
  }
  
  public void updateMenuView() {
    MenuBuilder menuBuilder = this.menu;
    if (menuBuilder == null || this.buttons == null)
      return; 
    int i = menuBuilder.size();
    if (i != this.buttons.length) {
      buildMenuView();
      return;
    } 
    int j = this.selectedItemId;
    byte b;
    for (b = 0; b < i; b++) {
      MenuItem menuItem = this.menu.getItem(b);
      if (menuItem.isChecked()) {
        this.selectedItemId = menuItem.getItemId();
        this.selectedItemPosition = b;
      } 
    } 
    if (j != this.selectedItemId)
      TransitionManager.beginDelayedTransition(this, (Transition)this.set); 
    boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
    for (b = 0; b < i; b++) {
      this.presenter.setUpdateSuspended(true);
      this.buttons[b].setLabelVisibilityMode(this.labelVisibilityMode);
      this.buttons[b].setShifting(bool);
      this.buttons[b].initialize((MenuItemImpl)this.menu.getItem(b), 0);
      this.presenter.setUpdateSuspended(false);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationBarMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */