package androidx.fragment.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;

class FragmentViewLifecycleOwner implements HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, ViewModelStoreOwner {
  private ViewModelProvider.Factory mDefaultFactory;
  
  private final Fragment mFragment;
  
  private LifecycleRegistry mLifecycleRegistry = null;
  
  private SavedStateRegistryController mSavedStateRegistryController = null;
  
  private final ViewModelStore mViewModelStore;
  
  FragmentViewLifecycleOwner(Fragment paramFragment, ViewModelStore paramViewModelStore) {
    this.mFragment = paramFragment;
    this.mViewModelStore = paramViewModelStore;
  }
  
  public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
    ViewModelProvider.Factory factory = this.mFragment.getDefaultViewModelProviderFactory();
    if (!factory.equals(this.mFragment.mDefaultFactory)) {
      this.mDefaultFactory = factory;
      return factory;
    } 
    if (this.mDefaultFactory == null) {
      Application application1;
      Application application2 = null;
      Context context = this.mFragment.requireContext().getApplicationContext();
      while (true) {
        application1 = application2;
        if (context instanceof ContextWrapper) {
          if (context instanceof Application) {
            application1 = (Application)context;
            break;
          } 
          context = ((ContextWrapper)context).getBaseContext();
          continue;
        } 
        break;
      } 
      this.mDefaultFactory = (ViewModelProvider.Factory)new SavedStateViewModelFactory(application1, this, this.mFragment.getArguments());
    } 
    return this.mDefaultFactory;
  }
  
  public Lifecycle getLifecycle() {
    initialize();
    return (Lifecycle)this.mLifecycleRegistry;
  }
  
  public SavedStateRegistry getSavedStateRegistry() {
    initialize();
    return this.mSavedStateRegistryController.getSavedStateRegistry();
  }
  
  public ViewModelStore getViewModelStore() {
    initialize();
    return this.mViewModelStore;
  }
  
  void handleLifecycleEvent(Lifecycle.Event paramEvent) {
    this.mLifecycleRegistry.handleLifecycleEvent(paramEvent);
  }
  
  void initialize() {
    if (this.mLifecycleRegistry == null) {
      this.mLifecycleRegistry = new LifecycleRegistry((LifecycleOwner)this);
      this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
    } 
  }
  
  boolean isInitialized() {
    boolean bool;
    if (this.mLifecycleRegistry != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void performRestore(Bundle paramBundle) {
    this.mSavedStateRegistryController.performRestore(paramBundle);
  }
  
  void performSave(Bundle paramBundle) {
    this.mSavedStateRegistryController.performSave(paramBundle);
  }
  
  void setCurrentState(Lifecycle.State paramState) {
    this.mLifecycleRegistry.setCurrentState(paramState);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentViewLifecycleOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */