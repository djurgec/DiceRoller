package androidx.emoji2.text.flatbuffer;

import java.util.Arrays;

public class ArrayReadWriteBuf implements ReadWriteBuf {
  private byte[] buffer;
  
  private int writePos;
  
  public ArrayReadWriteBuf() {
    this(10);
  }
  
  public ArrayReadWriteBuf(int paramInt) {
    this(new byte[paramInt]);
  }
  
  public ArrayReadWriteBuf(byte[] paramArrayOfbyte) {
    this.buffer = paramArrayOfbyte;
    this.writePos = 0;
  }
  
  public ArrayReadWriteBuf(byte[] paramArrayOfbyte, int paramInt) {
    this.buffer = paramArrayOfbyte;
    this.writePos = paramInt;
  }
  
  public byte[] data() {
    return this.buffer;
  }
  
  public byte get(int paramInt) {
    return this.buffer[paramInt];
  }
  
  public boolean getBoolean(int paramInt) {
    boolean bool;
    if (this.buffer[paramInt] != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public double getDouble(int paramInt) {
    return Double.longBitsToDouble(getLong(paramInt));
  }
  
  public float getFloat(int paramInt) {
    return Float.intBitsToFloat(getInt(paramInt));
  }
  
  public int getInt(int paramInt) {
    byte[] arrayOfByte = this.buffer;
    byte b1 = arrayOfByte[paramInt + 3];
    byte b2 = arrayOfByte[paramInt + 2];
    byte b3 = arrayOfByte[paramInt + 1];
    return arrayOfByte[paramInt] & 0xFF | b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  public long getLong(int paramInt) {
    byte[] arrayOfByte = this.buffer;
    int i = paramInt + 1;
    long l3 = arrayOfByte[paramInt];
    int j = i + 1;
    long l5 = arrayOfByte[i];
    paramInt = j + 1;
    long l2 = arrayOfByte[j];
    i = paramInt + 1;
    long l4 = arrayOfByte[paramInt];
    paramInt = i + 1;
    long l1 = arrayOfByte[i];
    i = paramInt + 1;
    return l3 & 0xFFL | (l5 & 0xFFL) << 8L | (l2 & 0xFFL) << 16L | (l4 & 0xFFL) << 24L | (l1 & 0xFFL) << 32L | (arrayOfByte[paramInt] & 0xFFL) << 40L | (0xFFL & arrayOfByte[i]) << 48L | arrayOfByte[i + 1] << 56L;
  }
  
  public short getShort(int paramInt) {
    byte[] arrayOfByte = this.buffer;
    byte b = arrayOfByte[paramInt + 1];
    return (short)(arrayOfByte[paramInt] & 0xFF | b << 8);
  }
  
  public String getString(int paramInt1, int paramInt2) {
    return Utf8Safe.decodeUtf8Array(this.buffer, paramInt1, paramInt2);
  }
  
  public int limit() {
    return this.writePos;
  }
  
  public void put(byte paramByte) {
    set(this.writePos, paramByte);
    this.writePos++;
  }
  
  public void put(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    set(this.writePos, paramArrayOfbyte, paramInt1, paramInt2);
    this.writePos += paramInt2;
  }
  
  public void putBoolean(boolean paramBoolean) {
    setBoolean(this.writePos, paramBoolean);
    this.writePos++;
  }
  
  public void putDouble(double paramDouble) {
    setDouble(this.writePos, paramDouble);
    this.writePos += 8;
  }
  
  public void putFloat(float paramFloat) {
    setFloat(this.writePos, paramFloat);
    this.writePos += 4;
  }
  
  public void putInt(int paramInt) {
    setInt(this.writePos, paramInt);
    this.writePos += 4;
  }
  
  public void putLong(long paramLong) {
    setLong(this.writePos, paramLong);
    this.writePos += 8;
  }
  
  public void putShort(short paramShort) {
    setShort(this.writePos, paramShort);
    this.writePos += 2;
  }
  
  public boolean requestCapacity(int paramInt) {
    byte[] arrayOfByte = this.buffer;
    if (arrayOfByte.length > paramInt)
      return true; 
    paramInt = arrayOfByte.length;
    this.buffer = Arrays.copyOf(arrayOfByte, (paramInt >> 1) + paramInt);
    return true;
  }
  
  public void set(int paramInt, byte paramByte) {
    requestCapacity(paramInt + 1);
    this.buffer[paramInt] = paramByte;
  }
  
  public void set(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    requestCapacity(paramInt3 - paramInt2 + paramInt1);
    System.arraycopy(paramArrayOfbyte, paramInt2, this.buffer, paramInt1, paramInt3);
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean) {
    set(paramInt, paramBoolean);
  }
  
  public void setDouble(int paramInt, double paramDouble) {
    requestCapacity(paramInt + 8);
    long l = Double.doubleToRawLongBits(paramDouble);
    int i = (int)l;
    byte[] arrayOfByte = this.buffer;
    int j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 8 & 0xFF);
    j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i >> 16 & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 24 & 0xFF);
    i = (int)(l >> 32L);
    j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 8 & 0xFF);
    arrayOfByte[paramInt] = (byte)(i >> 16 & 0xFF);
    arrayOfByte[paramInt + 1] = (byte)(i >> 24 & 0xFF);
  }
  
  public void setFloat(int paramInt, float paramFloat) {
    requestCapacity(paramInt + 4);
    int i = Float.floatToRawIntBits(paramFloat);
    byte[] arrayOfByte = this.buffer;
    int j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 8 & 0xFF);
    arrayOfByte[paramInt] = (byte)(i >> 16 & 0xFF);
    arrayOfByte[paramInt + 1] = (byte)(i >> 24 & 0xFF);
  }
  
  public void setInt(int paramInt1, int paramInt2) {
    requestCapacity(paramInt1 + 4);
    byte[] arrayOfByte = this.buffer;
    int i = paramInt1 + 1;
    arrayOfByte[paramInt1] = (byte)(paramInt2 & 0xFF);
    paramInt1 = i + 1;
    arrayOfByte[i] = (byte)(paramInt2 >> 8 & 0xFF);
    arrayOfByte[paramInt1] = (byte)(paramInt2 >> 16 & 0xFF);
    arrayOfByte[paramInt1 + 1] = (byte)(paramInt2 >> 24 & 0xFF);
  }
  
  public void setLong(int paramInt, long paramLong) {
    requestCapacity(paramInt + 8);
    int i = (int)paramLong;
    byte[] arrayOfByte = this.buffer;
    int j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 8 & 0xFF);
    j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i >> 16 & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 24 & 0xFF);
    i = (int)(paramLong >> 32L);
    j = paramInt + 1;
    arrayOfByte[paramInt] = (byte)(i & 0xFF);
    paramInt = j + 1;
    arrayOfByte[j] = (byte)(i >> 8 & 0xFF);
    arrayOfByte[paramInt] = (byte)(i >> 16 & 0xFF);
    arrayOfByte[paramInt + 1] = (byte)(i >> 24 & 0xFF);
  }
  
  public void setShort(int paramInt, short paramShort) {
    requestCapacity(paramInt + 2);
    byte[] arrayOfByte = this.buffer;
    arrayOfByte[paramInt] = (byte)(paramShort & 0xFF);
    arrayOfByte[paramInt + 1] = (byte)(paramShort >> 8 & 0xFF);
  }
  
  public int writePosition() {
    return this.writePos;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ArrayReadWriteBuf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */