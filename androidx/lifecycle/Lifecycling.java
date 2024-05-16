package androidx.lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lifecycling {
  private static final int GENERATED_CALLBACK = 2;
  
  private static final int REFLECTIVE_CALLBACK = 1;
  
  private static Map<Class<?>, Integer> sCallbackCache = new HashMap<>();
  
  private static Map<Class<?>, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters = new HashMap<>();
  
  private static GeneratedAdapter createGeneratedAdapter(Constructor<? extends GeneratedAdapter> paramConstructor, Object paramObject) {
    try {
      return paramConstructor.newInstance(new Object[] { paramObject });
    } catch (IllegalAccessException illegalAccessException) {
      throw new RuntimeException(illegalAccessException);
    } catch (InstantiationException instantiationException) {
      throw new RuntimeException(instantiationException);
    } catch (InvocationTargetException invocationTargetException) {
      throw new RuntimeException(invocationTargetException);
    } 
  }
  
  private static Constructor<? extends GeneratedAdapter> generatedConstructor(Class<?> paramClass) {
    try {
      String str1;
      Package package_ = paramClass.getPackage();
      String str2 = paramClass.getCanonicalName();
      if (package_ != null) {
        str1 = package_.getName();
      } else {
        str1 = "";
      } 
      if (!str1.isEmpty())
        str2 = str2.substring(str1.length() + 1); 
      str2 = getAdapterName(str2);
      if (str1.isEmpty()) {
        str1 = str2;
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        str1 = stringBuilder.append(str1).append(".").append(str2).toString();
      } 
      Constructor<?> constructor = Class.forName(str1).getDeclaredConstructor(new Class[] { paramClass });
      if (!constructor.isAccessible())
        constructor.setAccessible(true); 
      return (Constructor)constructor;
    } catch (ClassNotFoundException classNotFoundException) {
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new RuntimeException(noSuchMethodException);
    } 
  }
  
  public static String getAdapterName(String paramString) {
    return paramString.replace(".", "_") + "_LifecycleAdapter";
  }
  
  @Deprecated
  static GenericLifecycleObserver getCallback(Object paramObject) {
    return new GenericLifecycleObserver(lifecycleEventObserver(paramObject)) {
        final LifecycleEventObserver val$observer;
        
        public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
          observer.onStateChanged(param1LifecycleOwner, param1Event);
        }
      };
  }
  
  private static int getObserverConstructorType(Class<?> paramClass) {
    Integer integer = sCallbackCache.get(paramClass);
    if (integer != null)
      return integer.intValue(); 
    int i = resolveObserverCallbackType(paramClass);
    sCallbackCache.put(paramClass, Integer.valueOf(i));
    return i;
  }
  
  private static boolean isLifecycleParent(Class<?> paramClass) {
    boolean bool;
    if (paramClass != null && LifecycleObserver.class.isAssignableFrom(paramClass)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static LifecycleEventObserver lifecycleEventObserver(Object paramObject) {
    boolean bool1 = paramObject instanceof LifecycleEventObserver;
    boolean bool2 = paramObject instanceof FullLifecycleObserver;
    if (bool1 && bool2)
      return new FullLifecycleObserverAdapter((FullLifecycleObserver)paramObject, (LifecycleEventObserver)paramObject); 
    if (bool2)
      return new FullLifecycleObserverAdapter((FullLifecycleObserver)paramObject, null); 
    if (bool1)
      return (LifecycleEventObserver)paramObject; 
    Class<?> clazz = paramObject.getClass();
    if (getObserverConstructorType(clazz) == 2) {
      List<Constructor<? extends GeneratedAdapter>> list = sClassToAdapters.get(clazz);
      if (list.size() == 1)
        return new SingleGeneratedAdapterObserver(createGeneratedAdapter(list.get(0), paramObject)); 
      GeneratedAdapter[] arrayOfGeneratedAdapter = new GeneratedAdapter[list.size()];
      for (byte b = 0; b < list.size(); b++)
        arrayOfGeneratedAdapter[b] = createGeneratedAdapter(list.get(b), paramObject); 
      return new CompositeGeneratedAdaptersObserver(arrayOfGeneratedAdapter);
    } 
    return new ReflectiveGenericLifecycleObserver(paramObject);
  }
  
  private static int resolveObserverCallbackType(Class<?> paramClass) {
    ArrayList<Constructor<? extends GeneratedAdapter>> arrayList;
    if (paramClass.getCanonicalName() == null)
      return 1; 
    Constructor<? extends GeneratedAdapter> constructor = generatedConstructor(paramClass);
    if (constructor != null) {
      sClassToAdapters.put(paramClass, Collections.singletonList(constructor));
      return 2;
    } 
    if (ClassesInfoCache.sInstance.hasLifecycleMethods(paramClass))
      return 1; 
    Class<?> clazz = paramClass.getSuperclass();
    constructor = null;
    if (isLifecycleParent(clazz)) {
      if (getObserverConstructorType(clazz) == 1)
        return 1; 
      arrayList = new ArrayList(sClassToAdapters.get(clazz));
    } 
    for (Class<?> clazz1 : paramClass.getInterfaces()) {
      if (isLifecycleParent(clazz1)) {
        if (getObserverConstructorType(clazz1) == 1)
          return 1; 
        ArrayList<Constructor<? extends GeneratedAdapter>> arrayList1 = arrayList;
        if (arrayList == null)
          arrayList1 = new ArrayList(); 
        arrayList1.addAll(sClassToAdapters.get(clazz1));
        arrayList = arrayList1;
      } 
    } 
    if (arrayList != null) {
      sClassToAdapters.put(paramClass, arrayList);
      return 2;
    } 
    return 1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\Lifecycling.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */