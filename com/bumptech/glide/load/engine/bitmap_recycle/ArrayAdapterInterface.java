package com.bumptech.glide.load.engine.bitmap_recycle;

interface ArrayAdapterInterface<T> {
  int getArrayLength(T paramT);
  
  int getElementSizeInBytes();
  
  String getTag();
  
  T newArray(int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\ArrayAdapterInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */