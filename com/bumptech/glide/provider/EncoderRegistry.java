package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry {
  private final List<Entry<?>> encoders = new ArrayList<>();
  
  public <T> void append(Class<T> paramClass, Encoder<T> paramEncoder) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield encoders : Ljava/util/List;
    //   6: astore #4
    //   8: new com/bumptech/glide/provider/EncoderRegistry$Entry
    //   11: astore_3
    //   12: aload_3
    //   13: aload_1
    //   14: aload_2
    //   15: invokespecial <init> : (Ljava/lang/Class;Lcom/bumptech/glide/load/Encoder;)V
    //   18: aload #4
    //   20: aload_3
    //   21: invokeinterface add : (Ljava/lang/Object;)Z
    //   26: pop
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public <T> Encoder<T> getEncoder(Class<T> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield encoders : Ljava/util/List;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_3
    //   12: aload_3
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 51
    //   21: aload_3
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast com/bumptech/glide/provider/EncoderRegistry$Entry
    //   30: astore_2
    //   31: aload_2
    //   32: aload_1
    //   33: invokevirtual handles : (Ljava/lang/Class;)Z
    //   36: ifeq -> 48
    //   39: aload_2
    //   40: getfield encoder : Lcom/bumptech/glide/load/Encoder;
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: areturn
    //   48: goto -> 12
    //   51: aload_0
    //   52: monitorexit
    //   53: aconst_null
    //   54: areturn
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	55	finally
    //   12	44	55	finally
  }
  
  public <T> void prepend(Class<T> paramClass, Encoder<T> paramEncoder) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield encoders : Ljava/util/List;
    //   6: astore_3
    //   7: new com/bumptech/glide/provider/EncoderRegistry$Entry
    //   10: astore #4
    //   12: aload #4
    //   14: aload_1
    //   15: aload_2
    //   16: invokespecial <init> : (Ljava/lang/Class;Lcom/bumptech/glide/load/Encoder;)V
    //   19: aload_3
    //   20: iconst_0
    //   21: aload #4
    //   23: invokeinterface add : (ILjava/lang/Object;)V
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	31	finally
  }
  
  private static final class Entry<T> {
    private final Class<T> dataClass;
    
    final Encoder<T> encoder;
    
    Entry(Class<T> param1Class, Encoder<T> param1Encoder) {
      this.dataClass = param1Class;
      this.encoder = param1Encoder;
    }
    
    boolean handles(Class<?> param1Class) {
      return this.dataClass.isAssignableFrom(param1Class);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\provider\EncoderRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */