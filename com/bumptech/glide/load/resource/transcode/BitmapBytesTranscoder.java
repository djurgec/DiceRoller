package com.bumptech.glide.load.resource.transcode;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;
import java.io.ByteArrayOutputStream;

public class BitmapBytesTranscoder implements ResourceTranscoder<Bitmap, byte[]> {
  private final Bitmap.CompressFormat compressFormat;
  
  private final int quality;
  
  public BitmapBytesTranscoder() {
    this(Bitmap.CompressFormat.JPEG, 100);
  }
  
  public BitmapBytesTranscoder(Bitmap.CompressFormat paramCompressFormat, int paramInt) {
    this.compressFormat = paramCompressFormat;
    this.quality = paramInt;
  }
  
  public Resource<byte[]> transcode(Resource<Bitmap> paramResource, Options paramOptions) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ((Bitmap)paramResource.get()).compress(this.compressFormat, this.quality, byteArrayOutputStream);
    paramResource.recycle();
    return (Resource<byte[]>)new BytesResource(byteArrayOutputStream.toByteArray());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\transcode\BitmapBytesTranscoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */