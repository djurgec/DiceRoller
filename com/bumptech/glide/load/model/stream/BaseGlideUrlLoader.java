package com.bumptech.glide.load.model.stream;

import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseGlideUrlLoader<Model> implements ModelLoader<Model, InputStream> {
  private final ModelLoader<GlideUrl, InputStream> concreteLoader;
  
  private final ModelCache<Model, GlideUrl> modelCache;
  
  protected BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> paramModelLoader) {
    this(paramModelLoader, null);
  }
  
  protected BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> paramModelLoader, ModelCache<Model, GlideUrl> paramModelCache) {
    this.concreteLoader = paramModelLoader;
    this.modelCache = paramModelCache;
  }
  
  private static List<Key> getAlternateKeys(Collection<String> paramCollection) {
    ArrayList<GlideUrl> arrayList = new ArrayList(paramCollection.size());
    Iterator<String> iterator = paramCollection.iterator();
    while (iterator.hasNext())
      arrayList.add(new GlideUrl(iterator.next())); 
    return (List)arrayList;
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    GlideUrl glideUrl1 = null;
    ModelCache<Model, GlideUrl> modelCache = this.modelCache;
    if (modelCache != null)
      glideUrl1 = (GlideUrl)modelCache.get(paramModel, paramInt1, paramInt2); 
    GlideUrl glideUrl2 = glideUrl1;
    if (glideUrl1 == null) {
      String str = getUrl(paramModel, paramInt1, paramInt2, paramOptions);
      if (TextUtils.isEmpty(str))
        return null; 
      GlideUrl glideUrl = new GlideUrl(str, getHeaders(paramModel, paramInt1, paramInt2, paramOptions));
      ModelCache<Model, GlideUrl> modelCache1 = this.modelCache;
      glideUrl2 = glideUrl;
      if (modelCache1 != null) {
        modelCache1.put(paramModel, paramInt1, paramInt2, glideUrl);
        glideUrl2 = glideUrl;
      } 
    } 
    List<String> list = getAlternateUrls(paramModel, paramInt1, paramInt2, paramOptions);
    ModelLoader.LoadData<InputStream> loadData = this.concreteLoader.buildLoadData(glideUrl2, paramInt1, paramInt2, paramOptions);
    return (loadData == null || list.isEmpty()) ? loadData : new ModelLoader.LoadData(loadData.sourceKey, getAlternateKeys(list), loadData.fetcher);
  }
  
  protected List<String> getAlternateUrls(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    return Collections.emptyList();
  }
  
  protected Headers getHeaders(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    return Headers.DEFAULT;
  }
  
  protected abstract String getUrl(Model paramModel, int paramInt1, int paramInt2, Options paramOptions);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\stream\BaseGlideUrlLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */