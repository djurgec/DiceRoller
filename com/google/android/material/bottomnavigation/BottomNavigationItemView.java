package com.google.android.material.bottomnavigation;

import android.content.Context;
import com.google.android.material.R;
import com.google.android.material.navigation.NavigationBarItemView;

public class BottomNavigationItemView extends NavigationBarItemView {
  public BottomNavigationItemView(Context paramContext) {
    super(paramContext);
  }
  
  protected int getItemDefaultMarginResId() {
    return R.dimen.design_bottom_navigation_margin;
  }
  
  protected int getItemLayoutResId() {
    return R.layout.design_bottom_navigation_item;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\BottomNavigationItemView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */