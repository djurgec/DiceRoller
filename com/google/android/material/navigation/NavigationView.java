package com.google.android.material.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class NavigationView extends ScrimInsetsFrameLayout {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int DEF_STYLE_RES;
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
  
  OnNavigationItemSelectedListener listener;
  
  private final int maxWidth;
  
  private final NavigationMenu menu;
  
  private MenuInflater menuInflater;
  
  private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
  
  private final NavigationMenuPresenter presenter;
  
  private final int[] tmpLocation;
  
  static {
    DEF_STYLE_RES = R.style.Widget_Design_NavigationView;
  }
  
  public NavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.navigationViewStyle);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    ColorStateList colorStateList1;
    ColorStateList colorStateList2;
    NavigationMenuPresenter navigationMenuPresenter = new NavigationMenuPresenter();
    this.presenter = navigationMenuPresenter;
    this.tmpLocation = new int[2];
    Context context = getContext();
    NavigationMenu navigationMenu = new NavigationMenu(context);
    this.menu = navigationMenu;
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(context, paramAttributeSet, R.styleable.NavigationView, paramInt, i, new int[0]);
    if (tintTypedArray.hasValue(R.styleable.NavigationView_android_background))
      ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.NavigationView_android_background)); 
    if (getBackground() == null || getBackground() instanceof ColorDrawable) {
      ShapeAppearanceModel shapeAppearanceModel = ShapeAppearanceModel.builder(context, paramAttributeSet, paramInt, i).build();
      Drawable drawable = getBackground();
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
      if (drawable instanceof ColorDrawable)
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(((ColorDrawable)drawable).getColor())); 
      materialShapeDrawable.initializeElevationOverlay(context);
      ViewCompat.setBackground((View)this, (Drawable)materialShapeDrawable);
    } 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_elevation))
      setElevation(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0)); 
    setFitsSystemWindows(tintTypedArray.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
    this.maxWidth = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemIconTint)) {
      colorStateList2 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemIconTint);
    } else {
      colorStateList2 = createDefaultColorStateList(16842808);
    } 
    i = 0;
    paramInt = 0;
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
      paramInt = tintTypedArray.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
      i = 1;
    } 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemIconSize))
      setItemIconSize(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemIconSize, 0)); 
    paramContext = null;
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextColor))
      colorStateList1 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemTextColor); 
    ColorStateList colorStateList3 = colorStateList1;
    if (i == 0) {
      colorStateList3 = colorStateList1;
      if (colorStateList1 == null)
        colorStateList3 = createDefaultColorStateList(16842806); 
    } 
    Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.NavigationView_itemBackground);
    Drawable drawable1 = drawable2;
    if (drawable2 == null) {
      drawable1 = drawable2;
      if (hasShapeAppearance(tintTypedArray))
        drawable1 = createDefaultItemBackground(tintTypedArray); 
    } 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemHorizontalPadding))
      navigationMenuPresenter.setItemHorizontalPadding(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemHorizontalPadding, 0)); 
    int j = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemIconPadding, 0);
    setItemMaxLines(tintTypedArray.getInt(R.styleable.NavigationView_itemMaxLines, 1));
    navigationMenu.setCallback(new MenuBuilder.Callback() {
          final NavigationView this$0;
          
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            boolean bool;
            if (NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(param1MenuItem)) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
    navigationMenuPresenter.setId(1);
    navigationMenuPresenter.initForMenu(context, (MenuBuilder)navigationMenu);
    navigationMenuPresenter.setItemIconTintList(colorStateList2);
    navigationMenuPresenter.setOverScrollMode(getOverScrollMode());
    if (i != 0)
      navigationMenuPresenter.setItemTextAppearance(paramInt); 
    navigationMenuPresenter.setItemTextColor(colorStateList3);
    navigationMenuPresenter.setItemBackground(drawable1);
    navigationMenuPresenter.setItemIconPadding(j);
    navigationMenu.addMenuPresenter((MenuPresenter)navigationMenuPresenter);
    addView((View)navigationMenuPresenter.getMenuView((ViewGroup)this));
    if (tintTypedArray.hasValue(R.styleable.NavigationView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationView_menu, 0)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_headerLayout))
      inflateHeaderView(tintTypedArray.getResourceId(R.styleable.NavigationView_headerLayout, 0)); 
    tintTypedArray.recycle();
    setupInsetScrimsListener();
  }
  
  private ColorStateList createDefaultColorStateList(int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (!getContext().getTheme().resolveAttribute(paramInt, typedValue, true))
      return null; 
    ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
    if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true))
      return null; 
    int j = typedValue.data;
    paramInt = colorStateList.getDefaultColor();
    int[] arrayOfInt2 = DISABLED_STATE_SET;
    int[] arrayOfInt1 = CHECKED_STATE_SET;
    int[] arrayOfInt3 = EMPTY_STATE_SET;
    int i = colorStateList.getColorForState(arrayOfInt2, paramInt);
    return new ColorStateList(new int[][] { arrayOfInt2, arrayOfInt1, arrayOfInt3 }, new int[] { i, j, paramInt });
  }
  
  private final Drawable createDefaultItemBackground(TintTypedArray paramTintTypedArray) {
    int i = paramTintTypedArray.getResourceId(R.styleable.NavigationView_itemShapeAppearance, 0);
    int j = paramTintTypedArray.getResourceId(R.styleable.NavigationView_itemShapeAppearanceOverlay, 0);
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(ShapeAppearanceModel.builder(getContext(), i, j).build());
    materialShapeDrawable.setFillColor(MaterialResources.getColorStateList(getContext(), paramTintTypedArray, R.styleable.NavigationView_itemShapeFillColor));
    return (Drawable)new InsetDrawable((Drawable)materialShapeDrawable, paramTintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemShapeInsetStart, 0), paramTintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemShapeInsetTop, 0), paramTintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemShapeInsetEnd, 0), paramTintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemShapeInsetBottom, 0));
  }
  
  private MenuInflater getMenuInflater() {
    if (this.menuInflater == null)
      this.menuInflater = (MenuInflater)new SupportMenuInflater(getContext()); 
    return this.menuInflater;
  }
  
  private boolean hasShapeAppearance(TintTypedArray paramTintTypedArray) {
    return (paramTintTypedArray.hasValue(R.styleable.NavigationView_itemShapeAppearance) || paramTintTypedArray.hasValue(R.styleable.NavigationView_itemShapeAppearanceOverlay));
  }
  
  private void setupInsetScrimsListener() {
    this.onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        final NavigationView this$0;
        
        public void onGlobalLayout() {
          boolean bool1;
          NavigationView navigationView = NavigationView.this;
          navigationView.getLocationOnScreen(navigationView.tmpLocation);
          int[] arrayOfInt = NavigationView.this.tmpLocation;
          boolean bool2 = true;
          if (arrayOfInt[1] == 0) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          NavigationView.this.presenter.setBehindStatusBar(bool1);
          NavigationView.this.setDrawTopInsetForeground(bool1);
          Activity activity = ContextUtils.getActivity(NavigationView.this.getContext());
          if (activity != null && Build.VERSION.SDK_INT >= 21) {
            boolean bool3;
            boolean bool4;
            if (activity.findViewById(16908290).getHeight() == NavigationView.this.getHeight()) {
              bool3 = true;
            } else {
              bool3 = false;
            } 
            if (Color.alpha(activity.getWindow().getNavigationBarColor()) != 0) {
              bool4 = true;
            } else {
              bool4 = false;
            } 
            NavigationView navigationView1 = NavigationView.this;
            if (bool3 && bool4) {
              bool1 = bool2;
            } else {
              bool1 = false;
            } 
            navigationView1.setDrawBottomInsetForeground(bool1);
          } 
        }
      };
    getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
  }
  
  public void addHeaderView(View paramView) {
    this.presenter.addHeaderView(paramView);
  }
  
  public MenuItem getCheckedItem() {
    return (MenuItem)this.presenter.getCheckedItem();
  }
  
  public int getHeaderCount() {
    return this.presenter.getHeaderCount();
  }
  
  public View getHeaderView(int paramInt) {
    return this.presenter.getHeaderView(paramInt);
  }
  
  public Drawable getItemBackground() {
    return this.presenter.getItemBackground();
  }
  
  public int getItemHorizontalPadding() {
    return this.presenter.getItemHorizontalPadding();
  }
  
  public int getItemIconPadding() {
    return this.presenter.getItemIconPadding();
  }
  
  public ColorStateList getItemIconTintList() {
    return this.presenter.getItemTintList();
  }
  
  public int getItemMaxLines() {
    return this.presenter.getItemMaxLines();
  }
  
  public ColorStateList getItemTextColor() {
    return this.presenter.getItemTextColor();
  }
  
  public Menu getMenu() {
    return (Menu)this.menu;
  }
  
  public View inflateHeaderView(int paramInt) {
    return this.presenter.inflateHeaderView(paramInt);
  }
  
  public void inflateMenu(int paramInt) {
    this.presenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.menu);
    this.presenter.setUpdateSuspended(false);
    this.presenter.updateMenuView(false);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this);
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (Build.VERSION.SDK_INT < 16) {
      getViewTreeObserver().removeGlobalOnLayoutListener(this.onGlobalLayoutListener);
    } else {
      getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
    } 
  }
  
  protected void onInsetsChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    this.presenter.dispatchApplyWindowInsets(paramWindowInsetsCompat);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    switch (View.MeasureSpec.getMode(paramInt1)) {
      case 0:
        paramInt1 = View.MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
        break;
      case -2147483648:
        paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(paramInt1), this.maxWidth), 1073741824);
        break;
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.menu.restorePresenterStates(savedState.menuState);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.menuState = new Bundle();
    this.menu.savePresenterStates(savedState.menuState);
    return (Parcelable)savedState;
  }
  
  public void removeHeaderView(View paramView) {
    this.presenter.removeHeaderView(paramView);
  }
  
  public void setCheckedItem(int paramInt) {
    MenuItem menuItem = this.menu.findItem(paramInt);
    if (menuItem != null)
      this.presenter.setCheckedItem((MenuItemImpl)menuItem); 
  }
  
  public void setCheckedItem(MenuItem paramMenuItem) {
    paramMenuItem = this.menu.findItem(paramMenuItem.getItemId());
    if (paramMenuItem != null) {
      this.presenter.setCheckedItem((MenuItemImpl)paramMenuItem);
      return;
    } 
    throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
  }
  
  public void setElevation(float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      super.setElevation(paramFloat); 
    MaterialShapeUtils.setElevation((View)this, paramFloat);
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.presenter.setItemBackground(paramDrawable);
  }
  
  public void setItemBackgroundResource(int paramInt) {
    setItemBackground(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setItemHorizontalPadding(int paramInt) {
    this.presenter.setItemHorizontalPadding(paramInt);
  }
  
  public void setItemHorizontalPaddingResource(int paramInt) {
    this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconPadding(int paramInt) {
    this.presenter.setItemIconPadding(paramInt);
  }
  
  public void setItemIconPaddingResource(int paramInt) {
    this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconSize(int paramInt) {
    this.presenter.setItemIconSize(paramInt);
  }
  
  public void setItemIconTintList(ColorStateList paramColorStateList) {
    this.presenter.setItemIconTintList(paramColorStateList);
  }
  
  public void setItemMaxLines(int paramInt) {
    this.presenter.setItemMaxLines(paramInt);
  }
  
  public void setItemTextAppearance(int paramInt) {
    this.presenter.setItemTextAppearance(paramInt);
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.presenter.setItemTextColor(paramColorStateList);
  }
  
  public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    this.listener = paramOnNavigationItemSelectedListener;
  }
  
  public void setOverScrollMode(int paramInt) {
    super.setOverScrollMode(paramInt);
    NavigationMenuPresenter navigationMenuPresenter = this.presenter;
    if (navigationMenuPresenter != null)
      navigationMenuPresenter.setOverScrollMode(paramInt); 
  }
  
  public static interface OnNavigationItemSelectedListener {
    boolean onNavigationItemSelected(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public NavigationView.SavedState createFromParcel(Parcel param2Parcel) {
          return new NavigationView.SavedState(param2Parcel, null);
        }
        
        public NavigationView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new NavigationView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public NavigationView.SavedState[] newArray(int param2Int) {
          return new NavigationView.SavedState[param2Int];
        }
      };
    
    public Bundle menuState;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.menuState = param1Parcel.readBundle(param1ClassLoader);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeBundle(this.menuState);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public NavigationView.SavedState createFromParcel(Parcel param1Parcel) {
      return new NavigationView.SavedState(param1Parcel, null);
    }
    
    public NavigationView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new NavigationView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public NavigationView.SavedState[] newArray(int param1Int) {
      return new NavigationView.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */