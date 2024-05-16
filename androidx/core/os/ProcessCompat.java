package androidx.core.os;

import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import java.lang.reflect.Method;

public final class ProcessCompat {
  public static boolean isApplicationUid(int paramInt) {
    return (Build.VERSION.SDK_INT >= 24) ? Api24Impl.isApplicationUid(paramInt) : ((Build.VERSION.SDK_INT >= 17) ? Api17Impl.isApplicationUid(paramInt) : ((Build.VERSION.SDK_INT == 16) ? Api16Impl.isApplicationUid(paramInt) : true));
  }
  
  static class Api16Impl {
    private static Method sMethodUserIdIsAppMethod;
    
    private static boolean sResolved;
    
    private static final Object sResolvedLock = new Object();
    
    static boolean isApplicationUid(int param1Int) {
      try {
        synchronized (sResolvedLock) {
          if (!sResolved) {
            sResolved = true;
            sMethodUserIdIsAppMethod = Class.forName("android.os.UserId").getDeclaredMethod("isApp", new Class[] { int.class });
          } 
          null = sMethodUserIdIsAppMethod;
          if (null != null) {
            null = null.invoke(null, new Object[] { Integer.valueOf(param1Int) });
            if (null != null)
              return null.booleanValue(); 
            null = new NullPointerException();
            super();
            throw null;
          } 
        } 
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
      return true;
    }
  }
  
  static class Api17Impl {
    private static Method sMethodUserHandleIsAppMethod;
    
    private static boolean sResolved;
    
    private static final Object sResolvedLock = new Object();
    
    static boolean isApplicationUid(int param1Int) {
      try {
        synchronized (sResolvedLock) {
          if (!sResolved) {
            sResolved = true;
            sMethodUserHandleIsAppMethod = UserHandle.class.getDeclaredMethod("isApp", new Class[] { int.class });
          } 
          Method method = sMethodUserHandleIsAppMethod;
          if (method != null && (Boolean)method.invoke(null, new Object[] { Integer.valueOf(param1Int) }) == null) {
            NullPointerException nullPointerException = new NullPointerException();
            this();
            throw nullPointerException;
          } 
        } 
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
      return true;
    }
  }
  
  static class Api24Impl {
    static boolean isApplicationUid(int param1Int) {
      return Process.isApplicationUid(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\ProcessCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */