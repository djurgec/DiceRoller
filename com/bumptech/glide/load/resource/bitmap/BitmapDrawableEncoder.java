package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.File;

public class BitmapDrawableEncoder implements ResourceEncoder<BitmapDrawable> {
  private final BitmapPool bitmapPool;
  
  private final ResourceEncoder<Bitmap> encoder;
  
  public BitmapDrawableEncoder(BitmapPool paramBitmapPool, ResourceEncoder<Bitmap> paramResourceEncoder) {
    this.bitmapPool = paramBitmapPool;
    this.encoder = paramResourceEncoder;
  }
  
  public boolean encode(Resource<BitmapDrawable> paramResource, File paramFile, Options paramOptions) {
    return this.encoder.encode(new BitmapResource(((BitmapDrawable)paramResource.get()).getBitmap(), this.bitmapPool), paramFile, paramOptions);
  }
  
  public EncodeStrategy getEncodeStrategy(Options paramOptions) {
    return this.encoder.getEncodeStrategy(paramOptions);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\BitmapDrawableEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */