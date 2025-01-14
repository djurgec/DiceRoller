package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
  private static final int[] BYTES_PER_FORMAT;
  
  static final int EXIF_MAGIC_NUMBER = 65496;
  
  static final int EXIF_SEGMENT_TYPE = 225;
  
  private static final int GIF_HEADER = 4671814;
  
  private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
  
  private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\000\000";
  
  static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\000\000".getBytes(Charset.forName("UTF-8"));
  
  private static final int MARKER_EOI = 217;
  
  private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
  
  private static final int ORIENTATION_TAG_TYPE = 274;
  
  private static final int PNG_HEADER = -1991225785;
  
  private static final int RIFF_HEADER = 1380533830;
  
  private static final int SEGMENT_SOS = 218;
  
  static final int SEGMENT_START_ID = 255;
  
  private static final String TAG = "DfltImageHeaderParser";
  
  private static final int VP8_HEADER = 1448097792;
  
  private static final int VP8_HEADER_MASK = -256;
  
  private static final int VP8_HEADER_TYPE_EXTENDED = 88;
  
  private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
  
  private static final int VP8_HEADER_TYPE_MASK = 255;
  
  private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
  
  private static final int WEBP_HEADER = 1464156752;
  
  private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;
  
  static {
    BYTES_PER_FORMAT = new int[] { 
        0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 
        8, 4, 8 };
  }
  
  private static int calcTagOffset(int paramInt1, int paramInt2) {
    return paramInt1 + 2 + paramInt2 * 12;
  }
  
  private int getOrientation(Reader paramReader, ArrayPool paramArrayPool) throws IOException {
    try {
      int i = paramReader.getUInt16();
      boolean bool = handles(i);
      if (!bool) {
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
          null = new StringBuilder();
          this();
          Log.d("DfltImageHeaderParser", null.append("Parser doesn't handle magic number: ").append(i).toString());
        } 
        return -1;
      } 
      i = moveToExifSegmentAndGetLength((Reader)null);
      if (i == -1) {
        if (Log.isLoggable("DfltImageHeaderParser", 3))
          Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found"); 
        return -1;
      } 
      byte[] arrayOfByte = (byte[])paramArrayPool.get(i, byte[].class);
      try {
        i = parseExifSegment((Reader)null, arrayOfByte, i);
        return i;
      } finally {
        paramArrayPool.put(arrayOfByte);
      } 
    } catch (EndOfFileException endOfFileException) {
      return -1;
    } 
  }
  
  private ImageHeaderParser.ImageType getType(Reader paramReader) throws IOException {
    try {
      int i = paramReader.getUInt16();
      if (i == 65496)
        return ImageHeaderParser.ImageType.JPEG; 
      i = i << 8 | paramReader.getUInt8();
      if (i == 4671814)
        return ImageHeaderParser.ImageType.GIF; 
      i = i << 8 | paramReader.getUInt8();
      if (i == -1991225785) {
        paramReader.skip(21L);
        try {
          if (paramReader.getUInt8() >= 3) {
            null = ImageHeaderParser.ImageType.PNG_A;
          } else {
            null = ImageHeaderParser.ImageType.PNG;
          } 
          return null;
        } catch (EndOfFileException endOfFileException) {
          return ImageHeaderParser.ImageType.PNG;
        } 
      } 
      if (i != 1380533830)
        return ImageHeaderParser.ImageType.UNKNOWN; 
      endOfFileException.skip(4L);
      if ((endOfFileException.getUInt16() << 16 | endOfFileException.getUInt16()) != 1464156752)
        return ImageHeaderParser.ImageType.UNKNOWN; 
      i = endOfFileException.getUInt16() << 16 | endOfFileException.getUInt16();
      if ((i & 0xFFFFFF00) != 1448097792)
        return ImageHeaderParser.ImageType.UNKNOWN; 
      if ((i & 0xFF) == 88) {
        endOfFileException.skip(4L);
        if ((endOfFileException.getUInt8() & 0x10) != 0) {
          null = ImageHeaderParser.ImageType.WEBP_A;
        } else {
          null = ImageHeaderParser.ImageType.WEBP;
        } 
        return null;
      } 
      if ((i & 0xFF) == 76) {
        null.skip(4L);
        if ((null.getUInt8() & 0x8) != 0) {
          null = ImageHeaderParser.ImageType.WEBP_A;
        } else {
          null = ImageHeaderParser.ImageType.WEBP;
        } 
        return null;
      } 
      return ImageHeaderParser.ImageType.WEBP;
    } catch (EndOfFileException endOfFileException) {
      return ImageHeaderParser.ImageType.UNKNOWN;
    } 
  }
  
  private static boolean handles(int paramInt) {
    return ((paramInt & 0xFFD8) == 65496 || paramInt == 19789 || paramInt == 18761);
  }
  
  private boolean hasJpegExifPreamble(byte[] paramArrayOfbyte, int paramInt) {
    boolean bool1;
    if (paramArrayOfbyte != null && paramInt > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    boolean bool2 = bool1;
    if (bool1) {
      paramInt = 0;
      while (true) {
        byte[] arrayOfByte = JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
        bool2 = bool1;
        if (paramInt < arrayOfByte.length) {
          if (paramArrayOfbyte[paramInt] != arrayOfByte[paramInt]) {
            bool2 = false;
            break;
          } 
          paramInt++;
          continue;
        } 
        break;
      } 
    } 
    return bool2;
  }
  
  private int moveToExifSegmentAndGetLength(Reader paramReader) throws IOException {
    while (true) {
      short s1 = paramReader.getUInt8();
      if (s1 != 255) {
        if (Log.isLoggable("DfltImageHeaderParser", 3))
          Log.d("DfltImageHeaderParser", "Unknown segmentId=" + s1); 
        return -1;
      } 
      short s2 = paramReader.getUInt8();
      if (s2 == 218)
        return -1; 
      if (s2 == 217) {
        if (Log.isLoggable("DfltImageHeaderParser", 3))
          Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment"); 
        return -1;
      } 
      int i = paramReader.getUInt16() - 2;
      if (s2 != 225) {
        long l = paramReader.skip(i);
        if (l != i) {
          if (Log.isLoggable("DfltImageHeaderParser", 3))
            Log.d("DfltImageHeaderParser", "Unable to skip enough data, type: " + s2 + ", wanted to skip: " + i + ", but actually skipped: " + l); 
          return -1;
        } 
        continue;
      } 
      return i;
    } 
  }
  
  private static int parseExifSegment(RandomAccessReader paramRandomAccessReader) {
    // Byte code:
    //   0: ldc 'Exif  '
    //   2: invokevirtual length : ()I
    //   5: istore_2
    //   6: aload_0
    //   7: iload_2
    //   8: invokevirtual getInt16 : (I)S
    //   11: istore_3
    //   12: iconst_3
    //   13: istore_1
    //   14: iload_3
    //   15: lookupswitch default -> 40, 18761 -> 85, 19789 -> 77
    //   40: ldc 'DfltImageHeaderParser'
    //   42: iconst_3
    //   43: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   46: ifeq -> 93
    //   49: ldc 'DfltImageHeaderParser'
    //   51: new java/lang/StringBuilder
    //   54: dup
    //   55: invokespecial <init> : ()V
    //   58: ldc 'Unknown endianness = '
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: iload_3
    //   64: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   73: pop
    //   74: goto -> 93
    //   77: getstatic java/nio/ByteOrder.BIG_ENDIAN : Ljava/nio/ByteOrder;
    //   80: astore #9
    //   82: goto -> 98
    //   85: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   88: astore #9
    //   90: goto -> 98
    //   93: getstatic java/nio/ByteOrder.BIG_ENDIAN : Ljava/nio/ByteOrder;
    //   96: astore #9
    //   98: aload_0
    //   99: aload #9
    //   101: invokevirtual order : (Ljava/nio/ByteOrder;)V
    //   104: aload_0
    //   105: iload_2
    //   106: iconst_4
    //   107: iadd
    //   108: invokevirtual getInt32 : (I)I
    //   111: iload_2
    //   112: iadd
    //   113: istore #5
    //   115: aload_0
    //   116: iload #5
    //   118: invokevirtual getInt16 : (I)S
    //   121: istore #4
    //   123: iconst_0
    //   124: istore_2
    //   125: iload_2
    //   126: iload #4
    //   128: if_icmpge -> 532
    //   131: iload #5
    //   133: iload_2
    //   134: invokestatic calcTagOffset : (II)I
    //   137: istore #8
    //   139: aload_0
    //   140: iload #8
    //   142: invokevirtual getInt16 : (I)S
    //   145: istore_3
    //   146: iload_3
    //   147: sipush #274
    //   150: if_icmpeq -> 156
    //   153: goto -> 526
    //   156: aload_0
    //   157: iload #8
    //   159: iconst_2
    //   160: iadd
    //   161: invokevirtual getInt16 : (I)S
    //   164: istore #6
    //   166: iload #6
    //   168: iconst_1
    //   169: if_icmplt -> 484
    //   172: iload #6
    //   174: bipush #12
    //   176: if_icmple -> 182
    //   179: goto -> 484
    //   182: aload_0
    //   183: iload #8
    //   185: iconst_4
    //   186: iadd
    //   187: invokevirtual getInt32 : (I)I
    //   190: istore #7
    //   192: iload #7
    //   194: ifge -> 220
    //   197: ldc 'DfltImageHeaderParser'
    //   199: iload_1
    //   200: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   203: ifeq -> 217
    //   206: ldc 'DfltImageHeaderParser'
    //   208: ldc 'Negative tiff component count'
    //   210: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   213: pop
    //   214: goto -> 526
    //   217: goto -> 526
    //   220: ldc 'DfltImageHeaderParser'
    //   222: iload_1
    //   223: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   226: ifeq -> 283
    //   229: ldc 'DfltImageHeaderParser'
    //   231: new java/lang/StringBuilder
    //   234: dup
    //   235: invokespecial <init> : ()V
    //   238: ldc 'Got tagIndex='
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: iload_2
    //   244: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   247: ldc ' tagType='
    //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: iload_3
    //   253: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   256: ldc ' formatCode='
    //   258: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: iload #6
    //   263: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   266: ldc ' componentCount='
    //   268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: iload #7
    //   273: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   276: invokevirtual toString : ()Ljava/lang/String;
    //   279: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   282: pop
    //   283: getstatic com/bumptech/glide/load/resource/bitmap/DefaultImageHeaderParser.BYTES_PER_FORMAT : [I
    //   286: iload #6
    //   288: iaload
    //   289: iload #7
    //   291: iadd
    //   292: istore #7
    //   294: iload #7
    //   296: iconst_4
    //   297: if_icmple -> 341
    //   300: ldc 'DfltImageHeaderParser'
    //   302: iload_1
    //   303: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   306: ifeq -> 338
    //   309: ldc 'DfltImageHeaderParser'
    //   311: new java/lang/StringBuilder
    //   314: dup
    //   315: invokespecial <init> : ()V
    //   318: ldc 'Got byte count > 4, not orientation, continuing, formatCode='
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: iload #6
    //   325: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   328: invokevirtual toString : ()Ljava/lang/String;
    //   331: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   334: pop
    //   335: goto -> 526
    //   338: goto -> 526
    //   341: iload #8
    //   343: bipush #8
    //   345: iadd
    //   346: istore_1
    //   347: iload_1
    //   348: iflt -> 431
    //   351: iload_1
    //   352: aload_0
    //   353: invokevirtual length : ()I
    //   356: if_icmple -> 362
    //   359: goto -> 431
    //   362: iload #7
    //   364: iflt -> 387
    //   367: iload_1
    //   368: iload #7
    //   370: iadd
    //   371: aload_0
    //   372: invokevirtual length : ()I
    //   375: if_icmple -> 381
    //   378: goto -> 387
    //   381: aload_0
    //   382: iload_1
    //   383: invokevirtual getInt16 : (I)S
    //   386: ireturn
    //   387: ldc 'DfltImageHeaderParser'
    //   389: iconst_3
    //   390: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   393: ifeq -> 426
    //   396: ldc 'DfltImageHeaderParser'
    //   398: new java/lang/StringBuilder
    //   401: dup
    //   402: invokespecial <init> : ()V
    //   405: ldc 'Illegal number of bytes for TI tag data tagType='
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: iload_3
    //   411: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   414: invokevirtual toString : ()Ljava/lang/String;
    //   417: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   420: pop
    //   421: iconst_3
    //   422: istore_1
    //   423: goto -> 526
    //   426: iconst_3
    //   427: istore_1
    //   428: goto -> 526
    //   431: ldc 'DfltImageHeaderParser'
    //   433: iconst_3
    //   434: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   437: ifeq -> 479
    //   440: ldc 'DfltImageHeaderParser'
    //   442: new java/lang/StringBuilder
    //   445: dup
    //   446: invokespecial <init> : ()V
    //   449: ldc 'Illegal tagValueOffset='
    //   451: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   454: iload_1
    //   455: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   458: ldc ' tagType='
    //   460: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   463: iload_3
    //   464: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   467: invokevirtual toString : ()Ljava/lang/String;
    //   470: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   473: pop
    //   474: iconst_3
    //   475: istore_1
    //   476: goto -> 526
    //   479: iconst_3
    //   480: istore_1
    //   481: goto -> 526
    //   484: iconst_3
    //   485: istore_3
    //   486: iload_3
    //   487: istore_1
    //   488: ldc 'DfltImageHeaderParser'
    //   490: iconst_3
    //   491: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   494: ifeq -> 526
    //   497: ldc 'DfltImageHeaderParser'
    //   499: new java/lang/StringBuilder
    //   502: dup
    //   503: invokespecial <init> : ()V
    //   506: ldc_w 'Got invalid format code = '
    //   509: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: iload #6
    //   514: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   517: invokevirtual toString : ()Ljava/lang/String;
    //   520: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   523: pop
    //   524: iload_3
    //   525: istore_1
    //   526: iinc #2, 1
    //   529: goto -> 125
    //   532: iconst_m1
    //   533: ireturn
  }
  
  private int parseExifSegment(Reader paramReader, byte[] paramArrayOfbyte, int paramInt) throws IOException {
    int i = paramReader.read(paramArrayOfbyte, paramInt);
    if (i != paramInt) {
      if (Log.isLoggable("DfltImageHeaderParser", 3))
        Log.d("DfltImageHeaderParser", "Unable to read exif segment data, length: " + paramInt + ", actually read: " + i); 
      return -1;
    } 
    if (hasJpegExifPreamble(paramArrayOfbyte, paramInt))
      return parseExifSegment(new RandomAccessReader(paramArrayOfbyte, paramInt)); 
    if (Log.isLoggable("DfltImageHeaderParser", 3))
      Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble"); 
    return -1;
  }
  
  public int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(new StreamReader((InputStream)Preconditions.checkNotNull(paramInputStream)), (ArrayPool)Preconditions.checkNotNull(paramArrayPool));
  }
  
  public int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(new ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(paramByteBuffer)), (ArrayPool)Preconditions.checkNotNull(paramArrayPool));
  }
  
  public ImageHeaderParser.ImageType getType(InputStream paramInputStream) throws IOException {
    return getType(new StreamReader((InputStream)Preconditions.checkNotNull(paramInputStream)));
  }
  
  public ImageHeaderParser.ImageType getType(ByteBuffer paramByteBuffer) throws IOException {
    return getType(new ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(paramByteBuffer)));
  }
  
  private static final class ByteBufferReader implements Reader {
    private final ByteBuffer byteBuffer;
    
    ByteBufferReader(ByteBuffer param1ByteBuffer) {
      this.byteBuffer = param1ByteBuffer;
      param1ByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    
    public int getUInt16() throws DefaultImageHeaderParser.Reader.EndOfFileException {
      return getUInt8() << 8 | getUInt8();
    }
    
    public short getUInt8() throws DefaultImageHeaderParser.Reader.EndOfFileException {
      if (this.byteBuffer.remaining() >= 1)
        return (short)(this.byteBuffer.get() & 0xFF); 
      throw new DefaultImageHeaderParser.Reader.EndOfFileException();
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int) {
      param1Int = Math.min(param1Int, this.byteBuffer.remaining());
      if (param1Int == 0)
        return -1; 
      this.byteBuffer.get(param1ArrayOfbyte, 0, param1Int);
      return param1Int;
    }
    
    public long skip(long param1Long) {
      int i = (int)Math.min(this.byteBuffer.remaining(), param1Long);
      ByteBuffer byteBuffer = this.byteBuffer;
      byteBuffer.position(byteBuffer.position() + i);
      return i;
    }
  }
  
  private static final class RandomAccessReader {
    private final ByteBuffer data;
    
    RandomAccessReader(byte[] param1ArrayOfbyte, int param1Int) {
      this.data = (ByteBuffer)ByteBuffer.wrap(param1ArrayOfbyte).order(ByteOrder.BIG_ENDIAN).limit(param1Int);
    }
    
    private boolean isAvailable(int param1Int1, int param1Int2) {
      boolean bool;
      if (this.data.remaining() - param1Int1 >= param1Int2) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    short getInt16(int param1Int) {
      byte b;
      if (isAvailable(param1Int, 2)) {
        b = this.data.getShort(param1Int);
      } else {
        b = -1;
      } 
      return b;
    }
    
    int getInt32(int param1Int) {
      if (isAvailable(param1Int, 4)) {
        param1Int = this.data.getInt(param1Int);
      } else {
        param1Int = -1;
      } 
      return param1Int;
    }
    
    int length() {
      return this.data.remaining();
    }
    
    void order(ByteOrder param1ByteOrder) {
      this.data.order(param1ByteOrder);
    }
  }
  
  private static interface Reader {
    int getUInt16() throws IOException;
    
    short getUInt8() throws IOException;
    
    int read(byte[] param1ArrayOfbyte, int param1Int) throws IOException;
    
    long skip(long param1Long) throws IOException;
    
    public static final class EndOfFileException extends IOException {
      private static final long serialVersionUID = 1L;
      
      EndOfFileException() {
        super("Unexpectedly reached end of a file");
      }
    }
  }
  
  public static final class EndOfFileException extends IOException {
    private static final long serialVersionUID = 1L;
    
    EndOfFileException() {
      super("Unexpectedly reached end of a file");
    }
  }
  
  private static final class StreamReader implements Reader {
    private final InputStream is;
    
    StreamReader(InputStream param1InputStream) {
      this.is = param1InputStream;
    }
    
    public int getUInt16() throws IOException {
      return getUInt8() << 8 | getUInt8();
    }
    
    public short getUInt8() throws IOException {
      int i = this.is.read();
      if (i != -1)
        return (short)i; 
      throw new DefaultImageHeaderParser.Reader.EndOfFileException();
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int) throws IOException {
      int k;
      int j = 0;
      int i = 0;
      while (true) {
        k = i;
        if (j < param1Int) {
          int m = this.is.read(param1ArrayOfbyte, j, param1Int - j);
          i = m;
          k = i;
          if (m != -1) {
            j += i;
            continue;
          } 
        } 
        break;
      } 
      if (j != 0 || k != -1)
        return j; 
      throw new DefaultImageHeaderParser.Reader.EndOfFileException();
    }
    
    public long skip(long param1Long) throws IOException {
      if (param1Long < 0L)
        return 0L; 
      long l;
      for (l = param1Long; l > 0L; l--) {
        long l1 = this.is.skip(l);
        if (l1 > 0L) {
          l -= l1;
          continue;
        } 
        if (this.is.read() == -1)
          break; 
      } 
      return param1Long - l;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\DefaultImageHeaderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */