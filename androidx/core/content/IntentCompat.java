package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.util.Preconditions;

public final class IntentCompat {
  public static final String ACTION_CREATE_REMINDER = "android.intent.action.CREATE_REMINDER";
  
  public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
  
  public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
  
  public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
  
  public static final String EXTRA_TIME = "android.intent.extra.TIME";
  
  public static Intent createManageUnusedAppRestrictionsIntent(Context paramContext, String paramString) {
    if (PackageManagerCompat.areUnusedAppRestrictionsAvailable(paramContext.getPackageManager())) {
      if (Build.VERSION.SDK_INT >= 31)
        return (new Intent("android.settings.APPLICATION_DETAILS_SETTINGS")).setData(Uri.fromParts("package", paramString, null)); 
      Intent intent = (new Intent("android.intent.action.AUTO_REVOKE_PERMISSIONS")).setData(Uri.fromParts("package", paramString, null));
      if (Build.VERSION.SDK_INT >= 30)
        return intent; 
      String str = PackageManagerCompat.getPermissionRevocationVerifierApp(paramContext.getPackageManager());
      return intent.setPackage((String)Preconditions.checkNotNull(str));
    } 
    throw new UnsupportedOperationException("Unused App Restriction features are not available on this device");
  }
  
  public static Intent makeMainSelectorActivity(String paramString1, String paramString2) {
    if (Build.VERSION.SDK_INT >= 15)
      return Intent.makeMainSelectorActivity(paramString1, paramString2); 
    Intent intent = new Intent(paramString1);
    intent.addCategory(paramString2);
    return intent;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\IntentCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */