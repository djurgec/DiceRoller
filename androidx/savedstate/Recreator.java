package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class Recreator implements GenericLifecycleObserver {
  static final String CLASSES_KEY = "classes_to_restore";
  
  static final String COMPONENT_KEY = "androidx.savedstate.Restarter";
  
  private final SavedStateRegistryOwner mOwner;
  
  Recreator(SavedStateRegistryOwner paramSavedStateRegistryOwner) {
    this.mOwner = paramSavedStateRegistryOwner;
  }
  
  private void reflectiveNew(String paramString) {
    try {
      Class<? extends SavedStateRegistry.AutoRecreated> clazz = Class.forName(paramString, false, Recreator.class.getClassLoader()).asSubclass(SavedStateRegistry.AutoRecreated.class);
      try {
        Constructor<? extends SavedStateRegistry.AutoRecreated> constructor = clazz.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        try {
          SavedStateRegistry.AutoRecreated autoRecreated = constructor.newInstance(new Object[0]);
          autoRecreated.onRecreated(this.mOwner);
          return;
        } catch (Exception exception) {
          throw new RuntimeException("Failed to instantiate " + paramString, exception);
        } 
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new IllegalStateException("Class" + exception.getSimpleName() + " must have default constructor in order to be automatically recreated", noSuchMethodException);
      } 
    } catch (ClassNotFoundException classNotFoundException) {
      throw new RuntimeException("Class " + noSuchMethodException + " wasn't found", classNotFoundException);
    } 
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent) {
    if (paramEvent == Lifecycle.Event.ON_CREATE) {
      paramLifecycleOwner.getLifecycle().removeObserver((LifecycleObserver)this);
      Bundle bundle = this.mOwner.getSavedStateRegistry().consumeRestoredStateForKey("androidx.savedstate.Restarter");
      if (bundle == null)
        return; 
      ArrayList arrayList = bundle.getStringArrayList("classes_to_restore");
      if (arrayList != null) {
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext())
          reflectiveNew(iterator.next()); 
        return;
      } 
      throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
    } 
    throw new AssertionError("Next event must be ON_CREATE");
  }
  
  static final class SavedStateProvider implements SavedStateRegistry.SavedStateProvider {
    final Set<String> mClasses = new HashSet<>();
    
    SavedStateProvider(SavedStateRegistry param1SavedStateRegistry) {
      param1SavedStateRegistry.registerSavedStateProvider("androidx.savedstate.Restarter", this);
    }
    
    void add(String param1String) {
      this.mClasses.add(param1String);
    }
    
    public Bundle saveState() {
      Bundle bundle = new Bundle();
      bundle.putStringArrayList("classes_to_restore", new ArrayList<>(this.mClasses));
      return bundle;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\savedstate\Recreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */