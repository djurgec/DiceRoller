package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.Iterator;

final class SavedStateHandleController implements LifecycleEventObserver {
  static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
  
  private final SavedStateHandle mHandle;
  
  private boolean mIsAttached = false;
  
  private final String mKey;
  
  SavedStateHandleController(String paramString, SavedStateHandle paramSavedStateHandle) {
    this.mKey = paramString;
    this.mHandle = paramSavedStateHandle;
  }
  
  static void attachHandleIfNeeded(ViewModel paramViewModel, SavedStateRegistry paramSavedStateRegistry, Lifecycle paramLifecycle) {
    SavedStateHandleController savedStateHandleController = paramViewModel.<SavedStateHandleController>getTag("androidx.lifecycle.savedstate.vm.tag");
    if (savedStateHandleController != null && !savedStateHandleController.isAttached()) {
      savedStateHandleController.attachToLifecycle(paramSavedStateRegistry, paramLifecycle);
      tryToAddRecreator(paramSavedStateRegistry, paramLifecycle);
    } 
  }
  
  static SavedStateHandleController create(SavedStateRegistry paramSavedStateRegistry, Lifecycle paramLifecycle, String paramString, Bundle paramBundle) {
    SavedStateHandleController savedStateHandleController = new SavedStateHandleController(paramString, SavedStateHandle.createHandle(paramSavedStateRegistry.consumeRestoredStateForKey(paramString), paramBundle));
    savedStateHandleController.attachToLifecycle(paramSavedStateRegistry, paramLifecycle);
    tryToAddRecreator(paramSavedStateRegistry, paramLifecycle);
    return savedStateHandleController;
  }
  
  private static void tryToAddRecreator(final SavedStateRegistry registry, final Lifecycle lifecycle) {
    Lifecycle.State state = lifecycle.getCurrentState();
    if (state == Lifecycle.State.INITIALIZED || state.isAtLeast(Lifecycle.State.STARTED)) {
      registry.runOnNextRecreation(OnRecreation.class);
      return;
    } 
    lifecycle.addObserver(new LifecycleEventObserver() {
          final Lifecycle val$lifecycle;
          
          final SavedStateRegistry val$registry;
          
          public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
            if (param1Event == Lifecycle.Event.ON_START) {
              lifecycle.removeObserver(this);
              registry.runOnNextRecreation(SavedStateHandleController.OnRecreation.class);
            } 
          }
        });
  }
  
  void attachToLifecycle(SavedStateRegistry paramSavedStateRegistry, Lifecycle paramLifecycle) {
    if (!this.mIsAttached) {
      this.mIsAttached = true;
      paramLifecycle.addObserver(this);
      paramSavedStateRegistry.registerSavedStateProvider(this.mKey, this.mHandle.savedStateProvider());
      return;
    } 
    throw new IllegalStateException("Already attached to lifecycleOwner");
  }
  
  SavedStateHandle getHandle() {
    return this.mHandle;
  }
  
  boolean isAttached() {
    return this.mIsAttached;
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent) {
    if (paramEvent == Lifecycle.Event.ON_DESTROY) {
      this.mIsAttached = false;
      paramLifecycleOwner.getLifecycle().removeObserver(this);
    } 
  }
  
  static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
    public void onRecreated(SavedStateRegistryOwner param1SavedStateRegistryOwner) {
      if (param1SavedStateRegistryOwner instanceof ViewModelStoreOwner) {
        ViewModelStore viewModelStore = ((ViewModelStoreOwner)param1SavedStateRegistryOwner).getViewModelStore();
        SavedStateRegistry savedStateRegistry = param1SavedStateRegistryOwner.getSavedStateRegistry();
        Iterator<String> iterator = viewModelStore.keys().iterator();
        while (iterator.hasNext())
          SavedStateHandleController.attachHandleIfNeeded(viewModelStore.get(iterator.next()), savedStateRegistry, param1SavedStateRegistryOwner.getLifecycle()); 
        if (!viewModelStore.keys().isEmpty())
          savedStateRegistry.runOnNextRecreation(OnRecreation.class); 
        return;
      } 
      throw new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\SavedStateHandleController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */