package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public class DiskCacheAdapter implements DiskCache {
  public void clear() {}
  
  public void delete(Key paramKey) {}
  
  public File get(Key paramKey) {
    return null;
  }
  
  public void put(Key paramKey, DiskCache.Writer paramWriter) {}
  
  public static final class Factory implements DiskCache.Factory {
    public DiskCache build() {
      return new DiskCacheAdapter();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\DiskCacheAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */