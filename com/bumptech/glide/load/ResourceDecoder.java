package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public interface ResourceDecoder<T, Z> {
  Resource<Z> decode(T paramT, int paramInt1, int paramInt2, Options paramOptions) throws IOException;
  
  boolean handles(T paramT, Options paramOptions) throws IOException;
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\ResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */