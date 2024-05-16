package androidx.core.content;

import android.os.RemoteException;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;

public class UnusedAppRestrictionsBackportCallback {
  private IUnusedAppRestrictionsBackportCallback mCallback;
  
  public UnusedAppRestrictionsBackportCallback(IUnusedAppRestrictionsBackportCallback paramIUnusedAppRestrictionsBackportCallback) {
    this.mCallback = paramIUnusedAppRestrictionsBackportCallback;
  }
  
  public void onResult(boolean paramBoolean1, boolean paramBoolean2) throws RemoteException {
    this.mCallback.onIsPermissionRevocationEnabledForAppResult(paramBoolean1, paramBoolean2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\UnusedAppRestrictionsBackportCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */