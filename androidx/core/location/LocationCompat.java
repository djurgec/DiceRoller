package androidx.core.location;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public final class LocationCompat {
  public static final String EXTRA_BEARING_ACCURACY = "bearingAccuracy";
  
  public static final String EXTRA_IS_MOCK = "mockLocation";
  
  public static final String EXTRA_SPEED_ACCURACY = "speedAccuracy";
  
  public static final String EXTRA_VERTICAL_ACCURACY = "verticalAccuracy";
  
  private static Method sSetIsFromMockProviderMethod;
  
  public static float getBearingAccuracyDegrees(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.getBearingAccuracyDegrees(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? 0.0F : bundle.getFloat("bearingAccuracy", 0.0F);
  }
  
  public static long getElapsedRealtimeMillis(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 17)
      return TimeUnit.NANOSECONDS.toMillis(Api17Impl.getElapsedRealtimeNanos(paramLocation)); 
    long l1 = System.currentTimeMillis() - paramLocation.getTime();
    long l2 = SystemClock.elapsedRealtime();
    return (l1 < 0L) ? l2 : ((l1 > l2) ? 0L : (l2 - l1));
  }
  
  public static long getElapsedRealtimeNanos(Location paramLocation) {
    return (Build.VERSION.SDK_INT >= 17) ? Api17Impl.getElapsedRealtimeNanos(paramLocation) : TimeUnit.MILLISECONDS.toNanos(getElapsedRealtimeMillis(paramLocation));
  }
  
  private static Method getSetIsFromMockProviderMethod() throws NoSuchMethodException {
    if (sSetIsFromMockProviderMethod == null) {
      Method method = Location.class.getDeclaredMethod("setIsFromMockProvider", new Class[] { boolean.class });
      sSetIsFromMockProviderMethod = method;
      method.setAccessible(true);
    } 
    return sSetIsFromMockProviderMethod;
  }
  
  public static float getSpeedAccuracyMetersPerSecond(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.getSpeedAccuracyMetersPerSecond(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? 0.0F : bundle.getFloat("speedAccuracy", 0.0F);
  }
  
  public static float getVerticalAccuracyMeters(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.getVerticalAccuracyMeters(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? 0.0F : bundle.getFloat("verticalAccuracy", 0.0F);
  }
  
  public static boolean hasBearingAccuracy(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.hasBearingAccuracy(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? false : bundle.containsKey("bearingAccuracy");
  }
  
  public static boolean hasSpeedAccuracy(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.hasSpeedAccuracy(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? false : bundle.containsKey("speedAccuracy");
  }
  
  public static boolean hasVerticalAccuracy(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 26)
      return Api26Impl.hasVerticalAccuracy(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? false : bundle.containsKey("verticalAccuracy");
  }
  
  public static boolean isMock(Location paramLocation) {
    if (Build.VERSION.SDK_INT >= 18)
      return Api18Impl.isMock(paramLocation); 
    Bundle bundle = paramLocation.getExtras();
    return (bundle == null) ? false : bundle.getBoolean("mockLocation", false);
  }
  
  public static void setBearingAccuracyDegrees(Location paramLocation, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 26) {
      Api26Impl.setBearingAccuracyDegrees(paramLocation, paramFloat);
    } else {
      Bundle bundle2 = paramLocation.getExtras();
      Bundle bundle1 = bundle2;
      if (bundle2 == null) {
        paramLocation.setExtras(new Bundle());
        bundle1 = paramLocation.getExtras();
      } 
      bundle1.putFloat("bearingAccuracy", paramFloat);
    } 
  }
  
  public static void setMock(Location paramLocation, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 18) {
      try {
        getSetIsFromMockProviderMethod().invoke(paramLocation, new Object[] { Boolean.valueOf(paramBoolean) });
      } catch (NoSuchMethodException noSuchMethodException) {
        NoSuchMethodError noSuchMethodError = new NoSuchMethodError();
        noSuchMethodError.initCause(noSuchMethodException);
        throw noSuchMethodError;
      } catch (IllegalAccessException illegalAccessException) {
        IllegalAccessError illegalAccessError = new IllegalAccessError();
        illegalAccessError.initCause(illegalAccessException);
        throw illegalAccessError;
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException(invocationTargetException);
      } 
    } else {
      Bundle bundle = invocationTargetException.getExtras();
      if (bundle == null) {
        if (paramBoolean) {
          bundle = new Bundle();
          bundle.putBoolean("mockLocation", true);
          invocationTargetException.setExtras(bundle);
        } 
      } else if (paramBoolean) {
        bundle.putBoolean("mockLocation", true);
      } else {
        bundle.remove("mockLocation");
        if (bundle.isEmpty())
          invocationTargetException.setExtras(null); 
      } 
    } 
  }
  
  public static void setSpeedAccuracyMetersPerSecond(Location paramLocation, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 26) {
      Api26Impl.setSpeedAccuracyMetersPerSecond(paramLocation, paramFloat);
    } else {
      Bundle bundle2 = paramLocation.getExtras();
      Bundle bundle1 = bundle2;
      if (bundle2 == null) {
        paramLocation.setExtras(new Bundle());
        bundle1 = paramLocation.getExtras();
      } 
      bundle1.putFloat("speedAccuracy", paramFloat);
    } 
  }
  
  public static void setVerticalAccuracyMeters(Location paramLocation, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 26) {
      Api26Impl.setVerticalAccuracyMeters(paramLocation, paramFloat);
    } else {
      Bundle bundle2 = paramLocation.getExtras();
      Bundle bundle1 = bundle2;
      if (bundle2 == null) {
        paramLocation.setExtras(new Bundle());
        bundle1 = paramLocation.getExtras();
      } 
      bundle1.putFloat("verticalAccuracy", paramFloat);
    } 
  }
  
  private static class Api17Impl {
    static long getElapsedRealtimeNanos(Location param1Location) {
      return param1Location.getElapsedRealtimeNanos();
    }
  }
  
  private static class Api18Impl {
    static boolean isMock(Location param1Location) {
      return param1Location.isFromMockProvider();
    }
  }
  
  private static class Api26Impl {
    static float getBearingAccuracyDegrees(Location param1Location) {
      return param1Location.getBearingAccuracyDegrees();
    }
    
    static float getSpeedAccuracyMetersPerSecond(Location param1Location) {
      return param1Location.getSpeedAccuracyMetersPerSecond();
    }
    
    static float getVerticalAccuracyMeters(Location param1Location) {
      return param1Location.getVerticalAccuracyMeters();
    }
    
    static boolean hasBearingAccuracy(Location param1Location) {
      return param1Location.hasBearingAccuracy();
    }
    
    static boolean hasSpeedAccuracy(Location param1Location) {
      return param1Location.hasSpeedAccuracy();
    }
    
    static boolean hasVerticalAccuracy(Location param1Location) {
      return param1Location.hasVerticalAccuracy();
    }
    
    static void setBearingAccuracyDegrees(Location param1Location, float param1Float) {
      param1Location.setBearingAccuracyDegrees(param1Float);
    }
    
    static void setSpeedAccuracyMetersPerSecond(Location param1Location, float param1Float) {
      param1Location.setSpeedAccuracyMetersPerSecond(param1Float);
    }
    
    static void setVerticalAccuracyMeters(Location param1Location, float param1Float) {
      param1Location.setVerticalAccuracyMeters(param1Float);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\LocationCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */