package androidx.core.telephony;

import android.os.Build;
import android.telephony.TelephonyManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TelephonyManagerCompat {
  private static Method sGetDeviceIdMethod;
  
  private static Method sGetSubIdMethod;
  
  public static String getImei(TelephonyManager paramTelephonyManager) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.getImei(paramTelephonyManager); 
    if (Build.VERSION.SDK_INT >= 22) {
      int i = getSubscriptionId(paramTelephonyManager);
      if (i != Integer.MAX_VALUE && i != -1) {
        i = SubscriptionManagerCompat.getSlotIndex(i);
        if (Build.VERSION.SDK_INT >= 23)
          return Api23Impl.getDeviceId(paramTelephonyManager, i); 
        try {
          if (sGetDeviceIdMethod == null) {
            Method method = TelephonyManager.class.getDeclaredMethod("getDeviceId", new Class[] { int.class });
            sGetDeviceIdMethod = method;
            method.setAccessible(true);
          } 
          return (String)sGetDeviceIdMethod.invoke(paramTelephonyManager, new Object[] { Integer.valueOf(i) });
        } catch (NoSuchMethodException noSuchMethodException) {
        
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {}
        return null;
      } 
    } 
    return invocationTargetException.getDeviceId();
  }
  
  public static int getSubscriptionId(TelephonyManager paramTelephonyManager) {
    if (Build.VERSION.SDK_INT >= 30)
      return Api30Impl.getSubscriptionId(paramTelephonyManager); 
    if (Build.VERSION.SDK_INT >= 22)
      try {
        if (sGetSubIdMethod == null) {
          Method method = TelephonyManager.class.getDeclaredMethod("getSubId", new Class[0]);
          sGetSubIdMethod = method;
          method.setAccessible(true);
        } 
        Integer integer = (Integer)sGetSubIdMethod.invoke(paramTelephonyManager, new Object[0]);
        if (integer != null && integer.intValue() != -1)
          return integer.intValue(); 
      } catch (InvocationTargetException invocationTargetException) {
      
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (NoSuchMethodException noSuchMethodException) {} 
    return Integer.MAX_VALUE;
  }
  
  private static class Api23Impl {
    static String getDeviceId(TelephonyManager param1TelephonyManager, int param1Int) {
      return param1TelephonyManager.getDeviceId(param1Int);
    }
  }
  
  private static class Api26Impl {
    static String getImei(TelephonyManager param1TelephonyManager) {
      return param1TelephonyManager.getImei();
    }
  }
  
  private static class Api30Impl {
    static int getSubscriptionId(TelephonyManager param1TelephonyManager) {
      return param1TelephonyManager.getSubscriptionId();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\telephony\TelephonyManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */