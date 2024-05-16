package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.LogTime;
import java.util.List;

class SourceGenerator implements DataFetcherGenerator, DataFetcherGenerator.FetcherReadyCallback {
  private static final String TAG = "SourceGenerator";
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private Object dataToCache;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int loadDataListIndex;
  
  private DataCacheKey originalKey;
  
  private DataCacheGenerator sourceCacheGenerator;
  
  SourceGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this.helper = paramDecodeHelper;
    this.cb = paramFetcherReadyCallback;
  }
  
  private void cacheData(Object paramObject) {
    long l = LogTime.getLogTime();
    try {
      Encoder<DataType> encoder = this.helper.getSourceEncoder(paramObject);
      DataCacheWriter dataCacheWriter = new DataCacheWriter();
      this(encoder, (DataType)paramObject, this.helper.getOptions());
      DataCacheKey dataCacheKey = new DataCacheKey();
      this(this.loadData.sourceKey, this.helper.getSignature());
      this.originalKey = dataCacheKey;
      this.helper.getDiskCache().put(this.originalKey, dataCacheWriter);
      if (Log.isLoggable("SourceGenerator", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.v("SourceGenerator", stringBuilder.append("Finished encoding source to cache, key: ").append(this.originalKey).append(", data: ").append(paramObject).append(", encoder: ").append(encoder).append(", duration: ").append(LogTime.getElapsedMillis(l)).toString());
      } 
      this.loadData.fetcher.cleanup();
      return;
    } finally {
      this.loadData.fetcher.cleanup();
    } 
  }
  
  private boolean hasNextModelLoader() {
    boolean bool;
    if (this.loadDataListIndex < this.helper.getLoadData().size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void startNextLoad(final ModelLoader.LoadData<?> toStart) {
    this.loadData.fetcher.loadData(this.helper.getPriority(), new DataFetcher.DataCallback<Object>() {
          final SourceGenerator this$0;
          
          final ModelLoader.LoadData val$toStart;
          
          public void onDataReady(Object param1Object) {
            if (SourceGenerator.this.isCurrentRequest(toStart))
              SourceGenerator.this.onDataReadyInternal(toStart, param1Object); 
          }
          
          public void onLoadFailed(Exception param1Exception) {
            if (SourceGenerator.this.isCurrentRequest(toStart))
              SourceGenerator.this.onLoadFailedInternal(toStart, param1Exception); 
          }
        });
  }
  
  public void cancel() {
    ModelLoader.LoadData<?> loadData = this.loadData;
    if (loadData != null)
      loadData.fetcher.cancel(); 
  }
  
  boolean isCurrentRequest(ModelLoader.LoadData<?> paramLoadData) {
    boolean bool;
    ModelLoader.LoadData<?> loadData = this.loadData;
    if (loadData != null && loadData == paramLoadData) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onDataFetcherFailed(Key paramKey, Exception paramException, DataFetcher<?> paramDataFetcher, DataSource paramDataSource) {
    this.cb.onDataFetcherFailed(paramKey, paramException, paramDataFetcher, this.loadData.fetcher.getDataSource());
  }
  
  public void onDataFetcherReady(Key paramKey1, Object paramObject, DataFetcher<?> paramDataFetcher, DataSource paramDataSource, Key paramKey2) {
    this.cb.onDataFetcherReady(paramKey1, paramObject, paramDataFetcher, this.loadData.fetcher.getDataSource(), paramKey1);
  }
  
  void onDataReadyInternal(ModelLoader.LoadData<?> paramLoadData, Object paramObject) {
    DiskCacheStrategy diskCacheStrategy = this.helper.getDiskCacheStrategy();
    if (paramObject != null && diskCacheStrategy.isDataCacheable(paramLoadData.fetcher.getDataSource())) {
      this.dataToCache = paramObject;
      this.cb.reschedule();
    } else {
      this.cb.onDataFetcherReady(paramLoadData.sourceKey, paramObject, paramLoadData.fetcher, paramLoadData.fetcher.getDataSource(), this.originalKey);
    } 
  }
  
  void onLoadFailedInternal(ModelLoader.LoadData<?> paramLoadData, Exception paramException) {
    this.cb.onDataFetcherFailed(this.originalKey, paramException, paramLoadData.fetcher, paramLoadData.fetcher.getDataSource());
  }
  
  public void reschedule() {
    throw new UnsupportedOperationException();
  }
  
  public boolean startNext() {
    if (this.dataToCache != null) {
      Object object = this.dataToCache;
      this.dataToCache = null;
      cacheData(object);
    } 
    DataCacheGenerator dataCacheGenerator = this.sourceCacheGenerator;
    if (dataCacheGenerator != null && dataCacheGenerator.startNext())
      return true; 
    this.sourceCacheGenerator = null;
    this.loadData = null;
    boolean bool = false;
    while (!bool && hasNextModelLoader()) {
      List<ModelLoader.LoadData<?>> list = this.helper.getLoadData();
      int i = this.loadDataListIndex;
      this.loadDataListIndex = i + 1;
      this.loadData = list.get(i);
      if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
        bool = true;
        startNextLoad(this.loadData);
      } 
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\SourceGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */