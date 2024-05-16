package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;

public abstract class CustomTarget<T> implements Target<T> {
  private final int height;
  
  private Request request;
  
  private final int width;
  
  public CustomTarget() {
    this(-2147483648, -2147483648);
  }
  
  public CustomTarget(int paramInt1, int paramInt2) {
    if (Util.isValidDimensions(paramInt1, paramInt2)) {
      this.width = paramInt1;
      this.height = paramInt2;
      return;
    } 
    throw new IllegalArgumentException("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: " + paramInt1 + " and height: " + paramInt2);
  }
  
  public final Request getRequest() {
    return this.request;
  }
  
  public final void getSize(SizeReadyCallback paramSizeReadyCallback) {
    paramSizeReadyCallback.onSizeReady(this.width, this.height);
  }
  
  public void onDestroy() {}
  
  public void onLoadFailed(Drawable paramDrawable) {}
  
  public void onLoadStarted(Drawable paramDrawable) {}
  
  public void onStart() {}
  
  public void onStop() {}
  
  public final void removeCallback(SizeReadyCallback paramSizeReadyCallback) {}
  
  public final void setRequest(Request paramRequest) {
    this.request = paramRequest;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\CustomTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */