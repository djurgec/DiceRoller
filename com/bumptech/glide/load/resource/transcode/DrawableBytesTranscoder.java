package com.bumptech.glide.load.resource.transcode;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;

public final class DrawableBytesTranscoder implements ResourceTranscoder<Drawable, byte[]> {
  private final ResourceTranscoder<Bitmap, byte[]> bitmapBytesTranscoder;
  
  private final BitmapPool bitmapPool;
  
  private final ResourceTranscoder<GifDrawable, byte[]> gifDrawableBytesTranscoder;
  
  public DrawableBytesTranscoder(BitmapPool paramBitmapPool, ResourceTranscoder<Bitmap, byte[]> paramResourceTranscoder, ResourceTranscoder<GifDrawable, byte[]> paramResourceTranscoder1) {
    this.bitmapPool = paramBitmapPool;
    this.bitmapBytesTranscoder = paramResourceTranscoder;
    this.gifDrawableBytesTranscoder = paramResourceTranscoder1;
  }
  
  private static Resource<GifDrawable> toGifDrawableResource(Resource<Drawable> paramResource) {
    return (Resource)paramResource;
  }
  
  public Resource<byte[]> transcode(Resource<Drawable> paramResource, Options paramOptions) {
    Drawable drawable = (Drawable)paramResource.get();
    return (Resource)((drawable instanceof BitmapDrawable) ? this.bitmapBytesTranscoder.transcode((Resource<Bitmap>)BitmapResource.obtain(((BitmapDrawable)drawable).getBitmap(), this.bitmapPool), paramOptions) : ((drawable instanceof GifDrawable) ? this.gifDrawableBytesTranscoder.transcode(toGifDrawableResource(paramResource), paramOptions) : null));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\transcode\DrawableBytesTranscoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */