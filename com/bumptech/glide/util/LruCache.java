package com.bumptech.glide.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<T, Y> {
  private final Map<T, Entry<Y>> cache = new LinkedHashMap<>(100, 0.75F, true);
  
  private long currentSize;
  
  private final long initialMaxSize;
  
  private long maxSize;
  
  public LruCache(long paramLong) {
    this.initialMaxSize = paramLong;
    this.maxSize = paramLong;
  }
  
  private void evict() {
    trimToSize(this.maxSize);
  }
  
  public void clearMemory() {
    trimToSize(0L);
  }
  
  public boolean contains(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: istore_2
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_2
    //   16: ireturn
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  public Y get(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/util/LruCache$Entry
    //   15: astore_1
    //   16: aload_1
    //   17: ifnull -> 28
    //   20: aload_1
    //   21: getfield value : Ljava/lang/Object;
    //   24: astore_1
    //   25: goto -> 30
    //   28: aconst_null
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: areturn
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	34	finally
    //   20	25	34	finally
  }
  
  protected int getCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public long getCurrentSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  protected int getSize(Y paramY) {
    return 1;
  }
  
  protected void onItemEvicted(T paramT, Y paramY) {}
  
  public Y put(T paramT, Y paramY) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: invokevirtual getSize : (Ljava/lang/Object;)I
    //   7: istore_3
    //   8: iload_3
    //   9: i2l
    //   10: lstore #6
    //   12: aload_0
    //   13: getfield maxSize : J
    //   16: lstore #4
    //   18: aconst_null
    //   19: astore #9
    //   21: lload #6
    //   23: lload #4
    //   25: lcmp
    //   26: iflt -> 39
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   35: aload_0
    //   36: monitorexit
    //   37: aconst_null
    //   38: areturn
    //   39: aload_2
    //   40: ifnull -> 54
    //   43: aload_0
    //   44: aload_0
    //   45: getfield currentSize : J
    //   48: iload_3
    //   49: i2l
    //   50: ladd
    //   51: putfield currentSize : J
    //   54: aload_0
    //   55: getfield cache : Ljava/util/Map;
    //   58: astore #10
    //   60: aload_2
    //   61: ifnonnull -> 70
    //   64: aconst_null
    //   65: astore #8
    //   67: goto -> 81
    //   70: new com/bumptech/glide/util/LruCache$Entry
    //   73: dup
    //   74: aload_2
    //   75: iload_3
    //   76: invokespecial <init> : (Ljava/lang/Object;I)V
    //   79: astore #8
    //   81: aload #10
    //   83: aload_1
    //   84: aload #8
    //   86: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   91: checkcast com/bumptech/glide/util/LruCache$Entry
    //   94: astore #8
    //   96: aload #8
    //   98: ifnull -> 138
    //   101: aload_0
    //   102: aload_0
    //   103: getfield currentSize : J
    //   106: aload #8
    //   108: getfield size : I
    //   111: i2l
    //   112: lsub
    //   113: putfield currentSize : J
    //   116: aload #8
    //   118: getfield value : Ljava/lang/Object;
    //   121: aload_2
    //   122: invokevirtual equals : (Ljava/lang/Object;)Z
    //   125: ifne -> 138
    //   128: aload_0
    //   129: aload_1
    //   130: aload #8
    //   132: getfield value : Ljava/lang/Object;
    //   135: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   138: aload_0
    //   139: invokespecial evict : ()V
    //   142: aload #9
    //   144: astore_1
    //   145: aload #8
    //   147: ifnull -> 156
    //   150: aload #8
    //   152: getfield value : Ljava/lang/Object;
    //   155: astore_1
    //   156: aload_0
    //   157: monitorexit
    //   158: aload_1
    //   159: areturn
    //   160: astore_1
    //   161: aload_0
    //   162: monitorexit
    //   163: aload_1
    //   164: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	160	finally
    //   12	18	160	finally
    //   29	35	160	finally
    //   43	54	160	finally
    //   54	60	160	finally
    //   70	81	160	finally
    //   81	96	160	finally
    //   101	138	160	finally
    //   138	142	160	finally
    //   150	156	160	finally
  }
  
  public Y remove(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/util/LruCache$Entry
    //   15: astore_1
    //   16: aload_1
    //   17: ifnonnull -> 24
    //   20: aload_0
    //   21: monitorexit
    //   22: aconst_null
    //   23: areturn
    //   24: aload_0
    //   25: aload_0
    //   26: getfield currentSize : J
    //   29: aload_1
    //   30: getfield size : I
    //   33: i2l
    //   34: lsub
    //   35: putfield currentSize : J
    //   38: aload_1
    //   39: getfield value : Ljava/lang/Object;
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: areturn
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	47	finally
    //   24	43	47	finally
  }
  
  public void setSizeMultiplier(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: fload_1
    //   3: fconst_0
    //   4: fcmpg
    //   5: iflt -> 34
    //   8: aload_0
    //   9: aload_0
    //   10: getfield initialMaxSize : J
    //   13: l2f
    //   14: fload_1
    //   15: fmul
    //   16: invokestatic round : (F)I
    //   19: i2l
    //   20: putfield maxSize : J
    //   23: aload_0
    //   24: invokespecial evict : ()V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_2
    //   31: goto -> 46
    //   34: new java/lang/IllegalArgumentException
    //   37: astore_2
    //   38: aload_2
    //   39: ldc 'Multiplier must be >= 0'
    //   41: invokespecial <init> : (Ljava/lang/String;)V
    //   44: aload_2
    //   45: athrow
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_2
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   8	27	30	finally
    //   34	46	30	finally
  }
  
  protected void trimToSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSize : J
    //   6: lload_1
    //   7: lcmp
    //   8: ifle -> 93
    //   11: aload_0
    //   12: getfield cache : Ljava/util/Map;
    //   15: invokeinterface entrySet : ()Ljava/util/Set;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore #4
    //   27: aload #4
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast java/util/Map$Entry
    //   37: astore #5
    //   39: aload #5
    //   41: invokeinterface getValue : ()Ljava/lang/Object;
    //   46: checkcast com/bumptech/glide/util/LruCache$Entry
    //   49: astore_3
    //   50: aload_0
    //   51: aload_0
    //   52: getfield currentSize : J
    //   55: aload_3
    //   56: getfield size : I
    //   59: i2l
    //   60: lsub
    //   61: putfield currentSize : J
    //   64: aload #5
    //   66: invokeinterface getKey : ()Ljava/lang/Object;
    //   71: astore #5
    //   73: aload #4
    //   75: invokeinterface remove : ()V
    //   80: aload_0
    //   81: aload #5
    //   83: aload_3
    //   84: getfield value : Ljava/lang/Object;
    //   87: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   90: goto -> 2
    //   93: aload_0
    //   94: monitorexit
    //   95: return
    //   96: astore_3
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_3
    //   100: athrow
    // Exception table:
    //   from	to	target	type
    //   2	90	96	finally
  }
  
  static final class Entry<Y> {
    final int size;
    
    final Y value;
    
    Entry(Y param1Y, int param1Int) {
      this.value = param1Y;
      this.size = param1Int;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\LruCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */