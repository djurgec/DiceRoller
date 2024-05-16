package com.bumptech.glide.load.model;

public interface ModelLoaderFactory<T, Y> {
  ModelLoader<T, Y> build(MultiModelLoaderFactory paramMultiModelLoaderFactory);
  
  void teardown();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\ModelLoaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */