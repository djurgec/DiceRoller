package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
  private final List<Entry<?>> encoders = new ArrayList<>();
  
  public <Z> void append(Class<Z> paramClass, ResourceEncoder<Z> paramResourceEncoder) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield encoders : Ljava/util/List;
    //   6: astore #4
    //   8: new com/bumptech/glide/provider/ResourceEncoderRegistry$Entry
    //   11: astore_3
    //   12: aload_3
    //   13: aload_1
    //   14: aload_2
    //   15: invokespecial <init> : (Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceEncoder;)V
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
  
  public <Z> ResourceEncoder<Z> get(Class<Z> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_0
    //   5: getfield encoders : Ljava/util/List;
    //   8: invokeinterface size : ()I
    //   13: istore_3
    //   14: iload_2
    //   15: iload_3
    //   16: if_icmpge -> 59
    //   19: aload_0
    //   20: getfield encoders : Ljava/util/List;
    //   23: iload_2
    //   24: invokeinterface get : (I)Ljava/lang/Object;
    //   29: checkcast com/bumptech/glide/provider/ResourceEncoderRegistry$Entry
    //   32: astore #4
    //   34: aload #4
    //   36: aload_1
    //   37: invokevirtual handles : (Ljava/lang/Class;)Z
    //   40: ifeq -> 53
    //   43: aload #4
    //   45: getfield encoder : Lcom/bumptech/glide/load/ResourceEncoder;
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: areturn
    //   53: iinc #2, 1
    //   56: goto -> 14
    //   59: aload_0
    //   60: monitorexit
    //   61: aconst_null
    //   62: areturn
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   4	14	63	finally
    //   19	49	63	finally
  }
  
  public <Z> void prepend(Class<Z> paramClass, ResourceEncoder<Z> paramResourceEncoder) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield encoders : Ljava/util/List;
    //   6: astore_3
    //   7: new com/bumptech/glide/provider/ResourceEncoderRegistry$Entry
    //   10: astore #4
    //   12: aload #4
    //   14: aload_1
    //   15: aload_2
    //   16: invokespecial <init> : (Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceEncoder;)V
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
    final ResourceEncoder<T> encoder;
    
    private final Class<T> resourceClass;
    
    Entry(Class<T> param1Class, ResourceEncoder<T> param1ResourceEncoder) {
      this.resourceClass = param1Class;
      this.encoder = param1ResourceEncoder;
    }
    
    boolean handles(Class<?> param1Class) {
      return this.resourceClass.isAssignableFrom(param1Class);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\provider\ResourceEncoderRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */