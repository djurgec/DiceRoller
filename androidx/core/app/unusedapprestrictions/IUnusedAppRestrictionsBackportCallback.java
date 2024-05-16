package androidx.core.app.unusedapprestrictions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUnusedAppRestrictionsBackportCallback extends IInterface {
  void onIsPermissionRevocationEnabledForAppResult(boolean paramBoolean1, boolean paramBoolean2) throws RemoteException;
  
  public static class Default implements IUnusedAppRestrictionsBackportCallback {
    public IBinder asBinder() {
      return null;
    }
    
    public void onIsPermissionRevocationEnabledForAppResult(boolean param1Boolean1, boolean param1Boolean2) throws RemoteException {}
  }
  
  public static abstract class Stub extends Binder implements IUnusedAppRestrictionsBackportCallback {
    private static final String DESCRIPTOR = "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback";
    
    static final int TRANSACTION_onIsPermissionRevocationEnabledForAppResult = 1;
    
    public Stub() {
      attachInterface(this, "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
    }
    
    public static IUnusedAppRestrictionsBackportCallback asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
      return (iInterface != null && iInterface instanceof IUnusedAppRestrictionsBackportCallback) ? (IUnusedAppRestrictionsBackportCallback)iInterface : new Proxy(param1IBinder);
    }
    
    public static IUnusedAppRestrictionsBackportCallback getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IUnusedAppRestrictionsBackportCallback param1IUnusedAppRestrictionsBackportCallback) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IUnusedAppRestrictionsBackportCallback != null) {
          Proxy.sDefaultImpl = param1IUnusedAppRestrictionsBackportCallback;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException("setDefaultImpl() called twice");
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      boolean bool1;
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
          return true;
        case 1:
          break;
      } 
      param1Parcel1.enforceInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
      param1Int1 = param1Parcel1.readInt();
      boolean bool2 = false;
      if (param1Int1 != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (param1Parcel1.readInt() != 0)
        bool2 = true; 
      onIsPermissionRevocationEnabledForAppResult(bool1, bool2);
      return true;
    }
    
    private static class Proxy implements IUnusedAppRestrictionsBackportCallback {
      public static IUnusedAppRestrictionsBackportCallback sDefaultImpl;
      
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback";
      }
      
      public void onIsPermissionRevocationEnabledForAppResult(boolean param2Boolean1, boolean param2Boolean2) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
          boolean bool2 = false;
          if (param2Boolean1) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          parcel.writeInt(bool1);
          boolean bool1 = bool2;
          if (param2Boolean2)
            bool1 = true; 
          parcel.writeInt(bool1);
          if (!this.mRemote.transact(1, parcel, null, 1) && IUnusedAppRestrictionsBackportCallback.Stub.getDefaultImpl() != null) {
            IUnusedAppRestrictionsBackportCallback.Stub.getDefaultImpl().onIsPermissionRevocationEnabledForAppResult(param2Boolean1, param2Boolean2);
            return;
          } 
          return;
        } finally {
          parcel.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements IUnusedAppRestrictionsBackportCallback {
    public static IUnusedAppRestrictionsBackportCallback sDefaultImpl;
    
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback";
    }
    
    public void onIsPermissionRevocationEnabledForAppResult(boolean param1Boolean1, boolean param1Boolean2) throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
        boolean bool2 = false;
        if (param1Boolean1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        parcel.writeInt(bool1);
        boolean bool1 = bool2;
        if (param1Boolean2)
          bool1 = true; 
        parcel.writeInt(bool1);
        if (!this.mRemote.transact(1, parcel, null, 1) && IUnusedAppRestrictionsBackportCallback.Stub.getDefaultImpl() != null) {
          IUnusedAppRestrictionsBackportCallback.Stub.getDefaultImpl().onIsPermissionRevocationEnabledForAppResult(param1Boolean1, param1Boolean2);
          return;
        } 
        return;
      } finally {
        parcel.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\ap\\unusedapprestrictions\IUnusedAppRestrictionsBackportCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */