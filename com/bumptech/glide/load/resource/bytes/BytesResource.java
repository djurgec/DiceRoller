package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class BytesResource implements Resource<byte[]> {
  private final byte[] bytes;
  
  public BytesResource(byte[] paramArrayOfbyte) {
    this.bytes = (byte[])Preconditions.checkNotNull(paramArrayOfbyte);
  }
  
  public byte[] get() {
    return this.bytes;
  }
  
  public Class<byte[]> getResourceClass() {
    return byte[].class;
  }
  
  public int getSize() {
    return this.bytes.length;
  }
  
  public void recycle() {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bytes\BytesResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */