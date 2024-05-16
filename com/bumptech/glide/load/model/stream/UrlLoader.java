package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.net.URL;

public class UrlLoader implements ModelLoader<URL, InputStream> {
  private final ModelLoader<GlideUrl, InputStream> glideUrlLoader;
  
  public UrlLoader(ModelLoader<GlideUrl, InputStream> paramModelLoader) {
    this.glideUrlLoader = paramModelLoader;
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(URL paramURL, int paramInt1, int paramInt2, Options paramOptions) {
    return this.glideUrlLoader.buildLoadData(new GlideUrl(paramURL), paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(URL paramURL) {
    return true;
  }
  
  public static class StreamFactory implements ModelLoaderFactory<URL, InputStream> {
    public ModelLoader<URL, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new UrlLoader(param1MultiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
    }
    
    public void teardown() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\stream\UrlLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */