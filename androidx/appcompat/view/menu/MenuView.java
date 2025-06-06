package androidx.appcompat.view.menu;

import android.graphics.drawable.Drawable;

public interface MenuView {
  int getWindowAnimations();
  
  void initialize(MenuBuilder paramMenuBuilder);
  
  public static interface ItemView {
    MenuItemImpl getItemData();
    
    void initialize(MenuItemImpl param1MenuItemImpl, int param1Int);
    
    boolean prefersCondensedTitle();
    
    void setCheckable(boolean param1Boolean);
    
    void setChecked(boolean param1Boolean);
    
    void setEnabled(boolean param1Boolean);
    
    void setIcon(Drawable param1Drawable);
    
    void setShortcut(boolean param1Boolean, char param1Char);
    
    void setTitle(CharSequence param1CharSequence);
    
    boolean showsIcon();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\view\menu\MenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */