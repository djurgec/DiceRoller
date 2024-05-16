package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public interface GifDecoder {
  public static final int STATUS_FORMAT_ERROR = 1;
  
  public static final int STATUS_OK = 0;
  
  public static final int STATUS_OPEN_ERROR = 2;
  
  public static final int STATUS_PARTIAL_DECODE = 3;
  
  public static final int TOTAL_ITERATION_COUNT_FOREVER = 0;
  
  void advance();
  
  void clear();
  
  int getByteSize();
  
  int getCurrentFrameIndex();
  
  ByteBuffer getData();
  
  int getDelay(int paramInt);
  
  int getFrameCount();
  
  int getHeight();
  
  @Deprecated
  int getLoopCount();
  
  int getNetscapeLoopCount();
  
  int getNextDelay();
  
  Bitmap getNextFrame();
  
  int getStatus();
  
  int getTotalIterationCount();
  
  int getWidth();
  
  int read(InputStream paramInputStream, int paramInt);
  
  int read(byte[] paramArrayOfbyte);
  
  void resetFrameIndex();
  
  void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer);
  
  void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer, int paramInt);
  
  void setData(GifHeader paramGifHeader, byte[] paramArrayOfbyte);
  
  void setDefaultBitmapConfig(Bitmap.Config paramConfig);
  
  public static interface BitmapProvider {
    Bitmap obtain(int param1Int1, int param1Int2, Bitmap.Config param1Config);
    
    byte[] obtainByteArray(int param1Int);
    
    int[] obtainIntArray(int param1Int);
    
    void release(Bitmap param1Bitmap);
    
    void release(byte[] param1ArrayOfbyte);
    
    void release(int[] param1ArrayOfint);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GifDecodeStatus {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\gifdecoder\GifDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */