package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;

public abstract class AssetPathFetcher<T> implements DataFetcher<T> {
  private static final String TAG = "AssetPathFetcher";
  
  private final AssetManager assetManager;
  
  private final String assetPath;
  
  private T data;
  
  public AssetPathFetcher(AssetManager paramAssetManager, String paramString) {
    this.assetManager = paramAssetManager;
    this.assetPath = paramString;
  }
  
  public void cancel() {}
  
  public void cleanup() {
    T t = this.data;
    if (t == null)
      return; 
    try {
      close(t);
    } catch (IOException iOException) {}
  }
  
  protected abstract void close(T paramT) throws IOException;
  
  public DataSource getDataSource() {
    return DataSource.LOCAL;
  }
  
  public void loadData(Priority paramPriority, DataFetcher.DataCallback<? super T> paramDataCallback) {
    try {
      paramPriority = (Priority)loadResource(this.assetManager, this.assetPath);
      this.data = (T)paramPriority;
      paramDataCallback.onDataReady((T)paramPriority);
    } catch (IOException iOException) {
      if (Log.isLoggable("AssetPathFetcher", 3))
        Log.d("AssetPathFetcher", "Failed to load data from asset manager", iOException); 
      paramDataCallback.onLoadFailed(iOException);
    } 
  }
  
  protected abstract T loadResource(AssetManager paramAssetManager, String paramString) throws IOException;
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\AssetPathFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */