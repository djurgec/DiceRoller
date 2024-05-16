package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.google.android.material.R;
import java.util.ArrayList;

public class NavigationMenuPresenter implements MenuPresenter {
  private static final String STATE_ADAPTER = "android:menu:adapter";
  
  private static final String STATE_HEADER = "android:menu:header";
  
  private static final String STATE_HIERARCHY = "android:menu:list";
  
  NavigationMenuAdapter adapter;
  
  private MenuPresenter.Callback callback;
  
  boolean hasCustomItemIconSize;
  
  LinearLayout headerLayout;
  
  ColorStateList iconTintList;
  
  private int id;
  
  boolean isBehindStatusBar = true;
  
  Drawable itemBackground;
  
  int itemHorizontalPadding;
  
  int itemIconPadding;
  
  int itemIconSize;
  
  private int itemMaxLines;
  
  LayoutInflater layoutInflater;
  
  MenuBuilder menu;
  
  private NavigationMenuView menuView;
  
  final View.OnClickListener onClickListener = new View.OnClickListener() {
      final NavigationMenuPresenter this$0;
      
      public void onClick(View param1View) {
        NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)param1View;
        NavigationMenuPresenter.this.setUpdateSuspended(true);
        MenuItemImpl menuItemImpl = navigationMenuItemView.getItemData();
        boolean bool = NavigationMenuPresenter.this.menu.performItemAction((MenuItem)menuItemImpl, NavigationMenuPresenter.this, 0);
        boolean bool2 = false;
        boolean bool1 = bool2;
        if (menuItemImpl != null) {
          bool1 = bool2;
          if (menuItemImpl.isCheckable()) {
            bool1 = bool2;
            if (bool) {
              NavigationMenuPresenter.this.adapter.setCheckedItem(menuItemImpl);
              bool1 = true;
            } 
          } 
        } 
        NavigationMenuPresenter.this.setUpdateSuspended(false);
        if (bool1)
          NavigationMenuPresenter.this.updateMenuView(false); 
      }
    };
  
  private int overScrollMode = -1;
  
  int paddingSeparator;
  
  private int paddingTopDefault;
  
  int textAppearance;
  
  boolean textAppearanceSet;
  
  ColorStateList textColor;
  
  private void updateTopPadding() {
    byte b = 0;
    int i = b;
    if (this.headerLayout.getChildCount() == 0) {
      i = b;
      if (this.isBehindStatusBar)
        i = this.paddingTopDefault; 
    } 
    NavigationMenuView navigationMenuView = this.menuView;
    navigationMenuView.setPadding(0, i, 0, navigationMenuView.getPaddingBottom());
  }
  
  public void addHeaderView(View paramView) {
    this.headerLayout.addView(paramView);
    NavigationMenuView navigationMenuView = this.menuView;
    navigationMenuView.setPadding(0, 0, 0, navigationMenuView.getPaddingBottom());
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public void dispatchApplyWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    int i = paramWindowInsetsCompat.getSystemWindowInsetTop();
    if (this.paddingTopDefault != i) {
      this.paddingTopDefault = i;
      updateTopPadding();
    } 
    NavigationMenuView navigationMenuView = this.menuView;
    navigationMenuView.setPadding(0, navigationMenuView.getPaddingTop(), 0, paramWindowInsetsCompat.getSystemWindowInsetBottom());
    ViewCompat.dispatchApplyWindowInsets((View)this.headerLayout, paramWindowInsetsCompat);
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public MenuItemImpl getCheckedItem() {
    return this.adapter.getCheckedItem();
  }
  
  public int getHeaderCount() {
    return this.headerLayout.getChildCount();
  }
  
  public View getHeaderView(int paramInt) {
    return this.headerLayout.getChildAt(paramInt);
  }
  
  public int getId() {
    return this.id;
  }
  
  public Drawable getItemBackground() {
    return this.itemBackground;
  }
  
  public int getItemHorizontalPadding() {
    return this.itemHorizontalPadding;
  }
  
  public int getItemIconPadding() {
    return this.itemIconPadding;
  }
  
  public int getItemMaxLines() {
    return this.itemMaxLines;
  }
  
  public ColorStateList getItemTextColor() {
    return this.textColor;
  }
  
  public ColorStateList getItemTintList() {
    return this.iconTintList;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    if (this.menuView == null) {
      NavigationMenuView navigationMenuView = (NavigationMenuView)this.layoutInflater.inflate(R.layout.design_navigation_menu, paramViewGroup, false);
      this.menuView = navigationMenuView;
      navigationMenuView.setAccessibilityDelegateCompat(new NavigationMenuViewAccessibilityDelegate(this.menuView));
      if (this.adapter == null)
        this.adapter = new NavigationMenuAdapter(); 
      int i = this.overScrollMode;
      if (i != -1)
        this.menuView.setOverScrollMode(i); 
      this.headerLayout = (LinearLayout)this.layoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.menuView, false);
      this.menuView.setAdapter(this.adapter);
    } 
    return this.menuView;
  }
  
  public View inflateHeaderView(int paramInt) {
    View view = this.layoutInflater.inflate(paramInt, (ViewGroup)this.headerLayout, false);
    addHeaderView(view);
    return view;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    this.layoutInflater = LayoutInflater.from(paramContext);
    this.menu = paramMenuBuilder;
    this.paddingSeparator = paramContext.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
  }
  
  public boolean isBehindStatusBar() {
    return this.isBehindStatusBar;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    MenuPresenter.Callback callback = this.callback;
    if (callback != null)
      callback.onCloseMenu(paramMenuBuilder, paramBoolean); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof Bundle) {
      Bundle bundle1 = (Bundle)paramParcelable;
      SparseArray sparseArray2 = bundle1.getSparseParcelableArray("android:menu:list");
      if (sparseArray2 != null)
        this.menuView.restoreHierarchyState(sparseArray2); 
      Bundle bundle2 = bundle1.getBundle("android:menu:adapter");
      if (bundle2 != null)
        this.adapter.restoreInstanceState(bundle2); 
      SparseArray sparseArray1 = bundle1.getSparseParcelableArray("android:menu:header");
      if (sparseArray1 != null)
        this.headerLayout.restoreHierarchyState(sparseArray1); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    Bundle bundle = new Bundle();
    if (this.menuView != null) {
      SparseArray sparseArray = new SparseArray();
      this.menuView.saveHierarchyState(sparseArray);
      bundle.putSparseParcelableArray("android:menu:list", sparseArray);
    } 
    NavigationMenuAdapter navigationMenuAdapter = this.adapter;
    if (navigationMenuAdapter != null)
      bundle.putBundle("android:menu:adapter", navigationMenuAdapter.createInstanceState()); 
    if (this.headerLayout != null) {
      SparseArray sparseArray = new SparseArray();
      this.headerLayout.saveHierarchyState(sparseArray);
      bundle.putSparseParcelableArray("android:menu:header", sparseArray);
    } 
    return (Parcelable)bundle;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    return false;
  }
  
  public void removeHeaderView(View paramView) {
    this.headerLayout.removeView(paramView);
    if (this.headerLayout.getChildCount() == 0) {
      NavigationMenuView navigationMenuView = this.menuView;
      navigationMenuView.setPadding(0, this.paddingTopDefault, 0, navigationMenuView.getPaddingBottom());
    } 
  }
  
  public void setBehindStatusBar(boolean paramBoolean) {
    if (this.isBehindStatusBar != paramBoolean) {
      this.isBehindStatusBar = paramBoolean;
      updateTopPadding();
    } 
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {
    this.callback = paramCallback;
  }
  
  public void setCheckedItem(MenuItemImpl paramMenuItemImpl) {
    this.adapter.setCheckedItem(paramMenuItemImpl);
  }
  
  public void setId(int paramInt) {
    this.id = paramInt;
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.itemBackground = paramDrawable;
    updateMenuView(false);
  }
  
  public void setItemHorizontalPadding(int paramInt) {
    this.itemHorizontalPadding = paramInt;
    updateMenuView(false);
  }
  
  public void setItemIconPadding(int paramInt) {
    this.itemIconPadding = paramInt;
    updateMenuView(false);
  }
  
  public void setItemIconSize(int paramInt) {
    if (this.itemIconSize != paramInt) {
      this.itemIconSize = paramInt;
      this.hasCustomItemIconSize = true;
      updateMenuView(false);
    } 
  }
  
  public void setItemIconTintList(ColorStateList paramColorStateList) {
    this.iconTintList = paramColorStateList;
    updateMenuView(false);
  }
  
  public void setItemMaxLines(int paramInt) {
    this.itemMaxLines = paramInt;
    updateMenuView(false);
  }
  
  public void setItemTextAppearance(int paramInt) {
    this.textAppearance = paramInt;
    this.textAppearanceSet = true;
    updateMenuView(false);
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.textColor = paramColorStateList;
    updateMenuView(false);
  }
  
  public void setOverScrollMode(int paramInt) {
    this.overScrollMode = paramInt;
    NavigationMenuView navigationMenuView = this.menuView;
    if (navigationMenuView != null)
      navigationMenuView.setOverScrollMode(paramInt); 
  }
  
  public void setUpdateSuspended(boolean paramBoolean) {
    NavigationMenuAdapter navigationMenuAdapter = this.adapter;
    if (navigationMenuAdapter != null)
      navigationMenuAdapter.setUpdateSuspended(paramBoolean); 
  }
  
  public void updateMenuView(boolean paramBoolean) {
    NavigationMenuAdapter navigationMenuAdapter = this.adapter;
    if (navigationMenuAdapter != null)
      navigationMenuAdapter.update(); 
  }
  
  private static class HeaderViewHolder extends ViewHolder {
    public HeaderViewHolder(View param1View) {
      super(param1View);
    }
  }
  
  private class NavigationMenuAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
    
    private static final String STATE_CHECKED_ITEM = "android:menu:checked";
    
    private static final int VIEW_TYPE_HEADER = 3;
    
    private static final int VIEW_TYPE_NORMAL = 0;
    
    private static final int VIEW_TYPE_SEPARATOR = 2;
    
    private static final int VIEW_TYPE_SUBHEADER = 1;
    
    private MenuItemImpl checkedItem;
    
    private final ArrayList<NavigationMenuPresenter.NavigationMenuItem> items = new ArrayList<>();
    
    final NavigationMenuPresenter this$0;
    
    private boolean updateSuspended;
    
    NavigationMenuAdapter() {
      prepareMenuItems();
    }
    
    private void appendTransparentIconIfMissing(int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        ((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(param1Int1)).needsEmptyIcon = true;
        param1Int1++;
      } 
    }
    
    private void prepareMenuItems() {
      if (this.updateSuspended)
        return; 
      this.updateSuspended = true;
      this.items.clear();
      this.items.add(new NavigationMenuPresenter.NavigationMenuHeaderItem());
      int j = -1;
      int i = 0;
      boolean bool = false;
      byte b = 0;
      int k = NavigationMenuPresenter.this.menu.getVisibleItems().size();
      while (true) {
        boolean bool1 = false;
        if (b < k) {
          int m;
          MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.menu.getVisibleItems().get(b);
          if (menuItemImpl.isChecked())
            setCheckedItem(menuItemImpl); 
          if (menuItemImpl.isCheckable())
            menuItemImpl.setExclusiveCheckable(false); 
          if (menuItemImpl.hasSubMenu()) {
            SubMenu subMenu = menuItemImpl.getSubMenu();
            if (subMenu.hasVisibleItems()) {
              if (b != 0)
                this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, 0)); 
              this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl));
              boolean bool2 = false;
              int n = this.items.size();
              byte b1 = 0;
              int i1 = subMenu.size();
              while (b1 < i1) {
                MenuItemImpl menuItemImpl1 = (MenuItemImpl)subMenu.getItem(b1);
                boolean bool3 = bool2;
                if (menuItemImpl1.isVisible()) {
                  bool3 = bool2;
                  if (!bool2) {
                    bool3 = bool2;
                    if (menuItemImpl1.getIcon() != null)
                      bool3 = true; 
                  } 
                  if (menuItemImpl1.isCheckable())
                    menuItemImpl1.setExclusiveCheckable(false); 
                  if (menuItemImpl.isChecked())
                    setCheckedItem(menuItemImpl); 
                  this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl1));
                } 
                b1++;
                bool2 = bool3;
              } 
              if (bool2)
                appendTransparentIconIfMissing(n, this.items.size()); 
            } 
            m = i;
            bool1 = bool;
          } else {
            int n = menuItemImpl.getGroupId();
            if (n != j) {
              i = this.items.size();
              if (menuItemImpl.getIcon() != null)
                bool1 = true; 
              bool = bool1;
              m = i;
              bool1 = bool;
              if (b != 0) {
                m = i + 1;
                this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, NavigationMenuPresenter.this.paddingSeparator));
                bool1 = bool;
              } 
            } else {
              m = i;
              bool1 = bool;
              if (!bool) {
                m = i;
                bool1 = bool;
                if (menuItemImpl.getIcon() != null) {
                  bool1 = true;
                  appendTransparentIconIfMissing(i, this.items.size());
                  m = i;
                } 
              } 
            } 
            NavigationMenuPresenter.NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl);
            navigationMenuTextItem.needsEmptyIcon = bool1;
            this.items.add(navigationMenuTextItem);
            j = n;
          } 
          b++;
          i = m;
          bool = bool1;
          continue;
        } 
        this.updateSuspended = false;
        return;
      } 
    }
    
    public Bundle createInstanceState() {
      Bundle bundle = new Bundle();
      MenuItemImpl menuItemImpl = this.checkedItem;
      if (menuItemImpl != null)
        bundle.putInt("android:menu:checked", menuItemImpl.getItemId()); 
      SparseArray sparseArray = new SparseArray();
      byte b = 0;
      int i = this.items.size();
      while (b < i) {
        NavigationMenuPresenter.NavigationMenuItem navigationMenuItem = this.items.get(b);
        if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
          MenuItemImpl menuItemImpl1 = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
          if (menuItemImpl1 != null) {
            View view = menuItemImpl1.getActionView();
          } else {
            navigationMenuItem = null;
          } 
          if (navigationMenuItem != null) {
            ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
            navigationMenuItem.saveHierarchyState(parcelableSparseArray);
            sparseArray.put(menuItemImpl1.getItemId(), parcelableSparseArray);
          } 
        } 
        b++;
      } 
      bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
      return bundle;
    }
    
    public MenuItemImpl getCheckedItem() {
      return this.checkedItem;
    }
    
    public int getItemCount() {
      return this.items.size();
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public int getItemViewType(int param1Int) {
      NavigationMenuPresenter.NavigationMenuItem navigationMenuItem = this.items.get(param1Int);
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuSeparatorItem)
        return 2; 
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuHeaderItem)
        return 3; 
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem)
        return ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu() ? 1 : 0; 
      throw new RuntimeException("Unknown item type.");
    }
    
    int getRowCount() {
      int i;
      if (NavigationMenuPresenter.this.headerLayout.getChildCount() == 0) {
        i = 0;
      } else {
        i = 1;
      } 
      byte b = 0;
      while (b < NavigationMenuPresenter.this.adapter.getItemCount()) {
        int j = i;
        if (NavigationMenuPresenter.this.adapter.getItemViewType(b) == 0)
          j = i + 1; 
        b++;
        i = j;
      } 
      return i;
    }
    
    public void onBindViewHolder(NavigationMenuPresenter.ViewHolder param1ViewHolder, int param1Int) {
      NavigationMenuPresenter.NavigationMenuSeparatorItem navigationMenuSeparatorItem;
      switch (getItemViewType(param1Int)) {
        default:
          return;
        case 2:
          navigationMenuSeparatorItem = (NavigationMenuPresenter.NavigationMenuSeparatorItem)this.items.get(param1Int);
          param1ViewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
        case 1:
          ((TextView)param1ViewHolder.itemView).setText(((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(param1Int)).getMenuItem().getTitle());
        case 0:
          break;
      } 
      NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)param1ViewHolder.itemView;
      navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.iconTintList);
      if (NavigationMenuPresenter.this.textAppearanceSet)
        navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.textAppearance); 
      if (NavigationMenuPresenter.this.textColor != null)
        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.textColor); 
      if (NavigationMenuPresenter.this.itemBackground != null) {
        Drawable drawable = NavigationMenuPresenter.this.itemBackground.getConstantState().newDrawable();
      } else {
        param1ViewHolder = null;
      } 
      ViewCompat.setBackground((View)navigationMenuItemView, (Drawable)param1ViewHolder);
      NavigationMenuPresenter.NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(param1Int);
      navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
      navigationMenuItemView.setHorizontalPadding(NavigationMenuPresenter.this.itemHorizontalPadding);
      navigationMenuItemView.setIconPadding(NavigationMenuPresenter.this.itemIconPadding);
      if (NavigationMenuPresenter.this.hasCustomItemIconSize)
        navigationMenuItemView.setIconSize(NavigationMenuPresenter.this.itemIconSize); 
      navigationMenuItemView.setMaxLines(NavigationMenuPresenter.this.itemMaxLines);
      navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
    }
    
    public NavigationMenuPresenter.ViewHolder onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int) {
      switch (param1Int) {
        default:
          return null;
        case 3:
          return new NavigationMenuPresenter.HeaderViewHolder((View)NavigationMenuPresenter.this.headerLayout);
        case 2:
          return new NavigationMenuPresenter.SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, param1ViewGroup);
        case 1:
          return new NavigationMenuPresenter.SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, param1ViewGroup);
        case 0:
          break;
      } 
      return new NavigationMenuPresenter.NormalViewHolder(NavigationMenuPresenter.this.layoutInflater, param1ViewGroup, NavigationMenuPresenter.this.onClickListener);
    }
    
    public void onViewRecycled(NavigationMenuPresenter.ViewHolder param1ViewHolder) {
      if (param1ViewHolder instanceof NavigationMenuPresenter.NormalViewHolder)
        ((NavigationMenuItemView)param1ViewHolder.itemView).recycle(); 
    }
    
    public void restoreInstanceState(Bundle param1Bundle) {
      int i = param1Bundle.getInt("android:menu:checked", 0);
      if (i != 0) {
        this.updateSuspended = true;
        byte b = 0;
        int j = this.items.size();
        while (b < j) {
          NavigationMenuPresenter.NavigationMenuItem navigationMenuItem = this.items.get(b);
          if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
            MenuItemImpl menuItemImpl = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
            if (menuItemImpl != null && menuItemImpl.getItemId() == i) {
              setCheckedItem(menuItemImpl);
              break;
            } 
          } 
          b++;
        } 
        this.updateSuspended = false;
        prepareMenuItems();
      } 
      SparseArray sparseArray = param1Bundle.getSparseParcelableArray("android:menu:action_views");
      if (sparseArray != null) {
        byte b = 0;
        int j = this.items.size();
        while (b < j) {
          NavigationMenuPresenter.NavigationMenuItem navigationMenuItem = this.items.get(b);
          if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
            MenuItemImpl menuItemImpl = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
            if (menuItemImpl != null) {
              View view = menuItemImpl.getActionView();
              if (view != null) {
                ParcelableSparseArray parcelableSparseArray = (ParcelableSparseArray)sparseArray.get(menuItemImpl.getItemId());
                if (parcelableSparseArray != null)
                  view.restoreHierarchyState(parcelableSparseArray); 
              } 
            } 
          } 
          b++;
        } 
      } 
    }
    
    public void setCheckedItem(MenuItemImpl param1MenuItemImpl) {
      if (this.checkedItem == param1MenuItemImpl || !param1MenuItemImpl.isCheckable())
        return; 
      MenuItemImpl menuItemImpl = this.checkedItem;
      if (menuItemImpl != null)
        menuItemImpl.setChecked(false); 
      this.checkedItem = param1MenuItemImpl;
      param1MenuItemImpl.setChecked(true);
    }
    
    public void setUpdateSuspended(boolean param1Boolean) {
      this.updateSuspended = param1Boolean;
    }
    
    public void update() {
      prepareMenuItems();
      notifyDataSetChanged();
    }
  }
  
  private static class NavigationMenuHeaderItem implements NavigationMenuItem {}
  
  private static interface NavigationMenuItem {}
  
  private static class NavigationMenuSeparatorItem implements NavigationMenuItem {
    private final int paddingBottom;
    
    private final int paddingTop;
    
    public NavigationMenuSeparatorItem(int param1Int1, int param1Int2) {
      this.paddingTop = param1Int1;
      this.paddingBottom = param1Int2;
    }
    
    public int getPaddingBottom() {
      return this.paddingBottom;
    }
    
    public int getPaddingTop() {
      return this.paddingTop;
    }
  }
  
  private static class NavigationMenuTextItem implements NavigationMenuItem {
    private final MenuItemImpl menuItem;
    
    boolean needsEmptyIcon;
    
    NavigationMenuTextItem(MenuItemImpl param1MenuItemImpl) {
      this.menuItem = param1MenuItemImpl;
    }
    
    public MenuItemImpl getMenuItem() {
      return this.menuItem;
    }
  }
  
  private class NavigationMenuViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
    final NavigationMenuPresenter this$0;
    
    NavigationMenuViewAccessibilityDelegate(RecyclerView param1RecyclerView) {
      super(param1RecyclerView);
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(NavigationMenuPresenter.this.adapter.getRowCount(), 0, false));
    }
  }
  
  private static class NormalViewHolder extends ViewHolder {
    public NormalViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup, View.OnClickListener param1OnClickListener) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item, param1ViewGroup, false));
      this.itemView.setOnClickListener(param1OnClickListener);
    }
  }
  
  private static class SeparatorViewHolder extends ViewHolder {
    public SeparatorViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item_separator, param1ViewGroup, false));
    }
  }
  
  private static class SubheaderViewHolder extends ViewHolder {
    public SubheaderViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item_subheader, param1ViewGroup, false));
    }
  }
  
  private static abstract class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View param1View) {
      super(param1View);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\NavigationMenuPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */