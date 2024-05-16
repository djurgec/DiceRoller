package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.io.IOException;

public final class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap> {
  private final BitmapPool bitmapPool;
  
  public GifFrameResourceDecoder(BitmapPool paramBitmapPool) {
    this.bitmapPool = paramBitmapPool;
  }
  
  public Resource<Bitmap> decode(GifDecoder paramGifDecoder, int paramInt1, int paramInt2, Options paramOptions) {
    return (Resource<Bitmap>)BitmapResource.obtain(paramGifDecoder.getNextFrame(), this.bitmapPool);
  }
  
  public boolean handles(GifDecoder paramGifDecoder, Options paramOptions) {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\gif\GifFrameResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */