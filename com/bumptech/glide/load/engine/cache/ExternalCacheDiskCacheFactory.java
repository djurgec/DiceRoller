package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

@Deprecated
public final class ExternalCacheDiskCacheFactory extends DiskLruCacheFactory {
  public ExternalCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000);
  }
  
  public ExternalCacheDiskCacheFactory(Context paramContext, int paramInt) {
    this(paramContext, "image_manager_disk_cache", paramInt);
  }
  
  public ExternalCacheDiskCacheFactory(Context paramContext, String paramString, int paramInt) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          final Context val$context;
          
          final String val$diskCacheName;
          
          public File getCacheDirectory() {
            File file = context.getExternalCacheDir();
            return (file == null) ? null : ((diskCacheName != null) ? new File(file, diskCacheName) : file);
          }
        }paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\ExternalCacheDiskCacheFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */