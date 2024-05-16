package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class BitmapTransitionFactory extends BitmapContainerTransitionFactory<Bitmap> {
  public BitmapTransitionFactory(TransitionFactory<Drawable> paramTransitionFactory) {
    super(paramTransitionFactory);
  }
  
  protected Bitmap getBitmap(Bitmap paramBitmap) {
    return paramBitmap;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\BitmapTransitionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */