package androidx.emoji2.text.flatbuffer;

interface ReadWriteBuf extends ReadBuf {
  int limit();
  
  void put(byte paramByte);
  
  void put(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  void putBoolean(boolean paramBoolean);
  
  void putDouble(double paramDouble);
  
  void putFloat(float paramFloat);
  
  void putInt(int paramInt);
  
  void putLong(long paramLong);
  
  void putShort(short paramShort);
  
  boolean requestCapacity(int paramInt);
  
  void set(int paramInt, byte paramByte);
  
  void set(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3);
  
  void setBoolean(int paramInt, boolean paramBoolean);
  
  void setDouble(int paramInt, double paramDouble);
  
  void setFloat(int paramInt, float paramFloat);
  
  void setInt(int paramInt1, int paramInt2);
  
  void setLong(int paramInt, long paramLong);
  
  void setShort(int paramInt, short paramShort);
  
  int writePosition();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ReadWriteBuf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */