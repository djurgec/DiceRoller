package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class DrawableTransformation implements Transformation<Drawable> {
  private final boolean isRequired;
  
  private final Transformation<Bitmap> wrapped;
  
  public DrawableTransformation(Transformation<Bitmap> paramTransformation, boolean paramBoolean) {
    this.wrapped = paramTransformation;
    this.isRequired = paramBoolean;
  }
  
  private Resource<Drawable> newDrawableResource(Context paramContext, Resource<Bitmap> paramResource) {
    return (Resource)LazyBitmapDrawableResource.obtain(paramContext.getResources(), paramResource);
  }
  
  public Transformation<BitmapDrawable> asBitmapDrawable() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof DrawableTransformation) {
      paramObject = paramObject;
      return this.wrapped.equals(((DrawableTransformation)paramObject).wrapped);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.wrapped.hashCode();
  }
  
  public Resource<Drawable> transform(Context paramContext, Resource<Drawable> paramResource, int paramInt1, int paramInt2) {
    BitmapPool bitmapPool = Glide.get(paramContext).getBitmapPool();
    Drawable drawable = (Drawable)paramResource.get();
    Resource<Bitmap> resource2 = DrawableToBitmapConverter.convert(bitmapPool, drawable, paramInt1, paramInt2);
    if (resource2 == null) {
      if (!this.isRequired)
        return paramResource; 
      throw new IllegalArgumentException("Unable to convert " + drawable + " to a Bitmap");
    } 
    Resource<Bitmap> resource1 = this.wrapped.transform(paramContext, resource2, paramInt1, paramInt2);
    if (resource1.equals(resource2)) {
      resource1.recycle();
      return paramResource;
    } 
    return newDrawableResource(paramContext, resource1);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.wrapped.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\DrawableTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */