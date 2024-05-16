package com.bumptech.glide.load.model;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class AssetUriLoader<Data> implements ModelLoader<Uri, Data> {
  private static final String ASSET_PATH_SEGMENT = "android_asset";
  
  private static final String ASSET_PREFIX = "file:///android_asset/";
  
  private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
  
  private final AssetManager assetManager;
  
  private final AssetFetcherFactory<Data> factory;
  
  public AssetUriLoader(AssetManager paramAssetManager, AssetFetcherFactory<Data> paramAssetFetcherFactory) {
    this.assetManager = paramAssetManager;
    this.factory = paramAssetFetcherFactory;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    String str = paramUri.toString().substring(ASSET_PREFIX_LENGTH);
    return new ModelLoader.LoadData<>((Key)new ObjectKey(paramUri), this.factory.buildFetcher(this.assetManager, str));
  }
  
  public boolean handles(Uri paramUri) {
    boolean bool1 = "file".equals(paramUri.getScheme());
    boolean bool = false;
    if (bool1 && !paramUri.getPathSegments().isEmpty() && "android_asset".equals(paramUri.getPathSegments().get(0)))
      bool = true; 
    return bool;
  }
  
  public static interface AssetFetcherFactory<Data> {
    DataFetcher<Data> buildFetcher(AssetManager param1AssetManager, String param1String);
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<Uri, ParcelFileDescriptor>, AssetFetcherFactory<ParcelFileDescriptor> {
    private final AssetManager assetManager;
    
    public FileDescriptorFactory(AssetManager param1AssetManager) {
      this.assetManager = param1AssetManager;
    }
    
    public ModelLoader<Uri, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new AssetUriLoader<>(this.assetManager, this);
    }
    
    public DataFetcher<ParcelFileDescriptor> buildFetcher(AssetManager param1AssetManager, String param1String) {
      return (DataFetcher<ParcelFileDescriptor>)new FileDescriptorAssetPathFetcher(param1AssetManager, param1String);
    }
    
    public void teardown() {}
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream>, AssetFetcherFactory<InputStream> {
    private final AssetManager assetManager;
    
    public StreamFactory(AssetManager param1AssetManager) {
      this.assetManager = param1AssetManager;
    }
    
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new AssetUriLoader<>(this.assetManager, this);
    }
    
    public DataFetcher<InputStream> buildFetcher(AssetManager param1AssetManager, String param1String) {
      return (DataFetcher<InputStream>)new StreamAssetPathFetcher(param1AssetManager, param1String);
    }
    
    public void teardown() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\AssetUriLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */