package androidx.fragment.app;

import androidx.collection.SimpleArrayMap;
import java.lang.reflect.InvocationTargetException;

public class FragmentFactory {
  private static final SimpleArrayMap<ClassLoader, SimpleArrayMap<String, Class<?>>> sClassCacheMap = new SimpleArrayMap();
  
  static boolean isFragmentClass(ClassLoader paramClassLoader, String paramString) {
    try {
      return Fragment.class.isAssignableFrom(loadClass(paramClassLoader, paramString));
    } catch (ClassNotFoundException classNotFoundException) {
      return false;
    } 
  }
  
  private static Class<?> loadClass(ClassLoader paramClassLoader, String paramString) throws ClassNotFoundException {
    SimpleArrayMap<ClassLoader, SimpleArrayMap<String, Class<?>>> simpleArrayMap = sClassCacheMap;
    SimpleArrayMap simpleArrayMap2 = (SimpleArrayMap)simpleArrayMap.get(paramClassLoader);
    SimpleArrayMap simpleArrayMap1 = simpleArrayMap2;
    if (simpleArrayMap2 == null) {
      simpleArrayMap1 = new SimpleArrayMap();
      simpleArrayMap.put(paramClassLoader, simpleArrayMap1);
    } 
    Class<?> clazz2 = (Class)simpleArrayMap1.get(paramString);
    Class<?> clazz1 = clazz2;
    if (clazz2 == null) {
      clazz1 = Class.forName(paramString, false, paramClassLoader);
      simpleArrayMap1.put(paramString, clazz1);
    } 
    return clazz1;
  }
  
  public static Class<? extends Fragment> loadFragmentClass(ClassLoader paramClassLoader, String paramString) {
    try {
      return (Class)loadClass(paramClassLoader, paramString);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists", classNotFoundException);
    } catch (ClassCastException classCastException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class is a valid subclass of Fragment", classCastException);
    } 
  }
  
  public Fragment instantiate(ClassLoader paramClassLoader, String paramString) {
    try {
      return loadFragmentClass(paramClassLoader, paramString).getConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InstantiationException instantiationException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an empty constructor that is public", instantiationException);
    } catch (IllegalAccessException illegalAccessException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an empty constructor that is public", illegalAccessException);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": could not find Fragment constructor", noSuchMethodException);
    } catch (InvocationTargetException invocationTargetException) {
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + paramString + ": calling Fragment constructor caused an exception", invocationTargetException);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */