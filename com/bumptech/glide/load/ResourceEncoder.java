package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;

public interface ResourceEncoder<T> extends Encoder<Resource<T>> {
  EncodeStrategy getEncodeStrategy(Options paramOptions);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\ResourceEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */