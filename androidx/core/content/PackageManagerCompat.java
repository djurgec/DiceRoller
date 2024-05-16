package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.core.os.UserManagerCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public final class PackageManagerCompat {
  public static final String ACTION_PERMISSION_REVOCATION_SETTINGS = "android.intent.action.AUTO_REVOKE_PERMISSIONS";
  
  public static final String LOG_TAG = "PackageManagerCompat";
  
  public static boolean areUnusedAppRestrictionsAvailable(PackageManager paramPackageManager) {
    boolean bool1;
    boolean bool2;
    int i = Build.VERSION.SDK_INT;
    boolean bool4 = true;
    if (i >= 30) {
      i = 1;
    } else {
      i = 0;
    } 
    if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (getPermissionRevocationVerifierApp(paramPackageManager) != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    boolean bool3 = bool4;
    if (i == 0)
      if (bool1 && bool2) {
        bool3 = bool4;
      } else {
        bool3 = false;
      }  
    return bool3;
  }
  
  public static String getPermissionRevocationVerifierApp(PackageManager paramPackageManager) {
    String str;
    Intent intent = (new Intent("android.intent.action.AUTO_REVOKE_PERMISSIONS")).setData(Uri.fromParts("package", "com.example", null));
    List list = paramPackageManager.queryIntentActivities(intent, 0);
    intent = null;
    Iterator iterator = list.iterator();
    while (iterator.hasNext()) {
      String str1 = ((ResolveInfo)iterator.next()).activityInfo.packageName;
      if (paramPackageManager.checkPermission("android.permission.PACKAGE_VERIFICATION_AGENT", str1) != 0)
        continue; 
      if (intent != null)
        return (String)intent; 
      str = str1;
    } 
    return str;
  }
  
  public static ListenableFuture<Integer> getUnusedAppRestrictionsStatus(Context paramContext) {
    ResolvableFuture<Integer> resolvableFuture = ResolvableFuture.create();
    boolean bool = UserManagerCompat.isUserUnlocked(paramContext);
    Integer integer = Integer.valueOf(0);
    if (!bool) {
      resolvableFuture.set(integer);
      Log.e("PackageManagerCompat", "User is in locked direct boot mode");
      return (ListenableFuture<Integer>)resolvableFuture;
    } 
    if (!areUnusedAppRestrictionsAvailable(paramContext.getPackageManager())) {
      resolvableFuture.set(Integer.valueOf(1));
      return (ListenableFuture<Integer>)resolvableFuture;
    } 
    int i = (paramContext.getApplicationInfo()).targetSdkVersion;
    if (i < 30) {
      resolvableFuture.set(integer);
      Log.e("PackageManagerCompat", "Target SDK version below API 30");
      return (ListenableFuture<Integer>)resolvableFuture;
    } 
    int j = Build.VERSION.SDK_INT;
    byte b = 4;
    if (j >= 31) {
      if (Api30Impl.areUnusedAppRestrictionsEnabled(paramContext)) {
        if (i >= 31)
          b = 5; 
        resolvableFuture.set(Integer.valueOf(b));
      } else {
        resolvableFuture.set(Integer.valueOf(2));
      } 
      return (ListenableFuture<Integer>)resolvableFuture;
    } 
    if (Build.VERSION.SDK_INT == 30) {
      if (!Api30Impl.areUnusedAppRestrictionsEnabled(paramContext))
        b = 2; 
      resolvableFuture.set(Integer.valueOf(b));
      return (ListenableFuture<Integer>)resolvableFuture;
    } 
    UnusedAppRestrictionsBackportServiceConnection unusedAppRestrictionsBackportServiceConnection = new UnusedAppRestrictionsBackportServiceConnection(paramContext);
    Objects.requireNonNull(unusedAppRestrictionsBackportServiceConnection);
    resolvableFuture.addListener(new PackageManagerCompat$$ExternalSyntheticLambda0(unusedAppRestrictionsBackportServiceConnection), Executors.newSingleThreadExecutor());
    unusedAppRestrictionsBackportServiceConnection.connectAndFetchResult(resolvableFuture);
    return (ListenableFuture<Integer>)resolvableFuture;
  }
  
  private static class Api30Impl {
    static boolean areUnusedAppRestrictionsEnabled(Context param1Context) {
      return param1Context.getPackageManager().isAutoRevokeWhitelisted() ^ true;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UnusedAppRestrictionsStatus {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\PackageManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */