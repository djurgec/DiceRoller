package androidx.tracing;

import android.os.Build;
import android.util.Log;
import java.lang.reflect.Method;

public final class Trace {
  static final String TAG = "Trace";
  
  private static Method sAsyncTraceBeginMethod;
  
  private static Method sAsyncTraceEndMethod;
  
  private static Method sIsTagEnabledMethod;
  
  private static Method sTraceCounterMethod;
  
  private static long sTraceTagApp;
  
  public static void beginAsyncSection(String paramString, int paramInt) {
    try {
      if (sAsyncTraceBeginMethod == null) {
        TraceApi29Impl.beginAsyncSection(paramString, paramInt);
        return;
      } 
    } catch (NoSuchMethodError|NoClassDefFoundError noSuchMethodError) {}
    beginAsyncSectionFallback(paramString, paramInt);
  }
  
  private static void beginAsyncSectionFallback(String paramString, int paramInt) {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        if (sAsyncTraceBeginMethod == null)
          sAsyncTraceBeginMethod = android.os.Trace.class.getMethod("asyncTraceBegin", new Class[] { long.class, String.class, int.class }); 
        sAsyncTraceBeginMethod.invoke(null, new Object[] { Long.valueOf(sTraceTagApp), paramString, Integer.valueOf(paramInt) });
      } catch (Exception exception) {
        handleException("asyncTraceBegin", exception);
      }  
  }
  
  public static void beginSection(String paramString) {
    if (Build.VERSION.SDK_INT >= 18)
      TraceApi18Impl.beginSection(paramString); 
  }
  
  public static void endAsyncSection(String paramString, int paramInt) {
    try {
      if (sAsyncTraceEndMethod == null) {
        TraceApi29Impl.endAsyncSection(paramString, paramInt);
        return;
      } 
    } catch (NoSuchMethodError|NoClassDefFoundError noSuchMethodError) {}
    endAsyncSectionFallback(paramString, paramInt);
  }
  
  private static void endAsyncSectionFallback(String paramString, int paramInt) {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        if (sAsyncTraceEndMethod == null)
          sAsyncTraceEndMethod = android.os.Trace.class.getMethod("asyncTraceEnd", new Class[] { long.class, String.class, int.class }); 
        sAsyncTraceEndMethod.invoke(null, new Object[] { Long.valueOf(sTraceTagApp), paramString, Integer.valueOf(paramInt) });
      } catch (Exception exception) {
        handleException("asyncTraceEnd", exception);
      }  
  }
  
  public static void endSection() {
    if (Build.VERSION.SDK_INT >= 18)
      TraceApi18Impl.endSection(); 
  }
  
  private static void handleException(String paramString, Exception paramException) {
    Throwable throwable;
    if (paramException instanceof java.lang.reflect.InvocationTargetException) {
      throwable = paramException.getCause();
      if (throwable instanceof RuntimeException)
        throw (RuntimeException)throwable; 
      throw new RuntimeException(throwable);
    } 
    Log.v("Trace", "Unable to call " + throwable + " via reflection", paramException);
  }
  
  public static boolean isEnabled() {
    try {
      if (sIsTagEnabledMethod == null)
        return android.os.Trace.isEnabled(); 
    } catch (NoSuchMethodError|NoClassDefFoundError noSuchMethodError) {}
    return isEnabledFallback();
  }
  
  private static boolean isEnabledFallback() {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        if (sIsTagEnabledMethod == null) {
          sTraceTagApp = android.os.Trace.class.getField("TRACE_TAG_APP").getLong(null);
          sIsTagEnabledMethod = android.os.Trace.class.getMethod("isTagEnabled", new Class[] { long.class });
        } 
        return ((Boolean)sIsTagEnabledMethod.invoke(null, new Object[] { Long.valueOf(sTraceTagApp) })).booleanValue();
      } catch (Exception exception) {
        handleException("isTagEnabled", exception);
      }  
    return false;
  }
  
  public static void setCounter(String paramString, int paramInt) {
    try {
      if (sTraceCounterMethod == null) {
        TraceApi29Impl.setCounter(paramString, paramInt);
        return;
      } 
    } catch (NoSuchMethodError|NoClassDefFoundError noSuchMethodError) {}
    setCounterFallback(paramString, paramInt);
  }
  
  private static void setCounterFallback(String paramString, int paramInt) {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        if (sTraceCounterMethod == null)
          sTraceCounterMethod = android.os.Trace.class.getMethod("traceCounter", new Class[] { long.class, String.class, int.class }); 
        sTraceCounterMethod.invoke(null, new Object[] { Long.valueOf(sTraceTagApp), paramString, Integer.valueOf(paramInt) });
      } catch (Exception exception) {
        handleException("traceCounter", exception);
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\tracing\Trace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */