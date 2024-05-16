package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class ExternalPreferredCacheDiskCacheFactory extends DiskLruCacheFactory {
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000L);
  }
  
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext, long paramLong) {
    this(paramContext, "image_manager_disk_cache", paramLong);
  }
  
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext, String paramString, long paramLong) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          final Context val$context;
          
          final String val$diskCacheName;
          
          private File getInternalCacheDirectory() {
            File file = context.getCacheDir();
            return (file == null) ? null : ((diskCacheName != null) ? new File(file, diskCacheName) : file);
          }
          
          public File getCacheDirectory() {
            File file2 = getInternalCacheDirectory();
            if (file2 != null && file2.exists())
              return file2; 
            File file1 = context.getExternalCacheDir();
            return (file1 == null || !file1.canWrite()) ? file2 : ((diskCacheName != null) ? new File(file1, diskCacheName) : file1);
          }
        }paramLong);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\ExternalPreferredCacheDiskCacheFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */