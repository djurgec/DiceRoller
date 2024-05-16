package androidx.core.content;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;

public abstract class UnusedAppRestrictionsBackportService extends Service {
  public static final String ACTION_UNUSED_APP_RESTRICTIONS_BACKPORT_CONNECTION = "android.support.unusedapprestrictions.action.CustomUnusedAppRestrictionsBackportService";
  
  private IUnusedAppRestrictionsBackportService.Stub mBinder = new IUnusedAppRestrictionsBackportService.Stub() {
      final UnusedAppRestrictionsBackportService this$0;
      
      public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback param1IUnusedAppRestrictionsBackportCallback) throws RemoteException {
        if (param1IUnusedAppRestrictionsBackportCallback == null)
          return; 
        UnusedAppRestrictionsBackportCallback unusedAppRestrictionsBackportCallback = new UnusedAppRestrictionsBackportCallback(param1IUnusedAppRestrictionsBackportCallback);
        UnusedAppRestrictionsBackportService.this.isPermissionRevocationEnabled(unusedAppRestrictionsBackportCallback);
      }
    };
  
  protected abstract void isPermissionRevocationEnabled(UnusedAppRestrictionsBackportCallback paramUnusedAppRestrictionsBackportCallback);
  
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)this.mBinder;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\UnusedAppRestrictionsBackportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */