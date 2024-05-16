package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
  private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
  
  private final GlideExecutor animationExecutor;
  
  final ResourceCallbacksAndExecutors cbs = new ResourceCallbacksAndExecutors();
  
  DataSource dataSource;
  
  private DecodeJob<R> decodeJob;
  
  private final GlideExecutor diskCacheExecutor;
  
  private final EngineJobListener engineJobListener;
  
  EngineResource<?> engineResource;
  
  private final EngineResourceFactory engineResourceFactory;
  
  GlideException exception;
  
  private boolean hasLoadFailed;
  
  private boolean hasResource;
  
  private boolean isCacheable;
  
  private volatile boolean isCancelled;
  
  private boolean isLoadedFromAlternateCacheKey;
  
  private Key key;
  
  private boolean onlyRetrieveFromCache;
  
  private final AtomicInteger pendingCallbacks = new AtomicInteger();
  
  private final Pools.Pool<EngineJob<?>> pool;
  
  private Resource<?> resource;
  
  private final EngineResource.ResourceListener resourceListener;
  
  private final GlideExecutor sourceExecutor;
  
  private final GlideExecutor sourceUnlimitedExecutor;
  
  private final StateVerifier stateVerifier = StateVerifier.newInstance();
  
  private boolean useAnimationPool;
  
  private boolean useUnlimitedSourceGeneratorPool;
  
  EngineJob(GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, EngineJobListener paramEngineJobListener, EngineResource.ResourceListener paramResourceListener, Pools.Pool<EngineJob<?>> paramPool) {
    this(paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, paramEngineJobListener, paramResourceListener, paramPool, DEFAULT_FACTORY);
  }
  
  EngineJob(GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, EngineJobListener paramEngineJobListener, EngineResource.ResourceListener paramResourceListener, Pools.Pool<EngineJob<?>> paramPool, EngineResourceFactory paramEngineResourceFactory) {
    this.diskCacheExecutor = paramGlideExecutor1;
    this.sourceExecutor = paramGlideExecutor2;
    this.sourceUnlimitedExecutor = paramGlideExecutor3;
    this.animationExecutor = paramGlideExecutor4;
    this.engineJobListener = paramEngineJobListener;
    this.resourceListener = paramResourceListener;
    this.pool = paramPool;
    this.engineResourceFactory = paramEngineResourceFactory;
  }
  
  private GlideExecutor getActiveSourceExecutor() {
    GlideExecutor glideExecutor;
    if (this.useUnlimitedSourceGeneratorPool) {
      glideExecutor = this.sourceUnlimitedExecutor;
    } else if (this.useAnimationPool) {
      glideExecutor = this.animationExecutor;
    } else {
      glideExecutor = this.sourceExecutor;
    } 
    return glideExecutor;
  }
  
  private boolean isDone() {
    return (this.hasLoadFailed || this.hasResource || this.isCancelled);
  }
  
  private void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield key : Lcom/bumptech/glide/load/Key;
    //   6: ifnull -> 88
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: invokevirtual clear : ()V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield key : Lcom/bumptech/glide/load/Key;
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   26: aload_0
    //   27: aconst_null
    //   28: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   31: aload_0
    //   32: iconst_0
    //   33: putfield hasLoadFailed : Z
    //   36: aload_0
    //   37: iconst_0
    //   38: putfield isCancelled : Z
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield hasResource : Z
    //   46: aload_0
    //   47: iconst_0
    //   48: putfield isLoadedFromAlternateCacheKey : Z
    //   51: aload_0
    //   52: getfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   55: iconst_0
    //   56: invokevirtual release : (Z)V
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   64: aload_0
    //   65: aconst_null
    //   66: putfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   69: aload_0
    //   70: aconst_null
    //   71: putfield dataSource : Lcom/bumptech/glide/load/DataSource;
    //   74: aload_0
    //   75: getfield pool : Landroidx/core/util/Pools$Pool;
    //   78: aload_0
    //   79: invokeinterface release : (Ljava/lang/Object;)Z
    //   84: pop
    //   85: aload_0
    //   86: monitorexit
    //   87: return
    //   88: new java/lang/IllegalArgumentException
    //   91: astore_1
    //   92: aload_1
    //   93: invokespecial <init> : ()V
    //   96: aload_1
    //   97: athrow
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	85	98	finally
    //   88	98	98	finally
  }
  
  void addCallback(ResourceCallback paramResourceCallback, Executor paramExecutor) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: aload_1
    //   14: aload_2
    //   15: invokevirtual add : (Lcom/bumptech/glide/request/ResourceCallback;Ljava/util/concurrent/Executor;)V
    //   18: aload_0
    //   19: getfield hasResource : Z
    //   22: istore #4
    //   24: iconst_1
    //   25: istore_3
    //   26: iload #4
    //   28: ifeq -> 59
    //   31: aload_0
    //   32: iconst_1
    //   33: invokevirtual incrementPendingCallbacks : (I)V
    //   36: new com/bumptech/glide/load/engine/EngineJob$CallResourceReady
    //   39: astore #5
    //   41: aload #5
    //   43: aload_0
    //   44: aload_1
    //   45: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   48: aload_2
    //   49: aload #5
    //   51: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   56: goto -> 112
    //   59: aload_0
    //   60: getfield hasLoadFailed : Z
    //   63: ifeq -> 94
    //   66: aload_0
    //   67: iconst_1
    //   68: invokevirtual incrementPendingCallbacks : (I)V
    //   71: new com/bumptech/glide/load/engine/EngineJob$CallLoadFailed
    //   74: astore #5
    //   76: aload #5
    //   78: aload_0
    //   79: aload_1
    //   80: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   83: aload_2
    //   84: aload #5
    //   86: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   91: goto -> 112
    //   94: aload_0
    //   95: getfield isCancelled : Z
    //   98: ifne -> 104
    //   101: goto -> 106
    //   104: iconst_0
    //   105: istore_3
    //   106: iload_3
    //   107: ldc 'Cannot add callbacks to a cancelled EngineJob'
    //   109: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   112: aload_0
    //   113: monitorexit
    //   114: return
    //   115: astore_1
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	115	finally
    //   31	56	115	finally
    //   59	91	115	finally
    //   94	101	115	finally
    //   106	112	115	finally
  }
  
  void callCallbackOnLoadFailed(ResourceCallback paramResourceCallback) {
    try {
      return;
    } finally {
      paramResourceCallback = null;
    } 
  }
  
  void callCallbackOnResourceReady(ResourceCallback paramResourceCallback) {
    try {
      return;
    } finally {
      paramResourceCallback = null;
    } 
  }
  
  void cancel() {
    if (isDone())
      return; 
    this.isCancelled = true;
    this.decodeJob.cancel();
    this.engineJobListener.onEngineJobCancelled(this, this.key);
  }
  
  void decrementPendingCallbacks() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   8: invokevirtual throwIfRecycled : ()V
    //   11: aload_0
    //   12: invokespecial isDone : ()Z
    //   15: ldc 'Not yet complete!'
    //   17: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   20: aload_0
    //   21: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   24: invokevirtual decrementAndGet : ()I
    //   27: istore_1
    //   28: iload_1
    //   29: iflt -> 37
    //   32: iconst_1
    //   33: istore_2
    //   34: goto -> 39
    //   37: iconst_0
    //   38: istore_2
    //   39: iload_2
    //   40: ldc 'Can't decrement below 0'
    //   42: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   45: iload_1
    //   46: ifne -> 58
    //   49: aload_0
    //   50: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   53: astore_3
    //   54: aload_0
    //   55: invokespecial release : ()V
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_3
    //   61: ifnull -> 68
    //   64: aload_3
    //   65: invokevirtual release : ()V
    //   68: return
    //   69: astore_3
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_3
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   4	28	69	finally
    //   39	45	69	finally
    //   49	58	69	finally
    //   58	60	69	finally
    //   70	72	69	finally
  }
  
  public StateVerifier getVerifier() {
    return this.stateVerifier;
  }
  
  void incrementPendingCallbacks(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial isDone : ()Z
    //   6: ldc 'Not yet complete!'
    //   8: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   11: aload_0
    //   12: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   15: iload_1
    //   16: invokevirtual getAndAdd : (I)I
    //   19: ifne -> 35
    //   22: aload_0
    //   23: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   26: astore_2
    //   27: aload_2
    //   28: ifnull -> 35
    //   31: aload_2
    //   32: invokevirtual acquire : ()V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_2
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_2
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	38	finally
    //   31	35	38	finally
  }
  
  EngineJob<R> init(Key paramKey, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield key : Lcom/bumptech/glide/load/Key;
    //   7: aload_0
    //   8: iload_2
    //   9: putfield isCacheable : Z
    //   12: aload_0
    //   13: iload_3
    //   14: putfield useUnlimitedSourceGeneratorPool : Z
    //   17: aload_0
    //   18: iload #4
    //   20: putfield useAnimationPool : Z
    //   23: aload_0
    //   24: iload #5
    //   26: putfield onlyRetrieveFromCache : Z
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_0
    //   32: areturn
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	33	finally
  }
  
  boolean isCancelled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCancelled : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  void notifyCallbacksOfException() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield isCancelled : Z
    //   13: ifeq -> 23
    //   16: aload_0
    //   17: invokespecial release : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   27: invokevirtual isEmpty : ()Z
    //   30: ifne -> 148
    //   33: aload_0
    //   34: getfield hasLoadFailed : Z
    //   37: ifne -> 135
    //   40: aload_0
    //   41: iconst_1
    //   42: putfield hasLoadFailed : Z
    //   45: aload_0
    //   46: getfield key : Lcom/bumptech/glide/load/Key;
    //   49: astore_1
    //   50: aload_0
    //   51: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   54: invokevirtual copy : ()Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   57: astore_2
    //   58: aload_0
    //   59: aload_2
    //   60: invokevirtual size : ()I
    //   63: iconst_1
    //   64: iadd
    //   65: invokevirtual incrementPendingCallbacks : (I)V
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_0
    //   71: getfield engineJobListener : Lcom/bumptech/glide/load/engine/EngineJobListener;
    //   74: aload_0
    //   75: aload_1
    //   76: aconst_null
    //   77: invokeinterface onEngineJobComplete : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   82: aload_2
    //   83: invokevirtual iterator : ()Ljava/util/Iterator;
    //   86: astore_1
    //   87: aload_1
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 130
    //   96: aload_1
    //   97: invokeinterface next : ()Ljava/lang/Object;
    //   102: checkcast com/bumptech/glide/load/engine/EngineJob$ResourceCallbackAndExecutor
    //   105: astore_2
    //   106: aload_2
    //   107: getfield executor : Ljava/util/concurrent/Executor;
    //   110: new com/bumptech/glide/load/engine/EngineJob$CallLoadFailed
    //   113: dup
    //   114: aload_0
    //   115: aload_2
    //   116: getfield cb : Lcom/bumptech/glide/request/ResourceCallback;
    //   119: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   122: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   127: goto -> 87
    //   130: aload_0
    //   131: invokevirtual decrementPendingCallbacks : ()V
    //   134: return
    //   135: new java/lang/IllegalStateException
    //   138: astore_1
    //   139: aload_1
    //   140: ldc_w 'Already failed once'
    //   143: invokespecial <init> : (Ljava/lang/String;)V
    //   146: aload_1
    //   147: athrow
    //   148: new java/lang/IllegalStateException
    //   151: astore_1
    //   152: aload_1
    //   153: ldc_w 'Received an exception without any callbacks to notify'
    //   156: invokespecial <init> : (Ljava/lang/String;)V
    //   159: aload_1
    //   160: athrow
    //   161: astore_1
    //   162: aload_0
    //   163: monitorexit
    //   164: aload_1
    //   165: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	161	finally
    //   23	70	161	finally
    //   135	148	161	finally
    //   148	161	161	finally
    //   162	164	161	finally
  }
  
  void notifyCallbacksOfResult() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield isCancelled : Z
    //   13: ifeq -> 32
    //   16: aload_0
    //   17: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   20: invokeinterface recycle : ()V
    //   25: aload_0
    //   26: invokespecial release : ()V
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: aload_0
    //   33: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   36: invokevirtual isEmpty : ()Z
    //   39: ifne -> 189
    //   42: aload_0
    //   43: getfield hasResource : Z
    //   46: ifne -> 176
    //   49: aload_0
    //   50: aload_0
    //   51: getfield engineResourceFactory : Lcom/bumptech/glide/load/engine/EngineJob$EngineResourceFactory;
    //   54: aload_0
    //   55: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   58: aload_0
    //   59: getfield isCacheable : Z
    //   62: aload_0
    //   63: getfield key : Lcom/bumptech/glide/load/Key;
    //   66: aload_0
    //   67: getfield resourceListener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   70: invokevirtual build : (Lcom/bumptech/glide/load/engine/Resource;ZLcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;)Lcom/bumptech/glide/load/engine/EngineResource;
    //   73: putfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   76: aload_0
    //   77: iconst_1
    //   78: putfield hasResource : Z
    //   81: aload_0
    //   82: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   85: invokevirtual copy : ()Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   88: astore_3
    //   89: aload_0
    //   90: aload_3
    //   91: invokevirtual size : ()I
    //   94: iconst_1
    //   95: iadd
    //   96: invokevirtual incrementPendingCallbacks : (I)V
    //   99: aload_0
    //   100: getfield key : Lcom/bumptech/glide/load/Key;
    //   103: astore_1
    //   104: aload_0
    //   105: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   108: astore_2
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_0
    //   112: getfield engineJobListener : Lcom/bumptech/glide/load/engine/EngineJobListener;
    //   115: aload_0
    //   116: aload_1
    //   117: aload_2
    //   118: invokeinterface onEngineJobComplete : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   123: aload_3
    //   124: invokevirtual iterator : ()Ljava/util/Iterator;
    //   127: astore_1
    //   128: aload_1
    //   129: invokeinterface hasNext : ()Z
    //   134: ifeq -> 171
    //   137: aload_1
    //   138: invokeinterface next : ()Ljava/lang/Object;
    //   143: checkcast com/bumptech/glide/load/engine/EngineJob$ResourceCallbackAndExecutor
    //   146: astore_2
    //   147: aload_2
    //   148: getfield executor : Ljava/util/concurrent/Executor;
    //   151: new com/bumptech/glide/load/engine/EngineJob$CallResourceReady
    //   154: dup
    //   155: aload_0
    //   156: aload_2
    //   157: getfield cb : Lcom/bumptech/glide/request/ResourceCallback;
    //   160: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   163: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   168: goto -> 128
    //   171: aload_0
    //   172: invokevirtual decrementPendingCallbacks : ()V
    //   175: return
    //   176: new java/lang/IllegalStateException
    //   179: astore_1
    //   180: aload_1
    //   181: ldc_w 'Already have resource'
    //   184: invokespecial <init> : (Ljava/lang/String;)V
    //   187: aload_1
    //   188: athrow
    //   189: new java/lang/IllegalStateException
    //   192: astore_1
    //   193: aload_1
    //   194: ldc_w 'Received a resource without any callbacks to notify'
    //   197: invokespecial <init> : (Ljava/lang/String;)V
    //   200: aload_1
    //   201: athrow
    //   202: astore_1
    //   203: aload_0
    //   204: monitorexit
    //   205: aload_1
    //   206: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	202	finally
    //   32	111	202	finally
    //   176	189	202	finally
    //   189	202	202	finally
    //   203	205	202	finally
  }
  
  public void onLoadFailed(GlideException paramGlideException) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: invokevirtual notifyCallbacksOfException : ()V
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	14	finally
    //   15	17	14	finally
  }
  
  public void onResourceReady(Resource<R> paramResource, DataSource paramDataSource, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   7: aload_0
    //   8: aload_2
    //   9: putfield dataSource : Lcom/bumptech/glide/load/DataSource;
    //   12: aload_0
    //   13: iload_3
    //   14: putfield isLoadedFromAlternateCacheKey : Z
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_0
    //   20: invokevirtual notifyCallbacksOfResult : ()V
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	24	finally
    //   25	27	24	finally
  }
  
  boolean onlyRetrieveFromCache() {
    return this.onlyRetrieveFromCache;
  }
  
  void removeCallback(ResourceCallback paramResourceCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: aload_1
    //   14: invokevirtual remove : (Lcom/bumptech/glide/request/ResourceCallback;)V
    //   17: aload_0
    //   18: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   21: invokevirtual isEmpty : ()Z
    //   24: ifeq -> 73
    //   27: aload_0
    //   28: invokevirtual cancel : ()V
    //   31: aload_0
    //   32: getfield hasResource : Z
    //   35: ifne -> 53
    //   38: aload_0
    //   39: getfield hasLoadFailed : Z
    //   42: ifeq -> 48
    //   45: goto -> 53
    //   48: iconst_0
    //   49: istore_2
    //   50: goto -> 55
    //   53: iconst_1
    //   54: istore_2
    //   55: iload_2
    //   56: ifeq -> 73
    //   59: aload_0
    //   60: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   63: invokevirtual get : ()I
    //   66: ifne -> 73
    //   69: aload_0
    //   70: invokespecial release : ()V
    //   73: aload_0
    //   74: monitorexit
    //   75: return
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	45	76	finally
    //   59	73	76	finally
  }
  
  public void reschedule(DecodeJob<?> paramDecodeJob) {
    getActiveSourceExecutor().execute(paramDecodeJob);
  }
  
  public void start(DecodeJob<R> paramDecodeJob) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   7: aload_1
    //   8: invokevirtual willDecodeFromCache : ()Z
    //   11: ifeq -> 22
    //   14: aload_0
    //   15: getfield diskCacheExecutor : Lcom/bumptech/glide/load/engine/executor/GlideExecutor;
    //   18: astore_2
    //   19: goto -> 27
    //   22: aload_0
    //   23: invokespecial getActiveSourceExecutor : ()Lcom/bumptech/glide/load/engine/executor/GlideExecutor;
    //   26: astore_2
    //   27: aload_2
    //   28: aload_1
    //   29: invokevirtual execute : (Ljava/lang/Runnable;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	35	finally
    //   22	27	35	finally
    //   27	32	35	finally
  }
  
  private class CallLoadFailed implements Runnable {
    private final ResourceCallback cb;
    
    final EngineJob this$0;
    
    CallLoadFailed(ResourceCallback param1ResourceCallback) {
      this.cb = param1ResourceCallback;
    }
    
    public void run() {
      synchronized (this.cb.getLock()) {
        synchronized (EngineJob.this) {
          if (EngineJob.this.cbs.contains(this.cb))
            EngineJob.this.callCallbackOnLoadFailed(this.cb); 
          EngineJob.this.decrementPendingCallbacks();
          return;
        } 
      } 
    }
  }
  
  private class CallResourceReady implements Runnable {
    private final ResourceCallback cb;
    
    final EngineJob this$0;
    
    CallResourceReady(ResourceCallback param1ResourceCallback) {
      this.cb = param1ResourceCallback;
    }
    
    public void run() {
      synchronized (this.cb.getLock()) {
        synchronized (EngineJob.this) {
          if (EngineJob.this.cbs.contains(this.cb)) {
            EngineJob.this.engineResource.acquire();
            EngineJob.this.callCallbackOnResourceReady(this.cb);
            EngineJob.this.removeCallback(this.cb);
          } 
          EngineJob.this.decrementPendingCallbacks();
          return;
        } 
      } 
    }
  }
  
  static class EngineResourceFactory {
    public <R> EngineResource<R> build(Resource<R> param1Resource, boolean param1Boolean, Key param1Key, EngineResource.ResourceListener param1ResourceListener) {
      return new EngineResource<>(param1Resource, param1Boolean, true, param1Key, param1ResourceListener);
    }
  }
  
  static final class ResourceCallbackAndExecutor {
    final ResourceCallback cb;
    
    final Executor executor;
    
    ResourceCallbackAndExecutor(ResourceCallback param1ResourceCallback, Executor param1Executor) {
      this.cb = param1ResourceCallback;
      this.executor = param1Executor;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof ResourceCallbackAndExecutor) {
        param1Object = param1Object;
        return this.cb.equals(((ResourceCallbackAndExecutor)param1Object).cb);
      } 
      return false;
    }
    
    public int hashCode() {
      return this.cb.hashCode();
    }
  }
  
  static final class ResourceCallbacksAndExecutors implements Iterable<ResourceCallbackAndExecutor> {
    private final List<EngineJob.ResourceCallbackAndExecutor> callbacksAndExecutors;
    
    ResourceCallbacksAndExecutors() {
      this(new ArrayList<>(2));
    }
    
    ResourceCallbacksAndExecutors(List<EngineJob.ResourceCallbackAndExecutor> param1List) {
      this.callbacksAndExecutors = param1List;
    }
    
    private static EngineJob.ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback param1ResourceCallback) {
      return new EngineJob.ResourceCallbackAndExecutor(param1ResourceCallback, Executors.directExecutor());
    }
    
    void add(ResourceCallback param1ResourceCallback, Executor param1Executor) {
      this.callbacksAndExecutors.add(new EngineJob.ResourceCallbackAndExecutor(param1ResourceCallback, param1Executor));
    }
    
    void clear() {
      this.callbacksAndExecutors.clear();
    }
    
    boolean contains(ResourceCallback param1ResourceCallback) {
      return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(param1ResourceCallback));
    }
    
    ResourceCallbacksAndExecutors copy() {
      return new ResourceCallbacksAndExecutors(new ArrayList<>(this.callbacksAndExecutors));
    }
    
    boolean isEmpty() {
      return this.callbacksAndExecutors.isEmpty();
    }
    
    public Iterator<EngineJob.ResourceCallbackAndExecutor> iterator() {
      return this.callbacksAndExecutors.iterator();
    }
    
    void remove(ResourceCallback param1ResourceCallback) {
      this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(param1ResourceCallback));
    }
    
    int size() {
      return this.callbacksAndExecutors.size();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\EngineJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */