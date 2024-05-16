package androidx.core.app.unusedapprestrictions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUnusedAppRestrictionsBackportService extends IInterface {
  void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback paramIUnusedAppRestrictionsBackportCallback) throws RemoteException;
  
  public static class Default implements IUnusedAppRestrictionsBackportService {
    public IBinder asBinder() {
      return null;
    }
    
    public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback param1IUnusedAppRestrictionsBackportCallback) throws RemoteException {}
  }
  
  public static abstract class Stub extends Binder implements IUnusedAppRestrictionsBackportService {
    private static final String DESCRIPTOR = "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService";
    
    static final int TRANSACTION_isPermissionRevocationEnabledForApp = 1;
    
    public Stub() {
      attachInterface(this, "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
    }
    
    public static IUnusedAppRestrictionsBackportService asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
      return (iInterface != null && iInterface instanceof IUnusedAppRestrictionsBackportService) ? (IUnusedAppRestrictionsBackportService)iInterface : new Proxy(param1IBinder);
    }
    
    public static IUnusedAppRestrictionsBackportService getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IUnusedAppRestrictionsBackportService param1IUnusedAppRestrictionsBackportService) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IUnusedAppRestrictionsBackportService != null) {
          Proxy.sDefaultImpl = param1IUnusedAppRestrictionsBackportService;
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
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
          return true;
        case 1:
          break;
      } 
      param1Parcel1.enforceInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
      isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback.Stub.asInterface(param1Parcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy implements IUnusedAppRestrictionsBackportService {
      public static IUnusedAppRestrictionsBackportService sDefaultImpl;
      
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService";
      }
      
      public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback param2IUnusedAppRestrictionsBackportCallback) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          IBinder iBinder;
          parcel.writeInterfaceToken("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
          if (param2IUnusedAppRestrictionsBackportCallback != null) {
            iBinder = param2IUnusedAppRestrictionsBackportCallback.asBinder();
          } else {
            iBinder = null;
          } 
          parcel.writeStrongBinder(iBinder);
          if (!this.mRemote.transact(1, parcel, null, 1) && IUnusedAppRestrictionsBackportService.Stub.getDefaultImpl() != null) {
            IUnusedAppRestrictionsBackportService.Stub.getDefaultImpl().isPermissionRevocationEnabledForApp(param2IUnusedAppRestrictionsBackportCallback);
            return;
          } 
          return;
        } finally {
          parcel.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements IUnusedAppRestrictionsBackportService {
    public static IUnusedAppRestrictionsBackportService sDefaultImpl;
    
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService";
    }
    
    public void isPermissionRevocationEnabledForApp(IUnusedAppRestrictionsBackportCallback param1IUnusedAppRestrictionsBackportCallback) throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        IBinder iBinder;
        parcel.writeInterfaceToken("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
        if (param1IUnusedAppRestrictionsBackportCallback != null) {
          iBinder = param1IUnusedAppRestrictionsBackportCallback.asBinder();
        } else {
          iBinder = null;
        } 
        parcel.writeStrongBinder(iBinder);
        if (!this.mRemote.transact(1, parcel, null, 1) && IUnusedAppRestrictionsBackportService.Stub.getDefaultImpl() != null) {
          IUnusedAppRestrictionsBackportService.Stub.getDefaultImpl().isPermissionRevocationEnabledForApp(param1IUnusedAppRestrictionsBackportCallback);
          return;
        } 
        return;
      } finally {
        parcel.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\ap\\unusedapprestrictions\IUnusedAppRestrictionsBackportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */