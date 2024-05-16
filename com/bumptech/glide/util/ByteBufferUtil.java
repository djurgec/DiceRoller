package com.bumptech.glide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
  private static final AtomicReference<byte[]> BUFFER_REF = (AtomicReference)new AtomicReference<>();
  
  private static final int BUFFER_SIZE = 16384;
  
  public static ByteBuffer fromFile(File paramFile) throws IOException {
    MappedByteBuffer mappedByteBuffer;
    RandomAccessFile randomAccessFile2 = null;
    FileChannel fileChannel2 = null;
    RandomAccessFile randomAccessFile1 = randomAccessFile2;
    FileChannel fileChannel1 = fileChannel2;
    try {
      MappedByteBuffer mappedByteBuffer1;
      long l = paramFile.length();
      if (l <= 2147483647L) {
        if (l != 0L) {
          randomAccessFile1 = randomAccessFile2;
          fileChannel1 = fileChannel2;
          RandomAccessFile randomAccessFile4 = new RandomAccessFile();
          randomAccessFile1 = randomAccessFile2;
          fileChannel1 = fileChannel2;
          this(paramFile, "r");
          RandomAccessFile randomAccessFile3 = randomAccessFile4;
          randomAccessFile1 = randomAccessFile3;
          fileChannel1 = fileChannel2;
          fileChannel2 = randomAccessFile3.getChannel();
          randomAccessFile1 = randomAccessFile3;
          fileChannel1 = fileChannel2;
          mappedByteBuffer1 = fileChannel2.map(FileChannel.MapMode.READ_ONLY, 0L, l).load();
          return mappedByteBuffer1;
        } 
        MappedByteBuffer mappedByteBuffer2 = mappedByteBuffer1;
        fileChannel1 = fileChannel2;
        IOException iOException1 = new IOException();
        mappedByteBuffer2 = mappedByteBuffer1;
        fileChannel1 = fileChannel2;
        this("File unsuitable for memory mapping");
        mappedByteBuffer2 = mappedByteBuffer1;
        fileChannel1 = fileChannel2;
        throw iOException1;
      } 
      mappedByteBuffer = mappedByteBuffer1;
      fileChannel1 = fileChannel2;
      IOException iOException = new IOException();
      mappedByteBuffer = mappedByteBuffer1;
      fileChannel1 = fileChannel2;
      this("File too large to map into memory");
      mappedByteBuffer = mappedByteBuffer1;
      fileChannel1 = fileChannel2;
      throw iOException;
    } finally {
      if (fileChannel1 != null)
        try {
          fileChannel1.close();
        } catch (IOException iOException) {} 
      if (mappedByteBuffer != null)
        try {
          mappedByteBuffer.close();
        } catch (IOException iOException) {} 
    } 
  }
  
  public static ByteBuffer fromStream(InputStream paramInputStream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
    byte[] arrayOfByte2 = BUFFER_REF.getAndSet(null);
    byte[] arrayOfByte1 = arrayOfByte2;
    if (arrayOfByte2 == null)
      arrayOfByte1 = new byte[16384]; 
    while (true) {
      int i = paramInputStream.read(arrayOfByte1);
      if (i >= 0) {
        byteArrayOutputStream.write(arrayOfByte1, 0, i);
        continue;
      } 
      BUFFER_REF.set(arrayOfByte1);
      byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
      return (ByteBuffer)ByteBuffer.allocateDirect(arrayOfByte.length).put(arrayOfByte).position(0);
    } 
  }
  
  private static SafeArray getSafeArray(ByteBuffer paramByteBuffer) {
    return (!paramByteBuffer.isReadOnly() && paramByteBuffer.hasArray()) ? new SafeArray(paramByteBuffer.array(), paramByteBuffer.arrayOffset(), paramByteBuffer.limit()) : null;
  }
  
  public static byte[] toBytes(ByteBuffer paramByteBuffer) {
    byte[] arrayOfByte;
    SafeArray safeArray = getSafeArray(paramByteBuffer);
    if (safeArray != null && safeArray.offset == 0 && safeArray.limit == safeArray.data.length) {
      arrayOfByte = paramByteBuffer.array();
    } else {
      ByteBuffer byteBuffer = arrayOfByte.asReadOnlyBuffer();
      arrayOfByte = new byte[byteBuffer.limit()];
      byteBuffer.position(0);
      byteBuffer.get(arrayOfByte);
    } 
    return arrayOfByte;
  }
  
  public static void toFile(ByteBuffer paramByteBuffer, File paramFile) throws IOException {
    paramByteBuffer.position(0);
    RandomAccessFile randomAccessFile2 = null;
    FileChannel fileChannel2 = null;
    RandomAccessFile randomAccessFile1 = randomAccessFile2;
    FileChannel fileChannel1 = fileChannel2;
    try {
      RandomAccessFile randomAccessFile4 = new RandomAccessFile();
      randomAccessFile1 = randomAccessFile2;
      fileChannel1 = fileChannel2;
      this(paramFile, "rw");
      RandomAccessFile randomAccessFile3 = randomAccessFile4;
      randomAccessFile1 = randomAccessFile3;
      fileChannel1 = fileChannel2;
      fileChannel2 = randomAccessFile3.getChannel();
      randomAccessFile1 = randomAccessFile3;
      fileChannel1 = fileChannel2;
      fileChannel2.write(paramByteBuffer);
      randomAccessFile1 = randomAccessFile3;
      fileChannel1 = fileChannel2;
      fileChannel2.force(false);
      randomAccessFile1 = randomAccessFile3;
      fileChannel1 = fileChannel2;
      fileChannel2.close();
      randomAccessFile1 = randomAccessFile3;
      fileChannel1 = fileChannel2;
      randomAccessFile3.close();
      return;
    } finally {
      if (fileChannel1 != null)
        try {
          fileChannel1.close();
        } catch (IOException iOException) {} 
      if (randomAccessFile1 != null)
        try {
          randomAccessFile1.close();
        } catch (IOException iOException) {} 
    } 
  }
  
  public static InputStream toStream(ByteBuffer paramByteBuffer) {
    return new ByteBufferStream(paramByteBuffer);
  }
  
  public static void toStream(ByteBuffer paramByteBuffer, OutputStream paramOutputStream) throws IOException {
    SafeArray safeArray = getSafeArray(paramByteBuffer);
    if (safeArray != null) {
      paramOutputStream.write(safeArray.data, safeArray.offset, safeArray.offset + safeArray.limit);
    } else {
      byte[] arrayOfByte2 = BUFFER_REF.getAndSet(null);
      byte[] arrayOfByte1 = arrayOfByte2;
      if (arrayOfByte2 == null)
        arrayOfByte1 = new byte[16384]; 
      while (paramByteBuffer.remaining() > 0) {
        int i = Math.min(paramByteBuffer.remaining(), arrayOfByte1.length);
        paramByteBuffer.get(arrayOfByte1, 0, i);
        paramOutputStream.write(arrayOfByte1, 0, i);
      } 
      BUFFER_REF.set(arrayOfByte1);
    } 
  }
  
  private static class ByteBufferStream extends InputStream {
    private static final int UNSET = -1;
    
    private final ByteBuffer byteBuffer;
    
    private int markPos = -1;
    
    ByteBufferStream(ByteBuffer param1ByteBuffer) {
      this.byteBuffer = param1ByteBuffer;
    }
    
    public int available() {
      return this.byteBuffer.remaining();
    }
    
    public void mark(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_0
      //   4: getfield byteBuffer : Ljava/nio/ByteBuffer;
      //   7: invokevirtual position : ()I
      //   10: putfield markPos : I
      //   13: aload_0
      //   14: monitorexit
      //   15: return
      //   16: astore_2
      //   17: aload_0
      //   18: monitorexit
      //   19: aload_2
      //   20: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	16	finally
    }
    
    public boolean markSupported() {
      return true;
    }
    
    public int read() {
      return !this.byteBuffer.hasRemaining() ? -1 : (this.byteBuffer.get() & 0xFF);
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (!this.byteBuffer.hasRemaining())
        return -1; 
      param1Int2 = Math.min(param1Int2, available());
      this.byteBuffer.get(param1ArrayOfbyte, param1Int1, param1Int2);
      return param1Int2;
    }
    
    public void reset() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield markPos : I
      //   6: istore_1
      //   7: iload_1
      //   8: iconst_m1
      //   9: if_icmpeq -> 24
      //   12: aload_0
      //   13: getfield byteBuffer : Ljava/nio/ByteBuffer;
      //   16: iload_1
      //   17: invokevirtual position : (I)Ljava/nio/Buffer;
      //   20: pop
      //   21: aload_0
      //   22: monitorexit
      //   23: return
      //   24: new java/io/IOException
      //   27: astore_2
      //   28: aload_2
      //   29: ldc 'Cannot reset to unset mark position'
      //   31: invokespecial <init> : (Ljava/lang/String;)V
      //   34: aload_2
      //   35: athrow
      //   36: astore_2
      //   37: aload_0
      //   38: monitorexit
      //   39: aload_2
      //   40: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	36	finally
      //   12	21	36	finally
      //   24	36	36	finally
    }
    
    public long skip(long param1Long) throws IOException {
      if (!this.byteBuffer.hasRemaining())
        return -1L; 
      param1Long = Math.min(param1Long, available());
      ByteBuffer byteBuffer = this.byteBuffer;
      byteBuffer.position((int)(byteBuffer.position() + param1Long));
      return param1Long;
    }
  }
  
  static final class SafeArray {
    final byte[] data;
    
    final int limit;
    
    final int offset;
    
    SafeArray(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      this.data = param1ArrayOfbyte;
      this.offset = param1Int1;
      this.limit = param1Int2;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\ByteBufferUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */