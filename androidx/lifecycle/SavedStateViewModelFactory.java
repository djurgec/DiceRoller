package androidx.lifecycle;

import android.app.Application;
import android.os.Bundle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class SavedStateViewModelFactory extends ViewModelProvider.KeyedFactory {
  private static final Class<?>[] ANDROID_VIEWMODEL_SIGNATURE = new Class[] { Application.class, SavedStateHandle.class };
  
  private static final Class<?>[] VIEWMODEL_SIGNATURE = new Class[] { SavedStateHandle.class };
  
  private final Application mApplication;
  
  private final Bundle mDefaultArgs;
  
  private final ViewModelProvider.Factory mFactory;
  
  private final Lifecycle mLifecycle;
  
  private final SavedStateRegistry mSavedStateRegistry;
  
  public SavedStateViewModelFactory(Application paramApplication, SavedStateRegistryOwner paramSavedStateRegistryOwner) {
    this(paramApplication, paramSavedStateRegistryOwner, null);
  }
  
  public SavedStateViewModelFactory(Application paramApplication, SavedStateRegistryOwner paramSavedStateRegistryOwner, Bundle paramBundle) {
    ViewModelProvider.NewInstanceFactory newInstanceFactory;
    this.mSavedStateRegistry = paramSavedStateRegistryOwner.getSavedStateRegistry();
    this.mLifecycle = paramSavedStateRegistryOwner.getLifecycle();
    this.mDefaultArgs = paramBundle;
    this.mApplication = paramApplication;
    if (paramApplication != null) {
      newInstanceFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(paramApplication);
    } else {
      newInstanceFactory = ViewModelProvider.NewInstanceFactory.getInstance();
    } 
    this.mFactory = newInstanceFactory;
  }
  
  private static <T> Constructor<T> findMatchingConstructor(Class<T> paramClass, Class<?>[] paramArrayOfClass) {
    for (Constructor<T> constructor : paramClass.getConstructors()) {
      if (Arrays.equals((Object[])paramArrayOfClass, (Object[])constructor.getParameterTypes()))
        return constructor; 
    } 
    return null;
  }
  
  public <T extends ViewModel> T create(Class<T> paramClass) {
    String str = paramClass.getCanonicalName();
    if (str != null)
      return create(str, paramClass); 
    throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
  }
  
  public <T extends ViewModel> T create(String paramString, Class<T> paramClass) {
    Constructor<T> constructor;
    boolean bool = AndroidViewModel.class.isAssignableFrom(paramClass);
    if (bool && this.mApplication != null) {
      constructor = findMatchingConstructor(paramClass, ANDROID_VIEWMODEL_SIGNATURE);
    } else {
      constructor = findMatchingConstructor(paramClass, VIEWMODEL_SIGNATURE);
    } 
    if (constructor == null)
      return this.mFactory.create(paramClass); 
    SavedStateHandleController savedStateHandleController = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, paramString, this.mDefaultArgs);
    if (bool)
      try {
        Application application = this.mApplication;
        if (application != null) {
          ViewModel viewModel2 = (ViewModel)constructor.newInstance(new Object[] { application, savedStateHandleController.getHandle() });
          viewModel2.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", savedStateHandleController);
          return (T)viewModel2;
        } 
        ViewModel viewModel1 = (ViewModel)constructor.newInstance(new Object[] { savedStateHandleController.getHandle() });
        viewModel1.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", savedStateHandleController);
        return (T)viewModel1;
      } catch (IllegalAccessException illegalAccessException) {
        throw new RuntimeException("Failed to access " + paramClass, illegalAccessException);
      } catch (InstantiationException instantiationException) {
        throw new RuntimeException("A " + paramClass + " cannot be instantiated.", instantiationException);
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException("An exception happened in constructor of " + paramClass, invocationTargetException.getCause());
      }  
    ViewModel viewModel = (ViewModel)constructor.newInstance(new Object[] { savedStateHandleController.getHandle() });
    viewModel.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", savedStateHandleController);
    return (T)viewModel;
  }
  
  void onRequery(ViewModel paramViewModel) {
    SavedStateHandleController.attachHandleIfNeeded(paramViewModel, this.mSavedStateRegistry, this.mLifecycle);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\SavedStateViewModelFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */