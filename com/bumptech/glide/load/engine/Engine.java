package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
  private static final int JOB_POOL_SIZE = 150;
  
  private static final String TAG = "Engine";
  
  private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable("Engine", 2);
  
  private final ActiveResources activeResources;
  
  private final MemoryCache cache;
  
  private final DecodeJobFactory decodeJobFactory;
  
  private final LazyDiskCacheProvider diskCacheProvider;
  
  private final EngineJobFactory engineJobFactory;
  
  private final Jobs jobs;
  
  private final EngineKeyFactory keyFactory;
  
  private final ResourceRecycler resourceRecycler;
  
  Engine(MemoryCache paramMemoryCache, DiskCache.Factory paramFactory, GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, Jobs paramJobs, EngineKeyFactory paramEngineKeyFactory, ActiveResources paramActiveResources, EngineJobFactory paramEngineJobFactory, DecodeJobFactory paramDecodeJobFactory, ResourceRecycler paramResourceRecycler, boolean paramBoolean) {
    ActiveResources activeResources;
    DecodeJobFactory decodeJobFactory;
    ResourceRecycler resourceRecycler;
    this.cache = paramMemoryCache;
    LazyDiskCacheProvider lazyDiskCacheProvider = new LazyDiskCacheProvider(paramFactory);
    this.diskCacheProvider = lazyDiskCacheProvider;
    if (paramActiveResources == null) {
      activeResources = new ActiveResources(paramBoolean);
    } else {
      activeResources = paramActiveResources;
    } 
    this.activeResources = activeResources;
    activeResources.setListener(this);
    if (paramEngineKeyFactory == null)
      paramEngineKeyFactory = new EngineKeyFactory(); 
    this.keyFactory = paramEngineKeyFactory;
    if (paramJobs == null)
      paramJobs = new Jobs(); 
    this.jobs = paramJobs;
    if (paramEngineJobFactory == null)
      paramEngineJobFactory = new EngineJobFactory(paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, this, this); 
    this.engineJobFactory = paramEngineJobFactory;
    if (paramDecodeJobFactory == null) {
      decodeJobFactory = new DecodeJobFactory(lazyDiskCacheProvider);
    } else {
      decodeJobFactory = paramDecodeJobFactory;
    } 
    this.decodeJobFactory = decodeJobFactory;
    if (paramResourceRecycler == null) {
      resourceRecycler = new ResourceRecycler();
    } else {
      resourceRecycler = paramResourceRecycler;
    } 
    this.resourceRecycler = resourceRecycler;
    paramMemoryCache.setResourceRemovedListener(this);
  }
  
  public Engine(MemoryCache paramMemoryCache, DiskCache.Factory paramFactory, GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, boolean paramBoolean) {
    this(paramMemoryCache, paramFactory, paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, null, null, null, null, null, null, paramBoolean);
  }
  
  private EngineResource<?> getEngineResourceFromCache(Key paramKey) {
    EngineResource<?> engineResource;
    Resource<?> resource = this.cache.remove(paramKey);
    if (resource == null) {
      paramKey = null;
    } else if (resource instanceof EngineResource) {
      engineResource = (EngineResource)resource;
    } else {
      engineResource = new EngineResource(resource, true, true, (Key)engineResource, this);
    } 
    return engineResource;
  }
  
  private EngineResource<?> loadFromActiveResources(Key paramKey) {
    EngineResource<?> engineResource = this.activeResources.get(paramKey);
    if (engineResource != null)
      engineResource.acquire(); 
    return engineResource;
  }
  
  private EngineResource<?> loadFromCache(Key paramKey) {
    EngineResource<?> engineResource = getEngineResourceFromCache(paramKey);
    if (engineResource != null) {
      engineResource.acquire();
      this.activeResources.activate(paramKey, engineResource);
    } 
    return engineResource;
  }
  
  private EngineResource<?> loadFromMemory(EngineKey paramEngineKey, boolean paramBoolean, long paramLong) {
    if (!paramBoolean)
      return null; 
    EngineResource<?> engineResource = loadFromActiveResources(paramEngineKey);
    if (engineResource != null) {
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Loaded resource from active resources", paramLong, paramEngineKey); 
      return engineResource;
    } 
    engineResource = loadFromCache(paramEngineKey);
    if (engineResource != null) {
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Loaded resource from cache", paramLong, paramEngineKey); 
      return engineResource;
    } 
    return null;
  }
  
  private static void logWithTimeAndKey(String paramString, long paramLong, Key paramKey) {
    Log.v("Engine", paramString + " in " + LogTime.getElapsedMillis(paramLong) + "ms, key: " + paramKey);
  }
  
  private <R> LoadStatus waitForExistingOrStartNewJob(GlideContext paramGlideContext, Object paramObject, Key paramKey, int paramInt1, int paramInt2, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, DiskCacheStrategy paramDiskCacheStrategy, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, Options paramOptions, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, ResourceCallback paramResourceCallback, Executor paramExecutor, EngineKey paramEngineKey, long paramLong) {
    EngineJob<?> engineJob = this.jobs.get(paramEngineKey, paramBoolean6);
    if (engineJob != null) {
      engineJob.addCallback(paramResourceCallback, paramExecutor);
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Added to existing load", paramLong, paramEngineKey); 
      return new LoadStatus(paramResourceCallback, engineJob);
    } 
    engineJob = this.engineJobFactory.build(paramEngineKey, paramBoolean3, paramBoolean4, paramBoolean5, paramBoolean6);
    DecodeJob<R> decodeJob = this.decodeJobFactory.build(paramGlideContext, paramObject, paramEngineKey, paramKey, paramInt1, paramInt2, paramClass, paramClass1, paramPriority, paramDiskCacheStrategy, paramMap, paramBoolean1, paramBoolean2, paramBoolean6, paramOptions, (DecodeJob.Callback)engineJob);
    this.jobs.put(paramEngineKey, engineJob);
    engineJob.addCallback(paramResourceCallback, paramExecutor);
    engineJob.start(decodeJob);
    if (VERBOSE_IS_LOGGABLE)
      logWithTimeAndKey("Started new load", paramLong, paramEngineKey); 
    return new LoadStatus(paramResourceCallback, engineJob);
  }
  
  public void clearDiskCache() {
    this.diskCacheProvider.getDiskCache().clear();
  }
  
  public <R> LoadStatus load(GlideContext paramGlideContext, Object paramObject, Key paramKey, int paramInt1, int paramInt2, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, DiskCacheStrategy paramDiskCacheStrategy, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, Options paramOptions, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, ResourceCallback paramResourceCallback, Executor paramExecutor) {
    long l;
    if (VERBOSE_IS_LOGGABLE) {
      l = LogTime.getLogTime();
    } else {
      l = 0L;
    } 
    EngineKey engineKey = this.keyFactory.buildKey(paramObject, paramKey, paramInt1, paramInt2, paramMap, paramClass, paramClass1, paramOptions);
    /* monitor enter ThisExpression{ObjectType{com/bumptech/glide/load/engine/Engine}} */
    try {
      EngineResource<?> engineResource = loadFromMemory(engineKey, paramBoolean3, l);
      if (engineResource == null) {
        try {
          LoadStatus loadStatus = waitForExistingOrStartNewJob(paramGlideContext, paramObject, paramKey, paramInt1, paramInt2, paramClass, paramClass1, paramPriority, paramDiskCacheStrategy, paramMap, paramBoolean1, paramBoolean2, paramOptions, paramBoolean3, paramBoolean4, paramBoolean5, paramBoolean6, paramResourceCallback, paramExecutor, engineKey, l);
          /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/load/engine/Engine}} */
          return loadStatus;
        } finally {}
      } else {
        /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/load/engine/Engine}} */
        paramResourceCallback.onResourceReady(engineResource, DataSource.MEMORY_CACHE, false);
        return null;
      } 
    } finally {}
    while (true) {
      try {
        /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/load/engine/Engine}} */
        throw paramGlideContext;
      } finally {
        paramGlideContext = null;
      } 
    } 
  }
  
  public void onEngineJobCancelled(EngineJob<?> paramEngineJob, Key paramKey) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield jobs : Lcom/bumptech/glide/load/engine/Jobs;
    //   6: aload_2
    //   7: aload_1
    //   8: invokevirtual removeIfCurrent : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineJob;)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void onEngineJobComplete(EngineJob<?> paramEngineJob, Key paramKey, EngineResource<?> paramEngineResource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_3
    //   3: ifnull -> 22
    //   6: aload_3
    //   7: invokevirtual isMemoryCacheable : ()Z
    //   10: ifeq -> 22
    //   13: aload_0
    //   14: getfield activeResources : Lcom/bumptech/glide/load/engine/ActiveResources;
    //   17: aload_2
    //   18: aload_3
    //   19: invokevirtual activate : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   22: aload_0
    //   23: getfield jobs : Lcom/bumptech/glide/load/engine/Jobs;
    //   26: aload_2
    //   27: aload_1
    //   28: invokevirtual removeIfCurrent : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineJob;)V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   6	22	34	finally
    //   22	31	34	finally
  }
  
  public void onResourceReleased(Key paramKey, EngineResource<?> paramEngineResource) {
    this.activeResources.deactivate(paramKey);
    if (paramEngineResource.isMemoryCacheable()) {
      this.cache.put(paramKey, paramEngineResource);
    } else {
      this.resourceRecycler.recycle(paramEngineResource, false);
    } 
  }
  
  public void onResourceRemoved(Resource<?> paramResource) {
    this.resourceRecycler.recycle(paramResource, true);
  }
  
  public void release(Resource<?> paramResource) {
    if (paramResource instanceof EngineResource) {
      ((EngineResource)paramResource).release();
      return;
    } 
    throw new IllegalArgumentException("Cannot release anything but an EngineResource");
  }
  
  public void shutdown() {
    this.engineJobFactory.shutdown();
    this.diskCacheProvider.clearDiskCacheIfCreated();
    this.activeResources.shutdown();
  }
  
  static class DecodeJobFactory {
    private int creationOrder;
    
    final DecodeJob.DiskCacheProvider diskCacheProvider;
    
    final Pools.Pool<DecodeJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<DecodeJob<?>>() {
          final Engine.DecodeJobFactory this$0;
          
          public DecodeJob<?> create() {
            return new DecodeJob(Engine.DecodeJobFactory.this.diskCacheProvider, Engine.DecodeJobFactory.this.pool);
          }
        });
    
    DecodeJobFactory(DecodeJob.DiskCacheProvider param1DiskCacheProvider) {
      this.diskCacheProvider = param1DiskCacheProvider;
    }
    
    <R> DecodeJob<R> build(GlideContext param1GlideContext, Object param1Object, EngineKey param1EngineKey, Key param1Key, int param1Int1, int param1Int2, Class<?> param1Class, Class<R> param1Class1, Priority param1Priority, DiskCacheStrategy param1DiskCacheStrategy, Map<Class<?>, Transformation<?>> param1Map, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, Options param1Options, DecodeJob.Callback<R> param1Callback) {
      DecodeJob<R> decodeJob = (DecodeJob)Preconditions.checkNotNull(this.pool.acquire());
      int i = this.creationOrder;
      this.creationOrder = i + 1;
      return decodeJob.init(param1GlideContext, param1Object, param1EngineKey, param1Key, param1Int1, param1Int2, param1Class, param1Class1, param1Priority, param1DiskCacheStrategy, param1Map, param1Boolean1, param1Boolean2, param1Boolean3, param1Options, param1Callback, i);
    }
  }
  
  class null implements FactoryPools.Factory<DecodeJob<?>> {
    final Engine.DecodeJobFactory this$0;
    
    public DecodeJob<?> create() {
      return new DecodeJob(this.this$0.diskCacheProvider, this.this$0.pool);
    }
  }
  
  static class EngineJobFactory {
    final GlideExecutor animationExecutor;
    
    final GlideExecutor diskCacheExecutor;
    
    final EngineJobListener engineJobListener;
    
    final Pools.Pool<EngineJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<EngineJob<?>>() {
          final Engine.EngineJobFactory this$0;
          
          public EngineJob<?> create() {
            return new EngineJob(Engine.EngineJobFactory.this.diskCacheExecutor, Engine.EngineJobFactory.this.sourceExecutor, Engine.EngineJobFactory.this.sourceUnlimitedExecutor, Engine.EngineJobFactory.this.animationExecutor, Engine.EngineJobFactory.this.engineJobListener, Engine.EngineJobFactory.this.resourceListener, Engine.EngineJobFactory.this.pool);
          }
        });
    
    final EngineResource.ResourceListener resourceListener;
    
    final GlideExecutor sourceExecutor;
    
    final GlideExecutor sourceUnlimitedExecutor;
    
    EngineJobFactory(GlideExecutor param1GlideExecutor1, GlideExecutor param1GlideExecutor2, GlideExecutor param1GlideExecutor3, GlideExecutor param1GlideExecutor4, EngineJobListener param1EngineJobListener, EngineResource.ResourceListener param1ResourceListener) {
      this.diskCacheExecutor = param1GlideExecutor1;
      this.sourceExecutor = param1GlideExecutor2;
      this.sourceUnlimitedExecutor = param1GlideExecutor3;
      this.animationExecutor = param1GlideExecutor4;
      this.engineJobListener = param1EngineJobListener;
      this.resourceListener = param1ResourceListener;
    }
    
    <R> EngineJob<R> build(Key param1Key, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, boolean param1Boolean4) {
      return ((EngineJob<R>)Preconditions.checkNotNull(this.pool.acquire())).init(param1Key, param1Boolean1, param1Boolean2, param1Boolean3, param1Boolean4);
    }
    
    void shutdown() {
      Executors.shutdownAndAwaitTermination((ExecutorService)this.diskCacheExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.sourceExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.sourceUnlimitedExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.animationExecutor);
    }
  }
  
  class null implements FactoryPools.Factory<EngineJob<?>> {
    final Engine.EngineJobFactory this$0;
    
    public EngineJob<?> create() {
      return new EngineJob(this.this$0.diskCacheExecutor, this.this$0.sourceExecutor, this.this$0.sourceUnlimitedExecutor, this.this$0.animationExecutor, this.this$0.engineJobListener, this.this$0.resourceListener, this.this$0.pool);
    }
  }
  
  private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
    private volatile DiskCache diskCache;
    
    private final DiskCache.Factory factory;
    
    LazyDiskCacheProvider(DiskCache.Factory param1Factory) {
      this.factory = param1Factory;
    }
    
    void clearDiskCacheIfCreated() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   18: invokeinterface clear : ()V
      //   23: aload_0
      //   24: monitorexit
      //   25: return
      //   26: astore_1
      //   27: aload_0
      //   28: monitorexit
      //   29: aload_1
      //   30: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	26	finally
      //   14	23	26	finally
    }
    
    public DiskCache getDiskCache() {
      // Byte code:
      //   0: aload_0
      //   1: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   4: ifnonnull -> 59
      //   7: aload_0
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   13: ifnonnull -> 29
      //   16: aload_0
      //   17: aload_0
      //   18: getfield factory : Lcom/bumptech/glide/load/engine/cache/DiskCache$Factory;
      //   21: invokeinterface build : ()Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   26: putfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   29: aload_0
      //   30: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   33: ifnonnull -> 49
      //   36: new com/bumptech/glide/load/engine/cache/DiskCacheAdapter
      //   39: astore_1
      //   40: aload_1
      //   41: invokespecial <init> : ()V
      //   44: aload_0
      //   45: aload_1
      //   46: putfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   49: aload_0
      //   50: monitorexit
      //   51: goto -> 59
      //   54: astore_1
      //   55: aload_0
      //   56: monitorexit
      //   57: aload_1
      //   58: athrow
      //   59: aload_0
      //   60: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   63: areturn
      // Exception table:
      //   from	to	target	type
      //   9	29	54	finally
      //   29	49	54	finally
      //   49	51	54	finally
      //   55	57	54	finally
    }
  }
  
  public class LoadStatus {
    private final ResourceCallback cb;
    
    private final EngineJob<?> engineJob;
    
    final Engine this$0;
    
    LoadStatus(ResourceCallback param1ResourceCallback, EngineJob<?> param1EngineJob) {
      this.cb = param1ResourceCallback;
      this.engineJob = param1EngineJob;
    }
    
    public void cancel() {
      synchronized (Engine.this) {
        this.engineJob.removeCallback(this.cb);
        return;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\Engine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */