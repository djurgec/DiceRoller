package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationView extends NavigationBarView {
  static final int MAX_ITEM_COUNT = 5;
  
  public BottomNavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.bottomNavigationStyle);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, R.style.Widget_Design_BottomNavigationView);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = getContext();
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(paramContext, paramAttributeSet, R.styleable.BottomNavigationView, paramInt1, paramInt2, new int[0]);
    setItemHorizontalTranslationEnabled(tintTypedArray.getBoolean(R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
    tintTypedArray.recycle();
    if (shouldDrawCompatibilityTopDivider())
      addCompatibilityTopDivider(paramContext); 
  }
  
  private void addCompatibilityTopDivider(Context paramContext) {
    View view = new View(paramContext);
    view.setBackgroundColor(ContextCompat.getColor(paramContext, R.color.design_bottom_navigation_shadow_color));
    view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
    addView(view);
  }
  
  private boolean shouldDrawCompatibilityTopDivider() {
    boolean bool;
    if (Build.VERSION.SDK_INT < 21 && !(getBackground() instanceof com.google.android.material.shape.MaterialShapeDrawable)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected NavigationBarMenuView createNavigationBarMenuView(Context paramContext) {
    return new BottomNavigationMenuView(paramContext);
  }
  
  public int getMaxItemCount() {
    return 5;
  }
  
  public boolean isItemHorizontalTranslationEnabled() {
    return ((BottomNavigationMenuView)getMenuView()).isItemHorizontalTranslationEnabled();
  }
  
  public void setItemHorizontalTranslationEnabled(boolean paramBoolean) {
    BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView)getMenuView();
    if (bottomNavigationMenuView.isItemHorizontalTranslationEnabled() != paramBoolean) {
      bottomNavigationMenuView.setItemHorizontalTranslationEnabled(paramBoolean);
      getPresenter().updateMenuView(false);
    } 
  }
  
  @Deprecated
  public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener paramOnNavigationItemReselectedListener) {
    setOnItemReselectedListener(paramOnNavigationItemReselectedListener);
  }
  
  @Deprecated
  public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    setOnItemSelectedListener(paramOnNavigationItemSelectedListener);
  }
  
  @Deprecated
  public static interface OnNavigationItemReselectedListener extends NavigationBarView.OnItemReselectedListener {}
  
  @Deprecated
  public static interface OnNavigationItemSelectedListener extends NavigationBarView.OnItemSelectedListener {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\BottomNavigationView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */