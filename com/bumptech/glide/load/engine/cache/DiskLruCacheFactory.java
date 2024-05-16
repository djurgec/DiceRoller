package com.bumptech.glide.load.engine.cache;

import java.io.File;

public class DiskLruCacheFactory implements DiskCache.Factory {
  private final CacheDirectoryGetter cacheDirectoryGetter;
  
  private final long diskCacheSize;
  
  public DiskLruCacheFactory(CacheDirectoryGetter paramCacheDirectoryGetter, long paramLong) {
    this.diskCacheSize = paramLong;
    this.cacheDirectoryGetter = paramCacheDirectoryGetter;
  }
  
  public DiskLruCacheFactory(String paramString, long paramLong) {
    this(new CacheDirectoryGetter(paramString) {
          final String val$diskCacheFolder;
          
          public File getCacheDirectory() {
            return new File(diskCacheFolder);
          }
        },  paramLong);
  }
  
  public DiskLruCacheFactory(String paramString1, String paramString2, long paramLong) {
    this(new CacheDirectoryGetter(paramString1, paramString2) {
          final String val$diskCacheFolder;
          
          final String val$diskCacheName;
          
          public File getCacheDirectory() {
            return new File(diskCacheFolder, diskCacheName);
          }
        }paramLong);
  }
  
  public DiskCache build() {
    File file = this.cacheDirectoryGetter.getCacheDirectory();
    return (file == null) ? null : ((file.isDirectory() || file.mkdirs()) ? DiskLruCacheWrapper.create(file, this.diskCacheSize) : null);
  }
  
  public static interface CacheDirectoryGetter {
    File getCacheDirectory();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\DiskLruCacheFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */