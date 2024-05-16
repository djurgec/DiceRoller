package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import java.io.File;
import java.io.InputStream;

public class StringLoader<Data> implements ModelLoader<String, Data> {
  private final ModelLoader<Uri, Data> uriLoader;
  
  public StringLoader(ModelLoader<Uri, Data> paramModelLoader) {
    this.uriLoader = paramModelLoader;
  }
  
  private static Uri parseUri(String paramString) {
    Uri uri;
    if (TextUtils.isEmpty(paramString))
      return null; 
    if (paramString.charAt(0) == '/') {
      uri = toFileUri(paramString);
    } else {
      Uri uri1 = Uri.parse(paramString);
      uri = uri1;
      if (uri1.getScheme() == null)
        uri = toFileUri(paramString); 
    } 
    return uri;
  }
  
  private static Uri toFileUri(String paramString) {
    return Uri.fromFile(new File(paramString));
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(String paramString, int paramInt1, int paramInt2, Options paramOptions) {
    Uri uri = parseUri(paramString);
    return (uri == null || !this.uriLoader.handles(uri)) ? null : this.uriLoader.buildLoadData(uri, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(String paramString) {
    return true;
  }
  
  public static final class AssetFileDescriptorFactory implements ModelLoaderFactory<String, AssetFileDescriptor> {
    public ModelLoader<String, AssetFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new StringLoader<>(param1MultiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<String, ParcelFileDescriptor> {
    public ModelLoader<String, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new StringLoader<>(param1MultiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class StreamFactory implements ModelLoaderFactory<String, InputStream> {
    public ModelLoader<String, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new StringLoader<>(param1MultiModelLoaderFactory.build(Uri.class, InputStream.class));
    }
    
    public void teardown() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\StringLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */