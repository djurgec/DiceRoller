package com.bumptech.glide.gifdecoder;

import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
  static final int DEFAULT_FRAME_DELAY = 10;
  
  private static final int DESCRIPTOR_MASK_INTERLACE_FLAG = 64;
  
  private static final int DESCRIPTOR_MASK_LCT_FLAG = 128;
  
  private static final int DESCRIPTOR_MASK_LCT_SIZE = 7;
  
  private static final int EXTENSION_INTRODUCER = 33;
  
  private static final int GCE_DISPOSAL_METHOD_SHIFT = 2;
  
  private static final int GCE_MASK_DISPOSAL_METHOD = 28;
  
  private static final int GCE_MASK_TRANSPARENT_COLOR_FLAG = 1;
  
  private static final int IMAGE_SEPARATOR = 44;
  
  private static final int LABEL_APPLICATION_EXTENSION = 255;
  
  private static final int LABEL_COMMENT_EXTENSION = 254;
  
  private static final int LABEL_GRAPHIC_CONTROL_EXTENSION = 249;
  
  private static final int LABEL_PLAIN_TEXT_EXTENSION = 1;
  
  private static final int LSD_MASK_GCT_FLAG = 128;
  
  private static final int LSD_MASK_GCT_SIZE = 7;
  
  private static final int MASK_INT_LOWEST_BYTE = 255;
  
  private static final int MAX_BLOCK_SIZE = 256;
  
  static final int MIN_FRAME_DELAY = 2;
  
  private static final String TAG = "GifHeaderParser";
  
  private static final int TRAILER = 59;
  
  private final byte[] block = new byte[256];
  
  private int blockSize = 0;
  
  private GifHeader header;
  
  private ByteBuffer rawData;
  
  private boolean err() {
    boolean bool;
    if (this.header.status != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private int read() {
    int i = 0;
    try {
      byte b = this.rawData.get();
      i = b & 0xFF;
    } catch (Exception exception) {
      this.header.status = 1;
    } 
    return i;
  }
  
  private void readBitmap() {
    boolean bool1;
    this.header.currentFrame.ix = readShort();
    this.header.currentFrame.iy = readShort();
    this.header.currentFrame.iw = readShort();
    this.header.currentFrame.ih = readShort();
    int i = read();
    boolean bool2 = false;
    if ((i & 0x80) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int j = (int)Math.pow(2.0D, ((i & 0x7) + 1));
    GifFrame gifFrame = this.header.currentFrame;
    if ((i & 0x40) != 0)
      bool2 = true; 
    gifFrame.interlace = bool2;
    if (bool1) {
      this.header.currentFrame.lct = readColorTable(j);
    } else {
      this.header.currentFrame.lct = null;
    } 
    this.header.currentFrame.bufferFrameStart = this.rawData.position();
    skipImageData();
    if (err())
      return; 
    GifHeader gifHeader = this.header;
    gifHeader.frameCount++;
    this.header.frames.add(this.header.currentFrame);
  }
  
  private void readBlock() {
    int i = read();
    this.blockSize = i;
    int j = 0;
    if (i > 0) {
      i = 0;
      while (true) {
        int k = i;
        try {
          i = this.blockSize;
          if (j < i) {
            i -= j;
            k = i;
            this.rawData.get(this.block, j, i);
            j += i;
            continue;
          } 
          break;
        } catch (Exception exception) {
          if (Log.isLoggable("GifHeaderParser", 3))
            Log.d("GifHeaderParser", "Error Reading Block n: " + j + " count: " + k + " blockSize: " + this.blockSize, exception); 
          this.header.status = 1;
        } 
        return;
      } 
    } 
  }
  
  private int[] readColorTable(int paramInt) {
    int[] arrayOfInt2 = null;
    byte[] arrayOfByte = new byte[paramInt * 3];
    int[] arrayOfInt1 = arrayOfInt2;
    try {
      this.rawData.get(arrayOfByte);
      arrayOfInt1 = arrayOfInt2;
      arrayOfInt2 = new int[256];
      byte b = 0;
      int i = 0;
      while (b < paramInt) {
        int j = i + 1;
        i = arrayOfByte[i];
        int k = j + 1;
        arrayOfInt2[b] = 0xFF000000 | (i & 0xFF) << 16 | (arrayOfByte[j] & 0xFF) << 8 | arrayOfByte[k] & 0xFF;
        i = k + 1;
        b++;
      } 
      arrayOfInt1 = arrayOfInt2;
    } catch (BufferUnderflowException bufferUnderflowException) {
      if (Log.isLoggable("GifHeaderParser", 3))
        Log.d("GifHeaderParser", "Format Error Reading Color Table", bufferUnderflowException); 
      this.header.status = 1;
    } 
    return arrayOfInt1;
  }
  
  private void readContents() {
    readContents(2147483647);
  }
  
  private void readContents(int paramInt) {
    boolean bool = false;
    while (!bool && !err() && this.header.frameCount <= paramInt) {
      byte b;
      StringBuilder stringBuilder;
      switch (read()) {
        default:
          this.header.status = 1;
          continue;
        case 59:
          bool = true;
          continue;
        case 44:
          if (this.header.currentFrame == null)
            this.header.currentFrame = new GifFrame(); 
          readBitmap();
          continue;
        case 33:
          break;
      } 
      switch (read()) {
        default:
          skip();
          continue;
        case 255:
          readBlock();
          stringBuilder = new StringBuilder();
          for (b = 0; b < 11; b++)
            stringBuilder.append((char)this.block[b]); 
          if (stringBuilder.toString().equals("NETSCAPE2.0")) {
            readNetscapeExt();
            continue;
          } 
          skip();
          continue;
        case 254:
          skip();
          continue;
        case 249:
          this.header.currentFrame = new GifFrame();
          readGraphicControlExt();
          continue;
        case 1:
          break;
      } 
      skip();
    } 
  }
  
  private void readGraphicControlExt() {
    read();
    int i = read();
    this.header.currentFrame.dispose = (i & 0x1C) >> 2;
    int j = this.header.currentFrame.dispose;
    boolean bool = true;
    if (j == 0)
      this.header.currentFrame.dispose = 1; 
    GifFrame gifFrame = this.header.currentFrame;
    if ((i & 0x1) == 0)
      bool = false; 
    gifFrame.transparency = bool;
    j = readShort();
    i = j;
    if (j < 2)
      i = 10; 
    this.header.currentFrame.delay = i * 10;
    this.header.currentFrame.transIndex = read();
    read();
  }
  
  private void readHeader() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < 6; b++)
      stringBuilder.append((char)read()); 
    if (!stringBuilder.toString().startsWith("GIF")) {
      this.header.status = 1;
      return;
    } 
    readLSD();
    if (this.header.gctFlag && !err()) {
      GifHeader gifHeader = this.header;
      gifHeader.gct = readColorTable(gifHeader.gctSize);
      gifHeader = this.header;
      gifHeader.bgColor = gifHeader.gct[this.header.bgIndex];
    } 
  }
  
  private void readLSD() {
    boolean bool;
    this.header.width = readShort();
    this.header.height = readShort();
    int i = read();
    GifHeader gifHeader = this.header;
    if ((i & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    gifHeader.gctFlag = bool;
    this.header.gctSize = (int)Math.pow(2.0D, ((i & 0x7) + 1));
    this.header.bgIndex = read();
    this.header.pixelAspect = read();
  }
  
  private void readNetscapeExt() {
    do {
      readBlock();
      byte[] arrayOfByte = this.block;
      if (arrayOfByte[0] != 1)
        continue; 
      byte b2 = arrayOfByte[1];
      byte b1 = arrayOfByte[2];
      this.header.loopCount = (b1 & 0xFF) << 8 | b2 & 0xFF;
    } while (this.blockSize > 0 && !err());
  }
  
  private int readShort() {
    return this.rawData.getShort();
  }
  
  private void reset() {
    this.rawData = null;
    Arrays.fill(this.block, (byte)0);
    this.header = new GifHeader();
    this.blockSize = 0;
  }
  
  private void skip() {
    int i;
    do {
      i = read();
      int j = Math.min(this.rawData.position() + i, this.rawData.limit());
      this.rawData.position(j);
    } while (i > 0);
  }
  
  private void skipImageData() {
    read();
    skip();
  }
  
  public void clear() {
    this.rawData = null;
    this.header = null;
  }
  
  public boolean isAnimated() {
    readHeader();
    if (!err())
      readContents(2); 
    int i = this.header.frameCount;
    boolean bool = true;
    if (i <= 1)
      bool = false; 
    return bool;
  }
  
  public GifHeader parseHeader() {
    if (this.rawData != null) {
      if (err())
        return this.header; 
      readHeader();
      if (!err()) {
        readContents();
        if (this.header.frameCount < 0)
          this.header.status = 1; 
      } 
      return this.header;
    } 
    throw new IllegalStateException("You must call setData() before parseHeader()");
  }
  
  public GifHeaderParser setData(ByteBuffer paramByteBuffer) {
    reset();
    paramByteBuffer = paramByteBuffer.asReadOnlyBuffer();
    this.rawData = paramByteBuffer;
    paramByteBuffer.position(0);
    this.rawData.order(ByteOrder.LITTLE_ENDIAN);
    return this;
  }
  
  public GifHeaderParser setData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      setData(ByteBuffer.wrap(paramArrayOfbyte));
    } else {
      this.rawData = null;
      this.header.status = 2;
    } 
    return this;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\gifdecoder\GifHeaderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */