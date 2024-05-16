package androidx.core.content;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.ObjectsCompat;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class ContextCompat {
  private static final String TAG = "ContextCompat";
  
  private static final Object sLock = new Object();
  
  private static final Object sSync = new Object();
  
  private static TypedValue sTempValue;
  
  public static int checkSelfPermission(Context paramContext, String paramString) {
    ObjectsCompat.requireNonNull(paramString, "permission must be non-null");
    return paramContext.checkPermission(paramString, Process.myPid(), Process.myUid());
  }
  
  public static Context createDeviceProtectedStorageContext(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 24) ? Api24Impl.createDeviceProtectedStorageContext(paramContext) : null;
  }
  
  private static File createFilesDir(File paramFile) {
    synchronized (sSync) {
      if (!paramFile.exists()) {
        if (paramFile.mkdirs())
          return paramFile; 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.w("ContextCompat", stringBuilder.append("Unable to create files subdir ").append(paramFile.getPath()).toString());
      } 
      return paramFile;
    } 
  }
  
  public static String getAttributionTag(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 30) ? Api30Impl.getAttributionTag(paramContext) : null;
  }
  
  public static File getCodeCacheDir(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getCodeCacheDir(paramContext) : createFilesDir(new File((paramContext.getApplicationInfo()).dataDir, "code_cache"));
  }
  
  public static int getColor(Context paramContext, int paramInt) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getColor(paramContext, paramInt) : paramContext.getResources().getColor(paramInt);
  }
  
  public static ColorStateList getColorStateList(Context paramContext, int paramInt) {
    return ResourcesCompat.getColorStateList(paramContext.getResources(), paramInt, paramContext.getTheme());
  }
  
  public static File getDataDir(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 24)
      return Api24Impl.getDataDir(paramContext); 
    String str = (paramContext.getApplicationInfo()).dataDir;
    if (str != null) {
      File file = new File(str);
    } else {
      str = null;
    } 
    return (File)str;
  }
  
  public static Drawable getDrawable(Context paramContext, int paramInt) {
    if (Build.VERSION.SDK_INT >= 21)
      return Api21Impl.getDrawable(paramContext, paramInt); 
    if (Build.VERSION.SDK_INT >= 16)
      return paramContext.getResources().getDrawable(paramInt); 
    synchronized (sLock) {
      if (sTempValue == null) {
        TypedValue typedValue = new TypedValue();
        this();
        sTempValue = typedValue;
      } 
      paramContext.getResources().getValue(paramInt, sTempValue, true);
      paramInt = sTempValue.resourceId;
      return paramContext.getResources().getDrawable(paramInt);
    } 
  }
  
  public static File[] getExternalCacheDirs(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 19) ? Api19Impl.getExternalCacheDirs(paramContext) : new File[] { paramContext.getExternalCacheDir() };
  }
  
  public static File[] getExternalFilesDirs(Context paramContext, String paramString) {
    return (Build.VERSION.SDK_INT >= 19) ? Api19Impl.getExternalFilesDirs(paramContext, paramString) : new File[] { paramContext.getExternalFilesDir(paramString) };
  }
  
  public static Executor getMainExecutor(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 28) ? Api28Impl.getMainExecutor(paramContext) : ExecutorCompat.create(new Handler(paramContext.getMainLooper()));
  }
  
  public static File getNoBackupFilesDir(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getNoBackupFilesDir(paramContext) : createFilesDir(new File((paramContext.getApplicationInfo()).dataDir, "no_backup"));
  }
  
  public static File[] getObbDirs(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 19) ? Api19Impl.getObbDirs(paramContext) : new File[] { paramContext.getObbDir() };
  }
  
  public static <T> T getSystemService(Context paramContext, Class<T> paramClass) {
    if (Build.VERSION.SDK_INT >= 23)
      return Api23Impl.getSystemService(paramContext, paramClass); 
    String str = getSystemServiceName(paramContext, paramClass);
    if (str != null) {
      Object object = paramContext.getSystemService(str);
    } else {
      paramContext = null;
    } 
    return (T)paramContext;
  }
  
  public static String getSystemServiceName(Context paramContext, Class<?> paramClass) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getSystemServiceName(paramContext, paramClass) : LegacyServiceMapHolder.SERVICES.get(paramClass);
  }
  
  public static boolean isDeviceProtectedStorage(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 24) ? Api24Impl.isDeviceProtectedStorage(paramContext) : false;
  }
  
  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent) {
    return startActivities(paramContext, paramArrayOfIntent, null);
  }
  
  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent, Bundle paramBundle) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.startActivities(paramContext, paramArrayOfIntent, paramBundle);
    } else {
      paramContext.startActivities(paramArrayOfIntent);
    } 
    return true;
  }
  
  public static void startActivity(Context paramContext, Intent paramIntent, Bundle paramBundle) {
    if (Build.VERSION.SDK_INT >= 16) {
      Api16Impl.startActivity(paramContext, paramIntent, paramBundle);
    } else {
      paramContext.startActivity(paramIntent);
    } 
  }
  
  public static void startForegroundService(Context paramContext, Intent paramIntent) {
    if (Build.VERSION.SDK_INT >= 26) {
      Api26Impl.startForegroundService(paramContext, paramIntent);
    } else {
      paramContext.startService(paramIntent);
    } 
  }
  
  static class Api16Impl {
    static void startActivities(Context param1Context, Intent[] param1ArrayOfIntent, Bundle param1Bundle) {
      param1Context.startActivities(param1ArrayOfIntent, param1Bundle);
    }
    
    static void startActivity(Context param1Context, Intent param1Intent, Bundle param1Bundle) {
      param1Context.startActivity(param1Intent, param1Bundle);
    }
  }
  
  static class Api19Impl {
    static File[] getExternalCacheDirs(Context param1Context) {
      return param1Context.getExternalCacheDirs();
    }
    
    static File[] getExternalFilesDirs(Context param1Context, String param1String) {
      return param1Context.getExternalFilesDirs(param1String);
    }
    
    static File[] getObbDirs(Context param1Context) {
      return param1Context.getObbDirs();
    }
  }
  
  static class Api21Impl {
    static File getCodeCacheDir(Context param1Context) {
      return param1Context.getCodeCacheDir();
    }
    
    static Drawable getDrawable(Context param1Context, int param1Int) {
      return param1Context.getDrawable(param1Int);
    }
    
    static File getNoBackupFilesDir(Context param1Context) {
      return param1Context.getNoBackupFilesDir();
    }
  }
  
  static class Api23Impl {
    static int getColor(Context param1Context, int param1Int) {
      return param1Context.getColor(param1Int);
    }
    
    static <T> T getSystemService(Context param1Context, Class<T> param1Class) {
      return (T)param1Context.getSystemService(param1Class);
    }
    
    static String getSystemServiceName(Context param1Context, Class<?> param1Class) {
      return param1Context.getSystemServiceName(param1Class);
    }
  }
  
  static class Api24Impl {
    static Context createDeviceProtectedStorageContext(Context param1Context) {
      return param1Context.createDeviceProtectedStorageContext();
    }
    
    static File getDataDir(Context param1Context) {
      return param1Context.getDataDir();
    }
    
    static boolean isDeviceProtectedStorage(Context param1Context) {
      return param1Context.isDeviceProtectedStorage();
    }
  }
  
  static class Api26Impl {
    static ComponentName startForegroundService(Context param1Context, Intent param1Intent) {
      return param1Context.startForegroundService(param1Intent);
    }
  }
  
  static class Api28Impl {
    static Executor getMainExecutor(Context param1Context) {
      return param1Context.getMainExecutor();
    }
  }
  
  static class Api30Impl {
    static String getAttributionTag(Context param1Context) {
      return param1Context.getAttributionTag();
    }
  }
  
  private static final class LegacyServiceMapHolder {
    static final HashMap<Class<?>, String> SERVICES;
    
    static {
      HashMap<Object, Object> hashMap = new HashMap<>();
      SERVICES = (HashMap)hashMap;
      if (Build.VERSION.SDK_INT >= 22) {
        hashMap.put(SubscriptionManager.class, "telephony_subscription_service");
        hashMap.put(UsageStatsManager.class, "usagestats");
      } 
      if (Build.VERSION.SDK_INT >= 21) {
        hashMap.put(AppWidgetManager.class, "appwidget");
        hashMap.put(BatteryManager.class, "batterymanager");
        hashMap.put(CameraManager.class, "camera");
        hashMap.put(JobScheduler.class, "jobscheduler");
        hashMap.put(LauncherApps.class, "launcherapps");
        hashMap.put(MediaProjectionManager.class, "media_projection");
        hashMap.put(MediaSessionManager.class, "media_session");
        hashMap.put(RestrictionsManager.class, "restrictions");
        hashMap.put(TelecomManager.class, "telecom");
        hashMap.put(TvInputManager.class, "tv_input");
      } 
      if (Build.VERSION.SDK_INT >= 19) {
        hashMap.put(AppOpsManager.class, "appops");
        hashMap.put(CaptioningManager.class, "captioning");
        hashMap.put(ConsumerIrManager.class, "consumer_ir");
        hashMap.put(PrintManager.class, "print");
      } 
      if (Build.VERSION.SDK_INT >= 18)
        hashMap.put(BluetoothManager.class, "bluetooth"); 
      if (Build.VERSION.SDK_INT >= 17) {
        hashMap.put(DisplayManager.class, "display");
        hashMap.put(UserManager.class, "user");
      } 
      if (Build.VERSION.SDK_INT >= 16) {
        hashMap.put(InputManager.class, "input");
        hashMap.put(MediaRouter.class, "media_router");
        hashMap.put(NsdManager.class, "servicediscovery");
      } 
      hashMap.put(AccessibilityManager.class, "accessibility");
      hashMap.put(AccountManager.class, "account");
      hashMap.put(ActivityManager.class, "activity");
      hashMap.put(AlarmManager.class, "alarm");
      hashMap.put(AudioManager.class, "audio");
      hashMap.put(ClipboardManager.class, "clipboard");
      hashMap.put(ConnectivityManager.class, "connectivity");
      hashMap.put(DevicePolicyManager.class, "device_policy");
      hashMap.put(DownloadManager.class, "download");
      hashMap.put(DropBoxManager.class, "dropbox");
      hashMap.put(InputMethodManager.class, "input_method");
      hashMap.put(KeyguardManager.class, "keyguard");
      hashMap.put(LayoutInflater.class, "layout_inflater");
      hashMap.put(LocationManager.class, "location");
      hashMap.put(NfcManager.class, "nfc");
      hashMap.put(NotificationManager.class, "notification");
      hashMap.put(PowerManager.class, "power");
      hashMap.put(SearchManager.class, "search");
      hashMap.put(SensorManager.class, "sensor");
      hashMap.put(StorageManager.class, "storage");
      hashMap.put(TelephonyManager.class, "phone");
      hashMap.put(TextServicesManager.class, "textservices");
      hashMap.put(UiModeManager.class, "uimode");
      hashMap.put(UsbManager.class, "usb");
      hashMap.put(Vibrator.class, "vibrator");
      hashMap.put(WallpaperManager.class, "wallpaper");
      hashMap.put(WifiP2pManager.class, "wifip2p");
      hashMap.put(WifiManager.class, "wifi");
      hashMap.put(WindowManager.class, "window");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\ContextCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */