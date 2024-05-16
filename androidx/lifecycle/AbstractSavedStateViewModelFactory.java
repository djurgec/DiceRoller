package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

public abstract class AbstractSavedStateViewModelFactory extends ViewModelProvider.KeyedFactory {
  static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
  
  private final Bundle mDefaultArgs;
  
  private final Lifecycle mLifecycle;
  
  private final SavedStateRegistry mSavedStateRegistry;
  
  public AbstractSavedStateViewModelFactory(SavedStateRegistryOwner paramSavedStateRegistryOwner, Bundle paramBundle) {
    this.mSavedStateRegistry = paramSavedStateRegistryOwner.getSavedStateRegistry();
    this.mLifecycle = paramSavedStateRegistryOwner.getLifecycle();
    this.mDefaultArgs = paramBundle;
  }
  
  public final <T extends ViewModel> T create(Class<T> paramClass) {
    String str = paramClass.getCanonicalName();
    if (str != null)
      return create(str, paramClass); 
    throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
  }
  
  public final <T extends ViewModel> T create(String paramString, Class<T> paramClass) {
    SavedStateHandleController savedStateHandleController = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, paramString, this.mDefaultArgs);
    paramString = create(paramString, (Class)paramClass, savedStateHandleController.getHandle());
    paramString.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", savedStateHandleController);
    return (T)paramString;
  }
  
  protected abstract <T extends ViewModel> T create(String paramString, Class<T> paramClass, SavedStateHandle paramSavedStateHandle);
  
  void onRequery(ViewModel paramViewModel) {
    SavedStateHandleController.attachHandleIfNeeded(paramViewModel, this.mSavedStateRegistry, this.mLifecycle);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\AbstractSavedStateViewModelFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */