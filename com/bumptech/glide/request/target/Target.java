package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.transition.Transition;

public interface Target<R> extends LifecycleListener {
  public static final int SIZE_ORIGINAL = -2147483648;
  
  Request getRequest();
  
  void getSize(SizeReadyCallback paramSizeReadyCallback);
  
  void onLoadCleared(Drawable paramDrawable);
  
  void onLoadFailed(Drawable paramDrawable);
  
  void onLoadStarted(Drawable paramDrawable);
  
  void onResourceReady(R paramR, Transition<? super R> paramTransition);
  
  void removeCallback(SizeReadyCallback paramSizeReadyCallback);
  
  void setRequest(Request paramRequest);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */