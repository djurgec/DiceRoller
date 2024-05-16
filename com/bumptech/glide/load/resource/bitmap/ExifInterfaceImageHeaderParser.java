package com.bumptech.glide.load.resource.bitmap;

import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ExifInterfaceImageHeaderParser implements ImageHeaderParser {
  public int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    ExifInterface exifInterface = new ExifInterface(paramInputStream);
    int i = exifInterface.getAttributeInt("Orientation", 1);
    return (i == 0) ? -1 : i;
  }
  
  public int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(ByteBufferUtil.toStream(paramByteBuffer), paramArrayPool);
  }
  
  public ImageHeaderParser.ImageType getType(InputStream paramInputStream) {
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
  
  public ImageHeaderParser.ImageType getType(ByteBuffer paramByteBuffer) {
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\ExifInterfaceImageHeaderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */