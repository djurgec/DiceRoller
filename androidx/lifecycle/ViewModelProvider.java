package androidx.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
  private static final String DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey";
  
  private final Factory mFactory;
  
  private final ViewModelStore mViewModelStore;
  
  public ViewModelProvider(ViewModelStore paramViewModelStore, Factory paramFactory) {
    this.mFactory = paramFactory;
    this.mViewModelStore = paramViewModelStore;
  }
  
  public ViewModelProvider(ViewModelStoreOwner paramViewModelStoreOwner) {
    this(viewModelStore, factory);
  }
  
  public ViewModelProvider(ViewModelStoreOwner paramViewModelStoreOwner, Factory paramFactory) {
    this(paramViewModelStoreOwner.getViewModelStore(), paramFactory);
  }
  
  public <T extends ViewModel> T get(Class<T> paramClass) {
    String str = paramClass.getCanonicalName();
    if (str != null)
      return get("androidx.lifecycle.ViewModelProvider.DefaultKey:" + str, paramClass); 
    throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
  }
  
  public <T extends ViewModel> T get(String paramString, Class<T> paramClass) {
    Factory factory1;
    ViewModel viewModel = this.mViewModelStore.get(paramString);
    if (paramClass.isInstance(viewModel)) {
      factory1 = this.mFactory;
      if (factory1 instanceof OnRequeryFactory)
        ((OnRequeryFactory)factory1).onRequery(viewModel); 
      return (T)viewModel;
    } 
    Factory factory2 = this.mFactory;
    if (factory2 instanceof KeyedFactory) {
      paramClass = ((KeyedFactory)factory2).create((String)factory1, (Class)paramClass);
    } else {
      paramClass = factory2.create((Class)paramClass);
    } 
    this.mViewModelStore.put((String)factory1, (ViewModel)paramClass);
    return (T)paramClass;
  }
  
  public static class AndroidViewModelFactory extends NewInstanceFactory {
    private static AndroidViewModelFactory sInstance;
    
    private Application mApplication;
    
    public AndroidViewModelFactory(Application param1Application) {
      this.mApplication = param1Application;
    }
    
    public static AndroidViewModelFactory getInstance(Application param1Application) {
      if (sInstance == null)
        sInstance = new AndroidViewModelFactory(param1Application); 
      return sInstance;
    }
    
    public <T extends ViewModel> T create(Class<T> param1Class) {
      if (AndroidViewModel.class.isAssignableFrom(param1Class))
        try {
          return (T)param1Class.getConstructor(new Class[] { Application.class }).newInstance(new Object[] { this.mApplication });
        } catch (NoSuchMethodException noSuchMethodException) {
          throw new RuntimeException("Cannot create an instance of " + param1Class, noSuchMethodException);
        } catch (IllegalAccessException illegalAccessException) {
          throw new RuntimeException("Cannot create an instance of " + param1Class, illegalAccessException);
        } catch (InstantiationException instantiationException) {
          throw new RuntimeException("Cannot create an instance of " + param1Class, instantiationException);
        } catch (InvocationTargetException invocationTargetException) {
          throw new RuntimeException("Cannot create an instance of " + param1Class, invocationTargetException);
        }  
      return super.create(param1Class);
    }
  }
  
  public static interface Factory {
    <T extends ViewModel> T create(Class<T> param1Class);
  }
  
  static abstract class KeyedFactory extends OnRequeryFactory implements Factory {
    public <T extends ViewModel> T create(Class<T> param1Class) {
      throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory");
    }
    
    public abstract <T extends ViewModel> T create(String param1String, Class<T> param1Class);
  }
  
  public static class NewInstanceFactory implements Factory {
    private static NewInstanceFactory sInstance;
    
    static NewInstanceFactory getInstance() {
      if (sInstance == null)
        sInstance = new NewInstanceFactory(); 
      return sInstance;
    }
    
    public <T extends ViewModel> T create(Class<T> param1Class) {
      try {
        return param1Class.newInstance();
      } catch (InstantiationException instantiationException) {
        throw new RuntimeException("Cannot create an instance of " + param1Class, instantiationException);
      } catch (IllegalAccessException illegalAccessException) {
        throw new RuntimeException("Cannot create an instance of " + param1Class, illegalAccessException);
      } 
    }
  }
  
  static class OnRequeryFactory {
    void onRequery(ViewModel param1ViewModel) {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ViewModelProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */