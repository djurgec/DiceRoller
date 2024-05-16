package androidx.emoji2.text.flatbuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class FlatBufferBuilder {
  static final boolean $assertionsDisabled = false;
  
  ByteBuffer bb;
  
  ByteBufferFactory bb_factory;
  
  boolean finished = false;
  
  boolean force_defaults = false;
  
  int minalign = 1;
  
  boolean nested = false;
  
  int num_vtables = 0;
  
  int object_start;
  
  int space;
  
  final Utf8 utf8;
  
  int vector_num_elems = 0;
  
  int[] vtable = null;
  
  int vtable_in_use = 0;
  
  int[] vtables = new int[16];
  
  public FlatBufferBuilder() {
    this(1024);
  }
  
  public FlatBufferBuilder(int paramInt) {
    this(paramInt, HeapByteBufferFactory.INSTANCE, null, Utf8.getDefault());
  }
  
  public FlatBufferBuilder(int paramInt, ByteBufferFactory paramByteBufferFactory) {
    this(paramInt, paramByteBufferFactory, null, Utf8.getDefault());
  }
  
  public FlatBufferBuilder(int paramInt, ByteBufferFactory paramByteBufferFactory, ByteBuffer paramByteBuffer, Utf8 paramUtf8) {
    int i = paramInt;
    if (paramInt <= 0)
      i = 1; 
    this.bb_factory = paramByteBufferFactory;
    if (paramByteBuffer != null) {
      this.bb = paramByteBuffer;
      paramByteBuffer.clear();
      this.bb.order(ByteOrder.LITTLE_ENDIAN);
    } else {
      this.bb = paramByteBufferFactory.newByteBuffer(i);
    } 
    this.utf8 = paramUtf8;
    this.space = this.bb.capacity();
  }
  
  public FlatBufferBuilder(ByteBuffer paramByteBuffer) {
    this(paramByteBuffer, new HeapByteBufferFactory());
  }
  
  public FlatBufferBuilder(ByteBuffer paramByteBuffer, ByteBufferFactory paramByteBufferFactory) {
    this(paramByteBuffer.capacity(), paramByteBufferFactory, paramByteBuffer, Utf8.getDefault());
  }
  
  @Deprecated
  private int dataStart() {
    finished();
    return this.space;
  }
  
  static ByteBuffer growByteBuffer(ByteBuffer paramByteBuffer, ByteBufferFactory paramByteBufferFactory) {
    int i = paramByteBuffer.capacity();
    if ((0xC0000000 & i) == 0) {
      int j;
      if (i == 0) {
        j = 1;
      } else {
        j = i << 1;
      } 
      paramByteBuffer.position(0);
      ByteBuffer byteBuffer = paramByteBufferFactory.newByteBuffer(j);
      byteBuffer.position(byteBuffer.clear().capacity() - i);
      byteBuffer.put(paramByteBuffer);
      return byteBuffer;
    } 
    throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
  }
  
  public static boolean isFieldPresent(Table paramTable, int paramInt) {
    boolean bool;
    if (paramTable.__offset(paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void Nested(int paramInt) {
    if (paramInt == offset())
      return; 
    throw new AssertionError("FlatBuffers: struct must be serialized inline.");
  }
  
  public void addBoolean(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    if (this.force_defaults || paramBoolean1 != paramBoolean2) {
      addBoolean(paramBoolean1);
      slot(paramInt);
    } 
  }
  
  public void addBoolean(boolean paramBoolean) {
    prep(1, 0);
    putBoolean(paramBoolean);
  }
  
  public void addByte(byte paramByte) {
    prep(1, 0);
    putByte(paramByte);
  }
  
  public void addByte(int paramInt1, byte paramByte, int paramInt2) {
    if (this.force_defaults || paramByte != paramInt2) {
      addByte(paramByte);
      slot(paramInt1);
    } 
  }
  
  public void addDouble(double paramDouble) {
    prep(8, 0);
    putDouble(paramDouble);
  }
  
  public void addDouble(int paramInt, double paramDouble1, double paramDouble2) {
    if (this.force_defaults || paramDouble1 != paramDouble2) {
      addDouble(paramDouble1);
      slot(paramInt);
    } 
  }
  
  public void addFloat(float paramFloat) {
    prep(4, 0);
    putFloat(paramFloat);
  }
  
  public void addFloat(int paramInt, float paramFloat, double paramDouble) {
    if (this.force_defaults || paramFloat != paramDouble) {
      addFloat(paramFloat);
      slot(paramInt);
    } 
  }
  
  public void addInt(int paramInt) {
    prep(4, 0);
    putInt(paramInt);
  }
  
  public void addInt(int paramInt1, int paramInt2, int paramInt3) {
    if (this.force_defaults || paramInt2 != paramInt3) {
      addInt(paramInt2);
      slot(paramInt1);
    } 
  }
  
  public void addLong(int paramInt, long paramLong1, long paramLong2) {
    if (this.force_defaults || paramLong1 != paramLong2) {
      addLong(paramLong1);
      slot(paramInt);
    } 
  }
  
  public void addLong(long paramLong) {
    prep(8, 0);
    putLong(paramLong);
  }
  
  public void addOffset(int paramInt) {
    prep(4, 0);
    if (paramInt <= offset()) {
      putInt(offset() - paramInt + 4);
      return;
    } 
    throw new AssertionError();
  }
  
  public void addOffset(int paramInt1, int paramInt2, int paramInt3) {
    if (this.force_defaults || paramInt2 != paramInt3) {
      addOffset(paramInt2);
      slot(paramInt1);
    } 
  }
  
  public void addShort(int paramInt1, short paramShort, int paramInt2) {
    if (this.force_defaults || paramShort != paramInt2) {
      addShort(paramShort);
      slot(paramInt1);
    } 
  }
  
  public void addShort(short paramShort) {
    prep(2, 0);
    putShort(paramShort);
  }
  
  public void addStruct(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 != paramInt3) {
      Nested(paramInt2);
      slot(paramInt1);
    } 
  }
  
  public void clear() {
    this.space = this.bb.capacity();
    this.bb.clear();
    this.minalign = 1;
    while (true) {
      int i = this.vtable_in_use;
      if (i > 0) {
        int[] arrayOfInt = this.vtable;
        this.vtable_in_use = --i;
        arrayOfInt[i] = 0;
        continue;
      } 
      this.vtable_in_use = 0;
      this.nested = false;
      this.finished = false;
      this.object_start = 0;
      this.num_vtables = 0;
      this.vector_num_elems = 0;
      return;
    } 
  }
  
  public int createByteVector(ByteBuffer paramByteBuffer) {
    int i = paramByteBuffer.remaining();
    startVector(1, i, 1);
    ByteBuffer byteBuffer = this.bb;
    i = this.space - i;
    this.space = i;
    byteBuffer.position(i);
    this.bb.put(paramByteBuffer);
    return endVector();
  }
  
  public int createByteVector(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    startVector(1, i, 1);
    ByteBuffer byteBuffer = this.bb;
    i = this.space - i;
    this.space = i;
    byteBuffer.position(i);
    this.bb.put(paramArrayOfbyte);
    return endVector();
  }
  
  public int createByteVector(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    startVector(1, paramInt2, 1);
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - paramInt2;
    this.space = i;
    byteBuffer.position(i);
    this.bb.put(paramArrayOfbyte, paramInt1, paramInt2);
    return endVector();
  }
  
  public <T extends Table> int createSortedVectorOfTables(T paramT, int[] paramArrayOfint) {
    paramT.sortTables(paramArrayOfint, this.bb);
    return createVectorOfTables(paramArrayOfint);
  }
  
  public int createString(CharSequence paramCharSequence) {
    int i = this.utf8.encodedLength(paramCharSequence);
    addByte((byte)0);
    startVector(1, i, 1);
    ByteBuffer byteBuffer = this.bb;
    i = this.space - i;
    this.space = i;
    byteBuffer.position(i);
    this.utf8.encodeUtf8(paramCharSequence, this.bb);
    return endVector();
  }
  
  public int createString(ByteBuffer paramByteBuffer) {
    int i = paramByteBuffer.remaining();
    addByte((byte)0);
    startVector(1, i, 1);
    ByteBuffer byteBuffer = this.bb;
    i = this.space - i;
    this.space = i;
    byteBuffer.position(i);
    this.bb.put(paramByteBuffer);
    return endVector();
  }
  
  public ByteBuffer createUnintializedVector(int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt1 * paramInt2;
    startVector(paramInt1, paramInt2, paramInt3);
    ByteBuffer byteBuffer = this.bb;
    paramInt1 = this.space - i;
    this.space = paramInt1;
    byteBuffer.position(paramInt1);
    byteBuffer = this.bb.slice().order(ByteOrder.LITTLE_ENDIAN);
    byteBuffer.limit(i);
    return byteBuffer;
  }
  
  public int createVectorOfTables(int[] paramArrayOfint) {
    notNested();
    startVector(4, paramArrayOfint.length, 4);
    for (int i = paramArrayOfint.length - 1; i >= 0; i--)
      addOffset(paramArrayOfint[i]); 
    return endVector();
  }
  
  public ByteBuffer dataBuffer() {
    finished();
    return this.bb;
  }
  
  public int endTable() {
    if (this.vtable != null && this.nested) {
      addInt(0);
      int k = offset();
      int i;
      for (i = this.vtable_in_use - 1; i >= 0 && this.vtable[i] == 0; i--);
      int j = i;
      while (true) {
        int m = j;
        if (m >= 0) {
          int[] arrayOfInt = this.vtable;
          if (arrayOfInt[m] != 0) {
            j = k - arrayOfInt[m];
          } else {
            j = 0;
          } 
          addShort((short)j);
          j = m - 1;
          continue;
        } 
        addShort((short)(k - this.object_start));
        addShort((short)((i + 1 + 2) * 2));
        m = 0;
        i = 0;
        label45: while (true) {
          j = m;
          if (i < this.num_vtables) {
            int i1 = this.bb.capacity() - this.vtables[i];
            int n = this.space;
            short s = this.bb.getShort(i1);
            if (s == this.bb.getShort(n)) {
              for (j = 2; j < s; j += 2) {
                if (this.bb.getShort(i1 + j) != this.bb.getShort(n + j))
                  continue label45; 
              } 
              j = this.vtables[i];
              break;
            } 
            i++;
            continue;
          } 
          break;
        } 
        if (j != 0) {
          i = this.bb.capacity() - k;
          this.space = i;
          this.bb.putInt(i, j - k);
        } else {
          i = this.num_vtables;
          int[] arrayOfInt = this.vtables;
          if (i == arrayOfInt.length)
            this.vtables = Arrays.copyOf(arrayOfInt, i * 2); 
          arrayOfInt = this.vtables;
          i = this.num_vtables;
          this.num_vtables = i + 1;
          arrayOfInt[i] = offset();
          ByteBuffer byteBuffer = this.bb;
          byteBuffer.putInt(byteBuffer.capacity() - k, offset() - k);
        } 
        this.nested = false;
        return k;
      } 
    } 
    throw new AssertionError("FlatBuffers: endTable called without startTable");
  }
  
  public int endVector() {
    if (this.nested) {
      this.nested = false;
      putInt(this.vector_num_elems);
      return offset();
    } 
    throw new AssertionError("FlatBuffers: endVector called without startVector");
  }
  
  public void finish(int paramInt) {
    finish(paramInt, false);
  }
  
  public void finish(int paramInt, String paramString) {
    finish(paramInt, paramString, false);
  }
  
  protected void finish(int paramInt, String paramString, boolean paramBoolean) {
    byte b;
    int i = this.minalign;
    if (paramBoolean) {
      b = 4;
    } else {
      b = 0;
    } 
    prep(i, b + 8);
    if (paramString.length() == 4) {
      for (b = 3; b >= 0; b--)
        addByte((byte)paramString.charAt(b)); 
      finish(paramInt, paramBoolean);
      return;
    } 
    throw new AssertionError("FlatBuffers: file identifier must be length 4");
  }
  
  protected void finish(int paramInt, boolean paramBoolean) {
    byte b;
    int i = this.minalign;
    if (paramBoolean) {
      b = 4;
    } else {
      b = 0;
    } 
    prep(i, b + 4);
    addOffset(paramInt);
    if (paramBoolean)
      addInt(this.bb.capacity() - this.space); 
    this.bb.position(this.space);
    this.finished = true;
  }
  
  public void finishSizePrefixed(int paramInt) {
    finish(paramInt, true);
  }
  
  public void finishSizePrefixed(int paramInt, String paramString) {
    finish(paramInt, paramString, true);
  }
  
  public void finished() {
    if (this.finished)
      return; 
    throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
  }
  
  public FlatBufferBuilder forceDefaults(boolean paramBoolean) {
    this.force_defaults = paramBoolean;
    return this;
  }
  
  public FlatBufferBuilder init(ByteBuffer paramByteBuffer, ByteBufferFactory paramByteBufferFactory) {
    this.bb_factory = paramByteBufferFactory;
    this.bb = paramByteBuffer;
    paramByteBuffer.clear();
    this.bb.order(ByteOrder.LITTLE_ENDIAN);
    this.minalign = 1;
    this.space = this.bb.capacity();
    this.vtable_in_use = 0;
    this.nested = false;
    this.finished = false;
    this.object_start = 0;
    this.num_vtables = 0;
    this.vector_num_elems = 0;
    return this;
  }
  
  public void notNested() {
    if (!this.nested)
      return; 
    throw new AssertionError("FlatBuffers: object serialization must not be nested.");
  }
  
  public int offset() {
    return this.bb.capacity() - this.space;
  }
  
  public void pad(int paramInt) {
    for (byte b = 0; b < paramInt; b++) {
      ByteBuffer byteBuffer = this.bb;
      int i = this.space - 1;
      this.space = i;
      byteBuffer.put(i, (byte)0);
    } 
  }
  
  public void prep(int paramInt1, int paramInt2) {
    if (paramInt1 > this.minalign)
      this.minalign = paramInt1; 
    int i = (this.bb.capacity() - this.space + paramInt2 ^ 0xFFFFFFFF) + 1 & paramInt1 - 1;
    while (this.space < i + paramInt1 + paramInt2) {
      int j = this.bb.capacity();
      ByteBuffer byteBuffer1 = this.bb;
      ByteBuffer byteBuffer2 = growByteBuffer(byteBuffer1, this.bb_factory);
      this.bb = byteBuffer2;
      if (byteBuffer1 != byteBuffer2)
        this.bb_factory.releaseByteBuffer(byteBuffer1); 
      this.space += this.bb.capacity() - j;
    } 
    pad(i);
  }
  
  public void putBoolean(boolean paramBoolean) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 1;
    this.space = i;
    byteBuffer.put(i, (byte)paramBoolean);
  }
  
  public void putByte(byte paramByte) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 1;
    this.space = i;
    byteBuffer.put(i, paramByte);
  }
  
  public void putDouble(double paramDouble) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 8;
    this.space = i;
    byteBuffer.putDouble(i, paramDouble);
  }
  
  public void putFloat(float paramFloat) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 4;
    this.space = i;
    byteBuffer.putFloat(i, paramFloat);
  }
  
  public void putInt(int paramInt) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 4;
    this.space = i;
    byteBuffer.putInt(i, paramInt);
  }
  
  public void putLong(long paramLong) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 8;
    this.space = i;
    byteBuffer.putLong(i, paramLong);
  }
  
  public void putShort(short paramShort) {
    ByteBuffer byteBuffer = this.bb;
    int i = this.space - 2;
    this.space = i;
    byteBuffer.putShort(i, paramShort);
  }
  
  public void required(int paramInt1, int paramInt2) {
    paramInt1 = this.bb.capacity() - paramInt1;
    int i = this.bb.getInt(paramInt1);
    if (this.bb.getShort(paramInt1 - i + paramInt2) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0)
      return; 
    throw new AssertionError("FlatBuffers: field " + paramInt2 + " must be set");
  }
  
  public byte[] sizedByteArray() {
    return sizedByteArray(this.space, this.bb.capacity() - this.space);
  }
  
  public byte[] sizedByteArray(int paramInt1, int paramInt2) {
    finished();
    byte[] arrayOfByte = new byte[paramInt2];
    this.bb.position(paramInt1);
    this.bb.get(arrayOfByte);
    return arrayOfByte;
  }
  
  public InputStream sizedInputStream() {
    finished();
    ByteBuffer byteBuffer = this.bb.duplicate();
    byteBuffer.position(this.space);
    byteBuffer.limit(this.bb.capacity());
    return new ByteBufferBackedInputStream(byteBuffer);
  }
  
  public void slot(int paramInt) {
    this.vtable[paramInt] = offset();
  }
  
  public void startTable(int paramInt) {
    notNested();
    int[] arrayOfInt = this.vtable;
    if (arrayOfInt == null || arrayOfInt.length < paramInt)
      this.vtable = new int[paramInt]; 
    this.vtable_in_use = paramInt;
    Arrays.fill(this.vtable, 0, paramInt, 0);
    this.nested = true;
    this.object_start = offset();
  }
  
  public void startVector(int paramInt1, int paramInt2, int paramInt3) {
    notNested();
    this.vector_num_elems = paramInt2;
    prep(4, paramInt1 * paramInt2);
    prep(paramInt3, paramInt1 * paramInt2);
    this.nested = true;
  }
  
  static class ByteBufferBackedInputStream extends InputStream {
    ByteBuffer buf;
    
    public ByteBufferBackedInputStream(ByteBuffer param1ByteBuffer) {
      this.buf = param1ByteBuffer;
    }
    
    public int read() throws IOException {
      try {
        byte b = this.buf.get();
        return b & 0xFF;
      } catch (BufferUnderflowException bufferUnderflowException) {
        return -1;
      } 
    }
  }
  
  public static abstract class ByteBufferFactory {
    public abstract ByteBuffer newByteBuffer(int param1Int);
    
    public void releaseByteBuffer(ByteBuffer param1ByteBuffer) {}
  }
  
  public static final class HeapByteBufferFactory extends ByteBufferFactory {
    public static final HeapByteBufferFactory INSTANCE = new HeapByteBufferFactory();
    
    public ByteBuffer newByteBuffer(int param1Int) {
      return ByteBuffer.allocate(param1Int).order(ByteOrder.LITTLE_ENDIAN);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\FlatBufferBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */