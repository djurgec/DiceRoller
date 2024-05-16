package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class NavigationBarView extends FrameLayout {
  public static final int LABEL_VISIBILITY_AUTO = -1;
  
  public static final int LABEL_VISIBILITY_LABELED = 1;
  
  public static final int LABEL_VISIBILITY_SELECTED = 0;
  
  public static final int LABEL_VISIBILITY_UNLABELED = 2;
  
  private static final int MENU_PRESENTER_ID = 1;
  
  private ColorStateList itemRippleColor;
  
  private final NavigationBarMenu menu;
  
  private MenuInflater menuInflater;
  
  private final NavigationBarMenuView menuView;
  
  private final NavigationBarPresenter presenter;
  
  private OnItemReselectedListener reselectedListener;
  
  private OnItemSelectedListener selectedListener;
  
  public NavigationBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt1, paramInt2), paramAttributeSet, paramInt1);
    NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter();
    this.presenter = navigationBarPresenter;
    paramContext = getContext();
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(paramContext, paramAttributeSet, R.styleable.NavigationBarView, paramInt1, paramInt2, new int[] { R.styleable.NavigationBarView_itemTextAppearanceInactive, R.styleable.NavigationBarView_itemTextAppearanceActive });
    NavigationBarMenu navigationBarMenu = new NavigationBarMenu(paramContext, getClass(), getMaxItemCount());
    this.menu = navigationBarMenu;
    NavigationBarMenuView navigationBarMenuView = createNavigationBarMenuView(paramContext);
    this.menuView = navigationBarMenuView;
    navigationBarPresenter.setMenuView(navigationBarMenuView);
    navigationBarPresenter.setId(1);
    navigationBarMenuView.setPresenter(navigationBarPresenter);
    navigationBarMenu.addMenuPresenter(navigationBarPresenter);
    navigationBarPresenter.initForMenu(getContext(), navigationBarMenu);
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_itemIconTint)) {
      navigationBarMenuView.setIconTintList(tintTypedArray.getColorStateList(R.styleable.NavigationBarView_itemIconTint));
    } else {
      navigationBarMenuView.setIconTintList(navigationBarMenuView.createDefaultColorStateList(16842808));
    } 
    setItemIconSize(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationBarView_itemIconSize, getResources().getDimensionPixelSize(R.dimen.mtrl_navigation_bar_item_default_icon_size)));
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_itemTextAppearanceInactive))
      setItemTextAppearanceInactive(tintTypedArray.getResourceId(R.styleable.NavigationBarView_itemTextAppearanceInactive, 0)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_itemTextAppearanceActive))
      setItemTextAppearanceActive(tintTypedArray.getResourceId(R.styleable.NavigationBarView_itemTextAppearanceActive, 0)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_itemTextColor))
      setItemTextColor(tintTypedArray.getColorStateList(R.styleable.NavigationBarView_itemTextColor)); 
    if (getBackground() == null || getBackground() instanceof ColorDrawable)
      ViewCompat.setBackground((View)this, (Drawable)createMaterialShapeDrawableBackground(paramContext)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_elevation))
      setElevation(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationBarView_elevation, 0)); 
    ColorStateList colorStateList = MaterialResources.getColorStateList(paramContext, tintTypedArray, R.styleable.NavigationBarView_backgroundTint);
    DrawableCompat.setTintList(getBackground().mutate(), colorStateList);
    setLabelVisibilityMode(tintTypedArray.getInteger(R.styleable.NavigationBarView_labelVisibilityMode, -1));
    paramInt1 = tintTypedArray.getResourceId(R.styleable.NavigationBarView_itemBackground, 0);
    if (paramInt1 != 0) {
      navigationBarMenuView.setItemBackgroundRes(paramInt1);
    } else {
      setItemRippleColor(MaterialResources.getColorStateList(paramContext, tintTypedArray, R.styleable.NavigationBarView_itemRippleColor));
    } 
    if (tintTypedArray.hasValue(R.styleable.NavigationBarView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationBarView_menu, 0)); 
    tintTypedArray.recycle();
    addView((View)navigationBarMenuView);
    navigationBarMenu.setCallback(new MenuBuilder.Callback() {
          final NavigationBarView this$0;
          
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            NavigationBarView.OnItemReselectedListener onItemReselectedListener = NavigationBarView.this.reselectedListener;
            boolean bool = true;
            if (onItemReselectedListener != null && param1MenuItem.getItemId() == NavigationBarView.this.getSelectedItemId()) {
              NavigationBarView.this.reselectedListener.onNavigationItemReselected(param1MenuItem);
              return true;
            } 
            if (NavigationBarView.this.selectedListener == null || NavigationBarView.this.selectedListener.onNavigationItemSelected(param1MenuItem))
              bool = false; 
            return bool;
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
    applyWindowInsets();
  }
  
  private void applyWindowInsets() {
    ViewUtils.doOnApplyWindowInsets((View)this, new ViewUtils.OnApplyWindowInsetsListener() {
          final NavigationBarView this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, ViewUtils.RelativePadding param1RelativePadding) {
            param1RelativePadding.bottom += param1WindowInsetsCompat.getSystemWindowInsetBottom();
            int i = ViewCompat.getLayoutDirection(param1View);
            boolean bool = true;
            if (i != 1)
              bool = false; 
            i = param1WindowInsetsCompat.getSystemWindowInsetLeft();
            int j = param1WindowInsetsCompat.getSystemWindowInsetRight();
            int m = param1RelativePadding.start;
            if (bool) {
              k = j;
            } else {
              k = i;
            } 
            param1RelativePadding.start = m + k;
            int k = param1RelativePadding.end;
            if (bool)
              j = i; 
            param1RelativePadding.end = k + j;
            param1RelativePadding.applyToView(param1View);
            return param1WindowInsetsCompat;
          }
        });
  }
  
  private MaterialShapeDrawable createMaterialShapeDrawableBackground(Context paramContext) {
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
    Drawable drawable = getBackground();
    if (drawable instanceof ColorDrawable)
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(((ColorDrawable)drawable).getColor())); 
    materialShapeDrawable.initializeElevationOverlay(paramContext);
    return materialShapeDrawable;
  }
  
  private MenuInflater getMenuInflater() {
    if (this.menuInflater == null)
      this.menuInflater = (MenuInflater)new SupportMenuInflater(getContext()); 
    return this.menuInflater;
  }
  
  protected abstract NavigationBarMenuView createNavigationBarMenuView(Context paramContext);
  
  public BadgeDrawable getBadge(int paramInt) {
    return this.menuView.getBadge(paramInt);
  }
  
  public Drawable getItemBackground() {
    return this.menuView.getItemBackground();
  }
  
  @Deprecated
  public int getItemBackgroundResource() {
    return this.menuView.getItemBackgroundRes();
  }
  
  public int getItemIconSize() {
    return this.menuView.getItemIconSize();
  }
  
  public ColorStateList getItemIconTintList() {
    return this.menuView.getIconTintList();
  }
  
  public ColorStateList getItemRippleColor() {
    return this.itemRippleColor;
  }
  
  public int getItemTextAppearanceActive() {
    return this.menuView.getItemTextAppearanceActive();
  }
  
  public int getItemTextAppearanceInactive() {
    return this.menuView.getItemTextAppearanceInactive();
  }
  
  public ColorStateList getItemTextColor() {
    return this.menuView.getItemTextColor();
  }
  
  public int getLabelVisibilityMode() {
    return this.menuView.getLabelVisibilityMode();
  }
  
  public abstract int getMaxItemCount();
  
  public Menu getMenu() {
    return (Menu)this.menu;
  }
  
  public MenuView getMenuView() {
    return this.menuView;
  }
  
  public BadgeDrawable getOrCreateBadge(int paramInt) {
    return this.menuView.getOrCreateBadge(paramInt);
  }
  
  protected NavigationBarPresenter getPresenter() {
    return this.presenter;
  }
  
  public int getSelectedItemId() {
    return this.menuView.getSelectedItemId();
  }
  
  public void inflateMenu(int paramInt) {
    this.presenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.menu);
    this.presenter.setUpdateSuspended(false);
    this.presenter.updateMenuView(true);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.menu.restorePresenterStates(savedState.menuPresenterState);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.menuPresenterState = new Bundle();
    this.menu.savePresenterStates(savedState.menuPresenterState);
    return (Parcelable)savedState;
  }
  
  public void removeBadge(int paramInt) {
    this.menuView.removeBadge(paramInt);
  }
  
  public void setElevation(float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      super.setElevation(paramFloat); 
    MaterialShapeUtils.setElevation((View)this, paramFloat);
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.menuView.setItemBackground(paramDrawable);
    this.itemRippleColor = null;
  }
  
  public void setItemBackgroundResource(int paramInt) {
    this.menuView.setItemBackgroundRes(paramInt);
    this.itemRippleColor = null;
  }
  
  public void setItemIconSize(int paramInt) {
    this.menuView.setItemIconSize(paramInt);
  }
  
  public void setItemIconSizeRes(int paramInt) {
    setItemIconSize(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconTintList(ColorStateList paramColorStateList) {
    this.menuView.setIconTintList(paramColorStateList);
  }
  
  public void setItemOnTouchListener(int paramInt, View.OnTouchListener paramOnTouchListener) {
    this.menuView.setItemOnTouchListener(paramInt, paramOnTouchListener);
  }
  
  public void setItemRippleColor(ColorStateList paramColorStateList) {
    if (this.itemRippleColor == paramColorStateList) {
      if (paramColorStateList == null && this.menuView.getItemBackground() != null)
        this.menuView.setItemBackground(null); 
      return;
    } 
    this.itemRippleColor = paramColorStateList;
    if (paramColorStateList == null) {
      this.menuView.setItemBackground(null);
    } else {
      paramColorStateList = RippleUtils.convertToRippleDrawableColor(paramColorStateList);
      if (Build.VERSION.SDK_INT >= 21) {
        this.menuView.setItemBackground((Drawable)new RippleDrawable(paramColorStateList, null, null));
      } else {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1.0E-5F);
        Drawable drawable = DrawableCompat.wrap((Drawable)gradientDrawable);
        DrawableCompat.setTintList(drawable, paramColorStateList);
        this.menuView.setItemBackground(drawable);
      } 
    } 
  }
  
  public void setItemTextAppearanceActive(int paramInt) {
    this.menuView.setItemTextAppearanceActive(paramInt);
  }
  
  public void setItemTextAppearanceInactive(int paramInt) {
    this.menuView.setItemTextAppearanceInactive(paramInt);
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.menuView.setItemTextColor(paramColorStateList);
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    if (this.menuView.getLabelVisibilityMode() != paramInt) {
      this.menuView.setLabelVisibilityMode(paramInt);
      this.presenter.updateMenuView(false);
    } 
  }
  
  public void setOnItemReselectedListener(OnItemReselectedListener paramOnItemReselectedListener) {
    this.reselectedListener = paramOnItemReselectedListener;
  }
  
  public void setOnItemSelectedListener(OnItemSelectedListener paramOnItemSelectedListener) {
    this.selectedListener = paramOnItemSelectedListener;
  }
  
  public void setSelectedItemId(int paramInt) {
    MenuItem menuItem = this.menu.findItem(paramInt);
    if (menuItem != null && !this.menu.performItemAction(menuItem, this.presenter, 0))
      menuItem.setChecked(true); 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LabelVisibility {}
  
  public static interface OnItemReselectedListener {
    void onNavigationItemReselected(MenuItem param1MenuItem);
  }
  
  public static interface OnItemSelectedListener {
    boolean onNavigationItemSelected(MenuItem param1MenuItem);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public NavigationBarView.SavedState createFromParcel(Parcel param2Parcel) {
          return new NavigationBarView.SavedState(param2Parcel, null);
        }
        
        public NavigationBarView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new NavigationBarView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public NavigationBarView.SavedState[] newArray(int param2Int) {
          return new NavigationBarView.SavedState[param2Int];
        }
      };
    
    Bundle menuPresenterState;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      readFromParcel(param1Parcel, classLoader);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    private void readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      this.menuPresenterState = param1Parcel.readBundle(param1ClassLoader);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeBundle(this.menuPresenterState);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public NavigationBarView.SavedState createFromParcel(Parcel param1Parcel) {
      return new NavigationBarView.SavedState(param1Parcel, null);
    }
    
    public NavigationBarView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new NavigationBarView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public NavigationBarView.SavedState[] newArray(int param1Int) {
      return new NavigationBarView.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationBarView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */