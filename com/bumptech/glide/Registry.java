package com.bumptech.glide;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Registry {
  private static final String BUCKET_APPEND_ALL = "legacy_append";
  
  public static final String BUCKET_BITMAP = "Bitmap";
  
  public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
  
  public static final String BUCKET_GIF = "Gif";
  
  private static final String BUCKET_PREPEND_ALL = "legacy_prepend_all";
  
  private final DataRewinderRegistry dataRewinderRegistry;
  
  private final ResourceDecoderRegistry decoderRegistry;
  
  private final EncoderRegistry encoderRegistry;
  
  private final ImageHeaderParserRegistry imageHeaderParserRegistry;
  
  private final LoadPathCache loadPathCache = new LoadPathCache();
  
  private final ModelLoaderRegistry modelLoaderRegistry;
  
  private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
  
  private final ResourceEncoderRegistry resourceEncoderRegistry;
  
  private final Pools.Pool<List<Throwable>> throwableListPool;
  
  private final TranscoderRegistry transcoderRegistry;
  
  public Registry() {
    Pools.Pool<List<Throwable>> pool = FactoryPools.threadSafeList();
    this.throwableListPool = pool;
    this.modelLoaderRegistry = new ModelLoaderRegistry(pool);
    this.encoderRegistry = new EncoderRegistry();
    this.decoderRegistry = new ResourceDecoderRegistry();
    this.resourceEncoderRegistry = new ResourceEncoderRegistry();
    this.dataRewinderRegistry = new DataRewinderRegistry();
    this.transcoderRegistry = new TranscoderRegistry();
    this.imageHeaderParserRegistry = new ImageHeaderParserRegistry();
    setResourceDecoderBucketPriorityList(Arrays.asList(new String[] { "Gif", "Bitmap", "BitmapDrawable" }));
  }
  
  private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(Class<Data> paramClass, Class<TResource> paramClass1, Class<Transcode> paramClass2) {
    ArrayList<DecodePath> arrayList = new ArrayList();
    for (Class clazz : this.decoderRegistry.getResourceClasses(paramClass, paramClass1)) {
      for (Class<TResource> paramClass1 : (Iterable<Class<TResource>>)this.transcoderRegistry.getTranscodeClasses(clazz, paramClass2))
        arrayList.add(new DecodePath(paramClass, clazz, paramClass1, this.decoderRegistry.getDecoders(paramClass, clazz), this.transcoderRegistry.get(clazz, paramClass1), this.throwableListPool)); 
    } 
    return (List)arrayList;
  }
  
  public <Data> Registry append(Class<Data> paramClass, Encoder<Data> paramEncoder) {
    this.encoderRegistry.append(paramClass, paramEncoder);
    return this;
  }
  
  public <TResource> Registry append(Class<TResource> paramClass, ResourceEncoder<TResource> paramResourceEncoder) {
    this.resourceEncoderRegistry.append(paramClass, paramResourceEncoder);
    return this;
  }
  
  public <Data, TResource> Registry append(Class<Data> paramClass, Class<TResource> paramClass1, ResourceDecoder<Data, TResource> paramResourceDecoder) {
    append("legacy_append", paramClass, paramClass1, paramResourceDecoder);
    return this;
  }
  
  public <Model, Data> Registry append(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<Model, Data> paramModelLoaderFactory) {
    this.modelLoaderRegistry.append(paramClass, paramClass1, paramModelLoaderFactory);
    return this;
  }
  
  public <Data, TResource> Registry append(String paramString, Class<Data> paramClass, Class<TResource> paramClass1, ResourceDecoder<Data, TResource> paramResourceDecoder) {
    this.decoderRegistry.append(paramString, paramResourceDecoder, paramClass, paramClass1);
    return this;
  }
  
  public List<ImageHeaderParser> getImageHeaderParsers() {
    List<ImageHeaderParser> list = this.imageHeaderParserRegistry.getParsers();
    if (!list.isEmpty())
      return list; 
    throw new NoImageHeaderParserException();
  }
  
  public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(Class<Data> paramClass, Class<TResource> paramClass1, Class<Transcode> paramClass2) {
    LoadPath loadPath1 = this.loadPathCache.get(paramClass, paramClass1, paramClass2);
    if (this.loadPathCache.isEmptyLoadPath(loadPath1))
      return null; 
    LoadPath<Data, TResource, Transcode> loadPath = loadPath1;
    if (loadPath1 == null) {
      List<DecodePath<Data, TResource, Transcode>> list = getDecodePaths(paramClass, paramClass1, paramClass2);
      if (list.isEmpty()) {
        list = null;
      } else {
        loadPath = new LoadPath(paramClass, paramClass1, paramClass2, list, this.throwableListPool);
      } 
      this.loadPathCache.put(paramClass, paramClass1, paramClass2, loadPath);
    } 
    return loadPath;
  }
  
  public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Model paramModel) {
    return this.modelLoaderRegistry.getModelLoaders(paramModel);
  }
  
  public <Model, TResource, Transcode> List<Class<?>> getRegisteredResourceClasses(Class<Model> paramClass, Class<TResource> paramClass1, Class<Transcode> paramClass2) {
    List<Class<?>> list2 = this.modelToResourceClassCache.get(paramClass, paramClass1, paramClass2);
    List<Class<?>> list1 = list2;
    if (list2 == null) {
      list1 = new ArrayList();
      for (Class<?> clazz : (Iterable<Class<?>>)this.modelLoaderRegistry.getDataClasses(paramClass)) {
        for (Class<?> clazz : (Iterable<Class<?>>)this.decoderRegistry.getResourceClasses(clazz, paramClass1)) {
          if (!this.transcoderRegistry.getTranscodeClasses(clazz, paramClass2).isEmpty() && !list1.contains(clazz))
            list1.add(clazz); 
        } 
      } 
      this.modelToResourceClassCache.put(paramClass, paramClass1, paramClass2, Collections.unmodifiableList(list1));
    } 
    return list1;
  }
  
  public <X> ResourceEncoder<X> getResultEncoder(Resource<X> paramResource) throws NoResultEncoderAvailableException {
    ResourceEncoder<X> resourceEncoder = this.resourceEncoderRegistry.get(paramResource.getResourceClass());
    if (resourceEncoder != null)
      return resourceEncoder; 
    throw new NoResultEncoderAvailableException(paramResource.getResourceClass());
  }
  
  public <X> DataRewinder<X> getRewinder(X paramX) {
    return this.dataRewinderRegistry.build(paramX);
  }
  
  public <X> Encoder<X> getSourceEncoder(X paramX) throws NoSourceEncoderAvailableException {
    Encoder<X> encoder = this.encoderRegistry.getEncoder(paramX.getClass());
    if (encoder != null)
      return encoder; 
    throw new NoSourceEncoderAvailableException(paramX.getClass());
  }
  
  public boolean isResourceEncoderAvailable(Resource<?> paramResource) {
    boolean bool;
    if (this.resourceEncoderRegistry.get(paramResource.getResourceClass()) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public <Data> Registry prepend(Class<Data> paramClass, Encoder<Data> paramEncoder) {
    this.encoderRegistry.prepend(paramClass, paramEncoder);
    return this;
  }
  
  public <TResource> Registry prepend(Class<TResource> paramClass, ResourceEncoder<TResource> paramResourceEncoder) {
    this.resourceEncoderRegistry.prepend(paramClass, paramResourceEncoder);
    return this;
  }
  
  public <Data, TResource> Registry prepend(Class<Data> paramClass, Class<TResource> paramClass1, ResourceDecoder<Data, TResource> paramResourceDecoder) {
    prepend("legacy_prepend_all", paramClass, paramClass1, paramResourceDecoder);
    return this;
  }
  
  public <Model, Data> Registry prepend(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<Model, Data> paramModelLoaderFactory) {
    this.modelLoaderRegistry.prepend(paramClass, paramClass1, paramModelLoaderFactory);
    return this;
  }
  
  public <Data, TResource> Registry prepend(String paramString, Class<Data> paramClass, Class<TResource> paramClass1, ResourceDecoder<Data, TResource> paramResourceDecoder) {
    this.decoderRegistry.prepend(paramString, paramResourceDecoder, paramClass, paramClass1);
    return this;
  }
  
  public Registry register(ImageHeaderParser paramImageHeaderParser) {
    this.imageHeaderParserRegistry.add(paramImageHeaderParser);
    return this;
  }
  
  public Registry register(DataRewinder.Factory<?> paramFactory) {
    this.dataRewinderRegistry.register(paramFactory);
    return this;
  }
  
  @Deprecated
  public <Data> Registry register(Class<Data> paramClass, Encoder<Data> paramEncoder) {
    return append(paramClass, paramEncoder);
  }
  
  @Deprecated
  public <TResource> Registry register(Class<TResource> paramClass, ResourceEncoder<TResource> paramResourceEncoder) {
    return append(paramClass, paramResourceEncoder);
  }
  
  public <TResource, Transcode> Registry register(Class<TResource> paramClass, Class<Transcode> paramClass1, ResourceTranscoder<TResource, Transcode> paramResourceTranscoder) {
    this.transcoderRegistry.register(paramClass, paramClass1, paramResourceTranscoder);
    return this;
  }
  
  public <Model, Data> Registry replace(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    this.modelLoaderRegistry.replace(paramClass, paramClass1, paramModelLoaderFactory);
    return this;
  }
  
  public final Registry setResourceDecoderBucketPriorityList(List<String> paramList) {
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    arrayList.add("legacy_prepend_all");
    Iterator<String> iterator = paramList.iterator();
    while (iterator.hasNext())
      arrayList.add(iterator.next()); 
    arrayList.add("legacy_append");
    this.decoderRegistry.setBucketPriorityList(arrayList);
    return this;
  }
  
  public static class MissingComponentException extends RuntimeException {
    public MissingComponentException(String param1String) {
      super(param1String);
    }
  }
  
  public static final class NoImageHeaderParserException extends MissingComponentException {
    public NoImageHeaderParserException() {
      super("Failed to find image header parser.");
    }
  }
  
  public static class NoModelLoaderAvailableException extends MissingComponentException {
    public NoModelLoaderAvailableException(Class<?> param1Class1, Class<?> param1Class2) {
      super("Failed to find any ModelLoaders for model: " + param1Class1 + " and data: " + param1Class2);
    }
    
    public NoModelLoaderAvailableException(Object param1Object) {
      super("Failed to find any ModelLoaders registered for model class: " + param1Object.getClass());
    }
    
    public <M> NoModelLoaderAvailableException(M param1M, List<ModelLoader<M, ?>> param1List) {
      super("Found ModelLoaders for model class: " + param1List + ", but none that handle this specific model instance: " + param1M);
    }
  }
  
  public static class NoResultEncoderAvailableException extends MissingComponentException {
    public NoResultEncoderAvailableException(Class<?> param1Class) {
      super("Failed to find result encoder for resource class: " + param1Class + ", you may need to consider registering a new Encoder for the requested type or DiskCacheStrategy.DATA/DiskCacheStrategy.NONE if caching your transformed resource is unnecessary.");
    }
  }
  
  public static class NoSourceEncoderAvailableException extends MissingComponentException {
    public NoSourceEncoderAvailableException(Class<?> param1Class) {
      super("Failed to find source encoder for data class: " + param1Class);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\Registry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */