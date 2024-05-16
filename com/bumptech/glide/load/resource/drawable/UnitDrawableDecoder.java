package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public class UnitDrawableDecoder implements ResourceDecoder<Drawable, Drawable> {
  public Resource<Drawable> decode(Drawable paramDrawable, int paramInt1, int paramInt2, Options paramOptions) {
    return NonOwnedDrawableResource.newInstance(paramDrawable);
  }
  
  public boolean handles(Drawable paramDrawable, Options paramOptions) {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\drawable\UnitDrawableDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */