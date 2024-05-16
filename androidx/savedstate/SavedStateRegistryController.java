package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

public final class SavedStateRegistryController {
  private final SavedStateRegistryOwner mOwner;
  
  private final SavedStateRegistry mRegistry;
  
  private SavedStateRegistryController(SavedStateRegistryOwner paramSavedStateRegistryOwner) {
    this.mOwner = paramSavedStateRegistryOwner;
    this.mRegistry = new SavedStateRegistry();
  }
  
  public static SavedStateRegistryController create(SavedStateRegistryOwner paramSavedStateRegistryOwner) {
    return new SavedStateRegistryController(paramSavedStateRegistryOwner);
  }
  
  public SavedStateRegistry getSavedStateRegistry() {
    return this.mRegistry;
  }
  
  public void performRestore(Bundle paramBundle) {
    Lifecycle lifecycle = this.mOwner.getLifecycle();
    if (lifecycle.getCurrentState() == Lifecycle.State.INITIALIZED) {
      lifecycle.addObserver((LifecycleObserver)new Recreator(this.mOwner));
      this.mRegistry.performRestore(lifecycle, paramBundle);
      return;
    } 
    throw new IllegalStateException("Restarter must be created only during owner's initialization stage");
  }
  
  public void performSave(Bundle paramBundle) {
    this.mRegistry.performSave(paramBundle);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\savedstate\SavedStateRegistryController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */