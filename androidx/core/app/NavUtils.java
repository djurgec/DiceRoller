package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public final class NavUtils {
  public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
  
  private static final String TAG = "NavUtils";
  
  public static Intent getParentActivityIntent(Activity paramActivity) {
    if (Build.VERSION.SDK_INT >= 16) {
      Intent intent = paramActivity.getParentActivityIntent();
      if (intent != null)
        return intent; 
    } 
    String str = getParentActivityName(paramActivity);
    if (str == null)
      return null; 
    ComponentName componentName = new ComponentName((Context)paramActivity, str);
    try {
      Intent intent;
      if (getParentActivityName((Context)paramActivity, componentName) == null) {
        intent = Intent.makeMainActivity(componentName);
      } else {
        intent = new Intent();
        this();
        intent = intent.setComponent(componentName);
      } 
      return intent;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.e("NavUtils", "getParentActivityIntent: bad parentActivityName '" + str + "' in manifest");
      return null;
    } 
  }
  
  public static Intent getParentActivityIntent(Context paramContext, ComponentName paramComponentName) throws PackageManager.NameNotFoundException {
    Intent intent;
    String str = getParentActivityName(paramContext, paramComponentName);
    if (str == null)
      return null; 
    paramComponentName = new ComponentName(paramComponentName.getPackageName(), str);
    if (getParentActivityName(paramContext, paramComponentName) == null) {
      intent = Intent.makeMainActivity(paramComponentName);
    } else {
      intent = (new Intent()).setComponent(paramComponentName);
    } 
    return intent;
  }
  
  public static Intent getParentActivityIntent(Context paramContext, Class<?> paramClass) throws PackageManager.NameNotFoundException {
    Intent intent;
    String str = getParentActivityName(paramContext, new ComponentName(paramContext, paramClass));
    if (str == null)
      return null; 
    ComponentName componentName = new ComponentName(paramContext, str);
    if (getParentActivityName(paramContext, componentName) == null) {
      intent = Intent.makeMainActivity(componentName);
    } else {
      intent = (new Intent()).setComponent(componentName);
    } 
    return intent;
  }
  
  public static String getParentActivityName(Activity paramActivity) {
    try {
      return getParentActivityName((Context)paramActivity, paramActivity.getComponentName());
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      throw new IllegalArgumentException(nameNotFoundException);
    } 
  }
  
  public static String getParentActivityName(Context paramContext, ComponentName paramComponentName) throws PackageManager.NameNotFoundException {
    int i;
    int j;
    PackageManager packageManager = paramContext.getPackageManager();
    if (Build.VERSION.SDK_INT >= 24) {
      j = 0x80 | 0x200;
    } else {
      j = 0x80 | 0x200;
    } 
    if (Build.VERSION.SDK_INT >= 29) {
      i = j | 0x100C0000;
    } else {
      i = j;
      if (Build.VERSION.SDK_INT >= 24)
        i = j | 0xC0000; 
    } 
    ActivityInfo activityInfo = packageManager.getActivityInfo(paramComponentName, i);
    if (Build.VERSION.SDK_INT >= 16) {
      String str = activityInfo.parentActivityName;
      if (str != null)
        return str; 
    } 
    if (activityInfo.metaData == null)
      return null; 
    String str2 = activityInfo.metaData.getString("android.support.PARENT_ACTIVITY");
    if (str2 == null)
      return null; 
    String str1 = str2;
    if (str2.charAt(0) == '.')
      str1 = paramContext.getPackageName() + str2; 
    return str1;
  }
  
  public static void navigateUpFromSameTask(Activity paramActivity) {
    Intent intent = getParentActivityIntent(paramActivity);
    if (intent != null) {
      navigateUpTo(paramActivity, intent);
      return;
    } 
    throw new IllegalArgumentException("Activity " + paramActivity.getClass().getSimpleName() + " does not have a parent activity name specified. (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?)");
  }
  
  public static void navigateUpTo(Activity paramActivity, Intent paramIntent) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramActivity.navigateUpTo(paramIntent);
    } else {
      paramIntent.addFlags(67108864);
      paramActivity.startActivity(paramIntent);
      paramActivity.finish();
    } 
  }
  
  public static boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent) {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 16)
      return paramActivity.shouldUpRecreateTask(paramIntent); 
    String str = paramActivity.getIntent().getAction();
    if (str != null && !str.equals("android.intent.action.MAIN")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\NavUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */