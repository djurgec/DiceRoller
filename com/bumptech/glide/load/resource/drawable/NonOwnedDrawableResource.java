package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Resource;

final class NonOwnedDrawableResource extends DrawableResource<Drawable> {
  private NonOwnedDrawableResource(Drawable paramDrawable) {
    super(paramDrawable);
  }
  
  static Resource<Drawable> newInstance(Drawable paramDrawable) {
    if (paramDrawable != null) {
      NonOwnedDrawableResource nonOwnedDrawableResource = new NonOwnedDrawableResource(paramDrawable);
    } else {
      paramDrawable = null;
    } 
    return (Resource<Drawable>)paramDrawable;
  }
  
  public Class<Drawable> getResourceClass() {
    return (Class)this.drawable.getClass();
  }
  
  public int getSize() {
    return Math.max(1, this.drawable.getIntrinsicWidth() * this.drawable.getIntrinsicHeight() * 4);
  }
  
  public void recycle() {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\drawable\NonOwnedDrawableResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */