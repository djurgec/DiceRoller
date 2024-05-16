package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import java.io.InputStream;

@Deprecated
public class HttpUriLoader extends UrlUriLoader<InputStream> {
  public HttpUriLoader(ModelLoader<GlideUrl, InputStream> paramModelLoader) {
    super(paramModelLoader);
  }
  
  @Deprecated
  public static class Factory extends UrlUriLoader.StreamFactory {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\stream\HttpUriLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */