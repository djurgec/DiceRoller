package androidx.core.view;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public interface MenuHost {
  void addMenuProvider(MenuProvider paramMenuProvider);
  
  void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner);
  
  void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner, Lifecycle.State paramState);
  
  void invalidateMenu();
  
  void removeMenuProvider(MenuProvider paramMenuProvider);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\MenuHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */