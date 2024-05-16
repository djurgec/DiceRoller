package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
  private final List<String> bucketPriorityList = new ArrayList<>();
  
  private final Map<String, List<Entry<?, ?>>> decoders = new HashMap<>();
  
  private List<Entry<?, ?>> getOrAddEntryList(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield bucketPriorityList : Ljava/util/List;
    //   6: aload_1
    //   7: invokeinterface contains : (Ljava/lang/Object;)Z
    //   12: ifne -> 26
    //   15: aload_0
    //   16: getfield bucketPriorityList : Ljava/util/List;
    //   19: aload_1
    //   20: invokeinterface add : (Ljava/lang/Object;)Z
    //   25: pop
    //   26: aload_0
    //   27: getfield decoders : Ljava/util/Map;
    //   30: aload_1
    //   31: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   36: checkcast java/util/List
    //   39: astore_3
    //   40: aload_3
    //   41: astore_2
    //   42: aload_3
    //   43: ifnonnull -> 66
    //   46: new java/util/ArrayList
    //   49: astore_2
    //   50: aload_2
    //   51: invokespecial <init> : ()V
    //   54: aload_0
    //   55: getfield decoders : Ljava/util/Map;
    //   58: aload_1
    //   59: aload_2
    //   60: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_2
    //   69: areturn
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	70	finally
    //   26	40	70	finally
    //   46	54	70	finally
    //   54	66	70	finally
  }
  
  public <T, R> void append(String paramString, ResourceDecoder<T, R> paramResourceDecoder, Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial getOrAddEntryList : (Ljava/lang/String;)Ljava/util/List;
    //   7: astore #5
    //   9: new com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   12: astore_1
    //   13: aload_1
    //   14: aload_3
    //   15: aload #4
    //   17: aload_2
    //   18: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceDecoder;)V
    //   21: aload #5
    //   23: aload_1
    //   24: invokeinterface add : (Ljava/lang/Object;)Z
    //   29: pop
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	33	finally
  }
  
  public <T, R> List<ResourceDecoder<T, R>> getDecoders(Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: aload_0
    //   13: getfield bucketPriorityList : Ljava/util/List;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_3
    //   22: aload_3
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 126
    //   31: aload_3
    //   32: invokeinterface next : ()Ljava/lang/Object;
    //   37: checkcast java/lang/String
    //   40: astore #5
    //   42: aload_0
    //   43: getfield decoders : Ljava/util/Map;
    //   46: aload #5
    //   48: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   53: checkcast java/util/List
    //   56: astore #5
    //   58: aload #5
    //   60: ifnonnull -> 66
    //   63: goto -> 22
    //   66: aload #5
    //   68: invokeinterface iterator : ()Ljava/util/Iterator;
    //   73: astore #5
    //   75: aload #5
    //   77: invokeinterface hasNext : ()Z
    //   82: ifeq -> 123
    //   85: aload #5
    //   87: invokeinterface next : ()Ljava/lang/Object;
    //   92: checkcast com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   95: astore #6
    //   97: aload #6
    //   99: aload_1
    //   100: aload_2
    //   101: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   104: ifeq -> 120
    //   107: aload #4
    //   109: aload #6
    //   111: getfield decoder : Lcom/bumptech/glide/load/ResourceDecoder;
    //   114: invokeinterface add : (Ljava/lang/Object;)Z
    //   119: pop
    //   120: goto -> 75
    //   123: goto -> 22
    //   126: aload_0
    //   127: monitorexit
    //   128: aload #4
    //   130: areturn
    //   131: astore_1
    //   132: aload_0
    //   133: monitorexit
    //   134: aload_1
    //   135: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	131	finally
    //   22	58	131	finally
    //   66	75	131	finally
    //   75	120	131	finally
  }
  
  public <T, R> List<Class<R>> getResourceClasses(Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield bucketPriorityList : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: aload #4
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 140
    //   31: aload #4
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast java/lang/String
    //   41: astore #5
    //   43: aload_0
    //   44: getfield decoders : Ljava/util/Map;
    //   47: aload #5
    //   49: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   54: checkcast java/util/List
    //   57: astore #5
    //   59: aload #5
    //   61: ifnonnull -> 67
    //   64: goto -> 21
    //   67: aload #5
    //   69: invokeinterface iterator : ()Ljava/util/Iterator;
    //   74: astore #6
    //   76: aload #6
    //   78: invokeinterface hasNext : ()Z
    //   83: ifeq -> 137
    //   86: aload #6
    //   88: invokeinterface next : ()Ljava/lang/Object;
    //   93: checkcast com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   96: astore #5
    //   98: aload #5
    //   100: aload_1
    //   101: aload_2
    //   102: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   105: ifeq -> 134
    //   108: aload_3
    //   109: aload #5
    //   111: getfield resourceClass : Ljava/lang/Class;
    //   114: invokeinterface contains : (Ljava/lang/Object;)Z
    //   119: ifne -> 134
    //   122: aload_3
    //   123: aload #5
    //   125: getfield resourceClass : Ljava/lang/Class;
    //   128: invokeinterface add : (Ljava/lang/Object;)Z
    //   133: pop
    //   134: goto -> 76
    //   137: goto -> 21
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_3
    //   143: areturn
    //   144: astore_1
    //   145: aload_0
    //   146: monitorexit
    //   147: aload_1
    //   148: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	144	finally
    //   21	59	144	finally
    //   67	76	144	finally
    //   76	134	144	finally
  }
  
  public <T, R> void prepend(String paramString, ResourceDecoder<T, R> paramResourceDecoder, Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial getOrAddEntryList : (Ljava/lang/String;)Ljava/util/List;
    //   7: astore_1
    //   8: new com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   11: astore #5
    //   13: aload #5
    //   15: aload_3
    //   16: aload #4
    //   18: aload_2
    //   19: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceDecoder;)V
    //   22: aload_1
    //   23: iconst_0
    //   24: aload #5
    //   26: invokeinterface add : (ILjava/lang/Object;)V
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
  
  public void setBucketPriorityList(List<String> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: aload_0
    //   8: getfield bucketPriorityList : Ljava/util/List;
    //   11: invokespecial <init> : (Ljava/util/Collection;)V
    //   14: aload_0
    //   15: getfield bucketPriorityList : Ljava/util/List;
    //   18: invokeinterface clear : ()V
    //   23: aload_1
    //   24: invokeinterface iterator : ()Ljava/util/Iterator;
    //   29: astore #4
    //   31: aload #4
    //   33: invokeinterface hasNext : ()Z
    //   38: ifeq -> 66
    //   41: aload #4
    //   43: invokeinterface next : ()Ljava/lang/Object;
    //   48: checkcast java/lang/String
    //   51: astore_2
    //   52: aload_0
    //   53: getfield bucketPriorityList : Ljava/util/List;
    //   56: aload_2
    //   57: invokeinterface add : (Ljava/lang/Object;)Z
    //   62: pop
    //   63: goto -> 31
    //   66: aload_3
    //   67: invokeinterface iterator : ()Ljava/util/Iterator;
    //   72: astore_2
    //   73: aload_2
    //   74: invokeinterface hasNext : ()Z
    //   79: ifeq -> 116
    //   82: aload_2
    //   83: invokeinterface next : ()Ljava/lang/Object;
    //   88: checkcast java/lang/String
    //   91: astore_3
    //   92: aload_1
    //   93: aload_3
    //   94: invokeinterface contains : (Ljava/lang/Object;)Z
    //   99: ifne -> 113
    //   102: aload_0
    //   103: getfield bucketPriorityList : Ljava/util/List;
    //   106: aload_3
    //   107: invokeinterface add : (Ljava/lang/Object;)Z
    //   112: pop
    //   113: goto -> 73
    //   116: aload_0
    //   117: monitorexit
    //   118: return
    //   119: astore_1
    //   120: aload_0
    //   121: monitorexit
    //   122: aload_1
    //   123: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	119	finally
    //   31	63	119	finally
    //   66	73	119	finally
    //   73	113	119	finally
  }
  
  private static class Entry<T, R> {
    private final Class<T> dataClass;
    
    final ResourceDecoder<T, R> decoder;
    
    final Class<R> resourceClass;
    
    public Entry(Class<T> param1Class, Class<R> param1Class1, ResourceDecoder<T, R> param1ResourceDecoder) {
      this.dataClass = param1Class;
      this.resourceClass = param1Class1;
      this.decoder = param1ResourceDecoder;
    }
    
    public boolean handles(Class<?> param1Class1, Class<?> param1Class2) {
      boolean bool;
      if (this.dataClass.isAssignableFrom(param1Class1) && param1Class2.isAssignableFrom(this.resourceClass)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\provider\ResourceDecoderRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */