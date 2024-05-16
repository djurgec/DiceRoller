package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {
  private final Pools.Pool<List<Throwable>> exceptionListPool;
  
  private final List<ModelLoader<Model, Data>> modelLoaders;
  
  MultiModelLoader(List<ModelLoader<Model, Data>> paramList, Pools.Pool<List<Throwable>> paramPool) {
    this.modelLoaders = paramList;
    this.exceptionListPool = paramPool;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    Key key = null;
    int i = this.modelLoaders.size();
    ArrayList<DataFetcher> arrayList = new ArrayList(i);
    byte b = 0;
    while (b < i) {
      ModelLoader modelLoader = this.modelLoaders.get(b);
      Key key1 = key;
      if (modelLoader.handles(paramModel)) {
        ModelLoader.LoadData loadData = modelLoader.buildLoadData(paramModel, paramInt1, paramInt2, paramOptions);
        key1 = key;
        if (loadData != null) {
          key1 = loadData.sourceKey;
          arrayList.add(loadData.fetcher);
        } 
      } 
      b++;
      key = key1;
    } 
    if (!arrayList.isEmpty() && key != null) {
      ModelLoader.LoadData loadData = new ModelLoader.LoadData(key, new MultiFetcher((List)arrayList, this.exceptionListPool));
    } else {
      paramModel = null;
    } 
    return (ModelLoader.LoadData<Data>)paramModel;
  }
  
  public boolean handles(Model paramModel) {
    Iterator<ModelLoader<Model, Data>> iterator = this.modelLoaders.iterator();
    while (iterator.hasNext()) {
      if (((ModelLoader)iterator.next()).handles(paramModel))
        return true; 
    } 
    return false;
  }
  
  public String toString() {
    return "MultiModelLoader{modelLoaders=" + Arrays.toString(this.modelLoaders.toArray()) + '}';
  }
  
  static class MultiFetcher<Data> implements DataFetcher<Data>, DataFetcher.DataCallback<Data> {
    private DataFetcher.DataCallback<? super Data> callback;
    
    private int currentIndex;
    
    private List<Throwable> exceptions;
    
    private final List<DataFetcher<Data>> fetchers;
    
    private boolean isCancelled;
    
    private Priority priority;
    
    private final Pools.Pool<List<Throwable>> throwableListPool;
    
    MultiFetcher(List<DataFetcher<Data>> param1List, Pools.Pool<List<Throwable>> param1Pool) {
      this.throwableListPool = param1Pool;
      Preconditions.checkNotEmpty(param1List);
      this.fetchers = param1List;
      this.currentIndex = 0;
    }
    
    private void startNextOrFail() {
      if (this.isCancelled)
        return; 
      if (this.currentIndex < this.fetchers.size() - 1) {
        this.currentIndex++;
        loadData(this.priority, this.callback);
      } else {
        Preconditions.checkNotNull(this.exceptions);
        this.callback.onLoadFailed((Exception)new GlideException("Fetch failed", new ArrayList<>(this.exceptions)));
      } 
    }
    
    public void cancel() {
      this.isCancelled = true;
      Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
      while (iterator.hasNext())
        ((DataFetcher)iterator.next()).cancel(); 
    }
    
    public void cleanup() {
      List<Throwable> list = this.exceptions;
      if (list != null)
        this.throwableListPool.release(list); 
      this.exceptions = null;
      Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
      while (iterator.hasNext())
        ((DataFetcher)iterator.next()).cleanup(); 
    }
    
    public Class<Data> getDataClass() {
      return ((DataFetcher)this.fetchers.get(0)).getDataClass();
    }
    
    public DataSource getDataSource() {
      return ((DataFetcher)this.fetchers.get(0)).getDataSource();
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Data> param1DataCallback) {
      this.priority = param1Priority;
      this.callback = param1DataCallback;
      this.exceptions = (List<Throwable>)this.throwableListPool.acquire();
      ((DataFetcher)this.fetchers.get(this.currentIndex)).loadData(param1Priority, this);
      if (this.isCancelled)
        cancel(); 
    }
    
    public void onDataReady(Data param1Data) {
      if (param1Data != null) {
        this.callback.onDataReady(param1Data);
      } else {
        startNextOrFail();
      } 
    }
    
    public void onLoadFailed(Exception param1Exception) {
      ((List<Exception>)Preconditions.checkNotNull(this.exceptions)).add(param1Exception);
      startNextOrFail();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\MultiModelLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */