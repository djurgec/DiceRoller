package androidx.core.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
  public static IBinder getBinder(Bundle paramBundle, String paramString) {
    return (Build.VERSION.SDK_INT >= 18) ? paramBundle.getBinder(paramString) : BundleCompatBaseImpl.getBinder(paramBundle, paramString);
  }
  
  public static void putBinder(Bundle paramBundle, String paramString, IBinder paramIBinder) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramBundle.putBinder(paramString, paramIBinder);
    } else {
      BundleCompatBaseImpl.putBinder(paramBundle, paramString, paramIBinder);
    } 
  }
  
  static class BundleCompatBaseImpl {
    private static final String TAG = "BundleCompatBaseImpl";
    
    private static Method sGetIBinderMethod;
    
    private static boolean sGetIBinderMethodFetched;
    
    private static Method sPutIBinderMethod;
    
    private static boolean sPutIBinderMethodFetched;
    
    public static IBinder getBinder(Bundle param1Bundle, String param1String) {
      if (!sGetIBinderMethodFetched) {
        try {
          Method method1 = Bundle.class.getMethod("getIBinder", new Class[] { String.class });
          sGetIBinderMethod = method1;
          method1.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", noSuchMethodException);
        } 
        sGetIBinderMethodFetched = true;
      } 
      Method method = sGetIBinderMethod;
      if (method != null)
        try {
          return (IBinder)method.invoke(param1Bundle, new Object[] { param1String });
        } catch (InvocationTargetException|IllegalAccessException|IllegalArgumentException invocationTargetException) {
          Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", invocationTargetException);
          sGetIBinderMethod = null;
        }  
      return null;
    }
    
    public static void putBinder(Bundle param1Bundle, String param1String, IBinder param1IBinder) {
      if (!sPutIBinderMethodFetched) {
        try {
          Method method1 = Bundle.class.getMethod("putIBinder", new Class[] { String.class, IBinder.class });
          sPutIBinderMethod = method1;
          method1.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", noSuchMethodException);
        } 
        sPutIBinderMethodFetched = true;
      } 
      Method method = sPutIBinderMethod;
      if (method != null)
        try {
          method.invoke(param1Bundle, new Object[] { param1String, param1IBinder });
        } catch (InvocationTargetException|IllegalAccessException|IllegalArgumentException invocationTargetException) {
          Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", invocationTargetException);
          sPutIBinderMethod = null;
        }  
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\BundleCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */