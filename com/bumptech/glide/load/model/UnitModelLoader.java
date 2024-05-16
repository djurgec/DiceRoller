package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;

public class UnitModelLoader<Model> implements ModelLoader<Model, Model> {
  private static final UnitModelLoader<?> INSTANCE = new UnitModelLoader();
  
  public static <T> UnitModelLoader<T> getInstance() {
    return (UnitModelLoader)INSTANCE;
  }
  
  public ModelLoader.LoadData<Model> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<>((Key)new ObjectKey(paramModel), new UnitFetcher<>(paramModel));
  }
  
  public boolean handles(Model paramModel) {
    return true;
  }
  
  public static class Factory<Model> implements ModelLoaderFactory<Model, Model> {
    private static final Factory<?> FACTORY = new Factory();
    
    public static <T> Factory<T> getInstance() {
      return (Factory)FACTORY;
    }
    
    public ModelLoader<Model, Model> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return UnitModelLoader.getInstance();
    }
    
    public void teardown() {}
  }
  
  private static class UnitFetcher<Model> implements DataFetcher<Model> {
    private final Model resource;
    
    UnitFetcher(Model param1Model) {
      this.resource = param1Model;
    }
    
    public void cancel() {}
    
    public void cleanup() {}
    
    public Class<Model> getDataClass() {
      return (Class)this.resource.getClass();
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Model> param1DataCallback) {
      param1DataCallback.onDataReady(this.resource);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\UnitModelLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */