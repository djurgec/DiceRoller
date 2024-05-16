package androidx.emoji2.text.flatbuffer;

interface ReadBuf {
  byte[] data();
  
  byte get(int paramInt);
  
  boolean getBoolean(int paramInt);
  
  double getDouble(int paramInt);
  
  float getFloat(int paramInt);
  
  int getInt(int paramInt);
  
  long getLong(int paramInt);
  
  short getShort(int paramInt);
  
  String getString(int paramInt1, int paramInt2);
  
  int limit();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ReadBuf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */