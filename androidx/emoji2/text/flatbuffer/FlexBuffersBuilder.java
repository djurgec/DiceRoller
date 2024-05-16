package androidx.emoji2.text.flatbuffer;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FlexBuffersBuilder {
  static final boolean $assertionsDisabled = false;
  
  public static final int BUILDER_FLAG_NONE = 0;
  
  public static final int BUILDER_FLAG_SHARE_ALL = 7;
  
  public static final int BUILDER_FLAG_SHARE_KEYS = 1;
  
  public static final int BUILDER_FLAG_SHARE_KEYS_AND_STRINGS = 3;
  
  public static final int BUILDER_FLAG_SHARE_KEY_VECTORS = 4;
  
  public static final int BUILDER_FLAG_SHARE_STRINGS = 2;
  
  private static final int WIDTH_16 = 1;
  
  private static final int WIDTH_32 = 2;
  
  private static final int WIDTH_64 = 3;
  
  private static final int WIDTH_8 = 0;
  
  private final ReadWriteBuf bb;
  
  private boolean finished = false;
  
  private final int flags;
  
  private Comparator<Value> keyComparator = new Comparator<Value>() {
      final FlexBuffersBuilder this$0;
      
      public int compare(FlexBuffersBuilder.Value param1Value1, FlexBuffersBuilder.Value param1Value2) {
        int j = param1Value1.key;
        int i = param1Value2.key;
        while (true) {
          byte b1 = FlexBuffersBuilder.this.bb.get(j);
          byte b2 = FlexBuffersBuilder.this.bb.get(i);
          if (b1 == 0)
            return b1 - b2; 
          j++;
          i++;
          if (b1 != b2)
            return b1 - b2; 
        } 
      }
    };
  
  private final HashMap<String, Integer> keyPool = new HashMap<>();
  
  private final ArrayList<Value> stack = new ArrayList<>();
  
  private final HashMap<String, Integer> stringPool = new HashMap<>();
  
  public FlexBuffersBuilder() {
    this(256);
  }
  
  public FlexBuffersBuilder(int paramInt) {
    this(new ArrayReadWriteBuf(paramInt), 1);
  }
  
  public FlexBuffersBuilder(ReadWriteBuf paramReadWriteBuf, int paramInt) {
    this.bb = paramReadWriteBuf;
    this.flags = paramInt;
  }
  
  public FlexBuffersBuilder(ByteBuffer paramByteBuffer) {
    this(paramByteBuffer, 1);
  }
  
  @Deprecated
  public FlexBuffersBuilder(ByteBuffer paramByteBuffer, int paramInt) {
    this(new ArrayReadWriteBuf(paramByteBuffer.array()), paramInt);
  }
  
  private int align(int paramInt) {
    int i = 1 << paramInt;
    for (paramInt = Value.paddingBytes(this.bb.writePosition(), i); paramInt != 0; paramInt--)
      this.bb.put((byte)0); 
    return i;
  }
  
  private Value createKeyVector(int paramInt1, int paramInt2) {
    int i = Math.max(0, widthUInBits(paramInt2));
    int j;
    for (j = paramInt1; j < this.stack.size(); j++)
      i = Math.max(i, Value.elemWidth(4, 0, ((Value)this.stack.get(j)).key, this.bb.writePosition(), j + 1)); 
    j = align(i);
    writeInt(paramInt2, j);
    paramInt2 = this.bb.writePosition();
    while (paramInt1 < this.stack.size()) {
      if (((Value)this.stack.get(paramInt1)).key != -1) {
        writeOffset(((Value)this.stack.get(paramInt1)).key, j);
        paramInt1++;
        continue;
      } 
      throw new AssertionError();
    } 
    return new Value(-1, FlexBuffers.toTypedVector(4, 0), i, paramInt2);
  }
  
  private Value createVector(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, Value paramValue) {
    if (!paramBoolean2 || paramBoolean1) {
      int i = widthUInBits(paramInt3);
      boolean bool = false;
      int j = Math.max(0, i);
      int k = 1;
      i = j;
      if (paramValue != null) {
        i = Math.max(j, paramValue.elemWidth(this.bb.writePosition(), 0));
        k = 1 + 2;
      } 
      j = 4;
      int m;
      for (m = paramInt2; m < this.stack.size(); m++) {
        i = Math.max(i, ((Value)this.stack.get(m)).elemWidth(this.bb.writePosition(), m + k));
        if (paramBoolean1)
          if (m == paramInt2) {
            j = ((Value)this.stack.get(m)).type;
            if (!FlexBuffers.isTypedVectorElementType(j))
              throw new FlexBuffers.FlexBufferException("TypedVector does not support this element type"); 
          } else if (j != ((Value)this.stack.get(m)).type) {
            throw new AssertionError();
          }  
      } 
      if (!paramBoolean2 || FlexBuffers.isTypedVectorElementType(j)) {
        int n = align(i);
        if (paramValue != null) {
          writeOffset(paramValue.iValue, n);
          writeInt(1L << paramValue.minBitWidth, n);
        } 
        if (!paramBoolean2)
          writeInt(paramInt3, n); 
        m = this.bb.writePosition();
        for (k = paramInt2; k < this.stack.size(); k++)
          writeAny(this.stack.get(k), n); 
        if (!paramBoolean1)
          while (paramInt2 < this.stack.size()) {
            this.bb.put(((Value)this.stack.get(paramInt2)).storedPackedType(i));
            paramInt2++;
          }  
        if (paramValue != null) {
          paramInt2 = 9;
        } else if (paramBoolean1) {
          paramInt2 = bool;
          if (paramBoolean2)
            paramInt2 = paramInt3; 
          paramInt2 = FlexBuffers.toTypedVector(j, paramInt2);
        } else {
          paramInt2 = 10;
        } 
        return new Value(paramInt1, paramInt2, i, m);
      } 
      throw new AssertionError();
    } 
    throw new AssertionError();
  }
  
  private int putKey(String paramString) {
    if (paramString == null)
      return -1; 
    int i = this.bb.writePosition();
    if ((this.flags & 0x1) != 0) {
      byte[] arrayOfByte;
      Integer integer = this.keyPool.get(paramString);
      if (integer == null) {
        arrayOfByte = paramString.getBytes(StandardCharsets.UTF_8);
        this.bb.put(arrayOfByte, 0, arrayOfByte.length);
        this.bb.put((byte)0);
        this.keyPool.put(paramString, Integer.valueOf(i));
      } else {
        i = arrayOfByte.intValue();
      } 
    } else {
      byte[] arrayOfByte = paramString.getBytes(StandardCharsets.UTF_8);
      this.bb.put(arrayOfByte, 0, arrayOfByte.length);
      this.bb.put((byte)0);
      this.keyPool.put(paramString, Integer.valueOf(i));
    } 
    return i;
  }
  
  private void putUInt(String paramString, long paramLong) {
    Value value;
    int j = putKey(paramString);
    int i = widthUInBits(paramLong);
    if (i == 0) {
      value = Value.uInt8(j, (int)paramLong);
    } else if (i == 1) {
      value = Value.uInt16(j, (int)paramLong);
    } else if (i == 2) {
      value = Value.uInt32(j, (int)paramLong);
    } else {
      value = Value.uInt64(j, paramLong);
    } 
    this.stack.add(value);
  }
  
  private void putUInt64(String paramString, long paramLong) {
    this.stack.add(Value.uInt64(putKey(paramString), paramLong));
  }
  
  static int widthUInBits(long paramLong) {
    return (paramLong <= FlexBuffers.Unsigned.byteToUnsignedInt((byte)-1)) ? 0 : ((paramLong <= FlexBuffers.Unsigned.shortToUnsignedInt((short)-1)) ? 1 : ((paramLong <= FlexBuffers.Unsigned.intToUnsignedLong(-1)) ? 2 : 3));
  }
  
  private void writeAny(Value paramValue, int paramInt) {
    switch (paramValue.type) {
      default:
        writeOffset(paramValue.iValue, paramInt);
        return;
      case 3:
        writeDouble(paramValue.dValue, paramInt);
        return;
      case 0:
      case 1:
      case 2:
      case 26:
        break;
    } 
    writeInt(paramValue.iValue, paramInt);
  }
  
  private Value writeBlob(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, boolean paramBoolean) {
    int i = widthUInBits(paramArrayOfbyte.length);
    int j = align(i);
    writeInt(paramArrayOfbyte.length, j);
    j = this.bb.writePosition();
    this.bb.put(paramArrayOfbyte, 0, paramArrayOfbyte.length);
    if (paramBoolean)
      this.bb.put((byte)0); 
    return Value.blob(paramInt1, j, paramInt2, i);
  }
  
  private void writeDouble(double paramDouble, int paramInt) {
    if (paramInt == 4) {
      this.bb.putFloat((float)paramDouble);
    } else if (paramInt == 8) {
      this.bb.putDouble(paramDouble);
    } 
  }
  
  private void writeInt(long paramLong, int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 8:
        this.bb.putLong(paramLong);
      case 4:
        this.bb.putInt((int)paramLong);
      case 2:
        this.bb.putShort((short)(int)paramLong);
      case 1:
        break;
    } 
    this.bb.put((byte)(int)paramLong);
  }
  
  private void writeOffset(long paramLong, int paramInt) {
    int i = (int)(this.bb.writePosition() - paramLong);
    if (paramInt == 8 || i < 1L << paramInt * 8) {
      writeInt(i, paramInt);
      return;
    } 
    throw new AssertionError();
  }
  
  private Value writeString(int paramInt, String paramString) {
    return writeBlob(paramInt, paramString.getBytes(StandardCharsets.UTF_8), 5, true);
  }
  
  public int endMap(String paramString, int paramInt) {
    int i = putKey(paramString);
    ArrayList<Value> arrayList = this.stack;
    Collections.sort(arrayList.subList(paramInt, arrayList.size()), this.keyComparator);
    Value value = createKeyVector(paramInt, this.stack.size() - paramInt);
    value = createVector(i, paramInt, this.stack.size() - paramInt, false, false, value);
    while (this.stack.size() > paramInt) {
      ArrayList<Value> arrayList1 = this.stack;
      arrayList1.remove(arrayList1.size() - 1);
    } 
    this.stack.add(value);
    return (int)value.iValue;
  }
  
  public int endVector(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    Value value = createVector(putKey(paramString), paramInt, this.stack.size() - paramInt, paramBoolean1, paramBoolean2, null);
    while (this.stack.size() > paramInt) {
      ArrayList<Value> arrayList = this.stack;
      arrayList.remove(arrayList.size() - 1);
    } 
    this.stack.add(value);
    return (int)value.iValue;
  }
  
  public ByteBuffer finish() {
    if (this.stack.size() == 1) {
      int i = align(((Value)this.stack.get(0)).elemWidth(this.bb.writePosition(), 0));
      writeAny(this.stack.get(0), i);
      this.bb.put(((Value)this.stack.get(0)).storedPackedType());
      this.bb.put((byte)i);
      this.finished = true;
      return ByteBuffer.wrap(this.bb.data(), 0, this.bb.writePosition());
    } 
    throw new AssertionError();
  }
  
  public ReadWriteBuf getBuffer() {
    if (this.finished)
      return this.bb; 
    throw new AssertionError();
  }
  
  public int putBlob(String paramString, byte[] paramArrayOfbyte) {
    Value value = writeBlob(putKey(paramString), paramArrayOfbyte, 25, false);
    this.stack.add(value);
    return (int)value.iValue;
  }
  
  public int putBlob(byte[] paramArrayOfbyte) {
    return putBlob(null, paramArrayOfbyte);
  }
  
  public void putBoolean(String paramString, boolean paramBoolean) {
    this.stack.add(Value.bool(putKey(paramString), paramBoolean));
  }
  
  public void putBoolean(boolean paramBoolean) {
    putBoolean(null, paramBoolean);
  }
  
  public void putFloat(double paramDouble) {
    putFloat((String)null, paramDouble);
  }
  
  public void putFloat(float paramFloat) {
    putFloat((String)null, paramFloat);
  }
  
  public void putFloat(String paramString, double paramDouble) {
    this.stack.add(Value.float64(putKey(paramString), paramDouble));
  }
  
  public void putFloat(String paramString, float paramFloat) {
    this.stack.add(Value.float32(putKey(paramString), paramFloat));
  }
  
  public void putInt(int paramInt) {
    putInt((String)null, paramInt);
  }
  
  public void putInt(long paramLong) {
    putInt((String)null, paramLong);
  }
  
  public void putInt(String paramString, int paramInt) {
    putInt(paramString, paramInt);
  }
  
  public void putInt(String paramString, long paramLong) {
    int i = putKey(paramString);
    if (-128L <= paramLong && paramLong <= 127L) {
      this.stack.add(Value.int8(i, (int)paramLong));
    } else if (-32768L <= paramLong && paramLong <= 32767L) {
      this.stack.add(Value.int16(i, (int)paramLong));
    } else if (-2147483648L <= paramLong && paramLong <= 2147483647L) {
      this.stack.add(Value.int32(i, (int)paramLong));
    } else {
      this.stack.add(Value.int64(i, paramLong));
    } 
  }
  
  public int putString(String paramString) {
    return putString(null, paramString);
  }
  
  public int putString(String paramString1, String paramString2) {
    int i = putKey(paramString1);
    if ((this.flags & 0x2) != 0) {
      Value value1;
      Integer integer = this.stringPool.get(paramString2);
      if (integer == null) {
        value1 = writeString(i, paramString2);
        this.stringPool.put(paramString2, Integer.valueOf((int)value1.iValue));
        this.stack.add(value1);
        return (int)value1.iValue;
      } 
      int j = widthUInBits(paramString2.length());
      this.stack.add(Value.blob(i, value1.intValue(), 5, j));
      return value1.intValue();
    } 
    Value value = writeString(i, paramString2);
    this.stack.add(value);
    return (int)value.iValue;
  }
  
  public void putUInt(int paramInt) {
    putUInt(null, paramInt);
  }
  
  public void putUInt(long paramLong) {
    putUInt(null, paramLong);
  }
  
  public void putUInt64(BigInteger paramBigInteger) {
    putUInt64(null, paramBigInteger.longValue());
  }
  
  public int startMap() {
    return this.stack.size();
  }
  
  public int startVector() {
    return this.stack.size();
  }
  
  private static class Value {
    static final boolean $assertionsDisabled = false;
    
    final double dValue;
    
    long iValue;
    
    int key;
    
    final int minBitWidth;
    
    final int type;
    
    Value(int param1Int1, int param1Int2, int param1Int3, double param1Double) {
      this.key = param1Int1;
      this.type = param1Int2;
      this.minBitWidth = param1Int3;
      this.dValue = param1Double;
      this.iValue = Long.MIN_VALUE;
    }
    
    Value(int param1Int1, int param1Int2, int param1Int3, long param1Long) {
      this.key = param1Int1;
      this.type = param1Int2;
      this.minBitWidth = param1Int3;
      this.iValue = param1Long;
      this.dValue = Double.MIN_VALUE;
    }
    
    static Value blob(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return new Value(param1Int1, param1Int3, param1Int4, param1Int2);
    }
    
    static Value bool(int param1Int, boolean param1Boolean) {
      long l;
      if (param1Boolean) {
        l = 1L;
      } else {
        l = 0L;
      } 
      return new Value(param1Int, 26, 0, l);
    }
    
    private int elemWidth(int param1Int1, int param1Int2) {
      return elemWidth(this.type, this.minBitWidth, this.iValue, param1Int1, param1Int2);
    }
    
    private static int elemWidth(int param1Int1, int param1Int2, long param1Long, int param1Int3, int param1Int4) {
      if (FlexBuffers.isTypeInline(param1Int1))
        return param1Int2; 
      for (param1Int1 = 1; param1Int1 <= 32; param1Int1 *= 2) {
        param1Int2 = FlexBuffersBuilder.widthUInBits((int)((paddingBytes(param1Int3, param1Int1) + param1Int3 + param1Int4 * param1Int1) - param1Long));
        if (1L << param1Int2 == param1Int1)
          return param1Int2; 
      } 
      throw new AssertionError();
    }
    
    static Value float32(int param1Int, float param1Float) {
      return new Value(param1Int, 3, 2, param1Float);
    }
    
    static Value float64(int param1Int, double param1Double) {
      return new Value(param1Int, 3, 3, param1Double);
    }
    
    static Value int16(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 1, 1, param1Int2);
    }
    
    static Value int32(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 1, 2, param1Int2);
    }
    
    static Value int64(int param1Int, long param1Long) {
      return new Value(param1Int, 1, 3, param1Long);
    }
    
    static Value int8(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 1, 0, param1Int2);
    }
    
    private static byte packedType(int param1Int1, int param1Int2) {
      return (byte)(param1Int2 << 2 | param1Int1);
    }
    
    private static int paddingBytes(int param1Int1, int param1Int2) {
      return (param1Int1 ^ 0xFFFFFFFF) + 1 & param1Int2 - 1;
    }
    
    private byte storedPackedType() {
      return storedPackedType(0);
    }
    
    private byte storedPackedType(int param1Int) {
      return packedType(storedWidth(param1Int), this.type);
    }
    
    private int storedWidth(int param1Int) {
      return FlexBuffers.isTypeInline(this.type) ? Math.max(this.minBitWidth, param1Int) : this.minBitWidth;
    }
    
    static Value uInt16(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 2, 1, param1Int2);
    }
    
    static Value uInt32(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 2, 2, param1Int2);
    }
    
    static Value uInt64(int param1Int, long param1Long) {
      return new Value(param1Int, 2, 3, param1Long);
    }
    
    static Value uInt8(int param1Int1, int param1Int2) {
      return new Value(param1Int1, 2, 0, param1Int2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\FlexBuffersBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */