package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class DataCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
  private File cacheFile;
  
  private final List<Key> cacheKeys;
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int modelLoaderIndex;
  
  private List<ModelLoader<File, ?>> modelLoaders;
  
  private int sourceIdIndex = -1;
  
  private Key sourceKey;
  
  DataCacheGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this(paramDecodeHelper.getCacheKeys(), paramDecodeHelper, paramFetcherReadyCallback);
  }
  
  DataCacheGenerator(List<Key> paramList, DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this.cacheKeys = paramList;
    this.helper = paramDecodeHelper;
    this.cb = paramFetcherReadyCallback;
  }
  
  private boolean hasNextModelLoader() {
    boolean bool;
    if (this.modelLoaderIndex < this.modelLoaders.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cancel() {
    ModelLoader.LoadData<?> loadData = this.loadData;
    if (loadData != null)
      loadData.fetcher.cancel(); 
  }
  
  public void onDataReady(Object paramObject) {
    this.cb.onDataFetcherReady(this.sourceKey, paramObject, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
  }
  
  public void onLoadFailed(Exception paramException) {
    this.cb.onDataFetcherFailed(this.sourceKey, paramException, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
  }
  
  public boolean startNext() {
    while (true) {
      if (this.modelLoaders == null || !hasNextModelLoader()) {
        int i = this.sourceIdIndex + 1;
        this.sourceIdIndex = i;
        if (i >= this.cacheKeys.size())
          return false; 
        Key key = this.cacheKeys.get(this.sourceIdIndex);
        DataCacheKey dataCacheKey = new DataCacheKey(key, this.helper.getSignature());
        File file = this.helper.getDiskCache().get(dataCacheKey);
        this.cacheFile = file;
        if (file != null) {
          this.sourceKey = key;
          this.modelLoaders = this.helper.getModelLoaders(file);
          this.modelLoaderIndex = 0;
        } 
        continue;
      } 
      this.loadData = null;
      boolean bool;
      for (bool = false; !bool && hasNextModelLoader(); bool = bool1) {
        List<ModelLoader<File, ?>> list = this.modelLoaders;
        int i = this.modelLoaderIndex;
        this.modelLoaderIndex = i + 1;
        this.loadData = ((ModelLoader)list.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
        boolean bool1 = bool;
        if (this.loadData != null) {
          bool1 = bool;
          if (this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
            bool1 = true;
            this.loadData.fetcher.loadData(this.helper.getPriority(), this);
          } 
        } 
      } 
      return bool;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\DataCacheGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */