package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
import java.io.IOException;

public class ResourceBitmapDecoder implements ResourceDecoder<Uri, Bitmap> {
  private final BitmapPool bitmapPool;
  
  private final ResourceDrawableDecoder drawableDecoder;
  
  public ResourceBitmapDecoder(ResourceDrawableDecoder paramResourceDrawableDecoder, BitmapPool paramBitmapPool) {
    this.drawableDecoder = paramResourceDrawableDecoder;
    this.bitmapPool = paramBitmapPool;
  }
  
  public Resource<Bitmap> decode(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    Resource resource = this.drawableDecoder.decode(paramUri, paramInt1, paramInt2, paramOptions);
    if (resource == null)
      return null; 
    Drawable drawable = (Drawable)resource.get();
    return DrawableToBitmapConverter.convert(this.bitmapPool, drawable, paramInt1, paramInt2);
  }
  
  public boolean handles(Uri paramUri, Options paramOptions) {
    return "android.resource".equals(paramUri.getScheme());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\ResourceBitmapDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */