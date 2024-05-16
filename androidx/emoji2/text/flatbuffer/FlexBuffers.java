package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FlexBuffers {
  static final boolean $assertionsDisabled = false;
  
  private static final ReadBuf EMPTY_BB = new ArrayReadWriteBuf(new byte[] { 0 }, 1);
  
  public static final int FBT_BLOB = 25;
  
  public static final int FBT_BOOL = 26;
  
  public static final int FBT_FLOAT = 3;
  
  public static final int FBT_INDIRECT_FLOAT = 8;
  
  public static final int FBT_INDIRECT_INT = 6;
  
  public static final int FBT_INDIRECT_UINT = 7;
  
  public static final int FBT_INT = 1;
  
  public static final int FBT_KEY = 4;
  
  public static final int FBT_MAP = 9;
  
  public static final int FBT_NULL = 0;
  
  public static final int FBT_STRING = 5;
  
  public static final int FBT_UINT = 2;
  
  public static final int FBT_VECTOR = 10;
  
  public static final int FBT_VECTOR_BOOL = 36;
  
  public static final int FBT_VECTOR_FLOAT = 13;
  
  public static final int FBT_VECTOR_FLOAT2 = 18;
  
  public static final int FBT_VECTOR_FLOAT3 = 21;
  
  public static final int FBT_VECTOR_FLOAT4 = 24;
  
  public static final int FBT_VECTOR_INT = 11;
  
  public static final int FBT_VECTOR_INT2 = 16;
  
  public static final int FBT_VECTOR_INT3 = 19;
  
  public static final int FBT_VECTOR_INT4 = 22;
  
  public static final int FBT_VECTOR_KEY = 14;
  
  public static final int FBT_VECTOR_STRING_DEPRECATED = 15;
  
  public static final int FBT_VECTOR_UINT = 12;
  
  public static final int FBT_VECTOR_UINT2 = 17;
  
  public static final int FBT_VECTOR_UINT3 = 20;
  
  public static final int FBT_VECTOR_UINT4 = 23;
  
  public static Reference getRoot(ReadBuf paramReadBuf) {
    int i = paramReadBuf.limit() - 1;
    byte b = paramReadBuf.get(i);
    return new Reference(paramReadBuf, --i - b, b, Unsigned.byteToUnsignedInt(paramReadBuf.get(i)));
  }
  
  @Deprecated
  public static Reference getRoot(ByteBuffer paramByteBuffer) {
    ArrayReadWriteBuf arrayReadWriteBuf;
    ByteBufferReadWriteBuf byteBufferReadWriteBuf;
    if (paramByteBuffer.hasArray()) {
      arrayReadWriteBuf = new ArrayReadWriteBuf(paramByteBuffer.array(), paramByteBuffer.limit());
    } else {
      byteBufferReadWriteBuf = new ByteBufferReadWriteBuf((ByteBuffer)arrayReadWriteBuf);
    } 
    return getRoot(byteBufferReadWriteBuf);
  }
  
  private static int indirect(ReadBuf paramReadBuf, int paramInt1, int paramInt2) {
    return (int)(paramInt1 - readUInt(paramReadBuf, paramInt1, paramInt2));
  }
  
  static boolean isTypeInline(int paramInt) {
    return (paramInt <= 3 || paramInt == 26);
  }
  
  static boolean isTypedVector(int paramInt) {
    boolean bool;
    if ((paramInt >= 11 && paramInt <= 15) || paramInt == 36) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isTypedVectorElementType(int paramInt) {
    boolean bool = true;
    if ((paramInt < 1 || paramInt > 4) && paramInt != 26)
      bool = false; 
    return bool;
  }
  
  private static double readDouble(ReadBuf paramReadBuf, int paramInt1, int paramInt2) {
    switch (paramInt2) {
      default:
        return -1.0D;
      case 8:
        return paramReadBuf.getDouble(paramInt1);
      case 4:
        break;
    } 
    return paramReadBuf.getFloat(paramInt1);
  }
  
  private static int readInt(ReadBuf paramReadBuf, int paramInt1, int paramInt2) {
    return (int)readLong(paramReadBuf, paramInt1, paramInt2);
  }
  
  private static long readLong(ReadBuf paramReadBuf, int paramInt1, int paramInt2) {
    switch (paramInt2) {
      default:
        return -1L;
      case 8:
        return paramReadBuf.getLong(paramInt1);
      case 4:
        return paramReadBuf.getInt(paramInt1);
      case 2:
        return paramReadBuf.getShort(paramInt1);
      case 1:
        break;
    } 
    return paramReadBuf.get(paramInt1);
  }
  
  private static long readUInt(ReadBuf paramReadBuf, int paramInt1, int paramInt2) {
    switch (paramInt2) {
      default:
        return -1L;
      case 8:
        return paramReadBuf.getLong(paramInt1);
      case 4:
        return Unsigned.intToUnsignedLong(paramReadBuf.getInt(paramInt1));
      case 2:
        return Unsigned.shortToUnsignedInt(paramReadBuf.getShort(paramInt1));
      case 1:
        break;
    } 
    return Unsigned.byteToUnsignedInt(paramReadBuf.get(paramInt1));
  }
  
  static int toTypedVector(int paramInt1, int paramInt2) {
    if (isTypedVectorElementType(paramInt1)) {
      switch (paramInt2) {
        default:
          throw new AssertionError();
        case 4:
          return paramInt1 - 1 + 22;
        case 3:
          return paramInt1 - 1 + 19;
        case 2:
          return paramInt1 - 1 + 16;
        case 0:
          break;
      } 
      return paramInt1 - 1 + 11;
    } 
    throw new AssertionError();
  }
  
  static int toTypedVectorElementType(int paramInt) {
    return paramInt - 11 + 1;
  }
  
  public static class Blob extends Sized {
    static final boolean $assertionsDisabled = false;
    
    static final Blob EMPTY = new Blob(FlexBuffers.EMPTY_BB, 1, 1);
    
    Blob(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      super(param1ReadBuf, param1Int1, param1Int2);
    }
    
    public static Blob empty() {
      return EMPTY;
    }
    
    public ByteBuffer data() {
      ByteBuffer byteBuffer = ByteBuffer.wrap(this.bb.data());
      byteBuffer.position(this.end);
      byteBuffer.limit(this.end + size());
      return byteBuffer.asReadOnlyBuffer().slice();
    }
    
    public byte get(int param1Int) {
      if (param1Int >= 0 && param1Int <= size())
        return this.bb.get(this.end + param1Int); 
      throw new AssertionError();
    }
    
    public byte[] getBytes() {
      int i = size();
      byte[] arrayOfByte = new byte[i];
      for (byte b = 0; b < i; b++)
        arrayOfByte[b] = this.bb.get(this.end + b); 
      return arrayOfByte;
    }
    
    public String toString() {
      return this.bb.getString(this.end, size());
    }
    
    public StringBuilder toString(StringBuilder param1StringBuilder) {
      param1StringBuilder.append('"');
      param1StringBuilder.append(this.bb.getString(this.end, size()));
      return param1StringBuilder.append('"');
    }
  }
  
  public static class FlexBufferException extends RuntimeException {
    FlexBufferException(String param1String) {
      super(param1String);
    }
  }
  
  public static class Key extends Object {
    private static final Key EMPTY = new Key(FlexBuffers.EMPTY_BB, 0, 0);
    
    Key(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      super(param1ReadBuf, param1Int1, param1Int2);
    }
    
    public static Key empty() {
      return EMPTY;
    }
    
    int compareTo(byte[] param1ArrayOfbyte) {
      int i = this.end;
      byte b = 0;
      while (true) {
        byte b1 = this.bb.get(i);
        byte b2 = param1ArrayOfbyte[b];
        if (b1 == 0)
          return b1 - b2; 
        i++;
        if (++b == param1ArrayOfbyte.length)
          return b1 - b2; 
        if (b1 != b2)
          return b1 - b2; 
      } 
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      if (!bool)
        return false; 
      bool = bool1;
      if (((Key)param1Object).end == this.end) {
        bool = bool1;
        if (((Key)param1Object).byteWidth == this.byteWidth)
          bool = true; 
      } 
      return bool;
    }
    
    public int hashCode() {
      return this.end ^ this.byteWidth;
    }
    
    public String toString() {
      for (int i = this.end;; i++) {
        if (this.bb.get(i) == 0) {
          int j = this.end;
          return this.bb.getString(this.end, i - j);
        } 
      } 
    }
    
    public StringBuilder toString(StringBuilder param1StringBuilder) {
      return param1StringBuilder.append(toString());
    }
  }
  
  public static class KeyVector {
    private final FlexBuffers.TypedVector vec;
    
    KeyVector(FlexBuffers.TypedVector param1TypedVector) {
      this.vec = param1TypedVector;
    }
    
    public FlexBuffers.Key get(int param1Int) {
      if (param1Int >= size())
        return FlexBuffers.Key.EMPTY; 
      int j = this.vec.end;
      int i = this.vec.byteWidth;
      return new FlexBuffers.Key(this.vec.bb, FlexBuffers.indirect(this.vec.bb, j + i * param1Int, this.vec.byteWidth), 1);
    }
    
    public int size() {
      return this.vec.size();
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append('[');
      for (byte b = 0; b < this.vec.size(); b++) {
        this.vec.get(b).toString(stringBuilder);
        if (b != this.vec.size() - 1)
          stringBuilder.append(", "); 
      } 
      return stringBuilder.append("]").toString();
    }
  }
  
  public static class Map extends Vector {
    private static final Map EMPTY_MAP = new Map(FlexBuffers.EMPTY_BB, 1, 1);
    
    Map(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      super(param1ReadBuf, param1Int1, param1Int2);
    }
    
    private int binarySearch(FlexBuffers.KeyVector param1KeyVector, byte[] param1ArrayOfbyte) {
      int j = 0;
      int i = param1KeyVector.size() - 1;
      while (j <= i) {
        int m = j + i >>> 1;
        int k = param1KeyVector.get(m).compareTo(param1ArrayOfbyte);
        if (k < 0) {
          j = m + 1;
          continue;
        } 
        if (k > 0) {
          i = m - 1;
          continue;
        } 
        return m;
      } 
      return -(j + 1);
    }
    
    public static Map empty() {
      return EMPTY_MAP;
    }
    
    public FlexBuffers.Reference get(String param1String) {
      return get(param1String.getBytes(StandardCharsets.UTF_8));
    }
    
    public FlexBuffers.Reference get(byte[] param1ArrayOfbyte) {
      FlexBuffers.KeyVector keyVector = keys();
      int i = keyVector.size();
      int j = binarySearch(keyVector, param1ArrayOfbyte);
      return (j >= 0 && j < i) ? get(j) : FlexBuffers.Reference.NULL_REFERENCE;
    }
    
    public FlexBuffers.KeyVector keys() {
      int i = this.end - this.byteWidth * 3;
      return new FlexBuffers.KeyVector(new FlexBuffers.TypedVector(this.bb, FlexBuffers.indirect(this.bb, i, this.byteWidth), FlexBuffers.readInt(this.bb, this.byteWidth + i, this.byteWidth), 4));
    }
    
    public StringBuilder toString(StringBuilder param1StringBuilder) {
      param1StringBuilder.append("{ ");
      FlexBuffers.KeyVector keyVector = keys();
      int i = size();
      FlexBuffers.Vector vector = values();
      for (byte b = 0; b < i; b++) {
        param1StringBuilder.append('"').append(keyVector.get(b).toString()).append("\" : ");
        param1StringBuilder.append(vector.get(b).toString());
        if (b != i - 1)
          param1StringBuilder.append(", "); 
      } 
      param1StringBuilder.append(" }");
      return param1StringBuilder;
    }
    
    public FlexBuffers.Vector values() {
      return new FlexBuffers.Vector(this.bb, this.end, this.byteWidth);
    }
  }
  
  private static abstract class Object {
    ReadBuf bb;
    
    int byteWidth;
    
    int end;
    
    Object(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      this.bb = param1ReadBuf;
      this.end = param1Int1;
      this.byteWidth = param1Int2;
    }
    
    public String toString() {
      return toString(new StringBuilder(128)).toString();
    }
    
    public abstract StringBuilder toString(StringBuilder param1StringBuilder);
  }
  
  public static class Reference {
    private static final Reference NULL_REFERENCE = new Reference(FlexBuffers.EMPTY_BB, 0, 1, 0);
    
    private ReadBuf bb;
    
    private int byteWidth;
    
    private int end;
    
    private int parentWidth;
    
    private int type;
    
    Reference(ReadBuf param1ReadBuf, int param1Int1, int param1Int2, int param1Int3) {
      this(param1ReadBuf, param1Int1, param1Int2, 1 << (param1Int3 & 0x3), param1Int3 >> 2);
    }
    
    Reference(ReadBuf param1ReadBuf, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.bb = param1ReadBuf;
      this.end = param1Int1;
      this.parentWidth = param1Int2;
      this.byteWidth = param1Int3;
      this.type = param1Int4;
    }
    
    public FlexBuffers.Blob asBlob() {
      if (isBlob() || isString()) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.Blob(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
      } 
      return FlexBuffers.Blob.empty();
    }
    
    public boolean asBoolean() {
      boolean bool = isBoolean();
      boolean bool2 = true;
      boolean bool1 = true;
      if (bool) {
        if (this.bb.get(this.end) == 0)
          bool1 = false; 
        return bool1;
      } 
      if (asUInt() != 0L) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      return bool1;
    }
    
    public double asFloat() {
      ReadBuf readBuf;
      int i = this.type;
      if (i == 3)
        return FlexBuffers.readDouble(this.bb, this.end, this.parentWidth); 
      switch (i) {
        default:
          return 0.0D;
        case 10:
          return asVector().size();
        case 8:
          readBuf = this.bb;
          return FlexBuffers.readDouble(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 7:
          readBuf = this.bb;
          return FlexBuffers.readUInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 6:
          readBuf = this.bb;
          return FlexBuffers.readInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 5:
          return Double.parseDouble(asString());
        case 2:
        case 26:
          return FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
        case 1:
          return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
        case 0:
          break;
      } 
      return 0.0D;
    }
    
    public int asInt() {
      ReadBuf readBuf;
      int i = this.type;
      if (i == 1)
        return FlexBuffers.readInt(this.bb, this.end, this.parentWidth); 
      switch (i) {
        default:
          return 0;
        case 26:
          return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
        case 10:
          return asVector().size();
        case 8:
          readBuf = this.bb;
          return (int)FlexBuffers.readDouble(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 7:
          readBuf = this.bb;
          return (int)FlexBuffers.readUInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.parentWidth);
        case 6:
          readBuf = this.bb;
          return FlexBuffers.readInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 5:
          return Integer.parseInt(asString());
        case 3:
          return (int)FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
        case 2:
          return (int)FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
        case 0:
          break;
      } 
      return 0;
    }
    
    public FlexBuffers.Key asKey() {
      if (isKey()) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.Key(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
      } 
      return FlexBuffers.Key.empty();
    }
    
    public long asLong() {
      ReadBuf readBuf;
      int i = this.type;
      if (i == 1)
        return FlexBuffers.readLong(this.bb, this.end, this.parentWidth); 
      switch (i) {
        default:
          return 0L;
        case 26:
          return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
        case 10:
          return asVector().size();
        case 8:
          readBuf = this.bb;
          return (long)FlexBuffers.readDouble(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 7:
          readBuf = this.bb;
          return FlexBuffers.readUInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.parentWidth);
        case 6:
          readBuf = this.bb;
          return FlexBuffers.readLong(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 5:
          try {
            return Long.parseLong(asString());
          } catch (NumberFormatException numberFormatException) {
            return 0L;
          } 
        case 3:
          return (long)FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
        case 2:
          return FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
        case 0:
          break;
      } 
      return 0L;
    }
    
    public FlexBuffers.Map asMap() {
      if (isMap()) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.Map(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
      } 
      return FlexBuffers.Map.empty();
    }
    
    public String asString() {
      if (isString()) {
        int i = FlexBuffers.indirect(this.bb, this.end, this.parentWidth);
        ReadBuf readBuf = this.bb;
        int j = this.byteWidth;
        j = (int)FlexBuffers.readUInt(readBuf, i - j, j);
        return this.bb.getString(i, j);
      } 
      if (isKey()) {
        int j = FlexBuffers.indirect(this.bb, this.end, this.byteWidth);
        for (int i = j;; i++) {
          if (this.bb.get(i) == 0)
            return this.bb.getString(j, i - j); 
        } 
      } 
      return "";
    }
    
    public long asUInt() {
      ReadBuf readBuf;
      int i = this.type;
      if (i == 2)
        return FlexBuffers.readUInt(this.bb, this.end, this.parentWidth); 
      switch (i) {
        default:
          return 0L;
        case 26:
          return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
        case 10:
          return asVector().size();
        case 8:
          readBuf = this.bb;
          return (long)FlexBuffers.readDouble(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.parentWidth);
        case 7:
          readBuf = this.bb;
          return FlexBuffers.readUInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 6:
          readBuf = this.bb;
          return FlexBuffers.readLong(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        case 5:
          return Long.parseLong(asString());
        case 3:
          return (long)FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
        case 1:
          return FlexBuffers.readLong(this.bb, this.end, this.parentWidth);
        case 0:
          break;
      } 
      return 0L;
    }
    
    public FlexBuffers.Vector asVector() {
      if (isVector()) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.Vector(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
      } 
      int i = this.type;
      if (i == 15) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.TypedVector(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth, 4);
      } 
      if (FlexBuffers.isTypedVector(i)) {
        ReadBuf readBuf = this.bb;
        return new FlexBuffers.TypedVector(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth, FlexBuffers.toTypedVectorElementType(this.type));
      } 
      return FlexBuffers.Vector.empty();
    }
    
    public int getType() {
      return this.type;
    }
    
    public boolean isBlob() {
      boolean bool;
      if (this.type == 25) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isBoolean() {
      boolean bool;
      if (this.type == 26) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isFloat() {
      int i = this.type;
      return (i == 3 || i == 8);
    }
    
    public boolean isInt() {
      int i = this.type;
      boolean bool2 = true;
      boolean bool1 = bool2;
      if (i != 1)
        if (i == 6) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }  
      return bool1;
    }
    
    public boolean isIntOrUInt() {
      return (isInt() || isUInt());
    }
    
    public boolean isKey() {
      boolean bool;
      if (this.type == 4) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isMap() {
      boolean bool;
      if (this.type == 9) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isNull() {
      boolean bool;
      if (this.type == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isNumeric() {
      return (isIntOrUInt() || isFloat());
    }
    
    public boolean isString() {
      boolean bool;
      if (this.type == 5) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isTypedVector() {
      return FlexBuffers.isTypedVector(this.type);
    }
    
    public boolean isUInt() {
      int i = this.type;
      return (i == 2 || i == 7);
    }
    
    public boolean isVector() {
      int i = this.type;
      return (i == 10 || i == 9);
    }
    
    public String toString() {
      return toString(new StringBuilder(128)).toString();
    }
    
    StringBuilder toString(StringBuilder param1StringBuilder) {
      switch (this.type) {
        default:
          return param1StringBuilder;
        case 26:
          return param1StringBuilder.append(asBoolean());
        case 25:
          return asBlob().toString(param1StringBuilder);
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
          throw new FlexBuffers.FlexBufferException("not_implemented:" + this.type);
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 36:
          return param1StringBuilder.append(asVector());
        case 10:
          return asVector().toString(param1StringBuilder);
        case 9:
          return asMap().toString(param1StringBuilder);
        case 5:
          return param1StringBuilder.append('"').append(asString()).append('"');
        case 4:
          return asKey().toString(param1StringBuilder.append('"')).append('"');
        case 3:
        case 8:
          return param1StringBuilder.append(asFloat());
        case 2:
        case 7:
          return param1StringBuilder.append(asUInt());
        case 1:
        case 6:
          return param1StringBuilder.append(asLong());
        case 0:
          break;
      } 
      return param1StringBuilder.append("null");
    }
  }
  
  private static abstract class Sized extends Object {
    protected final int size;
    
    Sized(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      super(param1ReadBuf, param1Int1, param1Int2);
      this.size = FlexBuffers.readInt(this.bb, param1Int1 - param1Int2, param1Int2);
    }
    
    public int size() {
      return this.size;
    }
  }
  
  public static class TypedVector extends Vector {
    private static final TypedVector EMPTY_VECTOR = new TypedVector(FlexBuffers.EMPTY_BB, 1, 1, 1);
    
    private final int elemType;
    
    TypedVector(ReadBuf param1ReadBuf, int param1Int1, int param1Int2, int param1Int3) {
      super(param1ReadBuf, param1Int1, param1Int2);
      this.elemType = param1Int3;
    }
    
    public static TypedVector empty() {
      return EMPTY_VECTOR;
    }
    
    public FlexBuffers.Reference get(int param1Int) {
      if (param1Int >= size())
        return FlexBuffers.Reference.NULL_REFERENCE; 
      int i = this.end;
      int j = this.byteWidth;
      return new FlexBuffers.Reference(this.bb, i + j * param1Int, this.byteWidth, 1, this.elemType);
    }
    
    public int getElemType() {
      return this.elemType;
    }
    
    public boolean isEmptyVector() {
      boolean bool;
      if (this == EMPTY_VECTOR) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static class Unsigned {
    static int byteToUnsignedInt(byte param1Byte) {
      return param1Byte & 0xFF;
    }
    
    static long intToUnsignedLong(int param1Int) {
      return param1Int & 0xFFFFFFFFL;
    }
    
    static int shortToUnsignedInt(short param1Short) {
      return 0xFFFF & param1Short;
    }
  }
  
  public static class Vector extends Sized {
    private static final Vector EMPTY_VECTOR = new Vector(FlexBuffers.EMPTY_BB, 1, 1);
    
    Vector(ReadBuf param1ReadBuf, int param1Int1, int param1Int2) {
      super(param1ReadBuf, param1Int1, param1Int2);
    }
    
    public static Vector empty() {
      return EMPTY_VECTOR;
    }
    
    public FlexBuffers.Reference get(int param1Int) {
      long l = size();
      if (param1Int >= l)
        return FlexBuffers.Reference.NULL_REFERENCE; 
      int j = FlexBuffers.Unsigned.byteToUnsignedInt(this.bb.get((int)(this.end + this.byteWidth * l + param1Int)));
      int k = this.end;
      int i = this.byteWidth;
      return new FlexBuffers.Reference(this.bb, k + i * param1Int, this.byteWidth, j);
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this == EMPTY_VECTOR) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public StringBuilder toString(StringBuilder param1StringBuilder) {
      param1StringBuilder.append("[ ");
      int i = size();
      for (byte b = 0; b < i; b++) {
        get(b).toString(param1StringBuilder);
        if (b != i - 1)
          param1StringBuilder.append(", "); 
      } 
      param1StringBuilder.append(" ]");
      return param1StringBuilder;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\FlexBuffers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */