package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

interface ImageReader {
  Bitmap decodeBitmap(BitmapFactory.Options paramOptions) throws IOException;
  
  int getImageOrientation() throws IOException;
  
  ImageHeaderParser.ImageType getImageType() throws IOException;
  
  void stopGrowingBuffers();
  
  public static final class InputStreamImageReader implements ImageReader {
    private final ArrayPool byteArrayPool;
    
    private final InputStreamRewinder dataRewinder;
    
    private final List<ImageHeaderParser> parsers;
    
    InputStreamImageReader(InputStream param1InputStream, List<ImageHeaderParser> param1List, ArrayPool param1ArrayPool) {
      this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(param1ArrayPool);
      this.parsers = (List<ImageHeaderParser>)Preconditions.checkNotNull(param1List);
      this.dataRewinder = new InputStreamRewinder(param1InputStream, param1ArrayPool);
    }
    
    public Bitmap decodeBitmap(BitmapFactory.Options param1Options) throws IOException {
      return BitmapFactory.decodeStream(this.dataRewinder.rewindAndGet(), null, param1Options);
    }
    
    public int getImageOrientation() throws IOException {
      return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
    }
    
    public ImageHeaderParser.ImageType getImageType() throws IOException {
      return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
    }
    
    public void stopGrowingBuffers() {
      this.dataRewinder.fixMarkLimits();
    }
  }
  
  public static final class ParcelFileDescriptorImageReader implements ImageReader {
    private final ArrayPool byteArrayPool;
    
    private final ParcelFileDescriptorRewinder dataRewinder;
    
    private final List<ImageHeaderParser> parsers;
    
    ParcelFileDescriptorImageReader(ParcelFileDescriptor param1ParcelFileDescriptor, List<ImageHeaderParser> param1List, ArrayPool param1ArrayPool) {
      this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(param1ArrayPool);
      this.parsers = (List<ImageHeaderParser>)Preconditions.checkNotNull(param1List);
      this.dataRewinder = new ParcelFileDescriptorRewinder(param1ParcelFileDescriptor);
    }
    
    public Bitmap decodeBitmap(BitmapFactory.Options param1Options) throws IOException {
      return BitmapFactory.decodeFileDescriptor(this.dataRewinder.rewindAndGet().getFileDescriptor(), null, param1Options);
    }
    
    public int getImageOrientation() throws IOException {
      return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder, this.byteArrayPool);
    }
    
    public ImageHeaderParser.ImageType getImageType() throws IOException {
      return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder, this.byteArrayPool);
    }
    
    public void stopGrowingBuffers() {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\ImageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */