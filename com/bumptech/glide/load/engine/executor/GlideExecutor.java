package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class GlideExecutor implements ExecutorService {
  private static final String DEFAULT_ANIMATION_EXECUTOR_NAME = "animation";
  
  private static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
  
  private static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;
  
  private static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
  
  private static final String DEFAULT_SOURCE_UNLIMITED_EXECUTOR_NAME = "source-unlimited";
  
  private static final long KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
  
  private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
  
  private static final String TAG = "GlideExecutor";
  
  private static volatile int bestThreadCount;
  
  private final ExecutorService delegate;
  
  GlideExecutor(ExecutorService paramExecutorService) {
    this.delegate = paramExecutorService;
  }
  
  public static int calculateBestThreadCount() {
    if (bestThreadCount == 0)
      bestThreadCount = Math.min(4, RuntimeCompat.availableProcessors()); 
    return bestThreadCount;
  }
  
  public static Builder newAnimationBuilder() {
    boolean bool;
    if (calculateBestThreadCount() >= 4) {
      bool = true;
    } else {
      bool = true;
    } 
    return (new Builder(true)).setThreadCount(bool).setName("animation");
  }
  
  public static GlideExecutor newAnimationExecutor() {
    return newAnimationBuilder().build();
  }
  
  @Deprecated
  public static GlideExecutor newAnimationExecutor(int paramInt, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newAnimationBuilder().setThreadCount(paramInt).setUncaughtThrowableStrategy(paramUncaughtThrowableStrategy).build();
  }
  
  public static Builder newDiskCacheBuilder() {
    return (new Builder(true)).setThreadCount(1).setName("disk-cache");
  }
  
  public static GlideExecutor newDiskCacheExecutor() {
    return newDiskCacheBuilder().build();
  }
  
  @Deprecated
  public static GlideExecutor newDiskCacheExecutor(int paramInt, String paramString, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newDiskCacheBuilder().setThreadCount(paramInt).setName(paramString).setUncaughtThrowableStrategy(paramUncaughtThrowableStrategy).build();
  }
  
  @Deprecated
  public static GlideExecutor newDiskCacheExecutor(UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newDiskCacheBuilder().setUncaughtThrowableStrategy(paramUncaughtThrowableStrategy).build();
  }
  
  public static Builder newSourceBuilder() {
    return (new Builder(false)).setThreadCount(calculateBestThreadCount()).setName("source");
  }
  
  public static GlideExecutor newSourceExecutor() {
    return newSourceBuilder().build();
  }
  
  @Deprecated
  public static GlideExecutor newSourceExecutor(int paramInt, String paramString, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newSourceBuilder().setThreadCount(paramInt).setName(paramString).setUncaughtThrowableStrategy(paramUncaughtThrowableStrategy).build();
  }
  
  @Deprecated
  public static GlideExecutor newSourceExecutor(UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newSourceBuilder().setUncaughtThrowableStrategy(paramUncaughtThrowableStrategy).build();
  }
  
  public static GlideExecutor newUnlimitedSourceExecutor() {
    return new GlideExecutor(new ThreadPoolExecutor(0, 2147483647, KEEP_ALIVE_TIME_MS, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), new DefaultThreadFactory("source-unlimited", UncaughtThrowableStrategy.DEFAULT, false)));
  }
  
  public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return this.delegate.awaitTermination(paramLong, paramTimeUnit);
  }
  
  public void execute(Runnable paramRunnable) {
    this.delegate.execute(paramRunnable);
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection) throws InterruptedException {
    return this.delegate.invokeAll(paramCollection);
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return this.delegate.invokeAll(paramCollection, paramLong, paramTimeUnit);
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection) throws InterruptedException, ExecutionException {
    return this.delegate.invokeAny(paramCollection);
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.delegate.invokeAny(paramCollection, paramLong, paramTimeUnit);
  }
  
  public boolean isShutdown() {
    return this.delegate.isShutdown();
  }
  
  public boolean isTerminated() {
    return this.delegate.isTerminated();
  }
  
  public void shutdown() {
    this.delegate.shutdown();
  }
  
  public List<Runnable> shutdownNow() {
    return this.delegate.shutdownNow();
  }
  
  public Future<?> submit(Runnable paramRunnable) {
    return this.delegate.submit(paramRunnable);
  }
  
  public <T> Future<T> submit(Runnable paramRunnable, T paramT) {
    return this.delegate.submit(paramRunnable, paramT);
  }
  
  public <T> Future<T> submit(Callable<T> paramCallable) {
    return this.delegate.submit(paramCallable);
  }
  
  public String toString() {
    return this.delegate.toString();
  }
  
  public static final class Builder {
    public static final long NO_THREAD_TIMEOUT = 0L;
    
    private int corePoolSize;
    
    private int maximumPoolSize;
    
    private String name;
    
    private final boolean preventNetworkOperations;
    
    private long threadTimeoutMillis;
    
    private GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy = GlideExecutor.UncaughtThrowableStrategy.DEFAULT;
    
    Builder(boolean param1Boolean) {
      this.preventNetworkOperations = param1Boolean;
    }
    
    public GlideExecutor build() {
      if (!TextUtils.isEmpty(this.name)) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.threadTimeoutMillis, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(), new GlideExecutor.DefaultThreadFactory(this.name, this.uncaughtThrowableStrategy, this.preventNetworkOperations));
        if (this.threadTimeoutMillis != 0L)
          threadPoolExecutor.allowCoreThreadTimeOut(true); 
        return new GlideExecutor(threadPoolExecutor);
      } 
      throw new IllegalArgumentException("Name must be non-null and non-empty, but given: " + this.name);
    }
    
    public Builder setName(String param1String) {
      this.name = param1String;
      return this;
    }
    
    public Builder setThreadCount(int param1Int) {
      this.corePoolSize = param1Int;
      this.maximumPoolSize = param1Int;
      return this;
    }
    
    public Builder setThreadTimeoutMillis(long param1Long) {
      this.threadTimeoutMillis = param1Long;
      return this;
    }
    
    public Builder setUncaughtThrowableStrategy(GlideExecutor.UncaughtThrowableStrategy param1UncaughtThrowableStrategy) {
      this.uncaughtThrowableStrategy = param1UncaughtThrowableStrategy;
      return this;
    }
  }
  
  private static final class DefaultThreadFactory implements ThreadFactory {
    private static final int DEFAULT_PRIORITY = 9;
    
    private final String name;
    
    final boolean preventNetworkOperations;
    
    private int threadNum;
    
    final GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;
    
    DefaultThreadFactory(String param1String, GlideExecutor.UncaughtThrowableStrategy param1UncaughtThrowableStrategy, boolean param1Boolean) {
      this.name = param1String;
      this.uncaughtThrowableStrategy = param1UncaughtThrowableStrategy;
      this.preventNetworkOperations = param1Boolean;
    }
    
    public Thread newThread(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new com/bumptech/glide/load/engine/executor/GlideExecutor$DefaultThreadFactory$1
      //   5: astore_2
      //   6: new java/lang/StringBuilder
      //   9: astore_3
      //   10: aload_3
      //   11: invokespecial <init> : ()V
      //   14: aload_2
      //   15: aload_0
      //   16: aload_1
      //   17: aload_3
      //   18: ldc 'glide-'
      //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   23: aload_0
      //   24: getfield name : Ljava/lang/String;
      //   27: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   30: ldc '-thread-'
      //   32: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   35: aload_0
      //   36: getfield threadNum : I
      //   39: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   42: invokevirtual toString : ()Ljava/lang/String;
      //   45: invokespecial <init> : (Lcom/bumptech/glide/load/engine/executor/GlideExecutor$DefaultThreadFactory;Ljava/lang/Runnable;Ljava/lang/String;)V
      //   48: aload_0
      //   49: aload_0
      //   50: getfield threadNum : I
      //   53: iconst_1
      //   54: iadd
      //   55: putfield threadNum : I
      //   58: aload_0
      //   59: monitorexit
      //   60: aload_2
      //   61: areturn
      //   62: astore_1
      //   63: aload_0
      //   64: monitorexit
      //   65: aload_1
      //   66: athrow
      // Exception table:
      //   from	to	target	type
      //   2	58	62	finally
    }
  }
  
  class null extends Thread {
    final GlideExecutor.DefaultThreadFactory this$0;
    
    null(Runnable param1Runnable, String param1String) {
      super(param1Runnable, param1String);
    }
    
    public void run() {
      Process.setThreadPriority(9);
      if (this.this$0.preventNetworkOperations)
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder()).detectNetwork().penaltyDeath().build()); 
      try {
        super.run();
      } finally {
        Exception exception = null;
      } 
    }
  }
  
  public static interface UncaughtThrowableStrategy {
    public static final UncaughtThrowableStrategy DEFAULT;
    
    public static final UncaughtThrowableStrategy IGNORE = new UncaughtThrowableStrategy() {
        public void handle(Throwable param2Throwable) {}
      };
    
    public static final UncaughtThrowableStrategy LOG;
    
    public static final UncaughtThrowableStrategy THROW = new UncaughtThrowableStrategy() {
        public void handle(Throwable param2Throwable) {
          if (param2Throwable == null)
            return; 
          throw new RuntimeException("Request threw uncaught throwable", param2Throwable);
        }
      };
    
    static {
      DEFAULT = uncaughtThrowableStrategy;
    }
    
    void handle(Throwable param1Throwable);
    
    static {
      UncaughtThrowableStrategy uncaughtThrowableStrategy = new UncaughtThrowableStrategy() {
          public void handle(Throwable param2Throwable) {
            if (param2Throwable != null && Log.isLoggable("GlideExecutor", 6))
              Log.e("GlideExecutor", "Request threw uncaught throwable", param2Throwable); 
          }
        };
      LOG = uncaughtThrowableStrategy;
    }
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {}
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {
      if (param1Throwable != null && Log.isLoggable("GlideExecutor", 6))
        Log.e("GlideExecutor", "Request threw uncaught throwable", param1Throwable); 
    }
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {
      if (param1Throwable == null)
        return; 
      throw new RuntimeException("Request threw uncaught throwable", param1Throwable);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\executor\GlideExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */