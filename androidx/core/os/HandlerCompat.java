package androidx.core.os;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;

public final class HandlerCompat {
  private static final String TAG = "HandlerCompat";
  
  public static Handler createAsync(Looper paramLooper) {
    Throwable throwable;
    if (Build.VERSION.SDK_INT >= 28)
      return Api28Impl.createAsync(paramLooper); 
    if (Build.VERSION.SDK_INT >= 17) {
      try {
        return Handler.class.getDeclaredConstructor(new Class[] { Looper.class, Handler.Callback.class, boolean.class }).newInstance(new Object[] { paramLooper, null, Boolean.valueOf(true) });
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (NoSuchMethodException noSuchMethodException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throwable = invocationTargetException.getCause();
        if (!(throwable instanceof RuntimeException)) {
          if (throwable instanceof Error)
            throw (Error)throwable; 
          throw new RuntimeException(throwable);
        } 
        throw (RuntimeException)throwable;
      } 
      Log.w("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor", noSuchMethodException);
    } 
    return new Handler((Looper)throwable);
  }
  
  public static Handler createAsync(Looper paramLooper, Handler.Callback paramCallback) {
    Throwable throwable;
    if (Build.VERSION.SDK_INT >= 28)
      return Api28Impl.createAsync(paramLooper, paramCallback); 
    if (Build.VERSION.SDK_INT >= 17) {
      try {
        return Handler.class.getDeclaredConstructor(new Class[] { Looper.class, Handler.Callback.class, boolean.class }).newInstance(new Object[] { paramLooper, paramCallback, Boolean.valueOf(true) });
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (NoSuchMethodException noSuchMethodException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throwable = invocationTargetException.getCause();
        if (!(throwable instanceof RuntimeException)) {
          if (throwable instanceof Error)
            throw (Error)throwable; 
          throw new RuntimeException(throwable);
        } 
        throw (RuntimeException)throwable;
      } 
      Log.w("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor", noSuchMethodException);
    } 
    return new Handler((Looper)throwable, paramCallback);
  }
  
  public static boolean hasCallbacks(Handler paramHandler, Runnable paramRunnable) {
    nullPointerException = null;
    if (Build.VERSION.SDK_INT >= 29)
      return Api29Impl.hasCallbacks(paramHandler, paramRunnable); 
    if (Build.VERSION.SDK_INT >= 16)
      try {
        return ((Boolean)Handler.class.getMethod("hasCallbacks", new Class[] { Runnable.class }).invoke(paramHandler, new Object[] { paramRunnable })).booleanValue();
      } catch (InvocationTargetException invocationTargetException) {
        Throwable throwable = invocationTargetException.getCause();
        if (!(throwable instanceof RuntimeException)) {
          if (throwable instanceof Error)
            throw (Error)throwable; 
          throw new RuntimeException(throwable);
        } 
        throw (RuntimeException)throwable;
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (NoSuchMethodException noSuchMethodException) {
      
      } catch (NullPointerException nullPointerException) {} 
    throw new UnsupportedOperationException("Failed to call Handler.hasCallbacks(), but there is no safe failure mode for this method. Raising exception.", nullPointerException);
  }
  
  public static boolean postDelayed(Handler paramHandler, Runnable paramRunnable, Object paramObject, long paramLong) {
    if (Build.VERSION.SDK_INT >= 28)
      return Api28Impl.postDelayed(paramHandler, paramRunnable, paramObject, paramLong); 
    Message message = Message.obtain(paramHandler, paramRunnable);
    message.obj = paramObject;
    return paramHandler.sendMessageDelayed(message, paramLong);
  }
  
  private static class Api28Impl {
    public static Handler createAsync(Looper param1Looper) {
      return Handler.createAsync(param1Looper);
    }
    
    public static Handler createAsync(Looper param1Looper, Handler.Callback param1Callback) {
      return Handler.createAsync(param1Looper, param1Callback);
    }
    
    public static boolean postDelayed(Handler param1Handler, Runnable param1Runnable, Object param1Object, long param1Long) {
      return param1Handler.postDelayed(param1Runnable, param1Object, param1Long);
    }
  }
  
  private static class Api29Impl {
    public static boolean hasCallbacks(Handler param1Handler, Runnable param1Runnable) {
      return param1Handler.hasCallbacks(param1Runnable);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\HandlerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */