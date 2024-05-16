package com.google.android.material.navigation;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;

public final class NavigationBarMenu extends MenuBuilder {
  private final int maxItemCount;
  
  private final Class<?> viewClass;
  
  public NavigationBarMenu(Context paramContext, Class<?> paramClass, int paramInt) {
    super(paramContext);
    this.viewClass = paramClass;
    this.maxItemCount = paramInt;
  }
  
  protected MenuItem addInternal(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
    if (size() + 1 <= this.maxItemCount) {
      stopDispatchingItemsChanged();
      MenuItem menuItem = super.addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
      if (menuItem instanceof MenuItemImpl)
        ((MenuItemImpl)menuItem).setExclusiveCheckable(true); 
      startDispatchingItemsChanged();
      return menuItem;
    } 
    paramCharSequence = this.viewClass.getSimpleName();
    throw new IllegalArgumentException("Maximum number of items supported by " + paramCharSequence + " is " + this.maxItemCount + ". Limit can be checked with " + paramCharSequence + "#getMaxItemCount()");
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
    throw new UnsupportedOperationException(this.viewClass.getSimpleName() + " does not support submenus");
  }
  
  public int getMaxItemCount() {
    return this.maxItemCount;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationBarMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */