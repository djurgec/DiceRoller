package com.bumptech.glide.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import java.util.Arrays;

public class ViewPreloadSizeProvider<T> implements ListPreloader.PreloadSizeProvider<T>, SizeReadyCallback {
  private int[] size;
  
  private SizeViewTarget viewTarget;
  
  public ViewPreloadSizeProvider() {}
  
  public ViewPreloadSizeProvider(View paramView) {
    SizeViewTarget sizeViewTarget = new SizeViewTarget(paramView);
    this.viewTarget = sizeViewTarget;
    sizeViewTarget.getSize(this);
  }
  
  public int[] getPreloadSize(T paramT, int paramInt1, int paramInt2) {
    int[] arrayOfInt = this.size;
    return (arrayOfInt == null) ? null : Arrays.copyOf(arrayOfInt, arrayOfInt.length);
  }
  
  public void onSizeReady(int paramInt1, int paramInt2) {
    this.size = new int[] { paramInt1, paramInt2 };
    this.viewTarget = null;
  }
  
  public void setView(View paramView) {
    if (this.size != null || this.viewTarget != null)
      return; 
    SizeViewTarget sizeViewTarget = new SizeViewTarget(paramView);
    this.viewTarget = sizeViewTarget;
    sizeViewTarget.getSize(this);
  }
  
  static final class SizeViewTarget extends CustomViewTarget<View, Object> {
    SizeViewTarget(View param1View) {
      super(param1View);
    }
    
    public void onLoadFailed(Drawable param1Drawable) {}
    
    protected void onResourceCleared(Drawable param1Drawable) {}
    
    public void onResourceReady(Object param1Object, Transition<? super Object> param1Transition) {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\ViewPreloadSizeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */