package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
  public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT;
  
  public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", Integer.valueOf(90));
  
  private static final String TAG = "BitmapEncoder";
  
  private final ArrayPool arrayPool = null;
  
  static {
    COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
  }
  
  @Deprecated
  public BitmapEncoder() {}
  
  public BitmapEncoder(ArrayPool paramArrayPool) {}
  
  private Bitmap.CompressFormat getFormat(Bitmap paramBitmap, Options paramOptions) {
    Bitmap.CompressFormat compressFormat = (Bitmap.CompressFormat)paramOptions.get(COMPRESSION_FORMAT);
    return (compressFormat != null) ? compressFormat : (paramBitmap.hasAlpha() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG);
  }
  
  public boolean encode(Resource<Bitmap> paramResource, File paramFile, Options paramOptions) {
    Bitmap bitmap = (Bitmap)paramResource.get();
    Bitmap.CompressFormat compressFormat = getFormat(bitmap, paramOptions);
    GlideTrace.beginSectionFormat("encode: [%dx%d] %s", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), compressFormat);
    try {
      BufferedOutputStream bufferedOutputStream;
      long l = LogTime.getLogTime();
      int i = ((Integer)paramOptions.get(COMPRESSION_QUALITY)).intValue();
      boolean bool3 = false;
      boolean bool1 = false;
      boolean bool2 = false;
      FileOutputStream fileOutputStream2 = null;
      Resource<Bitmap> resource = null;
      paramResource = resource;
      FileOutputStream fileOutputStream1 = fileOutputStream2;
      try {
        BufferedOutputStream bufferedOutputStream2;
        FileOutputStream fileOutputStream5 = new FileOutputStream();
        paramResource = resource;
        fileOutputStream1 = fileOutputStream2;
        this(paramFile);
        FileOutputStream fileOutputStream4 = fileOutputStream5;
        fileOutputStream2 = fileOutputStream4;
        FileOutputStream fileOutputStream3 = fileOutputStream4;
        fileOutputStream1 = fileOutputStream4;
        if (this.arrayPool != null) {
          fileOutputStream3 = fileOutputStream4;
          fileOutputStream1 = fileOutputStream4;
          bufferedOutputStream2 = new BufferedOutputStream();
          fileOutputStream3 = fileOutputStream4;
          fileOutputStream1 = fileOutputStream4;
          this(fileOutputStream4, this.arrayPool);
        } 
        BufferedOutputStream bufferedOutputStream1 = bufferedOutputStream2;
        bufferedOutputStream = bufferedOutputStream2;
        bitmap.compress(compressFormat, i, (OutputStream)bufferedOutputStream2);
        bufferedOutputStream1 = bufferedOutputStream2;
        bufferedOutputStream = bufferedOutputStream2;
        bufferedOutputStream2.close();
        bool1 = true;
        bool2 = true;
        try {
          bufferedOutputStream2.close();
          bool1 = bool2;
        } catch (IOException iOException) {}
      } catch (IOException iOException) {
        BufferedOutputStream bufferedOutputStream1 = bufferedOutputStream;
        if (Log.isLoggable("BitmapEncoder", 3)) {
          bufferedOutputStream1 = bufferedOutputStream;
          Log.d("BitmapEncoder", "Failed to encode Bitmap", iOException);
        } 
        if (bufferedOutputStream != null) {
          bool1 = bool3;
          bufferedOutputStream.close();
          bool1 = bool2;
        } 
      } finally {}
      if (Log.isLoggable("BitmapEncoder", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.v("BitmapEncoder", stringBuilder.append("Compressed with type: ").append(compressFormat).append(" of size ").append(Util.getBitmapByteSize(bitmap)).append(" in ").append(LogTime.getElapsedMillis(l)).append(", options format: ").append(paramOptions.get(COMPRESSION_FORMAT)).append(", hasAlpha: ").append(bitmap.hasAlpha()).toString());
      } 
      return bool1;
    } finally {
      GlideTrace.endSection();
    } 
  }
  
  public EncodeStrategy getEncodeStrategy(Options paramOptions) {
    return EncodeStrategy.TRANSFORMED;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\BitmapEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */