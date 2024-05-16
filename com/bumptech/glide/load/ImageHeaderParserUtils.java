package com.bumptech.glide.load;

import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
  private static final int MARK_READ_LIMIT = 5242880;
  
  public static int getOrientation(List<ImageHeaderParser> paramList, final ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, final ArrayPool byteArrayPool) throws IOException {
    return getOrientationInternal(paramList, new OrientationReader() {
          final ArrayPool val$byteArrayPool;
          
          final ParcelFileDescriptorRewinder val$parcelFileDescriptorRewinder;
          
          public int getOrientation(ImageHeaderParser param1ImageHeaderParser) throws IOException {
            RecyclableBufferedInputStream recyclableBufferedInputStream2 = null;
            RecyclableBufferedInputStream recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
            try {
              RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream();
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              FileInputStream fileInputStream = new FileInputStream();
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              this(parcelFileDescriptorRewinder.rewindAndGet().getFileDescriptor());
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              this(fileInputStream, byteArrayPool);
              recyclableBufferedInputStream1 = recyclableBufferedInputStream;
              return param1ImageHeaderParser.getOrientation((InputStream)recyclableBufferedInputStream, byteArrayPool);
            } finally {
              if (recyclableBufferedInputStream1 != null)
                try {
                  recyclableBufferedInputStream1.close();
                } catch (IOException iOException) {} 
              parcelFileDescriptorRewinder.rewindAndGet();
            } 
          }
        });
  }
  
  public static int getOrientation(List<ImageHeaderParser> paramList, InputStream paramInputStream, final ArrayPool byteArrayPool) throws IOException {
    final RecyclableBufferedInputStream finalIs;
    if (paramInputStream == null)
      return -1; 
    InputStream inputStream = paramInputStream;
    if (!paramInputStream.markSupported())
      recyclableBufferedInputStream = new RecyclableBufferedInputStream(paramInputStream, byteArrayPool); 
    recyclableBufferedInputStream.mark(5242880);
    return getOrientationInternal(paramList, new OrientationReader() {
          final ArrayPool val$byteArrayPool;
          
          final InputStream val$finalIs;
          
          public int getOrientation(ImageHeaderParser param1ImageHeaderParser) throws IOException {
            try {
              return param1ImageHeaderParser.getOrientation(finalIs, byteArrayPool);
            } finally {
              finalIs.reset();
            } 
          }
        });
  }
  
  private static int getOrientationInternal(List<ImageHeaderParser> paramList, OrientationReader paramOrientationReader) throws IOException {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      int j = paramOrientationReader.getOrientation(paramList.get(b));
      if (j != -1)
        return j; 
      b++;
    } 
    return -1;
  }
  
  public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> paramList, final ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, final ArrayPool byteArrayPool) throws IOException {
    return getTypeInternal(paramList, new TypeReader() {
          final ArrayPool val$byteArrayPool;
          
          final ParcelFileDescriptorRewinder val$parcelFileDescriptorRewinder;
          
          public ImageHeaderParser.ImageType getType(ImageHeaderParser param1ImageHeaderParser) throws IOException {
            RecyclableBufferedInputStream recyclableBufferedInputStream2 = null;
            RecyclableBufferedInputStream recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
            try {
              RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream();
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              FileInputStream fileInputStream = new FileInputStream();
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              this(parcelFileDescriptorRewinder.rewindAndGet().getFileDescriptor());
              recyclableBufferedInputStream1 = recyclableBufferedInputStream2;
              this(fileInputStream, byteArrayPool);
              recyclableBufferedInputStream1 = recyclableBufferedInputStream;
              return param1ImageHeaderParser.getType((InputStream)recyclableBufferedInputStream);
            } finally {
              if (iOException != null)
                try {
                  iOException.close();
                } catch (IOException iOException1) {} 
              parcelFileDescriptorRewinder.rewindAndGet();
            } 
          }
        });
  }
  
  public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> paramList, InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    final RecyclableBufferedInputStream finalIs;
    if (paramInputStream == null)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    InputStream inputStream = paramInputStream;
    if (!paramInputStream.markSupported())
      recyclableBufferedInputStream = new RecyclableBufferedInputStream(paramInputStream, paramArrayPool); 
    recyclableBufferedInputStream.mark(5242880);
    return getTypeInternal(paramList, new TypeReader() {
          final InputStream val$finalIs;
          
          public ImageHeaderParser.ImageType getType(ImageHeaderParser param1ImageHeaderParser) throws IOException {
            try {
              return param1ImageHeaderParser.getType(finalIs);
            } finally {
              finalIs.reset();
            } 
          }
        });
  }
  
  public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> paramList, final ByteBuffer buffer) throws IOException {
    return (buffer == null) ? ImageHeaderParser.ImageType.UNKNOWN : getTypeInternal(paramList, new TypeReader() {
          final ByteBuffer val$buffer;
          
          public ImageHeaderParser.ImageType getType(ImageHeaderParser param1ImageHeaderParser) throws IOException {
            return param1ImageHeaderParser.getType(buffer);
          }
        });
  }
  
  private static ImageHeaderParser.ImageType getTypeInternal(List<ImageHeaderParser> paramList, TypeReader paramTypeReader) throws IOException {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      ImageHeaderParser.ImageType imageType = paramTypeReader.getType(paramList.get(b));
      if (imageType != ImageHeaderParser.ImageType.UNKNOWN)
        return imageType; 
      b++;
    } 
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
  
  private static interface OrientationReader {
    int getOrientation(ImageHeaderParser param1ImageHeaderParser) throws IOException;
  }
  
  private static interface TypeReader {
    ImageHeaderParser.ImageType getType(ImageHeaderParser param1ImageHeaderParser) throws IOException;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\ImageHeaderParserUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */