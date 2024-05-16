package com.bumptech.glide.load.engine;

public interface Resource<Z> {
  Z get();
  
  Class<Z> getResourceClass();
  
  int getSize();
  
  void recycle();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */