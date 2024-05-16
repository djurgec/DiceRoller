package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder<InputStream> {
  private static final int MARK_READ_LIMIT = 5242880;
  
  private final RecyclableBufferedInputStream bufferedStream;
  
  public InputStreamRewinder(InputStream paramInputStream, ArrayPool paramArrayPool) {
    RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(paramInputStream, paramArrayPool);
    this.bufferedStream = recyclableBufferedInputStream;
    recyclableBufferedInputStream.mark(5242880);
  }
  
  public void cleanup() {
    this.bufferedStream.release();
  }
  
  public void fixMarkLimits() {
    this.bufferedStream.fixMarkLimit();
  }
  
  public InputStream rewindAndGet() throws IOException {
    this.bufferedStream.reset();
    return (InputStream)this.bufferedStream;
  }
  
  public static final class Factory implements DataRewinder.Factory<InputStream> {
    private final ArrayPool byteArrayPool;
    
    public Factory(ArrayPool param1ArrayPool) {
      this.byteArrayPool = param1ArrayPool;
    }
    
    public DataRewinder<InputStream> build(InputStream param1InputStream) {
      return new InputStreamRewinder(param1InputStream, this.byteArrayPool);
    }
    
    public Class<InputStream> getDataClass() {
      return InputStream.class;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\InputStreamRewinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */