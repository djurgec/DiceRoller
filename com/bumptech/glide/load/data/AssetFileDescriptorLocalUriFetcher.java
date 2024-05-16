package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class AssetFileDescriptorLocalUriFetcher extends LocalUriFetcher<AssetFileDescriptor> {
  public AssetFileDescriptorLocalUriFetcher(ContentResolver paramContentResolver, Uri paramUri) {
    super(paramContentResolver, paramUri);
  }
  
  protected void close(AssetFileDescriptor paramAssetFileDescriptor) throws IOException {
    paramAssetFileDescriptor.close();
  }
  
  public Class<AssetFileDescriptor> getDataClass() {
    return AssetFileDescriptor.class;
  }
  
  protected AssetFileDescriptor loadResource(Uri paramUri, ContentResolver paramContentResolver) throws FileNotFoundException {
    AssetFileDescriptor assetFileDescriptor = paramContentResolver.openAssetFileDescriptor(paramUri, "r");
    if (assetFileDescriptor != null)
      return assetFileDescriptor; 
    throw new FileNotFoundException("FileDescriptor is null for: " + paramUri);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\AssetFileDescriptorLocalUriFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */