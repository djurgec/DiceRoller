package androidx.core.location;

import android.content.Context;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.collection.SimpleArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.Consumer;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class LocationManagerCompat {
  private static final long GET_CURRENT_LOCATION_TIMEOUT_MS = 30000L;
  
  private static final long MAX_CURRENT_LOCATION_AGE_MS = 10000L;
  
  private static final long PRE_N_LOOPER_TIMEOUT_S = 5L;
  
  private static Field sContextField;
  
  static final WeakHashMap<LocationListener, List<WeakReference<LocationListenerTransport>>> sLocationListeners = new WeakHashMap<>();
  
  private static Method sRequestLocationUpdatesExecutorMethod;
  
  private static Method sRequestLocationUpdatesLooperMethod;
  
  public static void getCurrentLocation(LocationManager paramLocationManager, String paramString, CancellationSignal paramCancellationSignal, Executor paramExecutor, Consumer<Location> paramConsumer) {
    if (Build.VERSION.SDK_INT >= 30) {
      Api30Impl.getCurrentLocation(paramLocationManager, paramString, paramCancellationSignal, paramExecutor, paramConsumer);
      return;
    } 
    if (paramCancellationSignal != null)
      paramCancellationSignal.throwIfCanceled(); 
    Location location = paramLocationManager.getLastKnownLocation(paramString);
    if (location != null && SystemClock.elapsedRealtime() - LocationCompat.getElapsedRealtimeMillis(location) < 10000L) {
      paramExecutor.execute(new LocationManagerCompat$$ExternalSyntheticLambda0(paramConsumer, location));
      return;
    } 
    final CancellableLocationListener listener = new CancellableLocationListener(paramLocationManager, paramExecutor, paramConsumer);
    paramLocationManager.requestLocationUpdates(paramString, 0L, 0.0F, cancellableLocationListener, Looper.getMainLooper());
    if (paramCancellationSignal != null)
      paramCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            final LocationManagerCompat.CancellableLocationListener val$listener;
            
            public void onCancel() {
              listener.cancel();
            }
          }); 
    cancellableLocationListener.startTimeout(30000L);
  }
  
  public static String getGnssHardwareModelName(LocationManager paramLocationManager) {
    return (Build.VERSION.SDK_INT >= 28) ? Api28Impl.getGnssHardwareModelName(paramLocationManager) : null;
  }
  
  public static int getGnssYearOfHardware(LocationManager paramLocationManager) {
    return (Build.VERSION.SDK_INT >= 28) ? Api28Impl.getGnssYearOfHardware(paramLocationManager) : 0;
  }
  
  public static boolean hasProvider(LocationManager paramLocationManager, String paramString) {
    if (Build.VERSION.SDK_INT >= 31)
      return Api31Impl.hasProvider(paramLocationManager, paramString); 
    boolean bool1 = paramLocationManager.getAllProviders().contains(paramString);
    boolean bool = true;
    if (bool1)
      return true; 
    try {
      LocationProvider locationProvider = paramLocationManager.getProvider(paramString);
      if (locationProvider == null)
        bool = false; 
      return bool;
    } catch (SecurityException securityException) {
      return false;
    } 
  }
  
  public static boolean isLocationEnabled(LocationManager paramLocationManager) {
    if (Build.VERSION.SDK_INT >= 28)
      return Api28Impl.isLocationEnabled(paramLocationManager); 
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    null = false;
    if (i <= 19)
      try {
        if (sContextField == null) {
          Field field = LocationManager.class.getDeclaredField("mContext");
          sContextField = field;
          field.setAccessible(true);
        } 
        Context context = (Context)sContextField.get(paramLocationManager);
        if (context != null) {
          if (Build.VERSION.SDK_INT == 19) {
            if (Settings.Secure.getInt(context.getContentResolver(), "location_mode", 0) != 0)
              null = true; 
            return null;
          } 
          null = TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed"));
          return null ^ true;
        } 
      } catch (ClassCastException|SecurityException|NoSuchFieldException|IllegalAccessException classCastException) {} 
    if (!paramLocationManager.isProviderEnabled("network")) {
      null = bool;
      return paramLocationManager.isProviderEnabled("gps") ? true : null;
    } 
    return true;
  }
  
  private static boolean registerGnssStatusCallback(LocationManager paramLocationManager, Handler paramHandler, Executor paramExecutor, GnssStatusCompat.Callback paramCallback) {
    GnssStatusTransport gnssStatusTransport;
    boolean bool;
    if (Build.VERSION.SDK_INT >= 30)
      synchronized (GnssLazyLoader.sGnssStatusListeners) {
        GnssStatusTransport gnssStatusTransport1 = (GnssStatusTransport)GnssLazyLoader.sGnssStatusListeners.get(paramCallback);
        gnssStatusTransport = gnssStatusTransport1;
        if (gnssStatusTransport1 == null) {
          gnssStatusTransport = new GnssStatusTransport();
          this(paramCallback);
        } 
        if (paramLocationManager.registerGnssStatusCallback(paramExecutor, gnssStatusTransport)) {
          GnssLazyLoader.sGnssStatusListeners.put(paramCallback, gnssStatusTransport);
          return true;
        } 
        return false;
      }  
    if (Build.VERSION.SDK_INT >= 24) {
      if (gnssStatusTransport != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool);
      synchronized (GnssLazyLoader.sGnssStatusListeners) {
        PreRGnssStatusTransport preRGnssStatusTransport = (PreRGnssStatusTransport)GnssLazyLoader.sGnssStatusListeners.get(paramCallback);
        if (preRGnssStatusTransport == null) {
          preRGnssStatusTransport = new PreRGnssStatusTransport();
          this(paramCallback);
        } else {
          preRGnssStatusTransport.unregister();
        } 
        preRGnssStatusTransport.register(paramExecutor);
        if (paramLocationManager.registerGnssStatusCallback(preRGnssStatusTransport, (Handler)gnssStatusTransport)) {
          GnssLazyLoader.sGnssStatusListeners.put(paramCallback, preRGnssStatusTransport);
          return true;
        } 
        return false;
      } 
    } 
    if (gnssStatusTransport != null) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool);
    synchronized (GnssLazyLoader.sGnssStatusListeners) {
      StringBuilder stringBuilder;
      GpsStatusTransport gpsStatusTransport = (GpsStatusTransport)GnssLazyLoader.sGnssStatusListeners.get(paramCallback);
      if (gpsStatusTransport == null) {
        gpsStatusTransport = new GpsStatusTransport();
        this(paramLocationManager, paramCallback);
      } else {
        gpsStatusTransport.unregister();
      } 
      gpsStatusTransport.register(paramExecutor);
      FutureTask futureTask = new FutureTask();
      LocationManagerCompat$$ExternalSyntheticLambda1 locationManagerCompat$$ExternalSyntheticLambda1 = new LocationManagerCompat$$ExternalSyntheticLambda1();
      this(paramLocationManager, gpsStatusTransport);
      this(locationManagerCompat$$ExternalSyntheticLambda1);
      if (Looper.myLooper() == gnssStatusTransport.getLooper()) {
        futureTask.run();
      } else {
        bool = gnssStatusTransport.post(futureTask);
        if (!bool) {
          IllegalStateException illegalStateException = new IllegalStateException();
          stringBuilder = new StringBuilder();
          this();
          this(stringBuilder.append(gnssStatusTransport).append(" is shutting down").toString());
          throw illegalStateException;
        } 
      } 
      boolean bool5 = false;
      boolean bool2 = false;
      boolean bool6 = false;
      boolean bool7 = false;
      boolean bool1 = bool5;
      boolean bool3 = bool2;
      boolean bool4 = bool6;
      try {
        long l2 = TimeUnit.SECONDS.toNanos(5L);
        bool1 = bool5;
        bool3 = bool2;
        bool4 = bool6;
        long l3 = System.nanoTime();
        long l1 = l2;
        bool2 = bool7;
        while (true) {
          bool1 = bool2;
          bool3 = bool2;
          bool4 = bool2;
          try {
            if (((Boolean)stringBuilder.get(l1, TimeUnit.NANOSECONDS)).booleanValue()) {
              bool1 = bool2;
              bool3 = bool2;
              bool4 = bool2;
              GnssLazyLoader.sGnssStatusListeners.put(paramCallback, gpsStatusTransport);
              if (bool2)
                Thread.currentThread().interrupt(); 
              return true;
            } 
            if (bool2)
              Thread.currentThread().interrupt(); 
            return false;
          } catch (InterruptedException interruptedException) {
            bool1 = true;
            bool3 = true;
            bool4 = true;
            bool2 = true;
            l1 = System.nanoTime();
            l1 = l3 + l2 - l1;
          } 
        } 
      } catch (ExecutionException executionException) {
        bool1 = bool4;
        if (!(executionException.getCause() instanceof RuntimeException)) {
          bool1 = bool4;
          if (executionException.getCause() instanceof Error) {
            bool1 = bool4;
            throw (Error)executionException.getCause();
          } 
          bool1 = bool4;
          IllegalStateException illegalStateException = new IllegalStateException();
          bool1 = bool4;
          this(executionException);
          bool1 = bool4;
          throw illegalStateException;
        } 
        bool1 = bool4;
        throw (RuntimeException)executionException.getCause();
      } catch (TimeoutException timeoutException) {
        bool1 = bool3;
        IllegalStateException illegalStateException = new IllegalStateException();
        bool1 = bool3;
        stringBuilder = new StringBuilder();
        bool1 = bool3;
        this();
        bool1 = bool3;
        this(stringBuilder.append(executionException).append(" appears to be blocked, please run registerGnssStatusCallback() directly on a Looper thread or ensure the main Looper is not blocked by this thread").toString(), timeoutException);
        bool1 = bool3;
        throw illegalStateException;
      } finally {}
      if (bool1)
        Thread.currentThread().interrupt(); 
      throw paramLocationManager;
    } 
  }
  
  public static boolean registerGnssStatusCallback(LocationManager paramLocationManager, GnssStatusCompat.Callback paramCallback, Handler paramHandler) {
    return (Build.VERSION.SDK_INT >= 30) ? registerGnssStatusCallback(paramLocationManager, ExecutorCompat.create(paramHandler), paramCallback) : registerGnssStatusCallback(paramLocationManager, new InlineHandlerExecutor(paramHandler), paramCallback);
  }
  
  public static boolean registerGnssStatusCallback(LocationManager paramLocationManager, Executor paramExecutor, GnssStatusCompat.Callback paramCallback) {
    if (Build.VERSION.SDK_INT >= 30)
      return registerGnssStatusCallback(paramLocationManager, null, paramExecutor, paramCallback); 
    Looper looper2 = Looper.myLooper();
    Looper looper1 = looper2;
    if (looper2 == null)
      looper1 = Looper.getMainLooper(); 
    return registerGnssStatusCallback(paramLocationManager, new Handler(looper1), paramExecutor, paramCallback);
  }
  
  public static void removeUpdates(LocationManager paramLocationManager, LocationListenerCompat paramLocationListenerCompat) {
    synchronized (sLocationListeners) {
      List list = null.remove(paramLocationListenerCompat);
      if (list != null) {
        Iterator<WeakReference<LocationListenerTransport>> iterator = list.iterator();
        while (iterator.hasNext()) {
          LocationListenerTransport locationListenerTransport = ((WeakReference<LocationListenerTransport>)iterator.next()).get();
          if (locationListenerTransport != null && locationListenerTransport.unregister())
            paramLocationManager.removeUpdates(locationListenerTransport); 
        } 
      } 
      paramLocationManager.removeUpdates(paramLocationListenerCompat);
      return;
    } 
  }
  
  public static void requestLocationUpdates(LocationManager paramLocationManager, String paramString, LocationRequestCompat paramLocationRequestCompat, LocationListenerCompat paramLocationListenerCompat, Looper paramLooper) {
    if (Build.VERSION.SDK_INT >= 31) {
      Api31Impl.requestLocationUpdates(paramLocationManager, paramString, paramLocationRequestCompat.toLocationRequest(), ExecutorCompat.create(new Handler(paramLooper)), paramLocationListenerCompat);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 19)
      try {
        if (sRequestLocationUpdatesLooperMethod == null) {
          Method method = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[] { LocationRequest.class, LocationListener.class, Looper.class });
          sRequestLocationUpdatesLooperMethod = method;
          method.setAccessible(true);
        } 
        LocationRequest locationRequest = paramLocationRequestCompat.toLocationRequest(paramString);
        if (locationRequest != null) {
          sRequestLocationUpdatesLooperMethod.invoke(paramLocationManager, new Object[] { locationRequest, paramLocationListenerCompat, paramLooper });
          return;
        } 
      } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException noSuchMethodException) {
      
      } catch (UnsupportedOperationException unsupportedOperationException) {} 
    paramLocationManager.requestLocationUpdates(paramString, paramLocationRequestCompat.getIntervalMillis(), paramLocationRequestCompat.getMinUpdateDistanceMeters(), paramLocationListenerCompat, paramLooper);
  }
  
  public static void requestLocationUpdates(LocationManager paramLocationManager, String paramString, LocationRequestCompat paramLocationRequestCompat, Executor paramExecutor, LocationListenerCompat paramLocationListenerCompat) {
    if (Build.VERSION.SDK_INT >= 31) {
      Api31Impl.requestLocationUpdates(paramLocationManager, paramString, paramLocationRequestCompat.toLocationRequest(), paramExecutor, paramLocationListenerCompat);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 30)
      try {
        if (sRequestLocationUpdatesExecutorMethod == null) {
          Method method = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[] { LocationRequest.class, Executor.class, LocationListener.class });
          sRequestLocationUpdatesExecutorMethod = method;
          method.setAccessible(true);
        } 
        LocationRequest locationRequest = paramLocationRequestCompat.toLocationRequest(paramString);
        if (locationRequest != null) {
          sRequestLocationUpdatesExecutorMethod.invoke(paramLocationManager, new Object[] { locationRequest, paramExecutor, paramLocationListenerCompat });
          return;
        } 
      } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException noSuchMethodException) {
      
      } catch (UnsupportedOperationException unsupportedOperationException) {} 
    LocationListenerTransport locationListenerTransport = new LocationListenerTransport(paramLocationListenerCompat, paramExecutor);
    if (Build.VERSION.SDK_INT >= 19)
      try {
        if (sRequestLocationUpdatesLooperMethod == null) {
          Method method = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[] { LocationRequest.class, LocationListener.class, Looper.class });
          sRequestLocationUpdatesLooperMethod = method;
          method.setAccessible(true);
        } 
        LocationRequest locationRequest = paramLocationRequestCompat.toLocationRequest(paramString);
        if (locationRequest != null)
          synchronized (sLocationListeners) {
            sRequestLocationUpdatesLooperMethod.invoke(paramLocationManager, new Object[] { locationRequest, locationListenerTransport, Looper.getMainLooper() });
            locationListenerTransport.register();
            return;
          }  
      } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException noSuchMethodException) {
      
      } catch (UnsupportedOperationException unsupportedOperationException) {} 
    synchronized (sLocationListeners) {
      paramLocationManager.requestLocationUpdates(paramString, paramLocationRequestCompat.getIntervalMillis(), paramLocationRequestCompat.getMinUpdateDistanceMeters(), locationListenerTransport, Looper.getMainLooper());
      locationListenerTransport.register();
      return;
    } 
  }
  
  public static void unregisterGnssStatusCallback(LocationManager paramLocationManager, GnssStatusCompat.Callback paramCallback) {
    GnssStatusTransport gnssStatusTransport;
    if (Build.VERSION.SDK_INT >= 30) {
      synchronized (GnssLazyLoader.sGnssStatusListeners) {
        gnssStatusTransport = (GnssStatusTransport)GnssLazyLoader.sGnssStatusListeners.remove(paramCallback);
        if (gnssStatusTransport != null)
          paramLocationManager.unregisterGnssStatusCallback(gnssStatusTransport); 
      } 
    } else {
      PreRGnssStatusTransport preRGnssStatusTransport;
      if (Build.VERSION.SDK_INT >= 24) {
        synchronized (GnssLazyLoader.sGnssStatusListeners) {
          preRGnssStatusTransport = (PreRGnssStatusTransport)GnssLazyLoader.sGnssStatusListeners.remove(gnssStatusTransport);
          if (preRGnssStatusTransport != null) {
            preRGnssStatusTransport.unregister();
            paramLocationManager.unregisterGnssStatusCallback(preRGnssStatusTransport);
          } 
        } 
      } else {
        synchronized (GnssLazyLoader.sGnssStatusListeners) {
          GpsStatusTransport gpsStatusTransport = (GpsStatusTransport)GnssLazyLoader.sGnssStatusListeners.remove(preRGnssStatusTransport);
          if (gpsStatusTransport != null) {
            gpsStatusTransport.unregister();
            paramLocationManager.removeGpsStatusListener(gpsStatusTransport);
          } 
          return;
        } 
      } 
    } 
  }
  
  private static class Api28Impl {
    static String getGnssHardwareModelName(LocationManager param1LocationManager) {
      return param1LocationManager.getGnssHardwareModelName();
    }
    
    static int getGnssYearOfHardware(LocationManager param1LocationManager) {
      return param1LocationManager.getGnssYearOfHardware();
    }
    
    static boolean isLocationEnabled(LocationManager param1LocationManager) {
      return param1LocationManager.isLocationEnabled();
    }
  }
  
  private static class Api30Impl {
    static void getCurrentLocation(LocationManager param1LocationManager, String param1String, CancellationSignal param1CancellationSignal, Executor param1Executor, Consumer<Location> param1Consumer) {
      if (param1CancellationSignal != null) {
        CancellationSignal cancellationSignal = (CancellationSignal)param1CancellationSignal.getCancellationSignalObject();
      } else {
        param1CancellationSignal = null;
      } 
      Objects.requireNonNull(param1Consumer);
      param1LocationManager.getCurrentLocation(param1String, (CancellationSignal)param1CancellationSignal, param1Executor, new LocationManagerCompat$Api30Impl$$ExternalSyntheticLambda0(param1Consumer));
    }
  }
  
  private static class Api31Impl {
    static boolean hasProvider(LocationManager param1LocationManager, String param1String) {
      return param1LocationManager.hasProvider(param1String);
    }
    
    static void requestLocationUpdates(LocationManager param1LocationManager, String param1String, LocationRequest param1LocationRequest, Executor param1Executor, LocationListener param1LocationListener) {
      param1LocationManager.requestLocationUpdates(param1String, param1LocationRequest, param1Executor, param1LocationListener);
    }
  }
  
  private static final class CancellableLocationListener implements LocationListener {
    private Consumer<Location> mConsumer;
    
    private final Executor mExecutor;
    
    private final LocationManager mLocationManager;
    
    private final Handler mTimeoutHandler;
    
    Runnable mTimeoutRunnable;
    
    private boolean mTriggered;
    
    CancellableLocationListener(LocationManager param1LocationManager, Executor param1Executor, Consumer<Location> param1Consumer) {
      this.mLocationManager = param1LocationManager;
      this.mExecutor = param1Executor;
      this.mTimeoutHandler = new Handler(Looper.getMainLooper());
      this.mConsumer = param1Consumer;
    }
    
    private void cleanup() {
      this.mConsumer = null;
      this.mLocationManager.removeUpdates(this);
      Runnable runnable = this.mTimeoutRunnable;
      if (runnable != null) {
        this.mTimeoutHandler.removeCallbacks(runnable);
        this.mTimeoutRunnable = null;
      } 
    }
    
    public void cancel() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTriggered : Z
      //   6: ifeq -> 12
      //   9: aload_0
      //   10: monitorexit
      //   11: return
      //   12: aload_0
      //   13: iconst_1
      //   14: putfield mTriggered : Z
      //   17: aload_0
      //   18: monitorexit
      //   19: aload_0
      //   20: invokespecial cleanup : ()V
      //   23: return
      //   24: astore_1
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_1
      //   28: athrow
      // Exception table:
      //   from	to	target	type
      //   2	11	24	finally
      //   12	19	24	finally
      //   25	27	24	finally
    }
    
    public void onLocationChanged(Location param1Location) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTriggered : Z
      //   6: ifeq -> 12
      //   9: aload_0
      //   10: monitorexit
      //   11: return
      //   12: aload_0
      //   13: iconst_1
      //   14: putfield mTriggered : Z
      //   17: aload_0
      //   18: monitorexit
      //   19: aload_0
      //   20: getfield mConsumer : Landroidx/core/util/Consumer;
      //   23: astore_2
      //   24: aload_0
      //   25: getfield mExecutor : Ljava/util/concurrent/Executor;
      //   28: new androidx/core/location/LocationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0
      //   31: dup
      //   32: aload_2
      //   33: aload_1
      //   34: invokespecial <init> : (Landroidx/core/util/Consumer;Landroid/location/Location;)V
      //   37: invokeinterface execute : (Ljava/lang/Runnable;)V
      //   42: aload_0
      //   43: invokespecial cleanup : ()V
      //   46: return
      //   47: astore_1
      //   48: aload_0
      //   49: monitorexit
      //   50: aload_1
      //   51: athrow
      // Exception table:
      //   from	to	target	type
      //   2	11	47	finally
      //   12	19	47	finally
      //   48	50	47	finally
    }
    
    public void onProviderDisabled(String param1String) {
      onLocationChanged((Location)null);
    }
    
    public void onProviderEnabled(String param1String) {}
    
    public void onStatusChanged(String param1String, int param1Int, Bundle param1Bundle) {}
    
    public void startTimeout(long param1Long) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTriggered : Z
      //   6: ifeq -> 12
      //   9: aload_0
      //   10: monitorexit
      //   11: return
      //   12: new androidx/core/location/LocationManagerCompat$CancellableLocationListener$1
      //   15: astore_3
      //   16: aload_3
      //   17: aload_0
      //   18: invokespecial <init> : (Landroidx/core/location/LocationManagerCompat$CancellableLocationListener;)V
      //   21: aload_0
      //   22: aload_3
      //   23: putfield mTimeoutRunnable : Ljava/lang/Runnable;
      //   26: aload_0
      //   27: getfield mTimeoutHandler : Landroid/os/Handler;
      //   30: aload_3
      //   31: lload_1
      //   32: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
      //   35: pop
      //   36: aload_0
      //   37: monitorexit
      //   38: return
      //   39: astore_3
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_3
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	11	39	finally
      //   12	38	39	finally
      //   40	42	39	finally
    }
  }
  
  class null implements Runnable {
    final LocationManagerCompat.CancellableLocationListener this$0;
    
    public void run() {
      this.this$0.mTimeoutRunnable = null;
      this.this$0.onLocationChanged((Location)null);
    }
  }
  
  private static class GnssLazyLoader {
    static final SimpleArrayMap<Object, Object> sGnssStatusListeners = new SimpleArrayMap();
  }
  
  private static class GnssStatusTransport extends GnssStatus.Callback {
    final GnssStatusCompat.Callback mCallback;
    
    GnssStatusTransport(GnssStatusCompat.Callback param1Callback) {
      boolean bool;
      if (param1Callback != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "invalid null callback");
      this.mCallback = param1Callback;
    }
    
    public void onFirstFix(int param1Int) {
      this.mCallback.onFirstFix(param1Int);
    }
    
    public void onSatelliteStatusChanged(GnssStatus param1GnssStatus) {
      this.mCallback.onSatelliteStatusChanged(GnssStatusCompat.wrap(param1GnssStatus));
    }
    
    public void onStarted() {
      this.mCallback.onStarted();
    }
    
    public void onStopped() {
      this.mCallback.onStopped();
    }
  }
  
  private static class GpsStatusTransport implements GpsStatus.Listener {
    final GnssStatusCompat.Callback mCallback;
    
    volatile Executor mExecutor;
    
    private final LocationManager mLocationManager;
    
    GpsStatusTransport(LocationManager param1LocationManager, GnssStatusCompat.Callback param1Callback) {
      boolean bool;
      if (param1Callback != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "invalid null callback");
      this.mLocationManager = param1LocationManager;
      this.mCallback = param1Callback;
    }
    
    public void onGpsStatusChanged(int param1Int) {
      GpsStatus gpsStatus;
      Executor executor = this.mExecutor;
      if (executor == null)
        return; 
      switch (param1Int) {
        default:
          return;
        case 4:
          gpsStatus = this.mLocationManager.getGpsStatus(null);
          if (gpsStatus != null)
            executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda3(this, executor, GnssStatusCompat.wrap(gpsStatus))); 
        case 3:
          gpsStatus = this.mLocationManager.getGpsStatus(null);
          if (gpsStatus != null)
            executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda2(this, executor, gpsStatus.getTimeToFirstFix())); 
        case 2:
          executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda1(this, executor));
        case 1:
          break;
      } 
      executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda0(this, executor));
    }
    
    public void register(Executor param1Executor) {
      boolean bool;
      if (this.mExecutor == null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkState(bool);
      this.mExecutor = param1Executor;
    }
    
    public void unregister() {
      this.mExecutor = null;
    }
  }
  
  private static final class InlineHandlerExecutor implements Executor {
    private final Handler mHandler;
    
    InlineHandlerExecutor(Handler param1Handler) {
      this.mHandler = (Handler)Preconditions.checkNotNull(param1Handler);
    }
    
    public void execute(Runnable param1Runnable) {
      if (Looper.myLooper() == this.mHandler.getLooper()) {
        param1Runnable.run();
      } else if (!this.mHandler.post((Runnable)Preconditions.checkNotNull(param1Runnable))) {
        throw new RejectedExecutionException(this.mHandler + " is shutting down");
      } 
    }
  }
  
  private static class LocationListenerTransport implements LocationListener {
    final Executor mExecutor;
    
    volatile LocationListenerCompat mListener;
    
    LocationListenerTransport(LocationListenerCompat param1LocationListenerCompat, Executor param1Executor) {
      this.mListener = (LocationListenerCompat)ObjectsCompat.requireNonNull(param1LocationListenerCompat, "invalid null listener");
      this.mExecutor = param1Executor;
    }
    
    public void onFlushComplete(int param1Int) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda0(this, locationListenerCompat, param1Int));
    }
    
    public void onLocationChanged(Location param1Location) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda1(this, locationListenerCompat, param1Location));
    }
    
    public void onLocationChanged(List<Location> param1List) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda5(this, locationListenerCompat, param1List));
    }
    
    public void onProviderDisabled(String param1String) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda2(this, locationListenerCompat, param1String));
    }
    
    public void onProviderEnabled(String param1String) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda3(this, locationListenerCompat, param1String));
    }
    
    public void onStatusChanged(String param1String, int param1Int, Bundle param1Bundle) {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return; 
      this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda4(this, locationListenerCompat, param1String, param1Int, param1Bundle));
    }
    
    public void register() {
      List list1;
      List list2 = LocationManagerCompat.sLocationListeners.get(this.mListener);
      if (list2 == null) {
        list1 = new ArrayList(1);
        LocationManagerCompat.sLocationListeners.put(this.mListener, list1);
      } else if (Build.VERSION.SDK_INT >= 24) {
        list2.removeIf(LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda6.INSTANCE);
        list1 = list2;
      } else {
        Iterator<WeakReference> iterator = list2.iterator();
        while (true) {
          list1 = list2;
          if (iterator.hasNext()) {
            if (((WeakReference)iterator.next()).get() == null)
              iterator.remove(); 
            continue;
          } 
          break;
        } 
      } 
      list1.add(new WeakReference<>(this));
    }
    
    public boolean unregister() {
      LocationListenerCompat locationListenerCompat = this.mListener;
      if (locationListenerCompat == null)
        return false; 
      this.mListener = null;
      List list = LocationManagerCompat.sLocationListeners.get(locationListenerCompat);
      if (list != null) {
        if (Build.VERSION.SDK_INT >= 24) {
          list.removeIf(LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda7.INSTANCE);
        } else {
          Iterator<WeakReference> iterator = list.iterator();
          while (iterator.hasNext()) {
            if (((WeakReference)iterator.next()).get() == null)
              iterator.remove(); 
          } 
        } 
        if (list.isEmpty())
          LocationManagerCompat.sLocationListeners.remove(locationListenerCompat); 
      } 
      return true;
    }
  }
  
  private static class PreRGnssStatusTransport extends GnssStatus.Callback {
    final GnssStatusCompat.Callback mCallback;
    
    volatile Executor mExecutor;
    
    PreRGnssStatusTransport(GnssStatusCompat.Callback param1Callback) {
      boolean bool;
      if (param1Callback != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkArgument(bool, "invalid null callback");
      this.mCallback = param1Callback;
    }
    
    public void onFirstFix(int param1Int) {
      Executor executor = this.mExecutor;
      if (executor == null)
        return; 
      executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda2(this, executor, param1Int));
    }
    
    public void onSatelliteStatusChanged(GnssStatus param1GnssStatus) {
      Executor executor = this.mExecutor;
      if (executor == null)
        return; 
      executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda3(this, executor, param1GnssStatus));
    }
    
    public void onStarted() {
      Executor executor = this.mExecutor;
      if (executor == null)
        return; 
      executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda0(this, executor));
    }
    
    public void onStopped() {
      Executor executor = this.mExecutor;
      if (executor == null)
        return; 
      executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda1(this, executor));
    }
    
    public void register(Executor param1Executor) {
      boolean bool1;
      boolean bool2 = true;
      if (param1Executor != null) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      Preconditions.checkArgument(bool1, "invalid null executor");
      if (this.mExecutor == null) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      Preconditions.checkState(bool1);
      this.mExecutor = param1Executor;
    }
    
    public void unregister() {
      this.mExecutor = null;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\LocationManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */