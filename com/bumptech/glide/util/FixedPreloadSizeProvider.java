package com.bumptech.glide.util;

import com.bumptech.glide.ListPreloader;

public class FixedPreloadSizeProvider<T> implements ListPreloader.PreloadSizeProvider<T> {
  private final int[] size;
  
  public FixedPreloadSizeProvider(int paramInt1, int paramInt2) {
    this.size = new int[] { paramInt1, paramInt2 };
  }
  
  public int[] getPreloadSize(T paramT, int paramInt1, int paramInt2) {
    return this.size;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\FixedPreloadSizeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */