package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
  private File cacheFile;
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private ResourceCacheKey currentKey;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int modelLoaderIndex;
  
  private List<ModelLoader<File, ?>> modelLoaders;
  
  private int resourceClassIndex = -1;
  
  private int sourceIdIndex;
  
  private Key sourceKey;
  
  ResourceCacheGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
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
    this.cb.onDataFetcherReady(this.sourceKey, paramObject, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
  }
  
  public void onLoadFailed(Exception paramException) {
    this.cb.onDataFetcherFailed(this.currentKey, paramException, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
  }
  
  public boolean startNext() {
    List<Key> list1 = this.helper.getCacheKeys();
    if (list1.isEmpty())
      return false; 
    List<Class<?>> list = this.helper.getRegisteredResourceClasses();
    if (list.isEmpty()) {
      if (File.class.equals(this.helper.getTranscodeClass()))
        return false; 
      throw new IllegalStateException("Failed to find any load path from " + this.helper.getModelClass() + " to " + this.helper.getTranscodeClass());
    } 
    while (true) {
      if (this.modelLoaders == null || !hasNextModelLoader()) {
        int i = this.resourceClassIndex + 1;
        this.resourceClassIndex = i;
        if (i >= list.size()) {
          i = this.sourceIdIndex + 1;
          this.sourceIdIndex = i;
          if (i >= list1.size())
            return false; 
          this.resourceClassIndex = 0;
        } 
        Key key = list1.get(this.sourceIdIndex);
        Class<?> clazz = list.get(this.resourceClassIndex);
        Transformation<?> transformation = this.helper.getTransformation(clazz);
        this.currentKey = new ResourceCacheKey(this.helper.getArrayPool(), key, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), transformation, clazz, this.helper.getOptions());
        File file = this.helper.getDiskCache().get(this.currentKey);
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
        List<ModelLoader<File, ?>> list2 = this.modelLoaders;
        int i = this.modelLoaderIndex;
        this.modelLoaderIndex = i + 1;
        this.loadData = ((ModelLoader)list2.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
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


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\ResourceCacheGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */