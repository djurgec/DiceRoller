package androidx.collection;

class ContainerHelpers {
  static final int[] EMPTY_INTS = new int[0];
  
  static final long[] EMPTY_LONGS = new long[0];
  
  static final Object[] EMPTY_OBJECTS = new Object[0];
  
  static int binarySearch(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    int i = 0;
    while (i <= --paramInt1) {
      int k = i + paramInt1 >>> 1;
      int j = paramArrayOfint[k];
      if (j < paramInt2) {
        i = k + 1;
        continue;
      } 
      if (j > paramInt2) {
        paramInt1 = k - 1;
        continue;
      } 
      return k;
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  static int binarySearch(long[] paramArrayOflong, int paramInt, long paramLong) {
    int i = 0;
    while (i <= --paramInt) {
      int j = i + paramInt >>> 1;
      long l = paramArrayOflong[j];
      if (l < paramLong) {
        i = j + 1;
        continue;
      } 
      if (l > paramLong) {
        paramInt = j - 1;
        continue;
      } 
      return j;
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  public static boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static int idealByteArraySize(int paramInt) {
    for (byte b = 4; b < 32; b++) {
      if (paramInt <= (1 << b) - 12)
        return (1 << b) - 12; 
    } 
    return paramInt;
  }
  
  public static int idealIntArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  public static int idealLongArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 8) / 8;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\collection\ContainerHelpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */