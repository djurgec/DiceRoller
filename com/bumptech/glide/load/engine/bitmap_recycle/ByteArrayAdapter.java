package com.bumptech.glide.load.engine.bitmap_recycle;

public final class ByteArrayAdapter implements ArrayAdapterInterface<byte[]> {
  private static final String TAG = "ByteArrayPool";
  
  public int getArrayLength(byte[] paramArrayOfbyte) {
    return paramArrayOfbyte.length;
  }
  
  public int getElementSizeInBytes() {
    return 1;
  }
  
  public String getTag() {
    return "ByteArrayPool";
  }
  
  public byte[] newArray(int paramInt) {
    return new byte[paramInt];
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\ByteArrayAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */