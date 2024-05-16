package com.bumptech.glide.load.model;

import android.net.Uri;
import com.bumptech.glide.load.Options;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UrlUriLoader<Data> implements ModelLoader<Uri, Data> {
  private static final Set<String> SCHEMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[] { "http", "https" })));
  
  private final ModelLoader<GlideUrl, Data> urlLoader;
  
  public UrlUriLoader(ModelLoader<GlideUrl, Data> paramModelLoader) {
    this.urlLoader = paramModelLoader;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    GlideUrl glideUrl = new GlideUrl(paramUri.toString());
    return this.urlLoader.buildLoadData(glideUrl, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(Uri paramUri) {
    return SCHEMES.contains(paramUri.getScheme());
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream> {
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new UrlUriLoader<>(param1MultiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
    }
    
    public void teardown() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\UrlUriLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */