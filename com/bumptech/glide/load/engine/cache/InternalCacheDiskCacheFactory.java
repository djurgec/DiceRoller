package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {
  public InternalCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000L);
  }
  
  public InternalCacheDiskCacheFactory(Context paramContext, long paramLong) {
    this(paramContext, "image_manager_disk_cache", paramLong);
  }
  
  public InternalCacheDiskCacheFactory(Context paramContext, String paramString, long paramLong) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          final Context val$context;
          
          final String val$diskCacheName;
          
          public File getCacheDirectory() {
            File file = context.getCacheDir();
            return (file == null) ? null : ((diskCacheName != null) ? new File(file, diskCacheName) : file);
          }
        }paramLong);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\InternalCacheDiskCacheFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */