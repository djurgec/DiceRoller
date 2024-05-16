package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.util.MultiClassKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModelToResourceClassCache {
  private final ArrayMap<MultiClassKey, List<Class<?>>> registeredResourceClassCache = new ArrayMap();
  
  private final AtomicReference<MultiClassKey> resourceClassKeyRef = new AtomicReference<>();
  
  public void clear() {
    synchronized (this.registeredResourceClassCache) {
      this.registeredResourceClassCache.clear();
      return;
    } 
  }
  
  public List<Class<?>> get(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3) {
    MultiClassKey multiClassKey = this.resourceClassKeyRef.getAndSet(null);
    if (multiClassKey == null) {
      null = new MultiClassKey(paramClass1, paramClass2, paramClass3);
    } else {
      multiClassKey.set((Class)null, paramClass2, paramClass3);
      null = multiClassKey;
    } 
    synchronized (this.registeredResourceClassCache) {
      List<Class<?>> list = (List)this.registeredResourceClassCache.get(null);
      this.resourceClassKeyRef.set(null);
      return list;
    } 
  }
  
  public void put(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3, List<Class<?>> paramList) {
    synchronized (this.registeredResourceClassCache) {
      ArrayMap<MultiClassKey, List<Class<?>>> arrayMap = this.registeredResourceClassCache;
      MultiClassKey multiClassKey = new MultiClassKey();
      this(paramClass1, paramClass2, paramClass3);
      arrayMap.put(multiClassKey, paramList);
      return;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\provider\ModelToResourceClassCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */