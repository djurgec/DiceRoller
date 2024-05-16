package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher<T> {
  void cancel();
  
  void cleanup();
  
  Class<T> getDataClass();
  
  DataSource getDataSource();
  
  void loadData(Priority paramPriority, DataCallback<? super T> paramDataCallback);
  
  public static interface DataCallback<T> {
    void onDataReady(T param1T);
    
    void onLoadFailed(Exception param1Exception);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\DataFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */