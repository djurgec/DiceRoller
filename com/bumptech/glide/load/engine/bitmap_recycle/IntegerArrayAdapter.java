package com.bumptech.glide.load.engine.bitmap_recycle;

public final class IntegerArrayAdapter implements ArrayAdapterInterface<int[]> {
  private static final String TAG = "IntegerArrayPool";
  
  public int getArrayLength(int[] paramArrayOfint) {
    return paramArrayOfint.length;
  }
  
  public int getElementSizeInBytes() {
    return 4;
  }
  
  public String getTag() {
    return "IntegerArrayPool";
  }
  
  public int[] newArray(int paramInt) {
    return new int[paramInt];
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\IntegerArrayAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */