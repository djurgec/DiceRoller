package androidx.core.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

public final class AppOpsManagerCompat {
  public static final int MODE_ALLOWED = 0;
  
  public static final int MODE_DEFAULT = 3;
  
  public static final int MODE_ERRORED = 2;
  
  public static final int MODE_IGNORED = 1;
  
  public static int checkOrNoteProxyOp(Context paramContext, int paramInt, String paramString1, String paramString2) {
    if (Build.VERSION.SDK_INT >= 29) {
      AppOpsManager appOpsManager = Api29Impl.getSystemService(paramContext);
      int i = Api29Impl.checkOpNoThrow(appOpsManager, paramString1, Binder.getCallingUid(), paramString2);
      return (i != 0) ? i : Api29Impl.checkOpNoThrow(appOpsManager, paramString1, paramInt, Api29Impl.getOpPackageName(paramContext));
    } 
    return noteProxyOpNoThrow(paramContext, paramString1, paramString2);
  }
  
  public static int noteOp(Context paramContext, String paramString1, int paramInt, String paramString2) {
    return (Build.VERSION.SDK_INT >= 19) ? ((AppOpsManager)paramContext.getSystemService("appops")).noteOp(paramString1, paramInt, paramString2) : 1;
  }
  
  public static int noteOpNoThrow(Context paramContext, String paramString1, int paramInt, String paramString2) {
    return (Build.VERSION.SDK_INT >= 19) ? ((AppOpsManager)paramContext.getSystemService("appops")).noteOpNoThrow(paramString1, paramInt, paramString2) : 1;
  }
  
  public static int noteProxyOp(Context paramContext, String paramString1, String paramString2) {
    return (Build.VERSION.SDK_INT >= 23) ? ((AppOpsManager)paramContext.getSystemService(AppOpsManager.class)).noteProxyOp(paramString1, paramString2) : 1;
  }
  
  public static int noteProxyOpNoThrow(Context paramContext, String paramString1, String paramString2) {
    return (Build.VERSION.SDK_INT >= 23) ? ((AppOpsManager)paramContext.getSystemService(AppOpsManager.class)).noteProxyOpNoThrow(paramString1, paramString2) : 1;
  }
  
  public static String permissionToOp(String paramString) {
    return (Build.VERSION.SDK_INT >= 23) ? AppOpsManager.permissionToOp(paramString) : null;
  }
  
  static class Api29Impl {
    static int checkOpNoThrow(AppOpsManager param1AppOpsManager, String param1String1, int param1Int, String param1String2) {
      return (param1AppOpsManager == null) ? 1 : param1AppOpsManager.checkOpNoThrow(param1String1, param1Int, param1String2);
    }
    
    static String getOpPackageName(Context param1Context) {
      return param1Context.getOpPackageName();
    }
    
    static AppOpsManager getSystemService(Context param1Context) {
      return (AppOpsManager)param1Context.getSystemService(AppOpsManager.class);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\AppOpsManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */