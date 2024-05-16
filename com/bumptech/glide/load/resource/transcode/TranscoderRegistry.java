package com.bumptech.glide.load.resource.transcode;

import java.util.ArrayList;
import java.util.List;

public class TranscoderRegistry {
  private final List<Entry<?, ?>> transcoders = new ArrayList<>();
  
  public <Z, R> ResourceTranscoder<Z, R> get(Class<Z> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: aload_1
    //   4: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   7: ifeq -> 18
    //   10: invokestatic get : ()Lcom/bumptech/glide/load/resource/transcode/ResourceTranscoder;
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: aload_0
    //   19: getfield transcoders : Ljava/util/List;
    //   22: invokeinterface iterator : ()Ljava/util/Iterator;
    //   27: astore_3
    //   28: aload_3
    //   29: invokeinterface hasNext : ()Z
    //   34: ifeq -> 71
    //   37: aload_3
    //   38: invokeinterface next : ()Ljava/lang/Object;
    //   43: checkcast com/bumptech/glide/load/resource/transcode/TranscoderRegistry$Entry
    //   46: astore #4
    //   48: aload #4
    //   50: aload_1
    //   51: aload_2
    //   52: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   55: ifeq -> 68
    //   58: aload #4
    //   60: getfield transcoder : Lcom/bumptech/glide/load/resource/transcode/ResourceTranscoder;
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: areturn
    //   68: goto -> 28
    //   71: new java/lang/IllegalArgumentException
    //   74: astore #4
    //   76: new java/lang/StringBuilder
    //   79: astore_3
    //   80: aload_3
    //   81: invokespecial <init> : ()V
    //   84: aload #4
    //   86: aload_3
    //   87: ldc 'No transcoder registered to transcode from '
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: aload_1
    //   93: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   96: ldc ' to '
    //   98: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: aload_2
    //   102: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   105: invokevirtual toString : ()Ljava/lang/String;
    //   108: invokespecial <init> : (Ljava/lang/String;)V
    //   111: aload #4
    //   113: athrow
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	114	finally
    //   18	28	114	finally
    //   28	64	114	finally
    //   71	114	114	finally
  }
  
  public <Z, R> List<Class<R>> getTranscodeClasses(Class<Z> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_2
    //   11: aload_1
    //   12: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   15: ifeq -> 30
    //   18: aload_3
    //   19: aload_2
    //   20: invokeinterface add : (Ljava/lang/Object;)Z
    //   25: pop
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_3
    //   29: areturn
    //   30: aload_0
    //   31: getfield transcoders : Ljava/util/List;
    //   34: invokeinterface iterator : ()Ljava/util/Iterator;
    //   39: astore #4
    //   41: aload #4
    //   43: invokeinterface hasNext : ()Z
    //   48: ifeq -> 80
    //   51: aload #4
    //   53: invokeinterface next : ()Ljava/lang/Object;
    //   58: checkcast com/bumptech/glide/load/resource/transcode/TranscoderRegistry$Entry
    //   61: aload_1
    //   62: aload_2
    //   63: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   66: ifeq -> 77
    //   69: aload_3
    //   70: aload_2
    //   71: invokeinterface add : (Ljava/lang/Object;)Z
    //   76: pop
    //   77: goto -> 41
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_3
    //   83: areturn
    //   84: astore_1
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	84	finally
    //   30	41	84	finally
    //   41	77	84	finally
  }
  
  public <Z, R> void register(Class<Z> paramClass, Class<R> paramClass1, ResourceTranscoder<Z, R> paramResourceTranscoder) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield transcoders : Ljava/util/List;
    //   6: astore #4
    //   8: new com/bumptech/glide/load/resource/transcode/TranscoderRegistry$Entry
    //   11: astore #5
    //   13: aload #5
    //   15: aload_1
    //   16: aload_2
    //   17: aload_3
    //   18: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/resource/transcode/ResourceTranscoder;)V
    //   21: aload #4
    //   23: aload #5
    //   25: invokeinterface add : (Ljava/lang/Object;)Z
    //   30: pop
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
    //   2	31	34	finally
  }
  
  private static final class Entry<Z, R> {
    private final Class<Z> fromClass;
    
    private final Class<R> toClass;
    
    final ResourceTranscoder<Z, R> transcoder;
    
    Entry(Class<Z> param1Class, Class<R> param1Class1, ResourceTranscoder<Z, R> param1ResourceTranscoder) {
      this.fromClass = param1Class;
      this.toClass = param1Class1;
      this.transcoder = param1ResourceTranscoder;
    }
    
    public boolean handles(Class<?> param1Class1, Class<?> param1Class2) {
      boolean bool;
      if (this.fromClass.isAssignableFrom(param1Class1) && param1Class2.isAssignableFrom(this.toClass)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\transcode\TranscoderRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */