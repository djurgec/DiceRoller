package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.OutputStream;

public final class BufferedOutputStream extends OutputStream {
  private ArrayPool arrayPool;
  
  private byte[] buffer;
  
  private int index;
  
  private final OutputStream out;
  
  public BufferedOutputStream(OutputStream paramOutputStream, ArrayPool paramArrayPool) {
    this(paramOutputStream, paramArrayPool, 65536);
  }
  
  BufferedOutputStream(OutputStream paramOutputStream, ArrayPool paramArrayPool, int paramInt) {
    this.out = paramOutputStream;
    this.arrayPool = paramArrayPool;
    this.buffer = (byte[])paramArrayPool.get(paramInt, byte[].class);
  }
  
  private void flushBuffer() throws IOException {
    int i = this.index;
    if (i > 0) {
      this.out.write(this.buffer, 0, i);
      this.index = 0;
    } 
  }
  
  private void maybeFlushBuffer() throws IOException {
    if (this.index == this.buffer.length)
      flushBuffer(); 
  }
  
  private void release() {
    byte[] arrayOfByte = this.buffer;
    if (arrayOfByte != null) {
      this.arrayPool.put(arrayOfByte);
      this.buffer = null;
    } 
  }
  
  public void close() throws IOException {
    try {
      flush();
      this.out.close();
      return;
    } finally {
      this.out.close();
    } 
  }
  
  public void flush() throws IOException {
    flushBuffer();
    this.out.flush();
  }
  
  public void write(int paramInt) throws IOException {
    byte[] arrayOfByte = this.buffer;
    int i = this.index;
    this.index = i + 1;
    arrayOfByte[i] = (byte)paramInt;
    maybeFlushBuffer();
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    int i = 0;
    do {
      int k = paramInt2 - i;
      int j = paramInt1 + i;
      int m = this.index;
      if (m == 0 && k >= this.buffer.length) {
        this.out.write(paramArrayOfbyte, j, k);
        return;
      } 
      k = Math.min(k, this.buffer.length - m);
      System.arraycopy(paramArrayOfbyte, j, this.buffer, this.index, k);
      this.index += k;
      i += k;
      maybeFlushBuffer();
    } while (i < paramInt2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\BufferedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */