package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StandardGifDecoder implements GifDecoder {
  private static final int BYTES_PER_INTEGER = 4;
  
  private static final int COLOR_TRANSPARENT_BLACK = 0;
  
  private static final int INITIAL_FRAME_POINTER = -1;
  
  private static final int MASK_INT_LOWEST_BYTE = 255;
  
  private static final int MAX_STACK_SIZE = 4096;
  
  private static final int NULL_CODE = -1;
  
  private static final String TAG = StandardGifDecoder.class.getSimpleName();
  
  private int[] act;
  
  private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
  
  private final GifDecoder.BitmapProvider bitmapProvider;
  
  private byte[] block;
  
  private int downsampledHeight;
  
  private int downsampledWidth;
  
  private int framePointer;
  
  private GifHeader header;
  
  private Boolean isFirstFrameTransparent;
  
  private byte[] mainPixels;
  
  private int[] mainScratch;
  
  private GifHeaderParser parser;
  
  private final int[] pct = new int[256];
  
  private byte[] pixelStack;
  
  private short[] prefix;
  
  private Bitmap previousImage;
  
  private ByteBuffer rawData;
  
  private int sampleSize;
  
  private boolean savePrevious;
  
  private int status;
  
  private byte[] suffix;
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider) {
    this.bitmapProvider = paramBitmapProvider;
    this.header = new GifHeader();
  }
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider, GifHeader paramGifHeader, ByteBuffer paramByteBuffer) {
    this(paramBitmapProvider, paramGifHeader, paramByteBuffer, 1);
  }
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider, GifHeader paramGifHeader, ByteBuffer paramByteBuffer, int paramInt) {
    this(paramBitmapProvider);
    setData(paramGifHeader, paramByteBuffer, paramInt);
  }
  
  private int averageColorsNear(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int i1 = 0;
    int n = 0;
    int m = 0;
    int k = 0;
    int j = 0;
    int i2 = paramInt1;
    while (i2 < this.sampleSize + paramInt1) {
      byte[] arrayOfByte = this.mainPixels;
      if (i2 < arrayOfByte.length && i2 < paramInt2) {
        int i5 = arrayOfByte[i2];
        int i10 = this.act[i5 & 0xFF];
        int i9 = i1;
        int i8 = n;
        int i7 = m;
        int i6 = k;
        i5 = j;
        if (i10 != 0) {
          i9 = i1 + (i10 >> 24 & 0xFF);
          i8 = n + (i10 >> 16 & 0xFF);
          i7 = m + (i10 >> 8 & 0xFF);
          i6 = k + (i10 & 0xFF);
          i5 = j + 1;
        } 
        i2++;
        i1 = i9;
        n = i8;
        m = i7;
        k = i6;
        j = i5;
      } 
    } 
    i2 = paramInt1 + paramInt3;
    int i3 = n;
    int i4 = i1;
    while (i2 < paramInt1 + paramInt3 + this.sampleSize) {
      byte[] arrayOfByte = this.mainPixels;
      if (i2 < arrayOfByte.length && i2 < paramInt2) {
        int i5;
        n = arrayOfByte[i2];
        int i10 = this.act[n & 0xFF];
        int i9 = i4;
        int i8 = i3;
        int i7 = m;
        i1 = k;
        int i6 = j;
        if (i10 != 0) {
          i9 = i4 + (i10 >> 24 & 0xFF);
          i8 = i3 + (i10 >> 16 & 0xFF);
          i7 = m + (i10 >> 8 & 0xFF);
          i1 = k + (i10 & 0xFF);
          i5 = j + 1;
        } 
        i2++;
        i4 = i9;
        i3 = i8;
        m = i7;
        k = i1;
        i = i5;
      } 
    } 
    return (i == 0) ? 0 : (i4 / i << 24 | i3 / i << 16 | m / i << 8 | k / i);
  }
  
  private void copyCopyIntoScratchRobust(GifFrame paramGifFrame) {
    boolean bool;
    int[] arrayOfInt2 = this.mainScratch;
    int i2 = paramGifFrame.ih / this.sampleSize;
    int i1 = paramGifFrame.iy / this.sampleSize;
    int n = paramGifFrame.iw / this.sampleSize;
    int m = paramGifFrame.ix / this.sampleSize;
    int j = 0;
    if (this.framePointer == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    int i5 = this.sampleSize;
    int i3 = this.downsampledWidth;
    int i4 = this.downsampledHeight;
    byte[] arrayOfByte = this.mainPixels;
    int[] arrayOfInt1 = this.act;
    int k = 1;
    Boolean bool1 = this.isFirstFrameTransparent;
    int i = 8;
    byte b = 0;
    while (b < i2) {
      int i7;
      int i6 = b;
      int i10 = j;
      int i8 = k;
      int i9 = i;
      if (paramGifFrame.interlace) {
        i6 = j;
        i8 = k;
        i7 = i;
        if (j >= i2) {
          i8 = k + 1;
          switch (i8) {
            default:
              i6 = j;
              i7 = i;
              break;
            case 4:
              i6 = 1;
              i7 = 2;
              break;
            case 3:
              i6 = 2;
              i7 = 4;
              break;
            case 2:
              i6 = 4;
              i7 = i;
              break;
          } 
        } 
        i = i6;
        i10 = i6 + i7;
        i6 = i;
        i9 = i7;
      } 
      i6 += i1;
      if (i5 == 1) {
        i7 = 1;
      } else {
        i7 = 0;
      } 
      if (i6 < i4) {
        k = i6 * i3;
        j = k + m;
        i = j + n;
        i6 = i;
        if (k + i3 < i)
          i6 = k + i3; 
        k = b * i5 * paramGifFrame.iw;
        if (i7) {
          i = j;
          while (i < i6) {
            Boolean bool2;
            j = arrayOfInt1[arrayOfByte[k] & 0xFF];
            if (j != 0) {
              arrayOfInt2[i] = j;
              bool2 = bool1;
            } else {
              bool2 = bool1;
              if (bool) {
                bool2 = bool1;
                if (bool1 == null)
                  bool2 = Boolean.valueOf(true); 
              } 
            } 
            k += i5;
            i++;
            bool1 = bool2;
          } 
        } else {
          int i11 = j;
          i = k;
          i7 = i6;
          while (i11 < i7) {
            int i12 = averageColorsNear(i, (i6 - j) * i5 + k, paramGifFrame.iw);
            if (i12 != 0) {
              arrayOfInt2[i11] = i12;
            } else if (bool && bool1 == null) {
              bool1 = Boolean.valueOf(true);
            } 
            i += i5;
            i11++;
          } 
        } 
      } 
      b++;
      j = i10;
      k = i8;
      i = i9;
    } 
    if (this.isFirstFrameTransparent == null) {
      boolean bool2;
      if (bool1 == null) {
        bool2 = false;
      } else {
        bool2 = bool1.booleanValue();
      } 
      this.isFirstFrameTransparent = Boolean.valueOf(bool2);
    } 
  }
  
  private void copyIntoScratchFast(GifFrame paramGifFrame) {
    boolean bool1;
    boolean bool2;
    int[] arrayOfInt1 = this.mainScratch;
    int j = paramGifFrame.ih;
    int i = paramGifFrame.iy;
    int n = paramGifFrame.iw;
    int m = paramGifFrame.ix;
    if (this.framePointer == 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int i1 = this.downsampledWidth;
    byte[] arrayOfByte = this.mainPixels;
    int[] arrayOfInt2 = this.act;
    int k = -1;
    for (byte b = 0; b < j; b++) {
      int i5 = (b + i) * i1;
      int i4 = i5 + m;
      int i3 = i4 + n;
      int i2 = i3;
      if (i5 + i1 < i3)
        i2 = i5 + i1; 
      i3 = paramGifFrame.iw * b;
      while (i4 < i2) {
        byte b1 = arrayOfByte[i3];
        int i6 = b1 & 0xFF;
        i5 = k;
        if (i6 != k) {
          i5 = arrayOfInt2[i6];
          if (i5 != 0) {
            arrayOfInt1[i4] = i5;
            i5 = k;
          } else {
            i5 = b1;
          } 
        } 
        i3++;
        i4++;
        k = i5;
      } 
    } 
    Boolean bool = this.isFirstFrameTransparent;
    if ((bool != null && bool.booleanValue()) || (this.isFirstFrameTransparent == null && bool1 && k != -1)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.isFirstFrameTransparent = Boolean.valueOf(bool2);
  }
  
  private void decodeBitmapData(GifFrame paramGifFrame) {
    int j;
    if (paramGifFrame != null)
      this.rawData.position(paramGifFrame.bufferFrameStart); 
    if (paramGifFrame == null) {
      j = this.header.width;
      i = this.header.height;
    } else {
      j = paramGifFrame.iw;
      i = paramGifFrame.ih;
    } 
    int i2 = j * i;
    byte[] arrayOfByte1 = this.mainPixels;
    if (arrayOfByte1 == null || arrayOfByte1.length < i2)
      this.mainPixels = this.bitmapProvider.obtainByteArray(i2); 
    byte[] arrayOfByte2 = this.mainPixels;
    if (this.prefix == null)
      this.prefix = new short[4096]; 
    short[] arrayOfShort = this.prefix;
    if (this.suffix == null)
      this.suffix = new byte[4096]; 
    byte[] arrayOfByte3 = this.suffix;
    if (this.pixelStack == null)
      this.pixelStack = new byte[4097]; 
    byte[] arrayOfByte4 = this.pixelStack;
    int i4 = readByte();
    int i3 = 1 << i4;
    int i1 = i3 + 2;
    int n = -1;
    int m = i4 + 1;
    int k = (1 << m) - 1;
    int i = 0;
    while (true) {
      int i10 = 0;
      if (i < i3) {
        arrayOfShort[i] = 0;
        arrayOfByte3[i] = (byte)i;
        i++;
        continue;
      } 
      arrayOfByte1 = this.block;
      int i6 = 0;
      int i8 = 0;
      i = 0;
      int i7 = 0;
      int i5 = 0;
      int i9 = 0;
      j = 0;
      label76: while (j < i2) {
        if (!i7) {
          i7 = readBlock();
          if (i7 <= 0) {
            this.status = 3;
            break;
          } 
          i6 = 0;
        } 
        i9 += (arrayOfByte1[i6] & 0xFF) << i5;
        int i12 = i6 + 1;
        int i13 = i7 - 1;
        i6 = i5 + 8;
        i5 = i;
        i7 = i8;
        i8 = k;
        i = n;
        k = i1;
        n = i10;
        i1 = i6;
        while (i1 >= m) {
          i6 = i9 & i8;
          i9 >>= m;
          i1 -= m;
          if (i6 == i3) {
            m = i4 + 1;
            i8 = (1 << m) - 1;
            k = i3 + 2;
            i = -1;
            continue;
          } 
          if (i6 == i3 + 1) {
            int i16 = i1;
            i10 = n;
            i1 = k;
            n = i;
            k = i8;
            i6 = i12;
            i8 = i7;
            i = i5;
            i7 = i13;
            i5 = i16;
            continue label76;
          } 
          if (i == -1) {
            arrayOfByte2[n] = arrayOfByte3[i6];
            n++;
            j++;
            i = i6;
            i5 = i6;
            continue;
          } 
          if (i6 >= k) {
            arrayOfByte4[i7] = (byte)i5;
            i5 = i7 + 1;
            i7 = i;
          } else {
            i5 = i7;
            i7 = i6;
          } 
          while (i7 >= i3) {
            arrayOfByte4[i5] = arrayOfByte3[i7];
            i5++;
            i7 = arrayOfShort[i7];
          } 
          int i15 = arrayOfByte3[i7] & 0xFF;
          arrayOfByte2[n] = (byte)i15;
          n++;
          j++;
          while (i5 > 0) {
            arrayOfByte2[n] = arrayOfByte4[--i5];
            n++;
            j++;
          } 
          i7 = k;
          i10 = m;
          int i14 = i8;
          if (k < 4096) {
            arrayOfShort[k] = (short)i;
            arrayOfByte3[k] = (byte)i15;
            i = k + 1;
            if ((i & i8) == 0) {
              i7 = i;
              i10 = m;
              i14 = i8;
              if (i < 4096) {
                i10 = m + 1;
                i14 = i8 + i;
                i7 = i;
              } 
            } else {
              i14 = i8;
              i10 = m;
              i7 = i;
            } 
          } 
          i = i6;
          i6 = i15;
          k = i7;
          m = i10;
          i8 = i14;
          i7 = i5;
          i5 = i6;
        } 
        int i11 = i1;
        i10 = n;
        i1 = k;
        n = i;
        k = i8;
        i6 = i12;
        i8 = i7;
        i = i5;
        i7 = i13;
        i5 = i11;
      } 
      Arrays.fill(arrayOfByte2, i10, i2, (byte)0);
      return;
    } 
  }
  
  private GifHeaderParser getHeaderParser() {
    if (this.parser == null)
      this.parser = new GifHeaderParser(); 
    return this.parser;
  }
  
  private Bitmap getNextBitmap() {
    Boolean bool = this.isFirstFrameTransparent;
    if (bool == null || bool.booleanValue()) {
      Bitmap.Config config1 = Bitmap.Config.ARGB_8888;
      Bitmap bitmap1 = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, config1);
      bitmap1.setHasAlpha(true);
      return bitmap1;
    } 
    Bitmap.Config config = this.bitmapConfig;
    Bitmap bitmap = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, config);
    bitmap.setHasAlpha(true);
    return bitmap;
  }
  
  private int readBlock() {
    int i = readByte();
    if (i <= 0)
      return i; 
    ByteBuffer byteBuffer = this.rawData;
    byteBuffer.get(this.block, 0, Math.min(i, byteBuffer.remaining()));
    return i;
  }
  
  private int readByte() {
    return this.rawData.get() & 0xFF;
  }
  
  private Bitmap setPixels(GifFrame paramGifFrame1, GifFrame paramGifFrame2) {
    int[] arrayOfInt = this.mainScratch;
    if (paramGifFrame2 == null) {
      Bitmap bitmap1 = this.previousImage;
      if (bitmap1 != null)
        this.bitmapProvider.release(bitmap1); 
      this.previousImage = null;
      Arrays.fill(arrayOfInt, 0);
    } 
    if (paramGifFrame2 != null && paramGifFrame2.dispose == 3 && this.previousImage == null)
      Arrays.fill(arrayOfInt, 0); 
    if (paramGifFrame2 != null && paramGifFrame2.dispose > 0)
      if (paramGifFrame2.dispose == 2) {
        int j = 0;
        if (!paramGifFrame1.transparency) {
          int i4 = this.header.bgColor;
          j = i4;
          if (paramGifFrame1.lct != null) {
            j = i4;
            if (this.header.bgIndex == paramGifFrame1.transIndex)
              j = 0; 
          } 
        } 
        int i2 = paramGifFrame2.ih / this.sampleSize;
        int m = paramGifFrame2.iy / this.sampleSize;
        int i1 = paramGifFrame2.iw / this.sampleSize;
        int k = paramGifFrame2.ix / this.sampleSize;
        int i3 = this.downsampledWidth;
        int n = m * i3 + k;
        for (k = n; k < i3 * i2 + n; k += this.downsampledWidth) {
          for (m = k; m < k + i1; m++)
            arrayOfInt[m] = j; 
        } 
      } else if (paramGifFrame2.dispose == 3) {
        Bitmap bitmap1 = this.previousImage;
        if (bitmap1 != null) {
          int j = this.downsampledWidth;
          bitmap1.getPixels(arrayOfInt, 0, j, 0, 0, j, this.downsampledHeight);
        } 
      }  
    decodeBitmapData(paramGifFrame1);
    if (paramGifFrame1.interlace || this.sampleSize != 1) {
      copyCopyIntoScratchRobust(paramGifFrame1);
    } else {
      copyIntoScratchFast(paramGifFrame1);
    } 
    if (this.savePrevious && (paramGifFrame1.dispose == 0 || paramGifFrame1.dispose == 1)) {
      if (this.previousImage == null)
        this.previousImage = getNextBitmap(); 
      Bitmap bitmap1 = this.previousImage;
      int j = this.downsampledWidth;
      bitmap1.setPixels(arrayOfInt, 0, j, 0, 0, j, this.downsampledHeight);
    } 
    Bitmap bitmap = getNextBitmap();
    int i = this.downsampledWidth;
    bitmap.setPixels(arrayOfInt, 0, i, 0, 0, i, this.downsampledHeight);
    return bitmap;
  }
  
  public void advance() {
    this.framePointer = (this.framePointer + 1) % this.header.frameCount;
  }
  
  public void clear() {
    this.header = null;
    byte[] arrayOfByte2 = this.mainPixels;
    if (arrayOfByte2 != null)
      this.bitmapProvider.release(arrayOfByte2); 
    int[] arrayOfInt = this.mainScratch;
    if (arrayOfInt != null)
      this.bitmapProvider.release(arrayOfInt); 
    Bitmap bitmap = this.previousImage;
    if (bitmap != null)
      this.bitmapProvider.release(bitmap); 
    this.previousImage = null;
    this.rawData = null;
    this.isFirstFrameTransparent = null;
    byte[] arrayOfByte1 = this.block;
    if (arrayOfByte1 != null)
      this.bitmapProvider.release(arrayOfByte1); 
  }
  
  public int getByteSize() {
    return this.rawData.limit() + this.mainPixels.length + this.mainScratch.length * 4;
  }
  
  public int getCurrentFrameIndex() {
    return this.framePointer;
  }
  
  public ByteBuffer getData() {
    return this.rawData;
  }
  
  public int getDelay(int paramInt) {
    byte b = -1;
    int i = b;
    if (paramInt >= 0) {
      i = b;
      if (paramInt < this.header.frameCount)
        i = ((GifFrame)this.header.frames.get(paramInt)).delay; 
    } 
    return i;
  }
  
  public int getFrameCount() {
    return this.header.frameCount;
  }
  
  public int getHeight() {
    return this.header.height;
  }
  
  @Deprecated
  public int getLoopCount() {
    return (this.header.loopCount == -1) ? 1 : this.header.loopCount;
  }
  
  public int getNetscapeLoopCount() {
    return this.header.loopCount;
  }
  
  public int getNextDelay() {
    if (this.header.frameCount > 0) {
      int i = this.framePointer;
      if (i >= 0)
        return getDelay(i); 
    } 
    return 0;
  }
  
  public Bitmap getNextFrame() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   6: getfield frameCount : I
    //   9: ifle -> 19
    //   12: aload_0
    //   13: getfield framePointer : I
    //   16: ifge -> 82
    //   19: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   22: astore_3
    //   23: aload_3
    //   24: iconst_3
    //   25: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   28: ifeq -> 77
    //   31: new java/lang/StringBuilder
    //   34: astore_2
    //   35: aload_2
    //   36: invokespecial <init> : ()V
    //   39: aload_3
    //   40: aload_2
    //   41: ldc_w 'Unable to decode frame, frameCount='
    //   44: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: aload_0
    //   48: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   51: getfield frameCount : I
    //   54: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   57: ldc_w ', framePointer='
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: aload_0
    //   64: getfield framePointer : I
    //   67: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   70: invokevirtual toString : ()Ljava/lang/String;
    //   73: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   76: pop
    //   77: aload_0
    //   78: iconst_1
    //   79: putfield status : I
    //   82: aload_0
    //   83: getfield status : I
    //   86: istore_1
    //   87: iload_1
    //   88: iconst_1
    //   89: if_icmpeq -> 343
    //   92: iload_1
    //   93: iconst_2
    //   94: if_icmpne -> 100
    //   97: goto -> 343
    //   100: aload_0
    //   101: iconst_0
    //   102: putfield status : I
    //   105: aload_0
    //   106: getfield block : [B
    //   109: ifnonnull -> 128
    //   112: aload_0
    //   113: aload_0
    //   114: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   117: sipush #255
    //   120: invokeinterface obtainByteArray : (I)[B
    //   125: putfield block : [B
    //   128: aload_0
    //   129: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   132: getfield frames : Ljava/util/List;
    //   135: aload_0
    //   136: getfield framePointer : I
    //   139: invokeinterface get : (I)Ljava/lang/Object;
    //   144: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   147: astore #4
    //   149: aconst_null
    //   150: astore_2
    //   151: aload_0
    //   152: getfield framePointer : I
    //   155: iconst_1
    //   156: isub
    //   157: istore_1
    //   158: iload_1
    //   159: iflt -> 179
    //   162: aload_0
    //   163: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   166: getfield frames : Ljava/util/List;
    //   169: iload_1
    //   170: invokeinterface get : (I)Ljava/lang/Object;
    //   175: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   178: astore_2
    //   179: aload #4
    //   181: getfield lct : [I
    //   184: ifnull -> 196
    //   187: aload #4
    //   189: getfield lct : [I
    //   192: astore_3
    //   193: goto -> 204
    //   196: aload_0
    //   197: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   200: getfield gct : [I
    //   203: astore_3
    //   204: aload_0
    //   205: aload_3
    //   206: putfield act : [I
    //   209: aload_3
    //   210: ifnonnull -> 264
    //   213: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   216: astore_2
    //   217: aload_2
    //   218: iconst_3
    //   219: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   222: ifeq -> 255
    //   225: new java/lang/StringBuilder
    //   228: astore_3
    //   229: aload_3
    //   230: invokespecial <init> : ()V
    //   233: aload_2
    //   234: aload_3
    //   235: ldc_w 'No valid color table found for frame #'
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: aload_0
    //   242: getfield framePointer : I
    //   245: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   248: invokevirtual toString : ()Ljava/lang/String;
    //   251: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   254: pop
    //   255: aload_0
    //   256: iconst_1
    //   257: putfield status : I
    //   260: aload_0
    //   261: monitorexit
    //   262: aconst_null
    //   263: areturn
    //   264: aload #4
    //   266: getfield transparency : Z
    //   269: ifeq -> 331
    //   272: aload_0
    //   273: getfield act : [I
    //   276: astore_3
    //   277: aload_3
    //   278: iconst_0
    //   279: aload_0
    //   280: getfield pct : [I
    //   283: iconst_0
    //   284: aload_3
    //   285: arraylength
    //   286: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   289: aload_0
    //   290: getfield pct : [I
    //   293: astore_3
    //   294: aload_0
    //   295: aload_3
    //   296: putfield act : [I
    //   299: aload_3
    //   300: aload #4
    //   302: getfield transIndex : I
    //   305: iconst_0
    //   306: iastore
    //   307: aload #4
    //   309: getfield dispose : I
    //   312: iconst_2
    //   313: if_icmpne -> 331
    //   316: aload_0
    //   317: getfield framePointer : I
    //   320: ifne -> 331
    //   323: aload_0
    //   324: iconst_1
    //   325: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   328: putfield isFirstFrameTransparent : Ljava/lang/Boolean;
    //   331: aload_0
    //   332: aload #4
    //   334: aload_2
    //   335: invokespecial setPixels : (Lcom/bumptech/glide/gifdecoder/GifFrame;Lcom/bumptech/glide/gifdecoder/GifFrame;)Landroid/graphics/Bitmap;
    //   338: astore_2
    //   339: aload_0
    //   340: monitorexit
    //   341: aload_2
    //   342: areturn
    //   343: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   346: astore_3
    //   347: aload_3
    //   348: iconst_3
    //   349: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   352: ifeq -> 385
    //   355: new java/lang/StringBuilder
    //   358: astore_2
    //   359: aload_2
    //   360: invokespecial <init> : ()V
    //   363: aload_3
    //   364: aload_2
    //   365: ldc_w 'Unable to decode frame, status='
    //   368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: aload_0
    //   372: getfield status : I
    //   375: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   378: invokevirtual toString : ()Ljava/lang/String;
    //   381: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   384: pop
    //   385: aload_0
    //   386: monitorexit
    //   387: aconst_null
    //   388: areturn
    //   389: astore_2
    //   390: aload_0
    //   391: monitorexit
    //   392: aload_2
    //   393: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	389	finally
    //   19	77	389	finally
    //   77	82	389	finally
    //   82	87	389	finally
    //   100	128	389	finally
    //   128	149	389	finally
    //   151	158	389	finally
    //   162	179	389	finally
    //   179	193	389	finally
    //   196	204	389	finally
    //   204	209	389	finally
    //   213	255	389	finally
    //   255	260	389	finally
    //   264	331	389	finally
    //   331	339	389	finally
    //   343	385	389	finally
  }
  
  public int getStatus() {
    return this.status;
  }
  
  public int getTotalIterationCount() {
    return (this.header.loopCount == -1) ? 1 : ((this.header.loopCount == 0) ? 0 : (this.header.loopCount + 1));
  }
  
  public int getWidth() {
    return this.header.width;
  }
  
  public int read(InputStream paramInputStream, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 96
    //   4: iload_2
    //   5: ifle -> 17
    //   8: wide iinc #2 4096
    //   14: goto -> 21
    //   17: sipush #16384
    //   20: istore_2
    //   21: new java/io/ByteArrayOutputStream
    //   24: astore #4
    //   26: aload #4
    //   28: iload_2
    //   29: invokespecial <init> : (I)V
    //   32: sipush #16384
    //   35: newarray byte
    //   37: astore_3
    //   38: aload_1
    //   39: aload_3
    //   40: iconst_0
    //   41: aload_3
    //   42: arraylength
    //   43: invokevirtual read : ([BII)I
    //   46: istore_2
    //   47: iload_2
    //   48: iconst_m1
    //   49: if_icmpeq -> 63
    //   52: aload #4
    //   54: aload_3
    //   55: iconst_0
    //   56: iload_2
    //   57: invokevirtual write : ([BII)V
    //   60: goto -> 38
    //   63: aload #4
    //   65: invokevirtual flush : ()V
    //   68: aload_0
    //   69: aload #4
    //   71: invokevirtual toByteArray : ()[B
    //   74: invokevirtual read : ([B)I
    //   77: pop
    //   78: goto -> 101
    //   81: astore_3
    //   82: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   85: ldc_w 'Error reading data from stream'
    //   88: aload_3
    //   89: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   92: pop
    //   93: goto -> 101
    //   96: aload_0
    //   97: iconst_2
    //   98: putfield status : I
    //   101: aload_1
    //   102: ifnull -> 127
    //   105: aload_1
    //   106: invokevirtual close : ()V
    //   109: goto -> 127
    //   112: astore_1
    //   113: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   116: ldc_w 'Error closing stream'
    //   119: aload_1
    //   120: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   123: pop
    //   124: goto -> 127
    //   127: aload_0
    //   128: getfield status : I
    //   131: ireturn
    // Exception table:
    //   from	to	target	type
    //   21	38	81	java/io/IOException
    //   38	47	81	java/io/IOException
    //   52	60	81	java/io/IOException
    //   63	78	81	java/io/IOException
    //   105	109	112	java/io/IOException
  }
  
  public int read(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial getHeaderParser : ()Lcom/bumptech/glide/gifdecoder/GifHeaderParser;
    //   6: aload_1
    //   7: invokevirtual setData : ([B)Lcom/bumptech/glide/gifdecoder/GifHeaderParser;
    //   10: invokevirtual parseHeader : ()Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   13: astore_3
    //   14: aload_0
    //   15: aload_3
    //   16: putfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   19: aload_1
    //   20: ifnull -> 29
    //   23: aload_0
    //   24: aload_3
    //   25: aload_1
    //   26: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;[B)V
    //   29: aload_0
    //   30: getfield status : I
    //   33: istore_2
    //   34: aload_0
    //   35: monitorexit
    //   36: iload_2
    //   37: ireturn
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	38	finally
    //   23	29	38	finally
    //   29	34	38	finally
  }
  
  public void resetFrameIndex() {
    this.framePointer = -1;
  }
  
  public void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: iconst_1
    //   6: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;Ljava/nio/ByteBuffer;I)V
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
  
  public void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_3
    //   3: ifle -> 180
    //   6: iload_3
    //   7: invokestatic highestOneBit : (I)I
    //   10: istore_3
    //   11: aload_0
    //   12: iconst_0
    //   13: putfield status : I
    //   16: aload_0
    //   17: aload_1
    //   18: putfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   21: aload_0
    //   22: iconst_m1
    //   23: putfield framePointer : I
    //   26: aload_2
    //   27: invokevirtual asReadOnlyBuffer : ()Ljava/nio/ByteBuffer;
    //   30: astore_2
    //   31: aload_0
    //   32: aload_2
    //   33: putfield rawData : Ljava/nio/ByteBuffer;
    //   36: aload_2
    //   37: iconst_0
    //   38: invokevirtual position : (I)Ljava/nio/Buffer;
    //   41: pop
    //   42: aload_0
    //   43: getfield rawData : Ljava/nio/ByteBuffer;
    //   46: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   49: invokevirtual order : (Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
    //   52: pop
    //   53: aload_0
    //   54: iconst_0
    //   55: putfield savePrevious : Z
    //   58: aload_1
    //   59: getfield frames : Ljava/util/List;
    //   62: invokeinterface iterator : ()Ljava/util/Iterator;
    //   67: astore_2
    //   68: aload_2
    //   69: invokeinterface hasNext : ()Z
    //   74: ifeq -> 104
    //   77: aload_2
    //   78: invokeinterface next : ()Ljava/lang/Object;
    //   83: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   86: getfield dispose : I
    //   89: iconst_3
    //   90: if_icmpne -> 101
    //   93: aload_0
    //   94: iconst_1
    //   95: putfield savePrevious : Z
    //   98: goto -> 104
    //   101: goto -> 68
    //   104: aload_0
    //   105: iload_3
    //   106: putfield sampleSize : I
    //   109: aload_0
    //   110: aload_1
    //   111: getfield width : I
    //   114: iload_3
    //   115: idiv
    //   116: putfield downsampledWidth : I
    //   119: aload_0
    //   120: aload_1
    //   121: getfield height : I
    //   124: iload_3
    //   125: idiv
    //   126: putfield downsampledHeight : I
    //   129: aload_0
    //   130: aload_0
    //   131: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   134: aload_1
    //   135: getfield width : I
    //   138: aload_1
    //   139: getfield height : I
    //   142: imul
    //   143: invokeinterface obtainByteArray : (I)[B
    //   148: putfield mainPixels : [B
    //   151: aload_0
    //   152: aload_0
    //   153: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   156: aload_0
    //   157: getfield downsampledWidth : I
    //   160: aload_0
    //   161: getfield downsampledHeight : I
    //   164: imul
    //   165: invokeinterface obtainIntArray : (I)[I
    //   170: putfield mainScratch : [I
    //   173: aload_0
    //   174: monitorexit
    //   175: return
    //   176: astore_1
    //   177: goto -> 212
    //   180: new java/lang/IllegalArgumentException
    //   183: astore_1
    //   184: new java/lang/StringBuilder
    //   187: astore_2
    //   188: aload_2
    //   189: invokespecial <init> : ()V
    //   192: aload_1
    //   193: aload_2
    //   194: ldc_w 'Sample size must be >=0, not: '
    //   197: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: iload_3
    //   201: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   204: invokevirtual toString : ()Ljava/lang/String;
    //   207: invokespecial <init> : (Ljava/lang/String;)V
    //   210: aload_1
    //   211: athrow
    //   212: aload_0
    //   213: monitorexit
    //   214: aload_1
    //   215: athrow
    // Exception table:
    //   from	to	target	type
    //   6	68	176	finally
    //   68	98	176	finally
    //   104	173	176	finally
    //   180	212	176	finally
  }
  
  public void setData(GifHeader paramGifHeader, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
    //   8: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;Ljava/nio/ByteBuffer;)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void setDefaultBitmapConfig(Bitmap.Config paramConfig) {
    if (paramConfig == Bitmap.Config.ARGB_8888 || paramConfig == Bitmap.Config.RGB_565) {
      this.bitmapConfig = paramConfig;
      return;
    } 
    throw new IllegalArgumentException("Unsupported format: " + paramConfig + ", must be one of " + Bitmap.Config.ARGB_8888 + " or " + Bitmap.Config.RGB_565);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\gifdecoder\StandardGifDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */