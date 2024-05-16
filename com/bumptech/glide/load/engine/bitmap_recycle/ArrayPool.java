package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool {
  public static final int STANDARD_BUFFER_SIZE_BYTES = 65536;
  
  void clearMemory();
  
  <T> T get(int paramInt, Class<T> paramClass);
  
  <T> T getExact(int paramInt, Class<T> paramClass);
  
  <T> void put(T paramT);
  
  @Deprecated
  <T> void put(T paramT, Class<T> paramClass);
  
  void trimMemory(int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\ArrayPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */