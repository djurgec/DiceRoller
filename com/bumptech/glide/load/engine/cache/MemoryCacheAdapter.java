package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;

public class MemoryCacheAdapter implements MemoryCache {
  private MemoryCache.ResourceRemovedListener listener;
  
  public void clearMemory() {}
  
  public long getCurrentSize() {
    return 0L;
  }
  
  public long getMaxSize() {
    return 0L;
  }
  
  public Resource<?> put(Key paramKey, Resource<?> paramResource) {
    if (paramResource != null)
      this.listener.onResourceRemoved(paramResource); 
    return null;
  }
  
  public Resource<?> remove(Key paramKey) {
    return null;
  }
  
  public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener paramResourceRemovedListener) {
    this.listener = paramResourceRemovedListener;
  }
  
  public void setSizeMultiplier(float paramFloat) {}
  
  public void trimMemory(int paramInt) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\MemoryCacheAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */