package androidx.core.content;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;

class UnusedAppRestrictionsBackportServiceConnection implements ServiceConnection {
  private final Context mContext;
  
  private boolean mHasBoundService = false;
  
  ResolvableFuture<Integer> mResultFuture;
  
  IUnusedAppRestrictionsBackportService mUnusedAppRestrictionsService = null;
  
  UnusedAppRestrictionsBackportServiceConnection(Context paramContext) {
    this.mContext = paramContext;
  }
  
  private IUnusedAppRestrictionsBackportCallback getBackportCallback() {
    return (IUnusedAppRestrictionsBackportCallback)new IUnusedAppRestrictionsBackportCallback.Stub() {
        final UnusedAppRestrictionsBackportServiceConnection this$0;
        
        public void onIsPermissionRevocationEnabledForAppResult(boolean param1Boolean1, boolean param1Boolean2) throws RemoteException {
          if (param1Boolean1) {
            if (param1Boolean2) {
              UnusedAppRestrictionsBackportServiceConnection.this.mResultFuture.set(Integer.valueOf(3));
            } else {
              UnusedAppRestrictionsBackportServiceConnection.this.mResultFuture.set(Integer.valueOf(2));
            } 
          } else {
            UnusedAppRestrictionsBackportServiceConnection.this.mResultFuture.set(Integer.valueOf(0));
            Log.e("PackageManagerCompat", "Unable to retrieve the permission revocation setting from the backport");
          } 
        }
      };
  }
  
  public void connectAndFetchResult(ResolvableFuture<Integer> paramResolvableFuture) {
    if (!this.mHasBoundService) {
      this.mHasBoundService = true;
      this.mResultFuture = paramResolvableFuture;
      Intent intent = (new Intent("android.support.unusedapprestrictions.action.CustomUnusedAppRestrictionsBackportService")).setPackage(PackageManagerCompat.getPermissionRevocationVerifierApp(this.mContext.getPackageManager()));
      this.mContext.bindService(intent, this, 1);
      return;
    } 
    throw new IllegalStateException("Each UnusedAppRestrictionsBackportServiceConnection can only be bound once.");
  }
  
  public void disconnectFromService() {
    if (this.mHasBoundService) {
      this.mHasBoundService = false;
      this.mContext.unbindService(this);
      return;
    } 
    throw new IllegalStateException("bindService must be called before unbind");
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder) {
    IUnusedAppRestrictionsBackportService iUnusedAppRestrictionsBackportService = IUnusedAppRestrictionsBackportService.Stub.asInterface(paramIBinder);
    this.mUnusedAppRestrictionsService = iUnusedAppRestrictionsBackportService;
    try {
      iUnusedAppRestrictionsBackportService.isPermissionRevocationEnabledForApp(getBackportCallback());
    } catch (RemoteException remoteException) {
      this.mResultFuture.set(Integer.valueOf(0));
    } 
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName) {
    this.mUnusedAppRestrictionsService = null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\UnusedAppRestrictionsBackportServiceConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */