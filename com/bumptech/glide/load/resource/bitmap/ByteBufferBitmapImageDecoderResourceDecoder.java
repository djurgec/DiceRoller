package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class ByteBufferBitmapImageDecoderResourceDecoder implements ResourceDecoder<ByteBuffer, Bitmap> {
  private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();
  
  public Resource<Bitmap> decode(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    ImageDecoder.Source source = ImageDecoder.createSource(paramByteBuffer);
    return this.wrapped.decode(source, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(ByteBuffer paramByteBuffer, Options paramOptions) throws IOException {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\ByteBufferBitmapImageDecoderResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */