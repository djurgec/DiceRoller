package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.util.MultiClassKey;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class LoadPathCache {
  private static final LoadPath<?, ?, ?> NO_PATHS_SIGNAL = new LoadPath(Object.class, Object.class, Object.class, Collections.singletonList(new DecodePath(Object.class, Object.class, Object.class, Collections.emptyList(), (ResourceTranscoder)new UnitTranscoder(), null)), null);
  
  private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache = new ArrayMap();
  
  private final AtomicReference<MultiClassKey> keyRef = new AtomicReference<>();
  
  private MultiClassKey getKey(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3) {
    MultiClassKey multiClassKey2 = this.keyRef.getAndSet(null);
    MultiClassKey multiClassKey1 = multiClassKey2;
    if (multiClassKey2 == null)
      multiClassKey1 = new MultiClassKey(); 
    multiClassKey1.set(paramClass1, paramClass2, paramClass3);
    return multiClassKey1;
  }
  
  public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(Class<Data> paramClass, Class<TResource> paramClass1, Class<Transcode> paramClass2) {
    MultiClassKey multiClassKey = getKey(paramClass, paramClass1, paramClass2);
    synchronized (this.cache) {
      LoadPath<Data, TResource, Transcode> loadPath = (LoadPath)this.cache.get(multiClassKey);
      this.keyRef.set(multiClassKey);
      return loadPath;
    } 
  }
  
  public boolean isEmptyLoadPath(LoadPath<?, ?, ?> paramLoadPath) {
    return NO_PATHS_SIGNAL.equals(paramLoadPath);
  }
  
  public void put(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3, LoadPath<?, ?, ?> paramLoadPath) {
    synchronized (this.cache) {
      LoadPath<?, ?, ?> loadPath;
      ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> arrayMap = this.cache;
      MultiClassKey multiClassKey = new MultiClassKey();
      this(paramClass1, paramClass2, paramClass3);
      if (paramLoadPath != null) {
        loadPath = paramLoadPath;
      } else {
        loadPath = NO_PATHS_SIGNAL;
      } 
      arrayMap.put(multiClassKey, loadPath);
      return;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\provider\LoadPathCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */