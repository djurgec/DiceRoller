package com.bumptech.glide.load.data;

import java.util.HashMap;
import java.util.Map;

public class DataRewinderRegistry {
  private static final DataRewinder.Factory<?> DEFAULT_FACTORY = new DataRewinder.Factory() {
      public DataRewinder<Object> build(Object param1Object) {
        return new DataRewinderRegistry.DefaultRewinder(param1Object);
      }
      
      public Class<Object> getDataClass() {
        throw new UnsupportedOperationException("Not implemented");
      }
    };
  
  private final Map<Class<?>, DataRewinder.Factory<?>> rewinders = new HashMap<>();
  
  public <T> DataRewinder<T> build(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield rewinders : Ljava/util/Map;
    //   11: aload_1
    //   12: invokevirtual getClass : ()Ljava/lang/Class;
    //   15: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   20: checkcast com/bumptech/glide/load/data/DataRewinder$Factory
    //   23: astore_3
    //   24: aload_3
    //   25: astore_2
    //   26: aload_3
    //   27: ifnonnull -> 91
    //   30: aload_0
    //   31: getfield rewinders : Ljava/util/Map;
    //   34: invokeinterface values : ()Ljava/util/Collection;
    //   39: invokeinterface iterator : ()Ljava/util/Iterator;
    //   44: astore #4
    //   46: aload_3
    //   47: astore_2
    //   48: aload #4
    //   50: invokeinterface hasNext : ()Z
    //   55: ifeq -> 91
    //   58: aload #4
    //   60: invokeinterface next : ()Ljava/lang/Object;
    //   65: checkcast com/bumptech/glide/load/data/DataRewinder$Factory
    //   68: astore_2
    //   69: aload_2
    //   70: invokeinterface getDataClass : ()Ljava/lang/Class;
    //   75: aload_1
    //   76: invokevirtual getClass : ()Ljava/lang/Class;
    //   79: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   82: ifeq -> 88
    //   85: goto -> 91
    //   88: goto -> 46
    //   91: aload_2
    //   92: astore_3
    //   93: aload_2
    //   94: ifnonnull -> 101
    //   97: getstatic com/bumptech/glide/load/data/DataRewinderRegistry.DEFAULT_FACTORY : Lcom/bumptech/glide/load/data/DataRewinder$Factory;
    //   100: astore_3
    //   101: aload_3
    //   102: aload_1
    //   103: invokeinterface build : (Ljava/lang/Object;)Lcom/bumptech/glide/load/data/DataRewinder;
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: areturn
    //   113: astore_1
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	113	finally
    //   30	46	113	finally
    //   48	85	113	finally
    //   97	101	113	finally
    //   101	109	113	finally
  }
  
  public void register(DataRewinder.Factory<?> paramFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield rewinders : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface getDataClass : ()Ljava/lang/Class;
    //   12: aload_1
    //   13: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  private static final class DefaultRewinder implements DataRewinder<Object> {
    private final Object data;
    
    DefaultRewinder(Object param1Object) {
      this.data = param1Object;
    }
    
    public void cleanup() {}
    
    public Object rewindAndGet() {
      return this.data;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\DataRewinderRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */