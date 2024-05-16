package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class SimpleResource<T> implements Resource<T> {
  protected final T data;
  
  public SimpleResource(T paramT) {
    this.data = (T)Preconditions.checkNotNull(paramT);
  }
  
  public final T get() {
    return this.data;
  }
  
  public Class<T> getResourceClass() {
    return (Class)this.data.getClass();
  }
  
  public final int getSize() {
    return 1;
  }
  
  public void recycle() {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\SimpleResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */