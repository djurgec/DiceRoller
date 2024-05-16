package androidx.collection;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap<K, V> {
  private static final int BASE_SIZE = 4;
  
  private static final int CACHE_SIZE = 10;
  
  private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
  
  private static final boolean DEBUG = false;
  
  private static final String TAG = "ArrayMap";
  
  static Object[] mBaseCache;
  
  static int mBaseCacheSize;
  
  static Object[] mTwiceBaseCache;
  
  static int mTwiceBaseCacheSize;
  
  Object[] mArray;
  
  int[] mHashes;
  
  int mSize;
  
  public SimpleArrayMap() {
    this.mHashes = ContainerHelpers.EMPTY_INTS;
    this.mArray = ContainerHelpers.EMPTY_OBJECTS;
    this.mSize = 0;
  }
  
  public SimpleArrayMap(int paramInt) {
    if (paramInt == 0) {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      allocArrays(paramInt);
    } 
    this.mSize = 0;
  }
  
  public SimpleArrayMap(SimpleArrayMap<K, V> paramSimpleArrayMap) {
    this();
    if (paramSimpleArrayMap != null)
      putAll(paramSimpleArrayMap); 
  }
  
  private void allocArrays(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: bipush #8
    //   3: if_icmpne -> 73
    //   6: ldc androidx/collection/SimpleArrayMap
    //   8: monitorenter
    //   9: getstatic androidx/collection/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   12: astore_2
    //   13: aload_2
    //   14: ifnull -> 61
    //   17: aload_0
    //   18: aload_2
    //   19: putfield mArray : [Ljava/lang/Object;
    //   22: aload_2
    //   23: iconst_0
    //   24: aaload
    //   25: checkcast [Ljava/lang/Object;
    //   28: putstatic androidx/collection/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   31: aload_0
    //   32: aload_2
    //   33: iconst_1
    //   34: aaload
    //   35: checkcast [I
    //   38: putfield mHashes : [I
    //   41: aload_2
    //   42: iconst_1
    //   43: aconst_null
    //   44: aastore
    //   45: aload_2
    //   46: iconst_0
    //   47: aconst_null
    //   48: aastore
    //   49: getstatic androidx/collection/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   52: iconst_1
    //   53: isub
    //   54: putstatic androidx/collection/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   57: ldc androidx/collection/SimpleArrayMap
    //   59: monitorexit
    //   60: return
    //   61: ldc androidx/collection/SimpleArrayMap
    //   63: monitorexit
    //   64: goto -> 145
    //   67: astore_2
    //   68: ldc androidx/collection/SimpleArrayMap
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: iload_1
    //   74: iconst_4
    //   75: if_icmpne -> 145
    //   78: ldc androidx/collection/SimpleArrayMap
    //   80: monitorenter
    //   81: getstatic androidx/collection/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   84: astore_2
    //   85: aload_2
    //   86: ifnull -> 133
    //   89: aload_0
    //   90: aload_2
    //   91: putfield mArray : [Ljava/lang/Object;
    //   94: aload_2
    //   95: iconst_0
    //   96: aaload
    //   97: checkcast [Ljava/lang/Object;
    //   100: putstatic androidx/collection/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   103: aload_0
    //   104: aload_2
    //   105: iconst_1
    //   106: aaload
    //   107: checkcast [I
    //   110: putfield mHashes : [I
    //   113: aload_2
    //   114: iconst_1
    //   115: aconst_null
    //   116: aastore
    //   117: aload_2
    //   118: iconst_0
    //   119: aconst_null
    //   120: aastore
    //   121: getstatic androidx/collection/SimpleArrayMap.mBaseCacheSize : I
    //   124: iconst_1
    //   125: isub
    //   126: putstatic androidx/collection/SimpleArrayMap.mBaseCacheSize : I
    //   129: ldc androidx/collection/SimpleArrayMap
    //   131: monitorexit
    //   132: return
    //   133: ldc androidx/collection/SimpleArrayMap
    //   135: monitorexit
    //   136: goto -> 145
    //   139: astore_2
    //   140: ldc androidx/collection/SimpleArrayMap
    //   142: monitorexit
    //   143: aload_2
    //   144: athrow
    //   145: aload_0
    //   146: iload_1
    //   147: newarray int
    //   149: putfield mHashes : [I
    //   152: aload_0
    //   153: iload_1
    //   154: iconst_1
    //   155: ishl
    //   156: anewarray java/lang/Object
    //   159: putfield mArray : [Ljava/lang/Object;
    //   162: return
    // Exception table:
    //   from	to	target	type
    //   9	13	67	finally
    //   17	41	67	finally
    //   49	60	67	finally
    //   61	64	67	finally
    //   68	71	67	finally
    //   81	85	139	finally
    //   89	113	139	finally
    //   121	132	139	finally
    //   133	136	139	finally
    //   140	143	139	finally
  }
  
  private static int binarySearchHashes(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    try {
      return ContainerHelpers.binarySearch(paramArrayOfint, paramInt1, paramInt2);
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new ConcurrentModificationException();
    } 
  }
  
  private static void freeArrays(int[] paramArrayOfint, Object[] paramArrayOfObject, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: bipush #8
    //   4: if_icmpne -> 73
    //   7: ldc androidx/collection/SimpleArrayMap
    //   9: monitorenter
    //   10: getstatic androidx/collection/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   13: bipush #10
    //   15: if_icmpge -> 61
    //   18: aload_1
    //   19: iconst_0
    //   20: getstatic androidx/collection/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   23: aastore
    //   24: aload_1
    //   25: iconst_1
    //   26: aload_0
    //   27: aastore
    //   28: iload_2
    //   29: iconst_1
    //   30: ishl
    //   31: iconst_1
    //   32: isub
    //   33: istore_2
    //   34: iload_2
    //   35: iconst_2
    //   36: if_icmplt -> 49
    //   39: aload_1
    //   40: iload_2
    //   41: aconst_null
    //   42: aastore
    //   43: iinc #2, -1
    //   46: goto -> 34
    //   49: aload_1
    //   50: putstatic androidx/collection/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   53: getstatic androidx/collection/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   56: iconst_1
    //   57: iadd
    //   58: putstatic androidx/collection/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   61: ldc androidx/collection/SimpleArrayMap
    //   63: monitorexit
    //   64: goto -> 145
    //   67: astore_0
    //   68: ldc androidx/collection/SimpleArrayMap
    //   70: monitorexit
    //   71: aload_0
    //   72: athrow
    //   73: aload_0
    //   74: arraylength
    //   75: iconst_4
    //   76: if_icmpne -> 145
    //   79: ldc androidx/collection/SimpleArrayMap
    //   81: monitorenter
    //   82: getstatic androidx/collection/SimpleArrayMap.mBaseCacheSize : I
    //   85: bipush #10
    //   87: if_icmpge -> 133
    //   90: aload_1
    //   91: iconst_0
    //   92: getstatic androidx/collection/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   95: aastore
    //   96: aload_1
    //   97: iconst_1
    //   98: aload_0
    //   99: aastore
    //   100: iload_2
    //   101: iconst_1
    //   102: ishl
    //   103: iconst_1
    //   104: isub
    //   105: istore_2
    //   106: iload_2
    //   107: iconst_2
    //   108: if_icmplt -> 121
    //   111: aload_1
    //   112: iload_2
    //   113: aconst_null
    //   114: aastore
    //   115: iinc #2, -1
    //   118: goto -> 106
    //   121: aload_1
    //   122: putstatic androidx/collection/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   125: getstatic androidx/collection/SimpleArrayMap.mBaseCacheSize : I
    //   128: iconst_1
    //   129: iadd
    //   130: putstatic androidx/collection/SimpleArrayMap.mBaseCacheSize : I
    //   133: ldc androidx/collection/SimpleArrayMap
    //   135: monitorexit
    //   136: goto -> 145
    //   139: astore_0
    //   140: ldc androidx/collection/SimpleArrayMap
    //   142: monitorexit
    //   143: aload_0
    //   144: athrow
    //   145: return
    // Exception table:
    //   from	to	target	type
    //   10	24	67	finally
    //   49	61	67	finally
    //   61	64	67	finally
    //   68	71	67	finally
    //   82	96	139	finally
    //   121	133	139	finally
    //   133	136	139	finally
    //   140	143	139	finally
  }
  
  public void clear() {
    if (this.mSize > 0) {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      int i = this.mSize;
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
      freeArrays(arrayOfInt, arrayOfObject, i);
    } 
    if (this.mSize <= 0)
      return; 
    throw new ConcurrentModificationException();
  }
  
  public boolean containsKey(Object paramObject) {
    boolean bool;
    if (indexOfKey(paramObject) >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean containsValue(Object paramObject) {
    boolean bool;
    if (indexOfValue(paramObject) >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void ensureCapacity(int paramInt) {
    int i = this.mSize;
    if (this.mHashes.length < paramInt) {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(paramInt);
      if (this.mSize > 0) {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, i);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, i << 1);
      } 
      freeArrays(arrayOfInt, arrayOfObject, i);
    } 
    if (this.mSize == i)
      return; 
    throw new ConcurrentModificationException();
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject instanceof SimpleArrayMap) {
      SimpleArrayMap simpleArrayMap = (SimpleArrayMap)paramObject;
      if (size() != simpleArrayMap.size())
        return false; 
      byte b = 0;
      try {
        while (b < this.mSize) {
          K k = keyAt(b);
          V v = valueAt(b);
          paramObject = simpleArrayMap.get(k);
          if (v == null) {
            if (paramObject != null || !simpleArrayMap.containsKey(k))
              return false; 
          } else {
            boolean bool = v.equals(paramObject);
            if (!bool)
              return false; 
          } 
          b++;
        } 
        return true;
      } catch (NullPointerException nullPointerException) {
        return false;
      } catch (ClassCastException classCastException) {
        return false;
      } 
    } 
    if (classCastException instanceof Map) {
      Map map = (Map)classCastException;
      if (size() != map.size())
        return false; 
      byte b = 0;
      try {
        while (b < this.mSize) {
          K k = keyAt(b);
          V v = valueAt(b);
          Object object = map.get(k);
          if (v == null) {
            if (object != null || !map.containsKey(k))
              return false; 
          } else {
            boolean bool = v.equals(object);
            if (!bool)
              return false; 
          } 
          b++;
        } 
        return true;
      } catch (NullPointerException nullPointerException) {
        return false;
      } catch (ClassCastException classCastException1) {
        return false;
      } 
    } 
    return false;
  }
  
  public V get(Object paramObject) {
    return getOrDefault(paramObject, null);
  }
  
  public V getOrDefault(Object paramObject, V paramV) {
    int i = indexOfKey(paramObject);
    if (i >= 0)
      paramV = (V)this.mArray[(i << 1) + 1]; 
    return paramV;
  }
  
  public int hashCode() {
    int[] arrayOfInt = this.mHashes;
    Object[] arrayOfObject = this.mArray;
    int i = 0;
    byte b = 0;
    boolean bool = true;
    int j = this.mSize;
    while (b < j) {
      int k;
      Object object = arrayOfObject[bool];
      int m = arrayOfInt[b];
      if (object == null) {
        k = 0;
      } else {
        k = object.hashCode();
      } 
      i += m ^ k;
      b++;
      bool += true;
    } 
    return i;
  }
  
  int indexOf(Object paramObject, int paramInt) {
    int k = this.mSize;
    if (k == 0)
      return -1; 
    int j = binarySearchHashes(this.mHashes, k, paramInt);
    if (j < 0)
      return j; 
    if (paramObject.equals(this.mArray[j << 1]))
      return j; 
    int i;
    for (i = j + 1; i < k && this.mHashes[i] == paramInt; i++) {
      if (paramObject.equals(this.mArray[i << 1]))
        return i; 
    } 
    while (--j >= 0 && this.mHashes[j] == paramInt) {
      if (paramObject.equals(this.mArray[j << 1]))
        return j; 
      j--;
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  public int indexOfKey(Object paramObject) {
    int i;
    if (paramObject == null) {
      i = indexOfNull();
    } else {
      i = indexOf(paramObject, paramObject.hashCode());
    } 
    return i;
  }
  
  int indexOfNull() {
    int j = this.mSize;
    if (j == 0)
      return -1; 
    int k = binarySearchHashes(this.mHashes, j, 0);
    if (k < 0)
      return k; 
    if (this.mArray[k << 1] == null)
      return k; 
    int i;
    for (i = k + 1; i < j && this.mHashes[i] == 0; i++) {
      if (this.mArray[i << 1] == null)
        return i; 
    } 
    for (j = k - 1; j >= 0 && this.mHashes[j] == 0; j--) {
      if (this.mArray[j << 1] == null)
        return j; 
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  int indexOfValue(Object paramObject) {
    int i = this.mSize * 2;
    Object[] arrayOfObject = this.mArray;
    if (paramObject == null) {
      for (byte b = 1; b < i; b += 2) {
        if (arrayOfObject[b] == null)
          return b >> 1; 
      } 
    } else {
      for (byte b = 1; b < i; b += 2) {
        if (paramObject.equals(arrayOfObject[b]))
          return b >> 1; 
      } 
    } 
    return -1;
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.mSize <= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public K keyAt(int paramInt) {
    return (K)this.mArray[paramInt << 1];
  }
  
  public V put(K paramK, V paramV) {
    int j;
    int k = this.mSize;
    if (paramK == null) {
      j = 0;
      i = indexOfNull();
    } else {
      j = paramK.hashCode();
      i = indexOf(paramK, j);
    } 
    if (i >= 0) {
      i = (i << 1) + 1;
      Object[] arrayOfObject = this.mArray;
      paramK = (K)arrayOfObject[i];
      arrayOfObject[i] = paramV;
      return (V)paramK;
    } 
    int m = i ^ 0xFFFFFFFF;
    if (k >= this.mHashes.length) {
      i = 4;
      if (k >= 8) {
        i = (k >> 1) + k;
      } else if (k >= 4) {
        i = 8;
      } 
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(i);
      if (k == this.mSize) {
        int[] arrayOfInt1 = this.mHashes;
        if (arrayOfInt1.length > 0) {
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
          System.arraycopy(arrayOfObject, 0, this.mArray, 0, arrayOfObject.length);
        } 
        freeArrays(arrayOfInt, arrayOfObject, k);
      } else {
        throw new ConcurrentModificationException();
      } 
    } 
    if (m < k) {
      int[] arrayOfInt = this.mHashes;
      System.arraycopy(arrayOfInt, m, arrayOfInt, m + 1, k - m);
      Object[] arrayOfObject = this.mArray;
      System.arraycopy(arrayOfObject, m << 1, arrayOfObject, m + 1 << 1, this.mSize - m << 1);
    } 
    int i = this.mSize;
    if (k == i) {
      int[] arrayOfInt = this.mHashes;
      if (m < arrayOfInt.length) {
        arrayOfInt[m] = j;
        Object[] arrayOfObject = this.mArray;
        arrayOfObject[m << 1] = paramK;
        arrayOfObject[(m << 1) + 1] = paramV;
        this.mSize = i + 1;
        return null;
      } 
    } 
    throw new ConcurrentModificationException();
  }
  
  public void putAll(SimpleArrayMap<? extends K, ? extends V> paramSimpleArrayMap) {
    int i = paramSimpleArrayMap.mSize;
    ensureCapacity(this.mSize + i);
    if (this.mSize == 0) {
      if (i > 0) {
        System.arraycopy(paramSimpleArrayMap.mHashes, 0, this.mHashes, 0, i);
        System.arraycopy(paramSimpleArrayMap.mArray, 0, this.mArray, 0, i << 1);
        this.mSize = i;
      } 
    } else {
      for (byte b = 0; b < i; b++)
        put(paramSimpleArrayMap.keyAt(b), paramSimpleArrayMap.valueAt(b)); 
    } 
  }
  
  public V putIfAbsent(K paramK, V paramV) {
    V v2 = get(paramK);
    V v1 = v2;
    if (v2 == null)
      v1 = put(paramK, paramV); 
    return v1;
  }
  
  public V remove(Object paramObject) {
    int i = indexOfKey(paramObject);
    return (i >= 0) ? removeAt(i) : null;
  }
  
  public boolean remove(Object paramObject1, Object paramObject2) {
    int i = indexOfKey(paramObject1);
    if (i >= 0) {
      paramObject1 = valueAt(i);
      if (paramObject2 == paramObject1 || (paramObject2 != null && paramObject2.equals(paramObject1))) {
        removeAt(i);
        return true;
      } 
    } 
    return false;
  }
  
  public V removeAt(int paramInt) {
    Object[] arrayOfObject = this.mArray;
    Object object = arrayOfObject[(paramInt << 1) + 1];
    int i = this.mSize;
    if (i <= 1) {
      freeArrays(this.mHashes, arrayOfObject, i);
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      paramInt = 0;
    } else {
      Object[] arrayOfObject1;
      int k = i - 1;
      int[] arrayOfInt = this.mHashes;
      int m = arrayOfInt.length;
      int j = 8;
      if (m > 8 && this.mSize < arrayOfInt.length / 3) {
        if (i > 8)
          j = i + (i >> 1); 
        int[] arrayOfInt1 = this.mHashes;
        arrayOfObject1 = this.mArray;
        allocArrays(j);
        if (i == this.mSize) {
          if (paramInt > 0) {
            System.arraycopy(arrayOfInt1, 0, this.mHashes, 0, paramInt);
            System.arraycopy(arrayOfObject1, 0, this.mArray, 0, paramInt << 1);
          } 
          if (paramInt < k) {
            System.arraycopy(arrayOfInt1, paramInt + 1, this.mHashes, paramInt, k - paramInt);
            System.arraycopy(arrayOfObject1, paramInt + 1 << 1, this.mArray, paramInt << 1, k - paramInt << 1);
          } 
          paramInt = k;
        } else {
          throw new ConcurrentModificationException();
        } 
      } else {
        if (paramInt < k) {
          System.arraycopy(arrayOfObject1, paramInt + 1, arrayOfObject1, paramInt, k - paramInt);
          arrayOfObject1 = this.mArray;
          System.arraycopy(arrayOfObject1, paramInt + 1 << 1, arrayOfObject1, paramInt << 1, k - paramInt << 1);
        } 
        arrayOfObject1 = this.mArray;
        arrayOfObject1[k << 1] = null;
        arrayOfObject1[(k << 1) + 1] = null;
        paramInt = k;
      } 
    } 
    if (i == this.mSize) {
      this.mSize = paramInt;
      return (V)object;
    } 
    throw new ConcurrentModificationException();
  }
  
  public V replace(K paramK, V paramV) {
    int i = indexOfKey(paramK);
    return (i >= 0) ? setValueAt(i, paramV) : null;
  }
  
  public boolean replace(K paramK, V paramV1, V paramV2) {
    int i = indexOfKey(paramK);
    if (i >= 0) {
      paramK = (K)valueAt(i);
      if (paramK == paramV1 || (paramV1 != null && paramV1.equals(paramK))) {
        setValueAt(i, paramV2);
        return true;
      } 
    } 
    return false;
  }
  
  public V setValueAt(int paramInt, V paramV) {
    paramInt = (paramInt << 1) + 1;
    Object[] arrayOfObject = this.mArray;
    Object object = arrayOfObject[paramInt];
    arrayOfObject[paramInt] = paramV;
    return (V)object;
  }
  
  public int size() {
    return this.mSize;
  }
  
  public String toString() {
    if (isEmpty())
      return "{}"; 
    StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
    stringBuilder.append('{');
    for (byte b = 0; b < this.mSize; b++) {
      if (b > 0)
        stringBuilder.append(", "); 
      V v = (V)keyAt(b);
      if (v != this) {
        stringBuilder.append(v);
      } else {
        stringBuilder.append("(this Map)");
      } 
      stringBuilder.append('=');
      v = valueAt(b);
      if (v != this) {
        stringBuilder.append(v);
      } else {
        stringBuilder.append("(this Map)");
      } 
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public V valueAt(int paramInt) {
    return (V)this.mArray[(paramInt << 1) + 1];
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\collection\SimpleArrayMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */