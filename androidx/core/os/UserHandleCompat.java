package androidx.core.os;

import android.os.Build;
import android.os.UserHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserHandleCompat {
  private static Method sGetUserIdMethod;
  
  private static Constructor<UserHandle> sUserHandleConstructor;
  
  private static Method getGetUserIdMethod() throws NoSuchMethodException {
    if (sGetUserIdMethod == null) {
      Method method = UserHandle.class.getDeclaredMethod("getUserId", new Class[] { int.class });
      sGetUserIdMethod = method;
      method.setAccessible(true);
    } 
    return sGetUserIdMethod;
  }
  
  private static Constructor<UserHandle> getUserHandleConstructor() throws NoSuchMethodException {
    if (sUserHandleConstructor == null) {
      Constructor<UserHandle> constructor = UserHandle.class.getDeclaredConstructor(new Class[] { int.class });
      sUserHandleConstructor = constructor;
      constructor.setAccessible(true);
    } 
    return sUserHandleConstructor;
  }
  
  public static UserHandle getUserHandleForUid(int paramInt) {
    if (Build.VERSION.SDK_INT >= 24)
      return Api24Impl.getUserHandleForUid(paramInt); 
    try {
      Integer integer = (Integer)getGetUserIdMethod().invoke((Object)null, new Object[] { Integer.valueOf(paramInt) });
      return getUserHandleConstructor().newInstance(new Object[] { integer });
    } catch (NoSuchMethodException noSuchMethodException) {
      NoSuchMethodError noSuchMethodError = new NoSuchMethodError();
      noSuchMethodError.initCause(noSuchMethodException);
      throw noSuchMethodError;
    } catch (IllegalAccessException illegalAccessException) {
      IllegalAccessError illegalAccessError = new IllegalAccessError();
      illegalAccessError.initCause(illegalAccessException);
      throw illegalAccessError;
    } catch (InstantiationException instantiationException) {
      InstantiationError instantiationError = new InstantiationError();
      instantiationError.initCause(instantiationException);
      throw instantiationError;
    } catch (InvocationTargetException invocationTargetException) {
      throw new RuntimeException(invocationTargetException);
    } 
  }
  
  private static class Api24Impl {
    static UserHandle getUserHandleForUid(int param1Int) {
      return UserHandle.getUserHandleForUid(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\UserHandleCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */