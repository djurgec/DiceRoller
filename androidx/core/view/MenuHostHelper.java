package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuHostHelper {
  private final CopyOnWriteArrayList<MenuProvider> mMenuProviders = new CopyOnWriteArrayList<>();
  
  private final Runnable mOnInvalidateMenuCallback;
  
  private final Map<MenuProvider, LifecycleContainer> mProviderToLifecycleContainers = new HashMap<>();
  
  public MenuHostHelper(Runnable paramRunnable) {
    this.mOnInvalidateMenuCallback = paramRunnable;
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider) {
    this.mMenuProviders.add(paramMenuProvider);
    this.mOnInvalidateMenuCallback.run();
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner) {
    addMenuProvider(paramMenuProvider);
    Lifecycle lifecycle = paramLifecycleOwner.getLifecycle();
    LifecycleContainer lifecycleContainer = this.mProviderToLifecycleContainers.remove(paramMenuProvider);
    if (lifecycleContainer != null)
      lifecycleContainer.clearObservers(); 
    MenuHostHelper$$ExternalSyntheticLambda0 menuHostHelper$$ExternalSyntheticLambda0 = new MenuHostHelper$$ExternalSyntheticLambda0(this, paramMenuProvider);
    this.mProviderToLifecycleContainers.put(paramMenuProvider, new LifecycleContainer(lifecycle, menuHostHelper$$ExternalSyntheticLambda0));
  }
  
  public void addMenuProvider(MenuProvider paramMenuProvider, LifecycleOwner paramLifecycleOwner, Lifecycle.State paramState) {
    Lifecycle lifecycle = paramLifecycleOwner.getLifecycle();
    LifecycleContainer lifecycleContainer = this.mProviderToLifecycleContainers.remove(paramMenuProvider);
    if (lifecycleContainer != null)
      lifecycleContainer.clearObservers(); 
    MenuHostHelper$$ExternalSyntheticLambda1 menuHostHelper$$ExternalSyntheticLambda1 = new MenuHostHelper$$ExternalSyntheticLambda1(this, paramState, paramMenuProvider);
    this.mProviderToLifecycleContainers.put(paramMenuProvider, new LifecycleContainer(lifecycle, menuHostHelper$$ExternalSyntheticLambda1));
  }
  
  public void onCreateMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    Iterator<MenuProvider> iterator = this.mMenuProviders.iterator();
    while (iterator.hasNext())
      ((MenuProvider)iterator.next()).onCreateMenu(paramMenu, paramMenuInflater); 
  }
  
  public boolean onMenuItemSelected(MenuItem paramMenuItem) {
    Iterator<MenuProvider> iterator = this.mMenuProviders.iterator();
    while (iterator.hasNext()) {
      if (((MenuProvider)iterator.next()).onMenuItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void removeMenuProvider(MenuProvider paramMenuProvider) {
    this.mMenuProviders.remove(paramMenuProvider);
    LifecycleContainer lifecycleContainer = this.mProviderToLifecycleContainers.remove(paramMenuProvider);
    if (lifecycleContainer != null)
      lifecycleContainer.clearObservers(); 
    this.mOnInvalidateMenuCallback.run();
  }
  
  private static class LifecycleContainer {
    final Lifecycle mLifecycle;
    
    private LifecycleEventObserver mObserver;
    
    LifecycleContainer(Lifecycle param1Lifecycle, LifecycleEventObserver param1LifecycleEventObserver) {
      this.mLifecycle = param1Lifecycle;
      this.mObserver = param1LifecycleEventObserver;
      param1Lifecycle.addObserver((LifecycleObserver)param1LifecycleEventObserver);
    }
    
    void clearObservers() {
      this.mLifecycle.removeObserver((LifecycleObserver)this.mObserver);
      this.mObserver = null;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\MenuHostHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */