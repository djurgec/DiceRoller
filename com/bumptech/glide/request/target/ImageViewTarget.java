package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements Transition.ViewAdapter {
  private Animatable animatable;
  
  public ImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public ImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  private void maybeUpdateAnimatable(Z paramZ) {
    if (paramZ instanceof Animatable) {
      Animatable animatable = (Animatable)paramZ;
      this.animatable = animatable;
      animatable.start();
    } else {
      this.animatable = null;
    } 
  }
  
  private void setResourceInternal(Z paramZ) {
    setResource(paramZ);
    maybeUpdateAnimatable(paramZ);
  }
  
  public Drawable getCurrentDrawable() {
    return this.view.getDrawable();
  }
  
  public void onLoadCleared(Drawable paramDrawable) {
    super.onLoadCleared(paramDrawable);
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.stop(); 
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onLoadFailed(Drawable paramDrawable) {
    super.onLoadFailed(paramDrawable);
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onLoadStarted(Drawable paramDrawable) {
    super.onLoadStarted(paramDrawable);
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onResourceReady(Z paramZ, Transition<? super Z> paramTransition) {
    if (paramTransition == null || !paramTransition.transition(paramZ, this)) {
      setResourceInternal(paramZ);
      return;
    } 
    maybeUpdateAnimatable(paramZ);
  }
  
  public void onStart() {
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.start(); 
  }
  
  public void onStop() {
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.stop(); 
  }
  
  public void setDrawable(Drawable paramDrawable) {
    this.view.setImageDrawable(paramDrawable);
  }
  
  protected abstract void setResource(Z paramZ);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\ImageViewTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */