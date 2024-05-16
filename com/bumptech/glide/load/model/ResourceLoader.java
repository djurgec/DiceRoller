package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Options;
import java.io.InputStream;

public class ResourceLoader<Data> implements ModelLoader<Integer, Data> {
  private static final String TAG = "ResourceLoader";
  
  private final Resources resources;
  
  private final ModelLoader<Uri, Data> uriLoader;
  
  public ResourceLoader(Resources paramResources, ModelLoader<Uri, Data> paramModelLoader) {
    this.resources = paramResources;
    this.uriLoader = paramModelLoader;
  }
  
  private Uri getResourceUri(Integer paramInteger) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      return Uri.parse(stringBuilder.append("android.resource://").append(this.resources.getResourcePackageName(paramInteger.intValue())).append('/').append(this.resources.getResourceTypeName(paramInteger.intValue())).append('/').append(this.resources.getResourceEntryName(paramInteger.intValue())).toString());
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      if (Log.isLoggable("ResourceLoader", 5))
        Log.w("ResourceLoader", "Received invalid resource id: " + paramInteger, (Throwable)notFoundException); 
      return null;
    } 
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Integer paramInteger, int paramInt1, int paramInt2, Options paramOptions) {
    ModelLoader.LoadData<Data> loadData;
    Uri uri = getResourceUri(paramInteger);
    if (uri == null) {
      uri = null;
    } else {
      loadData = this.uriLoader.buildLoadData(uri, paramInt1, paramInt2, paramOptions);
    } 
    return loadData;
  }
  
  public boolean handles(Integer paramInteger) {
    return true;
  }
  
  public static final class AssetFileDescriptorFactory implements ModelLoaderFactory<Integer, AssetFileDescriptor> {
    private final Resources resources;
    
    public AssetFileDescriptorFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, AssetFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<Integer, ParcelFileDescriptor> {
    private final Resources resources;
    
    public FileDescriptorFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Integer, InputStream> {
    private final Resources resources;
    
    public StreamFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, InputStream.class));
    }
    
    public void teardown() {}
  }
  
  public static class UriFactory implements ModelLoaderFactory<Integer, Uri> {
    private final Resources resources;
    
    public UriFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, Uri> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<>(this.resources, UnitModelLoader.getInstance());
    }
    
    public void teardown() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\ResourceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */