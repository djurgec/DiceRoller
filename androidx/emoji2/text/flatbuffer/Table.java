package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Comparator;

public class Table {
  protected ByteBuffer bb;
  
  protected int bb_pos;
  
  Utf8 utf8 = Utf8.getDefault();
  
  private int vtable_size;
  
  private int vtable_start;
  
  protected static boolean __has_identifier(ByteBuffer paramByteBuffer, String paramString) {
    if (paramString.length() == 4) {
      for (byte b = 0; b < 4; b++) {
        if (paramString.charAt(b) != (char)paramByteBuffer.get(paramByteBuffer.position() + 4 + b))
          return false; 
      } 
      return true;
    } 
    throw new AssertionError("FlatBuffers: file identifier must be length 4");
  }
  
  protected static int __indirect(int paramInt, ByteBuffer paramByteBuffer) {
    return paramByteBuffer.getInt(paramInt) + paramInt;
  }
  
  protected static int __offset(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
    paramInt2 = paramByteBuffer.capacity() - paramInt2;
    return paramByteBuffer.getShort(paramInt2 + paramInt1 - paramByteBuffer.getInt(paramInt2)) + paramInt2;
  }
  
  protected static String __string(int paramInt, ByteBuffer paramByteBuffer, Utf8 paramUtf8) {
    paramInt += paramByteBuffer.getInt(paramInt);
    return paramUtf8.decodeUtf8(paramByteBuffer, paramInt + 4, paramByteBuffer.getInt(paramInt));
  }
  
  protected static Table __union(Table paramTable, int paramInt, ByteBuffer paramByteBuffer) {
    paramTable.__reset(__indirect(paramInt, paramByteBuffer), paramByteBuffer);
    return paramTable;
  }
  
  protected static int compareStrings(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
    int j = paramInt1 + paramByteBuffer.getInt(paramInt1);
    paramInt1 = paramInt2 + paramByteBuffer.getInt(paramInt2);
    int i = paramByteBuffer.getInt(j);
    paramInt2 = paramByteBuffer.getInt(paramInt1);
    j += 4;
    int m = paramInt1 + 4;
    int k = Math.min(i, paramInt2);
    for (paramInt1 = 0; paramInt1 < k; paramInt1++) {
      if (paramByteBuffer.get(paramInt1 + j) != paramByteBuffer.get(paramInt1 + m))
        return paramByteBuffer.get(paramInt1 + j) - paramByteBuffer.get(paramInt1 + m); 
    } 
    return i - paramInt2;
  }
  
  protected static int compareStrings(int paramInt, byte[] paramArrayOfbyte, ByteBuffer paramByteBuffer) {
    paramInt += paramByteBuffer.getInt(paramInt);
    int j = paramByteBuffer.getInt(paramInt);
    int i = paramArrayOfbyte.length;
    int k = paramInt + 4;
    int m = Math.min(j, i);
    for (paramInt = 0; paramInt < m; paramInt++) {
      if (paramByteBuffer.get(paramInt + k) != paramArrayOfbyte[paramInt])
        return paramByteBuffer.get(paramInt + k) - paramArrayOfbyte[paramInt]; 
    } 
    return j - i;
  }
  
  protected int __indirect(int paramInt) {
    return this.bb.getInt(paramInt) + paramInt;
  }
  
  protected int __offset(int paramInt) {
    if (paramInt < this.vtable_size) {
      paramInt = this.bb.getShort(this.vtable_start + paramInt);
    } else {
      paramInt = 0;
    } 
    return paramInt;
  }
  
  public void __reset() {
    __reset(0, null);
  }
  
  protected void __reset(int paramInt, ByteBuffer paramByteBuffer) {
    this.bb = paramByteBuffer;
    if (paramByteBuffer != null) {
      this.bb_pos = paramInt;
      paramInt -= paramByteBuffer.getInt(paramInt);
      this.vtable_start = paramInt;
      this.vtable_size = this.bb.getShort(paramInt);
    } else {
      this.bb_pos = 0;
      this.vtable_start = 0;
      this.vtable_size = 0;
    } 
  }
  
  protected String __string(int paramInt) {
    return __string(paramInt, this.bb, this.utf8);
  }
  
  protected Table __union(Table paramTable, int paramInt) {
    return __union(paramTable, paramInt, this.bb);
  }
  
  protected int __vector(int paramInt) {
    paramInt += this.bb_pos;
    return this.bb.getInt(paramInt) + paramInt + 4;
  }
  
  protected ByteBuffer __vector_as_bytebuffer(int paramInt1, int paramInt2) {
    paramInt1 = __offset(paramInt1);
    if (paramInt1 == 0)
      return null; 
    ByteBuffer byteBuffer = this.bb.duplicate().order(ByteOrder.LITTLE_ENDIAN);
    int i = __vector(paramInt1);
    byteBuffer.position(i);
    byteBuffer.limit(__vector_len(paramInt1) * paramInt2 + i);
    return byteBuffer;
  }
  
  protected ByteBuffer __vector_in_bytebuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    paramInt1 = __offset(paramInt1);
    if (paramInt1 == 0)
      return null; 
    int i = __vector(paramInt1);
    paramByteBuffer.rewind();
    paramByteBuffer.limit(__vector_len(paramInt1) * paramInt2 + i);
    paramByteBuffer.position(i);
    return paramByteBuffer;
  }
  
  protected int __vector_len(int paramInt) {
    int i = paramInt + this.bb_pos;
    paramInt = this.bb.getInt(i);
    return this.bb.getInt(i + paramInt);
  }
  
  public ByteBuffer getByteBuffer() {
    return this.bb;
  }
  
  protected int keysCompare(Integer paramInteger1, Integer paramInteger2, ByteBuffer paramByteBuffer) {
    return 0;
  }
  
  protected void sortTables(int[] paramArrayOfint, final ByteBuffer bb) {
    Integer[] arrayOfInteger = new Integer[paramArrayOfint.length];
    byte b;
    for (b = 0; b < paramArrayOfint.length; b++)
      arrayOfInteger[b] = Integer.valueOf(paramArrayOfint[b]); 
    Arrays.sort(arrayOfInteger, new Comparator<Integer>() {
          final Table this$0;
          
          final ByteBuffer val$bb;
          
          public int compare(Integer param1Integer1, Integer param1Integer2) {
            return Table.this.keysCompare(param1Integer1, param1Integer2, bb);
          }
        });
    for (b = 0; b < paramArrayOfint.length; b++)
      paramArrayOfint[b] = arrayOfInteger[b].intValue(); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */