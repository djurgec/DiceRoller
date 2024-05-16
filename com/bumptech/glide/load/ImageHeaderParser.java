package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageHeaderParser {
  public static final int UNKNOWN_ORIENTATION = -1;
  
  int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException;
  
  int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException;
  
  ImageType getType(InputStream paramInputStream) throws IOException;
  
  ImageType getType(ByteBuffer paramByteBuffer) throws IOException;
  
  public enum ImageType {
    GIF, JPEG, PNG, PNG_A, RAW, UNKNOWN, WEBP, WEBP_A;
    
    private static final ImageType[] $VALUES;
    
    private final boolean hasAlpha;
    
    static {
      ImageType imageType1 = new ImageType("GIF", 0, true);
      GIF = imageType1;
      ImageType imageType5 = new ImageType("JPEG", 1, false);
      JPEG = imageType5;
      ImageType imageType8 = new ImageType("RAW", 2, false);
      RAW = imageType8;
      ImageType imageType6 = new ImageType("PNG_A", 3, true);
      PNG_A = imageType6;
      ImageType imageType2 = new ImageType("PNG", 4, false);
      PNG = imageType2;
      ImageType imageType3 = new ImageType("WEBP_A", 5, true);
      WEBP_A = imageType3;
      ImageType imageType7 = new ImageType("WEBP", 6, false);
      WEBP = imageType7;
      ImageType imageType4 = new ImageType("UNKNOWN", 7, false);
      UNKNOWN = imageType4;
      $VALUES = new ImageType[] { imageType1, imageType5, imageType8, imageType6, imageType2, imageType3, imageType7, imageType4 };
    }
    
    ImageType(boolean param1Boolean) {
      this.hasAlpha = param1Boolean;
    }
    
    public boolean hasAlpha() {
      return this.hasAlpha;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\ImageHeaderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */