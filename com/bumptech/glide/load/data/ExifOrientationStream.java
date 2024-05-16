package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream extends FilterInputStream {
  private static final byte[] EXIF_SEGMENT;
  
  private static final int ORIENTATION_POSITION;
  
  private static final int SEGMENT_LENGTH;
  
  private static final int SEGMENT_START_POSITION = 2;
  
  private final byte orientation;
  
  private int position;
  
  static {
    byte[] arrayOfByte = new byte[29];
    arrayOfByte[0] = -1;
    arrayOfByte[1] = -31;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 28;
    arrayOfByte[4] = 69;
    arrayOfByte[5] = 120;
    arrayOfByte[6] = 105;
    arrayOfByte[7] = 102;
    arrayOfByte[8] = 0;
    arrayOfByte[9] = 0;
    arrayOfByte[10] = 77;
    arrayOfByte[11] = 77;
    arrayOfByte[12] = 0;
    arrayOfByte[13] = 0;
    arrayOfByte[14] = 0;
    arrayOfByte[15] = 0;
    arrayOfByte[16] = 0;
    arrayOfByte[17] = 8;
    arrayOfByte[18] = 0;
    arrayOfByte[19] = 1;
    arrayOfByte[20] = 1;
    arrayOfByte[21] = 18;
    arrayOfByte[22] = 0;
    arrayOfByte[23] = 2;
    arrayOfByte[24] = 0;
    arrayOfByte[25] = 0;
    arrayOfByte[26] = 0;
    arrayOfByte[27] = 1;
    arrayOfByte[28] = 0;
    EXIF_SEGMENT = arrayOfByte;
    int i = arrayOfByte.length;
    SEGMENT_LENGTH = i;
    ORIENTATION_POSITION = i + 2;
  }
  
  public ExifOrientationStream(InputStream paramInputStream, int paramInt) {
    super(paramInputStream);
    if (paramInt >= -1 && paramInt <= 8) {
      this.orientation = (byte)paramInt;
      return;
    } 
    throw new IllegalArgumentException("Cannot add invalid orientation: " + paramInt);
  }
  
  public void mark(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public boolean markSupported() {
    return false;
  }
  
  public int read() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield position : I
    //   4: istore_1
    //   5: iload_1
    //   6: iconst_2
    //   7: if_icmplt -> 50
    //   10: getstatic com/bumptech/glide/load/data/ExifOrientationStream.ORIENTATION_POSITION : I
    //   13: istore_2
    //   14: iload_1
    //   15: iload_2
    //   16: if_icmple -> 22
    //   19: goto -> 50
    //   22: iload_1
    //   23: iload_2
    //   24: if_icmpne -> 35
    //   27: aload_0
    //   28: getfield orientation : B
    //   31: istore_1
    //   32: goto -> 55
    //   35: getstatic com/bumptech/glide/load/data/ExifOrientationStream.EXIF_SEGMENT : [B
    //   38: iload_1
    //   39: iconst_2
    //   40: isub
    //   41: baload
    //   42: sipush #255
    //   45: iand
    //   46: istore_1
    //   47: goto -> 55
    //   50: aload_0
    //   51: invokespecial read : ()I
    //   54: istore_1
    //   55: iload_1
    //   56: iconst_m1
    //   57: if_icmpeq -> 70
    //   60: aload_0
    //   61: aload_0
    //   62: getfield position : I
    //   65: iconst_1
    //   66: iadd
    //   67: putfield position : I
    //   70: iload_1
    //   71: ireturn
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    int i = this.position;
    int j = ORIENTATION_POSITION;
    if (i > j) {
      paramInt1 = super.read(paramArrayOfbyte, paramInt1, paramInt2);
    } else if (i == j) {
      paramArrayOfbyte[paramInt1] = this.orientation;
      paramInt1 = 1;
    } else if (i < 2) {
      paramInt1 = super.read(paramArrayOfbyte, paramInt1, 2 - i);
    } else {
      paramInt2 = Math.min(j - i, paramInt2);
      System.arraycopy(EXIF_SEGMENT, this.position - 2, paramArrayOfbyte, paramInt1, paramInt2);
      paramInt1 = paramInt2;
    } 
    if (paramInt1 > 0)
      this.position += paramInt1; 
    return paramInt1;
  }
  
  public void reset() throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public long skip(long paramLong) throws IOException {
    paramLong = super.skip(paramLong);
    if (paramLong > 0L)
      this.position = (int)(this.position + paramLong); 
    return paramLong;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\ExifOrientationStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */