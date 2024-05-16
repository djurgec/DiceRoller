package com.bumptech.glide.load.data;

import java.io.IOException;

public interface DataRewinder<T> {
  void cleanup();
  
  T rewindAndGet() throws IOException;
  
  public static interface Factory<T> {
    DataRewinder<T> build(T param1T);
    
    Class<T> getDataClass();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\DataRewinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */