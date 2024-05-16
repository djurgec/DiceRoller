package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

@Deprecated
public class BitmapDrawableTransformation implements Transformation<BitmapDrawable> {
  private final Transformation<Drawable> wrapped;
  
  public BitmapDrawableTransformation(Transformation<Bitmap> paramTransformation) {
    this.wrapped = (Transformation<Drawable>)Preconditions.checkNotNull(new DrawableTransformation(paramTransformation, false));
  }
  
  private static Resource<BitmapDrawable> convertToBitmapDrawableResource(Resource<Drawable> paramResource) {
    if (paramResource.get() instanceof BitmapDrawable)
      return (Resource)paramResource; 
    throw new IllegalArgumentException("Wrapped transformation unexpectedly returned a non BitmapDrawable resource: " + paramResource.get());
  }
  
  private static Resource<Drawable> convertToDrawableResource(Resource<BitmapDrawable> paramResource) {
    return (Resource)paramResource;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof BitmapDrawableTransformation) {
      paramObject = paramObject;
      return this.wrapped.equals(((BitmapDrawableTransformation)paramObject).wrapped);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.wrapped.hashCode();
  }
  
  public Resource<BitmapDrawable> transform(Context paramContext, Resource<BitmapDrawable> paramResource, int paramInt1, int paramInt2) {
    paramResource = (Resource)convertToDrawableResource(paramResource);
    return convertToBitmapDrawableResource(this.wrapped.transform(paramContext, paramResource, paramInt1, paramInt2));
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.wrapped.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\BitmapDrawableTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */