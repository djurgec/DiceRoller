package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ApplicationVersionSignature {
  private static final ConcurrentMap<String, Key> PACKAGE_NAME_TO_KEY = new ConcurrentHashMap<>();
  
  private static final String TAG = "AppVersionSignature";
  
  private static PackageInfo getPackageInfo(Context paramContext) {
    try {
      return paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.e("AppVersionSignature", "Cannot resolve info for" + paramContext.getPackageName(), (Throwable)nameNotFoundException);
      return null;
    } 
  }
  
  private static String getVersionCode(PackageInfo paramPackageInfo) {
    String str;
    if (paramPackageInfo != null) {
      str = String.valueOf(paramPackageInfo.versionCode);
    } else {
      str = UUID.randomUUID().toString();
    } 
    return str;
  }
  
  public static Key obtain(Context paramContext) {
    String str = paramContext.getPackageName();
    ConcurrentMap<String, Key> concurrentMap = PACKAGE_NAME_TO_KEY;
    Key key2 = concurrentMap.get(str);
    Key key1 = key2;
    if (key2 == null) {
      Key key = obtainVersionSignature(paramContext);
      key2 = concurrentMap.putIfAbsent(str, key);
      key1 = key2;
      if (key2 == null)
        key1 = key; 
    } 
    return key1;
  }
  
  private static Key obtainVersionSignature(Context paramContext) {
    return new ObjectKey(getVersionCode(getPackageInfo(paramContext)));
  }
  
  static void reset() {
    PACKAGE_NAME_TO_KEY.clear();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\signature\ApplicationVersionSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */