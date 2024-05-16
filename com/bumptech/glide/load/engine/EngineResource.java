package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

class EngineResource<Z> implements Resource<Z> {
  private int acquired;
  
  private final boolean isMemoryCacheable;
  
  private final boolean isRecyclable;
  
  private boolean isRecycled;
  
  private final Key key;
  
  private final ResourceListener listener;
  
  private final Resource<Z> resource;
  
  EngineResource(Resource<Z> paramResource, boolean paramBoolean1, boolean paramBoolean2, Key paramKey, ResourceListener paramResourceListener) {
    this.resource = (Resource<Z>)Preconditions.checkNotNull(paramResource);
    this.isMemoryCacheable = paramBoolean1;
    this.isRecyclable = paramBoolean2;
    this.key = paramKey;
    this.listener = (ResourceListener)Preconditions.checkNotNull(paramResourceListener);
  }
  
  void acquire() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isRecycled : Z
    //   6: ifne -> 22
    //   9: aload_0
    //   10: aload_0
    //   11: getfield acquired : I
    //   14: iconst_1
    //   15: iadd
    //   16: putfield acquired : I
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: new java/lang/IllegalStateException
    //   25: astore_1
    //   26: aload_1
    //   27: ldc 'Cannot acquire a recycled resource'
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: aload_1
    //   33: athrow
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	34	finally
    //   22	34	34	finally
  }
  
  public Z get() {
    return this.resource.get();
  }
  
  Resource<Z> getResource() {
    return this.resource;
  }
  
  public Class<Z> getResourceClass() {
    return this.resource.getResourceClass();
  }
  
  public int getSize() {
    return this.resource.getSize();
  }
  
  boolean isMemoryCacheable() {
    return this.isMemoryCacheable;
  }
  
  public void recycle() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield acquired : I
    //   6: ifgt -> 52
    //   9: aload_0
    //   10: getfield isRecycled : Z
    //   13: ifne -> 40
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield isRecycled : Z
    //   21: aload_0
    //   22: getfield isRecyclable : Z
    //   25: ifeq -> 37
    //   28: aload_0
    //   29: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   32: invokeinterface recycle : ()V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: new java/lang/IllegalStateException
    //   43: astore_1
    //   44: aload_1
    //   45: ldc 'Cannot recycle a resource that has already been recycled'
    //   47: invokespecial <init> : (Ljava/lang/String;)V
    //   50: aload_1
    //   51: athrow
    //   52: new java/lang/IllegalStateException
    //   55: astore_1
    //   56: aload_1
    //   57: ldc 'Cannot recycle a resource while it is still acquired'
    //   59: invokespecial <init> : (Ljava/lang/String;)V
    //   62: aload_1
    //   63: athrow
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	64	finally
    //   40	52	64	finally
    //   52	64	64	finally
  }
  
  void release() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield acquired : I
    //   8: istore_2
    //   9: iload_2
    //   10: ifle -> 48
    //   13: iinc #2, -1
    //   16: aload_0
    //   17: iload_2
    //   18: putfield acquired : I
    //   21: iload_2
    //   22: ifne -> 27
    //   25: iconst_1
    //   26: istore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: iload_1
    //   30: ifeq -> 47
    //   33: aload_0
    //   34: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   37: aload_0
    //   38: getfield key : Lcom/bumptech/glide/load/Key;
    //   41: aload_0
    //   42: invokeinterface onResourceReleased : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   47: return
    //   48: new java/lang/IllegalStateException
    //   51: astore_3
    //   52: aload_3
    //   53: ldc 'Cannot release a recycled or not yet acquired resource'
    //   55: invokespecial <init> : (Ljava/lang/String;)V
    //   58: aload_3
    //   59: athrow
    //   60: astore_3
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_3
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   4	9	60	finally
    //   16	21	60	finally
    //   27	29	60	finally
    //   48	60	60	finally
    //   61	63	60	finally
  }
  
  public String toString() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_1
    //   11: ldc 'EngineResource{isMemoryCacheable='
    //   13: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: aload_0
    //   17: getfield isMemoryCacheable : Z
    //   20: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   23: ldc ', listener='
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: aload_0
    //   29: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   32: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   35: ldc ', key='
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: aload_0
    //   41: getfield key : Lcom/bumptech/glide/load/Key;
    //   44: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   47: ldc ', acquired='
    //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: aload_0
    //   53: getfield acquired : I
    //   56: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   59: ldc ', isRecycled='
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload_0
    //   65: getfield isRecycled : Z
    //   68: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   71: ldc ', resource='
    //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: aload_0
    //   77: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   80: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   83: bipush #125
    //   85: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: astore_1
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_1
    //   95: areturn
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    // Exception table:
    //   from	to	target	type
    //   2	92	96	finally
  }
  
  static interface ResourceListener {
    void onResourceReleased(Key param1Key, EngineResource<?> param1EngineResource);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\EngineResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */