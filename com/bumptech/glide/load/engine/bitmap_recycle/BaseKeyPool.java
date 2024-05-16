package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool<T extends Poolable> {
  private static final int MAX_SIZE = 20;
  
  private final Queue<T> keyPool = Util.createQueue(20);
  
  abstract T create();
  
  T get() {
    Poolable poolable2 = (Poolable)this.keyPool.poll();
    Poolable poolable1 = poolable2;
    if (poolable2 == null)
      poolable1 = (Poolable)create(); 
    return (T)poolable1;
  }
  
  public void offer(T paramT) {
    if (this.keyPool.size() < 20)
      this.keyPool.offer(paramT); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\BaseKeyPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */