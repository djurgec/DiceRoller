package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.internal.view.SupportSubMenu;

class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu {
  private final SupportSubMenu mSubMenu;
  
  SubMenuWrapperICS(Context paramContext, SupportSubMenu paramSupportSubMenu) {
    super(paramContext, (SupportMenu)paramSupportSubMenu);
    this.mSubMenu = paramSupportSubMenu;
  }
  
  public void clearHeader() {
    this.mSubMenu.clearHeader();
  }
  
  public MenuItem getItem() {
    return getMenuItemWrapper(this.mSubMenu.getItem());
  }
  
  public SubMenu setHeaderIcon(int paramInt) {
    this.mSubMenu.setHeaderIcon(paramInt);
    return this;
  }
  
  public SubMenu setHeaderIcon(Drawable paramDrawable) {
    this.mSubMenu.setHeaderIcon(paramDrawable);
    return this;
  }
  
  public SubMenu setHeaderTitle(int paramInt) {
    this.mSubMenu.setHeaderTitle(paramInt);
    return this;
  }
  
  public SubMenu setHeaderTitle(CharSequence paramCharSequence) {
    this.mSubMenu.setHeaderTitle(paramCharSequence);
    return this;
  }
  
  public SubMenu setHeaderView(View paramView) {
    this.mSubMenu.setHeaderView(paramView);
    return this;
  }
  
  public SubMenu setIcon(int paramInt) {
    this.mSubMenu.setIcon(paramInt);
    return this;
  }
  
  public SubMenu setIcon(Drawable paramDrawable) {
    this.mSubMenu.setIcon(paramDrawable);
    return this;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\view\menu\SubMenuWrapperICS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */