package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool implements ArrayPool {
  private static final int DEFAULT_SIZE = 4194304;
  
  static final int MAX_OVER_SIZE_MULTIPLE = 8;
  
  private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
  
  private final Map<Class<?>, ArrayAdapterInterface<?>> adapters = new HashMap<>();
  
  private int currentSize;
  
  private final GroupedLinkedMap<Key, Object> groupedMap = new GroupedLinkedMap<>();
  
  private final KeyPool keyPool = new KeyPool();
  
  private final int maxSize = 4194304;
  
  private final Map<Class<?>, NavigableMap<Integer, Integer>> sortedSizes = new HashMap<>();
  
  public LruArrayPool() {}
  
  public LruArrayPool(int paramInt) {}
  
  private void decrementArrayOfSize(int paramInt, Class<?> paramClass) {
    NavigableMap<Integer, Integer> navigableMap = getSizesForAdapter(paramClass);
    Integer integer = navigableMap.get(Integer.valueOf(paramInt));
    if (integer != null) {
      if (integer.intValue() == 1) {
        navigableMap.remove(Integer.valueOf(paramInt));
      } else {
        navigableMap.put(Integer.valueOf(paramInt), Integer.valueOf(integer.intValue() - 1));
      } 
      return;
    } 
    throw new NullPointerException("Tried to decrement empty size, size: " + paramInt + ", this: " + this);
  }
  
  private void evict() {
    evictToSize(this.maxSize);
  }
  
  private void evictToSize(int paramInt) {
    while (this.currentSize > paramInt) {
      Object object = this.groupedMap.removeLast();
      Preconditions.checkNotNull(object);
      ArrayAdapterInterface<Object> arrayAdapterInterface = getAdapterFromObject(object);
      this.currentSize -= arrayAdapterInterface.getArrayLength(object) * arrayAdapterInterface.getElementSizeInBytes();
      decrementArrayOfSize(arrayAdapterInterface.getArrayLength(object), object.getClass());
      if (Log.isLoggable(arrayAdapterInterface.getTag(), 2))
        Log.v(arrayAdapterInterface.getTag(), "evicted: " + arrayAdapterInterface.getArrayLength(object)); 
    } 
  }
  
  private <T> ArrayAdapterInterface<T> getAdapterFromObject(T paramT) {
    return getAdapterFromType((Class)paramT.getClass());
  }
  
  private <T> ArrayAdapterInterface<T> getAdapterFromType(Class<T> paramClass) {
    ArrayAdapterInterface<?> arrayAdapterInterface2 = this.adapters.get(paramClass);
    ArrayAdapterInterface<?> arrayAdapterInterface1 = arrayAdapterInterface2;
    if (arrayAdapterInterface2 == null) {
      if (paramClass.equals(int[].class)) {
        arrayAdapterInterface1 = new IntegerArrayAdapter();
      } else if (paramClass.equals(byte[].class)) {
        arrayAdapterInterface1 = new ByteArrayAdapter();
      } else {
        throw new IllegalArgumentException("No array pool found for: " + paramClass.getSimpleName());
      } 
      this.adapters.put(paramClass, arrayAdapterInterface1);
    } 
    return (ArrayAdapterInterface)arrayAdapterInterface1;
  }
  
  private <T> T getArrayForKey(Key paramKey) {
    return (T)this.groupedMap.get(paramKey);
  }
  
  private <T> T getForKey(Key paramKey, Class<T> paramClass) {
    ArrayAdapterInterface<T> arrayAdapterInterface = getAdapterFromType(paramClass);
    T t2 = (T)getArrayForKey(paramKey);
    if (t2 != null) {
      this.currentSize -= arrayAdapterInterface.getArrayLength(t2) * arrayAdapterInterface.getElementSizeInBytes();
      decrementArrayOfSize(arrayAdapterInterface.getArrayLength(t2), paramClass);
    } 
    T t1 = t2;
    if (t2 == null) {
      if (Log.isLoggable(arrayAdapterInterface.getTag(), 2))
        Log.v(arrayAdapterInterface.getTag(), "Allocated " + paramKey.size + " bytes"); 
      t1 = arrayAdapterInterface.newArray(paramKey.size);
    } 
    return t1;
  }
  
  private NavigableMap<Integer, Integer> getSizesForAdapter(Class<?> paramClass) {
    NavigableMap<Object, Object> navigableMap2 = (NavigableMap)this.sortedSizes.get(paramClass);
    NavigableMap<Object, Object> navigableMap1 = navigableMap2;
    if (navigableMap2 == null) {
      navigableMap1 = new TreeMap<>();
      this.sortedSizes.put(paramClass, navigableMap1);
    } 
    return (NavigableMap)navigableMap1;
  }
  
  private boolean isNoMoreThanHalfFull() {
    int i = this.currentSize;
    return (i == 0 || this.maxSize / i >= 2);
  }
  
  private boolean isSmallEnoughForReuse(int paramInt) {
    boolean bool;
    if (paramInt <= this.maxSize / 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean mayFillRequest(int paramInt, Integer paramInteger) {
    boolean bool;
    if (paramInteger != null && (isNoMoreThanHalfFull() || paramInteger.intValue() <= paramInt * 8)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void clearMemory() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: invokespecial evictToSize : (I)V
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public <T> T get(int paramInt, Class<T> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: invokespecial getSizesForAdapter : (Ljava/lang/Class;)Ljava/util/NavigableMap;
    //   7: iload_1
    //   8: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   11: invokeinterface ceilingKey : (Ljava/lang/Object;)Ljava/lang/Object;
    //   16: checkcast java/lang/Integer
    //   19: astore_3
    //   20: aload_0
    //   21: iload_1
    //   22: aload_3
    //   23: invokespecial mayFillRequest : (ILjava/lang/Integer;)Z
    //   26: ifeq -> 45
    //   29: aload_0
    //   30: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   33: aload_3
    //   34: invokevirtual intValue : ()I
    //   37: aload_2
    //   38: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   41: astore_3
    //   42: goto -> 55
    //   45: aload_0
    //   46: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   49: iload_1
    //   50: aload_2
    //   51: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   54: astore_3
    //   55: aload_0
    //   56: aload_3
    //   57: aload_2
    //   58: invokespecial getForKey : (Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;Ljava/lang/Class;)Ljava/lang/Object;
    //   61: astore_2
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_2
    //   65: areturn
    //   66: astore_2
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	66	finally
    //   45	55	66	finally
    //   55	62	66	finally
  }
  
  int getCurrentSize() {
    int i = 0;
    for (Class<?> clazz : this.sortedSizes.keySet()) {
      for (Integer integer : ((NavigableMap)this.sortedSizes.get(clazz)).keySet()) {
        ArrayAdapterInterface<?> arrayAdapterInterface = getAdapterFromType(clazz);
        i += integer.intValue() * ((Integer)((NavigableMap)this.sortedSizes.get(clazz)).get(integer)).intValue() * arrayAdapterInterface.getElementSizeInBytes();
      } 
    } 
    return i;
  }
  
  public <T> T getExact(int paramInt, Class<T> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   7: iload_1
    //   8: aload_2
    //   9: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   12: aload_2
    //   13: invokespecial getForKey : (Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;Ljava/lang/Class;)Ljava/lang/Object;
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: areturn
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  public <T> void put(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getClass : ()Ljava/lang/Class;
    //   6: astore #7
    //   8: aload_0
    //   9: aload #7
    //   11: invokespecial getAdapterFromType : (Ljava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayAdapterInterface;
    //   14: astore #6
    //   16: aload #6
    //   18: aload_1
    //   19: invokeinterface getArrayLength : (Ljava/lang/Object;)I
    //   24: istore_2
    //   25: aload #6
    //   27: invokeinterface getElementSizeInBytes : ()I
    //   32: iload_2
    //   33: imul
    //   34: istore_3
    //   35: aload_0
    //   36: iload_3
    //   37: invokespecial isSmallEnoughForReuse : (I)Z
    //   40: istore #5
    //   42: iload #5
    //   44: ifne -> 50
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: aload_0
    //   51: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   54: iload_2
    //   55: aload #7
    //   57: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   60: astore #6
    //   62: aload_0
    //   63: getfield groupedMap : Lcom/bumptech/glide/load/engine/bitmap_recycle/GroupedLinkedMap;
    //   66: aload #6
    //   68: aload_1
    //   69: invokevirtual put : (Lcom/bumptech/glide/load/engine/bitmap_recycle/Poolable;Ljava/lang/Object;)V
    //   72: aload_0
    //   73: aload #7
    //   75: invokespecial getSizesForAdapter : (Ljava/lang/Class;)Ljava/util/NavigableMap;
    //   78: astore_1
    //   79: aload_1
    //   80: aload #6
    //   82: getfield size : I
    //   85: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   88: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   93: checkcast java/lang/Integer
    //   96: astore #7
    //   98: aload #6
    //   100: getfield size : I
    //   103: istore #4
    //   105: iconst_1
    //   106: istore_2
    //   107: aload #7
    //   109: ifnonnull -> 115
    //   112: goto -> 123
    //   115: iconst_1
    //   116: aload #7
    //   118: invokevirtual intValue : ()I
    //   121: iadd
    //   122: istore_2
    //   123: aload_1
    //   124: iload #4
    //   126: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   129: iload_2
    //   130: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   133: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   138: pop
    //   139: aload_0
    //   140: aload_0
    //   141: getfield currentSize : I
    //   144: iload_3
    //   145: iadd
    //   146: putfield currentSize : I
    //   149: aload_0
    //   150: invokespecial evict : ()V
    //   153: aload_0
    //   154: monitorexit
    //   155: return
    //   156: astore_1
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_1
    //   160: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	156	finally
    //   50	105	156	finally
    //   115	123	156	finally
    //   123	153	156	finally
  }
  
  @Deprecated
  public <T> void put(T paramT, Class<T> paramClass) {
    put(paramT);
  }
  
  public void trimMemory(int paramInt) {
    /* monitor enter ThisExpression{ObjectType{com/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool}} */
    if (paramInt >= 40) {
      try {
        clearMemory();
      } finally {
        Exception exception;
      } 
    } else if (paramInt >= 20 || paramInt == 15) {
      evictToSize(this.maxSize / 2);
    } 
    /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool}} */
  }
  
  private static final class Key implements Poolable {
    private Class<?> arrayClass;
    
    private final LruArrayPool.KeyPool pool;
    
    int size;
    
    Key(LruArrayPool.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      if (bool) {
        param1Object = param1Object;
        bool = bool1;
        if (this.size == ((Key)param1Object).size) {
          bool = bool1;
          if (this.arrayClass == ((Key)param1Object).arrayClass)
            bool = true; 
        } 
        return bool;
      } 
      return false;
    }
    
    public int hashCode() {
      byte b;
      int i = this.size;
      Class<?> clazz = this.arrayClass;
      if (clazz != null) {
        b = clazz.hashCode();
      } else {
        b = 0;
      } 
      return i * 31 + b;
    }
    
    void init(int param1Int, Class<?> param1Class) {
      this.size = param1Int;
      this.arrayClass = param1Class;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      return "Key{size=" + this.size + "array=" + this.arrayClass + '}';
    }
  }
  
  private static final class KeyPool extends BaseKeyPool<Key> {
    protected LruArrayPool.Key create() {
      return new LruArrayPool.Key(this);
    }
    
    LruArrayPool.Key get(int param1Int, Class<?> param1Class) {
      LruArrayPool.Key key = get();
      key.init(param1Int, param1Class);
      return key;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\LruArrayPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */