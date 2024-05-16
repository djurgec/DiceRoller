package androidx.core.telephony;

import android.os.Build;
import android.telephony.SubscriptionManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubscriptionManagerCompat {
  private static Method sGetSlotIndexMethod;
  
  public static int getSlotIndex(int paramInt) {
    if (paramInt == -1)
      return -1; 
    if (Build.VERSION.SDK_INT >= 29)
      return Api29Impl.getSlotIndex(paramInt); 
    try {
      if (sGetSlotIndexMethod == null) {
        if (Build.VERSION.SDK_INT >= 26) {
          sGetSlotIndexMethod = SubscriptionManager.class.getDeclaredMethod("getSlotIndex", new Class[] { int.class });
        } else {
          sGetSlotIndexMethod = SubscriptionManager.class.getDeclaredMethod("getSlotId", new Class[] { int.class });
        } 
        sGetSlotIndexMethod.setAccessible(true);
      } 
      Integer integer = (Integer)sGetSlotIndexMethod.invoke((Object)null, new Object[] { Integer.valueOf(paramInt) });
      if (integer != null)
        return integer.intValue(); 
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    return -1;
  }
  
  private static class Api29Impl {
    static int getSlotIndex(int param1Int) {
      return SubscriptionManager.getSlotIndex(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\telephony\SubscriptionManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */