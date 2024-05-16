package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageViewTargetFactory {
  public <Z> ViewTarget<ImageView, Z> buildTarget(ImageView paramImageView, Class<Z> paramClass) {
    if (Bitmap.class.equals(paramClass))
      return new BitmapImageViewTarget(paramImageView); 
    if (Drawable.class.isAssignableFrom(paramClass))
      return new DrawableImageViewTarget(paramImageView); 
    throw new IllegalArgumentException("Unhandled class: " + paramClass + ", try .as*(Class).transcode(ResourceTranscoder)");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\ImageViewTargetFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */