package androidx.loader.app;

import android.os.Bundle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class LoaderManager {
  public static void enableDebugLogging(boolean paramBoolean) {
    LoaderManagerImpl.DEBUG = paramBoolean;
  }
  
  public static <T extends LifecycleOwner & ViewModelStoreOwner> LoaderManager getInstance(T paramT) {
    return new LoaderManagerImpl((LifecycleOwner)paramT, ((ViewModelStoreOwner)paramT).getViewModelStore());
  }
  
  public abstract void destroyLoader(int paramInt);
  
  @Deprecated
  public abstract void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString);
  
  public abstract <D> Loader<D> getLoader(int paramInt);
  
  public boolean hasRunningLoaders() {
    return false;
  }
  
  public abstract <D> Loader<D> initLoader(int paramInt, Bundle paramBundle, LoaderCallbacks<D> paramLoaderCallbacks);
  
  public abstract void markForRedelivery();
  
  public abstract <D> Loader<D> restartLoader(int paramInt, Bundle paramBundle, LoaderCallbacks<D> paramLoaderCallbacks);
  
  public static interface LoaderCallbacks<D> {
    Loader<D> onCreateLoader(int param1Int, Bundle param1Bundle);
    
    void onLoadFinished(Loader<D> param1Loader, D param1D);
    
    void onLoaderReset(Loader<D> param1Loader);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\loader\app\LoaderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */