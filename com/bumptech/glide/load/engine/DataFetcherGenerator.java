package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;

interface DataFetcherGenerator {
  void cancel();
  
  boolean startNext();
  
  public static interface FetcherReadyCallback {
    void onDataFetcherFailed(Key param1Key, Exception param1Exception, DataFetcher<?> param1DataFetcher, DataSource param1DataSource);
    
    void onDataFetcherReady(Key param1Key1, Object param1Object, DataFetcher<?> param1DataFetcher, DataSource param1DataSource, Key param1Key2);
    
    void reschedule();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\DataFetcherGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */