package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class ThumbnailImageViewTarget<T> extends ImageViewTarget<T> {
  public ThumbnailImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public ThumbnailImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  protected abstract Drawable getDrawable(T paramT);
  
  protected void setResource(T paramT) {
    ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
    Drawable drawable2 = getDrawable(paramT);
    Drawable drawable1 = drawable2;
    if (layoutParams != null) {
      drawable1 = drawable2;
      if (layoutParams.width > 0) {
        drawable1 = drawable2;
        if (layoutParams.height > 0)
          drawable1 = new FixedSizeDrawable(drawable2, layoutParams.width, layoutParams.height); 
      } 
    } 
    this.view.setImageDrawable(drawable1);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\ThumbnailImageViewTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */