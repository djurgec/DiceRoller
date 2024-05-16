package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public interface DiskCache {
  void clear();
  
  void delete(Key paramKey);
  
  File get(Key paramKey);
  
  void put(Key paramKey, Writer paramWriter);
  
  public static interface Factory {
    public static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
    
    public static final int DEFAULT_DISK_CACHE_SIZE = 262144000;
    
    DiskCache build();
  }
  
  public static interface Writer {
    boolean write(File param1File);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\DiskCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */