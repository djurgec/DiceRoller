package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public final class ExceptionPassthroughInputStream extends InputStream {
  private static final Queue<ExceptionPassthroughInputStream> POOL = Util.createQueue(0);
  
  private IOException exception;
  
  private InputStream wrapped;
  
  static void clearQueue() {
    synchronized (POOL) {
      while (true) {
        Queue<ExceptionPassthroughInputStream> queue = POOL;
        if (!queue.isEmpty()) {
          queue.remove();
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static ExceptionPassthroughInputStream obtain(InputStream paramInputStream) {
    Queue<ExceptionPassthroughInputStream> queue;
    ExceptionPassthroughInputStream exceptionPassthroughInputStream;
    synchronized (POOL) {
      ExceptionPassthroughInputStream exceptionPassthroughInputStream1 = queue.poll();
      exceptionPassthroughInputStream = exceptionPassthroughInputStream1;
      if (exceptionPassthroughInputStream1 == null)
        exceptionPassthroughInputStream = new ExceptionPassthroughInputStream(); 
      exceptionPassthroughInputStream.setInputStream(paramInputStream);
      return exceptionPassthroughInputStream;
    } 
  }
  
  public int available() throws IOException {
    return this.wrapped.available();
  }
  
  public void close() throws IOException {
    this.wrapped.close();
  }
  
  public IOException getException() {
    return this.exception;
  }
  
  public void mark(int paramInt) {
    this.wrapped.mark(paramInt);
  }
  
  public boolean markSupported() {
    return this.wrapped.markSupported();
  }
  
  public int read() throws IOException {
    try {
      return this.wrapped.read();
    } catch (IOException iOException) {
      this.exception = iOException;
      throw iOException;
    } 
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    try {
      return this.wrapped.read(paramArrayOfbyte);
    } catch (IOException iOException) {
      this.exception = iOException;
      throw iOException;
    } 
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    try {
      return this.wrapped.read(paramArrayOfbyte, paramInt1, paramInt2);
    } catch (IOException iOException) {
      this.exception = iOException;
      throw iOException;
    } 
  }
  
  public void release() {
    this.exception = null;
    this.wrapped = null;
    synchronized (POOL) {
      null.offer(this);
      return;
    } 
  }
  
  public void reset() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield wrapped : Ljava/io/InputStream;
    //   6: invokevirtual reset : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  void setInputStream(InputStream paramInputStream) {
    this.wrapped = paramInputStream;
  }
  
  public long skip(long paramLong) throws IOException {
    try {
      return this.wrapped.skip(paramLong);
    } catch (IOException iOException) {
      this.exception = iOException;
      throw iOException;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\ExceptionPassthroughInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */