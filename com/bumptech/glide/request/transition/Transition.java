package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface Transition<R> {
  boolean transition(R paramR, ViewAdapter paramViewAdapter);
  
  public static interface ViewAdapter {
    Drawable getCurrentDrawable();
    
    View getView();
    
    void setDrawable(Drawable param1Drawable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\Transition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */