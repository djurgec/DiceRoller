package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadPath<Data, ResourceType, Transcode> {
  private final Class<Data> dataClass;
  
  private final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
  
  private final String failureMessage;
  
  private final Pools.Pool<List<Throwable>> listPool;
  
  public LoadPath(Class<Data> paramClass, Class<ResourceType> paramClass1, Class<Transcode> paramClass2, List<DecodePath<Data, ResourceType, Transcode>> paramList, Pools.Pool<List<Throwable>> paramPool) {
    this.dataClass = paramClass;
    this.listPool = paramPool;
    this.decodePaths = (List<? extends DecodePath<Data, ResourceType, Transcode>>)Preconditions.checkNotEmpty(paramList);
    this.failureMessage = "Failed LoadPath{" + paramClass.getSimpleName() + "->" + paramClass1.getSimpleName() + "->" + paramClass2.getSimpleName() + "}";
  }
  
  private Resource<Transcode> loadWithExceptionList(DataRewinder<Data> paramDataRewinder, Options paramOptions, int paramInt1, int paramInt2, DecodePath.DecodeCallback<ResourceType> paramDecodeCallback, List<Throwable> paramList) throws GlideException {
    Resource<Transcode> resource;
    int i = this.decodePaths.size();
    byte b = 0;
    DecodePath decodePath = null;
    while (true) {
      DecodePath decodePath1 = decodePath;
      if (b < i) {
        Resource<Transcode> resource1;
        decodePath1 = this.decodePaths.get(b);
        try {
          resource = decodePath1.decode(paramDataRewinder, paramInt1, paramInt2, paramOptions, paramDecodeCallback);
          resource1 = resource;
        } catch (GlideException glideException) {
          paramList.add(glideException);
        } 
        if (resource1 != null) {
          resource = resource1;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (resource != null)
      return resource; 
    throw new GlideException(this.failureMessage, new ArrayList(paramList));
  }
  
  public Class<Data> getDataClass() {
    return this.dataClass;
  }
  
  public Resource<Transcode> load(DataRewinder<Data> paramDataRewinder, Options paramOptions, int paramInt1, int paramInt2, DecodePath.DecodeCallback<ResourceType> paramDecodeCallback) throws GlideException {
    List<Throwable> list = (List)Preconditions.checkNotNull(this.listPool.acquire());
    try {
      return loadWithExceptionList(paramDataRewinder, paramOptions, paramInt1, paramInt2, paramDecodeCallback, list);
    } finally {
      this.listPool.release(list);
    } 
  }
  
  public String toString() {
    return "LoadPath{decodePaths=" + Arrays.toString(this.decodePaths.toArray()) + '}';
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\LoadPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */