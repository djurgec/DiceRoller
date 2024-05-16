package com.bumptech.glide.request.target;

import com.bumptech.glide.util.Util;

@Deprecated
public abstract class SimpleTarget<Z> extends BaseTarget<Z> {
  private final int height;
  
  private final int width;
  
  public SimpleTarget() {
    this(-2147483648, -2147483648);
  }
  
  public SimpleTarget(int paramInt1, int paramInt2) {
    this.width = paramInt1;
    this.height = paramInt2;
  }
  
  public final void getSize(SizeReadyCallback paramSizeReadyCallback) {
    if (Util.isValidDimensions(this.width, this.height)) {
      paramSizeReadyCallback.onSizeReady(this.width, this.height);
      return;
    } 
    throw new IllegalArgumentException("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: " + this.width + " and height: " + this.height + ", either provide dimensions in the constructor or call override()");
  }
  
  public void removeCallback(SizeReadyCallback paramSizeReadyCallback) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\SimpleTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */