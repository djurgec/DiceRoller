package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LocalUriFetcher<T> implements DataFetcher<T> {
  private static final String TAG = "LocalUriFetcher";
  
  private final ContentResolver contentResolver;
  
  private T data;
  
  private final Uri uri;
  
  public LocalUriFetcher(ContentResolver paramContentResolver, Uri paramUri) {
    this.contentResolver = paramContentResolver;
    this.uri = paramUri;
  }
  
  public void cancel() {}
  
  public void cleanup() {
    T t = this.data;
    if (t != null)
      try {
        close(t);
      } catch (IOException iOException) {} 
  }
  
  protected abstract void close(T paramT) throws IOException;
  
  public DataSource getDataSource() {
    return DataSource.LOCAL;
  }
  
  public final void loadData(Priority paramPriority, DataFetcher.DataCallback<? super T> paramDataCallback) {
    try {
      paramPriority = (Priority)loadResource(this.uri, this.contentResolver);
      this.data = (T)paramPriority;
      paramDataCallback.onDataReady((T)paramPriority);
    } catch (FileNotFoundException fileNotFoundException) {
      if (Log.isLoggable("LocalUriFetcher", 3))
        Log.d("LocalUriFetcher", "Failed to open Uri", fileNotFoundException); 
      paramDataCallback.onLoadFailed(fileNotFoundException);
    } 
  }
  
  protected abstract T loadResource(Uri paramUri, ContentResolver paramContentResolver) throws FileNotFoundException;
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\LocalUriFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */