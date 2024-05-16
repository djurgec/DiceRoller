package androidx.savedstate;

import android.os.Bundle;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.Map;

public final class SavedStateRegistry {
  private static final String SAVED_COMPONENTS_KEY = "androidx.lifecycle.BundlableSavedStateRegistry.key";
  
  boolean mAllowingSavingState = true;
  
  private SafeIterableMap<String, SavedStateProvider> mComponents = new SafeIterableMap();
  
  private Recreator.SavedStateProvider mRecreatorProvider;
  
  private boolean mRestored;
  
  private Bundle mRestoredState;
  
  public Bundle consumeRestoredStateForKey(String paramString) {
    if (this.mRestored) {
      Bundle bundle = this.mRestoredState;
      if (bundle != null) {
        bundle = bundle.getBundle(paramString);
        this.mRestoredState.remove(paramString);
        if (this.mRestoredState.isEmpty())
          this.mRestoredState = null; 
        return bundle;
      } 
      return null;
    } 
    throw new IllegalStateException("You can consumeRestoredStateForKey only after super.onCreate of corresponding component");
  }
  
  public boolean isRestored() {
    return this.mRestored;
  }
  
  void performRestore(Lifecycle paramLifecycle, Bundle paramBundle) {
    if (!this.mRestored) {
      if (paramBundle != null)
        this.mRestoredState = paramBundle.getBundle("androidx.lifecycle.BundlableSavedStateRegistry.key"); 
      paramLifecycle.addObserver((LifecycleObserver)new GenericLifecycleObserver() {
            final SavedStateRegistry this$0;
            
            public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
              if (param1Event == Lifecycle.Event.ON_START) {
                SavedStateRegistry.this.mAllowingSavingState = true;
              } else if (param1Event == Lifecycle.Event.ON_STOP) {
                SavedStateRegistry.this.mAllowingSavingState = false;
              } 
            }
          });
      this.mRestored = true;
      return;
    } 
    throw new IllegalStateException("SavedStateRegistry was already restored.");
  }
  
  void performSave(Bundle paramBundle) {
    Bundle bundle1 = new Bundle();
    Bundle bundle2 = this.mRestoredState;
    if (bundle2 != null)
      bundle1.putAll(bundle2); 
    SafeIterableMap.IteratorWithAdditions<Map.Entry> iteratorWithAdditions = this.mComponents.iteratorWithAdditions();
    while (iteratorWithAdditions.hasNext()) {
      Map.Entry entry = iteratorWithAdditions.next();
      bundle1.putBundle((String)entry.getKey(), ((SavedStateProvider)entry.getValue()).saveState());
    } 
    paramBundle.putBundle("androidx.lifecycle.BundlableSavedStateRegistry.key", bundle1);
  }
  
  public void registerSavedStateProvider(String paramString, SavedStateProvider paramSavedStateProvider) {
    if ((SavedStateProvider)this.mComponents.putIfAbsent(paramString, paramSavedStateProvider) == null)
      return; 
    throw new IllegalArgumentException("SavedStateProvider with the given key is already registered");
  }
  
  public void runOnNextRecreation(Class<? extends AutoRecreated> paramClass) {
    if (this.mAllowingSavingState) {
      if (this.mRecreatorProvider == null)
        this.mRecreatorProvider = new Recreator.SavedStateProvider(this); 
      try {
        paramClass.getDeclaredConstructor(new Class[0]);
        this.mRecreatorProvider.add(paramClass.getName());
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new IllegalArgumentException("Class" + paramClass.getSimpleName() + " must have default constructor in order to be automatically recreated", noSuchMethodException);
      } 
    } 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  public void unregisterSavedStateProvider(String paramString) {
    this.mComponents.remove(paramString);
  }
  
  public static interface AutoRecreated {
    void onRecreated(SavedStateRegistryOwner param1SavedStateRegistryOwner);
  }
  
  public static interface SavedStateProvider {
    Bundle saveState();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\savedstate\SavedStateRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */